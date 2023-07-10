package com.argusoft.medplat.code.dao.impl;

import com.argusoft.medplat.code.dao.SystemCodeListDao;
import com.argusoft.medplat.code.model.SystemCodeListMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *     Implements methods of SystemCodeDao
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
@Repository
public class SystemCodeListDaoImpl extends GenericDaoImpl<SystemCodeListMaster, UUID> implements SystemCodeListDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemCodeListMaster retrieveByUUID(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SystemCodeListMaster> cq = cb.createQuery(SystemCodeListMaster.class);
        Root<SystemCodeListMaster> root = cq.from(SystemCodeListMaster.class);
        cq.select(root);
        cq.where(cb.equal(root.get(SystemCodeListMaster.Fields.ID), uuid));
        return session.createQuery(cq).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListMaster> getSystemCodes(String searchNameString, String descTypeId, String moduleId, String codeType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SystemCodeListMaster> cq = cb.createQuery(SystemCodeListMaster.class);
        Root<SystemCodeListMaster> root = cq.from(SystemCodeListMaster.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (searchNameString != null && searchNameString.length() > 0) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get(SystemCodeListMaster.Fields.NAME)),
                            cb.lower(cb.literal(searchNameString))),
                    cb.equal(root.get(SystemCodeListMaster.Fields.CODE), searchNameString)
            ));
        }
        if (descTypeId != null && descTypeId.length() > 0) {
            predicates.add(cb.equal(root.get(SystemCodeListMaster.Fields.DESC_TYPE_ID),descTypeId));
        }
        if (moduleId != null && moduleId.length() > 0) {
            predicates.add(cb.equal(root.get(SystemCodeListMaster.Fields.PUBLISHED_EDITION),moduleId));
        }
        if (codeType != null && codeType.length() > 0) {
            predicates.add(cb.equal(root.get(SystemCodeListMaster.Fields.CODE_TYPE),codeType));
        }
        predicates.add(cb.equal(root.get(SystemCodeListMaster.Fields.IS_ACTIVE),Boolean.TRUE));
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(root.get(SystemCodeListMaster.Fields.NAME)));
        return session.createQuery(cq).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListMaster> getSystemCodesByCodeId(String codeId, String codeType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SystemCodeListMaster> cq = cb.createQuery(SystemCodeListMaster.class);
        Root<SystemCodeListMaster> root = cq.from(SystemCodeListMaster.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(SystemCodeListMaster.Fields.CODE_TYPE), codeType),
                        cb.equal(root.get(SystemCodeListMaster.Fields.CODE_ID), codeId),
                        cb.equal(root.get(SystemCodeListMaster.Fields.IS_ACTIVE), Boolean.TRUE)
                )
        );
        cq.orderBy(cb.asc(root.get(SystemCodeListMaster.Fields.NAME)));
        return session.createQuery(cq).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListMaster> getSystemCodesByCode(String code, String codeType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SystemCodeListMaster> cq = cb.createQuery(SystemCodeListMaster.class);
        Root<SystemCodeListMaster> root = cq.from(SystemCodeListMaster.class);
        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(SystemCodeListMaster.Fields.CODE_TYPE), codeType),
                        cb.equal(root.get(SystemCodeListMaster.Fields.CODE), code),
                        cb.equal(root.get(SystemCodeListMaster.Fields.IS_ACTIVE), Boolean.TRUE)
                )
        );
        cq.orderBy(cb.asc(root.get(SystemCodeListMaster.Fields.NAME)));
        return session.createQuery(cq).list();
    }

}
