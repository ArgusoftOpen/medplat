/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dao.impl;

import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.dashboard.fhs.view.DisplayObject;
import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.familyqrcode.dto.FamilyQRCodeDto;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dto.FhsVillagesDto;
import com.argusoft.medplat.fhs.dto.FhwServiceStatusDto;
import com.argusoft.medplat.fhs.dto.StarPerformersOfTheDayDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.web.location.dto.LocationMasterDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in family dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class FamilyDaoImpl extends GenericDaoImpl<FamilyEntity, Integer> implements FamilyDao {

    public static final String MODIFIED_ON = "modifiedOn";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> retrieveFamiliesByLocationIdsAndState(List<Integer> locationIds, List<String> states) {
        if (CollectionUtils.isEmpty(locationIds)) {
            return new ArrayList<>();
        }

        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(
                    builder.or(
                            root.get(FamilyEntity.Fields.LOCATION_ID).in(locationIds),
                            root.get(FamilyEntity.Fields.AREA_ID).in(locationIds)
                    )
            );
            if (!CollectionUtils.isEmpty(states)) {
                predicates.add(root.get(FamilyEntity.Fields.STATE).in(states));
            }
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> retrieveFamiliesByAreaIds(List<Integer> locationIds, List<String> states, Date lastUpdated) {
        if (CollectionUtils.isEmpty(locationIds)) {
            return new ArrayList<>();
        }

        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get(FamilyEntity.Fields.AREA_ID).in(locationIds));
            if (!CollectionUtils.isEmpty(states)) {
                predicates.add(root.get(FamilyEntity.Fields.STATE).in(states));
            }
            if (lastUpdated != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(MODIFIED_ON).as(java.sql.Date.class), lastUpdated));
            }
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DisplayObject> getFamiliesAndMembersForChildLocations(Integer locationId, Integer locationLevel, Integer userId) {

        String query = "select cast(loc.id as varchar) as \"id\", loc.name as \"value\", loc.type as \"locationType\" \n"
                + ",cast(coalesce(sum(fhs_imported_from_emamta_family), 0) as int) as \"importedFromEmamta\" \n"
                + ",cast(coalesce(sum(fhs_imported_from_emamta_member), 0) as int) as \"importedFromEmamtaMember\" \n"
                + ",cast(coalesce(sum(fhs_to_be_processed_family), 0) as int) as \"unverifiedFHS\" \n"
                + ",cast(coalesce(sum(fhs_verified_family), 0) as int) as \"verifiedFHS\" \n"
                + ",cast(coalesce(sum(fhs_archived_family), 0) as int) as \"archivedFHS\" \n"
                + ",cast(coalesce(sum(fhs_new_family), 0) as int) as \"newFamily\" \n"
                + ",cast(coalesce(sum(fhs_total_member), 0) as int) as \"totalMember\" \n"
                + ",cast(coalesce(sum(fhs_inreverification_family), 0) as int) as \"inReverification\" \n"
                + ",cast(coalesce(worker_det.worker_cnt, 0) as int) as \"worker\" \n"
                + "from um_user_location um_loc \n"
                + "inner join location_hierchy_closer_det um_loc_closer \n"
                + "on um_loc.loc_id = um_loc_closer.parent_id and um_loc.state = 'ACTIVE' and user_id = :userId\n"
                + "inner join location_hierchy_closer_det sel_loc_closer \n"
                + "on sel_loc_closer.parent_id in (select child_id from location_hierchy_closer_det where parent_id = :locationId and depth = 1)\n"
                + "and sel_loc_closer.child_id = um_loc_closer.child_id\n"
                + "inner join location_wise_analytics loc_anlyts\n"
                + "on loc_anlyts.loc_id = sel_loc_closer.child_id\n"
                + "inner join location_master loc on loc.id = sel_loc_closer.parent_id\n"
                + "left join (select parent_id,count(*) as worker_cnt from (select Distinct sel_loc_closer.parent_id,user_loc.user_id\n"
                + "from um_user_location um_loc \n"
                + "inner join location_hierchy_closer_det um_loc_closer \n"
                + "on um_loc.loc_id = um_loc_closer.parent_id and um_loc.state = 'ACTIVE' and user_id = :userId\n"
                + "inner join location_hierchy_closer_det sel_loc_closer \n"
                + "on sel_loc_closer.parent_id in (select child_id from location_hierchy_closer_det where parent_id = :locationId and depth = 1)\n"
                + "and sel_loc_closer.child_id = um_loc_closer.child_id\n"
                + "inner join um_user_location user_loc\n"
                + "on user_loc.loc_id = sel_loc_closer.child_id and user_loc.state = 'ACTIVE'\n"
                + "inner join um_user on um_user.id = user_loc.user_id \n"
                + "and um_user.role_id = (select id from um_role_master where name = 'FHW') and um_user.state = 'ACTIVE') as temp group by parent_id\n"
                + ") as worker_det on worker_det.parent_id = sel_loc_closer.parent_id\n"
                + "group by loc.id,worker_det.worker_cnt order by value;";

        NativeQuery<DisplayObject> q = getCurrentSession().createNativeQuery(query);

        q.setParameter(FamilyEntity.Fields.LOCATION_ID, locationId);
        q.setParameter(FamilyEntity.Fields.USER_ID, userId);
        return q.setResultTransformer(Transformers.aliasToBean(DisplayObject.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DisplayObject> familiesAndMembersByLocationId(Integer locationId, Integer locationLevel, Integer userId) {
        String query = "select cast(user_loc.user_id as varchar) as \"id\""
                + ", um_user.first_name || ' ' || um_user.last_name || ' (' || um_user.user_name || ' )' as \"personName\""
                + ",cast(coalesce(sum(fhs_imported_from_emamta_family), 0) as int) as \"importedFromEmamta\" \n"
                + ",cast(coalesce(sum(fhs_imported_from_emamta_member), 0) as int) as \"importedFromEmamtaMember\" \n"
                + ",cast(coalesce(sum(fhs_to_be_processed_family), 0) as int) as \"unverifiedFHS\" \n"
                + ",cast(coalesce(sum(fhs_verified_family), 0) as int) as \"verifiedFHS\" \n"
                + ",cast(coalesce(sum(fhs_archived_family), 0) as int) as \"archivedFHS\" \n"
                + ",cast(coalesce(sum(fhs_new_family), 0) as int) as \"newFamily\" \n"
                + ",cast(coalesce(sum(fhs_verified_family + fhs_new_family), 0) as int) as \"totalFamily\" "
                + ",cast(coalesce(sum(fhs_total_member), 0) as int) as \"totalMember\" \n"
                + ",cast(coalesce(sum(fhs_inreverification_family), 0) as int) as \"inReverification\" \n"
                + "from um_user_location um_loc \n"
                + "inner join location_hierchy_closer_det um_loc_closer \n"
                + "on um_loc.loc_id = um_loc_closer.parent_id and um_loc.state = 'ACTIVE' and user_id = :userId\n"
                + "inner join location_hierchy_closer_det sel_loc_closer \n"
                + "on sel_loc_closer.parent_id = :locationId\n"
                + "and sel_loc_closer.child_id = um_loc_closer.child_id\n"
                + "inner join um_user_location user_loc\n"
                + "on user_loc.loc_id = sel_loc_closer.child_id and user_loc.state = 'ACTIVE'\n"
                + "inner join um_user on um_user.id = user_loc.user_id \n"
                + "and um_user.role_id = (select id from um_role_master where name = 'FHW') and um_user.state = 'ACTIVE'\n"
                + "inner join location_wise_analytics loc_anlyts\n"
                + "on loc_anlyts.loc_id = um_loc_closer.child_id\n"
                + "group by user_loc.user_id, um_user.first_name, um_user.last_name, um_user.user_name "
                + "order by \"personName\" ";

        NativeQuery<DisplayObject> q = getCurrentSession().createNativeQuery(query);
        q.setParameter(FamilyEntity.Fields.LOCATION_ID, locationId);
        q.setParameter(FamilyEntity.Fields.USER_ID, userId);
        return q.setResultTransformer(Transformers.aliasToBean(DisplayObject.class)).list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FamilyEntity retrieveFamilyByFamilyId(String familyId) {
        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(FamilyEntity.Fields.FAMILY_ID), familyId));
            return predicates;
        };
        List<FamilyEntity> familyEntityList = findByCriteriaList(predicateBuilder);
        if (!CollectionUtils.isEmpty(familyEntityList)) {
            return new ArrayList<>(familyEntityList).get(0);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FamilyEntity updateFamily(FamilyEntity familyEntity) {
        super.merge(familyEntity);
        return familyEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FhsVillagesDto> getFhsVillagesByUserId(Integer userId) {
        String query = "select Distinct loc.id, loc.name from um_user_location um_usr_loc inner join location_hierchy_closer_det lcloser "
                + " on lcloser.parent_id = um_usr_loc.loc_id and um_usr_loc.user_id = " + userId
                + " inner join location_master loc on loc.id = lcloser.child_id inner join imt_family fam  on loc.id = fam.location_id "
                + " where fam.state = 'com.argusoft.medplat.family.state.unverified' order by loc.name";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<FhsVillagesDto> q = session.createNativeQuery(query);

        return q.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(FhsVillagesDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StarPerformersOfTheDayDto> starPerformersOfTheDay() {
        String query = "select modified_by as userId, count(*) as recordUpdated from imt_family "
                + "where modified_by is not null and modified_on between current_date - 1 and current_date "
                + "group by modified_by order by recordUpdated  limit 1;";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<StarPerformersOfTheDayDto> q = session.createNativeQuery(query);

        return q.addScalar("userid", StandardBasicTypes.INTEGER)
                .addScalar("recordupdated", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(StarPerformersOfTheDayDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> getFamilies(List<Integer> assignedPersonIds, Boolean isFamilyVerified, List<Integer> locationIds, List<String> states, Date lastUpdated) {
        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (assignedPersonIds != null && !assignedPersonIds.isEmpty()) {
                predicates.add(builder.in(root.get(FamilyEntity.Fields.ASSIGNED_TO)).value(assignedPersonIds));
            }
            if (isFamilyVerified != null) {
                predicates.add(builder.equal(root.get(FamilyEntity.Fields.IS_VERIFIED_FLAG), isFamilyVerified));
            }
            if (locationIds != null && !locationIds.isEmpty()) {
                predicates.add(
                        builder.or(
                                builder.in(root.get(FamilyEntity.Fields.LOCATION_ID)).value(locationIds),
                                builder.in(root.get(FamilyEntity.Fields.AREA_ID)).value(locationIds)
                        )
                );
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(builder.in(root.get(FamilyEntity.Fields.STATE)).value(states));
            }
            if (lastUpdated != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(MODIFIED_ON), lastUpdated));
            }
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FamilyEntity createFamily(FamilyEntity familyEntity) {
        super.create(familyEntity);
        return familyEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> retrieveFamiliesByFamilyIds(List<String> familyIds) {
        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.in(root.get(FamilyEntity.Fields.FAMILY_ID)).value(familyIds));
            return predicates;
        };
        return new ArrayList<>(findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FhwServiceStatusDto> getFhwServiceReportByUserId(Integer userId) {
        String query = "with loc as (\n" +
                "\tselect child_id from um_user_location uul\n" +
                "\tleft join location_hierchy_closer_det lhcd on uul.loc_id = lhcd.parent_id \n" +
                "\twhere uul.user_id = :userId\n" +
                "\tand child_loc_type in ('V', 'UA', 'ANG')\n" +
                "\tand uul.state = 'ACTIVE' \n" +
                ")\n" +
                "select loc.child_id as \"locationId\"\n" +
                ",sum(fhs_imported_from_emamta_family) as \"familiesImportedFromEMamta\" \n" +
                ",sum(fhs_verified_family) as \"familiesVerifiedTillNow\"\n" +
                ",sum(family_varified_last_3_days) as \"familiesVerifiedLast3Days\"\n" +
                ",sum(fhs_archived_family) as \"familiesArchivedTillNow\"\n" +
                ",sum(fhs_new_family) as \"newFamiliesAddedTillNow\"\n" +
                ",sum(fhs_verified_family+fhs_new_family) as \"totalFamiliesInIMTTillNow\"\n" +
                ",sum(seasonal_migrant_families) as \"totalNumberOfSeasonalMigrantFamilies\"\n" +
                ",sum(fhs_total_member) as \"totalMembersInIMTTillNow\"\n" +
                ",sum(eligible_couples_in_techo) as \"totalEligibleCouplesInTeCHO\"\n" +
                ",sum(pregnant_woman_techo) as \"totalPregnantWomenInTeCHO\"\n" +
                ",sum(member_with_adhar_number) as \"numberOfMembersWithAadharNumberEntered\"\n" +
                ",sum(member_with_mobile_number) as \"numberOfMembersWithMobileNumberEntered\"\n" +
                ",sum(child_under_5_year) as \"under5ChildrenTillNow\"\n" +
                "from loc , location_hierchy_closer_det , location_wise_analytics\n" +
                "where loc.child_id = location_hierchy_closer_det.parent_id\n" +
                "and location_wise_analytics.loc_id = location_hierchy_closer_det.child_id\n" +
                "group by loc.child_id";

        Session session = sessionFactory.getCurrentSession();

        NativeQuery<FhwServiceStatusDto> q = session.createNativeQuery(query)
                .addScalar(FamilyEntity.Fields.LOCATION_ID, StandardBasicTypes.INTEGER)
                .addScalar("familiesImportedFromEMamta", StandardBasicTypes.INTEGER)
                .addScalar("familiesVerifiedTillNow", StandardBasicTypes.INTEGER)
                .addScalar("familiesVerifiedLast3Days", StandardBasicTypes.INTEGER)
                .addScalar("familiesArchivedTillNow", StandardBasicTypes.INTEGER)
                .addScalar("newFamiliesAddedTillNow", StandardBasicTypes.INTEGER)
                .addScalar("totalFamiliesInIMTTillNow", StandardBasicTypes.INTEGER)
                .addScalar("totalNumberOfSeasonalMigrantFamilies", StandardBasicTypes.INTEGER)
                .addScalar("totalMembersInIMTTillNow", StandardBasicTypes.INTEGER)
                .addScalar("totalEligibleCouplesInTeCHO", StandardBasicTypes.INTEGER)
                .addScalar("totalPregnantWomenInTeCHO", StandardBasicTypes.INTEGER)
                .addScalar("numberOfMembersWithAadharNumberEntered", StandardBasicTypes.INTEGER)
                .addScalar("numberOfMembersWithMobileNumberEntered", StandardBasicTypes.INTEGER)
                .addScalar("under5ChildrenTillNow", StandardBasicTypes.INTEGER);

        q.setParameter(FamilyEntity.Fields.USER_ID, userId);
        return q.setResultTransformer(Transformers.aliasToBean(FhwServiceStatusDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FhwServiceStatusDto> getFhwServiceReportForNcdByUserId(Integer userId) {
        String query = "with user_id as (\n" +
                "\tselect :userId \n" +
                ")\n" +
                ", fdate as (\n" +
                "\tselect case when extract('month' from now()) > 4 then to_date('04-01-' || extract(year from now()), 'MM-DD-YYYY') \n" +
                "\telse to_date('04-01-' || extract(year from now()) - 1, 'MM-DD-YYYY') end \n" +
                ")\n" +
                ", mem as (\n" +
                "\tselect member_id from ncd_member_cervical_detail where created_by = (select * from user_id) and created_on >= (select * from fdate)\n" +
                "\tunion \n" +
                "\tselect member_id from ncd_member_oral_detail where created_by = (select * from user_id) and created_on >= (select * from fdate)\n" +
                "\tunion \n" +
                "\tselect member_id from ncd_member_hypertension_detail where created_by = (select * from user_id) and created_on >= (select * from fdate)\n" +
                "\tunion \n" +
                "\tselect member_id from ncd_member_diabetes_detail where created_by = (select * from user_id) and created_on >= (select * from fdate)\n" +
                "\tunion \n" +
                "\tselect member_id from ncd_member_breast_detail where created_by = (select * from user_id) and created_on >= (select * from fdate)\n" +
                ")\n" +
                ", det as (\n" +
                "\tselect m.id, \n" +
                "\tf.location_id,\n" +
                "\tm.gender, \n" +
                "\tsum(case when h.member_id is not null then 1 else 0 end) as hyp,\n" +
                "\tsum(case when d.member_id is not null then 1 else 0 end) as dia,\n" +
                "\tsum(case when b.member_id is not null then 1 else 0 end) as breast,\n" +
                "\tsum(case when o.member_id is not null then 1 else 0 end) as oral,\n" +
                "\tsum(case when c.member_id is not null then 1 else 0 end) as cerv\n" +
                "\tfrom mem\n" +
                "\tinner join imt_member m on m.id = mem.member_id\n" +
                "\tinner join imt_family f on f.family_id = m.family_id \n" +
                "\tleft join ncd_member_hypertension_detail h on h.member_id = mem.member_id and h.created_by = (select * from user_id) and h.created_on >= (select * from fdate)\n" +
                "\tleft join ncd_member_diabetes_detail d on d.member_id = mem.member_id and d.created_by = (select * from user_id) and d.created_on >= (select * from fdate)\n" +
                "\tleft join ncd_member_oral_detail o on o.member_id = mem.member_id and o.created_by = (select * from user_id) and o.created_on >= (select * from fdate)\n" +
                "\tleft join ncd_member_breast_detail b on b.member_id = mem.member_id and b.created_by = (select * from user_id) and b.created_on >= (select * from fdate)\n" +
                "\tleft join ncd_member_cervical_detail c on c.member_id = mem.member_id and c.created_by = (select * from user_id) and c.created_on >= (select * from fdate)\n" +
                "\tgroup by m.id, f.location_id\n" +
                ")\n" +
                "select \n" +
                "location_id as \"locationId\",\n" +
                "count(*) as \"ncdTotalMembersScreened\",\n" +
                "sum(case when gender = 'F' then 1 else 0 end) as \"ncdTotalFemaleMembersScreened\",\n" +
                "sum(case when gender = 'M' then 1 else 0 end) as \"ncdTotalMaleMembersScreened\",\n" +
                "sum(case when hyp > 0 then 1 else 0 end) as \"ncdTotalMembersScreenedForHypertension\",\n" +
                "sum(case when dia > 0 then 1 else 0 end) as \"ncdTotalMembersScreenedForDiabetes\",\n" +
                "sum(case when oral > 0 then 1 else 0 end) as \"ncdTotalMembersScreenedForOral\",\n" +
                "sum(case when breast > 0 then 1 else 0 end) as \"ncdTotalFemaleMembersScreenedForBreast\",\n" +
                "sum(case when cerv > 0 then 1 else 0 end) as \"ncdTotalFemaleMembersScreenedForCervical\"\n" +
                "from det\n" +
                "group by location_id";

        Session session = sessionFactory.getCurrentSession();

        NativeQuery<FhwServiceStatusDto> q = session.createNativeQuery(query).addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalMembersScreened", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalMaleMembersScreened", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalFemaleMembersScreened", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalMembersScreenedForHypertension", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalMembersScreenedForOral", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalMembersScreenedForDiabetes", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalFemaleMembersScreenedForBreast", StandardBasicTypes.INTEGER)
                .addScalar("ncdTotalFemaleMembersScreenedForCervical", StandardBasicTypes.INTEGER);

        q.setParameter(FamilyEntity.Fields.USER_ID, userId);
        return q.setResultTransformer(Transformers.aliasToBean(FhwServiceStatusDto.class)).list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> getFamiliesToBeAssignedBySearchString(List<String> familyIds, Integer locationId, Integer districtId, Integer userId) {

        String query = "update imt_family set location_id = :locationId, assigned_to = :userId "
                + "where family_id in (:familyIds) and assigned_to is null and location_id = :location_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameterList("familyIds", familyIds);
        q.setParameter("location_id", districtId);
        q.setParameter(FamilyEntity.Fields.LOCATION_ID, locationId);
        q.setParameter(FamilyEntity.Fields.USER_ID, userId);
        q.executeUpdate();

        PredicateBuilder<FamilyEntity> predicateBuilder = (root, builder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(FamilyEntity.Fields.LOCATION_ID), locationId));
            predicates.add(root.get(FamilyEntity.Fields.FAMILY_ID).in(familyIds));

            return predicates;
        };

        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FamilyEntity> getFamilyListByLocationId(Integer locationId, Boolean isArchivedFamily, Boolean isVerifiedFamily) {
        String query;
        if (Boolean.TRUE.equals(isVerifiedFamily)) {
            query = "with loc_det as(\n"
                    + "select case when type in('AA','A') then :locationId else null end as area_id\n"
                    + ",case when type in('AA','A') then parent else :locationId end as location_id\n"
                    + "from location_master where id = :locationId"
                    + ")\n"
                    + "select imt_family.family_id as familyId,imt_family.location_Id  as locationId ,imt_family.house_number as houseNumber,imt_family.address1 as address1 ,imt_family.address2 as address2,imt_family.state as state \n"
                    + "from imt_family,loc_det where imt_family.location_id = loc_det.location_id and (loc_det.area_id is null \n"
                    + "or imt_family.area_id = loc_det.area_id) "
                    + "and (:verifedflag = true and (state in (:verifiedstates)))";

            Session session = sessionFactory.getCurrentSession();
            NativeQuery<FamilyEntity> q = session.createNativeQuery(query);
            q.setParameter("verifedflag", isVerifiedFamily);
            q.setParameter(FamilyEntity.Fields.LOCATION_ID, locationId);
            q.setParameterList("verifiedstates", FamilyHealthSurveyServiceConstants.FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES);

            return q.addScalar(FamilyEntity.Fields.FAMILY_ID, StandardBasicTypes.STRING)
                    .addScalar(FamilyEntity.Fields.LOCATION_ID, StandardBasicTypes.INTEGER)
                    .addScalar("houseNumber", StandardBasicTypes.STRING)
                    .addScalar("address1", StandardBasicTypes.STRING)
                    .addScalar("address2", StandardBasicTypes.STRING)
                    .addScalar(FamilyEntity.Fields.STATE, StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(FamilyEntity.class)).list();
        } else {
            query = "with loc_det as(\n"
                    + "select case when type in('AA','A') then :locationId else null end as area_id,\n"
                    + "case when type in('AA','A') then parent else :locationId end as location_id\n"
                    + "from location_master where id = :locationId"
                    + ")\n"
                    + "select imt_family.family_id as familyId,imt_family.location_Id  as locationId ,imt_family.house_number as houseNumber,imt_family.address1 as address1 ,imt_family.address2 as address2,imt_family.state as state \n"
                    + "from imt_family,loc_det where imt_family.location_id = loc_det.location_id and (loc_det.area_id is null \n"
                    + "or imt_family.area_id = loc_det.area_id) "
                    + "and ((:archivedflag = true and (state in (:archivedstates) or state in(:ustate, :orphanstate))) or (:archivedflag = false and state in(:ustate, :orphanstate)))";
            Session session = sessionFactory.getCurrentSession();
            NativeQuery<FamilyEntity> q = session.createNativeQuery(query);
            q.setParameter("archivedflag", isArchivedFamily);
            q.setParameter(FamilyEntity.Fields.LOCATION_ID, locationId);

            q.setParameter("ustate", FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED);
            q.setParameter("orphanstate", FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN);
            q.setParameterList("archivedstates", FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES);

            return q.addScalar(FamilyEntity.Fields.FAMILY_ID, StandardBasicTypes.STRING)
                    .addScalar(FamilyEntity.Fields.LOCATION_ID, StandardBasicTypes.INTEGER)
                    .addScalar("houseNumber", StandardBasicTypes.STRING)
                    .addScalar("address1", StandardBasicTypes.STRING)
                    .addScalar("address2", StandardBasicTypes.STRING)
                    .addScalar(FamilyEntity.Fields.STATE, StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(FamilyEntity.class)).list();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVerifiedFamilyLocation(Integer loggedInUserId, List<String> familyList, Integer selectedMoveAnganwadiId, Integer selectedMoveAshaAreaId) {
        String query = "update imt_family set location_id = :selectedMoveAnganwadiId, "
                + "area_id = :selectedMoveAshaAreaId, modified_by = :loggedInUserId, modified_on = now() "
                + "where family_id in (" + familyList.toString().replace("[", "").replace("]", "") + ")";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameter("loggedInUserId", loggedInUserId);
        q.setParameter("selectedMoveAnganwadiId", selectedMoveAnganwadiId);
        q.setParameter("selectedMoveAshaAreaId", selectedMoveAshaAreaId);

        q.executeUpdate();
    }


    @Override
    public List<FamilyQRCodeDto> getFamiliesForQRCode(Integer locationId, String fromDate, String toDate, Integer limit, Integer offset) {
        String query = "with location_ids as (\n" +
                "\tselect lhcd.child_id \n" +
                "\tfrom location_hierchy_closer_det lhcd \n" +
                "\twhere lhcd.parent_id = :locationId\n" +
                "), asha_det as (\n" +
                "\tselect string_agg(\n" +
                "\t\tconcat_ws(' ', uu.first_name, uu.last_name)\n" +
                "\t\t,', '\n" +
                "\t) as name,\n" +
                "    uul.loc_id as location_id\n" +
                "    from um_user uu \n" +
                "    inner join um_user_location uul on uu.id = uul.user_id and uul.state = 'ACTIVE' \n" +
                "    inner join location_ids on location_ids.child_id = uul.loc_id\n" +
                "    where uu.state = 'ACTIVE' and uu.role_id = 24\n" +
                "    group by uul.loc_id\n" +
                ")\n" +
                "select if2.family_id as \"familyId\",\n" +
                "\tcoalesce(if2.house_number, 'N.A.') as \"houseNumber\",\n" +
                "    get_location_hierarchy(if2.location_id) as location, \n" +
                "    concat_ws(', ', if2.address1, if2.address2) as address,\n" +
                "    concat_ws(' ', im.first_name, im.middle_name, im.last_name) as \"familyHead\", \n" +
                "    im.mobile_number as \"contactNumber\",\n" +
                "    concat(lm2.name, '/', lm.name) as \"qrLocation\",\n" +
                "    coalesce(asha_det.name, 'N.A.') as \"ashaName\"\n" +
                "from imt_family if2 \n" +
                "left join location_master lm on lm.id = if2.location_id \n" +
                "left join location_master lm2 on lm2.id = lm.parent \n" +
                "left join imt_member im on if2.hof_id = im.id \n" +
                "left join asha_det on asha_det.location_id = if2.location_id\n" +
                "inner join location_ids on location_ids.child_id = if2.location_id \n" +
                "where if2.basic_state not in ('MIGRATED', 'ARCHIVED', 'IDSP') \n" +
                "    and if2.created_on between cast(:fromDate as date) and cast(:toDate as date) + interval '1 day' - interval '1 millisecond'\n" +
                "order by location, \"houseNumber\", if2.family_id\n" +
                "limit " + limit + " offset " + offset;

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<FamilyQRCodeDto> q = session.createNativeQuery(query);
        q.setParameter("locationId", locationId);
        q.setParameter("fromDate", fromDate);
        q.setParameter("toDate", toDate);

        return q.setResultTransformer(Transformers.aliasToBean(FamilyQRCodeDto.class)).list();
    }

    @Override
    public FamilyQRCodeDto getFamilyDetailsForQRCode(String familyId) {
        String query = "with asha_det as (\n" +
                "\tselect string_agg(concat_ws(' ', uu.first_name, uu.last_name), ', ') as name,\n" +
                "    uul.loc_id as location_id\n" +
                "    from um_user uu \n" +
                "    inner join um_user_location uul on uu.id = uul.user_id and uul.state = 'ACTIVE' \n" +
                "    inner join imt_family if2 on if2.location_id = uul.loc_id\n" +
                "    where uu.state = 'ACTIVE' and uu.role_id = 24\n" +
                "    group by uul.loc_id\n" +
                ")\n" +
                "select if2.family_id as \"familyId\",\n" +
                "\tcoalesce(if2.house_number, 'N.A.') as \"houseNumber\",\n" +
                "    get_location_hierarchy(if2.location_id) as location, \n" +
                "    concat_ws(', ', if2.address1, if2.address2) as address,\n" +
                "    concat_ws(' ', im.first_name, im.middle_name, im.last_name) as \"familyHead\",\n" +
                "    im.mobile_number as \"contactNumber\",\n" +
                "    concat(lm2.name, '/', lm.name) as \"qrLocation\", \n" +
                "    coalesce(asha_det.name, 'N.A.') as \"ashaName\" \n" +
                "from imt_family if2 \n" +
                "left join location_master lm on lm.id = if2.location_id  \n" +
                "left join location_master lm2 on lm2.id = lm.parent \n" +
                "left join imt_member im on if2.hof_id = im.id \n" +
                "left join asha_det on asha_det.location_id = if2.location_id\n" +
                "where if2.family_id = :familyId";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<FamilyQRCodeDto> q = session.createNativeQuery(query);
        q.setParameter("familyId", familyId);

        return q.setResultTransformer(Transformers.aliasToBean(FamilyQRCodeDto.class)).uniqueResult();
    }

    @Override
    public LocationMasterDto getLgdCodeByFamilyId(String familyId, String parentLocationType) {
        String query = "select lm.lgd_code as lgdCode, lm.\"name\" as name \n" +
                " from imt_family if2\n" +
                "inner join location_hierchy_closer_det lhcd on lhcd.child_id = if2.location_id  and lhcd.parent_loc_type = :locType\n" +
                " inner join location_master lm on lhcd.parent_id = lm.id\n" +
                " where if2.family_id = :familyId";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<LocationMasterDto> q = session.createNativeQuery(query);
        q.addScalar("lgdCode", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING);
        q.setParameter("familyId", familyId);
        q.setParameter("locType", parentLocationType);
        return q.setResultTransformer(Transformers.aliasToBean(LocationMasterDto.class)).uniqueResult();
    }

}
