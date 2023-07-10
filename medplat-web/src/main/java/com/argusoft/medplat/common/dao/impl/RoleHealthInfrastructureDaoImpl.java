
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.RoleHealthInfrastructureDao;
import com.argusoft.medplat.common.model.RoleHealthInfrastructure;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.model.RoleMaster;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * <p>
 *     Implements methods of RoleHealthInfrastructureDao
 * </p>
 * @author vaishali
 * @since 31/08/2020 4:30
 */
@Repository
public class RoleHealthInfrastructureDaoImpl extends GenericDaoImpl<RoleHealthInfrastructure, Integer> implements RoleHealthInfrastructureDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RoleHealthInfrastructure> retrieveByRoleId(Integer roleId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RoleHealthInfrastructure> cq = cb.createQuery(RoleHealthInfrastructure.class);
        Root<RoleHealthInfrastructure> root = cq.from(RoleHealthInfrastructure.class);
        Join<RoleHealthInfrastructure, RoleMaster> roleMasterJoin = root.join("roleMaster", JoinType.INNER);

        cq.select(root);
        cq.where(cb.and(
                cb.equal(root.get("roleId"), roleId),
                cb.equal(root.get("state"), RoleHealthInfrastructure.State.ACTIVE),
                cb.equal(roleMasterJoin.get("state"), RoleHealthInfrastructure.State.ACTIVE)
        ));

        Query<RoleHealthInfrastructure> query = session.createQuery(cq);
        return query.getResultList();
    }


}
