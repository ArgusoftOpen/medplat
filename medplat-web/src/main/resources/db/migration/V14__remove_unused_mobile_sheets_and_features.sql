update system_configuration set key_value = '1' where system_key = 'MOBILE_FORM_VERSION';

--delete unnecessary xls sheets
delete from mobile_form_details where form_name = 'ASHA_SAM_SCREENING';
delete from mobile_form_details where form_name = 'AWW_CS';
delete from mobile_form_details where form_name = 'AWW_THR';
delete from mobile_form_details where form_name = 'AWW_DAILY_NUTRITION';

--delete unnecessary mobile features
delete from mobile_feature_master where mobile_constant  = 'ASHA_CBAC_ENTRY';
delete from mobile_feature_master where mobile_constant  = 'ASHA_NCD_REGISTER';
delete from mobile_feature_master where mobile_constant  = 'ASHA_NPCB_SCREENING';