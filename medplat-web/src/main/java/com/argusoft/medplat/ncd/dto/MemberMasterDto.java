package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.ncd.model.*;
import lombok.Data;

@Data
public class MemberMasterDto {

    private Integer id;
    private String uniqueHealthId;
    private String name;
    private String gender;
    private java.sql.Date dob;
    private Integer familyId;
    private MemberDto basicDetails;
    private String mobileNumber;
    private Integer memberId;
    private MemberHypertensionDetail memberHypertensionDetail;
    private MemberDiabetesDetail memberDiabetesDetail;
    private MemberMentalHealthDetails memberMentalHealthDetails;
    private MemberBreastDetail memberBreastDetail;
    private MemberInitialAssessmentDetail memberInitialAssessmentDetail;
    private Boolean bpl;
    private Boolean vulnerable;
    private MemberAdditionalInfo memberAdditionalInfo;
    private NcdMemberEntity ncdMemberEntity;
}
