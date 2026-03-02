package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EActivity
public class TravellersScreeningActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;

    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private List<Integer> areaIds;
    private List<CovidTravellersInfoBean> covidTravellersList;
    private String screen;
    private CovidTravellersInfoBean covidTravellersInfoBean;
    private MaterialTextView textView;
    private PagingListView paginatedListViewWithItem;
    private int selectedTravellerIndex = -1;
    private long offset;
    private long limit = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        String locationId = getIntent().getExtras() != null ? getIntent().getExtras().getString("locationId") : null;
        if (locationId != null) {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            areaIds = new Gson().fromJson(locationId, type);
        }
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
        setTitle(UtilBean.getTitleText(UtilBean.getFullFormOfEntity().get(FormConstants.TRAVELLERS_SCREENING)));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        fetchTravellersListFromDB(null);
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListViewWithItem);

        if (covidTravellersList != null && !covidTravellersList.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_TRAVELLER);
            bodyLayoutContainer.addView(textView);

            List<ListItemDataBean> travellerItemsList = getTravellerItemsList(covidTravellersList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedTravellerIndex = position;
            paginatedListViewWithItem = MyStaticComponents.getPaginatedListViewWithItem(context, travellerItemsList, R.layout.listview_row_with_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListViewWithItem);
        } else {
            textView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_TRAVELLERS_IN_YOUR_AREA);
            bodyLayoutContainer.addView(textView);
            nextButton.setText(GlobalTypes.EVENT_OKAY);
        }
        hideProcessDialog();
    }

    @Background
    public void fetchTravellersListFromDB(String search) {
        offset = 0;
        covidTravellersList = fhsService.retrieveTravellersList(areaIds, search, limit, offset);
        offset = limit + offset;
        setMemberSelectionScreen();
    }

    private List<ListItemDataBean> getTravellerItemsList(List<CovidTravellersInfoBean> covidTravellersInfoBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        for (CovidTravellersInfoBean bean : covidTravellersInfoBeans) {
            list.add(new ListItemDataBean(bean.getName(), null));
        }
        return list;
    }

    private void startDynamicFormActivity() {
        formMetaDataUtil.setMetaDataForCovidTravellersForm(covidTravellersInfoBean, null);
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        intent.putExtra(SewaConstants.ENTITY, FormConstants.TRAVELLERS_SCREENING);
        startActivityForResult(intent, ActivityConstants.REQUEST_CODE_FOR_TRAVELLERS_SCREENING_ACTIVITY);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON && MEMBER_SELECTION_SCREEN.equals(screen)) {
            if (covidTravellersList.isEmpty()) {
                finish();
                return;
            }
            if (selectedTravellerIndex != -1) {
                covidTravellersInfoBean = covidTravellersList.get(selectedTravellerIndex);
                startDynamicFormActivity();
            } else {
                SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TRAVELLER));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.REQUEST_CODE_FOR_TRAVELLERS_SCREENING_ACTIVITY) {
            selectedTravellerIndex = -1;
            setMemberSelectionScreen();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }
        if (item.getItemId() == android.R.id.home && MEMBER_SELECTION_SCREEN.equals(screen)) {
            finish();
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
        List<CovidTravellersInfoBean> covidTravellersInfoBeans = fhsService.retrieveTravellersList(areaIds, null, limit, offset);
        offset = offset + limit;
        onLoadMoreUi(covidTravellersInfoBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<CovidTravellersInfoBean> covidTravellersInfoBeans) {
        if (!covidTravellersInfoBeans.isEmpty()) {
            List<ListItemDataBean> familyList = getTravellerItemsList(covidTravellersInfoBeans);
            covidTravellersList.addAll(covidTravellersInfoBeans);
            paginatedListViewWithItem.onFinishLoadingWithItem(true, familyList);
        } else {
            paginatedListViewWithItem.onFinishLoadingWithItem(false, null);
        }
    }
}
