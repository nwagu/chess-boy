package jwtc.chess;

import android.util.Log;

public class JNI {

	public JNI(){
		
	}
	
	public void newGame(){
		reset();
		putPiece(BoardConstants.a8, BoardConstants.ROOK, BoardConstants.BLACK);
		putPiece(BoardConstants.b8, BoardConstants.KNIGHT, BoardConstants.BLACK);
		putPiece(BoardConstants.c8, BoardConstants.BISHOP, BoardConstants.BLACK);
		putPiece(BoardConstants.d8, BoardConstants.QUEEN, BoardConstants.BLACK);
		putPiece(BoardConstants.e8, BoardConstants.KING, BoardConstants.BLACK);
		putPiece(BoardConstants.f8, BoardConstants.BISHOP, BoardConstants.BLACK);
		putPiece(BoardConstants.g8, BoardConstants.KNIGHT, BoardConstants.BLACK);
		putPiece(BoardConstants.h8, BoardConstants.ROOK, BoardConstants.BLACK);
		putPiece(BoardConstants.a7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.b7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.c7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.d7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.e7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.f7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.g7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.h7, BoardConstants.PAWN, BoardConstants.BLACK);
		
		putPiece(BoardConstants.a1, BoardConstants.ROOK, BoardConstants.WHITE);
		putPiece(BoardConstants.b1, BoardConstants.KNIGHT, BoardConstants.WHITE);
		putPiece(BoardConstants.c1, BoardConstants.BISHOP, BoardConstants.WHITE);
		putPiece(BoardConstants.d1, BoardConstants.QUEEN, BoardConstants.WHITE);
		putPiece(BoardConstants.e1, BoardConstants.KING, BoardConstants.WHITE);
		putPiece(BoardConstants.f1, BoardConstants.BISHOP, BoardConstants.WHITE);
		putPiece(BoardConstants.g1, BoardConstants.KNIGHT, BoardConstants.WHITE);
		putPiece(BoardConstants.h1, BoardConstants.ROOK, BoardConstants.WHITE);
		putPiece(BoardConstants.a2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.b2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.c2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.d2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.e2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.f2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.g2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.h2, BoardConstants.PAWN, BoardConstants.WHITE);
		
		setCastlingsEPAnd50(1, 1, 1, 1, -1, 0);
		
		commitBoard();
	}
	
	

