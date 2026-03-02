
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcWeight;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Weight
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcWeightDao extends GenericDao<ChildCmtcNrcWeight, Integer> {

    boolean checkIfEntryForWeightExists(Integer admissionId, Date date);

    List<ChildCmtcNrcWeight> getCmtcNrcWeightEntitiesFromAdmissionId(Integer admissionId);
}
