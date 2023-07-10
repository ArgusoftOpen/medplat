package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class FillInBlanksComponent extends LinearLayout {

    private final Context context;
    private final String text;
    private final List<EditText> editTextList = new ArrayList<>();

    public FillInBlanksComponent(Context context, String text) {
        super(context);
        this.context = context;
        this.text = text.trim();
        init();
    }

    private void init() {
        String[] strings = text.replace("_____", "RRRRR_____RRRRR").split("RRRRR");

        LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.lms_answer_layout));
        mainLayout.setPadding(20, 20, 20, 20);

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        LinearLayout innerLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);

        for (String str : strings) {
            if (str.equals("_____")) {
                EditText editText = new EditText(context);
                editText.setHint("Enter your answer here");
                LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                params.setMargins(0, 10, 0, 10);
                editText.setLayoutParams(params);
                editText.setBackgroundResource(R.drawable.lms_edit_text_background);
                editText.setPadding(30, 30, 100, 30);
                editTextList.add(editText);
                innerLayout.addView(editText);
            } else if (!str.trim().isEmpty()) {
                MaterialTextView textView = MyStaticComponents.generateAnswerView(context, str);
                textView.setTextColor(ContextCompat.getColor(context, R.color.lms_blanks_text));
                textView.setPadding(0, 20, 0, 20);
                innerLayout.addView(textView);
            }
        }

        mainLayout.addView(innerLayout);
        this.addView(mainLayout);
    }

    public List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        String ans;
        for (EditText editText : editTextList) {
            ans = editText.getText().toString().trim();
            if (!ans.isEmpty()) {
                answers.add(ans);
            }
        }
        return answers;
    }

    public List<EditText> getEditTextList() {
        return editTextList;
    }
}
