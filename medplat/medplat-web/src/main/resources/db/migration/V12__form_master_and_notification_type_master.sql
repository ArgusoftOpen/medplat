
TRUNCATE public.form_master CASCADE;
INSERT INTO public.form_master (id,created_by,created_on,code,name,state) VALUES
     (1,-1,CURRENT_TIMESTAMP,'FHW_LMPFU','FHW LMP Follow up','ACTIVE'),
     (6,-1,CURRENT_TIMESTAMP,'FHW_VAE','FHW VACCINE ADVERSE EFFECT','ACTIVE'),
     (7,-1,CURRENT_TIMESTAMP,'FHW_RIM','FHW REPRODUCTIVE INFO MODIFICATION','ACTIVE'),
     (3,-1,CURRENT_TIMESTAMP,'FHW_WPD','FHW WPD','ACTIVE'),
     (5,-1,CURRENT_TIMESTAMP,'FHW_CS','FHW CHild Service','ACTIVE'),
     (4,-1,CURRENT_TIMESTAMP,'FHW_PNC','FHW PNC','ACTIVE'),
     (2,-1,CURRENT_TIMESTAMP,'FHW_ANC','FHW ANC','ACTIVE'),
     (58,-1,CURRENT_TIMESTAMP,'TT2_ALERT','TT2 Alert','ACTIVE'),
     (59,-1,CURRENT_TIMESTAMP,'IRON_SUCROSE_ALERT','Iron Sucrose Alert','ACTIVE'),
     (68,-1,CURRENT_TIMESTAMP,'ASHA_LMPFU','ASHA LMP Follow Up Notification','ACTIVE'),
     (70,-1,CURRENT_TIMESTAMP,'ASHA_ANC','Asha Anc Service Visit','ACTIVE'),
     (72,-1,CURRENT_TIMESTAMP,'ASHA_WPD','ASHA WPD Notification','ACTIVE'),
     (50,-1,CURRENT_TIMESTAMP,'ASHA_PNC','ASHA Post Natal Care Alerts','ACTIVE'),
     (51,-1,CURRENT_TIMESTAMP,'ASHA_CS','ASHA Child Services Alerts','ACTIVE'),
     (40,-1,CURRENT_TIMESTAMP,'MIG_OUT','MIGRATION OUT','ACTIVE'),
     (41,-1,CURRENT_TIMESTAMP,'MIG_IN_CONF','MIGRATION IN CONFIRMATION','ACTIVE'),
     (42,-1,CURRENT_TIMESTAMP,'MIG_OUT_CONF','MIGRATION OUT CONFIRMATION','ACTIVE'),
     (10,-1,CURRENT_TIMESTAMP,'MIGRATION_OUT_REQ','MIGRATION OUT REQUEST','ACTIVE'),
     (43,-1,CURRENT_TIMESTAMP,'MIG_REVERT','REVERT MIGRATION','ACTIVE'),
     (12,-1,CURRENT_TIMESTAMP,'MIGRATION_OUT_RESP','MIGRATION OUT RESPONSE','ACTIVE'),
     (9,-1,CURRENT_TIMESTAMP,'MIGRATION_IN_REQ','MIGRATION IN REQUEST','ACTIVE'),
     (11,-1,CURRENT_TIMESTAMP,'MIGRATION_IN_RESP','MIGRATION IN RESPONSE','ACTIVE');


alter table notification_type_master disable trigger update_form_master_info_on_notification_type_add_or_update;

