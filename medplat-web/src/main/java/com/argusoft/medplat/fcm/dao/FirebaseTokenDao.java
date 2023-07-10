package com.argusoft.medplat.fcm.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.fcm.model.FirebaseTokenEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FirebaseTokenDao extends GenericDao<FirebaseTokenEntity, Integer> {

    boolean isAddedFirebaseToken(int userId, String firebaseToken);
    List<FirebaseTokenEntity> retrieveByUserId(int userId);
}