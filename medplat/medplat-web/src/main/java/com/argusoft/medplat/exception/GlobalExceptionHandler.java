package com.argusoft.medplat.exception;

import com.argusoft.medplat.common.util.EmailUtil;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Define methods for global exception.
 * </p>
 *
 * @author charmi
 * @since 26/08/20 10:19 AM
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private EmailUtil emailUtil;

    /**
     * Method of Java's throwable class which prints the throwable along with other details
     * like the line number and class name where the exception occurred.
     *
     * @param e Instance of HttpMessageNotReadableException.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        Logger.getLogger(GlobalExceptionHandler.class.getName()).log(Level.INFO, e.getMessage(), e);
    }

    /**
     * Handle exception.
     *
     * @param e Instance of ImtechoUserException.
     * @return Returns entity of imptecho response.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<ImtechoResponseEntity> handleException(ImtechoUserException e) {
        if (e != null) {
            ImtechoResponseEntity rE = (e).getResponse();
            return new ResponseEntity<>(rE, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle internal server error.
     *
     * @param request Instance of HttpServletRequest.
     * @param e       Instance of Exception.
     * @return Returns entity of imptecho response.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<ImtechoResponseEntity> handleException(HttpServletRequest request, Exception e) {
        if (e instanceof ImtechoSystemException) {
            ImtechoResponseEntity imtechoResponseEntity = ((ImtechoSystemException) e).getResponse();
            String message = ((ImtechoSystemException) e).getResponse().message;
            emailUtil.sendExceptionEmail(e, null, request, message);
            return new ResponseEntity<>(imtechoResponseEntity, HttpStatus.BAD_REQUEST);
        } else if (!(e instanceof ImtechoUserException) && !(e instanceof ClientAbortException)) {
            emailUtil.sendExceptionEmail(e, null, request, "");
            Logger.getLogger(GlobalExceptionHandler.class.getName()).log(Level.INFO, e.getMessage(), e);
        }
        return new ResponseEntity<>(new ImtechoResponseEntity(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
