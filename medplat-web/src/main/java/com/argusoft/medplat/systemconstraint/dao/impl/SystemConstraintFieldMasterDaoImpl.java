package com.argusoft.medplat.systemconstraint.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.systemconstraint.dao.SystemConstraintFieldMasterDao;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFieldMaster;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SystemConstraintFieldMasterDaoImpl extends GenericDaoImpl<SystemConstraintFieldMaster, UUID> implements SystemConstraintFieldMasterDao {

    @Override
    public void deleteSystemConstraintFieldConfigByUuid(UUID uuid) {
        String query = "DELETE FROM system_constraint_field_master WHERE cast(uuid as text) = :uuid ;\n" +
                "DELETE FROM system_constraint_field_value_master WHERE cast(field_master_uuid as text) = :uuid ;";
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.setParameter("uuid", uuid.toString());
        sqlQuery.executeUpdate();
    }

    @Override
    public String getSystemConstraintFieldConfigByUuid(UUID uuid) {
        StringBuilder queryString = new StringBuilder();
        queryString
                .append("select\n")
                .append("    cast(json_build_object(\n")
                .append("        'uuid',\n")
                .append("        field.uuid,\n")
                .append("        'formMasterUuid',\n")
                .append("        field.form_master_uuid,\n")
                .append("        'fieldKey',\n")
                .append("        field.field_key,\n")
                .append("        'fieldName',\n")
                .append("        field.field_name,\n")
                .append("        'fieldType',\n")
                .append("        field.field_type,\n")
                .append("        'ngModel',\n")
                .append("        field.ng_model,\n")
                .append("        'appName',\n")
                .append("        field.app_name,\n")
                .append("        'standardFieldMasterUuid',\n")
                .append("        field.standard_field_master_uuid,\n")
                .append("        'createdOn',\n")
                .append("        field.created_on,\n")
                .append("        'createdBy',\n")
                .append("        field.created_by,\n")
                .append("        'systemConstraintFieldValueMasterDtos',\n")
                .append("        (select \n")
                .append("            array_agg(json_build_object(\n")
                .append("                'uuid',\n")
                .append("                field_value.uuid,\n")
                .append("                'fieldMasterUuid',\n")
                .append("                field_value.field_master_uuid,\n")
                .append("                'valueType',\n")
                .append("                field_value.value_type,\n")
                .append("                'key',\n")
                .append("                field_value.key,\n")
                .append("                'value',\n")
                .append("                field_value.value,\n")
                .append("                'defaultValue',\n")
                .append("                field_value.default_value,\n")
                .append("                'enTranslationOfLabel',\n")
                .append("                (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.value and country = 'IN' and language = 'EN' and app_name = 'WEB'),\n")
                .append("                'enTranslationOfDefaultLabel',\n")
                .append("                (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.default_value and country = 'IN' and language = 'EN' and app_name = 'WEB'),\n")
                .append("                'createdOn',\n")
                .append("                field_value.created_on,\n")
                .append("                'createdBy',\n")
                .append("                field_value.created_by\n")
                .append("            )) \n")
                .append("        from system_constraint_field_value_master field_value where field_value.field_master_uuid = field.uuid)\n")
                .append("    ) as text)\n")
                .append("from system_constraint_field_master field where cast(field.uuid as text) = :uuid ;");
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
        sqlQuery.setParameter("uuid", uuid.toString());
        return (String) sqlQuery.list().get(0);
    }

    @Override
    public List<SystemConstraintFieldMasterDto> getSystemConstraintFieldsByFormMasterUuid(UUID formMasterUuid) {
        String query = "select\n" +
                "   field_master.uuid as \"uuid\",\n" +
                "   field_master.form_master_uuid as \"formMasterUuid\",\n" +
                "   field_master.field_key as \"fieldKey\",\n" +
                "   field_master.field_name as \"fieldName\",\n" +
                "   field_master.field_type as \"fieldType\",\n" +
                "   field_master.ng_model as \"ngModel\",\n" +
                "   field_master.app_name as \"appName\",\n" +
                "   field_master.standard_field_master_uuid as \"standardFieldMasterUuid\"\n" +
                "from\n" +
                "   system_constraint_field_master field_master\n" +
                "where\n" +
                "   cast(field_master.form_master_uuid as text) = :formMasterUuid ;";

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        sqlQuery.addScalar("uuid", StandardBasicTypes.UUID_CHAR)
                .addScalar("formMasterUuid", StandardBasicTypes.UUID_CHAR)
                .addScalar("fieldKey", StandardBasicTypes.STRING)
                .addScalar("fieldName", StandardBasicTypes.STRING)
                .addScalar("fieldType", StandardBasicTypes.STRING)
                .addScalar("ngModel", StandardBasicTypes.STRING)
                .addScalar("appName", StandardBasicTypes.STRING)
                .addScalar("standardFieldMasterUuid", StandardBasicTypes.UUID_CHAR)
                .setParameter("formMasterUuid", formMasterUuid.toString())
                .setResultTransformer(Transformers.aliasToBean(SystemConstraintFieldMasterDto.class));

        return sqlQuery.list();
    }
}
