package com.argusoft.medplat.nutrition.model;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) referral
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_referral_detail")
public class ChildCmtcNrcReferralDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "referred_from")
    private Integer referredFrom;

    @Column(name = "referred_to")
    private Integer referredTo;

    @Column(name = "referred_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referredDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(Integer referredFrom) {
        this.referredFrom = referredFrom;
    }

    public Integer getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(Integer referredTo) {
        this.referredTo = referredTo;
    }

    public Date getReferredDate() {
        return referredDate;
    }

    public void setReferredDate(Date referredDate) {
        this.referredDate = referredDate;
    }
}
