
INSERT INTO location_master (id,address,associated_user,contact1_email,contact1_name,contact1_phone,contact2_email,contact2_name,contact2_phone,created_by,created_on,is_active,is_archive,max_users,modified_by,modified_on,"name",pin_code,"type",unique_id,parent,is_tba_avaiable,total_population,location_code,state,location_flag,census_population,is_cmtc_present,english_name,is_nrc_present,cerebral_palsy_module,lgd_code,mdds_code,anmol_location_id,geo_fencing,rch_code,demographic_type,rch_integration,cm_dashboard_code,is_taaho,health_block_type,health_block_taluka,nin_number,health_facility_type,is_rural_or_urban,is_notional_or_physical,phc_taluka,subcenter_taluka,village_type,village_status,village_taluka) VALUES
	 (2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:19:19.829',true,false,NULL,'1','2023-07-07 14:19:19.829','Gujarat',NULL,'S',NULL,1,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Gujarat',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 (3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:20:22.336',true,false,NULL,'1','2023-07-07 14:20:22.336','Ahmedabad',NULL,'D',NULL,2,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Ahmedabad',NULL,NULL,'123456','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 (4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:21:11.785',true,false,NULL,'1','2023-07-07 14:21:11.785','Daskroi',NULL,'B',NULL,3,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Daskroi',NULL,NULL,'1234567',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 (5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:21:34.764',true,false,NULL,'1','2023-07-07 14:21:34.764','Aslali PHC',NULL,'P',NULL,4,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Aslali PHC',NULL,NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 (6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:21:52.42',true,false,NULL,'1','2023-07-07 14:21:52.42','Aslali SC',NULL,'SC',NULL,5,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Aslali SC',NULL,NULL,'','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 (7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','2023-07-07 14:22:11.314',true,false,NULL,'1','2023-07-07 15:16:08.04629','Aslali',NULL,'V',NULL,6,NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,'Aslali',NULL,NULL,'123487','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

SELECT setval('location_master_id_seq',  (SELECT MAX(id)+1 FROM location_master));




INSERT INTO location_type_master ("type","name","level",is_soh_enable,created_by,created_on,modified_by,modified_on,id,is_active) VALUES
	 ('S','State',2,true,1,'2023-07-07 14:13:53.619347',1,'2023-07-07 14:13:53.619347',2,true),
	 ('D','District',3,true,1,'2023-07-07 14:14:02.969506',1,'2023-07-07 14:14:02.969506',3,true),
	 ('B','Block',4,true,1,'2023-07-07 14:14:44.360746',1,'2023-07-07 14:14:44.360746',4,true),
	 ('P','PHC',5,true,1,'2023-07-07 14:17:51.786839',1,'2023-07-07 14:17:51.786839',5,true),
	 ('SC','Subcenter',6,true,1,'2023-07-07 14:18:50.005223',1,'2023-07-07 14:18:50.005223',6,true),
	 ('V','Village',7,true,1,'2023-07-07 14:19:01.475399',1,'2023-07-07 14:19:01.475399',7,true);

SELECT setval('location_type_master_id_seq',  (SELECT MAX(id)+1 FROM location_type_master));




INSERT INTO um_role_master (id,created_by,created_on,modified_by,modified_on,code,description,"name",state,max_position,is_email_mandatory,is_contact_num_mandatory,is_aadhar_num_mandatory,is_convox_id_mandatory,short_name,is_last_name_mandatory,role_type,is_health_infra_mandatory,is_geolocation_mandatory,max_health_infra) VALUES
	 (2,1,'2023-07-07 14:22:24.975',1,'2023-07-07 14:22:24.975',NULL,NULL,'FHW','ACTIVE',1,false,false,false,false,NULL,NULL,'MOBILE',false,false,NULL);

SELECT setval('um_role_master_id_seq',  (SELECT MAX(id)+1 FROM um_role_master));




INSERT INTO um_user (id,created_by,created_on,modified_by,modified_on,aadhar_number,address,code,contact_number,date_of_birth,email_id,first_name,gender,last_name,middle_name,"password",prefered_language,role_id,state,user_name,server_type,search_text,title,imei_number,techo_phone_number,aadhar_number_encrypted,no_of_attempts,rch_institution_master_id,infrastructure_id,sdk_version,free_space_mb,latitude,longitude,report_preferred_language,login_code,convox_id,activation_date,member_master_id,location_id,pincode,pin,first_time_password_changed) VALUES
	 (2,1,'2023-07-07 14:23:12.893',2,'2023-07-07 15:57:49.166',NULL,NULL,NULL,NULL,NULL,NULL,'FHW','M','Test',NULL,'Mk8e9I7V5J8EAAn9hNs7SHtQdp+t+Uvu','EN',2,'ACTIVE','fhw_test',NULL,'Mr FHW Test fhw_test null','Mr','3bf30806e3c5501f',NULL,NULL,0,NULL,NULL,30,5463,NULL,NULL,NULL,NULL,NULL,'2023-07-07 14:23:12.891284',NULL,NULL,NULL,NULL,NULL);

SELECT setval('um_user_id_seq',  (SELECT MAX(id)+1 FROM um_user));




INSERT INTO um_user_location (id,created_by,created_on,modified_by,modified_on,loc_id,state,"type",user_id,hierarchy_type,heirarchy_type,"level") VALUES
	 (2,1,'2023-07-07 14:23:12.924',1,'2023-07-07 14:23:12.924',7,'ACTIVE','V',2,NULL,NULL,7);

SELECT setval('um_user_location_id_seq',  (SELECT MAX(id)+1 FROM um_user_location));




INSERT INTO mobile_menu_master (id,config_json,menu_name,created_on,created_by,modified_on,modified_by) VALUES
	 (1,'[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_MY_PEOPLE","order":2},{"mobile_constant":"FHW_NOTIFICATION","order":3},{"mobile_constant":"FHW_WORK_STATUS","order":4},{"mobile_constant":"ANNOUNCEMENTS","order":5},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":6}]','FHW Menu','2023-07-07 14:23:33.78447',1,'2023-07-07 14:23:33.78447',1) ;

SELECT setval('mobile_menu_master_id_seq',  (SELECT MAX(id)+1 FROM mobile_menu_master));




INSERT INTO mobile_menu_role_relation (menu_id,role_id) VALUES
	 (1,2);




INSERT INTO imt_family (address1,address2,anganwadi_id,area_id,assigned_to,bpl_flag,caste,created_on,family_id,house_number,is_verified_flag,latitude,longitude,maa_vatsalya_number,migratory_flag,religion,state,toilet_available_flag,"type",vulnerable_flag,rsby_card_number,"comment",is_report,verified_by_fhsr,oldid,created_by,modified_by,modified_on,old_current_state,current_state,location_id,old_contact_person_id,contact_person_id,updated_by,updated_on,merged_with,emamta_location_id,emamta_location_parent_id,basic_state,hof_id,anganwadi_update_flag,any_member_cbac_done,type_of_house,type_of_toilet,electricity_availability,drinking_water_source,fuel_for_cooking,house_ownership_status,ration_card_number,annual_income,remarks,split_from,additional_info,pmjay_card_number,bpl_card_number,color_of_ration_card,available_cooking_gas,available_electric_connection,have_pmjay_card_number,is_providing_consent,vulnerability_criteria,eligible_for_pmjay,other_type_of_house,anyone_travelled_foreign,ration_card_color,residence_status,native_state,other_motorized_vehicle,other_toilet,other_water_source,vehicle_availability_flag,ration_card_type,pmjay_or_health_insurance) VALUES
	 ('Homes','3',NULL,7,NULL,false,'627','2023-07-07 14:27:29.235','FM/2023/1N','Maher',true,'37.421998333333335','-122.084',NULL,false,'621','CFHC_FV',NULL,NULL,false,NULL,NULL,NULL,NULL,NULL,2,2,'2023-07-07 15:56:53.461',NULL,2,6,NULL,2,NULL,NULL,NULL,NULL,NULL,'VERIFIED',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{"lastFhsDate":1688725613420}',NULL,NULL,NULL,NULL,NULL,NULL,true,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);


SELECT setval('family_id_seq',  (SELECT MAX(id)+1 FROM imt_family));




INSERT INTO imt_member (unique_health_id,family_id,first_name,middle_name,last_name,grandfather_name,dob,emamta_health_id,family_head,is_aadhar_verified,is_mobile_verified,is_native,is_pregnant,lmp,family_planning_method,gender,account_number,ifsc,marital_status,mobile_number,normal_cycle_days,state,education_status,is_report,name_as_per_aadhar,current_state,merged_from_family_id,agreed_to_share_aadhar,aadhar_number_available,aadhar_number_encrypted,death_detail_id,jsy_payment_given,early_registration,jsy_beneficiary,haemoglobin,weight,edd,anc_visit_dates,immunisation_given,place_of_birth,birth_weight,complementary_feeding_started,mother_id,year_of_wedding,last_method_of_contraception,is_high_risk_case,cur_preg_reg_det_id,blood_group,fp_insert_operate_date,menopause_arrived,kpsy_beneficiary,iay_beneficiary,chiranjeevi_yojna_beneficiary,sync_status,is_iucd_removed,iucd_removal_date,cur_preg_reg_date,basic_state,eye_issue,current_disease,chronic_disease,congenital_anomaly,created_by,created_on,modified_by,modified_on,last_delivery_date,hysterectomy_done,cbac_done,ncd_screening_needed,hypertension_screening_done,oral_screening_done,diabetes_screening_done,breast_screening_done,cervical_screening_done,child_nrc_cmtc_status,last_delivery_outcome,remarks,additional_info,suspected_cp,npcb_screening_date,fhsr_phone_verified,iucd_removal_reason,husband_name,current_gravida,current_para,eligible_couple_date,date_of_wedding,husband_id,family_planning_health_infra,relation_with_hof,previous_pregnancy_complication,religion,caste,pmjay_card_number,member_uuid,vulnerable_flag,aadhaar_reference_key,health_id,health_id_number,health_insurance,scheme_detail,is_personal_history_done,pmjay_availability,alcohol_addiction,smoking_addiction,tobacco_addiction,rch_id,hospitalized_earlier,alternate_number,physical_disability,cataract_surgery,sickle_cell_status,pension_scheme) VALUES
	 ('A2N','FM/2023/1N','Rohini','Rakesh','Patel',NULL,'1990-07-07',NULL,NULL,NULL,NULL,NULL,false,'2023-06-07',NULL,'F',NULL,NULL,629,'9544444444',NULL,'CFHC_MV',708,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,true,NULL,NULL,NULL,'2024-03-14 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2023-07-05 00:00:00','VERIFIED',NULL,NULL,NULL,NULL,2,'2023-07-07 14:27:29.278',2,'2023-07-07 15:56:53.473',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{"lastServiceLongDate":1688495400000,"ancAshaMorbidity":"[{\"code\":\"UTI\",\"status\":\"Y\",\"symptoms\":\"Burning urination \\u003d Yes,\"},{\"code\":\"EOP\",\"status\":\"G\",\"symptoms\":\"Vomiting \\u003d Yes,\"}]"}',NULL,NULL,NULL,NULL,NULL,1,1,NULL,'2022-07-07',2,NULL,'WIFE',NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,NULL,NULL,NULL,NULL,'YES','NO','NO','NO',NULL,false,NULL,NULL,NULL,NULL,NULL),
	 ('A1N','FM/2023/1N','Rakesh','Santoshbhai','Patel','Santoshbhai','1990-07-07',NULL,true,NULL,NULL,NULL,NULL,NULL,NULL,'M',NULL,NULL,629,'9588888888',NULL,'CFHC_MV',708,NULL,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'VERIFIED',NULL,NULL,NULL,NULL,2,'2023-07-07 14:27:29.284',2,'2023-07-07 15:56:53.473',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,NULL,NULL,NULL,NULL,'YES','NO','NO','NO',NULL,false,NULL,NULL,NULL,NULL,NULL);

SELECT setval('member_unique_health_id_seq',  (SELECT MAX(id)+1 FROM imt_member));




INSERT INTO rch_pregnancy_registration_det (mthr_reg_no,member_id,lmp_date,edd,reg_date,state,created_on,created_by,modified_on,modified_by,location_id,family_id,current_location_id,delivery_date,is_reg_date_verified) VALUES
(NULL,1,'2023-06-07','2024-03-14','2023-07-05 00:00:00','PENDING','2023-07-07 15:12:47.952419',2,'2023-07-07 15:12:47.952419',2,7,1,7,NULL,NULL);

SELECT setval('rch_pregnancy_registration_det_id_seq',  (SELECT MAX(id)+1 FROM rch_pregnancy_registration_det));
