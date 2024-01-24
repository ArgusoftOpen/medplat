package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.model.DrugInventoryDetail;
import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;

public interface MbbsMOReviewDao extends GenericDao<MbbsMOReviewDetail, Integer> {
    public MbbsMOReviewDetail retrieveLastRecordByMemberId(Integer memberId);

    MbbsMOReviewDto retrieveLastCommentByMBBS(Integer memberId);
}
