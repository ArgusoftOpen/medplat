package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberHealthDetailDao;
import com.argusoft.medplat.ncd.model.MemberHealthDetails;
import org.springframework.stereotype.Repository;


@Repository
public class MemberHealthDetailDaoImpl extends GenericDaoImpl<MemberHealthDetails, Integer> implements MemberHealthDetailDao {
}
