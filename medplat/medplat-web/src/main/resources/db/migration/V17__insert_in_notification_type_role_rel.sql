TRUNCATE public.notification_type_role_rel CASCADE;
insert into notification_type_role_rel(role_id, notification_type_id)
select 2, id from notification_type_master where code in
('MO','MI','FHW_WPD','FHW_CS','FHW_PNC','FHW_ANC','LMPFU','DISCHARGE','APPETITE','MR');