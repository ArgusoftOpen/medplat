package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.NcdMemberDataBean;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;

import java.util.Date;
import java.util.List;

public interface NcdMemberDao extends GenericDao<NcdMemberEntity, Integer> {

    List<NcdMemberDataBean> retrieveNcdMemberDetailsByUserId(Integer userId, Date lastModifiedOn);

    NcdMemberEntity retrieveNcdMemberByMemberId(Integer memberId);
}
