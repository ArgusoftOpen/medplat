
TRUNCATE public.oauth_client_details CASCADE;

INSERT INTO public.oauth_client_details (client_id,resource_ids,client_secret,"scope",authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) VALUES
	 ('imtecho-ui','','imtecho-ui-secret','write','password,refresh_token','','',NULL,NULL,'{}','');

TRUNCATE public.menu_config CASCADE;
TRUNCATE public.menu_group CASCADE;

INSERT INTO public.menu_group (id,group_name,active,parent_group,group_type,menu_display_order) VALUES
	 (1,'User Managment',true,NULL,'manage',NULL),
	 (2,'Training',true,NULL,'manage',NULL),
	 (3,'Reports',true,NULL,'manage',NULL),
	 (4,'Maternal Reports',true,NULL,'manage',NULL),
	 (5,'Child Reports',true,NULL,'manage',NULL),
	 (6,'GVK Call Center Report',true,NULL,'manage',NULL),
	 (7,'Administration',true,NULL,'manage',NULL),
	 (8,'NCD Reports',true,NULL,'manage',NULL),
	 (9,'Child Report 2',true,NULL,'manage',NULL),
	 (10,'Facility Data Entry',true,NULL,'manage',NULL);
INSERT INTO public.menu_group (id,group_name,active,parent_group,group_type,menu_display_order) VALUES
	 (11,'CMTC Reports',true,NULL,'manage',NULL),
	 (12,'FP Reports',true,4,'manage',NULL),
	 (13,'FP Reports',true,NULL,'manage',NULL),
	 (14,'High Risk Beneficiaries',true,6,'manage',NULL),
	 (15,'Dr. Techo',true,NULL,'admin',NULL),
	 (16,'My Techo',true,NULL,'admin',NULL),
	 (18,'GVK Call Center',true,NULL,'admin',NULL),
	 (19,'RBSK',true,NULL,'admin',NULL),
	 (21,'Manage OPD',true,NULL,'admin',NULL),
	 (22,'COVID-19',true,NULL,'manage',NULL);
INSERT INTO public.menu_group (id,group_name,active,parent_group,group_type,menu_display_order) VALUES
	 (23,'IDSP',true,NULL,'manage',NULL),
	 (20,'State of Health',true,NULL,'admin',NULL),
	 (24,'Application Management',true,NULL,'admin',NULL),
	 (25,'RCH Data Push',true,NULL,'manage',NULL),
	 (26,'COVID-19 Report',true,NULL,'manage',NULL),
	 (27,'CoWIN',true,NULL,'manage',NULL),
	 (28,'NCD Reports',true,NULL,'ncd',NULL);


INSERT INTO menu_config(id,feature_json,group_id,active,is_dynamic_report,menu_name,navigation_state,sub_group_id,menu_type,only_admin,menu_display_order,uuid,group_name_uuid,sub_group_uuid,description) VALUES

(313,'{"canAdd":false}',24,'True',NULL,'Manage System Configurations','techo.manage.systemconfigs',NULL,'admin',NULL,NULL,'a0bacbaa-345b-46a3-91be-350d1e283eba','f2b2cf32-fa6c-4442-93d4-4a2e4c024cc3','be970181-a811-45a6-94e0-4f8a6c66e154',NULL)

,(76,NULL,NULL,'False',NULL,'Manage PNC visit','techo.manage.pnc',NULL,'manage',NULL,NULL,'51b7ce1a-7d0e-4743-bcd6-a3ef88d05f79','893383bd-66bf-43f7-b24e-293c58c1a526','44994612-db9a-453e-a7a8-ce7991282926',NULL)

,(332,NULL,3,'False',NULL,'District Factsheet','techo.manage.districtperformancedashboard',NULL,'manage',NULL,NULL,NULL,NULL,NULL,NULL)

,(6,'{}',NULL,'True',NULL,'Role','techo.manage.role',NULL,'admin',NULL,NULL,'ffdb68c0-c312-483c-8599-e7d56a45659b','98209cc6-d484-42f9-9c9b-374813d5bb40','997fd1a8-5b7b-40d5-9479-cd29da6be54b',NULL)

,(109,NULL,NULL,'True',NULL,'Web Tasks','techo.dashboard.webtasks',NULL,'manage',NULL,NULL,'f517f225-8eff-4f92-8eb7-0154f93ff0a9','883886e1-c525-4805-a96f-93f7efbf68cd','7ff8cbae-40d0-4ea6-a61b-ce1f0d60e849',NULL)

,(14,'{"basic":true,"advanced":true}',NULL,'True',NULL,'Reports','techo.report.all',NULL,'admin','True',NULL,'4528d53b-bee4-48b6-a958-05512cfacf53','57f9e415-a26c-4800-9213-eae8f228fafe','a0ab9d5d-aaac-46e4-b17d-a6a2472287c2',NULL)

