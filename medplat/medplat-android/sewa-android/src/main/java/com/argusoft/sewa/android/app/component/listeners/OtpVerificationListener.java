package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class OtpVerificationListener implements View.OnClickListener {

    private Context context;
    private LinearLayout verificationComponent;
    private QueFormBean queFormBean;
    private Handler uIHandler;
    private Handler backgroundHandler;
    private int[] counter = {30, 0};

    private MaterialTextView mobileEditText;
    private MaterialButton sendButton;
    private MaterialTextView note;
    private MaterialTextView timer;
    private TextInputLayout otpText;
    private MaterialButton verifyButton;
    private MaterialTextView noticeText;
    private MaterialTextView scheme;
    private MaterialTextView instruction;
    private MaterialCheckBox smsScheme;
    private ImageView imageView;
    private MyProcessDialog processDialog;

    private Timer[] timerCounter = new Timer[1];

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.requestOtpBtn) {
            if (!SharedStructureData.sewaService.isOnline()) {
                SewaUtil.generateToast(context, LabelConstants.NETWORK_CONNECTIVITY_ALERT);
                return;
            }
            showProcessDialog();
            backgroundHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedStructureData.sewaServiceRestClient.sendOtpRequest(mobileEditText.getText().toString().trim());
                        uIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                counter[0] = 30;
                                counter[1]++;
                                sendButton.setEnabled(false);
                                note.setVisibility(View.VISIBLE);
                                timer.setVisibility(View.VISIBLE);
                                otpText.setVisibility(View.VISIBLE);
                                if (otpText.getEditText() != null) {
                                    otpText.getEditText().setText("");
                                }
                                verifyButton.setVisibility(View.VISIBLE);
                                verifyButton.setEnabled(false);
                                hideProcessDialog();

                                timerCounter[0].cancel();
                                timerCounter[0] = new Timer();
                                timerCounter[0].schedule(
                                        new TimerTask() {
                                            @Override
                                            public void run() {
                                                if (counter[0] > 0) {
                                                    uIHandler.post(() -> {
                                                        timer.setText(String.format(Locale.getDefault(), "%ds", counter[0]));
                                                        counter[0]--;
                                                    });
                                                } else {
                                                    uIHandler.post(() -> {
                                                        timer.setText(LabelConstants.ZERO_SECONDS);
                                                        if (counter[1] > 1) {
                                                            noticeText.setText(UtilBean.getMyLabel(LabelConstants.YOU_HAVE_REACHED_MAXIMUM_NUMBER_OF_RETRIES));
                                                            noticeText.setVisibility(View.VISIBLE);
                                                            SharedStructureData.isMobileVerificationBlocked = true;
                                                            queFormBean.setAnswer("F");
                                                            return;
                                                        }
                                                        sendButton.setText(UtilBean.getMyLabel(LabelConstants.REQUEST_VERIFICATION_CODE_AGAIN));
                                                        sendButton.setEnabled(true);
                                                    });
                                                    this.cancel();
                                                }
                                            }
                                        },
                                        50, 1500
                                );
                            }
                        });
                    } catch (RestHttpException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                        hideProcessDialog();
                        generateToast(e.getMessage());
                    }
                }
            });
        } else if (v.getId() == R.id.verifyOtpBtn) {
            if (!SharedStructureData.sewaService.isOnline()) {
                SewaUtil.generateToast(context, LabelConstants.NETWORK_CONNECTIVITY_ALERT);
                return;
            }
            if (otpText.getEditText() != null && otpText.getEditText().getText().toString().trim().isEmpty()) {
                SewaUtil.generateToast(context, LabelConstants.VERIFICATION_CODE_VALIDATION_FAILED);
            } else {
                showProcessDialog();
                sendButton.setEnabled(false);
                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Boolean result = SharedStructureData.sewaServiceRestClient.verifyOtp(
                                    mobileEditText.getText().toString().trim(), otpText.getEditText().getText().toString().trim());

                            uIHandler.post(() -> {
                                if (result != null && result) {
                                    timerCounter[0].cancel();
                                    timer.setText(LabelConstants.ZERO_SECONDS);
                                    noticeText.setVisibility(View.VISIBLE);
                                    noticeText.setText(UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_VERIFICATION_COMPLETED_SUCCESSFULLY));
                                    verifyButton.setEnabled(false);
                                    queFormBean.setAnswer("T/F");
                                    smsScheme.setVisibility(View.VISIBLE);
                                    scheme.setVisibility(View.VISIBLE);
                                    instruction.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.VISIBLE);
                                    SharedStructureData.isAnyMemberMobileVerificationDone = true;
                                    hideProcessDialog();
                                    generateToast(LabelConstants.VERIFICATION_CODE_VERIFIED_SUCCESSFULLY);
                                } else {
                                    if (counter[0] <= 0 && Boolean.FALSE.equals(SharedStructureData.isMobileVerificationBlocked)) {
                                        sendButton.setEnabled(true);
                                    }
                                    hideProcessDialog();
                                    generateToast(LabelConstants.INVALID_VERIFICATION_CODE);
                                }
                            });

                        } catch (RestHttpException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                            hideProcessDialog();
                            generateToast(e.getMessage());
                        }
                    }
                });
            }
        } else if (v.getId() == R.id.resetBtn) {
            timerCounter[0].cancel();
            timerCounter[0] = new Timer();
            counter[1] = 0;
            sendButton.setEnabled(true);
            sendButton.setText(LabelConstants.REQUEST_VERIFICATION_CODE);
            note.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            otpText.setVisibility(View.GONE);
            verifyButton.setVisibility(View.GONE);
            verifyButton.setEnabled(true);
            noticeText.setVisibility(View.GONE);
            smsScheme.setChecked(Boolean.FALSE);
            smsScheme.setVisibility(View.GONE);
            scheme.setVisibility(View.GONE);
            instruction.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }

    private void generateToast(final String message) {
        uIHandler.post(() -> SewaUtil.generateToast(context, message));
    }

    public OtpVerificationListener(Context context, LinearLayout verificationComponent, QueFormBean queFormBean) {
        this.context = context;
        this.verificationComponent = verificationComponent;
        this.queFormBean = queFormBean;

        uIHandler = new Handler(Looper.getMainLooper());

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        init();
    }

    private void init() {

        MaterialButton resetButton = verificationComponent.findViewById(R.id.resetBtn);
        MaterialTextView verificationTitleText = verificationComponent.findViewById(R.id.description);
        mobileEditText = verificationComponent.findViewById(R.id.mobileText);
        sendButton = verificationComponent.findViewById(R.id.requestOtpBtn);
        note = verificationComponent.findViewById(R.id.message);
        note.setText(UtilBean.getMyLabel(note.getText().toString()));
        timer = verificationComponent.findViewById(R.id.timer);
        otpText = verificationComponent.findViewById(R.id.otpText);
        verifyButton = verificationComponent.findViewById((R.id.verifyOtpBtn));
        noticeText = verificationComponent.findViewById((R.id.notice));
        smsScheme = verificationComponent.findViewById((R.id.smsScheme));
        scheme = verificationComponent.findViewById((R.id.scheme));
        scheme.setText(UtilBean.getMyLabel(scheme.getText().toString()));
        instruction = verificationComponent.findViewById((R.id.instruction));
        instruction.setText(UtilBean.getMyLabel(instruction.getText().toString()));
        imageView = verificationComponent.findViewById((R.id.image));

        verificationTitleText.setText(UtilBean.getMyLabel(LabelConstants.VERIFY_MOBILE_NUMBER));
        if (otpText.getEditText() != null) {
            otpText.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        timerCounter[0] = new Timer();

        otpText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyButton.setEnabled(Pattern.compile("^\\d{4}$").matcher(s).matches());
            }
        });

        smsScheme.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                queFormBean.setAnswer("T/T");
            } else {
                queFormBean.setAnswer("T/F");
            }
        }));

        resetButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);

        String key = queFormBean.getRelatedpropertyname();
        if (queFormBean.getLoopCounter() > 0) {
            key += queFormBean.getLoopCounter();
        }

        String mobNumber = SharedStructureData.relatedPropertyHashTable.get(key);
        if (mobNumber != null) {
            String mobileNumber = mobNumber.replace("F/", "");
            mobileEditText.setText(mobileNumber);
            queFormBean.setAnswer(null);
        }

    }

    private void showProcessDialog() {
        if (processDialog == null || !processDialog.isShowing()) {
            uIHandler.post(() -> {
                processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
                processDialog.show();
            });
        }
    }

    private void hideProcessDialog() {
        if (processDialog != null && processDialog.isShowing()) {
            uIHandler.post(() -> processDialog.dismiss());
        }
    }
}
