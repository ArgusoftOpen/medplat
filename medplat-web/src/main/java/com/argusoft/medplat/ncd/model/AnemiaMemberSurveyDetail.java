package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "anemia_member_detail")
@Data
public class AnemiaMemberSurveyDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "inclusion1")
    private Boolean inclusion1;

    @Column(name = "inclusion2")
    private Boolean inclusion2;

    @Column(name = "inclusion3")
    private Boolean inclusion3;

    @Column(name = "inclusion4")
    private Boolean inclusion4;

    @Column(name = "inclusion5")
    private Boolean inclusion5;

    @Column(name = "inclusion6")
    private Boolean inclusion6;

    @Column(name = "exclusion1")
    private Boolean exclusion1;

    @Column(name = "exclusion2")
    private Boolean exclusion2;

    @Column(name = "exclusion3")
    private Boolean exclusion3;

    @Column(name = "inclusion_comment")
    private String inclusionComment;

    @Column(name = "exclusion_comment")
    private String exclusionComment;

    @Column(name = "eligibility_comment")
    private String eligibilityComment;

    @Column(name = "eligible")
    private Boolean eligible;

    @Column(name = "not_eligible_reason")
    private String notEligibleReason;

    @Column(name = "clinical_setting_type")
    private String clinicalSettingType;

    @Column(name = "visit_reason")
    private String visitReason;

    @Column(name = "service_attended")
    private String serviceAttended;

    @Column(name = "smoking_history")
    private String smokingHistory;

    @Column(name = "lack_of_energy")
    private Boolean lackOfEnergy;

    @Column(name = "breath_shortness")
    private Boolean breathShortness;

    @Column(name = "fast_heart_beat")
    private Boolean fastHeartBeat;

    @Column(name = "losing_weight")
    private Boolean losingWeight;

    @Column(name = "chest_pain")
    private Boolean chestPain;

    @Column(name = "dizziness")
    private Boolean dizziness;

    @Column(name = "headache")
    private Boolean headache;

    @Column(name = "leg_cramp")
    private Boolean legCramp;

    @Column(name = "brittle_nail_hair")
    private Boolean brittleNailHair;

    @Column(name = "adult_other_symptom")
    private String adultOtherSymptom;

    @Column(name = "child_looks_pale")
    private Boolean childLooksPale;

    @Column(name = "child_losing_weight")
    private Boolean childLosingWeight;

    @Column(name = "child_tired")
    private Boolean childTired;

    @Column(name = "child_breathing_difficulty")
    private Boolean childBreathingDifficulty;

    @Column(name = "child_other_symptom")
    private String childOtherSymptom;

    @Column(name = "data_collection_place")
    private String dataCollectionPlace;

    @Column(name = "data_collection_location")
    private String dataCollectionLocation;

    @Column(name = "data_collection_time")
    private String dataCollectionTime;

    @Column(name = "data_collection_season")
    private String dataCollectionSeason;

    @Column(name = "light_intensity")
    private String lightIntensity;

    @Column(name = "infant_height")
    private Integer infantHeight;

    @Column(name = "infant_weight")
    private Integer infantWeight;

    @Column(name = "systolic_bp")
    private Integer systolicBp;

    @Column(name = "diastolic_bp")
    private Integer diastolicBp;

    @Column(name = "skin_tone_l")
    private Integer skinToneL;

    @Column(name = "skin_tone_b")
    private Integer skinToneB;

    @Column(name = "nail_polish_or_henna")
    private Boolean nailPolishOrHenna;

    @Column(name = "nail_examination")
    private String nailExamination;

    @Column(name = "other_nail_examination")
    private String otherNailExamination;

    @Column(name = "finger_or_hand_examination")
    private String fingerOrHandExamination;

    @Column(name = "other_finger_or_hand_examination")
    private String otherFingerOrHandExamination;

    @Column(name = "eye_examination")
    private String eyeExamination;

    @Column(name = "other_eye_examination")
    private String otherEyeExamination;

    @Column(name = "tongue_examination")
    private String tongueExamination;

    @Column(name = "ppg_data_collection")
    private String ppgDataCollection;

    @Column(name = "hemocue")
    private Integer hemocue;

    @Column(name = "sickle_cell_disease")
    private String sickleCellDisease;

    @Column(name = "lab_id")
    private Integer labId;

    @Column(name = "patient_uuid")
    private String patientUuid;

    @Column(name = "patient_fhir_resource_data")
    private String patientFhirResourceData;

    @Column(name = "hemoglobin", columnDefinition = "numeric", precision = 6, scale = 2)
    private Double hemoglobin;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "hypertension")
    private Boolean hypertension;

    @Column(name = "diabetes_mellitus")
    private Boolean diabetesMellitus;

    @Column(name = "preeclampsia")
    private Boolean preeclampsia;

    @Column(name = "anemia")
    private Boolean anemia;

    @Column(name = "disorders_of_blood")
    private Boolean disordersOfBlood;

    @Column(name = "type_of_blood_disorder")
    private String TypeOfBloodDisorder;

    @Column(name = "cancer_or_malignancy")
    private Boolean cancerOrMalignancy;

    @Column(name = "other_known_diseases")
    private String otherKnownDiseases;

    @Column(name = "other_occupation")
    private String otherOccupation;

    @Column(name = "ambient_temperature", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float ambientTemperature;

    @Column(name = "body_temperature", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float bodyTemperature;
}
