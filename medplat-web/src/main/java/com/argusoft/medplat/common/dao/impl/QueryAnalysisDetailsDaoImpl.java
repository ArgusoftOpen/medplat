package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.QueryAnalysisDetailsDao;
import com.argusoft.medplat.common.model.QueryAnalysisDetails;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Analysis of query if it returns more amount of data then expected
 */
@Repository
public class QueryAnalysisDetailsDaoImpl extends GenericDaoImpl<QueryAnalysisDetails, Integer> implements QueryAnalysisDetailsDao {
}
