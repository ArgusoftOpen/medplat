package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.MyWorkLogAdapter;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.WorkLogScreenDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author kelvin
 */
@EActivity
public class WorkLogActivity extends MenuActivity {

    @Bean
    SewaServiceImpl sewaService;
    private LinearLayout bodyLayoutContainer;

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, null);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);

        LinearLayout footer = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        globalPanel.removeView(footer);

        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        if (BuildConfig.FLAVOR.equalsIgnoreCase(GlobalTypes.UTTARAKHAND_FLAVOR)){
            setTitle(UtilBean.getTitleText(LabelConstants.SYNC_STATUS_TITLE));
        } else {
            setTitle(UtilBean.getTitleText(LabelConstants.WORK_LOG_TITLE));
        }
        retrieveWorkLog();
    }

    @UiThread
    public void retrieveWorkLog() {
        showProcessDialog();
        List<LoggerBean> loggerBeans = sewaService.getWorkLog();
        List<WorkLogScreenDataBean> lst = new ArrayList<>();
        WorkLogScreenDataBean log;
        if (loggerBeans != null && !loggerBeans.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT + " hh.mm aa", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);

            for (LoggerBean loggerBean : loggerBeans) {
                if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_SUCCESS)) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.success, loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_PENDING)) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.pending, loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_ERROR) && loggerBean.getModifiedOn() != null && loggerBean.getModifiedOn().after(calendar.getTime())) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.error, loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_HANDLED_ERROR) && loggerBean.getModifiedOn() != null && loggerBean.getModifiedOn().after(calendar.getTime())) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.warning, loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                }
            }
        } else {
            log = new WorkLogScreenDataBean(null, -1, LabelConstants.THERE_IS_NO_WORKLOG_TO_DISPLAY, null, null);
            lst.add(log);
        }
        MyWorkLogAdapter adapter = new MyWorkLogAdapter(this, lst);
        bodyLayoutContainer.addView(MyStaticComponents.getListView(this, adapter, null));
        hideProcessDialog();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(true);
        menu.findItem(R.id.menu_about).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(true);
        return true;
    }
}
