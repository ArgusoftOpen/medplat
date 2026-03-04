/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.LocationMasterDataBean;
import com.argusoft.medplat.mobile.dto.SurveyLocationMobDataBean;
import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.dto.LocationElasticHierarchyDto;
import com.argusoft.medplat.web.location.model.LocationMaster;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Define methods for location details.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
public interface LocationMasterDao extends GenericDao<LocationMaster, Integer> {

    /**
     * Retrieves user's location by user id and location level.
     *
     * @param userId User id.
     * @param level  Location level.
     * @return Returns user's location master details.
     */
    List<LocationMaster> retrieveUserLocationByLevel(Integer userId, Integer level);

    /**
     * Retrieves user's location by user id, location level and parent location id
     *
     * @param userId             User id.
     * @param locationId         Parent location id.
     * @param level              Location level.
     * @param languagePreference Preferred language.
     * @return Returns list of user's location details like id, name, type.
     */
    List<LocationDetailDto> retrieveUserLocationByParentLocation(Integer userId, List<Integer> locationIds, Integer level, String languagePreference);

    /**
     * Retrieves child location master details by parent location id.
     *
     * @param locationId Location id.
     * @return Returns list of child location master details.
     */
    List<LocationMaster> retrieveLocationByParentLocation(Integer locationId);

    /**
     * Retrieves location master details by location level.
     *
     * @param level Location level.
     * @return Returns list of location master details.
     */
    List<LocationMaster> retrieveLocationByLevel(Integer level);

    /**
     * Retrieves location master details by list of location ids.
     *
     * @param locationList List of location ids.
     * @return Returns list of location master details based on given list of location ids.
     */
    List<LocationMaster> retrieveLocationsByIdList(List<Integer> locationList);

    /**
     * Retrieves location by list of location ids.
     *
     * @param locationIds List of location ids.
     * @return Returns list of location master details by list of location ids.
     */
    List<LocationMaster> getLocationsByIds(List<Integer> locationIds);

    /**
     * Retrieves all location's id by list of parent location ids.
     *
     * @param parent List of parent location ids.
     * @return Returns list of location ids.
     */
    List<Integer> getAllLocationIdsByParentList(List<Integer> parent);

    /**
     * Retrieves all location's id by parent location id.
     *
     * @param parentId Parent location id.
     * @return Return list of location ids.
     */
    List<Integer> getAllLocationIdsByParentId(Integer parentId);

    /**
     * Retrieves list of active location master details by location level.
     *
     * @param locationLevel Location level.
     * @param isActive      Is location active or not.
     * @return Returns list of active location master details.
     */
    List<LocationMaster> getLocationsByLocationType(String locationLevel, Boolean isActive);

    /**
     * Retrieves list of active location master details by list of location levels.
     *
     * @param locationLevel List of location levels.
     * @param isActive      Is location active or not.
     * @return Returns list of active location master details.
     */
    List<LocationMaster> getLocationsByLocationType(List<String> locationLevel, Boolean isActive);

    /**
     * Retrieves locations master details by location name and parent details.
     *
     * @param name   Location name.
     * @param type   Location type.
     * @param parent Parent location details.
     * @return Returns list of location master details based on defined params.
     */
    List<LocationMaster> getLocationsByNameAndParent(String name, String type, LocationMaster parent);

    /**
     * Retrieves locations master details by list of parent location ids and location types.
     *
     * @param parentIds     List of parent location ids.
     * @param locationTypes List of location types.
     * @return Returns list of location master details.
     */
    List<LocationMaster> retrieveLocationsByParentIdAndType(List<Integer> parentIds, List<String> locationTypes);

    /**
     * Returns all active location details with worker details.
     *
     * @param lastUpdatedOn Last updated on date.
     * @return Returns list of location master details with worker info.
     */
    List<LocationMasterDataBean> retrieveAllActiveLocationsWithWorkerInfo(Date lastUpdatedOn);

    /**
     * Retrieves all location details for mobile by used id.
     *
     * @param userId User id.
     * @return Returns list of location details like name, parent, is active or not, lgdCode etc.
     */
    List<SurveyLocationMobDataBean> retrieveAllLocationForMobileByUserId(Integer userId, Integer roleId);

    /**
     * Retrieves locations those are assigned to ASHA worker by user id.
     *
     * @param userId User id.
     * @return Returns list of location details like name, parent, is active or not, lgdCode etc.
     */
    List<SurveyLocationMobDataBean> retrieveAllAreasAssignedToAshaForMobileByUserId(Integer userId);

