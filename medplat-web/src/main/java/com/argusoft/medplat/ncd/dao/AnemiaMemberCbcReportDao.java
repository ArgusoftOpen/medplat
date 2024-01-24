package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.AnemiaMemberCbcReport;

public interface AnemiaMemberCbcReportDao extends GenericDao<AnemiaMemberCbcReport, Integer> {

    boolean checkCbcReportEntry(Integer memberId);

    AnemiaMemberCbcReport retrieveAnemiaMemberCbcReportByLabId(Integer labId);

}
