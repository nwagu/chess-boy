package com.nwagu.android.chessboy;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

class Game implements Serializable, Parcelable {

    private int mData;

    public int describeContents() {
        return 0;
    }

    //save object in parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    //recreate object from parcel
    private Game(Parcel in) {
        mData = in.readInt();
    }


//===========================================================

    int[][] boardArray = new int[64][5];
    private StringBuffer[][] pathsArray = new StringBuffer[64][7]; //1 - 7: King, Queen, Bishop, Knight, Rook, White Pawn, Black Pawn.
    ArrayList<String[]> movesHistory = new ArrayList<>();

    int gameMode;

    boolean localWhite = true;
    boolean isLocalTurn = true;

    private int can11Castle; //castling may occur with rook 11 only when this variables is 0
    private int can18Castle; //castling may occur with rook 18 only when this variables is 0
    private int can81Castle; //castling may occur with rook 81 only when this variables is 0
    private int can88Castle; //castling may occur with rook 88 only when this variables is 0

    int lastSource = -1;
    int lastDestination = -1;
    private int whiteFlag; //this represents the position of the white King
    private int blackFlag; //this represents the position of the black King
    boolean whiteOnCheck = false;
    boolean blackOnCheck = false;

    private final long serialVersionUID = -152305222281557734L;

    Game(int mode, boolean isWhite) { //this constructs two arrays: boardArray & pathsArray

        gameMode = mode;

        localWhite = true;
        isLocalTurn = true;

        movesHistory = new ArrayList<>();

        whiteFlag = 60;
        blackFlag = 4;

        can11Castle = 0;
        can18Castle = 0;
        can81Castle = 0;
        can88Castle = 0;

        //boardArray with each row containing: ROW; COL; sum of ROW and COL; difference of same; and chessman code
        int d = 0;
        for(int r = 1; r <= 8; r++){ //per row of chessboard
            for(int c = 1; c <= 8; c++){ //per column of each row
                boardArray[d][0] = r;
                boardArray[d][1] = c;
                boardArray[d][2] = (r + c);
                boardArray[d][3] = (r - c);
                if((r == 1) || (r == 2) || (r == 7) || (r == 8))
                    boardArray[d][4] = ((r * 10) + c); //places chessmen with codes according to position at start eg. black rook on Queens side is 11
                else boardArray[d][4] = 0;
                d++;
            }
        }

        if(!isWhite) { // for if user-black game is being created
            rotateGame();
            whiteFlag = 3;
            blackFlag = 59;
        }

        //pathsArray
        for(int e = 0; e < pathsArray.length; e++) {
            for(int i = 0; i < 7; i++) {
                pathsArray[e][i] = findPath(e, i);
            }
        }

    }

    //this method is used for the initial construction of the pathsArray
    private StringBuffer findPath(int v, int w) { //this selects spots that match every chessman from every spot
        StringBuffer path = new StringBuffer();
        switch(w){
            case 0: //King
                for (int[] aBoardArray : boardArray) {
                    if ((Math.abs(aBoardArray[0] - boardArray[v][0]) <= 1) && (Math.abs(aBoardArray[1] - boardArray[v][1]) <= 1))
                        if (!((aBoardArray[0] == boardArray[v][0]) && (aBoardArray[1] == boardArray[v][1]))) //excludes self as path
                            addToPath(path, aBoardArray[0], aBoardArray[1]);
                    //for the kings' castling move
                    if ((v == 3 || v == 4 || v == 60 || v == 59) && (boardArray[v][4] == 15 || boardArray[v][4] == 85) && (aBoardArray[0] == boardArray[v][0]) && (Math.abs(aBoardArray[1] - boardArray[v][1]) == 2)) { //it is necessary to check if it is a king sitting there
                        if (localWhite) {
                            if (aBoardArray[1] > 5)
                                addToPath(path, 0, 0); //if castling with king's rook, destination 00
                            else
                                addToPath(path, 0, 1); //else if castling with queen's rook, destination 01
                        } else {
                            if (aBoardArray[1] > 5)
                                addToPath(path, 0, 1); //if castling with queen's rook, destination 01
                            else
                                addToPath(path, 0, 0); //else if castling with king's rook, destination 00
                        }
                    }
                }
                break;
            case 1: //Queen
                for (int[] aBoardArray1 : boardArray) {
                    if ((aBoardArray1[0] == boardArray[v][0]) || (aBoardArray1[1] == boardArray[v][1]) || (aBoardArray1[2] == boardArray[v][2]) || (aBoardArray1[3] == boardArray[v][3]))
                        if (!((aBoardArray1[0] == boardArray[v][0]) && (aBoardArray1[1] == boardArray[v][1]))) //excludes self as path
                            addToPath(path, aBoardArray1[0], aBoardArray1[1]);
                }
                break;
            case 2: //Bishop
                for (int[] aBoardArray : boardArray) {
                    if ((aBoardArray[2] == boardArray[v][2]) || (aBoardArray[3] == boardArray[v][3]))
                        if (!((aBoardArray[0] == boardArray[v][0]) && (aBoardArray[1] == boardArray[v][1]))) //excludes self as path
                            addToPath(path, aBoardArray[0], aBoardArray[1]);
                }
                break;
            case 3: //Knight
                for (int[] aBoardArray : boardArray) {
                    if ((Math.abs(aBoardArray[0] - boardArray[v][0]) == 2) && (Math.abs(aBoardArray[1] - boardArray[v][1]) == 1) || (Math.abs(aBoardArray[0] - boardArray[v][0]) == 1) && (Math.abs(aBoardArray[1] - boardArray[v][1]) == 2))
                        addToPath(path, aBoardArray[0], aBoardArray[1]);
                }
                break;
            case 4: //Rook
                for (int[] aBoardArray : boardArray) {
                    if ((aBoardArray[0] == boardArray[v][0]) || (aBoardArray[1] == boardArray[v][1]))
                        if (!((aBoardArray[0] == boardArray[v][0]) && (aBoardArray[1] == boardArray[v][1]))) //excludes self as path
                            addToPath(path, aBoardArray[0], aBoardArray[1]);
                }
                break;
            case 5: //Own Pawn
                for (int[] aBoardArray : boardArray) {
                    if (((boardArray[v][0] == 7) && (aBoardArray[0] == (boardArray[v][0] - 2)) && (aBoardArray[1] == (boardArray[v][1])))
                            || ((aBoardArray[0] == (boardArray[v][0] - 1)) && (Math.abs(aBoardArray[1] - (boardArray[v][1])) <= 1)))
                        addToPath(path, aBoardArray[0], aBoardArray[1]);
                }
                break;
            case 6: //Jude's Pawn
                for (int[] aBoardArray : boardArray) {
                    if (((boardArray[v][0] == 2) && (aBoardArray[0] == (boardArray[v][0] + 2)) && (aBoardArray[1] == (boardArray[v][1])))
                            || ((aBoardArray[0] == (boardArray[v][0] + 1)) && (Math.abs(aBoardArray[1] - (boardArray[v][1])) <= 1)))
                        addToPath(path, aBoardArray[0], aBoardArray[1]);
                }
                break;
        }
        return path;
    }

