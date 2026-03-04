/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service;

import com.argusoft.medplat.notification.dto.FormMasterDto;

import java.util.List;

/**
 *
 * <p>
 *     Define services for form master.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
 
public interface FormMasterService {

    /**
     * Retrieves all forms.
     * @param isActive Is form active or not.
     * @return Returns list of forms.
     */
    List<FormMasterDto> retrieveAll(Boolean isActive);

}
