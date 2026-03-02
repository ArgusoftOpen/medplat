/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service.impl;

import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dao.FormDao;
import com.argusoft.medplat.mobile.dao.UserFormAccessDao;
import com.argusoft.medplat.mobile.dto.UserFormAccessBean;
import com.argusoft.medplat.mobile.mapper.UserFormAccessMapper;
import com.argusoft.medplat.mobile.model.Form;
import com.argusoft.medplat.mobile.model.UserFormAccess;
import com.argusoft.medplat.mobile.model.UserFormAccessPKey;
import com.argusoft.medplat.mobile.service.MoveToProductionService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author avani
 */
@Service
public class MoveToProductionServiceImpl implements MoveToProductionService {

    @Autowired
    private FormDao formDao;
    @Autowired
    private UserFormAccessDao userFormAccessDao;
    @Autowired
    private QueryMasterService queryMasterService;

    @Transactional
    @Override
    public List<UserFormAccessBean> isAnyFormTrainingCompleted(int userId) {
        List<String> formCodeList = new LinkedList<>();

        List<UserFormAccessBean> resultUserForm = new LinkedList<>();
        List<UserFormAccessBean> userFormAccessList = userFormAccessDao.getUserFormAccessDetail(userId);
        for (UserFormAccessBean userFormAccessBean : userFormAccessList) {
            if (userFormAccessBean.isIsTrainingReq() && userFormAccessBean.getState() == null) {
                formCodeList.add(userFormAccessBean.getFormCode());
            }
        }
        if (!CollectionUtils.isEmpty(formCodeList)) {
            List<Form> formDetailList = formDao.findRequireTrainingForm(formCodeList);
            if (formDetailList != null && !formDetailList.isEmpty()) {
                AtomicInteger counter = new AtomicInteger(0);
                List<QueryDto> queryDetailList = formDetailList.stream().map(formDetail -> {
                    LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                    parameters.put("userId", userId);
                    parameters.put("formKey", formDetail.getFormKey());

                    QueryDto queryDTO = new QueryDto();

                    queryDTO.setCode(formDetail.getQuery());
                    queryDTO.setParameters(parameters);
                    queryDTO.setSequence(counter.getAndIncrement());

                    return queryDTO;

                }).collect(Collectors.toList());

                List<QueryDto> queryResultList = queryMasterService.executeQuery(queryDetailList, true);

                if (queryResultList != null && !queryResultList.isEmpty()) {
                    for (QueryDto result : queryResultList) {
                        LinkedHashMap<String, Object> resultMap = result.getResult().get(0);

                        boolean trainingResult = (boolean) resultMap.get(MobileConstantUtil.RESULT);
                        String formCode = (String) resultMap.get(MobileConstantUtil.FORM_CODE);

                        if (trainingResult && !userFormAccessDao.checkIfUserFormAccessAlreadyExists(userId, formCode)) {
                            //insert in user form access
                            UserFormAccess userForm = addUserFormAccessDetail(userId, formCode);
                            resultUserForm.add(UserFormAccessMapper.convertUserFormAccessToUserFormAccessBean(userForm));
                        }
                    }
                }
            }
        }
        return resultUserForm;
    }

    @Transactional
    @Override
    public void userReadyToMoveProduction(int userId, String formCode) {

        UserFormAccess userForm = new UserFormAccess();
        userForm.setUserFormAccessPKey(new UserFormAccessPKey(userId, formCode));
        userForm.setState(UserFormAccess.State.MOVE_TO_PRODUCTION);
        userForm.setCreatedOn(new Date());
        userFormAccessDao.createOrUpdate(userForm);
    }

    @Transactional
    @Override
    public List<UserFormAccessBean> getUserFormAccessDetail(int userId) {
        return userFormAccessDao.getUserFormAccessDetail(userId);
    }

    private UserFormAccess addUserFormAccessDetail(int userId, String formCode) {

        UserFormAccessPKey userPKey = new UserFormAccessPKey();
        userPKey.setUserId(userId);
        userPKey.setFormCode(formCode);

        UserFormAccess userForm = new UserFormAccess();

        userForm.setUserFormAccessPKey(userPKey);
        userForm.setState(UserFormAccess.State.MOVE_TO_PRODUCTION_RESPONSE_PENDING);
        userForm.setCreatedOn(new Date());

        userFormAccessDao.create(userForm);
        userFormAccessDao.flush();

        return userForm;
    }

}
