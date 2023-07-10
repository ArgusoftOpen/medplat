package com.argusoft.medplat.web.healthinfra.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureWardDetailsHisotryDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureWardDetailsHistory;
import org.springframework.stereotype.Repository;

/**
 *
 * <p>
 * Implementation of methods define in health infrastructure ward history dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class HealthInfrastructureWardDetailsHistoryDaoImpl extends GenericDaoImpl<HealthInfrastructureWardDetailsHistory, Integer> implements HealthInfrastructureWardDetailsHisotryDao {
}
