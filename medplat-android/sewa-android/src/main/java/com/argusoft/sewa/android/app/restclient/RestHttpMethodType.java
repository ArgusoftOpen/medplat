/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.restclient;

/**
 * @author alpesh
 */
public enum RestHttpMethodType {

    HTTP_GET(1),
    HTTP_POST(2),
    HTTP_PUT(3),
    HTTP_DELETE(4);

    private final int value;

    RestHttpMethodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HttpRestMethodType{" + "value=" + value + '}';
    }

}
