
package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.config.security.MobileUser;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dao.UserTokenDao;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserToken;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 *     Implements methods of UserTokenDao
 * </p>
 * @author prateek
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class UserTokenDaoImpl extends GenericDaoImpl<UserToken, Integer> implements UserTokenDao {

    @Autowired
    private MobileUser mobileUser;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken retrieveByUserToken(String token) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserToken> cq = cb.createQuery(UserToken.class);
        Root<UserToken> root = cq.from(UserToken.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(UserToken.Fields.USER_TOKEN), token),
                        cb.equal(root.get(UserToken.Fields.IS_ACTIVE), Boolean.TRUE)
                )
        );

        List<UserToken> userTokenList = session.createQuery(cq).getResultList();
        if (userTokenList != null && !userTokenList.isEmpty()) {
            UserToken userToken = userTokenList.get(0);
            mobileUser.setId(userToken.getUserId());
            return userToken;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken retrieveByUserId(Integer userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserToken> cq = cb.createQuery(UserToken.class);
        Root<UserToken> root = cq.from(UserToken.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal(root.get(UserToken.Fields.USER_ID), userId),
                        cb.equal(root.get(UserToken.Fields.IS_ACTIVE), Boolean.TRUE)
                )
        );

        List<UserToken> userTokenList = session.createQuery(cq).getResultList();
        if (userTokenList != null && !userTokenList.isEmpty()) {
            UserToken userToken = userTokenList.get(0);
            mobileUser.setId(userToken.getUserId());
            return userToken;
        } else {
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserToken retrieveByUserToken(String token, boolean allTokens) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserToken> cq = cb.createQuery(UserToken.class);
        Root<UserToken> root = cq.from(UserToken.class);

        cq.select(root);
        if (!allTokens) {
            cq.where(
                    cb.and(
                            cb.equal(root.get(UserToken.Fields.USER_TOKEN), token),
                            cb.equal(root.get(UserToken.Fields.IS_ACTIVE), Boolean.TRUE)
                    )
            );
        } else {
            cq.where(cb.equal(root.get(UserToken.Fields.USER_TOKEN), token));
        }

        List<UserToken> userTokenList = session.createQuery(cq).getResultList();
        if (userTokenList != null && !userTokenList.isEmpty()) {
            UserToken userToken = userTokenList.get(0);
            mobileUser.setId(userToken.getUserId());
            return userToken;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenDto retrieveDtoByUserToken(String token) {
        String query = "select user_id as userId, user_token as userToken, role_id as roleId from user_token u inner join um_user um  "
                + "on u.user_id = um.id where u.user_token = :userToken ";

        Session session = sessionFactory.getCurrentSession();

        NativeQuery<UserTokenDto> q = session.createNativeQuery(query).addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("userToken", StandardBasicTypes.STRING)
                .addScalar("roleId", StandardBasicTypes.INTEGER);

        q.setParameter("userToken", token);

        return q.setResultTransformer(Transformers.aliasToBean(UserTokenDto.class)).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivateAllActiveUserTokens(Integer userId) {
        String query = " update user_token set is_active = false , modified_on = now() where user_id = :userId and is_active ";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

}
