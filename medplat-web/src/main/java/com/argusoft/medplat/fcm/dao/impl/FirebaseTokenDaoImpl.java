package com.argusoft.medplat.fcm.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fcm.dao.FirebaseTokenDao;
import com.argusoft.medplat.fcm.model.FirebaseTokenEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class FirebaseTokenDaoImpl extends GenericDaoImpl<FirebaseTokenEntity, Integer> implements FirebaseTokenDao {

    @Override
    public boolean isAddedFirebaseToken(int userId, String firebaseToken) {
        if (0 == userId && firebaseToken == null) {
            return false;
        }
        PredicateBuilder<FirebaseTokenEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("userId"), userId));
            predicates.add(builder.equal(root.get("token"), firebaseToken));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder) != null;
    }

    @Override
    public List<FirebaseTokenEntity> retrieveByUserId(int userId) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("userId", userId));
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }
}