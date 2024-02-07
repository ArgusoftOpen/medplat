package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.NcdCVCForm;

import java.util.Date;

public interface NcdCVCFormDao extends GenericDao<NcdCVCForm, Integer> {
    NcdCVCForm retrieveCVCDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type);
}
