package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdStrokeMemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdStrokeMemberDetail;

import java.util.Date;

public class NcdStrokeMemberDetailMapper {
    public static NcdStrokeMemberDetail dtoToEntity(NcdStrokeMemberDetailDto dto){
        NcdStrokeMemberDetail entity = new NcdStrokeMemberDetail();

        entity.setMemberId(dto.getMemberId());
        entity.setCreatedBy(dto.getUserId());
        entity.setCreatedOn(new Date());
        entity.setModifiedBy(dto.getUserId());
        entity.setModifiedOn(new Date());
        entity.setScreeningDate(dto.getScreeningDate());
        entity.setStrokePresent(dto.isStrokePresent());

        return entity;
    }
}
