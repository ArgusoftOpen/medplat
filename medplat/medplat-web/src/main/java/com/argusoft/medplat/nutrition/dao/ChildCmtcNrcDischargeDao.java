
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcDischarge;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Discharge
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcDischargeDao extends GenericDao<ChildCmtcNrcDischarge, Integer> {
    /**
     * Get discharge details by admission id
     *
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcDischarge
     */
    ChildCmtcNrcDischarge getCmtcNrcDischargeFromAdmissionId(Integer admissionId);

}
