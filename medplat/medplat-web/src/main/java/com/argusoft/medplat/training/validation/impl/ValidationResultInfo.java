/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.validation.impl;

import com.argusoft.medplat.training.validation.ValidationResult;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * <p>
 * Implementation of methods define in validation result dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValidationResultInfo", propOrder = {
    "element", "level", "message"})

public class ValidationResultInfo 
        implements ValidationResult, Serializable{
    
    private static final int serialVersionUID = 1;
    @XmlElement
    private String element;
    @XmlElement
    private ErrorLevel level;
    @XmlElement
    private String message;
//  used to hold debugging information 
//  not intended to be sent over the wire          
    private transient Object invalidData;

    /**
     * Constructs a new ValidationResultInfo.
     */
    public ValidationResultInfo() {
        this.level = ErrorLevel.OK;
    }

    /**
     * Convenience constructor carried over from R1.
     *
     * @param element Element name.
     */
    public ValidationResultInfo(String element) {
        this();
        this.element = element;
    }

    /**
     * Convenience constructor carried over from R1.
     *
     * @param element  Element name.
     * @param invalidData Invalid data.
     */
    public ValidationResultInfo(String element,
            Object invalidData) {
        this(element);
        this.invalidData = invalidData;
    }

    /**
     * Constructs a new ValidationResultInfo from another ValidationResult.
     *
     * @param result the ValidationResult to copy
     */
    public ValidationResultInfo(ValidationResult result) {
        if (result != null) {
            this.level = result.getLevel();
            this.element = result.getElement();
            this.message = result.getMessage();
        }
    }
    
    /**
     * Constructs a new ValidationResultInfo from another ValidationResult.
     * @param level Error level.
     * @param element Element name.
     * @param message Error message.
     */
    public ValidationResultInfo(String element, ErrorLevel level, String message) {
        this.element = element;
        this.level = level;
        this.message = message;
    }

    /**
     * @deprecated
     * This is for compatibility with R1. Use getLevel instead.
     *
     * @return Returns error level.
     */
    @Deprecated(since = "21-MAY-2020", forRemoval = false)
    public ErrorLevel getErrorLevel() {
        return level;
    }

    /**
     * Set error level.
     * @param errorLevel Error level.
     */
    public void setErrorLevel(ErrorLevel errorLevel) {
        this.level = errorLevel;
    }

    /**
     * Convenience method from R1 to check if this is ok.
     *
     * @return Returns check if is ok or not.
     */
    public boolean isOk() {
        return this.level == ErrorLevel.OK;
    }

    /**
     * Convenience method carried over from R1.
     *
     * @return Returns is warning available or not.
     */
    public boolean isWarn() {
        return this.level == ErrorLevel.WARN;
    }

    /**
     * Convenience method carried over from R1.
     *
     * @return Returns error available or not.
     */
    public boolean isError() {
        return this.level == ErrorLevel.ERROR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElement() {
        return element;
    }

    /**
     * Set element value.
     * @param element Element name.
     */
    public void setElement(String element) {
        this.element = element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ErrorLevel getLevel() {
        return this.level;
    }

    /**
     * Set error level.
     * @param level Error level.
     */
    public void setLevel(ErrorLevel level) {
        this.level = level;
    }

    /**
     * Not part of the contract but carried over from r1.
     *
     * @return Returns invalid data.
     */
    public Object getInvalidData() {
        return invalidData;
    }

    /**
     * Not part of the contract but carried over from r1.
     *
     * @param invalidData Invalid data.
     */
    public void setInvalidData(Object invalidData) {
        this.invalidData = invalidData;
    }

    @Override
    public String toString() {
        return "[" + level + "] Path: [" + element + "] - " + message + " data=[" + invalidData + "]";
    }

    /**
     * Convenience method. Adds a message with an error level of WARN
     *
     * @param message the warning message
     */
    public void setWarning(String message) {
        this.level = ErrorLevel.WARN;
        this.message = message;
    }

    /**
     * Convenience method. Adds a message with an error level of ERROR
     *
     * @param message the error message to add
     */
    public void setError(String message) {
        this.level = ErrorLevel.ERROR;
        this.message = message;
    }
}
