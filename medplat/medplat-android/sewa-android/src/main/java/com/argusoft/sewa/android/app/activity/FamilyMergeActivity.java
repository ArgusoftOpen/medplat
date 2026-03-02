package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by prateek on 8/9/19
 */
@EActivity
public class FamilyMergeActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaFhsServiceImpl fhsService;

    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String FAMILY_INFO_SCREEN = "familyInfoScreen";
    private static final long DELAY = 500;
    private static final String IS_MERGED = "isMerged";

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screenName;
    private String locationId;
    private FamilyDataBean familyToExpand;
    private FamilyDataBean familyToMerge;
    private List<FamilyDataBean> familyDataBeans;
    private Timer timer = new Timer();
    private LinearLayout globalPanel;
    private long limit = 30;
    private long offset;
    private PagingListView paginatedListView;
    private int selectedFamilyIndex = -1;
    private MaterialTextView noFamilyAvailableView;
    private MaterialTextView listTitle;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String family = extras.getString("familyToExpand", null);
            familyToExpand = new Gson().fromJson(family, FamilyDataBean.class);
            locationId = extras.getString("locationId", null);
        }
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
            Intent myIntent = new Intent(context, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.FAMILY_MERGE_TITLE));
        setSubTitle(null);
    }


    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    public void setBodyDetail() {
        showProcessDialog();
        addSearchTextBox();
        retrieveFamilyList(null);
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(IS_MERGED, false);
                setResult(RESULT_CANCELED, intent);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(context, false,
                LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_MERGE_FAMILY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case FAMILY_SELECTION_SCREEN:
                    setSubTitle(null);
                    if (selectedFamilyIndex != -1) {
                        familyToMerge = familyDataBeans.get(selectedFamilyIndex);
                        if (familyToMerge.getFamilyId().equals(familyToExpand.getFamilyId())) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SELECTED_THE_SAME_FAMILY_PLEASE_SELECT_SOME_OTHER_FAMILY));
                        } else {
                            addFamilyInfoScreen();
                        }
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.SELECT_A_FAMILY_TO_MERGE);
                    }
                    break;

                case FAMILY_INFO_SCREEN:
                    View.OnClickListener myListener = v1 -> {
                        if (v1.getId() == BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                            showProcessDialog();
                            SharedStructureData.familyDataBeanToBeMerged = familyToMerge;
                            Intent intent = new Intent();
                            intent.putExtra(IS_MERGED, true);
                            intent.putExtra("familyToExpand", familyToExpand.getFamilyId());
                            intent.putExtra("familyToMerge", familyToMerge.getFamilyId());
                            setResult(RESULT_OK, intent);
                            hideProcessDialog();
                            finish();
                        } else {
                            alertDialog.dismiss();
                            SharedStructureData.familyDataBeanToBeMerged = null;
                            Intent intent = new Intent();
                            intent.putExtra(IS_MERGED, false);
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
                    };

                    alertDialog = new MyAlertDialog(context, false,
                            LabelConstants.SURE_YOU_WANT_TO_MERGE_THESE_TWO_FAMILIES,
                            myListener, DynamicUtils.BUTTON_YES_NO);
                    alertDialog.show();
                    break;
                default:
            }
        }
    }

    private void addSearchTextBox() {
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SEARCH_AND_SELECT_FAMILY_TO_MERGE));

        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

        if (editText.getEditText() != null) {
            editText.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //not implemented
                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    if (s != null && s.length() > 2) {
                                        runOnUiThread(() -> {
                                            retrieveFamilyList(s.toString());
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyList(null);
                                        });
                                    }
                                }
                            },
                            DELAY
                    );


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //not implemented
                }
            });
        }
    }

    @Background
    public void retrieveFamilyList(String search) {
        searchString = search;
        offset = 0;
        familyDataBeans = fhsService.retrieveFamilyDataBeansForMergeFamily(locationId, searchString, limit, offset);
        offset = offset + limit;
        setFamilySelectionScreen(search != null);
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch) {
        screenName = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyAvailableView);
        bodyLayoutContainer.removeView(listTitle);
        List<ListItemDataBean> list;
        if (!familyDataBeans.isEmpty()) {
            listTitle = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(listTitle);
            list = getFamilyList(familyDataBeans);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;

            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noFamilyAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        if (!isSearch) {
            hideProcessDialog();
        }
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;
        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(LabelConstants.NULL, ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }

            if (FhsConstants.CFHC_VERIFIED_FAMILY_STATES.contains(familyDataBean.getState())) {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, true));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, false));
            }
        }
        return list;
    }

    private void addFamilyInfoScreen() {
        screenName = FAMILY_INFO_SCREEN;
        bodyLayoutContainer.removeAllViews();

        addSelectedFamilyDetails(familyToExpand, LabelConstants.FAMILY_DETAILS);
        addSelectedFamilyDetails(familyToMerge, LabelConstants.DETAILS_OF_FAMILY_TO_BE_MERGED);
    }

    private void addSelectedFamilyDetails(FamilyDataBean familyDataBean, String titleText) {
        List<String> states = new ArrayList<>();
        states.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        states.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        if (states.contains(familyDataBean.getState())) {
            familyDataBean.setMembers(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(familyDataBean.getFamilyId()));
        } else {
            familyDataBean.setMembers(fhsService.retrieveMemberDataBeansByFamily(familyDataBean.getFamilyId()));
        }

        MaterialTextView titleView = MyStaticComponents.getListTitleView(this, titleText);
        titleView.setPadding(0, 30, 0, 0);
        if (titleText.equals(LabelConstants.FAMILY_DETAILS)) {
            LinearLayout detailsLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
            detailsLayout.addView(titleView);
            detailsLayout.addView(MyStaticComponents.generateLabelView(this, LabelConstants.FAMILY_ID));
            detailsLayout.addView(MyStaticComponents.generateBoldAnswerView(this, familyDataBean.getFamilyId()));
            detailsLayout.addView(MyStaticComponents.generateLabelView(this, LabelConstants.MEMBERS_INFO));
            detailsLayout.addView(UtilBean.getMembersListForDisplay(this, familyDataBean));
            detailsLayout.setPadding(40, 0, 0, 0);
            bodyLayoutContainer.addView(detailsLayout);
        } else {
            titleView = MyStaticComponents.getListTitleView(this, "");
            ImageSpan imageSpan = new ImageSpan(this, R.drawable.ic_merge_arrow);
            SpannableString spannableString = new SpannableString(" " + UtilBean.getMyLabel(titleText));
            titleView.setPadding(-10, 0, 0, 0);
            spannableString.setSpan(imageSpan, 0, 1, 0);
            titleView.setText(spannableString);
            bodyLayoutContainer.addView(titleView);
            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(this, LabelConstants.FAMILY_ID));
            bodyLayoutContainer.addView(MyStaticComponents.generateBoldAnswerView(this, familyDataBean.getFamilyId()));
            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(this, LabelConstants.MEMBERS_INFO));
            bodyLayoutContainer.addView(UtilBean.getMembersListForDisplay(this, familyDataBean));
        }
        nextButton.setText(GlobalTypes.EVENT_MERGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case FAMILY_SELECTION_SCREEN:
                    Intent intent = new Intent();
                    intent.putExtra(IS_MERGED, false);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;

                case FAMILY_INFO_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    setFamilySelectionScreen(false);
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> famDataBeans = fhsService.retrieveFamilyDataBeansForMergeFamily(locationId, searchString, limit, offset);
        offset = offset + limit;
        onLoadMoreUi(famDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeansList) {
        if (familyDataBeansList != null && !familyDataBeansList.isEmpty()) {
            familyDataBeans.addAll(familyDataBeansList);
            List<ListItemDataBean> stringList = getFamilyList(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
