package com.nwagu.stockfish13;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class StockfishService extends Service {

    private static final String TAG = "StockfishService";

    static {
        System.loadLibrary("stockfish13");
    }

    private Messenger mClient;

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    public final static String MSG_KEY = "MSG_KEY";

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mClient = msg.replyTo;
            if (mClient == null) {
                Log.e(TAG, "msg.replyTo is null");
                super.handleMessage(msg);
                return;
            }
            Bundle data = msg.peekData();
            if (data == null) {
                super.handleMessage(msg);
                return;
            }
            String line = data.getString(MSG_KEY);
            if (line == null) {
                super.handleMessage(msg);
                return;
            }
            clientToEngine(line);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Called from Native code.
     * @param line a line sent by the engine
     */
    public void engineToClient(final String line) {
        Log.v(TAG, "At engineToClient.");
        if (mClient != null) {
            Bundle bundle = new Bundle();
            bundle.setClassLoader(Thread.currentThread().getContextClassLoader());
            bundle.putString(MSG_KEY, line);
            Message msg = Message.obtain();
            msg.setData(bundle);
            try {
                mClient.send(msg);
            } catch (RemoteException e) {
                Log.d(TAG, "Client is not reachable.");
                mClient = null;
            }
        }
    }

    /**
     * Implemented in native code. Call this to send commands to the engine.
     * @param line a command line to be sent to the engine
     */
    public native void clientToEngine(final String line);

}
