/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.infrastructure.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.infrastructure.model.SchoolEntity;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for school master.
 * </p>
 *
 * @author rahul
 * @since 26/08/20 10:19 AM
 */
public interface SchoolMasterDao extends GenericDao<SchoolEntity, Long> {

     /**
      * Retrieves all schools for mobile.
      * @param lastUpdatedOn Last updated on date.
      * @return Returns list of all schools.
      */
     List<SchoolDataBean> retrieveAllSchoolForMobile(Date lastUpdatedOn);
}
