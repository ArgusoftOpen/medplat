package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.View.FOCUS_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static com.argusoft.sewa.android.app.component.MyStaticComponents.getLinearLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.FillInBlanksComponent;
import com.argusoft.sewa.android.app.component.MatchTheFollowingComponent;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.CheckBoxChangeListener;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsFIBQuestionAnswersDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionBankDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuizCompletionMessage;
import com.argusoft.sewa.android.app.databean.LmsQuizConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsSectionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserQuizMetaData;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.lms.LmsAnswerViewActivity_;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EActivity
public class LmsInteractiveQuestionModuleActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    LmsServiceImpl lmsService;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private NestedScrollView bodyScroll;
    private LinearLayout footerLayout;
    private Button nextButton;

    private String testFor;
    private LmsQuestionSetDataBean questionSet;
    private List<LmsSectionConfigDataBean> sectionConfigs;
    private LmsQuizConfigDataBean lmsQuizConfigDataBean;
    private LmsUserQuizMetaData attemptMetadata;

    private final Map<Integer, RadioGroup> radioGroupMap = new HashMap<>();
    private final Map<Integer, LinearLayout> checkBoxGroupMap = new HashMap<>();
    private final Map<Integer, FillInBlanksComponent> fillInTheBlanksMap = new HashMap<>();
    private final Map<Integer, QueFormBean> multiSelectQuestionMap = new HashMap<>();
    private final Map<Integer, MatchTheFollowingComponent> matchTheFollowingMap = new HashMap<>();
    private final Map<Integer, LmsQuestionAnswerDataBean> answerMap = new HashMap<>();

    private String screen;
    private Integer marksScored = 0;
    private Date startDate;
    private Date endDate;
    private final Gson gson = new Gson();

    Integer sectionCount;
    Integer currentSection = 0;
    Integer currentQuestion = 0;
    Boolean isVerified;
    LmsSectionConfigDataBean selectedSection;
    LmsQuestionConfigDataBean selectedQuestion;

    private Integer queCount = 0;

    private static final String QUIZ_START_SCREEN = "QUIZ_START_SCREEN";
    private static final String QUESTIONS_SCREEN = "QUESTIONS_SCREEN";
    private static final String SUBMIT_TEST_SCREEN = "SUBMIT_TEST_SCREEN";

    private Integer minHeight = null;

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
        setTitle(UtilBean.getTitleText(LabelConstants.LMS_QUIZ_TITLE));
        setSubTitle(testFor);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        menu.findItem(R.id.menu_about).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);
        return true;
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyScroll = globalPanel.findViewById(DynamicUtils.ID_BODY_SCROLL);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        getQuestionSetFromIntent();
        if (sectionConfigs == null || sectionConfigs.isEmpty()) {
            hideProcessDialog();
            SewaUtil.generateToast(context, "Question Section Configurations not found. Please refresh and try again.");
            finish();
            return;
        }
        setQuizStartScreen();
    }

    private void setQuizStartScreen() {
        screen = QUIZ_START_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.quiz_start_page, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        int margin = bodyLayoutContainer.getPaddingBottom() * -1;
        params.setMargins(margin, margin, margin, margin);
        layout.setLayoutParams(params);
        bodyLayoutContainer.addView(layout);

        TextView textView = layout.findViewById(R.id.questionCount);
        String postfix = sectionConfigs.size() == 1 ? "" : "s";
        textView.setText(String.format("%s section%s - %s", sectionConfigs.size(), postfix, getQuestionCountString()));

        textView = layout.findViewById(R.id.readyForQuiz);
        textView.setText(UtilBean.getMyLabel(getResources().getString(R.string.ready_for_the_quiz)));

        textView = layout.findViewById(R.id.testYourSkill);
        textView.setText(UtilBean.getMyLabel(getResources().getString(R.string.test_your_course_skill_in_our_short_quiz_section)));

        textView = layout.findViewById(R.id.putEverythingYouHaveLearned);
        textView.setText(UtilBean.getMyLabel(getResources().getString(R.string.put_everything_you_ve_learned_to_the_test_and_show_how_much_you_ve_learnt_so_far)));

        Button button = layout.findViewById(R.id.startButton);
        button.setText(UtilBean.getMyLabel(getResources().getString(R.string.let_s_start)));
        button.setOnClickListener(v -> {
            footerLayout.setVisibility(View.VISIBLE);
            startDate = new Date();
            setSectionQuestionsScreen();
        });
        hideProcessDialog();
    }

    private String getQuestionCountString() {
        int count = 0;
        for (LmsSectionConfigDataBean sectionConfig : sectionConfigs) {
            count += sectionConfig.getQuestions().size();
        }
        return String.format("%s questions", count);
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
                        sectionConfigs = gson.fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsSectionConfigDataBean>>() {
                        }.getType());
                        sectionCount = sectionConfigs.size() - 1;
                    }
                }

                LmsCourseDataBean lmsCourseDataBean = lmsService.retrieveCourseByCourseId(questionSet.getCourseId());
                if (questionSet.getQuestionSetType() != null && lmsCourseDataBean != null
                        && lmsCourseDataBean.getTestConfigJson() != null && lmsCourseDataBean.getTestConfigJson().containsKey(questionSet.getQuestionSetType())) {
                    lmsQuizConfigDataBean = lmsCourseDataBean.getTestConfigJson().get(questionSet.getQuestionSetType());
                }

                LmsUserMetaData lmsUserMetaData = lmsService.getLmsUserMetaDataByCourseId(questionSet.getCourseId());
                if (lmsUserMetaData != null && lmsUserMetaData.getQuizMetaData() != null) {
                    for (LmsUserQuizMetaData attempt : lmsUserMetaData.getQuizMetaData()) {
                        if (attempt.getQuizRefId().equals(questionSet.getRefId())
                                && attempt.getQuizRefType().equals(questionSet.getRefType())
                                && attempt.getQuizTypeId().equals(questionSet.getQuestionSetType())) {
                            attemptMetadata = attempt;

                            if (Boolean.TRUE.equals(attempt.getIsLocked())) {
                                hideProcessDialog();
                                SewaUtil.generateToast(context, "Sorry. You have already locked this quiz");
                                finish();
                            }
                            break;
                        }
                    }
                }

                if (lmsQuizConfigDataBean != null && lmsQuizConfigDataBean.getNoOfMaximumAttempts() != null
                        && attemptMetadata != null && attemptMetadata.getQuizAttempts() != null
                        && attemptMetadata.getQuizAttempts() >= lmsQuizConfigDataBean.getNoOfMaximumAttempts()) {
                    hideProcessDialog();
                    SewaUtil.generateToast(context, "Sorry. You have reached maximum number of attempts allowed for this Quiz");
                    finish();
                }
            }
        }
    }

    private Integer getMinHeight() {
        if (minHeight == null) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            minHeight = (int) typedValue.getDimension(metrics);
        }
        return minHeight;
    }

    private void setQuestionView() {
        queCount++;
        String questionTitle = selectedQuestion.getQuestionTitle();
        if (LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS.equals(selectedQuestion.getQuestionType())) {
            questionTitle = "Please fill in the blanks";
        }

        LinearLayout questionLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_quiz_question_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        int margin = bodyLayoutContainer.getPaddingBottom() * -1;
        params.setMargins(margin, 0, margin, 0);
        questionLayout.setLayoutParams(params);
        bodyLayoutContainer.addView(questionLayout);

        MaterialTextView questionLabelView = questionLayout.findViewById(R.id.questionLabel);
        questionLabelView.setText(String.format(Locale.getDefault(), "Q%d.", queCount));

        MaterialTextView questionTitleView = questionLayout.findViewById(R.id.questionTitle);
        questionTitleView.setText(questionTitle);

        // Adding image in question view
        Drawable imageDrawable = MyStaticComponents.getImageDrawable(context, selectedQuestion.getMediaId(), selectedQuestion.getMediaName());
        if (imageDrawable != null) {
            questionTitleView.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
            questionTitleView.setCompoundDrawablePadding(20);
        }

        LinearLayout optionsLayout = questionLayout.findViewById(R.id.answerLayout);
        optionsLayout.removeAllViews();

        int count;
        switch (selectedQuestion.getQuestionType()) {
            case LmsConstants.QUESTION_TYPE_SINGLE_SELECT:
                RadioGroup radioGroup = radioGroupMap.get(selectedQuestion.getId());
                if (radioGroup == null) {
                    radioGroup = new RadioGroup(context);
                    radioGroupMap.put(selectedQuestion.getId(), radioGroup);
                    count = 0;
                    RadioButton radioButton;
                    for (LmsQuestionOptionDataBean option : selectedQuestion.getOptions()) {
                        radioButton = new RadioButton(context);
                        radioButton.setLayoutParams(new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                        radioButton.setText(option.getOptionTitle());
                        radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.lms_radio_button_text_selector));
                        radioButton.setBackgroundResource(R.drawable.lms_radio_button_selector);
                        radioButton.setMinHeight(getMinHeight());
                        radioButton.setPadding(20, 20, 20, 20);
                        radioButton.setId(count++);

                        // Adding image in option view
                        imageDrawable = MyStaticComponents.getImageDrawable(context, option.getMediaId(), option.getMediaName());
                        if (imageDrawable != null) {
                            radioButton.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
                            radioButton.setCompoundDrawablePadding(20);
                            radioButton.setGravity(Gravity.CENTER_VERTICAL);
                        }

                        radioGroup.addView(radioButton);
                    }
                }
                optionsLayout.addView(radioGroup);
                break;

            case LmsConstants.QUESTION_TYPE_MULTI_SELECT:
                LinearLayout checkBoxGroup = checkBoxGroupMap.get(selectedQuestion.getId());
                if (checkBoxGroup == null) {
                    QueFormBean queFormBean = new QueFormBean();
                    multiSelectQuestionMap.put(selectedQuestion.getId(), queFormBean);
                    CheckBoxChangeListener listener = new CheckBoxChangeListener(queFormBean, true, false);
                    checkBoxGroup = getLinearLayout(context, -1, VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

                    List<OptionDataBean> options = new ArrayList<>();
                    int counter = 0;
                    for (LmsQuestionOptionDataBean lmsOption : selectedQuestion.getOptions()) {
                        OptionDataBean option = new OptionDataBean();
                        option.setKey(lmsOption.getOptionValue());
                        option.setValue(lmsOption.getOptionTitle());
                        option.setNext(null);
                        option.setRelatedProperty(null);
                        options.add(option);

                        MaterialCheckBox checkBox = MyStaticComponents.getCheckBox(context, lmsOption.getOptionValue(), counter++, false);
                        checkBox.setTextColor(ContextCompat.getColorStateList(context, R.color.lms_radio_button_text_selector));
                        checkBox.setBackgroundResource(R.drawable.lms_radio_button_selector);
                        checkBox.setMinHeight(getMinHeight());
                        checkBox.setPaddingRelative(20, 20, 20, 20);

                        // Adding image in option view
                        imageDrawable = MyStaticComponents.getImageDrawable(context, lmsOption.getMediaId(), lmsOption.getMediaName());
                        if (imageDrawable != null) {
                            checkBox.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
                            checkBox.setCompoundDrawablePadding(20);
                            checkBox.setGravity(Gravity.CENTER_VERTICAL);
                        }

                        checkBox.setOnCheckedChangeListener(listener);
                        checkBoxGroup.addView(checkBox);
                    }
                    listener.changeOptions(options);
                    checkBoxGroupMap.put(selectedQuestion.getId(), checkBoxGroup);
                }
                optionsLayout.addView(checkBoxGroup);
                break;

            case LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS:
                FillInBlanksComponent fillInBlanksComponent = fillInTheBlanksMap.get(selectedQuestion.getId());
                if (fillInBlanksComponent == null) {
                    fillInBlanksComponent = new FillInBlanksComponent(context, selectedQuestion.getQuestionTitle());
                    fillInTheBlanksMap.put(selectedQuestion.getId(), fillInBlanksComponent);
                }
                optionsLayout.addView(fillInBlanksComponent);
                break;

            case LmsConstants.QUESTION_TYPE_MATCH_THE_FOLLOWING:
                MatchTheFollowingComponent matchTheFollowingComponent = matchTheFollowingMap.get(selectedQuestion.getId());
                if (matchTheFollowingComponent == null) {
                    matchTheFollowingComponent = new MatchTheFollowingComponent(context, selectedQuestion.getLhs(), selectedQuestion.getRhs(), bodyScroll);
                    matchTheFollowingMap.put(selectedQuestion.getId(), matchTheFollowingComponent);
                }
                optionsLayout.addView(matchTheFollowingComponent);
                break;

            default:
        }
    }

    public void setSectionQuestionsScreen() {
        showProcessDialog();
        screen = QUESTIONS_SCREEN;
        bodyScroll.fullScroll(FOCUS_UP);
        bodyLayoutContainer.removeAllViews();
        selectedSection = sectionConfigs.get(currentSection);

        if (selectedSection.getQuestions() == null || selectedSection.getQuestions().isEmpty()) {
            hideProcessDialog();
            SewaUtil.generateToast(context, "Question Configurations not found. Please refresh and try again.");
            finish();
            return;
        }

        if (selectedSection.getSectionTitle() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(context, selectedSection.getSectionTitle()));
        }
        if (selectedSection.getSectionDescription() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedSection.getSectionDescription()));
        }

        selectedQuestion = selectedSection.getQuestions().get(currentQuestion);
        setQuestionView();
        isVerified = false;
        nextButton.setText(UtilBean.getMyLabel("Submit Answer"));
        hideProcessDialog();
    }

    private Integer validateAnswerAndGetResult(String correctAnswer, String answer) {
        return correctAnswer != null && correctAnswer.equalsIgnoreCase(answer) ? 1 : 0;
    }

    private Boolean testAndSetLmsAnswerDataBean() {
        LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean;
        List<String> correctAnswer;
        List<String> answer;
        int selectedSectionMarks = 0;

        switch (selectedQuestion.getQuestionType()) {
            case LmsConstants.QUESTION_TYPE_SINGLE_SELECT:
                RadioGroup radioGroup = radioGroupMap.get(selectedQuestion.getId());
                if (radioGroup == null || radioGroup.getCheckedRadioButtonId() == -1) {
                    return false;
                }

                lmsQuestionAnswerDataBean = new LmsQuestionAnswerDataBean();
                lmsQuestionAnswerDataBean.setQueId(selectedQuestion.getId());
                lmsQuestionAnswerDataBean.setQueTitle(selectedQuestion.getQuestionTitle());

                RadioButton selectedRadioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                for (LmsQuestionOptionDataBean option : selectedQuestion.getOptions()) {
                    if (Boolean.TRUE.equals(option.getCorrect())) {
                        lmsQuestionAnswerDataBean.setCorrectAnswer(option.getOptionTitle());
                    }
                    if (option.getOptionTitle().equals(selectedRadioButton.getText().toString()) || UtilBean.getMyLabel(option.getOptionTitle()).equals(selectedRadioButton.getText().toString())) {
                        lmsQuestionAnswerDataBean.setAnswer(option.getOptionTitle());
                    }
                }
                selectedSectionMarks += validateAnswerAndGetResult(lmsQuestionAnswerDataBean.getCorrectAnswer(), lmsQuestionAnswerDataBean.getAnswer());
                answerMap.put(selectedQuestion.getId(), lmsQuestionAnswerDataBean);
                break;

            case LmsConstants.QUESTION_TYPE_MULTI_SELECT:
                QueFormBean queFormBean = multiSelectQuestionMap.get(selectedQuestion.getId());
                if (queFormBean == null || queFormBean.getAnswer() == null) {
                    return false;
                }

                lmsQuestionAnswerDataBean = new LmsQuestionAnswerDataBean();
                lmsQuestionAnswerDataBean.setQueId(selectedQuestion.getId());
                lmsQuestionAnswerDataBean.setQueTitle(selectedQuestion.getQuestionTitle());
                correctAnswer = new ArrayList<>();
                List<String> strings = Arrays.asList(queFormBean.getAnswer().toString().split(","));
                answer = new ArrayList<>();

                for (LmsQuestionOptionDataBean option : selectedQuestion.getOptions()) {
                    if (Boolean.TRUE.equals(option.getCorrect())) {
                        correctAnswer.add(option.getOptionTitle());
                    }
                    if (strings.contains(option.getOptionValue())) {
                        answer.add(option.getOptionValue());
                    }
                }
                lmsQuestionAnswerDataBean.setCorrectAnswer(UtilBean.stringListJoin(correctAnswer, GlobalTypes.COMMA));
                lmsQuestionAnswerDataBean.setAnswer(UtilBean.stringListJoin(answer, GlobalTypes.COMMA));
                selectedSectionMarks += validateAnswerAndGetResult(lmsQuestionAnswerDataBean.getCorrectAnswer(), lmsQuestionAnswerDataBean.getAnswer());
                answerMap.put(selectedQuestion.getId(), lmsQuestionAnswerDataBean);
                break;

            case LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS:
                FillInBlanksComponent fillInBlanksComponent = fillInTheBlanksMap.get(selectedQuestion.getId());
                if (fillInBlanksComponent == null) {
                    return false;
                }

                answer = fillInBlanksComponent.getAnswers();
                answer.removeAll(Collections.singleton(null));
                if (answer.isEmpty() || answer.size() != selectedQuestion.getAnswers().size()) {
                    return false;
                }

                lmsQuestionAnswerDataBean = new LmsQuestionAnswerDataBean();
                lmsQuestionAnswerDataBean.setQueId(selectedQuestion.getId());
                lmsQuestionAnswerDataBean.setQueTitle(selectedQuestion.getQuestionTitle());
                correctAnswer = new ArrayList<>();

                for (LmsFIBQuestionAnswersDataBean answerDataBean : selectedQuestion.getAnswers()) {
                    correctAnswer.add(answerDataBean.getBlankNumber() - 1, answerDataBean.getBlankValue());
                    selectedSectionMarks += validateAnswerAndGetResult(answerDataBean.getBlankValue(), fillInBlanksComponent.getAnswers().get(answerDataBean.getBlankNumber() - 1));
                }
                lmsQuestionAnswerDataBean.setAnswer(UtilBean.stringListJoin(fillInBlanksComponent.getAnswers(), GlobalTypes.COMMA));
                lmsQuestionAnswerDataBean.setCorrectAnswer(UtilBean.stringListJoin(correctAnswer, GlobalTypes.COMMA));
                answerMap.put(selectedQuestion.getId(), lmsQuestionAnswerDataBean);
                break;

            case LmsConstants.QUESTION_TYPE_MATCH_THE_FOLLOWING:
                MatchTheFollowingComponent matchTheFollowingComponent = matchTheFollowingMap.get(selectedQuestion.getId());
                if (matchTheFollowingComponent == null) {
                    return false;
                }

                Map<String, String> answerPairs = gson.fromJson(selectedQuestion.getAnswerPairs(), new TypeToken<Map<String, String>>() {
                }.getType());

                Map<String, String> answers = matchTheFollowingComponent.getQuestionAnswersMap();
                if (answers.isEmpty() || answers.size() != answerPairs.size()) {
                    return false;
                }

                lmsQuestionAnswerDataBean = new LmsQuestionAnswerDataBean();
                lmsQuestionAnswerDataBean.setQueId(selectedQuestion.getId());
                lmsQuestionAnswerDataBean.setQueTitle(selectedQuestion.getQuestionTitle());

                for (Map.Entry<String, String> entry : answerPairs.entrySet()) {
                    selectedSectionMarks += validateAnswerAndGetResult(entry.getValue(), matchTheFollowingComponent.getQuestionAnswersMap().get(entry.getKey()));
                }

                lmsQuestionAnswerDataBean.setAnswer(gson.toJson(matchTheFollowingComponent.getQuestionAnswersMap()));
                lmsQuestionAnswerDataBean.setCorrectAnswer(selectedQuestion.getAnswerPairs().toString());
                answerMap.put(selectedQuestion.getId(), lmsQuestionAnswerDataBean);
                break;

            default:
                return false;
        }
        marksScored += selectedSectionMarks;
        return true;
    }

    private int getTotalMarks() {
        int total = 0;

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
        return total;
    }

    private void setSubmitScreen() {
        endDate = new Date();
        showProcessDialog();
        screen = SUBMIT_TEST_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyScroll.fullScroll(FOCUS_UP);
        footerLayout.setVisibility(View.GONE);

        LinearLayout scoreLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.quiz_end_page, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        int margin = bodyLayoutContainer.getPaddingBottom() * -1;
        params.setMargins(margin, margin, margin, margin);
        scoreLayout.setLayoutParams(params);
        bodyLayoutContainer.addView(scoreLayout);

        int totalMarks = getTotalMarks();
        ProgressBar progressBar = scoreLayout.findViewById(R.id.progressbar);
        progressBar.setMax(totalMarks);
        progressBar.setProgress(marksScored);

        String percentageText = (totalMarks == 0 ? 0 : ((marksScored * 100) / totalMarks)) + "%";
        TextView progressBarText = scoreLayout.findViewById(R.id.progressbarText);
        progressBarText.setText(percentageText);

        View separator1 = scoreLayout.findViewById(R.id.separator1);
        View separator2 = scoreLayout.findViewById(R.id.separator2);

        LinearLayout layer1 = scoreLayout.findViewById(R.id.layer1);
        LinearLayout layer2 = scoreLayout.findViewById(R.id.layer2);

        int count = 0;

        if (lmsQuizConfigDataBean != null) {
            //Show Quiz Completion Message in Submit Screen
            LmsQuizCompletionMessage quizCompletionMessage = lmsQuizConfigDataBean.getQuizCompletionMessage();
            if (quizCompletionMessage != null) {
                String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(quizCompletionMessage.getMediaId(), quizCompletionMessage.getMediaName());
                ImageView courseImage = scoreLayout.findViewById(R.id.courseImage);
                if (UtilBean.isFileExists(path)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    courseImage.setImageBitmap(bitmap);
                }

                String messageString = "Well done you've completed the quiz!";
                if (quizCompletionMessage.getMessage() != null) {
                    messageString = quizCompletionMessage.getMessage();
                }
                TextView message = scoreLayout.findViewById(R.id.message);
                message.setText(UtilBean.getMyLabel(messageString));
            }

            // Show attempts
            if (lmsQuizConfigDataBean.getNoOfMaximumAttempts() != null && lmsQuizConfigDataBean.getNoOfMaximumAttempts() > 0) {
                count += 1;
                int attempt = 1;
                if (attemptMetadata != null && attemptMetadata.getQuizAttempts() != null) {
                    attempt = attemptMetadata.getQuizAttempts() + 1;
                }
                setViewForScoreCard("Attempt", String.valueOf(attempt), count, layer1, layer2);
            }

            //Show Passing Marks
            if (Boolean.TRUE.equals(lmsQuizConfigDataBean.getDoYouWantAQuizToBeMarked()) && questionSet.getMinimumMarks() != null) {
                count += 1;
                setViewForScoreCard("Passing Marks", String.valueOf(questionSet.getMinimumMarks()), count, layer1, layer2);

                //Show Marks Scored
                if (Boolean.TRUE.equals(lmsQuizConfigDataBean.getShowQuizScoreQuizCompletion())) {
                    count += 1;
                    setViewForScoreCard("Marks Scored", String.format("%s / %s", marksScored, totalMarks), count, layer1, layer2);
                }
            }

            //Show Completion Time
            if (Boolean.TRUE.equals(lmsQuizConfigDataBean.getShowTimeTakenToCompleteTheQuiz())) {
                count += 1;
                setViewForScoreCard("Duration", UtilBean.getTimeSpentFromMillis(endDate.getTime() - startDate.getTime()), count, layer1, layer2);
            }

            //Allow option to restart quiz
            Button restartBtn = scoreLayout.findViewById(R.id.restartButton);
            restartBtn.setText(UtilBean.getMyLabel(getResources().getString(R.string.restart_quiz)));
            if (Boolean.TRUE.equals(lmsQuizConfigDataBean.getProvideOptionToRestartTestFromCompletionScreen())) {
                restartBtn.setOnClickListener(v -> {
                    saveQuizAndFinish();
                    Intent intent = new Intent(context, LmsQuestionModuleActivity_.class);
                    intent.putExtra("questionSet", gson.toJson(questionSet));
                    intent.putExtra("testFor", testFor);
                    startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
                });
            } else {
                restartBtn.setVisibility(View.GONE);
            }

            //Allow option to lock the quiz
            Button lockBtn = scoreLayout.findViewById(R.id.lockQuizButton);
            lockBtn.setText(UtilBean.getMyLabel(getResources().getString(R.string.lock_quiz_and_view_answer)));
            if (Boolean.TRUE.equals(lmsQuizConfigDataBean.getProvideOptionToRestartTestFromCompletionScreen())) {
                lockBtn.setOnClickListener(v -> {
                    View.OnClickListener onClickListener = v1 -> {
                        alertDialog.cancel();
                        if (v1.getId() == BUTTON_POSITIVE) {
                            saveQuizAndFinish();
                            Intent intent = new Intent(context, LmsAnswerViewActivity_.class);
                            intent.putExtra("questionSet", gson.toJson(questionSet));
                            intent.putExtra("testFor", testFor);
                            startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
                        }
                    };

                    alertDialog = new MyAlertDialog(context,
                            "Are you sure want to lock the quiz and view answers? After this all the attempts for this quiz will be exhausted.",
                            onClickListener, DynamicUtils.BUTTON_YES_NO);
                    alertDialog.show();
                });
            } else {
                lockBtn.setVisibility(View.GONE);
            }
        }

        if (count == 0) {
            separator1.setVisibility(View.GONE);
            separator2.setVisibility(View.GONE);
        }

        Button submitBtn = scoreLayout.findViewById(R.id.submitButton);
        submitBtn.setText(UtilBean.getMyLabel(getResources().getString(R.string.submit)));
        submitBtn.setOnClickListener(this);

        hideProcessDialog();
    }

    private void setViewForScoreCard(String label, String value, int count, LinearLayout layer1, LinearLayout layer2) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10);
        layoutParams.weight = 0.5f;

        LinearLayout layout = MyStaticComponents.getLinearLayout(context, -1, VERTICAL, layoutParams);
        layout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams labelLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        labelLayoutParams.setMargins(0, 0, 0, 5);

        TextView labelView = new TextView(context);
        labelView.setAllCaps(true);
        labelView.setTextColor(ContextCompat.getColor(context, R.color.white));
        labelView.setText(UtilBean.getMyLabel(label));
        labelView.setTextSize(15);
        labelView.setLayoutParams(labelLayoutParams);
        layout.addView(labelView);

        TextView answerView = new TextView(context);
        answerView.setTextColor(ContextCompat.getColor(context, R.color.white));
        answerView.setTypeface(null, Typeface.BOLD);
        answerView.setText(value);
        answerView.setTextSize(20);
        answerView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        layout.addView(answerView);

        if (count < 3) {
            layer1.addView(layout);
        } else {
            layer2.addView(layout);
        }
    }

    private void saveQuizAndFinish() {
        LmsQuestionSetAnswerDataBean questionSetAnswer = new LmsQuestionSetAnswerDataBean();
        questionSetAnswer.setUserId(SewaTransformer.loginBean.getUserID().intValue());
        questionSetAnswer.setQuestionSetId(questionSet.getActualId());
        questionSetAnswer.setPassingMarks(questionSet.getMinimumMarks());
        questionSetAnswer.setMarksScored(marksScored);
        if (questionSet.getMinimumMarks() != null) {
            questionSetAnswer.setPassed(marksScored != null && marksScored >= questionSet.getMinimumMarks());
        }
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
            attemptMetaData.setLatestScore(marksScored);
            attempts.add(attemptMetaData);
        }

        if (lmsUserMetaData == null) {
            lmsUserMetaData = new LmsUserMetaData();
        }
        lmsUserMetaData.setQuizMetaData(attempts);
        lmsService.createLmsUserMetaDataBean(new LmsUserMetaDataBean(lmsUserMetaData));
    }

    private void showVerifiedAnswer() {
        MaterialTextView correctAnswerLabel;
        int count;
        switch (selectedQuestion.getQuestionType()) {
            case LmsConstants.QUESTION_TYPE_SINGLE_SELECT:
                RadioGroup radioGroup = radioGroupMap.get(selectedQuestion.getId());
                if (radioGroup != null) {
                    count = 0;
                    for (LmsQuestionOptionDataBean option : selectedQuestion.getOptions()) {
                        RadioButton radioButton = radioGroup.findViewById(count);
                        radioButton.setEnabled(false);
                        if (Boolean.TRUE.equals(option.getCorrect())) {
                            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_correct));
                            radioButton.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                            radioButton.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_correct));
                            radioButton.setPadding(20, 20, 60, 20);
                        } else if (radioButton.getId() == radioGroup.getCheckedRadioButtonId()) {
                            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_incorrect));
                            radioButton.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                            radioButton.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_incorrect));
                            radioButton.setPadding(20, 20, 60, 20);
                        }
                        count++;
                    }
                    setCorrectIncorrectDescriptionForEachQuestion(answerMap.get(selectedQuestion.getId()));
                    setFeedbackContentForEachQuestion();
                }
                break;

            case LmsConstants.QUESTION_TYPE_MULTI_SELECT:
                QueFormBean queFormBean = multiSelectQuestionMap.get(selectedQuestion.getId());
                if (queFormBean == null || queFormBean.getAnswer() == null) {
                    return;
                }
                List<String> strings = Arrays.asList(queFormBean.getAnswer().toString().split(","));
                LinearLayout checkBoxGroup = checkBoxGroupMap.get(selectedQuestion.getId());
                if (checkBoxGroup != null) {
                    count = 0;
                    for (LmsQuestionOptionDataBean option : selectedQuestion.getOptions()) {
                        MaterialCheckBox checkBox = checkBoxGroup.findViewById(count);
                        checkBox.setEnabled(false);
                        if (Boolean.TRUE.equals(option.getCorrect())) {
                            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_correct));
                            checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                            checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_correct));
                            checkBox.setPaddingRelative(20, 20, 60, 20);
                        } else if (strings.contains(option.getOptionValue())) {
                            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_incorrect));
                            checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                            checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_incorrect));
                            checkBox.setPaddingRelative(20, 20, 60, 20);
                        }
                        count++;
                    }
                    setCorrectIncorrectDescriptionForEachQuestion(answerMap.get(selectedQuestion.getId()));
                    setFeedbackContentForEachQuestion();
                }
                break;

            case LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS:
                FillInBlanksComponent fillInBlanksComponent = fillInTheBlanksMap.get(selectedQuestion.getId());
                if (fillInBlanksComponent == null) {
                    return;
                }

                List<LmsFIBQuestionAnswersDataBean> answersDataBeans = selectedQuestion.getAnswers();
                Collections.sort(answersDataBeans, MyComparatorUtil.LMS_FIB_BLANKS_ORDER_COMPARATOR);
                List<EditText> editTextList = fillInBlanksComponent.getEditTextList();

                StringBuilder correctAnswer = new StringBuilder();
                int cnt = 0;
                for (LmsFIBQuestionAnswersDataBean answer : answersDataBeans) {
                    EditText editText = editTextList.get(cnt);
                    editText.setEnabled(false);
                    if (answer.getBlankValue().equalsIgnoreCase(editText.getText().toString().trim())) {
                        editText.setBackgroundResource(R.drawable.lms_edit_text_background_correct);
                        editText.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                    } else {
                        editText.setBackgroundResource(R.drawable.lms_edit_text_background_incorrect);
                        editText.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                    }
                    if (cnt > 0) {
                        correctAnswer.append("\n");
                    }
                    correctAnswer.append(answer.getBlankValue());
                    cnt++;
                }

                setCorrectIncorrectDescriptionForEachQuestion(answerMap.get(selectedQuestion.getId()));
                bodyLayoutContainer.addView(MyStaticComponents.getSeparator(context));
                correctAnswerLabel = MyStaticComponents.generateQuestionView(null, null, context, "Correct Answer:");
                correctAnswerLabel.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                bodyLayoutContainer.addView(correctAnswerLabel);
                bodyLayoutContainer.addView(MyStaticComponents.generateBoldAnswerView(context, correctAnswer.toString()));
                setFeedbackContentForEachQuestion();
                break;

            case LmsConstants.QUESTION_TYPE_MATCH_THE_FOLLOWING:
                MatchTheFollowingComponent matchTheFollowingComponent = matchTheFollowingMap.get(selectedQuestion.getId());
                if (matchTheFollowingComponent == null) {
                    return;
                }

                Map<String, String> answerPairs = gson.fromJson(selectedQuestion.getAnswerPairs(), new TypeToken<Map<String, String>>() {
                }.getType());
                for (Map.Entry<String, String> entry : answerPairs.entrySet()) {
                    boolean isCorrect = validateAnswerAndGetResult(entry.getValue(), matchTheFollowingComponent.getQuestionAnswersMap().get(entry.getKey())) == 1;

                    View answerView = matchTheFollowingComponent.getAnswerTagViewMap().get(entry.getValue());

                    if (answerView != null) {
                        if (matchTheFollowingComponent.isImageAvailable()) {
                            if (isCorrect) {
                                ((TextView) answerView).setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                answerView.setBackgroundResource(R.drawable.lms_match_image_ans_option_background_correct);
                            } else {
                                ((TextView) answerView).setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                answerView.setBackgroundResource(R.drawable.lms_match_image_ans_option_background_incorrect);
                            }
                        } else {
                            if (isCorrect) {
                                ((TextView) answerView).setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                answerView.setBackgroundResource(R.drawable.lms_match_text_ans_option_background_correct);
                            } else {
                                ((TextView) answerView).setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                answerView.setBackgroundResource(R.drawable.lms_match_text_ans_option_background_incorrect);
                            }
                        }
                    }

                    matchTheFollowingComponent.getAnswerTagViewMap().get(entry.getValue()).setEnabled(false);
                }

                bodyLayoutContainer.addView(MyStaticComponents.getSeparator(context));
                correctAnswerLabel = MyStaticComponents.generateQuestionView(null, null, context, "Correct Answer:");
                correctAnswerLabel.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                bodyLayoutContainer.addView(correctAnswerLabel);
                bodyLayoutContainer.addView(matchTheFollowingComponent.getCorrectAnswerLayout(answerPairs));
                break;

            default:
        }
        isVerified = true;
        nextButton.setText(GlobalTypes.EVENT_NEXT);
    }

    private void setCorrectIncorrectDescriptionForEachQuestion(LmsQuestionAnswerDataBean ans) {
        if (ans != null) {
            MaterialTextView textView;
            if (validateAnswerAndGetResult(ans.getCorrectAnswer(), ans.getAnswer()) == 1) {
                if (selectedQuestion.getCorrectAnswerDescription() != null) {
                    textView = MyStaticComponents.generateLabelView(context, selectedQuestion.getCorrectAnswerDescription());
                } else {
                    textView = MyStaticComponents.generateLabelView(context, "Yay! You have answered it correctly");
                }
                textView.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
            } else {
                if (selectedQuestion.getIncorrectAnswerDescription() != null) {
                    textView = MyStaticComponents.generateLabelView(context, selectedQuestion.getIncorrectAnswerDescription());
                } else {
                    textView = MyStaticComponents.generateLabelView(context, "Oops! You have answered it incorrectly");
                }
                textView.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
            }
            bodyLayoutContainer.addView(textView);
        }
    }

    private void setFeedbackContentForEachQuestion() {
        if (selectedQuestion.getInteractiveFeedbackContent() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.getSeparator(context));
            bodyLayoutContainer.addView(MyStaticComponents.generateBoldAnswerView(context, selectedQuestion.getInteractiveFeedbackContent()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (screen) {
            case QUESTIONS_SCREEN:
                if (Boolean.TRUE.equals(isVerified)) {
                    if (currentSection.equals(sectionCount) && currentQuestion.equals(selectedSection.getQuestions().size() - 1)) {
                        setSubmitScreen();
                    } else {
                        if (currentQuestion.equals(selectedSection.getQuestions().size() - 1)) {
                            currentQuestion = 0;
                            currentSection++;
                        } else {
                            currentQuestion++;
                        }
                        setSectionQuestionsScreen();
                    }
                } else if (Boolean.TRUE.equals(testAndSetLmsAnswerDataBean())) {
                    showVerifiedAnswer();
                } else {
                    SewaUtil.generateToast(context, "Please answer the question");
                }
                break;
            case SUBMIT_TEST_SCREEN:
                saveQuizAndFinish();
                break;
            default:
        }
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
                case QUESTIONS_SCREEN:
                    if (currentSection == 0 && currentQuestion == 0) {
                        onBackPressed();
                    } else {
                        SewaUtil.generateToast(context, "Sorry! You are not allowed to go back in this Quiz.");
                    }
                    break;
                case SUBMIT_TEST_SCREEN:
                    SewaUtil.generateToast(context, "Sorry! You can't go back from here. Please submit the test and proceed further.");
                    break;
                default:
                    finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        SewaUtil.generateToast(context, "Sorry! You are not allowed to cancel this Quiz.");
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.CLOSE_QUESTION_MODULE_ACTIVITY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }
}

