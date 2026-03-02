
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.util.ICDAPIclient;

/**
 * <p>
 *     Define methods for International Classification of Diseases
 * </p>
 * @author ashish
 * @since 27/08/2020 4:30
 */
public interface ICDService {

    /**
     * Inserts json detail of given criteria
     * @param version A value of version
     * @param releaseYear A value of release year
     * @param startingCode A string code
     * @param endingCode A ending code
     * @return return summary processing request
     */
     String insertJsonDetailsByCodeRange(Integer version,Integer releaseYear,String startingCode,String endingCode);

    /**
     * Establish a connection
     * @return ICDAPIclient An instance of ICDAPIclient
     */
     ICDAPIclient setConnections();
}
