package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.argusoft.sewa.android.app.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyAudioComponent extends LinearLayout implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, View.OnClickListener {

    private final Context context;

    private SeekBar seekbar;
    private TextView playedTime;
    private ImageButton playPauseButton;

    private LinearLayout mainLayout;
    private Timer timer = new Timer();

    private final android.media.MediaPlayer player;
    private final Handler uIHandler = new Handler(Looper.getMainLooper());

    private int currentPos = 0;
    private boolean isPlaying = false;

    //Lms Related
    private boolean isFromLms = false;
    private LmsServiceImpl lmsService;
    private LmsLessonDataBean lesson;
    private Integer courseId;
    private Date startDate;
    private LMSFeedbackComponent feedbackAlert;
    private LmsViewedMediaBean viewedMediaBean;
    private int layoutResId = R.layout.audio_player_layout;
    private MediaPlayerListener mediaPlayerListener;

    public MyAudioComponent(Context context, String title, android.media.MediaPlayer player) {
        super(context);
        this.context = context;
        this.player = player;
        init(context, title);
    }

    public MyAudioComponent(Context context, int layoutResId, android.media.MediaPlayer player, MediaPlayerListener mediaPlayerListener) {
        super(context);
        this.context = context;
        this.layoutResId = layoutResId;
        this.player = player;
        this.mediaPlayerListener = mediaPlayerListener;
        init(context, null);
    }

    public MyAudioComponent(Context context, String title, android.media.MediaPlayer player, boolean isFromLms, LmsServiceImpl lmsService, LmsLessonDataBean lesson, Integer courseId) {
        super(context);
        this.context = context;
        this.player = player;
        this.isFromLms = isFromLms;
        this.lmsService = lmsService;
        this.lesson = lesson;
        this.courseId = courseId;
        if (Boolean.TRUE.equals(isFromLms)) {
            startDate = new Date();
            lmsService.updateStartDateForMedia(startDate, lesson.getActualId(), lesson.getTopicId(), courseId);
            viewedMediaBean = lmsService.getViewedLessonById(lesson.getActualId());
        }
        init(context, title);
    }


    private void init(Context context, String title) {
        mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.audio_player_layout, null);
        mainLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView titleView = mainLayout.findViewById(R.id.audio_title_view);
        titleView.setText(title);

        if (title == null) {
            mainLayout.removeView(titleView);
        }

        if (player != null) {
            try {
                player.prepare();
            } catch (Exception e) {
                Log.e(getClass().getName(), null, e);
            }
            int maxDuration = (player.getDuration() / 1000);
            player.setOnCompletionListener(this);

            playPauseButton = mainLayout.findViewById(R.id.audio_play_button);
            playPauseButton.setOnClickListener(this);

            seekbar = mainLayout.findViewById(R.id.audio_seekbar);
            seekbar.setMax(maxDuration);
            seekbar.setOnSeekBarChangeListener(this);

            TextView totalTime = mainLayout.findViewById(R.id.audio_total_time);
            totalTime.setText(getTimeInHhMmSSFromMillis(seekbar.getMax() * 1000));

            playedTime = mainLayout.findViewById(R.id.audio_played_time);
        }

        try {
            if (viewedMediaBean != null && viewedMediaBean.getLastPausedOn() != null) {
                onProgressChanged(seekbar, viewedMediaBean.getLastPausedOn() / 1000, true);
            }
            play();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
            isPlaying = false;
        }
        this.addView(mainLayout);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mainLayout.setBackgroundResource(resid);
    }

    @Override
    public void onClick(View v) {
        if (isPlaying) {
            pause();
        } else {
            try {
                play();
            } catch (Exception e) {
                Log.e(getClass().getName(), null, e);
                isPlaying = false;
            }
        }
    }

    private void pause() {
        if (player != null) {
            isPlaying = false;
            player.pause();
            playPauseButton.setImageResource(R.drawable.my_video_view_player_btn);
        }
    }

    public void setPlayIcon() {
        if (player != null) {
            isPlaying = player.isPlaying();
            if (player.isPlaying()) {
                playPauseButton.setImageResource(R.drawable.my_video_view_stop_btn);
            } else {
                playPauseButton.setImageResource(R.drawable.my_video_view_player_btn);
            }
        }
    }


    public void play() {
        if (player != null) {
            player.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.my_video_view_stop_btn);
        }

        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (player != null && player.isPlaying() && currentPos < seekbar.getMax()) {
                            try {
                                seekbar.setProgress(currentPos++);
                                uIHandler.post(() -> playedTime.setText(getTimeInHhMmSSFromMillis(currentPos * 1000)));
                            } catch (Exception e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                    }
                },
                0, 1000
        );
    }

    private String getTimeInHhMmSSFromMillis(int millis) {
        if (seekbar.getMax() >= 3600) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }

    }

    @Override
    public void onCompletion(android.media.MediaPlayer mp) {
        isPlaying = false;
        currentPos = 0;
        playPauseButton.setImageResource(R.drawable.my_video_view_player_btn);
        if (mediaPlayerListener != null) {
            mediaPlayerListener.onCompletion();
        }
        if (Boolean.TRUE.equals(isFromLms)) {
            Date endDate = new Date();
            lmsService.updateEndDateForMedia(endDate, lesson.getActualId(), lesson.getTopicId(), courseId);
            lmsService.addSessionsForMedia(startDate, endDate, lesson.getActualId(), lesson.getTopicId(), courseId);
            lmsService.markLessonCompleted(lesson.getActualId(), lesson.getTopicId(), courseId);
            lmsService.updateLastPausedOnForMedia(null, lesson.getActualId(), lesson.getTopicId(), courseId);
            askForFeedback();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (player != null) {
                player.seekTo(progress * 1000);
            }
            currentPos = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar sb) {
        // do something
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb) {
        // do something
    }

    private void askForFeedback() {
        if (Boolean.TRUE.equals(lesson.getUserFeedbackRequired())) {
            viewedMediaBean = lmsService.getViewedLessonById(lesson.getActualId());
            if (viewedMediaBean == null || !Boolean.TRUE.equals(viewedMediaBean.getCompleted()) || viewedMediaBean.getUserFeedback() != null) {
                return;
            }
            feedbackAlert = new LMSFeedbackComponent(context, lmsService, viewedMediaBean);
            feedbackAlert.show();
        }
    }

    public interface MediaPlayerListener {
        void onCompletion();
    }
}