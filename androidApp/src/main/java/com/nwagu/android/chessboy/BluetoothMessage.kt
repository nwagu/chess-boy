package com.nwagu.android.chessboy

import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.moves.*

sealed class BluetoothMessage {

    abstract val value: String

    class SyncRequest(val position: String): BluetoothMessage() {
        override val value: String
            get() {
                return "SYNC-$position"
            }
    }

    object SyncOk: BluetoothMessage() {
        override val value: String
            get() = "SYNC_OK"
    }

    object SyncFailed: BluetoothMessage() {
        override val value: String
            get() = "SYNC_FAILED"
    }

    class MoveMessage(val move: Move): BluetoothMessage() {
        override val value: String
            get() {
                return when(move) {
                    is Castling -> "MOVE-CASTLING-${move.source}-${move.destination}-${move.secondarySource}-${move.secondaryDestination}"
                    is EnPassant -> "MOVE-ENPASSANT-${move.source}-${move.destination}"
                    is Promotion -> "MOVE-PROMOTION-${move.source}-${move.destination}-${move.promotionType.name}"
                    is RegularMove -> "MOVE-REGULAR-${move.source}-${move.destination}"
                }
            }
    }

    object MoveOk: BluetoothMessage() {
        override val value: String
            get() = "MOVE_OK"
    }

    object MoveFailed: BluetoothMessage() {
        override val value: String
            get() = "MOVE_FAIL"
    }

    object DrawRequest: BluetoothMessage() {
        override val value: String
            get() = "DRAW"
    }

    object DrawAccepted: BluetoothMessage() {
        override val value: String
            get() = "DRAW_ACCEPT"
    }

    object DrawRejected: BluetoothMessage() {
        override val value: String
            get() = "DRAW_REJECT"
    }

    object UndoRequest: BluetoothMessage() {
        override val value: String
            get() = "UNDO"
    }

    object UndoAccepted: BluetoothMessage() {
        override val value: String
            get() = "UNDO_ACCEPT"
    }

    object UndoRejected: BluetoothMessage() {
        override val value: String
            get() = "UNDO_REJECT"
    }
}

fun parseMessage(message: String): BluetoothMessage? {
    when {
        message.take(5) == "MOVE-" -> {
            val messageSplit = message.split("-")
            when(messageSplit[1]) {
                "REGULAR" -> {
                    return BluetoothMessage.MoveMessage(
                        RegularMove(
                            source = messageSplit[2].toInt(),
                            destination = messageSplit[3].toInt()))
                }
                "CASTLING" -> {
                    return BluetoothMessage.MoveMessage(
                        Castling(
                            source = messageSplit[2].toInt(),
                            destination = messageSplit[3].toInt(),
                            secondarySource = messageSplit[4].toInt(),
                            secondaryDestination = messageSplit[5].toInt()))
                }
                "PROMOTION" -> {
                    return BluetoothMessage.MoveMessage(
                        Promotion(
                            source = messageSplit[2].toInt(),
                            destination = messageSplit[3].toInt(),
                            promotionType = ChessPieceType.valueOf(messageSplit[4])))
                }
                "ENPASSANT" -> {
                    return BluetoothMessage.MoveMessage(
                        EnPassant(
                            source = messageSplit[2].toInt(),
                            destination = messageSplit[3].toInt()))
                }
                else -> {
                    // throw IllegalStateException("Illegal message ($message) received!")
                    return null
                }
            }

        }
        message.take(5) == "SYNC-" -> {
            return BluetoothMessage.SyncRequest(message.drop(5))
        }
        message == BluetoothMessage.SyncOk.value -> return BluetoothMessage.SyncOk
        message == BluetoothMessage.SyncFailed.value -> return BluetoothMessage.SyncFailed
        message == BluetoothMessage.MoveOk.value -> return BluetoothMessage.MoveOk
        message == BluetoothMessage.MoveFailed.value -> return BluetoothMessage.MoveFailed
        message == BluetoothMessage.DrawRequest.value -> return BluetoothMessage.DrawRequest
        message == BluetoothMessage.DrawAccepted.value -> return BluetoothMessage.DrawAccepted
        message == BluetoothMessage.DrawRejected.value -> return BluetoothMessage.DrawRejected
        message == BluetoothMessage.UndoRequest.value -> return BluetoothMessage.UndoRequest
        message == BluetoothMessage.UndoAccepted.value -> return BluetoothMessage.UndoAccepted
        message == BluetoothMessage.UndoRejected.value -> return BluetoothMessage.UndoRejected
        else -> {
            // throw IllegalStateException("Illegal message ($message) received!")
            return null
        }
    }
}