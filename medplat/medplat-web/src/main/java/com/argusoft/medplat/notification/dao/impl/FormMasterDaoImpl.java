/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.notification.dao.FormMasterDao;
import com.argusoft.medplat.notification.model.FormMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in form master dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository

public class FormMasterDaoImpl extends GenericDaoImpl<FormMaster, Integer> implements FormMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormMaster> retrieveAll(Boolean isActive) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<FormMaster> cq = cb.createQuery(FormMaster.class);
        Root<FormMaster> root = cq.from(FormMaster.class);

        cq.select(root);
        if (isActive != null && isActive.equals(Boolean.TRUE)) {
            cq.where(cb.equal(root.get(FormMaster.Fields.STATE), FormMaster.State.ACTIVE));
        }
        if (isActive != null && isActive.equals(Boolean.FALSE)) {
            cq.where(cb.notEqual(root.get(FormMaster.Fields.STATE), FormMaster.State.ACTIVE));
        }

        return session.createQuery(cq).getResultList();
    }


}
