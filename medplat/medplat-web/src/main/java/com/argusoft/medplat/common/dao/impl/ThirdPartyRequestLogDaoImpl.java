package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.ThirdPartyRequestLogDao;
import com.argusoft.medplat.common.model.ThirdPartyRequestLogModel;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <p>
 * Defines Database Logic for Third party API Log
 * </p>
 *
 * @author ashish
 * @since 02/09/2020 04:40
 *
 */
@Repository
@Transactional
public class ThirdPartyRequestLogDaoImpl extends GenericDaoImpl<ThirdPartyRequestLogModel, Integer> implements ThirdPartyRequestLogDao {

}
