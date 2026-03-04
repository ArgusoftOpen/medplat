package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.MobileBeanMasterDao;
import com.argusoft.medplat.mobile.model.SohElementModuleMaster;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

import static com.argusoft.medplat.common.component.AliasToEntityLinkedMapResultTransformer.INSTANCE;

/**
 * <p>
 * Add an understandable class description here
 * </p>
 *
 * @author rahul
 * @since 21/05/21 3:32 PM
 */
@Repository
@Transactional
public class MobileBeanMasterDaoImpl extends GenericDaoImpl<SohElementModuleMaster, Integer> implements MobileBeanMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> retrieveBeansListForUserRole(Integer roleId, Boolean dependsOnLastSync) {
        String query = "with features as (\n" +
                "\tselect cast(CAST(jsonb_array_elements(CAST(config_json AS jsonb)) as jsonb)->>'mobile_constant' as text) as const\n" +
                "\tfrom mobile_menu_master mmm\n" +
                "\tinner join mobile_menu_role_relation mmrr on mmm.id = mmrr.menu_id \n" +
                "\tinner join um_role_master urm on mmrr.role_id  = urm.id \n" +
                "\twhere urm.id = :roleId\n" +
                ")\n" +
                "select distinct mbm.bean\n" +
                "from features f\n" +
                "inner join mobile_feature_master mfm on mfm.mobile_constant = f.const\n" +
                "inner join mobile_beans_feature_rel mbrr on f.const = mbrr.feature\n" +
                "inner join mobile_beans_master mbm on mbm.bean = mbrr.bean \n" +
                "where mfm.state = 'ACTIVE'";
        if (dependsOnLastSync != null) {
            query += " and mbm.depends_on_last_sync = :dependsOnLastSync;";
        }

        NativeQuery<String> nativeQuery = getCurrentSession().createNativeQuery(query)
                .addScalar("bean", StandardBasicTypes.STRING)
                .setParameter("roleId", roleId);

        if (dependsOnLastSync != null) {
            nativeQuery.setParameter("dependsOnLastSync", dependsOnLastSync);
        }

        return nativeQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> retrieveAllBeansForUserRole(Integer roleId, Boolean dependsOnLastSync) {
        String query = "with features as (\n" +
                "\tselect cast(CAST(jsonb_array_elements(CAST(config_json AS jsonb)) as jsonb)->>'mobile_constant' as text) as const\n" +
                "\tfrom mobile_menu_master mmm\n" +
                "\tinner join mobile_menu_role_relation mmrr on mmm.id = mmrr.menu_id \n" +
                "\tinner join um_role_master urm on mmrr.role_id  = urm.id \n" +
                "\twhere urm.id = :roleId\n" +
                ")\n" +
                "select distinct mbm.bean, mbm.depends_on_last_sync\n" +
                "from features f\n" +
                "inner join mobile_feature_master mfm on mfm.mobile_constant = f.const\n" +
                "inner join mobile_beans_feature_rel mbrr on f.const = mbrr.feature\n" +
                "inner join mobile_beans_master mbm on mbm.bean = mbrr.bean \n" +
                "where mfm.state = 'ACTIVE'";
        if (dependsOnLastSync != null) {
            query += " and mbm.depends_on_last_sync = :dependsOnLastSync;";
        }

        NativeQuery<LinkedHashMap<String, Object>> nativeQuery = getCurrentSession().createNativeQuery(query)
                .addScalar("bean", StandardBasicTypes.STRING)
                .addScalar("depends_on_last_sync", StandardBasicTypes.BOOLEAN)
                .setParameter("roleId", roleId);

        if (dependsOnLastSync != null) {
            nativeQuery.setParameter("dependsOnLastSync", dependsOnLastSync);
        }

        return nativeQuery.setResultTransformer(INSTANCE).list();
    }
}
