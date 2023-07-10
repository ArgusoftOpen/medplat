package com.argusoft.sewa.android.app.datastructure;

import android.content.Context;

import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.DynamicFormActivity;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.service.GPSTracker;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.PointInPolygon;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.List;
import java.util.Map;

import static com.argusoft.sewa.android.app.datastructure.SharedStructureData.mapIndexPage;
import static com.argusoft.sewa.android.app.datastructure.SharedStructureData.mapIndexQuestion;

/**
 * @author alpeshkyada
 */
public class FormEngine implements View.OnClickListener {

    private static int next = 0;
    private static int firstPageNo = 1;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private NestedScrollView scrollView;
    private Context context;
    private String formType;
    private Button nextButton;
    private PageFormBean currentPage; // navigator's current page
    private GPSTracker gps;
    private boolean isVaccinationsPage = false;

    private FormEngine(Context context, String formType) {
        this.context = context;
        this.formType = formType;
        PageFormBean.context = context;
        gps = SharedStructureData.gps;
        //load dynamic structure
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this.context, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        scrollView = globalPanel.findViewById(DynamicUtils.ID_BODY_SCROLL);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        // initialize the sheet and generate ui of each questions
        this.initializeFormComponent();
        // set the first question of sheet
        firstPageNo = 1;
        next = 0;
        //load the page
        if (mapIndexPage != null) {
            currentPage = mapIndexPage.get(firstPageNo);// load default first page
            if (currentPage != null) {
                while (true) {
                    LinearLayout pageLayout = currentPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop());
                    if (pageLayout != null) {
                        bodyLayoutContainer.addView(pageLayout);
                        break;
                    } else {
                        PageFormBean pageNext = DynamicUtils.getPageFromNext(next);
                        if (pageNext != null) {
                            pageNext.setFirstQuestionId(next);
                            currentPage = pageNext;
                            firstPageNo = pageNext.getPageNo();
                        } else {
                            break;
                        }
                    }
                }
            }
            Log.i(getClass().getSimpleName(), "First Page is : \n " + currentPage);
        } else {
            Log.i(getClass().getSimpleName(), "Page Map is null ");
            bodyLayoutContainer.addView(MyStaticComponents.getMaterialTextView(context,
                    UtilBean.getMyLabel("Xls sheet not downloaded yet. Please login again...."), -1, 20, false));
        }
        setButtonDynamicText();
    }

    public static void setNext(int nextQue) {
        next = nextQue;
    }

    public static FormEngine generateForm(Context context, String formType) {
        SharedStructureData.formType = formType;
        return new FormEngine(context, formType);
    }

    private void initializeFormComponent() {
        if (mapIndexQuestion != null) {
            for (Map.Entry<Integer, QueFormBean> en : mapIndexQuestion.entrySet()) {
                QueFormBean quesFormBean = en.getValue();
                if (quesFormBean != null) {
                    try {
                        FormGenerator.generateQuestion(quesFormBean, context);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                }
            }
        }
    }

    public View getPageView() {
        return globalPanel;
    }

    @Override
    public void onClick(View view) {

        bodyLayoutContainer.clearFocus();
        if (view.getId() == DynamicUtils.ID_BACK_BUTTON) {
            this.navigateToPreviousPage();
            // if current page is for vaccination then 
            if (currentPage.isIsVaccinations()) {
                if (currentPage.getMyVaccination().getTotalVaccinations() > 0) {
                    setBackListener(currentPage.getMyVaccination());
                }
            } else {
                setBackListener(null);
                setNextListener(null);
            }
        } else if (view.getId() == DynamicUtils.ID_NEXT_BUTTON) {

            if (SharedStructureData.gpsEnabledForms.contains(formType)) {
                if (!SharedStructureData.gps.isLocationProviderEnabled()) {
                    gps.showSettingsAlert(context);
                } else {
                    if (!PointInPolygon.coordinateInsidePolygon()) {
                        UtilBean.showAlertAndExit(GlobalTypes.MSG_GEO_FENCING_VIOLATION, context);
                    }
                    this.navigateToNextPage();
                    // if current page is for vaccination than
                    if (currentPage.isIsVaccinations()) {
                        if (currentPage.getMyVaccination().getTotalVaccinations() > 0) {
                            currentPage.getMyVaccination().showFirst();
                            setNextListener(currentPage.getMyVaccination());
                        }
                    } else {
                        setBackListener(null);
                        setNextListener(null);
                    }
                }
            } else {
                this.navigateToNextPage();
                // if current page is for vaccination than
                if (currentPage.isIsVaccinations()) {
                    if (currentPage.getMyVaccination().getTotalVaccinations() > 0) {
                        currentPage.getMyVaccination().showFirst();
                        setNextListener(currentPage.getMyVaccination());
                    }
                } else {
                    setBackListener(null);
                    setNextListener(null);
                }
            }
        }
        setButtonDynamicText();

        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(bodyLayoutContainer.getWindowToken(), 0);
    }

    private void navigateToPreviousPage() {
        if (currentPage != null && currentPage.getPrePage() != null) {
            LinearLayout layout = currentPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop());
            layout.setVisibility(View.GONE);
            layout.clearFocus();
            layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            layout.setFocusableInTouchMode(true);
            currentPage = currentPage.getPrePage();
            scrollView.scrollTo(scrollView.getTop(), scrollView.getTop());
            layout = currentPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop());
            layout.setVisibility(View.VISIBLE);
        } else {
            ((DynamicFormActivity) context).onBackPressed();
        }
    }

    public void navigateToNextPage() {
        if (currentPage != null) {
            LinearLayout layout = currentPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop());
            layout.setVisibility(View.GONE);
            layout.clearFocus();
            layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            layout.setFocusableInTouchMode(true);
            if (isValidPage(currentPage)) {
                int nextPageId = currentPage.getNextPageId();
                if (nextPageId > 0) { // any page
                    PageFormBean nextPage = mapIndexPage.get(nextPageId);
                    if (nextPage != null) {
                        do {
                            if (nextPage != null && Boolean.TRUE.equals(nextPage.isIsFirst())) {
                                LinearLayout page = nextPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop());
                                if (page != null) {
                                    bodyLayoutContainer.addView(page);
                                    break;
                                } else {
                                    nextPage = DynamicUtils.getPageFromNext(next);
                                }
                            } else {
                                break;
                            }
                        } while (true);
                        if (nextPage != null) {
                            nextPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop()).setVisibility(View.VISIBLE);
                            scrollView.scrollTo(scrollView.getTop(), scrollView.getTop());
                            currentPage.setNextPage(nextPage);
                            currentPage = nextPage;
                        }
                    } else {
                        layout.setVisibility(View.VISIBLE);
                    }
                } else if (nextPageId < 0) { // page not found
                    layout.setVisibility(View.VISIBLE);
                    if (currentPage.getEvent() != null && currentPage.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_OKAY)) {
                        nextButton.setEnabled(Boolean.FALSE); //to restrict double click on form submit
                        String answerString = DynamicUtils.generateAnswerString(firstPageNo, formType, context);
                        ((DynamicFormActivity) context).onFormFillFinish(answerString, true);
                    } else if (currentPage.getEvent() != null && currentPage.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_SUBMIT)) {
                        nextButton.setEnabled(Boolean.FALSE); //to restrict double click on form submit
                        String answerString = DynamicUtils.generateAnswerString(firstPageNo, formType, context);
                        ((DynamicFormActivity) context).onFormFillFinish(answerString, false);
                    } else if (currentPage.getEvent() != null && currentPage.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_CANCEL)) {
                        ((DynamicFormActivity) context).onBackPressed();
                    } else if (currentPage.getEvent() != null && currentPage.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_SAVE_FORM)) {
                        nextButton.setEnabled(Boolean.FALSE); //to restrict double click on form submit
                        String answerString = DynamicUtils.generateAnswerString(firstPageNo, formType, context);
                        ((DynamicFormActivity) context).storeOpdLabTestForm(answerString);
                    } else {
                        SewaUtil.generateToast(context, "No more next page available");
                    }
                } else { // view first page 
                    currentPage = mapIndexPage.get(firstPageNo);
                    if (currentPage != null) {
                        currentPage.getPageLayout(false, bodyLayoutContainer.getPaddingTop()).setVisibility(View.VISIBLE);
                    }
                    scrollView.scrollTo(scrollView.getTop(), scrollView.getTop());
                }
            } else {
                layout.setVisibility(View.VISIBLE);
                SewaUtil.generateToast(context, currentPage.getValidationMessage());
            }
        } else {
            ((DynamicFormActivity) context).onBackPressed();
        }
    }

    private boolean isValidPage(PageFormBean currentPage) {
        QueFormBean tmpQueFormBean = mapIndexQuestion.get(currentPage.getFirstQuestionId());
        if (tmpQueFormBean != null && GlobalTypes.SHOW_VIDEO_MANDATORY.equals(tmpQueFormBean.getType())) {
            Boolean tmpBoolean = SharedStructureData.videoShownMap.get(currentPage.getFirstQuestionId());
            if (tmpBoolean != null && !tmpBoolean) {
                currentPage.setValidationMessage("લાભાર્થીને વિડિઓ બતાઓ");
                return false;
            }
        }

        int questionId = currentPage.getFirstQuestionId();
        currentPage.setLastQuestionId(questionId);
        List<Integer> listOfVisibleQuestions = currentPage.getListOfQuestion();
        if (listOfVisibleQuestions != null) {
            while (listOfVisibleQuestions.contains(questionId)) {
                QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(questionId);
                currentPage.setLastQuestionId(questionId);
                // mandatory validation
                if (queFormBean != null && queFormBean.getIshidden().equalsIgnoreCase(GlobalTypes.FALSE)) {
                    if (queFormBean.getIsmandatory() != null && queFormBean.getIsmandatory().equalsIgnoreCase(GlobalTypes.TRUE)
                            && (queFormBean.getAnswer() == null || queFormBean.getAnswer().toString().trim().length() < 1)) {
                        String mandatoryMessage = queFormBean.getMandatorymessage();
                        if (mandatoryMessage == null
                                || mandatoryMessage.trim().length() == 0
                                || mandatoryMessage.trim().equalsIgnoreCase("null")) {
                            mandatoryMessage = queFormBean.getQuestion();
                        }
                        currentPage.setValidationMessage(mandatoryMessage);
                        return false;
                    }

                    String answer = null;
                    if (queFormBean.getAnswer() != null) {
                        answer = queFormBean.getAnswer().toString();
                    }
                    if (answer != null && answer.trim().length() > 0 && !answer.trim().equalsIgnoreCase("null")) {
                        String type = queFormBean.getType();
                        if (type != null && !type.equalsIgnoreCase(GlobalTypes.VACCINATIONS_TYPE) && !type.equalsIgnoreCase(GlobalTypes.SIMPLE_RADIO_DATE)) {
                            if (type.equalsIgnoreCase(GlobalTypes.TEXT_BOX_WITH_AUDIO)) {
                                String[] aa = UtilBean.split(answer.trim(), GlobalTypes.KEY_VALUE_SEPARATOR);
                                if (aa.length > 0) {
                                    answer = aa[0];
                                } else {
                                    answer = null;
                                }
                            } else if (type.equalsIgnoreCase(GlobalTypes.CHECKBOX_TEXT_BOX)) {
                                if (answer.equalsIgnoreCase(GlobalTypes.TRUE)) {
                                    answer = null;
                                } else {
                                    String[] answers = UtilBean.split(answer, GlobalTypes.DATE_STRING_SEPARATOR);
                                    if (answers.length > 1) {
                                        answer = answers[1];
                                    }
                                }
                            }
                            List<ValidationTagBean> validations = queFormBean.getValidations();
                            int loopCounter = 0;
                            if (!queFormBean.isIgnoreLoop()) {
                                loopCounter = queFormBean.getLoopCounter();
                            }
                            String validationMsg = DynamicUtils.checkValidation(answer, loopCounter, validations);
                            if (validationMsg != null) {
                                currentPage.setValidationMessage(validationMsg);
                                return false;
                            }
                        }
                        DynamicUtils.applyFormula(queFormBean, true);
                    }
                }
                if (queFormBean != null) {
                    questionId = DynamicUtils.getNext(queFormBean);
                } else {
                    questionId = -1;
                }
            }
            currentPage.setValidationMessage(null);
            return true;
        }
        currentPage.setValidationMessage("No question found");
        return false;
    }

    private void setButtonDynamicText() {
        if (currentPage != null) {
            if (currentPage.getEvent() != null) {
                nextButton.setText(UtilBean.getMyLabel(currentPage.getEvent()));
            } else {
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            }
            if (currentPage.getFirstQuestionId() == GlobalTypes.LAST_QUESTION_ID) {
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_SUBMIT));
            }
        }
    }

    public void setNextListener(View.OnClickListener listener) {
        if (nextButton != null) {
            if (listener != null) {
                nextButton.setOnClickListener(listener);
            } else {
                nextButton.setOnClickListener(this);
            }
        }
    }

    public void backButtonClicked() {
        View view = new View(context);
        view.setId(DynamicUtils.ID_BACK_BUTTON);
        if (!isVaccinationsPage) {
            onClick(view);
        } else {
            currentPage.getMyVaccination().onClick(view);
        }
    }

    public void setBackListener(View.OnClickListener listener) {
        isVaccinationsPage = listener != null;
    }
}
