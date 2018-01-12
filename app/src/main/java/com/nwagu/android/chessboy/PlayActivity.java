package com.nwagu.android.chessboy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import android.media.MediaPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import jwtc.chess.BoardConstants;

public class PlayActivity extends AppCompatActivity {

    public static Game game;
    private UI ui;

    LinearLayout boardLayout, topBar, bottomBar, judeCaptivesCage, myCaptivesCage;
    NestedScrollView settingsPanel;
    RelativeLayout mRack, tRack;
    TextView mTurnLine, tTurnLine, gameStateView;
    ImageButton prevView, soundControl;
    LinearLayout modeOne, modeTwo, modeThree, modeFour, modeFive;
    CardView boardLayoutFrame;

    ArrayList<ImageButton> pathViews;

    MediaPlayer onCheckSound, checkBro, moveMade, boardSet;

    Animation animSlideInLeft;
    Animation animslideInRight;
    Animation animSlideOutLeft;
    Animation animslideOutRight;

    SharedPreferences settings;

    boolean iAmWhite;

    private String mConnectedDeviceName = null; //send it to handler
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;

    int widthOfScreen;
    int heightOfScreen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {Thread.sleep(1000);} catch (InterruptedException e) {} //give time for user to enjoy splash screen:)
        setTheme(R.style.MyTheme);
        setContentView(R.layout.activity_play);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0); }

        ui = new UI(PlayActivity.this);

        game = new Game(Constants.GAME_MODE_AI_WHITE, true); //make AI game default
        if (savedInstanceState != null) {
            game = savedInstanceState.getParcelable("key");
            if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                ui.loadPGN(savedInstanceState.getString("key2"));
            }
        }

        else {
            if(loadGame() != null) { game = loadGame(); }
        }

        //-------------Repair---------------
        game.repair();

        //--------Primitives and non-primitives-----------
        pathViews = new ArrayList<>();
        settings = getSharedPreferences("mSoundPrefs", MODE_PRIVATE);

        //--------Layouts and views------
        topBar = (LinearLayout) this.findViewById(R.id.upper_bar);
        boardLayout = (LinearLayout) this.findViewById(R.id.game_board);
        settingsPanel = (NestedScrollView) this.findViewById(R.id.settings_frame);
        bottomBar = (LinearLayout) this.findViewById(R.id.lower_bar);
        mRack = (RelativeLayout) this.findViewById(R.id.my_rack);
        tRack = (RelativeLayout) this.findViewById(R.id.opponent_rack);
        boardLayoutFrame = (CardView) this.findViewById(R.id.game_board_frame);
        prevView = new ImageButton(this);
        prevView = null;
        modeOne = (LinearLayout) findViewById(R.id.mode_1);
        modeTwo = (LinearLayout) findViewById(R.id.mode_2);
        modeThree = (LinearLayout) findViewById(R.id.mode_3);
        modeFour = (LinearLayout) findViewById(R.id.mode_4);
        modeFive = (LinearLayout) findViewById(R.id.mode_5);
        soundControl = (ImageButton) findViewById(R.id.sound_control);
        judeCaptivesCage = (LinearLayout) this.findViewById(R.id.opponent_captives_cage);
        myCaptivesCage = (LinearLayout) this.findViewById(R.id.my_captives_cage);
        mTurnLine = (TextView) this.findViewById(R.id.m_turn_line);
        tTurnLine = (TextView) this.findViewById(R.id.t_turn_line);
        gameStateView = (TextView) findViewById(R.id.game_status_view);

        //------Sounds and music--------
        onCheckSound = MediaPlayer.create(getApplicationContext(), R.raw.u_ar_on_check);
        checkBro = MediaPlayer.create(getApplicationContext(), R.raw.check_bro);
        moveMade = MediaPlayer.create(getApplicationContext(), R.raw.move);
        boardSet = MediaPlayer.create(getApplicationContext(), R.raw.boardset);
        resetVolumes();

        //--------Animations --------------
        animSlideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        animslideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        animSlideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        animslideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        //-------Bluetooth prelims---------
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        renderBoard();
        heyJude();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthOfScreen = size.x;
        heightOfScreen = size.y;

        settingsPanel.requestLayout();
        settingsPanel.getLayoutParams().height = widthOfScreen - 10; // -8 because of the margin to allow for

    }

    //===========================================================================================
    //===========================================================================================
    //===========================================================================================
    //===========================================================================================
    //===========================================================================================

    public void startBluetooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(PlayActivity.this, "Bluetooth is not supported on your device.", Toast.LENGTH_LONG).show();
        } else {

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
            } else {
                if (mChatService == null) {
                    mChatService = new BluetoothChatService(PlayActivity.this, mHandler);
                    mChatService.start();
                }
                Intent serverIntent = new Intent(PlayActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_INSECURE);
            }
            iAmWhite = false;
        }
    }

    public void placeClicked(View view) {
        boolean hasAValidMoveBeenMade = false;
        if (game.isLocalTurn) {
            if (prevView == null) {
                //check if start move is valid
                StringBuffer allMoves = game.getAllMoves(true, game.localWhite); //first get all possible moves
                for (int i = 0; i < allMoves.length(); i = i + 5) {
                    if ((game.boardArray[Integer.parseInt(view.getTag().toString())][0] == Integer.parseInt(allMoves.substring(i, (i + 1)))) && (game.boardArray[Integer.parseInt(view.getTag().toString())][1] == Integer.parseInt(allMoves.substring((i + 1), (i + 2))))) {
                        if (prevView == null) { //is this check necessary again?
                            prevView = (ImageButton) view;
                            prevView.setBackgroundResource(R.drawable.path_place_background);
                            //prevView.setForeground(R.drawable.path_place_background);
                        }
                        //find destination view by tag and set that view cronky
                        int tag = ((Integer.parseInt(allMoves.substring((i + 2), (i + 3))) - 1) * 8) + (Integer.parseInt(allMoves.substring((i + 3), (i + 4))) - 1);
                        ImageButton pathPlace;
                        try {
                            pathPlace = (ImageButton) boardLayout.findViewWithTag(tag);
                            pathPlace.setBackgroundResource(R.drawable.path_place_background);
                            pathViews.add(pathPlace);
                        } catch (NullPointerException e) { //due to castling
                            if (Integer.parseInt(allMoves.substring((i + 3), (i + 4))) == 0) {
                                if (game.localWhite)
                                    pathPlace = (ImageButton) boardLayout.findViewWithTag(62);
                                else pathPlace = (ImageButton) boardLayout.findViewWithTag(57);
                            } else { //if queen-side castling
                                if (game.localWhite)
                                    pathPlace = (ImageButton) boardLayout.findViewWithTag(58);
                                else pathPlace = (ImageButton) boardLayout.findViewWithTag(61);
                            }
                            pathPlace.setBackgroundResource(R.drawable.path_place_background);
                            pathViews.add(pathPlace);
                        }
                    }
                }


            } else {
                for (int i = 0; i < pathViews.size(); i++) {
                    if (view == pathViews.get(i)) {
                        //make move backend first
                        boolean promoTime = game.playMove(Integer.parseInt(prevView.getTag().toString()), Integer.parseInt(view.getTag().toString()), 1);

                        game.repair();
                        if ((game.localWhite && game.whiteOnCheck) ||
                                (!game.localWhite && game.blackOnCheck)) { //check if move obeys the rules of check
                            game.undoLastMove(true);
                            hasAValidMoveBeenMade = false;
                            Toast.makeText(PlayActivity.this, "Invalid move!", Toast.LENGTH_SHORT).show();
                            onCheckSound.start();
                            game.repair();
                            renderBoard(); //this is to remove the path dots
                        } else if(promoTime) {
                            renderBoard();
                            final int promoPlace = Integer.parseInt(view.getTag().toString());

                            //show dialog for homeboy to choose promo piece!
                            final Dialog dialog = new Dialog(this, R.style.MyDialogTheme);
                            dialog.setTitle("Promo!");

                            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                                        game.undoLastMove(true);
                                        dialog.dismiss();
                                        renderBoard();
                                    }
                                    return false;
                                }
                            });

                            if(game.localWhite) {
                                dialog.setContentView(R.layout.white_promotion_dialog);
                                //game.boardArray[promoPlace][4] = 84; //set queen as default promo piece
                            }
                            if(!game.localWhite) {
                                dialog.setContentView(R.layout.black_promotion_dialog);
                                //game.boardArray[promoPlace][4] = 14; //set queen as default promo piece
                            }

                            final ImageButton finalView = (ImageButton) view;
                            final ImageButton finalPrevView = (ImageButton) prevView;

                            View.OnClickListener terro = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chosenPromoPiece = v.getTag().toString();
                                    game.boardArray[promoPlace][4] = Integer.parseInt(chosenPromoPiece);
                                    dialog.dismiss();
                                    //send message cross-end if in bluetooth mode..
                                    if(game.gameMode == Constants.GAME_MODE_MULTI_BLUETOOTH) {
                                        int source = Integer.parseInt(finalPrevView.getTag().toString());
                                        int dest = Integer.parseInt(finalView.getTag().toString());
                                        //Here message will be source row 'n column + dest row 'n column + promo piece
                                        String moveToBeSent =  chosenPromoPiece + game.boardArray[source][0] + game.boardArray[source][1] + game.boardArray[dest][0] + game.boardArray[dest][1];
                                        sendBluetoothMessage(moveToBeSent);
                                    }
                                    else if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                                        int source = Integer.parseInt(finalPrevView.getTag().toString());
                                        int dest = Integer.parseInt(finalView.getTag().toString());
                                        int thepromoPiece = BoardConstants.QUEEN;
                                        if(chosenPromoPiece.equals("84") || chosenPromoPiece.equals("14")) thepromoPiece = BoardConstants.QUEEN;
                                        else if(chosenPromoPiece.equals("82") || chosenPromoPiece.equals("12")) thepromoPiece = BoardConstants.KNIGHT;
                                        else if(chosenPromoPiece.equals("81") || chosenPromoPiece.equals("11")) thepromoPiece = BoardConstants.ROOK;
                                        else if(chosenPromoPiece.equals("83") || chosenPromoPiece.equals("13")) thepromoPiece = BoardConstants.BISHOP;
                                        ui.setPromo(thepromoPiece);
                                        ui.requestMove(source, dest);
                                    }
                                    game.repair();
                                    game.isLocalTurn = false;
                                    renderBoard();
                                    heyJude();
                                }
                            };
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.findViewById(R.id.queen_choice).setOnClickListener(terro);
                            dialog.findViewById(R.id.knight_choice).setOnClickListener(terro);
                            dialog.findViewById(R.id.rook_choice).setOnClickListener(terro);
                            dialog.findViewById(R.id.bishop_choice).setOnClickListener(terro);

                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { //for in case user presses the back button without selecting
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    game.repair();
                                    game.isLocalTurn = false;
                                    renderBoard();
                                    heyJude();
                                }
                            });

                            dialog.show();
                        } else {

                            //send message cross-end if in bluetooth mode..
                            if(game.gameMode == Constants.GAME_MODE_MULTI_BLUETOOTH) {
                                int source = Integer.parseInt(prevView.getTag().toString());
                                int dest = Integer.parseInt(view.getTag().toString());
                                String moveToBeSent = "" + game.boardArray[source][0] + game.boardArray[source][1] + game.boardArray[dest][0] + game.boardArray[dest][1];
                                sendBluetoothMessage(moveToBeSent);
                            }

                            if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                                //jeroen chess engine
                                //send message to chess engine if in spar mode
                                int source = Integer.parseInt(prevView.getTag().toString());
                                int dest = Integer.parseInt(view.getTag().toString());
                                game.isLocalTurn = false;
                                ui.requestMove(source, dest);
                            }

                            //ok then, make move frontend
                            renderBoard();

                            hasAValidMoveBeenMade = true;

                            if ((!game.localWhite && game.whiteOnCheck) ||
                                    (game.localWhite && game.blackOnCheck)) { //if homeboy just checked jude
                                checkBro.start();
                            }
                            if(game.gameMode != Constants.GAME_MODE_AI_WHITE && game.gameMode != Constants.GAME_MODE_AI_BLACK)
                                game.isLocalTurn = false;
                        }
                        break;
                    }

                    setDefaultBackground(pathViews.get(i));
                }

                setDefaultBackground(prevView);
                prevView = null;
                pathViews = new ArrayList<>();

                if (hasAValidMoveBeenMade) {
                    moveMade.start();
                    heyJude();
                }

            }
        } else {
            Toast.makeText(PlayActivity.this, "Not your turn bro", Toast.LENGTH_SHORT).show();
        }
    }

    public void heyJude() {
        if(!game.isLocalTurn) {
            switch (game.gameMode) {
                case Constants.GAME_MODE_MULTI_BLUETOOTH:
                    renderTurnsLine(); //render and wait for opponent to send message across bluetooth........
                    break;
                case Constants.GAME_MODE_MULTI_LOCAL:
                    rotateBoard();
                    break;
                case Constants.GAME_MODE_PRACTICE:
                    new RandomMoveGenerator().execute();
                    break;
                case Constants.GAME_MODE_AI_WHITE:case Constants.GAME_MODE_AI_BLACK:
                    renderTurnsLine(); //render and wait for chess engine to send move...
                    break;
                default: break;
            }
        }
    }

    public void undoLastMove(View view) {
        if (game.movesHistory.size() > 0) {
            switch (game.gameMode) {
                case Constants.GAME_MODE_MULTI_BLUETOOTH:
                    sendBluetoothMessage(Integer.toString(Constants.BLUETOOTH_UNDO_REQUEST));
                    break;
                case Constants.GAME_MODE_MULTI_LOCAL:
                    game.undoLastMultiplayMove();
                    game.repair();
                    renderBoard();
                    heyJude();
                    break;
                case Constants.GAME_MODE_AI_BLACK: case Constants.GAME_MODE_AI_WHITE:
                    if(!game.isLocalTurn) break;
                    ui.undoTwice();
                    game.undoLastMove(true);
                    game.repair();
                    renderBoard();
                    if(game.movesHistory.size() == 0 && !game.localWhite) {
                        game.isLocalTurn = false;
                        ui.play();
                    }
                    break;
                case Constants.GAME_MODE_PRACTICE:
                    game.undoLastMove(true);
                    game.repair();
                    renderBoard();
                    game.isLocalTurn = true;
                    renderBoard();
                    if(game.movesHistory.size() == 0 && !game.localWhite) {
                        game.isLocalTurn = false;
                        heyJude();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void rotateBoard(View view) { // method to turn table
        if((game.gameMode != Constants.GAME_MODE_MULTI_BLUETOOTH && game.gameMode != Constants.GAME_MODE_MULTI_LOCAL) &&
                (game.movesHistory.size() == 0)) {
            game.rotateGame();
            final Animation an = new RotateAnimation(0.0f, 180.0f, boardLayout.getHeight() / 2, boardLayout.getWidth() / 2);
            an.setDuration(3000);
            an.setRepeatCount(1);
            an.setRepeatMode(Animation.REVERSE);
            an.setFillAfter(true);
            boardLayout.startAnimation(an);
            an.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    game.repair();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    renderBoard();
                    heyJude();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    an.setDuration(0);
                }
            });
        }
    }

    public void rotateBoard() { // method to turn table for local multiplayer mode..
        game.rotateBoard();
        final Animation an = new RotateAnimation(0.0f, 180.0f, boardLayout.getHeight() / 2, boardLayout.getWidth() / 2);
        an.setDuration(1500);
        an.setRepeatCount(1);
        an.setRepeatMode(Animation.REVERSE);
        an.setFillAfter(true);
        boardLayout.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                game.repair();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                renderBoard();
                heyJude();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                an.setDuration(0);
            }
        });
    }



