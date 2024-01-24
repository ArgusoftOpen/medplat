package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MOReviewDetail;

import java.util.Date;
import java.util.List;

public interface MoReviewDao extends GenericDao<MOReviewDetail, Integer> {
    MOReviewDetail retrieveMoReviewByMemberAndDate(Integer memberId, Date screeningDate);

    List<MOReviewDetail> retrieveMOReviewsByMemberId(Integer memberId);

    List<MemberDetailDto> retreiveSearchedMembersMOReview(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag);

    MOReviewDto retrieveLastCommentByMOReview(Integer memberId);
}
