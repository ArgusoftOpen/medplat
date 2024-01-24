/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "imt_member_ncd_detail")
public class NcdMemberEntity extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "last_service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastServiceDate;

    // Last Mo visit date
    @Column(name = "last_mo_visit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMoVisit;

    // Last Mobile Visit Date
    @Column(name = "last_mobile_visit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMobileVisit;

    // Latest comment added by MO
    @Column(name = "last_mo_comment")
    private String lastMoComment;

    // is form filled by mo for diabetes
    @Column(name = "mo_confirmed_diabetes")
    private Boolean moConfirmedDiabetes;

    // is form filled by mo for hyp
    @Column(name = "mo_confirmed_hypertension")
    private Boolean moConfirmedHypertension;

    // is form filled by mo for mental health
    @Column(name = "mo_confirmed_mental_health")
    private Boolean moConfirmedMentalHealth;

    // does suffering from ncd diabetes details table
    @Column(name = "suffering_diabetes")
    private Boolean sufferingDiabetes;

    // does suffering from ncd hypertension details table
    @Column(name = "suffering_hypertension")
    private Boolean sufferingHypertension;

    // does suffering from ncd mental health details table
    @Column(name = "suffering_mental_health")
    private Boolean sufferingMentalHealth;

    // diabetes Details (JSON) : refer NcdMentalHealthDetailDataBean class
    @Column(name = "diabetes_details")
    private String diabetesDetails;

    // hypertension Details (JSON) : refer NcdHypertensionDetailDataBean class
    @Column(name = "hypertension_details")
    private String hypertensionDetails;

    // mental Health Details (JSON) : refer NcdMentalHealthDetailDataBean class
    @Column(name = "mental_health_details")
    private String mentalHealthDetails;

    // Diabetes treatment status from web
    @Column(name = "diabetes_treatment_status")
    private String diabetesTreatmentStatus;

    // hypertension treatment status from web
    @Column(name = "hypertension_treatment_status")
    private String hypertensionTreatmentStatus;

    // Mental Health treatment status from web
    @Column(name = "mentalHealth_treatment_status")
    private String mentalHealthTreatmentStatus;

    // Medicine Details (JSON) : refer NcdMemberMedicineDataBean class
    @Column(name = "medicine_details")
    private String medicineDetails;

    @Column(name = "disease_history")
    private String diseaseHistory;

    @Column(name = "other_disease_history")
    private String otherDiseaseHistory;

    //TODO: medicine related fields are pending

    // Diabetes status - control, uncontrolled, suspected, normal etc
    @Column(name = "diabetes_status")
    private String diabetesStatus;

    // Hypertension status - control, uncontrolled, suspected, normal etc
    @Column(name = "hypertension_status")
    private String hypertensionStatus;

    // Mental Health status - control, uncontrolled, suspected, normal etc
    @Column(name = "mental_health_status")
    private String mentalHealthStatus;

    //TODO: This state related fields are pending
    // Diabetes referral state from web
    @Column(name = "diabetes_state")
    private String diabetesState;

    // Hypertension referral state from web
    @Column(name = "hypertension_state")
    private String hypertensionState;

    // Mental Health referral state from web
    @Column(name = "mental_health_state")
    private String mentalHealthState;

    // Latest Remark
    @Column(name = "last_remark")
    private String lastRemark;

    //CVC form treatement status
    @Column(name = "cvc_treatement_status")
    private String cvcTreatementStatus;

    //User id of MO who has given the comment
    @Column(name = "last_mo_comment_by")
    private Integer lastMoCommentBy;

    //Comment submited from which form
    @Column(name = "last_mo_comment_form_type")
    private String lastMoCommentFormType;

    //User id of user who has given the remark
    @Column(name = "last_remark_by")
    private Integer lastRemarkBy;

    //submited from which form
    @Column(name = "last_remark_form_type")
    private String lastRemarkFormType;

    //expired notification boolean for mobile
    @Column(name = "reference_due")
    private Boolean referenceDue;

    @Column(name = "evening_availability")
    private Boolean eveningAvailability;

}
