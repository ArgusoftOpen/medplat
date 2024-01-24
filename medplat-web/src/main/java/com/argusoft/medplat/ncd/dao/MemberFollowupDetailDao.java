package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.MemberFollowupDetail;

import java.util.List;

public interface MemberFollowupDetailDao extends GenericDao<MemberFollowupDetail, Integer> {
    List<MemberFollowupDetail> getFollowUpByReferenceId(Integer referenceId);
}
