package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Used for response code.
 * </p>
 *
 * @author charmi
 * @since 26/08/20 10:19 AM
 */
public enum ResponseCode {

    SUCCESS(0),
    ERROR(1);

    private final int responseCodeObj;

    ResponseCode(int rCode) {
        this.responseCodeObj = rCode;
    }

    /**
     * Retrieves response code.
     * @return Returns response code.
     */
    public int getResponseCode() {
        return this.responseCodeObj;
    }
}
