/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author kunjan
 */
public class TextChangeListener implements TextWatcher {

    private EditText editText;
    private int beforeLength;

    public TextChangeListener(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s != null) {
            beforeLength = s.length();
        } else {
            beforeLength = 0;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        editText.removeTextChangedListener(this);
        if (s == null || s.length() == 0) {
            editText.setText(null);
        } else if (s.length() > beforeLength && s.length() > 4) {
            StringBuilder sb = new StringBuilder(s.toString().replace("-", ""));
            int j = 4;
            int m = sb.length();
            for (int i = 0; i < m / 4; i++) {
                sb.insert(j, "-");
                j += 5;
            }
            editText.setText(sb.toString());
            editText.setSelection(editText.getText().length());
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        //No need to do anything
    }

}
