/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberGeneralScreeningDao;
import com.argusoft.medplat.ncd.model.MemberGeneralScreeningDetail;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class MemberGeneralScreeningDaoImpl extends GenericDaoImpl<MemberGeneralScreeningDetail, Integer> implements MemberGeneralScreeningDao {


    @Override
    public MemberGeneralScreeningDetail retrieveByImageUuId(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return null;
        }
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.ilike(MemberGeneralScreeningDetail.Fields.IMAGE_UUID, uuid, MatchMode.ANYWHERE));
        return super.findEntityByCriteriaList(criterions);
    }
}
