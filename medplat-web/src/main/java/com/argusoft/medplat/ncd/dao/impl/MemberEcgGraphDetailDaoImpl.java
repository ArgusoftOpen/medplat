/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberEcgGraphDetailDao;
import com.argusoft.medplat.ncd.model.MemberEcgGraphDetail;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class MemberEcgGraphDetailDaoImpl extends GenericDaoImpl<MemberEcgGraphDetail, Integer> implements MemberEcgGraphDetailDao {



}