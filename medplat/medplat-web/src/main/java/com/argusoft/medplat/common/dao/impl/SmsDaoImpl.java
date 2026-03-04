
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SmsDao;
import com.argusoft.medplat.common.model.Sms;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     Implements methods of SmsDao
 * </p>
 *
 * @author prateek
 * @since 31/08/2020 4:30
 */
@Repository
public class SmsDaoImpl extends GenericDaoImpl<Sms, Integer> implements SmsDao {

}