,(5,'{}',NULL,'True',NULL,'FHS Dashboard','techo.dashboard.fhs',NULL,'fhs',NULL,NULL,'aa548d3a-a2b1-4c71-b261-ab880bd4e0f8','1f816868-353a-4dce-886c-0380689f772d','2aee5449-ee8e-476c-82bd-ca715b21eb39',NULL)

,(11,'{}',NULL,'True',NULL,'Trainee Status','techo.training.traineeStatus',NULL,'training',NULL,NULL,'ed180ba2-5699-4bd6-9a40-9ae512a03b83','fab59197-d0f9-44c4-a152-c678a33f9d45','5b701a75-b614-4e91-9197-0320c963b596',NULL)

,(2,'{}',NULL,'True',NULL,'Course','techo.manage.courselist',NULL,'training',NULL,NULL,'beaacac0-f591-45c8-af06-695e29840282','bc289f1e-665a-41dd-aeea-7c8bf6022a09','4b021097-b431-444c-83f7-a14e3ea0de10',NULL)

,(4,'{}',NULL,'True',NULL,'Features','techo.manage.menu',NULL,'admin',NULL,NULL,'ded01e15-4b1b-409b-be0e-e3f556c85b7e','f1d84892-e49f-446e-a7a0-05d002c106d3','f0528627-1975-43c8-bc03-4bd6342877f3',NULL)

,(323,'{}',NULL,'True',NULL,'System Code Management Tool','techo.manage.systemcode',NULL,'manage',NULL,NULL,'14014394-e1be-4ad5-a6bc-94b721cc7725','4e7cd1ea-35b5-4767-b03e-de87560908ff','fe05ba39-3318-45f0-a8d5-788c0b6cbd16',NULL)

,(68,'{}',NULL,'True',NULL,'Manage Values And Multimedia','techo.manage.valuesnmultimedia',NULL,'admin',NULL,NULL,'9aa1ef27-5889-48a5-b81a-a8ad9780210d','70b77a9c-e9a4-4d5f-8ec4-4c10d5b192b7','a2e1082c-f7d1-4a44-81e7-99fecfbd17ab',NULL)

,(7,'{"canMarkAttendance":false}',NULL,'True',NULL,'Training Schedule','techo.training.scheduled',NULL,'training',NULL,NULL,'4df39456-ac49-444b-a2fd-f1bdad1b0c39','04470d24-2f8a-47db-9b0f-70cafcbfbd29','c2a9d481-263d-4aef-9da6-9e7cfdf172fb',NULL)

,(71,'{"canSearchByLocation":false,"canSearchByFamilyId":true,"canSearchByMemberHealthId":true,"canSearchArchived":false,

"canMarkUnverified":false}',NULL,'False',NULL,'Family Moving','techo.manage.familymoving',NULL,'manage',NULL,NULL,'71909bf2-8f9f-4296-b4a3-f9d4b2d6d4eb','3b560419-664b-4c28-89b2-0498974b371f','382c96d6-0915-4b3c-b93a-53daab2773ac',NULL)

,(67,'{}',NULL,'True',NULL,'Manage Translations','techo.manage.translator-label',NULL,'admin',NULL,NULL,'cca450b9-4cb4-43ce-8d83-d3c68c98c3c0','e58c59d3-4caa-49e5-a739-faf339e9b08b','f8aca781-8f23-4fab-b4ca-4d2e77a91517',NULL)

,(123,'{}',NULL,'True',NULL,'Member Information','techo.manage.memberinformation',NULL,'manage',NULL,NULL,'6c487648-7612-47ce-85e3-ffe6a1c66ed6','17c47330-fd9c-4d29-b7d8-7005b52cc8b7','3d1b9743-134e-469b-a57c-8a7853ef7c59',NULL)

,(127,'{}',NULL,'True',NULL,'Family Information','techo.manage.familyinformation',NULL,'manage',NULL,NULL,'7ec15101-8be8-48c2-a8f0-f640dac770b2','928c7d48-5129-455e-a839-1526e7ad868e','de0b803b-4bfe-44f3-8bd9-d42348bb11ba',NULL)

,(130,'{}',NULL,'True',NULL,'User Information','techo.manage.userinformation',NULL,'manage',NULL,NULL,'c462bc9e-0557-479d-b835-ebbd3a4934e3','91ec0042-5ab8-411a-82a1-0e70e746c68f','6ea0f6ce-04d6-4372-a725-c3e288f0b8a3',NULL)

,(153,'{}',NULL,'True',NULL,'Manage Migrations','techo.manage.migrations',NULL,'manage',NULL,NULL,'ad52c781-b7c1-4cfb-bd35-c70a48468a17','5767a442-edb4-443d-bd0a-662be72abfa9','95e1be1d-f21a-4a78-9552-abada64ea7f4',NULL)

