package com.nwagu.android.chessboy

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
import com.nwagu.android.chessboy.model.data.ScreenConfig
import com.nwagu.android.chessboy.screens.HomeView
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.vm.NewBluetoothGameViewModel
import com.nwagu.android.chessboy.vm.NewGameViewModel

class MainActivity : AppCompatActivity() {

    val gameViewModel: GameViewModel by viewModels()
    val newGameViewModel: NewGameViewModel by viewModels()
    val newbluetoothGameViewModel: NewBluetoothGameViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenConfig = ScreenConfig(
            resources.configuration.orientation,
            resources.configuration.screenHeightDp,
            resources.configuration.screenWidthDp
        )

        setContent {
            MaterialTheme {

                val dialogController = rememberDialogController(this)

                HomeView(
                    gameViewModel, newGameViewModel, newbluetoothGameViewModel, screenConfig, dialogController
                )

                DialogHost(dialogController)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        gameViewModel.saveGame()
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
