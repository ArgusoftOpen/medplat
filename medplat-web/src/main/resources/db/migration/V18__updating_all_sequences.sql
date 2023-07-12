SELECT setval('public.menu_group_id_seq', (SELECT MAX(id)+1 FROM menu_group));

SELECT setval('public.menu_config_id_seq', (SELECT MAX(id)+1 FROM menu_config));

SELECT setval('public.listvalue_field_value_detail_id_seq', (SELECT MAX(id)+1 FROM listvalue_field_value_detail));

SELECT setval('public.mobile_form_details_id_seq', (SELECT MAX(id)+1 FROM mobile_form_details));

SELECT setval('public.mobile_beans_feature_rel_id_seq', (SELECT MAX(id)+1 FROM mobile_beans_feature_rel));

SELECT setval('public.event_configuration_id_seq', (SELECT MAX(id)+1 FROM event_configuration));

SELECT setval('public.report_query_master_id_seq', (SELECT MAX(id)+1 FROM report_query_master));

SELECT setval('public.report_master_id_seq', (SELECT MAX(id)+1 FROM report_master));