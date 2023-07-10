/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.LocationMobileFeatureDao;
import com.argusoft.medplat.mobile.model.LocationMobileFeature;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author kunjan
 */
@Repository
public class LocationMobileFeatureDaoImpl extends GenericDaoImpl<LocationMobileFeature, Integer> implements LocationMobileFeatureDao {

    @Override
    public List<String> retrieveFeaturesAssignedToTheAoi(Integer userId) {
        String query = "select distinct feature from location_mobile_feature_master  where location_id in \n"
                + "(select distinct parent_id from location_hierchy_closer_det  where child_id  in \n"
                + "(select loc_id from um_user_location  where user_id = :userId and state = 'ACTIVE'));";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("userId", userId);
        return sQLQuery.list();
    }

    @Override
    public LocationMobileFeature retrieveByLocationId(Integer locationId) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationMobileFeature> criteria = criteriaBuilder.createQuery(LocationMobileFeature.class);
        Root<LocationMobileFeature> root = criteria.from(LocationMobileFeature.class);
        criteria.where(criteriaBuilder.equal(root.get("locationId"), locationId));
        return session.createQuery(criteria).uniqueResult();
    }

}
