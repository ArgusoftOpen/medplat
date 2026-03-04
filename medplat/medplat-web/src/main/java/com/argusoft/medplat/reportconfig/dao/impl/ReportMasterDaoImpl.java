/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.reportconfig.dao.ReportMasterDao;
import com.argusoft.medplat.reportconfig.model.ReportMaster;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Implementation of methods define in report master dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class ReportMasterDaoImpl
        extends GenericDaoImpl<ReportMaster, Integer>
        implements ReportMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportMaster getReportMasterWithParamterByIdOrCode(Integer id, String code) {
        String queryString = "SELECT p FROM ReportMaster p LEFT JOIN fetch p.reportParameterMasterList v "
                + " LEFT JOIN fetch p.menuConfig m left join fetch m.menuGroup mg left join fetch m.subGroup sg";

        if (code != null && !code.isEmpty()) {
            queryString = queryString.concat(" where p.code=:codeParam");
        } else {
            queryString = queryString.concat(" where p.id=:param");

        }
        Query<ReportMaster> query = sessionFactory.getCurrentSession().createQuery(
                queryString);
        if (code != null && !code.isEmpty()) {
            query.setParameter("codeParam", code);
        } else {
            query.setParameter("param", id);

        }

        List<ReportMaster> list = query.list();
        return !CollectionUtils.isEmpty(list) ? (list.get(0)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Integer> getAllIds() {
        String queryString = "SELECT DISTINCT r.id FROM ReportMaster r ORDER BY r.id ASC ";
        Query<Integer> query = sessionFactory.getCurrentSession().createQuery(queryString);
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportMaster getReportMasterWithParamterByUUIDOrCode(UUID uuid, String code) {
        String queryString = "SELECT p FROM ReportMaster p LEFT JOIN fetch p.reportParameterMasterList v "
                + " LEFT JOIN fetch p.menuConfig m left join fetch m.menuGroup mg left join fetch m.subGroup sg";

        if (code != null && !code.isEmpty()) {
            queryString = queryString.concat(" where p.code=:codeParam");
        } else {
            queryString = queryString.concat(" where p.uuid=:param");

        }
        Query<ReportMaster> query = sessionFactory.getCurrentSession().createQuery(
                queryString);
        if (code != null && !code.isEmpty()) {
            query.setParameter("codeParam", code);
        } else {
            query.setParameter("param", uuid);

        }

        List<ReportMaster> list = query.list();
        return !CollectionUtils.isEmpty(list) ? (list.get(0)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportMaster> getActiveReports(String reportName, Integer offset,
                                               Integer limit, Boolean groupAssociated, Boolean subGroupAssociated,
                                               Integer groupdId, Integer subGroupId, String userId, String menuType,
                                               String sortBy, String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "desc";
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "rm.id";
        }
        Session session = sessionFactory.getCurrentSession();

        String queryString = "select DISTINCT rm from " + ReportMaster.class.getSimpleName()
                + " rm left join fetch rm.menuConfig m left join fetch m.menuGroup mg "
                + "left join fetch m.subGroup sg";
        queryString += " left join fetch m.userMenuItemList e "
                + " where rm.active = true "
                //                +" ((e.userId= :userId) "
                //                + " OR (e.designationId= :designationId)) "
                + " AND (null=(:menu_type) OR (m.type=(:menu_type)))"
                + " AND (null=(:groupId) OR (m.groupId=(:groupId)))"
                + " AND (null=(:subGroupId) OR (m.subGroupId=(:subGroupId))) "
                + " AND (null=(:reportName) OR  lower(rm.reportName) LIKE (:reportName)) ";
        if (groupAssociated != null && !groupAssociated) {
            queryString += " AND (m.groupId = null)";
        }
        if (subGroupAssociated != null && !subGroupAssociated) {
            queryString += " AND (m.subGroupId = null)";
        }

        queryString += " order by "+sortBy+" "+sortOrder;
        Query query = session.createQuery(queryString);
//        query.setParameter("userId", userId);
//        query.setParameter("designationId", designationId);
        query.setParameter("menu_type", menuType);
        query.setParameter("groupId", groupdId);
        query.setParameter("subGroupId", subGroupId);
//        query.setParameter("sortOrder", sortOrder);
//        query.setParameter("sortBy", sortBy);
        if (reportName != null) {
            reportName = "%" + reportName.toLowerCase() + "%";
        }
        query.setParameter("reportName", reportName);
        if (offset == null) {
            offset = -1;
        }
        if (limit == null) {
            limit = -1;
        }
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isCodeAvailable(String code, Integer id) {
        Session currentSession = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = currentSession.getCriteriaBuilder();
        CriteriaQuery<ReportMaster> cq = cb.createQuery(ReportMaster.class);
        Root<ReportMaster> root = cq.from(ReportMaster.class);
        Predicate codeCondition = cb.equal(root.get("code"), code);
        Predicate idCondition = cb.notEqual(root.get("id"), id);
        if (id != null) {
            cq.select(root).where(cb.and(codeCondition, idCondition));
        } else {
            cq.select(root).where(codeCondition);
        }
        Query<ReportMaster> query = currentSession.createQuery(cq);
        List<ReportMaster> list = query.getResultList();
        if (!list.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isCodeAvailableByUUID(String code, UUID uuid) {
        PredicateBuilder<ReportMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("code"), code));
            if (uuid != null) {
                predicates.add(builder.notEqual(root.get("uuid"), uuid));
            }
            return predicates;
        };
        List<ReportMaster> list = super.findByCriteriaList(predicateBuilder);

        if (!list.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
