package com.argusoft.sewa.android.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.argusoft.sewa.android.app.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.argusoft.sewa.android.app.component.MyMediaPlayer;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @author kunjan
 */
public class CustomMandatoryVideoPlayerActivity extends MenuActivity
        implements MediaPlayer.OnCompletionListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {

    private static final String TAG = "MandatoryVideoPlayer";
    private int playingSourceNumber;
    private int currentPosition;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MyMediaPlayer mediaPlayer;
    private List<FieldValueMobDataBean> dataSources;
    private boolean onPauseCalled = false;
    private Integer questionId;
    private String fileLocation;

    @Override
    public int getAudioSessionId() {
        return 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // keep screen active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        LinearLayout parentLayout = MyStaticComponents.getLinearLayout(this, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            questionId = extras.getInt("questionId");
            String dataMap = extras.getString(GlobalTypes.DATA_MAP);
            boolean isFromAnnouncement = extras.getBoolean("isFromAnnouncement");
            fileLocation = extras.getString("fileLocation");

            if (fileLocation == null || fileLocation.isEmpty()) {
                fileLocation = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED);
            }

            if (dataMap != null && dataMap.trim().length() > 0) {
                Log.i(TAG, "Data map is Found is : " + dataMap);
                if (isFromAnnouncement) {
                    FieldValueMobDataBean mobDataBean = new FieldValueMobDataBean();
                    mobDataBean.setValue(dataMap);
                    dataSources = new ArrayList<>();
                    dataSources.add(mobDataBean);
                } else {
                    dataSources = UtilBean.getDataMapValues(dataMap);
                }
            }
        }

        if (dataSources != null && !dataSources.isEmpty()) {
            mediaPlayer = new MyMediaPlayer();
            mediaPlayer.setOnCompletionListener(this);

            if (playVideoSequentially()) {
                mediaPlayer.setScreenOnWhilePlaying(true);
                surfaceView = new SurfaceView(this);
                surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(this);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

                parentLayout.addView(surfaceView);
            } else {
                MaterialTextView msg = MyStaticComponents.generateTitleView(this, LabelConstants.NO_VIDEO_TO_PLAY);
                parentLayout.addView(msg);
            }

        } else {
            MaterialTextView msg = MyStaticComponents.generateTitleView(this, LabelConstants.NO_VIDEO_TO_PLAY);
            parentLayout.addView(msg);
        }
        setContentView(parentLayout);
    }

    private boolean playVideoSequentially() {
        boolean toastFlag = true;
        while (playingSourceNumber < dataSources.size()) {
            String filePath = fileLocation + GlobalTypes.PATH_SEPARATOR + dataSources.get(playingSourceNumber).getValue();

            if (UtilBean.isFileExists(filePath)) {
                Log.i(TAG, filePath + " is found and ready to play");
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(filePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    if (currentPosition > 0) {
                        mediaPlayer.seekTo(currentPosition);
                    }
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    playingSourceNumber++;
                    currentPosition = 0;
                    if (toastFlag) {
                        SewaUtil.generateToast(this, LabelConstants.VIDEO_FILE_CORRUPTED);
                        toastFlag = false;
                    }
                    mediaPlayer.reset();
                    UtilBean.deleteFile(filePath);
                }
            } else {
                Log.i(TAG, "File not found");
                playingSourceNumber++;
                currentPosition = 0;
                if (toastFlag) {
                    SewaUtil.generateToast(this, LabelConstants.VIDEO_FILE_NOT_DOWNLOADED);
                    toastFlag = false;
                }
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated Called");
        mediaPlayer.setDisplay(holder);
        try {
            MediaController controller = new MediaController(this);
            controller.setMediaPlayer(this);
            controller.setAnchorView(surfaceView);
            controller.bringToFront();
            controller.show();
            surfaceView.setOnTouchListener((new View.OnTouchListener() {
                private MediaController mc;

                public View.OnTouchListener setMediaController(MediaController mc) {
                    this.mc = mc;
                    return this;
                }

                @Override
                public boolean onTouch(View view, MotionEvent me) {
                    mc.show();
                    return true;
                }
            }).setMediaController(controller));
        } catch (Exception e) {
            Log.e(TAG, null, e);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPauseCalled = false;
        setTitle(LabelConstants.VIDEO_PLAYER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseCalled = true;
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
            Log.i(TAG, "Current position is : " + currentPosition);
            mediaPlayer.stop();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.v(TAG, "surfaceChanged Called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed Called");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, "onCompletion Called");
        playingSourceNumber++;
        currentPosition = 0;
        if (!playVideoSequentially()) {
            if (!onPauseCalled)
                SharedStructureData.videoShownMap.put(questionId, Boolean.TRUE);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //******DO NOT DELETE THIS FUNCTION*****
        //User wont be able to stop the video in between
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }


    @Override
    public void finish() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        currentPosition = 0;
        playingSourceNumber = 0;
        surfaceHolder = null;
        surfaceView = null;
        super.finish();
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int i) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i);
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 20;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
