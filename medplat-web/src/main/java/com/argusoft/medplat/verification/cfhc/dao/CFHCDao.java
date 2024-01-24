
package com.argusoft.medplat.verification.cfhc.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.verification.cfhc.model.CFHCUpdate;

/**
 * <p>
 *     Defines database methods for child malnutrition treatment center
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
public interface CFHCDao extends GenericDao<CFHCUpdate, Integer> {

    /**
     * Updates member given in CFHCUpdate
     * @param cFHCUpdate An instance of CFHCUpdate
     */
    void updateMember(CFHCUpdate cFHCUpdate);

    /**
     * Returns a location hierarchy of given location id
     * @param locationId A location id
     * @return A location hierarchy string
     */
    String getLocationHierarchy(Integer locationId);

    /**
     * Returns a list value of given field id
     * @param id An id of field
     * @return A list value
     */
    String getValueFromListValue(Integer id);
           
}
