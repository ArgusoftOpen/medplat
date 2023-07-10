
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcScreening;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Screening
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcScreeningDao extends GenericDao<ChildCmtcNrcScreening, Integer> {
    /**
     * Returns child cmtc nrc details based on given child id
     * @param childId An id of admission
     * @return An instance of ChildCmtcNrcScreening
     */
    ChildCmtcNrcScreening retrieveByChildId(Integer childId);

    /**
     * Returns child cmtc nrc details based on given admission id
     * @param admissionId An id of admission
     * @return An instance of ChildCmtcNrcScreening
     */
    ChildCmtcNrcScreening retrieveByAdmissionId(Integer admissionId);

    /**
     * Updates admission id in screening info
     * @param screeningId A screening id
     * @param admissionId An id of admission
     * @return
     */
    int updateAdmissionIdInScreeningInfo(Integer screeningId, Integer admissionId);

    /**
     * Returns a details of child sd score based on given gender, height, weight
     * @param gender A gender of child
     * @param height A height of child
     * @param weight A weight of child
     * @return A string of sd score
     */
    String getSdScore(String gender, Integer height, Float weight);

    /**
     * updates discharge status of given screening id
     * @param screeningId A discharge id
     * @param id A screening id
     * @return An id of update row
     */
    int updateDischargeIdInScreeningInfo(Integer screeningId, Integer id);

    /**
     * updates default status of given screening id
     * @param screeningId A screening id
     * @return An id of update row
     */
    int updateDefualterStateInScreeningInfo(Integer screeningId);

    /**
     * updates death status of given screening id
     * @param screeningId A screening id
     * @return An id of update row
     */
    int updateDeathStatusInScreeningInfo(Integer screeningId);

    /**
     * Cron job to mark child state to default
     */
    void markChildAsDefaulterCronJob();

    /**
     * Cron job to mark child state to default by follow up visit
     */
    void markChildAsDefaulterByFollowUpVisitsCronJob();

    /**
     * updates screening center
     * @param screeningId A screening id
     * @param screeningCenter A name of screening cemter
     */
    void updateScreeningCenter(Integer screeningId, Integer screeningCenter);

    /**
     * Deletes child screening details of given child id
     * @param childId An id of child
     */
    void deleteChildScreeningByChildId(Integer childId);

    /**
     * Updates completed program in screening
     * @param caseId A case od
     */
    void updateCompletedProgramInScreening(Integer caseId);

    /**
     * Checks admission status of given child
     * @param childId An id of child
     * @return An instance of ChildCmtcNrcScreening
     */
    ChildCmtcNrcScreening checkAdmissionValidity(Integer childId);

    /**
     * Returns an admission id of given child id
     * @param childId An id of child
     * @return An admission id
     */
    Integer retrieveAdmisionIdByChildId(Integer childId);

    /**
     * Updates higher facility referral details
     * @param screeningId A screening id
     * @param screeningCenter A name of screening cemter
     * @param higherFacilityId A higher facility id
     * @return An id of updated row
     */
    int updateHigherFacilityReferralDetails(Integer screeningId, Integer screeningCenter, Integer higherFacilityId);
}
