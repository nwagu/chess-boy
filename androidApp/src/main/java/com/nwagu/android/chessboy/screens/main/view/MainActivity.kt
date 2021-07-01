package com.nwagu.android.chessboy.screens.main.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import com.nwagu.android.chessboy.constants.RequestCodes
import com.nwagu.android.chessboy.dialogs.DialogHost
import com.nwagu.android.chessboy.dialogs.rememberDialogController
import com.nwagu.android.chessboy.screens.analysis.vm.GameAnalysisViewModel
import com.nwagu.android.chessboy.screens.main.vm.MainViewModel
import com.nwagu.android.chessboy.ui.data.ScreenConfig
import com.nwagu.android.chessboy.screens.play.vm.PlayViewModel
import com.nwagu.android.chessboy.screens.newgame.vm.NewBluetoothGameViewModel
import com.nwagu.android.chessboy.screens.newgame.vm.NewGameViewModel
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.Player

class MainActivity : AppCompatActivity() {

    val mainViewModel: MainViewModel by viewModels()
    val playViewModel: PlayViewModel by viewModels()
    val gameAnalysisViewModel: GameAnalysisViewModel by viewModels()
    val newGameViewModel: NewGameViewModel by viewModels()
    val newBluetoothGameViewModel: NewBluetoothGameViewModel by viewModels()

    lateinit var screenConfig: ScreenConfig

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenConfig = ScreenConfig(
            resources.configuration.orientation,
            resources.configuration.screenHeightDp,
            resources.configuration.screenWidthDp
        )

        setContent {
            MaterialTheme {
                val dialogController = rememberDialogController(this)
                MainView(dialogController)
                DialogHost(dialogController)
            }
        }

        resumeLastGame()

    }

    private fun resumeLastGame() {
        if (!playViewModel.isGameInitialized()) {
            val game = mainViewModel.getLastGameOrDefault()
            playViewModel.init(game)
        }
    }

    fun startNewGame(
        whitePlayer: Player,
        blackPlayer: Player
    ) {
        val game = mainViewModel.createNewGame(whitePlayer, blackPlayer)
        mainViewModel.saveGame(playViewModel.game)
        playViewModel.init(game)
    }

    fun startNewBluetoothGame(bluetoothChatService: BluetoothChatService) {
        val game = mainViewModel.createNewBluetoothGame(bluetoothChatService)
        mainViewModel.saveGame(playViewModel.game)
        playViewModel.init(game, bluetoothChatService)
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.saveGame(playViewModel.game)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodes.REQUEST_ENABLE_BT ->
                if (resultCode == RESULT_OK) {
                    //
                } else {
                    Toast.makeText(
                        this,
                        "Bluetooth required",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

}
