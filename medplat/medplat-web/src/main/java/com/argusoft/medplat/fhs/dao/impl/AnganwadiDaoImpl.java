/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fhs.constants.AnganwadiConstant;
import com.argusoft.medplat.fhs.dao.AnganwadiDao;
import com.argusoft.medplat.fhs.dto.AnganwadiDto;
import com.argusoft.medplat.fhs.model.Anganwadi;
import com.argusoft.medplat.mobile.dto.SurveyLocationMobDataBean;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in anganwadi dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class AnganwadiDaoImpl extends GenericDaoImpl<Anganwadi, Integer> implements AnganwadiDao {

    public static final String ANGANWADI_STATE_ACTIVE = "com.argusoft.medplat.anganwadi.state.active";
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Anganwadi> getAnganwadisByParentIdsList(List<Integer> parentIds) {
        String query = "select id as \"id\", \"name\" as \"name\", parent as \"parent\",\n" +
                "\"type\" as \"type\", state as \"state\", emamta_id as \"emamtaId\",\n" +
                "alias_name as \"aliasName\", icds_code as \"icdsCode\",\n" +
                "created_by as \"createdBy\", created_on as \"createdOn\",\n" +
                "modified_by as \"modifiedBy\", modified_on as \"modifiedOn\"\n" +
                "from anganwadi_master where state = :activeState and parent in :parentIds";
        NativeQuery<Anganwadi> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("activeState", ANGANWADI_STATE_ACTIVE)
                .setParameter("parentIds", parentIds)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.PARENT, StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.STATE, StandardBasicTypes.STRING)
                .addScalar("emamtaId", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ALIAS_NAME, StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ICDS_CODE, StandardBasicTypes.STRING)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("createdOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("modifiedBy", StandardBasicTypes.INTEGER)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);

        List<Anganwadi> resultList = nativeQuery.setResultTransformer(Transformers.aliasToBean(Anganwadi.class)).getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnganwadiDto> getAnganwadisByUserId(Integer userId, Integer locationId, Integer limit, Integer offset) {
        String query = "select ang_det.id,ang_det.name,string_agg(location_master.name,'> ' order by lhcd.depth desc) as location,ang_det.alias_name as \"aliasName\",ang_det.icds_code as \"icdsCode\", "
                + "ang_det.state "
                + "from "
                + "(select am.id,am.name,am.state,alias_name ,icds_code,am.parent from anganwadi_master am,location_hierchy_closer_det lhcd "
                + "where am.parent = lhcd.child_id "
                + "and ( "
                + "( " + locationId + " is null and lhcd.parent_id in (select loc_id from um_user_location where user_id = " + userId + " and state = 'ACTIVE')) "
                + "or (" + locationId + " is not null and lhcd.parent_id = " + locationId + ")) "
                + "limit " + limit + " offset " + offset + ") as ang_det ,location_hierchy_closer_det lhcd,location_master "
                + "where lhcd.child_id = ang_det.parent and lhcd.parent_id = location_master.id "
                + "group by ang_det.id,ang_det.name,ang_det.alias_name,ang_det.icds_code,ang_det.state ";
        NativeQuery<AnganwadiDto> q = getCurrentSession().createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.STATE, StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ALIAS_NAME, StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ICDS_CODE, StandardBasicTypes.STRING)
                .addScalar("location", StandardBasicTypes.STRING);

        return q.setResultTransformer(Transformers.aliasToBean(AnganwadiDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(Integer id, Boolean isActive) {
        String state = Boolean.TRUE.equals(isActive) ? AnganwadiConstant.ANGANWADI_STATE_ACTIVE : AnganwadiConstant.ANGANWADI_STATE_INACTIVE;
        String query = "update anganwadi_master set state = :state where id = :id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter("id", id);
        q.setParameter(Anganwadi.Fields.STATE, state);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String fetchAnganwadiArea(Integer anganwadiId) {
        String query = "select id as \"id\", \"name\" as \"name\", parent as \"parent\",\n" +
                "\"type\" as \"type\", state as \"state\", emamta_id as \"emamtaId\",\n" +
                "alias_name as \"aliasName\", icds_code as \"icdsCode\"\n" +
                "from anganwadi_master where id = :anganwadiId";
        NativeQuery<Anganwadi> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("anganwadiId", anganwadiId)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.PARENT, StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.STATE, StandardBasicTypes.STRING)
                .addScalar("emamtaId", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ALIAS_NAME, StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.ICDS_CODE, StandardBasicTypes.STRING);
        Anganwadi anganwadi = nativeQuery.setResultTransformer(Transformers.aliasToBean(Anganwadi.class)).uniqueResult();
        return anganwadi.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SurveyLocationMobDataBean> retrieveAllAnganwadiForMobileByUserId(Integer userId) {
        String query = "select id, \n"
                + "case when alias_name is not null then alias_name else name end as name, \n"
                + "parent, \n"
                + "case when state = 'com.argusoft.medplat.anganwadi.state.active' then true else false end as \"isActive\", \n"
                + "case when type = null then 8 else 8 end as level \n"
                + "from anganwadi_master \n"
                + "where state = 'com.argusoft.medplat.anganwadi.state.active' \n"
                + "and parent in ( \n"
                + "select child_id from location_hierchy_closer_det where parent_id in (\n"
                + "select parent_id from location_hierchy_closer_det lhcd inner join \n"
                + "(select loc_id from um_user_location where user_id = :userId) r1 \n"
                + "on r1.loc_id = lhcd.child_id where lhcd.parent_loc_type in ('P','U')) and child_loc_type in ('ANG','V')\n"
                + ")";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<SurveyLocationMobDataBean> q = session.createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar(Anganwadi.Fields.PARENT, StandardBasicTypes.INTEGER)
                .addScalar("isActive", StandardBasicTypes.BOOLEAN)
                .addScalar("level", StandardBasicTypes.INTEGER);
        q.setParameter("userId", userId);

        return q.setResultTransformer(Transformers.aliasToBean(SurveyLocationMobDataBean.class)).list();
    }
}
