/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * @author kelvin
 */
public class FormulaTagBean {

    private String formulavalue;

    public String getFormulavalue() {
        return formulavalue;
    }

    public void setFormulavalue(String formulavalue) {
        this.formulavalue = formulavalue;
    }

    @NonNull
    @Override
    public String toString() {
        return "FormulaTagBean{" +
                "formulavalue='" + formulavalue + '\'' +
                '}';
    }
}
