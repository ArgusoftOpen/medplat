package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author alpeshkyada
 */
public class MyAudioPlayer extends AlertDialog implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, View.OnClickListener {

    private SeekBar seekbar;
    private TextView timeView;
    private MediaPlayer player;
    private final Handler uIHandler = new Handler(Looper.getMainLooper());
    private int currentPos = 0;
    private boolean isPlay = false;
    private AlertDialog alertDialog;
    private Button button;
    private boolean isFromLms = false;
    private LmsServiceImpl lmsService;
    private LmsLessonDataBean lesson;
    private Integer courseId;
    private Date startDate;
    private LMSFeedbackComponent feedbackAlert;
    private final Context context;
    private LmsViewedMediaBean viewedMediaBean;

    public MyAudioPlayer(Context context, String title, MediaPlayer player) {
        super(context);
        this.context = context;
        this.player = player;
        init(context, title);
    }

    public MyAudioPlayer(Context context, String title, MediaPlayer player, boolean isFromLms, LmsServiceImpl lmsService, LmsLessonDataBean lesson, Integer courseId) {
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

    private void init(Context context, String msg) {
        Builder alertDialogBuilder = new Builder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        alertDialogBuilder.setCancelable(false);

        if (msg != null) {
            alertDialogBuilder.setTitle(msg);
        }

        if (player != null) {
            try {
                player.prepare();
            } catch (Exception e) {
                Log.e(getClass().getName(), null, e);
            }
            int maxDuration = (player.getDuration() / 1000);
            player.setOnCompletionListener(this);

            LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            mainLayout.setPadding(30, 30, 30, 0);
            seekbar = new SeekBar(context);
            seekbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            seekbar.setMax(maxDuration);
            seekbar.setOnSeekBarChangeListener(this);
            mainLayout.addView(seekbar);
            timeView = MyStaticComponents.getMaterialTextView(context,
                    getTimeViewString(),
                    -1, R.style.CustomLabelView, false);
            timeView.setPadding(30, 0, 0, 0);
            mainLayout.addView(timeView);
            alertDialogBuilder.setView(mainLayout);

        }

        OnClickListener onCancelClickListener = (dialog, which) -> {
            if (player != null) {
                player.stop();
                if (isFromLms) {
                    if (currentPos != 0) {
                        lmsService.updateLastPausedOnForMedia(currentPos, lesson.getActualId(), lesson.getTopicId(), courseId);
                    }
                    lmsService.addSessionsForMedia(startDate, new Date(), lesson.getActualId(), lesson.getTopicId(), courseId);
                    askForFeedback();
                }
            }
        };

        alertDialogBuilder.setPositiveButton(UtilBean.getMyLabel(GlobalTypes.EVENT_PAUSE), null);
        alertDialogBuilder.setNegativeButton("Cancel", onCancelClickListener);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        button = alertDialog.getButton(BUTTON_POSITIVE);
        button.setOnClickListener(this);

        try {
            if (viewedMediaBean != null && viewedMediaBean.getLastPausedOn() != null) {
                onProgressChanged(seekbar, viewedMediaBean.getLastPausedOn(), true);
            }
            play();
            button.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_PAUSE));
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
            button.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_PLAY));
            isPlay = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (isPlay) {
            pause();
            button.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_PLAY));
        } else {
            try {
                play();
                button.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_PAUSE));
            } catch (Exception e) {
                Log.e(getClass().getName(), null, e);
                isPlay = false;
            }
        }
    }

    @Override
    public void show() {
        alertDialog.show();
    }

    @Override
    public void dismiss() {
        alertDialog.dismiss();
    }

    @Override
    public void hide() {
        alertDialog.hide();
    }

    private void play() {
        if (player != null) {
            player.start();
            isPlay = true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null && player.isPlaying()) {
                    try {
                        seekbar.setProgress(currentPos++);
                        uIHandler.post(() -> timeView.setText(getTimeViewString()));
                        Thread.sleep(1000);
                        if (player == null) {
                            break;
                        }
                    } catch (Exception e) {
                        Log.e(getClass().getName(), null, e);
                        return;
                    }
                }
            }
        }).start();
    }

    private String getTimeViewString() {
        return getTimeInHhMmSSFromMillis(currentPos * 1000) + "/" + getTimeInHhMmSSFromMillis(seekbar.getMax() * 1000);
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
    public void onCompletion(MediaPlayer mp) {
        isPlay = false;
        cancel();
        currentPos = 0;
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

    private void pause() {
        if (player != null) {
            isPlay = false;
            player.pause();
        }
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
}