    private void addToPath(StringBuffer path, int i, int j) { //this converts selected spot to string and adds it to a StringBuffer
        path.append("..");
        path.append(i);
        path.append(j);
        path.append(".");
        path.append("000");
    }

    void rotateGame() { // method to turn board
        // this amounts essentially to turning over the boardArray
        // first create tempArray
        int[] tempArray = new int[64]; // I choose this over creating another array[64][5]
        for(int i = 0; i < 64; i++) {
            tempArray[i] = boardArray[63 - i][4];
            if((tempArray[i] > 20) && (tempArray[i] < 80)) { //do this just for the pawns, to enable en passant. not doing it for rooks will not affect castling
                switch (tempArray[i] % 10) { // I am ashamed of what I am doing here. There has to be a formula..
                    case 1: tempArray[i] = tempArray[i] + 7; break;
                    case 2: tempArray[i] = tempArray[i] + 5; break;
                    case 3: tempArray[i] = tempArray[i] + 3; break;
                    case 4: tempArray[i] = tempArray[i] + 1; break;
                    case 5: tempArray[i] = tempArray[i] - 1; break;
                    case 6: tempArray[i] = tempArray[i] - 3; break;
                    case 7: tempArray[i] = tempArray[i] - 5; break;
                    case 8: tempArray[i] = tempArray[i] - 7; break;
                }
            }
        }
        //..assign
        for(int i = 0; i < 64; i++) {
            boardArray[i][4] = tempArray[i];
        }

        //..turn turns and colours
        localWhite = !localWhite;
        isLocalTurn = localWhite;

        //RECREATE THE PATHSARRAY!!. I DONT LIKE THIS ONE BIT!!!
        // (i have to do this because of one portion of the pathsarray creation code, the one about castling. there the localWhite of the game is required. so here make sure to reset the local white before recreating pathsarray)
        // TODO if possible avoid this recreation
        for(int e = 0; e < pathsArray.length; e++) {
            for(int i = 0; i < 7; i++) {
                pathsArray[e][i] = findPath(e, i);
            }
        }
    }

    void rotateBoard() {
        rotateGame();
        isLocalTurn = true;
        lastSource = 63 - lastSource;
        lastDestination = 63 - lastDestination;
    }


    //------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------- METHODS THAT UPDATE BOARDARRAY AND PATHSARRAY DURING PLAY --------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------


    private void makeMove(int source, int destination) {
        boardArray[destination][4] = boardArray[source][4];
        boardArray[source][4] = 0;
    }

    void playMove(String move, int typeOfMove) {
        int moveInt = Integer.parseInt(move);
        //take care of castling moves if it is the chosen move
        if (moveInt == 1500) moveInt = 1517;
        if (moveInt == 1501) moveInt = 1513;
        if (moveInt == 1400) moveInt = 1412;
        if (moveInt == 1401) moveInt = 1416;
        final int source = (((moveInt / 1000) - 1) * 8) + (((moveInt / 100) % 10) - 1);
        final int destination = ((((moveInt / 10) % 10) - 1) * 8) + ((moveInt % 10) - 1);
        playMove(source, destination, typeOfMove);
    }

