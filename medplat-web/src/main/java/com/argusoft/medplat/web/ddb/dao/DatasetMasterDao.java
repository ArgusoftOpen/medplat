package com.argusoft.medplat.web.ddb.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;
import java.util.List;

public interface DatasetMasterDao extends GenericDao<DatasetMaster, Integer> {
    List<DatasetMaster> getAllDatasetMaster();
}
