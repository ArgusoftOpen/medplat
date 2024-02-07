/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.MemberComplaintsDto;
import com.argusoft.medplat.ncd.model.MemberComplaints;
import java.util.List;

/**
 *
 * <p>
 * Define methods for member complaints.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface MemberComplaintsDao extends GenericDao<MemberComplaints, Integer> {

    /**
     * Retrieves complaint by member id.
     * @param memberId Member id.
     * @return Returns list of complaint details by member id.
     */
    List<MemberComplaintsDto> retrieveByMemberId(Integer memberId);
    
}
