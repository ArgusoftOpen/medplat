package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

/**
 * @author alpeshkyada
 */
public class MyAlertDialog extends AlertDialog {

    private LinearLayout parentLayout;

    public MyAlertDialog(Context context,
                         boolean isCancelable,
                         String msg,
                         View.OnClickListener lister,
                         int buttonCode) {
        super(context, isCancelable, new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface di) {
                Log.i(getClass().getSimpleName(), "OnCancelListener called");
            }
        });
        init(context, msg, lister, buttonCode, null, null);
    }

    public MyAlertDialog(Context context,
                         String msg,
                         View.OnClickListener lister,
                         int buttonCode) {
        super(context);
        init(context, msg, lister, buttonCode, null, null);
    }

    // Only for Alert Dialog with buttonCode == DynamicUtils.BUTTON_YES_NO and change the Button Text
    public MyAlertDialog(Context context,
                         String msg,
                         View.OnClickListener lister,
                         int buttonCode,
                         String button1,
                         String button2) {
        super(context);
        init(context, msg, lister, buttonCode, button1, button2);
    }

    public MyAlertDialog(Context context,
                         Spanned msg,
                         View.OnClickListener lister,
                         int buttonCode,
                         String button1,
                         String button2) {
        super(context);
        init(context, msg, lister, buttonCode, button1, button2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(parentLayout);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void init(Context context,
                      String msg,
                      View.OnClickListener lister,
                      int buttonCode,
                      String button1,
                      String button2) {

        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        parentLayoutParams.setMargins(50, 0, 50, 0);

        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                parentLayoutParams);
        parentLayout.setPadding(50, 30, 50, 30);
        parentLayout.setBackgroundResource(R.drawable.alert_dialog);

        if (msg != null && msg.trim().length() > 0) {
            NestedScrollView bodyScroller = MyStaticComponents.getScrollView(context, -1, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
            LinearLayout bodyLayout = MyStaticComponents.getLinearLayout(
                    context, -1, LinearLayout.VERTICAL,
                    new LinearLayout.LayoutParams(
                            WRAP_CONTENT,
                            WRAP_CONTENT));
            bodyLayout.setGravity(Gravity.CENTER);
            bodyLayout.setPadding(0, 0, 0, 30);
            MaterialTextView messageView = new MaterialTextView(context);
            messageView.setText(UtilBean.getMyLabel(msg));
            messageView.setTextAppearance(context, R.style.AlertDialogLabel);
            bodyLayout.addView(messageView);
            bodyScroller.addView(bodyLayout);
            parentLayout.addView(bodyScroller);
        }

        String btn1 = LabelConstants.OK;
        String btn2 = LabelConstants.CANCEL;
        if (buttonCode == DynamicUtils.BUTTON_YES_NO) {
            btn1 = LabelConstants.YES;
            btn2 = LabelConstants.NO;
            if (button1 != null && button2 != null) {
                btn1 = button1;
                btn2 = button2;
            }
        } else if (buttonCode == DynamicUtils.BUTTON_HIDE_LOGOUT) {
            btn1 = LabelConstants.LOGOUT;
            btn2 = LabelConstants.HIDE;
        } else if (buttonCode == DynamicUtils.BUTTON_RETRY_CANCEL) {
            btn1 = LabelConstants.RETRY;
            btn2 = LabelConstants.CANCEL;
        }

        LinearLayout footerLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        footerLayout.setGravity(Gravity.CENTER);
        footerLayout.setPadding(30, 0, 30, 0);
        footerLayout.setWeightSum(2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1);
        layoutParams.setMargins(20, 0, 20, 0);

        MaterialButton positiveButton = MyStaticComponents.getCustomButton(context, btn1, BUTTON_POSITIVE, layoutParams);
        positiveButton.setCornerRadius(0);
        positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
        positiveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonBackground));

        MaterialButton negativeButton = null;
        if (buttonCode != DynamicUtils.BUTTON_OK) {
            negativeButton = MyStaticComponents.getCustomButton(context, btn2, BUTTON_NEGATIVE, layoutParams);
            negativeButton.setCornerRadius(0);
            negativeButton.setTypeface(Typeface.DEFAULT_BOLD);
            negativeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            footerLayout.addView(negativeButton);
        } else {
            positiveButton.setId(DynamicUtils.BUTTON_OK);
        }
        footerLayout.addView(positiveButton);
        parentLayout.addView(footerLayout);

        if (lister != null) {
            if (negativeButton != null) {
                negativeButton.setOnClickListener(lister);
            }
            positiveButton.setOnClickListener(lister);
        } else {
            View.OnClickListener onClickListener = v -> hideDialog();
            if (negativeButton != null) {
                negativeButton.setOnClickListener(onClickListener);
            }
            positiveButton.setOnClickListener(onClickListener);
        }
    }

    private void init(Context context,
                      Spanned msg,
                      View.OnClickListener lister,
                      int buttonCode,
                      String button1,
                      String button2) {

        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        parentLayoutParams.setMargins(50, 0, 50, 0);

        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                parentLayoutParams);
        parentLayout.setPadding(50, 30, 50, 30);
        parentLayout.setBackgroundResource(R.drawable.alert_dialog);

        if (msg != null && msg.length() > 0) {
            NestedScrollView bodyScroller = MyStaticComponents.getScrollView(context, -1, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
            LinearLayout bodyLayout = MyStaticComponents.getLinearLayout(
                    context, -1, LinearLayout.VERTICAL,
                    new LinearLayout.LayoutParams(
                            WRAP_CONTENT,
                            WRAP_CONTENT));
            bodyLayout.setGravity(Gravity.CENTER);
            bodyLayout.setPadding(0, 0, 0, 30);
            MaterialTextView messageView = new MaterialTextView(context);
            messageView.setText(msg);
            messageView.setTextAppearance(context, R.style.AlertDialogLabel);
            bodyLayout.addView(messageView);
            bodyScroller.addView(bodyLayout);
            parentLayout.addView(bodyScroller);
        }

        String btn1 = LabelConstants.OK;
        String btn2 = LabelConstants.CANCEL;
        if (buttonCode == DynamicUtils.BUTTON_YES_NO) {
            btn1 = LabelConstants.YES;
            btn2 = LabelConstants.NO;
            if (button1 != null && button2 != null) {
                btn1 = button1;
                btn2 = button2;
            }
        } else if (buttonCode == DynamicUtils.BUTTON_HIDE_LOGOUT) {
            btn1 = LabelConstants.LOGOUT;
            btn2 = LabelConstants.HIDE;
        } else if (buttonCode == DynamicUtils.BUTTON_RETRY_CANCEL) {
            btn1 = LabelConstants.RETRY;
            btn2 = LabelConstants.CANCEL;
        }

        LinearLayout footerLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        footerLayout.setGravity(Gravity.CENTER);
        footerLayout.setPadding(30, 0, 30, 0);
        footerLayout.setWeightSum(2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1);
        layoutParams.setMargins(20, 0, 20, 0);

        MaterialButton positiveButton = MyStaticComponents.getCustomButton(context, btn1, BUTTON_POSITIVE, layoutParams);
        positiveButton.setCornerRadius(0);
        positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
        positiveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonBackground));

        MaterialButton negativeButton = null;
        if (buttonCode != DynamicUtils.BUTTON_OK) {
            negativeButton = MyStaticComponents.getCustomButton(context, btn2, BUTTON_NEGATIVE, layoutParams);
            negativeButton.setCornerRadius(0);
            negativeButton.setTypeface(Typeface.DEFAULT_BOLD);
            if (SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_KIOSK) ||
                    SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_MRP) ){
                negativeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.normal));
            } else {
                negativeButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
            footerLayout.addView(negativeButton);
        } else {
            positiveButton.setId(DynamicUtils.BUTTON_OK);
        }
        footerLayout.addView(positiveButton);
        parentLayout.addView(footerLayout);

        if (lister != null) {
            if (negativeButton != null) {
                negativeButton.setOnClickListener(lister);
            }
            positiveButton.setOnClickListener(lister);
        } else {
            View.OnClickListener onClickListener = v -> hideDialog();
            if (negativeButton != null) {
                negativeButton.setOnClickListener(onClickListener);
            }
            positiveButton.setOnClickListener(onClickListener);
        }
    }

    private void hideDialog() {
        this.dismiss();
    }
}