,(149,NULL,10,'True',NULL,'PNC Institution Form','techo.manage.pncSearch',NULL,'manage',NULL,NULL,'d89e6edd-9360-4fd0-b158-3e0fbbe76bff','d24ffc27-8e53-42ae-a248-06b711794701','b3271ae4-f9c4-4ed7-a9ef-bf1e13dd25fc',NULL)

,(151,NULL,10,'True',NULL,'Child Service Visit','techo.manage.childServiceSearch',NULL,'manage',NULL,NULL,'89ec6ee2-f594-4389-b408-b9b9e5ec0a5e','f096a014-f60c-4cec-8904-1892aa178331','f5768c47-814d-4813-a6a5-c1d1a9a73f08',NULL)

,(158,NULL,NULL,'True',NULL,'Mobile Management','techo.manage.mobilemangementinfo',NULL,'manage',NULL,NULL,'e327b0a9-faf6-4aaa-ba29-4b4092287dba','2728aaef-a985-4cf3-b4f8-2289c73b7929','de062f15-98d8-42b6-afc6-33bec4e0f72c',NULL)

,(165,NULL,NULL,'True',NULL,'Mobile Library','techo.manage.mobilelibrary',NULL,'manage',NULL,NULL,'b291f661-c23c-4dc4-930e-00f1eb3420bb','f2280473-9c2f-4bd6-aec2-9a626e805504','f2f5ae97-6f91-46bb-998b-5f7bf350363d',NULL)

,(166,NULL,24,'True',NULL,'Widgets management','techo.manage.manage-widgets',NULL,'admin',NULL,NULL,'ce1a0005-4fa2-48ce-985b-437296992d45','084f8a03-74ab-428e-8ebe-7f32b8814125','32f82a8b-a095-4f17-8193-978e7acebd60',NULL)

,(176,'{"canLock":false,"canUnlock":false}',7,'False',NULL,'Location Wise Expected Target','techo.manage.expectedTarget',NULL,'manage',NULL,NULL,'6d384f39-2112-41f9-8e95-b2708b741d8e','ea4d2b7f-c52f-496d-923e-3e2b9330468e','04a87cca-4418-42f7-9b32-9f0d60ebddf1',NULL)

,(257,NULL,NULL,'True',NULL,'Manage Schools','techo.manage.schools',NULL,'manage','False',NULL,'b46d41b7-b629-436e-a48d-6cdac0e62d7e','ecddfd27-8090-4e65-be36-80ef4bd786bc','c6cf7b12-3093-4d2f-b4b4-81d4fe02ce49',NULL)

,(122,'{"canAdd": false, "canEdit": false, "canEditFru": false, "canEditHWC": false, "canEditNrc": false, "canEditCmtc": false, "canEditIdsp": false, "canEditNpcb": false, "canEditSncu": false, "canEditGynaec": false, "canEditLabTest": false, "canEditMaYojna": false, "canEditBalsakha1": false, "canEditBalsakha3": false, "canEditBloodBank": false, "canChangeLocation": false, "canEditChiranjeevi": false, "canEditUsgFacility": false, "canEditVentilators": false, "canEditPediatrician": false, "canEditPmjayFacility": false, "canEditDefibrillators": false, "manageBasedOnLocation": false, "canEditOxygenCylinders": false, "canEditReferralFacility": false, "manageBasedOnAssignment": false, "canManageHealthInfraType": true}',NULL,'True',NULL,'Health Facility Mapping','techo.manage.healthinfrastructures',NULL,'manage',NULL,NULL,'84280b3c-1d09-40ff-a75b-375dbb73a4cd','759556bd-6dcb-4c02-b57b-846dcf8ea45c','c5f5a891-4c6a-4828-9f13-21903793b04e',NULL)

,(19,'{}',24,'True',NULL,'Query Builder','techo.manage.query',NULL,'admin','True',NULL,'7cf986e3-8efb-4107-8012-70ebcd2d726b','dd0d7e71-b68a-491e-8033-11327f08c6ed','06460456-e9f0-4e26-b121-71ad6467f6b6',NULL)

,(18,'{}',24,'True',NULL,'Events Configured','techo.notification.all',NULL,'admin','True',NULL,'56afdb6d-64e0-48dc-a1df-42ceb920bc41','09544ee0-e9fd-4d04-9dd4-f3a215f0748e','af91f9f5-f27c-4dce-a1b7-909e142a443b',NULL)

,(339,'{}',NULL,'False',NULL,'Performance Dashboard','techo.manage.performancedashboard',NULL,'manage',NULL,NULL,NULL,NULL,NULL,NULL)

,(340,'{}',NULL,'True',NULL,'Location Type','techo.manage.locationtype',NULL,'manage',NULL,NULL,NULL,NULL,NULL,NULL)

