/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.dto;

/**
*
* <p> User basket preference dto </p>
* @author kunjan
* @since 26/08/20 11:30 AM
*
*/

public class UserBasketPreferenceDto {

    private Integer id;
    private Integer userId;
    private String preference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserBasketPreferenceDto{" + "id=" + id + ", userId=" + userId + ", preference=" + preference + '}';
    }
}
