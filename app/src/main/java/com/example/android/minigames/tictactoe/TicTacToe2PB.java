package com.example.android.minigames.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.minigames.BluetoothConnectionService;
import com.example.android.minigames.DeviceListActivity;
import com.example.android.minigames.R;

import java.util.Stack;

import static com.example.android.minigames.Constants.*;
import static com.example.android.minigames.Constants.REPLY_AFFIRMATIVE;
import static com.example.android.minigames.Constants.REPLY_NEGATIVE;
import static com.example.android.minigames.Constants.REQUEST_RESTART_GAME;
import static com.example.android.minigames.Constants.REQUEST_UNDO_MOVE;
import static com.example.android.minigames.Constants.marker;
import static com.example.android.minigames.R.id.UNDO;

public class TicTacToe2PB extends AppCompatActivity {

    private static int REQUEST_PENDING = 0;
    //Game parameters
    ImageView[] list = new ImageView[10];
    boolean winner = false;
    int[] idArray;
    int id;
    Stack<Integer> undo = new Stack<>();

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final String TAG = "BLUETOOTH GAME";

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothConnectionService gameService = null;

    /*Current activity context to be used in handler*/
    Context bluetooth_tictactoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetooth_tictactoe = this;
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        /*Initializing the game parameters*/
        idArray = new int[]{0, R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six,
                R.id.seven, R.id.eight, R.id.nine};
        for (int i = 1; i < 10; i++) {
            list[i] = (ImageView) findViewById(idArray[i]);
        }
        ((TextView)findViewById(R.id.Status)).setText(R.string.bt_setUpConnection);
        blockForExternalInput(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (gameService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gameService != null) {
            gameService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (gameService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (gameService.getState() == BluetoothConnectionService.STATE_NONE) {
                // Start the Bluetooth chat services
                gameService.start();
            }
        }
    }

    /*
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the BluetoothConnectionService to perform bluetooth connections
        gameService = new BluetoothConnectionService(this, mHandler);

    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
//
//    private void sendMessage(String message) {
//        // Check that we're actually connected before trying anything
//        if (gameService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
//            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Check that there's actually something to send
//        if (message.length() > 0) {
//            // Get the message bytes and tell the BluetoothConnectionService to write
//            byte[] send = message.getBytes();
//            gameService.write(send);
//
//            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
//        }
//    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
//    private TextView.OnEditorActionListener mWriteListener
//            = new TextView.OnEditorActionListener() {
//        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
//            // If the action is a key-up event on the return key, send the message
//            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
//                String message = view.getText().toString();
//                sendMessage(message);
//            }
//            return true;
//        }
//    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
//    private void setStatus(int resId) {
//        if (null == this) {
//            return;
//        }
//        final ActionBar actionBar = getActionBar();
//        if (null == actionBar) {
//            return;
//        }
//        actionBar.setSubtitle(resId);
//        ((TextView)findViewById(R.id.Player)).setText(resId);
//    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
//    private void setStatus(CharSequence subTitle) {
//        if (null == this) {
//            return;
//        }
//        final ActionBar actionBar = getActionBar();
//        if (null == actionBar) {
//            return;
//        }
//        actionBar.setSubtitle(subTitle);
//    }

    /**
     * The Handler that gets information back from the BluetoothConnectionService
     */
    //TODO Handle I/O from other user to mark tiles
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GAME_REQUEST:
                    handleRequest(msg.arg1);
                    break;
                case MESSAGE_OPPONENT_MOVE:
                    playForFriend(msg.arg1);
                    break;
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConnectionService.STATE_CONNECTED:

                            break;
                        case BluetoothConnectionService.STATE_CONNECTING:

                            break;
                        case BluetoothConnectionService.STATE_LISTEN:
                        case BluetoothConnectionService.STATE_NONE:

                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                case MESSAGE_READ:

                    // construct a string from the valid bytes in the buffer


                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    if (bluetooth_tictactoe != null) {
                        Toast.makeText(bluetooth_tictactoe, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        if(!marker.get()){
                            blockForExternalInput(true);
                            ((TextView)findViewById(R.id.Status)).setText(R.string.bt_wait);
                        }
                        else
                            ((TextView)findViewById(R.id.Status)).setText(R.string.you_play);
                    }
                    break;
                case MESSAGE_TOAST:
                    if (null != bluetooth_tictactoe) {
                        Toast.makeText(bluetooth_tictactoe, msg.getData().getString(TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        gameService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    /*UI changes for the game*/
    //TODO Disable every button except main menu unless i get a reply.
    public void addMarker(View view) {
        ImageView a = (ImageView) view;
        Log.d(TAG, "entered on click listener.");
        if (marker.get()) {
            a.setImageResource(R.mipmap.x);
            Log.d(TAG, "Setting value as x ");
            a.setTag(R.mipmap.x);
            ((TextView) findViewById(R.id.Status)).setText(R.string.bt_wait);
        } else {
            a.setImageResource(R.mipmap.gameo);
            a.setTag(R.mipmap.gameo);
            ((TextView) findViewById(R.id.Status)).setText(R.string.bt_wait);
        }
        Log.d(TAG, "Completed setting resource.");
        a.setOnClickListener(null);
        id = view.getId();
        int move = getMoveFromId(id);
        undo.push(id);
        gameService.write(move);
        Log.d(TAG, "Returned from writing the tag");
        blockForExternalInput(true);
        if (checkIfOver(id)) {
            /*Ready to start new game*/
            blockForExternalInput(false);
            Button undo_button = (Button) findViewById(UNDO);
            undo_button.setEnabled(false);
            Log.d(TAG, "Entered game over");
            for (int i = 1; i <= 9; i++) {
                list[i].setOnClickListener(null);
            }
            ((TextView) findViewById(R.id.Status)).setText(R.string.you_win);
        }
        if (draw()) {
            Log.d(TAG, "Entered game Draw");
            ((TextView) findViewById(R.id.Status)).setText(R.string.draw);
        }
        Log.d(TAG, "Method execution complete.");
    }

    /*Method for recieving input from friend by using the handler*/
    void playForFriend(int move) {
        ImageView current = ((ImageView) findViewById(idArray[move]));
        if (!marker.get()) {
            current.setImageResource(R.mipmap.x);
            current.setTag(R.mipmap.x);
            ((TextView) findViewById(R.id.Status)).setText(R.string.you_play);
        } else {
            current.setImageResource(R.mipmap.gameo);
            current.setTag(R.mipmap.gameo);
            ((TextView) findViewById(R.id.Status)).setText(R.string.you_play);
        }
        blockForExternalInput(false);
        undo.push(idArray[move]);
        if (checkIfOver(idArray[move])) {
            /*Ready to start new game*/
            blockForExternalInput(false);
            Button undo_button = (Button) findViewById(UNDO);
            undo_button.setEnabled(false);
            for (int i = 1; i <= 9; i++) {
                list[i].setOnClickListener(null);
            }
            ((TextView) findViewById(R.id.Status)).setText(R.string.you_lose);
        }
        if (draw()) {
            ((TextView) findViewById(R.id.Status)).setText(R.string.draw);
        }
    }

    private int getMoveFromId(int id) {
        for (int i = 1; i < idArray.length; i++) {
            if (id == idArray[i])
                return i;
        }
        return 0;
    }

    private boolean draw() {
        for (int i = 1; i <= 9; i++) {
            if (list[i].getTag() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfOver(int id) {

        for (int i = 1; i < 10; i++) {
            list[i] = (ImageView) findViewById(idArray[i]);
        }
        //Check for 2nd 4th and 6th position
        for (int i = 2; i < 9; i += 2) {
            if (id == idArray[i]) {
                winner = checkHorizontal(id);
                winner = winner || checkVertical(id);
                return winner;
            }
        }

        for (int i = 1; i <= 9; i += 2) {
            if (id == idArray[i]) {
                winner = checkHorizontal(id);
                winner = winner || checkVertical(id);
                winner = winner || checkDiagonal(id);
                return winner;
            }
        }
        return winner;
    }

    private boolean checkVertical(int id) {
        boolean set;
        for (int i = 1; i <= 3; i++) {
            set = true;
            for (int j = i; j <= 9; j += 3) {
                if (list[j].getTag() == null)
                    set = false;
            }
            if (set && (id == idArray[i] || id == idArray[i + 3] || id == idArray[i + 6])) {
                if (list[i].getTag().equals(list[i + 3].getTag()) && list[i].getTag().
                        equals(list[i + 6].getTag())) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean checkHorizontal(int id) {
        boolean set;
        for (int i = 1; i <= 9; i += 3) {
            set = true;
            for (int j = i; j < i + 3; j++) {
                if (list[j].getTag() == null)
                    set = false;
            }
            if (set && (id == idArray[i] || id == idArray[i + 1] || id == idArray[i + 2])) {
                if (list[i].getTag().equals(list[i + 1].getTag()) && list[i].getTag().
                        equals(list[i + 2].getTag())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(int id) {
        boolean set = true;
        for (int i = 1; i <= 9; i += 4) {
            if (list[i].getTag() == null)
                set = false;
        }
        if (set && (id == idArray[1] || id == idArray[5] || id == idArray[9])) {
            if (list[1].getTag().equals(list[5].getTag()) && list[1].getTag().
                    equals(list[9].getTag())) {
                return true;
            }
        }
        set = true;
        for (int i = 3; i < 9; i += 2) {
            if (list[i].getTag() == null)
                set = false;
        }
        if (set && (id == idArray[3] || id == idArray[5] || id == idArray[7])) {
            if (list[3].getTag().equals(list[5].getTag()) && list[3].getTag().
                    equals(list[7].getTag())) {
                return true;
            }
        }
        return false;
    }

    /*Implementation of restart after confirmation*/
    public void restartGame() {
        undo.clear();
        for (int i = 1; i < 10; i++) {
            ImageView current = list[i];
            current.setTag(null);
            current.setImageResource(0);
            boolean t = marker.get();
            marker.set(!t);
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMarker(v);
                }
            });
        }
        findViewById(UNDO).setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Button) findViewById(UNDO)).setTextColor(getResources().getColor(R.color.colorAccent, null));
        } else
            ((Button) findViewById(UNDO)).setTextColor(getResources().getColor(R.color.colorAccent));
        winner = false;
        ((TextView) findViewById(R.id.Status)).setText(R.string.player_1_can_play);
    }

    /*Implement undo after confirmation*/
    public void undoTwoMoves() {
        if (!undo.isEmpty()) {
            int k= (undo.size()>1) ? 2:undo.size();
            for (int i = 0; i < k; i++) {
                int undo_id = undo.pop();
                ImageView current = ((ImageView) findViewById(undo_id));
                current.setTag(null);
                current.setImageResource(0);
                current.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMarker(v);
                    }
                });
            }
        }
    }

    private void sendRequest(int requestCode) {
        switch (requestCode) {
            case REQUEST_UNDO_MOVE:
                REQUEST_PENDING = REQUEST_UNDO_MOVE;
                ((TextView)findViewById(R.id.Status)).setText(R.string.bt_waita_reply);
                gameService.write(REQUEST_UNDO_MOVE);
                break;
            case REQUEST_RESTART_GAME:
                ((TextView)findViewById(R.id.Status)).setText(R.string.bt_waita_reply);
                REQUEST_PENDING = REQUEST_RESTART_GAME;
                gameService.write(REQUEST_RESTART_GAME);
                break;
        }

    }

    /*Handle additional request by showing popup box*/
    private void handleRequest(final int requestCode) {
        String msg = "none";
        switch (requestCode) {
            case REQUEST_UNDO_MOVE:
                msg = "Undo move";
                break;
            case REQUEST_RESTART_GAME:
                msg = "Restart game";
        }
        if (requestCode < 100) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Play").setMessage(mConnectedDeviceName + " wants to " + msg + "\nAllow ?")
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    gameService.write(REPLY_AFFIRMATIVE);
                                    switch (requestCode){
                                        case REQUEST_UNDO_MOVE:undoTwoMoves();
                                            break;
                                        case REQUEST_RESTART_GAME:restartGame();
                                            break;
                                    }
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gameService.write(REPLY_NEGATIVE);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            switch (REQUEST_PENDING) {
                case REQUEST_UNDO_MOVE:
                    if (requestCode == REPLY_AFFIRMATIVE) {
                        ((TextView) findViewById(R.id.Status)).setText(R.string.you_play);
                        undoTwoMoves();
                    }
                    else{
                        Toast.makeText(this,"Your Request was denied.",Toast.LENGTH_LONG).show();
                    }
                    break;
                case REQUEST_RESTART_GAME:
                    if (requestCode == REPLY_AFFIRMATIVE)
                        restartGame();
                    else{
                        Toast.makeText(this,"Your Request was denied.",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
            REQUEST_PENDING = 0;
            blockForExternalInput(false);
        }
    }

    /*Helper method to make player wait for friend to play*/
    private void blockForExternalInput(boolean value) {
        for (int i = 1; i < 10; i++) {
            ImageView current = (ImageView) findViewById(idArray[i]);
            current.setClickable(!value);
        }
        findViewById(UNDO).setEnabled(!value);
        findViewById(R.id.t1Restart).setEnabled(!value);
    }

    /*on click listener for the button*/
    public void restart(View view) {
        blockForExternalInput(true);
        sendRequest(REQUEST_RESTART_GAME);
    }

    /*on click listener for the button*/
    public void undo(View view) {
        blockForExternalInput(true);
        sendRequest(REQUEST_UNDO_MOVE);
    }

    public void mainMenu(View view) {
        this.finish();
    }

}
