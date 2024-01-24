package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdOphthalmologistDto;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;

public class NcdOphthalmologistDataMapper {

    public static NcdOphthalmologistData dtoToEntity(NcdOphthalmologistDto ncdOphthalmologistDto){
        NcdOphthalmologistData ncdOphthalmologistData = new NcdOphthalmologistData();
        ncdOphthalmologistData.setMemberId(ncdOphthalmologistDto.getMemberId());
        ncdOphthalmologistData.setScreeningDate(ncdOphthalmologistDto.getScreeningDate());
        ncdOphthalmologistData.setLeftEyeFeedback(ncdOphthalmologistDto.getLeftEyeFeedback());
        ncdOphthalmologistData.setLeftEyeOtherType(ncdOphthalmologistDto.getLeftEyeOtherType());
        ncdOphthalmologistData.setRightEyeFeedback(ncdOphthalmologistDto.getRightEyeFeedback());
        ncdOphthalmologistData.setRightEyeOtherType(ncdOphthalmologistDto.getRightEyeOtherType());
        return ncdOphthalmologistData;
    }
}
