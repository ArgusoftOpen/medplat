package com.argusoft.medplat.database.common;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @param <EntityType> Represents ORM entity
 * @author dhavalhirpara
 * @since 22-MAY-2020
 */
@FunctionalInterface
public interface PredicateBuilder<EntityType> {
    /**
     * This abstract method is to implement to get list of Predicate
     *
     * @param root            JPA Root object
     * @param criteriaBuilder JPA CriteriaBuilder object
     * @param criteriaQuery   JPA CriteriaQuery object
     * @return list of Predicate
     */
    List<Predicate> toPredicates(Root<EntityType> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<EntityType> criteriaQuery);
}
