package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class LMSFeedbackComponent extends AlertDialog {

    private LinearLayout parentLayout;
    private final Context context;
    private LmsViewedMediaBean viewedMediaBean;
    private LmsServiceImpl lmsService;

    public LMSFeedbackComponent(Context context, LmsServiceImpl lmsService, LmsViewedMediaBean viewedMediaBean) {
        super(context);
        this.context = context;
        init();
        this.lmsService = lmsService;
        this.viewedMediaBean = viewedMediaBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(parentLayout);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void init() {

        // Parent layout
        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        parentLayoutParams.setMargins(50, 0, 50, 0);

        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                parentLayoutParams);
        parentLayout.setPadding(50, 30, 50, 30);
        parentLayout.setBackgroundResource(R.drawable.alert_dialog);

        // Header for feedback alert
        LinearLayout headerLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setPadding(10, 0, 0, 10);
        MaterialTextView headerView = MyStaticComponents.generateTitleView(context, "Feedback");
        headerLayout.addView(headerView);
        parentLayout.addView(headerLayout);

        View separator = MyStaticComponents.getSeparator(context);
        ((LinearLayout.LayoutParams) separator.getLayoutParams()).setMargins(0, 0, 0, 50);
        parentLayout.addView(separator);

        // Alert message
        LinearLayout bodyLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        bodyLayout.setGravity(Gravity.CENTER);
        bodyLayout.setPadding(0, 0, 0, 30);
        MaterialTextView messageView = new MaterialTextView(context);
        messageView.setText(UtilBean.getMyLabel(LabelConstants.RATE_LESSON));
        messageView.setTextAppearance(context, R.style.AlertDialogLabel);
        bodyLayout.addView(messageView);
        parentLayout.addView(bodyLayout);

        // Footer
        LinearLayout footerLayout = MyStaticComponents.getLinearLayout(context, IdConstants.FOOTER_LAYOUT_ID, LinearLayout.HORIZONTAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ((LinearLayout.LayoutParams) footerLayout.getLayoutParams()).setMargins(0, 20, 0, 0);
        footerLayout.setGravity(Gravity.CENTER);
        footerLayout.setPadding(20, 0, 20, 0);
        footerLayout.setWeightSum(2);

        //Rating TextView
        final MaterialTextView ratingText = new MaterialTextView(context);
        ratingText.setTextAppearance(context, R.style.AlertDialogLabel);
        ratingText.setGravity(Gravity.CENTER);

        final RatingBar ratingBar = new RatingBar(context, null);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1.0f);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (rating == 1.0) {
                ratingText.setText(LabelConstants.BAD);
            } else if (rating == 2.0) {
                ratingText.setText(LabelConstants.POOR);
            } else if (rating == 3.0) {
                ratingText.setText(LabelConstants.AVERAGE);
            } else if (rating == 4.0) {
                ratingText.setText(LabelConstants.GREAT);
            } else if (rating == 5.0) {
                ratingText.setText(LabelConstants.EXCELLENT);
            }
        });

        footerLayout.addView(ratingBar);
        parentLayout.addView(footerLayout);

        LinearLayout buttonLayout = MyStaticComponents.getLinearLayout(context, IdConstants.Button_LAYOUT_ID, LinearLayout.HORIZONTAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ((LinearLayout.LayoutParams) buttonLayout.getLayoutParams()).setMargins(0, 30, 0, 0);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setPadding(30, 0, 30, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        final MaterialButton submitButton = MyStaticComponents.getCustomButton(context, UtilBean.getMyLabel(LabelConstants.SUBMIT),
                104, layoutParams);

        submitButton.setOnClickListener(v -> {
            lmsService.storeUserFeedbackOfMedia((int) ratingBar.getRating(), viewedMediaBean.getLessonId(), viewedMediaBean.getModuleId(), viewedMediaBean.getCourseId());
            dismiss();
            SewaUtil.generateToast(context, LabelConstants.FEEDBACK_SUBMITTED);
        });
        parentLayout.addView(ratingText);
        buttonLayout.addView(submitButton);
        parentLayout.addView(buttonLayout);
    }
}
