
package com.argusoft.medplat.code.dao;

import com.argusoft.medplat.code.model.SystemCodeListMaster;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *     Defines database methods for system code list
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
public interface SystemCodeListDao extends GenericDao<SystemCodeListMaster, UUID> {

    /**
     * Returns a system code of given id
     * @param uuid An UUID
     * @return An instance of SystemCodeListMaster
     */
    SystemCodeListMaster retrieveByUUID(UUID uuid);

    /**
     * Returns a list of system code based on given search parameter
     * @param searchNameString A name search string
     * @param descTypeId A description type id
     * @param moduleId A module id
     * @param codeType A code type
     * @return A list of SystemCodeListMaster
     */
    List<SystemCodeListMaster> getSystemCodes(String searchNameString,String descTypeId,String moduleId, String codeType );

    /**
     * Returns a list of system code based on given code id and code type
     * @param codeId A code id
     * @param codeType A code type
     * @return A list of SystemCodeListMaster
     */
    List<SystemCodeListMaster> getSystemCodesByCodeId(String codeId, String codeType);

    /**
     * Returns a list of system code based on given code and code type
     * @param code A code value
     * @param codeType A code type
     * @return A list of SystemCodeListMaster
     */
    List<SystemCodeListMaster> getSystemCodesByCode(String code, String codeType);

}
