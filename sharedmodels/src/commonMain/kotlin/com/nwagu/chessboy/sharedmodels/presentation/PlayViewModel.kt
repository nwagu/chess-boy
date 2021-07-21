package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.bluetoothchat.ChatListener
import com.nwagu.bluetoothchat.ConnectionState
import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.gamelogic.moves.getPossibleMovesFrom
import com.nwagu.chess.model.*
import com.nwagu.chess.representation.*
import com.nwagu.chessboy.sharedmodels.bluetooth.BluetoothMessage
import com.nwagu.chessboy.sharedmodels.bluetooth.parseMessage
import com.nwagu.chessboy.sharedmodels.players.*
import com.nwagu.chessboy.sharedmodels.utils.initPlayers
import com.nwagu.chessboy.sharedmodels.utils.isBluetoothGame
import com.nwagu.chessboy.sharedmodels.utils.isUserTurn
import com.nwagu.chessboy.sharedmodels.utils.userColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlayViewModel: BaseViewModel() {

    lateinit var game: Game

    val gameUpdated = MutableStateFlow(0)
    val boardUpdated = MutableStateFlow(0)

    val selectedSquare = MutableStateFlow<Square?>(null)
    val possibleMoves = MutableStateFlow<List<Move>>(listOf())

    val pendingPromotion = MutableStateFlow<Promotion?>(null)

    lateinit var bluetoothChatService: BluetoothChatService

    fun isGameInitialized() = this::game.isInitialized

    fun init(game: Game) {

        endCurrentGame()

        this.game = game

        if (game.isBluetoothGame()) {
            attachBluetoothChatService()
        }

        game.initPlayers()

        updateGameUI()
        updateBoardUI()
        getNextMove()
    }

    fun init(game: Game, bluetoothChatService: BluetoothChatService) {

        endCurrentGame()

        this.game = game

        this.bluetoothChatService = bluetoothChatService
        this.bluetoothChatService.setListener(bluetoothChatListener)

        game.initPlayers()

        updateGameUI()
        updateBoardUI()
        getNextMove()
    }

    private fun attachBluetoothChatService() {
        (game.userColor == ChessPieceColor.WHITE).let { userIsWhite ->
            bluetoothChatService = BluetoothChatService()
            bluetoothChatService.init(userIsWhite)
            bluetoothChatService.setListener(bluetoothChatListener)
        }
    }

    fun attemptReconnectToDevice(address: String) {

        if (!this::bluetoothChatService.isInitialized) {
            attachBluetoothChatService()
        }

        if (bluetoothChatService.connectionState.value != ConnectionState.NONE)
            return

        bluetoothChatService.init(true)
        bluetoothChatService.connectDevice(address, true)
    }

    fun listenForReconnection() {

        if (!this::bluetoothChatService.isInitialized) {
            attachBluetoothChatService()
        }

        if (bluetoothChatService.connectionState.value != ConnectionState.NONE)
            return

        bluetoothChatService.init(false)
        bluetoothChatService.startListeningForConnection()
    }

    fun squareClicked(square: Square) {

        when {
            !game.isUserTurn -> {
                clearPossibleMoves()
            }

            selectedSquare.value == null || possibleMoves.value.isEmpty() -> {
                if (game.board.squareContainsOccupantColored(square, game.userColor)) {
                    selectedSquare.value = square
                    possibleMoves.value = game.board.getPossibleMovesFrom(square)
                }
            }

            square in possibleMoves.value.map { move -> move.destination } -> {

                val move = possibleMoves.value.find { move -> square == move.destination }!!

                if (move is Promotion)
                    pendingPromotion.value = move
                else
                    makeUserMove(move)

                clearPossibleMoves()

            }

            else -> {
                clearPossibleMoves()
            }
        }
    }

    fun makeUserMove(move: Move) {
        if (game.isBluetoothGame()) {
            if (bluetoothChatService.connectionState.value == ConnectionState.CONNECTED) {
                bluetoothChatService.sendMessage(BluetoothMessage.MoveMessage(move).value)
            } else {
                showToast("Not connected.")
            }
        } else {
            makeMove(move)
        }
    }

    private fun makeMove(move: Move): Boolean {
        val response = game.board.move(move)

        if (response) {
            updateBoardUI()
            getNextMove()
        }

        return response
    }

    fun undo() {

        if (game.isBluetoothGame()) {
            showToast("Undo not available for bluetooth games")
            return
        }

        if (User !in listOf(game.whitePlayer, game.blackPlayer))
            return

        do {
            game.board.undoMove() ?: break
        } while (!game.isUserTurn)

        updateBoardUI()
    }

    private fun clearPossibleMoves() {
        selectedSquare.value = null
        possibleMoves.value = emptyList()
    }

    private fun updateBoardUI() {
        if (boardUpdated.value == 0) boardUpdated.value = 1 else boardUpdated.value = 0
        clearPossibleMoves()
    }

    private fun updateGameUI() {
        if (gameUpdated.value == 0) gameUpdated.value = 1 else gameUpdated.value = 0
        clearPossibleMoves()
    }

    fun getNextMove() {
        clientScope.launch {

            delay(100)

            val player = if (game.board.turn == ChessPieceColor.WHITE) game.whitePlayer else game.blackPlayer
            when(player) {
                is User -> {
                    //
                }
                is BluetoothPlayer -> {
                    //
                }
                is MoveGenerator -> {
                    delay(200)
                    makeMove(player.getNextMove(game.board) ?: return@launch)
                    delay(100)
                    updateBoardUI()
                }
            }
        }
    }

    private val bluetoothChatListener: ChatListener
        get() = object : ChatListener {

            override fun onConnecting() {
                showToast("Connecting...")
            }

            override fun onListening() {
                showToast("Listening...")
            }

            override fun onConnected(address: String) {
                // reconnected
                showToast("Connected to bluetooth player at: $address")
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
                when (_message) {
                    is BluetoothMessage.MoveMessage -> {
                        clientScope.launch {
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
                    BluetoothMessage.DrawAccepted -> {
                        showToast("Draw accepted!")
                    }
                    BluetoothMessage.DrawRejected -> {
                        showToast("Draw rejected")
                    }
                    BluetoothMessage.DrawRequest -> {
                        showToast("Draw request received")
                    }
                    BluetoothMessage.MoveFailed -> {
                        showToast("Move failed")
                    }
                    BluetoothMessage.MoveOk -> {
                        // showToast("Move ok")
                    }
                    BluetoothMessage.SyncFailed -> {
                        showToast("Sync failed")
                    }
                    BluetoothMessage.SyncOk -> {
                        showToast("Sync OK")
                    }
                    is BluetoothMessage.SyncRequest -> {
                        showToast("Sync request")
                        if (game.board.compareTo(_message.fen)) {
                            bluetoothChatService.sendMessage(BluetoothMessage.SyncOk.value)
                        }
                    }
                    BluetoothMessage.UndoAccepted -> {
                        showToast("Undo Accepted")
                        // TODO Do Undo
                    }
                    BluetoothMessage.UndoRejected -> {
                        showToast("Undo rejected")
                    }
                    BluetoothMessage.UndoRequest -> {
                        showToast("Undo request received")
                    }
                }
            }

            override fun onDisconnected() {
                showToast("Bluetooth player disconnected.")
            }
        }

    fun endCurrentGame() {

        if (!isGameInitialized()) {
            return
        }

        if (game.isBluetoothGame()) {
            bluetoothChatService.stop()
        }

        for (player in listOf(game.whitePlayer, game.blackPlayer)) {
            if (player is UCIChessEngine)
                player.quit()
        }
    }

}