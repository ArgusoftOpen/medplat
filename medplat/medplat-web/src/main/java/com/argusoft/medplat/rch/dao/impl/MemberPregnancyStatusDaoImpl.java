package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.MemberPregnancyStatusDao;
import com.argusoft.medplat.rch.dto.MemberPregnancyStatusBean;
import com.argusoft.medplat.rch.model.MemberPregnancyStatusMaster;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Implementation of methods define in member pregnancy status dao.
 * </p>
 *
 * @author krishna
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberPregnancyStatusDaoImpl extends GenericDaoImpl<MemberPregnancyStatusMaster, Integer> implements MemberPregnancyStatusDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberPregnancyStatusBean> retrievePregnancyStatusForMobile(Integer userId) {
        String query = "with loc as (\n" +
                "\tselect child_id from location_hierchy_closer_det where parent_id in (\n" +
                "\t\tselect ul.loc_id from um_user u \n" +
                "\t\tleft join um_user_location ul on ul.user_id = u.id and ul.state = 'ACTIVE'\n" +
                "\t\twhere u.id = :userId and u.state = 'ACTIVE'\n" +
                "\t)\n" +
                ")\n" +
                "select pr.member_id as \"memberId\", pr.reg_service_date as \"regDate\", pr.anc_visit_dates as \"ancDate\",\n" +
                "pr.bp_details as \"bp\" , pr.hb_details as \"haemoglobin\", pr.calcium_tablets_details as \"calciumTablets\",\n" +
                "pr.weight_details as \"weight\" , pr.urine_test_details as \"urineTest\", pr.ifa_tablets_details as \"ifaTablets\",\n" +
                "pr.fa_tablets_details as \"faTablets\", pr.vaccines_details as \"immunisation\"\n" +
                "from rch_pregnancy_analytics_details pr\n" +
                "where pr.member_current_location_id in (select child_id from loc) and pr.preg_reg_state in ('PENDING','PREGNANT');";

        NativeQuery<MemberPregnancyStatusBean> sqlQuery = getCurrentSession().createNativeQuery(query)
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("regDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("ancDate", StandardBasicTypes.STRING)
                .addScalar("bp", StandardBasicTypes.STRING)
                .addScalar("haemoglobin", StandardBasicTypes.STRING)
                .addScalar("calciumTablets", StandardBasicTypes.STRING)
                .addScalar("faTablets", StandardBasicTypes.STRING)
                .addScalar("ifaTablets", StandardBasicTypes.STRING)
                .addScalar("immunisation", StandardBasicTypes.STRING)
                .addScalar("weight", StandardBasicTypes.STRING)
                .addScalar("urineTest", StandardBasicTypes.STRING);

        sqlQuery.setParameter("userId", userId);
        return sqlQuery.setResultTransformer(Transformers.aliasToBean(MemberPregnancyStatusBean.class)).list();
    }
}
