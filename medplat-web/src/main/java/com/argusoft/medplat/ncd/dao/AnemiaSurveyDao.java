package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
//import com.argusoft.medplat.mobile.dto.AnemiaMemberDataBean;
import com.argusoft.medplat.ncd.model.AnemiaMemberSurveyDetail;

import java.util.Date;
import java.util.List;

public interface AnemiaSurveyDao extends GenericDao<AnemiaMemberSurveyDetail, Integer> {

//    List<AnemiaMemberDataBean> retrieveAnemiaMemberDetailsByUserId(Integer userId, Date lastModifiedOn);

    AnemiaMemberSurveyDetail retrieveAnemiaMemberDetailsByLabId(Integer labId);

}
