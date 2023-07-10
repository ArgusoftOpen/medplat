package com.argusoft.medplat.rch.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * Define morbidity details primary key.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Embeddable
public class MorbidityDetailsPKey implements Serializable {

    @Basic(optional = false)
    @Column(name = "morbidity_id")
    private Integer morbidityId;

    @Basic(optional = false)
    @Column(name = "code")
    private String code;

    public MorbidityDetailsPKey() {
    }

    public MorbidityDetailsPKey(Integer morbidityId, String code) {
        this.morbidityId = morbidityId;
        this.code = code;
    }

    public Integer getMorbidityId() {
        return morbidityId;
    }

    public void setMorbidityId(Integer morbidityId) {
        this.morbidityId = morbidityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MorbidityDetailsPKey that = (MorbidityDetailsPKey) o;
        return Objects.equals(morbidityId, that.morbidityId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(morbidityId, code);
    }
}
