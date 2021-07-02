package jwtc.chess

import android.util.Log
import jwtc.chess.Pos.fromColAndRow
import jwtc.chess.Pos.fromString

class JNI {
    fun newGame() {
        reset()
        putPiece(BoardConstants.a8, BoardConstants.ROOK, BoardConstants.BLACK)
        putPiece(BoardConstants.b8, BoardConstants.KNIGHT, BoardConstants.BLACK)
        putPiece(BoardConstants.c8, BoardConstants.BISHOP, BoardConstants.BLACK)
        putPiece(BoardConstants.d8, BoardConstants.QUEEN, BoardConstants.BLACK)
        putPiece(BoardConstants.e8, BoardConstants.KING, BoardConstants.BLACK)
        putPiece(BoardConstants.f8, BoardConstants.BISHOP, BoardConstants.BLACK)
        putPiece(BoardConstants.g8, BoardConstants.KNIGHT, BoardConstants.BLACK)
        putPiece(BoardConstants.h8, BoardConstants.ROOK, BoardConstants.BLACK)
        putPiece(BoardConstants.a7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.b7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.c7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.d7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.e7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.f7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.g7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.h7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.a1, BoardConstants.ROOK, BoardConstants.WHITE)
        putPiece(BoardConstants.b1, BoardConstants.KNIGHT, BoardConstants.WHITE)
        putPiece(BoardConstants.c1, BoardConstants.BISHOP, BoardConstants.WHITE)
        putPiece(BoardConstants.d1, BoardConstants.QUEEN, BoardConstants.WHITE)
        putPiece(BoardConstants.e1, BoardConstants.KING, BoardConstants.WHITE)
        putPiece(BoardConstants.f1, BoardConstants.BISHOP, BoardConstants.WHITE)
        putPiece(BoardConstants.g1, BoardConstants.KNIGHT, BoardConstants.WHITE)
        putPiece(BoardConstants.h1, BoardConstants.ROOK, BoardConstants.WHITE)
        putPiece(BoardConstants.a2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.b2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.c2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.d2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.e2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.f2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.g2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.h2, BoardConstants.PAWN, BoardConstants.WHITE)
        setCastlingsEPAnd50(1, 1, 1, 1, -1, 0)
        commitBoard()
    }

