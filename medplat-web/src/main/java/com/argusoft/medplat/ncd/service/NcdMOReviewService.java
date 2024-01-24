package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MOReviewDetail;

import java.util.Date;
import java.util.List;

public interface NcdMOReviewService {

    void saveMOReview(MOReviewDto moReviewDto);

    MOReviewDetail retrieveMoReviewByMemberAndDate(Integer memberId, Date screeningDate);

    List<MemberDetailDto> retreiveSearchedMembersMOReview(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag);

    MOReviewDto retrieveLastCommentByMOReview(Integer memberId);
}
