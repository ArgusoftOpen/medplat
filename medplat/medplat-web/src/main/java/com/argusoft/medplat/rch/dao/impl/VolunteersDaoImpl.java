/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.VolunteersDao;
import com.argusoft.medplat.rch.model.VolunteersMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in volunteers dao.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 10:19 AM
 */
@Repository
public class VolunteersDaoImpl extends GenericDaoImpl<VolunteersMaster, Integer> implements VolunteersDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public VolunteersMaster retrieveData(Integer healthInfrastructureId, Date monthYear) {
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(VolunteersMaster.Fields.HEALTH_INFRASTRUCTURE_ID), healthInfrastructureId));
            predicates.add(criteriaBuilder.equal(root.get(VolunteersMaster.Fields.MONTH_YEAR), monthYear));
            return predicates;
        });
    }

}
