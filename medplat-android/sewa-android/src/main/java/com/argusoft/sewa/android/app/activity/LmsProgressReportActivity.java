package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionBankDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsSectionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserLessonMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserLessonSessionMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserQuizMetaData;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.lms.LmsCourseListAdaptor;
import com.argusoft.sewa.android.app.lms.LmsCourseScreenDataBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class LmsProgressReportActivity extends MenuActivity {

    @Bean
    LmsServiceImpl lmsService;
    @Bean
    SewaFhsServiceImpl fhsService;

    private static final String COURSE_LIST_SCREEN = "COURSE_LIST_SCREEN";
    private static final String COURSE_DETAIL_SCREEN = "COURSE_DETAIL_SCREEN";

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;

    private List<LmsCourseDataBean> courses;
    private Map<Integer, LmsCourseDataBean> coursesMap;
    private LmsCourseDataBean selectedCourse;
    private LmsUserMetaData userMetaData;
    private String screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.LMS_PROGRESS_REPORT_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, null);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        globalPanel.findViewById(DynamicUtils.ID_FOOTER).setVisibility(View.GONE);
        setBodyDetail();
    }

    public void setBodyDetail() {
        retrieveCourseList();
    }

    public void retrieveCourseList() {
        courses = lmsService.retrieveCourses();
        setCourseListScreen();
    }

    public void setCourseListScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = COURSE_LIST_SCREEN;
        coursesMap = new HashMap<>();

        if (courses == null || courses.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, "No information to show at this time. Check back after you start a course to see progress details"));
            hideProcessDialog();
            return;
        }


        List<LmsCourseScreenDataBean> screenDataBeans = new ArrayList<>();
        int count = 0;
        for (LmsCourseDataBean bean : courses) {
            if (bean.getCompletionStatus() <= 0) {
                continue;
            }
            coursesMap.put(count, bean);
            Bitmap image = null;
            if (bean.getCourseImage() != null) {
                String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(bean.getCourseImage().getMediaId(), bean.getCourseImage().getMediaName());
                if (UtilBean.isFileExists(path)) {
                    image = BitmapFactory.decodeFile(path);
                }
            }
            int getQuestionSetSize = 0;
            if (bean.getQuestionSet() != null) {
                getQuestionSetSize = bean.getQuestionSet().size();
            }
            int getTopicsSize = 0;
            if (bean.getTopics() != null) {
                getTopicsSize = bean.getTopics().size();
            }
            screenDataBeans.add(new LmsCourseScreenDataBean(image,  bean.getCourseId(), bean.getCourseName(), bean.getCourseDescription(), bean.getCompletionStatus(), getQuestionSetSize, getTopicsSize,true, 0L));
            count++;
        }

        if (count == 0) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, "No information to show at this time. Check back after you start a course to see progress details"));
            hideProcessDialog();
            return;
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedCourse = coursesMap.get(position);
            openCourseDetailScreen();
        };
        LmsCourseListAdaptor adapter = new LmsCourseListAdaptor(context,
                screenDataBeans, false, false, lmsService) {
            @Override
            public void onAdapterItemClick(int position) {
                selectedCourse = coursesMap.get(position);
                openCourseDetailScreen();
            }

            @Override
            public void onMenuClick(int position, View view) {

            }
        };

        RecyclerView listView = new RecyclerView(context);
        listView.setLayoutManager(new LinearLayoutManager(this));
//            listView.setDivider(ContextCompat.getDrawable(context, R.color.transparent));
        listView.setNestedScrollingEnabled(true);
//            listView.setDividerHeight(30);
        listView.setAdapter(adapter);
