/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.model.Form;

import java.util.List;


/**
 *
 * @author piyush
 */
public interface FormDao extends GenericDao<Form, String>{

     List<Form> findRequireTrainingForm(List<String> formCodeList);
}
