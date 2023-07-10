/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.VolunteersDto;
import com.argusoft.medplat.rch.model.VolunteersMaster;

/**
 * <p>
 * Mapper for volunteer in order to convert dto to model or model to dto.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 11:00 AM
 */
public class VolunteersMapper {
    private VolunteersMapper() {
    }

    /**
     * Convert volunteer details into entity.
     *
     * @param volunteersDto Volunteer details.
     * @return Returns entity of volunteer.
     */
    public static VolunteersMaster convertDtoToEntity(VolunteersDto volunteersDto) {
        VolunteersMaster volunteersMaster = new VolunteersMaster();
        volunteersMaster.setHealthInfrastructureId(volunteersDto.getHealthInfrastructureId());
        volunteersMaster.setNoOfVolunteers(volunteersDto.getNoOfVolunteers());
        volunteersMaster.setMonthYear(volunteersDto.getMonthYear());
        return volunteersMaster;
    }

    /**
     * Convert volunteer entity into dto.
     *
     * @param volunteersMaster Entity of volunteer.
     * @return Returns details of volunteer.
     */
    public static VolunteersDto convertEntityToDto(VolunteersMaster volunteersMaster) {
        VolunteersDto volunteersDto = new VolunteersDto();
        volunteersDto.setId(volunteersMaster.getId());
        volunteersDto.setHealthInfrastructureId(volunteersMaster.getHealthInfrastructureId());
        volunteersDto.setNoOfVolunteers(volunteersMaster.getNoOfVolunteers());
        volunteersDto.setMonthYear(volunteersMaster.getMonthYear());
        return volunteersDto;
    }

}
