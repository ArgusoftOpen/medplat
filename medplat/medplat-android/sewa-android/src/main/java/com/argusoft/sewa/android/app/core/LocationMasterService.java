package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;

import java.util.List;

public interface LocationMasterService {

    List<LocationMasterBean> retrieveLocationMasterBeansByLevelAndParent(Integer level, Long parent);

    List<LocationBean> retrieveLocationBeansByLevelAndParent(Integer level, Long parent);

    List<LocationBean> retrieveSubCenterListForFHSR();

    String retrieveLocationHierarchyByLocationId(Long locationId);

    LocationBean getLocationBeanByActualId(String actualId);

    String getLocationTypeNameByType(String type);

    List<LocationMasterBean> retrieveLocationMastersAssignedToUser();
    List<Integer> getLocationIdsInsideDistrictOfUser();
    List<LocationMasterBean> getLocationWithNoParent();
    LocationMasterBean getLocationMasterBeanByActualId(Integer actualId);

}