    boolean playMove(int source, int destination, int typeOfMove) { //makes a move by altering the boardArray
        boolean promo = false; //this variable is for when move involves pawn promotion

        String capturedPiece = Integer.toString(boardArray[destination][4]);
        String[] move = new String[3];
        move[0] = "" + boardArray[source][0] + boardArray[source][1] + boardArray[destination][0] + boardArray[destination][1];
        move[1] = capturedPiece;
        move[2] = "" + typeOfMove; //0 is a board-rotation move; 1 is a local move; 2 is a jude move; 3 is a latent move (like in en passant, promotion and castling)

        switch (boardArray[source][4]){
            default:
                makeMove(source, destination);
                break;
            case 15: // black king can castle
                can11Castle++;
                can18Castle++;
                switch (move[0]) {
                    default:
                        makeMove(source, destination);
                        break;
                    // own black castling
                    case "8482":
                        playMove(56, 58, 3);
                        makeMove(source, destination);
                        break;
                    case "8486":
                        playMove(63, 60, 3);
                        makeMove(source, destination);
                        break;
                    // jude's black castling
                    case "1513":
                        playMove(0, 3, 3);
                        makeMove(source, destination);
                        break;
                    case "1517":
                        playMove(7, 5, 3);
                        makeMove(source, destination);
                        break;
                }
                break;
            case 85: // white king can castle
                can81Castle++;
                can88Castle++;
                switch (move[0]) {
                    default:
                        makeMove(source, destination);
                        break;
                    // own white castling
                    case "8583":
                        playMove(56, 59, 3);
                        makeMove(source, destination);
                        break;
                    case "8587":
                        playMove(63, 61, 3);
                        makeMove(source, destination);
                        break;
                    // jude's white castling
                    case "1412":
                        playMove(0, 2, 3);
                        makeMove(source, destination);
                        break;
                    case "1416":
                        playMove(7, 4, 3);
                        makeMove(source, destination);
                        break;
                }
                break;
            case 11: makeMove(source, destination); can11Castle++; break;
            case 18: makeMove(source, destination); can18Castle++; break;
            case 81: makeMove(source, destination); can81Castle++; break;
            case 88: makeMove(source, destination); can88Castle++; break;
            case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: //white pawns who can do en passant, and can be promoted
                if(localWhite) {
                    if (((source - destination == 7) || (source - destination == 9)) && boardArray[destination][4] == 0) { //this is true if the pawn move is en passant
                        int enPassedPawn;
                        if (source - destination == 9) enPassedPawn = source - 1;
                        else enPassedPawn = source + 1;
                        playMove(enPassedPawn, destination, 3);
                        move[1] = Integer.toString(boardArray[destination][4]); //it is pertinent to include this line here because the destination has since changed since the previous line
                    }
                    if(boardArray[destination][0] == 1) { //this means promotion
                        move[1] = boardArray[source][4] + move[1];
                        makeMove(source, destination);
                        promo = true;
                    } else {
                        makeMove(source, destination); //here is where move is made for all ordinary pawn moves, and main en passant
                    }

                } else { //this is actually for jude
                    if (((destination - source == 7) || (destination - source == 9)) && boardArray[destination][4] == 0) { //this is true if the pawn move is en passant
                        int enPassedPawn;
                        if(destination - source == 9) enPassedPawn = source + 1;
                        else enPassedPawn = source - 1;
                        playMove(enPassedPawn, destination, 3);
                        move[1] = Integer.toString(boardArray[destination][4]); //it is pertinent to include this line here because the destination has since changed since the previous line
                    }
                    if(boardArray[destination][0] == 8) { //this means promotion
                        move[1] = boardArray[source][4] + move[1];
                        makeMove(source, destination);
                        if(gameMode != Constants.GAME_MODE_MULTI_BLUETOOTH) boardArray[destination][4] = 84; //Because of this, default jude's promotion will always be to a queen
                    } else {
                        makeMove(source, destination); //here is where move is made for all ordinary pawn moves
                    }
                }

                break;
            case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: //black pawns who can do en passant, and can be promoted
                if(localWhite) { //this is actually for jude
                    if (((destination - source == 7) || (destination - source == 9)) && boardArray[destination][4] == 0) { //this is true if the pawn move is en passant
                        int enPassedPawn;
                        if(destination - source == 9) enPassedPawn = source + 1;
                        else enPassedPawn = source - 1;
                        playMove(enPassedPawn, destination, 3);
                        move[1] = Integer.toString(boardArray[destination][4]); //it is pertinent to include this line here because the destination has since changed since the previous line
                    }
                    if(boardArray[destination][0] == 8) { //this means promotion
                        move[1] = boardArray[source][4] + move[1];
                        makeMove(source, destination);
                        if(gameMode != Constants.GAME_MODE_MULTI_BLUETOOTH) boardArray[destination][4] = 14; //no choice given..Because of this, default jude's promotion will always be to a queen
                    } else {
                        makeMove(source, destination);
                    }
                } else {
                    if (((source - destination == 7) || (source - destination == 9)) && boardArray[destination][4] == 0) { //this is true if the pawn move is en passant
                        int enPassedPawn;
                        if(source - destination == 9) enPassedPawn = source - 1;
                        else enPassedPawn = source + 1;
                        playMove(enPassedPawn, destination, 3);
                        move[1] = Integer.toString(boardArray[destination][4]); //it is pertinent to include this line here because the destination has since changed since the previous line
                    }
                    if(boardArray[destination][0] == 1) { //this means promotion
                        move[1] = boardArray[source][4] + move[1];
                        makeMove(source, destination);
                        promo = true;
                    } else {
                        makeMove(source, destination); //here is where move is made for all ordinary pawn moves, and main en passant
                    }
                }

                break;
        }
        movesHistory.add(move);
        lastSource = source;
        lastDestination = destination;
        return promo;
    }

