/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.media.MediaPlayer;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.io.IOException;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class AudioPlayerListener implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "AudioPlayerListener";
    private final Context context;
    private final QueFormBean queFormBean;
    private boolean flag;
    private MediaPlayer mPlayer = null;
    private int currentAudioPlay = -1;
    private List<FieldValueMobDataBean> dataSources = null;
    private ImageView actionButton;

    public AudioPlayerListener(Context context, QueFormBean queFormBean, String dataMap) {
        this.queFormBean = queFormBean;
        this.context = context;
        flag = true;
        if (dataMap != null && dataMap.trim().length() > 0) {
            Log.i(TAG, "Data map is Found is : " + dataMap);
            dataSources = UtilBean.getDataMapValues(dataMap);
            if (dataSources != null && !dataSources.isEmpty()) {
                currentAudioPlay = 0;
            } else {
                Log.i(TAG, "No Files Found in local");
            }
        } else {
            Log.i(TAG, "No Audio list found in dataMap");
        }
    }

    @Override
    public void onClick(View view) {

        actionButton = (ImageView) view;

        if (flag) { // do playing 
            try {
                startPlaying();
                actionButton.setImageResource(R.drawable.stop_audio);
                flag = !flag;
                setMandatory(true);
            } catch (Exception e) {
                Log.e(TAG, "Audio Player not started", e);
                actionButton.setImageResource(R.drawable.play_audio);
                SewaUtil.generateToast(context, LabelConstants.NO_AUDIO_TO_PLAY);
                setMandatory(false);
            }
        } else {
            //stop playing
            try {
                stopPlaying();
                actionButton.setImageResource(R.drawable.play_audio);
                flag = !flag;
                setMandatory(false);
            } catch (Exception e) {
                Log.e(TAG, "Audio Player not stopped", e);
                actionButton.setImageResource(R.drawable.stop_audio);
                SewaUtil.generateToast(context, "Audio can not be stopped");
            }
        }
    }

    private void startPlaying() throws IOException {
        if (dataSources != null && !dataSources.isEmpty()) {
            mPlayer = new MediaPlayer();
            while (currentAudioPlay < dataSources.size()) {
                String filePath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + dataSources.get(currentAudioPlay).getValue();
                if (UtilBean.isFileExists(filePath)) {
                    Log.i(TAG, filePath + " is found and ready to play");
                    try {
                        mPlayer.setDataSource(filePath);
                        break;
                    } catch (Exception e) {
                        Log.e(TAG, null, e);
                        currentAudioPlay++;
                    }
                } else {
                    currentAudioPlay++;
                }
            }
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(this);
        } else {
            Log.e(getClass().getSimpleName(), LabelConstants.NO_AUDIO_TO_PLAY);
            throw new NullPointerException();
        }
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
        currentAudioPlay++;
        try {
            startPlaying();
        } catch (Exception e) {
            Log.e(TAG, "Audio Player not started", e);
            if (actionButton != null) {
                actionButton.setImageResource(R.drawable.play_audio);
                flag = !flag;
                currentAudioPlay = 0;
                setMandatory(false);
            }
        }
    }

    private void setMandatory(boolean flag) {
        if (flag) {
            queFormBean.setAnswer(null);
            queFormBean.setMandatorymessage(LabelConstants.AUDIO_IS_PLAYING);
            queFormBean.setIsmandatory(GlobalTypes.TRUE);
        } else {
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
        }
    }
}
