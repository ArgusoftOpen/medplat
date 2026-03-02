package com.argusoft.sewa.android.app.databean;

import android.os.Parcel;
import android.os.Parcelable;

import com.argusoft.sewa.android.app.model.MemberBean;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author kunjan
 */
public class MemberDataBean implements Parcelable {

    private String id;

    private String familyId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private String maritalStatus;

    private String aadharNumber;

    private boolean isAadharUpdated;

    private String mobileNumber;

    private String alternateNumber;

    private Boolean familyHeadFlag;

    private Long dob;

    private String uniqueHealthId;

    private String ifsc;

    private String accountNumber;

    private String maaVatsalyaCardNumber;

    private Boolean isPregnantFlag;

    private Long lmpDate;

    private Integer normalCycleDays;

    private String familyPlanningMethod;

    private Long fpInsertOperateDate;

    private String type;

    private String state;

    private String createdBy;

    private String updatedBy;

    private Long createdOn;

    private Long updatedOn;

    private String grandfatherName;

    private String nameBasedOnAadhar;

    private String educationStatus;
    //Comma Separated IDs
    private String congenitalAnomalyIds;

    //Comma Separated IDs
    private String chronicDiseaseIds;

    //Comma Separated IDs
    private String currentDiseaseIds;

    //Comma Separated IDs
    private String eyeIssueIds;

    private String lastMethodOfContraception;

    private Integer yearOfWedding;

    private Boolean jsyPaymentGiven;

    private Boolean isEarlyRegistration;

    private Boolean jsyBeneficiary;

    private Boolean iayBeneficiary;

    private Boolean kpsyBeneficiary;

    private Boolean chiranjeeviYojnaBeneficiary;

    private Float haemoglobin;

    private Float weight;

    private Long edd;

    private String ancVisitDates;

    private String immunisationGiven;

    private String bloodGroup;

    private String placeOfBirth;

    private Float birthWeight;

    private Long motherId;

    private Boolean complementaryFeedingStarted;

    private Boolean isHighRiskCase;

    private Long curPregRegDetId;

    private Long curPregRegDate;

    private Boolean menopauseArrived;

    private String syncStatus;

    private Boolean isIucdRemoved;
    private String chronicDiseaseIdsForTreatment;

    private Long iucdRemovalDate;

    private Long lastDeliveryDate;

    private Boolean hysterectomyDone;

    private String childNrcCmtcStatus;

    private String lastDeliveryOutcome;

    //Comma Separated
    private String previousPregnancyComplication;

    private String additionalInfo;

    private Long npcbScreeningDate;

    private Boolean fhsrPhoneVerified;

    private Short currentGravida;

    private Short currentPara;

    private Long dateOfWedding;

    private Long husbandId;

    private String npcbStatus;

    private Boolean isMobileNumberVerified;

    private String relationWithHof;

    private Long cbacDate;

    private String hypYear;         //Comma Separated financial year in which HYPERTENSION Screening is done

    private String oralYear;        //Comma Separated financial year in which ORAL Screening is done

    private String diabetesYear;    //Comma Separated financial year in which DIABETES Screening is done

    private String breastYear;      //Comma Separated financial year in which BREAST Screening is done

    private String cervicalYear;    //Comma Separated financial year in which CERVICAL Screening is done

    private String mentalHealthYear;    //Comma Separated financial year in which CERVICAL Screening is done

    private String healthYear;      //Comma Separated financial year in which health Screening is done

    private String healthId;

    private String healthIdNumber;

    private Boolean vulnerableFlag;

    private Boolean healthInsurance;

    private String schemeDetail;

    private Boolean personalHistoryDone;

    private Boolean confirmedDiabetes;

    private Boolean suspectedForDiabetes;

    private Integer cbacScore;

    private Boolean sufferingDiabetes;

    private Boolean sufferingHypertension;

    private Boolean sufferingMentalHealth;

    private Boolean eveningAvailability;

    private String pmjayAvailability;

    private String alcoholAddiction;

    private String smokingAddiction;

    private String tobaccoAddiction;

    private Boolean moConfirmedDiabetes;

    private Boolean moConfirmedHypertension;

    private Boolean generalScreeningDone;

    private Boolean ecgTestDone;

    private Boolean retinopathyTestDone;

    private Boolean urineTestDone;

    private String occupation;

    private String physicalDisability;

    private String otherDisability;

    private String cataractSurgery;

    private String otherChronic;

    private String otherEyeIssue;

    private String sickleCellStatus;

    private Boolean isChildGoingSchool;

    private String currentStudyingStandard;

    private String aadharStatus;

    private String pensionScheme;

    private Boolean underTreatmentChronic;

    private Long hmisId;

    private String memberId;

    private String serviceLocation;

    private String counsellingDone;

    private Boolean isHaemoglobinMeasured;

    private Integer ifaTabTakenLastMonth;

    private Integer ifaTabTakenNow;

    private String absorbentMaterialUsed;

    private Boolean isSanitaryPadGiven;

    private Integer numberOfSanitaryPadsGiven;

    private Boolean isHavingMenstrualProblem;

    private String issueWithMenstruation;

    private Boolean isTDInjectionGiven;

    private Long tdInjectionDate;

    private Boolean isAlbandazoleGivenInLastSixMonths;

    private Long adolescentScreeningDate;

    private Integer height;

    private Boolean isPeriodStarted;
    private Long schoolActualId;
    private Long anganwadiId;
    private String areaId;
    private String concatWs;
    private Long hypDiaMentalServiceDate;
    private Long cancerServiceDate;
    private String isHivPositive;
    private String isVDRLPositive;
    private String nutritionStatus;
    private String sdScore;
    private String nrcNumber;
    private Boolean isTbCured;
    private Boolean isTbSuspected;
    private Boolean indexCase;
    private String rdtStatus;
    private String memberReligion;
    private String passportNumber;
    private String searchString;
    private Boolean bcgSurveyStatus;
    private String otherChronicDiseaseTreatment;
    private Boolean bcgEligible;
    private Boolean bcgWilling;
    private Boolean bcgEligibleFilled;
    private String birthCertNumber;

    public MemberDataBean() {
    }

