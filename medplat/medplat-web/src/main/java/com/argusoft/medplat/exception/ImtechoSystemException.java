package com.argusoft.medplat.exception;

/**
 *
 * <p>
 * Define methods for imtecho system exception.
 * </p>
 *
 * @author harshit
 * @since 26/08/20 10:19 AM
 */
public class ImtechoSystemException extends RuntimeException {

    ImtechoResponseEntity agdRes;

    public ImtechoSystemException(String message, Exception exception) {
        super(exception);
        this.agdRes = new ImtechoResponseEntity(message);
    }

    public ImtechoSystemException(String message, int errorCode) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode);
    }

    public ImtechoSystemException(String message, int errorCode, Object data) {
        super(message);
        this.agdRes = new ImtechoResponseEntity(message, errorCode, data);
    }

    public ImtechoSystemException(String message, String systemMessage, Exception exception) {
        super(systemMessage, exception);
        this.agdRes = new ImtechoResponseEntity(message);
    }

    /**
     * Retrieves imtecho system response.
     * @return Returns imtecho system response.
     */
    public ImtechoResponseEntity getResponse() {
        return agdRes;
    }

}
