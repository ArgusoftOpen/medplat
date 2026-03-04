package com.argusoft.sewa.android.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.LMSFeedbackComponent;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.Date;

/**
 * Created by prateek on 25/10/18.
 */
@EActivity
public class CustomPDFViewerActivity extends MenuActivity implements OnPageChangeListener {

    @Bean
    LmsServiceImpl lmsService;

    private PDFView pdfView;
    private String pdfFileName;
    private String path;
    private Integer courseId;
    private LmsLessonDataBean lesson;
    private Boolean fromLms = false;
    private Date startDate;
    private Boolean isTranscript = false;
    private LMSFeedbackComponent feedbackAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("path");
            pdfFileName = extras.getString("fileName");
            fromLms = extras.getBoolean("fromLms");
            isTranscript = extras.getBoolean("transcript");
            if (Boolean.TRUE.equals(fromLms)) {
                courseId = extras.getInt("courseId");
                String lessonString = extras.getString("lesson");
                startDate = new Date();
                if (lessonString != null && Boolean.FALSE.equals(isTranscript)) {
                    lesson = new Gson().fromJson(lessonString, LmsLessonDataBean.class);
                    lmsService.updateStartDateForMedia(startDate, lesson.getActualId(), lesson.getTopicId(), courseId);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        setTitle(UtilBean.getMyLabel(LabelConstants.PDF_VIEWER));
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        setTitle(String.format("%s (%s) (%s / %s)", UtilBean.getMyLabel(LabelConstants.PDF_VIEWER), pdfFileName, page, pageCount));
    }

    private void initView() {
        showProcessDialog();
        RelativeLayout mainLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.pdf_layout, null);
        LinearLayout pdfLayout = mainLayout.findViewById(R.id.pdf_layout);
        pdfView = new PDFView(this, null);
        pdfView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        pdfLayout.addView(pdfView);

        ImageButton backButton = mainLayout.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        setContentView(mainLayout);
        displayFromAsset();
    }

    private void displayFromAsset() {
        if (path != null && path.trim().length() > 0) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    pdfView.fromFile(file)
                            .defaultPage(1)
                            .showMinimap(false)
                            .enableSwipe(true)
                            .onPageChange(this)
                            .load();
                } catch (Exception e) {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.FILE_CORRUPTED));
                    finish();
                }
            } else {
                SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.FILE_NOT_FOUND));
                finish();
            }
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PATH_OF_FILE_NOT_FOUND));
            finish();
        }
        hideProcessDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (Boolean.TRUE.equals(fromLms) && Boolean.FALSE.equals(isTranscript)) {
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
}