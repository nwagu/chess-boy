#include <jni.h>
#include <android/log.h>

#define LOG_TAG "StockfishServiceJNI"

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG,__VA_ARGS__)

#include <string>
#include <vector>
#include <iostream>
#include <thread>
#include <algorithm>
#include <streambuf>


// Stockfish
#include "bitboard.h"
#include "evaluate.h"
#include "position.h"
#include "search.h"
#include "thread.h"
#include "tt.h"
#include "uci.h"
#include "syzygy/tbprobe.h"

namespace stockfishservice {

class engine_wrapper {
    engine_wrapper() { }

    ~engine_wrapper() {
        if (engine_thread.joinable()) {
            engine_thread.join();
        }
        if (service_object) {
            JNIEnv *jenv;
            vm->AttachCurrentThread(&jenv, NULL);
            jenv->DeleteGlobalRef(service_object);
            vm->DetachCurrentThread();
        }
    }

    void thread_loop();

public:
    static engine_wrapper &sharedEngine() {
        static engine_wrapper engineWrapper;
        return engineWrapper;
    }

    void startEngine();

    // delete copy and move constructors and assign operators
    engine_wrapper(engine_wrapper const &) = delete;             // Copy construct
    engine_wrapper(engine_wrapper &&) = delete;                  // Move construct
    engine_wrapper &operator=(engine_wrapper const &) = delete;  // Copy assign
    engine_wrapper &operator=(engine_wrapper &&) = delete;      // Move assign

    // ALL PUBLIC
    bool engine_is_running = false;
    std::mutex startup_mutex;
    std::condition_variable startup_cv;
    // output handling
    std::string output_line;
    jmethodID output_method = nullptr;
    jobject service_object = nullptr;
    JavaVM *vm = nullptr;
    // input handling
    std::deque<std::string> input_lines;
    std::thread engine_thread;
    std::mutex mutex;
    std::condition_variable condition_variable;
};

void engine_wrapper::startEngine() {
    engine_thread = std::thread(&engine_wrapper::thread_loop, this);
}

void engine_wrapper::thread_loop() {
    LOGV("Initializing stockfish");
    UCI::init(Options);
    PSQT::init();
    Bitboards::init();
    Position::init();
    Bitbases::init();
    Search::init();
    Eval::init();
    Pawns::init();
    Threads.init();
    Tablebases::init(Options["SyzygyPath"]);
    TT.resize(Options["Hash"]);

    LOGV("Going into UCI::loop");

    char *argv = "stockfish_engine";
    UCI::loop(1, &argv);

    Threads.exit();
    {
        std::lock_guard<std::mutex> lk(startup_mutex);
        LOGE("engine is stopped");
        engine_is_running = false;
    }
}

class jni_sink : public std::streambuf {
protected:
    virtual int_type overflow(int_type c) {
        if (c == EOF) {
            return c;
        }
        engine_wrapper &engine = engine_wrapper::sharedEngine();
        if (isprint(c)) {
            engine.output_line += c;
            return c;
        }
        if (c != '\n') {
            return EOF;
        }
        if (engine.output_line.length() == 0) {
            return c;
        }
        engine.output_line += c;
        LOGV("sending line : %s to Java", engine.output_line.c_str());
        JNIEnv *jenv;
        jint result = engine.vm->AttachCurrentThread(&jenv, NULL);
        if (result != 0) {
            LOGE("attaching to JVM failed");
            engine.output_line = "";
            return c;
        }
        jstring args = jenv->NewStringUTF(engine.output_line.c_str());
        jenv->CallVoidMethod(engine.service_object, engine.output_method, args);
        jenv->DeleteLocalRef(args);
        result = engine.vm->DetachCurrentThread();
        if (result != 0) {
            LOGE("detaching from JVM failed");
            return EOF;
        }
        engine.output_line = "";
        return c;
    }
};

class jni_source : public std::streambuf {
    char buffer[1001]; // 1 char putback area
public:
    jni_source() {
        setg(buffer + 1, buffer + 1, buffer + 1);
    }

protected:
    virtual int_type underflow() {
        if (gptr() < egptr()) {
            return traits_type::to_int_type(*gptr());
        }

        int numPutBack = std::min(traits_type::to_int_type(gptr() - eback()), 1);

        std::memmove(buffer + (1 - numPutBack), gptr() - numPutBack, numPutBack);

        engine_wrapper &engine = engine_wrapper::sharedEngine();
        LOGV("Locking input mutex in jni_source");
        std::unique_lock<std::mutex> lk(engine.mutex);
        if (!engine.engine_is_running) {
            {
                LOGV("Engine was not running.");
                std::lock_guard<std::mutex> lk(engine.startup_mutex);
                engine.engine_is_running = true;
            }
            engine.startup_cv.notify_all();
            LOGV("Notifying engine startup waiters.");
        }
        LOGV("Start waiting for input...");
        engine.condition_variable.wait(lk, [&] { return (engine.input_lines.size() > 0); });
        LOGV("Input arrived to jni_source.");
        int len = std::min((int)engine.input_lines.front().length(), 1000);
        LOGD("Input: %s", engine.input_lines.front().c_str());
        std::copy(engine.input_lines.front().begin(), engine.input_lines.front().begin() + len, buffer + 1);
        if (len == engine.input_lines.front().length()) {
            engine.input_lines.pop_front();
        } else {
            engine.input_lines.front() = engine.input_lines.front().substr(len);
        }

        setg(buffer + (1 - numPutBack), buffer + 1, buffer + 1 + len);
        return traits_type::to_int_type(*gptr());
    }
};

} // namespace

