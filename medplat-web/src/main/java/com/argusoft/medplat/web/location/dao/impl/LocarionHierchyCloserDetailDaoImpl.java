/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.model.LocationHierchyCloserDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in location hierarchy closer detail dao.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class LocarionHierchyCloserDetailDaoImpl extends GenericDaoImpl<LocationHierchyCloserDetail, Integer> implements LocationHierchyCloserDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> retrieveParentLocations(Integer childId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<LocationHierchyCloserDetail> root = criteriaQuery.from(LocationHierchyCloserDetail.class);
        Predicate eqChildId = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.CHILD_ID), childId);
        criteriaQuery.select(root.get("parentLocationDetail").get("name")).where(eqChildId);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(LocationHierchyCloserDetail.Fields.DEPTH)));
        Query<String> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveParentLocationIds(Integer childId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<LocationHierchyCloserDetail> root = criteriaQuery.from(LocationHierchyCloserDetail.class);
        Predicate eqChildId = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.CHILD_ID), childId);
        Predicate neParentId = criteriaBuilder.notEqual(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID), -1);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID)));
        criteriaQuery.select(root.get("parentLocationDetail").get("id")).where(criteriaBuilder.and(eqChildId, neParentId));
        Query<Integer> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveChildLocationIds(Integer parentId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<LocationHierchyCloserDetail> root = criteriaQuery.from(LocationHierchyCloserDetail.class);
        Predicate eqChildId = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID), parentId);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(LocationHierchyCloserDetail.Fields.CHILD_ID)));
        criteriaQuery.select(root.get("childLocationDetail").get("id")).where(eqChildId);
        Query<Integer> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getParentLocationIds(Integer childId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<LocationHierchyCloserDetail> root = criteriaQuery.from(LocationHierchyCloserDetail.class);
        Predicate eqChildId = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.CHILD_ID), childId);
        Predicate neParentId = criteriaBuilder.notEqual(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID), -1);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(LocationHierchyCloserDetail.Fields.DEPTH)));
        criteriaQuery.select(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID)).where(criteriaBuilder.and(eqChildId, neParentId));
        Query<Integer> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveChildLocationIdsFromParentList(List<Integer> parentIds, List<String> childLocationType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<LocationHierchyCloserDetail> root = criteriaQuery.from(LocationHierchyCloserDetail.class);
        Predicate getParentsId = criteriaBuilder.in(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID)).value(parentIds);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(LocationHierchyCloserDetail.Fields.CHILD_ID)));
        if (childLocationType != null && !childLocationType.isEmpty()) {
            Predicate getLocationType = criteriaBuilder.in(root.get(LocationHierchyCloserDetail.Fields.CHILD_LOCATION_TYPE)).value(childLocationType);
            criteriaQuery.select(root.get("childLocationDetail").get("id")).where(getParentsId, getLocationType);
        } else {
            criteriaQuery.select(root.get("childLocationDetail").get("id")).where(getParentsId);
        }
        Query<Integer> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocationHierarchyStringByLocationId(Integer locationId) {
        String query = "select string_agg(l2.name,'>' order by lhcd.depth desc) as name\n"
                + "from location_master l1 \n"
                + "inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id\n"
                + "inner join location_master l2 on l2.id = lhcd.parent_id\n"
                + "where l1.id = :locationId";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationId);
        sQLQuery.addScalar("name", StandardBasicTypes.STRING);
        return sQLQuery.list().get(0);
    }
}
