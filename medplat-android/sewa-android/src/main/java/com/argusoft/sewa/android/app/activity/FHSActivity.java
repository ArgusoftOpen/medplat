package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import static android.content.DialogInterface.BUTTON_POSITIVE;

@EActivity
public class FHSActivity extends MenuActivity {

    private LinearLayout bodyLayoutContainer;
    private MyAlertDialog myAlertDialog;
    private LinearLayout globalPanel;
    private LinearLayout footerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        setTitle(UtilBean.getTitleText(LabelConstants.COMPREHENSIVE_FAMILY_HEALTH_CENSUS_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, null);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setChoiceSelectionScreen();
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                LabelConstants.WANT_TO_CLOSE_COMPREHENSIVE_FAMILY_HEALTH_CENSUS,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @UiThread
    public void setChoiceSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.WHAT_DO_YOU_WANT_TO_DO)));

        List<String> options = new ArrayList<>();

        options.add(UtilBean.getMyLabel(LabelConstants.COMPREHENSIVE_FAMILY_HEALTH_CENSUS));
        options.add(UtilBean.getMyLabel(LabelConstants.RE_VERIFICATION));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            final Intent intent = new Intent(context, CFHCActivity_.class);
            intent.putExtra("reverification", position == 1);
            startActivityForResult(intent, 100);
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
        footerLayout.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setChoiceSelectionScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            navigateToHomeScreen(false);
        }
        return true;
    }
}
