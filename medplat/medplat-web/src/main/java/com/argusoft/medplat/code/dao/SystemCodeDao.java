/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.code.dao;

import com.argusoft.medplat.code.mapper.CodeType;
import com.argusoft.medplat.code.mapper.TableType;
import com.argusoft.medplat.code.model.SystemCodeMaster;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Hiren Morzariya
 */
public interface SystemCodeDao extends GenericDao<SystemCodeMaster, UUID> {

    public SystemCodeMaster retrieveByUUID(UUID uuid);

    public List<SystemCodeMaster> getSystemCodes();

    public List<SystemCodeMaster> getSystemCodesByTypeAndCode(TableType tableType, String tableId, CodeType codeType);

    public List<SystemCodeMaster> getSystemCodesByTableType(String tableType);

    public List<SystemCodeMaster> getSystemCodesByCodeType(String codeType);

}
