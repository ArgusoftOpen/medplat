package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.CancerScreeningDao;
import com.argusoft.medplat.ncddnhdd.model.CancerScreeningMaster;
import org.springframework.stereotype.Repository;

@Repository
public class CancerScreeningDaoImpl extends GenericDaoImpl<CancerScreeningMaster, Integer>
        implements CancerScreeningDao {
}
