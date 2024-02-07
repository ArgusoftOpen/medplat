/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.MemberEcgDetail;

public interface MemberEcgDetailDao extends GenericDao<MemberEcgDetail, Integer> {

    MemberEcgDetail retrieveByPdfUuId(String uuid);
    MemberEcgDetail retrieveByImageUuId(String uuid);

}