    public MemberDataBean(MemberBean memberBean) {
        this.id = memberBean.getActualId();
        this.familyId = memberBean.getFamilyId();
        this.firstName = memberBean.getFirstName();
        this.middleName = memberBean.getMiddleName();
        this.lastName = memberBean.getLastName();
        this.gender = memberBean.getGender();
        this.maritalStatus = memberBean.getMaritalStatus();
        this.aadharNumber = memberBean.getAadharNumber();
        this.isAadharUpdated = memberBean.isAadharUpdated();
        this.mobileNumber = memberBean.getMobileNumber();
        this.familyHeadFlag = memberBean.getFamilyHeadFlag();
        this.uniqueHealthId = memberBean.getUniqueHealthId();
        this.ifsc = memberBean.getIfsc();
        this.accountNumber = memberBean.getAccountNumber();
        this.maaVatsalyaCardNumber = memberBean.getMaaVatsalyaCardNumber();
        this.isPregnantFlag = memberBean.getIsPregnantFlag();
        this.normalCycleDays = memberBean.getNormalCycleDays();
        this.familyPlanningMethod = memberBean.getFamilyPlanningMethod();
        this.type = memberBean.getType();
        this.state = memberBean.getState();
        this.createdBy = memberBean.getCreatedBy();
        this.updatedBy = memberBean.getUpdatedBy();
        this.grandfatherName = memberBean.getGrandfatherName();
        this.nameBasedOnAadhar = memberBean.getNameBasedOnAadhar();
        this.educationStatus = memberBean.getEducationStatus();
        this.congenitalAnomalyIds = memberBean.getCongenitalAnomalyIds();
        this.eyeIssueIds = memberBean.getEyeIssueIds();
        this.chronicDiseaseIds = memberBean.getChronicDiseaseIds();
        this.currentDiseaseIds = memberBean.getCurrentDiseaseIds();
        this.lastMethodOfContraception = memberBean.getLastMethodOfContraception();
        this.yearOfWedding = memberBean.getYearOfWedding();
        this.jsyPaymentGiven = memberBean.getJsyPaymentGiven();
        this.isEarlyRegistration = memberBean.getEarlyRegistration();
        this.jsyBeneficiary = memberBean.getJsyBeneficiary();
        this.iayBeneficiary = memberBean.getIayBeneficiary();
        this.kpsyBeneficiary = memberBean.getKpsyBeneficiary();
        this.chiranjeeviYojnaBeneficiary = memberBean.getChiranjeeviYojnaBeneficiary();
        this.haemoglobin = memberBean.getHaemoglobin();
        this.weight = memberBean.getWeight();
        this.ancVisitDates = memberBean.getAncVisitDates();
        this.immunisationGiven = memberBean.getImmunisationGiven();
        this.bloodGroup = memberBean.getBloodGroup();
        this.motherId = memberBean.getMotherId();
        this.complementaryFeedingStarted = memberBean.getComplementaryFeedingStarted();
        this.isHighRiskCase = memberBean.getHighRiskCase();
        this.curPregRegDetId = memberBean.getCurPregRegDetId();
        this.menopauseArrived = memberBean.getMenopauseArrived();
        this.syncStatus = memberBean.getSyncStatus();
        this.isIucdRemoved = memberBean.getIucdRemoved();
        this.hysterectomyDone = memberBean.getHysterectomyDone();
        this.childNrcCmtcStatus = memberBean.getChildNrcCmtcStatus();
        this.lastDeliveryOutcome = memberBean.getLastDeliveryOutcome();
        this.previousPregnancyComplication = memberBean.getPreviousPregnancyComplication();
        this.additionalInfo = memberBean.getAdditionalInfo();
        this.fhsrPhoneVerified = memberBean.getFhsrPhoneVerified();
        this.currentGravida = memberBean.getCurrentGravida();
        this.currentPara = memberBean.getCurrentPara();
        this.husbandId = memberBean.getHusbandId();
        this.isMobileNumberVerified = memberBean.getMobileNumberVerified();
        this.relationWithHof = memberBean.getRelationWithHof();
        this.birthWeight = memberBean.getBirthWeight();
        this.placeOfBirth = memberBean.getPlaceOfBirth();
        this.hypYear = memberBean.getHypYear();
        this.diabetesYear = memberBean.getDiabetesYear();
        this.oralYear = memberBean.getOralYear();
        this.breastYear = memberBean.getBreastYear();
        this.cervicalYear = memberBean.getCervicalYear();
        this.mentalHealthYear = memberBean.getMentalHealthYear();
        this.healthId = memberBean.getHealthId();
        this.healthIdNumber = memberBean.getHealthIdNumber();
        this.vulnerableFlag = memberBean.getVulnerableFlag();
        this.healthYear = memberBean.getHealthYear();
        this.confirmedDiabetes = memberBean.getConfirmedDiabetes();
        this.healthInsurance = memberBean.getHealthInsurance();
        this.schemeDetail = memberBean.getSchemeDetail();
        this.suspectedForDiabetes = memberBean.getSuspectedForDiabetes();
        this.personalHistoryDone = memberBean.getPersonalHistoryDone();
        this.cbacScore = memberBean.getCbacScore();
        this.sufferingDiabetes = memberBean.getSufferingDiabetes();
        this.sufferingHypertension = memberBean.getSufferingHypertension();
        this.sufferingMentalHealth = memberBean.getSufferingMentalHealth();
        this.pmjayAvailability = memberBean.getPmjayAvailability();
        this.alcoholAddiction = memberBean.getAlcoholAddiction();
        this.smokingAddiction = memberBean.getSmokingAddiction();
        this.tobaccoAddiction = memberBean.getTobaccoAddiction();
        this.moConfirmedDiabetes = memberBean.getMoConfirmedDiabetes();
        this.moConfirmedHypertension = memberBean.getMoConfirmedHypertension();
        this.generalScreeningDone = memberBean.getGeneralScreeningDone();
        this.ecgTestDone = memberBean.getEcgTestDone();
        this.retinopathyTestDone = memberBean.getRetinopathyTestDone();
        this.urineTestDone = memberBean.getUrineTestDone();
        this.occupation = memberBean.getOccupation();
        this.physicalDisability = memberBean.getPhysicalDisability();
        this.otherDisability = memberBean.getOtherDisability();
        this.otherChronic = memberBean.getOtherChronic();
        this.otherEyeIssue = memberBean.getOtherEyeIssue();
        this.cataractSurgery = memberBean.getCataractSurgery();
        this.sickleCellStatus = memberBean.getSickleCellStatus();
        this.isChildGoingSchool = memberBean.getIsChildGoingSchool();
        this.currentStudyingStandard = memberBean.getCurrentStudyingStandard();
        this.aadharStatus = memberBean.getAadharStatus();
        this.pensionScheme = memberBean.getPensionScheme();
        this.underTreatmentChronic = memberBean.getUnderTreatmentChronic();
        this.alternateNumber = memberBean.getAlternateNumber();
        this.hmisId = memberBean.getHmisId();
        this.isHivPositive = memberBean.getIsHivPositive();
        this.isVDRLPositive = memberBean.getIsVdrlPositive();
        this.nutritionStatus = memberBean.getNutritionStatus();
        this.nrcNumber = memberBean.getNrcNumber();
        this.memberReligion = memberBean.getMemberReligion();
        this.passportNumber = memberBean.getPassportNumber();
        this.searchString = memberBean.getSearchString();
        this.chronicDiseaseIdsForTreatment = memberBean.getChronicDiseaseIdsForTreatment();
        this.otherChronicDiseaseTreatment = memberBean.getOtherChronicDiseaseTreatment();
        this.birthCertNumber = memberBean.getBirthCertNumber();

        if (memberBean.getAdditionalInfo() != null) {
            MemberAdditionalInfoDataBean additionalInfo = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            this.npcbStatus = additionalInfo.getNpcbStatus();
            this.serviceLocation = additionalInfo.getServiceLocation();
            this.counsellingDone = additionalInfo.getCounsellingDone();
            this.isHaemoglobinMeasured = additionalInfo.getHaemoglobinMeasured();
            this.ifaTabTakenLastMonth = additionalInfo.getIfaTabTakenLastMonth();
            this.ifaTabTakenNow = additionalInfo.getIfaTabTakenNow();
            this.absorbentMaterialUsed = additionalInfo.getAbsorbentMaterialUsed();
            this.isSanitaryPadGiven = additionalInfo.getSanitaryPadGiven();
            this.numberOfSanitaryPadsGiven = additionalInfo.getNumberOfSanitaryPadsGiven();
            this.isHavingMenstrualProblem = additionalInfo.getHavingMenstrualProblem();
            this.issueWithMenstruation = additionalInfo.getIssueWithMenstruation();
            this.isTDInjectionGiven = additionalInfo.getTDInjectionGiven();
            this.tdInjectionDate = additionalInfo.getTdInjectionDate();
            this.isAlbandazoleGivenInLastSixMonths = additionalInfo.getAlbandazoleGivenInLastSixMonths();
            this.adolescentScreeningDate = additionalInfo.getAdolescentScreeningDate();
            this.height = additionalInfo.getHeight();
            this.isPeriodStarted = additionalInfo.getPeriodStarted();
            this.schoolActualId = additionalInfo.getSchoolActualId();
            this.npcbStatus = additionalInfo.getNpcbStatus();
            this.nutritionStatus = additionalInfo.getNutritionStatus();
            this.sdScore = additionalInfo.getSdScore();
            this.indexCase = additionalInfo.getIndexCase();
            this.rdtStatus = additionalInfo.getRdtStatus();
            this.bcgSurveyStatus = additionalInfo.getBcgSurveyStatus();
            this.bcgEligible = additionalInfo.getBcgEligible();
            this.bcgWilling = additionalInfo.getBcgWilling();
            this.bcgEligibleFilled = additionalInfo.getBcgEligibleFilled();
        }

        if (memberBean.getLmpDate() != null) {
            this.lmpDate = memberBean.getLmpDate().getTime();
        }

        if (memberBean.getDob() != null) {
            this.dob = memberBean.getDob().getTime();
        }

        if (memberBean.getCreatedOn() != null) {
            this.createdOn = memberBean.getCreatedOn().getTime();
        }

        if (memberBean.getUpdatedOn() != null) {
            this.updatedOn = memberBean.getUpdatedOn().getTime();
        }

        if (memberBean.getEdd() != null) {
            this.edd = memberBean.getEdd().getTime();
        }

        if (memberBean.getFpInsertOperateDate() != null) {
            this.fpInsertOperateDate = memberBean.getFpInsertOperateDate().getTime();
        }

        if (memberBean.getIucdRemovalDate() != null) {
            this.iucdRemovalDate = memberBean.getIucdRemovalDate().getTime();
        }

        if (memberBean.getCurPregRegDate() != null) {
            this.curPregRegDate = memberBean.getCurPregRegDate().getTime();
        }

        if (memberBean.getLastDeliveryDate() != null) {
            this.lastDeliveryDate = memberBean.getLastDeliveryDate().getTime();
        }

        if (memberBean.getNpcbScreeningDate() != null) {
            this.npcbScreeningDate = memberBean.getNpcbScreeningDate().getTime();
        }

        if (memberBean.getDateOfWedding() != null) {
            this.dateOfWedding = memberBean.getDateOfWedding().getTime();
        }

        if (memberBean.getCbacDate() != null) {
            this.cbacDate = memberBean.getCbacDate().getTime();
        }

        if (memberBean.getHypDiaMentalServiceDate() != null) {
            this.hypDiaMentalServiceDate = memberBean.getHypDiaMentalServiceDate().getTime();
        }

        if (memberBean.getCancerServiceDate() != null) {
            this.cancerServiceDate = memberBean.getCancerServiceDate().getTime();
        }
    }


