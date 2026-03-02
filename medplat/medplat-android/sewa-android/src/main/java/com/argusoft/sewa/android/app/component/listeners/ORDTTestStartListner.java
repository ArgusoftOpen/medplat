package com.argusoft.sewa.android.app.component.listeners;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ORDTTestStartListner implements View.OnClickListener {
    private Activity activity;
    private QueFormBean queFormBean;
    private ImageView actionBtn;
    private Boolean flag;
    private Boolean isTrainingMode;

    public ORDTTestStartListner(Activity activity, QueFormBean queFormBean, ImageView actionBtn) {
        this.activity = activity;
        this.queFormBean = queFormBean;
        this.actionBtn = actionBtn;
        this.flag = false;
    }

    public void resetClick() {
        flag = false;
        actionBtn.setAlpha(1f);
    }

    @Override
    public void onClick(View view) {
        if (Boolean.TRUE.equals(flag)) {
            return;
        }
        try {
            String packageName = "org.auderenow.openrdt.reader";
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = activity.getPackageManager().queryIntentActivities(intent, 0);

            for (ResolveInfo info : resolveInfoList) {
                if (info.activityInfo.packageName.equals(packageName)) {
                    actionBtn.setAlpha(0.6f);

                    intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.setComponent(
                            new ComponentName(
                                    info.activityInfo.packageName, info.activityInfo.name
                            )
                    );
                    intent.setFlags(0);
                    SharedStructureData.currentQuestion = DynamicUtils.getLoopId(queFormBean);
                    String requestId = UUID.randomUUID().toString();
                    String patientName = SharedStructureData.relatedPropertyHashTable.get("memberName");
                    intent.putExtra("requestId", requestId);
                    if (queFormBean.getDatamap() != null) {
                        for (Map.Entry<String, String> entry : ((Map<String, String>) new Gson().fromJson(queFormBean.getDatamap(), Map.class)).entrySet()) {
                            if (entry.getKey().equals("trainingMode")) {
                                isTrainingMode = String.valueOf(entry.getValue()).equalsIgnoreCase("true");
                                intent.putExtra("trainingMode", isTrainingMode);
                                continue;
                            }
                            intent.putExtra(entry.getKey(), entry.getValue());
                        }
                    }
                    LoginBean loginBean = SharedStructureData.sewaService.getCurrentLoggedInUser();
                    if (loginBean != null) {
                        intent.putExtra("practitionerId", loginBean.getUsername());
                    }
                    intent.putExtra("partnerId", "Techo+");
                    intent.putExtra("patientName", patientName);
                    intent.putExtra("includeCapturedImageUri", true);

                    flag = true;
                    if (Boolean.TRUE.equals(isTrainingMode)) {
                        SharedStructureData.ordtIntent = intent;
                        intent.putExtra("testProcessingStartTime", new Date());
                        activity.startActivityForResult(intent, ActivityConstants.OPENRDTREADER_TRAINING_ACTIVITY_REQUEST_CODE);
                    } else {
                        activity.startActivityForResult(intent, ActivityConstants.OPENRDTREADER_ACTIVITY_REQUEST_CODE);
                    }
                    return;
                }
            }
            SewaUtil.generateToast(activity, LabelConstants.ORDT_READER_APPLICATION_NOT_FOUND_ALERT);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }
}
