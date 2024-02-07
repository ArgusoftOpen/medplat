/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberDiseaseMedicineDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.MemberTreatmentHistoryDto;
import com.argusoft.medplat.ncd.model.MemberDiseaseMedicine;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods defined in member disease medicine dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberDiseaseMedicineDaoImpl extends GenericDaoImpl<MemberDiseaseMedicine, Integer> implements MemberDiseaseMedicineDao {

    @Override
    public MemberDiseaseMedicine retrieveLastActiveRecordByMemberId(Integer memberId, Integer medicineId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiseaseMedicine> criteriaQuery = criteriaBuilder.createQuery(MemberDiseaseMedicine.class);
        Root<MemberDiseaseMedicine> root = criteriaQuery.from(MemberDiseaseMedicine.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberDiseaseMedicine.Fields.MEMBER_ID), memberId);
        Predicate medicineIdEqual = criteriaBuilder.equal(root.get("medicineId"), medicineId);
        Predicate expiryDate = criteriaBuilder.greaterThan(root.get("expiryDate"), new Date());
        Predicate checkActive = criteriaBuilder.equal(root.get("active"), true);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, expiryDate, checkActive, medicineIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberDiseaseMedicine.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistory(Integer memberId, String diseaseCode) {
        String sql = "with diagnosis as( \n"
                + "select nmr.id, nmr.status, nmr.referred_on, uu.first_name || ' ' || uu.last_name as diagnosed_by, \n"
                + "(case when nmr.disease_code = 'HT' then CONCAT(hyp.systolic_bp, '/', hyp.diastolic_bp) when nmr.disease_code = 'D' then CONCAT('', diab.blood_sugar) else null end) as readings \n"
                + "from ncd_member_referral nmr \n"
                + "inner join um_user uu \n"
                + "on nmr.member_id=:memberId and nmr.disease_code=:diseaseCode and \n"
                + "nmr.created_by=uu.id \n"
                + "left join ncd_member_hypertension_detail hyp on nmr.id = hyp.referral_id\n"
                + "left join ncd_member_diabetes_detail diab on nmr.id = diab.referral_id\n"
                + "), \n"
                + "medicines as ( \n"
                + "select string_agg(name, ', ') as medicine, referral_id from \n"
                + "medicine_master, ncd_member_disesase_medicine m where m.member_id = :memberId and m.disease_code = :diseaseCode and \n"
                + "medicine_master.id = m.medicine_id \n"
                + "group by referral_id \n"
                + ") \n"
                + "select d.readings, d.diagnosed_by as diagnosedBy, d.referred_on as diagnosedOn, medicine, d.status as diagnosis \n"
                + "from diagnosis d \n"
                + "left join medicines m \n"
                + "on d.id = m.referral_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberTreatmentHistoryDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("readings", StandardBasicTypes.STRING)
                .addScalar("diagnosis", StandardBasicTypes.STRING)
                .addScalar("diagnosedBy", StandardBasicTypes.STRING)
                .addScalar("medicine", StandardBasicTypes.STRING)
                .addScalar("diagnosedOn", StandardBasicTypes.TIMESTAMP)
                .setParameter("memberId", memberId)
                .setParameter("diseaseCode", diseaseCode)
                .setResultTransformer(Transformers.aliasToBean(MemberTreatmentHistoryDto.class))
                .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDiseaseMedicine> retrieveMedicinesByReferenceId(Integer referenceId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberDiseaseMedicine.Fields.REFERENCE_ID), referenceId));
            return predicates;
        });
    }
    @Override
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineForUser(Integer memberId) {
        String query="select ncd_member_disesase_medicine.medicine_id \"medicineId\",\n" +
                "listvalue_field_value_detail.value \"medicineName\",\n" +
                "ncd_member_disesase_medicine.frequency \"frequency\",\n" +
                "ncd_member_disesase_medicine.quantity \"quantity\",\n" +
                "ncd_member_disesase_medicine.duration \"duration\",\n" +
                "ncd_member_disesase_medicine.special_instruction \"specialInstruction\",\n" +
                "ncd_member_disesase_medicine.expiry_date \"expiryDate\",\n" +
                "ncd_member_disesase_medicine.diagnosed_on \"issuedDate\",\n" +
                "ncd_member_disesase_medicine.start_date \"startDate\",\n" +
                "ncd_member_disesase_medicine.id \"id\"\n"+
                "from ncd_member_disesase_medicine left join listvalue_field_value_detail on ncd_member_disesase_medicine.medicine_id=listvalue_field_value_detail.id \n" +
                "where ncd_member_disesase_medicine.member_id=:memberId  and ncd_member_disesase_medicine.expiry_date > :expiryDate and (ncd_member_disesase_medicine.is_deleted is null or ncd_member_disesase_medicine.is_deleted = false)\n" +
                "order by ncd_member_disesase_medicine.created_on desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<GeneralDetailMedicineDto> sqlQuery = session.createNativeQuery(query);
        return sqlQuery
                .addScalar("medicineId", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .addScalar("frequency", StandardBasicTypes.INTEGER)
                .addScalar("quantity", StandardBasicTypes.INTEGER)
                .addScalar("specialInstruction", StandardBasicTypes.STRING)
                .addScalar("duration", StandardBasicTypes.INTEGER)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("issuedDate",StandardBasicTypes.TIMESTAMP)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("startDate",StandardBasicTypes.TIMESTAMP)
                .setParameter("memberId", memberId)
                .setParameter("expiryDate", new Date())
                .setResultTransformer(Transformers.aliasToBean(GeneralDetailMedicineDto.class))
                .list();
    }

    @Override
    public MemberDiseaseMedicine retrieveDetailByMemberAndMedicine(Integer memberId, Integer medicineId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiseaseMedicine> criteriaQuery = criteriaBuilder.createQuery(MemberDiseaseMedicine.class);
        Root<MemberDiseaseMedicine> root = criteriaQuery.from(MemberDiseaseMedicine.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberDiseaseMedicine.Fields.MEMBER_ID), memberId);
        Predicate medicineIdEq = criteriaBuilder.equal(root.get("medicineId"), medicineId);
        Predicate expiryDate = criteriaBuilder.greaterThan(root.get("expiryDate"), new Date());
        Predicate checkActive = criteriaBuilder.equal(root.get("active"), true);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, medicineIdEq, expiryDate, checkActive));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberDiseaseMedicine.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineHistoryForUser(Integer memberId) {
        String query="select ncd_member_disesase_medicine.medicine_id \"medicineId\",\n" +
                "listvalue_field_value_detail.value \"medicineName\",\n" +
                "ncd_member_disesase_medicine.frequency \"frequency\",\n" +
                "ncd_member_disesase_medicine.diagnosed_on \"issuedDate\",\n" +
                "ncd_member_disesase_medicine.quantity \"quantity\",\n" +
                "ncd_member_disesase_medicine.duration \"duration\",\n" +
                "ncd_member_disesase_medicine.special_instruction \"specialInstruction\",\n" +
                "ncd_member_disesase_medicine.expiry_date \"expiryDate\",\n" +
                "ncd_member_disesase_medicine.start_date \"startDate\",\n" +
                "ncd_member_disesase_medicine.id \"id\"\n"+
                "from ncd_member_disesase_medicine left join listvalue_field_value_detail on ncd_member_disesase_medicine.medicine_id=listvalue_field_value_detail.id \n" +
                "where ncd_member_disesase_medicine.member_id=:memberId and (ncd_member_disesase_medicine.is_deleted is null or ncd_member_disesase_medicine.is_deleted = false)\n" +
                "order by ncd_member_disesase_medicine.created_on desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<GeneralDetailMedicineDto> sqlQuery = session.createNativeQuery(query);
        return sqlQuery
                .addScalar("medicineId", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .addScalar("frequency", StandardBasicTypes.INTEGER)
                .addScalar("quantity", StandardBasicTypes.INTEGER)
                .addScalar("specialInstruction", StandardBasicTypes.STRING)
                .addScalar("duration", StandardBasicTypes.INTEGER)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("issuedDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("startDate",StandardBasicTypes.TIMESTAMP)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(GeneralDetailMedicineDto.class))
                .list();
    }

    @Override
    public List<GeneralDetailMedicineDto> retrievePrescribedMedicineForMobile(Integer memberId) {
        String query="select ncd_member_disesase_medicine.medicine_id \"medicineId\",\n" +
                "listvalue_field_value_detail.value \"medicineName\",\n" +
                "ncd_member_disesase_medicine.frequency \"frequency\",\n" +
                "ncd_member_disesase_medicine.quantity \"quantity\",\n" +
                "ncd_member_disesase_medicine.duration \"duration\",\n" +
                "ncd_member_disesase_medicine.special_instruction \"specialInstruction\",\n" +
                "ncd_member_disesase_medicine.expiry_date \"expiryDate\",\n" +
                "ncd_member_disesase_medicine.id \"id\"\n"+
                "from ncd_member_disesase_medicine left join listvalue_field_value_detail on ncd_member_disesase_medicine.medicine_id=listvalue_field_value_detail.id \n" +
                "where ncd_member_disesase_medicine.member_id=:memberId  and ncd_member_disesase_medicine.diagnosed_on = (select diagnosed_on from ncd_member_disesase_medicine order by id desc limit 1)\n" +
                "and (ncd_member_disesase_medicine.is_deleted is null or ncd_member_disesase_medicine.is_deleted = false)\n" +
                "order by ncd_member_disesase_medicine.created_on desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<GeneralDetailMedicineDto> sqlQuery = session.createNativeQuery(query);
        return sqlQuery
                .addScalar("medicineId", StandardBasicTypes.INTEGER)
                .addScalar("medicineName", StandardBasicTypes.STRING)
                .addScalar("frequency", StandardBasicTypes.INTEGER)
                .addScalar("quantity", StandardBasicTypes.INTEGER)
                .addScalar("specialInstruction", StandardBasicTypes.STRING)
                .addScalar("duration", StandardBasicTypes.INTEGER)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(GeneralDetailMedicineDto.class))
                .list();
    }

    @Override
    public MemberDiseaseMedicine retrieveByIdAndMemberId(Integer id, Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiseaseMedicine> criteriaQuery = criteriaBuilder.createQuery(MemberDiseaseMedicine.class);
        Root<MemberDiseaseMedicine> root = criteriaQuery.from(MemberDiseaseMedicine.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberDiseaseMedicine.Fields.MEMBER_ID), memberId);
        Predicate idEqual = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, idEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<MemberTreatmentHistoryDto> retrieveTreatmentHistoryDnhdd(Integer memberId, String diseaseCode) {
        String sql = "with diagnosis as( \n"
                + "select nmr.id, nmr.status, nmr.referred_on, uu.first_name || ' ' || uu.last_name as diagnosed_by, \n"
                + "(case when nmr.disease_code = 'HT' then CONCAT(hyp.systolic_bp, '/', hyp.diastolic_bp) when nmr.disease_code = 'D' then CONCAT_WS(',', coalesce(diab.blood_sugar, 0), coalesce(diab.fasting_blood_sugar, 0), coalesce(diab.post_prandial_blood_sugar, 0)) else null end) as readings \n"
                + "from ncd_member_referral nmr \n"
                + "inner join um_user uu \n"
                + "on nmr.member_id=:memberId and nmr.disease_code=:diseaseCode and \n"
                + "nmr.created_by=uu.id and nmr.status is not null\n"
                + "left join ncd_member_hypertension_detail hyp on nmr.id = hyp.referral_id\n"
                + "left join ncd_member_diabetes_detail diab on nmr.id = diab.referral_id\n"
                + "), \n"
                + "medicines as ( \n"
                + "select string_agg(name, ', ') as medicine, referral_id from \n"
                + "medicine_master, ncd_member_disesase_medicine m where m.member_id = :memberId and m.disease_code = :diseaseCode and \n"
                + "medicine_master.id = m.medicine_id \n"
                + "group by referral_id \n"
                + ") \n"
                + "select d.readings, d.diagnosed_by as diagnosedBy, d.referred_on as diagnosedOn, medicine, d.status as diagnosis \n"
                + "from diagnosis d \n"
                + "left join medicines m \n"
                + "on d.id = m.referral_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberTreatmentHistoryDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("readings", StandardBasicTypes.STRING)
                .addScalar("diagnosis", StandardBasicTypes.STRING)
                .addScalar("diagnosedBy", StandardBasicTypes.STRING)
                .addScalar("medicine", StandardBasicTypes.STRING)
                .addScalar("diagnosedOn", StandardBasicTypes.TIMESTAMP)
                .setParameter("memberId", memberId)
                .setParameter("diseaseCode", diseaseCode)
                .setResultTransformer(Transformers.aliasToBean(MemberTreatmentHistoryDto.class))
                .list();
    }
}
