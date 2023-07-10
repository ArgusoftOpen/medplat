/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.training.dao.TopicCoverageDao;
import com.argusoft.medplat.training.model.TopicCoverage;
import com.argusoft.medplat.training.util.TrainingUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in topic coverage dao.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@Repository("topicCoverageDao")
public class TopicCoverageDaoImpl extends GenericDaoImpl<TopicCoverage, Integer> implements TopicCoverageDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoverageByTrainingId(Integer trainingId) {
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(TopicCoverage.Fields.TRAINING_ID), trainingId));
            return predicates;
        };
        return new ArrayList<>(this.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoveragesByTrainingIdAndDate(Integer trainingId, Date date) {
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(TopicCoverage.Fields.TRAINING_ID), trainingId));
            predicates.add(builder.greaterThanOrEqualTo(root.get(TopicCoverage.Fields.TOPIC_COVERAGE_EFFECTIVE_DATE), date));
            return predicates;
        };
        return this.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoveragesByTrainingOnDate(Integer trainingId, Date onDate){
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(TopicCoverage.Fields.TRAINING_ID), trainingId));
            predicates.add(builder.between(
                    root.get(TopicCoverage.Fields.TOPIC_COVERAGE_EFFECTIVE_DATE),
                    TrainingUtil.prepareDate(onDate, 0, 0, 0, 0),
                    TrainingUtil.prepareDate(onDate, 23, 59, 59, 0)
            ));
            return predicates;
        };
        return this.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoverageByTopicCoverageIds(List<Integer> topicCoverageIds) {
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(TopicCoverage.Fields.TOPIC_COVERAGE_ID)).value(topicCoverageIds));
            return predicates;
        };
        return this.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoveragesByTrainingAndBeforeDate(Integer trainingId, Date beforeDate) {
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate criteriaTrainingId = builder.equal(root.get(TopicCoverage.Fields.TRAINING_ID), trainingId);
            Predicate criteriaToday = builder.between(
                    root.get(TopicCoverage.Fields.TOPIC_COVERAGE_EFFECTIVE_DATE),
                    TrainingUtil.prepareDate(beforeDate, 0, 0, 0, 0),
                    TrainingUtil.prepareDate(beforeDate, 23, 59, 59, 0)
            );
            Predicate criteriaLessThan = builder.lessThan(root.get(TopicCoverage.Fields.TOPIC_COVERAGE_EFFECTIVE_DATE), TrainingUtil.prepareDate(beforeDate, 0, 0, 0, 0));
            Predicate criteriaCompletedOnNull = builder.isNull(root.get(TopicCoverage.Fields.TOPIC_COVERAGE_COMPLETED_ON));
            Predicate criteriaBeforeWithCompletedOn = builder.and(criteriaLessThan, criteriaCompletedOnNull);
            Predicate criteriaCompletedOnToday = builder.between(
                    root.get(TopicCoverage.Fields.TOPIC_COVERAGE_COMPLETED_ON),
                    TrainingUtil.prepareDate(beforeDate, 0, 0, 0, 0),
                    TrainingUtil.prepareDate(beforeDate, 23, 59, 59, 0)
            );
            Predicate criteraLessthanTodayWithCompletedOnDate = builder.or(criteriaToday, criteriaBeforeWithCompletedOn);
            Predicate finalCriteria = builder.and(
                    criteriaTrainingId,
                    builder.or(criteriaCompletedOnToday, criteraLessthanTodayWithCompletedOnDate)
            );
            predicates.add(finalCriteria);
            return predicates;
        };
        return this.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicCoverage> getTopicCoveragesByTrainingIds(List<Integer> trainingIds) {
        PredicateBuilder<TopicCoverage> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(TopicCoverage.Fields.TRAINING_ID)).value(trainingIds));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Date> getDistinctDatesOfTopicCoveragesByTrainingIds(List<Integer> trainingIds) {
        String query = "select distinct effective_date from tr_topic_coverage_master where training_id in (:trainingId)";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Date> q = session.createNativeQuery(query)
                .addScalar("effective_date", StandardBasicTypes.DATE);
        q.setParameterList("trainingId", trainingIds);
        return q.list();
    }
}
