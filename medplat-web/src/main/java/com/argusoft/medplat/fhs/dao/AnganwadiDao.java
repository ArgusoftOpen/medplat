/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import com.argusoft.medplat.fhs.model.Anganwadi;
import com.argusoft.medplat.mobile.dto.SurveyLocationMobDataBean;

import java.util.List;

/**
 *
 * <p>
 * Define methods for anganwadi.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface AnganwadiDao extends GenericDao<Anganwadi,Integer>{

    /**
     * Retrives all anganwadis by parent id.
     * @param parentIds List of parent ids.
     * @return Returns list of anganwadi details.
     */
    List<Anganwadi> getAnganwadisByParentIdsList(List<Integer> parentIds);

    /**
     * Retrieves all anganwadi for the given user and location id.
     * @param userId Id of user.
     * @param locationId Id of location.
     * @param limit The number of data need to fetch.
     * @param offset The number of data to skip before starting to fetch details.
     * @return Returns list of anganwadi details.
     */
    List<AnganwadiDto> getAnganwadisByUserId(Integer userId,Integer locationId,Integer limit,Integer offset);

    /**
     * Used to toggle the state of anganwadi.
     * @param id Id of anganwadi.
     * @param isActive Is anganwadi active or not.
     */
    void toggleActive(Integer id,Boolean isActive);

    /**
     * Retrieves anganwadi name by id.
     * @param anganwadiId Id of anganwadi.
     * @return Returns name of anganwadi.
     */
    String fetchAnganwadiArea(Integer anganwadiId);


    /**
     * Retrieves all anganwadi details for mobile by user id.
     * @param userId Id of user.
     * @return Returns list of all anganwadi.
     */
    List<SurveyLocationMobDataBean> retrieveAllAnganwadiForMobileByUserId(Integer userId);
}
