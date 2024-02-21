
delete from menu_config where menu_name in ('CBAC Status Report','NCD Screening Status','NCD Progress tracking report','Referred Patients');

INSERT INTO public.menu_config (id,feature_json,group_id,active,is_dynamic_report,menu_name,navigation_state,sub_group_id,menu_type,only_admin,uuid,group_name_uuid,sub_group_uuid,description) VALUES
(400, '{"canDrillDown":true}', 28, true, true, 'CBAC Status Report', 'techo.report.view({id:682})', NULL, 'ncd', NULL, 'b78f225f-4fff-428d-bafa-5dbe7cd51a89', NULL, NULL, NULL),
(142, '{"canDrillDown":true}', 28, true, true, 'NCD Screening Status', 'techo.report.view({id:681})', NULL, 'ncd', NULL, 'f33270ab-cba3-4bb2-82de-0f81705078ea', 'c3cee2f7-8de1-4ef9-9235-e79908d052c3', '5db2ab2d-7459-4e24-8686-b0ec77959697', NULL),
(207, '{"canDrillDown":true}', 28, true, true, 'NCD Progress tracking report', 'techo.report.view({id:680})', NULL, 'ncd', NULL, '70755cf6-8548-40ed-a0cc-dc7645075383', '2a4bb2dd-ba0f-4145-b478-e2f4c04028e7', NULL, NULL),
(148,'{"isShowHealthIdModal":true,"isShowHIPModal":false, "canExamine":true}',null,true,null,'Referred Patients','techo.ncd.membersdnhdd',null,'ncd',null,null,'49d9bb10-05c0-42c5-87c6-f5ac0554e942','c7104aa4-1f42-460a-91d4-256057cb8b42','408b3025-9e3a-4294-826d-7dc33e357545');

--adding asha user
INSERT INTO public.um_user (created_by,created_on,modified_by,modified_on,aadhar_number,address,code,contact_number,date_of_birth,email_id,first_name,gender,last_name,middle_name,"password",prefered_language,role_id,state,user_name,server_type,search_text,title,imei_number,techo_phone_number,aadhar_number_encrypted,no_of_attempts,rch_institution_master_id,infrastructure_id,sdk_version,free_space_mb,latitude,longitude,report_preferred_language,login_code,convox_id,activation_date,member_master_id,location_id,pincode,pin,first_time_password_changed) VALUES
         (1,'2024-01-24 18:54:36.53',-1,'2024-01-31 11:34:12.106',NULL,NULL,NULL,NULL,NULL,NULL,'asha','F','test',NULL,'onfoP0JuT+ShfKQ+5xsjzhHYSRjwGa0o','EN',24,'ACTIVE','asha_test',NULL,'Mrs asha test asha_test SUPERADMIN','Mr',NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2024-01-24 18:54:36.462413',NULL,NULL,NULL,NULL,true);