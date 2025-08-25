package com.argusoft.medplat.web.ddb.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import java.util.List;

public interface IndicatorMasterDao extends GenericDao<IndicatorMaster, Integer> {
    List<IndicatorMaster> getAllIndicatorMaster();
}
