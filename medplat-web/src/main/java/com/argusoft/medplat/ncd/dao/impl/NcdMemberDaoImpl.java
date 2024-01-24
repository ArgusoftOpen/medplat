package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.NcdMemberDataBean;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class NcdMemberDaoImpl extends GenericDaoImpl<NcdMemberEntity, Integer> implements NcdMemberDao {

    @Override
    public List<NcdMemberDataBean> retrieveNcdMemberDetailsByUserId(Integer userId, Date lastModifiedOn) {
        String query = "with t1 as (select child_id from location_hierchy_closer_det where parent_id in (select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE')) \n" +
                "select id as \"actualId\",\n" +
                "member_id  as \"memberId\",\n" +
                "location_id as \"locationId\",\n" +
                "last_service_date as \"lastServiceDate\",\n" +
                "last_mo_comment as \"lastMoComment\",\n" +
                "mo_confirmed_diabetes as \"moConfirmedDiabetes\",\n" +
                "mo_confirmed_hypertension as \"moConfirmedHypertension\",\n" +
                "mo_confirmed_mental_health as \"moConfirmedMentalHealth\",\n" +
                "suffering_diabetes as \"sufferingDiabetes\",\n" +
                "suffering_hypertension as \"sufferingHypertension\",\n" +
                "suffering_mental_health as \"sufferingMentalHealth\",\n" +
                "diabetes_details as \"diabetesDetails\",\n" +
                "hypertension_details as \"hypertensionDetails\",\n" +
                "mental_health_details as \"mentalHealthDetails\",\n" +
                "diabetes_treatment_status as \"diabetesTreatmentStatus\",\n" +
                "hypertension_treatment_status as \"hypertensionTreatmentStatus\",\n" +
                "mentalHealth_treatment_status as \"mentalHealthTreatmentStatus\",\n" +
                "medicine_details as \"medicineDetails\",\n" +
                "disease_history as \"diseaseHistory\",\n" +
                "reference_due as \"referenceDue\",\n" +
                "diabetes_status as \"diabetesStatus\",\n" +
                "hypertension_status as \"hypertensionStatus\",\n" +
                "mental_health_status as \"mentalHealthStatus\",\n" +
                "last_remark as \"lastRemark\",\n" +
                "modified_on as \"modifiedOn\"\n" +
                "from imt_member_ncd_detail\n" +
                "where location_id in (select * from t1) ";
        if (lastModifiedOn != null) {
            query = query + " and modified_on > :lastModifiedOn";
        }

        NativeQuery<NcdMemberDataBean> q = getCurrentSession().createNativeQuery(query)
                .addScalar("actualId", StandardBasicTypes.INTEGER)
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("lastServiceDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("lastMoComment", StandardBasicTypes.STRING)
                .addScalar("moConfirmedDiabetes", StandardBasicTypes.BOOLEAN)
                .addScalar("moConfirmedHypertension", StandardBasicTypes.BOOLEAN)
                .addScalar("moConfirmedMentalHealth", StandardBasicTypes.BOOLEAN)
                .addScalar("sufferingDiabetes", StandardBasicTypes.BOOLEAN)
                .addScalar("sufferingHypertension", StandardBasicTypes.BOOLEAN)
                .addScalar("sufferingMentalHealth", StandardBasicTypes.BOOLEAN)
                .addScalar("diabetesDetails", StandardBasicTypes.STRING)
                .addScalar("hypertensionDetails", StandardBasicTypes.STRING)
                .addScalar("mentalHealthDetails", StandardBasicTypes.STRING)
                .addScalar("diabetesTreatmentStatus", StandardBasicTypes.STRING)
                .addScalar("hypertensionTreatmentStatus", StandardBasicTypes.STRING)
                .addScalar("mentalHealthTreatmentStatus", StandardBasicTypes.STRING)
                .addScalar("medicineDetails", StandardBasicTypes.STRING)
                .addScalar("diseaseHistory", StandardBasicTypes.STRING)
                .addScalar("diabetesStatus", StandardBasicTypes.STRING)
                .addScalar("hypertensionStatus", StandardBasicTypes.STRING)
                .addScalar("mentalHealthStatus", StandardBasicTypes.STRING)
                .addScalar("lastRemark", StandardBasicTypes.STRING)
                .addScalar("referenceDue", StandardBasicTypes.BOOLEAN)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);
        q.setParameter("userId", userId);
        if (lastModifiedOn != null) {
            q.setParameter("lastModifiedOn", lastModifiedOn);
        }
        return q.setResultTransformer(Transformers.aliasToBean(NcdMemberDataBean.class)).list();
    }

    @Override
    public NcdMemberEntity retrieveNcdMemberByMemberId(Integer memberId) {
        if (Objects.isNull(memberId)) {
            return null;
        }
        PredicateBuilder<NcdMemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("memberId"), memberId));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }
}
