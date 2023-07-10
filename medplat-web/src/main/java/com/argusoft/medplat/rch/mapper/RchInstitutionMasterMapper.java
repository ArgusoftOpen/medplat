/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.RchInstitutionMasterDto;
import com.argusoft.medplat.rch.model.RchInstitutionMaster;

/**
 * <p>
 * Mapper for rch institute master in order to convert dto to model or model to dto.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class RchInstitutionMasterMapper {
    private RchInstitutionMasterMapper() {
    }

    /**
     * Convert rch institute master entity into dto.
     *
     * @param rchInstitutionMaster Entity of rch institute master.
     * @return Returns rch institute master details.
     */
    public static RchInstitutionMasterDto convertRchInstitutionMasterToRchInstitutionMasterDto(RchInstitutionMaster rchInstitutionMaster) {
        RchInstitutionMasterDto rchInstitutionMasterDto = new RchInstitutionMasterDto();
        rchInstitutionMasterDto.setId(rchInstitutionMaster.getId());
        rchInstitutionMasterDto.setName(rchInstitutionMaster.getName());
        rchInstitutionMasterDto.setLocationId(rchInstitutionMaster.getLocationId());
        rchInstitutionMasterDto.setType(rchInstitutionMaster.getType());
        rchInstitutionMasterDto.setIsLocation(rchInstitutionMaster.getIsLocation());
        rchInstitutionMasterDto.setState(rchInstitutionMaster.getState());
        return rchInstitutionMasterDto;
    }

}
