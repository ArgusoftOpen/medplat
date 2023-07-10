package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Define methods for imtecho mobile exception.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public class ImtechoMobileException extends RuntimeException {

    ImtechoResponseEntity agdRes;

    public ImtechoMobileException(String message, Exception exception) {
        super(exception);
        this.agdRes = new ImtechoResponseEntity(message);
    }

    public ImtechoMobileException(String message, int errorCode) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode);
    }

    public ImtechoMobileException(String message, int errorCode, Object data) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode, data);
    }

    /**
     * Retrieves response.
     * @return Returns response details.
     */
    public ImtechoResponseEntity getResponse() {
        return agdRes;
    }
}
