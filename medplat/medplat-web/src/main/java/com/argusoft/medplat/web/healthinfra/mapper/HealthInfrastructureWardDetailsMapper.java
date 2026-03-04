package com.argusoft.medplat.web.healthinfra.mapper;

import com.argusoft.medplat.web.healthinfra.dto.HealthInfrastructureWardDetailsDto;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureWardDetails;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureWardDetailsHistory;

import java.util.Date;

/**
 * <p>
 * Mapper for health infrastructure ward in order to convert dto to model or model to dto.
 * </p>
 *
 * @author jay
 * @since 26/08/20 11:00 AM
 */
public class HealthInfrastructureWardDetailsMapper {

    private HealthInfrastructureWardDetailsMapper() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Convert health infra structure ward details to entity.
     *
     * @param healthInfrastructureWardDetailsDto Details of health infra structure ward.
     * @param loggedInUserId                     Logged in user id.
     * @return Returns entity of health infra structure ward.
     */
    public static HealthInfrastructureWardDetails dtoToEntity(HealthInfrastructureWardDetailsDto healthInfrastructureWardDetailsDto, Integer loggedInUserId) {
        HealthInfrastructureWardDetails healthInfrastructureWardDetails = new HealthInfrastructureWardDetails();
        healthInfrastructureWardDetails.setId(healthInfrastructureWardDetailsDto.getId());
        healthInfrastructureWardDetails.setHealthInfraId(healthInfrastructureWardDetailsDto.getHealthInfraId());
        healthInfrastructureWardDetails.setWardName(healthInfrastructureWardDetailsDto.getWardName());
        healthInfrastructureWardDetails.setWardType(healthInfrastructureWardDetailsDto.getWardType());
        healthInfrastructureWardDetails.setNumberOfBeds(healthInfrastructureWardDetailsDto.getNumberOfBeds());
        healthInfrastructureWardDetails.setNumberOfVentilatorsType1(healthInfrastructureWardDetailsDto.getNumberOfVentilatorsType1());
        healthInfrastructureWardDetails.setNumberOfVentilatorsType2(healthInfrastructureWardDetailsDto.getNumberOfVentilatorsType2());
        healthInfrastructureWardDetails.setNumberOfO2(healthInfrastructureWardDetailsDto.getNumberOfO2());
        healthInfrastructureWardDetails.setStatus(healthInfrastructureWardDetailsDto.getStatus());
        if (healthInfrastructureWardDetailsDto.getCreatedBy() == null) {
            healthInfrastructureWardDetails.setCreatedBy(loggedInUserId);
        } else {
            healthInfrastructureWardDetails.setCreatedBy(healthInfrastructureWardDetailsDto.getCreatedBy());
        }
        if (healthInfrastructureWardDetailsDto.getCreatedOn() == null) {
            healthInfrastructureWardDetails.setCreatedOn(new Date());
        } else {
            healthInfrastructureWardDetails.setCreatedOn(healthInfrastructureWardDetailsDto.getCreatedOn());
        }
        healthInfrastructureWardDetails.setModifiedBy(loggedInUserId);
        healthInfrastructureWardDetails.setModifiedOn(new Date());

        return healthInfrastructureWardDetails;
    }

    /**
     * Convert health infrastructure ward entity to dto.
     *
     * @param healthInfrastructureWardDetails Entity of health infrastructure ward.
     * @param wardId                          Ward id.
     * @param loggedInUserId                  Logged in user id.
     * @param action                          Action name.
     * @return Returns details of health infrastructure ward.
     */
    public static HealthInfrastructureWardDetailsHistory entityToHistoryEntity(HealthInfrastructureWardDetails healthInfrastructureWardDetails, Integer wardId, Integer loggedInUserId, String action) {
        HealthInfrastructureWardDetailsHistory healthInfrastructureWardDetailsHistory = new HealthInfrastructureWardDetailsHistory();
        healthInfrastructureWardDetailsHistory.setHealthInfrastructureWardDetailsId(wardId);
        healthInfrastructureWardDetailsHistory.setAction(action);
        healthInfrastructureWardDetailsHistory.setWardName(healthInfrastructureWardDetails.getWardName());
        healthInfrastructureWardDetailsHistory.setWardType(healthInfrastructureWardDetails.getWardType());
        healthInfrastructureWardDetailsHistory.setNumberOfBeds(healthInfrastructureWardDetails.getNumberOfBeds());
        healthInfrastructureWardDetailsHistory.setNumberOfVentilatorsType1(healthInfrastructureWardDetails.getNumberOfVentilatorsType1());
        healthInfrastructureWardDetailsHistory.setNumberOfVentilatorsType2(healthInfrastructureWardDetails.getNumberOfVentilatorsType2());
        healthInfrastructureWardDetailsHistory.setNumberOfO2(healthInfrastructureWardDetails.getNumberOfO2());
        healthInfrastructureWardDetailsHistory.setStatus(healthInfrastructureWardDetails.getStatus());
        healthInfrastructureWardDetailsHistory.setCreatedBy(loggedInUserId);
        healthInfrastructureWardDetailsHistory.setCreatedOn(new Date());
        healthInfrastructureWardDetailsHistory.setModifiedBy(loggedInUserId);
        healthInfrastructureWardDetailsHistory.setModifiedOn(new Date());

        return healthInfrastructureWardDetailsHistory;
    }
}
