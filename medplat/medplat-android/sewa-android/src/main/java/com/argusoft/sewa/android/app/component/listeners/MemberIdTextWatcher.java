package com.argusoft.sewa.android.app.component.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.argusoft.sewa.android.app.constants.LabelConstants;

import java.util.regex.Pattern;

public class MemberIdTextWatcher implements TextWatcher {
    private String before;
    private EditText memberEditText;
    private String pattern;

    public MemberIdTextWatcher(EditText memberEditText) {
        this.memberEditText = memberEditText;
        this.pattern = "^((A\\d?)|(A\\d{1,10}N?))$";
        init();
    }

    private void init() {
        memberEditText.setText(LabelConstants.MEMBER_ID_PREFIX);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String search = s.toString().toUpperCase();
        if (Pattern.compile(pattern).matcher(search).matches()) {
            before = search;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing because afterTextChanged will automatically modify it.
    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = s.toString().toUpperCase();
        if (search.length() < 1) {
            memberEditText.removeTextChangedListener(this);
            memberEditText.setText(LabelConstants.MEMBER_ID_PREFIX);
            memberEditText.setSelection(1);
            memberEditText.addTextChangedListener(this);
        } else if (!Pattern.compile(pattern).matcher(search).matches()) {
            memberEditText.removeTextChangedListener(this);
            memberEditText.setText(before);
            memberEditText.setSelection(before.length());
            memberEditText.addTextChangedListener(this);
        }

        if (search.split("A").length == 3) {
            search = search.replaceFirst("A", "");
            if (Pattern.compile(pattern).matcher(search).matches()) {
                memberEditText.removeTextChangedListener(this);
                memberEditText.setText(search);
                memberEditText.setSelection(search.length());
                memberEditText.addTextChangedListener(this);
            }
        }
    }
}
