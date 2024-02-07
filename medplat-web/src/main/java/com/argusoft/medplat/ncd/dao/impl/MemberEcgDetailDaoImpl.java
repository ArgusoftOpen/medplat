/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberEcgDetailDao;
import com.argusoft.medplat.ncd.model.MemberEcgDetail;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class MemberEcgDetailDaoImpl extends GenericDaoImpl<MemberEcgDetail, Integer> implements MemberEcgDetailDao {

    @Override
    public MemberEcgDetail retrieveByPdfUuId(String uuid) {
        if (uuid == null) {
            return null;
        }
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq(MemberEcgDetail.Fields.REPORT_PDF_UUID, uuid));
        return super.findEntityByCriteriaList(criterions);
    }

    public MemberEcgDetail retrieveByImageUuId(String uuid) {
        if (uuid == null) {
            return null;
        }
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq(MemberEcgDetail.Fields.REPORT_IMAGE_UUID, uuid));
        return super.findEntityByCriteriaList(criterions);
    }
}
