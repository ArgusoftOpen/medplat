/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.mapper;

import com.argusoft.medplat.web.users.dto.UserLocationDto;
import com.argusoft.medplat.web.users.model.UserLocation;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 *    An util class for user location to convert modal to dto  or dto to modal
 *</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class UserLocationMappper {

    private UserLocationMappper() {
            
    }

    /**
     * Converts user location dto to user location modal
     * @param userLocationDto An instance of UserLocationDto
     * @return An instance of UserLocation
     */
    public static UserLocation convertUserLocationDtoToMaster(UserLocationDto userLocationDto) {

       UserLocation userLocation=new UserLocation();
       userLocation.setId(userLocationDto.getId());
       userLocation.setCreatedBy(userLocationDto.getCreatedBy());
       userLocation.setCreatedOn(userLocationDto.getCreatedOn());
       userLocation.setLocationId(userLocationDto.getLocationId());
       userLocation.setLevel(userLocationDto.getLevel());
       userLocation.setState(userLocationDto.getState()==null?UserLocation.State.ACTIVE:userLocationDto.getState());
       userLocation.setUserId(userLocationDto.getUserId());
       userLocation.setType(userLocationDto.getType());
       return userLocation;
    }

    /**
     * Converts user location modal to user location dto
     * @param userLocation An instance of UserLocation
     * @return An instance of UserLocationDto
     */
    public static UserLocationDto convertUserMasterToDto(UserLocation userLocation) {

       UserLocationDto userLocationDto=new UserLocationDto();
       userLocationDto.setId(userLocation.getId());
       userLocationDto.setCreatedBy(userLocation.getCreatedBy());
       userLocationDto.setCreatedOn(userLocation.getCreatedOn());
       userLocationDto.setLocationId(userLocation.getLocationId());
       userLocationDto.setLevel(userLocation.getLevel());
       userLocationDto.setState(userLocation.getState());
       userLocationDto.setUserId(userLocation.getUserId());
       userLocationDto.setName(userLocation.getLocationMaster().getName());
       userLocationDto.setType(userLocation.getType());
       return userLocationDto;
    }


    /**
     * Converts a list of user location modal to user location dto
     * @param userMasterList A list of UserLocation
     * @return A list of UserLocationDto
     */
    public static List<UserLocationDto> convertUserLocationListToDtoList(List<UserLocation> userMasterList) {
        LinkedList<UserLocationDto> userDtoList = new LinkedList<>();
        for (UserLocation userMaster : userMasterList) {
            userDtoList.push(convertUserMasterToDto(userMaster));
        }
        return userDtoList;
    }
}