    private void backMove(int source, int destination) {

        switch (boardArray[source][4]){
            default:
                makeMove(source, destination);
                break;
            case 15: // black king can castle
                can11Castle--;
                can18Castle--;
                makeMove(source, destination);
                break;
            case 85: // white king can castle
                can81Castle--;
                can88Castle--;
                makeMove(source, destination);
                break;
            case 11: makeMove(source, destination); can11Castle--; break;
            case 18: makeMove(source, destination); can18Castle--; break;
            case 81: makeMove(source, destination); can81Castle--; break;
            case 88: makeMove(source, destination); can88Castle--; break;

        }
    }

    void undoLastMove(boolean localUndoRequest) {

        if(movesHistory.size() <= 0) return;

        int moveNumber = movesHistory.size() - 1; //index starts from 0
        String[] lastMove = movesHistory.get(moveNumber);
        movesHistory.remove(moveNumber);

        if(lastMove[0].equals("0000")) { // which is not possible?
            rotateBoard();
        } else {
            int lastMoveInt = Integer.parseInt(lastMove[0]);
            //now get the reverse move. remember that now the first place is the destination
            int destination = (((lastMoveInt / 1000) - 1) * 8) + (((lastMoveInt / 100) % 10) - 1);
            int source = ((((lastMoveInt / 10) % 10) - 1) * 8) + ((lastMoveInt % 10) - 1);

            backMove(source, destination); //undo backend
            if (lastMove[1].length() < 3) boardArray[source][4] = Integer.parseInt(lastMove[1]);
            else { //takes care of promotion capture rewinding..
                boardArray[destination][4] = Integer.parseInt(lastMove[1].substring(0, 2));
                boardArray[source][4] = Integer.parseInt(lastMove[1].substring(2));
            }
        }

        if(moveNumber > 0) {
            lastMove = movesHistory.get(moveNumber - 1);

            //===============================
            if(gameMode == Constants.GAME_MODE_MULTI_LOCAL) { //meaning that the last move was a rotation. U have to jump to previous move, and then u have to compile the move ie x = 63 - x
                int lastMoveInt = Integer.parseInt(lastMove[0]);
                lastSource = 63 - ((((lastMoveInt / 1000) - 1) * 8) + (((lastMoveInt / 100) % 10) - 1));
                lastDestination = 63 - (((((lastMoveInt / 10) % 10) - 1) * 8) + ((lastMoveInt % 10) - 1));
            } else {
                int lastMoveInt = Integer.parseInt(lastMove[0]);
                lastSource = (((lastMoveInt / 1000) - 1) * 8) + (((lastMoveInt / 100) % 10) - 1);
                lastDestination = ((((lastMoveInt / 10) % 10) - 1) * 8) + ((lastMoveInt % 10) - 1);
            }
            //=====================

            if(((localUndoRequest && (lastMove[2].equals("1") || lastMove[2].equals("3"))) ||
                    (!localUndoRequest && (lastMove[2].equals("2") || lastMove[2].equals("3")))) &&
                            gameMode != Constants.GAME_MODE_MULTI_LOCAL)
                undoLastMove(localUndoRequest); //undo up till the last accepted move of jude

        } else {
            lastSource = -1;
            lastDestination = -1;
        }

    }

