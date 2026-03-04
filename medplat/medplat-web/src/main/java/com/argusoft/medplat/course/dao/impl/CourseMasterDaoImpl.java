/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.web.users.dto.RoleMasterDto;
import com.argusoft.medplat.course.dao.CourseMasterDao;
import com.argusoft.medplat.course.dto.CourseMasterDto;
import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.CourseDataBean;
import com.argusoft.medplat.mobile.dto.SurveyLocationMobDataBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author akshar
 */
@Repository("courseMasterDao")
public class CourseMasterDaoImpl extends GenericDaoImpl<CourseMaster, Integer> implements CourseMasterDao {

    @Override
    public List<CourseMaster> getCoursesbyRoleIds(List<Integer> roleIds) {
        String query = "select t.course_id as \"courseId\",t.course_name as \"courseName\",t.course_type as \"courseType\" from (select count(*),cm.course_id,cm.course_name,cm.course_type from tr_course_master cm "
                + "inner join tr_course_role_rel cr "
                + "on cr.course_id = cm.course_id "
                + "where cr.role_id in (:roleId) and cm.course_state = :active "
                + "group by cm.course_id "
                + "having count(*) = :courseLength order by cm.created_on desc) t";

        Properties params = new Properties();
        params.put("enumClass", CourseMaster.CourseType.class.getName());
        params.put("type", "12");

        Type myEnumType = sessionFactory.getTypeHelper().custom(EnumType.class, params);

        NativeQuery<CourseMaster> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("roleId", roleIds)
                .setParameter("active", CourseMaster.State.ACTIVE.toString())
                .setParameter("courseLength", roleIds.size())
                .addScalar("courseId", StandardBasicTypes.INTEGER)
                .addScalar("courseType", myEnumType)
                .addScalar("courseName", StandardBasicTypes.STRING);

        List<CourseMaster> resultList = nativeQuery.setResultTransformer(Transformers.aliasToBean(CourseMaster.class)).getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        return resultList;

    }

    @Override
    public void toggleActive(Integer courseId, Boolean isActive) {
        String state = isActive ? CourseMaster.State.ACTIVE.toString() : CourseMaster.State.INACTIVE.toString();
        String query = "update tr_course_master set course_state = :state where course_id = :id ";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter("state", state)
                .setParameter("id", courseId);
        q.executeUpdate();
    }

    @Override
    public List<RoleMasterDto> getRolesByCourse(Integer courseId) {
        String query = "select id,name from um_role_master inner join tr_course_role_rel "
                + "on id = role_id where course_id = :courseId";

        NativeQuery<RoleMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("courseId", courseId)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(RoleMasterDto.class)).list();
    }

    @Override
    public List<RoleMasterDto> getAllRoles(){
        String query = "select distinct name,id from um_role_master inner join tr_course_role_rel "
                + "on id = role_id" + " where state = 'ACTIVE'";

        NativeQuery<RoleMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(RoleMasterDto.class)).list();
    }

    @Override
    public List<RoleMasterDto> getTrainerRolesByCourse(Integer courseId) {
        String query = "select id,name from um_role_master inner join tr_course_trainer_role_rel "
                + "on id = trainer_role_id where course_id = :courseId";

        NativeQuery<RoleMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("courseId", courseId)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(RoleMasterDto.class)).list();

    }

    @Override
    public List<CourseMasterDto> getCoursesByModuleIds(List<Integer> moduleIds) {
        String query = "select course_id as \"courseId\",course_name as \"courseName\"  from tr_course_master  where module_id in (:moduleIds) and course_state='ACTIVE'";

        NativeQuery<CourseMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("moduleIds", moduleIds)
                .addScalar("courseId", StandardBasicTypes.INTEGER)
                .addScalar("courseName", StandardBasicTypes.STRING);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(CourseMaster.class)).list();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDataBean> getCourseDataBeansByUserId(Integer userId) {
        String query = "select distinct tcm.course_id as \"courseId\", tcm.course_name as \"courseName\", " +
                "tcm.course_description as \"courseDescription\", tcm.course_type as \"courseType\", " +
                "tcm.course_state as \"courseState\", " +
                "tr.effective_date as \"scheduleDate\", " +
                "tcm.test_config_json as \"testConfigJsonString\", " +
                "tcm.course_image_json as \"courseImageJsonString\", " +
                "tcm.is_allowed_to_skip_lessons as \"isAllowedToSkipLessons\" " +
                "from tr_course_master tcm " +
                "inner join tr_topic_coverage_master tc on tc.course_id = tcm.course_id " +
                "inner join tr_training_master tr on tr.training_id = tc.training_id " +
                "left join tr_training_attendee_rel att on att.training_id = tr.training_id " +
                "left join tr_training_additional_attendee_rel ttaar on ttaar.training_id = tr.training_id " +
                "where tcm.course_state = 'ACTIVE' " +
                "and ( att.attendee_id = :userId or ttaar.additional_attendee_id = :userId ) " +
                "and tr.training_state != 'SUBMITTED'" +
                "and tr.created_on < now() " +
                "and tcm.course_type  = 'ONLINE'";

        NativeQuery<CourseDataBean> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("userId", userId)
                .addScalar("courseId", StandardBasicTypes.INTEGER)
                .addScalar("courseName", StandardBasicTypes.STRING)
                .addScalar("courseDescription", StandardBasicTypes.STRING)
                .addScalar("scheduleDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("courseType", StandardBasicTypes.STRING)
                .addScalar("courseState", StandardBasicTypes.STRING)
                .addScalar("testConfigJsonString", StandardBasicTypes.STRING)
                .addScalar("courseImageJsonString", StandardBasicTypes.STRING)
                .addScalar("isAllowedToSkipLessons", StandardBasicTypes.BOOLEAN);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(CourseDataBean.class)).list();


    }

}
