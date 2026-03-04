/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.document.dto.DocumentModuleMasterDto;
import com.argusoft.medplat.document.model.DocumentModuleMaster;

import java.util.List;

/**
 *
 * @author jay
 */
public interface DocumentModuleMasterDao extends GenericDao<DocumentModuleMaster, Integer> {

    List<DocumentModuleMasterDto> retrieveModuleName(String moduleName);
}
