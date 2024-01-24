package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.NcdSpecialistMaster;

public interface NcdSpecicalistMasterDao  extends GenericDao<NcdSpecialistMaster, Long> {
    NcdSpecialistMaster retrieveByMemberId(Integer memberId);
}
