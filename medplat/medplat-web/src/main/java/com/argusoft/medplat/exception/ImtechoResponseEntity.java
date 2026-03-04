package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Define methods for imtecho response exception.
 * </p>
 *
 * @author charmi
 * @since 26/08/20 10:19 AM
 */
public class ImtechoResponseEntity {

    protected String message;
    protected Object data;
    protected int errorcode;

    public ImtechoResponseEntity(String message, int errorcode) {
        this.message = message;
        this.errorcode = errorcode;
    }

    public ImtechoResponseEntity(String message, int errorcode, Object data) {
        this.message = message;
        this.data = data;
        this.errorcode = errorcode;
    }

    ImtechoResponseEntity(String message) {
        this.message = message;
        this.errorcode = ResponseCode.ERROR.getResponseCode();
    }

    /**
     * Retrieve message details.
     * @return Returns message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieve data.
     * @return Returns data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Retrieves error code.
     * @return Returns error code.
     */
    public int getErrorcode() {
        return errorcode;
    }

}
