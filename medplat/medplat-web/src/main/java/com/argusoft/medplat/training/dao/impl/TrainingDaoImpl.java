/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.training.dao.TrainingDao;
import com.argusoft.medplat.training.model.Training;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in training dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@Repository("trainingDao")
public class TrainingDaoImpl extends GenericDaoImpl<Training, Integer> implements TrainingDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getEffectiveTrainingsByTrainerOnDate(Integer trainerId, Date date) {

        String query = "select t from Training t join t.trainingPrimaryTrainerIds p where p = :trainerId "
                + "AND (t.trainingEffectiveDate IS NULL OR  t.trainingEffectiveDate <= :date) "
                + "AND (t.trainingExpirationDate IS NULL OR t.trainingExpirationDate >= :date) "
                + "AND t.trainingState <> :state";
        Session currentSession = getCurrentSession();
        Query<Training> q = currentSession.createQuery(query);
        q.setParameter("date", date);
        q.setParameter(Training.Fields.TRAINER_ID, trainerId);
        q.setParameter("state", Training.State.ARCHIVED);

        return q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsByTrainerBeforeDate(Integer trainerId, Date beforeDate) {

        String query = "select t from Training t join t.trainingPrimaryTrainerIds p where :date > t.trainingEffectiveDate and  p = :trainerId";

        Session currentSession = getCurrentSession();
        Query<Training> q = currentSession.createQuery(query);

        q.setParameter("date", beforeDate);
        q.setParameter(Training.Fields.TRAINER_ID, trainerId);

        return q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getEffectiveTrainingsByTrainerAfterDate(Integer trainerId, Date date) {

        String query = "select t from Training t join t.trainingPrimaryTrainerIds p where :date < t.trainingEffectiveDate and  p = :trainerId and t.trainingState <> :state  order by t.createdOn asc";

        Session currentSession = getCurrentSession();
        Query<Training> q = currentSession.createQuery(query);
        q.setParameter("date", date);
        q.setParameter(Training.Fields.TRAINER_ID, trainerId);
        q.setParameter("state", Training.State.ARCHIVED);

       return q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsByTrainer(Integer trainerId, boolean fetchOptionalTrainerTrainings) {

        String query = "select t from Training t join t.trainingPrimaryTrainerIds p where p = :trainerId";

        Session currentSession = getCurrentSession();
        Query<Training> q = currentSession.createQuery(query);
        q.setParameter(Training.Fields.TRAINER_ID, trainerId);

        List<Training> query1Result = q.list();
        List<Training> result = new ArrayList<>(query1Result);

        if (fetchOptionalTrainerTrainings) {
            String query2 = "select t from Training t join t.trainingOptionalTrainerIds o where o = :trainerId";
            Query<Training> q2 = currentSession.createQuery(query2);
            q2.setParameter(Training.Fields.TRAINER_ID, trainerId);
            List<Training> query2Result = q2.list();
            result.addAll(query2Result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getEffectiveTrainingsAfterDate(Date afterDate) {
        PredicateBuilder<Training> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.lessThan(root.get(Training.Fields.TRAINING_EFFECTIVE_DATE), afterDate));
            predicates.add(builder.notEqual(root.get(Training.Fields.TRAINING_STATE), Training.State.ARCHIVED));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsBeforeDate(Date beforeDate) {
        PredicateBuilder<Training> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.lessThan(root.get(Training.Fields.TRAINING_EFFECTIVE_DATE), beforeDate));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getTrainingIdsofUserChildLocationsAndUsersTraining(Integer userId) {

        String query = "select tm.training_id as \"trainingId\" from tr_training_master tm inner join tr_training_org_unit_rel tor using (training_id) "
                + "where org_unit_id in ( "
                + "select child_id from location_hierchy_closer_det where parent_id in (select loc_id from um_user_location where user_id = :userId) "
                + ") union "
                + "(select training_id as \"trainingId\" from tr_training_primary_trainer_rel where primary_trainer_id = :userId) union "
                + "(select training_id as \"trainingId\" from tr_training_optional_trainer_rel where optional_trainer_id = :userId)";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Training> q = session.createNativeQuery(query).addScalar(Training.Fields.TRAINING_ID, StandardBasicTypes.INTEGER);
        q.setParameter("userId", userId);
        List<Training> tranings = q.setResultTransformer(Transformers.aliasToBean(Training.class)).list();
        List<Integer> result = new ArrayList<>();
        for (Training t : tranings) {
            result.add(t.getTrainingId());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsByTrainingIdsAndDate(List<Integer> trainingIds,
                                                           Date afterDate,
                                                           Date currentDate, String limit, String offset)
    {
        if (trainingIds == null || trainingIds.isEmpty() || (afterDate == null && currentDate == null)) {
            return new ArrayList<>();
        }
        PredicateBuilder<Training> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (afterDate != null) {
                predicates.add(builder.greaterThan(root.get(Training.Fields.TRAINING_EFFECTIVE_DATE), afterDate));
            } else if (currentDate != null) {
                predicates.add(builder.and(
                        builder.greaterThanOrEqualTo(root.get(Training.Fields.TRAINING_EXPIRATION_DATE), currentDate),
                        builder.lessThanOrEqualTo(root.get(Training.Fields.TRAINING_EFFECTIVE_DATE), currentDate)
                ));
            }
            predicates.add(root.get(Training.Fields.TRAINING_ID).in(trainingIds));
            return predicates;
        };

        return findByCriteriaList(predicateBuilder, PageRequest.of((Integer.parseInt(offset) / Integer.parseInt(limit)), Integer.parseInt(limit)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAttendeesForUpcomingTrainingsByRoleAndCourse(Integer roleId, Integer courseId) {
        String query = "select attendee.attendee_id as id from tr_training_master tm inner join tr_training_course_rel tcr using (training_id) "
                + "inner join (select * from tr_training_attendee_rel union select * from tr_training_additional_attendee_rel) attendee "
                + "on attendee.training_id = tm.training_id inner join tr_training_target_role_rel tar on tar.training_id = tm.training_id "
                + "where tcr.course_id = :courseId and tm.training_state = 'DRAFT' and "
                + "(tm.effective_date > current_date  or (tm.effective_date <= current_date and tm.expiration_date >= current_date)) "
                + "and tar.target_role_id = :roleId";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query).addScalar("id", StandardBasicTypes.INTEGER);
        q.setParameter("roleId", roleId);
        q.setParameter("courseId", courseId);
        return q.list();
    }
}
