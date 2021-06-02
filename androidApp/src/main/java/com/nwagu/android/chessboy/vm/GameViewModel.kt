package com.nwagu.android.chessboy.vm

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.BluetoothMessage
import com.nwagu.android.chessboy.movesgenerators.AI
import com.nwagu.android.chessboy.movesgenerators.BluetoothOpponent
import com.nwagu.android.chessboy.movesgenerators.RandomMoveGenerator
import com.nwagu.android.chessboy.movesgenerators.User
import com.nwagu.android.chessboy.parseMessage
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.Game
import com.nwagu.chess.Player
import com.nwagu.chess.board.Square
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.getPossibleMovesFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class GameViewModel(application: Application): AndroidViewModel(application) {

    // TODO get game from last saved game
    var game = Game(
        whitePlayer = User,
        blackPlayer = RandomMoveGenerator()
    )

    val boardUpdated = MutableStateFlow(0)

    var selectedPosition = MutableStateFlow<Square?>(null)
    var possibleMoves = MutableStateFlow<List<Move>>(listOf())

    val bluetoothChatService: BluetoothChatService by lazy { BluetoothChatService() }

    fun startNewGame(
        whitePlayer: Player = User,
        blackPlayer: Player = RandomMoveGenerator()
    ) {

        if (whitePlayer is BluetoothOpponent || blackPlayer is BluetoothOpponent) {
            throw InvalidParameterException("Please use startNewBluetoothGame to start bluetooth game!")
        }

        game = Game(
            whitePlayer = whitePlayer,
            blackPlayer = blackPlayer
        )

        updateBoardUI()
        getNextMove()
    }

    fun startNewBluetoothGame(isWhite: Boolean, address: String?) {

        game = Game(
            whitePlayer = if (isWhite) User else BluetoothOpponent(address = address),
            blackPlayer = if (isWhite) BluetoothOpponent(address = address) else User
        )

        updateBoardUI()

        bluetoothChatService.init(getApplication(), bluetoothChatListener)

        if (isWhite) {
            bluetoothChatService.connectDevice(address!!, true)
        } else {
            bluetoothChatService.startListeningForConnection()
        }

    }

    fun reconnectToBluetoothGame() {
        if (game.blackPlayer is BluetoothOpponent)
            bluetoothChatService.connectDevice((game.blackPlayer as BluetoothOpponent).address!!, true)
        else
            bluetoothChatService.startListeningForConnection()
    }

    fun cellClicked(position: Square) {

        if (!game.isUserTurn) {
            clearPossibleMoves()
            return
        }

        when {
            selectedPosition.value == null || possibleMoves.value.isEmpty() -> {
                selectedPosition.value = position
                possibleMoves.value = game.board.getPossibleMovesFrom(position)
            }
            position in possibleMoves.value.map { move -> move.destination } -> {

                val move = possibleMoves.value.find { move -> position == move.destination }!!

                if (game.whitePlayer is BluetoothOpponent || game.blackPlayer is BluetoothOpponent) {
                    if (bluetoothChatService.connectionState.value == BluetoothChatService.ConnectionState.CONNECTED) {
                        bluetoothChatService.sendMessage(BluetoothMessage.MoveMessage(move).value)
                    } else {
                        showToast("Not connected.")
                        clearPossibleMoves()
                    }
                } else {
                    makeMove(move)
                }
            }
            else -> {
                clearPossibleMoves()
            }
        }
    }

    fun makeMove(move: Move): Boolean {
        val response = game.board.move(move)

        if (response) {
            updateBoardUI()
            getNextMove()
        }

        return response
    }

    fun undo() {

        if (User !in listOf(game.whitePlayer, game.blackPlayer))
            return

        do {
            game.board.undoMove() ?: break
        } while (!game.isUserTurn)

        updateBoardUI()
    }

    private fun clearPossibleMoves() {
        selectedPosition.value = null
        possibleMoves.value = emptyList()
    }

    fun updateBoardUI() {
        if (boardUpdated.value == 0) boardUpdated.value = 1 else boardUpdated.value = 0
        clearPossibleMoves()
    }

    fun getNextMove() {
        GlobalScope.launch {

            delay(100)

            val player = if (game.board.turn == ChessPieceColor.WHITE) game.whitePlayer else game.blackPlayer
            when(player) {
                is User -> {
                    //
                }
                is BluetoothOpponent -> {
                    //
                }
                is AI -> {
                    delay(200)
                    makeMove(player.getNextMove(game.board) ?: return@launch)
                    delay(100)
                    updateBoardUI()
                }
            }
        }
    }

    val bluetoothChatListener = object : BluetoothChatService.ChatListener {
        override fun onConnecting() {
            showToast("Connecting...")
        }

        override fun onListening() {
            showToast("Listening...")
        }

        override fun onConnected() {
            showToast("Connected")
        }

        override fun onChatStart(deviceName: String) {
            showToast("Game resumed")
        }

        override fun onConnectionFailed() {
            showToast("Connection failed")
        }

        override fun onMessageSent(message: String) {
            parseMessage(message).let {
                if (it is BluetoothMessage.MoveMessage)
                    makeMove(it.move)
            }
        }

        override fun onMessageReceived(message: String) {
            val _message = parseMessage(message)
            when(_message) {
                is BluetoothMessage.MoveMessage -> {
                    GlobalScope.launch {
                        delay(200)
                        if (makeMove(_message.move)) {
                            delay(200)
                            bluetoothChatService.sendMessage(BluetoothMessage.MoveOk.value)
                        } else {
                            delay(200)
                            bluetoothChatService.sendMessage(BluetoothMessage.MoveFailed.value)
                        }

                    }
                }
                BluetoothMessage.DrawAccepted -> { showToast("Draw accepted!") }
                BluetoothMessage.DrawRejected -> { showToast("Draw rejected") }
                BluetoothMessage.DrawRequest -> { showToast("Draw request received") }
                BluetoothMessage.MoveFailed -> { showToast("Move failed") }
                BluetoothMessage.MoveOk -> { showToast("Move ok") }
                BluetoothMessage.SyncFailed -> { showToast("Sync failed") }
                BluetoothMessage.SyncOk -> { showToast("Sync OK") }
                is BluetoothMessage.SyncRequest -> {
                    showToast("Sync request")
                    if (game.compareBoardPositionTo(_message.position)) {
                        bluetoothChatService.sendMessage(BluetoothMessage.SyncOk.value)
                    }
                }
                BluetoothMessage.UndoAccepted -> {
                    showToast("Undo Accepted")
                    // TODO Do Undo
                }
                BluetoothMessage.UndoRejected -> { showToast("Undo rejected") }
                BluetoothMessage.UndoRequest -> { showToast("Undo request received") }
            }
        }

        override fun onDisconnected() {
            showToast("Disconnected!")
        }
    }

    fun showToast(message: String) {
        ContextCompat.getMainExecutor(getApplication()).execute {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }
    }
}