//========================================================================================================================




    private void sendBluetoothMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(PlayActivity.this, "You are not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send); //=====HERE!!!!!!!!!!!!!--------------!!!!!!!!!!!!!!!!!

        }
    }


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = PlayActivity.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            saveGame(game); // save the ongoing game
                            startNewGame(Constants.GAME_MODE_MULTI_BLUETOOTH);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //Toast.makeText(PlayActivity.this, "Move sent: " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    //here i will get message from other device, decode it for local consumption, make the move, and set local turn to be true and render
                    if(readMessage.length() == 4) { // Standard move length
                        int orderInt = Integer.parseInt(readMessage); //readMessage is opponentsMove
                        int source = (((orderInt / 1000) - 1) * 8) + (((orderInt / 100) % 10) - 1); // this conversion is repeated in the makemove method. it is not necessary. use only one makemove method
                        int destination = ((((orderInt / 10) % 10) - 1) * 8) + ((orderInt % 10) - 1);
                        // compile it for local consumption..
                        source = 63 - source;
                        destination = 63 - destination;
                        game.playMove(source, destination, 2); //make move backend
                        game.repair();
                        game.isLocalTurn = true;
                        renderBoard();

                    } else if (readMessage.length() > 4) { // Promotion move length
                        int orderInt = Integer.parseInt(readMessage); //readMessage is opponentsMove
                        int chosenPromoPiece = orderInt / 10000;
                        int source = ((((orderInt / 1000) % 10) - 1) * 8) + (((orderInt / 100) % 10) - 1); // this conversion is repeated in the makemove method. it is not necessary. use only one makemove method
                        int destination = ((((orderInt / 10) % 10) - 1) * 8) + ((orderInt % 10) - 1);
                        // decode it for local consumption..
                        source = 63 - source;
                        destination = 63 - destination;
                        game.playMove(source, destination, 2); //make move backend
                        game.boardArray[destination][4] = chosenPromoPiece;
                        game.repair();
                        game.isLocalTurn = true;
                        renderBoard();

                    } else { //then it is a communication about undo, etc..
                        switch (Integer.parseInt(readMessage)) {
                            case Constants.BLUETOOTH_UNDO_REQUEST:
                                new AlertDialog.Builder(PlayActivity.this)
                                        .setIcon(R.drawable.undo)
                                        .setTitle("Undo")
                                        .setMessage(mConnectedDeviceName + " wants to undo last move. Agree?")
                                        .setPositiveButton("Undo",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface abc, int def) {
                                                        sendBluetoothMessage(Integer.toString(Constants.BLUETOOTH_UNDO_GRANTED));
                                                        game.undoLastMove(false);
                                                        game.isLocalTurn = false;
                                                        game.repair();
                                                        renderBoard();
                                                    }

                                                })

                                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface abc, int def) {
                                                sendBluetoothMessage(Integer.toString(Constants.BLUETOOTH_UNDO_DENIED));
                                            }

                                        }).show();
                                break;
                            case Constants.BLUETOOTH_UNDO_GRANTED:
                                Toast.makeText(PlayActivity.this, "Request granted", Toast.LENGTH_SHORT).show();
                                game.undoLastMove(true);
                                game.isLocalTurn = true;
                                game.repair();
                                renderBoard();
                                break;
                            case Constants.BLUETOOTH_UNDO_DENIED:
                                Toast.makeText(PlayActivity.this, "Request denied", Toast.LENGTH_LONG).show();
                                break;
                            default: break;

                        }
                    }


                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS); // Get the device MAC address
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address); // Get the BluetoothDevice object
        iAmWhite = true; //the initiator of the connection is white
        mChatService.connect(device, secure); // Attempt to connect to the device
    }


    public void switchGameSettingsBoards(View view) {
        highlightGameMode();
        if(boardLayoutFrame.getVisibility() == View.VISIBLE) {
            removeBoardStuff();
        } else {
            returnBoardStuff();
        }
    }

    public void removeBoardStuff() {
        boardLayoutFrame.startAnimation(animSlideOutLeft);
        boardLayoutFrame.setVisibility(View.INVISIBLE);
        tRack.startAnimation(animslideOutRight);
        tRack.setVisibility(View.INVISIBLE);
        mRack.startAnimation(animslideOutRight);
        mRack.setVisibility(View.INVISIBLE);
        scrollUpToStatus();
    }

    public void returnBoardStuff() {
        boardLayoutFrame.startAnimation(animslideInRight);
        boardLayoutFrame.setVisibility(View.VISIBLE);
        tRack.startAnimation(animSlideInLeft);
        tRack.setVisibility(View.VISIBLE);
        mRack.startAnimation(animSlideInLeft);
        mRack.setVisibility(View.VISIBLE);

        animSlideInLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(game.movesHistory.size() == 0 && !game.localWhite) {
                    if(game.gameMode == Constants.GAME_MODE_PRACTICE) heyJude();
                    if(game.gameMode == Constants.GAME_MODE_AI_BLACK) { heyJude(); ui.play(); }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



    public void modeSelection(View view) {
        final int modeViewTagAsGameMode = Integer.parseInt(view.getTag().toString());
        String alertTitle  = "";
        String alertMessage = "";
        switch (modeViewTagAsGameMode) {
            case Constants.GAME_MODE_MULTI_BLUETOOTH: startBluetooth(); break;
            case Constants.GAME_MODE_AI_WHITE:
                alertTitle = "White";
                alertMessage = "Play as white against a chess AI.\nYour last game will be discarded.";
                break;
            case Constants.GAME_MODE_AI_BLACK:
                alertTitle = "Black";
                alertMessage = "Play as black against a chess AI.\nYour last game will be discarded.";
                break;
            case Constants.GAME_MODE_MULTI_LOCAL:
                alertTitle = "New MultiPlayer Game";
                alertMessage = "Start a new multiplayer game with a friend.\nYour last game will be discarded.";
                break;
            case Constants.GAME_MODE_PRACTICE:
                alertTitle = "Practice";
                alertMessage = "Grandpa barely knows the rules:) Practice with a random game. Your last game will be discarded.";
                break;
            default:
                alertTitle = "Error!";
                alertMessage = "Contact the developer at once.";
                break;
        }

        if(modeViewTagAsGameMode != Constants.GAME_MODE_MULTI_BLUETOOTH) {
            new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setIcon(R.drawable.blackking)
                    .setTitle(alertTitle)
                    .setMessage(alertMessage)
                    .setPositiveButton("New Game",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface abc, int def) {
                                    startNewGame(modeViewTagAsGameMode);
                                }

                            })

                    .setNegativeButton("Cancel", null).show();
        }

    }


    public void startNewGame(int mode) {
        gameStateView.setTextColor(Color.rgb(128, 128, 128));
        switch (mode) {
            case Constants.GAME_MODE_MULTI_BLUETOOTH:
                game = new Game(mode, iAmWhite); returnBoardStuff(); game.repair(); renderBoard(); boardSet.start();
                gameStateView.setText("Bluetooth mode\n");
                break;
            case Constants.GAME_MODE_AI_WHITE:
                ui.newGame();
                game = new Game(mode, true); returnBoardStuff(); game.repair(); renderBoard(); boardSet.start();
                gameStateView.setText("With Computer\n");
                break;
            case Constants.GAME_MODE_AI_BLACK:
                ui.newGame();
                game = new Game(mode, false); returnBoardStuff(); game.repair(); renderBoard(); boardSet.start();
                gameStateView.setText("With Computer\n");
                //let engine start the game
                //ui.play();
                break;
            case Constants.GAME_MODE_MULTI_LOCAL:
                game = new Game(mode, true); returnBoardStuff(); game.repair(); renderBoard(); boardSet.start();
                gameStateView.setText("MultiPlayer mode\n");
                break;
            case Constants.GAME_MODE_PRACTICE:
                game = new Game(mode, !game.localWhite); returnBoardStuff(); game.repair(); renderBoard(); boardSet.start();
                gameStateView.setText("Random mode\n");
                break;
            default: break;
        }

    }



    //============================RENDER METHODS ======================================================================


    public void renderBoard() {
        int count = 0;
        boardLayout.removeAllViews();

        TableLayout table = new TableLayout(this);
        table.removeAllViews();
        table.invalidate();
        table.refreshDrawableState();

        boardLayout.addView(table);

        table.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        table.setStretchAllColumns(true);
        table.setOrientation(LinearLayout.VERTICAL);

        // this is 1-8
        for (int r = 1; r <= 8; r++) {
            TableRow row = new TableRow(this);
            row.removeAllViews();
            row.invalidate();
            row.refreshDrawableState();
            table.addView(row);
            row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // this is a-h
            for (int c = 1; c <= 8; c++) {
                final RelativeLayout placeLayout = new RelativeLayout(this);
                ImageButton place = new ImageButton(this);

                place.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        placeClicked(v);
                    }
                });

                place.setTag(count);

                place.setPadding(0, 0, 0, 0);
                place.setAdjustViewBounds(true);

                if (r % 2 == c % 2) {
                    placeLayout.setBackgroundColor(Color.rgb(255, 250, 220));
                    place.setBackgroundResource(R.drawable.white_place_background);
                } else {
                    placeLayout.setBackgroundColor(Color.rgb(205, 133, 63));
                    place.setBackgroundResource(R.drawable.black_place_background);
                }

                if ((count == game.lastSource) || (count == game.lastDestination))
                    place.setBackgroundResource(R.drawable.last_move_place_background);

                count++;
                updatePlace(place);

                placeLayout.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT / 8,
                        TableRow.LayoutParams.WRAP_CONTENT));

                placeLayout.addView(place, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                row.addView(placeLayout);
            }

        }
        renderCaptiveCages();
        renderTurnsLine();

        if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
            switch (ui.getStateOfPlay()) {
                case Constants.MATE:
                    if(game.isLocalTurn) gameStateView.setText("Checkmate. You Lost.\n");
                    else gameStateView.setText("Checkmate. You Win.\n");
                    gameStateView.setTextColor(Color.rgb(178, 34, 34));
                    checkBro.start();
                    switchGameSettingsBoards(new View(this));
                    break;
                case Constants.STALEMATE:
                    gameStateView.setText("Stalemate\n");
                    gameStateView.setTextColor(Color.rgb(128, 0, 0));
                    switchGameSettingsBoards(new View(this));
                    break;
                case Constants.DRAW_REPEAT:
                    gameStateView.setText("Draw by repeat\n");
                    gameStateView.setTextColor(Color.rgb(128, 0, 0));
                    switchGameSettingsBoards(new View(this));
                    break;

                case Constants.DRAW_50:
                    gameStateView.setText("Draw by 50 moves rule\n");
                    gameStateView.setTextColor(Color.rgb(128, 0, 0));
                    switchGameSettingsBoards(new View(this));
                    break;
                default:
                    gameStateView.setTextColor(Color.rgb(128, 128, 128));
                    break;
            }
        }

    }

    public void scrollUpToStatus() {
        settingsPanel.scrollTo(0, settingsPanel.getTop());
    }

    public void renderCaptiveCages() {
        judeCaptivesCage.removeAllViews();
        myCaptivesCage.removeAllViews();

        for(int i = game.movesHistory.size() - 1; i >= 0; i--) {
            String capturedPiece = game.movesHistory.get(i)[1];

            int capturedPieceInt;

            if(capturedPiece.length() < 3) capturedPieceInt = Integer.parseInt(capturedPiece);
            else { //takes care of pawn promotion capture..
                capturedPieceInt = Integer.parseInt(capturedPiece.substring(2));
            }

            if(capturedPieceInt != 0) {
                ImageButton captiveCell = new ImageButton(this);
                captiveCell.invalidate();

                captiveCell.setPadding(0, 0, 0, 0);
                captiveCell.setAdjustViewBounds(true);

                updateCaptiveCell(captiveCell, capturedPieceInt);

                //set a default dimension of 40 * 40 before adjusting to size of screen
                captiveCell.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
                captiveCell.getLayoutParams().height = widthOfScreen / 8;

                if ((game.localWhite && capturedPieceInt > 50) || (!game.localWhite && capturedPieceInt < 50)) {
                    judeCaptivesCage.addView(captiveCell);
                } else {
                    myCaptivesCage.addView(captiveCell);
                }

            }
        }
    }

    public void renderTurnsLine() {
        if(game.isLocalTurn) {
            mTurnLine.setBackgroundColor(Color.rgb(0, 255, 0)); //greenish colour
            if(game.localWhite) tTurnLine.setBackgroundColor(Color.rgb(0, 0, 0));
            else tTurnLine.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            tTurnLine.setBackgroundColor(Color.rgb(0, 255, 0)); //greenish colour
            if(game.localWhite) mTurnLine.setBackgroundColor(Color.rgb(255, 255, 255));
            else mTurnLine.setBackgroundColor(Color.rgb(0, 0, 0));
        }
    }


    public void updatePlace(ImageButton place) {
        int w = game.boardArray[Integer.parseInt(place.getTag().toString())][4];

        switch (w) {
            default:
                place.setImageResource(R.drawable.transparent);
                break;
            case 85: //white king
                place.setImageResource(R.drawable.whiteking);
                if (game.whiteOnCheck)
                    place.setBackgroundResource(R.drawable.check_place_background);
                break;
            case 15: //black king
                place.setImageResource(R.drawable.blackking);
                if (game.blackOnCheck)
                    place.setBackgroundResource(R.drawable.check_place_background);
                break;
            case 84: //white queen
                place.setImageResource(R.drawable.whitequeen);
                break;
            case 14: //black queen
                place.setImageResource(R.drawable.blackqueen);
                break;
            case 88:case 81: //white rooks
                place.setImageResource(R.drawable.whiterook);
                break;
            case 18:case 11: //black rooks
                place.setImageResource(R.drawable.blackrook);
                break;
            case 86:case 83: //white bishops
                place.setImageResource(R.drawable.whitebishop);
                break;
            case 16:case 13: //black bishops
                place.setImageResource(R.drawable.blackbishop);
                break;
            case 87:case 82: //white knights
                place.setImageResource(R.drawable.whiteknight);
                break;
            case 17:case 12: //black knights
                place.setImageResource(R.drawable.blackknight);
                break;
            case 71:case 72:case 73:case 74:case 75:case 76:case 77:case 78: //white pawns
                place.setImageResource(R.drawable.whitepawn);
                break;
            case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28: //black pawns
                place.setImageResource(R.drawable.blackpawn);
                break;
        }
    }

    public void updateCaptiveCell(ImageButton place, int captive) {
        place.setBackgroundResource(R.drawable.transparent);
        switch (captive) {
            default: // not necessary
                place.setImageResource(R.drawable.transparent);
                break;
            case 85: //white king, although it is not possible to capture a king
                place.setImageResource(R.drawable.whiteking);
                break;
            case 15: //black king, although it is not possible to capture a king
                place.setImageResource(R.drawable.blackking);
                break;
            case 84: //white queen
                place.setImageResource(R.drawable.whitequeen);
                break;
            case 14: //black queen
                place.setImageResource(R.drawable.blackqueen);
                break;
            case 88:case 81: //white rooks
                place.setImageResource(R.drawable.whiterook);
                break;
            case 18:case 11: //black rooks
                place.setImageResource(R.drawable.blackrook);
                break;
            case 86:case 83: //white bishops
                place.setImageResource(R.drawable.whitebishop);
                break;
            case 16:case 13: //black bishops
                place.setImageResource(R.drawable.blackbishop);
                break;
            case 87:case 82: //white knights
                place.setImageResource(R.drawable.whiteknight);
                break;
            case 17:case 12: //black knights
                place.setImageResource(R.drawable.blackknight);
                break;
            case 71:case 72:case 73:case 74:case 75:case 76:case 77:case 78: //white pawns
                place.setImageResource(R.drawable.whitepawn);
                break;
            case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28: //black pawns
                place.setImageResource(R.drawable.blackpawn);
                break;
        }
    }

    public void setDefaultBackground(ImageButton place) {
        if (game.boardArray[Integer.parseInt(place.getTag().toString())][0] % 2 == game.boardArray[Integer.parseInt(place.getTag().toString())][1] % 2) {
            place.setBackgroundResource(R.drawable.white_place_background);
        } else {
            place.setBackgroundResource(R.drawable.black_place_background);
        }
        //replace last move background in case the place is a last move place
        int tagInt = Integer.parseInt(place.getTag().toString());
        if ((tagInt == game.lastSource) || (tagInt == game.lastDestination))
            place.setBackgroundResource(R.drawable.last_move_place_background);
    }




    //================== TECHNICAL METHODS ============================

    public void highlightGameMode() { //hmm.. see coding
        if(Integer.parseInt(modeOne.getTag().toString()) != game.gameMode) modeOne.setAlpha(1.0f); else modeOne.setAlpha(0.5f);
        if(Integer.parseInt(modeTwo.getTag().toString()) != game.gameMode) modeTwo.setAlpha(1.0f); else modeTwo.setAlpha(0.5f);
        if(Integer.parseInt(modeThree.getTag().toString()) != game.gameMode) modeThree.setAlpha(1.0f); else modeThree.setAlpha(0.5f);
        if(Integer.parseInt(modeFour.getTag().toString()) != game.gameMode) modeFour.setAlpha(1.0f); else modeFour.setAlpha(0.5f);
        if(Integer.parseInt(modeFive.getTag().toString()) != game.gameMode) modeFive.setAlpha(1.0f); else modeFive.setAlpha(0.5f);
    }

    public void setSoundPref(View view) {
        SharedPreferences.Editor editor = settings.edit();
        boolean soundState = settings.getBoolean("soundState", true);
        editor.putBoolean("soundState", !soundState);
        editor.apply();
        //this gap of time is not enough like.
        resetVolumes();
        moveMade.start(); //make a test sound with the new set volume
    }

    public void resetVolumes() {
        boolean soundState = settings.getBoolean("soundState", true);
        if(soundState) {
            checkBro.setVolume(1.0f, 1.0f);
            onCheckSound.setVolume(1.0f, 1.0f);
            moveMade.setVolume(1.0f, 1.0f);
            boardSet.setVolume(1.0f, 1.0f);
            soundControl.setImageResource(R.drawable.vol_on);
        } else {
            checkBro.setVolume(0.0f, 0.0f);
            onCheckSound.setVolume(0.0f, 0.0f);
            moveMade.setVolume(0.0f, 0.0f);
            boardSet.setVolume(0.0f, 0.0f);
            soundControl.setImageResource(R.drawable.vol_off);
        }
    }





    //====================== WRAP-UP METHODS ===========================================================

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            /*case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;*/
            case Constants.REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    mChatService = new BluetoothChatService(PlayActivity.this, mHandler);
                    mChatService.start();
                    Intent serverIntent = new Intent(PlayActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_INSECURE);
                } else {

                }
                break;
            case Constants.REQUEST_AI_MOVE:
                if(!game.isLocalTurn) {
                    game = (Game) data.getSerializableExtra("yes");
                    game.repair(); //even after repairing in the processor class?
                    renderBoard();
                    game.isLocalTurn = true;
                }
                break;
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("key", game);
        if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
            String s = ui.getMovesPGN();
            outState.putString("key2", s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(loadGame() != null) { game = loadGame(); game.repair(); }

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame(game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onBackPressed() {

        if(boardLayoutFrame.getVisibility() == View.INVISIBLE) {
            returnBoardStuff();
            return;
        }

        switch (game.gameMode) {
            case Constants.GAME_MODE_MULTI_LOCAL:case Constants.GAME_MODE_PRACTICE:case Constants.GAME_MODE_AI_WHITE:case Constants.GAME_MODE_AI_BLACK:
                new FinishGame().execute();
                break;
            case Constants.GAME_MODE_MULTI_BLUETOOTH:
                new AlertDialog.Builder(this, R.style.MyDialogTheme)
                        .setIcon(R.drawable.blackking)
                        .setTitle("Quit")
                        .setMessage("Quit? Bluetooth multiPlayer games are NOT saved.")
                        .setPositiveButton("Quit Game",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface abc, int def) {
                                        game = loadGame(); //load last game from last point
                                        game.repair();
                                        renderBoard();
                                        Toast.makeText(PlayActivity.this, "Last game loaded", Toast.LENGTH_LONG).show();
                                    }

                                })

                        .setNegativeButton("Back", null).show();
                break;
            default: break;
        }

    }


    //==============Methods for storage purposes===========
    //=============================================

    public void saveGame(Game instance) {
        if(instance.gameMode == Constants.GAME_MODE_MULTI_BLUETOOTH) {
            //Toast.makeText(PlayActivity.this, "Attempt to save a bluetooth game!", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "gcspar";
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.reset();
            out.writeUnshared(instance);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("FATAL", e.toString());
            //Toast.makeText(PlayActivity.this, "Could not save game.", Toast.LENGTH_SHORT).show();
        }

        if(instance.gameMode == Constants.GAME_MODE_AI_WHITE || instance.gameMode == Constants.GAME_MODE_AI_BLACK) {
            String fileNamePGN = "gcsparpgn";
            String s = ui.getMovesPGN();
            FileOutputStream fosPGN;
            DataOutputStream outPGN;
            try {
                fosPGN = openFileOutput(fileNamePGN, MODE_PRIVATE);
                outPGN = new DataOutputStream(fosPGN);
                outPGN.write(s.getBytes());
                outPGN.flush();
                outPGN.close();
            } catch (IOException e) {
                Log.e("FATAL", e.toString());
                //Toast.makeText(PlayActivity.this, "Could not save game's pgn.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Game loadGame() {
        String fileName = "gcspar";
        FileInputStream fis;
        ObjectInputStream in;
        Game savedGame;
        try {
            fis = openFileInput(fileName);
            in = new ObjectInputStream(fis);
            savedGame = (Game) in.readObject();
            in.close();
        } catch (Exception e) {
            Log.e("FATAL", e.toString());
            //Toast.makeText(PlayActivity.this, "Could not load saved game.", Toast.LENGTH_SHORT).show();
            return null;
        }

        if(savedGame.gameMode == Constants.GAME_MODE_AI_WHITE || savedGame.gameMode == Constants.GAME_MODE_AI_BLACK) {
            String fileNamePGN = "gcsparpgn";
            FileInputStream fisPGN;
            DataInputStream inPGN;
            String savedGamePGN = null;
            try {
                fisPGN = openFileInput(fileNamePGN);
                inPGN = new DataInputStream(fisPGN);
                savedGamePGN = inPGN.readLine();
                inPGN.close();
                ui.loadPGN(savedGamePGN);
            } catch (Exception e) {
                Log.e("FATAL", e.toString());
                //Toast.makeText(PlayActivity.this, "Could not load saved game PGN.", Toast.LENGTH_SHORT).show();
                savedGame = new Game(Constants.GAME_MODE_AI_WHITE, true);
            }
        }
        return savedGame;
    }



    //============================ INNER CLASSES ================================


    private class RandomMoveGenerator extends AsyncTask<Void, Void, Void> {

        //Game gameCopy = new Game(Constants.GAME_MODE_PRACTICE, game.localWhite); //to test moves on

        //Damn but the two instances still interact

        String finalMove = "";
        StringBuffer allMoves = game.getAllMoves(false, !game.localWhite);

        @Override
        protected void onPreExecute() {
            renderTurnsLine();
            //gameCopy = game;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(Looper.myLooper() == null) Looper.prepare();

            boolean r = false;
            do {
                if(r) {
                    game.undoLastMove(false);
                    //remove the previous checked move from allmoves
                    for (int i = 0; i < allMoves.length(); i = i + 5) {
                        if(allMoves.substring(i, (i + 4)).equals(finalMove)) {
                            allMoves.delete(i, (i + 5));
                        }
                    }
                }
                r = true;
                game.repair();

                if(allMoves.length() < 3) return null; //stop when there is no more move in all moves

                finalMove = game.chooseRandomMove(allMoves); //choose random move after getting all moves for black
                game.playMove(finalMove, 2); //make move backend
                game.repair();
            } while((game.localWhite && game.blackOnCheck) || (!game.localWhite && game.whiteOnCheck));

            game.undoLastMove(false);
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            try {Thread.sleep(200);} catch (InterruptedException e) {} // give time for homeboy to think that the computer is thinking :)
            if (game.isLocalTurn)
                return;
            if(allMoves.length() > 3) {
                game.playMove(finalMove, 2);
                game.repair();
                game.isLocalTurn = true;
                renderBoard();
                moveMade.start(); //play the move made sound clip
            } else {
                Toast.makeText(PlayActivity.this, "No move found", Toast.LENGTH_LONG).show();
            }
        }

    }


    private class FinishGame extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //Toast.makeText(PlayActivity.this, "Saving your progress...", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveGame(game);
            if (mBluetoothAdapter.isEnabled()) mBluetoothAdapter.disable(); //close bluetooth
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //finish();
        }

    }

    public void viewGamePGN(View view) {
        String heck = "PGN is only available for games against AI";

        if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK)
            heck = ui.getFullPGN();

        Intent intent = new Intent(PlayActivity.this, PGNViewActivity.class);
        intent.putExtra("PGN", heck);
        startActivityForResult(intent, Constants.SHOW_PGN);
    }

}