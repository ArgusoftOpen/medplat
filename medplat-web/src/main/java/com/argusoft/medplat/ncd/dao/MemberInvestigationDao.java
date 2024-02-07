package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.MemberInvestigationDetail;

import java.util.List;

public interface MemberInvestigationDao extends GenericDao<MemberInvestigationDetail, Integer> {

    List<MemberInvestigationDetail> findByMemberId(Integer memberId);
}
