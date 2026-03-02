/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Define methods for email exception.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public class EmailException extends Exception {

    private String errorMessage = "Email Exception occurred.";

    public EmailException() {
        super();
    }

    public EmailException(String msg) {
        super(msg);
        this.errorMessage = msg;
    }

    public EmailException(String userMsg, String errorMsg) {
        super(userMsg + "\n" + errorMsg);
        this.errorMessage = userMsg + "\n" + errorMsg;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return  the detail message string of this {@code Throwable} instance
     *          (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return errorMessage;
    }
}

