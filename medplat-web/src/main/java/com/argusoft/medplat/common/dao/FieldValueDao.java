package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.FieldValueMaster;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for field value</p>
 * @author shrey
 * @since 31/08/2020 10:30
 */
public interface FieldValueDao extends GenericDao<FieldValueMaster,Integer>{

    /**
     * Returns a list of FieldValueMaster
     * @param keys A list of keys
     * @return A list of FieldValueMaster
     */
     List<FieldValueMaster> getFieldValuesByList(List<Integer> keys);
    
}
