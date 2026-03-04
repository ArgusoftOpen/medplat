package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.UserFormAccessDao;
import com.argusoft.medplat.mobile.dto.UserFormAccessBean;
import com.argusoft.medplat.mobile.model.UserFormAccess;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author avani
 */
@Repository
public class UserFormAccessDaoImpl extends GenericDaoImpl<UserFormAccess, Integer> implements UserFormAccessDao {

    /**
     * Find All form with given user id
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserFormAccessBean> getUserFormAccessDetail(int userId) {
        Session session = sessionFactory.getCurrentSession();

        String sql = "select form_key as formCode, form, is_training_req as isTrainingReq, state from listvalue_form_master form "
                + "left join user_form_access usr on form.form_key = usr.form_code and usr.user_id = :userId";

        NativeQuery<UserFormAccessBean> query = session.createNativeQuery(sql);
        query.setParameter("userId", userId);

        return query
                .addScalar("formCode", StandardBasicTypes.STRING)
                .addScalar("form", StandardBasicTypes.STRING)
                .addScalar("isTrainingReq", StandardBasicTypes.BOOLEAN)
                .addScalar("state", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(UserFormAccessBean.class))
                .list();
    }

    @Override
    public void updateUserFormAccess() {
        String query = "INSERT INTO user_form_access(user_id, form_code, state, created_on)\n"
                + "    select user_id,'FHS','MOVE_TO_PRODUCTION',created_on from (SELECT user_id,created_on \n"
                + "    FROM dblink('dbname=techo_t','select user_id,created_on from user_form_access where state = ''MOVE_TO_PRODUCTION''')\n"
                + "    AS user_id(user_id bigint,\n"
                + "    created_on timestamp without time zone\n"
                + "    )) t where t.user_id not in (select user_id from user_form_access where state = 'MOVE_TO_PRODUCTION')";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    public boolean checkIfUserFormAccessAlreadyExists(Integer userId, String formCode) {
        Session session = sessionFactory.getCurrentSession();

        String query = "select user_id as \"userId\" from user_form_access where user_id = :userId and form_code = :formCode";
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameter("userId", userId);
        q.setParameter("formCode", formCode);
        List<Integer> result = q.addScalar("userId", StandardBasicTypes.INTEGER).list();
        return result != null && !result.isEmpty();
    }
}
