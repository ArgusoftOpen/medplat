package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdEcgMemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdEcgMemberDetail;

import java.util.Date;

public class NcdEcgMemberDetailMapper {

    public static NcdEcgMemberDetail dtoToEntity(NcdEcgMemberDetailDto dto){
        NcdEcgMemberDetail entity = new NcdEcgMemberDetail();
        entity.setMemberId(dto.getMemberId());
        entity.setSatisfactoryImage(dto.getNeedsRetake());
        entity.setCreatedBy(dto.getUserId());
        entity.setCreatedOn(new Date());
        entity.setModifiedBy(dto.getUserId());
        entity.setModifiedOn(new Date());
        entity.setScreeningDate(dto.getScreeningDate());
        entity.setOldMi(dto.getOldMI());
        entity.setLvh(dto.getLvh());
        entity.setType(dto.getType());
        return entity;
    }

}
