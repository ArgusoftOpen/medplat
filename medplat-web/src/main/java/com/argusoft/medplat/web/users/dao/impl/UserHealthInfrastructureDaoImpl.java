
package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.users.dao.UserHealthInfrastructureDao;
import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implements methods of UserHealthInfrastructureDao
 * </p>
 *
 * @author vaishali
 * @since 31/08/2020 4:30
 */
@Repository
public class UserHealthInfrastructureDaoImpl extends GenericDaoImpl<UserHealthInfrastructure, Integer> implements UserHealthInfrastructureDao {

    private static final String USER_ID = "userId";
    private static final String STATE = "state";
    private static final String MODIFIED_ON = "modifiedOn";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserHealthInfrastructure> retrieveByHealthInfrastructureIdAndUserId(Integer userId, Integer healthInfrastructureId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserHealthInfrastructure> cq = cb.createQuery(UserHealthInfrastructure.class);
        Root<UserHealthInfrastructure> root = cq.from(UserHealthInfrastructure.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get("healthInfrastructureId"), healthInfrastructureId),
                        cb.equal(root.get(USER_ID), userId),
                        cb.equal(root.get(STATE), UserHealthInfrastructure.State.ACTIVE)
                )
        );

        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserHealthInfrastructure> retrieveByUserId(Integer userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserHealthInfrastructure> cq = cb.createQuery(UserHealthInfrastructure.class);
        Root<UserHealthInfrastructure> root = cq.from(UserHealthInfrastructure.class);
        Join<UserHealthInfrastructure, UserMaster> join = root.join("userMaster", JoinType.INNER);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(USER_ID), userId),
                        cb.equal(root.get(STATE), UserHealthInfrastructure.State.ACTIVE),
                        cb.equal(join.get(STATE), UserHealthInfrastructure.State.ACTIVE)
                )
        );

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<UserHealthInfrastructure> retrieveByHealthInfrastructureId(Integer healthInfrastructureId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("healthInfrastructureId"), healthInfrastructureId));
            predicates.add(criteriaBuilder.equal(root.get(STATE), UserHealthInfrastructure.State.ACTIVE));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int retrieveCountByUserId(Integer userId) {
        StringBuilder query = new StringBuilder("select ")
                .append("count(1) count \n")
                .append("from user_health_infrastructure \n")
                .append("where user_id = :userId and state = :state ;");

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query.toString());
        nativeQuery.setParameter(USER_ID, userId);
        nativeQuery.setParameter(STATE, UserHealthInfrastructure.State.ACTIVE.toString());
        nativeQuery.addScalar("count", StandardBasicTypes.INTEGER);

        return Integer.parseInt(nativeQuery.uniqueResult().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HealthInfrastructureDetails> healthInfraExistsUnderLocation(Integer locationId, List<Integer> healthInfraIds) {
        if (!healthInfraIds.isEmpty()) {
            String query = "select h.name as name,h.id as id from health_infrastructure_details  h where  h.id in (:healthInfraIds) and h.location_id in (select child_id from location_hierchy_closer_det  l\n"
                    + "              where l.parent_id=:locationId)";
            Session session = sessionFactory.getCurrentSession();
            NativeQuery<HealthInfrastructureDetails> sqlQuery = session.createNativeQuery(query);
            return sqlQuery
                    .addScalar("id", StandardBasicTypes.INTEGER)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .setParameter("locationId", locationId)
                    .setParameterList("healthInfraIds", healthInfraIds)
                    .setResultTransformer(Transformers.aliasToBean(HealthInfrastructureDetails.class)).list();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<UserHealthInfrastructure> retrieveByUserId(Integer userId, Date modifiedOn) {
        String query = "select user_id as userId, " +
                "health_infrastrucutre_id as healthInfrastructureId, " +
                "modified_on as modifiedOn, " +
                "is_default as isDefault " +
                "from user_health_infrastructure " +
                "where user_id = :userId  ";

        if (modifiedOn != null) {
            query = query + " and modified_on >= :modifiedOn";
        }


        SQLQuery q = getCurrentSession().createSQLQuery(query);
        q.setParameter("userId", userId);
        if (modifiedOn != null) {
            q.setParameter("modifiedOn", modifiedOn);
        }

        q.addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("healthInfrastructureId", StandardBasicTypes.INTEGER)
                .addScalar("isDefault", StandardBasicTypes.BOOLEAN)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);

        q.setResultTransformer(Transformers.aliasToBean(UserHealthInfrastructure.class));
        return q.list();
    }

}
