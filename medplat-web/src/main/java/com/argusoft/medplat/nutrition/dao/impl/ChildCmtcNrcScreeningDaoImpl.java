
package com.argusoft.medplat.nutrition.dao.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.nutrition.dao.ChildCmtcNrcScreeningDao;
import com.argusoft.medplat.nutrition.model.ChildCmtcNrcScreening;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implements methods of ChildCmtcNrcScreeningDao
 * </p>
 *
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Repository
public class ChildCmtcNrcScreeningDaoImpl extends GenericDaoImpl<ChildCmtcNrcScreening, Integer> implements ChildCmtcNrcScreeningDao {

    @Autowired
    ImtechoSecurityUser imtechoSecurityUser;

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcScreening retrieveByChildId(Integer childId) {
        if (childId == null) {
            return null;
        }
        return super.findEntityByCriteriaList((root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(ChildCmtcNrcScreening.Fields.CHILD_ID), childId));
            query.orderBy(builder.desc(root.get(ChildCmtcNrcScreening.Fields.ID)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcScreening retrieveByAdmissionId(Integer admissionId) {
        if (admissionId == null) {
            return null;
        }
        return super.findEntityByCriteriaList((root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(ChildCmtcNrcScreening.Fields.ADMISSION_ID), admissionId));
            predicates.add(builder.isNull(root.get(ChildCmtcNrcScreening.Fields.IS_CASE_COMPLETED)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateAdmissionIdInScreeningInfo(Integer screeningId, Integer admissionid) {
        String query = "update child_cmtc_nrc_screening_detail set state='" + ConstantUtil.CMTC_ACTIVE_STATE + "',admission_id = :admissionId, modified_on = now(), modified_by = :userId where id = :screeningId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("admissionId", admissionid);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        sQLQuery.setParameter(ChildCmtcNrcScreening.Fields.SCREENING_ID, screeningId);
        return sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSdScore(String gender, Integer height, Float weight) {
        String query = " select get_sd_score( :gender , :height , cast( :weight as real) ); ";
        NativeQuery<String> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("gender", gender);
        sQLQuery.setParameter("height", height);
        sQLQuery.setParameter("weight", weight);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateDischargeIdInScreeningInfo(Integer screeningId, Integer id) {
        String query = "update child_cmtc_nrc_screening_detail set discharge_id = :dischargeId,state='" + ConstantUtil.CMTC_DISCHARGE_STATE + "', modified_on = now(), modified_by = :userId where id = :screeningId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("dischargeId", id);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        sQLQuery.setParameter(ChildCmtcNrcScreening.Fields.SCREENING_ID, screeningId);
        return sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateDefualterStateInScreeningInfo(Integer screeningId) {
        String query = "update child_cmtc_nrc_screening_detail set state = '" + ConstantUtil.CMTC_DEFAULTER_STATE + "', modified_on = now(), modified_by = :userId where id = :screeningId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter(ChildCmtcNrcScreening.Fields.SCREENING_ID, screeningId);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        return sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateDeathStatusInScreeningInfo(Integer screeningId) {
        String query = "update child_cmtc_nrc_screening_detail set state = '" + ConstantUtil.CMTC_DEATH_STATE + "', modified_on = now(), modified_by = :userId where id = :screeningId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter(ChildCmtcNrcScreening.Fields.SCREENING_ID, screeningId);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        return sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markChildAsDefaulterCronJob() {
        String query = "create temp table tbl as\n" +
                "(\n" +
                "select child_cmtc_nrc_admission_detail.id as admission_id, max(child_cmtc_nrc_weight_detail.weight_date)\n" +
                "from child_cmtc_nrc_admission_detail\n" +
                "left join child_cmtc_nrc_weight_detail on child_cmtc_nrc_admission_detail.id = child_cmtc_nrc_weight_detail.admission_id\n" +
                "inner join child_cmtc_nrc_screening_detail on child_cmtc_nrc_screening_detail.id = child_cmtc_nrc_admission_detail.case_id\n" +
                "and child_cmtc_nrc_screening_detail.state = '" + ConstantUtil.CMTC_ACTIVE_STATE + "'\n" +
                "and child_cmtc_nrc_screening_detail.is_case_completed is null\n" +
                "where child_cmtc_nrc_admission_detail.defaulter_date is null\n" +
                "and cast(child_cmtc_nrc_admission_detail.admission_date as date) < current_date and cast(child_cmtc_nrc_admission_detail.admission_date as date) > current_date-15\n" +
                "group by child_cmtc_nrc_admission_detail.id\n" +
                "having (cast(child_cmtc_nrc_admission_detail.created_on as date) != cast(child_cmtc_nrc_admission_detail.admission_date as date) \n" +
                "\t\tand max(case when child_cmtc_nrc_weight_detail.weight_date is null then cast(child_cmtc_nrc_admission_detail.admission_date as date) \n" +
                "\t\t\telse child_cmtc_nrc_weight_detail.weight_date end) < (current_date-5))\n" +
                "\t\tor\n" +
                "\t\t(cast(child_cmtc_nrc_admission_detail.created_on as date) = cast(child_cmtc_nrc_admission_detail.admission_date as date)\n" +
                "\t\tand max(case when child_cmtc_nrc_weight_detail.weight_date is null then cast(child_cmtc_nrc_admission_detail.admission_date as date)\n" +
                "\t\t\telse child_cmtc_nrc_weight_detail.weight_date end) < (current_date-3))\n" +
                ");\n" +
                "update child_cmtc_nrc_screening_detail set state='" + ConstantUtil.CMTC_DEFAULTER_STATE + "' from tbl\n" +
                "where child_cmtc_nrc_screening_detail.admission_id = tbl.admission_id;\n" +
                "update child_cmtc_nrc_admission_detail set defaulter_date =(current_date-1) from tbl\n" +
                "where child_cmtc_nrc_admission_detail.id=tbl.admission_id;\n" +
                "update imt_member set child_nrc_cmtc_status='" + ConstantUtil.CMTC_DEFAULTER_STATE + "'\n" +
                "where id in\n" +
                "(select child_id from child_cmtc_nrc_admission_detail,tbl where child_cmtc_nrc_admission_detail.id=tbl.admission_id);\n" +
                "drop table tbl;";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markChildAsDefaulterByFollowUpVisitsCronJob() {
        String query = "create temp table tbl as \n"
                + "(\n"
                + "select child_screening.admission_id,child_discharge.child_id from child_cmtc_nrc_discharge_detail child_discharge \n"
                + "left join child_cmtc_nrc_follow_up child_follow_up on child_discharge.admission_id = child_follow_up.admission_id\n"
                + "inner join child_cmtc_nrc_screening_detail child_screening on child_screening.id= child_discharge.case_id\n"
                + "where child_screening.state = '" + ConstantUtil.CMTC_DISCHARGE_STATE + "'\n"
                + "and child_screening.is_case_completed is null\n"
                + "group by child_discharge.child_id,child_discharge.discharge_date,child_screening.admission_id\n"
                + "having (case when max(child_follow_up.follow_up_visit)=1 then current_date>=(child_discharge.discharge_date+interval '60' day) end) or \n"
                + "(case when max(child_follow_up.follow_up_visit)is null then current_date>=(child_discharge.discharge_date+interval '45' day) end)\n"
                + ");\n"
                + "update child_cmtc_nrc_screening_detail set state='" + ConstantUtil.CMTC_DEFAULTER_STATE + "' from tbl\n"
                + "where child_cmtc_nrc_screening_detail.admission_id = tbl.admission_id;\n"
                + "update child_cmtc_nrc_admission_detail set defaulter_date =current_date from tbl\n"
                + "where child_cmtc_nrc_admission_detail.id=tbl.admission_id;\n"
                + "update imt_member set child_nrc_cmtc_status='" + ConstantUtil.CMTC_DEFAULTER_STATE + "' from tbl\n"
                + "where id = tbl.child_id;"
                + "drop table tbl;";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateScreeningCenter(Integer screeningId, Integer screeningCenter) {
        String query = "update child_cmtc_nrc_screening_detail set screening_center = :screeningCenter, modified_on = now(), modified_by = :userId where id = :screeningId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("screeningCenter", screeningCenter);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        sQLQuery.setParameter(ChildCmtcNrcScreening.Fields.SCREENING_ID, screeningId);
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteChildScreeningByChildId(Integer childId) {
        String query = "delete from child_cmtc_nrc_screening_detail where child_id = :childId and admission_id is null";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("childId", childId);
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCompletedProgramInScreening(Integer caseId) {
        String query = "update child_cmtc_nrc_screening_detail set is_case_completed = true, modified_on = now(), modified_by = :userId where id = :caseId ";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("caseId", caseId);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildCmtcNrcScreening checkAdmissionValidity(Integer childId) {
        String query = "select id,state\n"
                + "from child_cmtc_nrc_screening_detail\n"
                + "where child_id = " + childId + "\n"
                + "and (\n"
                + "(state = '" + ConstantUtil.CMTC_ACTIVE_STATE + "' and admission_id is not null)\n"
                + "or\n"
                + "(state = '" + ConstantUtil.CMTC_DISCHARGE_STATE + "' and is_case_completed is null)\n"
                + "or state in ('" + ConstantUtil.CMTC_WEB_REFERRED + "','" + ConstantUtil.CMTC_DEATH_STATE + "')\n"
                + ")";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<ChildCmtcNrcScreening> q = session.createNativeQuery(query);
        return q
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("state", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ChildCmtcNrcScreening.class))
                .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer retrieveAdmisionIdByChildId(Integer childId) {
        String query = "select admission_id from child_cmtc_nrc_screening_detail where child_id = " + childId + " and is_case_completed is null";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery q = session.createNativeQuery(query);
        Integer admissionId = (Integer) q.uniqueResult();
        if (admissionId == null) {
            query = "with max_id as (\n" +
                    "\tselect max(id) as id from child_cmtc_nrc_screening_detail where child_id = " + childId + "\n" +
                    "\tgroup by child_id\n" +
                    ")select is_case_completed from child_cmtc_nrc_screening_detail where id = (select id from max_id)";
            q = session.createNativeQuery(query);
            boolean isCaseCompleted = (boolean) q.uniqueResult();
            if (isCaseCompleted) {
                throw new ImtechoUserException("Case for this child is already closed", 1);
            } else {
                throw new ImtechoSystemException("Cannot find admission Id of :: " + childId + "(retrieveAdmissionByChildId)", 1);
            }
        }
        return admissionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateHigherFacilityReferralDetails(Integer screeningId, Integer screeningCenter, Integer higherFacilityId) {
        String query = "update child_cmtc_nrc_screening_detail "
                + "set state = '" + ConstantUtil.CMTC_WEB_REFERRED + "',"
                + "referred_from = " + screeningCenter + ","
                + "referred_to = " + higherFacilityId + ","
                + "referred_date = '" + new Date() + "',"
                + "is_archive = null,"
                + "screening_center = null,"
                + "modified_on = now(),"
                + "modified_by = :userId\n"
                + "where id = " + screeningId;
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("userId", imtechoSecurityUser.getId());
        return sQLQuery.executeUpdate();
    }
}
