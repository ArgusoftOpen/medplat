/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao.impl;


import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.LocationMasterDataBean;
import com.argusoft.medplat.mobile.dto.SurveyLocationMobDataBean;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.dto.LocationElasticHierarchyDto;
import com.argusoft.medplat.web.location.model.LocationHierchyCloserDetail;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import com.argusoft.medplat.web.users.model.UserLocation;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementation of methods define in location master dao.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class LocarionMasterDaoImpl extends GenericDaoImpl<LocationMaster, Integer> implements LocationMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveUserLocationByLevel(Integer userId, Integer level) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationMaster> criteria = criteriaBuilder.createQuery(LocationMaster.class);
        Root<LocationMaster> root = criteria.from(LocationMaster.class);

        Subquery<UserLocation> subquery = criteria.subquery(UserLocation.class);
        Root<UserLocation> subqueryForm = subquery.from(UserLocation.class);
        Join<LocationMaster, UserLocation> join = subqueryForm.join(UserLocation.Fields.LOCATION_MASTER, JoinType.INNER);
        Predicate eqUserId = criteriaBuilder.equal(subqueryForm.get(UserLocation.Fields.USER_ID), userId);
        Predicate eqStatus = criteriaBuilder.equal(subqueryForm.get(UserLocation.Fields.STATE), UserLocation.State.ACTIVE);
        subquery.where(criteriaBuilder.and(eqUserId, eqStatus));
        subquery.select(join.get(LocationMaster.Fields.ID)).distinct(true);

        Subquery<LocationTypeMaster> locationTypeMasterSubquery = criteria.subquery(LocationTypeMaster.class);
        Root<LocationTypeMaster> locationTypeMasterSubqueryRoot = locationTypeMasterSubquery.from(LocationTypeMaster.class);
        Predicate eqParentId = criteriaBuilder.equal(locationTypeMasterSubqueryRoot.get("level"), level);
        locationTypeMasterSubquery.select(locationTypeMasterSubqueryRoot.get("type")).where(eqParentId);

        Subquery<LocationHierchyCloserDetail> locationHierchyCloserDetailSubquery = criteria.subquery((LocationHierchyCloserDetail.class));
        Root<LocationHierchyCloserDetail> locationHierchyCloserDetailRoot = locationHierchyCloserDetailSubquery.from(LocationHierchyCloserDetail.class);
        Predicate parentIdIn = criteriaBuilder.in(locationHierchyCloserDetailRoot.get("parentId")).value(subquery);
        Predicate childLocTypeIn = criteriaBuilder.in(locationHierchyCloserDetailRoot.get("childLocationType")).value(locationTypeMasterSubquery);
        locationHierchyCloserDetailSubquery.select(locationHierchyCloserDetailRoot.get("childId")).where(parentIdIn, childLocTypeIn);

        root.fetch(LocationMaster.Fields.HIERARCHY_TYPE, JoinType.INNER);
        Predicate eqState = criteriaBuilder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE);
        Predicate propertyIn = criteriaBuilder.in(root.get(LocationMaster.Fields.ID)).value(locationHierchyCloserDetailSubquery);
        criteria.orderBy(criteriaBuilder.asc(root.get(LocationMaster.Fields.NAME)));
        criteria.select(root).where(eqState, propertyIn);
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationDetailDto> retrieveUserLocationByParentLocation(Integer userId, List<Integer> locationIds, Integer level, String languagePreference) {
        String query = "with loc_det as(\n"
                + "select lh.child_id,lt.\"name\" as loc_type\n"
                + "from um_user_location um_loc,location_hierchy_closer_det lh,location_type_master lt\n"
                + ",location_hierchy_closer_det lh1\n"
                + "where um_loc.user_id = :userId\n"
                + "and um_loc.state = 'ACTIVE'\n"
                + "and lh.parent_id = um_loc.loc_id and lh.child_loc_type = lt.\"type\"\n"
                + "and lt.level = :level\n"
                + "and lh1.parent_id in (:locationId) and lh.child_loc_type = lt.\"type\" \n"
                + "and lh1.child_id = lh.child_id\n"
                + ")\n"
                + "select location_master.id as id\n"
                + ",case when 'EN' = :prefLanguage then location_master.english_name else location_master.name end as name\n"
                + ",location_master.type\n"
                + ",loc_det.loc_type as \"locType\"\n"
                + "from loc_det,location_master \n"
                + "where loc_det.child_id = location_master.id order by name;";

        NativeQuery<LocationDetailDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationIds);
        sQLQuery.setParameter("userId", userId);
        sQLQuery.setParameter("level", level);
        sQLQuery.setParameter("prefLanguage", languagePreference);
        sQLQuery.setResultTransformer(Transformers.aliasToBean(LocationDetailDto.class));
        sQLQuery.addScalar(LocationMaster.Fields.ID, StandardBasicTypes.INTEGER);
        sQLQuery.addScalar("name", StandardBasicTypes.STRING);
        sQLQuery.addScalar("type", StandardBasicTypes.STRING);
        sQLQuery.addScalar("locType", StandardBasicTypes.STRING);
        return sQLQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveLocationByParentLocation(Integer locationId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationHierchyCloserDetail> criteria = criteriaBuilder.createQuery(LocationHierchyCloserDetail.class);
        Root<LocationHierchyCloserDetail> root = criteria.from(LocationHierchyCloserDetail.class);
        Predicate equalParentId = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.PARENT_ID), locationId);
        Predicate depth = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.DEPTH), 1);
        root.fetch(LocationHierchyCloserDetail.Fields.CHILD_LOCATION_DETAIL, JoinType.INNER);
        Predicate status = criteriaBuilder.equal(root.get(LocationHierchyCloserDetail.Fields.CHILD_LOCATION_DETAIL).get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE);
        criteria.orderBy(criteriaBuilder.asc(root.get(LocationHierchyCloserDetail.Fields.CHILD_LOCATION_DETAIL).get(LocationMaster.Fields.NAME)));
        criteria.select(root).where(criteriaBuilder.and(equalParentId, depth, status));
        return session.createQuery(criteria).list().stream().map(LocationHierchyCloserDetail::getChildLocationDetail).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveLocationByLevel(Integer level) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationMaster> criteria = criteriaBuilder.createQuery(LocationMaster.class);
        Root<LocationMaster> root = criteria.from(LocationMaster.class);

        Subquery<LocationTypeMaster> subquery = criteria.subquery(LocationTypeMaster.class);
        Root<LocationTypeMaster> subqueryForm = subquery.from(LocationTypeMaster.class);
        Predicate levelEq = criteriaBuilder.equal(subqueryForm.get("level"), level);
        subquery.select(subqueryForm.get("type")).where(levelEq);

        root.fetch(LocationMaster.Fields.HIERARCHY_TYPE, JoinType.INNER);
        Predicate eqState = criteriaBuilder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE);
        Predicate propertyIn = criteriaBuilder.in(root.get(LocationMaster.Fields.TYPE)).value(subquery);
        criteria.orderBy(criteriaBuilder.asc(root.get(LocationMaster.Fields.NAME)));
        criteria.select(root).where(eqState, propertyIn);
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveLocationsByIdList(List<Integer> locationList) {
        PredicateBuilder<LocationMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (locationList != null && !locationList.isEmpty()) {
                predicates.add(builder.in(root.get(LocationMaster.Fields.ID)).value(locationList));
            }
            predicates.add(builder.equal(root.get(LocationMaster.Fields.IS_ARCHIVE), Boolean.FALSE));
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getLocationsByIds(List<Integer> locationIds) {
        PredicateBuilder<LocationMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(LocationMaster.Fields.ID)).value(locationIds));
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllLocationIdsByParentList(List<Integer> parent) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<LocationMaster> root = criteria.from(LocationMaster.class);
        Predicate eqState = criteriaBuilder.equal(root.get(LocationMaster.Fields.IS_ARCHIVE), Boolean.FALSE);
        Predicate getIds = criteriaBuilder.in(root.get(LocationMaster.Fields.PARENT)).value(parent);
        criteria.select(root.get(LocationMaster.Fields.ID)).where(getIds, eqState);
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getLocationsByLocationType(String locationLevel, Boolean isActive) {
        PredicateBuilder<LocationMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (locationLevel != null) {
                predicates.add(builder.equal(root.get(LocationMaster.Fields.TYPE), locationLevel));
            }
            if (isActive != null) {
                predicates.add(builder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE));
            }
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getLocationsByLocationType(List<String> locationLevel, Boolean isActive) {
        PredicateBuilder<LocationMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (locationLevel != null && !locationLevel.isEmpty()) {
                predicates.add(builder.in(root.get(LocationMaster.Fields.TYPE)).value(locationLevel));
            }
            if (isActive != null) {
                predicates.add(builder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE));
            }
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getLocationsByNameAndParent(String name, String type, LocationMaster parent) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationMaster> criteria = criteriaBuilder.createQuery(LocationMaster.class);
        Root<LocationMaster> root = criteria.from(LocationMaster.class);
        List<Predicate> predicates = new ArrayList<>();
        if (type != null) {
            Predicate eqLocationId = criteriaBuilder.equal(root.get(LocationMaster.Fields.TYPE), type);
            predicates.add(eqLocationId);
        }
        if (parent != null) {
            Predicate eqStatus = criteriaBuilder.equal(root.get(LocationMaster.Fields.PARENT_MASTER), parent);
            predicates.add(eqStatus);
        }
        Predicate isArchive = criteriaBuilder.equal(root.get(LocationMaster.Fields.IS_ARCHIVE), Boolean.FALSE);
        predicates.add(isArchive);
        Predicate eqName = criteriaBuilder.equal(root.get(LocationMaster.Fields.NAME), name);
        predicates.add(eqName);
        criteria.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveLocationsByParentIdAndType(List<Integer> parentIds, List<String> locationTypes) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<LocationMaster> criteria = criteriaBuilder.createQuery(LocationMaster.class);
        Root<LocationMaster> root = criteria.from(LocationMaster.class);

        Subquery<LocationHierchyCloserDetail> subquery = criteria.subquery(LocationHierchyCloserDetail.class);
        Root<LocationHierchyCloserDetail> subqueryForm = subquery.from(LocationHierchyCloserDetail.class);
        subquery.select(subqueryForm.get(LocationHierchyCloserDetail.Fields.CHILD_ID));
        subquery.where(criteriaBuilder.in(subqueryForm.get(LocationHierchyCloserDetail.Fields.PARENT_ID)).value(parentIds), criteriaBuilder.in(subqueryForm.get(LocationHierchyCloserDetail.Fields.CHILD_LOCATION_TYPE)).value(locationTypes));

        Predicate eqState = criteriaBuilder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE);
        Predicate propertyIn = criteriaBuilder.in(root.get(LocationMaster.Fields.ID)).value(subquery);
        criteria.select(root).where(eqState, propertyIn);
        return session.createQuery(criteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMasterDataBean> retrieveAllActiveLocationsWithWorkerInfo(Date lastUpdatedOn) {
        String query;
        if (lastUpdatedOn != null) {
            query = "with changed_loc_det as(\n"
                    + "select id,name,parent,type,case when modified_on is not null then modified_on else created_on end as \"modifiedOn\" \n"
                    + "from location_master l where modified_on >= :lastUpdatedOn \n"
                    + "),loc_user_detail as (\n"
                    + "select ul.loc_id,CAST(json_agg(json_build_object('name',CONCAT(u.first_name,' ',u.last_name),'mobileNumber',u.contact_number)) as text) \n"
                    + "as fhw_names \n"
                    + "from um_user u,um_user_location ul,changed_loc_det c_loc\n"
                    + "where u.id = ul.user_id and u.role_id in (30,24) and ul.state = 'ACTIVE' and u.state = 'ACTIVE' \n"
                    + "and ul.loc_id = c_loc.id\n"
                    + "group by ul.loc_id\n"
                    + ")\n"
                    + "select id as \"actualID\", name, parent, type, fhw_names as \"fhwDetailString\",\"modifiedOn\" \n"
                    + "from changed_loc_det t\n"
                    + "left join loc_user_detail t1 on t.id = t1.loc_id;";
        } else {
            query = "select id as \"actualID\", name, parent, type, fhw_names as \"fhwDetailString\",\"modifiedOn\" from ((select id,name,parent,type, "
                    + "case when modified_on is not null then modified_on else created_on end as \"modifiedOn\" from location_master l "
                    + "where l.state = 'ACTIVE') "
                    + "as t left join \n"
                    + "(select ul.loc_id,CAST(json_agg(json_build_object('name',CONCAT(u.first_name,' ',u.last_name),'mobileNumber',u.contact_number)) as text) "
                    + "as fhw_names from um_user u inner join um_user_location ul on u.id = ul.user_id and u.role_id in (30,24) and ul.state = 'ACTIVE' "
                    + "and u.state = 'ACTIVE' group by ul.loc_id) as t1 on t.id = t1.loc_id);";
        }

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<LocationMasterDataBean> q = session.createNativeQuery(query)
                .addScalar("actualID", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("parent", StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("fhwDetailString", StandardBasicTypes.STRING);
        if (lastUpdatedOn != null) {
            q.setParameter("lastUpdatedOn", lastUpdatedOn);
        }
        return q.setResultTransformer(Transformers.aliasToBean(LocationMasterDataBean.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SurveyLocationMobDataBean> retrieveAllLocationForMobileByUserId(Integer userId, Integer roleId) {
        String query = "with loc_det as(\n"
                + "select child_id from location_hierchy_closer_det where  location_hierchy_closer_det.parent_id in ("
                + "select id from location_master where type in (select type from location_type_master where level > 4) and id in ("
                + "select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE'"
                + ")"
                + ")"
                + "),user_det as ("
                + "select ul.loc_id,u.role_id,u.id as u_id,u.state as user_state,u.first_name,u.last_name "
                + "from um_user_location ul inner join loc_det ld on ul.loc_id = ld.child_id "
                + "inner join um_user u on u.id = ul.user_id "
                + "where u.state = 'ACTIVE' and ul.state = 'ACTIVE'"
                + "and u.role_id = :roleId"
                + ")"
                + "select distinct location_master.id,location_master.state, "
                + "case "
                + "when location_master.type in ('A','AA') "
                + "then (case when ul.u_id is not null then CONCAT(location_master.name, ' - ', ul.first_name, ' ', ul.last_name) "
                + "else CONCAT(location_master.name, ' - ', 'No ASHA Worker') end) else location_master.name end, location_master.parent, "
                + "case when location_master.state = 'ACTIVE' then true else false end as \"isActive\", "
                + "case "
                + "when location_master.type in ('P', 'U') then 4 "
                + "when location_master.type in ('SC', 'ANM') then 5 "
                + "when location_master.type in ('V', 'UA', 'ANG') then 6 "
                + "when location_master.type in ('A', 'AA') then 7 "
                + "end as level, "
                + "location_master.lgd_code as \"lgdCode\" "
                + "from loc_det inner join location_master on loc_det.child_id = location_master.id "
                + "left join user_det ul on loc_det.child_id = ul.loc_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<SurveyLocationMobDataBean> q = session.createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("parent", StandardBasicTypes.INTEGER)
                .addScalar("isActive", StandardBasicTypes.BOOLEAN)
                .addScalar("level", StandardBasicTypes.INTEGER)
                .addScalar("lgdCode", StandardBasicTypes.STRING);
        q.setParameter("userId", userId);
        q.setParameter("roleId", roleId);

        return q.setResultTransformer(Transformers.aliasToBean(SurveyLocationMobDataBean.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SurveyLocationMobDataBean> retrieveAllAreasAssignedToAshaForMobileByUserId(Integer userId) {
        String query = "with loc_det as (\n"
                + "select child_id from location_hierchy_closer_det where  location_hierchy_closer_det.parent_id in (\n"
                + "select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE'\n"
                + ")\n"
                + ")\n"
                + "select distinct location_master.id, location_master.state, location_master.name, location_master.parent, \n"
                + "case when location_master.state = 'ACTIVE' then true else false end as \"isActive\", \n"
                + "case \n"
                + "when location_master.type in ('P', 'U') then 4 \n"
                + "when location_master.type in ('SC', 'ANM') then 5 \n"
                + "when location_master.type in ('V', 'UA', 'ANG') then 6 \n"
                + "when location_master.type in ('A', 'AA') then 7\n"
                + "end as level, \n"
                + "location_master.lgd_code as \"lgdCode\" \n"
                + "from loc_det inner join location_master on loc_det.child_id = location_master.id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<SurveyLocationMobDataBean> q = session.createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("parent", StandardBasicTypes.INTEGER)
                .addScalar("isActive", StandardBasicTypes.BOOLEAN)
                .addScalar("level", StandardBasicTypes.INTEGER)
                .addScalar("lgdCode", StandardBasicTypes.STRING);
        q.setParameter("userId", userId);

        return q.setResultTransformer(Transformers.aliasToBean(SurveyLocationMobDataBean.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveLocationHierarchyById(Integer locationId) {
        String query = " select string_agg(l.name,'>' order by ld.depth desc) from location_hierchy_closer_det ld, \n"
                + "location_master l  where child_id  = :locationId and ld.parent_id = l.id";

        NativeQuery<String> q = sessionFactory.getCurrentSession().createNativeQuery(query);
        q.setParameter("locationId", locationId);
        return q.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveEngLocationHierarchyById(Integer locationId) {
        String query = " select string_agg(l.english_name,'>' order by ld.depth desc) from location_hierchy_closer_det ld, \n"
                + "location_master l  where child_id  = :locationId and ld.parent_id = l.id";

        NativeQuery<String> q = sessionFactory.getCurrentSession().createNativeQuery(query);
        q.setParameter("locationId", locationId);
        return q.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveBlockLocationsByUserId(Integer userId) {
        String query = "select id, name, type, parent from location_master where id in\n"
                + "(select child_id from location_hierchy_closer_det where parent_id in\n"
                + "(select parent_id from location_hierchy_closer_det where child_id in\n"
                + "(select loc_id from um_user_location where user_id=" + userId + " and state='ACTIVE') and parent_loc_type = 'D') and type in ('B'))";
        NativeQuery<LocationMaster> sQLQuery = getCurrentSession().createNativeQuery(query);
        List<LocationMaster> locationMasters = sQLQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("parent", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(LocationMaster.class)).list();
        if (CollectionUtils.isEmpty(locationMasters)) {
            return Collections.emptyList();
        }
        return locationMasters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveWorkerInfoByLocationId(Integer locationId) {
        String query = "select cast(u.first_name || ' ' || u.last_name || ' (' || u.contact_number || ')' as text) as \"workerInfo\"\n"
                + "from um_user u, um_user_location ul where u.id = ul.user_id and ul.state = 'ACTIVE' and u.state = 'ACTIVE' "
                + "and ul.loc_id = :locationId";

        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationId);
        List<String> list = sQLQuery.list();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isParentLocationIsCorrect(Integer parentLocationId, String type) {
        String query = "select case when parent_level.level+1 = loc_level.level then true else false end as is_parent_loc_level_correct from \n"
                + "(select level from location_type_master where type = (\n"
                + "select type from location_master where id = :parentLocationId)) as parent_level,\n"
                + "(select level from location_type_master where type = :type) as loc_level";

        NativeQuery<Boolean> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("parentLocationId", parentLocationId);
        sQLQuery.setParameter("type", type);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationMaster retrieveLocationByParentIdAndName(Integer parent, String name) {
        return findEntityByCriteriaList((root, builder, type) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(LocationMaster.Fields.PARENT), parent));
            predicates.add(builder.equal(root.get(LocationMaster.Fields.NAME), name));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDetailDto getParentLocationDetail(Integer locationId, String languagePreference) {
        String query = "select location_master.id as id \n"
                + ",case when 'EN' = :prefLanguage then location_master.english_name else location_master.name end as name \n"
                + ",location_master.type as type \n"
                + ",lt.name as \"locType\" \n"
                + "from location_master,location_type_master lt \n"
                + " where location_master.id = (select parent from location_master where id = :locationId) \n"
                + " and lt.type = location_master.type;";

        NativeQuery<LocationDetailDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationId);
        sQLQuery.setParameter("prefLanguage", languagePreference);
        sQLQuery.setResultTransformer(Transformers.aliasToBean(LocationDetailDto.class));
        sQLQuery.addScalar(LocationMaster.Fields.ID, StandardBasicTypes.INTEGER);
        sQLQuery.addScalar("name", StandardBasicTypes.STRING);
        sQLQuery.addScalar("type", StandardBasicTypes.STRING);
        sQLQuery.addScalar("locType", StandardBasicTypes.STRING);

        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationDetailDto> retrieveLocationByParentLocation(List<Integer> locationIds, Integer level, String languagePreference) {
        String query = "with loc_det as (\n"
                + "select lh.child_id as loc_id,lt.name as loc_type\n"
                + "from location_hierchy_closer_det lh,location_type_master lt\n"
                + "where lh.parent_id in (:locationId) and lt.level = :level\n"
                + "and lt.type = lh.child_loc_type \n"
                + ")\n"
                + "select location_master.id as id\n"
                + ",case when 'EN' = :prefLanguage then location_master.english_name else location_master.name end as name\n"
                + ",location_master.type\n"
                + ",loc_det.loc_type as \"locType\"\n"
                + "from loc_det,location_master \n"
                + "where  location_master.id = loc_det.loc_id order by name;";

        NativeQuery<LocationDetailDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationIds);
        sQLQuery.setParameter("level", level);
        sQLQuery.setParameter("prefLanguage", languagePreference);
        sQLQuery.setResultTransformer(Transformers.aliasToBean(LocationDetailDto.class));
        sQLQuery.addScalar(LocationMaster.Fields.ID, StandardBasicTypes.INTEGER);
        sQLQuery.addScalar("name", StandardBasicTypes.STRING);
        sQLQuery.addScalar("type", StandardBasicTypes.STRING);
        sQLQuery.addScalar("locType", StandardBasicTypes.STRING);
        return sQLQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveLocationFullNameById(Integer locationId) {
        String query = "select get_location_hierarchy(:location_id) as location_name";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("location_id", locationId);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationElasticHierarchyDto> retriveLocationListByIds(List<Integer> ids) {
        String query = "with location_det as (\n"
                + "select lm.id as location_id,lm.english_name  as location_name\n"
                + "from location_master lm\n"
                + "where lm.id in( :ids )\n"
                + "limit 50\n"
                + ")\n"
                + "select lh.child_id as locationId\n"
                + ",location_det.location_name as locationName\n"
                + ",max(case when lh.parent_loc_type = 'D' then l.id else null end) as \"districtId\"\n"
                + ",max(case when lh.parent_loc_type = 'D' then l.\"name\" else null end) as \"districtName\"\n"
                + ",max(case when lh.parent_loc_type = 'C' then l.id else null end) as \"corporationId\"\n"
                + ",max(case when lh.parent_loc_type = 'C' then l.\"name\" else null end) as \"corporationName\"\n"
                + ",max(case when lh.parent_loc_type = 'B' then l.id else null end) as \"blockId\"\n"
                + ",max(case when lh.parent_loc_type = 'B' then l.\"name\" else null end) as \"blockName\"\n"
                + ",max(case when lh.parent_loc_type = 'Z' then l.id else null end) as \"zoneId\"\n"
                + ",max(case when lh.parent_loc_type = 'Z' then l.\"name\" else null end) as \"zoneName\"\n"
                + ",max(case when lh.parent_loc_type = 'U' then l.id else null end) as \"uphcId\"\n"
                + ",max(case when lh.parent_loc_type = 'U' then l.\"name\" else null end) as \"uphcName\"\n"
                + ",max(case when lh.parent_loc_type = 'P' then l.id else null end) as \"phcId\"\n"
                + ",max(case when lh.parent_loc_type = 'P' then l.\"name\" else null end) as \"phcName\"\n"
                + ",max(case when lh.parent_loc_type = 'ANM' then l.id else null end) as \"anmAreaId\"\n"
                + ",max(case when lh.parent_loc_type = 'ANM' then l.\"name\" else null end) as \"anmAreaName\"\n"
                + ",max(case when lh.parent_loc_type = 'SC' then l.id else null end) as \"scId\"\n"
                + ",max(case when lh.parent_loc_type = 'SC' then l.\"name\" else null end) as \"scName\"\n"
                + ",max(case when lh.parent_loc_type = 'V' then l.id else null end) as \"villageId\"\n"
                + ",max(case when lh.parent_loc_type = 'V' then l.\"name\" else null end) as \"villageName\"\n"
                + ",max(case when lh.parent_loc_type = 'ANG' then l.id else null end) as \"anganwadiId\"\n"
                + ",max(case when lh.parent_loc_type = 'ANG' then l.\"name\" else null end) as \"anganwadiName\"\n"
                + "from location_hierchy_closer_det lh,location_det,location_master l\n"
                + "where lh.child_id = location_det.location_id\n"
                + "and l.id = lh.parent_id\n"
                + "group by lh.child_id,location_det.location_name";

        NativeQuery<LocationElasticHierarchyDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        List<LocationElasticHierarchyDto> elasticHierarchyDtos = sQLQuery
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("locationName", StandardBasicTypes.STRING)
                .addScalar("districtId", StandardBasicTypes.INTEGER)
                .addScalar("districtName", StandardBasicTypes.STRING)
                .addScalar("corporationId", StandardBasicTypes.INTEGER)
                .addScalar("corporationName", StandardBasicTypes.STRING)
                .addScalar("blockId", StandardBasicTypes.INTEGER)
                .addScalar("blockName", StandardBasicTypes.STRING)
                .addScalar("zoneId", StandardBasicTypes.INTEGER)
                .addScalar("zoneName", StandardBasicTypes.STRING)
                .addScalar("uphcId", StandardBasicTypes.INTEGER)
                .addScalar("uphcName", StandardBasicTypes.STRING)
                .addScalar("phcId", StandardBasicTypes.INTEGER)
                .addScalar("phcName", StandardBasicTypes.STRING)
                .addScalar("anmAreaId", StandardBasicTypes.INTEGER)
                .addScalar("anmAreaName", StandardBasicTypes.STRING)
                .addScalar("scId", StandardBasicTypes.INTEGER)
                .addScalar("scName", StandardBasicTypes.STRING)
                .addScalar("villageId", StandardBasicTypes.INTEGER)
                .addScalar("villageName", StandardBasicTypes.STRING)
                .addScalar("anganwadiId", StandardBasicTypes.INTEGER)
                .addScalar("anganwadiName", StandardBasicTypes.STRING)
                .setParameterList("ids", ids)
                .setResultTransformer(Transformers.aliasToBean(LocationElasticHierarchyDto.class)).list();

        if (CollectionUtils.isEmpty(elasticHierarchyDtos)) {
            return Collections.emptyList();
        }

        return elasticHierarchyDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAllLocationIdsByParentId(Integer parentId) {
        String query = "select child_id as \"id\" from location_hierchy_closer_det where parent_id = :parentId and child_loc_type in ('A','AA','V','ANG')";

        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("parentId", parentId);
        sQLQuery.addScalar("id", StandardBasicTypes.INTEGER);
        return sQLQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationElasticHierarchyDto> retriveLocationListByQuary(String queries) {
        String query = "with location_det as (\n"
                + "select lm.id as location_id,lm.english_name  as location_name\n"
                + "from location_master lm\n"
                + "where \"name\" ilike '%" + queries + "%' or english_name ilike '%" + queries + "%'\n"
                + "limit 50\n"
                + ")\n"
                + "select lh.child_id as locationId\n"
                + ",location_det.location_name as locationName\n"
                + ",max(case when lh.parent_loc_type = 'D' then l.id else null end) as \"districtId\"\n"
                + ",max(case when lh.parent_loc_type = 'D' then l.\"name\" else null end) as \"districtName\"\n"
                + ",max(case when lh.parent_loc_type = 'C' then l.id else null end) as \"corporationId\"\n"
                + ",max(case when lh.parent_loc_type = 'C' then l.\"name\" else null end) as \"corporationName\"\n"
                + ",max(case when lh.parent_loc_type = 'B' then l.id else null end) as \"blockId\"\n"
                + ",max(case when lh.parent_loc_type = 'B' then l.\"name\" else null end) as \"blockName\"\n"
                + ",max(case when lh.parent_loc_type = 'Z' then l.id else null end) as \"zoneId\"\n"
                + ",max(case when lh.parent_loc_type = 'Z' then l.\"name\" else null end) as \"zoneName\"\n"
                + ",max(case when lh.parent_loc_type = 'U' then l.id else null end) as \"uphcId\"\n"
                + ",max(case when lh.parent_loc_type = 'U' then l.\"name\" else null end) as \"uphcName\"\n"
                + ",max(case when lh.parent_loc_type = 'P' then l.id else null end) as \"phcId\"\n"
                + ",max(case when lh.parent_loc_type = 'P' then l.\"name\" else null end) as \"phcName\"\n"
                + ",max(case when lh.parent_loc_type = 'ANM' then l.id else null end) as \"anmAreaId\"\n"
                + ",max(case when lh.parent_loc_type = 'ANM' then l.\"name\" else null end) as \"anmAreaName\"\n"
                + ",max(case when lh.parent_loc_type = 'SC' then l.id else null end) as \"scId\"\n"
                + ",max(case when lh.parent_loc_type = 'SC' then l.\"name\" else null end) as \"scName\"\n"
                + ",max(case when lh.parent_loc_type = 'V' then l.id else null end) as \"villageId\"\n"
                + ",max(case when lh.parent_loc_type = 'V' then l.\"name\" else null end) as \"villageName\"\n"
                + ",max(case when lh.parent_loc_type = 'ANG' then l.id else null end) as \"anganwadiId\"\n"
                + ",max(case when lh.parent_loc_type = 'ANG' then l.\"name\" else null end) as \"anganwadiName\"\n"
                + "from location_hierchy_closer_det lh,location_det,location_master l\n"
                + "where lh.child_id = location_det.location_id\n"
                + "and l.id = lh.parent_id\n"
                + "group by lh.child_id,location_det.location_name";

        NativeQuery<LocationElasticHierarchyDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        List<LocationElasticHierarchyDto> elasticHierarchyDtos = sQLQuery
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("locationName", StandardBasicTypes.STRING)
                .addScalar("districtId", StandardBasicTypes.INTEGER)
                .addScalar("districtName", StandardBasicTypes.STRING)
                .addScalar("corporationId", StandardBasicTypes.INTEGER)
                .addScalar("corporationName", StandardBasicTypes.STRING)
                .addScalar("blockId", StandardBasicTypes.INTEGER)
                .addScalar("blockName", StandardBasicTypes.STRING)
                .addScalar("zoneId", StandardBasicTypes.INTEGER)
                .addScalar("zoneName", StandardBasicTypes.STRING)
                .addScalar("uphcId", StandardBasicTypes.INTEGER)
                .addScalar("uphcName", StandardBasicTypes.STRING)
                .addScalar("phcId", StandardBasicTypes.INTEGER)
                .addScalar("phcName", StandardBasicTypes.STRING)
                .addScalar("anmAreaId", StandardBasicTypes.INTEGER)
                .addScalar("anmAreaName", StandardBasicTypes.STRING)
                .addScalar("scId", StandardBasicTypes.INTEGER)
                .addScalar("scName", StandardBasicTypes.STRING)
                .addScalar("villageId", StandardBasicTypes.INTEGER)
                .addScalar("villageName", StandardBasicTypes.STRING)
                .addScalar("anganwadiId", StandardBasicTypes.INTEGER)
                .addScalar("anganwadiName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(LocationElasticHierarchyDto.class)).list();

        if (CollectionUtils.isEmpty(elasticHierarchyDtos)) {
            return Collections.emptyList();
        }

        return elasticHierarchyDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> retrieveLocationsByLocationIdAndType(List<Integer> ids, List<String> locationTypes) {
        PredicateBuilder<LocationMaster> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(LocationMaster.Fields.STATE), LocationMaster.State.ACTIVE));
            predicates.add(builder.in(root.get(LocationMaster.Fields.ID)).value(ids));
            if (locationTypes != null && !locationTypes.isEmpty()) {
                predicates.add(builder.in(root.get(LocationMaster.Fields.TYPE)).value(locationTypes));
            }
            return predicates;
        };
        return super.findByCriteriaList(predicateBuilder);
    }
}
