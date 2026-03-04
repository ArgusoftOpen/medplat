/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author alpesh
 */
public class MyListInColorComponent {

    private final Context context;
    private final LinearLayout mainLayout;
    private Map<String, List<String>> mapReasons;

    public MyListInColorComponent(Context context, Map<String, List<String>> mapReasons) {
        this.context = context;
        this.mapReasons = mapReasons;
        this.mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        generateList();
    }

    public void resetList(Map<String, List<String>> mapReasons) {
        this.mapReasons = mapReasons;
        generateList();
    }

    public LinearLayout getListView() {
        return mainLayout;
    }

    private void generateList() {
        mainLayout.removeAllViews();
        boolean flag = false;
        if (mapReasons != null && !mapReasons.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : mapReasons.entrySet()) {
                String key = entry.getKey();
                List<String> valueList = entry.getValue();
                if (valueList != null && !valueList.isEmpty()) {
                    flag = true;
                    StringBuilder stringToDisplay = new StringBuilder();
                    String color = null;
                    int counter = 1;
                    String[] keySplit = UtilBean.split(key, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (keySplit.length == 2) {
                        if (keySplit[1] != null && keySplit[1].length() == 1) {
                            color = keySplit[1];
                        } else {// if name is appends
                            stringToDisplay.append(UtilBean.getMyLabel(keySplit[1])).append("\n");
                        }
                    }
                    for (String reason : valueList) {
                        stringToDisplay.append(counter).append(") ").append(UtilBean.getMyLabel(reason)).append("\n");
                        counter++;
                    }
                    stringToDisplay.setCharAt(stringToDisplay.length() - 1, ' ');
                    MaterialTextView textView = MyStaticComponents.generateAnswerView(context, stringToDisplay.toString());
                    if (color != null) {
                        SewaUtil.setColor(textView, color);
                    } else {
                        textView.setPadding(0, 0, 0, 0);
                    }
                    mainLayout.addView(textView);
                }
            }
        }
        if (!flag) {
            mainLayout.addView(MyStaticComponents.generateInstructionView(context, GlobalTypes.NO_ISSUES_FOUND));
        }
    }
}
