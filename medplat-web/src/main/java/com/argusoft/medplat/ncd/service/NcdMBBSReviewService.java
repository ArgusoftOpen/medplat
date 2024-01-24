package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;

import java.util.List;

public interface NcdMBBSReviewService {

    MbbsMOReviewDetail retrieveMbbsMOReviewDetail(Integer memberId);
    void saveMbbsMOReview(MbbsMOReviewDto mbbsMOReviewDto);
    List<MemberDetailDto> retrieveMembersForMbbsMOReview(Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, String status);

    MbbsMOReviewDto retrieveLastCommentByMBBS(Integer memberId);
}
