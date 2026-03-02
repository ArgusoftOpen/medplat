
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.UserAttendanceMasterDao;
import com.argusoft.medplat.common.model.UserAttendanceInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     Implements methods of UserAttendanceMasterDao
 * </p>
 *
 * @author rahul
 * @since 31/08/2020 4:30
 */
@Repository
public class UserAttendanceMasterDaoImpl extends GenericDaoImpl<UserAttendanceInfo, Integer> implements UserAttendanceMasterDao{
    
}
