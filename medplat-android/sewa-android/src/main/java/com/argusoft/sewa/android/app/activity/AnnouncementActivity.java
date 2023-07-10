/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.AnnouncementArrayAdaptor;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.Collections;
import java.util.List;

/**
 * @author alpeshkyada
 */
@EActivity
public class AnnouncementActivity extends MenuActivity {

    @Bean
    SewaServiceImpl sewaService;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnnouncementBean, Integer> announcementBeansDao;

    //  Template Member
    private LinearLayout bodyLayoutContainer;
    private List<AnnouncementBean> announcementBeans = null;

    @Override
    protected void onResume() {
        super.onResume(); //To change body of generated methods, choose Tools | Templates.
        setContentView(R.layout.activity_announcement);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = findViewById(R.id.announcementContainer);
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }

        processDialog = new MyProcessDialog(this, GlobalTypes.MSG_PROCESSING);
        processDialog.show();

        try {
            retrieveAnnouncements();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @Background
    void retrieveAnnouncements() {
        announcementBeans = sewaService.getAllAnnouncement();
        onRetrieveAnnouncementsComplete();
    }

    @UiThread
    void onRetrieveAnnouncementsComplete() {
        setTitle(UtilBean.getTitleText(LabelConstants.ANNOUNCEMENT_TITLE));
        setSubTitle(null);

        if (announcementBeans != null && !announcementBeans.isEmpty()) {
            Collections.reverse(announcementBeans);
            AnnouncementArrayAdaptor adapter = new AnnouncementArrayAdaptor(context, announcementBeans, announcementBeansDao);
            ListView listView = MyStaticComponents.getListView(this, adapter, null);
            bodyLayoutContainer.addView(listView);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_ANNOUNCEMENT_AVAILABLE)));
        }

        processDialog.dismiss();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "On Restore CALLED");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            navigateToHomeScreen(false);
        } else if (item.getItemId() == R.id.menu_refresh) {
            processDialog = new MyProcessDialog(this, GlobalTypes.PLEASE_WAIT);
            processDialog.show();
            doUpdate(true);
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        navigateToHomeScreen(false);
    }
}
