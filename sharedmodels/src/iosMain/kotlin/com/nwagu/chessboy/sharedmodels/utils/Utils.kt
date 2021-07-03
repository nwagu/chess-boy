package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chess.model.SquareColor

actual typealias ImageRes = String
actual typealias ColorRes = String

fun getChessPieceImage(piece: ChessPiece): ImageRes {
    return piece.imageRes()
}

actual fun ChessPiece.imageRes(): ImageRes {
    return when (chessPieceType) {
        ChessPieceType.QUEEN -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_queen" else "img_black_queen"
        ChessPieceType.KING -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_king" else "img_black_king"
        ChessPieceType.KNIGHT -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_knight" else "img_black_knight"
        ChessPieceType.BISHOP -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_bishop" else "img_black_bishop"
        ChessPieceType.ROOK -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_rook" else "img_black_rook"
        ChessPieceType.PAWN -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_pawn" else "img_black_pawn"
    }
}

actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> "white"
        SquareColor.BLACK -> "black"
    }
}

actual fun getDefaultPlayerAvatar(): ImageRes = "img_avatar_default"
actual fun getBluetoothPlayerAvatar(): ImageRes = "img_avatar_default"
actual fun getRandomMoveGeneratorAvatar(): ImageRes = "img_avatar_random"