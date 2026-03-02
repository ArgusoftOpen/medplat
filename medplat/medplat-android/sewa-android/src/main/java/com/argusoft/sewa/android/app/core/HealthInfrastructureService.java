package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;

import java.util.List;
import java.util.Map;

public interface HealthInfrastructureService {

    Map<Long, String> retrieveDistinctHealthInfraType();

    Map<Long, String> retrieveDistinctHealthInfraTypeForChardham();

    List<HealthInfrastructureBean> retrieveHealthInfraBySearch(String search, Long typeId, String attribute);

    List<HealthInfrastructureBean> retrieveHealthInfraByLocationId(Long locationId, Long typeId, String attribute);

    List<HealthInfrastructureBean> retrieveHealthInfrastructures(Long typeId, String attribute);

    HealthInfrastructureBean retrieveHealthInfrastructureAssignedToUser(Long userId);
    List<HealthInfrastructureBean> retrieveHealthInfraListByLocationList(List<Integer> locationIds);
    UserHealthInfraBean retrieveUserHealthInfraBean();

}
