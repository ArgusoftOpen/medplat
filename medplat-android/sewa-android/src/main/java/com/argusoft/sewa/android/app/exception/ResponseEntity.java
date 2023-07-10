package com.argusoft.sewa.android.app.exception;

/**
 * Created by prateek on 11/6/19
 */
public class ResponseEntity {

    private String message;
    private Object data;
    private int errorcode;

    public ResponseEntity(String message, int errorcode) {
        this.message = message;
        this.errorcode = errorcode;
    }

    public ResponseEntity(String message, int errorcode, Object data) {
        this.message = message;
        this.data = data;
        this.errorcode = errorcode;
    }

    ResponseEntity(String message) {
        this.message = message;
        this.errorcode = 1;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public int getErrorcode() {
        return errorcode;
    }

}
