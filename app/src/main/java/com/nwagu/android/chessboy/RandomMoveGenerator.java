package com.nwagu.android.chessboy;

import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import com.nwagu.android.chessboy.Data.Constants;

import java.lang.ref.WeakReference;

public class RandomMoveGenerator extends AsyncTask<Void, Void, Void> {
    private final WeakReference<PlayActivity> mActivity;
    private Game game;
    private String finalMove;
    private StringBuffer allMoves;

    RandomMoveGenerator(WeakReference<PlayActivity> activity, Game game) {
        this.mActivity = activity;
        this.game = game;
        finalMove = "";
        allMoves = game.getAllMoves(false, !game.localWhite);
    }

    @Override
    protected void onPreExecute() {
        mActivity.get().renderTurnsLine();
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
        PlayActivity activity = mActivity.get();
        if(activity == null)
            return;
        try {Thread.sleep(500);} catch (InterruptedException ignored) {} // Give time for player to think that the computer is thinking :)
        if (game.isLocalTurn)
            return;
        if(allMoves.length() > 3) {
            game.playMove(finalMove, 2);
            game.repair();
            game.isLocalTurn = true;
            activity.renderBoard();
            activity.soundManager.soundMove();

            if((game.gameMode == Constants.GAME_MODE_AI_BLACK) || (game.gameMode == Constants.GAME_MODE_AI_WHITE)) {
                int finalMoveInt = Integer.parseInt(finalMove);
                int source = (((finalMoveInt / 1000) - 1) * 8) + (((finalMoveInt / 100) % 10) - 1);
                int destination = ((((finalMoveInt / 10) % 10) - 1) * 8) + ((finalMoveInt % 10) - 1);
                activity.enginePort.requestMove(source, destination);
            }

        } else {
            Toast.makeText(activity, "No move found", Toast.LENGTH_LONG).show();
        }
    }
}
