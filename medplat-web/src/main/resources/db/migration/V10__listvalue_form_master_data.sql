/**
 * Author:  hmorzariya
 * Created: 4 Jul, 2023
 */



DELETE FROM public.listvalue_form_master;

INSERT INTO public.listvalue_form_master (form_key,form,is_active,is_training_req,query_for_training_completed) VALUES
	 ('ANCHV','ANCHomeVisit',true,false,NULL),
	 ('INCPAY','Incentive Payment',true,false,NULL),
	 ('CCHV','ChildCareVisit',true,false,NULL),
	 ('ANCMORB','ANCMorbidity',true,false,NULL),
	 ('MAMTA','Mamta Day',true,false,NULL),
	 ('BANK','BANK WEB',true,false,NULL),
	 ('COMVIS','Compliance visit',true,false,NULL),
	 ('RYWANC','RedYellowWomanANC',true,false,NULL),
	 ('RYCCC','RedYellowChildCare',true,false,NULL),
	 ('FUCHILD','FollowUpChild',true,false,NULL);
INSERT INTO public.listvalue_form_master (form_key,form,is_active,is_training_req,query_for_training_completed) VALUES
	 ('EDP','EnrollmentDuringPregnancy',true,false,NULL),
	 ('POV','PNCOutcomeVisit',true,false,NULL),
	 ('PNCVF','PNCHomeVisit',true,false,NULL),
	 ('UNSCHEDULE','UnscheduleEvents',true,false,NULL),
	 ('RYWPNC','RedYellowWomanPNC',false,false,NULL),
	 ('RYCPNC','RedYellowChildPNC',false,false,NULL),
	 ('PDW','PostDischargeWoman',false,false,NULL),
	 ('PDC','PostDischargeChild',false,false,NULL),
	 ('PDNB','PostDischargeNewBorn',false,false,NULL),
	 ('FUW','FollowUpWoman',false,false,NULL);
INSERT INTO public.listvalue_form_master (form_key,form,is_active,is_training_req,query_for_training_completed) VALUES
	 ('EAP','EnrollmentAfterPregnancy',false,false,NULL),
	 ('ECR','EnrollmentChildRegistration',false,false,NULL),
	 ('CCHVM','ChildCareMorbidity',true,false,NULL),
	 ('PNCM','PNCMorbidity',true,false,NULL),
	 ('TBA','TBATraining',true,false,NULL),
	 ('ES69','Endline Surveyor 6 to 9',true,false,NULL),
	 ('ES14','Endline Surveyor 1 to 4',true,false,NULL),
	 ('FHW_ANC','FHW ANC',true,false,NULL),
	 ('FHW_WPD','FHW WPD',true,false,NULL),
	 ('LMPFU','FHW LMPFU',true,false,NULL);
INSERT INTO public.listvalue_form_master (form_key,form,is_active,is_training_req,query_for_training_completed) VALUES
	 ('FHW_PNC','FHW PNC',true,false,NULL),
	 ('FHW_CS','FHW Child Services',true,false,NULL),
	 ('FHW_CI','FHW Child Immunisation',true,false,NULL),
	 ('FHS','Family Health Survey',true,true,'move_to_production_fhs_form'),
	 ('RCH','Reproductive Child Health',true,true,'move_to_production_rch_form'),
	 ('NCD','Non Communicable Diseases',true,true,'move_to_production_ncd_form'),
	 ('WEB_DASHBOARD','WEB Dashbord',true,false,NULL),
	 ('WEB','WEB',true,false,NULL),
	 ('WEB_WPD','Institutional WPD',true,false,NULL),
	 ('CHO','CHO Role Form',true,true,'move_to_production_cho_form');
INSERT INTO public.listvalue_form_master (form_key,form,is_active,is_training_req,query_for_training_completed) VALUES
	 ('NPCB','NPCB Screening Form',true,true,'move_to_production_npcb_form'),
	 ('CORONA_SYMPTOMS','Corona Symptoms',true,false,NULL),
	 ('WEB_NPCB','WEB NPCB',true,false,NULL),
	 ('ASHA_RCH','Asha RCH Module',true,true,'move_to_production_asha_rch_form'),
	 ('MYTECHO','MYTECHO',true,false,NULL),
	 ('ASHA_FHS','ASHA FHS Read Only',true,false,NULL),
	 ('IDSP_2','Integrated Disease Surveillance Programme',true,true,NULL),
	 ('DATA_CHANGE_REQUEST_REASON','Data Change Request Reason',true,false,NULL),
	 ('WEB_TRAINING','WEB Training',true,false,NULL);