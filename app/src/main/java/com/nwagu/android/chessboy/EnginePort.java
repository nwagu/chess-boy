package com.nwagu.android.chessboy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nwagu.android.chessboy.Data.Constants;

import java.lang.ref.WeakReference;

import jwtc.chess.*;

import static com.nwagu.android.chessboy.PlayActivity.game;

/*
	This is like a port to the game engine
 */
class EnginePort extends GameControl {

	private PlayActivity pa;

	// last mouse click position
	private int m_iFrom;
	
	
	// search thread message handling
	private static final int MSG_MOVE = 1;
	private static final int MSG_UCI_MOVE = 2;
	private static final int MSG_PLAY = 3;
	private static final int MSG_TEXT = 5;
	@SuppressLint("HandlerLeak")
	private Handler m_searchThreadUpdateHandler = new Handler(){
        /** Gets called on every message that is received */
        // @Override
        public void handleMessage(Message msg) {
             
       	 	//Log.i(TAG, "searchThreadUpdateHandler ->" + msg.what);
       	 	// do move
	       	 if(msg.what == MSG_MOVE){
	       		 
	       		 enableControl();
	       		 int move = msg.getData().getInt("move");
	       		 if(move == 0){
	       			 setMessage("No moves found");
	       		 } else
	       			 move(move, "", true);

                 int source = Move.getFrom(move);
                 int destination = Move.getTo(move);
                 if(!game.localWhite) {
                     source = 63 - source;
                     destination = 63 - destination;
                 }

                 if(game.gameMode == Constants.GAME_MODE_AI_WHITE || game.gameMode == Constants.GAME_MODE_AI_BLACK) {
                     game.playMove(source, destination, 2);
					 if(Move.isPromotionMove(move)) {
                         int promoPiece = Move.getPromotionPiece(move);

                         //=======this is bad ==
                         switch (promoPiece) {
                             case BoardConstants.QUEEN:
                                 if(game.localWhite) game.boardArray[destination][4] = 14; else game.boardArray[destination][4] = 84;
                                 break;
                             case BoardConstants.KNIGHT:
                                 if(game.localWhite) game.boardArray[destination][4] = 12; else game.boardArray[destination][4] = 82;
                                 break;
                             case BoardConstants.ROOK:
                                 if(game.localWhite) game.boardArray[destination][4] = 11; else game.boardArray[destination][4] = 81;
                                 break;
                             case BoardConstants.BISHOP:
                                 if(game.localWhite) game.boardArray[destination][4] = 13; else game.boardArray[destination][4] = 83;
                                 break;
                         }
                         //===================
                     }
                     game.repair();
                     System.gc();
                     game.isLocalTurn = true;
                     pa.renderBoard();
					 pa.soundManager.soundMove();
                     System.gc();
                 }

	       		 updateState();
	       	 }
	       	 else if(msg.what == MSG_UCI_MOVE){
	       		enableControl();
	       		doUCIMove(msg.getData().getInt("from"), msg.getData().getInt("to"), msg.getData().getInt("promo"));
	       		//playNotification();
	       	 }
	       	 else if(msg.what == MSG_TEXT){
	       		 setEngineMessage(msg.getData().getString("text"));
	       	 }
	       	 else if(msg.what == MSG_PLAY){
	       		 play();
	       	 } 
	           
	       	 super.handleMessage(msg);
        }
   }; 

	
	
	EnginePort(PlayActivity pla) {
		pa = pla;
		m_iFrom = -1;
		m_bActive = true;
	}
	
	public void start()
	{
		updateState();
	}
	
	public void newGame() {
		super.newGame();
		updateState();
	}

    void undoTwice() {
        if (m_bActive) {
            undo();
        } else {
            stopThreadAndUndo();
            //companion to enableControl();
        }

        //undo twice,.. that's how for chess engine
		if (m_bActive) {
			undo();
		} else {
			stopThreadAndUndo();
		}

		game.isLocalTurn = true;
    }
	public void undo() {
		super.undo();
		updateState();
	}

