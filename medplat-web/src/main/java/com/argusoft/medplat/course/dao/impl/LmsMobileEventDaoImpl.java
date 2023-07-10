package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.LmsMobileEventDao;
import com.argusoft.medplat.course.model.LmsMobileEventMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class LmsMobileEventDaoImpl extends GenericDaoImpl<LmsMobileEventMaster, String> implements LmsMobileEventDao {
}
