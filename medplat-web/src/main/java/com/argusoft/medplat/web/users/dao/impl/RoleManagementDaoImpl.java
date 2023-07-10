
package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dao.RoleManagementDao;
import com.argusoft.medplat.web.users.model.RoleManagement;
import com.argusoft.medplat.web.users.model.RoleMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * <p>
 *     Implements methods of RoleManagementDao
 * </p>
 * @author vaishali
 * @since 31/08/2020 4:30
 */
@Repository
public class RoleManagementDaoImpl extends GenericDaoImpl<RoleManagement, Integer> implements RoleManagementDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleManagement> retrieveRolesManagedByRoleId(Integer roleId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RoleManagement> cq = cb.createQuery(RoleManagement.class);
        Root<RoleManagement> root = cq.from(RoleManagement.class);
        Join<RoleManagement, RoleMaster> roleMasterJoin = root.join("roleMaster", JoinType.INNER);

        cq.select(root).orderBy(cb.asc(roleMasterJoin.get("name")));
        cq.where(cb.and(
                cb.equal(root.get(RoleManagement.Fields.MANAGED_BY_ROLE_ID), roleId),
                cb.equal(root.get(RoleManagement.Fields.STATE), RoleManagement.State.ACTIVE),
                cb.equal(roleMasterJoin.get("state"), RoleManagement.State.ACTIVE)
        ));

        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleManagement> retrieveRolesManagementByRoleId(Integer roleId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RoleManagement> cq = cb.createQuery(RoleManagement.class);
        Root<RoleManagement> root = cq.from(RoleManagement.class);

        cq.where(cb.equal(root.get(RoleManagement.Fields.MANAGED_BY_ROLE_ID), roleId));

        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleManagement> retrieveActiveRolesManagementByRoleId(Integer roleId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RoleManagement> cq = cb.createQuery(RoleManagement.class);
        Root<RoleManagement> root = cq.from(RoleManagement.class);

        cq.where(cb.and(
                cb.equal(root.get(RoleManagement.Fields.MANAGED_BY_ROLE_ID), roleId),
                cb.equal(root.get(RoleManagement.Fields.STATE), RoleManagement.State.ACTIVE)
        ));

        return session.createQuery(cq).getResultList();
    }
}
