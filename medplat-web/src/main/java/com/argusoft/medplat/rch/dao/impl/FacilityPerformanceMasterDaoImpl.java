/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.FacilityPerformanceMasterDao;
import com.argusoft.medplat.rch.model.FacilityPerformanceMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in facility performance dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class FacilityPerformanceMasterDaoImpl extends GenericDaoImpl<FacilityPerformanceMaster, Integer> implements FacilityPerformanceMasterDao {


    /**
     * {@inheritDoc}
     */
    @Override
    public FacilityPerformanceMaster getFacilityPerformaceByHidAndDate(Integer hid, Date performanceDate) {
        List<FacilityPerformanceMaster> facilityMasters = super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(FacilityPerformanceMaster.Fields.HEALTH_INFRASTRUCTURE_ID), hid));
            predicates.add(criteriaBuilder.equal(root.get(FacilityPerformanceMaster.Fields.PERFORMANCE_DATE), performanceDate));
            return predicates;
        });
        if (facilityMasters == null || facilityMasters.isEmpty()) {
            return null;
        } else {
            return facilityMasters.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(FacilityPerformanceMaster facilityPerformanceMaster) {
        super.createOrUpdate(facilityPerformanceMaster);
    }
}
