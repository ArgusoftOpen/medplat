
DELETE FROM rch_pregnancy_registration_det WHERE id = 1 ;

ALTER TABLE imt_member DISABLE TRIGGER imt_member_delete_trigger ;
DELETE FROM imt_member WHERE id IN (1,2) ;
ALTER TABLE imt_member ENABLE TRIGGER imt_member_delete_trigger ;

DELETE FROM imt_family WHERE family_id = 'FM/2023/1N' ;

DELETE FROM mobile_menu_role_relation WHERE menu_id = 1 AND role_id = 2 ;

DELETE FROM mobile_menu_master WHERE id = 1 ;

DELETE FROM um_user_location WHERE user_id != 1 ;

DELETE FROM um_user where id != 1;

DELETE FROM um_role_master WHERE id != 1 ;

DELETE FROM location_hierchy_closer_det WHERE parent_id != 1 OR child_id != 1;

DELETE FROM location_type_master WHERE id != 1;

DELETE FROM location_wise_analytics WHERE loc_id != 1 ;

DELETE FROM location_master WHERE id != 1 ;