TRUNCATE public.notification_type_master CASCADE;
INSERT INTO public.notification_type_master (id,created_by,created_on,modified_by,modified_on,code,"name","type",role_id,state,notification_for,action_on_role_id,data_query,action_query,order_no,color_code,data_for,url_based_action,url,modal_based_action,modal_name,is_location_filter_required,fetch_up_to_level,required_up_to_level,is_fetch_according_aoi,uuid) VALUES
	 (7,-1,'2018-08-07 22:35:01.794212',409,'2018-10-08 16:01:37.331','MO','Migration-Out','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'6c4f95d6-0fcf-43f7-87db-490d57771155'),
	 (6,-1,'2018-08-07 22:35:01.794212',409,'2018-10-08 16:01:41.098','MI','Migration-In','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'90c3261e-7b9f-48f3-950b-e1175eb95534'),
	 (5,1,'2018-04-30 19:47:21.717693',409,'2018-10-08 16:01:43.85','FHW_WPD','FHW WPD','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'7d3d809f-e90f-40a2-95a8-f1e5fe3aeb0d'),
	 (4,1,'2018-04-30 19:47:21.717693',409,'2018-10-08 16:01:47.871','FHW_CS','FHW CHild Service','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'91fb960a-6168-4d07-879f-fc3d9e637b18'),
	 (3,1,'2018-04-30 19:47:21.717693',409,'2018-10-08 16:01:54.265','FHW_PNC','FHW PNC','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'bfd41946-0837-4ff7-8c16-c90d8b019c06'),
	 (2,1,'2018-04-30 19:47:21.717693',409,'2018-10-08 16:02:12.11','FHW_ANC','FHW ANC','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'d6eae54f-0650-4985-9628-c6a37a92c4d4'),
	 (1,1,'2018-04-30 19:47:21.717693',409,'2018-10-08 16:02:25.318','LMPFU','LMP Follow Up','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'704fc0c3-5bfa-4d80-ac29-10f9d9bc816d'),
	 (8,-1,'2018-10-22 18:44:55.477199',-1,'2018-10-22 18:44:55.477199','DISCHARGE','WPD Discharge Date Entry','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'a3ad4736-bfc4-47f4-bdb3-73552fa975fc'),
	 (9,-1,'2018-11-02 00:33:48.934152',-1,'2018-11-02 00:33:48.934152','APPETITE','Appetite Test Alert For Child','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'47b7093d-1081-4077-aee3-798485b1c11b'),
	 (21,1,'2019-02-28 16:11:50.376508',1,'2019-02-28 16:11:50.376508','MR','MIGRATION REVERTED','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'e9138cac-ed61-4af6-b433-2c99f732948e'),
	 (33,66522,'2019-06-24 13:15:28.309',58784,'2019-06-25 12:49:42.388','tho_user_not_logged_in_last_4_days','THO Not Logged in for Last 4 Days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in_tho.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in_tho.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in_tho,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in_tho.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin'';',NULL,NULL,'USER',NULL,NULL,NULL,NULL,true,6,1,true,'a79c5f20-fde8-47f7-86f3-b140fb106274'),
	 (35,-1,'2019-08-01 17:11:25.400013',-1,'2019-08-01 17:11:25.400013','TT2_ALERT','TT2 Alert','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'cb251690-e755-4e6f-aacd-32deccb13b79'),
	 (36,-1,'2019-08-01 17:11:25.400013',-1,'2019-08-01 17:11:25.400013','IRON_SUCROSE_ALERT','Iron Sucrose Alert','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'99d837e9-3df4-4dc9-aafc-b84660d8b066'),
	 (38,-1,'2019-08-05 15:55:28.54106',-1,'2019-08-05 15:55:28.54106','SAM_SCREENING','SAM Screening','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'2c699c25-82ad-42d9-8132-94f8eefd48bd'),
	 (25,1,'2019-03-25 18:34:46.018372',1,'2019-03-25 18:34:46.018372','READ_ONLY','Read Only Notification','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'542c9217-bc48-4f1b-b5c8-4617652147ce'),
	 (63,74841,'2020-03-17 15:52:51.551',74841,'2020-04-27 11:28:11.373','cfhc_death_verification_mo','MO death verification from CHFC','WEB',NULL,'ACTIVE','MEMBER',NULL,'with role_id as (
select
	id as role_id,code
	from
	um_role_master
	where	id = (select role_id from um_user where	id = #loggedInUserId#) )
,notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (select role_id from	role_id )
	and nt.id = el.notification_type_id )

,notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and (#memberId# is null or member_id = #memberId#)
order by
	due_on,
	schedule_date
	limit #limit# offset #offset#
 )
 ,user_det as (
	select
		distinct on (notification_detail.location_id) notification_detail.location_id,
		um_user.user_name,
		um_user.first_name || '' '' || um_user.last_name as name,
		um_user.contact_number as mobile_no
	from
		um_user,
		notification_detail,
		location_hierchy_closer_det,
		um_user_location
	where
		notification_detail.location_id = location_hierchy_closer_det.child_id
		and um_user.state = ''ACTIVE''
		and um_user_location.state = ''ACTIVE''
		and um_user_location.loc_id = location_hierchy_closer_det.parent_id
		and um_user_location.user_id = um_user.id
		and um_user.role_id in (
		select
			id
		from
			um_role_master
		where
			code in (''FHW'') ))
,  death_det as (
	select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name, ''('' , imt_member.unique_health_id , '')'', ''('' , imt_family.family_id , '')'') as name ,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name,
		'' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	get_location_hierarchy(notification_detail.location_id) as loc_det,
	rch_member_death_deatil.dod
	from
	notification_detail
	inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
	inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
	inner join imt_member on
	notification_detail.member_id = imt_member.id
	inner join imt_family on
	imt_family.family_id = imt_member.family_id
	left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
	inner join rch_member_death_deatil on notification_detail.ref_code = rch_member_death_deatil.id
	group by
	notification_detail.id,
	notification_detail.location_id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
imt_member.unique_health_id,
	imt_family.family_id,
    rch_member_death_deatil.dod,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number
)
select
	notification_detail.id as "taskId",
	true as "isActionRequired",
	death_det.name as "Name",
	death_det.loc_det as "Location",
	to_char(death_det.dod , ''DD/MM/YYYY'') as "Death Date",
	death_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join death_det on
	death_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select
	id as "actionKey",
	value as "displayText",
	true as "isOtherDetailsRequired"
from
	listvalue_field_value_detail
where
	field_key = ''cfhc_death_verification_mo''',NULL,NULL,'MEMBER',false,NULL,false,NULL,NULL,NULL,NULL,NULL,'0902cb71-45f8-4625-926a-f218f015af0e'),
	 (24,409,'2019-03-13 16:50:06.266',97054,'2020-12-07 18:21:24.272','maternal_high_risk_notification_mo','High Risk Notification MO','WEB',NULL,'ACTIVE','MEMBER',NULL,'-------High Risk Notification MO---------
with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	imt_family.area_id as area_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	case
		when wt_high_risk_member_detail.diseases_type = ''M'' then concat(''Service Date : '', to_char(wt_high_risk_member_detail.service_date, ''DD/MM/YYYY''), ''<BR/>'' , ''LMP : '', to_char(rpr.lmp_date, ''DD/MM/YYYY''), ''<BR/>'' , ''EDD : '', to_char(rpr.edd, ''DD/MM/YYYY''))
		else concat(''Service Date : '', to_char(wt_high_risk_member_detail.service_date, ''DD/MM/YYYY''), ''<BR/>'' , ''DOB : '', to_char(imt_member.dob, ''DD/MM/YYYY''))
	end as member_detail,
	wt_high_risk_member_detail.high_risk_diseases as high_risk_diseases,
	wt_high_risk_member_detail.high_risk_diseases_detail as high_risk_diseases_detail,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join wt_high_risk_member_detail on
	wt_high_risk_member_detail.id = notification_detail.ref_code
inner join imt_member on
	wt_high_risk_member_detail.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join rch_pregnancy_registration_det as rpr on
	rpr.id = wt_high_risk_member_detail.pregnancy_reg_id
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	wt_high_risk_member_detail.diseases_type,
	wt_high_risk_member_detail.service_date,
	rpr.lmp_date,
	rpr.edd,
	imt_member.dob,
	wt_high_risk_member_detail.high_risk_diseases,
	wt_high_risk_member_detail.high_risk_diseases_detail,
	imt_member.unique_health_id ,
	imt_family.area_id ),
asha_areas as (
select
	distinct area_id
from
	benificary_det ),
asha_det as (
select
	asha_areas.area_id,
	u.first_name || '' '' || u.last_name || '' ('' || u.user_name || '')'' || ''(''||
	case
		when u.contact_number is not null then u.contact_number || '')''
		else ''N/A'' || '')''
	end as asha
from
	um_user_location ul,
	um_user u,
	asha_areas
where
	asha_areas.area_id = ul.loc_id
	and u.id = ul.user_id
	and u.state = ''ACTIVE''
	and ul.state = ''ACTIVE''
	and u.role_id = (
	select
		id
	from
		um_role_master
	where
		name = ''Asha'')
group by
	asha_areas.area_id,
	ul.state,
	u.state,
	u.first_name,
	u.last_name,
	u.user_name,
	u.contact_number) select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
,
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	benificary_det.loc_det as "Location",
	benificary_det.member_detail as "Beneficiary Detail",
	high_risk_diseases as "High Risk Diseases",
	high_risk_diseases_detail as "Diseases Detail",
	''FHW Details: '' || user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' || ''<BR/>Asha Details: '' || asha_det.asha as "FHW and Asha details"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
left join asha_det on
	asha_det.area_id = benificary_det.area_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''high_risk_notification_mo''',6,'#df0000','MEMBER',false,NULL,false,NULL,true,6,1,true,'657a5c6d-d04c-4f20-8f56-ecea21155c16'),
	 (55,-1,'2019-10-07 20:38:28.377266',-1,'2019-10-07 20:38:28.377266','FMO','Family Migration Out Notification','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'00a30b42-009b-469c-bdde-9115f57ce396'),
	 (56,-1,'2019-10-07 20:38:28.377266',-1,'2019-10-07 20:38:28.377266','FMI','Family Migration In Notification','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'be8ea6f3-0233-4a44-80cf-fb1ff10b2928'),
	 (26,409,'2019-04-05 18:59:20.782',97058,'2022-11-11 12:12:46.655','child_high_risk_notification_mo','Child High Risk Notification Mo','WEB',NULL,'ACTIVE','MEMBER',NULL,'----Child High Risk Notification Mo------
with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	imt_family.area_id as area_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	case
		when wt_high_risk_member_detail.diseases_type = ''M'' then concat(''Service Date : '', to_char(wt_high_risk_member_detail.service_date, ''DD/MM/YYYY''), ''<BR/>'' , ''LMP : '', to_char(rpr.lmp_date, ''DD/MM/YYYY''), ''<BR/>'' , ''EDD : '', to_char(rpr.edd, ''DD/MM/YYYY''))
		else concat(''Service Date : '', to_char(wt_high_risk_member_detail.service_date, ''DD/MM/YYYY''), ''<BR/>'' , ''DOB : '', to_char(imt_member.dob, ''DD/MM/YYYY''))
	end as member_detail,
	wt_high_risk_member_detail.high_risk_diseases as high_risk_diseases,
	wt_high_risk_member_detail.high_risk_diseases_detail as high_risk_diseases_detail,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join wt_high_risk_member_detail on
	wt_high_risk_member_detail.id = notification_detail.ref_code
inner join imt_member on
	wt_high_risk_member_detail.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join rch_pregnancy_registration_det as rpr on
	rpr.id = wt_high_risk_member_detail.pregnancy_reg_id
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	wt_high_risk_member_detail.diseases_type,
	wt_high_risk_member_detail.service_date,
	rpr.lmp_date,
	rpr.edd,
	imt_member.dob,
	wt_high_risk_member_detail.high_risk_diseases,
	wt_high_risk_member_detail.high_risk_diseases_detail,
	imt_member.unique_health_id ,
	imt_family.area_id ),
asha_areas as (
select
	distinct area_id
from
	benificary_det ),
asha_det as (
select
	asha_areas.area_id,
	u.first_name || '' '' || u.last_name || '' ('' || u.user_name || '')'' || ''(''||
	case
		when u.contact_number is not null then u.contact_number || '')''
		else ''N/A'' || '')''
	end as asha
from
	um_user_location ul,
	um_user u,
	asha_areas
where
	asha_areas.area_id = ul.loc_id
	and u.id = ul.user_id
	and u.state = ''ACTIVE''
	and ul.state = ''ACTIVE''
	and u.role_id = (
	select
		id
	from
		um_role_master
	where
		name = ''Asha'')
group by
	asha_areas.area_id,
	ul.state,
	u.state,
	u.first_name,
	u.last_name,
	u.user_name,
	u.contact_number) select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
,
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	benificary_det.loc_det as "Location",
	benificary_det.member_detail as "Beneficiary Detail",
	high_risk_diseases as "High Risk Diseases",
	high_risk_diseases_detail as "Diseases Detail",
	''FHW Details: '' || user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' || ''<BR/>Asha Details: '' || asha_det.asha as "FHW and Asha details"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
left join asha_det on
	asha_det.area_id = benificary_det.area_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''high_risk_notification_mo''',NULL,'#fa1638','MEMBER',false,NULL,false,NULL,true,6,1,true,'61bd6aa9-2eba-470c-a8a8-8f11c92db83b'),
	 (46,409,'2019-09-02 23:16:14.413',58784,'2020-05-26 11:17:24.6','asha_notification_overdue','More then 10 days notification overdue','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset#
							),
                                user_det as (

									select
									notification_detail.id,
									concat(concat_ws('' '',m.first_name,m.middle_name,m.last_name),''('',m.unique_health_id,'')'') as member_name,
									to_char(wta.notification_start_date,''DD/MM/YYYY'') as notification_schedule_on,
									to_char(wta.notification_due_date,''DD/MM/YYYY'') as notification_due_on,
									wta.notification_type,
									concat(concat_ws('' '',u.first_name,u.middle_name,u.last_name),''('',u.user_name,'')'') as name
									from
									notification_detail,
									wt_asha_10days_overdue_notification_details as wta
									,imt_member m
									,um_user u
									where wta.id =notification_detail.ref_code
									and m.id = wta.member_id
									and u.id = notification_detail.user_id
							)
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.member_name as "Member Name"
								,user_det.notification_type as "Notification Type"
								,user_det.notification_schedule_on as "Schedule On"
								,user_det.notification_due_on as "Due On"
								,user_det.name as "Asha Detail",
                                ''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                role_id
                            where
                                user_det.id = notification_detail.id
                            order by
                                due_on,
                                schedule_date;','select 1;',NULL,NULL,'USER',false,NULL,false,NULL,true,6,1,NULL,'229858fd-b9e1-4e61-b4d7-b616f2fda3df'),
	 (71,-1,'2020-05-18 20:16:40.339714',-1,'2020-05-18 20:16:40.339714','FHW_SAM_AFTER_CMAM','FHW SAM Screening after CMAM','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'990caa06-a116-4965-9b63-b9630767634c'),
	 (44,-1,'2019-08-31 14:17:27.696228',-1,'2019-08-31 14:17:27.696228','ASHA_ANC','Asha Anc Service Visit','MO',3,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'36c98c12-2dd1-4593-956b-4d8f9e02419d'),
	 (45,-1,'2019-09-02 09:15:54.138327',-1,'2019-09-02 09:15:54.138327','ASHA_WPD','ASHA WPD Notification','MO',3,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'43010c8f-1f19-45ea-9f9c-80f0afd12d30'),
	 (39,409,'2019-08-12 09:58:11.094',409,'2020-06-25 16:56:36.183','member_upcoming_edd','Upcoming And Due EDD','WEB',NULL,'ACTIVE','MEMBER',NULL,'with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
notification_detail1 as (
	SELECT notification.id,
	concat (im.first_name,'' '',im.last_name, ''('',im.unique_health_id, '')'') as name,
	im.unique_health_id,
	im.mobile_number,
	notification.location_id,
	rprd.edd,
	ifm.area_id,
	can_perform_action,
	case when ifm.location_id = rprd.location_id then ''Native'' else ''Migrated In'' end as "Migration Status"
	FROM notification_detail notification
	--inner join notification_type_det on notification_type_escalation_id = notification_esc_id
	inner join imt_member im on im.id = notification.member_id
	inner join rch_pregnancy_registration_det rprd on rprd.id = notification.ref_code
	inner join imt_family ifm on ifm.family_id = im.family_id
	--WHERE notification.notification_type_id in (select id from notification_type_master where code = ''member_edd_due'')
	--and notification.member_id=1771927

),
user_det as (
select
	distinct(notification_detail1.location_id),
	um_user.first_name || '' '' || um_user.last_name || '' ('' || um_user.user_name || '')'' || ''(''||
	case
		when um_user.contact_number is not null then um_user.contact_number || '')''
		else ''N/A'' || '')''
	end as fhw
from
	um_user ,
	notification_detail1,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail1.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' )),
asha_areas as (
select
	distinct area_id
from
	notification_detail1 ),
asha_det_temp as(
select
	asha_areas.area_id,
	max(u.id) as user_id
from
	um_user_location ul,
	um_user u,
	asha_areas
where
	asha_areas.area_id = ul.loc_id
	and u.id = ul.user_id
	and u.state = ''ACTIVE''
	and ul.state = ''ACTIVE''
	and u.role_id = (
	select
		id
	from
		um_role_master
	where
		name = ''Asha'') group by asha_areas.area_id
),
asha_det as (
select
	u.id,
	asha_det_temp.area_id,
	u.first_name || '' '' || u.last_name || '' ('' || u.user_name || '')'' || ''(''||
	case
		when u.contact_number is not null then u.contact_number || '')''
		else ''N/A'' || '')''
	end as asha
from
	um_user u,
	asha_det_temp
where
	u.id = asha_det_temp.user_id
)
select
notification.id as "taskId",
case when notification.edd < current_date and #limit# is not null then concat(''<font color="red">'',notification.name,''</font>'')
	else notification.name end  as "Member Name",
case when notification.edd < current_date and #limit# is not null  then concat(''<font color="red">'',notification.mobile_number,''</font>'')
	else notification.mobile_number end   "Member Contact No",
case when notification.edd < current_date and #limit# is not null then concat(''<font color="red">'',get_location_hierarchy(notification.location_id),''</font>'')
	else get_location_hierarchy(notification.location_id) end as "Location",
case when notification.edd < current_date and #limit# is not null then concat(''<font color="red">'',to_char(notification.edd,''dd-MM-yyyy''),''</font>'')
	else to_char(notification.edd,''dd-MM-yyyy'') end  as "EDD",
/*case when notification.edd < current_date then concat(''<font color="red">'',notification."Migration Status",''</font>'')
	else notification."Migration Status" end as "Migration Status",*/
case when notification.edd < current_date and #limit# is not null then concat(''<font color="red">'',users.fhw,''</font>'')
	else users.fhw end  as "FHW Details",
case when notification.edd < current_date and #limit# is not null then concat(''<font color="red">'',aa.asha,''</font>'')
	else aa.asha  end  as "Asha Details",
concat(''techo.manage.wpd({"id":"'',unique_health_id,''"})'') as "url_name",
case
		when can_perform_action is true then true
		else false
	end as "isActionRequired"
from notification_detail1 notification
left join user_det users on users.location_id = notification.location_id
left join asha_det aa on notification.area_id = aa.area_id','select id as "actionKey",value as "displayText",false as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''member_edd_due'';',NULL,'#f17073','MEMBER',true,'https://techo.gujarat.gov.in/imtecho-ui/soh/index.html',false,NULL,true,6,1,true,'43f29d49-f0c5-4081-a33b-80ed986ed06c'),
	 (43,-1,'2019-08-30 16:42:45.034148',-1,'2019-08-30 16:42:45.034148','ASHA_LMPFU','ASHA LMP Follow Up Notification','MO',3,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'87cb77b6-f6fa-4c82-abd6-f6e631960a3c'),
	 (40,409,'2019-08-13 10:48:49.784',58784,'2020-05-13 15:58:01.804','confirm_high_risk_beneficary','Confirmed High Risk PW','WEB',NULL,'ACTIVE','MEMBER',NULL,'-------High Risk Notification MO---------
with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	imt_family.area_id as area_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	wt_high_risk_confirm_case_det.high_risk_det,
	concat(''LMP : '', to_char(rpr.lmp_date, ''DD/MM/YYYY''), ''<BR/>'' , ''EDD : '', to_char(rpr.edd, ''DD/MM/YYYY'')) as member_detail,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join wt_high_risk_confirm_case_det on
	wt_high_risk_confirm_case_det.pregnancy_reg_det_id = notification_detail.ref_code
inner join imt_member on
	wt_high_risk_confirm_case_det.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join rch_pregnancy_registration_det as rpr on
	rpr.id = wt_high_risk_confirm_case_det.pregnancy_reg_det_id
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	rpr.lmp_date,
	rpr.edd,
	imt_member.dob,
	wt_high_risk_confirm_case_det.high_risk_det,
	imt_member.unique_health_id ,
	imt_family.area_id ),
asha_areas as (
select
	distinct area_id
from
	benificary_det ),
asha_det as (
select
	asha_areas.area_id,
	u.first_name || '' '' || u.last_name || '' ('' || u.user_name || '')'' || ''(''||
	case
		when u.contact_number is not null then u.contact_number || '')''
		else ''N/A'' || '')''
	end as asha
from
	um_user_location ul,
	um_user u,
	asha_areas
where
	asha_areas.area_id = ul.loc_id
	and u.id = ul.user_id
	and u.state = ''ACTIVE''
	and ul.state = ''ACTIVE''
	and u.role_id = (
	select
		id
	from
		um_role_master
	where
		name = ''Asha'')
group by
	asha_areas.area_id,
	ul.state,
	u.state,
	u.first_name,
	u.last_name,
	u.user_name,
	u.contact_number)
select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
	,
	case
		when can_perform_action is true then false
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	benificary_det.loc_det as "Location",
	benificary_det.member_detail as "Beneficiary Detail",
	benificary_det.high_risk_det as "High Risk Reason",
	''FHW Details: '' || user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' || ''<BR/>Asha Details: '' || asha_det.asha as "FHW and Asha details"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
left join asha_det on
	asha_det.area_id = benificary_det.area_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''high_risk_notification_mo''',13,'#ff574f','MEMBER',false,NULL,false,NULL,true,6,1,NULL,'8f297c58-3ccd-47c6-915f-732f246bb4ea'),
	 (10,409,'2018-11-03 12:24:09.075',409,'2020-03-03 18:16:04.958','last_4_day','Not logged in for the last 4 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin''',1,'#e2a71d','USER',false,NULL,false,NULL,true,6,1,true,'39df56ee-0b7b-49d5-9143-c2603872f9f5'),
	 (31,-1,'2019-05-20 10:32:59.083443',-1,'2019-05-20 10:32:59.083443','ASHA_PNC','ASHA Post Natal Care Alerts','MO',3,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'93576c97-172b-436f-9c9a-cb3e7226c524'),
	 (32,-1,'2019-05-20 10:32:59.083443',-1,'2019-05-20 10:32:59.083443','ASHA_CS','ASHA Child Services Alerts','MO',3,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'51095276-de38-4e97-8744-ec1a688764a2'),
	 (16,409,'2018-11-16 17:07:28.324',74840,'2019-11-26 17:39:46.303','still_birth_verification_mo','Still Birth Verification Mo','WEB',NULL,'ACTIVE','MEMBER',NULL,'-------Still Birth Verification Mo-----
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id as wpd_mother_id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_wpd_mother_master on
	rch_wpd_mother_master.id = notification_detail.ref_code
inner join imt_member on
	rch_wpd_mother_master.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id ) select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
,
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.date_of_delivery,
	''DD-MM-YYYY'') as "Date of delivery",
	--,num_of_child as "No of delivery",
 benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''still_birth_verification_mo''',NULL,'#467d22','MEMBER',false,NULL,false,NULL,true,6,1,true,'89a8f9a1-9048-4466-ad16-0f306c4c09fb'),
	 (48,-1,'2019-09-04 15:00:32.219299',-1,'2019-09-04 15:00:32.219299','FHW_PREG_CONF','FHW Pregnancy Confirmation','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'99a90346-3bfb-466e-bf5c-ebb35f0bee61'),
	 (49,-1,'2019-09-25 19:19:09.613114',-1,'2019-09-25 19:19:09.613114','FHW_DEATH_CONF','FHW Death Confirmation','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'52937a0b-bd6d-4c08-9b92-2cc59b9de115'),
	 (50,-1,'2019-09-25 19:19:09.613114',-1,'2019-09-25 19:19:09.613114','FHW_DELIVERY_CONF','FHW Delivery Confirmation','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'3f663af0-5b3a-4f1d-b8f3-ee3eb3b0e8fc'),
	 (51,-1,'2019-09-25 19:19:09.613114',-1,'2019-09-25 19:19:09.613114','FHW_MEMBER_MIGRATION','FHW Member Migration','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'7769c40b-a3be-488d-963c-af7269e85066'),
	 (52,-1,'2019-09-25 19:19:09.613114',-1,'2019-09-25 19:19:09.613114','FHW_FAMILY_MIGRATION','FHW Family Migration','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'cf160fb5-abea-4391-8508-a026ed36e1f8'),
	 (53,-1,'2019-09-25 19:19:09.613114',-1,'2019-09-25 19:19:09.613114','FHW_FAMILY_SPLIT','FHW family Split','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'2e7a407c-e08e-44f3-9d15-ce2750049057'),
	 (54,-1,'2019-09-25 19:19:10.26867',-1,'2019-09-25 19:19:10.26867','ASHA_READ_ONLY','Asha Read Only Notification','MO',3,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'1f1f8c02-5122-41e7-92ac-93f31a1561b6'),
	 (42,409,'2019-08-20 14:00:15.433',409,'2020-03-03 18:23:35.501','migration_pending_out','Migration OUT Pending','WEB',NULL,'ACTIVE','USER',NULL,'-------High Risk Notification MO---------
with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(um_user.id) as user_id,
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail
where
	um_user.id = notification_detail.user_id
),
benificary_det as(
select
	notification_detail.id as notification_id,
	mm.name as name,
	mm.location,
	mm.reported_on,
	mm.pending_at,
	mm.location_migrated_to
from
	notification_detail
inner join (select mm.id
,concat(concat_ws('' '',m.first_name,m.middle_name,m.last_name),''('',m.unique_health_id,'')'',''('',m.family_id,'')'') as name
,to_char(mm.reported_on,''DD/MM/YYYY'') as reported_on
,mm.location_migrated_to
,case when mm.location_migrated_to is not null
	then ''FHW''
	else ''Call Center'' end as pending_at
,get_location_hierarchy(location_migrated_from)  as location
from (select cast(mobile_data as json) as mobile_data_json,* from migration_master where id in (select ref_code from notification_detail)) mm
left join imt_member m on m.id = mm.member_id) as mm on
	mm.id = notification_detail.ref_code
),fhw_detail as (
select t.location_id,concat(concat_ws('' '',u.first_name,u.last_name),''('',u.contact_number,'')'') as name from (
select lh.child_id as location_id,max(u.id) as user_id
from um_user u,benificary_det bd,um_user_location uul,location_hierchy_closer_det lh
where u.state = ''ACTIVE'' and uul.state = ''ACTIVE'' and u.role_id = 2 and lh.child_id = bd.location_migrated_to
and lh.parent_id = uul.loc_id
group by lh.child_id) as t,um_user u
where u.id = t.user_id
)
select
	notification_detail.id as "taskId",
	case
		when can_perform_action is true then false
		else false
	end as "isActionRequired",
	benificary_det.name as "Name",
	benificary_det.location as "Location Migrated From",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "Reported by",
	benificary_det.reported_on as "Reported On"
	,case when benificary_det.pending_at = ''FHW'' then fhw_detail.name else benificary_det.pending_at end  as "Pending At"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.user_id = notification_detail.user_id
left join fhw_detail on
	fhw_detail.location_id = benificary_det.location_migrated_to
order by
	schedule_date;','select 1',11,'#4d0afc','MEMBER',false,NULL,false,NULL,true,6,1,true,'b58cd650-6230-4d0b-82e1-1d2f57b3826d'),
	 (15,409,'2018-11-03 12:29:21.809',409,'2019-09-02 15:01:39.286','twin_deliveries_verification','Twin Deliveries Verification','WEB',NULL,'ACTIVE','MEMBER',NULL,'----Twin Deliveries Verification----
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id as wpd_mother_id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_wpd_mother_master on
	rch_wpd_mother_master.id = notification_detail.ref_code
inner join imt_member on
	rch_wpd_mother_master.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id ),
number_of_delivery as(
select
	rch_wpd_child_master.wpd_mother_id,
	count(*) as num_of_child
from
	benificary_det,
	rch_wpd_child_master
where
	benificary_det.wpd_mother_id = rch_wpd_child_master.wpd_mother_id
group by
	rch_wpd_child_master.wpd_mother_id ) select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
,
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.date_of_delivery,
	''DD-MM-YYYY'') as "Date of delivery",
	num_of_child as "No of delivery",
	benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join number_of_delivery on
	number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''twin_delivery_verification_mo''',NULL,'#7d78e9','MEMBER',false,NULL,false,NULL,true,6,1,true,'df082992-8412-4674-9a8e-04fba3477296'),
	 (29,409,'2019-04-11 19:17:48.266',1117,'2019-12-24 15:16:25.423','fhs_not_logged_in_last_4_days','FHS Not logged in for last 4 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in_fhs.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in_fhs.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in_fhs,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in_fhs.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin''',NULL,'#3939cc','USER',false,NULL,false,NULL,true,6,1,true,'38a033a8-d318-4091-840b-721244533907'),
	 (12,409,'2018-11-03 12:26:26.546',409,'2020-03-03 18:21:39.492','not_enter_any_pregnancy_for_30_days','Not enter any pregnancy for 30 days','WEB',NULL,'ACTIVE','USER',NULL,'----------Not enter any pregnancy for 30 days----------
with role_id as (
select
	role_id
from
	um_user
where
	id = #loggedInUserId# ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #userId# is null
	or user_id = #userId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	id,
	user_name,
	name,
	mobile_no,
	role_name,
	string_agg(loc_det,
	''<br>'') as loc_det
from
	(
	select
		um_user.id,
		um_user.user_name,
		um_user.first_name || '' '' || um_user.last_name as name,
		um_user.contact_number as mobile_no,
		um_role_master.name as role_name,
		string_agg( location_master.name,
		'' > ''
	order by
		depth desc ) as loc_det
	from
		um_user,
		um_user_location,
		notification_detail,
		location_hierchy_closer_det,
		location_master,
		um_role_master
	where
		notification_detail.user_id = um_user.id
		and um_user_location.user_id = um_user.id
		and um_user_location.state = ''ACTIVE''
		and location_hierchy_closer_det.child_id = um_user_location.loc_id
		and depth < 5
		and location_master.id = location_hierchy_closer_det.parent_id
		and um_role_master.id = um_user.role_id
	group by
		um_user.id,
		um_user.user_name,
		um_user.first_name,
		um_user.last_name,
		um_role_master.name,
		um_user.contact_number,
		um_user_location.loc_id ) as usr_det
group by
	id,
	user_name,
	name,
	mobile_no,
	role_name ) select
	notification_detail.id as "taskId",
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	user_det.name || ''('' || user_det.user_name || '')'' as "Name",
	mobile_no as "Contact No",
	user_det.loc_det as "Location",
	to_char( wt_last_30_days_not_registerd_any_pregnancy.from_date,
	''DD-MM-YYYY'' ) as "From date",
	to_char( wt_last_30_days_not_registerd_any_pregnancy.to_date,
	''DD-MM-YYYY'' ) as "To date"
	--,''Call to '' || user_det.name as "actionName"

	from user_det,
	notification_detail,
	wt_last_30_days_not_registerd_any_pregnancy
where
	user_det.id = notification_detail.user_id
	and wt_last_30_days_not_registerd_any_pregnancy.id = notification_detail.ref_code
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_30_days_no_pregnancy_reg''',1,'#dc3e2c','USER',false,NULL,false,NULL,true,6,1,true,'259227b3-624a-4a22-8db8-896c1f80e642'),
	 (28,409,'2019-04-10 16:41:53.294',1117,'2019-12-24 15:16:06.365','sd_mch_deo_not_logged_in_last_4_days','SD/MCH DEO Not logged in for last 4 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in_sd_mch_deo.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in_sd_mch_deo.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in_sd_mch_deo,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in_sd_mch_deo.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin''',NULL,NULL,'USER',false,NULL,false,NULL,true,6,1,NULL,'6990abb5-56f5-449d-8349-eeabfca5eac9'),
	 (13,409,'2018-11-03 12:27:27.006',409,'2020-03-03 18:19:14.82','maternal_death_verification_ttc','Maternal Death Verification TTC','WEB',NULL,'ACTIVE','USER',NULL,'-------Maternal Death Verification TTC-----
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	case when imt_member.is_pregnant then ''PRE-PARTUM''
		when cast(rch_member_death_deatil.dod as date) = cast(imt_member.last_delivery_date as date) then ''INTRA-PARTUM''
		else ''POST-PARTUM'' end as death_type,
        reson.value as death_reason,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id,
	list.value as caste,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	max(case when r.registered_with_no_of_child is not null then r.registered_with_no_of_child else 0 end +
		case when r.total_out_come is not null then r.total_out_come else 0 end) as total_child
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_member_death_deatil on
	rch_member_death_deatil.id = notification_detail.ref_code
inner join imt_member on
	rch_member_death_deatil.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
inner join listvalue_field_value_detail list on
	imt_family.caste = cast(list.id as text)
left join rch_pregnancy_analytics_details r
	on r.member_id = imt_member.id and r.death_location_id = location_hierchy_closer_det.child_id and r.maternal_detah = true
left join listvalue_field_value_detail reson on
        reson.id = rch_member_death_deatil.death_reason\:\:bigint and
        rch_member_death_deatil.death_reason != ''null''
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
        reson.value,
        list.value,
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id
) select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	benificary_det.caste as "Caste",
	benificary_det.total_child as "No of Live Children",
	to_char(benificary_det.dob,
	''DD-MM-YYYY'') as "Birth date",
	to_char(benificary_det.dod,
	''DD-MM-YYYY'') as "Death date",
	benificary_det.death_type as "Death Type",
        benificary_det.death_reason as "Death Reason",
	to_char(benificary_det.last_delivery_date,
	''DD-MM-YYYY'') as "Last delivery date",
	benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''maternal_death_verification_ttc''',4,'#3f2996','MEMBER',false,NULL,false,NULL,true,6,1,true,'d5e22f33-fb6d-43a0-bf5c-d02894721213'),
	 (30,409,'2019-04-16 18:06:06.224',409,'2020-03-03 18:19:42.394','asha_not_logged_in_last_4_days','Asha Not Logged in for  Last 4 Days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in_asha.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in_asha.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in_asha,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in_asha.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin''',5,NULL,'USER',false,NULL,false,NULL,true,6,1,NULL,'9ef955ce-991c-48be-8624-ca081d09e4e2'),
	 (60,74841,'2019-12-12 18:22:26.399',409,'2020-03-03 18:19:59.062','migration_response_pending','Migration response pending','WEB',NULL,'ACTIVE','USER',NULL,'----Home Deliveries Verification----
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	mm.type,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join imt_member on
	imt_member.id = notification_detail.member_Id
inner join migration_master mm on
	mm.id = notification_detail.ref_code
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
group by
	notification_detail.id,
	mm.type,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	imt_member.unique_health_id

 )
	select
	notification_detail.id as "taskId",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	benificary_det.type as "Type",
	benificary_det.loc_det as "Location",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select 1;',NULL,'#1b39ae','MEMBER',false,NULL,false,NULL,true,6,1,true,'3b36a3ce-c212-4c29-bb70-a46985e1bb2f'),
	 (27,409,'2019-04-10 16:40:16.908',409,'2020-03-03 18:20:16.123','phc_chc_deo_not_logged_in_last_4_days','PHC/CHC DEO Not logged in for last 4 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
),notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
                                user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from
                                        (
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,
                                                (
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId", --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                role_name as "Role",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_4_days_not_logged_in_phc_chc_deo.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_4_days_not_logged_in_phc_chc_deo.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_4_days_not_logged_in_phc_chc_deo,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_4_days_not_logged_in_phc_chc_deo.id = notification_detail.ref_code
                            order by
                                due_on,
                                schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin''',NULL,'#2f44d7','USER',false,NULL,false,NULL,true,4,1,true,'bde69fb2-2051-49c9-b0b1-74e058b63edf'),
	 (59,409,'2019-12-04 15:53:08.279',409,'2020-03-03 18:20:22.624','home_delivery_verification','Home Delivery Verification','WEB',NULL,'ACTIVE','MEMBER',NULL,'----Home Deliveries Verification----
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id as wpd_mother_id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_wpd_mother_master on
	rch_wpd_mother_master.id = notification_detail.ref_code
inner join imt_member on
	rch_wpd_mother_master.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id ),
number_of_delivery as(
select
	rch_wpd_child_master.wpd_mother_id,
	count(*) as num_of_child
from
	benificary_det,
	rch_wpd_child_master
where
	benificary_det.wpd_mother_id = rch_wpd_child_master.wpd_mother_id
group by
	rch_wpd_child_master.wpd_mother_id ) select
	notification_detail.id as "taskId"
	--,true as "isActionRequired",
,
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.date_of_delivery,
	''DD-MM-YYYY'') as "Date of delivery",
	num_of_child as "No of delivery",
	benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join number_of_delivery on
	number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''twin_delivery_verification_mo''',NULL,'#3527de','MEMBER',false,NULL,false,NULL,true,6,1,true,'223493a5-f1f1-417d-910b-eb4223d91b50'),
	 (11,409,'2018-11-03 12:25:23.81',409,'2020-03-03 18:20:34.399','last_7_day','No data submitted in the last 7 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
    select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#)
        ),
        notification_type_det as(
            select
                distinct CONCAT(nt.id, ''_'', el.id) as notification_esc_id,
                nt.id as ntid,
                elr.can_perform_action
            from
                notification_type_master nt,
                escalation_level_master el,
                escalation_level_role_rel elr
            where
                nt.id = #taskTypeId#
                and el.id = elr.escalation_level_id
                and elr.role_id in (
                    select
                        role_id
                    from
                        role_id
                )
                and nt.id = el.notification_type_id
        ),
        notification_detail as (
            select
                *
            from
                techo_web_notification_master,
                notification_type_det
            where
                notification_type_escalation_id = notification_esc_id
                and location_id in (
                    select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')) and state = ''PENDING''
                                and (
                                    #userId# is null or user_id = #userId#)
                                    order by
                                        due_on,
                                        schedule_date
                                    limit
                                        #limit# offset #offset#
                                ), user_det as (
                                    select
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name,
                                        string_agg(loc_det, ''<br>'') as loc_det
                                    from(
                                            select
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name || '' '' || um_user.last_name as name,
                                                um_user.contact_number as mobile_no,
                                                um_role_master.name as role_name,
                                                string_agg(
                                                    location_master.name,
                                                    '' > ''
                                                    order by
                                                        depth desc
                                                ) as loc_det
                                            from
                                                um_user,
                                                um_user_location,(
                                                    select
                                                        distinct user_id
                                                    from
                                                        notification_detail
                                                ) as notification_detail,
                                                location_hierchy_closer_det,
                                                location_master,
                                                um_role_master
                                            where
                                                notification_detail.user_id = um_user.id
                                                and um_user_location.user_id = um_user.id
                                                and um_user_location.state = ''ACTIVE''
                                                and location_hierchy_closer_det.child_id = um_user_location.loc_id
                                                and parent_loc_type != ''S''
                                                and location_master.id = location_hierchy_closer_det.parent_id
                                                and um_role_master.id = um_user.role_id
                                            group by
                                                um_user.id,
                                                um_user.user_name,
                                                um_user.first_name,
                                                um_user.last_name,
                                                um_role_master.name,
                                                um_user.contact_number,
                                                um_user_location.loc_id
                                        ) as usr_det
                                    group by
                                        id,
                                        user_name,
                                        name,
                                        mobile_no,
                                        role_name
                                )
                            select
                                notification_detail.id as "taskId",
                                --,true as "isActionRequired",
                                case when can_perform_action is true then true else false end as "isActionRequired",
                                user_det.name || ''('' || user_det.user_name || '')'' as "Name",
                                mobile_no as "Contact No",
                                user_det.loc_det as "Location",
                                to_char(
                                    wt_last_7_days_not_subbmitted_any_data.from_date,
                                    ''DD-MM-YYYY''
                                ) as "From date",
                                to_char(
                                    wt_last_7_days_not_subbmitted_any_data.to_date,
                                    ''DD-MM-YYYY''
                                ) as "To date" --,''Call to '' || user_det.name as "actionName"
                            from
                                user_det,
                                notification_detail,
                                wt_last_7_days_not_subbmitted_any_data,
                                role_id
                            where
                                user_det.id = notification_detail.user_id
                                and wt_last_7_days_not_subbmitted_any_data.id = notification_detail.ref_code;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_7_days_no_data''',2,'#0e47b1','USER',false,NULL,false,NULL,true,6,1,true,'ebcd5c2b-8568-4cb8-98df-0f970b2f93b7'),
	 (18,409,'2018-12-31 02:04:07.159',409,'2020-03-03 18:17:35.053','maternal_death_verification_mo','Maternal Death Verification MO','WEB',NULL,'ACTIVE','MEMBER',NULL,'----------Maternal Death Verification MO----
--ANM Death
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	case
		when can_perform_action is true then nt.modal_name
		else null
	end as modal_name,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	notification_detail.modal_name,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name,
		'' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	case when imt_member.is_pregnant then ''PRE-PARTUM''
		when cast(rch_member_death_deatil.dod as date) = cast(imt_member.last_delivery_date as date) then ''INTRA-PARTUM''
		else ''POST-PARTUM'' end as death_type,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
		list.value as caste,
        reson.value as death_reason,
		max(case when r.registered_with_no_of_child is not null then r.registered_with_no_of_child else 0 end +
case when r.total_out_come is not null then r.total_out_come else 0 end) as total_child
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_member_death_deatil on
	rch_member_death_deatil.id = notification_detail.ref_code
inner join imt_member on
	rch_member_death_deatil.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
inner join listvalue_field_value_detail list on
	imt_family.caste = cast(list.id as text)
left join rch_pregnancy_analytics_details r
on r.member_id = imt_member.id and r.death_location_id = location_hierchy_closer_det.child_id and r.maternal_detah = true
left join listvalue_field_value_detail reson on
         cast(reson.id as text) = rch_member_death_deatil.death_reason and
         rch_member_death_deatil.death_reason != ''null''
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
        reson.value,
		list.value,
	notification_detail.id,
	notification_detail.modal_name,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date ) select
	notification_detail.id as "taskId",
	notification_detail.modal_name,
	--,true as "isActionRequired",
 case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name,
	benificary_det.caste as "Caste",
	benificary_det.total_child as "No of Live Children",
	to_char(benificary_det.dob,
	''DD-MM-YYYY'') as "Birth date",
	to_char(benificary_det.dod,
	''DD-MM-YYYY'') as "Death date",
        benificary_det.death_reason as "Death Reason",
	benificary_det.death_type as "Death Type",
	to_char(benificary_det.last_delivery_date,
	''DD-MM-YYYY'') as "Last delivery date",
	benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where
field_key = ''maternal_death_verification_mo'' and is_active=true
union all
select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''2018''
and is_active=true and id not in(787,792)',NULL,NULL,'MEMBER',false,NULL,true,'maternal_death_verification_modal_mo',true,6,1,true,'f68e2b18-70fa-466a-bee5-64b8e5670242'),
	 (19,409,'2018-12-31 02:05:24.632',409,'2020-03-03 18:18:17.005','child_death_veriffication_mo','Child Death Veriffication MO','WEB',NULL,'ACTIVE','MEMBER',NULL,'----------Child Death Veriffication MO--------
--ANM Death
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
        reson.value as death_reason,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_member_death_deatil on
	rch_member_death_deatil.id = notification_detail.ref_code
inner join imt_member on
	rch_member_death_deatil.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join listvalue_field_value_detail reson on
        reson.id\:\:text = rch_member_death_deatil.death_reason and rch_member_death_deatil.death_reason != ''null''
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
        reson.value,
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id ) select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.dob,
	''DD-MM-YYYY'') as "Birth date",
	to_char(benificary_det.dod,
	''DD-MM-YYYY'') as "Death date",
        benificary_det.death_reason as "Death Reason",
	--,benificary_det.is_pregnant as "Pregnant",to_char(benificary_det.last_delivery_date,''DD-MM-YYYY'') as "Last delivery date",
 benificary_det.loc_det as "Location",
	benificary_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''child_death_verification_mo''',NULL,NULL,'MEMBER',false,NULL,false,NULL,true,6,1,true,'044dc038-b336-4a56-b1ea-7949d91568bc'),
	 (14,409,'2018-11-03 12:28:36.156',409,'2020-03-03 18:22:42.834','child_death_veriffication_ttc','Child Death Veriffication TTC','WEB',NULL,'ACTIVE','USER',NULL,'---------Child Death Veriffication TTC-------
--Child less then 5 year
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #userId# is null
	or user_id = #userId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(um_user.id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail
where
	notification_detail.user_id = um_user.id ),
benificary_det as(
select
	notification_detail.id as notification_id,
	imt_member.id,
	imt_member.unique_health_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	listvalue_field_value_detail.value as death_reason
from
	imt_member,
	notification_detail,
	location_hierchy_closer_det,
	location_master,
	rch_member_death_deatil,
	listvalue_field_value_detail

where
	imt_member.id = notification_detail.member_id
	and location_hierchy_closer_det.child_id = notification_detail.location_id
	and rch_member_death_deatil.id = notification_detail.ref_code
	and location_master.id = location_hierchy_closer_det.parent_id
	and listvalue_field_value_detail.id = rch_member_death_deatil.death_reason\:\:bigint and rch_member_death_deatil.death_reason != ''null''
group by
	listvalue_field_value_detail.value,
	notification_detail.id,
	imt_member.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	imt_member.is_pregnant,
	rch_member_death_deatil.dod,
	imt_member.dob,
	imt_member.last_delivery_date ) select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 case
		when user_det.id is not null
		and can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.dob,
	''DD-MM-YYYY'') as "Birth date",
	to_char(benificary_det.dod,
	''DD-MM-YYYY'') as "Death date",
	benificary_det.death_reason as "Death Reason",
	benificary_det.loc_det as "Location",
	case
		when user_det.name is null then ''No MO assigned on this location''
		else user_det.name || ''('' || user_det.user_name || '')''
	end as "Mo Name",
	mobile_no as "Contact No"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.id = notification_detail.user_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''child_death_verification_ttc''',5,'#008040','MEMBER',false,NULL,false,NULL,true,6,1,true,'529c18f4-231c-43eb-a324-42d642aa0b05'),
	 (17,1,'2018-12-20 16:47:39.195',409,'2020-03-03 18:21:07.034','CMTC_PROBABLES','CMTC/NRC Probable Children','WEB',NULL,'ACTIVE','MEMBER',NULL,'with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# )
,user_det as (
select
	distinct(notification_detail.location_id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' ) )
,benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( mother.first_name, '' '', mother.middle_name, '' '', mother.last_name, '' ('', case when mother.mobile_number is null then ''N/A'' else mother.mobile_number end, '')'' ) as mother_name,
	concat (imt_member.unique_health_id,''('',imt_member.family_id,'')'') as unique_health_id,
	imt_member.id,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join imt_member on
	notification_detail.member_id = imt_member.id
left join imt_member as mother on
	mother.id = imt_member.mother_id
group by
    notification_detail.id,
	imt_member.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	mother.first_name,
	mother.middle_name,
	mother.last_name,
	mother.mobile_number,
	imt_member.unique_health_id,
	imt_member.family_id
)
select
	notification_detail.id as "taskId",
	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	case
		when can_perform_action is true then nt.modal_name
		else null
	end as "modal_name",
	benificary_det.id as "id",
	benificary_det.unique_health_id as "Unique Health ID (Family ID)",
	benificary_det.name as "Child Name",
	benificary_det.mother_name as "Mother Name",
	benificary_det.loc_det as "Location",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "FHW Name",
	TO_CHAR(notification_detail.created_on,''DD-MM-YYYY'') as "Generated On"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join notification_type_master nt on notification_detail.notification_type_id = nt.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;',NULL,NULL,'#152cdf','MEMBER',false,NULL,true,'cmtc_probable_confirmation_modal',false,6,1,true,'1ee172eb-6e76-46ab-bac6-4ad6d3e62c48'),
	 (41,409,'2019-08-19 19:37:31.96',409,'2020-03-03 18:23:21.639','migration_pending_in','Migration IN Pending','WEB',NULL,'ACTIVE','MEMBER',NULL,'-------High Risk Notification MO---------
with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #memberId# is null
	or member_id = #memberId#)
order by
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(um_user.id) as user_id,
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail
where
	um_user.id = notification_detail.user_id
),
benificary_det as(
select
	notification_detail.id as notification_id,
	mm.name as name,
	mm.unique_health_id,
	mm.village_name,
	mm.nearest_location,
	mm.reported_on,
	mm.pending_at,
	mm.source_asha_fhw_detail,
	mm.location_migrated_from
from
	notification_detail
inner join (select mm.id,case when m.id is not null then concat_ws('' '',m.first_name,m.middle_name,m.last_name)
else concat(concat_ws('' '',mobile_data_json->>''firstname'',mobile_data_json->>''middleName'',mobile_data_json->>''lastName'')
,'' ('',mobile_data_json->>''phoneNumber'','')'') end as name
,case when mm.id is not null then m.unique_health_id when mobile_data_json->>''healthId'' is null then ''N/A'' else mobile_data_json->>''healthId'' end as unique_health_id
,to_char(mm.reported_on,''DD/MM/YYYY'') as reported_on
,mm.location_migrated_from
,case when mm.member_id is not null
	and mm.out_of_state is false
	and is_temporary = false then ''FHW''
	else ''Call Center'' end as pending_at
,case when mobile_data_json->>''villageName'' is null then ''N/A'' else mobile_data_json->>''villageName'' end as village_name
--,cast(mobile_data_json->>''nearestLocId'' as bigint)
,case when mobile_data_json->>''nearestLocId'' is null then ''N/A'' else get_location_hierarchy(cast(mobile_data_json->>''nearestLocId'' as bigint)) end as nearest_location
,concat(''Name :'',case when mobile_data_json-> ''fhwOrAshaName'' is null then ''N/A'' else mobile_data_json->>''fhwOrAshaName'' end
,'', Phone :'',case when mobile_data_json->> ''fhwOrAshaPhone'' is null then ''N/A'' else mobile_data_json->>''fhwOrAshaPhone'' end) as source_asha_fhw_detail
from (select cast(mobile_data as json) as mobile_data_json,* from migration_master where id in (select ref_code from notification_detail)) mm
left join imt_member m on m.id = mm.member_id) as mm on
	mm.id = notification_detail.ref_code
),fhw_detail as (
select t.location_id,concat(concat_ws('' '',u.first_name,u.last_name),''('',u.contact_number,'')'') as name from (
select lh.child_id as location_id,max(u.id) as user_id
from um_user u,benificary_det bd,um_user_location uul,location_hierchy_closer_det lh
where u.state = ''ACTIVE'' and uul.state = ''ACTIVE'' and u.role_id = 2 and lh.child_id = bd.location_migrated_from
and lh.parent_id = uul.loc_id
group by lh.child_id) as t,um_user u
where u.id = t.user_id
)
select
	notification_detail.id as "taskId",
	case
		when can_perform_action is true then false
		else false
	end as "isActionRequired",
	benificary_det.name as "Name",
	benificary_det.unique_health_id as "Unique Health Id",
	benificary_det.village_name as "Village Name(Enter By FHW)",
	benificary_det.nearest_location as "Nearest Location",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "Reported by",
	benificary_det.reported_on as "Reported On"
	,benificary_det.source_asha_fhw_detail as "Source Fhw/Asha Details(Enter By FHW)"
	,case when benificary_det.pending_at = ''FHW'' then fhw_detail.name else benificary_det.pending_at end  as "Pending At"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
	--inner join number_of_delivery on number_of_delivery.wpd_mother_id = benificary_det.wpd_mother_id
inner join role_id on
	true
left join user_det on
	user_det.user_id = notification_detail.user_id
left join fhw_detail on
	fhw_detail.location_id = benificary_det.location_migrated_from
order by
	schedule_date;','select 1;',10,'#04a3ee','MEMBER',false,NULL,false,NULL,true,6,1,true,'bd035b04-bd30-4fe3-af56-53df33559333'),
	 (20,409,'2019-01-21 17:56:40.1',409,'2020-03-03 18:25:22.761','still_birth_verification_ttc','Still Birth Verification TTC','WEB',NULL,'ACTIVE','USER',NULL,'----Still Birth Verification TTC------
 with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#)),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and ( #userId# is null
	or user_id = #userId#)
order by
	due_on,
	schedule_date
limit #limit# offset #offset# ),
user_det as (
select
	distinct(um_user.id),
	um_user.user_name,
	um_user.first_name || '' '' || um_user.last_name as name,
	um_user.contact_number as mobile_no
from
	um_user,
	notification_detail
where
	notification_detail.user_id = um_user.id ),
benificary_det as(
select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name, '' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id as wpd_mother_id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	string_agg( location_master.name,
	'' > ''
order by
	depth desc ) as loc_det,
	imt_member.unique_health_id
from
	notification_detail
inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
inner join rch_wpd_mother_master on
	rch_wpd_mother_master.id = notification_detail.ref_code
inner join imt_member on
	rch_wpd_mother_master.member_id = imt_member.id
inner join imt_family on
	imt_family.family_id = imt_member.family_id
left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
group by
	notification_detail.id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number,
	imt_member.is_pregnant,
	rch_wpd_mother_master.id,
	rch_wpd_mother_master.date_of_delivery,
	imt_member.dob,
	imt_member.last_delivery_date,
	imt_member.unique_health_id ) select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 case
		when user_det.id is not null
		and can_perform_action is true then true
		else false
	end as "isActionRequired",
	benificary_det.name || ''('' || unique_health_id || '')'' as "Name",
	to_char(benificary_det.date_of_delivery,
	''DD-MM-YYYY'') as "Date of delivery",
	benificary_det.loc_det as "Location",
	case
		when user_det.name is null then ''No MO assigned for this location''
		else user_det.name || ''('' || user_det.user_name || '')''
	end as "Mo Name",
	mobile_no as "Contact No"
from
	notification_detail
inner join benificary_det on
	benificary_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.id = notification_detail.user_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''still_birth_verification_ttc''',6,NULL,'MEMBER',false,NULL,false,NULL,true,6,1,true,'d383c12a-f286-4ce6-aa33-37afb9c50426'),
	 (34,66522,'2019-06-28 11:50:21.715',409,'2020-03-03 18:25:40.643','member_edd_due','Member EDD Due','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
	id as role_id,
	code
from
	um_role_master
where
	id = (
	select
		role_id
	from
		um_user
	where
		id = #loggedInUserId#) ),
notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (
	select
		role_id
	from
		role_id )
	and nt.id = el.notification_type_id ),
notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset# ),
notification_detail1 as (
	SELECT notification.id,
	concat (im.first_name,'' '',im.last_name, ''('',im.unique_health_id, '')'') as name,
	im.mobile_number,
	notification.location_id,
	rprd.edd,
	ifm.area_id,
	can_perform_action,
	case when ifm.location_id = rprd.location_id then ''Native'' else ''Migrated In'' end as "Migration Status"
	FROM notification_detail notification
	--inner join notification_type_det on notification_type_escalation_id = notification_esc_id
	inner join imt_member im on im.id = notification.member_id
	inner join rch_pregnancy_registration_det rprd on rprd.id = notification.ref_code
	inner join imt_family ifm on ifm.family_id = im.family_id
	--WHERE notification.notification_type_id in (select id from notification_type_master where code = ''member_edd_due'')
	--and notification.member_id=1771927

),
user_det as (
select
	distinct(notification_detail1.location_id),
	um_user.first_name || '' '' || um_user.last_name || '' ('' || um_user.user_name || '')'' || ''(''||
	case
		when um_user.contact_number is not null then um_user.contact_number || '')''
		else ''N/A'' || '')''
	end as fhw
from
	um_user ,
	notification_detail1,
	location_hierchy_closer_det,
	um_user_location
where
	notification_detail1.location_id = location_hierchy_closer_det.child_id
	and um_user.state = ''ACTIVE''
	and um_user_location.state = ''ACTIVE''
	and um_user_location.loc_id = location_hierchy_closer_det.parent_id
	and um_user_location.user_id = um_user.id
	and um_user.role_id = (
	select
		id
	from
		um_role_master
	where
		code = ''FHW'' )),
asha_areas as (
select
	distinct area_id
from
	notification_detail1 ),
asha_det_temp as(
select
	asha_areas.area_id,
	max(u.id) as user_id
from
	um_user_location ul,
	um_user u,
	asha_areas
where
	asha_areas.area_id = ul.loc_id
	and u.id = ul.user_id
	and u.state = ''ACTIVE''
	and ul.state = ''ACTIVE''
	and u.role_id = (
	select
		id
	from
		um_role_master
	where
		name = ''Asha'') group by asha_areas.area_id
),
asha_det as (
select
	u.id,
	asha_det_temp.area_id,
	u.first_name || '' '' || u.last_name || '' ('' || u.user_name || '')'' || ''(''||
	case
		when u.contact_number is not null then u.contact_number || '')''
		else ''N/A'' || '')''
	end as asha
from
	um_user u,
	asha_det_temp
where
	u.id = asha_det_temp.user_id
)
select
notification.id as "taskId",
notification.name as "Member Name",
notification.mobile_number "Member Contact No",
get_location_hierarchy(notification.location_id) as "Location",
to_char(notification.edd,''dd-MM-yyyy'') as "EDD",
notification."Migration Status",
users.fhw as "FHW Details",
aa.asha as "Asha Details",
case
		when can_perform_action is true then true
		else false
	end as "isActionRequired"
from notification_detail1 notification
left join user_det users on users.location_id = notification.location_id
left join asha_det aa on notification.area_id = aa.area_id','select id as "actionKey",value as "displayText",false as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''member_edd_due'';',11,'#dc7a7a','MEMBER',false,NULL,false,NULL,true,6,1,true,'466b5535-c366-42e5-87f4-dc9512362aca'),
	 (64,-1,'2020-03-19 20:41:42.119462',-1,'2020-03-19 20:41:42.119462','TRAVELLERS_SCREENING','Travellers Screening Alert','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'6640e972-08a3-40d2-a894-bfc11d5502a2'),
	 (57,74840,'2019-11-27 11:59:53.697',409,'2020-06-07 11:25:48.282','CHO-HWC_not_logged_in_for_4_days','CHO-HWC Not Logged In  for 4 days','WEB',NULL,'ACTIVE','USER',NULL,'with role_id as (
select
        id as role_id,
        code
    from
        um_role_master
    where
        id = (
            select
                role_id
            from
                um_user
            where
                id = #loggedInUserId#))
,notification_type_det as(
                    select
                        distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
                        nt.id as ntid,
                        elr.can_perform_action
                    from
                        notification_type_master nt,
                        escalation_level_master el,
                        escalation_level_role_rel elr
                    where
                       nt.id = #taskTypeId#
                        and el.id = elr.escalation_level_id
                        and elr.role_id in (
                            select
                                role_id
                            from
                                role_id
                        )
                        and nt.id = el.notification_type_id
)
,notification_detail as (
select
    *
from
techo_web_notification_master,
notification_type_det
where
                        notification_type_escalation_id in (
                            select
                                notification_esc_id
                            from
                                notification_type_det
                        )
                        and location_id in (
                            select child_id from location_hierchy_closer_det
where parent_id in (select distinct case when #locationId# is not null
then  #locationId# else loc_id end from um_user_location where user_id = #loggedInUserId# and state = ''ACTIVE'')
                        )
                        and state = ''PENDING''
                        and (
                            #userId# is null
                            or user_id = #userId#)
                            order by
                                due_on,
                                schedule_date
                            limit
                                #limit# offset #offset#
),
user_det as (
            select
                id,
                user_name,
                name,
                mobile_no,
                role_name,
                string_agg(loc_det, ''<br>'') as loc_det
            from
                (
                    select
                        um_user.id,
                        um_user.user_name,
                        um_user.first_name || '' '' || um_user.last_name as name,
                        um_user.contact_number as mobile_no,
                        um_role_master.name as role_name,
                        string_agg(
                            location_master.name,
                            '' > ''
                            order by
                                depth desc
                        ) as loc_det
                    from
                        um_user,
                        um_user_location,
                        (
                            select
                                distinct user_id
                            from
                                notification_detail
                        ) as notification_detail,
                        location_hierchy_closer_det,
                        location_master,
                        um_role_master
                    where
                        notification_detail.user_id = um_user.id
                        and um_user_location.user_id = um_user.id
                        and um_user_location.state = ''ACTIVE''
                        and location_hierchy_closer_det.child_id = um_user_location.loc_id
                        and parent_loc_type != ''S''
                        and location_master.id = location_hierchy_closer_det.parent_id
                        and um_role_master.id = um_user.role_id
                    group by
                        um_user.id,
                        um_user.user_name,
                        um_user.first_name,
                        um_user.last_name,
                        um_role_master.name,
                        um_user.contact_number,
                        um_user_location.loc_id
                ) as usr_det
            group by
                id,
                user_name,
                name,
                mobile_no,
                role_name
        )
    select
        notification_detail.id as "taskId", --,true as "isActionRequired",
        case when can_perform_action is true then true else false end as "isActionRequired",
        user_det.name || ''('' || user_det.user_name || '')'' as "Name",
        mobile_no as "Contact No",
        role_name as "Role",
        user_det.loc_det as "Location",
        to_char(
            wt_last_4_days_not_logged_in_CHO_HWC.from_date,
            ''DD-MM-YYYY''
        ) as "From date",
        to_char(
            wt_last_4_days_not_logged_in_CHO_HWC.to_date,
            ''DD-MM-YYYY''
        ) as "To date" --,''Call to '' || user_det.name as "actionName"
    from
        user_det,
        notification_detail,
        wt_last_4_days_not_logged_in_CHO_HWC,
        role_id
    where
        user_det.id = notification_detail.user_id
        and wt_last_4_days_not_logged_in_CHO_HWC.id = notification_detail.ref_code
    order by
        due_on,
        schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''last_4_days_not_loggin'';',NULL,NULL,'USER',false,NULL,false,NULL,true,6,1,NULL,'0b0ea07b-ea12-4e51-a2eb-a0ef11bf8ab4'),
	 (62,74840,'2020-03-16 11:47:48.816',74840,'2020-03-16 17:07:25.425','cfhc_confirm_disease','CFHC- confirmed disease','WEB',NULL,'ACTIVE','MEMBER',NULL,'with role_id as (
select
	id as role_id,code
	from
	um_role_master
	where	id = (select role_id from um_user where	id = #loggedInUserId#) )
,notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (select role_id from	role_id )
	and nt.id = el.notification_type_id )

,notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and (#memberId# is null or member_id = #memberId#)
order by
	due_on,
	schedule_date
	limit #limit# offset #offset#
 )
 ,user_det as (
	select
		distinct on (notification_detail.location_id) notification_detail.location_id,
		um_user.user_name,
		um_user.first_name || '' '' || um_user.last_name as name,
		um_user.contact_number as mobile_no
	from
		um_user,
		notification_detail,
		location_hierchy_closer_det,
		um_user_location
	where
		notification_detail.location_id = location_hierchy_closer_det.child_id
		and um_user.state = ''ACTIVE''
		and um_user_location.state = ''ACTIVE''
		and um_user_location.loc_id = location_hierchy_closer_det.parent_id
		and um_user_location.user_id = um_user.id
		and um_user.role_id in (
		select
			id
		from
			um_role_master
		where
			code in (''mo_phc'',''mo_ayush'',''mo_corporation'',''mo_uphc'') ))
,  suspected_disease_det as (
	select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name,
		'' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	get_location_hierarchy(notification_detail.location_id) as loc_det,
	wt_cfhc_suspected_disease.type_of_disease
	from
	notification_detail
	inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
	inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
	inner join imt_member on
	notification_detail.member_id = imt_member.id
	inner join imt_family on
	imt_family.family_id = imt_member.family_id
	left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
	inner join wt_cfhc_suspected_disease on notification_detail.ref_code = wt_cfhc_suspected_disease.id
	group by
	notification_detail.id,
	notification_detail.location_id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	wt_cfhc_suspected_disease.type_of_disease,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number
)
select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	suspected_disease_det.name as "Name",
	suspected_disease_det.loc_det as "Location",
	concat_ws('','',suspected_disease_det.type_of_disease) as "Disease type",
	suspected_disease_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "MO Name"
from
	notification_detail
inner join suspected_disease_det on
	suspected_disease_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''cfhc_suspected_disease_status'' and (value not ilike ''Confirmed'')',NULL,'#900404','MEMBER',false,NULL,false,NULL,true,6,1,NULL,'b9887b16-4ef4-4a51-bd97-0be4cbb08160'),
	 (61,409,'2020-02-27 12:48:21.761',74840,'2020-03-19 12:13:03.947','cfhc_suspected_disease','CFHC - suspected Disease','WEB',NULL,'ACTIVE','MEMBER',NULL,'with role_id as (
select
	id as role_id,code
	from
	um_role_master
	where	id = (select role_id from um_user where	id = #loggedInUserId#) )
,notification_type_det as(
select
	distinct concat(nt.id, ''_'', el.id) as notification_esc_id,
	nt.id as ntid,
	elr.can_perform_action
from
	notification_type_master nt,
	escalation_level_master el,
	escalation_level_role_rel elr
where
	nt.id = #taskTypeId#
	and el.id = elr.escalation_level_id
	and elr.role_id in (select role_id from	role_id )
	and nt.id = el.notification_type_id )

,notification_detail as (
select
	*
from
	techo_web_notification_master,
	notification_type_det
where
	notification_type_escalation_id = notification_esc_id
	and location_id in (
	select
		child_id
	from
		location_hierchy_closer_det
	where
		parent_id in (
		select
			distinct
			case
				when #locationId# is not null then #locationId#
				else loc_id
			end
		from
			um_user_location
		where
			user_id = #loggedInUserId#
			and state = ''ACTIVE''))
	and state = ''PENDING''
	and (#memberId# is null or member_id = #memberId#)
	order by
	due_on,
	schedule_date
	limit #limit# offset #offset#
 )
 ,user_det as (
	select
		distinct on (notification_detail.location_id) notification_detail.location_id,
		um_user.user_name,
		um_user.first_name || '' '' || um_user.last_name as name,
		um_user.contact_number as mobile_no
	from
		um_user,
		notification_detail,
		location_hierchy_closer_det,
		um_user_location
	where
		notification_detail.location_id = location_hierchy_closer_det.child_id
		and um_user.state = ''ACTIVE''
		and um_user_location.state = ''ACTIVE''
		and um_user_location.loc_id = location_hierchy_closer_det.parent_id
		and um_user_location.user_id = um_user.id
		and um_user.role_id in (
		select
			id
		from
			um_role_master
		where
			code in (''mo_phc'',''mo_ayush'',''mo_corporation'',''mo_uphc'') )
	order by notification_detail.location_id,um_user.user_name
)
,  suspected_disease_det as (
	select
	notification_detail.id as notification_id,
	concat( imt_member.first_name, '' '', imt_member.middle_name, '' '', imt_member.last_name ) as name,
	concat( contact_person.first_name, '' '', contact_person.middle_name, '' '', contact_person.last_name,
		'' ('', case when contact_person.mobile_number is null then ''N/A'' else contact_person.mobile_number end, '')'' ) as contact_name,
	get_location_hierarchy(notification_detail.location_id) as loc_det,
	to_date(cast (notification_detail.created_on as text),''YYYYMMDD'')  as created_on,
	wt_cfhc_suspected_disease.type_of_disease
	from
	notification_detail
	inner join location_hierchy_closer_det on
	location_hierchy_closer_det.child_id = notification_detail.location_id
	inner join location_master on
	location_master.id = location_hierchy_closer_det.parent_id
	inner join imt_member on
	notification_detail.member_id = imt_member.id
	inner join imt_family on
	imt_family.family_id = imt_member.family_id
	left join imt_member as contact_person on
	contact_person.id = (
	case
		when imt_family.contact_person_id is null then imt_family.hof_id
		else imt_family.contact_person_id
	end )
	inner join wt_cfhc_suspected_disease on notification_detail.ref_code = wt_cfhc_suspected_disease.id
	group by
	notification_detail.id,
	notification_detail.location_id,
	imt_member.first_name,
	imt_member.middle_name,
	imt_member.last_name,
	notification_detail.created_on,
	wt_cfhc_suspected_disease.type_of_disease,
	contact_person.first_name,
	contact_person.middle_name,
	contact_person.last_name,
	contact_person.mobile_number
)
select
	notification_detail.id as "taskId",
	--,true as "isActionRequired",
 	case
		when can_perform_action is true then true
		else false
	end as "isActionRequired",
	suspected_disease_det.name as "Name",
	suspected_disease_det.loc_det as "Location",
	to_char(suspected_disease_det.created_on, ''DD-MM-YYYY'') as "Created On",
	concat_ws('','',suspected_disease_det.type_of_disease) as "Disease type",
	suspected_disease_det.contact_name as "Family Contact Detail",
	user_det.name || ''('' || user_det.user_name || '')'' || '' ('' || user_det.mobile_no || '')'' as "MO Name"
from
	notification_detail
inner join suspected_disease_det on
	suspected_disease_det.notification_id = notification_detail.id
inner join role_id on
	true
left join user_det on
	user_det.location_id = notification_detail.location_id
order by
	due_on,
	schedule_date;','select id as "actionKey",value as "displayText",true as "isOtherDetailsRequired" from listvalue_field_value_detail  where field_key = ''cfhc_suspected_disease_status''',NULL,'#a9012f','MEMBER',false,NULL,false,NULL,true,8,1,true,'a2ab6a54-334f-40c0-ab95-cbdb7b906f27'),
	 (65,-1,'2020-03-19 20:41:42.406637',-1,'2020-03-19 20:41:42.406637','GMA','Geriatrics Medication','MO',2,'ACTIVE','MEMBER',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,'a3d99e2a-b68f-40fd-8699-ec0f52840c64'),
	 (72,-1,'2022-11-21 16:53:44.526364',-1,'2022-11-21 16:53:44.526364','NCD_CLINIC_VISIT','NCD Clinic Visit','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,NULL),
	 (73,-1,'2022-11-21 16:53:44.526364',-1,'2022-11-21 16:53:44.526364','NCD_HOME_VISIT','NCD Home Visit','MO',2,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,false,NULL,NULL,true,NULL);



alter table notification_type_master enable trigger update_form_master_info_on_notification_type_add_or_update;

SELECT setval('notification_type_master_id_seq', COALESCE((SELECT MAX(id)+1 FROM notification_type_master), 1), false);

SELECT setval('form_master_id_seq', COALESCE((SELECT MAX(id)+1 FROM form_master), 1), false);
