package com.argusoft.sewa.android.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.LMSFeedbackComponent;
import com.argusoft.sewa.android.app.component.ZoomImage;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.Date;

/**
 * Created by prateek on 25/10/18.
 */
@EActivity
public class CustomImageViewerActivity extends MenuActivity {

    private String path;
    private Integer courseId;
    private LmsLessonDataBean lesson;
    private Boolean fromLms = false;
    private Date startDate;
    private LMSFeedbackComponent feedbackAlert;

    @Bean
    LmsServiceImpl lmsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // keep screen active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("path");
            fromLms = extras.getBoolean("fromLms");
            if (Boolean.TRUE.equals(fromLms)) {
                courseId = extras.getInt("courseId");
                String lessonString = extras.getString("lesson");
                startDate = new Date();
                if (lessonString != null) {
                    lesson = new Gson().fromJson(lessonString, LmsLessonDataBean.class);
                    lmsService.updateStartDateForMedia(startDate, lesson.getActualId(), lesson.getTopicId(), courseId);
                }
            }
        } else {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onBackPressed() {
        if (Boolean.TRUE.equals(fromLms)) {
            Date endDate = new Date();
            lmsService.updateEndDateForMedia(endDate, lesson.getActualId(), lesson.getTopicId(), courseId);
            lmsService.addSessionsForMedia(startDate, endDate, lesson.getActualId(), lesson.getTopicId(), courseId);
            lmsService.markLessonCompleted(lesson.getActualId(), lesson.getTopicId(), courseId);

            if (Boolean.TRUE.equals(lesson.getUserFeedbackRequired())) {
                LmsViewedMediaBean viewedMediaBean = lmsService.getViewedLessonById(lesson.getActualId());
                if (viewedMediaBean != null && viewedMediaBean.getUserFeedback() == null) {
                    feedbackAlert = new LMSFeedbackComponent(context, lmsService, viewedMediaBean);
                    feedbackAlert.show();
                    return;
                }
            }
        }

        super.onBackPressed();
        finish();
    }

    private void initView() {
        if (path != null && path.trim().length() > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                showProcessDialog();
                RelativeLayout mainLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.pdf_layout, null);
                LinearLayout pdfLayout = mainLayout.findViewById(R.id.pdf_layout);
                final ZoomImage imageView = new ZoomImage(this);
                imageView.setMaxZoom(4f);
                imageView.setBackgroundColor(Color.BLACK);
                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                pdfLayout.addView(imageView);
                setContentView(mainLayout);
                ImageButton backButton = mainLayout.findViewById(R.id.back_button);
                backButton.setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
                backButton.setOnClickListener(v -> onBackPressed());
                hideProcessDialog();
            } else {
                SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.FILE_CORRUPTED));
                finish();
            }
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PATH_OF_FILE_NOT_FOUND));
            finish();
        }
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
