package com.example.android.minigames;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Defines several constants used between {@link BluetoothConnectionService} and the UI.
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
    int MESSAGE_OPPONENT_MOVE = 6;
    int MESSAGE_OWN_MOVE = 7;
    // Key names received from the BluetoothChatService Handler
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";
    AtomicBoolean marker = new AtomicBoolean(false);

    /*Constant for the game*/
    int GAME_REQUEST = 80;
    //Goes over the network
    int REQUEST_UNDO_MOVE = 90;
    int REQUEST_RESTART_GAME = 91;
    int REPLY_AFFIRMATIVE = 100;
    int REPLY_NEGATIVE = 101;

}
