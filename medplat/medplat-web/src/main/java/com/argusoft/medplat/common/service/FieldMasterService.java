
package com.argusoft.medplat.common.service;

import java.util.List;

/**
 * <p>
 *     Define methods for field master
 * </p>
 * @author shrey
 * @since 27/08/2020 4:30
 */
public interface FieldMasterService {

     /**
      * Returns a list of id of given name of field constants
      * @param names A list of field constant names
      * @return A list of ids
      */
      List<Integer> getIdsByNameForFieldConstants(List<String> names);
}
