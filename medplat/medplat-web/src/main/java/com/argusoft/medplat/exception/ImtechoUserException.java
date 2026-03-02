package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Define methods for imtecho user exception.
 * </p>
 *
 * @author charmi
 * @since 26/08/20 10:19 AM
 */
public class ImtechoUserException extends RuntimeException {

    ImtechoResponseEntity agdRes;

    public ImtechoUserException(String message, Exception exception) {
        super(message, exception);
        this.agdRes = new ImtechoResponseEntity(message);
    }

    public ImtechoUserException(String message, int errorCode) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode);
    }

    public ImtechoUserException(String message, int errorCode, Object data) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode, data);
    }

    /**
     * Retrieves imtecho user exception response.
     * @return Returns imtecho user exception response.
     */
    public ImtechoResponseEntity getResponse() {
        return agdRes;
    }

}
