
package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.model.UserLocation;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Implements methods of UserLocationDao
 * </p>
 * @author Harshit
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class UserLocationDaoImpl extends GenericDaoImpl<UserLocation, Integer> implements UserLocationDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getUserMinLevel(Integer userId) {
        StringBuilder query = new StringBuilder("select ")
                .append("min(level) min \n")
                .append("from um_user_location \n")
                .append("where user_id = :userId and state = :state ;");

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query.toString());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("state", UserLocation.State.ACTIVE.toString());
        nativeQuery.addScalar("min", StandardBasicTypes.INTEGER);

        return Integer.parseInt(nativeQuery.uniqueResult().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsInactive(UserLocation userLocation) {
        userLocation.setState(UserLocation.State.INACTIVE);
        super.merge(userLocation);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserLocation> retrieveAllLocationsByUserId(Integer userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserLocation> cq = cb.createQuery(UserLocation.class);
        Root<UserLocation> root = cq.from(UserLocation.class);

        cq.select(root);
        cq.where(cb.equal(root.get(UserLocation.Fields.USER_ID), userId));

        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserLocation> retrieveLocationsByUserId(Integer userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserLocation> cq = cb.createQuery(UserLocation.class);
        Root<UserLocation> root = cq.from(UserLocation.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(UserLocation.Fields.USER_ID), userId),
                        cb.equal(root.get(UserLocation.Fields.STATE), UserLocation.State.ACTIVE)
                )
        );

        return session.createQuery(cq).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserLocation> getUserLocationByLocationId(List<Integer> locationIds) {
        PredicateBuilder<UserLocation> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (locationIds != null && !locationIds.isEmpty()) {
                predicates.add(builder.in(root.get(UserLocation.Fields.LOCATION_ID)).value(locationIds));
            }
            predicates.add(builder.equal(root.get(UserLocation.Fields.STATE), UserLocation.State.ACTIVE));
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveLocationIdsByUserId(Integer userId) {
        StringBuilder query = new StringBuilder("select ")
                .append("loc_id as \"locationId\" \n")
                .append("from um_user_location ")
                .append("where user_id = :userId and state = :state ;");

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query.toString());
        nativeQuery.setParameter(UserLocation.Fields.USER_ID, userId);
        nativeQuery.setParameter(UserLocation.Fields.STATE, UserLocation.State.ACTIVE.toString());
        nativeQuery.addScalar(UserLocation.Fields.LOCATION_ID, StandardBasicTypes.INTEGER);

        return nativeQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLocation getUserLocationByUserIdLocationId(Integer userId, Integer locationId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserLocation> cq = cb.createQuery(UserLocation.class);
        Root<UserLocation> root = cq.from(UserLocation.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(UserLocation.Fields.USER_ID), userId),
                        cb.equal(root.get(UserLocation.Fields.LOCATION_ID), locationId)
                )
        );

        return session.createQuery(cq).uniqueResult();
    }
}
