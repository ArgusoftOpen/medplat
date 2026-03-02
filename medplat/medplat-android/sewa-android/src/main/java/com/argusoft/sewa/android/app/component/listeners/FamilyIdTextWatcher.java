package com.argusoft.sewa.android.app.component.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.argusoft.sewa.android.app.constants.LabelConstants;

import java.util.regex.Pattern;

public class FamilyIdTextWatcher implements TextWatcher {

    private String before;
    private boolean flag = false;
    private EditText familyEditText;

    public FamilyIdTextWatcher(EditText familyEditText) {
        this.familyEditText = familyEditText;
        init();
    }

    private void init() {
        familyEditText.setText(LabelConstants.FAMILY_ID_PREFIX);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String search = s.toString().toUpperCase();
        if (Pattern.compile("^((FM/\\d{0,4})|((FM/\\d{4}/\\d?)|(FM/\\d{4}/\\d{1,10}N?)))$").matcher(search).matches()) {
            before = search;
        }
        flag = s.length() == 8;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        flag = flag && (s.length() < 8);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = s.toString().toUpperCase();
        if (flag && Pattern.compile("^(FM/)\\d{0,4}$").matcher(search).matches())
            return;

        if (search.length() < 3) {
            familyEditText.removeTextChangedListener(this);
            familyEditText.setText(LabelConstants.FAMILY_ID_PREFIX);
            familyEditText.setSelection(3);
            familyEditText.addTextChangedListener(this);
        } else if (search.length() < 8) {
            if (Pattern.compile("^(FM/)\\d{4}$").matcher(search).matches()) {
                familyEditText.removeTextChangedListener(this);
                familyEditText.setText(String.format("%s/", search));
                familyEditText.setSelection(8);
                familyEditText.addTextChangedListener(this);
            } else if (!Pattern.compile("^(FM/)\\d{0,4}$").matcher(search).matches()) {
                familyEditText.removeTextChangedListener(this);
                familyEditText.setText(before);
                familyEditText.setSelection(before.length());
                familyEditText.addTextChangedListener(this);
            }
        } else if (!Pattern.compile("^((FM/\\d{4}/\\d?)|(FM/\\d{4}/\\d{1,10}N?))$").matcher(search).matches()) {
            if (search.length() == 8 && search.split("/").length < 3) {
                before += "/";
                if (Pattern.compile("^((FM/\\d{4}/\\d?)|(FM/\\d{4}/\\d{1,10}N?))$").matcher(before + search.charAt(search.length() - 1)).matches()) {
                    before += search.charAt(search.length() - 1);
                }
            }
            familyEditText.removeTextChangedListener(this);
            familyEditText.setText(before);
            familyEditText.setSelection(before.length());
            familyEditText.addTextChangedListener(this);
        }
        if(search.split("FM/").length == 3) {
            search = search.replaceFirst("FM/", "");
            if (Pattern.compile("^((FM/\\d{0,4})|((FM/\\d{4}/\\d?)|(FM/\\d{4}/\\d{1,10}N?)))$").matcher(search).matches()) {
                familyEditText.removeTextChangedListener(this);
                familyEditText.setText(search);
                familyEditText.setSelection(search.length());
                familyEditText.addTextChangedListener(this);
            }
        }
    }

}