    /**
     * Retrieves locations those are assigned to FHSR by user id.
     * @param userId User id.
     * @return Returns list of location details like name, parent, is active or not, lgdCode etc.
     */
//    List<SurveyLocationMobDataBean> retrieveAllLocationsAssignedToFhsrForMobileByUserId(Integer userId);

    /**
     * Retrieves location hierarchy by location id.
     *
     * @param locationId Location id.
     * @return Returns location hierarchy.
     */
    String retrieveLocationHierarchyById(Integer locationId);

    /**
     * Retrieves location hierarchy in english.
     *
     * @param locationId Location id.
     * @return Returns location details which contains hierarchy in english.
     */
    String retrieveEngLocationHierarchyById(Integer locationId);

    /**
     * Retrieves block type locations by user id.
     *
     * @param userId User id.
     * @return Returns list of location master details.
     */
    List<LocationMaster> retrieveBlockLocationsByUserId(Integer userId);

    /**
     * Retrieves KPI records for villages by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI records details like individuals enrolled, count of male individuals enrolled etc.
     */
//    List<KpiRecord> retrieveKpiRecordForVillages(Integer parentId);

    /**
     * Retrieves KPI params for hypertension by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveKpiParamsForHypertension(Integer parentId);

    /**
     * Retrieves KPI params for diabetes by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveKpiParamsForDiabetes(Integer parentId);

    /**
     * Retrieves KPI params for oral by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveKpiParamsForOral(Integer parentId);

    /**
     * Retrieves KPI params for breast by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveKpiParamsForBreast(Integer parentId);

    /**
     * Retrieves KPI params for cervical by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveKpiParamsForCervical(Integer parentId);

    /**
     * Retrieves KPI params for aggregate by parent location id.
     * @param parentId Parent location id.
     * @return Returns list of KPI params details like individuals fully screened by ANM, individuals screened etc.
     */
//    List<KpiParams> retrieveAggregateKpiParams(Integer parentId);

    /**
     * Retrieves worker's name by location id.
     *
     * @param locationId Location id.
     * @return Returns name of worker.
     */
    String retrieveWorkerInfoByLocationId(Integer locationId);

    /**
     * Check for parent location correct or not.
     *
     * @param parentLocationId Parent location id.
     * @param type             Location type.
     * @return Returns parent location correct or not.
     */
    Boolean isParentLocationIsCorrect(Integer parentLocationId, String type);

    /**
     * Retrieves location master details by parent location id and location name.
     *
     * @param parent Parent location id.
     * @param name   Location name.
     * @return Returns location master details.
     */
    LocationMaster retrieveLocationByParentIdAndName(Integer parent, String name);

    /**
     * Retrieves parent location details by location id and preferred language.
     *
     * @param locationId         Location id.
     * @param languagePreference Preferred language.
     * @return Returns parent location details by defined params.
     */
    LocationDetailDto getParentLocationDetail(Integer locationId, String languagePreference);

    /**
     * Retrieves location details by location id, location level and preferred language.
     *
     * @param locationId         Location id.
     * @param level              Location level.
     * @param languagePreference Preferred language.
     * @return Returns list of location details by defined params.
     */
    List<LocationDetailDto> retrieveLocationByParentLocation(List<Integer> locationIds, Integer level, String languagePreference);

    /**
     * Retrieves location full name by location id.
     *
     * @param locationId Location id.
     * @return Returns full name of location.
     */
    String retrieveLocationFullNameById(Integer locationId);

    /**
     * Retrieves location elastic hierarchy details by list of location ids.
     *
     * @param ids List of location ids.
     * @return Returns list of location elastic hierarchy details.
     */
    List<LocationElasticHierarchyDto> retriveLocationListByIds(List<Integer> ids);

    /**
     * Retrieves location elastic hierarchy details by location name.
     *
     * @param query Location name.
     * @return Returns list of location elastic hierarchy details.
     */
    List<LocationElasticHierarchyDto> retriveLocationListByQuary(String query);

    /**
     * retrieve active locations by given id and allowed type
     *
     * @param ids           locationId
     * @param locationTypes allowed location type
     * @return locations
     */
    List<LocationMaster> retrieveLocationsByLocationIdAndType(List<Integer> ids, List<String> locationTypes);
}
