
package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyAudioComponent;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsOptionFeedbackDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionBankDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsScenarioConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserQuizMetaData;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.lms.CaseStudyQueAdapter;
import com.argusoft.sewa.android.app.lms.IGetCaseStudyInterface;
import com.argusoft.sewa.android.app.lms.MyVideoMediaController;
import com.argusoft.sewa.android.app.lms.MyVideoView;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class LmsSimulationActivity extends MenuActivity implements IGetCaseStudyInterface {

    private static final String TAG = "LmsCaseStudyQuestionModuleActivity";

    @Bean
    LmsServiceImpl lmsService;

    private ViewPager2 viewPager;
    private MaterialButton nextButton;
    private Integer quePosition;
    private RadioButton selectedRadioButtonFromAdapter;
    private Boolean isCorrectOptionSelected;
    private String optionCorrectFeedback;
    private String optionInCorrectFeedback;
    private LmsOptionFeedbackDataBean optionCorrectFeedbackBean;
    private LmsOptionFeedbackDataBean optionInCorrectFeedbackBean;
    private AlertDialog alertDialog;
    private MyAlertDialog myAlertDialog;
    private AppCompatTextView caseTitle, caseDescription;
    private LinearLayout footerLayout;
    private ImageView caseImage;
    private FrameLayout flVideoView;
    private FrameLayout flAudioView;
    private String testFor;
    private LmsQuestionSetDataBean questionSet;
    private Date startDate;
    private Date endDate;
    MyVideoView myVideoView;
    private MyAudioComponent myAudioComponent;
    private boolean isFullScreen = false;
    private boolean isMediaVideo = false;
    private final Gson gson = new Gson();
    private MediaPlayer audioMediaPlayer;
    private List<LmsScenarioConfigDataBean> sectionDataBeans; //in scenario base there will only be one section
    private LmsScenarioConfigDataBean sectionData;
    List<LmsQuestionConfigDataBean> lmsQuestionConfigDataBeanList = new ArrayList<>();
    private final Map<Integer, LmsQuestionAnswerDataBean> answerMap = new HashMap<>();
    private Map<Integer, RadioGroup> radioGroupMapFromAdapter = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_lms_case_study_quiz);
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getQuestionSetFromIntent();

        if (sectionDataBeans == null || sectionDataBeans.isEmpty()) {
            hideProcessDialog();
            SewaUtil.generateToast(context, "Question Section Configurations not found. Please refresh and try again.");
            finish();
            return;
        }
        setCaseStudyScreen();
    }

    private void getQuestionSetFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            testFor = extras.getString("testFor");
            String queSet = extras.getString("questionSet");
            if (queSet == null) {
                hideProcessDialog();
                SewaUtil.generateToast(context, "Question Set not found. Please refresh and try again.");
                finish();
            } else {
                questionSet = gson.fromJson(queSet, LmsQuestionSetDataBean.class);
                if (questionSet.getQuestionBank() != null && !questionSet.getQuestionBank().isEmpty()) {
                    LmsQuestionBankDataBean questionBank = questionSet.getQuestionBank().get(0);
                    if (questionBank.getConfigJson() != null && !questionBank.getConfigJson().isEmpty()) {
                        sectionDataBeans = gson.fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsScenarioConfigDataBean>>() {
                        }.getType());
                    }
                }
            }
        }
    }

    @UiThread
    public void setCaseStudyScreen() {
        setTitle(UtilBean.getTitleText(LabelConstants.CASE_STUDY_TITLE));
        caseTitle = findViewById(R.id.caseTitle);
        caseDescription = findViewById(R.id.caseDescription);
        viewPager = findViewById(R.id.questions_view_pager);
        nextButton = findViewById(R.id.nextButton);
        caseImage = findViewById(R.id.caseImage);
        flVideoView = findViewById(R.id.flVideoView);
        flAudioView = findViewById(R.id.flAudioView);
        footerLayout = findViewById(R.id.footerLayout);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setInlineLabel(true);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("Description"));
        tabLayout.addTab(tabLayout.newTab().setText("Media"));

        startDate = new Date();
        sectionData = sectionDataBeans.get(0); //In scenario based there will be only one section
        if (sectionData.getScenarioQuestions() == null || sectionData.getScenarioQuestions().isEmpty()) {
            hideProcessDialog();
            SewaUtil.generateToast(context, "Question Configurations not found. Please refresh and try again.");
            finish();
            return;
        }

        if (sectionData.getScenarioTitle() != null) {
            caseTitle.setText(sectionData.getScenarioTitle());
        }

        if (sectionData.getScenarioDescription() != null) {
            caseDescription.setText(sectionData.getScenarioDescription());
        }

        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(sectionData.getMediaId(), sectionData.getMediaName());
        switch (sectionData.getMediaType()) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                addVideoLesson(path);
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                addImageForScenario(path);
                break;
            default:
                break;
        }

        if (sectionData.getAudio().getMediaId() != null && sectionData.getAudio().getMediaName() != null) {
            String audioPath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(sectionData.getAudio().getMediaId(), sectionData.getAudio().getMediaName());
            if (sectionData.getAudio().getAudioWithControls()) {
                addAudioLessonWithControls(audioPath);
            } else {
                addAudioLessonWithoutControls(audioPath);
            }
        }

        setupViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    caseDescription.setVisibility(View.VISIBLE);
                    caseImage.setVisibility(View.GONE);
                    flVideoView.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    caseDescription.setVisibility(View.GONE);
                    if (isMediaVideo) {
                        flVideoView.setVisibility(View.VISIBLE);
                    } else {
                        caseImage.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        hideProcessDialog();
    }

    private void addVideoLesson(final String path) {
        isMediaVideo = true;
        File file = new File(path);
        if (file.exists()) {
            if (!UtilBean.videoFileIsCorrupted(this, path)) {
                SewaUtil.generateToast(this, UtilBean.getMyLabel("Some error occurred. Please refresh and try again."));
                UtilBean.deleteFile(path);
                return;
            }

            FrameLayout layout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.lms_universal_video_view, null);
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
            myVideoView = layout.findViewById(R.id.videoView);
            MyVideoMediaController myVideoMediaController = layout.findViewById(R.id.media_controller);
            myVideoView.setMediaController(myVideoMediaController);
            flVideoView.addView(layout);
            myVideoView.setOnErrorListener((mp, what, extra) -> {
                SewaUtil.generateToast(this, UtilBean.getMyLabel("Some error occurred. Please refresh and try again."));
                UtilBean.deleteFile(path);
                return false;
            });
            setVideoCallback(myVideoView);
            myVideoMediaController.setTitle(sectionData.getScenarioTitle());
            myVideoView.setVideoURI(uri);
            myVideoView.seekTo(0);
            myVideoView.start();
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel("File not found. Please refresh and try again."));
        }
    }

    private void setVideoCallback(MyVideoView myVideoView) {
        myVideoView.setVideoViewCallback(new MyVideoView.VideoViewCallback() {
            final int cachedHeight = flVideoView.getLayoutParams().height;
            ActionBar actionBar;

            @Override
            public void onScaleChange(boolean isFullscreen) {
                isFullScreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = flVideoView.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    flVideoView.setLayoutParams(layoutParams);
                    viewPager.setVisibility(View.GONE);
                    caseTitle.setVisibility(View.GONE);
                    caseDescription.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                    actionBar = ((LmsSimulationActivity) context).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.hide();
                    }
                } else {
                    ViewGroup.LayoutParams layoutParams = flVideoView.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = this.cachedHeight;
                    flVideoView.setLayoutParams(layoutParams);
                    viewPager.setVisibility(View.VISIBLE);
                    caseTitle.setVisibility(View.VISIBLE);
                    caseDescription.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                    actionBar = ((LmsSimulationActivity) context).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.show();
                    }
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d(TAG, "onPause MyVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d(TAG, "onStart MyVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart MyVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd MyVideoView callback");
            }
        });
    }

    private void addAudioLessonWithControls(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (myAudioComponent == null) {
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                audioMediaPlayer = MediaPlayer.create(this, uri);
                myAudioComponent = new MyAudioComponent(this, sectionData.getScenarioTitle(), audioMediaPlayer);
            }
            myAudioComponent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            flAudioView.addView(myAudioComponent);
            flAudioView.setVisibility(View.VISIBLE);
            myAudioComponent.play();
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel("File not found. Please refresh and try again."));
        }
    }

    private void addAudioLessonWithoutControls(String path) {
        File file = new File(path);
        if (file.exists()) {
            flAudioView.addView(MyDynamicComponents.getAudioPlayerForSimulations(context, -1, path));
            flAudioView.setVisibility(View.VISIBLE);
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel("File not found. Please refresh and try again."));
        }
    }

    private void addImageForScenario(String path) {
        isMediaVideo = false;
        Drawable imageDrawable = MyStaticComponents.getImageDrawable(context, sectionData.getMediaId(), sectionData.getMediaName());
        if (imageDrawable != null) {
            caseImage.setImageDrawable(imageDrawable);
            caseImage.setOnClickListener(view -> {
                Intent intent = new Intent(context, CustomImageViewerActivity_.class);
                intent.putExtra("path", path);
                startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
            });
        }
    }

    private void setupViewPager(ViewPager2 viewPager) {
        viewPager.setUserInputEnabled(false);
        lmsQuestionConfigDataBeanList = sectionData.getScenarioQuestions();
        CaseStudyQueAdapter caseStudyQueAdapter = new CaseStudyQueAdapter(this, this, lmsQuestionConfigDataBeanList, this);
        viewPager.setAdapter(caseStudyQueAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View alertView = LayoutInflater.from(this).inflate(R.layout.custom_option_alert_box, null);
        alertDialog.setView(alertView);
        alertDialog.setCancelable(true);

        nextButton.setOnClickListener(view -> {
            if (selectedRadioButtonFromAdapter != null) {
                if (viewPager.getCurrentItem() != lmsQuestionConfigDataBeanList.size() - 1) {
                    for (LmsQuestionOptionDataBean option : lmsQuestionConfigDataBeanList.get(quePosition).getOptions()) {
                        if (option.getCorrect() && (option.getOptionTitle().equals(selectedRadioButtonFromAdapter.getText().toString())
                                || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButtonFromAdapter.getText().toString()))) {
                            isCorrectOptionSelected = true;
                            optionCorrectFeedback = option.getOptionFeedback().getFeedbackValue();
                            optionCorrectFeedbackBean = option.getOptionFeedback();
                        } else {
                            if (!option.getCorrect() && (option.getOptionTitle().equals(selectedRadioButtonFromAdapter.getText().toString())
                                    || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButtonFromAdapter.getText().toString()))) {
                                isCorrectOptionSelected = false;
                                optionInCorrectFeedback = option.getOptionFeedback().getFeedbackValue();
                                optionInCorrectFeedbackBean = option.getOptionFeedback();
                            }
                        }
                    }
                    if (isCorrectOptionSelected) {
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionTitle)).setText(Html.fromHtml("<font color='#03C988'><b>Great! Your answer is correct</b></font>"));
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionFeedback)).setText(optionCorrectFeedback);
                        ((MaterialButton) alertView.findViewById(R.id.buttonAlert)).setText(getResources().getString(R.string.ok));
                        if (optionCorrectFeedbackBean != null) {
                            Drawable optionDrawable = MyStaticComponents.getImageDrawable(context, optionCorrectFeedbackBean.getMediaId(), optionCorrectFeedbackBean.getMediaName());
                            if (optionDrawable != null) {
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setVisibility(View.VISIBLE);
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setImageDrawable(optionDrawable);
                            }
                        }
                        alertView.findViewById(R.id.buttonAlert).setOnClickListener(view1 -> {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            selectedRadioButtonFromAdapter = null;
                            alertDialog.dismiss();
                        });
                    } else {
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionTitle)).setText(Html.fromHtml("<font color='#F94A29'><b>Oops! Your answer is incorrect</b></font>"));
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionFeedback)).setText(optionInCorrectFeedback);
                        ((MaterialButton) alertView.findViewById(R.id.buttonAlert)).setText(getResources().getString(R.string.ok));
                        if (optionInCorrectFeedbackBean != null) {
                            Drawable optionDrawable = MyStaticComponents.getImageDrawable(context, optionInCorrectFeedbackBean.getMediaId(), optionInCorrectFeedbackBean.getMediaName());
                            if (optionDrawable != null) {
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setVisibility(View.VISIBLE);
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setImageDrawable(optionDrawable);
                            }
                        }
                        alertView.findViewById(R.id.buttonAlert).setOnClickListener(view1 -> {
                            alertDialog.dismiss();
                        });
                    }
                } else {
                    for (LmsQuestionOptionDataBean option : lmsQuestionConfigDataBeanList.get(quePosition).getOptions()) {
                        if (option.getCorrect() && (option.getOptionTitle().equals(selectedRadioButtonFromAdapter.getText().toString())
                                || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButtonFromAdapter.getText().toString()))) {
                            isCorrectOptionSelected = true;
                            optionCorrectFeedback = option.getOptionFeedback().getFeedbackValue();
                        } else {
                            if (!option.getCorrect() && (option.getOptionTitle().equals(selectedRadioButtonFromAdapter.getText().toString())
                                    || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButtonFromAdapter.getText().toString()))) {
                                isCorrectOptionSelected = false;
                                optionInCorrectFeedback = option.getOptionFeedback().getFeedbackValue();
                            }
                        }
                    }
                    if (isCorrectOptionSelected) {
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionTitle)).setText(Html.fromHtml("<font color='#03C988'><b>Great! Your answer is correct</b></font>"));
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionFeedback)).setText(optionCorrectFeedback);
                        ((MaterialButton) alertView.findViewById(R.id.buttonAlert)).setText(getResources().getString(R.string.ok));
                        if (optionCorrectFeedbackBean != null) {
                            Drawable optionDrawable = MyStaticComponents.getImageDrawable(context, optionCorrectFeedbackBean.getMediaId(), optionCorrectFeedbackBean.getMediaName());
                            if (optionDrawable != null) {
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setVisibility(View.VISIBLE);
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setImageDrawable(optionDrawable);
                            }
                        }
                        alertView.findViewById(R.id.buttonAlert).setOnClickListener(view1 -> {
                            if (testAndSetLmsAnswerDataBean()) {
                                alertDialog.dismiss();
                                showSubmitAlertDialog();
                            }
                        });
                    } else {
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionTitle)).setText(Html.fromHtml("<font color='#F94A29'><b>Oops! Your answer is incorrect</b></font>"));
                        ((AppCompatTextView) alertView.findViewById(R.id.tvOptionFeedback)).setText(optionInCorrectFeedback);
                        ((MaterialButton) alertView.findViewById(R.id.buttonAlert)).setText(getResources().getString(R.string.ok));
                        if (optionInCorrectFeedbackBean != null) {
                            Drawable optionDrawable = MyStaticComponents.getImageDrawable(context, optionInCorrectFeedbackBean.getMediaId(), optionInCorrectFeedbackBean.getMediaName());
                            if (optionDrawable != null) {
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setVisibility(View.VISIBLE);
                                ((AppCompatImageView) alertView.findViewById(R.id.ivOptionImage)).setImageDrawable(optionDrawable);
                            }
                        }
                        alertView.findViewById(R.id.buttonAlert).setOnClickListener(view1 -> {
                            alertDialog.dismiss();
                        });
                    }
                }
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            } else {
                SewaUtil.generateToast(this, "Please select at least one option");
            }
        });
    }

    private void showSubmitAlertDialog() {
        endDate = new Date();
        String msg = String.format("%s %s, %s", "You have successfully completed the quiz in", UtilBean.getTimeSpentFromMillis(endDate.getTime() - startDate.getTime()), "Please click on submit to submit your answers");
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(Html.fromHtml("<font color='#0081B4'><b>Well Done!!</b></font>"))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Submit", (dialog, which) -> {
                    saveQuizAndFinish();
                    alertDialog.dismiss();
                }).show();
    }

    private Boolean testAndSetLmsAnswerDataBean() {
        for (LmsQuestionConfigDataBean configDataBean : sectionData.getScenarioQuestions()) {
            RadioGroup radioGroup = radioGroupMapFromAdapter.get(configDataBean.getId());
            if (radioGroup == null || radioGroup.getCheckedRadioButtonId() == -1) {
                return false;
            }

            LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean = new LmsQuestionAnswerDataBean();
            lmsQuestionAnswerDataBean.setQueId(configDataBean.getId());
            lmsQuestionAnswerDataBean.setQueTitle(configDataBean.getQuestionTitle());

            RadioButton selectedRadioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
            for (LmsQuestionOptionDataBean option : configDataBean.getOptions()) {
                if (Boolean.TRUE.equals(option.getCorrect())) {
                    lmsQuestionAnswerDataBean.setCorrectAnswer(option.getOptionTitle());
                }
                if (option.getOptionTitle().equals(selectedRadioButton.getText().toString()) || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButton.getText().toString())) {
                    lmsQuestionAnswerDataBean.setAnswer(option.getOptionTitle());
                }
            }
            answerMap.put(configDataBean.getId(), lmsQuestionAnswerDataBean);
        }
        return true;
    }

    private void saveQuizAndFinish() {
        LmsQuestionSetAnswerDataBean questionSetAnswer = new LmsQuestionSetAnswerDataBean();
        questionSetAnswer.setUserId(SewaTransformer.loginBean.getUserID().intValue());
        questionSetAnswer.setQuestionSetId(questionSet.getActualId());
        questionSetAnswer.setStartDate(startDate);
        questionSetAnswer.setEndDate(endDate);
        questionSetAnswer.setAnswerJson(gson.toJson(answerMap.values()));
        lmsService.storeLmsTestResult(questionSetAnswer, testFor);
        updateNumberOfAttemptsForThisQuiz();
        SewaUtil.generateToast(context, "Quiz Completed");
        finish();
    }

    private void updateNumberOfAttemptsForThisQuiz() {
        LmsUserMetaData lmsUserMetaData = lmsService.getLmsUserMetaDataByCourseId(questionSet.getCourseId());
        boolean isUpdated = false;
        List<LmsUserQuizMetaData> attempts;
        if (lmsUserMetaData != null && lmsUserMetaData.getQuizMetaData() != null) {
            attempts = lmsUserMetaData.getQuizMetaData();
            for (LmsUserQuizMetaData attempt : attempts) {
                if (attempt.getQuizRefId() != null && attempt.getQuizRefId().equals(questionSet.getRefId())
                        && attempt.getQuizRefType() != null && attempt.getQuizRefType().equals(questionSet.getRefType())
                        && attempt.getQuizTypeId() != null && attempt.getQuizTypeId().equals(questionSet.getQuestionSetType())) {
                    attempt.setQuizAttempts(attempt.getQuizAttempts() != null ? attempt.getQuizAttempts() + 1 : 1);
                    attempt.setLatestScore(0);
                    isUpdated = true;
                    break;
                }
            }
        } else {
            attempts = new ArrayList<>();
        }

        if (!isUpdated) {
            LmsUserQuizMetaData attemptMetaData = new LmsUserQuizMetaData();
            attemptMetaData.setQuizAttempts(1);
            attemptMetaData.setQuizRefId(questionSet.getRefId());
            attemptMetaData.setQuizRefType(questionSet.getRefType());
            attemptMetaData.setQuizTypeId(questionSet.getQuestionSetType());
            attemptMetaData.setLatestScore(0);
            attempts.add(attemptMetaData);
        }

        if (lmsUserMetaData == null) {
            lmsUserMetaData = new LmsUserMetaData();
        }
        lmsUserMetaData.setQuizMetaData(attempts);
        lmsService.createLmsUserMetaDataBean(new LmsUserMetaDataBean(lmsUserMetaData));
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        menu.findItem(R.id.menu_about).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            /*if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            } else {
                onBackPressed();
            }*/
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.menu_home) {
            navigateToHomeScreen(false);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void getAnswersOfCaseStudy(Map<Integer, RadioGroup> radioGroupMap, Integer
            position, RadioButton selectedRadioButton) {
        quePosition = position;
        selectedRadioButtonFromAdapter = selectedRadioButton;
        radioGroupMapFromAdapter = radioGroupMap;
    }

    @Override
    public void onBackPressed() {

        if (isFullScreen && sectionData.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO) && myVideoView != null) {
            myVideoView.setFullscreen(false);
        }

        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.CLOSE_QUESTION_MODULE_ACTIVITY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.audioMediaPlayer != null) {
            audioMediaPlayer.pause();
        }
    }
}
