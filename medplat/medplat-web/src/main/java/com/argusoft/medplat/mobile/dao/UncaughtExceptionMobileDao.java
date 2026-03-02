/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.model.UncaughtExceptionMobile;

import java.util.List;

/**
 *
 * @author kunjan
 */
public interface UncaughtExceptionMobileDao extends GenericDao<UncaughtExceptionMobile, Integer> {

    List<UncaughtExceptionMobile> retrieveActiveUncaughtExceptions();

}
