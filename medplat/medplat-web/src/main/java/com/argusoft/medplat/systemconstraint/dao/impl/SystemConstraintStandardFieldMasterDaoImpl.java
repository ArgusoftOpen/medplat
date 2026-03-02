package com.argusoft.medplat.systemconstraint.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.systemconstraint.dao.SystemConstraintStandardFieldMasterDao;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMaster;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SystemConstraintStandardFieldMasterDaoImpl extends GenericDaoImpl<SystemConstraintStandardFieldMaster, UUID> implements SystemConstraintStandardFieldMasterDao {

    @Override
    public String getSystemConstraintFieldConfigByUuid(UUID standardFieldMappingMasterUuid) {
        StringBuilder queryString = new StringBuilder();
        queryString
                .append("select \n")
                .append("    cast(json_build_object(\n")
                .append("        'uuid',\n")
                .append("        field.uuid,\n")
                .append("        'fieldKey',\n")
                .append("        field.field_key,\n")
                .append("        'fieldName',\n")
                .append("        field.field_name,\n")
                .append("        'fieldType',\n")
                .append("        field.field_type,\n")
                .append("        'categoryId',\n")
                .append("        field.category_id,\n")
                .append("        'state',\n")
                .append("        field.state,\n")
                .append("        'standardFieldMappingMasterUuid',\n")
                .append("        mapping.uuid,\n")
                .append("        'createdOn',\n")
                .append("        field.created_on,\n")
                .append("        'createdBy',\n")
                .append("        field.created_by,\n")
                .append("        'systemConstraintStandardFieldValueMasterDtos',\n")
                .append("        (select \n")
                .append("            array_agg(json_build_object(\n")
                .append("                'uuid',\n")
                .append("                field_value.uuid,\n")
                .append("                'standardFieldMappingMasterUuid',\n")
                .append("                field_value.standard_field_mapping_master_uuid,\n")
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
                .append("        from system_constraint_standard_field_value_master field_value where field_value.standard_field_mapping_master_uuid = mapping.uuid)\n")
                .append("    ) as text)\n")
                .append("from\n")
                .append("    system_constraint_standard_field_mapping_master mapping\n")
                .append("inner join\n")
                .append("    system_constraint_standard_field_master field on field.uuid = mapping.standard_field_master_uuid\n")
                .append("where\n")
                .append("    cast(mapping.uuid as text) = :standardFieldMappingMasterUuid ;");
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
        sqlQuery.setParameter("standardFieldMappingMasterUuid", standardFieldMappingMasterUuid.toString());
        return (String) sqlQuery.list().get(0);
    }
}
