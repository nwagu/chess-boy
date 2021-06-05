package com.nwagu.android.chessboy.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.nwagu.android.chessboy.R
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.SquareColor

@DrawableRes
fun ChessPiece.imageRes(): Int {
    return when (chessPieceType) {
        ChessPieceType.QUEEN -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_queen else R.drawable.img_black_queen
        ChessPieceType.KING -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_king else R.drawable.img_black_king
        ChessPieceType.KNIGHT -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_knight else R.drawable.img_black_knight
        ChessPieceType.BISHOP -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_bishop else R.drawable.img_black_bishop
        ChessPieceType.ROOK -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_rook else R.drawable.img_black_rook
        ChessPieceType.PAWN -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_pawn else R.drawable.img_black_pawn
    }
}

fun SquareColor.colorResource(): Color {
    return when (this) {
        SquareColor.WHITE -> AppColor.lightCell
        SquareColor.BLACK -> AppColor.darkCell
    }
}