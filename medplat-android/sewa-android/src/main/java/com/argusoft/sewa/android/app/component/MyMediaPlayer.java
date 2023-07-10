package com.argusoft.sewa.android.app.component;

import android.media.MediaPlayer;

/**
 * @author kunjan
 */
public class MyMediaPlayer extends MediaPlayer {

    @Override
    public void stop() {
//          ****DO NOT DELETE THIS FUNCTION****        
    }

    @Override
    public void seekTo(int msec) {
        if (super.getCurrentPosition() > msec) {
            super.seekTo(msec);
        }
    }
}
