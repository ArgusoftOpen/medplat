package com.argusoft.sewa.android.app.restclient;

/**
 * @author alpesh
 */
public enum RestContentType {

    APPLICATION_JSON("application/json");
    private final String value;

    RestContentType(String string) {
        this.value = string;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RestContentType{" + "value=" + value + '}';
    }

}
