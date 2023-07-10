package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.AnnouncementHealthInfraDetail;
import com.argusoft.medplat.common.model.AnnouncementHealthInfraDetailPKey;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

public interface AnnouncementHealthInfraDetailDao extends GenericDao<AnnouncementHealthInfraDetail, Integer> {

    List<AnnouncementHealthInfraDetail> retrieveById(AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey);
}
