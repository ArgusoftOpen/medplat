package com.argusoft.medplat.ncd.mapper;

import com.argusoft.medplat.ncd.dto.NcdAmputationMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdRenalMemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdAmputationMemberDetail;
import com.argusoft.medplat.ncd.model.NcdRenalMemberDetail;

import java.util.Date;

public class NcdRenalMemberDetailMapper {
    public static NcdRenalMemberDetail dtoToEntity(NcdRenalMemberDetailDto dto){
        NcdRenalMemberDetail entity = new NcdRenalMemberDetail();

        entity.setMemberId(dto.getMemberId());
        entity.setCreatedBy(dto.getUserId());
        entity.setCreatedOn(new Date());
        entity.setModifiedBy(dto.getUserId());
        entity.setModifiedOn(new Date());
        entity.setScreeningDate(dto.getScreeningDate());
        entity.setIsSCreatinineDone(dto.getIsSCreatinineDone());
        entity.setSCreatinineValue(dto.getSCreatinineValue());
        entity.setIsRenalComplicationPresent(dto.getIsRenalComplicationPresent());

        return entity;
    }
}
