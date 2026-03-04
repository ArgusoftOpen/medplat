package com.argusoft.medplat.internationalization.dao.impl;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.internationalization.dao.InternationalizationLabelDao;
import com.argusoft.medplat.internationalization.model.InternationalizationLabel;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in internationalization dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class InternationalizationLabelDaoImpl extends GenericDaoImpl<InternationalizationLabel, Long> implements InternationalizationLabelDao {

    @Autowired
    private ImtechoSecurityUser imtechoSecurityUser;

    @Override
    public List<InternationalizationLabel> getAllLanguageLabels() {

        String query = "select key, language, text from internationalization_label_master where app_name = 'WEB'";

        NativeQuery<InternationalizationLabel> q = getCurrentSession().createNativeQuery(query)
                .addScalar("key", StandardBasicTypes.STRING)
                .addScalar("language", StandardBasicTypes.STRING)
                .addScalar("text", StandardBasicTypes.STRING);

        return q.setResultTransformer(Transformers.aliasToBean(InternationalizationLabel.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<InternationalizationLabel> getAllLanguageLabelsAfterGivenDate(String labelsMapLastUpdatedAt) {

        String query = "select key, language, text from internationalization_label_master " +
                "where app_name = 'WEB' AND (created_on > '" + labelsMapLastUpdatedAt + "' OR modified_on > '" + labelsMapLastUpdatedAt + "')";

        NativeQuery<InternationalizationLabel> q = getCurrentSession().createNativeQuery(query)
                .addScalar("key", StandardBasicTypes.STRING)
                .addScalar("language", StandardBasicTypes.STRING)
                .addScalar("text", StandardBasicTypes.STRING);

        return q.setResultTransformer(Transformers.aliasToBean(InternationalizationLabel.class)).list();
    }

    @Override
    public InternationalizationLabel getLabelByKeyLanguageAndCountry(String key, String language, String country, String appName) {
        if (key == null || key.isEmpty()) return null;

        String query = "select key, text from internationalization_label_master \n" +
                "where \"key\" = :key and country = :country and \"language\" = :language and app_name = :appName ";

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query);
        return (InternationalizationLabel) sqlQuery.addScalar("key", StandardBasicTypes.STRING)
                .addScalar("text", StandardBasicTypes.STRING)
                .setParameter("key", key)
                .setParameter("country", country)
                .setParameter("language", language)
                .setParameter("appName", appName)
                .setResultTransformer(Transformers.aliasToBean(InternationalizationLabel.class))
                .uniqueResult();
    }

    @Override
    public void createOrUpdateLabel(String key, String value, String language, String country, String appName) {
        if ((key == null || key.isEmpty()) || (value == null || value.isEmpty())) return;

        String query = "INSERT INTO internationalization_label_master (country, \"key\", \"language\", created_by, created_on, custom3b, \"text\", translation_pending, modified_on, app_name) \n" +
                "VALUES ( :country , :key , :language , :createdBy , now() , false , :text , false , now() , :appName ) \n" +
                "ON CONFLICT ON CONSTRAINT internationalization_label_master_pkey \n" +
                "DO \n" +
                "UPDATE \n" +
                "SET \"text\" = :text, modified_on = now() ;\n";
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.setParameter("key", key)
                .setParameter("text", value)
                .setParameter("language", language)
                .setParameter("country", country)
                .setParameter("appName", appName)
                .setParameter("createdBy", imtechoSecurityUser.getId())
                .executeUpdate();
    }
}
