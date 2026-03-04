package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.dto.MemberPregnancyStatusBean;
import com.argusoft.medplat.rch.model.MemberPregnancyStatusMaster;

import java.util.List;

/**
 * <p>
 * Define methods for member pregnancy status.
 * </p>
 *
 * @author krishna
 * @since 26/08/20 10:19 AM
 */
public interface MemberPregnancyStatusDao extends GenericDao<MemberPregnancyStatusMaster, Integer> {

    /**
     * Retrieves pregnancy status for mobile.
     *
     * @param userId User id.
     * @return Returns list of pregnancy status details of members.
     */
    List<MemberPregnancyStatusBean> retrievePregnancyStatusForMobile(Integer userId);

}
