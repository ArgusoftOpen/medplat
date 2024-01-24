package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.MOReviewFollowupDto;
import com.argusoft.medplat.ncd.model.MOReviewFollowupDetail;

import java.util.Date;

public interface NcdMOReviewFollowupService {
    void saveMOReviewFollowup(MOReviewFollowupDto moReviewFollowupDto);

    MOReviewFollowupDetail retrieveMoReviewFollowupByMemberAndDate(Integer memberId, Date date);

    MOReviewFollowupDto retrieveLastCommentByMOReviewFollowup(Integer memberId);
}
