package com.argusoft.medplat.web.healthinfra.dto;

import java.util.List;

/**
 *
 * <p>
 *     Used for health infrastructure ward details payload.
 * </p>
 * @author dhaval
 * @since 26/08/20 11:00 AM
 *
 */
public class HealthInfrastructureWardDetailsPayloadDto {

    private Boolean isCovidHospital;

    private Integer healthInfraId;

    private List<HealthInfrastructureWardDetailsDto> healthInfrastructureWardDetails;

    public Boolean getIsCovidHospital() {
        return isCovidHospital;
    }

    public void setIsCovidHospital(Boolean isCovidHospital) {
        this.isCovidHospital = isCovidHospital;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public List<HealthInfrastructureWardDetailsDto> getHealthInfrastructureWardDetails() {
        return healthInfrastructureWardDetails;
    }

    public void setHealthInfrastructureWardDetails(List<HealthInfrastructureWardDetailsDto> healthInfrastructureWardDetails) {
        this.healthInfrastructureWardDetails = healthInfrastructureWardDetails;
    }
}
