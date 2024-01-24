package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;

import java.util.Date;

public interface NcdOphthalmologistDao extends GenericDao<NcdOphthalmologistData, Integer> {
    NcdOphthalmologistData retrieveOphthalmologistReponseByDateAndMemberId(Integer memberId, Date date);
}
