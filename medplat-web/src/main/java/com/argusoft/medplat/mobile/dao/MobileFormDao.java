package com.argusoft.medplat.mobile.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.model.SohElementModuleMaster;

import java.util.List;

public interface MobileFormDao extends GenericDao<SohElementModuleMaster, Integer> {

    List<String> getFileNames();

    List<String> getFileNamesByFeature(Integer roleId);
}
