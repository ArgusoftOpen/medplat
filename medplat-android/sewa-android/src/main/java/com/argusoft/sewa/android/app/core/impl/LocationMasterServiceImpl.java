package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.LocationMasterService;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LocationTypeBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.Log;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class LocationMasterServiceImpl implements LocationMasterService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationMasterBean, Integer> locationMasterBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationBean, Integer> locationBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationTypeBean, Integer> locationTypeBeanDao;

    @Override
    public List<LocationMasterBean> retrieveLocationMasterBeansByLevelAndParent(Integer level, Long parent) {
        List<LocationMasterBean> locationMasterBeans = new ArrayList<>();
        if (parent != null) {
            try {
                locationMasterBeans = locationMasterBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.PARENT, parent)
                        .and().eq(FieldNameConstants.LEVEL, level).query();
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }
        return locationMasterBeans;
    }

    @Override
    public List<LocationBean> retrieveLocationBeansByLevelAndParent(Integer level, Long parent) {
        List<LocationBean> locationBeans = new ArrayList<>();
        if (parent != null) {
            try {
                locationBeans = locationBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.PARENT, parent)
                        .and().eq(FieldNameConstants.LEVEL, level).query();
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }
        return locationBeans;
    }

    @Override
    public List<LocationBean> retrieveSubCenterListForFHSR() {
        if (SewaTransformer.loginBean.getVillageCode() == null || SewaTransformer.loginBean.getVillageCode().isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> locationIdsAssignedToUser = new ArrayList<>();
        for (String id : SewaTransformer.loginBean.getVillageCode().split(":")) {
            locationIdsAssignedToUser.add(Integer.valueOf(id));
        }

        List<LocationBean> locations = new ArrayList<>();
        try {
            locations = locationBeanDao.queryBuilder().where().in(FieldNameConstants.PARENT, locationIdsAssignedToUser).query();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return locations;
    }

    @Override
    public String retrieveLocationHierarchyByLocationId(Long locationId) {
        StringBuilder hierarchy = new StringBuilder();
        try {
            LocationMasterBean locationMasterBean = locationMasterBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.ACTUAL_I_D, locationId).queryForFirst();

            if (locationMasterBean != null) {
                hierarchy.append(locationMasterBean.getName());
                for (int i = locationMasterBean.getLevel(); i > 2; i--) {
                    locationMasterBean = locationMasterBeanDao.queryBuilder().where()
                            .eq(FieldNameConstants.ACTUAL_I_D, locationMasterBean.getParent()).queryForFirst();

                    hierarchy.insert(0, locationMasterBean.getName() + " > ");
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return hierarchy.toString();
    }

    @Override
    public LocationBean getLocationBeanByActualId(String actualId) {
        try {
            return locationBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, actualId).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }
    @Override
    public LocationMasterBean getLocationMasterBeanByActualId(Integer actualId) {
        try {
            return locationMasterBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, actualId).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return null;
    }

    @Override
    public String getLocationTypeNameByType(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }

        LocationTypeBean locationTypeBean = null;
        try {
            locationTypeBean = locationTypeBeanDao.queryBuilder().where().eq("type", type).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (locationTypeBean == null) {
            return null;
        }

        return locationTypeBean.getName();
    }

    @Override
    public List<LocationMasterBean> retrieveLocationMastersAssignedToUser() {
        if (SewaTransformer.loginBean == null || SewaTransformer.loginBean.getVillageCode() == null || SewaTransformer.loginBean.getVillageCode().isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> locationIdsAssignedToUser = new ArrayList<>();
        for (String id : SewaTransformer.loginBean.getVillageCode().split(":")) {
            locationIdsAssignedToUser.add(Integer.valueOf(id));
        }

        List<LocationMasterBean> locations = new ArrayList<>();
        try {
            locations = locationMasterBeanDao.queryBuilder().where().in("actualID", locationIdsAssignedToUser).query();
        } catch (SQLException ex) {
            Log.e(getClass().getSimpleName(), null, ex);
        }
        return locations;
    }

    @Override
    public List<LocationMasterBean> getLocationWithNoParent() {
        List<LocationMasterBean> assignedLoc = retrieveLocationMastersAssignedToUser();
        if (assignedLoc == null || assignedLoc.isEmpty()) {
            try {
                return locationMasterBeanDao.queryBuilder().where().isNull("parent").query();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        LocationMasterBean bean = assignedLoc.get(0);
        while (bean.getParent() != null) {
            try {
                bean = locationMasterBeanDao.queryBuilder().where().eq("actualID", bean.getParent()).queryForFirst();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        List<LocationMasterBean> beans = new ArrayList<>();
        beans.add(bean);
        return beans;
    }
    public List<LocationMasterBean> retrieveLocationMasterBeansByParentList(List<Integer> parentList) {
        if (parentList == null || parentList.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return locationMasterBeanDao.queryBuilder().where()
                    .in(FieldNameConstants.PARENT, parentList).query();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return Collections.emptyList();
    }
    public List<Integer> getLocationIdsInsideDistrictOfUser() {
        List<Integer> allLocationIds = new ArrayList<>();
        boolean bool = true;
        LocationMasterBean location = retrieveLocationMastersAssignedToUser().get(0);
        while (bool) {
            location = getLocationMasterBeanByActualId(location.getParent());
            if (location.getType().equalsIgnoreCase("D") || location.getType().equalsIgnoreCase("C")) {
                bool = false;
            }
        }

        List<Integer> currentLevelIds = new ArrayList<>();
        currentLevelIds.add(location.getActualID().intValue());
        allLocationIds.add(location.getActualID().intValue());
        while (!currentLevelIds.isEmpty()) {
            List<LocationMasterBean> children = retrieveLocationMasterBeansByParentList(currentLevelIds);
            currentLevelIds.clear();
            if (children.isEmpty()) {
                continue;
            }
            for (LocationMasterBean master : children) {
                currentLevelIds.add(master.getActualID().intValue());
            }
            allLocationIds.addAll(currentLevelIds);
        }

        return allLocationIds;
    }
}
