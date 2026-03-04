package com.argusoft.medplat.sms_queue.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     Defines fields for sms queue
 * </p>
 * @author Harshit
 * @since 03/09/2020 10:30
 */
@Entity
@Table(name = "sms_queue")
@Data
public class SmsQueue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "message_type")
    private String messageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS status;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    @Column(name = "is_sent")
    private Boolean isSent;

    @Column(name = "processed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_on")
    private Date completedOn;

    @Column(name = "exception_string")
    private String exceptionString;
    
    @Column(name = "sms_id")
    private Integer smsId;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SmsQueue)) {
            return false;
        }
        SmsQueue other = (SmsQueue) object;
        return !((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * An util class for string constants of sms queue
     */
    public static class SmsQueueFields {

        private SmsQueueFields(){
        }

        public static final String ID = "id";
        public static final String MOBILE_NUMBER = "mobileNumber";
        public static final String MESSAGE = "message";
        public static final String MESSAGE_TYPE = "messageType";
        public static final String IS_PROCESSED = "isProcessed";
        public static final String IS_SENT = "isSent";
        public static final String PROCESSED_ON = "processedOn";
        public static final String CREATED_ON = "createdOn";
        public static final String STATUS = "status";
        public static final String EXCEPTION_STRING = "exceptionString";
        public static final String COMPLETED_ON = "completedOn";
        public static final String SMSID = "smsId";

    }

    public enum STATUS {
        NEW,
        PROCESSED,
        SENT,
        EXCEPTION
    }

}