,(152,'{"isShowHIPModal": false, "isShowConsentList": false, "isShowHealthIdModal": true}',10,'True',NULL,'ANC Service Visit','techo.manage.ancSearch',NULL,'manage',NULL,NULL,'63a16dbc-2b9d-478c-8be2-d82434cc9f33','0944a4be-1ac0-4bc4-9935-7188de90ef21','8102bbe1-ec26-45f2-a490-537e3ef84ee1',NULL)

,(111,'{"isShowHIPModal": false, "isShowConsentList": false, "isShowHealthIdModal": true}',10,'True',NULL,'Institutional Delivery Reg. Form','techo.manage.wpdSearch',NULL,'manage',NULL,NULL,'417831e3-acb5-402a-bce4-60ce4dcc1087','e17d25bd-7371-4786-90b8-d91b325a4be4','f1444b6c-c6f2-4e50-9b6c-8a643b341edb',NULL)

,(347,'{}',24,'True',NULL,'Form Configurator','techo.admin.systemConstraints',NULL,'admin','True',NULL,NULL,NULL,NULL,NULL)
,(17,'{"canAdd":true,"canEdit":true,"canEditLGDCode":false}',NULL,'True',NULL,'Locations','techo.manage.location',NULL,'manage',NULL,NULL,'868bc0f6-a8fb-478a-8c43-e0c3fbcdf00f','ee39856f-faa8-43a2-9b86-5bbf6c167f78','118b1a05-a061-420a-91a6-0dd36a38ae47',NULL)
,(1,'{"canAdd":true,"canEdit":true,"canEditUserName":false,"canEditPassword":false}',NULL,'True',NULL,'Users','techo.manage.users',NULL,'manage',NULL,NULL,'72cb560b-be3c-4a05-90de-95802a221d78','dcebfd0b-1339-4129-8ae4-eb1222baa904','18ad0f19-096a-46f7-a2af-93a2d0b5b568',NULL)
,(359,'{"canDrillDown":true}',NULL,'True','True','Mobile Menu Management','techo.manage.mobileMenuManagement',NULL,'admin',NULL,NULL,NULL,NULL,NULL,NULL),
(141,'{"canDrillDown":true}',5,true,true,'Child for monitoring of Pentavalent 3 Vaccine as per Date of Birth','techo.report.view({id:104})',NULL,'manage',NULL,NULL,'7d37611d-5a9e-4e23-a93b-a0b6efeb0408','7619ba8e-ea68-4ac6-af79-3b5cd1add162','852a948d-b158-4c34-8316-fd6cf9e3e8c2',NULL),
(140,'{"canDrillDown":true}',5,true,true,'Immunization Services Provided to Children with Date of Birth','techo.report.view({id:105})',NULL,'manage',NULL,NULL,'53763990-8729-4041-99ff-de597c7e8c63','56ab2338-7a81-4c04-8420-5527cc3c3421',NULL,NULL),
(113,'{"canDrillDown":true}',5,true,true,'Services Provided to the Children Registered during the Year ( 1 to 6 Year )','techo.report.view({id:87})',NULL,'manage',NULL,NULL,'c3935136-deee-4c52-a559-b655f906648f','20dbcbd8-2bf4-4bf9-9b52-20a95f0c1fc8','4ee44287-1146-42c2-af02-d9966626ceb7',NULL),
(114,'{"canDrillDown":true}',5,true,true,'Services Provided to the Children Registered during the Year ( 0 to 1 Year )','techo.report.view({id:86})',NULL,'manage',NULL,NULL,'4e946051-2845-4af5-9673-38aca100becc','399b6498-87f9-4ecf-a518-fc31759485d1','26fe4076-5ea2-4c01-8d09-0231a4b8df8b',NULL),
(117,'{"canDrillDown":true}',5,true,true,'Child Health Services Provided during the Year (Current Year)','techo.report.view({id:83})',NULL,'manage',NULL,NULL,'72e4c026-6e2f-4c46-b03b-cbef68ee4f45','3141a794-e578-4466-9c3d-a385e458bd1f','dd2ec4a1-9347-4a63-b38c-51207b2e3f13',NULL),
(136,'{"canDrillDown":true}',5,true,true,'Monitoring BCG Vaccine as per DOB','techo.report.view({id:101})',NULL,'manage',NULL,NULL,'4f3b16b2-0b3c-4736-ac90-f9ebcf9b5c1b','4d99f215-214a-4b85-b727-8d582a8d76bf','609e811a-14eb-4e7b-9441-0e8f13de3e59',NULL),
(239,'{"canDrillDown":true}',5,true,true,'Child services provided for the 2nd Year Immunization','techo.report.view({id:526})',NULL,'manage',NULL,NULL,'001d9665-429c-4b6a-9caf-47b70976e439','0cf1aa27-6d14-46d7-8aa3-972f62e8630b','e82f4251-049d-47b6-8dad-ccb80c9b2a00',NULL),
(328,'{"canDrillDown":true}',13,true,true,'FP Target couple Reports - Child count wise(EN)','techo.report.view({id:628})',NULL,'manage',NULL,NULL,'c00601e8-97f5-468b-9112-91876a589ccc',NULL,NULL,NULL),
(179,'{"canDrillDown":true}',13,true,true,'FP Target couple Reports - Age wise(EN)','techo.report.view({id:392})',NULL,'manage',NULL,NULL,'c4dbf25a-bb48-46e7-81bb-221ac035b3ba','e4ef4395-6d6d-4ce4-ae8b-168e49b888c9',NULL,NULL),
(263,'{"canDrillDown":true}',NULL,true,true,'CFHC Report','techo.report.view({id:549})',NULL,'fhs',NULL,NULL,'6ea74dcf-328f-431b-a79c-78d500f15bda','a45420d9-53fc-4a93-89d7-44d092b0a214','fc4e808a-177c-4ab6-b0dc-2d99b41af015',NULL),
(86,'{"canDrillDown":true}',4,true,true,'High Risk Mother-Present Pregnancy Complication','techo.report.view({id:62})',NULL,'manage',NULL,NULL,'2ce1918d-5693-4f59-893b-858be10c2541','5bea3533-fc5e-48e3-b6ce-20da51d2fe9f','658dc2db-bb37-4ec0-8901-00e9fcb5fcd8',NULL),
(93,'{"canDrillDown":true}',4,true,true,'Pregnant Women Report','techo.report.view({id:69})',NULL,'manage',NULL,NULL,'2270b36d-3479-497f-9663-ca3606387a66','ccb518ad-3f8c-4a47-9b05-0fbf49587c1b','1e627b4d-de7c-480e-9375-d5e6f44042fd',NULL),
(79,'{"canDrillDown":true}',4,true,true,'Delivery monitoring of Registered LMP','techo.report.view({id:55})',NULL,'manage',NULL,NULL,'9630c951-9a3b-466a-9e32-b82795f2d60d','c9206c30-62b8-4671-a726-4fd04502015f','553edc6e-a109-4b6d-905f-3fed2ead32d3',NULL),
(90,'{"canDrillDown":true}',4,true,true,'Maternal Service Delivery','techo.report.view({id:66})',NULL,'manage',NULL,NULL,'12ddf52c-103c-4ef2-b174-105851e206a9','37389191-ec41-443e-b57e-bb66cb36e3bc',NULL,NULL),
(175,'{"canDrillDown":true}',4,true,true,'Maternal Health Services Provided During The Year (Monthly)','techo.report.view({id:344})',NULL,'manage',NULL,NULL,'0cbb5fea-fb01-49f1-8e3e-de6876d121f9','d63f14e9-a6e6-4f06-8e07-9c3a7ef7dd2c',NULL,NULL),
(82,'{"canDrillDown":true}',4,true,true,'Report of Antenatal Corticosteroid (ANC)','techo.report.view({id:58})',NULL,'manage',NULL,NULL,'49417ecf-ce4c-4d84-abea-840f4ff1c856','b33d7ba5-4cf0-4f09-a428-e0f441a96895','a47f4fda-9ed0-4973-817c-2004d9286497',NULL),
(80,'{"canDrillDown":true}',4,true,true,'High Risk Mother-Previous Pregnancy and Pre Existing Chronic Illness','techo.report.view({id:56})',NULL,'manage',NULL,NULL,'82c79746-a8aa-40fa-8042-2e0dcfd7517a','1abbfafb-342b-4cbc-a651-bb0447a0456f','d536f3c6-d0db-4db8-af1f-a445492860cd',NULL),
(81,'{"canDrillDown":true}',4,true,true,'LMP Based Pregnant Women Reg. for ANC Services','techo.report.view({id:57})',NULL,'manage',NULL,NULL,'c8e7b31f-44b8-4278-9c1d-de837cf47455','4a65200b-e248-4824-b1a0-782e97b8a104',NULL,NULL);



