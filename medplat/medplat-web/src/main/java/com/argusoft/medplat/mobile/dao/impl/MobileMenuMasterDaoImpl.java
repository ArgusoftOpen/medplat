package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.MobileMenuMasterDao;
import com.argusoft.medplat.mobile.dto.MenuDataBean;
import com.argusoft.medplat.mobile.model.SohElementModuleMaster;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Implementation of methods define in MobileMenuMasterDao
 * </p>
 *
 * @author rahul
 * @since 13/01/21 6:00 PM
 */
@Repository
@Transactional
public class MobileMenuMasterDaoImpl extends GenericDaoImpl<SohElementModuleMaster, Integer> implements MobileMenuMasterDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuDataBean> retrieveAllMenusForMobile(Date lastUpdatedOn, int roleId) {
        String query = "with datas as (\n" +
                "\tselect id, modified_on, jsonb_array_elements(CAST(config_json AS jsonb)) as conf from mobile_menu_master mmm \n" +
                "\tinner join mobile_menu_role_relation mmrr on mmm.id = mmrr.menu_id and mmrr.role_id = :roleId\n" +
                ")\n" +
                "select t.id as \"actualId\", \n" +
                "cast(t.conf->>'order' as int) as \"order\",\n" +
                "cast(t.conf->>'mobile_constant' as text) as \"constant\",\n" +
                "mf.mobile_display_name as \"displayName\",\n" +
                "t.modified_on as \"modifiedOn\"\n" +
                "from datas t\n" +
                "left join mobile_feature_master mf on cast(t.conf->>'mobile_constant' as text) = mf.mobile_constant \n" +
                "where mf.state = 'ACTIVE'";

        if (lastUpdatedOn != null) {
            query = query + " and t.modified_on > (CAST(:lastModifiedOn AS TIMESTAMP) + CAST('1 second' AS INTERVAL))";
        }

        NativeQuery<MenuDataBean> nativeQuery = getCurrentSession().createNativeQuery(query)
                .addScalar("actualId", StandardBasicTypes.INTEGER)
                .addScalar("order", StandardBasicTypes.INTEGER)
                .addScalar("constant", StandardBasicTypes.STRING)
                .addScalar("displayName", StandardBasicTypes.STRING)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP)
                .setParameter("roleId", roleId);

        if (lastUpdatedOn != null) {
            nativeQuery.setParameter("lastModifiedOn", lastUpdatedOn);
        }
        return nativeQuery.setResultTransformer(Transformers.aliasToBean(MenuDataBean.class)).getResultList();
    }
}
