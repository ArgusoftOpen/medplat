/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoResponseEntity;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.dto.UserPasswordDto;
import com.argusoft.medplat.web.users.service.UserService;
import com.argusoft.medplat.web.users.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *<p>Defines rest end points for user management</p>
 * @author vaishali
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @Autowired
//    MyTechoUserService myTechoUserService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ImtechoSecurityUser user;

    /**
     * Create or update user
     * @param userDto An instance of UserMasterDto
     */
    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody UserMasterDto userDto) {
        userService.createOrUpdate(userDto);
    }

    /**
     * Returns user of given id
     * @param id An id of user
     * @return An instance of UserMasterDto
     */
    @GetMapping(value = "/{id}")
    public UserMasterDto retrieveById(@PathVariable() Integer id) {
        return userService.retrieveById(id);
    }

    /**
     * Returns loggedIn user object
     * @return An  instance of UserMasterDto
     */
    @GetMapping(value = "/details")
    public UserMasterDto getdrUser() {
        return userService.retrieveById(user.getId());
    }

    /**
     * Returns users of given ids
     * @param ids A set of ids of users
     * @return A list of UserMasterDto
     */
    @GetMapping(value = "/ids")
    public List<UserMasterDto> retrieveUsersByIds(@RequestParam("userIds") Set<Integer> ids) {
        return userService.getUsersByIds(ids);
    }

    /**
     * Updates state of given user
     * @param userDto An instance of user
     * @param isActive A boolean value for state of user
     */
    @PutMapping(value = "")
    public void toggleActive(@RequestBody UserMasterDto userDto, @RequestParam("is_active") Boolean isActive) {
        userService.toggleActive(userDto, isActive);
    }

    /**
     * Returns all users.
     * @param isActive A boolean value for state of user
     * @return A list of UserMasterDto
     */
    @GetMapping(value = "")
    public List<UserMasterDto> retrieveAll(@RequestParam(name = "is_active", required = false) Boolean isActive) {
        return userService.retrieveAll(isActive);

    }

    /**
     * Remove all token and logout user from system
     * @param userName A username of user
     * @param clientId string clientId
     */
    @GetMapping(value = "/logout")
    public void logout(@RequestParam("userName")String userName,@RequestParam("clientId")String clientId){
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, userName);
        if (tokens != null) {
            for (OAuth2AccessToken token : tokens) {
                tokenStore.removeRefreshToken(token.getRefreshToken());
                tokenStore.removeAccessToken(token);
            }
        }

    }


    /**
     * Checks username is available or not
     * @param username A username of user
     * @param userId An id of user
     * @return A boolean value for username is available or not
     */
    @GetMapping(value = "/checkusername")
    public boolean isUsernameAvailable(@RequestParam("username") String username, @RequestParam(name = "user_id", required = false) Integer userId) {
        return userService.validateUserName(username, userId);
    }

    /**
     * Returns available username based on given username
     * @param givenUsername A value for username
     * @return A string of available username
     */
    @GetMapping(value = "/availableUsername")
    public String fetchAvailableUsername(@RequestParam("username") String givenUsername) {
        return userService.fetchAvailableUsername(givenUsername);
    }

    /**
     * Returns users based on criteria
     * @param roleId An id of role
     * @param locationId An id of location
     * @param searchString A string value for search
     * @param orderBy A string value for order by field
     * @param order A value of order it can be asc or desc
     * @param limit A value for limit
     * @param offset A value for offset
     * @return A list of UserMasterDto
     */
    @GetMapping(value = "/retrievebycriteria")
    public List<UserMasterDto> retrieveByCriteria(@RequestParam(name = "roleid", required = false) Integer roleId,
            @RequestParam(name = "locationId", required = false) Integer locationId,
            @RequestParam(name = "searchString", required = false) String searchString,
            @RequestParam(name = "orderby", required = false) String orderBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "status", required = false) String status) {
        return userService.retrieveByCriteria(user.getId(), roleId, locationId, searchString, orderBy, limit, offset, order,status);

    }

    /**
     * Verifies password of user
     * @param userPasswordDto An instance of UserPasswordDto
     * @return boolean value regarding password is valid or not
     */
    @GetMapping(value = "/verifypassword")
    public Boolean verifyOldPassword(@RequestBody UserPasswordDto userPasswordDto) {
        return userService.verifyPassword(userPasswordDto.getOldPassword(), userPasswordDto.getUserId());
    }

    /**
     * Changes password of user
     * @param userPasswordDto An instance of UserPasswordDto
     */
    @PutMapping(value = "/changepassword")
    public void changePassword(@RequestBody UserPasswordDto userPasswordDto) {
        userService.changePassword(userPasswordDto.getOldPassword(), userPasswordDto.getUserId());
    }

    /**
     * Changes old password of user with new password
     * @param userPasswordDto An instance of UserPasswordDto
     */
    @PutMapping(value = "/selfchangepassword")
    public void changePasswordOldtoNew(@RequestBody UserPasswordDto userPasswordDto) {
        userService.changePasswordOldtoNew(userPasswordDto.getOldPassword(), userPasswordDto.getNewPassword());
    }

    /**
     * Generates otp in case of forgot password
     * @param userPasswordDto An instance of UserPasswordDto
     */
    @PutMapping(value = "/forgotpassword/generateotp")
    public void validateAndGenerateOtp(@RequestBody UserPasswordDto userPasswordDto) {
        userService.validateAndGenerateOtp(userPasswordDto.getUsername());
    }

    /**
     * Verifies otp
     * @param userPasswordDto An instance of UserPasswordDto
     */
    @PutMapping(value = "/forgotpassword/verifyotp")
    public void verifyOtp(@RequestBody UserPasswordDto userPasswordDto) {
        userService.verifyOtp(userPasswordDto.getUsername(), userPasswordDto.getOtp(), userPasswordDto.getNoOfAttempts());
    }

    /**
     * Resets password of user
     * @param userPasswordDto An instance of UserPasswordDto
     */
    @PutMapping(value = "/forgotpassword/resetpassword")
    public void resetPassword(@RequestBody UserPasswordDto userPasswordDto) {
        userService.resetPassword(userPasswordDto.getUsername(), userPasswordDto.getOtp(), userPasswordDto.getPassword());
    }

    /**
     * Checks if user exists with same aadhar number or phone number
     * @param aadharNumber An aadhar number to be check
     * @param phoneNumber A phone number to be check
     * @param userId An id of user
     * @return A boolean value regarding user with same aadhar number or phone number exists or not
     */
    @GetMapping(value = "/checkaadharorphone")
    public boolean checkAadharOrPhone(@RequestParam(value = "aadhar", required = false) String aadharNumber,
            @RequestParam(value = "phone", required = false) String phoneNumber,
            @RequestParam(value = "userId", required = false) Integer userId) {
        return userService.checkIfUserExistsWithSameAadharOrPhone(aadharNumber, phoneNumber, userId, null);
    }

    /**
     * Validates area of interventions for given userId
     * @param userId An id of user
     * @param roleId An id of role
     * @param locationIds Ids of location
     * @param toBeAdded Ids of locations to be added
     * @return ImtechoResponseEntity with data object and string message
     *         regarding locations to be added is valid or not
     */
    @GetMapping(value = "/validateaoi")
    public ImtechoResponseEntity validateAOI(@RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "locationIds", required = false) List<Integer> locationIds,
            @RequestParam(value = "toBeAdded", required = false) Integer toBeAdded) {
        return userService.validateAOI(userId, roleId, locationIds, toBeAdded);
    }

    /**
     * Returns all active users
     * @return A list of UserMasterDto
     */
    @GetMapping(value = "/all")
    public List<UserMasterDto> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    /**
     * Validates health infrastructure for user
     * @param healthInfraIds A list of health infrastructure Ids
     * @param toBeRemoved A id of health infra to be remove
     * @return ImtechoResponseEntity An ImtechoResponseEntity with data object and string message
     *         regarding health infra to be remove is valid or not
     */
    @GetMapping(value = "/validateHealthInfra")
    public ImtechoResponseEntity validateHealthInfra(
            @RequestParam(value = "healthInfraIds", required = false) List<Integer> healthInfraIds,
            @RequestParam(value = "toBeRemoved", required = false) Integer toBeRemoved) {
        return userService.validateHealthInfra(toBeRemoved, healthInfraIds);
    }

    /**
     * Generates login code for given userId
     * @param userId An id of user
     * @return A string of generated login code
     */
    @GetMapping(value = "/generateLoginCode")
    public String generateLoginCode(
            @RequestParam(value = "userId", required = true) Integer userId) {
        return userService.generateLoginCode(userId);
    }
}