    void undoLastMultiplayMove() {
        int moveNumber = movesHistory.size() - 1; //index starts from 0
        String[] lastMove = movesHistory.get(moveNumber);
        movesHistory.remove(moveNumber);
        rotateBoard();
        int lastMoveInt = Integer.parseInt(lastMove[0]);
        //now get the reverse move. remember the first place is the destination
        int destination = (((lastMoveInt / 1000) - 1) * 8) + (((lastMoveInt / 100) % 10) - 1);
        int source = ((((lastMoveInt / 10) % 10) - 1) * 8) + ((lastMoveInt % 10) - 1);

        backMove(source, destination); //undo backend
        if (lastMove[1].length() < 3) boardArray[source][4] = Integer.parseInt(lastMove[1]);
        else { //takes care of promotion capture rewinding..
            boardArray[destination][4] = Integer.parseInt(lastMove[1].substring(0, 2));
            boardArray[source][4] = Integer.parseInt(lastMove[1].substring(2));
        }

        if(moveNumber > 0) {
            lastMove = movesHistory.get(moveNumber - 1);
            if(lastMove[2].equals("3")) {
                rotateBoard(); //in anticipation of another rotateBoard()... re-rotate, un-rotate
                undoLastMultiplayMove();
            } else {
                lastMoveInt = Integer.parseInt(lastMove[0]);
                lastSource = 63 - ((((lastMoveInt / 1000) - 1) * 8) + (((lastMoveInt / 100) % 10) - 1));
                lastDestination = 63 - (((((lastMoveInt / 10) % 10) - 1) * 8) + ((lastMoveInt % 10) - 1));
            }

        } else {
            lastSource = -1;
            lastDestination = -1;
        }
    }


    void repair() {
        whiteOnCheck = false;
        blackOnCheck = false;
        for(int f = 0; f < 64; f++) {
            for(int i = 0; i < 7; i++) {
                if(boardArray[f][4] != 0) {
                    pathsArray[f][i] = repairPath(pathsArray[f][i], f, i);
                }
            }
        }
    }

    public boolean clearMove(String moveToClear, boolean ownMove) {
        boolean moveIsClear = true;

        //plan is to check if the intending source or destination is a direct path of the flag
        //if it is then make move, repair the particular path, with the check4check involved, then undo

        final String source = moveToClear.substring(0, 2);
        final String dest = moveToClear.substring(2, 4);

        int flag;
        int x;
        if(ownMove) { //ie own move
            if(localWhite) flag = whiteFlag; else flag = blackFlag;
            x = 1;
        } else {
            if(localWhite) flag = blackFlag; else flag = whiteFlag;
            x = 2;
        }

        playMove(moveToClear, x);

        for(int i = 0; i < 7; i++) {
            StringBuffer flagPath = pathsArray[flag][i];
            for(int g = 2; g < flagPath.length(); g = g + 8) {
                String roe = flagPath.substring(g, (g + 2));
                String rio = flagPath.substring((g + 3), (g + 4));
                if(rio.equals("0") &&
                        ((roe.equals(source)) || (roe.equals(dest)))) {
                    repairPath(flagPath, flag, i);
                    //repair();
                    if(flag == whiteFlag) if(!whiteOnCheck) {undoLastMove(ownMove); return true;}
                    if(flag == blackFlag) if(!blackOnCheck) {undoLastMove(ownMove); return true;}
                }
            }
        }

        if(flag == whiteFlag) if(whiteOnCheck) {undoLastMove(ownMove); return false;}
        if(flag == blackFlag) if(blackOnCheck) {undoLastMove(ownMove); return false;}

        undoLastMove(ownMove);
        return true;
    }


    private StringBuffer unRepairPath(StringBuffer path) {
        for(int g = 2; g < path.length(); g = g + 8) {

            path.setCharAt((g + 3), '0'); //default for unoccupied place, unless there is a blocking place
            path.setCharAt((g + 4), '0'); //default for unoccupied place
            path.setCharAt((g + 5), '0'); //default for unoccupied place
        }
        return path;
    }

    private void checkForCheck(StringBuffer path, int f, int i) {
        for(int g = 2; g < path.length(); g = g + 8) {

            int gel = Integer.parseInt(path.substring(g, (g + 2)));

            if (gel > 10) {
                int c = (((gel / 10) - 1) * 8) + ((gel % 10) - 1);

                if (boardArray[c][4] == 15 && boardArray[f][4] > 50) { //if there is a black king at stake and a white chessman is actually available
                    int p;
                    switch (boardArray[f][4]) {
                        case 85: p = 0; break;
                        case 84: p = 1; break;
                        case 83:case 86: p = 2; break;
                        case 82:case 87: p = 3; break;
                        case 88:case 81: p = 4; break;
                        case 71:case 72:case 73:case 74:case 75:case 76:case 77:case 78:
                            if (localWhite) p = 5; else p = 6; break;
                        default: p = 0;
                    }
                    if (p == i && path.substring((g + 3), (g + 4)).equals("0") /*direct attack line*/) /*confam!! :)*/
                        blackOnCheck = true;
                }

                if (boardArray[c][4] == 85 && boardArray[f][4] > 10 && boardArray[f][4] < 50) { //if there is a white king at stake and a black chessman is actually available
                    int p;
                    switch (boardArray[f][4]) {
                        case 15: p = 0; break;
                        case 14: p = 1; break;
                        case 13:case 16: p = 2; break;
                        case 12:case 17: p = 3; break;
                        case 18:case 11: p = 4; break;
                        case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28:
                            if (localWhite) p = 6;else p = 5; break;
                        default: p = 0;
                    }
                    if (p == i && path.substring((g + 3), (g + 4)).equals("0") /*direct attack line*/) /*confam!! :)*/
                        whiteOnCheck = true;
                }
            }
        }
    }

