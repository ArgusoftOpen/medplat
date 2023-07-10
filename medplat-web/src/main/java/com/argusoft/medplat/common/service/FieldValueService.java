package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.dto.FieldValueMasterDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Define methods for field value
 * </p>
 * @author shrey
 * @since 27/08/2020 4:30
 */
public interface FieldValueService {

    /**
     * Returns a map of FieldValueMasterDto
     * @param keys A list of keys
     * @return A map of FieldValueMasterDto
     */
     Map<String,List<FieldValueMasterDto>> getFieldValuesByList(List<Integer> keys);

    /**
     * Returns field name of given id
     * @param id An id of field
     * @return A name of field
     */
     String getFieldNameById(Integer id);
}