	boolean requestMove(int from, int to) {
        if(!game.localWhite) { from = 63 - from;to = 63 - to; }

		if(_jni.isEnded() != 0)
			return false;
		
		if(_jni.requestMove(from, to) == 0)
		{
			//setMessage(R.string.msg_illegal_move);
            Toast.makeText(pa, "Chess Engine Error. Please start a new game.", Toast.LENGTH_SHORT).show(); //as in my hand no dey am..

            //================================ this should be done in PlayActivity: this method returns a boolean for crying out loud
            game.undoLastMove(true);
            game.repair();
            pa.renderBoard();
            game.isLocalTurn = true;
            //===============================

			return false;
		}
		
		addPGNEntry(_jni.getNumBoard()-1, _jni.getMyMoveToString(), "", _jni.getMyMove(), true);
		
		updateState();
		if(_jni.isEnded() == 0 && getPlayMode() == HUMAN_PC && !game.isLocalTurn){
			play();
		}
		return true;
	}
	
	
	// in case of ambiguous castle (Fischer random), this method is called to handle the move
	protected void requestMoveCastle(int from, int to){
		if(_jni.isEnded() != 0)
			return;
		
		_jni.doCastleMove(from, to);
		addPGNEntry(_jni.getNumBoard()-1, _jni.getMyMoveToString(), "", _jni.getMyMove(), true);
		
		updateState();
		if(_jni.isEnded() == 0 && getPlayMode() == HUMAN_PC){
			play();
		}
	}
	
	private void doUCIMove(int from, int to, int promo){
		if(promo > 0){
			_jni.setPromo(promo);
		}

		if(_jni.isEnded() != 0)
			return ;
		
		if(_jni.requestMove(from, to) == 0)
		{
			//setMessage(R.string.msg_illegal_move); // UCI should make legal move
			return ;
		}
		
		addPGNEntry(_jni.getNumBoard()-1, _jni.getMyMoveToString(), "", _jni.getMyMove(), true);
		
		updateState();
	}
	
	@Override
	public void updateState() {
		super.updateState();
		paintBoard();
	}

	private void paintBoard(){
		setMessage("paint from UI");
	}

	// handle call from clickedEvent with parameters for the x and y coords of the point
	public boolean handleClick(int index)
	{
		//m_textStatus.setText("");
		if(!m_bActive)
		{
			//setMessage(R.string.msg_wait);
			return false;
		}
		
		if(m_iFrom == -1)
		{
			int turn = _jni.getTurn();
			if(_jni.pieceAt(turn, index) == BoardConstants.FIELD)
			{
				return false;
			}
			m_iFrom = index;
			paintBoard();
		}
		else
		{
			// test and make move if valid move
			boolean bValid = requestMove(m_iFrom, index);
			m_iFrom = -1;
			if(!bValid){
				paintBoard();
				return false;
			}
		}	
		return true;
	}
		
	public int getPlayMode()
	{
		return HUMAN_PC; //m_choiceMode.getSelectedIndex();
	}

	@Override
	public void setMessage(String sMsg)
	{
	}
	@Override
	public void setEngineMessage(String sText)
	{
	}
	public void setMessage(int res){
		
	}

	@Override
	public void sendMessageFromThread(String sText)
	{
		Message m = new Message();
		Bundle b = new Bundle();
		m.what = MSG_TEXT;
		b.putString("text", sText);
		m.setData(b);
		m_searchThreadUpdateHandler.sendMessage(m);
	}
	@Override
	public void sendMoveMessageFromThread(int move){
		Message m = new Message();
		Bundle b = new Bundle();
		b.putInt("move", move);
		m.what = MSG_MOVE;
		m.setData(b);
		m_searchThreadUpdateHandler.sendMessage(m);
	}
	@Override
	public void sendUCIMoveMessageFromThread(int from, int to, int promo){
		Message m = new Message();
		Bundle b = new Bundle();
		b.putInt("from", from);
		b.putInt("to", to);
		b.putInt("promo", promo);
		m.what = MSG_UCI_MOVE;
		m.setData(b);
		m_searchThreadUpdateHandler.sendMessage(m);
	}

	int getStateOfPlay() {
        return _jni.getState();
    }

    String getFullPGN() {
        return exportFullPGN();
    }

    String getMovesPGN() {
        return exportMovesPGN();
    }

    void setPromo(int promoPiece) {
        _jni.setPromo(promoPiece);
    }


    @Override
	public void play() {
		if(game.movesHistory.size() > 3)
			super.play();
		else
			new RandomMoveGenerator(new WeakReference<>(pa), game).execute();
	}






}
