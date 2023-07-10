package com.argusoft.sewa.android.app.lms;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Map;

public interface IGetCaseStudyInterface {
    void getAnswersOfCaseStudy(Map<Integer, RadioGroup> radioGroupMap, Integer position, RadioButton selectedRadioButton);
}
