package com.argusoft.medplat.web.healthinfra.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.HealthInfrastructureBean;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureLocationName;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for health infrastructure.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface HealthInfrastructureDetailsDao extends GenericDao<HealthInfrastructureDetails, Integer> {

    /**
     * Retrieves all health infrastructure details.
     * @param lastUpdatedOn Last updated date.
     * @return Returns list of health infrastructure details.
     */
    List<HealthInfrastructureBean> retrieveAllHealthInfrastructureForMobile(Date lastUpdatedOn);

    /**
     * Add new health infrastructure details.
     * @param entity Entity of health infrastructure.
     * @return Returns health infrastructure entity.
     */
    HealthInfrastructureDetails save(HealthInfrastructureDetails entity);

    /**
     * Retrieves health infrastructure by type.
     * @param typeId Type id.
     * @param searchQuery Name of health infrastructure.
     * @return Returns list of health infrastructure for given search text and type id.
     */
    List<HealthInfrastructureBean> getHealthInfrastructureByType (Integer typeId,String searchQuery);

    /**
     * Returns phc health infra of given member id
     * @param memberId A member id
     * @return An instance of HealthInfrastructureDetails
     */
    HealthInfrastructureDetails getPHCHealthInfraByMemberId(Integer memberId);

    /**
     * Returns health infrastructure of given facilityId
     * @param hfrFacilityId A hfrFacilityId
     * @return An instance of HealthInfrastructureDetails
     */
    HealthInfrastructureDetails retrieveByHFRFacilityId(String hfrFacilityId);

    /**
     * Returns health infrastructure location name of given locationId
     * @param locationId A hfrFacilityId
     * @return An instance of HealthInfrastructureLocationName
     */
    HealthInfrastructureLocationName getLocationName(Integer locationId);

    List<HealthInfrastructureDetails> getHealthInfraByUserIdAndType(Integer userId, String type);

    HealthInfrastructureDetails getHealthInfrastructureDetailsById(Integer healthInfraId);

    HealthInfrastructureDetails getHealthInfrastructureDetailsByHFRId(String hfrFacilityId);

    HealthInfrastructureDetails getHealthInfraByMemberIdAndType(String type, Integer memberId);

    void toggleActive(Integer healthInfraId, String state);

}
