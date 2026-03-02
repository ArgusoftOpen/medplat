package com.argusoft.medplat.common.util;

import com.argusoft.medplat.common.dao.QueryAnalysisDetailsDao;
import com.argusoft.medplat.common.dao.ResponseAnalysisByTimeDetailsDao;
import com.argusoft.medplat.common.model.QueryAnalysisDetails;
import com.argusoft.medplat.common.model.ResponseAnalysisByTimeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Analysis query and response and store it in db
 */
@Service
@Transactional
public class QueryAndResponseAnalysisService {
    @Autowired
    QueryAnalysisDetailsDao queryAnalysisDetailsDao;

    @Autowired
    ResponseAnalysisByTimeDetailsDao responseAnalysisByTimeDetailsDao;

    /**
     * Insert the query details which returns more data then expected (> 10000)
     * @param query Query String
     * @param parameters Contains different parameter it can be report name, class name and other params used in query
     * @param totalCount Total rows return by query
     */
    public void insertQueryAnalysisDetails(String query, String parameters, Integer totalCount){
        QueryAnalysisDetails queryAnalysisDetails = new QueryAnalysisDetails();
        queryAnalysisDetails.setQueryString(query);
        queryAnalysisDetails.setParameters(parameters);
        queryAnalysisDetails.setExecutionTime(new Date());
        queryAnalysisDetails.setTotalRows(totalCount);
        queryAnalysisDetailsDao.create(queryAnalysisDetails);
    }

    /**
     * Insert the request details which takes more time then expected (> 10 s)
     * @param requestedUrl Requested URL
     * @param timeTakenInMs Time taken by request
     */
    public void insertResponseAnalysisByTimeDetails(String requestedUrl, String requestBody, String requestParam, String timeTakenInMs){
        ResponseAnalysisByTimeDetails responseAnalysisByTimeDetails = new ResponseAnalysisByTimeDetails();
        responseAnalysisByTimeDetails.setRequestedTime(new Date());
        responseAnalysisByTimeDetails.setRequestedUrl(requestedUrl);
        responseAnalysisByTimeDetails.setRequestBody(requestBody);
        responseAnalysisByTimeDetails.setRequestParam(requestParam);
        responseAnalysisByTimeDetails.setTimeTakenInMs(timeTakenInMs);
        responseAnalysisByTimeDetailsDao.create(responseAnalysisByTimeDetails);
    }
}
