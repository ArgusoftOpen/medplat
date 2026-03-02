package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author avani
 */
@Entity
@Table(name = "user_form_access")
public class UserFormAccess implements Serializable {

    private static final int serialVersionUID = 1;

    @EmbeddedId
    private UserFormAccessPKey userFormAccessPKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 255)
    private State state;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public UserFormAccessPKey getUserFormAccessPKey() {
        return userFormAccessPKey;
    }

    public void setUserFormAccessPKey(UserFormAccessPKey userFormAccessPKey) {
        this.userFormAccessPKey = userFormAccessPKey;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public enum State {

        MOVE_TO_PRODUCTION_RESPONSE_PENDING,
        MOVE_TO_PRODUCTION

    }

    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String USER_FORM_ACCESS_PKEY = "userFormAccessPKey";
        public static final String USER_ID = "userFormAccessPKey.user_id";
        public static final String FORM_CODE = "userFormAccessPKey.form_code";
        public static final String STATE = "state";
        public static final String CREATED_ON = "created_on";
    }

}
