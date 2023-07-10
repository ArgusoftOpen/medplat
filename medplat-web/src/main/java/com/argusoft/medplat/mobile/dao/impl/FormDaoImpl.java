/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.model.Form;
import com.argusoft.medplat.mobile.dao.FormDao;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author avani
 */
@Repository
public class FormDaoImpl extends GenericDaoImpl<Form, String> implements FormDao {

    /**
     * Find form which has is training require true and exclude form which are in input list
     *
     * @param formCodeList
     * @return
     */
    @Override
    public List<Form> findRequireTrainingForm(List<String> formCodeList) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Form> criteria = criteriaBuilder.createQuery(Form.class);
        Root<Form> root = criteria.from(Form.class);
        //Exclude input form keys
        if (formCodeList != null) {
            criteria.where(criteriaBuilder.in(root.get(Form.Fields.FORM_KEY)).value(formCodeList));
        }
        criteria.where(criteriaBuilder.equal(root.get(Form.Fields.IS_TRAINING_REQUIRE), true));
        return session.createQuery(criteria).getResultList();
    }
}
