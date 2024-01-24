package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.model.NcdAmputationMemberDetail;

import java.util.Date;
import java.util.List;

public interface NcdMoSpecialistReviewService {


    public void saveEcgData(NcdEcgMemberDetailDto ncdEcgMemberDetailDto);

    public void saveStrokeData(NcdStrokeMemberDetailDto dto);

    public void saveAmputationData(NcdAmputationMemberDetailDto dto);

    public void saveRenalData(NcdRenalMemberDetailDto dto);
}
