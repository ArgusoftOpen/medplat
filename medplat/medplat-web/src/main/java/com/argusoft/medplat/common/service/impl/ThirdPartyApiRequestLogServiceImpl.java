/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.ThirdPartyRequestLogDao;
import com.argusoft.medplat.common.dto.ThirdPartyApiRequestLogDto;
import com.argusoft.medplat.common.mapper.ThirdPartyApiRequestLogMapper;
import com.argusoft.medplat.common.model.ThirdPartyRequestLogModel;
import com.argusoft.medplat.common.service.ThirdPartyApiRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <p>
 * Defines service logic for ThirdPartyRequestLog
 * </p>
 *
 * @author ashish
 * @since 02/09/2020 04:40
 *
 */
@Service
@Transactional
public class ThirdPartyApiRequestLogServiceImpl implements ThirdPartyApiRequestLogService {

    @Autowired
    private ThirdPartyRequestLogDao thirdPartyRequestLogDao;

    /**
        {@inheritDoc }
     */
    @Override
    public Integer insertRequestLogForThirdPartyAPI(ThirdPartyApiRequestLogDto dto) {
        ThirdPartyRequestLogModel model = ThirdPartyApiRequestLogMapper.convertDtoToModel(dto);
        return thirdPartyRequestLogDao.create(model);
    }

}
