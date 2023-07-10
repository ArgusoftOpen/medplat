package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.AshaPncMotherMaster;

/**
 * <p>
 * Define methods for ASHA pnc mother master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface AshaPncMotherMasterDao extends GenericDao<AshaPncMotherMaster, Integer> {

    /**
     * Retrieves pnc mother master details by pnc master id and member id.
     *
     * @param pncMasterId Pnc master id.
     * @param memberId    Member id.
     * @return Returns ASHA pnc mother master details.
     */
    AshaPncMotherMaster retrievePncMotherMasterByPncMasterIdAndMemberId(Integer pncMasterId, Integer memberId);
}
