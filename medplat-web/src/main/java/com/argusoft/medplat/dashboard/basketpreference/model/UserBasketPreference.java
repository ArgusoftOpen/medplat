/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.basketpreference.model;

import javax.persistence.*;

/**
 * <p>Database fields related to user basket preference</p>
 *
 * @author kunjan
 * @since 26/08/20 12:30 PM
 */
@Entity
@Table(name = "user_basket_preference")
public class UserBasketPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "preference")
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

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String USERID = "userId";
        public static final String PREFERENCE = "preference";
    }

    @Override
    public String toString() {
        return "UserBasketPreference{" + "id=" + id + ", userId=" + userId + ", preference=" + preference + '}';
    }
}