DELETE FROM public.um_user_location WHERE
    loc_id = (SELECT id FROM public.location_master WHERE name = 'INDIA')
    OR user_id = (SELECT id from public.um_user where user_name = 'admin');
DELETE FROM public.um_user WHERE user_name = 'admin' ;
DELETE FROM public.location_master WHERE name = 'INDIA' ;
DELETE FROM user_menu_item WHERE role_id = ((SELECT id from public.um_role_master WHERE name = 'SUPERADMIN'));
DELETE FROM public.role_management WHERE managed_by_role_id = (SELECT id from public.um_role_master WHERE name = 'SUPERADMIN') ;
DELETE FROM public.um_role_master where name = 'SUPERADMIN' ;


-- Enter data in location_type_master
DELETE FROM public.location_type_master where type ilike 'CC';
INSERT INTO public.location_type_master(
    type, name, level, is_soh_enable, created_by, created_on, is_active)
    VALUES ('CC', 'Country', 1, true, -1, now(), true);

-- Enter data in location_master
INSERT INTO public.location_master(
    created_by, created_on, is_active, is_archive, name, english_name,type,state )
    VALUES ('ADMIN', now(), true, false, 'INDIA', 'INDIA','CC','ACTIVE') ;

-- Enter data in um_role_master
INSERT INTO public.um_role_master
    (created_by, created_on, name, state, is_last_name_mandatory, role_type)
    VALUES (-1, now(),'SUPERADMIN', 'ACTIVE', 'True','BOTH');

