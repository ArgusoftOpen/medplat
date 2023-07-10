/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dto;

/**
 *
 * <p>
 *     Used for filter object.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class FilterObj {
        private String displayName;
        private String fieldName;
        private String value;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
}
