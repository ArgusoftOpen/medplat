/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.reportconfig.dao.ReportParameterDao.ReportParameterMasterDao;
import com.argusoft.medplat.reportconfig.model.ReportParameterMaster;
import org.springframework.stereotype.Repository;

/**
 *
 * <p>
 * Implementation of methods define in report parameter dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class ReportParameterMasterDaoImpl
        extends GenericDaoImpl<ReportParameterMaster, Integer>
        implements ReportParameterMasterDao {
}
