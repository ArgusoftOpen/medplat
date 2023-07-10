/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.validation;

import javax.xml.bind.annotation.XmlEnum;

/**
 *
 * <p>
 *     Used for validation.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public interface ValidationResult {
    
    @XmlEnum
    enum ErrorLevel {

        OK(0), WARN(1), ERROR(2);
        int level;

        ErrorLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public static ErrorLevel fromInt(int level) {
            switch (level) {
                case 0:
                    return OK;
                case 1:
                    return WARN;
                case 2:
                    return ERROR;
                default:
                    throw new IllegalArgumentException(level + "");
            }
        }

        public static ErrorLevel min(ErrorLevel e1, ErrorLevel e2) {
            return e1.ordinal() < e2.ordinal() ? e1 : e2;
        }

        public static ErrorLevel max(ErrorLevel e1, ErrorLevel e2) {
            return e1.ordinal() > e2.ordinal() ? e1 : e2;
        }
    }

    /**
     * Message explaining this validation result
     *
     * If an error it is an an error message.
     *
     * TODO: decide if this is a key that then gets resolved into a real
     * localized message using the message service or the final localized
     * message itself
     *
     * @return Returns message
     */
    String getMessage();

    /**
     * Identifies the element (field) that is the focus of the validation. Uses
     * xpath (dot) notation to navigate to the field, for example:
     * officialIdentifier.code
     *
     * TODO: find out how repeating substructures are handled in this notation,
     * with [n] occurrence brackets?
     *
     * @return Returns element name.
     */
    String getElement();

    /**
     * Indicates the severity of the validation result.
     *
     * 0=OK 1=WARN 2=ERROR
     *
     * @return Returns element level.
     */
    ErrorLevel getLevel();
}
