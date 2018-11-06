package com.nwagu.android.chessboy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
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

import com.nwagu.android.chessboy.Bluetooth.BluetoothChatService;
import com.nwagu.android.chessboy.Bluetooth.BluetoothManager;
import com.nwagu.android.chessboy.Data.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jwtc.chess.BoardConstants;

public class PlayActivity extends AppCompatActivity {

    public static Game game;
    EnginePort enginePort;

    LinearLayout boardLayout, header, topBar, bottomBar, judeCaptivesCage, myCaptivesCage,
            modeOne, modeTwo, modeThree, modeFour, modeFive;
    NestedScrollView settingsPanel;
    RelativeLayout mRack, tRack;
    TextView headerText;
    View mTurnLine, tTurnLine;
    ImageButton prevView, soundControlButton;
    CardView boardLayoutFrame;

    ArrayList<ImageButton> pathViews;

    Animation animSlideInLeft;
    Animation animslideInRight;
    Animation animSlideOutLeft;
    Animation animslideOutRight;

    BluetoothManager bluetoothManager;
    SoundManager soundManager;
    public final BluetoothHandler bluetoothHandler = new BluetoothHandler(new WeakReference<>(this));

    public boolean iAmBluetoothWhite;

    int screenWidth, screenHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {Thread.sleep(1000);} catch (InterruptedException ignored) {} //give time for user to enjoy splash screen:)
        setTheme(R.style.MyTheme);
        setContentView(R.layout.activity_play);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0); }

        enginePort = new EnginePort(PlayActivity.this);

        game = new Game(Constants.GAME_MODE_AI_WHITE, true); //make AI game default
        if (savedInstanceState != null) {
            game = savedInstanceState.getParcelable("key");
            if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                enginePort.loadPGN(savedInstanceState.getString("key2"));
            }
        }

        else {
            if(loadGame() != null) { game = loadGame(); }
        }

        //-------------Repair---------------
        game.repair();

        //--------Primitives and non-primitives-----------
        pathViews = new ArrayList<>();

        //--------Layouts and views------
        header = this.findViewById(R.id.header);
        topBar = this.findViewById(R.id.top_bar);
        boardLayout = this.findViewById(R.id.game_board);
        settingsPanel = this.findViewById(R.id.settings_frame);
        bottomBar = this.findViewById(R.id.lower_bar);
        mRack = this.findViewById(R.id.my_rack);
        tRack = this.findViewById(R.id.opponent_rack);
        boardLayoutFrame = this.findViewById(R.id.game_board_frame);
        prevView = new ImageButton(this);
        prevView = null;
        modeOne = findViewById(R.id.mode_1);
        modeTwo = findViewById(R.id.mode_2);
        modeThree = findViewById(R.id.mode_3);
        modeFour = findViewById(R.id.mode_4);
        modeFive = findViewById(R.id.mode_5);
        headerText = findViewById(R.id.header_text);
        soundControlButton = findViewById(R.id.sound_control);
        judeCaptivesCage = this.findViewById(R.id.opponent_captives_cage);
        myCaptivesCage = this.findViewById(R.id.my_captives_cage);
        mTurnLine = this.findViewById(R.id.m_turn_line);
        tTurnLine = this.findViewById(R.id.t_turn_line);

        //--------Animations --------------
        animSlideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        animslideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        animSlideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        animslideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);


        bluetoothManager = new BluetoothManager(this);
        soundManager = new SoundManager(this, soundControlButton);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        settingsPanel.requestLayout();
        settingsPanel.getLayoutParams().height = screenWidth - 10; // -8 because of the margin to allow for

        setHeader(game.gameMode);
        renderBoard();
        heyJude();

    } // End of onCreate Method


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
                            pathPlace = boardLayout.findViewWithTag(tag);
                            pathPlace.setBackgroundResource(R.drawable.path_place_background);
                            pathViews.add(pathPlace);
                        } catch (NullPointerException e) { //due to castling
                            if (Integer.parseInt(allMoves.substring((i + 3), (i + 4))) == 0) {
                                if (game.localWhite)
                                    pathPlace = boardLayout.findViewWithTag(62);
                                else pathPlace = boardLayout.findViewWithTag(57);
                            } else { //if queen-side castling
                                if (game.localWhite)
                                    pathPlace = boardLayout.findViewWithTag(58);
                                else pathPlace = boardLayout.findViewWithTag(61);
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
                            soundManager.soundCheck();
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
                            final ImageButton finalPrevView = prevView;

                            View.OnClickListener clickListener = new View.OnClickListener() {
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
                                        bluetoothManager.sendMessage(moveToBeSent);
                                    }
                                    else if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                                        int source = Integer.parseInt(finalPrevView.getTag().toString());
                                        int dest = Integer.parseInt(finalView.getTag().toString());
                                        int thepromoPiece = BoardConstants.QUEEN;
                                        switch (chosenPromoPiece) {
                                            case "84": case "14":
                                                thepromoPiece = BoardConstants.QUEEN;
                                                break;
                                            case "82": case "12":
                                                thepromoPiece = BoardConstants.KNIGHT;
                                                break;
                                            case "81": case "11":
                                                thepromoPiece = BoardConstants.ROOK;
                                                break;
                                            case "83": case "13":
                                                thepromoPiece = BoardConstants.BISHOP;
                                                break;
                                        }
                                        enginePort.setPromo(thepromoPiece);
                                        enginePort.requestMove(source, dest);
                                    }
                                    game.repair();
                                    game.isLocalTurn = false;
                                    renderBoard();
                                    heyJude();
                                }
                            };
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.findViewById(R.id.queen_choice).setOnClickListener(clickListener);
                            dialog.findViewById(R.id.knight_choice).setOnClickListener(clickListener);
                            dialog.findViewById(R.id.rook_choice).setOnClickListener(clickListener);
                            dialog.findViewById(R.id.bishop_choice).setOnClickListener(clickListener);

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
                                bluetoothManager.sendMessage(moveToBeSent);
                            }

                            if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                                //send message to chess engine if in spar mode
                                int source = Integer.parseInt(prevView.getTag().toString());
                                int dest = Integer.parseInt(view.getTag().toString());
                                game.isLocalTurn = false;
                                enginePort.requestMove(source, dest);
                            }

                            //ok then, make move frontend
                            renderBoard();

                            hasAValidMoveBeenMade = true;

                            if ((!game.localWhite && game.whiteOnCheck) ||
                                    (game.localWhite && game.blackOnCheck)) { //if homeboy just checked jude
                                soundManager.reSoundCheck();
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
                    soundManager.soundMove();
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
                    renderTurnsLine(); // Render and wait for opponent to send message across bluetooth...
                    break;
                case Constants.GAME_MODE_MULTI_LOCAL:
                    rotateBoard();
                    break;
                case Constants.GAME_MODE_PRACTICE:
                    new RandomMoveGenerator(new WeakReference<>(PlayActivity.this), game).execute();
                    break;
                case Constants.GAME_MODE_AI_WHITE:case Constants.GAME_MODE_AI_BLACK:
                    renderTurnsLine(); // Render and wait for chess engine to send move...
                    break;
                default: break;
            }
        }
    }

    public void undoLastMove(View view) {
        if (game.movesHistory.size() > 0) {
            switch (game.gameMode) {
                case Constants.GAME_MODE_MULTI_BLUETOOTH:
                    bluetoothManager.sendMessage(Integer.toString(Constants.BLUETOOTH_UNDO_REQUEST));
                    break;
                case Constants.GAME_MODE_MULTI_LOCAL:
                    game.undoLastMultiplayMove();
                    game.repair();
                    renderBoard();
                    heyJude();
                    break;
                case Constants.GAME_MODE_AI_BLACK: case Constants.GAME_MODE_AI_WHITE:
                    if(!game.isLocalTurn) break;
                    enginePort.undoTwice();
                    game.undoLastMove(true);
                    game.repair();
                    renderBoard();
                    if(game.movesHistory.size() == 0 && !game.localWhite) {
                        game.isLocalTurn = false;
                        enginePort.play();
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

    /*
    //TODO
    This method should allow the user to switch sides in the middle of a game
     */
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

    /*
        Method to turn table for local multiplayer mode..
     */
    public void rotateBoard() {
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



    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private static class BluetoothHandler extends Handler {
        private final WeakReference<PlayActivity> mActivity;

        BluetoothHandler(WeakReference<PlayActivity> activity) {
            this.mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            final PlayActivity activity = mActivity.get();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            activity.saveGame(game); // save the ongoing game
                            activity.startNewGame(Constants.GAME_MODE_MULTI_BLUETOOTH);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
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

                    /*
                        Here we will get message from other device, decode it for local consumption,
                        make the move, and set local turn to be true, and render
                     */
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
                        activity.renderBoard();

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
                        activity.renderBoard();

                    } else { //then it is a communication about undo, etc..
                        switch (Integer.parseInt(readMessage)) {
                            case Constants.BLUETOOTH_UNDO_REQUEST:
                                new AlertDialog.Builder(activity)
                                        .setIcon(R.drawable.undo)
                                        .setTitle("Undo")
                                        .setMessage(activity.bluetoothManager.getConnectedDeviceName() + " wants to undo last move. Agree?")
                                        .setPositiveButton("Undo",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface abc, int def) {
                                                        activity.bluetoothManager.sendMessage(Integer.toString(Constants.BLUETOOTH_UNDO_GRANTED));
                                                        game.undoLastMove(false);
                                                        game.isLocalTurn = false;
                                                        game.repair();
                                                        activity.renderBoard();
                                                    }

                                                })

                                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface abc, int def) {
                                                activity.bluetoothManager.sendMessage(Integer.toString(Constants.BLUETOOTH_UNDO_DENIED));
                                            }

                                        }).show();
                                break;
                            case Constants.BLUETOOTH_UNDO_GRANTED:
                                Toast.makeText(activity, "Request granted", Toast.LENGTH_SHORT).show();
                                game.undoLastMove(true);
                                game.isLocalTurn = true;
                                game.repair();
                                activity.renderBoard();
                                break;
                            case Constants.BLUETOOTH_UNDO_DENIED:
                                Toast.makeText(activity, "Request denied", Toast.LENGTH_LONG).show();
                                break;
                            default: break;

                        }
                    }

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    activity.bluetoothManager.setConnectedDeviceName(msg.getData().getString(Constants.DEVICE_NAME));
                    //if(null != activity) {
                    Toast.makeText(activity, "Connected to " + activity.bluetoothManager.getConnectedDeviceName(), Toast.LENGTH_SHORT).show();
                    //}
                    break;
                case Constants.MESSAGE_TOAST:
                    //if(null != activity) {
                    Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    //}
                    break;
            }
        }
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
                    if(game.gameMode == Constants.GAME_MODE_AI_BLACK) { heyJude(); enginePort.play(); }
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
            case Constants.GAME_MODE_MULTI_BLUETOOTH: bluetoothManager.startBluetooth(); break;
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
        switch (mode) {
            case Constants.GAME_MODE_MULTI_BLUETOOTH:
                game = new Game(mode, iAmBluetoothWhite); returnBoardStuff(); game.repair(); renderBoard(); soundManager.soundBoardSet();
                break;
            case Constants.GAME_MODE_AI_WHITE:
                enginePort.newGame();
                game = new Game(mode, true); returnBoardStuff(); game.repair(); renderBoard(); soundManager.soundBoardSet();
                break;
            case Constants.GAME_MODE_AI_BLACK:
                enginePort.newGame();
                game = new Game(mode, false); returnBoardStuff(); game.repair(); renderBoard(); soundManager.soundBoardSet();
                //let engine start the game
                //enginePort.play();
                break;
            case Constants.GAME_MODE_MULTI_LOCAL:
                game = new Game(mode, true); returnBoardStuff(); game.repair(); renderBoard(); soundManager.soundBoardSet();
                break;
            case Constants.GAME_MODE_PRACTICE:
                game = new Game(mode, !game.localWhite); returnBoardStuff(); game.repair(); renderBoard(); soundManager.soundBoardSet();
                break;
            default: break;
        }

        setHeader(mode);

    }


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
            switch (enginePort.getStateOfPlay()) {
                case Constants.MATE:
                    if(game.isLocalTurn) headerText.setText("Checkmate. You Lost.\n");
                    else headerText.setText("Checkmate. You Win.\n");
                    soundManager.reSoundCheck();
                    switchGameSettingsBoards(new View(this));
                    break;
                case Constants.STALEMATE:
                    headerText.setText("Stalemate\n");
                    switchGameSettingsBoards(new View(this));
                    break;
                case Constants.DRAW_REPEAT:
                    headerText.setText("Draw by repeat\n");
                    switchGameSettingsBoards(new View(this));
                    break;

                case Constants.DRAW_50:
                    headerText.setText("Draw by 50 moves rule\n");
                    switchGameSettingsBoards(new View(this));
                    break;
                default:
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
                captiveCell.getLayoutParams().height = screenWidth / 8;

                if ((game.localWhite && capturedPieceInt > 50) || (!game.localWhite && capturedPieceInt < 50)) {
                    judeCaptivesCage.addView(captiveCell);
                    Log.e("FATAL", "im in");
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
            case 71:case 72:case 73:case 74:case 75:case 76:case 77:case 78: // white pawns
                place.setImageResource(R.drawable.whitepawn);
                break;
            case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28: // black pawns
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



    public void highlightGameMode() { //hmm.. see coding
        if(Integer.parseInt(modeOne.getTag().toString()) != game.gameMode) modeOne.setAlpha(1.0f); else modeOne.setAlpha(0.5f);
        if(Integer.parseInt(modeTwo.getTag().toString()) != game.gameMode) modeTwo.setAlpha(1.0f); else modeTwo.setAlpha(0.5f);
        if(Integer.parseInt(modeThree.getTag().toString()) != game.gameMode) modeThree.setAlpha(1.0f); else modeThree.setAlpha(0.5f);
        if(Integer.parseInt(modeFour.getTag().toString()) != game.gameMode) modeFour.setAlpha(1.0f); else modeFour.setAlpha(0.5f);
        if(Integer.parseInt(modeFive.getTag().toString()) != game.gameMode) modeFive.setAlpha(1.0f); else modeFive.setAlpha(0.5f);
    }



    public void toggleSoundPref(View view) {
        soundManager.toggleSoundPref();
    }

    public void setHeader(int gameMode) {
        String text;
        switch (gameMode) {
            case Constants.GAME_MODE_MULTI_BLUETOOTH:
                text = Constants.GAME_TITLE_MULTI_BLUETOOTH;
                break;
            case Constants.GAME_MODE_AI_WHITE:
                text = Constants.GAME_TITLE_AI_WHITE;
                break;
            case Constants.GAME_MODE_AI_BLACK:
                text = Constants.GAME_TITLE_AI_BLACK;
                break;
            case Constants.GAME_MODE_MULTI_LOCAL:
                text = Constants.GAME_TITLE_MULTI_LOCAL;
                break;
            case Constants.GAME_MODE_PRACTICE:
                text = Constants.GAME_TITLE_PRACTICE;
                break;
            default: text = Constants.APP_NAME; break;
        }
        headerText.setText(text);
    }


    public void viewGamePGN(View view) {
        String pgnNote = "PGN is only available for games against AI";

        if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK)
            pgnNote = enginePort.getFullPGN();

        Intent intent = new Intent(PlayActivity.this, PGNViewActivity.class);
        intent.putExtra("PGN", pgnNote);
        startActivityForResult(intent, Constants.SHOW_PGN);
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
                    bluetoothManager.connectDevice(data, false);
                }
                break;
            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothManager.init();
                    Intent serverIntent = new Intent(PlayActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_INSECURE);
                } else {
                    Toast.makeText(PlayActivity.this, "Bluetooth must be enabled", Toast.LENGTH_LONG).show();
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
            String s = enginePort.getMovesPGN();
            outState.putString("key2", s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loadGame() != null) { game = loadGame(); game.repair(); }
        bluetoothManager.startChatService();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame(game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothManager.stopChatService();
    }

    @Override
    public void onBackPressed() {

        if(boardLayoutFrame.getVisibility() == View.INVISIBLE) {
            returnBoardStuff();
            return;
        }

        switch (game.gameMode) {
            case Constants.GAME_MODE_MULTI_LOCAL:case Constants.GAME_MODE_PRACTICE:case Constants.GAME_MODE_AI_WHITE:case Constants.GAME_MODE_AI_BLACK:
                new ExitSequence(new WeakReference<>(PlayActivity.this)).execute();
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
            String s = enginePort.getMovesPGN();
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
                enginePort.loadPGN(savedGamePGN);
            } catch (Exception e) {
                Log.e("FATAL", e.toString());
                //Toast.makeText(PlayActivity.this, "Could not load saved game PGN.", Toast.LENGTH_SHORT).show();
                savedGame = new Game(Constants.GAME_MODE_AI_WHITE, true);
            }
        }
        return savedGame;
    }


    private static class ExitSequence extends AsyncTask<Void, Void, Void> {

        private final WeakReference<PlayActivity> mActivity;

        ExitSequence(WeakReference<PlayActivity> activity) {
            this.mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            PlayActivity activity = mActivity.get();
            // TODO Use animations to make the disappearance nicer
            activity.header.setVisibility(View.INVISIBLE);
            activity.topBar.setVisibility(View.INVISIBLE);
            activity.bottomBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PlayActivity activity = mActivity.get();
            activity.saveGame(game);
            activity.bluetoothManager.closeBluetooth();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PlayActivity activity = mActivity.get();
            if(activity == null)
                return;
            activity.finish();
        }

    }


}