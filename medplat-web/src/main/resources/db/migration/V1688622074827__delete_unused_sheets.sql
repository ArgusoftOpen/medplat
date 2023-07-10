--delete unnecessary xls sheets
delete from mobile_form_details where form_name = 'ASHA_SAM_SCREENING';
delete from mobile_form_details where form_name = 'AWW_CS';
delete from mobile_form_details where form_name = 'AWW_THR';
delete from mobile_form_details where form_name = 'AWW_DAILY_NUTRITION';