package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdVisitHistoryDao;
import com.argusoft.medplat.ncd.model.NcdVisitHistory;
import org.springframework.stereotype.Repository;

@Repository
public class NcdVisitHistoryDaoImpl extends GenericDaoImpl<NcdVisitHistory, Integer> implements NcdVisitHistoryDao {
}
