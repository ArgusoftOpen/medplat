/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;

/**
 *
 * <p>
 * Define methods for member disease diagnosis.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface MemberDiseaseDiagnosisDao extends GenericDao<MemberDiseaseDiagnosis, Integer>{

    /**
     * Retrieves disease diagnosis details by member id and disease code.
     * @param memberId Member id.
     * @param diseaseCode Disease code like HT(hypertension),D(diabetes) etc.
     * @return Returns list of disease diagnosis details.
     */
    MemberDiseaseDiagnosis retrieveByMemberIdAndDiseaseType(Integer memberId, DiseaseCode diseaseCode);
    
}