    private StringBuffer repairPath(StringBuffer path, int f, int i) {
        //first of all un-repair
        path = unRepairPath(path);

        for(int g = 2; g < path.length(); g = g + 8) {
            int gel = Integer.parseInt(path.substring(g, (g + 2)));

            if(gel > 10) {
                int c = (((gel / 10) - 1) * 8) + ((gel % 10) - 1);

                if (boardArray[c][4] != 0) { //if a place in the path is occupied..

                    //-----------------------------------setting black/white occupant: 1/8 --------------------------------
                    if (boardArray[c][4] > 50) path.setCharAt((g + 5), '8');
                    else path.setCharAt((g + 5), '1');
                    //------------------------------------ end of setting black/white -----------------------------------------

                    //---------------------------------- setting type of chessman ---------------------------------------
                    switch (boardArray[c][4]) {
                        case 85:case 15: //king
                            path.setCharAt((g + 4), 'K');
                            break;
                        case 84:case 14: //queen
                            path.setCharAt((g + 4), 'Q');
                            break;
                        case 88:case 81:case 18:case 11://rooks
                            path.setCharAt((g + 4), 'R');
                            break;
                        case 86:case 83:case 16:case 13: //bishops
                            path.setCharAt((g + 4), 'B');
                            break;
                        case 87:case 82:case 17:case 12: //knights
                            path.setCharAt((g + 4), 'H');
                            break;
                        case 71:case 72:case 73:case 74:case 75:case 76:case 77:case 78:
                        case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28: //pawns
                            path.setCharAt((g + 4), 'P');
                            break;
                    }
                    //------------------------------------ end of setting type of chessman ---------------------------------------

                    //---------------------------------------------- hops setting -----------------------------------------------
                    if ((i == 1) || (i == 4)) { //queens and rooks
                        if ((boardArray[c][0] == boardArray[f][0]) && (boardArray[c][1] < boardArray[f][1])) {
                            for (int h = 2; h < path.length(); h = h + 8) { //for all places on the path
                                if ((Integer.parseInt(path.substring(h, (h + 1))) == boardArray[f][0]) && (Integer.parseInt(path.substring((h + 1), (h + 2))) < boardArray[c][1])) { //if this occupied place is blocking it
                                    path.setCharAt((h + 3), (Integer.toString(Integer.parseInt(path.substring(h + 3, (h + 4))) + 1)).charAt(0)); //go three paces into the path, collect the char u see, convert to int, add one, convert to char and replace. one additional hop to get there
                                }
                            }
                        }
                        if ((boardArray[c][0] == boardArray[f][0]) && (boardArray[c][1] > boardArray[f][1])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if ((Integer.parseInt(path.substring(h, (h + 1))) == boardArray[f][0]) && (Integer.parseInt(path.substring((h + 1), (h + 2))) > boardArray[c][1])) {
                                    path.setCharAt((h + 3), (Integer.toString(Integer.parseInt(path.substring(h + 3, (h + 4))) + 1)).charAt(0));
                                }
                            }
                        }
                        if ((boardArray[c][1] == boardArray[f][1]) && (boardArray[c][0] < boardArray[f][0])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if ((Integer.parseInt(path.substring((h + 1), (h + 2))) == boardArray[f][1]) && (Integer.parseInt(path.substring(h, (h + 1))) < boardArray[c][0])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                        if ((boardArray[c][1] == boardArray[f][1]) && (boardArray[c][0] > boardArray[f][0])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if ((Integer.parseInt(path.substring((h + 1), (h + 2))) == boardArray[f][1]) && (Integer.parseInt(path.substring(h, (h + 1))) > boardArray[c][0])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                    }
                    if ((i == 1) || (i == 2)) { //queens and bishops
                        if ((boardArray[c][2] == boardArray[f][2]) && (boardArray[c][3] < boardArray[f][3])) {
                            for (int h = 2; h < path.length(); h = h + 8) { //for all places on the path
                                if (((Integer.parseInt(path.substring(h, (h + 1))) + Integer.parseInt(path.substring((h + 1), (h + 2)))) == boardArray[f][2]) && ((Integer.parseInt(path.substring(h, (h + 1))) - Integer.parseInt(path.substring((h + 1), (h + 2)))) < boardArray[c][3])) { //if this occupied place is blocking it
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10)); //go three paces into the path, collect the char u see, convert to int, add one, convert to char and replace.
                                }
                            }
                        }
                        if ((boardArray[c][2] == boardArray[f][2]) && (boardArray[c][3] > boardArray[f][3])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if (((Integer.parseInt(path.substring(h, (h + 1))) + Integer.parseInt(path.substring((h + 1), (h + 2)))) == boardArray[f][2]) && ((Integer.parseInt(path.substring(h, (h + 1))) - Integer.parseInt(path.substring((h + 1), (h + 2)))) > boardArray[c][3])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                        if ((boardArray[c][3] == boardArray[f][3]) && (boardArray[c][2] < boardArray[f][2])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if (((Integer.parseInt(path.substring(h, (h + 1))) - Integer.parseInt(path.substring((h + 1), (h + 2)))) == boardArray[f][3]) && ((Integer.parseInt(path.substring(h, (h + 1))) + Integer.parseInt(path.substring((h + 1), (h + 2)))) < boardArray[c][2])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                        if ((boardArray[c][3] == boardArray[f][3]) && (boardArray[c][2] > boardArray[f][2])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if (((Integer.parseInt(path.substring(h, (h + 1))) - Integer.parseInt(path.substring((h + 1), (h + 2)))) == boardArray[f][3]) && ((Integer.parseInt(path.substring(h, (h + 1))) + Integer.parseInt(path.substring((h + 1), (h + 2)))) > boardArray[c][2])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                    }
                    if ((i == 5) || (i == 6)) {
                        if ((boardArray[c][1] == boardArray[f][1]) && (boardArray[c][0] < boardArray[f][0])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if ((Integer.parseInt(path.substring((h + 1), (h + 2))) == boardArray[f][1]) && (Integer.parseInt(path.substring(h, (h + 1))) <= boardArray[c][0])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                        if ((boardArray[c][1] == boardArray[f][1]) && (boardArray[c][0] > boardArray[f][0])) {
                            for (int h = 2; h < path.length(); h = h + 8) {
                                if ((Integer.parseInt(path.substring((h + 1), (h + 2))) == boardArray[f][1]) && (Integer.parseInt(path.substring(h, (h + 1))) >= boardArray[c][0])) {
                                    path.setCharAt((h + 3), Character.forDigit((Integer.parseInt(path.substring(h + 3, (h + 4))) + 1), 10));
                                }
                            }
                        }
                        if ((boardArray[c][2] == boardArray[f][2]) || (boardArray[c][3] == boardArray[f][3])) {
                            path.setCharAt((g + 3), '0');
                        }
                    }
                    //----------------------------------------- partial end of hops setting -------------------------------------------

                } else {
                    //prevent pawn from moving diagonal when there is no enemy to capture
                    if ((i == 5) || (i == 6)) {
                        if ((boardArray[c][2] == boardArray[f][2]) || (boardArray[c][3] == boardArray[f][3])) {
                            path.setCharAt((g + 3), '1');
                        }
                    }
                    //en passant
                    if (i == 5 && (movesHistory.size() > 1)) {
                        if((boardArray[c][2] == boardArray[f][2]) || (boardArray[c][3] == boardArray[f][3])) {
                            int peer = boardArray[((boardArray[f][0] - 1) * 8) + (boardArray[c][1] - 1)][4];
                            if(((localWhite && ((peer > 20) && (peer < 30))) || (!localWhite && ((peer > 70) && (peer < 80)))) && (Math.abs((peer % 10) - boardArray[f][1]) == 1)) {
                                if(lastSource == (c - 8) && lastDestination == (c + 8)) path.setCharAt((g + 3), '0');
                            }
                        }
                    }
                    if (i == 6 && (movesHistory.size() > 1)) {
                        if((boardArray[c][2] == boardArray[f][2]) || (boardArray[c][3] == boardArray[f][3])) {
                            int peer = boardArray[((boardArray[f][0] - 1) * 8) + (boardArray[c][1] - 1)][4];
                            if(((localWhite && ((peer > 70) && (peer < 80))) || (!localWhite && ((peer > 20) && (peer < 30)))) && (Math.abs((peer % 10) - boardArray[f][1]) == 1)) {
                                if(lastSource == (c + 8) && lastDestination == (c - 8)) path.setCharAt((g + 3), '0');
                            }
                        }
                    }

                }
                //----------------------------------------- full end of hops setting -------------------------------------------------------

            } else { //for special moves like castling
                if(localWhite) {
                    if (gel == 0) { //ie kings side castling
                        if (boardArray[f + 1][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f + 2][4] != 0) path.setCharAt((g + 3), '1');
                    }
                    if (gel == 1) { //ie queens side castling
                        if (boardArray[f - 3][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f - 2][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f - 1][4] != 0) path.setCharAt((g + 3), '1');
                    }
                } else {
                    if (gel == 0) { //ie kings side castling
                        if (boardArray[f - 2][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f - 1][4] != 0) path.setCharAt((g + 3), '1');
                    }
                    if (gel == 1) { //ie queens side castling
                        if (boardArray[f + 1][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f + 2][4] != 0) path.setCharAt((g + 3), '1');
                        if (boardArray[f + 3][4] != 0) path.setCharAt((g + 3), '1');
                    }
                }
            }
        }

        checkForCheck(path, f, i);

        return path;
    } //end of method repairPath


    //------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------- END OF METHODS THAT UPDATE BOARDARRAY AND PATHSARRAY DURING PLAY ------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------








    //------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------- METHODS THAT READ BOARDARRAY AND PATHSARRAY DURING RUN ------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------

    StringBuffer getAllMoves(boolean ownMoves, boolean whiteMoves) { //this reads the pathsArray and returns all the possible moves
        StringBuffer possibleMoves = new StringBuffer();
        if(whiteMoves) { //getting white moves
            for(int m = 0; m < 64; m++) {
                if(boardArray[m][4] > 50) {
                    int p;
                    switch(boardArray[m][4]){
                        case 85: p = 0; break;
                        case 84: p = 1; break;
                        case 83: case 86: p = 2; break;
                        case 82: case 87: p = 3; break;
                        case 88: case 81: p = 4; break;
                        case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78:
                            if(ownMoves) p = 5; else p = 6; break;
                        default: p = 0;
                    }
                    for(int r = 2; r < pathsArray[m][p].length(); r = r + 8) {
                        if((pathsArray[m][p].charAt(r) != '0') && //for non-special cases
                                (pathsArray[m][p].charAt(r + 3) == '0') && ((pathsArray[m][p].charAt(r + 5) == '0') || (pathsArray[m][p].charAt(r + 5) == '1'))) { //if hop is 0 and the occupant is empty or white
                            addToAllMoves(possibleMoves, boardArray[m][0], boardArray[m][1], pathsArray[m][p].charAt(r), pathsArray[m][p].charAt(r + 1));
                        }
                        if((pathsArray[m][p].charAt(r) == '0') && //for special cases
                                (!whiteOnCheck) &&
                                ((ownMoves && ((pathsArray[m][p].charAt(r + 1) == '0' && can88Castle == 0) || (pathsArray[m][p].charAt(r + 1) == '1' && can81Castle == 0))) ||
                                (!ownMoves && ((pathsArray[m][p].charAt(r + 1) == '0' && can81Castle == 0) || (pathsArray[m][p].charAt(r + 1) == '1' && can88Castle == 0)))) &&
                                (pathsArray[m][p].charAt(r + 3) == '0') && (pathsArray[m][p].charAt(r + 4) == '0') && (pathsArray[m][p].charAt(r + 5) == '0')) {
                            addToAllMoves(possibleMoves, boardArray[m][0], boardArray[m][1], pathsArray[m][p].charAt(r), pathsArray[m][p].charAt(r + 1));
                        }
                    }
                }
            }
        } else { //for black moves
            for(int m = 0; m < 64; m++) {
                if(boardArray[m][4] > 10 && boardArray[m][4] < 50) {
                    int p;
                    switch(boardArray[m][4]){
                        case 15: p = 0; break;
                        case 14: p = 1; break;
                        case 13: case 16: p = 2; break;
                        case 12: case 17: p = 3; break;
                        case 18: case 11: p = 4; break;
                        case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28:
                            if(ownMoves) p = 5; else p = 6; break;
                        default: p = 0;
                    }
                    for(int r = 2; r < pathsArray[m][p].length(); r = r + 8) {
                        if((pathsArray[m][p].charAt(r) != '0') && //for non-special cases
                                (pathsArray[m][p].charAt(r + 3) == '0') && ((pathsArray[m][p].charAt(r + 5) == '0') || (pathsArray[m][p].charAt(r + 5) == '8'))) { //if hop is 0 and the occupant is empty or black
                            addToAllMoves(possibleMoves, boardArray[m][0], boardArray[m][1], pathsArray[m][p].charAt(r), pathsArray[m][p].charAt(r + 1)); // add as move
                        }
                        if((pathsArray[m][p].charAt(r) == '0') && //for special cases
                                (!blackOnCheck) &&
                                ((ownMoves && ((pathsArray[m][p].charAt(r + 1) == '0' && can11Castle == 0) || (pathsArray[m][p].charAt(r + 1) == '1' && can18Castle == 0))) ||
                                (!ownMoves && ((pathsArray[m][p].charAt(r + 1) == '0' && can18Castle == 0) || (pathsArray[m][p].charAt(r + 1) == '1' && can11Castle == 0)))) &&
                                (pathsArray[m][p].charAt(r + 3) == '0') && (pathsArray[m][p].charAt(r + 4) == '0') && (pathsArray[m][p].charAt(r + 5) == '0')) {
                            addToAllMoves(possibleMoves, boardArray[m][0], boardArray[m][1], pathsArray[m][p].charAt(r), pathsArray[m][p].charAt(r + 1));
                        }
                    }
                }
            }
        }
        return possibleMoves;

    } //end of method getAllMoves

    private void addToAllMoves(StringBuffer moves, int i, int j, char k, char l) { //this converts selected move to string and adds it to a StringBuffer
        moves.append(i);
        moves.append(j);
        moves.append(k);
        moves.append(l);
        moves.append(".");
    }



    String chooseRandomMove(StringBuffer moveList) { //this method selects a move randomly
        String myMove = "";
        Random random = new Random();
        int choice = 5 * random.nextInt(moveList.length() / 5);
        myMove = moveList.substring(choice, (choice + 4));
        if(myMove.substring(2, 3).equals("0")) { //this method effectively prevents any attempt of the enemy (grandpa) to castle
            chooseRandomMove(moveList);
        }
        return myMove;
    }


}