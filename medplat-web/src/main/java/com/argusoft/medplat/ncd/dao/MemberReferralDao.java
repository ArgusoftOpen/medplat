/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
//import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDnhddDto;
import com.argusoft.medplat.ncd.dto.MemberReferralDto;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.model.MemberReferral;

import java.util.List;

/**
 *
 * <p>
 * Define methods for member referral.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface MemberReferralDao extends GenericDao<MemberReferral, Integer> {
    /**
     * Retrieves referral for today for particular member.
     * @param memberId Member id.
     * @param referredBy Referred by.
     * @return Returns list of member referral.
     */
    List<MemberReferralDto> retrieveReffForToday(Integer memberId, Integer referredBy);

    /**
     * Retrieves pending follow up by member id and diseases type.
     * @param memberId Member id.
     * @param diseaseCode Disease code like HT(hypertension),D(diabetes) etc.
     * @return Returns pending follow up.
     */
    MemberReferral retrievePendingFollowUpByMemberIdAndDiseaseType(Integer memberId, DiseaseCode diseaseCode);

    /**
     * Retrieves referral by member id.
     *
     * @param memberId Member id.
     * @return Returns list of member referral.
     */
    List<MemberReferral> retrieveByMemberId(Integer memberId);

    List<MemberReferral> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfrasturctureId);

//    List<MemberReferralDnhddDto> retrieveMembers(Integer userId, Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, Boolean isSus);
}
