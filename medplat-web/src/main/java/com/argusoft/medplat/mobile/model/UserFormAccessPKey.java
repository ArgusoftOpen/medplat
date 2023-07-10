package com.argusoft.medplat.mobile.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author avani
 */
@Embeddable
public class UserFormAccessPKey implements Serializable{
    
     private static final int serialVersionUID = 1;

    public UserFormAccessPKey(int userId, String formCode) {
        this.userId = userId;
        this.formCode = formCode;
    }

    public UserFormAccessPKey() {
    }
     
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    @Basic(optional = false)
    @Column(name = "form_code" , length = 10)
    private String formCode;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFormAccessPKey that = (UserFormAccessPKey) o;
        return userId == that.userId && Objects.equals(formCode, that.formCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, formCode);
    }
}
