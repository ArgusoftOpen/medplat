package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.rch.dao.SicklecellScreeningDao;
import com.argusoft.medplat.rch.dto.SicklecellScreeningDto;
import com.argusoft.medplat.rch.mapper.SicklecellScreeningMapper;
import com.argusoft.medplat.rch.service.SicklecellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Define services for rim.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class SicklecellServiceImpl implements SicklecellService {

    @Autowired
    SicklecellScreeningDao sicklecellScreeningDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(SicklecellScreeningDto sicklecellScreeningDto) {
        sicklecellScreeningDao.create(SicklecellScreeningMapper.convertDtoToEntity(sicklecellScreeningDto));
    }
}
