/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.dto.UserFormAccessBean;

import java.util.List;

/**
 *
 * @author avani
 */
public interface MoveToProductionService {
    
    List<UserFormAccessBean> isAnyFormTrainingCompleted(int userId);
    
    void userReadyToMoveProduction(int userId,String formCode);
    
    List<UserFormAccessBean> getUserFormAccessDetail(int userId);
    
}
