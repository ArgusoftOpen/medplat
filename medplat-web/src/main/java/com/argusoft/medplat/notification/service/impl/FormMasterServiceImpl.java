/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.service.impl;

import com.argusoft.medplat.notification.dao.FormMasterDao;
import com.argusoft.medplat.notification.dto.FormMasterDto;
import com.argusoft.medplat.notification.mapper.FormMasterMapper;
import com.argusoft.medplat.notification.service.FormMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * <p>
 *     Define services for form master.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class FormMasterServiceImpl implements FormMasterService {

    @Autowired
    FormMasterDao formMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormMasterDto> retrieveAll(Boolean isActive) {
        return FormMasterMapper.convertListMasterToDtoList(formMasterDao.retrieveAll(isActive));
    }

}
