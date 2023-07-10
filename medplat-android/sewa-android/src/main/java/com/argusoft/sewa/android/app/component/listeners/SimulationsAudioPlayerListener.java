/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.io.IOException;
import java.util.List;

/**
 * @author utkarsh jadav
 */
public class SimulationsAudioPlayerListener implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "AudioPlayerListener";
    private final Context context;
    private boolean flag;
    private MediaPlayer mPlayer = null;
    private ImageView actionButton;
    private final String path;

    public SimulationsAudioPlayerListener(Context context, String path) {
        this.context = context;
        this.path = path;
        flag = true;
    }

    @Override
    public void onClick(View view) {

        actionButton = (ImageView) view;

        if (flag) { // do playing 
            try {
                startPlaying();
                actionButton.setImageResource(R.drawable.stop_audio);
                flag = !flag;
            } catch (Exception e) {
                Log.e(TAG, "Audio Player not started", e);
                actionButton.setImageResource(R.drawable.play_audio);
                SewaUtil.generateToast(context, LabelConstants.NO_AUDIO_TO_PLAY);
            }
        } else {
            //stop playing
            try {
                stopPlaying();
                actionButton.setImageResource(R.drawable.play_audio);
                flag = !flag;
            } catch (Exception e) {
                Log.e(TAG, "Audio Player not stopped", e);
                actionButton.setImageResource(R.drawable.stop_audio);
                SewaUtil.generateToast(context, "Audio can not be stopped");
            }
        }
    }

    private void startPlaying() throws IOException {
        mPlayer = new MediaPlayer();
        if (UtilBean.isFileExists(path)) {
            Log.i(TAG, path + " is found and ready to play");
            try {
                mPlayer.setDataSource(path);
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
        }
        mPlayer.prepare();
        mPlayer.start();
        mPlayer.setOnCompletionListener(this);
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                Log.e(TAG, "Audio Player not stopped", e);
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            stopPlaying();
            if (actionButton != null) {
                actionButton.setImageResource(R.drawable.play_audio);
                flag = !flag;
            }
        } catch (Exception e) {
            Log.e(TAG, "Audio Player not started", e);
        }
    }
}