-- Enter data in um_user
INSERT INTO public.um_user(
    created_by, created_on, first_name, last_name, password, role_id, state, user_name)
    VALUES (-1, now(), 'admin', 'admin', '2JpZ8jdGmQ0uln1aaUpZGTN8x3Ixqg8C',
        (SELECT id from public.um_role_master WHERE name = 'SUPERADMIN'), 'ACTIVE', 'admin') ;

-- Enter data in um_user_location
DELETE FROM public.um_user_location WHERE
    loc_id = (SELECT id FROM public.location_master WHERE name = 'INDIA')
    AND user_id = (SELECT id from public.um_user where user_name = 'admin');
INSERT INTO public.um_user_location(
    created_by, created_on, loc_id, state, type, user_id, level)
    VALUES (-1, now(),
        (SELECT id FROM public.location_master WHERE name = 'INDIA'), 'ACTIVE', 'CC',
        (SELECT id from public.um_user where user_name = 'admin'), 1);

INSERT INTO public.user_menu_item(
feature_json, group_id, menu_config_id, user_id, role_id, created_by, created_on)
(SELECT mc.feature_json, mc.group_id, mc.id AS menu_config_id, um.id as user_id, um.role_id,-1, now()
 FROM menu_config mc JOIN public.um_user um on um.user_name = 'admin');

 INSERT INTO public.role_management(created_by, created_on, role_id, managed_by_role_id, state) VALUES
 (-1, now(),(SELECT id from public.um_role_master WHERE name = 'SUPERADMIN'),
 (SELECT id from public.um_role_master WHERE name = 'SUPERADMIN'),'ACTIVE');

 DELETE FROM document_module_master WHERE module_name = 'TRAINING';
 INSERT INTO public.document_module_master(module_name, base_path, created_by, created_on)
 VALUES ('TRAINING', 'training', -1, now());

TRUNCATE public.system_configuration CASCADE;
INSERT INTO public.system_configuration (system_key,is_active,key_value) VALUES
    ('SYSTEM_CONSTRAINT_ACTIVE_STANDARD_ID',true,'2437'),
    ('member_comorbidites_last_update_schedule_date',true,'07-17-2020'),
    ('rch_pregnancy_analytics_last_schedule_date',true,'07-21-2023'),
    ('rch_pregnancy_analytics_last_schedule_date_for_child_analytics',true,'07-21-2023'),
    ('rch_child_analytics_run_for_all_child',true,'false'),
    ('LAST_UPDATE_DATE_OF_ELIGIBLE_COUPLE_ANALYTICS',true,'07-20-2023');

DELETE FROM field_constant_master WHERE field_name = 'COURSE_MODULE_NAME' ;
INSERT INTO field_constant_master (field_name, created_on, created_by)
VALUES ('COURSE_MODULE_NAME', now(), '-1') ;

INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(45, 1.70, 1.90, 2.00, 2.20, 2.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(46, 1.80, 2.00, 2.20, 2.40, 2.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(47, 2.00, 2.10, 2.30, 2.50, 2.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(48, 2.10, 2.30, 2.50, 2.70, 2.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(49, 2.20, 2.40, 2.60, 2.90, 3.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(50, 2.40, 2.60, 2.80, 3.00, 3.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(51, 2.50, 2.70, 3.00, 3.20, 3.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(52, 2.70, 2.90, 3.20, 3.50, 3.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(53, 2.90, 3.10, 3.40, 3.70, 4.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(54, 3.10, 3.30, 3.60, 3.90, 4.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(55, 3.30, 3.60, 3.80, 4.20, 4.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(56, 3.50, 3.80, 4.10, 4.40, 4.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(57, 3.70, 4.00, 4.30, 4.70, 5.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(58, 3.90, 4.30, 4.60, 5.00, 5.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(59, 4.10, 4.50, 4.80, 5.30, 5.70, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(60, 4.30, 4.70, 5.10, 5.50, 6.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(61, 4.50, 4.90, 5.30, 5.80, 6.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(62, 4.70, 5.10, 5.60, 6.00, 6.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(63, 4.90, 5.30, 5.80, 6.20, 6.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(64, 5.10, 5.50, 6.00, 6.50, 7.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(65, 5.30, 5.70, 6.20, 6.70, 7.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(66, 5.50, 5.90, 6.40, 6.90, 7.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(67, 5.60, 6.10, 6.60, 7.10, 7.70, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(68, 5.80, 6.30, 6.80, 7.30, 8.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(69, 6.00, 6.50, 7.00, 7.60, 8.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(70, 6.10, 6.60, 7.20, 7.80, 8.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(71, 6.30, 6.80, 7.40, 8.00, 8.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(72, 6.40, 7.00, 7.60, 8.20, 8.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(73, 6.60, 7.20, 7.70, 8.40, 9.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(74, 6.70, 7.30, 7.90, 8.60, 9.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(75, 6.90, 7.50, 8.10, 8.80, 9.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(76, 7.00, 7.60, 8.30, 8.90, 9.70, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(77, 7.20, 7.80, 8.40, 9.10, 9.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(78, 7.30, 7.90, 8.60, 9.30, 10.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(79, 7.40, 8.10, 8.70, 9.50, 10.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(80, 7.60, 8.20, 8.90, 9.60, 10.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(81, 7.70, 8.40, 9.10, 9.80, 10.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(82, 7.90, 8.50, 9.20, 10.00, 10.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(83, 8.00, 8.70, 9.40, 10.20, 11.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(84, 8.20, 8.90, 9.60, 10.40, 11.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(85, 8.40, 9.10, 9.80, 10.60, 11.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(86, 8.60, 9.30, 10.00, 10.80, 11.70, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(87, 8.90, 9.60, 10.40, 11.20, 12.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(88, 9.10, 9.80, 10.60, 11.50, 12.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(89, 9.30, 10.00, 10.80, 11.70, 12.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(90, 9.40, 10.20, 11.00, 11.90, 12.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(91, 9.60, 10.40, 11.20, 12.10, 13.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(92, 9.80, 10.60, 11.40, 12.30, 13.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(93, 9.90, 10.80, 11.60, 12.60, 13.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(94, 10.10, 11.00, 11.80, 12.80, 13.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(95, 10.30, 11.10, 12.00, 13.00, 14.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(96, 10.40, 11.30, 12.20, 13.20, 14.30, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(97, 10.60, 11.50, 12.40, 13.40, 14.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(98, 10.80, 11.70, 12.60, 13.70, 14.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(99, 11.00, 11.90, 12.90, 13.90, 15.10, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(100, 11.20, 12.10, 13.10, 14.20, 15.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(101, 11.30, 12.30, 13.30, 14.40, 15.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(102, 11.50, 12.50, 13.60, 14.70, 15.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(103, 11.70, 12.80, 13.80, 14.90, 16.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(104, 11.90, 13.00, 14.00, 15.20, 16.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(105, 12.10, 13.20, 14.30, 15.50, 16.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(106, 12.30, 13.40, 14.50, 15.80, 17.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(107, 12.50, 13.70, 14.80, 16.10, 17.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(108, 12.70, 13.90, 15.10, 16.40, 17.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(109, 12.90, 14.10, 15.30, 16.70, 18.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(110, 13.20, 14.40, 15.60, 17.00, 18.50, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(111, 13.40, 14.60, 15.90, 17.30, 18.90, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(112, 13.60, 14.90, 16.20, 17.60, 19.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(113, 13.80, 15.20, 16.50, 18.00, 19.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(114, 14.10, 15.40, 16.80, 18.30, 20.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(115, 14.30, 15.70, 17.10, 18.60, 20.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(116, 14.60, 16.00, 17.40, 19.00, 20.80, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(117, 14.80, 16.20, 17.70, 19.30, 21.20, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(118, 15.00, 16.50, 18.00, 19.70, 21.60, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(119, 15.30, 16.80, 18.30, 20.00, 22.00, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(120, 15.50, 17.10, 18.60, 20.40, 22.40, 'M');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(45, 1.70, 1.90, 2.10, 2.30, 2.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(46, 1.90, 2.00, 2.20, 2.40, 2.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(47, 2.00, 2.20, 2.40, 2.60, 2.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(48, 2.10, 2.30, 2.50, 2.70, 3.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(49, 2.20, 2.40, 2.60, 2.90, 3.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(50, 2.40, 2.60, 2.80, 3.10, 3.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(51, 2.50, 2.80, 3.00, 3.30, 3.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(52, 2.70, 2.90, 3.20, 3.50, 3.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(53, 2.80, 3.10, 3.40, 3.70, 4.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(54, 3.00, 3.30, 3.60, 3.90, 4.30, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(55, 3.20, 3.50, 3.80, 4.20, 4.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(56, 3.40, 3.70, 4.00, 4.40, 4.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(57, 3.60, 3.90, 4.30, 4.60, 5.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(58, 3.80, 4.10, 4.50, 4.90, 5.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(59, 3.90, 4.30, 4.70, 5.10, 5.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(60, 4.10, 4.50, 4.90, 5.40, 5.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(61, 4.30, 4.70, 5.10, 5.60, 6.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(62, 4.50, 4.90, 5.30, 5.80, 6.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(63, 4.70, 5.10, 5.50, 6.00, 6.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(64, 4.80, 5.30, 5.70, 6.30, 6.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(65, 5.00, 5.50, 5.90, 6.50, 7.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(66, 5.10, 5.60, 6.10, 6.70, 7.30, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(67, 5.30, 5.80, 6.30, 6.90, 7.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(68, 5.50, 6.00, 6.50, 7.10, 7.70, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(69, 5.60, 6.10, 6.70, 7.30, 8.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(70, 5.80, 6.30, 6.90, 7.50, 8.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(71, 5.90, 6.50, 7.00, 7.70, 8.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(72, 6.00, 6.60, 7.20, 7.80, 8.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(73, 6.20, 6.80, 7.40, 8.00, 8.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(74, 6.30, 6.90, 7.50, 8.20, 9.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(75, 6.50, 7.10, 7.70, 8.40, 9.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(76, 6.60, 7.20, 7.80, 8.50, 9.30, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(77, 6.70, 7.40, 8.00, 8.70, 9.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(78, 6.90, 7.50, 8.20, 8.90, 9.70, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(79, 7.00, 7.70, 8.30, 9.10, 9.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(80, 7.10, 7.80, 8.50, 9.20, 10.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(81, 7.30, 8.00, 8.70, 9.40, 10.30, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(82, 7.50, 8.10, 8.80, 9.60, 10.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(83, 7.60, 8.30, 9.00, 9.80, 10.70, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(84, 7.80, 8.50, 9.20, 10.10, 11.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(85, 8.00, 8.70, 9.40, 10.30, 11.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(86, 8.10, 8.90, 9.70, 10.50, 11.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(87, 8.40, 9.20, 10.00, 10.90, 11.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(88, 8.60, 9.40, 10.20, 11.10, 12.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(89, 8.80, 9.60, 10.40, 11.40, 12.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(90, 9.00, 9.80, 10.60, 11.60, 12.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(91, 9.10, 10.00, 10.90, 11.80, 12.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(92, 9.30, 10.20, 11.10, 12.00, 13.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(93, 9.50, 10.40, 11.30, 12.30, 13.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(94, 9.70, 10.60, 11.50, 12.50, 13.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(95, 9.80, 10.80, 11.70, 12.70, 13.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(96, 10.00, 10.90, 11.90, 12.90, 14.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(97, 10.20, 11.10, 12.10, 13.20, 14.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(98, 10.40, 11.30, 12.30, 13.40, 14.70, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(99, 10.50, 11.50, 12.50, 13.70, 14.90, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(100, 10.70, 11.70, 12.80, 13.90, 15.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(101, 10.90, 12.00, 13.00, 14.20, 15.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(102, 11.10, 12.20, 13.30, 14.50, 15.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(103, 11.30, 12.40, 13.50, 14.70, 16.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(104, 11.50, 12.60, 13.80, 15.00, 16.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(105, 11.80, 12.90, 14.00, 15.30, 16.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(106, 12.00, 13.10, 14.30, 15.60, 17.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(107, 12.20, 13.40, 14.60, 15.90, 17.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(108, 12.40, 13.70, 14.90, 16.30, 17.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(109, 12.70, 13.90, 15.20, 16.60, 18.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(110, 12.90, 14.20, 15.50, 17.00, 18.60, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(111, 13.20, 14.50, 15.80, 17.30, 19.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(112, 13.50, 14.80, 16.20, 17.70, 19.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(113, 13.70, 15.10, 16.50, 18.00, 19.80, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(114, 14.00, 15.40, 16.80, 18.40, 20.20, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(115, 14.30, 15.70, 17.20, 18.80, 20.70, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(116, 14.50, 16.00, 17.50, 19.20, 21.10, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(117, 14.80, 16.30, 17.80, 19.60, 21.50, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(118, 15.10, 16.60, 18.20, 19.90, 22.00, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(119, 15.40, 16.90, 18.50, 20.30, 22.40, 'F');
INSERT INTO public.sd_score_master
(height, minus4, minus3, minus2, minus1, median, gender)
VALUES(120, 15.60, 17.30, 18.90, 20.70, 22.80, 'F');