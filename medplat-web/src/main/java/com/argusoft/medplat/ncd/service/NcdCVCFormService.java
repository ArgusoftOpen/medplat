package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.NcdCVCFormDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.NcdCVCForm;

import java.util.Date;

public interface NcdCVCFormService {
    NcdCVCForm saveCVCForm(NcdCVCFormDto ncdCVCFormDto);

    NcdCVCForm retrieveCVCDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type);
}
