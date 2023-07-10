package com.argusoft.medplat.systemconstraint.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.systemconstraint.dao.SystemConstraintFormMasterDao;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFormMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintMobileTemplateConfigDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldMasterDto;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintFormMaster;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class SystemConstraintFormMasterDaoImpl extends GenericDaoImpl<SystemConstraintFormMaster, UUID> implements SystemConstraintFormMasterDao {

    @Override
    public SystemConstraintFormMasterDto getSystemConstraintFormByUuid(UUID uuid) {
        String query = "select\n" +
                "\tform_master.uuid as \"uuid\",\n" +
                "\tform_master.form_name as \"formName\",\n" +
                "\tform_master.form_code as \"formCode\",\n" +
                "\tform_master.menu_config_id as \"menuConfigId\",\n" +
                "\tform_master.web_template_config as \"webTemplateConfig\",\n" +
                "\tmobile.template_config as \"mobileTemplateConfig\",\n" +
                "\tform_master.web_state as \"webState\",\n" +
                "\tform_master.mobile_state as \"mobileState\",\n" +
                "\tmenu.menu_name as \"menuName\"\n" +
                "from\n" +
                "\tsystem_constraint_form_master form_master\n" +
                "inner join\n" +
                "   menu_config menu on\n" +
                "\tmenu.id = form_master.menu_config_id\n" +
                "left join \n" +
                "\tsystem_constraint_form_version mobile on form_master.uuid  = mobile.form_master_uuid \n" +
                "\t\tand mobile.\"type\" ='MOBILE' and mobile.\"version\" =(select max(aa.\"version\") from system_constraint_form_version aa\n" +
                "\t\t\twhere aa.\"type\"='MOBILE' and aa.form_master_uuid = form_master.uuid)\n" +
                "\t\tleft join system_constraint_form_version web on form_master.uuid = web.form_master_uuid \n" +
                "\t\tand web.\"type\" ='WEB'\t\n" +
                "\t\tand web.\"version\"= (select max(aa.\"version\") from system_constraint_form_version aa\n" +
                "\t\t\twhere aa.\"type\"='WEB' and aa.form_master_uuid = form_master.uuid)\n" +
                "where " +
                "   cast(form_master.uuid as text) = :uuid ;";

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        sqlQuery.addScalar("uuid", StandardBasicTypes.UUID_CHAR)
                .addScalar("formName", StandardBasicTypes.STRING)
                .addScalar("formCode", StandardBasicTypes.STRING)
                .addScalar("menuConfigId", StandardBasicTypes.INTEGER)
                .addScalar("webTemplateConfig", StandardBasicTypes.STRING)
                .addScalar("mobileTemplateConfig", StandardBasicTypes.STRING)
                .addScalar("webState", StandardBasicTypes.STRING)
                .addScalar("mobileState", StandardBasicTypes.STRING)
                .addScalar("menuName", StandardBasicTypes.STRING)
                .setParameter("uuid", uuid.toString())
                .setResultTransformer(Transformers.aliasToBean(SystemConstraintFormMasterDto.class));

        return (SystemConstraintFormMasterDto) sqlQuery.uniqueResult();
    }

    @Override
    public List<SystemConstraintFormMasterDto> getSystemConstraintForms(Integer menuConfigId) {
        String query = "\n" +
                "select\n" +
                "\tform_master.uuid as \"uuid\",\n" +
                "\tform_master.form_name as \"formName\",\n" +
                "\tform_master.form_code as \"formCode\",\n" +
                "\tform_master.menu_config_id as \"menuConfigId\",\n" +
                "\tform_master.web_template_config as \"webTemplateConfig\",\n" +
                "\tmobile.template_config as \"mobileTemplateConfig\",\n" +
                "\tform_master.web_state as \"webState\",\n" +
                "\tform_master.mobile_state as \"mobileState\",\n" +
                "\tconcat( menu.menu_type, (case when menu.group_id is not null then ' > ' else '' end), \n" +
                "\tmg.group_name, ' > ', menu.menu_name) as \"menuName\"\n" +
                "from\n" +
                "\tsystem_constraint_form_master form_master\n" +
                "inner join\n" +
                "   menu_config menu on\n" +
                "\tmenu.id = form_master.menu_config_id\n" +
                "left join \n" +
                "\tsystem_constraint_form_version mobile on form_master.uuid  = mobile.form_master_uuid \n" +
                "\t\tand mobile.\"type\" ='MOBILE' and mobile.\"version\" =(select max(aa.\"version\") from system_constraint_form_version aa\n" +
                "\t\t\twhere aa.\"type\"='MOBILE' and aa.form_master_uuid = form_master.uuid)\n" +
                "\t\tleft join system_constraint_form_version web on form_master.uuid = web.form_master_uuid \n" +
                "\t\tand web.\"type\" ='WEB'\t\n" +
                "\t\tand web.\"version\"= (select max(aa.\"version\") from system_constraint_form_version aa\n" +
                "\t\t\twhere aa.\"type\"='WEB' and aa.form_master_uuid = form_master.uuid)\n" +
                "left join menu_group mg on\n" +
                "\tmg.id = menu.group_id\n" +
                "\tand mg.group_type = menu.menu_type\n" +
                "\tand mg.active is true ";

        if (menuConfigId != null) query += "and form_master.menu_config_id = :menuConfigId\n";

        query += "order by form_master.menu_config_id;";
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        if (menuConfigId != null) sqlQuery.setParameter("menuConfigId", menuConfigId);

        sqlQuery.addScalar("uuid", StandardBasicTypes.UUID_CHAR)
                .addScalar("formName", StandardBasicTypes.STRING)
                .addScalar("formCode", StandardBasicTypes.STRING)
                .addScalar("menuConfigId", StandardBasicTypes.INTEGER)
                .addScalar("webTemplateConfig", StandardBasicTypes.STRING)
                .addScalar("mobileTemplateConfig", StandardBasicTypes.STRING)
                .addScalar("webState", StandardBasicTypes.STRING)
                .addScalar("mobileState", StandardBasicTypes.STRING)
                .addScalar("menuName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(SystemConstraintFormMasterDto.class));

        return sqlQuery.list();
    }

    @Override
    public String getSystemConstraintFormConfigByUuid(UUID uuid, String appName) {
        Session session = sessionFactory.getCurrentSession();
        String query = "select\n" +
                "    cast(json_build_object(\n" +
                "        'systemConstraintFormMasterDto',\n" +
                "        json_build_object(\n" +
                "            'uuid',\n" +
                "            form.uuid,\n" +
                "            'formName',\n" +
                "            form.\"form_name\",\n" +
                "            'formCode',\n" +
                "            form.\"form_code\",\n" +
                "            'menuConfigId',\n" +
                "            form.\"menu_config_id\",\n" +
                "            'webTemplateConfig',\n" +
                "            form.\"web_template_config\",\n" +
                "            'mobileTemplateConfig',\n" +
                "            mobile.\"template_config\",\n" +
                "            'menuName',\n" +
                "            menu.menu_name,\n" +
                "            'webState',\n" +
                "            form.\"web_state\",\n" +
                "            'mobileState',\n" +
                "            form.\"mobile_state\",\n" +
                "            'createdBy',\n" +
                "            form.\"created_by\",\n" +
                "            'createdOn',\n" +
                "            form.\"created_on\"\n" +
                "        ),\n" +
                "        'systemConstraintFieldMasterDtos',\n" +
                "        (\n" +
                "            select \n" +
                "                array_agg(json_build_object(\n" +
                "                    'uuid',\n" +
                "                    field.uuid,\n" +
                "                    'formMasterUuid',\n" +
                "                    field.form_master_uuid,\n" +
                "                    'fieldKey',\n" +
                "                    field.field_key,\n" +
                "                    'fieldName',\n" +
                "                    field.field_name,\n" +
                "                    'fieldType',\n" +
                "                    field.field_type,\n" +
                "                    'ngModel',\n" +
                "                    field.ng_model,\n" +
                "                    'appName',\n" +
                "                    field.app_name,\n" +
                "                    'standardFieldMasterUuid',\n" +
                "                    field.standard_field_master_uuid,\n" +
                "                    'createdOn',\n" +
                "                    field.created_on,\n" +
                "                    'createdBy',\n" +
                "                    field.created_by,\n" +
                "                    'systemConstraintFieldValueMasterDtos',\n" +
                "                    (select \n" +
                "                        array_agg(json_build_object(\n" +
                "                            'uuid',\n" +
                "                            field_value.uuid,\n" +
                "                            'fieldMasterUuid',\n" +
                "                            field_value.field_master_uuid,\n" +
                "                            'valueType',\n" +
                "                            field_value.value_type,\n" +
                "                            'key',\n" +
                "                            field_value.key,\n" +
                "                            'value',\n" +
                "                            field_value.value,\n" +
                "                            'defaultValue',\n" +
                "                            field_value.default_value,\n" +
                "                            'enTranslationOfLabel',\n" +
                "                            (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.value and country = 'IN' and language = 'EN' and app_name in ('WEB','TECHO_MOBILE_APP') limit 1 ),\n" +
                "                            'enTranslationOfDefaultLabel',\n" +
                "                            (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.default_value and country = 'IN' and language = 'EN' and app_name in ('WEB','TECHO_MOBILE_APP') limit 1 ),\n" +
                "                            'createdOn',\n" +
                "                            field_value.created_on,\n" +
                "                            'createdBy',\n" +
                "                            field_value.created_by\n" +
                "                        )) \n" +
                "                    from system_constraint_field_value_master field_value where field_value.field_master_uuid = field.uuid)\n" +
                "                ))\n" +
                "            from system_constraint_field_master field where field.form_master_uuid = form.uuid";
        if (appName != null) {
            query += "and field.app_name in (" + appName + ")\n";
        }
        query += ")\n" +
                "    ) as text)\n" +
                "from\n" +
                "\tsystem_constraint_form_master form\n" +
                "inner join\n" +
                "    menu_config menu on\n" +
                "\tmenu.id = form.menu_config_id\n" +
                "left join \n" +
                "\tsystem_constraint_form_version mobile on\n" +
                "\tform.uuid = mobile.form_master_uuid\n" +
                "\tand mobile.\"type\" = 'MOBILE'\n" +
                "\tand mobile.\"version\" =(\n" +
                "\tselect\n" +
                "\t\tmax(aa.\"version\")\n" +
                "\tfrom\n" +
                "\t\tsystem_constraint_form_version aa\n" +
                "\twhere\n" +
                "\t\taa.\"type\" = 'MOBILE' and aa.form_master_uuid = form.uuid)\n" +
                "left join\n" +
                "\t\tsystem_constraint_form_version web on\n" +
                "\tform.uuid = web.form_master_uuid\n" +
                "\tand web.\"type\" = 'WEB'\n" +
                "\tand web.\"version\" = (\n" +
                "\tselect\n" +
                "\t\tmax(aa.\"version\")\n" +
                "\tfrom\n" +
                "\t\tsystem_constraint_form_version aa\n" +
                "\twhere\n" +
                "\t\taa.\"type\" = 'WEB' and aa.form_master_uuid = form.uuid)\n" +
                "where\n" +
                "     cast(form.uuid as text) = :uuid ;";
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.setParameter("uuid", uuid.toString());
        List<String> strings = sqlQuery.list();
        if (CollectionUtils.isEmpty(strings)) {
            throw new ImtechoUserException("Form not found!", 0);
        }
        return strings.get(0);
    }

    @Override
    public List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFields(Boolean fetchOnlyActive) {
        String query = "select\n" +
                "   standard_field_master.uuid as \"uuid\",\n" +
                "   standard_field_master.field_key as \"fieldKey\",\n" +
                "   standard_field_master.field_name as \"fieldName\",\n" +
                "   standard_field_master.field_type as \"fieldType\",\n" +
                "   standard_field_master.category_id as \"categoryId\",\n" +
                "   standard_field_master.state as \"state\",\n" +
                "   standard_field_master.created_on as \"createdOn\",\n" +
                "   standard_field_master.created_by as \"createdBy\",\n" +
                "   list.value as \"categoryName\"\n" +
                "from\n" +
                "   system_constraint_standard_field_master standard_field_master\n" +
                "left join\n" +
                "   listvalue_field_value_detail list on list.field_key = 'system_constraint_standard_field_categories' and standard_field_master.category_id = list.id and is_active is true\n";

        if (fetchOnlyActive.equals(Boolean.TRUE)) {
            query += "where standard_field_master.state = 'ACTIVE' \n";
        }
        query += "order by standard_field_master.category_id;";

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.addScalar("uuid", StandardBasicTypes.UUID_CHAR)
                .addScalar("fieldKey", StandardBasicTypes.STRING)
                .addScalar("fieldName", StandardBasicTypes.STRING)
                .addScalar("fieldType", StandardBasicTypes.STRING)
                .addScalar("categoryId", StandardBasicTypes.INTEGER)
                .addScalar("categoryName", StandardBasicTypes.STRING)
                .addScalar("state", StandardBasicTypes.STRING)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(SystemConstraintStandardFieldMasterDto.class));
        return sqlQuery.list();
    }

    @Override
    public String getSystemConstraintStandardConfigById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString
                .append("select\n")
                .append("   cast(json_build_object(\n")
                .append("       'standardId',\n")
                .append("       list.id,\n")
                .append("       'standardName',\n")
                .append("       list.\"value\",\n")
                .append("       'systemConstraintStandardFieldMasterDtos',\n")
                .append("        (\n")
                .append("            select \n")
                .append("                array_agg(json_build_object(\n")
                .append("                    'uuid',\n")
                .append("                    field.uuid,\n")
                .append("                    'fieldKey',\n")
                .append("                    field.field_key,\n")
                .append("                    'fieldName',\n")
                .append("                    field.field_name,\n")
                .append("                    'fieldType',\n")
                .append("                    field.field_type,\n")
                .append("                    'categoryId',\n")
                .append("                    field.category_id,\n")
                .append("                    'state',\n")
                .append("                    field.state,\n")
                .append("                    'standardFieldMappingMasterUuid',\n")
                .append("                    mapping.uuid,\n")
                .append("                    'createdOn',\n")
                .append("                    field.created_on,\n")
                .append("                    'createdBy',\n")
                .append("                    field.created_by,\n")
                .append("                    'systemConstraintStandardFieldValueMasterDtos',\n")
                .append("                    (select \n")
                .append("                        array_agg(json_build_object(\n")
                .append("                            'uuid',\n")
                .append("                            field_value.uuid,\n")
                .append("                            'standardFieldMappingMasterUuid',\n")
                .append("                            field_value.standard_field_mapping_master_uuid,\n")
                .append("                            'valueType',\n")
                .append("                            field_value.value_type,\n")
                .append("                            'key',\n")
                .append("                            field_value.key,\n")
                .append("                            'value',\n")
                .append("                            field_value.value,\n")
                .append("                            'defaultValue',\n")
                .append("                            field_value.default_value,\n")
                .append("                            'enTranslationOfLabel',\n")
                .append("                            (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.value and country = 'IN' and language = 'EN' and app_name = 'WEB'),\n")
                .append("                            'enTranslationOfDefaultLabel',\n")
                .append("                            (select text from internationalization_label_master where field_value.key = 'label' and key = field_value.default_value and country = 'IN' and language = 'EN' and app_name = 'WEB'),\n")
                .append("                            'createdOn',\n")
                .append("                            field_value.created_on,\n")
                .append("                            'createdBy',\n")
                .append("                            field_value.created_by\n")
                .append("                        )) \n")
                .append("                    from system_constraint_standard_field_value_master field_value where field_value.standard_field_mapping_master_uuid = mapping.uuid)\n")
                .append("                ))\n")
                .append("            from\n")
                .append("                system_constraint_standard_field_master field\n")
                .append("            inner join\n")
                .append("                system_constraint_standard_field_mapping_master mapping on mapping.standard_field_master_uuid = field.uuid and mapping.standard_master_id = list.id\n")
                .append("       )\n")
                .append("    ) as text)\n")
                .append("from\n")
                .append("    listvalue_field_value_detail list\n")
                .append("where\n")
                .append("    list.id = :id ;");
        SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
        sqlQuery.setParameter("id", id);
        List<String> strings = sqlQuery.list();
        if (CollectionUtils.isEmpty(strings)) {
            throw new ImtechoUserException("Standard not found!", 0);
        }
        return strings.get(0);
    }

    @Override
    public List<SystemConstraintMobileTemplateConfigDto> getMobileTemplateConfig(String formCode) {
        String query = "with details as (\n" +
                "select\n" +
                "\tform_master.uuid,\n" +
                "\tform_master.form_code,\n" +
                "\tconfigurations.questions,\n" +
                "\tconfigurations.\"pageNumber\" as page\n" +
                "from\n" +
                "\tsystem_constraint_form_master form_master\n" +
                "left join \n" +
                "    \tsystem_constraint_form_version form_version on\n" +
                "\tform_master.uuid = form_version.form_master_uuid ,\n" +
                "\tjsonb_to_recordset(\n" +
                "            cast(\n" +
                "                form_version.template_config as jsonb\n" +
                "            )\n" +
                "        ) as configurations(\"questions\" jsonb,\n" +
                "\t\"pageNumber\" text)\n" +
                "where\n" +
                "\tform_code = :formCode\n" +
                "\tand form_version.\"version\" =(select cast(sc.key_value as INTEGER) from system_configuration sc\n" +
                "\t\twhere sc.system_key ='MOBILE_FORM_VERSION' )\n" +
                "),\n" +
                "field_details as (\n" +
                "select\n" +
                "\tdetails.uuid,\n" +
                "\tdetails.form_code,\n" +
                "\tdetails.page,\n" +
                "\tquestion.field,\n" +
                "\tquestion.\"fieldName\" as field_name,\n" +
                "\tquestion.\"nextField\" as next_field_uuid,\n" +
                "\tquestion.\"nextFieldJson\" as next_field_json,\n" +
                "\tquestion.\"systemConstraintFieldValueMasterDtos\" as field_data,\n" +
                "\tcase\n" +
                "\t\twhen question.\"fieldType\" = 'DROPDOWN' then 'CBDS'\n" +
                "\t\twhen question.\"fieldType\" = 'RADIO' then 'RB'\n" +
                "\t\twhen question.\"fieldType\" = 'DATE' then 'CDB'\n" +
                "\t\twhen question.\"fieldType\" = 'LONG_TEXT' then 'TA'\n" +
                "\t\twhen question.\"fieldType\" = 'SHORT_TEXT'\n" +
                "\t\t\tor question.\"fieldType\" = 'NUMBER' then 'TB'\n" +
                "\t\t\twhen question.\"fieldType\" = 'INFORMATION_DISPLAY' then 'L'\n" +
                "\t\t\twhen question.\"fieldType\" = 'CHECKBOX' then 'SCB'\n" +
                "\t\t\telse question.\"fieldType\"\n" +
                "\t\tend as field_type\n" +
                "\tfrom\n" +
                "\t\tdetails,\n" +
                "\t\tjsonb_to_recordset(details.questions) as question (\n" +
                "            \"field\" text,\n" +
                "\t\t\"nextField\" text,\n" +
                "\t\t\"nextFieldJson\" jsonb,\n" +
                "\t\t\"systemConstraintFieldValueMasterDtos\" jsonb,\n" +
                "\t\t\"fieldName\" text,\n" +
                "\t\t\"fieldType\" text\n" +
                "        )\n" +
                "),\n" +
                "field_value_details as (\n" +
                "select\n" +
                "\tfield.uuid,\n" +
                "\tfield.\"fieldMasterUuid\" as field_master_uuid,\n" +
                "\tfield.\"valueType\" as value_type,\n" +
                "\tfield.key,\n" +
                "\tfield.value,\n" +
                "\tfield.\"defaultValue\" as default_value\n" +
                "from\n" +
                "\tfield_details,\n" +
                "\tjsonb_to_recordset(field_details.field_data) as field (\n" +
                "            uuid text,\n" +
                "\t\"fieldMasterUuid\" text,\n" +
                "\t\"valueType\" text,\n" +
                "\tkey text,\n" +
                "\tvalue text,\n" +
                "\t\"defaultValue\" text,\n" +
                "\tenTranslationOfLabel text,\n" +
                "\tenTranslationOfDefaultLabel text,\n" +
                "\tcreatedOn date,\n" +
                "\tcreatedBy text\n" +
                "        )\n" +
                "),\n" +
                "next_field_json as (\n" +
                "select\n" +
                "\tfield_details.field,\n" +
                "\tnext_json.key,\n" +
                "\tnext_json.value,\n" +
                "\tnext_json.\"nextField\" as next_field_uuid,\n" +
                "\tcase\n" +
                "\t\twhen field_value_details.value is not null then field_value_details.value\n" +
                "\t\telse field_value_details.default_value\n" +
                "\tend as next\n" +
                "from\n" +
                "\tfield_details,\n" +
                "\tjsonb_to_recordset(field_details.next_field_json) as next_json(key text,\n" +
                "\tvalue text,\n" +
                "\t\"nextField\" text)\n" +
                "left join field_value_details on\n" +
                "\tnext_json.\"nextField\" = cast(\n" +
                "            field_value_details.field_master_uuid as text\n" +
                "        )\n" +
                "\t\tand field_value_details.key = 'mobileQuestionNumber'\n" +
                "),\n" +
                "\n" +
                "next_field_json_agg as (\n" +
                "    select next_field_json.field,\n" +
                "        cast(json_agg(next_field_json) as text) as next_field_json\n" +
                "    from next_field_json\n" +
                "    group by next_field_json.field\n" +
                ")\n" +
                "select case\n" +
                "        when field_id.value is not null then field_id.value\n" +
                "        else field_id.default_value\n" +
                "    end as id,\n" +
                "    field_details.form_code as formCode,\n" +
                "    case\n" +
                "        when field_title.value is not null then field_title.value\n" +
                "        else (\n" +
                "            case\n" +
                "                when field_title.default_value is null then ''\n" +
                "                else field_title.default_value\n" +
                "            end\n" +
                "        )\n" +
                "    end as title,\n" +
                "    case\n" +
                "        when field_subtitle.value is not null then field_subtitle.value\n" +
                "        else (\n" +
                "            case\n" +
                "                when field_subtitle.default_value is null then ''\n" +
                "                else field_subtitle.default_value\n" +
                "            end\n" +
                "        )\n" +
                "    end as subTitle,\n" +
                "    case\n" +
                "        when field_instruction.value is not null then field_instruction.value\n" +
                "        else (\n" +
                "            case\n" +
                "                when field_instruction.default_value is null then ''\n" +
                "                else field_instruction.default_value\n" +
                "            end\n" +
                "        )\n" +
                "    end as instruction,\n" +
                "    field_details.field_name as question,\n" +
                "    field_details.field_type as type,\n" +
                "    case\n" +
                "        when field_required.value is not null then (\n" +
                "            case\n" +
                "                when field_required.value = 'true' then 'T'\n" +
                "                else 'F'\n" +
                "            end\n" +
                "        )\n" +
                "        else (\n" +
                "            case\n" +
                "                when field_required.default_value = 'true' then 'T'\n" +
                "                else 'F'\n" +
                "            end\n" +
                "        )\n" +
                "    end as isMandatory,\n" +
                "    case\n" +
                "        when field_requiredmessage.value is not null then field_requiredmessage.value\n" +
                "        else field_requiredmessage.default_value\n" +
                "    end as mandatoryMessage,\n" +
                "    case\n" +
                "        when field_length.value is not null then field_length.value\n" +
                "        else field_length.default_value\n" +
                "    end as length,\n" +
                "    case\n" +
                "        when field_validation.value is not null then field_validation.value\n" +
                "        else field_validation.default_value\n" +
                "    end as validations,\n" +
                "    case\n" +
                "        when field_formulas.value is not null then field_formulas.value\n" +
                "        else field_formulas.default_value\n" +
                "    end as formulas,\n" +
                "    case\n" +
                "        when field_datamap.value is not null then field_datamap.value\n" +
                "        else field_datamap.default_value\n" +
                "    end as dataMap,\n" +
                "    case\n" +
                "        when next_field_json_agg.next_field_json is not null then next_field_json_agg.next_field_json\n" +
                "        else (\n" +
                "            case\n" +
                "                when option_type.default_value = 'listValueField' then cast(\n" +
                "                    array(\n" +
                "                        select json_build_object(\n" +
                "                                'key',\n" +
                "                                listvalue_field_value_detail.value,\n" +
                "                                'value',\n" +
                "                                listvalue_field_value_detail.value\n" +
                "                            )\n" +
                "                        from listvalue_field_value_detail\n" +
                "                        where field_key = (\n" +
                "                                select field_key\n" +
                "                                from listvalue_field_master\n" +
                "                                where field = list_value_field.default_value\n" +
                "                            )\n" +
                "                            and is_active = true\n" +
                "                    ) as text\n" +
                "                )\n" +
                "                else static_options.default_value\n" +
                "            end\n" +
                "        )\n" +
                "    end as options,\n" +
                "     case\n" +
                "            when next_field.value is not null then next_field.value\n" +
                "            else next_field.default_value\n" +
                "        end as next,\n" +
                "    case\n" +
                "        when field_relatedpropertyname.value is not null then field_relatedpropertyname.value\n" +
                "        else field_relatedpropertyname.default_value\n" +
                "    end as relatedPropertyName,\n" +
                "    case\n" +
                "        when field_hidden.value is not null then (\n" +
                "            case\n" +
                "                when field_hidden.value = 'true' then 'T'\n" +
                "                else 'F'\n" +
                "            end\n" +
                "        )\n" +
                "        else (\n" +
                "            case\n" +
                "                when field_hidden.default_value = 'true' then 'T'\n" +
                "                else 'F'\n" +
                "            end\n" +
                "        )\n" +
                "    end as isHidden,\n" +
                "    case\n" +
                "        when field_event.value is not null then field_event.value\n" +
                "        else field_event.default_value\n" +
                "    end as event,\n" +
                "    case\n" +
                "        when field_binding.value is not null then field_binding.value\n" +
                "        else field_binding.default_value\n" +
                "    end as binding,\n" +
                "    field_details.page,\n" +
                "    case\n" +
                "        when field_hint.value is not null then field_hint.value\n" +
                "        else field_hint.default_value\n" +
                "    end as hint,\n" +
                "    case\n" +
                "        when field_helpvideofield.value is not null then field_helpvideofield.value\n" +
                "        else field_helpvideofield.default_value\n" +
                "    end as helpVideo\n" +
                "from field_details\n" +
                "    left join next_field_json_agg on field_details.field = next_field_json_agg.field\n" +
                "    left join field_value_details field_title on field_details.field = cast(field_title.field_master_uuid as text)\n" +
                "    and field_title.key = 'mobileTitle'\n" +
                "    left join field_value_details field_subtitle on field_details.field = cast(field_subtitle.field_master_uuid as text)\n" +
                "    and field_subtitle.key = 'mobileSubtitle'\n" +
                "    left join field_value_details field_instruction on field_details.field = cast(field_instruction.field_master_uuid as text)\n" +
                "    and field_instruction.key = 'mobileInstruction'\n" +
                "    left join field_value_details field_question on field_details.field = cast(field_question.field_master_uuid as text)\n" +
                "    and field_question.key = 'label'\n" +
                "    left join field_value_details field_validation on field_details.field = cast(field_validation.field_master_uuid as text)\n" +
                "    and field_validation.key = 'mobileValidation'\n" +
                "    left join field_value_details field_formulas on field_details.field = cast(field_formulas.field_master_uuid as text)\n" +
                "    and field_formulas.key = 'mobileFormulas'\n" +
                "    left join field_value_details field_datamap on field_details.field = cast(field_datamap.field_master_uuid as text)\n" +
                "    and field_datamap.key = 'mobileDataMap'\n" +
                "    left join field_value_details field_relatedpropertyname on field_details.field = cast(\n" +
                "        field_relatedpropertyname.field_master_uuid as text\n" +
                "    )\n" +
                "    and field_relatedpropertyname.key = 'mobileRelatedPropertyName'\n" +
                "    left join field_value_details field_binding on field_details.field = cast(field_binding.field_master_uuid as text)\n" +
                "    and field_binding.key = 'mobileBinding'\n" +
                "    left join field_value_details field_tooltip on field_details.field = cast(field_tooltip.field_master_uuid as text)\n" +
                "    and field_tooltip.key = 'mobileTooltip'\n" +
                "    left join field_value_details field_helpvideofield on field_details.field = cast(field_helpvideofield.field_master_uuid as text)\n" +
                "    and field_helpvideofield.key = 'mobileHelpVideoField'\n" +
                "    left join field_value_details field_event on field_details.field = cast(field_event.field_master_uuid as text)\n" +
                "    and field_event.key = 'mobileEvent'\n" +
                "    left join field_value_details field_hint on field_details.field = cast(field_hint.field_master_uuid as text)\n" +
                "    and field_hint.key = 'mobileHint'\n" +
                "    left join field_value_details field_required on field_details.field = cast(field_required.field_master_uuid as text)\n" +
                "    and field_required.key = 'isRequired'\n" +
                "    left join field_value_details field_requiredmessage on field_details.field = cast(field_requiredmessage.field_master_uuid as text)\n" +
                "    and field_requiredmessage.key = 'requiredMessage'\n" +
                "    left join field_value_details field_hidden on field_details.field = cast(field_hidden.field_master_uuid as text)\n" +
                "    and field_hidden.key = 'isHidden'\n" +
                "    left join field_value_details field_id on field_details.field = cast(field_id.field_master_uuid as text)\n" +
                "    and field_id.key = 'mobileQuestionNumber'\n" +
                "    left join field_value_details field_length on field_details.field = cast(field_length.field_master_uuid as text)\n" +
                "    and field_length.key = 'maxLength'\n" +
                "    left join field_value_details static_options on field_details.field = cast(static_options.field_master_uuid as text)\n" +
                "    and static_options.key = 'staticOptions'\n" +
                "    left join field_value_details list_value_field on field_details.field = cast(list_value_field.field_master_uuid as text)\n" +
                "    and list_value_field.key = 'listValueField'\n" +
                "    left join field_value_details query_builder on field_details.field = cast(query_builder.field_master_uuid as text)\n" +
                "    and query_builder.key = 'queryBuilder'\n" +
                "    left join field_value_details option_type on field_details.field = cast(option_type.field_master_uuid as text)\n" +
                "    and option_type.key = 'optionsType'\n" +
                "    left join field_value_details next_field on field_details.next_field_uuid= cast(next_field.field_master_uuid as text)\n" +
                "    and next_field.key = 'mobileQuestionNumber'\n" +
                "ORDER by id";
        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);
        if (formCode != null) {
            sqlQuery.setParameter("formCode", formCode);
        }
        sqlQuery.addScalar("id", StandardBasicTypes.STRING)
                .addScalar("formCode", StandardBasicTypes.STRING)
                .addScalar("title", StandardBasicTypes.STRING)
                .addScalar("subTitle", StandardBasicTypes.STRING)
                .addScalar("instruction", StandardBasicTypes.STRING)
                .addScalar("question", StandardBasicTypes.STRING)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("isMandatory", StandardBasicTypes.STRING)
                .addScalar("mandatoryMessage", StandardBasicTypes.STRING)
                .addScalar("length", StandardBasicTypes.STRING)
                .addScalar("validations", StandardBasicTypes.STRING)
                .addScalar("formulas", StandardBasicTypes.STRING)
                .addScalar("dataMap", StandardBasicTypes.STRING)
                .addScalar("options", StandardBasicTypes.STRING)
                .addScalar("next", StandardBasicTypes.STRING)
                .addScalar("relatedPropertyName", StandardBasicTypes.STRING)
                .addScalar("isHidden", StandardBasicTypes.STRING)
                .addScalar("event", StandardBasicTypes.STRING)
                .addScalar("binding", StandardBasicTypes.STRING)
                .addScalar("page", StandardBasicTypes.STRING)
                .addScalar("hint", StandardBasicTypes.STRING)
                .addScalar("helpVideo", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(SystemConstraintMobileTemplateConfigDto.class));
        return sqlQuery.list();
    }

    @Override
    public List<String> getActiveMobileForms() {
        Session session = sessionFactory.getCurrentSession();
        String query = "select form_code from system_constraint_form_master scfm\n" +
                "\t\tleft join system_constraint_form_version scfv on scfm.uuid =scfv.form_master_uuid \n" +
                "\t\t\tand scfv.\"type\" ='MOBILE'\n" +
                "\t where \n" +
                "\tscfm.mobile_state ='ACTIVE' and scfv.\"version\" =(select CAST(sc.key_value as INTEGER) from system_configuration sc\n" +
                "\t\twhere sc.system_key ='MOBILE_FORM_VERSION'\n" +
                "\t\t);";
        SQLQuery sqlQuery = session.createSQLQuery(query);
        List<String> strings = sqlQuery.list();
        return strings;
    }

    @Override
    public String getSystemConstraintConfigsByMenuConfigId(Integer menuConfigId) {
        Session session = sessionFactory.getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString
                .append("with language_with_priority as (\n")
                .append("   select\n")
                .append("       lang,\n")
                .append("       priority\n")
                .append("   from\n")
                .append("       (select prefered_language as lang, 1 as priority from um_user where id = 1) as preferred_lang\n")
                .append("       union \n")
                .append("       (select 'EN' as lang, 2 as priority)\n")
                .append(")")
                .append("select\n")
                .append("   cast(\n")
                .append("       json_object_agg(\n")
                .append("           form_master.form_name,\n")
                .append("           (select\n")
                .append("               json_object_agg(\n")
                .append("                   concat(field_master.field_name, '__',field_master.field_type),\n")
                .append("                   (select\n")
                .append("                       json_object_agg(\n")
                .append("                           r.key,\n")
                .append("                           (case\n")
                .append("                               when (r.key = 'listValueField' and r.field_value is not null) then (select cast(json_agg(list.*) as text) from listvalue_field_value_detail list where field_key = (select field_key from listvalue_field_master where field = r.field_value) and is_active = true)\n")
                .append("                               when (r.key = 'label' and r.field_value is not null) then (select text from internationalization_label_master, language_with_priority lwp where key = r.field_value and app_name = 'WEB' and \"language\" in (lwp.lang) order by lwp.priority limit 1)\n")
                .append("                               else r.field_value\n")
                .append("                           end)\n")
                .append("                        )\n")
                .append("                   from (\n")
                .append("                        select\n")
                .append("                           value_type,\n")
                .append("                           key,\n")
                .append("                           value,\n")
                .append("                           default_value,\n")
                .append("                           case\n")
                .append("                               when value is not null and trim(value) <> '' then trim(value)\n")
                .append("                               when default_value is not null and trim(default_value) <> '' then trim(default_value)\n")
                .append("                               else null\n")
                .append("                           end as field_value\n")
                .append("                       from\n")
                .append("                           system_constraint_field_value_master field_value_master\n")
                .append("                       where\n")
                .append("                           field_value_master.field_master_uuid = field_master.uuid\n")
                .append("                           and field_value_master.key not in (\n")
                .append("                               select\n")
                .append("                                   s_value.key\n")
                .append("                               from\n")
                .append("                                   system_constraint_standard_field_mapping_master s_mapping\n")
                .append("                               inner join\n")
                .append("                                   system_constraint_standard_field_value_master s_value on s_mapping.uuid = s_value.standard_field_mapping_master_uuid\n")
                .append("                               where\n")
                .append("                                   cast(s_mapping.standard_master_id as text) = (select key_value from system_configuration where system_key = 'SYSTEM_CONSTRAINT_ACTIVE_STANDARD_ID' and is_active is true)\n")
                .append("                                   and s_mapping.standard_field_master_uuid = field_master.standard_field_master_uuid\n")
                .append("                                   and (select state from system_constraint_standard_field_master where uuid = field_master.standard_field_master_uuid) = 'ACTIVE'\n")
                .append("                           )\n")
                .append("                       union\n")
                .append("                       select\n")
                .append("                           value_type,\n")
                .append("                           key,\n")
                .append("                           value,\n")
                .append("                           default_value,\n")
                .append("                           case\n")
                .append("                               when value is not null and trim(value) <> '' then trim(value)\n")
                .append("                               when default_value is not null and trim(default_value) <> '' then trim(default_value)\n")
                .append("                               else null\n")
                .append("                           end as field_value\n")
                .append("                       from\n")
                .append("                           system_constraint_standard_field_mapping_master standard_field_mapping_master\n")
                .append("                       inner join\n")
                .append("                           system_constraint_standard_field_value_master standard_field_value_master on standard_field_mapping_master.uuid = standard_field_value_master.standard_field_mapping_master_uuid\n")
                .append("                       where\n")
                .append("                           cast(standard_field_mapping_master.standard_master_id as text) = (select key_value from system_configuration where system_key = 'SYSTEM_CONSTRAINT_ACTIVE_STANDARD_ID' and is_active is true)\n")
                .append("                           and standard_field_mapping_master.standard_field_master_uuid = field_master.standard_field_master_uuid\n")
                .append("                           and (select state from system_constraint_standard_field_master where uuid = field_master.standard_field_master_uuid) = 'ACTIVE'\n")
                .append("                       union\n")
                .append("                           select 'TEXT' as value_type, 'fieldKey' as key, null as value, null as default_value, field_master.field_key as field_value\n")
                .append("                       union\n")
                .append("                           select 'TEXT' as value_type, 'fieldName' as key, null as value, null as default_value, field_master.field_name as field_value\n")
                .append("                       union\n")
                .append("                           select 'TEXT' as value_type, 'fieldType' as key, null as value, null as default_value, field_master.field_type as field_value\n")
                .append("                       union\n")
                .append("                           select 'TEXT' as value_type, 'ngModel' as key, null as value, null as default_value, field_master.ng_model as field_value\n")
                .append("                       union\n")
                .append("                           select 'TEXT' as value_type, 'appName' as key, null as value, null as default_value, field_master.app_name as field_value\n")
                .append("                   ) r)\n")
                .append("               )\n")
                .append("           from\n")
                .append("               system_constraint_field_master field_master\n")
                .append("           where\n")
                .append("               form_master.uuid = field_master.form_master_uuid\n")
                .append("           )\n")
                .append("       )\n")
                .append("   as text)\n")
                .append("from\n")
                .append("   system_constraint_form_master form_master\n")
                .append("where\n")
                .append("   form_master.menu_config_id = :menuConfigId \n")
                .append("   and form_master.web_state = 'ACTIVE' ;");
        SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
        sqlQuery.setParameter("menuConfigId", menuConfigId);
        List<String> strings = sqlQuery.list();
        if (CollectionUtils.isEmpty(strings)) {
            throw new ImtechoUserException("Menu not found!", 0);
        }
        return strings.get(0);
    }
}
