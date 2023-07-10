package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.nutrition.enums.Identification;
import com.argusoft.medplat.nutrition.enums.State;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * <p>
 *     Defines fields for Child Nutrition CMAM
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_nutrition_cmam_master")
public class ChildNutritionCmamMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "location_id")
    private Integer locationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "identified_from")
    private Identification identifiedFrom;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "is_case_completed")
    private Boolean isCaseCompleted;

    @Column(name = "cured_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date curedOn;

    @Column(name = "cured_muac", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float curedMuac;

    @Column(name = "cured_visit_id")
    private Integer curedVisitId;

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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
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

    public Boolean getCaseCompleted() {
        return isCaseCompleted;
    }

    public void setCaseCompleted(Boolean caseCompleted) {
        isCaseCompleted = caseCompleted;
    }

    public Date getCuredOn() {
        return curedOn;
    }

    public void setCuredOn(Date curedOn) {
        this.curedOn = curedOn;
    }

    public Float getCuredMuac() {
        return curedMuac;
    }

    public void setCuredMuac(Float curedMuac) {
        this.curedMuac = curedMuac;
    }

    public Integer getCuredVisitId() {
        return curedVisitId;
    }

    public void setCuredVisitId(Integer curedVisitId) {
        this.curedVisitId = curedVisitId;
    }

    public static class Fields {
        private Fields(){
        }
        public static final String ID = "id";
        public static final String CHILD_ID = "childId";
        public static final String LOCATION_ID = "locationId";
        public static final String STATE = "state";
        public static final String SERVICE_DATE = "serviceDate";
        public static final String IS_CASE_COMPLETED = "isCaseCompleted";
    }
}
