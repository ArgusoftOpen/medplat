package com.argusoft.medplat.web.ddb.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.ddb.dao.IndicatorMasterDao;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
/**
 * @author ashwin
 * @since 23/08/2025 15:30
 */

@Repository
@Transactional
public class IndicatorMasterDaoImpl extends GenericDaoImpl<IndicatorMaster, Integer> implements IndicatorMasterDao {

    @Override
    public List<IndicatorMaster> getAllIndicatorMaster() {
        Session session = getCurrentSession();
        TypedQuery<IndicatorMaster> query = session.createQuery(
                "SELECT i FROM IndicatorMaster i ORDER BY i.id DESC",
                IndicatorMaster.class
        );
        return query.getResultList();
    }
}
