package com.argusoft.sewa.android.app.lms;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaseStudyQueAdapter extends RecyclerView.Adapter<CaseStudyQueAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<LmsQuestionConfigDataBean> questionConfigDataBeansList;
    private final Map<Integer, RadioGroup> radioGroupMap = new HashMap<>();
    private Integer minHeight = null;
    private IGetCaseStudyInterface iGetCaseStudyInterface;

    public CaseStudyQueAdapter(Context context, Activity activity, List<LmsQuestionConfigDataBean> questionConfigDataBeansList, IGetCaseStudyInterface iGetCaseStudyInterface) {
        this.context = context;
        this.questionConfigDataBeansList = questionConfigDataBeansList;
        this.activity = activity;
        this.iGetCaseStudyInterface = iGetCaseStudyInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_case_study, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionLabel.setText(String.format(Locale.getDefault(), "Q%d.", position + 1));
        holder.queTitle.setText(questionConfigDataBeansList.get(position).getQuestionTitle());

        Drawable queDrawable = MyStaticComponents.getImageDrawable(context, questionConfigDataBeansList.get(position).getMediaId(), questionConfigDataBeansList.get(position).getMediaName());
        if (queDrawable != null) {
            //holder.queTitle.setCompoundDrawablesWithIntrinsicBounds(queDrawable, null, null, null);
            //holder.queTitle.setCompoundDrawablePadding(20);
            holder.questionImageView.setVisibility(View.VISIBLE);
            holder.questionImageView.setImageDrawable(queDrawable);
        }

        RadioGroup radioGroup = radioGroupMap.get(questionConfigDataBeansList.get(position).getId());
        if (radioGroup == null) {
            radioGroup = new RadioGroup(context);
            radioGroupMap.put(questionConfigDataBeansList.get(position).getId(), radioGroup);
            RadioButton radioButton;
            for (LmsQuestionOptionDataBean option : questionConfigDataBeansList.get(position).getOptions()) {
                radioButton = new RadioButton(context);
                radioButton.setLayoutParams(new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                radioButton.setText(option.getOptionTitle());
                radioButton.setTextColor(ContextCompat.getColorStateList(context, R.color.lms_radio_button_text_selector));
                radioButton.setBackgroundResource(R.drawable.lms_radio_button_selector);
                radioButton.setMinHeight(getMinHeight());
                radioButton.setPadding(20, 20, 20, 20);

                // Adding image in option view
                Drawable imageDrawable = MyStaticComponents.getImageDrawable(context, option.getMediaId(), option.getMediaName());
                if (imageDrawable != null) {
                    radioButton.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, null, null, null);
                    radioButton.setCompoundDrawablePadding(20);
                    radioButton.setGravity(Gravity.CENTER_VERTICAL);
                }
                radioGroup.addView(radioButton);
                RadioGroup finalRadioGroup = radioGroup;
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    iGetCaseStudyInterface.getAnswersOfCaseStudy(radioGroupMap, position, finalRadioGroup.findViewById(finalRadioGroup.getCheckedRadioButtonId()));
                });
            }
        }
        holder.optionsLayout.addView(radioGroup);
        holder.noOfQue.setText(String.format("%s of %s", position + 1, questionConfigDataBeansList.size()));
    }

    private Integer getMinHeight() {
        if (minHeight == null) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            minHeight = (int) typedValue.getDimension(metrics);
        }
        return minHeight;
    }

    @Override
    public int getItemCount() {
        return questionConfigDataBeansList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView queTitle, questionLabel, noOfQue;
        LinearLayout optionsLayout;
        ImageView questionImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            queTitle = itemView.findViewById(R.id.questionTitle);
            questionLabel = itemView.findViewById(R.id.questionLabel);
            optionsLayout = itemView.findViewById(R.id.answerLayout);
            noOfQue = itemView.findViewById(R.id.noOfQue);
            questionImageView = itemView.findViewById(R.id.questionImage);
        }
    }
}

