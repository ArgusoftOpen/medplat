/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.mapper;

import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *<p>
 *    An util class for user to convert modal to dto  or dto to modal
 *</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class UserMapper {

    private UserMapper() {
            
    }

    /**
     * Converts user dto to user modal
     * @param userDto An instance of UserMasterDto
     * @return An instance of UserMaster
     */
    public static UserMaster convertUserDtoToMaster(UserMasterDto userDto, String roleName) {

        UserMaster userMaster = new UserMaster();
        userMaster.setAadharNumber(userDto.getAadharNumber());
        userMaster.setAddress(userDto.getAddress());
        userMaster.setCode(userDto.getCode());
        userMaster.setContactNumber(userDto.getContactNumber() == null ? userDto.getMobileNumber() : userDto.getContactNumber());
        userMaster.setDateOfBirth(userDto.getDateOfBirth());
        userMaster.setEmailId(userDto.getEmailId());
        userMaster.setConvoxId(userDto.getConvoxId());
        userMaster.setFirstName(userDto.getFirstName());
        userMaster.setMiddleName(userDto.getMiddleName());
        userMaster.setLastName(userDto.getLastName());
        userMaster.setPrefferedLanguage(userDto.getPrefferedLanguage());
        userMaster.setRoleId(userDto.getRoleId());
        userMaster.setState(userDto.getState() == null ? UserMaster.State.ACTIVE : userDto.getState());
        userMaster.setUserName(userDto.getUserName());
        userMaster.setId(userDto.getId());
        userMaster.setCreatedBy(userDto.getCreatedBy());
        userMaster.setCreatedOn(userDto.getCreatedOn());
        userMaster.setPassword(userDto.getPassword());
        userMaster.setGender(userDto.getGender());
        userMaster.setImeiNumber(userDto.getImeiNumber());
        userMaster.setTechoPhoneNumber(userDto.getTechoPhoneNumber());
        userMaster.setLoginCode(userDto.getLoginCode());
        userMaster.setTitle(userDto.getTitle());
        if (userDto.getDisplayState() != null) {
            userMaster.setState(userDto.getState());
        }
        String search = userDto.getTitle() + " " + userDto.getFirstName() + " " + userDto.getLastName() + " " + userDto.getUserName() + " " + roleName;
        if (Objects.nonNull(userDto.getMiddleName()) && !userDto.getMiddleName().isEmpty()) {
            search += " " + userDto.getMiddleName();
        }
        if (userDto.getAadharNumber() != null) {
            search += " " + userDto.getAadharNumber();
        }
        if (userDto.getImeiNumber() != null) {
            search += " " + userDto.getImeiNumber();
        }
        if (userDto.getTechoPhoneNumber() != null) {
            search += " " + userDto.getTechoPhoneNumber();
        }
        if (userDto.getContactNumber() != null) {
            search += " " + userDto.getContactNumber();
        }
        userMaster.setSearchText(search);
        userMaster.setNoOfAttempts(userDto.getNoOfAttempts());
        userMaster.setRchInstitutionId(userDto.getRchInstitutionId());
        userMaster.setInfrastructureId(userDto.getInfrastructureId());
        userMaster.setReportPreferredLanguage(userDto.getReportPreferredLanguage());
        userMaster.setMemberId(userDto.getMemberId());
        userMaster.setLocationId(userDto.getLocationId());
        userMaster.setPinCode(userDto.getPinCode());
        userMaster.setLatitude(userDto.getLatitude());
        userMaster.setLongitude(userDto.getLongitude());
        userMaster.setPin(userDto.getPin());
        return userMaster;
    }

    /**
     * Converts user modal to user dto
     * @param user An instance of UserMaster
     * @return An instance of UserMasterDto
     */
    public static UserMasterDto convertUserMasterToDto(UserMaster user) {

        UserMasterDto userMasterDto = new UserMasterDto();
        userMasterDto.setAadharNumber(user.getAadharNumber());
        userMasterDto.setAddress(user.getAddress());
        userMasterDto.setCode(user.getCode());
        userMasterDto.setContactNumber(user.getContactNumber());
        userMasterDto.setDateOfBirth(user.getDateOfBirth());
        userMasterDto.setEmailId(user.getEmailId());
        userMasterDto.setFirstName(user.getFirstName());
        userMasterDto.setMiddleName(user.getMiddleName());
        userMasterDto.setLastName(user.getLastName());
        userMasterDto.setPrefferedLanguage(user.getPrefferedLanguage());
        userMasterDto.setRoleId(user.getRoleId());
        userMasterDto.setState(user.getState() == null ? UserMaster.State.ACTIVE : user.getState());
        userMasterDto.setUserName(user.getUserName());
        userMasterDto.setFullName(user.getFirstName() + " " + user.getLastName());
        userMasterDto.setId(user.getId());
        userMasterDto.setPassword(user.getPassword());
        userMasterDto.setImeiNumber(user.getImeiNumber());
        userMasterDto.setTechoPhoneNumber(user.getTechoPhoneNumber());
        userMasterDto.setTitle(user.getTitle());
        userMasterDto.setLoginCode(user.getLoginCode());
        if (user.getRole() != null) {
            userMasterDto.setRoleName(user.getRole().getName());
        }
        userMasterDto.setCreatedBy(user.getCreatedBy());
        userMasterDto.setCreatedOn(user.getCreatedOn());
        userMasterDto.setGender(user.getGender());
        userMasterDto.setNoOfAttempts(user.getNoOfAttempts());
        userMasterDto.setRchInstitutionId(user.getRchInstitutionId());
        userMasterDto.setInfrastructureId(user.getInfrastructureId());
        if (user.getReportPreferredLanguage() != null) {
            userMasterDto.setReportPreferredLanguage(user.getReportPreferredLanguage());
        }
        userMasterDto.setConvoxId(user.getConvoxId());
        userMasterDto.setMemberId(user.getMemberId());
        userMasterDto.setLocationId(user.getLocationId());
        userMasterDto.setLatitude(user.getLatitude());
        userMasterDto.setLongitude(user.getLongitude());
        userMasterDto.setPinCode(user.getPinCode());
        userMasterDto.setPin(user.getPin());
        return userMasterDto;
    }

    /**
     * Converts a list of user modal to user dto
     * @param userMasterList A list of UserMaster
     * @return A list of UserMasterDto
     */
    public static List<UserMasterDto> convertUserMasterListToDtoList(List<UserMaster> userMasterList) {
        LinkedList<UserMasterDto> userDtoList = new LinkedList<>();
        for (UserMaster userMaster : userMasterList) {
            userDtoList.push(convertUserMasterToDto(userMaster));
        }
        return userDtoList;
    }

}
