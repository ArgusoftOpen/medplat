/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *<p>Defines fields related to user</p>
 * @author harsh
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "system_configuration")
public class SystemConfiguration implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @Basic(optional = false)
    @Column(name = "system_key", nullable = false, length = 200)
    private String systemKey;
    @Basic(optional = false)
    @Column(name = "key_value", nullable = false, length = 200)
    private String keyValue;
    @Column(name = "is_active")
    private Boolean isActive;

    public SystemConfiguration() {
    }

    public SystemConfiguration(String systemKey) {
        this.systemKey = systemKey;
    }

    public SystemConfiguration(String systemKey, String keyValue) {
        this.systemKey = systemKey;
        this.keyValue = keyValue;
    }

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.systemKey);
        hash = 89 * hash + Objects.hashCode(this.keyValue);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemConfiguration other = (SystemConfiguration) obj;
        if (!Objects.equals(this.systemKey, other.systemKey)) {
            return false;
        }

        return Objects.equals(this.keyValue, other.keyValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SystemConfiguration{" + "systemKey=" + systemKey + ", keyValue=" + keyValue + ", isActive=" + isActive + '}';
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String SYSTEM_KEY = "systemKey";
        public static final String KEY_VALUE = "keyValue";
        public static final String IS_ACTIVE = "isActive";
        public static final String FEATURE_TYPE = "featureType";
        public static final String CONFIG_JSON = "configJson";
    }

}
