package jwtc.chess

import kotlin.jvm.JvmStatic

// Pos - Wrapper class for a position or square on the board
// Provides only static methods
// A position is an integer between 0 and 63 for the 64 squares
// This part of the code is given under Pos to "free up" some lines in ChessBoard.
// In ChessBoard all other positional related stuff is done
object Pos {
    // returns positional value [0-63] for squares [a8-h1]
    // when a position cannot be created a message is sent on console out (co).
    // used to initialize values, no speed needed
	@JvmStatic
	fun fromString(s: String): Int {
        val c = s[0]
        val row = s.substring(1).toInt()
        val col = c.code - 'a'.code
        return (8 - row) * 8 + col
    }

    // returns positional value [0-63] from a column and row.
    // @col the column [0-7] (left to right)
    // @row the row [0-7] (top to bottom)
    // no check for invalid row or col is done for reasons of speed
	@JvmStatic
	fun fromColAndRow(col: Int, row: Int): Int {
        return row * 8 + col
    }

    // returns the row [0-7] from top to bottom; ie values in [a8-h8] return 0.
    fun row(`val`: Int): Int {
        return `val` shr 3 and 7
    }

    //  returns the column [0-7] from left to right; ie values in [a8-a8] return 0.
    fun col(`val`: Int): Int {
        return `val` % 8
    }

    // returns string representation of the value; ie "d5"
    // @val positional value [0-63] - no check on valid range
    fun toString(`val`: Int): String {
        return "" + (col(`val`) + 'a'.code).toChar() + "" + (8 - row(`val`))
    }

    // returns string representation of the row of val - human represented so from bottom to top ["1"-"8"]
    fun rowToString(`val`: Int): String {
        return "" + (8 - row(`val`))
    }

    // returns string representation of the column. ["a"-"h"]
    fun colToString(`val`: Int): String {
        return "" + (col(`val`) + 'a'.code).toChar()
    }
}