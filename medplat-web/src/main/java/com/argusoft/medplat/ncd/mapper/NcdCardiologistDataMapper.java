package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;

public class NcdCardiologistDataMapper {

    public static NcdCardiologistData dtoToEntity (NcdCardiologistDto ncdCardiologistDto){
        NcdCardiologistData ncdCardiologistData = new NcdCardiologistData();

        ncdCardiologistData.setMemberId(ncdCardiologistDto.getMemberId());
        ncdCardiologistData.setScreeningDate(ncdCardiologistDto.getScreeningDate());
        ncdCardiologistData.setCaseConfirmed(ncdCardiologistDto.getCaseConfirmed());
        ncdCardiologistData.setNote(ncdCardiologistDto.getNote());
        ncdCardiologistData.setSatisfactoryImage(ncdCardiologistDto.getSatisfactoryImage());
        ncdCardiologistData.setLvh(ncdCardiologistDto.getLvh());
        ncdCardiologistData.setOldMi(ncdCardiologistDto.getOldMI());
        ncdCardiologistData.setType(ncdCardiologistDto.getType());

        return ncdCardiologistData;
    }
}
