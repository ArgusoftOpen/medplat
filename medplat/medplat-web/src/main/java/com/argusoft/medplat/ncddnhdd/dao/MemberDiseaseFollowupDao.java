/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncddnhdd.dto.MemberDiseaseFollowupDto;
import com.argusoft.medplat.ncddnhdd.model.MemberDiseaseFollowup;
import java.util.List;

/**
 *
 * <p>
 * Define methods for member disease follow up.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface MemberDiseaseFollowupDao extends GenericDao<MemberDiseaseFollowup, Integer>{

    /**
     * Retrieves next follow up details for particular member.
     * @param memberId Member id.
     * @param userId User id.
     * @return Returns list of next follow up details.
     */
    List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId,Integer userId);
}

