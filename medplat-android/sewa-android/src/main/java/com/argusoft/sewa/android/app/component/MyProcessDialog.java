/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.google.android.material.textview.MaterialTextView;

/**
 * @author alpeshkyada
 */
public class MyProcessDialog extends Dialog {

    private Context context;
    private String title;
    private LinearLayout parentLayout;

    public MyProcessDialog(Context context, String msg) {
        super(context);
        this.context = context;
        this.title = msg;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(parentLayout);
    }

    private void init() {
        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parentLayout.setGravity(Gravity.CENTER);

        parentLayout.setPadding(15, 15, 15, 15);
        parentLayout.addView(new ProgressBar(context));

        if (title != null) {
            LinearLayout linearLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(30, 0, 30, 0);
            MaterialTextView materialTextView = MyStaticComponents.getMaterialTextView(context, title,
                    IdConstants.PROGRESS_DIALOG_MESSAGE_ID, R.style.CustomQuestionView, false);
            linearLayout.addView(materialTextView);
            parentLayout.addView(linearLayout);
        }
    }
}
