package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdMaster;

import java.util.List;

public interface NcdMasterDao extends GenericDao<NcdMaster, Integer> {
    NcdMaster retriveByMemberIdAndDiseaseCode(Integer memberId, String diseaseCode);

    List<MemberDetailDto> retrieveAllMembers(Integer userId, String type,Integer limit, Integer offset);

    List<MemberDetailDto> retreiveSearchedMembers(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag,Integer userId , Boolean review);
}