// JNI
// ==================
// ==================

extern "C" {

stockfishservice::jni_sink sink_buf;
stockfishservice::jni_source source_buf;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *jenv;
    if (vm->GetEnv(reinterpret_cast<void **>(&jenv), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    jclass clazz = jenv->FindClass("com/nwagu/stockfish13/StockfishService");
    if (clazz == 0) {
        LOGE("class com/nwagu/stockfish13/StockfishService not found");
    }

    jmethodID engineToClient = jenv->GetMethodID(clazz, "engineToClient", "(Ljava/lang/String;)V");
    if (engineToClient == 0) {
        LOGE("method engineToClient not found");
    }
    LOGV("Creating sharedEngine()");
    stockfishservice::engine_wrapper &engine = stockfishservice::engine_wrapper::sharedEngine();
    engine.output_method = engineToClient;
    engine.vm = vm;

    jenv->DeleteLocalRef(clazz);
    LOGV("redirecting cout and cin");

    std::cin.rdbuf(&source_buf);
    std::cout.rdbuf(&sink_buf);

    return JNI_VERSION_1_6;
}


JNIEXPORT void JNICALL Java_com_nwagu_stockfish13_StockfishService_clientToEngine(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jstring line) {
    LOGV("At clientToEngine.");
    stockfishservice::engine_wrapper &engine = stockfishservice::engine_wrapper::sharedEngine();
    if (!engine.service_object) {
        LOGV("Setting Java Service object.");
        engine.service_object = env->NewGlobalRef(thiz);
    }
    if (!engine.engine_is_running) {
        LOGV("Calling startEngine().");
        engine.startEngine();
        {
            std::unique_lock<std::mutex> ul(engine.startup_mutex);
            LOGV("Waiting for engine startup.");
            engine.startup_cv.wait(ul, [&] {return engine.engine_is_running;});
            LOGV("Engine startup wait is over.");
        }
    }
    {
        jboolean is_copy;
        const char *line_str = env->GetStringUTFChars(line, &is_copy);
        std::lock_guard<std::mutex> lk(engine.mutex);
        engine.input_lines.emplace_back(line_str);
        env->ReleaseStringUTFChars(line, line_str);
    }
    LOGV("Waking up reader thread.");
    engine.condition_variable.notify_one();
}

} // extern "C"



