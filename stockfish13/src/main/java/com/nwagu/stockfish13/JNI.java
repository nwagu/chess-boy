package com.nwagu.stockfish13;

public class JNI {

	public JNI(){
		
	}
	
	public native void destroy();
	public native int isInited();
	public native int requestMove(int from, int to);
	public native int move(int move);
	public native void undo();
	public native void reset();
	public native void putPiece(int pos, int piece, int turn);
    public native void searchMove(int secs);
    public native void searchDepth(int depth);
    public native int getMove();
    public native int getBoardValue();
    public native int peekSearchDone();
    public native int peekSearchBestMove(int ply);
    public native int peekSearchBestValue();
    public native int peekSearchDepth();
    public native int getEvalCount();
    public native void setPromo(int piece);
    public native int getState();
    public native int isEnded();
    public native void setCastlingsEPAnd50(int wccl, int wccs, int bccl, int bccs, int ep, int r50);
    public native int getNumBoard();
    public native int getTurn();
    public native void commitBoard();
    public native void setTurn(int turn);
    public native int getMoveArraySize();
    public native int getMoveArrayAt(int i);
    public native int pieceAt(int turn, int pos);
    public native String getMyMoveToString();
    public native int getMyMove();
    public native int isLegalPosition();
    public native int isAmbiguousCastle(int from, int to);
    public native int doCastleMove(int from, int to);
    public native String toFEN();
    public native void removePiece(int turn, int pos);
    public native long getHashKey();
    public native void loadDB(String sFile, int depth);
    public native void interrupt();
    public native int getNumCaptured(int turn, int piece);

    static {
        System.loadLibrary("stockfish13");
    }

}
