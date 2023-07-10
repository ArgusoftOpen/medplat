package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.ResponseAnalysisByTimeDetailsDao;
import com.argusoft.medplat.common.model.ResponseAnalysisByTimeDetails;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Analysis of response if it takes more time then expected
 */
@Repository
public class ResponseAnalysisByTimeDetailsDaoImpl extends GenericDaoImpl<ResponseAnalysisByTimeDetails, Integer> implements ResponseAnalysisByTimeDetailsDao {
}
