package com.nwagu.chessboy.screens.main

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
import com.nwagu.chessboy.constants.RequestCodes
import com.nwagu.chessboy.model.ScreenConfig
import com.nwagu.chessboy.util.createViewModelFactory
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.model.Player
import com.nwagu.chessboy.sharedmodels.presentation.*

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
class MainActivity : AppCompatActivity() {

    val mainViewModel: MainViewModel by viewModels { createViewModelFactory { MainViewModel() } }
    val playViewModel: PlayViewModel by viewModels { createViewModelFactory { PlayViewModel() } }
    val gameAnalysisViewModel: GameAnalysisViewModel by viewModels { createViewModelFactory { GameAnalysisViewModel() } }
    val newGameViewModel: NewGameViewModel by viewModels { createViewModelFactory { NewGameViewModel() } }
    val newBluetoothGameViewModel: NewBluetoothGameViewModel by viewModels { createViewModelFactory { NewBluetoothGameViewModel() } }

    lateinit var screenConfig: ScreenConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initScreenConfig()

        setContent {
            MaterialTheme { MainView() }
        }

        resumeLastGame()

    }

    private fun initScreenConfig() {
        screenConfig = ScreenConfig(
            resources.configuration.orientation,
            resources.configuration.screenHeightDp,
            resources.configuration.screenWidthDp
        )
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
