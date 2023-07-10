
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.nutrition.enums.Identification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) screening
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_screening_detail")
public class ChildCmtcNrcScreening extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "screened_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screenedOn;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "state")
    private String state;

    @Column(name = "appetite_test_done")
    private String appetiteTestDone;

    @Column(name = "appetite_test_reported_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date appetiteTestReportedOn;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "discharge_id")
    private Integer dischargeId;

    @Column(name = "is_direct_admission")
    private Boolean isDirectAdmission;

    @Column(name = "screening_center")
    private Integer screeningCenter;

    @Column(name = "is_case_completed")
    private Boolean isCaseCompleted;

    @Column(name = "referred_from")
    private Integer referredFrom;

    @Column(name = "referred_to")
    private Integer referredTo;

    @Column(name = "referred_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referredDate;

    @Column(name = "is_archive")
    private Boolean isArchive;

    @Column(name = "identified_from")
    @Enumerated(EnumType.STRING)
    private Identification identifiedFrom;

    @Column(name = "reference_id")
    private Integer referenceId;

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

    public Date getScreenedOn() {
        return screenedOn;
    }

    public void setScreenedOn(Date screenedOn) {
        this.screenedOn = screenedOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAppetiteTestDone() {
        return appetiteTestDone;
    }

    public void setAppetiteTestDone(String appetiteTestDone) {
        this.appetiteTestDone = appetiteTestDone;
    }

    public Date getAppetiteTestReportedOn() {
        return appetiteTestReportedOn;
    }

    public void setAppetiteTestReportedOn(Date appetiteTestReportedOn) {
        this.appetiteTestReportedOn = appetiteTestReportedOn;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getDischargeId() {
        return dischargeId;
    }

    public void setDischargeId(Integer dischargeId) {
        this.dischargeId = dischargeId;
    }

    public Boolean getIsDirectAdmission() {
        return isDirectAdmission;
    }

    public void setIsDirectAdmission(Boolean isDirectAdmission) {
        this.isDirectAdmission = isDirectAdmission;
    }

    public Integer getScreeningCenter() {
        return screeningCenter;
    }

    public void setScreeningCenter(Integer screeningCenter) {
        this.screeningCenter = screeningCenter;
    }

    public Boolean getIsCaseCompleted() {
        return isCaseCompleted;
    }

    public void setIsCaseCompleted(Boolean isCaseCompleted) {
        this.isCaseCompleted = isCaseCompleted;
    }

    public Integer getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(Integer referredFrom) {
        this.referredFrom = referredFrom;
    }

    public Date getReferredDate() {
        return referredDate;
    }

    public void setReferredDate(Date referredDate) {
        this.referredDate = referredDate;
    }

    public Integer getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(Integer referredTo) {
        this.referredTo = referredTo;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Identification getIdentifiedFrom() {
        return identifiedFrom;
    }

    public void setIdentifiedFrom(Identification identifiedFrom) {
        this.identifiedFrom = identifiedFrom;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public static class Fields {
        private Fields(){
        }
        public static final String ID = "id";
        public static final String CHILD_ID = "childId";
        public static final String SCREENING_ID = "screeningId";
        public static final String LOCATION_ID = "locationId";
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
        public static final String STATE = "state";
        public static final String APETITE_TEST_DONE = "appetiteTestDone";
        public static final String APETITE_TEST_REPORTED_ON = "appetiteTestReportedOn";
        public static final String ADMISSION_ID = "admissionId";
        public static final String DISCHARGE_ID = "dischargeId";
        public static final String USER_ID = "userId";
        public static final String SCREENING_CENTER = "screeningCenter";
        public static final String IS_CASE_COMPLETED = "isCaseCompleted";
        public static final String REFERRED_FROM = "referredFrom";
        public static final String REFERRED_TO = "referredTo";
        public static final String REFERRED_DATE = "referredDate";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String IDENTIFIED_FROM = "identifiedFrom";
        public static final String REFERENCE_ID = "referenceId";
    }
}