    protected MemberDataBean(Parcel in) {
        id = in.readString();
        familyId = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        gender = in.readString();
        maritalStatus = in.readString();
        aadharNumber = in.readString();
        isAadharUpdated = in.readByte() != 0;
        mobileNumber = in.readString();
        alternateNumber = in.readString();
        byte tmpFamilyHeadFlag = in.readByte();
        familyHeadFlag = tmpFamilyHeadFlag == 0 ? null : tmpFamilyHeadFlag == 1;
        if (in.readByte() == 0) {
            dob = null;
        } else {
            dob = in.readLong();
        }
        uniqueHealthId = in.readString();
        ifsc = in.readString();
        accountNumber = in.readString();
        maaVatsalyaCardNumber = in.readString();
        byte tmpIsPregnantFlag = in.readByte();
        isPregnantFlag = tmpIsPregnantFlag == 0 ? null : tmpIsPregnantFlag == 1;
        if (in.readByte() == 0) {
            lmpDate = null;
        } else {
            lmpDate = in.readLong();
        }
        if (in.readByte() == 0) {
            normalCycleDays = null;
        } else {
            normalCycleDays = in.readInt();
        }
        familyPlanningMethod = in.readString();
        if (in.readByte() == 0) {
            fpInsertOperateDate = null;
        } else {
            fpInsertOperateDate = in.readLong();
        }
        type = in.readString();
        state = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        if (in.readByte() == 0) {
            createdOn = null;
        } else {
            createdOn = in.readLong();
        }
        if (in.readByte() == 0) {
            updatedOn = null;
        } else {
            updatedOn = in.readLong();
        }
        grandfatherName = in.readString();
        nameBasedOnAadhar = in.readString();
        educationStatus = in.readString();
        congenitalAnomalyIds = in.readString();
        chronicDiseaseIds = in.readString();
        currentDiseaseIds = in.readString();
        eyeIssueIds = in.readString();
        lastMethodOfContraception = in.readString();
        if (in.readByte() == 0) {
            yearOfWedding = null;
        } else {
            yearOfWedding = in.readInt();
        }
        byte tmpJsyPaymentGiven = in.readByte();
        jsyPaymentGiven = tmpJsyPaymentGiven == 0 ? null : tmpJsyPaymentGiven == 1;
        byte tmpIsEarlyRegistration = in.readByte();
        isEarlyRegistration = tmpIsEarlyRegistration == 0 ? null : tmpIsEarlyRegistration == 1;
        byte tmpJsyBeneficiary = in.readByte();
        jsyBeneficiary = tmpJsyBeneficiary == 0 ? null : tmpJsyBeneficiary == 1;
        byte tmpIayBeneficiary = in.readByte();
        iayBeneficiary = tmpIayBeneficiary == 0 ? null : tmpIayBeneficiary == 1;
        byte tmpKpsyBeneficiary = in.readByte();
        kpsyBeneficiary = tmpKpsyBeneficiary == 0 ? null : tmpKpsyBeneficiary == 1;
        byte tmpChiranjeeviYojnaBeneficiary = in.readByte();
        chiranjeeviYojnaBeneficiary = tmpChiranjeeviYojnaBeneficiary == 0 ? null : tmpChiranjeeviYojnaBeneficiary == 1;
        if (in.readByte() == 0) {
            haemoglobin = null;
        } else {
            haemoglobin = in.readFloat();
        }
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = in.readFloat();
        }
        if (in.readByte() == 0) {
            edd = null;
        } else {
            edd = in.readLong();
        }
        ancVisitDates = in.readString();
        immunisationGiven = in.readString();
        bloodGroup = in.readString();
        placeOfBirth = in.readString();
        if (in.readByte() == 0) {
            birthWeight = null;
        } else {
            birthWeight = in.readFloat();
        }
        if (in.readByte() == 0) {
            motherId = null;
        } else {
            motherId = in.readLong();
        }
        byte tmpComplementaryFeedingStarted = in.readByte();
        complementaryFeedingStarted = tmpComplementaryFeedingStarted == 0 ? null : tmpComplementaryFeedingStarted == 1;
        byte tmpIsHighRiskCase = in.readByte();
        isHighRiskCase = tmpIsHighRiskCase == 0 ? null : tmpIsHighRiskCase == 1;
        if (in.readByte() == 0) {
            curPregRegDetId = null;
        } else {
            curPregRegDetId = in.readLong();
        }
        if (in.readByte() == 0) {
            curPregRegDate = null;
        } else {
            curPregRegDate = in.readLong();
        }
        byte tmpMenopauseArrived = in.readByte();
        menopauseArrived = tmpMenopauseArrived == 0 ? null : tmpMenopauseArrived == 1;
        syncStatus = in.readString();
        byte tmpIsIucdRemoved = in.readByte();
        isIucdRemoved = tmpIsIucdRemoved == 0 ? null : tmpIsIucdRemoved == 1;
        if (in.readByte() == 0) {
            iucdRemovalDate = null;
        } else {
            iucdRemovalDate = in.readLong();
        }
        if (in.readByte() == 0) {
            lastDeliveryDate = null;
        } else {
            lastDeliveryDate = in.readLong();
        }
        byte tmpHysterectomyDone = in.readByte();
        hysterectomyDone = tmpHysterectomyDone == 0 ? null : tmpHysterectomyDone == 1;
        childNrcCmtcStatus = in.readString();
        lastDeliveryOutcome = in.readString();
        previousPregnancyComplication = in.readString();
        additionalInfo = in.readString();
        if (in.readByte() == 0) {
            npcbScreeningDate = null;
        } else {
            npcbScreeningDate = in.readLong();
        }
        byte tmpFhsrPhoneVerified = in.readByte();
        fhsrPhoneVerified = tmpFhsrPhoneVerified == 0 ? null : tmpFhsrPhoneVerified == 1;
        int tmpCurrentGravida = in.readInt();
        currentGravida = tmpCurrentGravida != Integer.MAX_VALUE ? (short) tmpCurrentGravida : null;
        int tmpCurrentPara = in.readInt();
        currentPara = tmpCurrentPara != Integer.MAX_VALUE ? (short) tmpCurrentPara : null;
        if (in.readByte() == 0) {
            dateOfWedding = null;
        } else {
            dateOfWedding = in.readLong();
        }
        if (in.readByte() == 0) {
            husbandId = null;
        } else {
            husbandId = in.readLong();
        }
        npcbStatus = in.readString();
        byte tmpIsMobileNumberVerified = in.readByte();
        isMobileNumberVerified = tmpIsMobileNumberVerified == 0 ? null : tmpIsMobileNumberVerified == 1;
        relationWithHof = in.readString();
        if (in.readByte() == 0) {
            cbacDate = null;
        } else {
            cbacDate = in.readLong();
        }
        hypYear = in.readString();
        oralYear = in.readString();
        diabetesYear = in.readString();
        breastYear = in.readString();
        cervicalYear = in.readString();
        mentalHealthYear = in.readString();
        healthYear = in.readString();
        healthId = in.readString();
        healthIdNumber = in.readString();
        byte tmpVulnerableFlag = in.readByte();
        vulnerableFlag = tmpVulnerableFlag == 0 ? null : tmpVulnerableFlag == 1;
        byte tmpHealthInsurance = in.readByte();
        healthInsurance = tmpHealthInsurance == 0 ? null : tmpHealthInsurance == 1;
        schemeDetail = in.readString();
        byte tmpPersonalHistoryDone = in.readByte();
        personalHistoryDone = tmpPersonalHistoryDone == 0 ? null : tmpPersonalHistoryDone == 1;
        byte tmpConfirmedDiabetes = in.readByte();
        confirmedDiabetes = tmpConfirmedDiabetes == 0 ? null : tmpConfirmedDiabetes == 1;
        byte tmpSuspectedForDiabetes = in.readByte();
        suspectedForDiabetes = tmpSuspectedForDiabetes == 0 ? null : tmpSuspectedForDiabetes == 1;
        if (in.readByte() == 0) {
            cbacScore = null;
        } else {
            cbacScore = in.readInt();
        }
        byte tmpSufferingDiabetes = in.readByte();
        sufferingDiabetes = tmpSufferingDiabetes == 0 ? null : tmpSufferingDiabetes == 1;
        byte tmpSufferingHypertension = in.readByte();
        sufferingHypertension = tmpSufferingHypertension == 0 ? null : tmpSufferingHypertension == 1;
        byte tmpSufferingMentalHealth = in.readByte();
        sufferingMentalHealth = tmpSufferingMentalHealth == 0 ? null : tmpSufferingMentalHealth == 1;
        byte tmpEveningAvailability = in.readByte();
        eveningAvailability = tmpEveningAvailability == 0 ? null : tmpEveningAvailability == 1;
        pmjayAvailability = in.readString();
        alcoholAddiction = in.readString();
        smokingAddiction = in.readString();
        tobaccoAddiction = in.readString();
        byte tmpMoConfirmedDiabetes = in.readByte();
        moConfirmedDiabetes = tmpMoConfirmedDiabetes == 0 ? null : tmpMoConfirmedDiabetes == 1;
        byte tmpMoConfirmedHypertension = in.readByte();
        moConfirmedHypertension = tmpMoConfirmedHypertension == 0 ? null : tmpMoConfirmedHypertension == 1;
        byte tmpGeneralScreeningDone = in.readByte();
        generalScreeningDone = tmpGeneralScreeningDone == 0 ? null : tmpGeneralScreeningDone == 1;
        byte tmpEcgTestDone = in.readByte();
        ecgTestDone = tmpEcgTestDone == 0 ? null : tmpEcgTestDone == 1;
        byte tmpRetinopathyTestDone = in.readByte();
        retinopathyTestDone = tmpRetinopathyTestDone == 0 ? null : tmpRetinopathyTestDone == 1;
        byte tmpUrineTestDone = in.readByte();
        urineTestDone = tmpUrineTestDone == 0 ? null : tmpUrineTestDone == 1;
        occupation = in.readString();
        physicalDisability = in.readString();
        otherDisability = in.readString();
        cataractSurgery = in.readString();
        otherChronic = in.readString();
        otherEyeIssue = in.readString();
        sickleCellStatus = in.readString();
        byte tmpIsChildGoingSchool = in.readByte();
        isChildGoingSchool = tmpIsChildGoingSchool == 0 ? null : tmpIsChildGoingSchool == 1;
        currentStudyingStandard = in.readString();
        aadharStatus = in.readString();
        pensionScheme = in.readString();
        byte tmpUnderTreatmentChronic = in.readByte();
        underTreatmentChronic = tmpUnderTreatmentChronic == 0 ? null : tmpUnderTreatmentChronic == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(familyId);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(gender);
        dest.writeString(maritalStatus);
        dest.writeString(aadharNumber);
        dest.writeByte((byte) (isAadharUpdated ? 1 : 0));
        dest.writeString(mobileNumber);
        dest.writeString(alternateNumber);
        dest.writeByte((byte) (familyHeadFlag == null ? 0 : familyHeadFlag ? 1 : 2));
        if (dob == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(dob);
        }
        dest.writeString(uniqueHealthId);
        dest.writeString(ifsc);
        dest.writeString(accountNumber);
        dest.writeString(maaVatsalyaCardNumber);
        dest.writeByte((byte) (isPregnantFlag == null ? 0 : isPregnantFlag ? 1 : 2));
        if (lmpDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lmpDate);
        }
        if (normalCycleDays == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(normalCycleDays);
        }
        dest.writeString(familyPlanningMethod);
        if (fpInsertOperateDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(fpInsertOperateDate);
        }
        dest.writeString(type);
        dest.writeString(state);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        if (createdOn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(createdOn);
        }
        if (updatedOn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(updatedOn);
        }
        dest.writeString(grandfatherName);
        dest.writeString(nameBasedOnAadhar);
        dest.writeString(educationStatus);
        dest.writeString(congenitalAnomalyIds);
        dest.writeString(chronicDiseaseIds);
        dest.writeString(currentDiseaseIds);
        dest.writeString(eyeIssueIds);
        dest.writeString(lastMethodOfContraception);
        if (yearOfWedding == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(yearOfWedding);
        }
        dest.writeByte((byte) (jsyPaymentGiven == null ? 0 : jsyPaymentGiven ? 1 : 2));
        dest.writeByte((byte) (isEarlyRegistration == null ? 0 : isEarlyRegistration ? 1 : 2));
        dest.writeByte((byte) (jsyBeneficiary == null ? 0 : jsyBeneficiary ? 1 : 2));
        dest.writeByte((byte) (iayBeneficiary == null ? 0 : iayBeneficiary ? 1 : 2));
        dest.writeByte((byte) (kpsyBeneficiary == null ? 0 : kpsyBeneficiary ? 1 : 2));
        dest.writeByte((byte) (chiranjeeviYojnaBeneficiary == null ? 0 : chiranjeeviYojnaBeneficiary ? 1 : 2));
        if (haemoglobin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(haemoglobin);
        }
        if (weight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(weight);
        }
        if (edd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(edd);
        }
        dest.writeString(ancVisitDates);
        dest.writeString(immunisationGiven);
        dest.writeString(bloodGroup);
        dest.writeString(placeOfBirth);
        if (birthWeight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(birthWeight);
        }
        if (motherId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(motherId);
        }
        dest.writeByte((byte) (complementaryFeedingStarted == null ? 0 : complementaryFeedingStarted ? 1 : 2));
        dest.writeByte((byte) (isHighRiskCase == null ? 0 : isHighRiskCase ? 1 : 2));
        if (curPregRegDetId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(curPregRegDetId);
        }
        if (curPregRegDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(curPregRegDate);
        }
        dest.writeByte((byte) (menopauseArrived == null ? 0 : menopauseArrived ? 1 : 2));
        dest.writeString(syncStatus);
        dest.writeByte((byte) (isIucdRemoved == null ? 0 : isIucdRemoved ? 1 : 2));
        if (iucdRemovalDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(iucdRemovalDate);
        }
        if (lastDeliveryDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastDeliveryDate);
        }
        dest.writeByte((byte) (hysterectomyDone == null ? 0 : hysterectomyDone ? 1 : 2));
        dest.writeString(childNrcCmtcStatus);
        dest.writeString(lastDeliveryOutcome);
        dest.writeString(previousPregnancyComplication);
        dest.writeString(additionalInfo);
        if (npcbScreeningDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(npcbScreeningDate);
        }
        dest.writeByte((byte) (fhsrPhoneVerified == null ? 0 : fhsrPhoneVerified ? 1 : 2));
        dest.writeInt(currentGravida != null ? (int) currentGravida : Integer.MAX_VALUE);
        dest.writeInt(currentPara != null ? (int) currentPara : Integer.MAX_VALUE);
        if (dateOfWedding == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(dateOfWedding);
        }
        if (husbandId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(husbandId);
        }
        dest.writeString(npcbStatus);
        dest.writeByte((byte) (isMobileNumberVerified == null ? 0 : isMobileNumberVerified ? 1 : 2));
        dest.writeString(relationWithHof);
        if (cbacDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(cbacDate);
        }
        dest.writeString(hypYear);
        dest.writeString(oralYear);
        dest.writeString(diabetesYear);
        dest.writeString(breastYear);
        dest.writeString(cervicalYear);
        dest.writeString(mentalHealthYear);
        dest.writeString(healthYear);
        dest.writeString(healthId);
        dest.writeString(healthIdNumber);
        dest.writeByte((byte) (vulnerableFlag == null ? 0 : vulnerableFlag ? 1 : 2));
        dest.writeByte((byte) (healthInsurance == null ? 0 : healthInsurance ? 1 : 2));
        dest.writeString(schemeDetail);
        dest.writeByte((byte) (personalHistoryDone == null ? 0 : personalHistoryDone ? 1 : 2));
        dest.writeByte((byte) (confirmedDiabetes == null ? 0 : confirmedDiabetes ? 1 : 2));
        dest.writeByte((byte) (suspectedForDiabetes == null ? 0 : suspectedForDiabetes ? 1 : 2));
        if (cbacScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cbacScore);
        }
        dest.writeByte((byte) (sufferingDiabetes == null ? 0 : sufferingDiabetes ? 1 : 2));
        dest.writeByte((byte) (sufferingHypertension == null ? 0 : sufferingHypertension ? 1 : 2));
        dest.writeByte((byte) (sufferingMentalHealth == null ? 0 : sufferingMentalHealth ? 1 : 2));
        dest.writeByte((byte) (eveningAvailability == null ? 0 : eveningAvailability ? 1 : 2));
        dest.writeString(pmjayAvailability);
        dest.writeString(alcoholAddiction);
        dest.writeString(smokingAddiction);
        dest.writeString(tobaccoAddiction);
        dest.writeByte((byte) (moConfirmedDiabetes == null ? 0 : moConfirmedDiabetes ? 1 : 2));
        dest.writeByte((byte) (moConfirmedHypertension == null ? 0 : moConfirmedHypertension ? 1 : 2));
        dest.writeByte((byte) (generalScreeningDone == null ? 0 : generalScreeningDone ? 1 : 2));
        dest.writeByte((byte) (ecgTestDone == null ? 0 : ecgTestDone ? 1 : 2));
        dest.writeByte((byte) (retinopathyTestDone == null ? 0 : retinopathyTestDone ? 1 : 2));
        dest.writeByte((byte) (urineTestDone == null ? 0 : urineTestDone ? 1 : 2));
        dest.writeString(occupation);
        dest.writeString(physicalDisability);
        dest.writeString(otherDisability);
        dest.writeString(cataractSurgery);
        dest.writeString(otherChronic);
        dest.writeString(otherEyeIssue);
        dest.writeString(sickleCellStatus);
        dest.writeByte((byte) (isChildGoingSchool == null ? 0 : isChildGoingSchool ? 1 : 2));
        dest.writeString(currentStudyingStandard);
        dest.writeString(aadharStatus);
        dest.writeString(pensionScheme);
        dest.writeByte((byte) (underTreatmentChronic == null ? 0 : underTreatmentChronic ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemberDataBean> CREATOR = new Creator<MemberDataBean>() {
        @Override
        public MemberDataBean createFromParcel(Parcel in) {
            return new MemberDataBean(in);
        }

        @Override
        public MemberDataBean[] newArray(int size) {
            return new MemberDataBean[size];
        }
    };

    public Long getDateOfWedding() {
        return dateOfWedding;
    }

    public void setDateOfWedding(Long dateOfWedding) {
        this.dateOfWedding = dateOfWedding;
    }

    public Short getCurrentGravida() {
        return currentGravida;
    }

    public void setCurrentGravida(Short currentGravida) {
        this.currentGravida = currentGravida;
    }

    public Short getCurrentPara() {
        return currentPara;
    }

    public void setCurrentPara(Short currentPara) {
        this.currentPara = currentPara;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public boolean isAadharUpdated() {
        return isAadharUpdated;
    }

    public void setAadharUpdated(boolean aadharUpdated) {
        isAadharUpdated = aadharUpdated;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getFamilyHeadFlag() {
        return familyHeadFlag;
    }

    public void setFamilyHeadFlag(Boolean familyHeadFlag) {
        this.familyHeadFlag = familyHeadFlag;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMaaVatsalyaCardNumber() {
        return maaVatsalyaCardNumber;
    }

    public void setMaaVatsalyaCardNumber(String maaVatsalyaCardNumber) {
        this.maaVatsalyaCardNumber = maaVatsalyaCardNumber;
    }

    public Boolean getIsPregnantFlag() {
        return isPregnantFlag;
    }

    public void setIsPregnantFlag(Boolean isPregnantFlag) {
        this.isPregnantFlag = isPregnantFlag;
    }

    public Long getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Long lmpDate) {
        this.lmpDate = lmpDate;
    }

    public Integer getNormalCycleDays() {
        return normalCycleDays;
    }

    public void setNormalCycleDays(Integer normalCycleDays) {
        this.normalCycleDays = normalCycleDays;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Long getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Long fpInsertOperateDate) {
        this.fpInsertOperateDate = fpInsertOperateDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getNameBasedOnAadhar() {
        return nameBasedOnAadhar;
    }

    public void setNameBasedOnAadhar(String nameBasedOnAadhar) {
        this.nameBasedOnAadhar = nameBasedOnAadhar;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public String getCongenitalAnomalyIds() {
        return congenitalAnomalyIds;
    }

    public void setCongenitalAnomalyIds(String congenitalAnomalyIds) {
        this.congenitalAnomalyIds = congenitalAnomalyIds;
    }

    public String getChronicDiseaseIds() {
        return chronicDiseaseIds;
    }

    public void setChronicDiseaseIds(String chronicDiseaseIds) {
        this.chronicDiseaseIds = chronicDiseaseIds;
    }

    public String getCurrentDiseaseIds() {
        return currentDiseaseIds;
    }

    public void setCurrentDiseaseIds(String currentDiseaseIds) {
        this.currentDiseaseIds = currentDiseaseIds;
    }

    public String getEyeIssueIds() {
        return eyeIssueIds;
    }

    public void setEyeIssueIds(String eyeIssueIds) {
        this.eyeIssueIds = eyeIssueIds;
    }

    public String getLastMethodOfContraception() {
        return lastMethodOfContraception;
    }

    public void setLastMethodOfContraception(String lastMethodOfContraception) {
        this.lastMethodOfContraception = lastMethodOfContraception;
    }

    public Integer getYearOfWedding() {
        return yearOfWedding;
    }

    public void setYearOfWedding(Integer yearOfWedding) {
        this.yearOfWedding = yearOfWedding;
    }

    public Boolean getJsyPaymentGiven() {
        return jsyPaymentGiven;
    }

    public void setJsyPaymentGiven(Boolean jsyPaymentGiven) {
        this.jsyPaymentGiven = jsyPaymentGiven;
    }

    public Boolean getEarlyRegistration() {
        return isEarlyRegistration;
    }

    public void setEarlyRegistration(Boolean earlyRegistration) {
        isEarlyRegistration = earlyRegistration;
    }

    public Boolean getJsyBeneficiary() {
        return jsyBeneficiary;
    }

    public void setJsyBeneficiary(Boolean jsyBeneficiary) {
        this.jsyBeneficiary = jsyBeneficiary;
    }

    public Boolean getIayBeneficiary() {
        return iayBeneficiary;
    }

    public void setIayBeneficiary(Boolean iayBeneficiary) {
        this.iayBeneficiary = iayBeneficiary;
    }

    public Boolean getKpsyBeneficiary() {
        return kpsyBeneficiary;
    }

    public void setKpsyBeneficiary(Boolean kpsyBeneficiary) {
        this.kpsyBeneficiary = kpsyBeneficiary;
    }

    public Boolean getChiranjeeviYojnaBeneficiary() {
        return chiranjeeviYojnaBeneficiary;
    }

    public void setChiranjeeviYojnaBeneficiary(Boolean chiranjeeviYojnaBeneficiary) {
        this.chiranjeeviYojnaBeneficiary = chiranjeeviYojnaBeneficiary;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Long getEdd() {
        return edd;
    }

    public void setEdd(Long edd) {
        this.edd = edd;
    }

    public String getAncVisitDates() {
        return ancVisitDates;
    }

    public void setAncVisitDates(String ancVisitDates) {
        this.ancVisitDates = ancVisitDates;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Float getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Float birthWeight) {
        this.birthWeight = birthWeight;
    }

    public Long getMotherId() {
        return motherId;
    }

    public void setMotherId(Long motherId) {
        this.motherId = motherId;
    }

    public Boolean getComplementaryFeedingStarted() {
        return complementaryFeedingStarted;
    }

    public void setComplementaryFeedingStarted(Boolean complementaryFeedingStarted) {
        this.complementaryFeedingStarted = complementaryFeedingStarted;
    }

    public Boolean getHighRiskCase() {
        return isHighRiskCase;
    }

    public void setHighRiskCase(Boolean highRiskCase) {
        isHighRiskCase = highRiskCase;
    }

    public Long getCurPregRegDetId() {
        return curPregRegDetId;
    }

    public void setCurPregRegDetId(Long curPregRegDetId) {
        this.curPregRegDetId = curPregRegDetId;
    }

    public Long getCurPregRegDate() {
        return curPregRegDate;
    }

    public void setCurPregRegDate(Long curPregRegDate) {
        this.curPregRegDate = curPregRegDate;
    }

    public Boolean getMenopauseArrived() {
        return menopauseArrived;
    }

    public void setMenopauseArrived(Boolean menopauseArrived) {
        this.menopauseArrived = menopauseArrived;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Boolean getIucdRemoved() {
        return isIucdRemoved;
    }

    public void setIucdRemoved(Boolean iucdRemoved) {
        isIucdRemoved = iucdRemoved;
    }

    public Long getIucdRemovalDate() {
        return iucdRemovalDate;
    }

    public void setIucdRemovalDate(Long iucdRemovalDate) {
        this.iucdRemovalDate = iucdRemovalDate;
    }

    public Long getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(Long lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public Boolean getHysterectomyDone() {
        return hysterectomyDone;
    }

    public void setHysterectomyDone(Boolean hysterectomyDone) {
        this.hysterectomyDone = hysterectomyDone;
    }

    public String getChildNrcCmtcStatus() {
        return childNrcCmtcStatus;
    }

    public void setChildNrcCmtcStatus(String childNrcCmtcStatus) {
        this.childNrcCmtcStatus = childNrcCmtcStatus;
    }

    public String getLastDeliveryOutcome() {
        return lastDeliveryOutcome;
    }

    public void setLastDeliveryOutcome(String lastDeliveryOutcome) {
        this.lastDeliveryOutcome = lastDeliveryOutcome;
    }

    public String getPreviousPregnancyComplication() {
        return previousPregnancyComplication;
    }

    public void setPreviousPregnancyComplication(String previousPregnancyComplication) {
        this.previousPregnancyComplication = previousPregnancyComplication;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        if (additionalInfo != null) {
            this.npcbStatus = new Gson().fromJson(additionalInfo, MemberAdditionalInfoDataBean.class).getNpcbStatus();
        }
    }

    public Long getNpcbScreeningDate() {
        return npcbScreeningDate;
    }

    public void setNpcbScreeningDate(Long npcbScreeningDate) {
        this.npcbScreeningDate = npcbScreeningDate;
    }

    public Boolean getFhsrPhoneVerified() {
        return fhsrPhoneVerified;
    }

    public void setFhsrPhoneVerified(Boolean fhsrPhoneVerified) {
        this.fhsrPhoneVerified = fhsrPhoneVerified;
    }

    public Long getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(Long husbandId) {
        this.husbandId = husbandId;
    }

    public String getNpcbStatus() {
        return npcbStatus;
    }

    public void setNpcbStatus(String npcbStatus) {
        this.npcbStatus = npcbStatus;
    }

    public Boolean getMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setMobileNumberVerified(Boolean mobileNumberVerified) {
        isMobileNumberVerified = mobileNumberVerified;
    }

    public String getRelationWithHof() {
        return relationWithHof;
    }

    public void setRelationWithHof(String relationWithHof) {
        this.relationWithHof = relationWithHof;
    }

    public Long getCbacDate() {
        return cbacDate;
    }

    public void setCbacDate(Long cbacDate) {
        this.cbacDate = cbacDate;
    }

    public String getHypYear() {
        return hypYear;
    }

    public void setHypYear(String hypYear) {
        this.hypYear = hypYear;
    }

    public String getOralYear() {
        return oralYear;
    }

    public void setOralYear(String oralYear) {
        this.oralYear = oralYear;
    }

    public String getDiabetesYear() {
        return diabetesYear;
    }

    public void setDiabetesYear(String diabetesYear) {
        this.diabetesYear = diabetesYear;
    }

    public String getBreastYear() {
        return breastYear;
    }

    public void setBreastYear(String breastYear) {
        this.breastYear = breastYear;
    }

    public String getCervicalYear() {
        return cervicalYear;
    }

    public void setCervicalYear(String cervicalYear) {
        this.cervicalYear = cervicalYear;
    }

    public String getMentalHealthYear() {
        return mentalHealthYear;
    }

    public void setMentalHealthYear(String mentalHealthYear) {
        this.mentalHealthYear = mentalHealthYear;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getHealthIdNumber() {
        return healthIdNumber;
    }

    public void setHealthIdNumber(String healthIdNumber) {
        this.healthIdNumber = healthIdNumber;
    }

    public Boolean getVulnerableFlag() {
        return vulnerableFlag;
    }

    public void setVulnerableFlag(Boolean vulnerableFlag) {
        this.vulnerableFlag = vulnerableFlag;
    }

    public Boolean getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(Boolean healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public String getSchemeDetail() {
        return schemeDetail;
    }

    public void setSchemeDetail(String schemeDetail) {
        this.schemeDetail = schemeDetail;
    }

    public String getHealthYear() {
        return healthYear;
    }

    public void setHealthYear(String healthYear) {
        this.healthYear = healthYear;
    }

    public Boolean getPersonalHistoryDone() {
        return personalHistoryDone;
    }

    public void setPersonalHistoryDone(Boolean personalHistoryDone) {
        this.personalHistoryDone = personalHistoryDone;
    }

    public Boolean getConfirmedDiabetes() {
        return confirmedDiabetes;
    }

    public void setConfirmedDiabetes(Boolean confirmedDiabetes) {
        this.confirmedDiabetes = confirmedDiabetes;
    }

    public Boolean getSuspectedForDiabetes() {
        return suspectedForDiabetes;
    }

    public void setSuspectedForDiabetes(Boolean suspectedForDiabetes) {
        this.suspectedForDiabetes = suspectedForDiabetes;
    }

    public Integer getCbacScore() {
        return cbacScore;
    }

    public void setCbacScore(Integer cbacScore) {
        this.cbacScore = cbacScore;
    }

    public Boolean getSufferingDiabetes() {
        return sufferingDiabetes;
    }

    public void setSufferingDiabetes(Boolean sufferingDiabetes) {
        this.sufferingDiabetes = sufferingDiabetes;
    }

    public Boolean getSufferingHypertension() {
        return sufferingHypertension;
    }

    public void setSufferingHypertension(Boolean sufferingHypertension) {
        this.sufferingHypertension = sufferingHypertension;
    }

    public Boolean getSufferingMentalHealth() {
        return sufferingMentalHealth;
    }

    public void setSufferingMentalHealth(Boolean sufferingMentalHealth) {
        this.sufferingMentalHealth = sufferingMentalHealth;
    }

    public Boolean getEveningAvailability() {
        return eveningAvailability;
    }

    public void setEveningAvailability(Boolean eveningAvailability) {
        this.eveningAvailability = eveningAvailability;
    }

    public String getPmjayAvailability() {
        return pmjayAvailability;
    }

    public void setPmjayAvailability(String pmjayAvailability) {
        this.pmjayAvailability = pmjayAvailability;
    }

    public String getAlcoholAddiction() {
        return alcoholAddiction;
    }

    public void setAlcoholAddiction(String alcoholAddiction) {
        this.alcoholAddiction = alcoholAddiction;
    }

    public String getSmokingAddiction() {
        return smokingAddiction;
    }

    public void setSmokingAddiction(String smokingAddiction) {
        this.smokingAddiction = smokingAddiction;
    }

    public String getTobaccoAddiction() {
        return tobaccoAddiction;
    }

    public void setTobaccoAddiction(String tobaccoAddiction) {
        this.tobaccoAddiction = tobaccoAddiction;
    }

    public Boolean getMoConfirmedDiabetes() {
        return moConfirmedDiabetes;
    }

    public void setMoConfirmedDiabetes(Boolean moConfirmedDiabetes) {
        this.moConfirmedDiabetes = moConfirmedDiabetes;
    }

    public Boolean getMoConfirmedHypertension() {
        return moConfirmedHypertension;
    }

    public void setMoConfirmedHypertension(Boolean moConfirmedHypertension) {
        this.moConfirmedHypertension = moConfirmedHypertension;
    }

    public Boolean getGeneralScreeningDone() {
        return generalScreeningDone;
    }

    public void setGeneralScreeningDone(Boolean generalScreeningDone) {
        this.generalScreeningDone = generalScreeningDone;
    }

    public Boolean getEcgTestDone() {
        return ecgTestDone;
    }

    public void setEcgTestDone(Boolean ecgTestDone) {
        this.ecgTestDone = ecgTestDone;
    }

    public Boolean getRetinopathyTestDone() {
        return retinopathyTestDone;
    }

    public void setRetinopathyTestDone(Boolean retinopathyTestDone) {
        this.retinopathyTestDone = retinopathyTestDone;
    }

    public Boolean getUrineTestDone() {
        return urineTestDone;
    }

    public void setUrineTestDone(Boolean urineTestDone) {
        this.urineTestDone = urineTestDone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhysicalDisability() {
        return physicalDisability;
    }

    public void setPhysicalDisability(String physicalDisability) {
        this.physicalDisability = physicalDisability;
    }

    public String getOtherDisability() {
        return otherDisability;
    }

    public void setOtherDisability(String otherDisability) {
        this.otherDisability = otherDisability;
    }

    public String getCataractSurgery() {
        return cataractSurgery;
    }

    public void setCataractSurgery(String cataractSurgery) {
        this.cataractSurgery = cataractSurgery;
    }

    public String getOtherChronic() {
        return otherChronic;
    }

    public void setOtherChronic(String otherChronic) {
        this.otherChronic = otherChronic;
    }

    public String getOtherEyeIssue() {
        return otherEyeIssue;
    }

    public void setOtherEyeIssue(String otherEyeIssue) {
        this.otherEyeIssue = otherEyeIssue;
    }

    public String getSickleCellStatus() {
        return sickleCellStatus;
    }

    public void setSickleCellStatus(String sickleCellStatus) {
        this.sickleCellStatus = sickleCellStatus;
    }

    public Boolean getIsChildGoingSchool() {
        return isChildGoingSchool;
    }

    public void setIsChildGoingSchool(Boolean isChildGoingSchool) {
        this.isChildGoingSchool = isChildGoingSchool;
    }

    public String getCurrentStudyingStandard() {
        return currentStudyingStandard;
    }

    public void setCurrentStudyingStandard(String currentStudyingStandard) {
        this.currentStudyingStandard = currentStudyingStandard;
    }

    public String getAadharStatus() {
        return aadharStatus;
    }

    public void setAadharStatus(String aadharStatus) {
        this.aadharStatus = aadharStatus;
    }

    public String getPensionScheme() {
        return pensionScheme;
    }

    public void setPensionScheme(String pensionScheme) {
        this.pensionScheme = pensionScheme;
    }

    public Boolean getUnderTreatmentChronic() {
        return underTreatmentChronic;
    }

    public void setUnderTreatmentChronic(Boolean underTreatmentChronic) {
        this.underTreatmentChronic = underTreatmentChronic;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public Boolean getPregnantFlag() {
        return isPregnantFlag;
    }

    public void setPregnantFlag(Boolean pregnantFlag) {
        isPregnantFlag = pregnantFlag;
    }

    public Boolean getChildGoingSchool() {
        return isChildGoingSchool;
    }

    public void setChildGoingSchool(Boolean childGoingSchool) {
        isChildGoingSchool = childGoingSchool;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getCounsellingDone() {
        return counsellingDone;
    }

    public void setCounsellingDone(String counsellingDone) {
        this.counsellingDone = counsellingDone;
    }

    public Boolean getHaemoglobinMeasured() {
        return isHaemoglobinMeasured;
    }

    public void setHaemoglobinMeasured(Boolean haemoglobinMeasured) {
        isHaemoglobinMeasured = haemoglobinMeasured;
    }

    public Integer getIfaTabTakenLastMonth() {
        return ifaTabTakenLastMonth;
    }

    public void setIfaTabTakenLastMonth(Integer ifaTabTakenLastMonth) {
        this.ifaTabTakenLastMonth = ifaTabTakenLastMonth;
    }

    public Integer getIfaTabTakenNow() {
        return ifaTabTakenNow;
    }

    public void setIfaTabTakenNow(Integer ifaTabTakenNow) {
        this.ifaTabTakenNow = ifaTabTakenNow;
    }

    public String getAbsorbentMaterialUsed() {
        return absorbentMaterialUsed;
    }

    public void setAbsorbentMaterialUsed(String absorbentMaterialUsed) {
        this.absorbentMaterialUsed = absorbentMaterialUsed;
    }

    public Boolean getSanitaryPadGiven() {
        return isSanitaryPadGiven;
    }

    public void setSanitaryPadGiven(Boolean sanitaryPadGiven) {
        isSanitaryPadGiven = sanitaryPadGiven;
    }

    public Integer getNumberOfSanitaryPadsGiven() {
        return numberOfSanitaryPadsGiven;
    }

    public void setNumberOfSanitaryPadsGiven(Integer numberOfSanitaryPadsGiven) {
        this.numberOfSanitaryPadsGiven = numberOfSanitaryPadsGiven;
    }

    public Boolean getHavingMenstrualProblem() {
        return isHavingMenstrualProblem;
    }

    public void setHavingMenstrualProblem(Boolean havingMenstrualProblem) {
        isHavingMenstrualProblem = havingMenstrualProblem;
    }

    public String getIssueWithMenstruation() {
        return issueWithMenstruation;
    }

    public void setIssueWithMenstruation(String issueWithMenstruation) {
        this.issueWithMenstruation = issueWithMenstruation;
    }

    public Boolean getTDInjectionGiven() {
        return isTDInjectionGiven;
    }

    public void setTDInjectionGiven(Boolean TDInjectionGiven) {
        isTDInjectionGiven = TDInjectionGiven;
    }

    public Long getTdInjectionDate() {
        return tdInjectionDate;
    }

    public void setTdInjectionDate(Long tdInjectionDate) {
        this.tdInjectionDate = tdInjectionDate;
    }

    public Boolean getAlbandazoleGivenInLastSixMonths() {
        return isAlbandazoleGivenInLastSixMonths;
    }

    public void setAlbandazoleGivenInLastSixMonths(Boolean albandazoleGivenInLastSixMonths) {
        isAlbandazoleGivenInLastSixMonths = albandazoleGivenInLastSixMonths;
    }

    public Long getAdolescentScreeningDate() {
        return adolescentScreeningDate;
    }

    public void setAdolescentScreeningDate(Long adolescentScreeningDate) {
        this.adolescentScreeningDate = adolescentScreeningDate;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getPeriodStarted() {
        return isPeriodStarted;
    }

    public void setPeriodStarted(Boolean periodStarted) {
        isPeriodStarted = periodStarted;
    }

    public Long getSchoolActualId() {
        return schoolActualId;
    }

    public void setSchoolActualId(Long schoolActualId) {
        this.schoolActualId = schoolActualId;
    }

    public Long getAnganwadiId() {
        return anganwadiId;
    }

    public void setAnganwadiId(Long anganwadiId) {
        this.anganwadiId = anganwadiId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getConcatWs() {
        return concatWs;
    }

    public void setConcatWs(String concatWs) {
        this.concatWs = concatWs;
    }

    public Long getHmisId() {
        return hmisId;
    }

    public void setHmisId(Long hmisId) {
        this.hmisId = hmisId;
    }

    public Long getHypDiaMentalServiceDate() {
        return hypDiaMentalServiceDate;
    }

    public void setHypDiaMentalServiceDate(Long hypDiaMentalServiceDate) {
        this.hypDiaMentalServiceDate = hypDiaMentalServiceDate;
    }

    public Long getCancerServiceDate() {
        return cancerServiceDate;
    }

    public void setCancerServiceDate(Long cancerServiceDate) {
        this.cancerServiceDate = cancerServiceDate;
    }

    public String getIsHivPositive() {
        return isHivPositive;
    }

    public void setIsHivPositive(String isHivPositive) {
        this.isHivPositive = isHivPositive;
    }

    public String getIsVDRLPositive() {
        return isVDRLPositive;
    }

    public void setIsVDRLPositive(String isVDRLPositive) {
        this.isVDRLPositive = isVDRLPositive;
    }

    public String getNutritionStatus() {
        return nutritionStatus;
    }

    public void setNutritionStatus(String nutritionStatus) {
        this.nutritionStatus = nutritionStatus;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public String getNrcNumber() {
        return nrcNumber;
    }

    public void setNrcNumber(String nrcNumber) {
        this.nrcNumber = nrcNumber;
    }

    public Boolean getTbCured() {
        return isTbCured;
    }

    public void setTbCured(Boolean tbCured) {
        isTbCured = tbCured;
    }

    public Boolean getTbSuspected() {
        return isTbSuspected;
    }

    public void setTbSuspected(Boolean tbSuspected) {
        isTbSuspected = tbSuspected;
    }

    public Boolean getIndexCase() {
        return indexCase;
    }

    public void setIndexCase(Boolean indexCase) {
        this.indexCase = indexCase;
    }

    public String getMemberReligion() {
        return memberReligion;
    }

    public void setMemberReligion(String memberReligion) {
        this.memberReligion = memberReligion;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getChronicDiseaseIdsForTreatment() {
        return chronicDiseaseIdsForTreatment;
    }

    public void setChronicDiseaseIdsForTreatment(String chronicDiseaseIdsForTreatment) {
        this.chronicDiseaseIdsForTreatment = chronicDiseaseIdsForTreatment;
    }

    public Boolean getBcgSurveyStatus() {
        return bcgSurveyStatus;
    }

    public void setBcgSurveyStatus(Boolean bcgSurveyStatus) {
        this.bcgSurveyStatus = bcgSurveyStatus;
    }

    public Boolean getBcgEligible() {
        return bcgEligible;
    }

    public void setBcgEligible(Boolean bcgEligible) {
        this.bcgEligible = bcgEligible;
    }

    public Boolean getBcgWilling() {
        return bcgWilling;
    }

    public void setBcgWilling(Boolean bcgWilling) {
        this.bcgWilling = bcgWilling;
    }

    public Boolean getBcgEligibleFilled() {
        return bcgEligibleFilled;
    }

    public void setBcgEligibleFilled(Boolean bcgEligibleFilled) {
        this.bcgEligibleFilled = bcgEligibleFilled;
    }

    public String getOtherChronicDiseaseTreatment() {
        return otherChronicDiseaseTreatment;
    }

    public void setOtherChronicDiseaseTreatment(String otherChronicDiseaseTreatment) {
        this.otherChronicDiseaseTreatment = otherChronicDiseaseTreatment;
    }

    public String getRdtStatus() {
        return rdtStatus;
    }

    public void setRdtStatus(String rdtStatus) {
        this.rdtStatus = rdtStatus;
    }

    public String getBirthCertNumber() {
        return birthCertNumber;
    }

    public void setBirthCertNumber(String birthCertNumber) {
        this.birthCertNumber = birthCertNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MemberDataBean that = (MemberDataBean) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(Integer.parseInt(id));
    }
}
