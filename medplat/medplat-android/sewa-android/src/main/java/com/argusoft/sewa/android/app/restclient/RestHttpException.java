/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.restclient;

import androidx.annotation.NonNull;

/**
 * @author alpesh
 */
public class RestHttpException extends Exception {

    private final String message;
    private final RestHttpResponseCode statusCode;

    public RestHttpException(RestHttpResponseCode statusCode, String message) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public RestHttpException(Exception exception) {
        super(exception);
        this.message = exception.getMessage();
        this.statusCode = RestHttpResponseCode.SC_INTERNAL_SERVER_ERROR;
    }

    public RestHttpResponseCode getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @NonNull
    @Override
    public String toString() {
        return "HttpRestException{" + "message=" + message + ", statusCode=" + statusCode + '}';
    }

}
