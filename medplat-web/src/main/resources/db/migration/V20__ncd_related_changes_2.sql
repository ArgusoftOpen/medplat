
delete from menu_config where menu_name in ('CBAC Status Report','NCD Screening Status','NCD Progress tracking report','Referred Patients');

INSERT INTO public.menu_config (id,feature_json,group_id,active,is_dynamic_report,menu_name,navigation_state,sub_group_id,menu_type,only_admin,uuid,group_name_uuid,sub_group_uuid,description) VALUES
(400, '{"canDrillDown":true}', 28, true, true, 'CBAC Status Report', 'techo.report.view({id:682})', NULL, 'ncd', NULL, 'b78f225f-4fff-428d-bafa-5dbe7cd51a89', NULL, NULL, NULL),
(142, '{"canDrillDown":true}', 28, true, true, 'NCD Screening Status', 'techo.report.view({id:681})', NULL, 'ncd', NULL, 'f33270ab-cba3-4bb2-82de-0f81705078ea', 'c3cee2f7-8de1-4ef9-9235-e79908d052c3', '5db2ab2d-7459-4e24-8686-b0ec77959697', NULL),
(207, '{"canDrillDown":true}', 28, true, true, 'NCD Progress tracking report', 'techo.report.view({id:680})', NULL, 'ncd', NULL, '70755cf6-8548-40ed-a0cc-dc7645075383', '2a4bb2dd-ba0f-4145-b478-e2f4c04028e7', NULL, NULL),
(148,'{"isShowHealthIdModal":true,"isShowHIPModal":false, "canExamine":true}',null,true,null,'Referred Patients','techo.ncd.membersdnhdd',null,'ncd',null,null,'49d9bb10-05c0-42c5-87c6-f5ac0554e942','c7104aa4-1f42-460a-91d4-256057cb8b42','408b3025-9e3a-4294-826d-7dc33e357545');

--adding asha user
INSERT INTO public.um_user(created_by, created_on, first_name, last_name, password, role_id, state, user_name)
    VALUES (1, now(), 'asha', 'test', '2JpZ8jdGmQ0uln1aaUpZGTN8x3Ixqg8C',24, 'ACTIVE', 'asha_test') ;

INSERT INTO public.um_user_location(created_by, created_on, loc_id, state, type, user_id, level)
    VALUES (1, now(),7,'ACTIVE','V',24, 7);

INSERT INTO mobile_form_feature_rel (form_id,mobile_constant) VALUES
	 ((select id from mobile_form_details where form_name = 'DNHDD_NCD_CBAC_AND_NUTRITION') ,'DNHDD_NCD_CBAC_AND_NUTRITION'),
	 ((select id from mobile_form_details where form_name = 'CANCER_SCREENING'),'DNHDD_NCD_SCREENING'),
	 ((select id from mobile_form_details where form_name = 'DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH'),'DNHDD_NCD_SCREENING');

insert into listvalue_field_form_relation(field,form_id) values
     ('chronicDiseaseList',(select id from mobile_form_details where form_name = 'CANCER_SCREENING')),
     ('chronicDiseaseList',(select id from mobile_form_details where form_name = 'DNHDD_NCD_CBAC_AND_NUTRITION'));

insert into listvalue_field_form_relation(field,form_id) values
     ('mentalHealthOtherProblemList',(select id from mobile_form_details where form_name = 'DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH'));

delete from mobile_menu_role_relation where role_id in (2,24);
INSERT INTO mobile_menu_role_relation(menu_id, role_id) values
     ((select id from mobile_menu_master where menu_name = 'FHW Menu' order by id desc limit 1), 2),
     ((select id from mobile_menu_master where menu_name = 'ASHA Menu' order by id desc limit 1), 24);
