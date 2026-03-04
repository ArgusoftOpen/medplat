/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.restclient;

/**
 * @author alpesh
 */
public enum RestHttpResponseCode {

    SC_OK(200),
    SC_OK_NO_CONTENT(204),
    SC_BAD_REQUEST(400),
    SC_UNAUTHORIZED(401),
    SC_FORBIDDEN(403),
    SC_NOT_FOUND(404),
    SC_NOT_ACCEPTABLE(406),
    SC_REQUEST_TIMEOUT(408),
    SC_INTERNAL_SERVER_ERROR(500),
    // custome code
    SC_PERAMETER_NOT_PROPER(600),
    SC_RESPONSE_NOT_FORMATED(601),
    SC_URL_NULL(602),
    SC_NOT_IMPLEMENTED_STILL(603),
    SC_NO_HTTP_METHOD_TYPE(604),
    SC_ERROR_IN_READING_RESONSE(605),
    SC_OTHER(666);

    // other that we want
    private final int value;

    RestHttpResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RestHttpResponseCode{" + "code=" + value + '}';
    }

}
