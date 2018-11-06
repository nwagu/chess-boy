package com.nwagu.android.chessboy.Data;

public interface Constants {

    String APP_NAME = "Chess Boy";

    // Game mode codes
    int GAME_MODE_MULTI_BLUETOOTH = 1;
    int GAME_MODE_AI_WHITE = 2;
    int GAME_MODE_AI_BLACK = 3;
    int GAME_MODE_MULTI_LOCAL = 4;
    int GAME_MODE_PRACTICE = 5;

    // Game mode titles
    String GAME_TITLE_MULTI_BLUETOOTH = "Bluetooth Multiplayer";
    String GAME_TITLE_AI_WHITE = "With AI";
    String GAME_TITLE_AI_BLACK = "With AI";
    String GAME_TITLE_MULTI_LOCAL = "Local Multiplayer";
    String GAME_TITLE_PRACTICE = "Random Mode";

    // Play states
    int DRAW_50 = 5;
    int MATE = 6;
    int STALEMATE = 7;
    int DRAW_REPEAT = 8;

    // Relating to BluetoothChatService
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

    String PREFERENCES_FILE = "pref file";
    String PREF_SOUND_STATE = "soundState";

}
