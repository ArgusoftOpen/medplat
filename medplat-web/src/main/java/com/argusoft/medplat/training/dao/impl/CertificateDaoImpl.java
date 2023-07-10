/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.training.dao.CertificateDao;
import com.argusoft.medplat.training.model.Certificate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in certificate dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@Repository("certificateDao")
public class CertificateDaoImpl extends GenericDaoImpl<Certificate, Integer> implements CertificateDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Certificate> getCertificatesByCourseAndType(Integer courseId, Certificate.Type type) {
        PredicateBuilder<Certificate> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(Certificate.Fields.COURSE_ID), courseId));
            predicates.add(builder.equal(root.get(Certificate.Fields.TYPE), type));
            return predicates;
        };
        return new ArrayList<>(this.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Certificate> getCertificatesByTrainingAndUser(Integer trainingId, Integer userId) {
        PredicateBuilder<Certificate> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(Certificate.Fields.TRAINING_ID), trainingId));
            predicates.add(builder.equal(root.get(Certificate.Fields.USER_ID), userId));
            return predicates;
        };
        return new ArrayList<>(this.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Certificate> getCertificatesByTrainingIdsAndCourseAndType(List<Integer> trainingIds, Integer courseId, Certificate.Type type) {
        PredicateBuilder<Certificate> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(Certificate.Fields.TRAINING_ID)).value(trainingIds));
            predicates.add(builder.equal(root.get(Certificate.Fields.COURSE_ID), courseId));
            predicates.add(builder.equal(root.get(Certificate.Fields.TYPE), type));
            return predicates;
        };
        return new ArrayList<>(this.findByCriteriaList(predicateBuilder));
    }

}
