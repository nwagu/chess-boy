package com.nwagu.chessengineintegration

import java.io.*
import java.lang.Process
import java.lang.Runtime
import java.lang.StringBuffer
import java.lang.Thread
import java.util.regex.Pattern

val uciMovePattern = Pattern.compile("([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?")

class UCIBridge(
    val pathToBinary: String,
    val uciEngineListener: UCIEngineListener
) {

    interface UCIEngineListener {
        fun onUciOk()
        fun onReady()
        fun onBestMove(move: String)
        fun onCopyProtection()
        fun onId(id: Pair<String, String>)
        fun onInfo(info: String)
        fun onOption(option: Pair<String, String>)
        fun onError(error: String)
    }

    private var _reader: BufferedReader? = null
    private var _writer: PrintWriter? = null
    private lateinit var _process: Process


    fun init() {
        try {

            runConsole("/system/bin/chmod 777 $pathToBinary")

            _process = Runtime.getRuntime().exec(pathToBinary)

            _reader = BufferedReader(InputStreamReader(_process.inputStream))
            _writer = PrintWriter(OutputStreamWriter(_process.outputStream))

            Thread {
                try {

                    var i = 0
                    while (true) {

                        val line = _reader!!.readLine()

                        when {
                            line == null -> {
                                true
                            }
                            (line.isNotEmpty()) -> {
                                var x = 6
                            }
                            line.startsWith("id") -> {
                                // TODO Parse id
                                uciEngineListener.onId(Pair("", ""))
                            }
                            line.startsWith("option") -> {
                                // TODO Parse options
                                uciEngineListener.onOption(Pair("", ""))
                            }
                            line.startsWith("uciok") -> {
                                uciEngineListener.onUciOk()
                            }
                            line.startsWith("copyprotection") -> {
                                uciEngineListener.onCopyProtection()
                            }
                            line.startsWith("readyok") -> {
                                uciEngineListener.onReady()
                            }
                            line.startsWith("info") -> {
                                // TODO Parse info
                                uciEngineListener.onInfo(line)
                            }
                            line.startsWith("bestmove") -> {
                                val move = line
                                    .removePrefix("bestmove ")
                                    .trimStart()
                                    .takeWhile { it != ' ' }
                                if (uciMovePattern.matcher(move).matches()) {
                                    uciEngineListener.onBestMove(move)
                                }
                            }
                        }
                    }
                } catch (ex: Exception) {
                    try {
                        _process.destroy()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
            sendCommand("uci")
        } catch (e: Exception) {
            e.printStackTrace()
            uciEngineListener.onError(e.toString())
        }
    }

    fun play(fen: String, level: Int) {
        sendCommand("ucinewgame")
        sendCommand("position fen $fen")
        sendCommand("go depth $level")
    }

    fun quit() {
        sendCommand("quit")
        try {
            _process.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendCommand(cmd: String) {
        try {
            _writer!!.println(cmd)
            _writer!!.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        sendCommand("stop")
    }

    companion object {

        fun install(`in`: InputStream, sEngine: String) {
            Thread {
                try {
                    val out: OutputStream =
                        FileOutputStream("/data/data/com.nwagu.android.chessboy/$sEngine")
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    out.close()
                    `in`.close()
                    runConsole("/system/bin/ls /data/data/com.nwagu.android.chessboy/")
                    runConsole("/system/bin/chmod 744 /data/data/com.nwagu.android.chessboy/$sEngine")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }

        fun runConsole(sCmd: String?): String? {
            return try {
                // Executes the command.
                val process = Runtime.getRuntime().exec(sCmd)
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var read: Int
                val buffer = CharArray(4096)
                val output = StringBuffer()
                while (reader.read(buffer).also { read = it } > 0) {
                    output.append(buffer, 0, read)
                }
                reader.close()

                // Waits for the command to finish.
                process.waitFor()
                output.toString()
            } catch (e: Exception) {
                null
            }
        }
    }

}