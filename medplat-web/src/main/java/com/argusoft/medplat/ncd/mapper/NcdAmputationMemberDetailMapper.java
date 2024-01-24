package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdAmputationMemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdAmputationMemberDetail;

import java.util.Date;

public class NcdAmputationMemberDetailMapper {
    public static NcdAmputationMemberDetail dtoToEntity(NcdAmputationMemberDetailDto dto){
        NcdAmputationMemberDetail entity = new NcdAmputationMemberDetail();

        entity.setMemberId(dto.getMemberId());
        entity.setCreatedBy(dto.getUserId());
        entity.setCreatedOn(new Date());
        entity.setModifiedBy(dto.getUserId());
        entity.setModifiedOn(new Date());
        entity.setScreeningDate(dto.getScreeningDate());
        entity.setAmputationPresent(dto.isAmputationPresent());

        return entity;
    }
}
