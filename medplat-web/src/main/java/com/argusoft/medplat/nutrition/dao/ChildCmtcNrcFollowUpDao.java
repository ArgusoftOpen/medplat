
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcFollowUp;

import java.util.List;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Followup
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcFollowUpDao extends GenericDao<ChildCmtcNrcFollowUp, Integer> {

    /**
     * Returns a last follow up visit details of given child
     * @param childId An id of child
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcFollowUp
     */
    ChildCmtcNrcFollowUp getLastFollowUpVisit(Integer childId, Integer admissionId);

    /**
     * Retrieve list of follow up visits of given child by admission id
     * @param childId An id of child
     * @param admissionId An id of admission
     * @return List of follow up visits
     */
    List<ChildCmtcNrcFollowUp> getAllFollowUpVisit(Integer childId, Integer admissionId);

}
