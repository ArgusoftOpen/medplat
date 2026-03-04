package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.FieldMasterDao;
import com.argusoft.medplat.common.dao.FieldValueDao;
import com.argusoft.medplat.common.dto.FieldValueMasterDto;
import com.argusoft.medplat.common.mapper.FieldValueMasterMapper;
import com.argusoft.medplat.common.model.FieldValueMaster;
import com.argusoft.medplat.common.service.FieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements methods of FieldValueService
 * @author shrey
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class FieldValueServiceImpl implements FieldValueService {

    @Autowired
    FieldValueDao fieldValueDao;
    
    @Autowired
    FieldMasterDao fieldMasterDao;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<FieldValueMasterDto>> getFieldValuesByList(List<Integer> keys) {
        Map<String, List<FieldValueMasterDto>> result = new HashMap<>();
        Map<Integer,String> constantMap = new HashMap<>();
        for (FieldValueMaster field : fieldValueDao.getFieldValuesByList(keys)) {
            if(!constantMap.containsKey(field.getFieldId())){
                constantMap.put(field.getFieldId(),fieldMasterDao.retrieveById(field.getFieldId()).getFieldName());
            }    
            if (!result.containsKey(constantMap.get(field.getFieldId()))) {
                List<FieldValueMasterDto> list = new ArrayList<>();
                list.add(FieldValueMasterMapper.entityToDto(field));
                result.put(constantMap.get(field.getFieldId()), list);
            } else {
                List<FieldValueMasterDto> list = result.get(constantMap.get(field.getFieldId()));
                list.add(FieldValueMasterMapper.entityToDto(field));
                result.put(constantMap.get(field.getFieldId()), list);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldNameById(Integer id) {
        FieldValueMaster fieldValueMaster =  fieldValueDao.retrieveById(id);
        if(fieldValueMaster != null)
            return fieldValueMaster.getFieldValue();
        return null;
    }

       
}
