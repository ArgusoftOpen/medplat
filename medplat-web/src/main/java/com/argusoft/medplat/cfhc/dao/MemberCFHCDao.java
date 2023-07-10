package com.argusoft.medplat.cfhc.dao;

import com.argusoft.medplat.cfhc.model.MemberCFHCEntity;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database methods for Community First Health Co-op survey</p>
 *
 * @author rahul
 * @since 25/08/20 4:30 PM
 */

public interface MemberCFHCDao extends GenericDao<MemberCFHCEntity, Long> {

    /**
     * Returns list of MemberCFHCEntity based on given familyId
     *
     * @param familyId family id
     * @return A list of MemberCFHCEntity
     */
    List<MemberCFHCEntity> retrieveMemberCFHCEntitiesByFamilyId(Integer familyId);

    MemberCFHCEntity retrieveMemberCFHCEntitiesByMemberId(Integer id);
}
