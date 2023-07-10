/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

/**
 * @author kelvin
 */
public class ValidationTagBean {

    private String method;
    private String message;

    public ValidationTagBean(String method, String message) {
        this.method = method;
        this.message = message;
    }

    public ValidationTagBean() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
