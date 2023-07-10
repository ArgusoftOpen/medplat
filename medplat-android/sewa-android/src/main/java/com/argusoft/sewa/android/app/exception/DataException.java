package com.argusoft.sewa.android.app.exception;

/**
 * Created by prateek on 11/6/19
 */
public class DataException extends RuntimeException {

    private final transient ResponseEntity response;

    public DataException(String message, Exception exception) {
        super(exception);
        this.response = new ResponseEntity(message);
    }

    public DataException(String message, int errorCode) {
        super(message);
        this.response = new ResponseEntity(message, errorCode);
    }

    public DataException(String message, int errorCode, Object data) {
        super(message);
        this.response = new ResponseEntity(message, errorCode, data);
    }

    public ResponseEntity getResponse() {
        return response;
    }
}
