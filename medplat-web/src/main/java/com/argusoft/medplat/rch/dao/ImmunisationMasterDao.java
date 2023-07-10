/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.rch.model.ImmunisationMaster;

import java.util.List;

/**
 * <p>
 * Define methods for immunisation master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public interface ImmunisationMasterDao extends GenericDao<ImmunisationMaster, Integer> {

    /**
     * Check for immunisation given or not.
     *
     * @param memberId                Member id.
     * @param immunisationGiven       Given immunisation name.
     * @param pregnancyRegistrationId Pregnancy registration id.
     * @return Returns true or false based on immunisation given or not.
     */
    boolean checkImmunisationEntry(Integer memberId, String immunisationGiven, Integer pregnancyRegistrationId);

    /**
     * Retrieves total amount of vitamin A dose given to particular member.
     *
     * @param memberId Member id.
     * @return Returns count of vitamin A does given to member.
     */
    Integer getTotalVitaminADoseGiven(Integer memberId);

    public List<ImmunisationMaster> getAllVaccinesByVaccineType(Integer memberId, String immunisationGiven);

    /**
     * @param memberId A memberId of member
     * @return List of Immunization order by latest entry
     */
    public List<ImmunisationMaster> getAllVaccinesByMemberId(Integer memberId);

    /**
     * @param visitId A visitId of Immunisation
     * @return List of Immunization order by latest entry
     */
    public List<ImmunisationMaster> getAllVaccinesByVisitId(List<Integer> visitId);

    /**
     * @param memberId A memberIf pf member
     * @param vaccine  A vaccine code
     * @return Immunization given to member by vaccine code
     */
    public ImmunisationMaster getVaccineByMemberIdAndVaccineName(Integer memberId, String vaccine);
}
