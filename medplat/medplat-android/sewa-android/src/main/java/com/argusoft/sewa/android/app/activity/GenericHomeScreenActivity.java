package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.comparator.MenuConstantComparator;
import com.argusoft.sewa.android.app.component.HomeMenuAdapter;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.OnMenuItemViewVClickListener;
import com.argusoft.sewa.android.app.component.listeners.MenuClickListener;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.MenuConstants;
import com.argusoft.sewa.android.app.core.impl.MenuServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MoveToProductionServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.MenuBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@EActivity
public class GenericHomeScreenActivity extends HomeScreenMenuActivity implements OnMenuItemViewVClickListener {

    @Bean
    MenuServiceImpl menuService;
    @Bean
    MoveToProductionServiceImpl moveToProductionService;

    HomeMenuAdapter homeMenuAdapter;

    private List<MenuBean> orderList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedStructureData.relatedPropertyHashTable.clear();
        context = this;
        init();
    }

    private void init() {
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setActionBarDesign();
        setNavigationView();
        orderList = menuService.retrieveMenus();

        Collections.sort(orderList, new MenuConstantComparator());
        LinkedList<Integer> icons = new LinkedList<>();
        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> constant = new LinkedList<>();
        List<MenuBean> menuToRemove = new ArrayList<>();
        for (MenuBean m : orderList) {
            Integer menuIcons = MenuConstants.getMenuIcons(m.getConstant());
            if (menuIcons == null) {
                menuToRemove.add(m);
                continue;
            }
            icons.add(menuIcons);
            names.add(m.getDisplayName());
            constant.add(m.getConstant());
        }
        orderList.removeAll(menuToRemove);

        if (orderList.isEmpty()) {
            showAlert(LabelConstants.NO_FEATURE_FOUND, GlobalTypes.MSG_NO_MENU, v -> alertDialog.dismiss(), DynamicUtils.BUTTON_OK);
        }

        //set up recycler view
        RecyclerView recyclerView = findViewById(R.id.rv_home_screen_icons);
        homeMenuAdapter = new HomeMenuAdapter(context, integerListToIntArray(icons), names.toArray(new String[0]), constant.toArray(new String[0]), true,
                techoService.isNewNotification(), lmsService.isAnyUpdatedDataAvailable(), this::onItemClickListener);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(context, 6);
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(homeMenuAdapter);
        //setCardView(context, names.toArray(new String[0]), integerListToIntArray(icons), this);
    }

    private int[] integerListToIntArray(LinkedList<Integer> icons) {
        int[] iconsArray = new int[icons.size()];
        int i = 0;
        for (Integer icon : icons) {
            iconsArray[i] = icon;
            i++;
        }
        return iconsArray;
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener onClickListener = this::logoutAlertDialogClick;
        alertDialog = new MyAlertDialog(this,
                UtilBean.getMyLabel(GlobalTypes.MSG_CANCEL_APPLICATION),
                onClickListener, DynamicUtils.BUTTON_HIDE_LOGOUT);
        alertDialog.show();
    }

    private boolean checkMenuAccessibleToUser(View view, String menuConstant) {
        if (SewaTransformer.loginBean.isTrainingUser()) {
            return true;
        }

        String formCode = MenuConstants.getFormCodeFromMenuConstant(menuConstant);
        if (formCode == null) {
            return true;
        }

        FormAccessibilityBean formAccessibilityBean = moveToProductionService.retrieveFormAccessibilityBeanByFormType(formCode);
        return checkIfFormIsAccessibleInProduction(formAccessibilityBean, view, menuConstant);
    }

    private boolean checkIfFormIsAccessibleInProduction(FormAccessibilityBean formAccessibilityBean, View view, String menuConstant) {
        if (formAccessibilityBean == null) {
            showAlertDialogForFormNotAccessible(view, menuConstant);
            return false;
        }

        if (Boolean.TRUE.equals(formAccessibilityBean.getTrainingReq())
                && (formAccessibilityBean.getState() == null || !formAccessibilityBean.getState().equals("MOVE_TO_PRODUCTION"))) {
            showAlertDialogForFormNotAccessible(view, menuConstant);
            return false;
        }
        return true;
    }

    @UiThread
    public void showAlertDialogForFormNotAccessible(View view, String menuConstant) {
        processDialog.dismiss();

        String alertLabel = LabelConstants.TRAINING_REQUIRED_AND_PRACTICE;

        alertDialog = new MyAlertDialog(context,
                UtilBean.getMyLabel(alertLabel),
                v -> {
                    alertDialog.dismiss();
                }, DynamicUtils.BUTTON_OK);
        alertDialog.show();
    }

    @UiThread
    public void showTrainingCompletedForm() {
        List<FormAccessibilityBean> formAccessibilityBeans = moveToProductionService.isAnyFormTrainingCompleted();
        if (formAccessibilityBeans != null && !formAccessibilityBeans.isEmpty()) {
            Intent intent = new Intent(this, MoveToProductionActivity_.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClickListener(int position, View view) {
        processDialog = new MyProcessDialog(this, GlobalTypes.PLEASE_WAIT);
        processDialog.show();
        new Thread() {
            @Override
            public void run() {
                MenuBean menuConstant = orderList.get(position);
                //if (checkMenuAccessibleToUser(view, menuConstant.getConstant())) {
                    MenuClickListener menuListener = new MenuClickListener(context, menuConstant.getConstant());
                    menuListener.onClick(view);
                //}
                processDialog.dismiss();
            }
        }.start();
    }
}
