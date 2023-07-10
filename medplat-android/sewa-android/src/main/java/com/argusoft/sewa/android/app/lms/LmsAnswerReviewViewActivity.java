package com.argusoft.sewa.android.app.lms;

import static android.view.View.FOCUS_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static com.argusoft.sewa.android.app.component.MyStaticComponents.getLinearLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
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
import com.argusoft.sewa.android.app.activity.LoginActivity_;
import com.argusoft.sewa.android.app.activity.MenuActivity;
import com.argusoft.sewa.android.app.component.FillInBlanksComponent;
import com.argusoft.sewa.android.app.component.MatchTheFollowingComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.CheckBoxChangeListener;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsFIBQuestionAnswersDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionAnswerDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionBankDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuizConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsSectionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.argusoft.sewa.android.app.databean.LmsUserQuizMetaData;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EActivity
public class LmsAnswerReviewViewActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    LmsServiceImpl lmsService;

    private LinearLayout globalPanel;
    private NestedScrollView bodyScroll;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;

    private String testFor;
    private LmsQuestionSetDataBean questionSet;
    private List<LmsSectionConfigDataBean> sectionConfigs;
    private LmsQuizConfigDataBean lmsQuizConfigDataBean;
    private LmsUserQuizMetaData attemptMetadata;
    private List<LmsQuestionAnswerDataBean> lmsQuestionAnswerBeans = new ArrayList<>();

    private final Map<Integer, RadioGroup> radioGroupMap = new HashMap<>();
    private final Map<Integer, LinearLayout> checkBoxGroupMap = new HashMap<>();
    private final Map<Integer, FillInBlanksComponent> fillInTheBlanksMap = new HashMap<>();
    private final Map<Integer, QueFormBean> multiSelectQuestionMap = new HashMap<>();
    private final Map<Integer, MatchTheFollowingComponent> matchTheFollowingMap = new HashMap<>();
    private final Map<Integer, LinearLayout> questionLayoutMap = new HashMap<>();

    private String screen;
    private final Gson gson = new Gson();

    private Integer sectionCount;
    private Integer currentSection = 0;
    private LmsSectionConfigDataBean selectedSection;

    private static final String QUESTIONS_SCREEN = "QUESTIONS_SCREEN";

    private Integer minHeight = null;

    int queCount = 0;

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
        getQuestionSetFromIntent();
        if (sectionConfigs == null || sectionConfigs.isEmpty()) {
            hideProcessDialog();
            SewaUtil.generateToast(context, "Question Section Configurations not found. Please refresh and try again.");
            finish();
            return;
        }
        footerLayout.setVisibility(View.VISIBLE);
        setSectionQuestionsScreen();
    }

    private void getQuestionSetFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            testFor = extras.getString("testFor");
            String queSet = extras.getString("questionSet");
            String lmsQuestionAnswerBeanString = getIntent().getStringExtra("questionAnswerDataBeans");
            lmsQuestionAnswerBeans = gson.fromJson(lmsQuestionAnswerBeanString, new TypeToken<List<LmsQuestionAnswerDataBean>>() {
            }.getType());
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

    public void setSectionQuestionsScreen() {
        showProcessDialog();
        screen = QUESTIONS_SCREEN;
        bodyScroll.fullScroll(FOCUS_UP);
        bodyLayoutContainer.removeAllViews();
        if (currentSection == sectionConfigs.size() - 1) {
            footerLayout.setVisibility(View.GONE);
        } else {
            footerLayout.setVisibility(View.VISIBLE);
        }
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

        for (LmsQuestionConfigDataBean queConfig : selectedSection.getQuestions()) {
            queCount++;
            if (questionLayoutMap.get(queCount) != null) {
                bodyLayoutContainer.addView(questionLayoutMap.get(queCount));
            } else {
                LinearLayout questionLayout = getQuestionLayout(queConfig);
                questionLayoutMap.put(queCount, questionLayout);
                bodyLayoutContainer.addView(questionLayout);
            }
        }
        hideProcessDialog();
    }

    private LinearLayout getQuestionLayout(LmsQuestionConfigDataBean queConfig) {
        String questionTitle = queConfig.getQuestionTitle();
        if (LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS.equals(queConfig.getQuestionType())) {
            questionTitle = "Please fill in the blanks";
        }

        LinearLayout questionLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_quiz_question_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        int margin = bodyLayoutContainer.getPaddingBottom() * -1;
        params.setMargins(margin, 0, margin, 0);
        questionLayout.setLayoutParams(params);

        MaterialTextView questionLabelView = questionLayout.findViewById(R.id.questionLabel);
        questionLabelView.setText(String.format(Locale.getDefault(), "Q%d.", queCount));

        MaterialTextView questionTitleView = questionLayout.findViewById(R.id.questionTitle);
        questionTitleView.setText(questionTitle);

        // Adding image in question view
        Drawable imageDrawable = MyStaticComponents.getImageDrawable(context, queConfig.getMediaId(), queConfig.getMediaName());
        if (imageDrawable != null) {
            questionTitleView.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
            questionTitleView.setCompoundDrawablePadding(20);
        }

        LinearLayout optionsLayout = questionLayout.findViewById(R.id.answerLayout);
        optionsLayout.removeAllViews();

        switch (queConfig.getQuestionType()) {
            case LmsConstants.QUESTION_TYPE_SINGLE_SELECT:
                RadioGroup radioGroup = radioGroupMap.get(queConfig.getId());
                if (radioGroup == null) {
                    radioGroup = new RadioGroup(context);
                    radioGroupMap.put(queConfig.getId(), radioGroup);
                    RadioButton radioButton;
                    for (LmsQuestionOptionDataBean option : queConfig.getOptions()) {
                        radioButton = new RadioButton(context);
                        radioButton.setLayoutParams(new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                        radioButton.setText(option.getOptionTitle());
                        radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.lms_radio_button_text_selector));
                        radioButton.setBackgroundResource(R.drawable.lms_radio_button_selector);
                        radioButton.setMinHeight(getMinHeight());
                        radioButton.setPadding(20, 20, 20, 20);
                        radioButton.setEnabled(false);

                        // Adding image in option view
                        imageDrawable = MyStaticComponents.getImageDrawable(context, option.getMediaId(), option.getMediaName());
                        if (imageDrawable != null) {
                            radioButton.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
                            radioButton.setCompoundDrawablePadding(20);
                            radioButton.setGravity(Gravity.CENTER_VERTICAL);
                        }

                        radioGroup.addView(radioButton);
                        for (LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean : lmsQuestionAnswerBeans) {
                            if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && lmsQuestionAnswerDataBean.getAnswer().equals(option.getOptionValue()) && !option.getCorrect()) {
                                radioButton.setSelected(true);
                                radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_edit_text_background_incorrect));
                                radioButton.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                radioButton.setButtonTintList(ContextCompat.getColorStateList(context, R.color.rb_incorrect_text));
                                radioButton.setPadding(20, 20, 60, 20);
                            } else if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && lmsQuestionAnswerDataBean.getAnswer().equals(option.getOptionValue()) && option.getCorrect()) {
                                radioButton.setSelected(true);
                                radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_correct));
                                radioButton.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                radioButton.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_correct));
                                radioButton.setPadding(20, 20, 60, 20);
                            }
                        }
                    }
                }
                radioGroup.setEnabled(false);
                optionsLayout.addView(radioGroup);
                break;

            case LmsConstants.QUESTION_TYPE_MULTI_SELECT:
                LinearLayout checkBoxGroup = checkBoxGroupMap.get(queConfig.getId());
                if (checkBoxGroup == null) {
                    QueFormBean queFormBean = new QueFormBean();
                    multiSelectQuestionMap.put(queConfig.getId(), queFormBean);
                    CheckBoxChangeListener listener = new CheckBoxChangeListener(queFormBean, true, false);
                    checkBoxGroup = getLinearLayout(context, -1, VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

                    List<OptionDataBean> options = new ArrayList<>();
                    int counter = 0;
                    for (LmsQuestionOptionDataBean lmsOption : queConfig.getOptions()) {
                        OptionDataBean option = new OptionDataBean();
                        option.setKey(lmsOption.getOptionValue());
                        option.setValue(lmsOption.getOptionTitle());
                        option.setNext(null);
                        option.setRelatedProperty(null);
                        options.add(option);

                        CheckBox checkBox = MyStaticComponents.getCheckBox(context, lmsOption.getOptionValue(), counter++, false);
                        checkBox.setTextColor(ContextCompat.getColorStateList(context, R.color.lms_radio_button_text_selector));
                        checkBox.setBackgroundResource(R.drawable.lms_radio_button_selector);
                        checkBox.setMinHeight(getMinHeight());
                        checkBox.setPaddingRelative(20, 20, 20, 20);
                        checkBox.setEnabled(false);

                        // Adding image in option view
                        imageDrawable = MyStaticComponents.getImageDrawable(context, lmsOption.getMediaId(), lmsOption.getMediaName());
                        if (imageDrawable != null) {
                            checkBox.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
                            checkBox.setCompoundDrawablePadding(20);
                            checkBox.setGravity(Gravity.CENTER_VERTICAL);
                        }

                        for (LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean : lmsQuestionAnswerBeans) {
                            if (lmsQuestionAnswerDataBean.getAnswer().length() > 2) {
                                String[] answerString = lmsQuestionAnswerDataBean.getAnswer().split(",");
                                for (String answer : answerString) {
                                    if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && answer.equals(lmsOption.getOptionValue()) && !lmsOption.getCorrect()) {
                                        checkBox.setChecked(true);
                                        checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_edit_text_background_incorrect));
                                        checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                        checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.rb_incorrect_text));
                                        checkBox.setPaddingRelative(20, 20, 60, 20);
                                    } else if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && answer.equals(lmsOption.getOptionValue()) && lmsOption.getCorrect()) {
                                        checkBox.setChecked(true);
                                        checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_correct));
                                        checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                        checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_correct));
                                        checkBox.setPaddingRelative(20, 20, 60, 20);
                                    }
                                }
                            } else {
                                if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && lmsQuestionAnswerDataBean.getAnswer().equals(lmsOption.getOptionValue()) && !lmsOption.getCorrect()) {
                                    checkBox.setChecked(true);
                                    checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_edit_text_background_incorrect));
                                    checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                    checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.rb_incorrect_text));
                                    checkBox.setPaddingRelative(20, 20, 60, 20);
                                } else if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId()) && lmsQuestionAnswerDataBean.getAnswer().equals(lmsOption.getOptionValue()) && lmsOption.getCorrect()) {
                                    checkBox.setChecked(true);
                                    checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.lms_radio_button_background_correct));
                                    checkBox.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                    checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.cb_tick_correct));
                                    checkBox.setPaddingRelative(20, 20, 60, 20);
                                }
                            }
                        }
                        checkBoxGroup.addView(checkBox);
                    }
                    listener.changeOptions(options);
                    checkBoxGroupMap.put(queConfig.getId(), checkBoxGroup);
                }
                optionsLayout.addView(checkBoxGroup);
                break;

            case LmsConstants.QUESTION_TYPE_FILL_IN_THE_BLANKS:
                FillInBlanksComponent fillInBlanksComponent = fillInTheBlanksMap.get(queConfig.getId());
                if (fillInBlanksComponent == null) {
                    fillInBlanksComponent = new FillInBlanksComponent(context, queConfig.getQuestionTitle());
                    fillInTheBlanksMap.put(queConfig.getId(), fillInBlanksComponent);
                }

                List<LmsFIBQuestionAnswersDataBean> answersDataBeans = queConfig.getAnswers();
                Collections.sort(answersDataBeans, MyComparatorUtil.LMS_FIB_BLANKS_ORDER_COMPARATOR);
                List<EditText> editTextList = fillInBlanksComponent.getEditTextList();

                int cnt = 0;

                for (LmsFIBQuestionAnswersDataBean answer : answersDataBeans) {
                    EditText editText = editTextList.get(cnt);
                    editText.setEnabled(false);
                    for (LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean : lmsQuestionAnswerBeans) {
                        if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId())) {
                            if (lmsQuestionAnswerDataBean.getAnswer().equals(answer.getBlankValue())) {
                                editText.setBackgroundResource(R.drawable.lms_edit_text_background_correct);
                                editText.setTextColor(ContextCompat.getColor(context, R.color.rb_correct_text));
                                editText.setText(answer.getBlankValue());
                            } else {
                                editText.setBackgroundResource(R.drawable.lms_edit_text_background_incorrect);
                                editText.setTextColor(ContextCompat.getColor(context, R.color.rb_incorrect_text));
                                editText.setText(lmsQuestionAnswerDataBean.getAnswer());
                            }
                        }
                    }
                    cnt++;
                }

                optionsLayout.addView(fillInBlanksComponent);
                break;

            case LmsConstants.QUESTION_TYPE_MATCH_THE_FOLLOWING:
                MatchTheFollowingComponent matchTheFollowingComponent = matchTheFollowingMap.get(queConfig.getId());
                if (matchTheFollowingComponent == null) {
                    matchTheFollowingComponent = new MatchTheFollowingComponent(context, queConfig.getLhs(), queConfig.getRhs(), bodyScroll);
                    matchTheFollowingMap.put(queConfig.getId(), matchTheFollowingComponent);
                }

                Map<String, String> answerPairs = gson.fromJson(queConfig.getAnswerPairs(), new TypeToken<Map<String, String>>() {
                }.getType());

                Map<String, String> givenAnswerPairs = null;
                for (LmsQuestionAnswerDataBean lmsQuestionAnswerDataBean : lmsQuestionAnswerBeans){
                    if (lmsQuestionAnswerDataBean.getQueId().equals(queConfig.getId())){
                        givenAnswerPairs = gson.fromJson(lmsQuestionAnswerDataBean.getAnswer(), new TypeToken<Map<String, String>>() {
                        }.getType());
                    }
                }

                if (givenAnswerPairs != null){
                    optionsLayout.addView(matchTheFollowingComponent.getCorrectAndIncorrectAnswerLayout(answerPairs, givenAnswerPairs));
                }

                optionsLayout.setPadding(20, 20, 20, 20);
                break;

            default:
        }
        return questionLayout;
    }

    @Override
    public void onClick(View v) {
        if (QUESTIONS_SCREEN.equals(screen)) {
            currentSection++;
            setSectionQuestionsScreen();
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
            if (QUESTIONS_SCREEN.equals(screen)) {
                if (currentSection == 0) {
                    onBackPressed();
                } else {
                    currentSection--;
                    queCount = queCount - selectedSection.getQuestions().size() - sectionConfigs.get(currentSection).getQuestions().size();
                    setSectionQuestionsScreen();
                }
            } else {
                finish();
            }
            return true;
        }
        return false;
    }
}
