package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.MemberEcgTokenDao;
import com.argusoft.medplat.ncd.model.MemberEcgToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MemberEcgTokenDaoImpl extends GenericDaoImpl<MemberEcgToken, Integer> implements MemberEcgTokenDao {
}
