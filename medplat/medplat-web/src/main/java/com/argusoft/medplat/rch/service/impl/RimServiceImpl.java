/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.service.RimService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Define services for rim.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class RimServiceImpl implements RimService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private EventHandler eventHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeRimVisitForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        Integer memberId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        MemberEntity memberEntity = memberDao.retrieveById(memberId);
        if (keyAndAnswerMap.get("3") != null) {
            memberEntity.setLastMethodOfContraception(keyAndAnswerMap.get("3"));
            if (keyAndAnswerMap.get("31") != null) {
                memberEntity.setFpInsertOperateDate(new Date(Long.parseLong(keyAndAnswerMap.get("31"))));
            }
        }
        if (keyAndAnswerMap.get("4") != null) {
            memberEntity.setMenopauseArrived(ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("4")));
        }
        if (keyAndAnswerMap.get("7") != null) {
            memberEntity.setHysterectomyDone(ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("7")));
        }
        if (keyAndAnswerMap.get("5") != null) {
            memberEntity.setIsIucdRemoved(ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("5")));
            if (Boolean.TRUE.equals(ImtechoUtil.returnTrueFalseFromInitials(keyAndAnswerMap.get("5")))
                    && keyAndAnswerMap.get("6") != null) {
                memberEntity.setIucdRemovalDate(new Date(Long.parseLong(keyAndAnswerMap.get("6"))));
            }
        }
        memberDao.update(memberEntity);
        memberDao.flush();
        eventHandler.handle(new Event(Event.EVENT_TYPE.FORM_SUBMITTED, null, SystemConstantUtil.FHW_RIM, memberId));
        return 1;
    }

}