    fun initFEN(sFEN: String): Boolean {
        // rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1
        reset()
        try {
            var s: String
            var pos = 0
            var i = 0
            var iAdd: Int
            while (pos < 64 && i < sFEN.length) {
                iAdd = 1
                s = sFEN.substring(i, i + 1)
                when (s) {
                    "k" -> putPiece(pos, BoardConstants.KING, BoardConstants.BLACK)
                    "K" -> putPiece(pos, BoardConstants.KING, BoardConstants.WHITE)
                    "q" -> putPiece(pos, BoardConstants.QUEEN, BoardConstants.BLACK)
                    "Q" -> putPiece(pos, BoardConstants.QUEEN, BoardConstants.WHITE)
                    "r" -> putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK)
                    "R" -> putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE)
                    "b" -> putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK)
                    "B" -> putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE)
                    "n" -> putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK)
                    "N" -> putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE)
                    "p" -> putPiece(pos, BoardConstants.PAWN, BoardConstants.BLACK)
                    "P" -> putPiece(pos, BoardConstants.PAWN, BoardConstants.WHITE)
                    "/" -> iAdd = 0
                    else -> iAdd = s.toInt()
                }
                pos += iAdd
                i++
            }
            i++ // skip space
            if (i < sFEN.length) {
                var wccl = 0
                var wccs = 0
                var bccl = 0
                var bccs = 0
                val colA = 0
                val colH = 7
                var ep = -1
                var r50 = 0
                var turn: Int
                val arr = sFEN.substring(i).split(" ").toTypedArray()
                if (arr.isNotEmpty()) {
                    turn = if (arr[0] == "w") {
                        BoardConstants.WHITE
                    } else {
                        BoardConstants.BLACK
                    }
                    if (arr.size > 1) {
                        if (arr[1].contains("k")) {
                            bccs = 1
                        }
                        if (arr[1].contains("q")) {
                            bccl = 1
                        }
                        if (arr[1].contains("K")) {
                            wccs = 1
                        }
                        if (arr[1].contains("Q")) {
                            wccl = 1
                        }
                        if (arr.size > 2) {
                            if (arr[2] != "-") {
                                ep = fromString(arr[2])
                            }
                            if (arr.size > 3) {
                                r50 = arr[3].toInt()
                            }
                        }
                        setCastlingsEPAnd50(wccl, wccs, bccl, bccs, ep, r50)
                        setTurn(turn)
                        commitBoard()
                        return true
                    }
                }
            }
        } catch (ex: Exception) {
            //Log.e("initFEN", ex.toString());
            return false
        }
        return false
    }

    protected fun isPosFree(pos: Int): Boolean {
        return pieceAt(BoardConstants.BLACK, pos) == BoardConstants.FIELD &&
                pieceAt(BoardConstants.WHITE, pos) == BoardConstants.FIELD
    }

    protected fun getAvailableCol(colNum: Int): Int {
        var col = 0
        var i = 0
        var pos: Int
        do {
            pos = fromColAndRow(col, 0)
            if (isPosFree(pos)) {
                i++
            }
            col++
        } while (i <= colNum && col < 9)
        col--
        return col
    }

    protected val firstAvailableCol: Int
        protected get() {
            var col = 0
            var pos: Int
            do {
                pos = fromColAndRow(col, 0)
                if (isPosFree(pos)) {
                    return col
                }
                col++
            } while (col < 8)
            return col
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
    fun initRandomFisher(n: Int): Int {
        var n = n
        reset()
        val NN = arrayOf(
            intArrayOf(0, 1),
            intArrayOf(0, 2),
            intArrayOf(0, 3),
            intArrayOf(0, 4),
            intArrayOf(1, 2),
            intArrayOf(1, 3),
            intArrayOf(1, 4),
            intArrayOf(2, 3),
            intArrayOf(2, 4),
            intArrayOf(3, 4)
        )
        val Bw: Int
        val Bb: Int
        val Q: Int
        val N1: Int
        val N2: Int
        var col: Int
        val col2: Int
        var pos: Int
        var ret = 0
        if (n >= 0) {
            Bw = n % 4
            n = Math.floor(n / 4.0).toInt()
            Bb = n % 4
            n = Math.floor(n / 4.0).toInt()
            Q = n % 6
            n = Math.floor(n / 6.0).toInt()
            n = n % 10
            N1 = NN[n][0]
            N2 = NN[n][1]
        } else {
            Bw = (Math.random() * 3).toInt()
            Bb = (Math.random() * 3).toInt()
            Q = (Math.random() * 5).toInt()
            n = (Math.random() * 8).toInt()
            N1 = NN[n][0]
            N2 = NN[n][1]
        }
        ret = 96 * (5 - (3 - N1) * (4 - N1) / 2 + N2) + 16 * Q + 4 * Bb + Bw
        Log.i("Chess960", "Bw $Bw Bb $Bb Q $Q n $n N1 $N1 N2 $N2")
        // white square bishop
        col = 1 + 2 * Bw
        Log.i("Chess960", "Bw col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE)

        // black-square bishop
        col = 2 * Bb
        Log.i("Chess960", "Bb col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.BISHOP, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.BISHOP, BoardConstants.WHITE)

        // queen
        col = getAvailableCol(Q)
        Log.i("Chess960", "Q col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.QUEEN, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.QUEEN, BoardConstants.WHITE)

        // knight 1
        col = getAvailableCol(N1)
        col2 = getAvailableCol(N2)
        Log.i("Chess960", "N1 col $col N2 $col2")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE)

        // knight 2
        pos = fromColAndRow(col2, 0)
        putPiece(pos, BoardConstants.KNIGHT, BoardConstants.BLACK)
        pos = fromColAndRow(col2, 7)
        putPiece(pos, BoardConstants.KNIGHT, BoardConstants.WHITE)

        // ROOK A
        col = firstAvailableCol
        Log.i("Chess960", "R1 col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE)

        // KING
        col = firstAvailableCol
        Log.i("Chess960", "K col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.KING, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.KING, BoardConstants.WHITE)
        // ROOK H
        col = firstAvailableCol
        Log.i("Chess960", "R2 col $col")
        pos = fromColAndRow(col, 0)
        putPiece(pos, BoardConstants.ROOK, BoardConstants.BLACK)
        pos = fromColAndRow(col, 7)
        putPiece(pos, BoardConstants.ROOK, BoardConstants.WHITE)

        //
        putPiece(BoardConstants.a7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.b7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.c7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.d7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.e7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.f7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.g7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.h7, BoardConstants.PAWN, BoardConstants.BLACK)
        putPiece(BoardConstants.a2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.b2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.c2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.d2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.e2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.f2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.g2, BoardConstants.PAWN, BoardConstants.WHITE)
        putPiece(BoardConstants.h2, BoardConstants.PAWN, BoardConstants.WHITE)
        setCastlingsEPAnd50(1, 1, 1, 1, -1, 0)
        commitBoard()
        return ret
    }

    external fun destroy()
    external fun isInited(): Int
    external fun requestMove(from: Int, to: Int): Int
    external fun move(move: Int): Int
    external fun undo()
    external fun reset()
    external fun putPiece(pos: Int, piece: Int, turn: Int)
    external fun searchMove(secs: Int)
    external fun searchDepth(depth: Int)
    external fun getMove(): Int
    external fun getBoardValue(): Int
    external fun peekSearchDone(): Int
    external fun peekSearchBestMove(ply: Int): Int
    external fun peekSearchBestValue(): Int
    external fun peekSearchDepth(): Int
    external fun getEvalCount(): Int
    external fun setPromo(piece: Int)
    external fun getState(): Int
    external fun isEnded(): Int
    external fun setCastlingsEPAnd50(wccl: Int, wccs: Int, bccl: Int, bccs: Int, ep: Int, r50: Int)
    external fun getNumBoard(): Int
    external fun getTurn(): Int
    external fun setTurn(turn: Int)
    external fun commitBoard()
    //public native int[] getMoveArray();
    external fun getMoveArraySize(): Int
    external fun getMoveArrayAt(i: Int): Int
    external fun pieceAt(turn: Int, pos: Int): Int
    external fun getMyMoveToString(): String?
    external fun getMyMove(): Int
    external fun isLegalPosition(): Int
    external fun isAmbiguousCastle(from: Int, to: Int): Int
    external fun doCastleMove(from: Int, to: Int)
    external fun toFEN(): String?
    external fun removePiece(turn: Int, pos: Int)
    external fun getHashKey(): Long
    external fun loadDB(sFile: String?, depth: Int)
    external fun interrupt()
    external fun getNumCaptured(turn: Int, piece: Int): Int

    companion object {
        init {
            System.loadLibrary("jwtc")
        }
    }
}