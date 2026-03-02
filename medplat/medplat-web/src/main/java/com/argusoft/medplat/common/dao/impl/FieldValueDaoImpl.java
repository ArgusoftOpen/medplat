package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.FieldValueDao;
import com.argusoft.medplat.common.model.FieldValueMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *     Implements methods of FieldValueDao
 * </p>
 * @author shrey
 * @since 31/08/2020 4:30
 */
@Repository
public class FieldValueDaoImpl extends GenericDaoImpl<FieldValueMaster, Integer> implements FieldValueDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldValueMaster> getFieldValuesByList(List<Integer> keys) {
        // Native query way
        String query = "select\n" +
                "id as \"id\",\n" +
                "field_id as \"fieldId\",\n" +
                "field_value as \"fieldValue\",\n" +
                "created_on as \"createdOn\",\n" +
                "created_by as \"createdBy\",\n" +
                "modified_on as \"modifiedOn\",\n" +
                "modified_by as \"modifiedBy\"\n" +
                "from field_value_master\n" +
                "where field_id in ( :fieldId );";
        NativeQuery<FieldValueMaster> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("fieldId", keys)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("fieldId", StandardBasicTypes.INTEGER)
                .addScalar("fieldValue", StandardBasicTypes.STRING)
                .addScalar("createdOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("modifiedBy", StandardBasicTypes.INTEGER);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(FieldValueMaster.class)).getResultList();
    }
}
