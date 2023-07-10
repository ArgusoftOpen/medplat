package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class NcdConfirmationFhwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final long DELAY = 500;
    private static final long LIMIT = 30;
    @Bean
    public SewaServiceImpl sewaService;
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public NcdServiceImpl ncdService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private String screen;
    private Spinner ashaAreaSpinner;
    private List<LocationBean> ashaAreaList;
    private MemberDataBean selectedMember;
    private List<MemberDataBean> memberDataBeanList;
    private List<LocationBean> villageList;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private long offset;
    private int selectedMemberIndex = -1;
    private PagingListView paginatedListView;
    private String searchString;
    private LinearLayout globalPanel;
    private MaterialTextView textView;
    private MaterialTextView noFamilyTextView;

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
        setTitle(UtilBean.getTitleText("NCD Confirmation"));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, "NCD Confirmation");
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != DynamicUtils.ID_NEXT_BUTTON) {
            return;
        }

        setSubTitle(null);
        switch (screen) {
            case VILLAGE_SELECTION_SCREEN:
                showProcessDialog();
                String selectedAshaArea = ashaAreaSpinner.getSelectedItem().toString();
                selectedAshaAreas = new ArrayList<>();
                if (selectedAshaArea.equals(LabelConstants.ALL)) {
                    for (LocationBean locationBean : ashaAreaList) {
                        selectedAshaAreas.add(locationBean.getActualID());
                    }
                } else {
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedAshaArea.equals(locationBean.getName())) {
                            selectedAshaAreas.add(locationBean.getActualID());
                            break;
                        }
                    }
                }
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveMemberListFromDB(null);
                break;

            case MEMBER_SELECTION_SCREEN:
                if (memberDataBeanList == null || memberDataBeanList.isEmpty()) {
                    navigateToHomeScreen(false);
                    return;
                }
                if (selectedMemberIndex != -1) {
                    selectedMember = memberDataBeanList.get(selectedMemberIndex);
                    setMetadataForFormByFormType(selectedMember);
                    Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                    myIntent.putExtra(SewaConstants.ENTITY, FormConstants.NCD_FHW_DIABETES_CONFIRMATION);
                    startActivityForResult(myIntent, 200);
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                }
                break;

            default:
        }
    }

    private void addSearchTextBox() {
        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_MEMBER, 1, 20, 1);

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
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                retrieveMemberListFromDB(s.toString());
                                                editText.setFocusable(true);
                                            }
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showProcessDialog();
                                                retrieveMemberListFromDB(null);
                                            }
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
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    @Background
    public void retrieveMemberListFromDB(String familyId) {
        searchString = familyId;
        offset = 0;
        selectedMemberIndex = -1;
        memberDataBeanList = ncdService.retrieveMembersListForNcdConfirmation(familyId, selectedAshaAreas, LIMIT, offset);
        offset = offset + LIMIT;
        setMemberSelectionScreen(familyId != null);
    }

    @UiThread
    public void setMemberSelectionScreen(boolean isSearch) {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyTextView);

        if (!memberDataBeanList.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            bodyLayoutContainer.addView(textView);
            List<ListItemDataBean> familyList = getMemberList(memberDataBeanList);

            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedMemberIndex = position;
                }
            };

            if (isSearch) {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            }
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyTextView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_MEMBERS_FOUND));
            bodyLayoutContainer.addView(noFamilyTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        if (!isSearch) {
            showProcessDialog();
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getMemberList(List<MemberDataBean> memberDataBeans) {
        List<ListItemDataBean> list = new ArrayList<>();

        for (MemberDataBean memberDataBean : memberDataBeans) {
            list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean),
                    null, null));
        }
        return list;
    }

    private void setMetadataForFormByFormType(MemberDataBean memberDataBean) {
        FamilyDataBean familyDataBean = null;
        if (memberDataBean != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (memberDataBean != null) {
            formMetaDataUtil.setMetaDataForNCDForms(memberDataBean, familyDataBean, sharedPref);
        }
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_VILLAGE));
        String[] arrayOfOptions = new String[villageList.size()];
        int i = 0;
        for (LocationBean locationBean : villageList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        Spinner villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyLayoutContainer.removeView(ashaAreaSpinner);
                ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(villageList.get(position).getActualID());
                addAshaAreaSelectionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // not doing anything here
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA)));
        addAshaAreaSelectionSpinner();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
        arrayOfOptions[0] = "All";
        int i = 1;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == BUTTON_POSITIVE) {
                    myAlertDialog.dismiss();
                    navigateToHomeScreen(false);
                    finish();
                } else {
                    myAlertDialog.dismiss();
                }
            }
        };

        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.ON_NCD_CONFIRMATION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveMemberListFromDB(null);
            } else {
                navigateToHomeScreen(false);
            }
            return;
        }
        hideProcessDialog();
    }

    private void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    View.OnClickListener myListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    };
                    alertDialog = new MyAlertDialog(context, false,
                            UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                            myListener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                }
            });
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
            setSubTitle(null);
            switch (screen) {

                case MEMBER_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<MemberDataBean> memberDataBeansList = ncdService.retrieveMembersListForNcdConfirmation(searchString, selectedAshaAreas, LIMIT, offset);
        offset = offset + LIMIT;
        onLoadMoreUi(memberDataBeansList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> memberDataBeansList) {
        if (!memberDataBeansList.isEmpty()) {
            List<ListItemDataBean> familyList = getMemberList(memberDataBeansList);
            memberDataBeanList.addAll(memberDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, familyList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
