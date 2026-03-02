/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.dao.impl;

import com.argusoft.medplat.dashboard.basketpreference.dao.UserBasketPreferenceDao;
import com.argusoft.medplat.dashboard.basketpreference.model.UserBasketPreference;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * <p> Database logic for user basket preference</p>
 * @author kunjan
 * @since 26/08/20 11:50 AM
 *
 */
@Repository
public class UserBasketPreferenceDaoImpl extends GenericDaoImpl<UserBasketPreference, Integer> implements UserBasketPreferenceDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserBasketPreference retrievePreferenceByUserId(Integer userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserBasketPreference> criteriaQuery = criteriaBuilder.createQuery(UserBasketPreference.class);
        Root<UserBasketPreference> root = criteriaQuery.from(UserBasketPreference.class);
        Predicate userIdEqual = criteriaBuilder.equal(root.get(UserBasketPreference.Fields.USERID), userId);
        criteriaQuery.select(root).where(criteriaBuilder.and(userIdEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }

}
