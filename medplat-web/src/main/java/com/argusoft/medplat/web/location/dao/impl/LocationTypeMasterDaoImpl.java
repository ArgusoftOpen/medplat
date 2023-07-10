/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.location.dao.LocationTypeMasterDao;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in location type master dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@Repository("hierarchyTypeMasterDao")
public class LocationTypeMasterDaoImpl extends GenericDaoImpl<LocationTypeMaster, Integer> implements LocationTypeMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationTypeMaster> retrieveLocationTypeMasterByModifiedOn(Date modifiedOn) {
        return findByCriteriaList((root, builder, type) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (modifiedOn != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("modifiedOn"), modifiedOn));
            }
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationTypeMaster retrieveLocationType(String type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<LocationTypeMaster> cq = cb.createQuery(LocationTypeMaster.class);
        Root<LocationTypeMaster> root = cq.from(LocationTypeMaster.class);
        cq.select(root).where(cb.equal(root.get(LocationTypeMaster.Fields.TYPE), type));
        return session.createQuery(cq).uniqueResult();
    }

}
