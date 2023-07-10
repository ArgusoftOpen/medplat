package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.QueryAnalysisDetails;
import com.argusoft.medplat.database.common.GenericDao;

/**
 * Analysis of query if it returns more amount of data then expected
 */
public interface QueryAnalysisDetailsDao extends GenericDao<QueryAnalysisDetails, Integer> {
}
