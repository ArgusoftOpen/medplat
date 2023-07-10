package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dao.UserLoginDetailMasterDao;
import com.argusoft.medplat.web.users.model.UserLoginDetailMaster;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *     Implements methods of UserLoginDetailMasterDao
 * </p>
 *
 * @author shrey
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class UserLoginDetailDaoImpl 
        extends GenericDaoImpl<UserLoginDetailMaster, Integer>
        implements UserLoginDetailMasterDao {

    
}
