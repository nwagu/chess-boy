package com.nwagu.android.chessboy.vm

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nwagu.android.chessboy.bluetooth.BluetoothMessage
import com.nwagu.android.chessboy.bluetooth.parseMessage
import com.nwagu.android.chessboy.players.*
import com.nwagu.android.chessboy.util.SharedPrefUtils.getSavedPGNs
import com.nwagu.android.chessboy.util.SharedPrefUtils.savePGNs
import com.nwagu.android.chessboy.util.isBluetoothGame
import com.nwagu.android.chessboy.util.isUserTurn
import com.nwagu.android.chessboy.util.userColor
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.Game
import com.nwagu.chess.Player
import com.nwagu.chess.board.*
import com.nwagu.chess.convention.*
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.Promotion
import com.nwagu.chess.moves.getPossibleMovesFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class GameViewModel(application: Application): AndroidViewModel(application) {

    lateinit var game: Game

    val gameUpdated = MutableStateFlow(0)
    val boardUpdated = MutableStateFlow(0)

    var selectedSquare = MutableStateFlow<Square?>(null)
    var possibleMoves = MutableStateFlow<List<Move>>(listOf())

    lateinit var bluetoothChatService: BluetoothChatService

    init {
        viewModelScope.launch {

            val lastGamePgn = getLastGamePgn()

            if (lastGamePgn.isNullOrEmpty()) {
                game = Game(
                    whitePlayer = User,
                    blackPlayer = RandomMoveGenerator()
                )
                return@launch
            }

            val lastGameId = getHeaderValueFromPgn(PGN_HEADER_GAME_ID, lastGamePgn) ?: ""

            val lastWhitePlayer =
                getPlayerWithId(
                    getApplication(),
                    getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER_ID, lastGamePgn) ?: ""
                ).apply { if (this is UCIChessEngine) init() }

            val lastBlackPlayer =
                getPlayerWithId(
                    getApplication(),
                    getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER_ID, lastGamePgn) ?: ""
                ).apply { if (this is UCIChessEngine) init() }

            game = Game(lastGameId, lastWhitePlayer, lastBlackPlayer)
            game.importPGN(lastGamePgn)

            if (game.isBluetoothGame()) {
                recreateBluetoothChatService()
            }

            updateBoardUI()
            getNextMove()
        }
    }

    fun startNewGame(
        whitePlayer: Player,
        blackPlayer: Player
    ) {

        if (whitePlayer is BluetoothPlayer || blackPlayer is BluetoothPlayer) {
            throw InvalidParameterException("Please use startNewBluetoothGame to start bluetooth game!")
        }

        endCurrentGame()

        if (whitePlayer is UCIChessEngine) whitePlayer.init()
        if (blackPlayer is UCIChessEngine) blackPlayer.init()

        game = Game(
            whitePlayer = whitePlayer,
            blackPlayer = blackPlayer
        )

        updateGameUI()
        updateBoardUI()
        getNextMove()
    }

    fun startNewBluetoothGame(bluetoothChatService: BluetoothChatService) {

        val isWhite = bluetoothChatService.isInitiator
        val address = bluetoothChatService.partnerAddress

        this.bluetoothChatService = bluetoothChatService
        this.bluetoothChatService.setListener(bluetoothChatListener)

        endCurrentGame(disconnectBluetooth = false)

        game = Game(
            whitePlayer = if (isWhite) User else BluetoothPlayer(address = address),
            blackPlayer = if (isWhite) BluetoothPlayer(address = address) else User
        )
        updateGameUI()
        updateBoardUI()
    }

    private fun recreateBluetoothChatService() {
        (game.userColor == ChessPieceColor.WHITE).let { userIsWhite ->
            bluetoothChatService = BluetoothChatService()
            bluetoothChatService.init(getApplication(), userIsWhite)
            bluetoothChatService.setListener(bluetoothChatListener)
        }
    }

    fun attemptReconnectToDevice(address: String) {

        if (!this::bluetoothChatService.isInitialized) {
            recreateBluetoothChatService()
        }

        if (bluetoothChatService.connectionState.value != BluetoothChatService.ConnectionState.NONE)
            return

        bluetoothChatService.init(getApplication(), true)
        bluetoothChatService.connectDevice(address, true)
    }

    fun listenForReconnection() {

        if (!this::bluetoothChatService.isInitialized) {
            recreateBluetoothChatService()
        }

        if (bluetoothChatService.connectionState.value != BluetoothChatService.ConnectionState.NONE)
            return

        bluetoothChatService.init(getApplication(), false)
        bluetoothChatService.startListeningForConnection()
    }

    fun squareClicked(square: Square): Move? {

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

                if (move !is Promotion)
                    makeUserMove(move)

                clearPossibleMoves()

                return move

            }

            else -> {
                clearPossibleMoves()
            }
        }

        return null
    }

    fun makeUserMove(move: Move) {
        if (game.isBluetoothGame()) {
            if (bluetoothChatService.connectionState.value == BluetoothChatService.ConnectionState.CONNECTED) {
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

    fun updateBoardUI() {
        if (boardUpdated.value == 0) boardUpdated.value = 1 else boardUpdated.value = 0
        clearPossibleMoves()
    }

    fun updateGameUI() {
        if (gameUpdated.value == 0) gameUpdated.value = 1 else gameUpdated.value = 0
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

    val bluetoothChatListener: BluetoothChatService.ChatListener
        get() = object : BluetoothChatService.ChatListener {

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
                        if (game.compareBoardPositionTo(_message.position)) {
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

    fun showToast(message: String) {
        ContextCompat.getMainExecutor(getApplication()).execute {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }
    }

    fun saveGame() {
        viewModelScope.launch {
            val history = getGamesHistory()

            while (history.size >= 20) {
                history.removeFirst()
            }

            history.removeAll(history.filter {
                getHeaderValueFromPgn(PGN_HEADER_GAME_ID, it) == game.id
            })
            history.addLast(game.exportPGN())

            savePGNs(getApplication(), history.toList())
        }
    }

    private fun getLastGamePgn(): String? {
        return getGamesHistory().lastOrNull()
    }

    fun getGamesHistory(): ArrayDeque<String> {
        return ArrayDeque(getSavedPGNs(getApplication()))
    }

    fun endCurrentGame(disconnectBluetooth: Boolean = true) {

        saveGame()

        if (game.isBluetoothGame() && disconnectBluetooth) {
            bluetoothChatService.stop()
        }

        for (player in listOf(game.whitePlayer, game.blackPlayer)) {
            if (player is UCIChessEngine)
                player.quit()
        }
    }

}