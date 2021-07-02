package jwtc.chess

// Move. Wrapper class for the integer representation of a move
// The positional values "from" and "to" are shifted into the integer.
// Also en-passant, castling, hit, first pawn move, promotion and promotion piece
// are part of the move.
object Move {
    // mask for a position [0-63], 6 bits.
    private const val MASK_POS = 0x3F

    // mask for a boolean [false=0, true=1], 1 bit
    private const val MASK_BOOL = 1

    // shift values
    private const val SHIFT_TO = 6
    private const val SHIFT_EP = 13

    // short castling OO
    private const val SHIFT_OO = 14

    // long castling OOO
    private const val SHIFT_OOO = 15
    private const val SHIFT_HIT = 16

    // is the first 2 step move of a pawn
    private const val SHIFT_FIRSTPAWN = 17

    // with this move the opponent king is checked
    private const val SHIFT_CHECK = 18

    // a pawn is promoted with this move
    private const val SHIFT_PROMOTION = 19

    // the piece the pawn is promoted to
    private const val SHIFT_PROMOTIONPIECE = 20

    // returns the integer representation of the simpelest move, from
    // position @from to position @to
    fun makeMove(from: Int, to: Int): Int {
        return from or (to shl SHIFT_TO)
    }

    fun makeMoveFirstPawn(from: Int, to: Int): Int {
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_FIRSTPAWN)
    }

    fun makeMoveHit(from: Int, to: Int): Int {
        //co.pl("makeMovehit " + from + "-" + to);
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_HIT)
    }

    fun makeMoveEP(from: Int, to: Int): Int {
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_HIT) or (1 shl SHIFT_EP)
    }

    fun makeMoveOO(from: Int, to: Int): Int {
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_OO)
    }

    fun makeMoveOOO(from: Int, to: Int): Int {
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_OOO)
    }

    fun makeMovePromotion(from: Int, to: Int, piece: Int, bHit: Boolean): Int {
        return from or (to shl SHIFT_TO) or (1 shl SHIFT_PROMOTION) or (piece shl SHIFT_PROMOTIONPIECE) or if (bHit == true) 1 shl SHIFT_HIT else 0
    }

    fun setCheck(move: Int): Int {
        return move or (1 shl SHIFT_CHECK)
    }

    // returns true when "from" and "to" are equal in both arguments
    fun equalPositions(m: Int, m2: Int): Boolean {
        return m and MASK_POS == m2 and MASK_POS && m shr SHIFT_TO and MASK_POS == m2 shr SHIFT_TO and MASK_POS
    }

    // return true when "to" in both arguments are equal
    fun equalTos(m: Int, m2: Int): Boolean {
        return m shr SHIFT_TO and MASK_POS == m2 shr SHIFT_TO and MASK_POS
    }

    // returns "from" of the move
    fun getFrom(move: Int): Int {
        return move and MASK_POS
    }

    fun getTo(move: Int): Int {
        return move shr SHIFT_TO and MASK_POS
    }

    fun isEP(move: Int): Boolean {
        return move shr SHIFT_EP and MASK_BOOL == MASK_BOOL
    }

    fun isOO(move: Int): Boolean {
        return move shr SHIFT_OO and MASK_BOOL == MASK_BOOL
    }

    fun isOOO(move: Int): Boolean {
        return move shr SHIFT_OOO and MASK_BOOL == MASK_BOOL
    }

    fun isHIT(move: Int): Boolean {
        return move shr SHIFT_HIT and MASK_BOOL == MASK_BOOL
    }

    fun isCheck(move: Int): Boolean {
        return move shr SHIFT_CHECK and MASK_BOOL == MASK_BOOL
    }

    fun isFirstPawnMove(move: Int): Boolean {
        return move shr SHIFT_FIRSTPAWN and MASK_BOOL == MASK_BOOL
    }

    fun isPromotionMove(move: Int): Boolean {
        return move shr SHIFT_PROMOTION and MASK_BOOL == MASK_BOOL
    }

    fun getPromotionPiece(move: Int): Int {
        return move shr SHIFT_PROMOTIONPIECE
    }

    // returns pgn alike string representation of the move - not full pgn format because then more information is needed
    fun toDbgString(move: Int): String {
        if (isOO(move)) return "O-O"
        return if (isOOO(move)) "O-O-O" else "[" + Pos.toString(
            getFrom(
                move
            )
        ) + (if (isHIT(move)) "x" else "-") + Pos.toString(
            getTo(
                move
            )
        ) + (if (isCheck(move)) "+" else "") + (if (isEP(move)) " ep" else "") + "]"
    }
}