	public final boolean initFEN(final String sFEN){
		// rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1
		
		reset();
		try { 
				
			String s;
			int pos = 0, i = 0, iAdd;
			while(pos < 64 && i < sFEN.length()){
				iAdd = 1;
				s = sFEN.substring(i, i+1);
				switch (s) {
					case "k":
						putPiece(pos, BoardConstants.KING, BoardConstants.BLACK);
						break;
					case "K":
						putPiece(pos, BoardConstants.KING, BoardConstants.WHITE);
						break;
					case "q":
						putPiece(pos, BoardConstants.QUEEN, BoardConstants.BLACK);
						break;
					case "Q":
						putPiece(pos, BoardConstants.QUEEN, BoardConstants.WHITE);
						break;
					case "r":
						putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK);
						break;
					case "R":
						putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE);
						break;
					case "b":
						putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK);
						break;
					case "B":
						putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE);
						break;
					case "n":
						putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK);
						break;
					case "N":
						putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE);
						break;
					case "p":
						putPiece(pos, BoardConstants.PAWN, BoardConstants.BLACK);
						break;
					case "P":
						putPiece(pos, BoardConstants.PAWN, BoardConstants.WHITE);
						break;
					case "/":
						iAdd = 0;
						break;
					default:
						iAdd = Integer.parseInt(s);
						break;
				}
				pos += iAdd;
				i++;
			}
			i++;// skip space
			if(i < sFEN.length()){
				int wccl = 0, wccs = 0, bccl = 0, bccs = 0, colA = 0, colH = 7, ep = -1, r50 = 0, turn;
				String[] arr = sFEN.substring(i).split(" ");
				if(arr.length > 0){
					if(arr[0].equals("w")){
						turn = BoardConstants.WHITE;
					} else {
						turn = BoardConstants.BLACK;
					}
					if(arr.length > 1){
						if(arr[1].contains("k")){
							bccs = 1;
						}
						if(arr[1].contains("q")){
							bccl = 1;
						} 
						if(arr[1].contains("K")){
							wccs = 1;
						}
						if(arr[1].contains("Q")){
							wccl = 1;
						}
						
						if(arr.length > 2){
							if(!arr[2].equals("-")){
								ep = Pos.fromString(arr[2]);
							}
							if(arr.length > 3){
								r50 = Integer.parseInt(arr[3]);
							}
						}
						setCastlingsEPAnd50(wccl, wccs, bccl, bccs, ep, r50);
						
						setTurn(turn);
						commitBoard();
						
						return true;
					}
				}
				
			}
		} catch (Exception ex){
			//Log.e("initFEN", ex.toString());
			return false;
		}
		return false;
	}
	
	protected boolean isPosFree(int pos)
	{
		return (pieceAt(BoardConstants.BLACK, pos) == BoardConstants.FIELD &&
			pieceAt(BoardConstants.WHITE, pos) == BoardConstants.FIELD);
	}
	
	protected int getAvailableCol(int colNum){
		int col = 0, i = 0, pos;
		do {
			pos = Pos.fromColAndRow(col, 0);
			if(isPosFree(pos)){
				i++;
			}
			col++;
		} while(i <= colNum && col < 9);
		
		col--;
		return col;
	}
	
	protected int getFirstAvailableCol(){
		int col = 0,  pos;
		do{
			pos = Pos.fromColAndRow(col, 0);
			if(isPosFree(pos)){
				return col;
			}
			col++;
		}while(col < 8);
		return col;
	}
	
	
	
	/*
0 NNxxx
1 NxNxx
2 NxxNx
3 NxxxN
4 xNNxx
5 xNxNx
6 xNxxN
7 xxNNx
8 xxNxN
9 xxxNN
	*/
	public int initRandomFisher(int n){
		
		reset();
		
		int[][] NN = {
				{0, 1},
				{0, 2},
				{0, 3},
				{0, 4},
				{1, 2},
				{1, 3},
				{1, 4},
				{2, 3},
				{2, 4},
				{3, 4}
		};
		
		int Bw, Bb, Q, N1, N2;
		
		int col, col2, pos, ret = 0;
		
		if(n >= 0){
			
			Bw = n % 4;
			n = (int)Math.floor(n / 4.0);
			
			Bb = n % 4;
			n = (int)Math.floor(n / 4.0);
			
			Q = n % 6;
			n = (int)Math.floor(n / 6.0);
			
			n = (n % 10);
			
			N1 = NN[n][0];
			N2 = NN[n][1];
			
		} else {
			
			Bw = (int)(Math.random() * 3);
			Bb = (int)(Math.random() * 3);
			
			Q = (int)(Math.random() * 5);
			
			n = (int)(Math.random() * 8);
			
			N1 = NN[n][0];
			N2 = NN[n][1];
		
		}
		
		ret = (96 * (5 - (((3 - N1)*(4 - N1))/2) + N2)) + (16 * Q) + (4 * Bb) + Bw;
		
		Log.i("Chess960", "Bw " + Bw + " Bb " + Bb + " Q " + Q + " n " + n + " N1 " + N1 + " N2 " + N2);
		// white square bishop
		col = 1 + 2 * Bw;
		Log.i("Chess960", "Bw col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE);
		
		// black-square bishop
		col = 2 * Bb;
		Log.i("Chess960", "Bb col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE);
		
		// queen
		col = getAvailableCol(Q);
		Log.i("Chess960", "Q col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.QUEEN, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.QUEEN, BoardConstants.WHITE);

		// knight 1
		col = getAvailableCol(N1);
		col2 = getAvailableCol(N2);
		Log.i("Chess960", "N1 col " + col + " N2 " + col2);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE);

		// knight 2
		pos = Pos.fromColAndRow(col2, 0);
		putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col2, 7);
		putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE);
		
		// ROOK A
		col = getFirstAvailableCol();
		Log.i("Chess960", "R1 col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE);
		
		// KING
		col = getFirstAvailableCol();
		Log.i("Chess960", "K col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.KING, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.KING, BoardConstants.WHITE);
		// ROOK H
		col = getFirstAvailableCol();
		Log.i("Chess960", "R2 col " + col);
		pos = Pos.fromColAndRow(col, 0);
		putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK);
		pos = Pos.fromColAndRow(col, 7);
		putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE);

		// 
		putPiece(BoardConstants.a7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.b7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.c7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.d7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.e7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.f7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.g7, BoardConstants.PAWN, BoardConstants.BLACK);
		putPiece(BoardConstants.h7, BoardConstants.PAWN, BoardConstants.BLACK);
	
		putPiece(BoardConstants.a2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.b2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.c2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.d2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.e2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.f2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.g2, BoardConstants.PAWN, BoardConstants.WHITE);
		putPiece(BoardConstants.h2, BoardConstants.PAWN, BoardConstants.WHITE);
		
		setCastlingsEPAnd50(1, 1, 1, 1, -1, 0);

		commitBoard();
		
		return ret;
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
    //public native int[] getMoveArray();
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
        System.loadLibrary("jwtc");
    }

}
