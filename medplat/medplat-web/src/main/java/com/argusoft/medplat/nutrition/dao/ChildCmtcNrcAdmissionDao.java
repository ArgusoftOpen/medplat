
package com.argusoft.medplat.nutrition.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcAdmission;

import java.util.List;

/**
 * <p>
 *     Defines database methods for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) Admission
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public interface ChildCmtcNrcAdmissionDao extends GenericDao<ChildCmtcNrcAdmission, Integer> {
    /**
     *
     * @param memberId A memberId of member
     * @return List of CMTC order by latest entry
     */
    List<ChildCmtcNrcAdmission> getAllCmtcByMemberId(Integer memberId);
    List<ChildCmtcNrcAdmission> getAllCmtcByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId);
}
