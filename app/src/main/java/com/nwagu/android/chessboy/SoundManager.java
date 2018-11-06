package com.nwagu.android.chessboy;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageButton;

import com.nwagu.android.chessboy.Utils.SharedPrefUtils;

import static com.nwagu.android.chessboy.Data.Constants.PREF_SOUND_STATE;

public class SoundManager {
    private Context context;
    private MediaPlayer onCheckSound, checkBro, moveMade, boardSet;
    private ImageButton soundControlButton;

    SoundManager(Context context, ImageButton soundControlButton) {
        this.context = context;
        this.soundControlButton = soundControlButton;
        onCheckSound = MediaPlayer.create(context, R.raw.u_ar_on_check);
        checkBro = MediaPlayer.create(context, R.raw.check_bro);
        moveMade = MediaPlayer.create(context, R.raw.move);
        boardSet = MediaPlayer.create(context, R.raw.boardset);
        resetVolumes();
    }

    private void resetVolumes() {
        if(Boolean.parseBoolean(SharedPrefUtils.readSharedSetting(context, PREF_SOUND_STATE, "true"))) {
            checkBro.setVolume(1.0f, 1.0f);
            onCheckSound.setVolume(1.0f, 1.0f);
            moveMade.setVolume(1.0f, 1.0f);
            boardSet.setVolume(1.0f, 1.0f);
            soundControlButton.setImageResource(R.drawable.vol_on);
        } else {
            checkBro.setVolume(0.0f, 0.0f);
            onCheckSound.setVolume(0.0f, 0.0f);
            moveMade.setVolume(0.0f, 0.0f);
            boardSet.setVolume(0.0f, 0.0f);
            soundControlButton.setImageResource(R.drawable.vol_off);
        }
    }

    public void toggleSoundPref() {
        boolean soundState = Boolean.parseBoolean(SharedPrefUtils.readSharedSetting(context, PREF_SOUND_STATE, "true"));
        SharedPrefUtils.saveSharedSetting(context, PREF_SOUND_STATE, Boolean.toString(!soundState));

        resetVolumes();
        moveMade.start(); //make a test sound with the new set volume
    }

    public void soundCheck() {
        onCheckSound.start();
    }

    public void reSoundCheck() {
        checkBro.start();
    }

    public void soundMove() {
        moveMade.start();
    }

    public void soundBoardSet() {
        boardSet.start();
    }
}
