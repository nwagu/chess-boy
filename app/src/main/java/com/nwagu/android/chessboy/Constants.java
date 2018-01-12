package com.nwagu.android.chessboy;

public interface Constants {

    //================ Game Modes ==============
    int GAME_MODE_MULTI_BLUETOOTH = 1;
    int GAME_MODE_AI_WHITE = 2;
    int GAME_MODE_AI_BLACK = 3;
    int GAME_MODE_MULTI_LOCAL = 4;
    int GAME_MODE_PRACTICE = 5;

    //============ PLAY STATES ========
    int DRAW_50 = 5;
    int MATE = 6;
    int STALEMATE = 7;
    int DRAW_REPEAT = 8;

    //============== BluetoothChatService ========
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
    int BLUETOOTH_UNDO_REQUEST = 10;
    int BLUETOOTH_UNDO_GRANTED = 20;
    int BLUETOOTH_UNDO_DENIED = 30;
    int REQUEST_CONNECT_DEVICE_INSECURE = 1;
    int REQUEST_ENABLE_BT = 2;
    int REQUEST_AI_MOVE = 3;
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";

    int SHOW_PGN = 23;

}
