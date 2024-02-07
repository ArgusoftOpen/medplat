package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MOReviewFollowupDto;
import com.argusoft.medplat.ncd.model.MOReviewFollowupDetail;

import java.util.Date;

public interface NcdMOReviewFollowupDao extends GenericDao<MOReviewFollowupDetail, Integer> {
    MOReviewFollowupDetail retrieveMoReviewFollowupByMemberAndDate(Integer memberId, Date date);

    MOReviewFollowupDto retrieveLastCommentByMOReviewFollowup(Integer memberId);
}
