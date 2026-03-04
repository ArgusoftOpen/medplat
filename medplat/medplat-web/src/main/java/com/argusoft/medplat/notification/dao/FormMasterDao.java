/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.notification.model.FormMaster;

import java.util.List;

/**
 *
 * <p>
 * Define methods for form master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public interface FormMasterDao extends GenericDao<FormMaster, Integer>{
    /**
     * Retrieves all forms.
     * @param isActive Is form active or not.
     * @return Returns list of forms.
     */
    List<FormMaster> retrieveAll(Boolean isActive);
}
