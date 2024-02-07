package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.enums.SubType;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;
import com.argusoft.medplat.ncd.model.MemberMentalHealthDetails;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class MemberMentalHealthDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Date screeningDate;
    private String reason;
    private String readings;
    private SubType subType;
    private Integer referredFromHealthInfrastructureId;
    private DoneBy doneBy;
    private Date doneOn;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Integer healthInfraId;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private Status status;
    private Integer talk;
    private Integer ownDailyWork;
    private Integer socialWork;
    private Boolean flag;
    private Integer understanding;
    private MemberMentalHealthDetails.Status todayResult;
    private Boolean isSuffering;
    private Boolean sufferingEarlier;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private Boolean takeMedicine;
    private Boolean htn;
    private Boolean doesSuffering;

}
