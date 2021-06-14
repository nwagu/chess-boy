package com.nwagu.android.chessboy.vm

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nwagu.android.chessboy.BluetoothMessage
import com.nwagu.android.chessboy.constants.PreferenceKeys.LAST_GAME
import com.nwagu.android.chessboy.parseMessage
import com.nwagu.android.chessboy.players.*
import com.nwagu.android.chessboy.util.SharedPrefUtils
import com.nwagu.android.chessboy.util.SharedPrefUtils.saveString
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

    val bluetoothChatService: BluetoothChatService by lazy { BluetoothChatService() }

    init {
        viewModelScope.launch {

            var lastGamePgn = ""
            try {
                lastGamePgn = SharedPrefUtils.readString(getApplication(), LAST_GAME, "") ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (lastGamePgn.isEmpty()) {
                game = Game(
                    whitePlayer = User,
                    blackPlayer = RandomMoveGenerator()
                )
                return@launch
            }

            game = Game(
                getPlayerWithRegisterId(getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER_ID, lastGamePgn)),
                getPlayerWithRegisterId(getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER_ID, lastGamePgn))
            )
            game.importPGN(lastGamePgn)

            updateBoardUI()
            getNextMove()
        }
    }

    private fun getPlayerWithRegisterId(id: String?): Player {
        return when {
            id == null -> RandomMoveGenerator()
            id == PlayersRegister.USER -> User
            id == PlayersRegister.GRANDPA -> GrandPa()
            id == PlayersRegister.RANDOM -> RandomMoveGenerator()
            id.startsWith(PlayersRegister.JWTC) -> {
                val level = try {
                    id.split("-")[1].split("=")[1].toInt()
                } catch (e: Exception) {
                    5
                }
                JWTC().also {
                    it.level = level
                    it.init()
                }
            }
            id.startsWith(PlayersRegister.STOCKFISH13) -> {
                val level = try {
                    id.split("-")[1].split("=")[1].toInt()
                } catch (e: Exception) {
                    5
                }
                Stockfish13(getApplication()).also {
                    it.level = level
                    it.init()
                }
            }
            id.startsWith(PlayersRegister.BLUETOOTH) -> {
                val address = id.split("-")[1]
                BluetoothPlayer(address = address)
            }
            else -> RandomMoveGenerator()
        }
    }

    fun startNewGame(
        whitePlayer: Player,
        blackPlayer: Player
    ) {

        if (whitePlayer is BluetoothPlayer || blackPlayer is BluetoothPlayer) {
            throw InvalidParameterException("Please use startNewBluetoothGame to start bluetooth game!")
        }

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

    fun startNewBluetoothGame(isWhite: Boolean, address: String) {
        game = Game(
            whitePlayer = if (isWhite) User else BluetoothPlayer(address = address),
            blackPlayer = if (isWhite) BluetoothPlayer(address = address) else User
        )
        updateGameUI()
        updateBoardUI()
    }

    fun attemptConnectToDevice(address: String) {
        bluetoothChatService.init(getApplication(), getBluetoothChatListener(true))
        bluetoothChatService.connectDevice(address, true)
    }

    fun listenForConnection() {
        bluetoothChatService.init(getApplication(), getBluetoothChatListener(false))
        bluetoothChatService.startListeningForConnection()
    }

    fun reconnectToBluetoothGame() {
        if (game.blackPlayer is BluetoothPlayer)
            bluetoothChatService.connectDevice((game.blackPlayer as BluetoothPlayer).address, true)
        else
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

                if (game.whitePlayer is BluetoothPlayer || game.blackPlayer is BluetoothPlayer) {
                    if (bluetoothChatService.connectionState.value == BluetoothChatService.ConnectionState.CONNECTED) {
                        bluetoothChatService.sendMessage(BluetoothMessage.MoveMessage(move).value)
                    } else {
                        showToast("Not connected.")
                        clearPossibleMoves()
                    }
                } else {
                    if (move !is Promotion)
                        makeMove(move)
                    return move
                }
            }

            else -> {
                clearPossibleMoves()
            }
        }

        return null
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

    fun getBluetoothChatListener(isWhite: Boolean): BluetoothChatService.ChatListener {

        return object : BluetoothChatService.ChatListener {

            override fun onConnecting() {
                showToast("Connecting...")
            }

            override fun onListening() {
                showToast("Listening...")
            }

            override fun onConnected(address: String) {
                showToast("Connected to: $address")
                startNewBluetoothGame(isWhite, address)
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
                        showToast("Move ok")
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
                showToast("Disconnected!")
            }
        }
    }

    fun showToast(message: String) {
        ContextCompat.getMainExecutor(getApplication()).execute {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        }
    }

    fun saveGame() {
        viewModelScope.launch {
            saveString(getApplication(), LAST_GAME, game.exportPGN())
        }
    }

}