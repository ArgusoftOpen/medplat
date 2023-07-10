
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SystemBuildHistoryDao;
import com.argusoft.medplat.common.model.SystemBuildHistory;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * <p>
 *     Implements methods of SystemBuildHistoryDao
 * </p>
 * @author smeet
 * @since 31/08/2020 4:30
 */
@Repository
public class SystemBuildHistoryDaoImpl extends GenericDaoImpl<SystemBuildHistory, Integer> implements SystemBuildHistoryDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemBuildHistory retrieveLastSystemBuild() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SystemBuildHistory> cq = cb.createQuery(SystemBuildHistory.class);
        Root<SystemBuildHistory> root = cq.from(SystemBuildHistory.class);

        cq.select(root).orderBy(cb.desc(root.get("id")));

        return session.createQuery(cq).setMaxResults(1).uniqueResult();
    }
}
