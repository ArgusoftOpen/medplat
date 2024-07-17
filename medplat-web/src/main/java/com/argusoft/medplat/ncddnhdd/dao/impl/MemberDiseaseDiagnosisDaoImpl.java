/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberDiseaseDiagnosisDao;
import com.argusoft.medplat.ncddnhdd.enums.DiseaseCode;
import com.argusoft.medplat.ncddnhdd.model.MemberDiseaseDiagnosis;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods defined in member disease diagnosis dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberDiseaseDiagnosisDaoImpl extends GenericDaoImpl<MemberDiseaseDiagnosis, Integer> implements MemberDiseaseDiagnosisDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDiseaseDiagnosis retrieveByMemberIdAndDiseaseType(Integer memberId, DiseaseCode diseaseCode) {
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberDiseaseDiagnosis.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(MemberDiseaseDiagnosis.Fields.DISEASE_CODE), diseaseCode));
            predicates.add(criteriaBuilder.isNull(root.get(MemberDiseaseDiagnosis.Fields.IS_CASE_COMPLETED)));
            return predicates;
        });
    }
}
