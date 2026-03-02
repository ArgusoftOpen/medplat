package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.MobileFormDao;
import com.argusoft.medplat.mobile.model.SohElementModuleMaster;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MobileFormDaoImpl extends GenericDaoImpl<SohElementModuleMaster, Integer> implements MobileFormDao {

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public List<String> getFileNames() {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<String> sql = session.createNativeQuery("select file_name from mobile_form_details;");
        return sql.list();
    }

    @Override
    public List<String> getFileNamesByFeature(Integer roleId) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<String> sql = session.createNativeQuery("with features as (\n" +
                "\tselect cast(CAST(jsonb_array_elements(CAST(config_json AS jsonb)) as jsonb)->>'mobile_constant' as text) as const\n" +
                "\tfrom mobile_menu_master mmm\n" +
                "\tinner join mobile_menu_role_relation mmrr on mmm.id = mmrr.menu_id \n" +
                "\tinner join um_role_master urm on mmrr.role_id  = urm.id \n" +
                "\twhere urm.id = :roleId\n" +
                ")\n" +
                "select distinct mfd.file_name \n" +
                "from features f\n" +
                "inner join mobile_feature_master mfm on mfm.mobile_constant = f.const\n" +
                "inner join mobile_form_feature_rel mffr on f.const = mffr.mobile_constant\n" +
                "inner join mobile_form_details mfd on mfd.id = mffr.form_id \n" +
                "where mfm.state = 'ACTIVE';");
        sql.setParameter("roleId",roleId);
        return sql.list();
    }

}
