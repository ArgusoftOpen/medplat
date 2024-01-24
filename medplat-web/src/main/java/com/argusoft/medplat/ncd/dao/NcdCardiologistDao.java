package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;

import java.util.Date;

public interface NcdCardiologistDao extends GenericDao<NcdCardiologistData, Integer> {
    NcdCardiologistData retrieveCardiologistReponseByMemberIdAndDate(Integer memberId, Date date);
}