//            listView.setOnItemClickListener(onItemClickListener);
        bodyLayoutContainer.addView(listView);

        hideProcessDialog();
    }

    @SuppressLint("RestrictedApi")
    public void openCourseDetailScreen() {
        showProcessDialog();
        userMetaData = lmsService.getLmsUserMetaDataByCourseId(selectedCourse.getCourseId());
        bodyLayoutContainer.removeAllViews();
        screen = COURSE_DETAIL_SCREEN;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_report_course_details, null);

        MaterialTextView courseTitle = layout.findViewById(R.id.course_title);
        ImageView courseImage = layout.findViewById(R.id.course_image);

        courseTitle.setText(UtilBean.getMyLabel(selectedCourse.getCourseName()));

        if (selectedCourse.getCourseImage() != null) {
            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(selectedCourse.getCourseImage().getMediaId(), selectedCourse.getCourseImage().getMediaName());
            if (UtilBean.isFileExists(path)) {
                courseImage.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }

        MaterialTextView timeSpentLabel = layout.findViewById(R.id.time_spent_label);
        MaterialTextView timeSpent = layout.findViewById(R.id.time_spent);
        MaterialTextView completionStatusLabel = layout.findViewById(R.id.completion_status_label);
        ProgressBar progressBar = layout.findViewById(R.id.progressbar);
        MaterialTextView progressBarText = layout.findViewById(R.id.progressbarText);
        MaterialTextView mostWatchedLessonLabel = layout.findViewById(R.id.most_watched_lesson_label);
        MaterialTextView mostWatchedLesson = layout.findViewById(R.id.most_watched_lesson);

        long courseSpent = 0L;
        Map<Integer, Long> lessonTimeMap = new HashMap<>();
        LmsLessonDataBean lessonDataBean = null;

        if (userMetaData != null && userMetaData.getLessonMetaData() != null) {
            for (LmsUserLessonMetaData lessonMetaData : userMetaData.getLessonMetaData()) {
                long lessonSpent = 0L;
                if (lessonMetaData.getSessions() != null) {
                    for (LmsUserLessonSessionMetaData session : lessonMetaData.getSessions()) {
                        courseSpent = courseSpent + (session.getEndDate() - session.getStartDate());
                        lessonSpent = lessonSpent + (session.getEndDate() - session.getStartDate());
                    }
                }
                lessonTimeMap.put(lessonMetaData.getLessonId(), lessonSpent);
            }
        }

        Map.Entry<Integer, Long> entryWithMaxValue = null;
        for (Map.Entry<Integer, Long> entry : lessonTimeMap.entrySet()) {
            if (entryWithMaxValue == null || entry.getValue().compareTo(entryWithMaxValue.getValue()) > 0) {
                entryWithMaxValue = entry;
            }
        }

        if (entryWithMaxValue != null) {
            for (LmsTopicDataBean topic : selectedCourse.getTopics()) {
                for (LmsLessonDataBean lesson : topic.getTopicMedias()) {
                    if (lesson.getActualId().equals(entryWithMaxValue.getKey())) {
                        lessonDataBean = lesson;
                        break;
                    }
                }
            }
        }

        timeSpentLabel.setText(UtilBean.getMyLabel("Total\nTime\nSpent"));
        timeSpent.setText(UtilBean.getTimeSpentFromMillis(courseSpent));

        completionStatusLabel.setText(UtilBean.getMyLabel("Completion\nStatus"));
        progressBar.setProgress(selectedCourse.getCompletionStatus());
        progressBarText.setText(selectedCourse.getCompletionStatus() + "%");

        mostWatchedLessonLabel.setText(UtilBean.getMyLabel("Most\nWatched\nLesson"));
        if (lessonDataBean != null) {
            mostWatchedLesson.setText(UtilBean.getMyLabel(lessonDataBean.getTitle()));
            mostWatchedLesson.setAutoSizeTextTypeUniformWithConfiguration(8, 16, 1, TypedValue.COMPLEX_UNIT_SP);
        } else {
            mostWatchedLesson.setText(UtilBean.getMyLabel(LabelConstants.N_A));
        }

        bodyLayoutContainer.addView(layout);

        View separatorView = new View(context);
        separatorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 20));
        separatorView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        bodyLayoutContainer.addView(separatorView);

        addQuizDetails();
        hideProcessDialog();
    }

    private void addQuizDetails() {
        MaterialTextView textView = MyStaticComponents.generateLabelView(context, "Click on a Quiz to view its details");
        bodyLayoutContainer.addView(textView);
        int count = 1;
        for (LmsTopicDataBean topic : selectedCourse.getTopics()) {
            for (LmsLessonDataBean lesson : topic.getTopicMedias()) {
                if (lesson.getQuestionSet() != null) {
                    for (LmsQuestionSetDataBean questionSet : lesson.getQuestionSet()) {
                        addQuizDetailsForEachQuiz(questionSet, count + ". " + lesson.getTitle(), lesson.getActualId(), "LESSON");
                        count++;
                    }
                }
            }
            if (topic.getQuestionSet() != null) {
                for (LmsQuestionSetDataBean questionSet : topic.getQuestionSet()) {
                    addQuizDetailsForEachQuiz(questionSet, count + ". " + topic.getTopicName(), topic.getTopicId(), "MODULE");
                    count++;
                }
            }
        }
        if (selectedCourse.getQuestionSet() != null) {
            for (LmsQuestionSetDataBean questionSet : selectedCourse.getQuestionSet()) {
                addQuizDetailsForEachQuiz(questionSet, count + ". " + selectedCourse.getCourseName(), selectedCourse.getCourseId(), "COURSE");
                count++;
            }
        }
        if (count == 1) {
            bodyLayoutContainer.removeView(textView);
        }
    }

    private void addQuizDetailsForEachQuiz(LmsQuestionSetDataBean questionSet, String title, Integer refId, String refType) {
        LmsUserQuizMetaData quizMetaData = null;
        if (userMetaData != null && userMetaData.getQuizMetaData() != null) {
            for (LmsUserQuizMetaData userQuizMetaData : userMetaData.getQuizMetaData()) {
                if (userQuizMetaData.getQuizRefId().equals(refId) && userQuizMetaData.getQuizRefType().equals(refType)) {
                    quizMetaData = userQuizMetaData;
                    break;
                }
            }
        }

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_report_quiz_details, null);
        LinearLayout quizTitleLayout = layout.findViewById(R.id.quiz_title_layout);
        MaterialTextView quizTitle = layout.findViewById(R.id.quiz_title);
        final ImageView quizArrow = layout.findViewById(R.id.quiz_arrow);
        final LinearLayout quizContent = layout.findViewById(R.id.quiz_contents);

        String quizTitleStr;
        if (fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString()) != null) {
            quizTitleStr = UtilBean.getMyLabel(title) + " (" + fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString()) + ")";
        } else {
            quizTitleStr = UtilBean.getMyLabel(title);
        }
        quizTitle.setText(quizTitleStr);

        quizTitleLayout.setOnClickListener(v -> {
            if (quizContent.getVisibility() == View.VISIBLE) {
                quizArrow.setImageResource(R.drawable.ic_arrow_down);
                quizContent.setVisibility(View.GONE);
            } else {
                quizArrow.setImageResource(R.drawable.ic_arrow_up);
                quizContent.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout totalAttemptLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.horizontal_key_value_pair_layout, null);
        MaterialTextView totalAttemptLabel = totalAttemptLayout.findViewById(R.id.keyLabel);
        MaterialTextView totalAttemptValue = totalAttemptLayout.findViewById(R.id.valueLabel);
        totalAttemptLabel.setText(UtilBean.getMyLabel("Total attempts"));
        if (quizMetaData != null && quizMetaData.getQuizAttempts() != null) {
            totalAttemptValue.setText(String.valueOf(quizMetaData.getQuizAttempts()));
        } else {
            totalAttemptValue.setText("0");
        }

        LinearLayout attemptToPassLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.horizontal_key_value_pair_layout, null);
        MaterialTextView attemptToPassLabel = attemptToPassLayout.findViewById(R.id.keyLabel);
        MaterialTextView attemptToPassValue = attemptToPassLayout.findViewById(R.id.valueLabel);
        attemptToPassLabel.setText(UtilBean.getMyLabel("Attempts to pass the quiz"));
        if (quizMetaData != null && quizMetaData.getQuizAttemptsToPass() != null) {
            attemptToPassValue.setText(String.valueOf(quizMetaData.getQuizAttemptsToPass()));
        } else {
            attemptToPassValue.setText(LabelConstants.N_A);
        }

        LinearLayout scoreInLastAttemptLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.horizontal_key_value_pair_layout, null);
        MaterialTextView scoreInLastAttemptLabel = scoreInLastAttemptLayout.findViewById(R.id.keyLabel);
        MaterialTextView scoreInLastAttemptValue = scoreInLastAttemptLayout.findViewById(R.id.valueLabel);
        scoreInLastAttemptLabel.setText(UtilBean.getMyLabel("Score in last attempt"));
        if (quizMetaData != null && quizMetaData.getLatestScore() != null) {
            Integer totalMarks = getTotalMarksInAQuiz(questionSet);
            String percentageText = (totalMarks == 0 ? 0 : ((quizMetaData.getLatestScore() * 100) / totalMarks)) + "%";
            scoreInLastAttemptValue.setText(percentageText);
        } else {
            scoreInLastAttemptValue.setText(LabelConstants.N_A);
        }

        quizContent.addView(totalAttemptLayout);
        if (questionSet.getQuestionSetType() != 2640) { //todo make id value dynamic as per field value name
            quizContent.addView(attemptToPassLayout);
            quizContent.addView(scoreInLastAttemptLayout);
        }
        bodyLayoutContainer.addView(layout);
    }

    private Integer getTotalMarksInAQuiz(LmsQuestionSetDataBean questionSet) {
        List<LmsSectionConfigDataBean> sectionConfigs = new ArrayList<>();
        if (questionSet.getQuestionBank() != null && !questionSet.getQuestionBank().isEmpty()) {
            LmsQuestionBankDataBean questionBank = questionSet.getQuestionBank().get(0);
            if (questionBank.getConfigJson() != null && !questionBank.getConfigJson().isEmpty()) {
                sectionConfigs = new Gson().fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsSectionConfigDataBean>>() {
                }.getType());
            }
        }
        int total = 0;
        //todo change id to dynamic
        if (questionSet.getQuestionSetType() != 2640) { //todo make id value dynamic as per field value name
            for (LmsSectionConfigDataBean section : sectionConfigs) {
                for (LmsQuestionConfigDataBean question : section.getQuestions()) {
                    if (LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS.equals(question.getQuestionType())) {
                        total += question.getAnswers().size();
                    } else if (LmsConstants.QUESTION_TYPE_MATCH_THE_FOLLOWING.equals(question.getQuestionType())) {
                        total += question.getLhs().size();
                    } else {
                        total += 1;
                    }
                }
            }
        }
        return total;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case COURSE_LIST_SCREEN:
                    finish();
                    break;
                case COURSE_DETAIL_SCREEN:
                    setCourseListScreen();
                    break;
                default:
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.CLOSE_LMS_PROGRESS_REPORT_ACTIVITY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }
}
