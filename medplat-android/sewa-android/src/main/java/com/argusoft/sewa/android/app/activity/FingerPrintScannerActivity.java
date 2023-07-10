package com.argusoft.sewa.android.app.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.CreateHidAadharBioRequest;
import com.argusoft.sewa.android.app.databean.HealthIdAccountProfileResponse;
import com.argusoft.sewa.android.app.rdservice.RDServiceEvents;
import com.argusoft.sewa.android.app.rdservice.RDServiceManager;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.json.JSONException;

import java.util.Map;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

@EActivity
public class FingerPrintScannerActivity extends MenuActivity implements View.OnClickListener, RDServiceEvents {

    private static final String TAG = "FingerPrintScanner";

    private RDServiceManager rdServiceManager;

    @Bean
    SewaServiceRestClientImpl restClient;

    public Context context = this;
    private MyAlertDialog myAlertDialog;
    private String abhaConsentCheckboxModel;
    private Boolean isAadhaarDetailMatchConsentGiven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rdServiceManager = new RDServiceManager.Builder(this).create();
        rdServiceManager.discoverRdService();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Add this line -->
        try {
            rdServiceManager.onActivityResult(requestCode, resultCode, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ...
    }

    @Override
    public void onRDServiceDriverDiscovery(String rdServiceInfo, String rdServicePackage, Boolean isWhitelisted) throws JSONException {
        XmlToJson json = new XmlToJson.Builder(rdServiceInfo).build();
        String status = json.toJson().getJSONObject("RDService").getString("status");
        if (!status.equals("READY")) {
            onRDServiceDriverNotFound();
        } else {
            rdServiceManager.captureRdService(rdServicePackage, "<PidOptions ver=\"1.0\"> <Opts fCount=\"1\" fType=\"2\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" \n" +
                    "wadh=\"E0jzJ/P8UopUHAieZn8CKqS4WPMi5ZSYXgfnlfkWjrc=\" \n" +
                    "timeout=\"20000\" posh=\"UNKNOWN\" env=\"P\" /> </PidOptions>");
        }
    }

    @Override
    public void onRDServiceCaptureResponse(String pidData, String rdServicePackage) throws JSONException {
        XmlToJson json = new XmlToJson.Builder(pidData).build();
        String errorCode = json.toJson().getJSONObject("PidData").getJSONObject("Resp").getString("errCode");
        String errorInfo = json.toJson().getJSONObject("PidData").getJSONObject("Resp").getString("errInfo");
        if (errorCode.equals("0")) {
            showProcessDialog();
            this.generateHealthIdUsingFingerPrint(pidData);
        } else {
            View.OnClickListener myListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAlertDialog.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            };
            myAlertDialog = new MyAlertDialog(this,
                    errorInfo,
                    myListener, DynamicUtils.BUTTON_OK);
            myAlertDialog.show();
        }
    }

    @Override
    public void onRDServiceDriverNotFound() {
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.RD_SERVICE_DRIVERS_NOT_FOUND,
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
    }

    @Override
    public void onRDServiceDriverDiscoveryFailed(int resultCode, Intent data, String rdServicePackage, String reason) {
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.RD_SERVICE_DRIVERS_DISCOVERY_FAILED,
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
    }

    @Override
    public void onRDServiceCaptureFailed(int resultCode, Intent data, String rdServicePackage) {
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.RD_SERVICE_CAPTURE_FAILED,
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
    }

    @Background
    public void generateHealthIdUsingFingerPrint(String pid) {
        final String[] message = {"Issue for creating Health ID"};
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            try {
                abhaConsentCheckboxModel = extras.getString(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL);
                Boolean isNonGov = extras.getBoolean("isNonGov");
                isAadhaarDetailMatchConsentGiven = extras.getBoolean(RelatedPropertyNameConstants.AADHAAR_MATCH_WITH_BENEFICIARY_CONSENT);
                CreateHidAadharBioRequest request = new CreateHidAadharBioRequest();
                request.setAadhaar(extras.getString("aadhaar"));
                request.setBioType("FMR");
                request.setMobileNumber(extras.getString("mobile"));
                request.setPid(pid);
                if (isNonGov) {
                    Map<String, String> result = restClient.verifyBiometricsForNonGov(request);
                    hideProcessDialog();
                    Intent intent = new Intent();
                    intent.putExtra("txnId", result.get("txnId"));
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                } else {
                    String token;
                    HealthIdAccountProfileResponse accountProfileResponse = restClient.createUsingAadhaarBiometrics(extras.getInt("memberId"),
                            extras.getString("featureName"), request, abhaConsentCheckboxModel, isAadhaarDetailMatchConsentGiven);
                    if (accountProfileResponse != null) {
                        if (accountProfileResponse.getJwtResponse() != null && accountProfileResponse.getJwtResponse().getToken() != null && accountProfileResponse.getHealthIdNumber() != null) {
                            token = accountProfileResponse.getJwtResponse().getToken();
                        } else {
                            token = accountProfileResponse.getToken();
                        }
                        hideProcessDialog();
                        Intent intent = new Intent();
                        intent.putExtra("token", token);
                        intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER, accountProfileResponse.getHealthIdNumber());
                        intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID, accountProfileResponse.getHealthId());
                        setResult(RESULT_OK, intent);
                        finish();
                        return;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                message[0] = e.getMessage();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProcessDialog();
                    SewaUtil.generateToast(context, message[0]);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }
}
