/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.rch.model.ImmunisationMaster;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Define services for immunisations.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public interface ImmunisationService {

    /**
     * Add immunisations details.
     *
     * @param immunisationMaster Immunisations details.
     */
    void createImmunisationMaster(ImmunisationMaster immunisationMaster);

    /**
     * Check for immunisation given or not.
     *
     * @param immunisationMaster Immunisations details.
     * @return Returns true or false based on immunisation given or not.
     */
    boolean checkImmunisationEntry(ImmunisationMaster immunisationMaster);

    /**
     * Retrieves due date of given immunization for child.
     *
     * @param dateOfBirth        Date of birth.
     * @param givenImmunisations Given immunization details.
     * @return Returns due date of given immunization details.
     */
    Set<String> getDueImmunisationsForChild(Date dateOfBirth, String givenImmunisations);

    /**
     * Validate vaccine.
     *
     * @param dob                Date of birth.
     * @param givenDate          Given date of vaccine.
     * @param currentVaccine     Current vaccine name.
     * @param givenImmunisations Given immunisations details.
     * @return Returns validation message like vaccine is valid or not.
     */
    String vaccinationValidationChild(Date dob, Date givenDate, String currentVaccine, String givenImmunisations);
}
