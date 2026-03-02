
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcLaboratory;

import java.util.List;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Laboratory
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcLaboratoryDao extends GenericDao<ChildCmtcNrcLaboratory, Integer>{

    /**
     * Returns a last child laboratory test details based on given admission id
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcLaboratory
     */
    ChildCmtcNrcLaboratory retrieveLastLaboratoryTest(Integer admissionId);

    /**
     * Returns a list of child laboratory info based on given admission id
     * @param admissionId An id of admission
     * @return A list of ChildCmtcNrcLaboratory
     */
    List<ChildCmtcNrcLaboratory> getCmtcNrcLaboratoryEntitiesFromAdmissionId(Integer admissionId);
    
}
