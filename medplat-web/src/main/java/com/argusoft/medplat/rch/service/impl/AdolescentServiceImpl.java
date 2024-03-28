/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.event.dto.Event;
import com.argusoft.medplat.event.service.EventHandler;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.listvalues.service.ListValueFieldValueDetailService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.AdolescentDao;
import com.argusoft.medplat.rch.dao.AncVisitDao;
import com.argusoft.medplat.rch.dto.AncMasterDto;
import com.argusoft.medplat.rch.dto.ImmunisationDto;
import com.argusoft.medplat.rch.mapper.AncMapper;
import com.argusoft.medplat.rch.model.AdolescentEntity;
import com.argusoft.medplat.rch.model.AncVisit;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import com.argusoft.medplat.rch.service.AdolescentService;
import com.argusoft.medplat.rch.service.AncService;
import com.argusoft.medplat.rch.service.ImmunisationService;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Define services for anc.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class AdolescentServiceImpl implements AdolescentService {


    @Autowired
    private AdolescentDao adolescentDao;


    @Override
    public Integer storeAdolescentForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
        AdolescentEntity adolescentEntity = new AdolescentEntity();
        keyAndAnswerMap.forEach((key, answer) -> setAnswersToAdolescentForm(key, answer, adolescentEntity));

        adolescentDao.create(adolescentEntity);
        adolescentDao.flush();
        return adolescentEntity.getId();
    }

    private void setAnswersToAdolescentForm(String key, String answer, AdolescentEntity adolescentEntity) {
        switch (key) {
            case "3":
                adolescentEntity.setHeightOfMember(Integer.parseInt(answer));
                break;
            case "4" :
                adolescentEntity.setWeight(Integer.parseInt(answer));
                break;
        }
    }
}
