package com.argusoft.medplat.web.healthinfra.service;

/**
 * <p>
 * Define services for health infra structure.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface HealthInfrastructureService {

    /**
     * Update all health infrastructure for mobile.
     */
    void updateAllHealthInfrastructureForMobile();

    /**
     * Retrieves private health infrastructure by name.
     *
     * @param query Name of health infrastructure.
     * @return Returns list of health infrastructure details.
     */
//    List<HealthInfrastructureBean> getHealthInfrastructurePrivateHospital(String query);

    /**
     * update health infrastructure for given health infra id
     *
     * @param id      An id of  health infrastructure.
     * @param request an instance of FacilityBasicInformation
     */
//    void updateHealthInfraStructure(Integer id, FacilityBasicInformation request);

    /**
     * update health infrastructure for given health infra id
     *
     * @param id      An id of  health infrastructure.
     * @param request an instance of FacilityAdditionalInformationRequest
     */
//    void updateHealthInfraStructure(Integer id, FacilityAdditionalInformationRequest request);

    /**
     * update health infrastructure for given health infra id
     *
     * @param id      An id of  health infrastructure.
     * @param request an instance of FacilityDetailedInformationRequest
     */
//    void updateHealthInfraStructure(Integer id, FacilityDetailedInformationRequest request);

    /**
     * get health infrastructure by given id
     *
     * @param id An id of  health infrastructure.
     * @return an instance of HealthInfrastructureHFRDetails.
     */
//    HealthInfrastructureHFRDetails getHealthInfrastructureById(Integer id);

    /**
     * Link hfrFacilityId with bridgeID if not linked with other facility
     *
     * @param id            An id of  private health infrastructure.
     * @param hfrFacilityId A facilityID of health infrastructure.
     * @return an instance of HealthInfrastructureMatchDetail.
     */
//    HealthInfrastructureMatchDetail saveAndLinkHFRId(Integer id, String hfrFacilityId);
}
