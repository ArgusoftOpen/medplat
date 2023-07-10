/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.training.dao.AttendanceDao;
import com.argusoft.medplat.training.model.Attendance;
import com.argusoft.medplat.training.util.TrainingUtil;
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
 *
 * <p>
 * Implementation of methods define in attendance dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@Repository("attendanceDao")
public class AttendanceDaoImpl extends GenericDaoImpl<Attendance, Integer> implements AttendanceDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> getAttendanceByTrainingOnDate(Integer trainingId, Date onDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Attendance> criteria = criteriaBuilder.createQuery(Attendance.class);
        Root<Attendance> root = criteria.from(Attendance.class);
        Predicate equalTrainingId = criteriaBuilder.equal(root.get(Attendance.Fields.TRAINING_ID), trainingId);
        Predicate dateBetween = criteriaBuilder.between(root.get(Attendance.Fields.EFFECTIVE_DATE),
                TrainingUtil.prepareDate(onDate, 0, 0, 0, 0),
                TrainingUtil.prepareDate(onDate, 23, 59, 59, 0));
        criteria.select(root).where(criteriaBuilder.and(equalTrainingId, dateBetween));
        return session.createQuery(criteria).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> getAttendanceByTrainingForDate(Integer trainingId, Date forDate){
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Attendance> criteria = criteriaBuilder.createQuery(Attendance.class);
        Root<Attendance> root = criteria.from(Attendance.class);
        Predicate equalTrainingId = criteriaBuilder.equal(root.get(Attendance.Fields.TRAINING_ID), trainingId);
        Predicate dateBetween = criteriaBuilder.between(root.get(Attendance.Fields.EFFECTIVE_DATE),
                TrainingUtil.prepareDate(forDate, 0, 0, 0, 0),
                TrainingUtil.prepareDate(forDate, 23, 59, 59, 0));
        Predicate stateActive = criteriaBuilder.equal(root.get(Attendance.Fields.STATE), Attendance.State.ACTIVE);
        criteria.select(root).where(criteriaBuilder.and(equalTrainingId, dateBetween, stateActive));
        return session.createQuery(criteria).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> getAttendancesByTraining(Integer trainingId) {
        PredicateBuilder<Attendance> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(Attendance.Fields.TRAINING_ID), trainingId));
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> getAttendancesByTrainingIds(List<Integer> trainingIds) {
        PredicateBuilder<Attendance> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(Attendance.Fields.TRAINING_ID)).value(trainingIds));
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

}
