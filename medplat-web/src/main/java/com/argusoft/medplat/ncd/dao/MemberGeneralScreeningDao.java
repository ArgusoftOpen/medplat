/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.MemberGeneralScreeningDetail;


public interface MemberGeneralScreeningDao extends GenericDao<MemberGeneralScreeningDetail, Integer> {
    
    MemberGeneralScreeningDetail retrieveByImageUuId(String uuid);

}
