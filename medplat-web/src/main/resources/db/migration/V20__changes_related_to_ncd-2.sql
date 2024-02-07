
delete from menu_config where menu_name in ('NCD Form Fillup (Location wise)','HMIS Report','CBAC Form Fillup Count User Wise',
'NCD Screening Status','NCD Progress tracking report','Drug Inventory','Consultant Followup Screen members','MBBS MO Review Screen','MO specialist patient list',
'Cardiologist patient list','Ophthalmologist patient list','MO Review Screen','MO Review followup Screen','NCD Screening Dashboard','Referred Patients');

INSERT INTO public.menu_config (feature_json,group_id,active,is_dynamic_report,menu_name,navigation_state,sub_group_id,menu_type,only_admin,menu_display_order,"uuid",group_name_uuid,sub_group_uuid,description) VALUES
	 ('{"canDrillDown":true}',28,true,true,'NCD Form Fillup (Location wise)','techo.report.view({id:690})',NULL,'ncd',NULL,NULL,'cf7ccbb7-b36f-4a4a-82ed-0ec69f0ff2c6','c010e432-20b6-4199-bf51-436bb76517b9',NULL,NULL),
	 ('{"canDrillDown":true}',3,true,true,'HMIS Report','techo.report.view({id:686})',NULL,'manage',NULL,NULL,'c05e3f5c-9aac-4796-bd8d-371189190e8c',NULL,NULL,NULL),
	 ('{"canDrillDown":true}',7,true,true,'CBAC Form Fillup Count User Wise','techo.report.view({id:687})',NULL,'manage',NULL,NULL,'d8077557-e38f-48e8-a0a1-68fba9f46e94','3a3ecc19-d690-4b01-8f33-f5dbe870d116','41ebb15a-cdcd-47e2-ab98-4a531919a526',NULL),
	 ('{"canDrillDown":true}',28,true,true,'NCD Screening Status','techo.report.view({id:689})',NULL,'ncd',NULL,NULL,'f33270ab-cba3-4bb2-82de-0f81705078ea','c3cee2f7-8de1-4ef9-9235-e79908d052c3',NULL,NULL),
	 ('{"canDrillDown":true}',28,true,true,'NCD Progress tracking report','techo.report.view({id:688})',NULL,'ncd',NULL,NULL,'70755cf6-8548-40ed-a0cc-dc7645075383','2a4bb2dd-ba0f-4145-b478-e2f4c04028e7',NULL,NULL),
	 ('{}',NULL,true,NULL,'Drug Inventory','techo.ncd.druginventory',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'Consultant Followup Screen members','techo.ncd.followupscreenlisting',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'MBBS MO Review Screen','techo.ncd.moreviewscreen',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'MO specialist patient list','techo.ncd.mospecialistList',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'Cardiologist patient list','techo.ncd.cardiologistList',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'Ophthalmologist patient list','techo.ncd.ophthalmologistList',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'MO Review Screen','techo.ncd.moreview',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 ('{}',NULL,true,NULL,'MO Review followup Screen','techo.ncd.moreviewfollowup',NULL,'ncd',NULL,NULL,NULL,NULL,NULL,NULL),
	 (NULL,NULL,true,NULL,'NCD Screening Dashboard','techo.dashboard.ncdscreeningdashboard',NULL,'ncd',NULL,NULL,'9cbb91fc-620f-403f-92fd-c3bdb1d30857','be0abc26-0907-4533-8941-324e6d0d27d5','0c1a7a3b-eb85-4570-abde-4f04be4bcc92',NULL),
	 ('{"isShowHealthIdModal":true,"isShowHIPModal":false, "canExamine":true}',NULL,true,NULL,'Referred Patients','techo.ncd.members',NULL,'ncd',NULL,NULL,'49d9bb10-05c0-42c5-87c6-f5ac0554e942','c7104aa4-1f42-460a-91d4-256057cb8b42','408b3025-9e3a-4294-826d-7dc33e357545',NULL);

--adding asha user
INSERT INTO public.um_user (created_by,created_on,modified_by,modified_on,aadhar_number,address,code,contact_number,date_of_birth,email_id,first_name,gender,last_name,middle_name,"password",prefered_language,role_id,state,user_name,server_type,search_text,title,imei_number,techo_phone_number,aadhar_number_encrypted,no_of_attempts,rch_institution_master_id,infrastructure_id,sdk_version,free_space_mb,latitude,longitude,report_preferred_language,login_code,convox_id,activation_date,member_master_id,location_id,pincode,pin,first_time_password_changed) VALUES
         (1,'2024-01-24 18:54:36.53',-1,'2024-01-31 11:34:12.106',NULL,NULL,NULL,NULL,NULL,NULL,'asha','F','test',NULL,'onfoP0JuT+ShfKQ+5xsjzhHYSRjwGa0o','EN',24,'ACTIVE','asha_test',NULL,'Mrs asha test asha_test SUPERADMIN','Mr',NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2024-01-24 18:54:36.462413',NULL,NULL,NULL,NULL,true);
