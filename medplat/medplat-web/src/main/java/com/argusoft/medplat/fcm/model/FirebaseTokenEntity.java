package com.argusoft.medplat.fcm.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "firebase_token")
public class FirebaseTokenEntity extends EntityAuditInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "token")
    private String token;

    public FirebaseTokenEntity() {
    }

    public FirebaseTokenEntity(Integer userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "FirebaseTokenEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }

}
