
--change admin password
UPDATE public.um_user
    SET password = '2JpZ8jdGmQ0uln1aaUpZGTN8x3Ixqg8C'
    WHERE user_name = 'admin';

delete from um_role_master where id = 24;
INSERT INTO um_role_master (Id,created_by,created_on,modified_by,modified_on,code,description,"name",state,max_position,is_email_mandatory,is_contact_num_mandatory,is_aadhar_num_mandatory,is_convox_id_mandatory,short_name,is_last_name_mandatory,role_type,is_health_infra_mandatory,is_geolocation_mandatory) VALUES
	 (24,1,now(),97067,now(),'ASHA',NULL,'ASHA','ACTIVE',2,false,false,false,false,'ASHA',null,'MOBILE',null,null);

--asha user added
INSERT INTO public.um_user (created_by,created_on,modified_by,modified_on,aadhar_number,address,code,contact_number,date_of_birth,email_id,first_name,gender,last_name,middle_name,"password",prefered_language,role_id,state,user_name,server_type,search_text,title,imei_number,techo_phone_number,aadhar_number_encrypted,no_of_attempts,rch_institution_master_id,infrastructure_id,sdk_version,free_space_mb,latitude,longitude,report_preferred_language,login_code,convox_id,activation_date,member_master_id,location_id,pincode,pin,first_time_password_changed) VALUES
         (1,'2024-01-24 18:54:36.53',-1,'2024-01-31 11:34:12.106',NULL,NULL,NULL,NULL,NULL,NULL,'asha','F','test',NULL,'onfoP0JuT+ShfKQ+5xsjzhHYSRjwGa0o','EN',24,'ACTIVE','asha_test',NULL,'Mrs asha test asha_test SUPERADMIN','Mr',NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2024-01-24 18:54:36.462413',NULL,NULL,NULL,NULL,true);

-- public.ncd_amputation_member_detail definition
CREATE TABLE IF NOT EXISTS public.ncd_amputation_member_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	created_by int4 NULL,
	created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	modified_by int4 NULL,
	modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	screening_date timestamp NULL,
	amputation_present bool NULL,
	CONSTRAINT ncd_amputation_member_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_analytics_detail definition
CREATE TABLE IF NOT EXISTS public.ncd_analytics_detail (
	location_id int4 NOT NULL,
	month_year date NOT NULL,
	member_enrolled int4 NULL,
	member_30_plus int4 NULL,
	number_of_asha int4 NULL,
	number_of_inactive_asha int4 NULL,
	number_of_fhw int4 NULL,
	number_of_inactive_fhw int4 NULL,
	number_of_mo int4 NULL,
	number_of_active_mo int4 NULL,
	number_of_cbac_form_filled int4 NULL,
	number_of_member_at_risk int4 NULL,
	fhw_screened_diabetes_male int4 NULL,
	fhw_screened_diabetes_female int4 NULL,
	fhw_screened_hypertension_male int4 NULL,
	fhw_screened_hypertension_female int4 NULL,
	fhw_screened_oral_male int4 NULL,
	fhw_screened_oral_female int4 NULL,
	fhw_screened_breast_female int4 NULL,
	fhw_screened_cervical_female int4 NULL,
	no_abnormally_detected_male int4 NULL,
	no_abnormally_detected_female int4 NULL,
	fhw_referred_diabetes_male int4 NULL,
	fhw_referred_diabetes_female int4 NULL,
	fhw_referred_hypertension_male int4 NULL,
	fhw_referred_hypertension_female int4 NULL,
	fhw_referred_oral_male int4 NULL,
	fhw_referred_oral_female int4 NULL,
	fhw_referred_breast_female int4 NULL,
	fhw_referred_cervical_female int4 NULL,
	mo_examined_diabetes_male int4 NULL,
	mo_examined_diabetes_female int4 NULL,
	mo_examined_hypertension_male int4 NULL,
	mo_examined_hypertension_female int4 NULL,
	mo_examined_oral_male int4 NULL,
	mo_examined_oral_female int4 NULL,
	mo_examined_breast_female int4 NULL,
	mo_examined_cervical_female int4 NULL,
	mo_diagnosed_diabetes_male int4 NULL,
	mo_diagnosed_diabetes_female int4 NULL,
	mo_diagnosed_hypertension_male int4 NULL,
	mo_diagnosed_hypertension_female int4 NULL,
	mo_diagnosed_oral_male int4 NULL,
	mo_diagnosed_oral_female int4 NULL,
	mo_diagnosed_breast_female int4 NULL,
	mo_diagnosed_cervical_female int4 NULL,
	under_treatment_diabetes_male int4 NULL,
	under_treatment_diabetes_female int4 NULL,
	under_treatment_hypertension_male int4 NULL,
	under_treatment_hypertension_female int4 NULL,
	under_treatment_oral_male int4 NULL,
	under_treatment_oral_female int4 NULL,
	under_treatment_breast_female int4 NULL,
	under_treatment_cervical_female int4 NULL,
	secondary_referred_diabetes_male int4 NULL,
	secondary_referred_diabetes_female int4 NULL,
	secondary_referred_hypertension_male int4 NULL,
	secondary_referred_hypertension_female int4 NULL,
	secondary_referred_oral_male int4 NULL,
	secondary_referred_oral_female int4 NULL,
	secondary_referred_breast_female int4 NULL,
	secondary_referred_cervical_female int4 NULL,
	CONSTRAINT ncd_analytics_detail_t_pkey1 PRIMARY KEY (location_id, month_year)
);


-- public.ncd_cardiologist_data definition

CREATE TABLE IF NOT EXISTS public.ncd_cardiologist_data (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_on timestamp NULL DEFAULT now(),
	created_by int4 NULL,
	modified_on timestamp NULL DEFAULT now(),
	modified_by int4 NULL,
	screening_date timestamp NULL,
	case_confirmed bool NULL,
	note text NULL,
	satisfactory_image bool NULL,
	old_mi int4 NULL,
	lvh int4 NULL,
	"type" text NULL,
	CONSTRAINT ncd_cardiologist_data_pkey PRIMARY KEY (id)
);

-- public.ncd_cbac_nutrition_master definition

CREATE TABLE IF NOT EXISTS public.ncd_cbac_nutrition_master (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	cbac_table_id text NULL,
	child_nutrition_table_id text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	location_id int4 NULL,
	service_date timestamp NULL,
	CONSTRAINT ncd_cbac_nutrition_master_pkey PRIMARY KEY (id)
);


-- public.ncd_cvc_form_details definition
CREATE TABLE IF NOT EXISTS public.ncd_cvc_form_details (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NULL,
	mobile_end_date timestamp NULL,
	screening_date timestamp NULL,
	health_infra_id int4 NULL,
	take_medicine bool NULL,
	treatement_status text NULL,
	done_by varchar(200) NULL,
	CONSTRAINT ncd_cvc_form_details_pkey PRIMARY KEY (id)
);


-- public.ncd_dell_api_push_response definition

CREATE TABLE IF NOT EXISTS public.ncd_dell_api_push_response (
	id serial4 NOT NULL,
	location_id int4 NULL,
	location_name text NULL,
	request_start_date timestamp NULL,
	request_end_date timestamp NULL,
	response text NULL,
	enrolled int4 NULL,
	CONSTRAINT ncd_dell_api_push_response_pkey PRIMARY KEY (id)
);


-- public.ncd_diabetes_confirmation_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_diabetes_confirmation_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	fasting_blood_sugar int4 NULL,
	post_prandial_blood_sugar int4 NULL,
	flag bool NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	note text NULL,
	CONSTRAINT ncd_diabetes_confirmation_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_drug_inventory_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_drug_inventory_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	is_date timestamp NULL,
	is_issued bool NULL,
	is_received bool NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	health_infra_id int4 NULL,
	quantity_issued int4 NULL,
	quantity_received int4 NULL,
	parent_health_id int4 NULL,
	balance_in_hand int4 NULL,
	medicine_id int4 NULL,
	is_return bool NULL,
	CONSTRAINT ncd_drug_inventory_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_ecg_graph_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_ecg_graph_detail (
	id serial4 NOT NULL,
	avf_data text NULL,
	avl_data text NULL,
	lead1_data text NULL,
	lead2_data text NULL,
	lead3_data text NULL,
	v1_data text NULL,
	v2_data text NULL,
	v3_data text NULL,
	v4_data text NULL,
	v5_data text NULL,
	v6_data text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	avr_data text NULL,
	CONSTRAINT ncd_ecg_graph_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_ecg_member_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_ecg_member_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	created_by int4 NULL,
	created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	modified_by int4 NULL,
	modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	screening_date timestamp NULL,
	"type" text NULL,
	satisfactory_image bool NULL,
	old_mi int4 NULL,
	lvh int4 NULL,
	CONSTRAINT ncd_ecg_member_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_general_screening definition

CREATE TABLE IF NOT EXISTS public.ncd_general_screening (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NOT NULL,
	duration_of_hypertension int4 NULL,
	duration_of_diabetes int4 NULL,
	stroke_history bool NULL,
	stroke_duration_years int4 NULL,
	stroke_duration_months int4 NULL,
	stroke_symptoms text NULL,
	foot_problem_history bool NULL,
	foot_problem_cause text NULL,
	amputated_body_part text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	image text NULL,
	had_cervical_cancer_test bool NULL,
	had_breast_cancer_test bool NULL,
	had_oral_cancer_test bool NULL,
	image_uuid text NULL,
	CONSTRAINT ncd_general_screening_pkey PRIMARY KEY (id)
);


-- public.ncd_hypertension_diabetes_mental_health_master definition

CREATE TABLE IF NOT EXISTS public.ncd_hypertension_diabetes_mental_health_master (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NULL,
	hypertension_id int4 NULL,
	diabetes_id int4 NULL,
	mental_health_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	CONSTRAINT ncd_hypertension_diabetes_mental_health_master_pkey PRIMARY KEY (id)
);


-- public.ncd_master definition

CREATE TABLE IF NOT EXISTS public.ncd_master (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	health_infra_id int4 NULL,
	location_id int4 NULL,
	created_on timestamp NULL,
	modified_on timestamp NULL,
	created_by int4 NULL,
	modified_by int4 NULL,
	disease_code varchar(20) NULL,
	status varchar(50) NULL,
	sub_status varchar(50) NULL,
	is_active bool NULL,
	flag bool NULL,
	last_visit_date timestamp NULL,
	CONSTRAINT ncd_master_pkey PRIMARY KEY (id)
);


-- Table Triggers
DROP TRIGGER IF EXISTS insert_ncd_status_trigger ON public.ncd_master;
CREATE TRIGGER insert_ncd_status_trigger BEFORE INSERT ON public.ncd_master FOR EACH ROW EXECUTE FUNCTION update_ncd_status_trigger();

DROP TRIGGER IF EXISTS update_ncd_status_trigger ON public.ncd_master;
CREATE TRIGGER update_ncd_status_trigger BEFORE UPDATE ON public.ncd_master FOR EACH ROW EXECUTE FUNCTION update_ncd_status_trigger();

-- public.ncd_mbbsmo_review_detail definition
CREATE TABLE IF NOT EXISTS public.ncd_mbbsmo_review_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	is_approved bool NULL,
	"comment" varchar(200) NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	CONSTRAINT ncd_mbbsmo_review_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_cbac_detail definition
CREATE TABLE IF NOT EXISTS public.ncd_member_cbac_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	smoke_or_consume_gutka text NULL,
	waist text NULL,
	consume_alcohol_daily bool NULL,
	physical_activity_150_min text NULL,
	bp_diabetes_heart_history bool NULL,
	shortness_of_breath bool NULL,
	fits_history bool NULL,
	two_weeks_coughing bool NULL,
	mouth_opening_difficulty bool NULL,
	blood_in_sputum bool NULL,
	two_weeks_ulcers_in_mouth bool NULL,
	two_weeks_fever bool NULL,
	change_in_tone_of_voice bool NULL,
	loss_of_weight bool NULL,
	patch_on_skin bool NULL,
	night_sweats bool NULL,
	taking_anti_tb_drugs bool NULL,
	difficulty_holding_objects bool NULL,
	sensation_loss_palm bool NULL,
	family_member_suffering_from_tb bool NULL,
	history_of_tb bool NULL,
	lump_in_breast bool NULL,
	bleeding_after_menopause bool NULL,
	nipple_blood_stained_discharge bool NULL,
	bleeding_after_intercourse bool NULL,
	change_in_size_of_breast bool NULL,
	foul_vaginal_discharge bool NULL,
	bleeding_between_periods bool NULL,
	occupational_exposure text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	score int4 NULL,
	age_at_menarche int4 NULL,
	menopause_arrived bool NULL,
	duration_of_menopause int4 NULL,
	pregnant bool NULL,
	lactating bool NULL,
	regular_periods bool NULL,
	lmp timestamp NULL,
	bleeding text NULL,
	associated_with text NULL,
	remarks text NULL,
	diagnosed_for_hypertension bool NULL,
	under_treatement_for_hypertension bool NULL,
	diagnosed_for_diabetes bool NULL,
	under_treatement_for_diabetes bool NULL,
	diagnosed_for_heart_diseases bool NULL,
	under_treatement_for_heart_diseases bool NULL,
	diagnosed_for_stroke bool NULL,
	under_treatement_for_stroke bool NULL,
	diagnosed_for_kidney_failure bool NULL,
	under_treatement_for_kidney_failure bool NULL,
	diagnosed_for_non_healing_wound bool NULL,
	under_treatement_for_non_healing_wound bool NULL,
	diagnosed_for_copd bool NULL,
	under_treatement_for_copd bool NULL,
	diagnosed_for_asthama bool NULL,
	under_treatement_for_asthama bool NULL,
	diagnosed_for_oral_cancer bool NULL,
	under_treatement_for_oral_cancer bool NULL,
	diagnosed_for_breast_cancer bool NULL,
	under_treatement_for_breast_cancer bool NULL,
	diagnosed_for_cervical_cancer bool NULL,
	under_treatement_for_cervical_cancer bool NULL,
	height int4 NULL,
	weight numeric(6, 2) NULL,
	bmi numeric(8, 2) NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	family_id int4 NULL,
	referral_id int4 NULL,
	manual_verification bool NULL,
	location_id int4 NULL,
	interest_in_doing_things text NULL,
	feeling_ddoh text NULL,
	feeling_unsteady bool NULL,
	suffering_from_disability bool NULL,
	need_help_to_perform_activity bool NULL,
	forget_name_or_address bool NULL,
	ulcers_in_mouth bool NULL,
	patch_in_mouth bool NULL,
	cloudy_or_blurred_vision bool NULL,
	pain_in_eyes_lasting bool NULL,
	difficulty_in_hearing bool NULL,
	hypo_patch_or_discolored_lesion bool NULL,
	ulceration_on_palm_or_sole bool NULL,
	clawing_of_fingers bool NULL,
	inability_to_close_eyelid bool NULL,
	pain_while_chewing bool NULL,
	having_known_disability bool NULL,
	using_state_health_insurance_schemes bool NULL,
	state_health_insurance_schemes text NULL,
	recurrent_ulceration bool NULL,
	recurrent_tingling bool NULL,
	cloudy_vision bool NULL,
	reading_difficluty bool NULL,
	eye_pain bool NULL,
	eye_redness bool NULL,
	hearing_difficulty bool NULL,
	chewing_pain bool NULL,
	mouth_ulcers bool NULL,
	mouth_patch bool NULL,
	thick_skin bool NULL,
	nodules_on_skin bool NULL,
	tingling_in_hand bool NULL,
	inability_close_eyelid bool NULL,
	feet_weakness bool NULL,
	crop_residue_burning bool NULL,
	garbage_burning bool NULL,
	working_industry bool NULL,
	interest_doing_things text NULL,
	feeling_down text NULL,
	forget_names bool NULL,
	need_help_from_others bool NULL,
	physical_disability bool NULL,
	hmis_id int8 NULL,
	growth_in_mouth bool NULL,
	known_disabilities text NULL,
	blurred_vision_eye text NULL,
	physical_activity_30_min text NULL,
	cbac_and_nutrition_master_id int4 NULL,
	treatment_for_leprosy bool NULL,
	consume_alcohol text NULL,
	smoke text NULL,
	consume_gutka text NULL,
	CONSTRAINT ncd_member_cbac_detail_t_pkey PRIMARY KEY (id)
);
-- Indexes
DROP INDEX IF EXISTS ncd_member_cbac_detail_t_family_id_idx;
CREATE INDEX ncd_member_cbac_detail_t_family_id_idx ON public.ncd_member_cbac_detail USING btree (family_id);

DROP INDEX IF EXISTS ncd_member_cbac_detail_t_member_id_idx;
CREATE INDEX ncd_member_cbac_detail_t_member_id_idx ON public.ncd_member_cbac_detail USING btree (member_id);



-- public.ncd_member_cervical_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_cervical_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	cervical_related_symptoms bool NULL,
	excessive_bleeding_during_periods bool NULL,
	bleeding_between_periods bool NULL,
	bleeding_after_intercourse bool NULL,
	excessive_smelling_vaginal_discharge bool NULL,
	postmenopausal_bleeding bool NULL,
	refferal_done bool NULL,
	refferal_place text NULL,
	remarks text NULL,
	polyp bool NULL,
	ectopy bool NULL,
	hypertrophy bool NULL,
	prolapse_uterus bool NULL,
	bleeds_on_touch bool NULL,
	unhealthy_cervix bool NULL,
	suspicious_looking_cervix bool NULL,
	frank_malignancy bool NULL,
	other_symptoms bool NULL,
	other_desc varchar(250) NULL,
	via_exam varchar(200) NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	via_exam_points varchar(500) NULL,
	other_clinical_examination text NULL,
	referral_id int4 NULL,
	papsmear_test bool NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	via_test varchar(50) NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL,
	external_genitalia_healthy bool NULL,
	consultant_flag bool NULL,
	other_finding bool NULL,
	does_suffering bool NULL,
	bimanual_examination varchar NULL,
	other_finding_description varchar NULL,
	trained_via_examination bool NULL,
	excessive_discharge bool NULL,
	visual_polyp text NULL,
	visual_ectopy text NULL,
	visual_hypertrophy text NULL,
	visual_bleeds_on_touch text NULL,
	visual_unhealthy_cervix text NULL,
	visual_suspicious_looking text NULL,
	visual_frank_growth text NULL,
	visual_prolapse_uterus text NULL,
	other_symptoms_description varchar NULL,
	status varchar(50) NULL,
	master_id int4 NULL,
	is_suspected bool NULL,
	take_medicine bool NULL,
	treatment_status text NULL,
	cancer_screening_master_id int4 NULL,
	diagnosed_earlier bool NULL,
	currently_under_treatment bool NULL,
	current_treatment_place text NULL,
	govt_facility_id int4 NULL,
	private_facility text NULL,
	out_of_territory_facility text NULL,
	future_screening_date timestamp NULL,
	CONSTRAINT ncd_member_cervical_detail_t_pkey PRIMARY KEY (id)
);
--indexes
DROP INDEX IF EXISTS ncd_member_cervical_detail_modified_on_idx;
CREATE INDEX ncd_member_cervical_detail_modified_on_idx ON public.ncd_member_cervical_detail USING btree (modified_on DESC NULLS LAST);
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_created_by_idx;
CREATE INDEX ncd_member_cervical_detail_t_created_by_idx ON public.ncd_member_cervical_detail USING btree (created_by);
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_member_id_idx;
CREATE INDEX ncd_member_cervical_detail_t_member_id_idx ON public.ncd_member_cervical_detail USING btree (member_id);
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_refferal_done_refferal_place_idx;
CREATE INDEX ncd_member_cervical_detail_t_refferal_done_refferal_place_idx ON public.ncd_member_cervical_detail USING btree (refferal_done, refferal_place);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_cervical_hmis_update ON public.ncd_member_cervical_detail;
create trigger ncd_cervical_hmis_update after
insert
    on
    public.ncd_member_cervical_detail for each row execute function ncd_cervical_hmis_updation();


-- public.ncd_member_clinic_visit_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_clinic_visit_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	clinic_date timestamp NULL,
	clinic_type varchar(200) NULL,
	systolic_bp int4 NULL,
	diastolic_bp int4 NULL,
	pulse_rate int4 NULL,
	hypertension_result varchar(200) NULL,
	mental_health_result varchar(200) NULL,
	diabetes_result varchar(200) NULL,
	talk int4 NULL,
	own_daily_work int4 NULL,
	social_work int4 NULL,
	understanding int4 NULL,
	blood_sugar int4 NULL,
	patient_taking_medicine bool NULL,
	required_reference bool NULL,
	reference_reason text NULL,
	referral_place varchar(200) NULL,
	refer_status varchar(200) NULL,
	flag bool NULL,
	flag_reason varchar(200) NULL,
	other_reason text NULL,
	followup_visit_type varchar(200) NULL,
	followup_date timestamp NULL,
	remarks text NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	death_date date NULL,
	death_place text NULL,
	death_reason text NULL,
	health_infra_id int4 NULL,
	height int4 NULL,
	weight numeric(6, 2) NULL,
	bmi numeric(6, 2) NULL,
	CONSTRAINT ncd_member_clinic_visit_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_complaints definition

CREATE TABLE IF NOT EXISTS public.ncd_member_complaints (
	id serial4 NOT NULL,
	member_id int4 NULL,
	complaint varchar(1000) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modifiedby int4 NULL,
	modified_on timestamp NULL,
	referral_id int4 NULL,
	modified_by int4 NULL,
	CONSTRAINT ncd_member_complaints_pkey PRIMARY KEY (id)
);


-- public.ncd_member_cvc_clinic_visit_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_cvc_clinic_visit_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	clinic_date timestamp NULL,
	clinic_type varchar(200) NULL,
	patient_taking_medicine bool NULL,
	required_reference bool NULL,
	reference_reason text NULL,
	referral_place varchar(200) NULL,
	refer_status varchar(200) NULL,
	flag bool NULL,
	flag_reason varchar(200) NULL,
	other_reason text NULL,
	followup_visit_type varchar(200) NULL,
	followup_date timestamp NULL,
	remarks text NULL,
	death_date date NULL,
	death_place text NULL,
	death_reason text NULL,
	health_infra_id int4 NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	CONSTRAINT ncd_member_cvc_clinic_visit_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_cvc_home_visit_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_cvc_home_visit_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	clinic_date timestamp NULL,
	patient_taking_medicine bool NULL,
	any_adverse_effect bool NULL,
	adverse_effect text NULL,
	required_reference bool NULL,
	given_consent bool NULL,
	referral_place varchar(200) NULL,
	other_referral_place varchar(200) NULL,
	referral_id int4 NULL,
	remarks text NULL,
	death_date date NULL,
	death_place text NULL,
	death_reason text NULL,
	health_infra_id int4 NULL,
	reference_reason text NULL,
	refer_status varchar(200) NULL,
	flag bool NULL,
	flag_reason varchar(200) NULL,
	other_reason text NULL,
	followup_visit_type varchar(200) NULL,
	followup_date timestamp NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	CONSTRAINT ncd_member_cvc_home_visit_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_diabetes_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_diabetes_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	blood_sugar int4 NULL,
	earlier_diabetes_diagnosis bool NULL,
	currently_under_treatment bool NULL,
	refferal_done bool NULL,
	refferal_place text NULL,
	remarks text NULL,
	fasting_blood_sugar int4 NULL,
	post_prandial_blood_sugar int4 NULL,
	hba1c numeric NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	sensory_loss bool NULL,
	regular_rythm_cardio bool NULL,
	any_injuries bool NULL,
	edema bool NULL,
	prominent_veins bool NULL,
	gangrene_feet bool NULL,
	ulcers_feet bool NULL,
	calluses_feet bool NULL,
	peripheral_pulses bool NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	gluco_strips_available bool NULL,
	dka bool NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL,
	current_treatment_place varchar(20) NULL,
	is_continue_treatment_from_current_place bool NULL,
	measurement_type varchar(20) NULL,
	is_suspected bool NULL,
	flag bool NULL,
	current_treatment_place_other text NULL,
	urine_sugar int4 NULL,
	status varchar(50) NULL,
	does_suffering bool NULL,
	master_id int4 NULL,
	note text NULL,
	take_medicine bool NULL,
	treatment_status text NULL,
	visit_type varchar(30) NULL,
	weight numeric(6, 2) NULL,
	height int4 NULL,
	bmi numeric(6, 2) NULL,
	hyper_dia_mental_master_id int4 NULL,
	govt_facility_id int4 NULL,
	private_facility text NULL,
	out_of_territory_facility text NULL,
	CONSTRAINT ncd_member_diabetes_detail_t_pkey PRIMARY KEY (id)
);
-- Indexes
DROP INDEX IF EXISTS ncd_member_diabetes_detail_modified_on_idx;
CREATE INDEX ncd_member_diabetes_detail_modified_on_idx ON public.ncd_member_diabetes_detail USING btree (modified_on DESC NULLS LAST);

DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_created_by_idx;
CREATE INDEX ncd_member_diabetes_detail_t_created_by_idx ON public.ncd_member_diabetes_detail USING btree (created_by);

DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_member_id_idx;
CREATE INDEX ncd_member_diabetes_detail_t_member_id_idx ON public.ncd_member_diabetes_detail USING btree (member_id);

DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_refferal_done_refferal_place_idx;
CREATE INDEX ncd_member_diabetes_detail_t_refferal_done_refferal_place_idx ON public.ncd_member_diabetes_detail USING btree (refferal_done, refferal_place);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_diabetes_hmis_update ON public.ncd_member_diabetes_detail;
create trigger ncd_diabetes_hmis_update after
insert
    on
    public.ncd_member_diabetes_detail for each row execute function ncd_diabetes_hmis_updation();


-- public.ncd_member_diseases_diagnosis definition

CREATE TABLE IF NOT EXISTS public.ncd_member_diseases_diagnosis (
	member_id int4 NULL,
	diagnosis text NULL,
	remarks text NULL,
	disease_code varchar(50) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	id serial4 NOT NULL,
	diagnosed_on timestamp NULL,
	referral_id int4 NULL,
	readings varchar NULL,
	location_id int4 NULL,
	status varchar(255) NULL,
	is_case_completed bool NULL,
	sub_type varchar(50) NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL
);

-- Table Triggers
drop trigger if exists ncd_disease_diagnosis_hmis_update on public.ncd_member_diseases_diagnosis;
create trigger ncd_disease_diagnosis_hmis_update after
insert
    on
    public.ncd_member_diseases_diagnosis for each row execute function ncd_disease_diagnosis_hmis_updation();


-- public.ncd_member_disesase_followup definition

CREATE TABLE IF NOT EXISTS public.ncd_member_disesase_followup (
	id serial4 NOT NULL,
	member_id int4 NULL,
	followup_date timestamp NULL,
	disease_code varchar(50) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_on timestamp NULL,
	modified_by int4 NULL,
	referral_id int4 NULL,
	referral_from varchar NULL,
	health_infrastructure_id int4 NULL
);


-- public.ncd_member_disesase_medicine definition

CREATE TABLE IF NOT EXISTS public.ncd_member_disesase_medicine (
	id serial4 NOT NULL,
	member_id int4 NULL,
	medicine_id int4 NULL,
	disease_code varchar(50) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_on timestamp NULL,
	modified_by int4 NULL,
	diagnosed_on timestamp NULL,
	referral_id int4 NULL,
	reference_id int4 NULL,
	frequency int4 NULL,
	duration int4 NULL,
	quantity int4 NULL,
	special_instruction text NULL,
	expiry_date timestamp NULL,
	is_active bool NULL,
	is_deleted bool NULL,
	deleted_on timestamp NULL,
	start_date timestamp NULL
);

-- Table Triggers
drop trigger if exists ncd_member_disesase_medicine_update_trigger on public.ncd_member_disesase_medicine;
create trigger ncd_member_disesase_medicine_update_trigger after
update
    on
    public.ncd_member_disesase_medicine for each row execute function ncd_member_disesase_medicine_update_trigger_func();


-- public.ncd_member_ecg_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_ecg_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NOT NULL,
	symptom text NULL,
	other_symptom text NULL,
	detection text NULL,
	ecg_type text NULL,
	recommendation text NULL,
	risk text NULL,
	anomalies text NULL,
	heart_rate int4 NULL,
	pr int4 NULL,
	qrs int4 NULL,
	qt int4 NULL,
	qtc float8 NULL,
	graph_detail_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	report_pdf_doc_id int8 NULL,
	report_image_doc_id int8 NULL,
	report_pdf_doc_uuid text NULL,
	report_image_doc_uuid text NULL,
	CONSTRAINT ncd_member_ecg_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_followup_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_followup_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	disease_code varchar(50) NULL,
	referral_id int4 NULL,
	reference_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	created_from varchar(50) NULL,
	CONSTRAINT ncd_member_followup_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_general_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_general_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	mark_review bool NULL,
	symptoms text NULL,
	clinical_observation text NULL,
	diagnosis text NULL,
	"comment" text NULL,
	refferal_place text NULL,
	remarks text NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	is_suspected bool NULL,
	consultant_flag bool NULL,
	other_details text NULL,
	does_required_ref bool NULL,
	refferral_reason text NULL,
	followup_place varchar(50) NULL,
	followup_date timestamp NULL,
	category varchar(50) NULL,
	master_id int4 NULL,
	treatment_status text NULL,
	take_medicine bool NULL,
	CONSTRAINT ncd_member_general_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_health_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_health_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	is_provided_consent bool NULL,
	weight numeric(6, 2) NULL,
	height int4 NULL,
	bmi numeric(6, 2) NULL,
	waist int4 NULL,
	disease_history varchar(200) NULL,
	other_disease text NULL,
	risk_factor varchar(200) NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	CONSTRAINT ncd_member_health_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_home_visit_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_home_visit_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	clinic_date timestamp NULL,
	systolic_bp int4 NULL,
	diastolic_bp int4 NULL,
	pulse_rate int4 NULL,
	hypertension_result varchar(200) NULL,
	mental_health_result varchar(200) NULL,
	diabetes_result varchar(200) NULL,
	talk int4 NULL,
	own_daily_work int4 NULL,
	social_work int4 NULL,
	understanding int4 NULL,
	blood_sugar int4 NULL,
	patient_taking_medicine bool NULL,
	any_adverse_effect bool NULL,
	adverse_effect text NULL,
	required_reference bool NULL,
	given_consent bool NULL,
	referral_place varchar(200) NULL,
	other_referral_place varchar(200) NULL,
	referral_id int4 NULL,
	remarks text NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	reference_reason text NULL,
	refer_status varchar(200) NULL,
	flag bool NULL,
	flag_reason varchar(200) NULL,
	other_reason text NULL,
	followup_visit_type varchar(200) NULL,
	followup_date timestamp NULL,
	death_date date NULL,
	death_place text NULL,
	death_reason text NULL,
	health_infra_id int4 NULL,
	height int4 NULL,
	weight numeric(6, 2) NULL,
	bmi numeric(6, 2) NULL,
	CONSTRAINT ncd_member_home_visit_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_hypertension_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_hypertension_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	systolic_bp int4 NULL,
	diastolic_bp int4 NULL,
	pulse_rate int4 NULL,
	diagnosed_earlier bool NULL,
	currently_under_treatement bool NULL,
	refferal_done bool NULL,
	refferal_place text NULL,
	remarks text NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	is_regular_rythm bool NULL,
	murmur bool NULL,
	bilateral_clear bool NULL,
	bilateral_basal_crepitation bool NULL,
	rhonchi bool NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	bp_machine_available bool NULL,
	health_infra_id int4 NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL,
	current_treatment_place varchar(20) NULL,
	is_continue_treatment_from_current_place bool NULL,
	systolic_bp2 int4 NULL,
	diastolic_bp2 int4 NULL,
	pulse_rate2 int4 NULL,
	is_suspected bool NULL,
	flag bool NULL,
	current_treatment_place_other text NULL,
	does_suffering bool NULL,
	status varchar(50) NULL,
	master_id int4 NULL,
	weight numeric(6, 2) NULL,
	height int4 NULL,
	bmi numeric(6, 2) NULL,
	waist int4 NULL,
	disease_history varchar(200) NULL,
	other_disease text NULL,
	risk_factor varchar(200) NULL,
	undertake_physical_activity bool NULL,
	have_family_history bool NULL,
	note text NULL,
	take_medicine bool NULL,
	treatment_status text NULL,
	visit_type varchar(30) NULL,
	pedal_oedema bool NULL,
	hyper_dia_mental_master_id int4 NULL,
	govt_facility_id int4 NULL,
	private_facility text NULL,
	out_of_territory_facility text NULL,
	CONSTRAINT ncd_member_hypertension_detail_t_pkey PRIMARY KEY (id)
);

-- Drop Indexes If They Exist
DROP INDEX IF EXISTS ncd_member_hypertension_detai_refferal_done_refferal_place_idx1;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_t_created_by_idx;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_t_member_id_idx;
CREATE INDEX ncd_member_hypertension_detai_refferal_done_refferal_place_idx1 ON public.ncd_member_hypertension_detail USING btree (refferal_done, refferal_place);
CREATE INDEX ncd_member_hypertension_detail_modified_on_idx ON public.ncd_member_hypertension_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_hypertension_detail_t_created_by_idx ON public.ncd_member_hypertension_detail USING btree (created_by);
CREATE INDEX ncd_member_hypertension_detail_t_member_id_idx ON public.ncd_member_hypertension_detail USING btree (member_id);

-- Table Triggers
drop trigger if exists ncd_hypertension_hmis_update on public.ncd_member_hypertension_detail;
create trigger ncd_hypertension_hmis_update after
insert
    on
    public.ncd_member_hypertension_detail for each row execute function ncd_hypertension_hmis_updation();


-- public.ncd_member_initial_assessment_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_initial_assessment_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	excess_thirst bool NULL,
	excess_urination bool NULL,
	excess_hunger bool NULL,
	recurrent_skin_gui bool NULL,
	delayed_healing_of_wounds bool NULL,
	change_in_dietary_habits bool NULL,
	sudden_visual_disturbances bool NULL,
	significant_edema bool NULL,
	breathlessness bool NULL,
	angina bool NULL,
	intermittent_claudication bool NULL,
	limpness bool NULL,
	diagnosed_earlier bool NULL,
	currently_under_treatement bool NULL,
	refferal_done bool NULL,
	height int4 NULL,
	waist_circumference int4 NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	current_treatment_place varchar(20) NULL,
	is_continue_treatment_from_current_place bool NULL,
	is_suspected bool NULL,
	consultant_flag bool NULL,
	weight numeric(6, 2) NULL,
	bmi numeric(6, 2) NULL,
	refferal_place varchar NULL,
	form_type varchar(50) NULL,
	master_id int4 NULL,
	history_disease text NULL,
	other_disease text NULL,
	CONSTRAINT ncd_member_initial_assessment_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_investigation_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_investigation_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	screening_date timestamp NULL,
	report text NULL,
	"type" varchar(50) NULL,
	health_infra_id int4 NULL,
	done_by varchar(50) NULL,
	CONSTRAINT ncd_member_investigation_detail_pkey PRIMARY KEY (id),
	CONSTRAINT ncd_member_investigation_unique UNIQUE (member_id, screening_date, report)
);


-- public.ncd_member_medicine_history definition

CREATE TABLE IF NOT EXISTS public.ncd_member_medicine_history (
	id serial4 NOT NULL,
	member_id int4 NULL,
	medicine_id int4 NULL,
	old_frequency int4 NULL,
	new_frequency int4 NULL,
	old_duration int4 NULL,
	new_duration int4 NULL,
	old_quantity int4 NULL,
	new_quantity int4 NULL,
	old_expiry_date timestamp NULL,
	new_expiry_date timestamp NULL,
	created_date timestamp NULL,
	old_is_active bool NULL,
	new_is_active bool NULL,
	updated_by int4 NULL,
	CONSTRAINT ncd_member_medicine_history_pkey PRIMARY KEY (id)
);


-- public.ncd_member_mental_health_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_mental_health_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	suffering_earlier bool NULL,
	diagnosis varchar(200) NULL,
	currently_under_treatement bool NULL,
	current_treatment_place varchar(20) NULL,
	is_continue_treatment_from_current_place bool NULL,
	observation text NULL,
	today_result varchar(20) NULL,
	is_suffering bool NULL,
	flag bool NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	referral_id int4 NULL,
	talk int4 NULL,
	own_daily_work int4 NULL,
	social_work int4 NULL,
	understanding int4 NULL,
	current_treatment_place_other text NULL,
	health_infra_id int4 NULL,
	status varchar(50) NULL,
	master_id int4 NULL,
	other_problems text NULL,
	other_diagnosis text NULL,
	note text NULL,
	take_medicine bool NULL,
	treatment_status text NULL,
	visit_type varchar(30) NULL,
	does_suffering bool NULL,
	hyper_dia_mental_master_id int4 NULL,
	govt_facility_id int4 NULL,
	private_facility text NULL,
	out_of_territory_facility text NULL,
	referral_done bool NULL,
	CONSTRAINT ncd_member_mental_health_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_member_oral_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_oral_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	mobile_start_date timestamp NOT NULL,
	mobile_end_date timestamp NOT NULL,
	screening_date timestamp NULL,
	any_issues_in_mouth bool NULL,
	white_red_patch_oral_cavity bool NULL,
	difficulty_in_spicy_food bool NULL,
	voice_change bool NULL,
	difficulty_in_opening_mouth bool NULL,
	three_weeks_mouth_ulcer bool NULL,
	remarks text NULL,
	growth_of_recent_origins text NULL,
	non_healing_ulcers text NULL,
	red_patches text NULL,
	white_patches text NULL,
	restricted_mouth_opening varchar NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	refferal_done bool NULL,
	refferal_place text NULL,
	other_symptoms varchar(1000) NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	growth_of_recent_origin_flag bool NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL,
	submucous_fibrosis varchar NULL,
	smokers_palate varchar NULL,
	lichen_planus varchar NULL,
	flag bool NULL,
	symptoms_remarks text NULL,
	white_or_red_patch bool NULL,
	ulceration_roughened_areas bool NULL,
	is_red_patch bool NULL,
	status varchar(50) NULL,
	ulcer text NULL,
	does_suffering bool NULL,
	master_id int4 NULL,
	is_suspected bool NULL,
	take_medicine bool NULL,
	treatment_status text NULL,
	cancer_screening_master_id int4 NULL,
	diagnosed_earlier bool NULL,
	currently_under_treatment bool NULL,
	current_treatment_place text NULL,
	govt_facility_id int4 NULL,
	private_facility text NULL,
	out_of_territory_facility text NULL,
	CONSTRAINT ncd_member_oral_detail_t_pkey1 PRIMARY KEY (id)
);
DROP INDEX IF EXISTS ncd_member_oral_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_oral_detail_t_created_by_idx1;
DROP INDEX IF EXISTS ncd_member_oral_detail_t_member_id_idx1;
DROP INDEX IF EXISTS ncd_member_oral_detail_t_refferal_done_refferal_place_idx1;
CREATE INDEX ncd_member_oral_detail_modified_on_idx ON public.ncd_member_oral_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_oral_detail_t_created_by_idx1 ON public.ncd_member_oral_detail USING btree (created_by);
CREATE INDEX ncd_member_oral_detail_t_member_id_idx1 ON public.ncd_member_oral_detail USING btree (member_id);
CREATE INDEX ncd_member_oral_detail_t_refferal_done_refferal_place_idx1 ON public.ncd_member_oral_detail USING btree (refferal_done, refferal_place);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_oral_hmis_update ON public.ncd_member_oral_detail;
create trigger ncd_oral_hmis_update after
insert
    on
    public.ncd_member_oral_detail for each row execute function ncd_oral_hmis_updation();


-- public.ncd_member_other_information definition

CREATE TABLE IF NOT EXISTS public.ncd_member_other_information (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	excess_thirst bool NULL,
	excess_urination bool NULL,
	excess_hunger bool NULL,
	recurrent_skin bool NULL,
	delay_in_healing bool NULL,
	change_in_dietery_habits bool NULL,
	visual_disturbances_history_or_present bool NULL,
	significant_edema bool NULL,
	angina bool NULL,
	intermittent_claudication bool NULL,
	limpness bool NULL,
	mo_screening_done bool NULL,
	history_of_stroke bool NULL,
	history_of_heart_attack bool NULL,
	family_history_of_diabetes bool NULL,
	family_history_of_stroke bool NULL,
	family_history_of_premature_mi bool NULL,
	is_report_verified bool NULL,
	confirmed_case_of_copd bool NULL,
	confirmed_case_of_diabetes bool NULL,
	confirmed_case_of_hypertension bool NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	smoke_tobacco bool NULL,
	smokeless_tobacco bool NULL,
	alcohol_consumption varchar(200) NULL,
	referral_id int4 NULL,
	CONSTRAINT ncd_member_other_information_pkey PRIMARY KEY (id)
);

-- public.ncd_member_provisional_diagnosis definition

CREATE TABLE IF NOT EXISTS public.ncd_member_provisional_diagnosis (
	id serial4 NOT NULL,
	member_id int4 NULL,
	diagnosis varchar(1000) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modifiedby int4 NULL,
	modified_on timestamp NULL,
	referral_id int4 NULL,
	CONSTRAINT ncd_member_provisional_diagnosis_pkey PRIMARY KEY (id)
);

-- public.ncd_member_referral definition

CREATE TABLE IF NOT EXISTS public.ncd_member_referral (
	id serial4 NOT NULL,
	referred_by int4 NULL,
	referred_to varchar NULL,
	referred_on timestamp NULL,
	disease_code varchar(100) NULL,
	location_id int4 NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	referred_from varchar NOT NULL,
	reason varchar NULL,
	state text NULL,
	health_infrastructure_id int4 NULL,
	referred_from_health_infrastructure_id_delete int4 NULL,
	status varchar(50) NULL,
	follow_up_date timestamp NULL,
	member_location int4 NOT NULL,
	referred_from_health_infrastructure_id int4 NULL,
	pvt_health_infra_name text NULL,
	CONSTRAINT ncd_member_referral_t_pkey PRIMARY KEY (id)
);


-- public.ncd_member_status_history definition

CREATE TABLE IF NOT EXISTS public.ncd_member_status_history (
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	from_status varchar(50) NULL,
	to_status varchar(50) NULL,
	from_sub_status varchar(50) NULL,
	to_sub_status varchar(50) NULL,
	disease_code varchar(20) NULL
);

-- public.ncd_mo_review_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_mo_review_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	screening_date timestamp NULL,
	location_id int4 NULL,
	health_infra_id int4 NULL,
	does_required_ref bool NULL,
	refferral_reason varchar(100) NULL,
	followup_date timestamp NULL,
	refferral_place varchar(50) NULL,
	followup_place varchar(50) NULL,
	"comment" text NULL,
	is_followup bool NULL,
	diseases varchar(50) NULL,
	other_reason varchar(200) NULL,
	CONSTRAINT ncd_mo_review_detail_pkey PRIMARY KEY (id)
);

-- public.ncd_mo_review_followup_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_mo_review_followup_detail (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	screening_date timestamp NULL,
	location_id int4 NULL,
	health_infra_id int4 NULL,
	does_required_ref bool NULL,
	refferral_reason varchar(100) NULL,
	followup_date timestamp NULL,
	refferral_place varchar(50) NULL,
	followup_place varchar(50) NULL,
	"comment" text NULL,
	is_remove bool NULL,
	diseases varchar(50) NULL,
	other_reason varchar(200) NULL,
	CONSTRAINT ncd_mo_review_followup_detail_pkey PRIMARY KEY (id)
);

-- public.ncd_mo_review_members definition

CREATE TABLE IF NOT EXISTS public.ncd_mo_review_members (
	id serial4 NOT NULL,
	member_id int4 NULL,
	disease_code varchar(50) NULL,
	"type" varchar(100) NULL,
	on_date date NULL,
	is_followup bool NULL,
	state varchar(50) NULL,
	CONSTRAINT ncd_mo_review_members_pkey PRIMARY KEY (id)
);

-- public.ncd_opthamologist_data definition

CREATE TABLE IF NOT EXISTS public.ncd_opthamologist_data (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_on timestamp NULL DEFAULT now(),
	created_by int4 NULL,
	modified_on timestamp NULL DEFAULT now(),
	modified_by int4 NULL,
	screening_date timestamp NULL,
	lefteye_feedback text NULL,
	lefteye_other_type text NULL,
	righteye_feedback text NULL,
	righteye_other_type text NULL,
	CONSTRAINT ncd_opthamologist_data_pkey PRIMARY KEY (id)
);

-- public.ncd_personal_history definition

CREATE TABLE IF NOT EXISTS public.ncd_personal_history (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NOT NULL,
	age_at_menarche int4 NULL,
	menopause_arrived bool NULL,
	duration_of_menopause int4 NULL,
	pregnant bool NULL,
	lactating bool NULL,
	regular_periods bool NULL,
	lmp timestamp NULL,
	bleeding text NULL,
	associated_with text NULL,
	remarks text NULL,
	diagnosed_for_hypertension bool NULL,
	under_treatement_for_hypertension bool NULL,
	diagnosed_for_diabetes bool NULL,
	under_treatement_for_diabetes bool NULL,
	diagnosed_for_heart_diseases bool NULL,
	under_treatement_for_heart_diseases bool NULL,
	diagnosed_for_stroke bool NULL,
	under_treatement_for_stroke bool NULL,
	diagnosed_for_kidney_failure bool NULL,
	under_treatement_for_kidney_failure bool NULL,
	diagnosed_for_non_healing_wound bool NULL,
	under_treatement_for_non_healing_wound bool NULL,
	diagnosed_for_copd bool NULL,
	under_treatement_for_copd bool NULL,
	diagnosed_for_asthama bool NULL,
	under_treatement_for_asthama bool NULL,
	diagnosed_for_oral_cancer bool NULL,
	under_treatement_for_oral_cancer bool NULL,
	diagnosed_for_breast_cancer bool NULL,
	under_treatement_for_breast_cancer bool NULL,
	diagnosed_for_cervical_cancer bool NULL,
	under_treatement_for_cervical_cancer bool NULL,
	any_other_examination bool NULL,
	specify_other_examination text NULL,
	height int4 NULL,
	weight numeric(6, 2) NULL,
	bmi numeric(8, 2) NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	CONSTRAINT ncd_personal_history_pkey PRIMARY KEY (id)
);


-- public.ncd_renal_member_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_renal_member_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	created_by int4 NULL,
	created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	modified_by int4 NULL,
	modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	screening_date timestamp NULL,
	is_s_creatinine_done bool NULL,
	s_creatinine_value numeric(6, 2) NULL,
	is_renal_complication_present bool NULL,
	CONSTRAINT ncd_renal_member_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_retinopathy_test_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_retinopathy_test_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NOT NULL,
	on_diabetes_treatment bool NULL,
	diabetes_treatment text NULL,
	past_eye_operation bool NULL,
	eye_operation_type text NULL,
	vision_problem_flag bool NULL,
	vision_problem text NULL,
	vision_problem_duration int4 NULL,
	absent_eyeball text NULL,
	retinopathy_test_flag bool NULL,
	remidio_id text NULL,
	right_eye_retinopathy_detected bool NULL,
	left_eye_retinopathy_detected bool NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	CONSTRAINT ncd_retinopathy_test_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_service_provided_during_year_t definition

CREATE TABLE IF NOT EXISTS public.ncd_service_provided_during_year_t (
	health_infra_id int8 NULL,
	month_year date NULL,
	no_of_hypertension_cases_screened int4 NULL,
	no_of_hypertension_cases int4 NULL,
	no_of_hypertension_cases_in_treatment int4 NULL,
	no_of_diabetes_cases_screened int4 NULL,
	no_of_diabetes_cases int4 NULL,
	no_of_diabetes_cases_in_treatment int4 NULL,
	no_of_hypertension_and_diabetes_cases_screened int4 NULL,
	no_of_hypertension_and_diabetes_cases int4 NULL,
	no_of_hypertension_and_diabetes_cases_in_treatment int4 NULL,
	no_of_oral_cancer_cases_screened int4 NULL,
	no_of_oral_cancer_cases int4 NULL,
	no_of_oral_cancer_cases_in_followup int4 NULL,
	no_of_breast_cancer_cases_screened int4 NULL,
	no_of_breast_cancer_cases int4 NULL,
	no_of_breast_cancer_cases_in_treatment int4 NULL,
	no_of_cervical_cases_screened int4 NULL,
	no_of_cervical_cases int4 NULL,
	no_of_cervical_cases_in_treatment int4 NULL,
	no_of_hypertension_and_diabetes_outpatients int4 NULL,
	no_of_nsv_or_cvc int4 NULL,
	no_of_laparoscopic_sterilizations_conducted int4 NULL,
	no_of_mini_lap_sterilization_conducted int4 NULL,
	no_of_outpatient_diabetes int4 NULL,
	no_of_outpatient_hypertension int4 NULL,
	no_of_allopathic_outpatient_attendance int4 NULL,
	no_of_operation_major int4 NULL,
	no_of_operation_minor int4 NULL
);


-- public.ncd_specialist_master definition

CREATE TABLE IF NOT EXISTS public.ncd_specialist_master (
	id bigserial NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	modified_by int4 NULL,
	modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_ecg_specialist_id int4 NULL,
	last_stroke_specialist_id int4 NULL,
	last_amputation_specialist_id int4 NULL,
	last_renal_specialist_id int4 NULL,
	last_cardiologist_id int4 NULL,
	last_opthamologist_id int4 NULL,
	last_generic_screening_id int4 NULL,
	last_urine_screening_id int4 NULL,
	last_ecg_screening_id int4 NULL,
	last_retinopathy_test_id int4 NULL,
	CONSTRAINT ncd_specialist_master_pkey PRIMARY KEY (id),
	CONSTRAINT ncd_specialist_master_unique_member UNIQUE (member_id)
);

CREATE TABLE IF NOT EXISTS public.imt_member_ncd_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	location_id int4 NULL,
	last_service_date timestamp NULL,
	last_mo_visit timestamp NULL,
	last_mobile_visit timestamp NULL,
	last_mo_comment text NULL,
	mo_confirmed_diabetes bool NULL,
	mo_confirmed_hypertension bool NULL,
	mo_confirmed_mental_health bool NULL,
	suffering_diabetes bool NULL,
	suffering_hypertension bool NULL,
	suffering_mental_health bool NULL,
	diabetes_details text NULL,
	hypertension_details text NULL,
	mental_health_details text NULL,
	diabetes_treatment_status text NULL,
	hypertension_treatment_status text NULL,
	mentalhealth_treatment_status text NULL,
	medicine_details text NULL,
	disease_history text NULL,
	notification_details text NULL,
	diabetes_status text NULL,
	hypertension_status text NULL,
	mental_health_status text NULL,
	diabetes_state text NULL,
	hypertension_state text NULL,
	mental_health_state text NULL,
	last_remark text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	cvc_treatement_status text NULL,
	last_mo_comment_by int4 NULL,
	last_mo_comment_form_type text NULL,
	last_remark_by int4 NULL,
	last_remark_form_type text NULL,
	other_disease_history text NULL,
	evening_availability bool NULL,
	reference_due bool NULL,
	CONSTRAINT imt_member_ncd_detail_pkey PRIMARY KEY (id),
	CONSTRAINT imt_member_ncd_detail_unique_members UNIQUE (member_id)
);


-- public.ncd_stroke_member_detail definition
CREATE TABLE IF NOT EXISTS public.ncd_stroke_member_detail (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	created_by int4 NULL,
	created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	modified_by int4 NULL,
	modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	screening_date timestamp NULL,
	stroke_present bool NULL,
	CONSTRAINT ncd_stroke_member_detail_pkey PRIMARY KEY (id)
);


-- public.ncd_urine_test definition

CREATE TABLE IF NOT EXISTS public.ncd_urine_test (
	id serial4 NOT NULL,
	member_id int4 NOT NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NOT NULL,
	urine_test_flag bool NULL,
	albumin text NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	latitude varchar(100) NULL,
	longitude varchar(100) NULL,
	CONSTRAINT ncd_urine_test_pkey PRIMARY KEY (id)
);


-- public.ncd_visit_history definition

CREATE TABLE IF NOT EXISTS public.ncd_visit_history (
	id serial4 NOT NULL,
	member_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	visit_date timestamp NULL,
	reference_id int4 NULL,
	disease_code varchar(50) NULL,
	status varchar(50) NULL,
	visit_by varchar(50) NULL,
	flag bool NULL,
	master_id int4 NULL,
	reading varchar(200) NULL,
	CONSTRAINT ncd_visit_history_pkey PRIMARY KEY (id)
);

--deleting user_menu_relation_to_mitigate_foreign_key_contraint
delete from user_menu_item where menu_config_id in (361,362,363,364,365,366,367,368,369,370,371,372,373,374,375);

--Reports related to ncd
delete from menu_config where menu_name in ('NCD Form Fillup (Location wise)','HMIS Report','CBAC Form Fillup Count User Wise',
'NCD Screening Status','NCD Progress tracking report','Drug Inventory','Consultant Followup Screen members','MBBS MO Review Screen','MO specialist patient list',
'Cardiologist patient list','Ophthalmologist patient list','MO Review Screen','MO Review followup Screen','NCD Screening Dashboard','Referred Patients');

delete from report_master where report_name in ('HMIS Report','CBAC Form Fillup Count User Wise','NCD Progress tracking report','NCD Screening Status','NCD Form Fillup (Location wise)');

INSERT INTO public.report_master (id,report_name,file_name,active,report_type,menu_id,modified_on,created_by,created_on,modified_by,config_json,code,"uuid") VALUES
	 (686,'HMIS Report',NULL,true,'DYNAMIC',(select id from menu_config where menu_name = 'HMIS Report'),'2020-10-23 14:10:07.14',74841,'2020-10-23 13:09:37.124',74841,'{"layout":"dynamicReport1","templateType":"DYNAMIC_REPORTS","containers":{"fieldsContainer":[{"fieldName":"location_id","displayName":"Location","fieldType":"location","demographicFilterRequired":true,"requiredUptoLevel":1,"isMandatory":true,"undefined":0,"index":2},{"fieldName":"hospital_type","displayName":"Hospital Type","fieldType":"comboForReport","isMandatory":true,"requiredUptoLevel":1,"isQuery":true,"availableOptions":[],"query":"select value as hospital_type from listvalue_field_value_detail where field_key = ''infra_type'' and code in (''P'',''SC'',''U'',''G'',''D'',''B'',''C'');","undefined":1,"index":0,"queryId":1789,"queryParams":null,"queryUUID":"041e093a-af28-4faa-b02a-ca609a0f1286"},{"queryForParam":"","isMandatory":true,"requiredUptoLevel":1,"fieldName":"health_infra_id","displayName":"Health Infra","fieldType":"comboForReport","isQuery":true,"availableOptions":[],"query":"select id as health_infra_id, name as \"Health Infra\" from health_infrastructure_details where location_id = #location_id# and \"type\" = (select id from listvalue_field_value_detail where field_key = ''infra_type'' and value = ''#hospital_type#'')","undefined":2,"queryIdForParam":"","queryId":1790,"queryParams":"hospital_type,location_id","queryUUID":"c62eac9d-f0a8-4b74-a188-1998f32eb802","index":1},{"fieldName":"month_year","displayName":"Month","fieldType":"onlyMonth","isMandatory":true,"requiredUptoLevel":1,"undefined":null,"index":3}],"tableContainer":[{"fieldName":"tableField","query":"with property as (\nselect\n\tcase\n\t\twhen lfvd.value = ''Community Health Center'' then ''CHC''\n\t\telse lfvd.value\n\tend as infra_type ,\n\thid.id as health_infra_id ,\n\tcase\n\t\twhen ''#month_year#'' != ''null'' then to_date(''#month_year#'', ''MM-DD-YYYY'')\n\t\telse null\n\tend as month_year\nfrom\n\thealth_infrastructure_details hid\nleft join listvalue_field_value_detail lfvd on\n\tlfvd.id = hid.\"type\"\nwhere\n\thid.id = cast(#health_infra_id# as integer) ),\nhmis_data as(\nselect\n\tcoalesce(sum(anc_reg), 0) as anc_reg,\n\tcoalesce(sum(anc1), 0) as anc1,\n\tcoalesce(sum(high_risk), 0) as high_risk,\n\tcoalesce(sum(high_risk_follow_up), 0) as high_risk_follow_up,\n\tcoalesce(sum(tt1), 0) as tt1,\n\tcoalesce(sum(tt2), 0) as tt2,\n\tcoalesce(sum(tt_booster), 0) as tt_booster,\n\tcoalesce(sum(no_of_pw_180_ifa_given), 0) as no_of_pw_180_ifa_given,\n\tcoalesce(sum(no_of_pw_ca_given), 0) as no_of_pw_ca_given,\n\tcoalesce(sum(no_of_pw_alben_given), 0) as no_of_pw_alben_given,\n\tcoalesce(sum(no_of_pw_with_more_then_4_anc_checkup), 0) as no_of_pw_with_more_then_4_anc_checkup,\n\tcoalesce(sum(no_of_pw_anc_corticosteroids_given), 0) as no_of_pw_anc_corticosteroids_given,\n\tcoalesce(sum(no_of_pw_with_hypertension), 0) as no_of_pw_with_hypertension,\n\tcoalesce(sum(no_of_pw_with_hypertension_migrated_at_institution), 0) as no_of_pw_with_hypertension_migrated_at_institution,\n\tcoalesce(sum(no_of_pw_tested_for_hb_4_or_more_time), 0) as hb_test_more_than_4_time,\n\tcoalesce(sum(hbsag_test_cnt), 0) as hbsag_test_cnt,\n\tcoalesce(sum(0), 0) as temp2,\n\tcoalesce(sum(no_of_pw_with_hb_count_less_than_11), 0) as no_of_pw__with_hb_count_less_then_11,\n\tcoalesce(sum(no_of_pw_with_hb_count_less_than_7), 0) as no_of_pw__with_hb_count_less_then_7,\n\tcoalesce(sum(no_of_pw_tested_for_anemia_with_hb_count_less_than_7), 0) as no_of_pw_tested_for_anemia_with_hb_count_less_then_7,\n\tcoalesce(sum(no_of_pw_tested_for_gdm), 0) as no_of_pw_tested_for_gdm,\n\tcoalesce(sum(0), 0) as temp3,\n\tcoalesce(sum(no_of_pw_tested_for_syphilis), 0) as no_of_pw_tested_for_syphilis,\n\tcoalesce(sum(no_of_pw_tested_positive_for_syphilis), 0) as no_of_pw_tested_positive_for_syphilis,\n\tcoalesce(sum(no_of_pw_treated_for_syphilis), 0) as no_of_pw_treated_for_syphilis,\n\tcoalesce(sum(0), 0) as temp4,\n\tcoalesce(sum(0), 0) as temp5,\n\tcoalesce(sum(no_of_home_del), 0) as no_of_home_del,\n\tcoalesce(sum(home_del_by_sba), 0) as home_del_by_sba,\n\tcoalesce(sum(home_del_by_non_sba), 0) as home_del_by_non_sba,\n\tcoalesce(sum(no_of_pw_misoprostol_given_home_del), 0) as no_of_pw_misoprostol_given_home_del,\n\tcoalesce(sum(no_of_inst_del), 0) as no_of_inst_dl,\n\tcoalesce(sum(no_of_delivery_in_transit), 0) as no_of_delivery_in_transit,\n\tcoalesce(sum(no_of_inst_del_stayed_48_hrs), 0) as no_of_inst_del_stayed_48_hrs,\n\tcoalesce(sum(no_of_c_section_delivery), 0) as no_of_c_section_delivery,\n\tcoalesce(sum(no_of_c_section_delivery_at_night), 0) as no_of_c_section_delivery_at_night,\n\tcoalesce(sum(live_birth_male), 0) as live_birth_male,\n\tcoalesce(sum(live_birth_female), 0) as live_birth_female,\n\tcoalesce(sum(no_of_pre_term_newborns), 0) as no_of_pre_term_newborns,\n\tcoalesce(sum(abortion), 0) as abortion,\n\tcoalesce(sum(abortion_medical_method), 0) as abortion_medical_method,\n\tcoalesce(sum(abortion_surgical_method), 0) as abortion_surgical_method,\n\tcoalesce(sum(mtp), 0) as mtp,\n\tcoalesce(sum(hmis_rch_service_provided_during_year.no_of_complications_refered_to_hf), 0) as no_of_complications_refered_to_hf,\n\tcoalesce(sum(mtp_medical_method), 0) as mtp_medical_method,\n\tcoalesce(sum(mtp_surgical_method), 0) as mtp_surgical_method,\n\tcoalesce(sum(mtp_more_than_12_weeks), 0) as mtp_more_than_12_weeks,\n\tcoalesce(sum(mtp_complication_identified_post_abortion), 0) as mtp_complication_identified_post_abortion,\n\tcoalesce(sum(mtp_complication_treated_post_abortion), 0) as mtp_complication_treated_post_abortion,\n\tcoalesce(sum(no_of_pw_provided_contraception_post_abortion), 0) as no_of_pw_provided_contraception_post_abortion,\n\tcoalesce(sum(weighed), 0) as weighed,\n\tcoalesce(sum(weighed_less_than_2_5), 0) as weighed_less_than_2_5,\n\tcoalesce(sum(breast_feeding_within_1_hr), 0) as breast_feeding_within_1_hr,\n\tcoalesce(sum(no_complications_observed), 0) as no_complications_observed,\n\tcoalesce(sum(no_complications_managed_at_facility), 0) as no_complications_managed_at_facility,\n\tcoalesce(sum(no_of_pw_with_obstetric_complications), 0) as no_of_pw_with_obstetric_complications,\n\tcoalesce(sum(no_of_pw_treated_with_blood_transfusion), 0) as no_of_pw_treated_with_blood_transfusion,\n\tcoalesce(sum(no_of_pw_recived_post_partum_in_48_hrs_hd), 0) as no_of_pw_recived_post_partum_in_48_hrs_hd,\n\tcoalesce(sum(no_of_pw_180_ifa_full_course_given), 0) as no_of_pw_180_ifa_full_course_given,\n\tcoalesce(sum(0), 0) as temp6,\n\tcoalesce(sum(no_of_nsv_or_cvc), 0) as no_of_nsv_or_cvc,\n\tcoalesce(sum(no_of_laparoscopic_sterilizations_conducted), 0) as no_of_laparoscopic_sterilizations_conducted,\n\tcoalesce(sum(no_of_mini_lap_sterilization_conducted), 0) as no_of_mini_lap_sterilization_conducted,\n\tcoalesce(sum(no_of_postpartum_sterilizations), 0) as no_of_postpartum_sterilizations,\n\tcoalesce(sum(no_of_post_abortion_sterilizations), 0) as no_of_post_abortion_sterilizations,\n\tcoalesce(sum(no_of_interval_iucd_insertions), 0) as no_of_interval_iucd_insertions,\n\tcoalesce(sum(no_of_postpartum_iucd_insertion), 0) as no_of_postpartum_iucd_insertion,\n\tcoalesce(sum(no_of_post_abortion_iucd_insertion), 0) as no_of_post_abortion_iucd_insertion,\n\tcoalesce(sum(no_of_iucd_removal), 0) as no_of_iucd_removal,\n\tcoalesce(sum(injectable_cap_first_dose), 0) as injectable_cap_first_dose,\n\tcoalesce(sum(injectable_cap_second_dose), 0) as injectable_cap_second_dose,\n\tcoalesce(sum(injectable_cap_third_dose), 0) as injectable_cap_third_dose,\n\tcoalesce(sum(injectable_cap_fourth_dose), 0) as injectable_cap_fourth_dose,\n\tcoalesce(sum(no_of_oral_pills_distributed_by_facility), 0) as no_of_oral_pills_distributed_by_facility,\n\tcoalesce(sum(no_of_oral_pills_distributed_by_asha), 0) as no_of_oral_pills_distributed_by_asha,\n\tcoalesce(sum(no_of_condom_distributed_by_facility), 0) as no_of_condom_distributed_by_facility,\n\tcoalesce(sum(no_of_condom_distributed_by_asha), 0) as no_of_condom_distributed_by_asha,\n\tcoalesce(sum(no_of_centchroman_pills_distributed_by_facility), 0) as no_of_centchroman_pills_distributed_by_facility,\n\tcoalesce(sum(no_of_centchroman_pills_distributed_by_asha), 0) as no_of_centchroman_pills_distributed_by_asha,\n\tcoalesce(sum(no_of_ptk_utilized_by_facility), 0) as no_of_ptk_utilized_by_facility,\n\tcoalesce(sum(no_of_ptk_utilized_by_ash), 0) as no_of_ptk_utilized_by_ash,\n\tcoalesce(sum(vitamin_k_given), 0) as vitamin_k_given,\n\tcoalesce(sum(bcg_given), 0) as bcg_given,\n\tcoalesce(sum(penta_1_given), 0) as penta_1_given,\n\tcoalesce(sum(penta_2_given), 0) as penta_2_given,\n\tcoalesce(sum(penta_3_given), 0) as penta_3_given,\n\tcoalesce(sum(opv_0_given), 0) as opv_0_given,\n\tcoalesce(sum(opv_1_given), 0) as opv_1_given,\n\tcoalesce(sum(opv_2_given), 0) as opv_2_given,\n\tcoalesce(sum(opv_3_given), 0) as opv_3_given,\n\tcoalesce(sum(hep_b_given), 0) as hep_b_given,\n\tcoalesce(sum(fipv_1_given), 0) as fipv_1_given,\n\tcoalesce(sum(fipv_2_given), 0) as fipv_2_given,\n\tcoalesce(sum(rota_virus_1_given), 0) as rota_virus_1_given,\n\tcoalesce(sum(rota_virus_2_given), 0) as rota_virus_2_given,\n\tcoalesce(sum(rota_virus_3_given), 0) as rota_virus_3_given,\n\tcoalesce(sum(measles_rubella_1_given_in_9_to_11_month), 0) as measles_rubella_1_given_in_9_to_11_month,\n\tcoalesce(sum(measles_rubella_2_given_in_9_to_11_month), 0) as measles_rubella_2_given_in_9_to_11_month,\n\tcoalesce(sum(fully_immunized_bwtween_9_to_11_months), 0) as fully_immunized_bwtween_9_to_11_months,\n\tcoalesce(sum(fully_immunized_bwtween_9_to_11_months_male), 0) as fully_immunized_bwtween_9_to_11_months_male,\n\tcoalesce(sum(fully_immunized_bwtween_9_to_11_months_female), 0) as fully_immunized_bwtween_9_to_11_months_female,\n\tcoalesce(sum(measles_rubella_1_given), 0) as measles_rubella_1_given,\n\tcoalesce(sum(measles_1_given), 0) as measles_1_given,\n\tcoalesce(sum(measles_rubella_2_given), 0) as measles_rubella_2_given,\n\tcoalesce(sum(measles_or_rubella_2_given), 0) as measles_or_rubella_2_given,\n\tcoalesce(sum(dpt_booster_given), 0) as dpt_booster_given,\n\tcoalesce(sum(opv_booster_given), 0) as opv_booster_given,\n\tcoalesce(sum(no_of_cases_of_aefi_minor), 0) as no_of_cases_of_aefi_minor,\n\tcoalesce(sum(no_of_cases_of_aefi_severe), 0) as no_of_cases_of_aefi_severe,\n\tcoalesce(sum(no_of_cases_of_aefi_serious), 0) as no_of_cases_of_aefi_serious,\n\tcoalesce(sum(vitamin_a_given), 0) as vitamin_a_given,\n\tcoalesce(sum(ifa_given), 0) as ifa_given,\n\tcoalesce(sum(unweighted_child_given_health_checkup), 0) as unweighted_child_given_health_checkup,\n\tcoalesce(sum(childhood_diseases_pneumonia), 0) as childhood_diseases_pneumonia,\n\tcoalesce(sum(childhood_diseases_asthma), 0) as childhood_diseases_asthma,\n\tcoalesce(sum(childhood_diseases_sepsis), 0) as childhood_diseases_sepsis,\n\tcoalesce(sum(childhood_diseases_diphtheria), 0) as childhood_diseases_diphtheria,\n\tcoalesce(sum(childhood_diseases_pertussis), 0) as childhood_diseases_pertussis,\n\tcoalesce(sum(childhood_diseases_tetanus_neonatorum), 0) as childhood_diseases_tetanus_neonatorum,\n\tcoalesce(sum(childhood_diseases_sam), 0) as childhood_diseases_sam,\n\tcoalesce(sum(0), 0) as temp7,\n\tcoalesce(sum(0), 0) as temp8,\n\tcoalesce(sum(no_of_hypertension_cases_screened), 0) as no_of_hypertension_cases_screened,\n\tcoalesce(sum(no_of_hypertension_cases), 0) as no_of_hypertension_cases,\n\tcoalesce(sum(no_of_hypertension_cases_in_treatment), 0) as no_of_hypertension_cases_in_treatment,\n\tcoalesce(sum(no_of_diabetes_cases_screened), 0) as no_of_diabetes_cases_screened,\n\tcoalesce(sum(no_of_diabetes_cases), 0) as no_of_diabetes_cases,\n\tcoalesce(sum(no_of_diabetes_cases_in_treatment), 0) as no_of_diabetes_cases_in_treatment,\n\tcoalesce(sum(no_of_hypertension_and_diabetes_cases_screened), 0) as no_of_hypertension_and_diabetes_cases_screened,\n\tcoalesce(sum(no_of_hypertension_and_diabetes_cases), 0) as no_of_hypertension_and_diabetes_cases,\n\tcoalesce(sum(no_of_hypertension_and_diabetes_cases_in_treatment), 0) as no_of_hypertension_and_diabetes_cases_in_treatment,\n\tcoalesce(sum(no_of_oral_cancer_cases_screened), 0) as no_of_oral_cancer_cases_screened,\n\tcoalesce(sum(no_of_oral_cancer_cases), 0) as no_of_oral_cancer_cases,\n\tcoalesce(sum(no_of_oral_cancer_cases_in_followup), 0) as no_of_oral_cancer_cases_in_followup,\n\tcoalesce(sum(no_of_breast_cancer_cases_screened), 0) as no_of_breast_cancer_cases_screened,\n\tcoalesce(sum(no_of_breast_cancer_cases), 0) as no_of_breast_cancer_cases,\n\tcoalesce(sum(no_of_breast_cancer_cases_in_treatment), 0) as no_of_breast_cancer_cases_in_treatment,\n\tcoalesce(sum(no_of_cervical_cases_screened), 0) as no_of_cervical_cases_screened,\n\tcoalesce(sum(no_of_cervical_cases), 0) as no_of_cervical_cases,\n\tcoalesce(sum(no_of_cervical_cases_in_treatment), 0) as no_of_cervical_cases_in_treatment,\n\tcoalesce(sum(no_of_outpatient_diabetes), 0) as no_of_outpatient_diabetes,\n\tcoalesce(sum(no_of_outpatient_hypertension), 0) as no_of_outpatient_hypertension,\n\tcoalesce(sum(no_of_hypertension_and_diabetes_outpatients), 0) as no_of_hypertension_and_diabetes_outpatients,\n\tcoalesce(sum(no_of_allopathic_outpatient_attendance), 0) as no_of_allopathic_outpatient_attendance,\n\tcoalesce(sum(no_of_operation_major), 0) as no_of_operation_major,\n\tcoalesce(sum(no_of_operation_minor), 0) as no_of_operation_minor,\n\t--coalesce(sum(no_of_child_admitted_at_nrc), 0) as no_of_child_admitted_at_nrc,\n    coalesce(sum(0), 0) as no_of_child_admitted_at_nrc,\n\t--coalesce(sum(no_of_child_discharge_after_weight_gain_nrc), 0) as no_of_child_discharge_after_weight_gain_nrc,\n    coalesce(sum(0), 0) as no_of_child_discharge_after_weight_gain_nrc,\n\t--coalesce(sum(no_of_lab_test_done), 0) as no_of_lab_test_done,\n    coalesce(sum(0), 0) as no_of_lab_test_done,\n\tcoalesce(sum(no_of_pw_tested_for_hiv), 0) as no_of_pw_tested_for_hiv,\n\tcoalesce(sum(no_of_deaths_in_1_to_24_hrs_at_facility), 0) as no_of_deaths_in_1_to_24_hrs_at_facility,\n\tcoalesce(sum(no_of_deaths_in_1_to_24_hrs_at_community), 0) as no_of_deaths_in_1_to_24_hrs_at_community,\n\tcoalesce(sum(no_of_deaths_in_transit), 0) as no_of_deaths_in_transit,\n\tcoalesce(sum(no_of_deaths_in_1_week_at_facility), 0) as no_of_deaths_in_1_week_at_facility,\n\tcoalesce(sum(no_of_deaths_in_1_week_at_community), 0) as no_of_deaths_in_1_week_at_community,\n\tcoalesce(sum(no_of_deaths_in_8_to_28_days_at_facility), 0) as no_of_deaths_in_8_to_28_days_at_facility,\n\tcoalesce(sum(no_of_deaths_in_8_to_28_days_at_community), 0) as no_of_deaths_in_8_to_28_days_at_community,\n\tcoalesce(sum(id_upto_4_weeks_dueto_sepsis), 0) as id_upto_4_weeks_dueto_sepsis,\n\tcoalesce(sum(id_upto_4_weeks_dueto_asphyxia), 0) as id_upto_4_weeks_dueto_asphyxia,\n\tcoalesce(sum(id_upto_4_weeks_dueto_other), 0) as id_upto_4_weeks_dueto_other,\n\tcoalesce(sum(id_upto_12_moths_dueto_pneumonia), 0) as id_upto_12_moths_dueto_pneumonia,\n\tcoalesce(sum(id_upto_12_moths_dueto_diarrhoea), 0) as id_upto_12_moths_dueto_diarrhoea,\n\tcoalesce(sum(id_upto_12_moths_dueto_feaver), 0) as id_upto_12_moths_dueto_feaver,\n\tcoalesce(sum(id_upto_12_moths_dueto_measles), 0) as id_upto_12_moths_dueto_measles,\n\tcoalesce(sum(id_upto_12_moths_dueto_other), 0) as id_upto_12_moths_dueto_other,\n\tcoalesce(sum(no_of_infant_deaths_1_to_12_months_at_facility), 0) as no_of_infant_deaths_1_to_12_months_at_facility,\n\tcoalesce(sum(no_of_infant_deaths_1_to_12_months_at_community), 0) as no_of_infant_deaths_1_to_12_months_at_community,\n\tcoalesce(sum(no_of_infant_deaths_1_to_12_months_in_transit), 0) as no_of_infant_deaths_1_to_12_months_in_transit,\n\tcoalesce(sum(cd_upto_5_years_dueto_pneumonia), 0) as cd_upto_5_years_dueto_pneumonia,\n\tcoalesce(sum(cd_upto_5_years_dueto_diarrhoea), 0) as cd_upto_5_years_dueto_diarrhoea,\n\tcoalesce(sum(cd_upto_5_years_dueto_fever), 0) as cd_upto_5_years_dueto_fever,\n\tcoalesce(sum(cd_upto_5_years_dueto_measles), 0) as cd_upto_5_years_dueto_measles,\n\tcoalesce(sum(cd_upto_5_years_dueto_others), 0) as cd_upto_5_years_dueto_others,\n\tcoalesce(sum(no_of_child_death_1_to_5_years_due_to_cause), 0) as no_of_child_death_1_to_5_years_due_to_cause,\n\tcoalesce(sum(no_of_child_death_reported_through_cbcdr), 0) as no_of_child_death_reported_through_cbcdr,\n\tcoalesce(sum(total_fbcdr_done), 0) as total_fbcdr_done,\n\tcoalesce(sum(no_of_maternal_death_due_to_aph), 0) as no_of_maternal_death_due_to_aph,\n\tcoalesce(sum(no_of_maternal_death_due_to_pr_pph_issue), 0) as no_of_maternal_death_due_to_pr_pph_issue,\n\tcoalesce(sum(no_of_maternal_death_due_to_pr_anaemia_issue), 0) as no_of_maternal_death_due_to_pr_anaemia_issue,\n\tcoalesce(sum(no_of_maternal_death_due_to_pr_sepsis), 0) as no_of_maternal_death_due_to_pr_sepsis,\n\tcoalesce(sum(no_of_maternal_death_due_to_abortive_outcome), 0) as no_of_maternal_death_due_to_abortive_outcome,\n\tcoalesce(sum(no_of_maternal_death_due_to_obstructed_labour), 0) as no_of_maternal_death_due_to_obstructed_labour,\n\tcoalesce(sum(no_of_maternal_death_due_to_hypertensive_disorder), 0) as no_of_maternal_death_due_to_hypertensive_disorder,\n\tcoalesce(sum(no_of_maternal_death_due_to_cd), 0) as no_of_maternal_death_due_to_cd,\n\tcoalesce(sum(no_of_maternal_death_due_to_ld), 0) as no_of_maternal_death_due_to_ld,\n\tcoalesce(sum(no_of_maternal_death_due_to_rd), 0) as no_of_maternal_death_due_to_rd,\n\tcoalesce(sum(no_of_maternal_death_due_to_infection), 0) as no_of_maternal_death_due_to_infection,\n\tcoalesce(sum(no_of_maternal_death_due_to_bd_intrasit), 0) as no_of_maternal_death_due_to_bd_intrasit,\n\tcoalesce(sum(no_of_maternal_death_due_to_other_causes), 0) as no_of_maternal_death_due_to_other_causes,\n\tcoalesce(sum(number_of_maternal_death_due_to_other_causes), 0) as number_of_maternal_death_due_to_other_causes,\n\tcoalesce(sum(pw_provided_free_medicines_jssk), 0) as pw_provided_free_medicines_jssk,\n\tcoalesce(sum(pw_provided_free_diet_jssk), 0) as pw_provided_free_diet_jssk,\n\tcoalesce(sum(pw_provided_free_diagnostics_jssk), 0) as pw_provided_free_diagnostics_jssk,\n\tcoalesce(sum(pw_provided_free_home_to_facility_trasport_jssk), 0) as pw_provided_free_home_to_facility_trasport_jssk,\n\tcoalesce(sum(pw_provided_free_home_interfacility_transfers_jssk), 0) as pw_provided_free_home_interfacility_transfers_jssk,\n\tcoalesce(sum(pw_provided_free_drop_back_to_home_jssk), 0) as pw_provided_free_drop_back_to_home_jssk,\n\tcoalesce(sum(sick_infant_provided_free_medicines_jssk), 0) as sick_infant_provided_free_medicines_jssk,\n\tcoalesce(sum(sick_infant_provided_free_drop_back_to_home_jssk), 0) as sick_infant_provided_free_drop_back_to_home_jssk,\n\tcoalesce(sum(sick_infant_provided_free_diagnostics_jssk), 0) as sick_infant_provided_free_diagnostics_jssk,\n\tcoalesce(sum(sick_infant_provided_free_home_to_facility_trasport_jssk), 0) as sick_infant_provided_free_home_to_facility_trasport_jssk,\n\tcoalesce(sum(sick_infant_provided_free_home_interfacility_transfers_jssk), 0) as sick_infant_provided_free_home_interfacility_transfers_jssk,\n\tcoalesce(sum(sick_infant_provided_free_drop_back_to_home_jssk), 0) as assick_infant_provided_free_drop_back_to_home_jssk\nfrom\n\thealth_infrastructure_details lhc\nleft join property on\n\tlhc.id = property.health_infra_id\nleft join hmis_rch_service_provided_during_year on\n\tlhc.id = hmis_rch_service_provided_during_year.health_infra_id\n\tand hmis_rch_service_provided_during_year.month_year = property.month_year\nleft join ncd_service_provided_during_year_t on\n\tlhc.id = ncd_service_provided_during_year_t.health_infra_id\n\tand ncd_service_provided_during_year_t.month_year = property.month_year\nleft join hmis_child_services_given_analytics on\n\tlhc.id = hmis_child_services_given_analytics.health_infa_id\n\tand hmis_child_services_given_analytics.month_year = property.month_year ),\nfinal_data as (\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.1'' as \"Sr No\",\n\t''Total number of pregnant women registered for ANC '' as \"Data Item\",\n\tanc_reg as count\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.1.1'',\n\t''Out of the total ANC registered, number registered within 1st trimester (within 12 weeks)'',\n\tanc1\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.1.2'',\n\t''Number of High risk pregnant women identified'',\n\thigh_risk\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.1.3'',\n\t''out of above number of High Risk Pregnant women received fiollw up visits'',\n\thigh_risk_follow_up\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.1'',\n\t''Number of PW given TT1/Td1'',\n\ttt1\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.2'',\n\t''Number of PW given TT2/Td2'',\n\ttt2\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.3'',\n\t''Number of PW given TT/Td Booster'',\n\ttt_booster\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.4'',\n\t''Number of PW given 180 Iron Folic Acid (IFA) tablets '',\n\tno_of_pw_180_ifa_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.5'',\n\t''Number of PW given 360 Calcium tablets '',\n\tno_of_pw_ca_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.6'',\n\t''Number of PW given one Albendazole tablet after 1st trimester'',\n\tno_of_pw_alben_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.7'',\n\t''Number of PW received 4 or more ANC check ups'',\n\tno_of_pw_with_more_then_4_anc_checkup\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.2.8'',\n\t''Number of PW given ANC Corticosteroids in Pre Term Labour'',\n\tno_of_pw_anc_corticosteroids_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.3.1'',\n\t''New cases of PW with hypertension detected '',\n\tno_of_pw_with_hypertension\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.3.1.a'',\n\t''Out of the new cases of PW with hypertension detected, cases managed at institution '',\n\tno_of_pw_with_hypertension_migrated_at_institution\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.1'',\n\t''Number of PW tested for Haemoglobin (Hb ) 4 or more than 4 times for respective ANCs'',\n\thb_test_more_than_4_time\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.2'',\n\t''Total Number of PW tested for Haemoglobin (Hb )'',\n\thbsag_test_cnt\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.3'',\n\t''Out of Total Number of PW tested for Haemoglobin (Hb ):'',\n\ttemp2\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.3.a'',\n\t''Number of PW having Hb level<11(7.1 to 10.9)'',\n\tno_of_pw__with_hb_count_less_then_11\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.3.b'',\n\t''Number of PW having Hb level<7 '',\n\tno_of_pw__with_hb_count_less_then_7\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''1.4.4'',\n\t''Number of PW treated for severe anaemia (Hb<7)'',\n\tno_of_pw_tested_for_anemia_with_hb_count_less_then_7\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''1.5.2'',\n\t''Number of PW tested positive for GDM'',\n\tno_of_pw_tested_for_gdm\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''1.6'',\n\t''Pregnant Women (PW) with Syphilis'',\n\ttemp3\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''1.6.2.a'',\n\t''Number of pregnant women tested for Syphilis'',\n\tno_of_pw_tested_for_syphilis\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''1.6.2.b'',\n\t''Number of pregnant women tested found sero positive for Syphilis'',\n\tno_of_pw_tested_positive_for_syphilis\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''1.6.2.c'',\n\t''Number of syphilis positive pregnanat women treated for Syphilis'',\n\tno_of_pw_treated_for_syphilis\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''2.1'',\n\t''Deliveries conducted at Home'',\n\tno_of_home_del\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''2.1.1.a'',\n\t''Number of Home Deliveries attended by Skill Birth Attendant(SBA) (Doctor/Nurse/ANM/Midwife) '',\n\thome_del_by_sba\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''2.1.1.b'',\n\t''Number of Home Deliveries attended by Non SBA (Trained Birth Attendant(TBA) /Relatives/etc.)'',\n\thome_del_by_non_sba\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''2.1.2'',\n\t''Number of PW given Tablet Misoprostol during home delivery'',\n\tno_of_pw_misoprostol_given_home_del\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''2.2'',\n\t''Number of Institutional Deliveries conducted (Including C-Sections)'',\n\tno_of_inst_dl\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''2.2.1'',\n\t''Delivery took place in Transit (during home to health facility, in referral from one health facility to another, in ambulance, etc.)'',\n\tno_of_delivery_in_transit\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''2.2.2'',\n\t''Out of total institutional deliveries(excluding C-section), number of women have stayed till 48 hours '',\n\tno_of_inst_del_stayed_48_hrs\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''3.1'',\n\t''Total C -Section deliveries performed'',\n\tno_of_c_section_delivery\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''3.1.1'',\n\t''C-sections, performed at night (8 PM- 8 AM)'',\n\tno_of_c_section_delivery_at_night\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.1.1.a'',\n\t''Live Birth - Male'',\n\tlive_birth_male\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.1.1.b'',\n\t''Live Birth - Female'',\n\tlive_birth_female\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.1.2'',\n\t''Number of Pre term newborns ( < 37 weeks of pregnancy)'',\n\tno_of_pre_term_newborns\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.2'',\n\t''Abortion (spontaneous)'',\n\tabortion\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.3'',\n\t''Abortion by Medical methods'',\n\tabortion_medical_method\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.4'',\n\t''Abortion by Surgical methods'',\n\tabortion_surgical_method\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.1.a'',\n\t''MTP up to 12 weeks of pregnancy through '',\n\tmtp\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.1.a.i.'',\n\t''Medical methods'',\n\tmtp_medical_method\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.1.a.i.'',\n\t''Surgical methods'',\n\tmtp_surgical_method\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.1.b'',\n\t''MTP more than 12 weeks of pregnancy'',\n\tmtp_more_than_12_weeks\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.2.a'',\n\t''Post Abortion/ MTP Complications Identified'',\n\tmtp_complication_identified_post_abortion\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.2.b'',\n\t''Post Abortion/ MTP Complications Treated'',\n\tmtp_complication_treated_post_abortion\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.3.3'',\n\t''Number of women provided with post abortion/ MTP contraception '',\n\tno_of_pw_provided_contraception_post_abortion\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.1'',\n\t''Number of newborns weighed at birth'',\n\tweighed\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.2'',\n\t''Number of newborns having weight less than 2.5 kg'',\n\tweighed_less_than_2_5\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.3'',\n\t''Number of Newborns breast fed within 1 hour of birth '',\n\tbreast_feeding_within_1_hr\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.4'',\n\t''Number of Newborns omplications identified (birth defects, deformity, birth asphyxia etc.)'',\n\tno_complications_observed\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.5'',\n\t''Out of above, newborns compilcations managed at the facility'',\n\tno_complications_managed_at_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''4.4.6'',\n\t''out of newborns compilcations, cases refereed to higher facilities'',\n\tno_of_complications_refered_to_hf\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''5.1'',\n\t''Number of cases of pregnant women with Obstetric Complications attended (Antepartum haemorrhage (APH), Post-Partum Hemorrhage (PPH), Sepsis, Eclampsia, Obstructed labour and others) '',\n\tno_of_pw_with_obstetric_complications\nfrom\n\thmis_data\nunion all\nselect\n\t''CHC,SDH,DH'' as display_at,\n\t''5.2'',\n\t''Number of Complicated pregnancies treated with Blood Transfusion'',\n\tno_of_pw_treated_with_blood_transfusion\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''5.3'',\n\t''Number of complicated pregnancy cases referred to higher facilities'',\n\tno_of_complications_refered_to_hf\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''6.1'',\n\t''Women receiving 1st post partum checkups within 48 hours of home delivery'',\n\tno_of_pw_recived_post_partum_in_48_hrs_hd\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''6.3'',\n\t''Number of mothers provided full course of 180 IFA tablets after delivery'',\n\tno_of_pw_180_ifa_full_course_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''6.4'',\n\t''Number of mothers provided 360 Calcium tablets after delivery'',\n\ttemp6\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.1.1'',\n\t''Number of Non Scalpel Vasectomy (NSV) / Conventional Vasectomy conducted'',\n\tno_of_nsv_or_cvc\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.2.1'',\n\t''Number of Laparoscopic sterilizations (excluding post abortion) conducted'',\n\tno_of_laparoscopic_sterilizations_conducted\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.2.2'',\n\t''Number of Interval Mini-lap (other than post-partum and post abortion) sterilizations conducted'',\n\tno_of_mini_lap_sterilization_conducted\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.2.3'',\n\t''Number of Postpartum sterilizations (within 7 days of delivery by minilap or concurrent with caesarean section) conducted'',\n\tno_of_postpartum_sterilizations\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.2.4'',\n\t''Number of Post Abortion sterilizations (within 7 days of spontaneous or surgical abortion) conducted'',\n\tno_of_post_abortion_sterilizations\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.3'',\n\t''Number of Interval IUCD Insertions (excluding PPIUCD and PAIUCD)'',\n\tno_of_interval_iucd_insertions\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.4'',\n\t''Number of Postpartum (within 48 hours of delivery) IUCD insertions'',\n\tno_of_postpartum_iucd_insertion\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''8.5'',\n\t''Number of Post Abortion (within 12 days of spontaneous or surgical abortion) IUCD insertions'',\n\tno_of_post_abortion_iucd_insertion\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.6'',\n\t''Number of IUCD Removals '',\n\tno_of_iucd_removal\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.8'',\n\t''Injectable Contraceptive-Antara Program- First Dose'',\n\tinjectable_cap_first_dose\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.9'',\n\t''Injectable Contraceptive-Antara Program- Second Dose'',\n\tinjectable_cap_second_dose\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.10'',\n\t''Injectable Contraceptive-Antara Program- Third Dose'',\n\tinjectable_cap_third_dose\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.11'',\n\t''Injectable Contraceptive-Antara Program- Fourth or more than fourth'',\n\tinjectable_cap_fourth_dose\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.12.a.'',\n\t''Number of Combined Oral Pills Cycle distributed by the facility'',\n\tno_of_oral_pills_distributed_by_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''8.12.b.'',\n\t''Number of Combined Oral Pills Cycle distributed by ASHA under the HDC ( Home Delivery of Contraceptives)'',\n\tno_of_oral_pills_distributed_by_asha\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.13.a'',\n\t''Number of condom pieces distributed by the facility'',\n\tno_of_condom_distributed_by_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''8.13.b'',\n\t''Number of Condom pieces distributed by ASHA under the HDC ( Home Delivery of Contraceptives)'',\n\tno_of_condom_distributed_by_asha\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.14.a'',\n\t''Number of Centchroman (weekly) pills distributed by the facility '',\n\tno_of_centchroman_pills_distributed_by_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''8.14.b'',\n\t''Number of Centchroman (weekly) pills distributed by ASHA '',\n\tno_of_centchroman_pills_distributed_by_asha\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''8.16.a'',\n\t''Number of Pregnancy Testing kits utilized by the facility '',\n\tno_of_ptk_utilized_by_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''8.16.b'',\n\t''Number of Pregnancy Testing kits utilized by ASHA '',\n\tno_of_ptk_utilized_by_ash\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.1'',\n\t''Child immunisation - Vitamin K (Birth Dose)'',\n\tvitamin_k_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.2'',\n\t''Child immunisation - BCG'',\n\tbcg_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.6'',\n\t''Child immunisation - Pentavalent 1'',\n\tpenta_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.7'',\n\t''Child immunisation - Pentavalent 2'',\n\tpenta_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.8'',\n\t''Child immunisation - Pentavalent 3'',\n\tpenta_3_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.9'',\n\t''Child immunisation - OPV 0 (Birth Dose)'',\n\topv_0_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.10'',\n\t''Child immunisation - OPV1'',\n\topv_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.11'',\n\t''Child immunisation - OPV2'',\n\topv_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.12'',\n\t''Child immunisation - OPV3'',\n\topv_3_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.13'',\n\t''Child immunisation - Hepatitis-B0 (Birth Dose)'',\n\thep_b_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.17'',\n\t''Child immunisation - Inactivated Injectable Polio Vaccine 1 (IPV 1)'',\n\tfipv_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.18'',\n\t''Child immunisation - Inactivated Injectable Polio Vaccine 2 (IPV 2) '',\n\tfipv_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.19'',\n\t''Child immunisation - Rotavirus 1'',\n\trota_virus_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.20'',\n\t''Child immunisation - Rotavirus 2'',\n\trota_virus_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.1.21'',\n\t''Child immunisation - Rotavirus 3'',\n\trota_virus_3_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.2.1'',\n\t''Child immunisation (9-11months) - Measles & Rubella (MR)- 1st Dose'',\n\tmeasles_rubella_1_given_in_9_to_11_month\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.2.2'',\n\t''Child immunisation (9-11months) - Measles 1st dose'',\n\tmeasles_rubella_2_given_in_9_to_11_month\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.2.4'' ,\n\t''Number of children aged between 9 and 11 months fully immunized'',\n\tfully_immunized_bwtween_9_to_11_months\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.2.4.a'' ,\n\t''Children aged between 9 and 11 months fully immunized- Male'',\n\tfully_immunized_bwtween_9_to_11_months_male\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.2.4.b'' ,\n\t''Children aged between 9 and 11 months fully immunized - Female'',\n\tfully_immunized_bwtween_9_to_11_months_female\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.3.1'' ,\n\t''Child immunisation - Measles & Rubella (MR)- 1st Dose'',\n\tmeasles_rubella_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.3.2'' ,\n\t''Child immunisation - Measles-1st dose'',\n\tmeasles_1_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.4.1'' ,\n\t''Child immunisation - Measles & Rubella (MR)- 2nd Dose (16-24 months)'',\n\tmeasles_rubella_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.4.2'' ,\n\t''Child immunisation - Measles 2nd dose (More than 16 months)'',\n\tmeasles_or_rubella_2_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.4.3'' ,\n\t''Child immunisation - DPT 1st Booster '',\n\tdpt_booster_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.4.4'' ,\n\t''Child immunisation - OPV Booster'',\n\topv_booster_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.6.1'',\n\t''Number of cases of AEFI -Minor'',\n\tno_of_cases_of_aefi_minor\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.6.2'',\n\t''Number of cases of AEFI - Severe'',\n\tno_of_cases_of_aefi_severe\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.6.3'',\n\t''Number of cases of AEFI - Serious'',\n\tno_of_cases_of_aefi_serious\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.8.1'',\n\t''Child immunisation - Vitamin A Dose - 1'',\n\tvitamin_a_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.9'',\n\t''Number of children (6-59 months) provided 8-10 doses (1ml) of IFA syrup (Bi weekly)'',\n\tifa_given\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''9.11'',\n\t''Number of severely underweight children provided Health Checkup (0-5 yrs)'',\n\tunweighted_child_given_health_checkup\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.1'',\n\t''Childhood Diseases - Pneumonia'',\n\tchildhood_diseases_pneumonia\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.2'',\n\t''Childhood Diseases - Asthma'',\n\tchildhood_diseases_asthma\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.3'',\n\t''Childhood Diseases - Sepsis'',\n\tchildhood_diseases_sepsis\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.4'',\n\t''Childhood Diseases - Diphtheria'',\n\tchildhood_diseases_diphtheria\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.5'',\n\t''Childhood Diseases - Pertussis'',\n\tchildhood_diseases_pertussis\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.6'',\n\t''Childhood Diseases - Tetanus Neonatorum'',\n\tchildhood_diseases_tetanus_neonatorum\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''10.15'',\n\t''Childhood Diseases - Severe Acute Malnutrition (SAM)'',\n\tchildhood_diseases_sam\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.0.1'',\n\t''HWC Screening services'',\n\ttemp7\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.0.1.1'',\n\t''Hypertension'',\n\ttemp8\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.1.a'',\n\t''Number of hypertension cases screened'',\n\tno_of_hypertension_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.1.b'',\n\t''out of above, Number diagnosed with hypertension'',\n\tno_of_hypertension_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.1.c'',\n\t''Number of hypertension cases on treatment'',\n\tno_of_hypertension_cases_in_treatment\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.2.a'',\n\t''Number of diabetes cases screened'',\n\tno_of_diabetes_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.2.b'',\n\t''out of above, Number diagnosed with diabetes'',\n\tno_of_diabetes_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.2.c'',\n\t''Number of diabetes cases on treatment'',\n\tno_of_diabetes_cases_in_treatment\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.3.a'',\n\t''Number of hypertension & diabetes cases screened'',\n\tno_of_hypertension_and_diabetes_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.3.b'',\n\t''out of above, Number diagnosed with hypertension & diabetes'',\n\tno_of_hypertension_and_diabetes_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.3.c'',\n\t''Number of hypertension & diabetes cases on treatment'',\n\tno_of_hypertension_and_diabetes_cases_in_treatment\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.4.a'',\n\t''Number of Oral cancer cases screened'',\n\tno_of_oral_cancer_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.4.b'',\n\t''out of above, Number of oral cancer cases referred'',\n\tno_of_oral_cancer_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.4.c'',\n\t''Number of oral cancer cases on follow up care'',\n\tno_of_oral_cancer_cases_in_followup\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.5.a'',\n\t''Number of breast cancer cases screened'',\n\tno_of_breast_cancer_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.5.b'',\n\t''out of above, Number of breast cases referred'',\n\tno_of_breast_cancer_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.5.c'',\n\t''Number of breast cancer cases on follow up care'',\n\tno_of_breast_cancer_cases_in_treatment\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.6.a'',\n\t''Number of cervical cancer cases screened'',\n\tno_of_cervical_cases_screened\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.6.b'',\n\t''out of above, Number of cervical cases referred'',\n\tno_of_cervical_cases\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''14.0.1.6.c'',\n\t''Number of cervical cancer cases on follow up care'',\n\tno_of_cervical_cases_in_treatment\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.1.1'',\n\t''Outpatient - Diabetes'',\n\tno_of_outpatient_diabetes\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.1.2'',\n\t''Outpatient - Hypertension'',\n\tno_of_outpatient_hypertension\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.1.3'',\n\t''Outpatients- both Hypertension and Diabetes'',\n\tno_of_hypertension_and_diabetes_outpatients\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''14.2.1'',\n\t''Allopathic- Outpatient attendance'',\n\tno_of_allopathic_outpatient_attendance\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''14.8.1'',\n\t''Operation major (General and spinal anaesthesia)'',\n\tno_of_operation_major\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''14.8.4'',\n\t''Operation minor (No or local anaesthesia)'',\n\tno_of_operation_minor\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''14.14.1'',\n\t''Number of children admitted in NRC'',\n\tno_of_child_admitted_at_nrc\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''14.14.2'',\n\t''Number of children discharged with target weight gain from the NRCs'',\n\tno_of_child_discharge_after_weight_gain_nrc\nfrom\n\thmis_data\nunion all\nselect\n\t''PHC,CHC,SDH,DH'' as display_at,\n\t''15.1'',\n\t''Number of Lab Tests done'',\n\tno_of_lab_test_done\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''15.3.3'',\n\t''Pregnant women tested for HIV'',\n\tno_of_pw_tested_for_hiv\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.1.0'',\n\t''New born deaths within 24 hrs(1 to 23 Hrs) of birth at facility'',\n\tno_of_deaths_in_1_to_24_hrs_at_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''16.1.1'',\n\t''New born deaths within 24 hrs(1 to 23 Hrs) of birth at community'',\n\tno_of_deaths_in_1_to_24_hrs_at_community\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.1.2'',\n\t''New born deaths in transit'',\n\tno_of_deaths_in_transit\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.1.3'',\n\t''New born deaths within 1 week (1 to 7 days) at facility'',\n\tno_of_deaths_in_1_week_at_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''16.1.4'',\n\t''New born deaths within 1 week (1 to 7 days) at community'',\n\tno_of_deaths_in_1_week_at_community\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.1.5'',\n\t''New born deaths within 8 to 28 days at facility'',\n\tno_of_deaths_in_8_to_28_days_at_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''16.1.6'',\n\t''New born deaths within 8 to 28 days at Community'',\n\tno_of_deaths_in_8_to_28_days_at_community\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.2.1'',\n\t''Infant Deaths up to 4 weeks due to Sepsis'',\n\tid_upto_4_weeks_dueto_sepsis\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.2.2'',\n\t''Infant Deaths up to 4 weeks due to Asphyxia'',\n\tid_upto_4_weeks_dueto_asphyxia\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.2.3'',\n\t''Infant Deaths up to 4 weeks due to Other causes'',\n\tid_upto_4_weeks_dueto_other\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.1'',\n\t''Number of Infant Deaths (1 -12 months) due to Pneumonia'',\n\tid_upto_12_moths_dueto_pneumonia\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.2'',\n\t''Number of Infant Deaths (1 -12 months) due to Diarrhoea'',\n\tid_upto_12_moths_dueto_diarrhoea\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.3'',\n\t''Number of Infant Deaths (1 -12 months) due to Fever related'',\n\tid_upto_12_moths_dueto_feaver\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.4'',\n\t''Number of Infant Deaths (1 -12 months) due to Measles'',\n\tid_upto_12_moths_dueto_measles\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.5'',\n\t''Number of Infant Deaths (1 -12 months) due to Others'',\n\tid_upto_12_moths_dueto_other\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.6'',\n\t''Infant Deaths up to 1 to 12 month at facility'',\n\tno_of_infant_deaths_1_to_12_months_at_facility\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC'' as display_at,\n\t''16.3.7'',\n\t''Infant Deaths up to 1 to 12 month at community'',\n\tno_of_infant_deaths_1_to_12_months_at_community\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.3.8'',\n\t''Infant deaths in transit (1 week to 12 months)'',\n\tno_of_infant_deaths_1_to_12_months_in_transit\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.1'',\n\t''Number of Child Deaths (1 -5 years) due to Pneumonia'',\n\tcd_upto_5_years_dueto_pneumonia\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.2'',\n\t''Number of Child Deaths (1 -5 years) due to Diarrhoea'',\n\tcd_upto_5_years_dueto_diarrhoea\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.3'',\n\t''Number of Child Deaths (1 -5 years) due to Fever related'',\n\tcd_upto_5_years_dueto_fever\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.4'',\n\t''Number of Child Deaths (1 -5 years) due to Measles'',\n\tcd_upto_5_years_dueto_measles\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.5'',\n\t''Number of Child Deaths (1 -5 years) due to Others'',\n\tcd_upto_5_years_dueto_others\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.4.6'',\n\t''Child deaths in transit (1- 5 years) du to any cause'',\n\tno_of_child_death_1_to_5_years_due_to_cause\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''16.4.7'',\n\t''Number of Child deaths reported through Community based Child Death Review (CBCDR)'',\n\tno_of_child_death_reported_through_cbcdr\nfrom\n\thmis_data\nunion all\nselect\n\t''CHC,SDH,DH'' as display_at,\n\t''16.4.8'',\n\t''Total Facility Based Child Death Reviews (FBCDR) done'',\n\ttotal_fbcdr_done\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.1'',\n\t''Number of Maternal Deaths due to APH (Antepartum Haemmorhage)'',\n\tno_of_maternal_death_due_to_aph\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.2'',\n\t''Number of Maternal Deaths due to PPH (Postpartum Haemmorhage)'',\n\tno_of_maternal_death_due_to_pr_pph_issue\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.3'',\n\t''Number of Maternal Deaths due to Anaemia'',\n\tno_of_maternal_death_due_to_pr_anaemia_issue\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.4'',\n\t''Number of Maternal Deaths due to Pregnancy related sepsis'',\n\tno_of_maternal_death_due_to_pr_sepsis\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.6'',\n\t''Number of Maternal Deaths due to Abortive outcome'',\n\tno_of_maternal_death_due_to_abortive_outcome\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.7'',\n\t''Number of Maternal Deaths due to Obstructed/prolonged labour'',\n\tno_of_maternal_death_due_to_obstructed_labour\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.8'',\n\t''Number of Maternal Deaths due to Hypertensive disorder in pregnancy, birth and puerperium'',\n\tno_of_maternal_death_due_to_hypertensive_disorder\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.9.a'',\n\t''Cardiac Disorder'',\n\tno_of_maternal_death_due_to_cd\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.9.b'',\n\t''Liver Disorder'',\n\tno_of_maternal_death_due_to_ld\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.9.c'',\n\t''Renal Disorder'',\n\tno_of_maternal_death_due_to_rd\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.9.d'',\n\t''Infections/Infestations'',\n\tno_of_maternal_death_due_to_infection\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.9.e'',\n\t''Brought Dead (in transit)'',\n\tno_of_maternal_death_due_to_bd_intrasit\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.10'',\n\t''Number of Maternal Deaths due to Other Causes'',\n\tno_of_maternal_death_due_to_other_causes\nfrom\n\thmis_data\nunion all\nselect\n\t''SC,PHC,CHC,SDH,DH'' as display_at,\n\t''16.5.11'',\n\t''Number of Maternal Deaths due to Other Causes (causes not known)'',\n\tnumber_of_maternal_death_due_to_other_causes\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.1'',\n\t''Number of Pregnant Women provided - Free Medicines under JSSK'',\n\tpw_provided_free_medicines_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.2'',\n\t''Number of Pregnant Women provided - Free Diet under JSSK'',\n\tpw_provided_free_diet_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.3'',\n\t''Number of Pregnant Women provided - Free Diagnostics under JSSK'',\n\tpw_provided_free_diagnostics_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.4'',\n\t''Number of Pregnant Women provided - Free Home to facility transport under JSSK'',\n\tpw_provided_free_home_to_facility_trasport_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.5'',\n\t''Number of Pregnant Women provided - Interfacility transfers when needed under JSSK'',\n\tpw_provided_free_home_interfacility_transfers_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.1.6'',\n\t''Number of Pregnant Women provided - Free Drop Back home under JSSK'',\n\tpw_provided_free_drop_back_to_home_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.2.1'',\n\t''Number of sick infants provided - Free Medicines under JSSK'',\n\tsick_infant_provided_free_medicines_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.2.2'',\n\t''Number of sick infants provided - Free Diagnostics under JSSK'',\n\tsick_infant_provided_free_diagnostics_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.2.3'',\n\t''Number of sick infants provided - Free Home to facility transport under JSSK'',\n\tsick_infant_provided_free_home_to_facility_trasport_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.2.4'',\n\t''Number of sick infants provided - Interfacility transfers when needed under JSSK'',\n\tsick_infant_provided_free_home_interfacility_transfers_jssk\nfrom\n\thmis_data\nunion all\nselect\n\t''DHQ'' as display_at,\n\t''25.2.5'',\n\t''Number of sick infants provided - Free Drop Back home under JSSK'',\n\tsick_infant_provided_free_drop_back_to_home_jssk\nfrom\n\thmis_data )\nselect\n\t\"Sr No\",\n\t\"Data Item\",\n\tcount\nfrom\n\tfinal_data\nwhere display_at ilike concat(''%'',(select cast(infra_type as text) from property), ''%'');","queryId":1791,"queryUUID":"868ffd49-a3ca-4ddd-9e12-c8ed83d20f32","queryParams":"month_year,health_infra_id"}]},"isPrintOption":true,"isExcelOption":true,"locationLevel":"","showDateAsOn":true}','hmis_new','74481c42-5a91-4ba0-990a-e77e3326a0a9'),
	 (687,'CBAC Form Fillup Count User Wise',NULL,true,'DYNAMIC',(select id from menu_config where menu_name = 'CBAC Form Fillup Count User Wise'),'2019-04-08 14:37:00.071',409,'2019-03-29 21:10:54.497',63746,'{"layout": "dynamicReportWithPagination", "containers": {"tableContainer": [{"query": "with dates as(\nselect to_date(case when ''#from_date#'' = ''null'' then null else ''#from_date#'' end,''MM-DD-YYYY'') as from_date\n,to_date(case when ''#to_date#'' = ''null'' then null else ''#to_date#'' end,''MM-DD-YYYY'') + interval ''1 day'' - interval ''1 millisecond'' as to_date\n),user_wise_count_det as(\nselect created_by as user_id,count(*) as cnt from ncd_member_cbac_detail,dates\nwhere created_on between dates.from_date and dates.to_date\ngroup by created_by\n),user_id_det as(\nselect distinct um_user.id as user_id,user_name from um_user,um_user_location,location_hierchy_closer_det\nwhere um_user_location.user_id = um_user.id\nand um_user.role_id = 24/*ASHA*/\nand um_user_location.state = ''ACTIVE'' and um_user.state = ''ACTIVE'' \nand um_user_location.loc_id = location_hierchy_closer_det.child_id\nand location_hierchy_closer_det.parent_id = #location_id#\nand um_user.id in (select user_id from user_wise_count_det)\n/*union\nselect user_id from user_wise_count_det*/\norder by um_user.user_name\n#limit_offset#\n),location_det as(\nselect location_hierchy_closer_det.child_id as loc_id,string_agg(name,'' > '' order by depth desc) as loc_name\nfrom location_hierchy_closer_det,location_master\nwhere location_hierchy_closer_det.child_id in (\nselect loc_id from um_user_location,user_id_det  where user_id_det.user_id = um_user_location.user_id and state = ''ACTIVE'')\nand location_hierchy_closer_det.parent_id = location_master.id\ngroup by location_hierchy_closer_det.child_id\n)\nselect ROW_NUMBER() over () + cast(SUBSTRING(''#limit_offset#'', POSITION(''offset'' in ''#limit_offset#'') + 7) as int) as \"Sr No\",concat(concat_ws('' '',um_user.first_name,um_user.middle_name,um_user.last_name),''('',um_user.user_name,'')'') as \"Asha name\"\n, string_agg(location_det.loc_name,''<br/>'') as \"Location\"\n,user_wise_count_det.cnt as \"Count\"\nfrom user_id_det\nleft join user_wise_count_det on user_wise_count_det.user_id = user_id_det.user_id\ninner join um_user on um_user.id  = user_id_det.user_id\ninner join um_user_location on um_user.id  = um_user_location.user_id and um_user_location.state = ''ACTIVE''\ninner join location_det on um_user_location.loc_id = location_det.loc_id \ngroup by um_user.first_name,um_user.middle_name,um_user.last_name,um_user.user_name,user_wise_count_det.cnt", "queryId": 942, "fieldName": "tableField", "queryUUID": "db223953-778d-4a0d-a399-56e25ba4e402", "queryParams": "limit_offset,from_date,to_date,location_id"}], "fieldsContainer": [{"index": 0, "fieldName": "location_id", "fieldType": "location", "queryUUID": null, "displayName": "Location", "isMandatory": true, "queryUUIDForParam": null, "requiredUptoLevel": 1, "fetchAccordingToUserAoi": true}, {"fieldName": "date", "fieldType": "dateFromTo", "queryUUID": null, "displayName": "Date", "isMandatory": true, "queryUUIDForParam": null, "requiredUptoLevel": 1}]}, "showDateAsOn": true, "templateType": "DYNAMIC_REPORTS", "isExcelOption": true, "isPrintOption": true, "locationLevel": ""}','cbac_user_wise','ee98259d-4c96-4aab-b30e-0c87abeea575'),
	 (688,'NCD Progress tracking report',NULL,true,'DYNAMIC',(select id from menu_config where menu_name = 'NCD Progress tracking report'),'2020-12-30 17:49:12.782',409,'2019-07-29 06:58:40.571',66522,'{"layout":"dynamicReport1","containers":{"tableContainer":[{"query":"with location_detail as (\n\tselect child_id from location_hierchy_closer_det where parent_id = #location_id# and depth = 1\n),dates as (\n\tselect to_date(case when ''#from_date#'' = ''null'' then null else ''#from_date#'' end, ''MM-DD-YYYY'') as from_date,\n\tto_date(case when ''#to_date#'' = ''null'' then null else ''#to_date#'' end,''MM-DD-YYYY'') + interval ''1 month'' - interval ''1 millisecond'' as to_date\n),details as (\n\tselect location_detail.child_id,\n\tcoalesce(sum(member_enrolled),0) as member_enrolled,\n\tcoalesce(sum(member_30_plus),0) as member_30_plus,\n\tcoalesce(sum(number_of_asha),0) as number_of_asha,\n\tcoalesce(sum(number_of_inactive_asha),0) as number_of_inactive_asha,\n\tcoalesce(sum(number_of_fhw),0) as number_of_fhw,\n\tcoalesce(sum(number_of_inactive_fhw),0) as number_of_inactive_fhw,\n\tcoalesce(sum(number_of_mo),0) as number_of_mo,\n\tcoalesce(sum(number_of_active_mo),0) as number_of_active_mo\n\tfrom location_detail\n\tinner join location_hierchy_closer_det on location_detail.child_id = location_hierchy_closer_det.parent_id\n\tinner join ncd_analytics_detail on location_hierchy_closer_det.child_id = ncd_analytics_detail.location_id\n\tinner join dates on ncd_analytics_detail.month_year = dates.from_date\n\tgroup by location_detail.child_id\n),analytics as (\n\tselect location_detail.child_id,\n\tcoalesce(sum(number_of_cbac_form_filled),0) as number_of_cbac_form_filled,\n\tcoalesce(sum(number_of_member_at_risk),0) as number_of_member_at_risk,\n\tcoalesce(sum(fhw_screened_diabetes_male),0) as fhw_screened_diabetes_male,\n\tcoalesce(sum(fhw_screened_diabetes_female),0) as fhw_screened_diabetes_female,\n\tcoalesce(sum(fhw_screened_hypertension_male),0) as fhw_screened_hypertension_male,\n\tcoalesce(sum(fhw_screened_hypertension_female),0) as fhw_screened_hypertension_female,\n\tcoalesce(sum(fhw_screened_oral_male),0) as fhw_screened_oral_male,\n\tcoalesce(sum(fhw_screened_oral_female),0) as fhw_screened_oral_female,\n\tcoalesce(sum(fhw_screened_breast_female),0) as fhw_screened_breast_female,\n\tcoalesce(sum(fhw_screened_cervical_female),0) as fhw_screened_cervical_female,\n\tcoalesce(sum(no_abnormally_detected_male),0) as no_abnormally_detected_male,\n\tcoalesce(sum(no_abnormally_detected_female),0) as no_abnormally_detected_female,\n\tcoalesce(sum(fhw_referred_diabetes_male),0) as fhw_referred_diabetes_male,\n\tcoalesce(sum(fhw_referred_diabetes_female),0) as fhw_referred_diabetes_female,\n\tcoalesce(sum(fhw_referred_hypertension_male),0) as fhw_referred_hypertension_male,\n\tcoalesce(sum(fhw_referred_hypertension_female),0) as fhw_referred_hypertension_female,\n\tcoalesce(sum(fhw_referred_oral_male),0) as fhw_referred_oral_male,\n\tcoalesce(sum(fhw_referred_oral_female),0) as fhw_referred_oral_female,\n\tcoalesce(sum(fhw_referred_breast_female),0) as fhw_referred_breast_female,\n\tcoalesce(sum(fhw_referred_cervical_female),0) as fhw_referred_cervical_female,\n\tcoalesce(sum(mo_examined_diabetes_male),0) as mo_examined_diabetes_male,\n\tcoalesce(sum(mo_examined_diabetes_female),0) as mo_examined_diabetes_female,\n\tcoalesce(sum(mo_examined_hypertension_male),0) as mo_examined_hypertension_male,\n\tcoalesce(sum(mo_examined_hypertension_female),0) as mo_examined_hypertension_female,\n\tcoalesce(sum(mo_examined_oral_male),0) as mo_examined_oral_male,\n\tcoalesce(sum(mo_examined_oral_female),0) as mo_examined_oral_female,\n\tcoalesce(sum(mo_examined_breast_female),0) as mo_examined_breast_female,\n\tcoalesce(sum(mo_examined_cervical_female),0) as mo_examined_cervical_female,\n\tcoalesce(sum(mo_diagnosed_diabetes_male),0) as mo_diagnosed_diabetes_male,\n\tcoalesce(sum(mo_diagnosed_diabetes_female),0) as mo_diagnosed_diabetes_female,\n\tcoalesce(sum(mo_diagnosed_hypertension_male),0) as mo_diagnosed_hypertension_male,\n\tcoalesce(sum(mo_diagnosed_hypertension_female),0) as mo_diagnosed_hypertension_female,\n\tcoalesce(sum(mo_diagnosed_oral_male),0) as mo_diagnosed_oral_male,\n\tcoalesce(sum(mo_diagnosed_oral_female),0) as mo_diagnosed_oral_female,\n\tcoalesce(sum(mo_diagnosed_breast_female),0) as mo_diagnosed_breast_female,\n\tcoalesce(sum(mo_diagnosed_cervical_female),0) as mo_diagnosed_cervical_female,\n\tcoalesce(sum(under_treatment_diabetes_male),0) as under_treatment_diabetes_male,\n\tcoalesce(sum(under_treatment_diabetes_female),0) as under_treatment_diabetes_female,\n\tcoalesce(sum(under_treatment_hypertension_male),0) as under_treatment_hypertension_male,\n\tcoalesce(sum(under_treatment_hypertension_female),0) as under_treatment_hypertension_female,\n\tcoalesce(sum(under_treatment_oral_male),0) as under_treatment_oral_male,\n\tcoalesce(sum(under_treatment_oral_female),0) as under_treatment_oral_female,\n\tcoalesce(sum(under_treatment_breast_female),0) as under_treatment_breast_female,\n\tcoalesce(sum(under_treatment_cervical_female),0) as under_treatment_cervical_female,\n\tcoalesce(sum(secondary_referred_diabetes_male),0) as secondary_referred_diabetes_male,\n\tcoalesce(sum(secondary_referred_diabetes_female),0) as secondary_referred_diabetes_female,\n\tcoalesce(sum(secondary_referred_hypertension_male),0) as secondary_referred_hypertension_male,\n\tcoalesce(sum(secondary_referred_hypertension_female),0) as secondary_referred_hypertension_female,\n\tcoalesce(sum(secondary_referred_oral_male),0) as secondary_referred_oral_male,\n\tcoalesce(sum(secondary_referred_oral_female),0) as secondary_referred_oral_female,\n\tcoalesce(sum(secondary_referred_breast_female),0) as secondary_referred_breast_female,\n\tcoalesce(sum(secondary_referred_cervical_female),0) as secondary_referred_cervical_female\n\tfrom location_detail\n\tinner join location_hierchy_closer_det on location_detail.child_id = location_hierchy_closer_det.parent_id\n\tinner join ncd_analytics_detail on location_hierchy_closer_det.child_id = ncd_analytics_detail.location_id\n\tinner join dates on ncd_analytics_detail.month_year between dates.from_date and dates.to_date\n\tgroup by location_detail.child_id\n)\nselect row_number() over() as \"Sr.no\",\nlocation_master.name as \"Location\",\nlocation_master.id as hidden_location_id,\nmember_enrolled as \"Population enrollment\",\nmember_30_plus as \"Population ≥ 30 years\",\nnumber_of_asha as \"Number of ASHAs\",\nnumber_of_inactive_asha as \"Number of Inactive ASHAs\",\nnumber_of_cbac_form_filled as \"Number of CBAC forms filled\",\nnumber_of_member_at_risk as \"Number of Members At Risk\",\nnumber_of_fhw as \"Number of ANMs/CHO\",\nnumber_of_inactive_fhw as \"Number of Inactive ANMs/CHOs\",\nfhw_screened_diabetes_male as \"Screened by ANM/CHOs Diabetes Male\",\nfhw_screened_diabetes_female as \"Screened by ANM/CHOs Diabetes Female\",\nfhw_screened_hypertension_male as \"Screened by ANM/CHOs Hypertension Male\",\nfhw_screened_hypertension_female as \"Screened by ANM/CHOs Hypertension Female\",\nfhw_screened_oral_male as \"Screened by ANM/CHOs Oral Cancer Male\",\nfhw_screened_oral_female as \"Screened by ANM/CHOs Oral Cancer Female\",\nfhw_screened_breast_female as \"Screened by ANM/CHOs Breast Cancer Female\",\nfhw_screened_cervical_female as \"Screened by ANM/CHOs Cervical Cancer Female\",\nno_abnormally_detected_male as \"Number of No Abnormality Detected Male\",\nno_abnormally_detected_female as \"Number of No Abnormality Detected Female\",\nfhw_referred_diabetes_male as \"Referred to PHC Diabetes Male\",\nfhw_referred_diabetes_female as \"Referred to PHC Diabetes Female\",\nfhw_referred_hypertension_male as \"Referred to PHC Hypertension Male\",\nfhw_referred_hypertension_female as \"Referred to PHC Hypertension Female\",\nfhw_referred_oral_male as \"Referred to PHC Oral Cancer Male\",\nfhw_referred_oral_female as \"Referred to PHC Oral Cancer Female\",\nfhw_referred_breast_female as \"Referred to PHC Breast Cancer Female\",\nfhw_referred_cervical_female as \"Referred to PHC Cervical Cancer Female\",\nnumber_of_mo as \"Number of MO\",\nnumber_of_active_mo as \"Number of Active Mos\",\nmo_examined_diabetes_male as \"Examined at PHC Diabetes Male\",\nmo_examined_diabetes_female as \"Examined at PHC Diabetes Female\",\nmo_examined_hypertension_male as \"Examined at PHC Hypertension Male\",\nmo_examined_hypertension_female as \"Examined at PHC Hypertension Female\",\nmo_examined_oral_male as \"Examined at PHC Oral Cancer Male\",\nmo_examined_oral_female as \"Examined at PHC Oral Cancer Female\",\nmo_examined_breast_female as \"Examined at PHC Breast Cancer Female\",\nmo_examined_cervical_female as \"Examined at PHC Cervical Cancer Female\",\nmo_diagnosed_diabetes_male as \"Diagnosed Cases Diabetes Male\",\nmo_diagnosed_diabetes_female as \"Diagnosed Cases Diabetes Female\",\nmo_diagnosed_hypertension_male as \"Diagnosed Cases Hypertension Male\",\nmo_diagnosed_hypertension_female as \"Diagnosed Cases Hypertension Female\",\nmo_diagnosed_oral_male as \"Diagnosed Cases Oral Cancer Male\",\nmo_diagnosed_oral_female as \"Diagnosed Cases Oral Cancer Female\",\nmo_diagnosed_breast_female as \"Diagnosed Cases Breast Cancer Female\",\nmo_diagnosed_cervical_female as \"Diagnosed Cases Cervical Cancer Female\",\nunder_treatment_diabetes_male as \"Under Treatment Diabetes Male\",\nunder_treatment_diabetes_female as \"Under Treatment Diabetes Female\",\nunder_treatment_hypertension_male as \"Under Treatment Hypertension Male\",\nunder_treatment_hypertension_female as \"Under Treatment Hypertension Female\",\nunder_treatment_oral_male as \"Under Treatment Oral Cancer Male\",\nunder_treatment_oral_female as \"Under Treatment Oral Cancer Female\",\nunder_treatment_breast_female as \"Under Treatment Breast Cancer Female\",\nunder_treatment_cervical_female as \"Under Treatment Cervical Cancer Female\",\nsecondary_referred_diabetes_male as \"Referred to Secondary Diabetes Male\",\nsecondary_referred_diabetes_female as \"Referred to Secondary Diabetes Female\",\nsecondary_referred_hypertension_male as \"Referred to Secondary Hypertension Male\",\nsecondary_referred_hypertension_female as \"Referred to Secondary Hypertension Female\",\nsecondary_referred_oral_male as \"Referred to Secondary Oral Cancer Male\",\nsecondary_referred_oral_female as \"Referred to Secondary Oral Cancer Female\",\nsecondary_referred_breast_female as \"Referred to Secondary Breast Cancer Female\",\nsecondary_referred_cervical_female as \"Referred to Secondary Cervical Cancer Female\"\nfrom location_master\ninner join analytics on location_master.id = analytics.child_id\ninner join details on location_master.id = details.child_id\nunion all\nselect null,\n''<b>Total<b>'',\n#location_id#,\nsum(member_enrolled),\nsum(member_30_plus),\nsum(number_of_asha),\nsum(number_of_inactive_asha),\nsum(number_of_cbac_form_filled),\nsum(number_of_member_at_risk),\nsum(number_of_fhw),\nsum(number_of_inactive_fhw),\nsum(fhw_screened_diabetes_male),\nsum(fhw_screened_diabetes_female),\nsum(fhw_screened_hypertension_male),\nsum(fhw_screened_hypertension_female),\nsum(fhw_screened_oral_male),\nsum(fhw_screened_oral_female),\nsum(fhw_screened_breast_female),\nsum(fhw_screened_cervical_female),\nsum(no_abnormally_detected_male),\nsum(no_abnormally_detected_female),\nsum(fhw_referred_diabetes_male),\nsum(fhw_referred_diabetes_female),\nsum(fhw_referred_hypertension_male),\nsum(fhw_referred_hypertension_female),\nsum(fhw_referred_oral_male),\nsum(fhw_referred_oral_female),\nsum(fhw_referred_breast_female),\nsum(fhw_referred_cervical_female),\nsum(number_of_mo),\nsum(number_of_active_mo),\nsum(mo_examined_diabetes_male),\nsum(mo_examined_diabetes_female),\nsum(mo_examined_hypertension_male),\nsum(mo_examined_hypertension_female),\nsum(mo_examined_oral_male),\nsum(mo_examined_oral_female),\nsum(mo_examined_breast_female),\nsum(mo_examined_cervical_female),\nsum(mo_diagnosed_diabetes_male),\nsum(mo_diagnosed_diabetes_female),\nsum(mo_diagnosed_hypertension_male),\nsum(mo_diagnosed_hypertension_female),\nsum(mo_diagnosed_oral_male),\nsum(mo_diagnosed_oral_female),\nsum(mo_diagnosed_breast_female),\nsum(mo_diagnosed_cervical_female),\nsum(under_treatment_diabetes_male),\nsum(under_treatment_diabetes_female),\nsum(under_treatment_hypertension_male),\nsum(under_treatment_hypertension_female),\nsum(under_treatment_oral_male),\nsum(under_treatment_oral_female),\nsum(under_treatment_breast_female),\nsum(under_treatment_cervical_female),\nsum(secondary_referred_diabetes_male),\nsum(secondary_referred_diabetes_female),\nsum(secondary_referred_hypertension_male),\nsum(secondary_referred_hypertension_female),\nsum(secondary_referred_oral_male),\nsum(secondary_referred_oral_female),\nsum(secondary_referred_breast_female),\nsum(secondary_referred_cervical_female)\nfrom location_master\ninner join analytics on location_master.id = analytics.child_id\ninner join details on location_master.id = details.child_id","queryId":1264,"fieldName":"tableField","queryUUID":"2f841f54-170e-4c03-818b-b5228fb4f3a6","queryParams":"from_date,to_date,location_id"}],"fieldsContainer":[{"index":0,"fieldName":"location_id","fieldType":"location","queryUUID":null,"displayName":"Location","isMandatory":true,"fetchUptoLevel":"7","queryUUIDForParam":null,"requiredUptoLevel":1,"fetchAccordingToUserAoi":true},{"fieldName":"date","fieldType":"onlyMonthFromTo","queryUUID":null,"displayName":"Month","isMandatory":true,"queryUUIDForParam":null,"requiredUptoLevel":1}],"tableFieldContainer":[]},"templateType":"DYNAMIC_REPORTS","isExcelOption":true,"isPrintOption":true,"locationLevel":"","isLandscape":false,"numberOfRecordsPerPage":20,"numberOfColumnPerPage":10}','ncd_prog','04737cee-18b2-4d04-bb14-0d9ee73c67d1'),
	 (689,'NCD Screening Status',NULL,true,'DYNAMIC',(select id from menu_config where menu_name = 'NCD Screening Status'),'2022-12-20 15:10:34.433',58981,'2019-01-10 11:53:00.648',97072,'{"layout":"dynamicReport1","containers":{"tableContainer":[{"query":"with prefered_language as(\n    select (\n            case\n                when report_preferred_language = ''EN'' then true\n                else false\n            end\n        ) as is_enlish\n    from um_user\n    where id = #loggedInUserId#\n),\nusers as (\n    select distinct created_by\n    from ncd_member_breast_detail\n    union\n    select distinct created_by\n    from ncd_member_cervical_detail\n    union\n    select distinct created_by\n    from ncd_member_diabetes_detail\n    union\n    select distinct created_by\n    from ncd_member_hypertension_detail\n    union\n    select distinct created_by\n    from ncd_member_oral_detail\n    union\n    select distinct created_by\n    from ncd_member_mental_health_detail\n),\naoi as (\n    select usr.id as id,\n        usr.user_name,\n        usr.first_name,\n        usr.last_name,\n        string_agg(loc_name, ''<br>'') as aoi,\n        (\n            select count(*)\n            from ncd_member_breast_detail\n            where created_by = usr.id\n        ) as breast,\n        (\n            select count(*)\n            from ncd_member_cervical_detail\n            where created_by = usr.id\n        ) as cervical,\n        (\n            select count(*)\n            from ncd_member_diabetes_detail\n            where created_by = usr.id\n        ) as diabetes,\n        (\n            select count(*)\n            from ncd_member_hypertension_detail\n            where created_by = usr.id\n        ) as hyper,\n        (\n            select count(*)\n            from ncd_member_oral_detail\n            where created_by = usr.id\n        ) as oral,\n        (\n            select count(*)\n            from ncd_member_mental_health_detail\n            where created_by = usr.id\n        ) as mental_health\n    from (\n            select usr.id,\n                u_loc.loc_id,\n                string_agg(\n                    case\n                        when (\n                            prefered_language.is_enlish\n                            and (lm.english_name is not null)\n                        ) then lm.english_name\n                        else lm.name\n                    end,\n                    '' >''\n                    order by lh.depth desc\n                ) as loc_name\n            from (\n                    select distinct usr.id\n                    from um_user usr,\n                        um_user_location u_loc,\n                        location_hierchy_closer_det lcloser\n                    where usr.id in (\n                            select *\n                            from users\n                        )\n                        and usr.id = u_loc.user_id\n                        and u_loc.state = ''ACTIVE''\n                        and lcloser.child_id = u_loc.loc_id\n                ) as usr,\n                um_user_location u_loc,\n                location_hierchy_closer_det lh,\n                location_master lm,\n                prefered_language\n            where usr.id = u_loc.user_id\n                and u_loc.state = ''ACTIVE''\n                and u_loc.loc_id = lh.child_id\n                and lh.parent_id = lm.id\n            group by usr.id,\n                u_loc.loc_id\n        ) as user_loc_det,\n        um_user usr\n    where user_loc_det.id = usr.id\n    group by usr.id\n    order by aoi\n)\nselect row_number() over() as \"Sr. no\",\n    aoi.user_name as \"User Name\",\n    aoi.first_name || '' '' || aoi.last_name as \"Name\",\n    aoi.aoi as \"Area of Intervention\",\n    breast as \"Breast Form Filled\",\n    cervical as \"Cervical Form Filled\",\n    diabetes as \"Diabetes Form Filled\",\n    hyper as \"Hypertension Form Filled\",\n    oral as \"Oral Form Filled\",\n    mental_health as \"Mental Health Form Filled\"\nfrom aoi\nunion all\nselect null,\n    null,\n    null,\n    ''<b>Total Forms Filled</b>'',\n    sum(breast),\n    sum(cervical),\n    sum(diabetes),\n    sum(hyper),\n    sum(oral),\n    sum(mental_health)\nfrom aoi","queryId":127,"fieldName":"tableField","queryUUID":"e761dc24-22fb-4f36-9375-b4802102db57","queryParams":"loggedInUserId"}],"fieldsContainer":[],"tableFieldContainer":[]},"templateType":"DYNAMIC_REPORTS","isExcelOption":true,"isPrintOption":true,"locationLevel":"","selectedContainer":"tableContainer","isLandscape":false,"numberOfRecordsPerPage":20,"numberOfColumnPerPage":10}','anm_ncd_work_done','598bde2d-dd11-40bd-aea8-a946a9925e53'),
	 (690,'NCD Form Fillup (Location wise)',NULL,true,'DYNAMIC',(select id from menu_config where menu_name = 'NCD Form Fillup (Location wise)'),'2022-12-20 15:12:42.335',409,'2019-06-25 21:01:36.854',97072,'{"layout":"dynamicReport1","containers":{"tableContainer":[{"query":"with prefered_language as(\n    select (\n            case\n                when report_preferred_language = ''EN'' then true\n                else false\n            end\n        ) as is_enlish\n    from um_user\n    where id = #loggedInUserId#\n),\nloc_det as (\n    select *\n    from location_hierchy_closer_det\n    where parent_id = #location_id#\n        and \"depth\" = 1\n),\ndates as (\n    select to_date(\n            case\n                when ''#from_date#'' = ''null'' then null\n                else ''#from_date#''\n            end,\n            ''MM-DD-YYYY''\n        ) as from_date,\n        to_date(\n            case\n                when ''#to_date#'' = ''null'' then null\n                else ''#to_date#''\n            end,\n            ''MM-DD-YYYY''\n        ) + interval ''1 day'' - interval ''1 millisecond'' as to_date\n),\nncd_member_breast_detail_cnt as (\n    select lhc.parent_id as loc_id,\n        count(*) as member_breast_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_breast_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nncd_member_cervical_detail_cnt as(\n    select lhc.parent_id as loc_id,\n        count(*) as member_cervical_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_cervical_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nncd_member_diabetes_detail_cnt as(\n    select lhc.parent_id as loc_id,\n        count(*) as member_diabetes_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_diabetes_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nncd_member_hypertension_detail_cnt as(\n    select lhc.parent_id as loc_id,\n        count(*) as member_hypertension_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_hypertension_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nncd_member_oral_detail_cnt as (\n    select lhc.parent_id as loc_id,\n        count(*) as member_oral_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_oral_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nncd_member_mental_health_detail_cnt as (\n    select lhc.parent_id as loc_id,\n        count(*) as member_mental_health_detail_cnt\n    from location_hierchy_closer_det lhc,\n        ncd_member_mental_health_detail nmb,\n        dates\n    where nmb.location_id = lhc.child_id\n        and lhc.parent_id in (\n            select child_id\n            from loc_det\n        )\n        and nmb.created_on between dates.from_date and dates.to_date\n    group by lhc.parent_id\n),\nfinal_det as (\n    select case\n            when (\n                prefered_language.is_enlish\n                and (l.english_name is not null)\n            ) then l.english_name\n            else l.name\n        end as location,\n        coalesce(\n            ncd_member_breast_detail_cnt.member_breast_detail_cnt,\n            0\n        ) as member_breast_detail_cnt,\n        coalesce(\n            ncd_member_cervical_detail_cnt.member_cervical_detail_cnt,\n            0\n        ) as member_cervical_detail_cnt,\n        coalesce(\n            ncd_member_diabetes_detail_cnt.member_diabetes_detail_cnt,\n            0\n        ) as member_diabetes_detail_cnt,\n        coalesce(\n            ncd_member_hypertension_detail_cnt.member_hypertension_detail_cnt,\n            0\n        ) as member_hypertension_detail_cnt,\n        coalesce(\n            ncd_member_oral_detail_cnt.member_oral_detail_cnt,\n            0\n        ) as member_oral_detail_cnt,\n        coalesce(\n            ncd_member_mental_health_detail_cnt.member_mental_health_detail_cnt,\n            0\n        ) as member_mental_health_detail_cnt\n    from loc_det ld\n        inner join location_master l on ld.child_id = l.id\n        left join ncd_member_breast_detail_cnt on ncd_member_breast_detail_cnt.loc_id = ld.child_id\n        left join ncd_member_cervical_detail_cnt on ncd_member_cervical_detail_cnt.loc_id = ld.child_id\n        left join ncd_member_diabetes_detail_cnt on ncd_member_diabetes_detail_cnt.loc_id = ld.child_id\n        left join ncd_member_hypertension_detail_cnt on ncd_member_hypertension_detail_cnt.loc_id = ld.child_id\n        left join ncd_member_oral_detail_cnt on ncd_member_oral_detail_cnt.loc_id = ld.child_id\n        left join ncd_member_mental_health_detail_cnt on ncd_member_mental_health_detail_cnt.loc_id = ld.child_id\n        cross join prefered_language\n    order by location\n)\nselect row_number() over() as \"Sr. no\",\n    location as \"Location\",\n    member_breast_detail_cnt as \"Breast Form Filled\",\n    member_cervical_detail_cnt as \"Cervical Form Filled\",\n    member_diabetes_detail_cnt as \"Diabetes Form Filled\",\n    member_hypertension_detail_cnt as \"Hypertension Form Filled\",\n    member_oral_detail_cnt as \"Oral Form Filled\",\n    member_mental_health_detail_cnt as \"Mental Health Form Filled\"\nfrom final_det\nunion all\nselect null,\n    ''Total'',\n    sum(member_breast_detail_cnt),\n    sum(member_cervical_detail_cnt),\n    sum(member_diabetes_detail_cnt),\n    sum(member_hypertension_detail_cnt),\n    sum(member_oral_detail_cnt),\n    sum(member_mental_health_detail_cnt)\nfrom final_det","queryId":1177,"fieldName":"tableField","queryUUID":"c7444c65-e0dc-4acf-91c4-2f8d5a0491dc","queryParams":"from_date,to_date,loggedInUserId,location_id"}],"fieldsContainer":[{"fieldName":"location_id","fieldType":"location","queryUUID":null,"displayName":"Location","isMandatory":true,"fetchUptoLevel":"","queryUUIDForParam":null,"requiredUptoLevel":1,"fetchAccordingToUserAoi":true},{"fieldName":"date","fieldType":"dateFromTo","queryUUID":null,"displayName":"Date","isMandatory":true,"queryUUIDForParam":null,"requiredUptoLevel":1}],"tableFieldContainer":[]},"isFilterOpen":true,"showDateAsOn":true,"templateType":"DYNAMIC_REPORTS","isExcelOption":true,"isPrintOption":true,"locationLevel":"","isLandscape":false,"numberOfRecordsPerPage":20,"numberOfColumnPerPage":10}','ncd_loc_wise','000e1c87-978a-4c44-ba70-9b3e8eee66b3');

-- Menu_config_related_changes

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


--Changes related to query_master
delete from query_master where code in ('fetch_ophthalmologist_patients_list','fetch_unsure_cardiologist_patients_list','fetch_cardiologist_patients_list','fetch_mo_specialist_patients_list');
INSERT INTO public.query_master(created_by, created_on, modified_by, modified_on, code, params, query, returns_result_set, state, description, is_public, "uuid")
    VALUES
    (97070, current_date, 97070, current_date, 'fetch_ophthalmologist_patients_list', 'offset,locationId,limit,uniqueHealthId,userId', 'with base_data as( select distinct imm.id, imf.location_id from imt_member imm inner join imt_family imf on imf.family_id = imm.family_id inner join location_hierchy_closer_det lhcd on lhcd.child_id = imf.location_id where ( case when #locationId# is not null then lhcd.parent_id = #locationId# when #uniqueHealthId# is not null then imm.unique_health_id = #uniqueHealthId# else lhcd.parent_id in ( select uul.loc_id from um_user_location uul where uul.user_id = #userId# and state = ''ACTIVE'' ) end ) ), members as( select member_id, last_retinopathy_test_id, last_opthamologist_id from ncd_specialist_master inner join base_data on base_data.id = ncd_specialist_master.member_id where last_retinopathy_test_id is not null ), member_with_location as( select distinct members.member_id, concat( imm.first_name, '' '', imm.middle_name, '' '', imm.last_name ) as name, date_part(''year'', age(imm.dob)) as age, imm.unique_health_id as "uniqueHealthId", imm.gender, imf.location_id as locationId, lm.name as village, ( case when nod.id is not null then case when nod.created_on > nrtd.created_on and nod.lefteye_feedback = ''unsatisfactory'' or nod.righteye_feedback = ''unsatisfactory'' then ''blue'' when nod.created_on > nrtd.created_on and nod.lefteye_feedback != ''unsatisfactory'' or nod.righteye_feedback != ''unsatisfactory''  then ''green'' when nod.created_on <= nrtd.created_on then ''red'' end end ) as color from members inner join imt_member imm on imm.id = members.member_id inner join imt_family imf on imm.family_id = imf.family_id left join location_master lm on lm.id = imf.location_id left join ncd_opthamologist_data nod on nod.id = members.last_opthamologist_id left join ncd_retinopathy_test_detail nrtd on nrtd.id = members.last_retinopathy_test_id ) select member_with_location.*, lm.name as "subCenter" from member_with_location left join location_hierchy_closer_det lhcd on member_with_location.locationId = lhcd.child_id left join location_master lm on lhcd.parent_id = lm.id where parent_loc_type = ''SC'' order by member_with_location.name ASC limit #limit# offset #offset#',true, 'ACTIVE', NULL, true,'9e85bf90-642e-4fbc-b88d-aeca158c55b1'),
    (97070, current_date, 97070, current_date, 'fetch_unsure_cardiologist_patients_list', 'offset,locationId,limit,uniqueHealthId,userId', 'with base_data as( select distinct imm.id, imf.location_id from imt_member imm inner join imt_family imf on imf.family_id = imm.family_id inner join location_hierchy_closer_det lhcd on lhcd.child_id = imf.location_id where ( case when #locationId# is not null then lhcd.parent_id = #locationId# when #uniqueHealthId# is not null then imm.unique_health_id = #uniqueHealthId# else lhcd.parent_id in ( select uul.loc_id from um_user_location uul where uul.user_id = #userId# and state = ''ACTIVE'' ) end ) )', true, 'ACTIVE', NULL, true, '1c640f4b-124b-4ce5-af56-2270eff80a25'),
    (97070, current_date, 97070, current_date, 'fetch_cardiologist_patients_list', 'offset,locationId,limit,uniqueHealthId,userId', 'with base_data as( select distinct imm.id, imf.location_id from imt_member imm inner join imt_family imf on imf.family_id = imm.family_id inner join location_hierchy_closer_det lhcd on lhcd.child_id = imf.location_id where ( case when #locationId# is not null then lhcd.parent_id = #locationId# when #uniqueHealthId# is not null then imm.unique_health_id = #uniqueHealthId# else lhcd.parent_id in ( select uul.loc_id from um_user_location uul where uul.user_id = #userId# and state = ''ACTIVE'' ) end ) ), members as( select distinct member_id, nsm.last_ecg_specialist_id, nsm.last_cardiologist_id, nsm.last_ecg_screening_id from ncd_specialist_master nsm inner join base_data on base_data.id = nsm.member_id where member_id not in( select nsm.member_id from ncd_specialist_master nsm inner join ncd_ecg_member_detail nemd on nemd.id = nsm.last_ecg_specialist_id and ( nemd.old_mi = 2 or nemd.lvh = 2 ) ) and ( last_ecg_specialist_id is not null or last_cardiologist_id is not null ) ), member_with_location as( select distinct members.member_id, concat( imm.first_name, '' '', imm.middle_name, '' '', imm.last_name ) as name, date_part(''year'', age(imm.dob)) as age, imm.unique_health_id as "uniqueHealthId", imm.gender, imf.location_id as locationId, lm.name as village, ( case when ncd.id is not null then ( case when ncd.case_confirmed is false then ( case when ncd.created_on > nemd.created_on then ''orange'' else ''red'' end ) when ncd.case_confirmed is true or ncd.satisfactory_image is true then ''green'' when ncd.satisfactory_image is false then ( case when ncd.created_on > nmed.created_on then ''blue'' else ''red'' end ) end ) end ) as color, nemd.old_mi, nemd.lvh from members inner join imt_member imm on imm.id = members.member_id inner join imt_family imf on imm.family_id = imf.family_id left join location_master lm on lm.id = imf.location_id left join ncd_member_ecg_detail nmed on nmed.id = members.last_ecg_screening_id left join ncd_cardiologist_data ncd on ncd.id = members.last_cardiologist_id left join ncd_ecg_member_detail nemd on nemd.id = members.last_ecg_specialist_id ) select member_with_location.*, lm.name as "subCenter" from member_with_location left join location_hierchy_closer_det lhcd on member_with_location.locationId = lhcd.child_id left join location_master lm on lhcd.parent_id = lm.id where parent_loc_type = ''SC'' order by member_with_location.name ASC limit #limit# offset #offset#', true, 'ACTIVE', NULL, true, '71598c1a-e9da-4008-88be-391bdf6f985c'),
    (97070, current_date, 97070, current_date, 'fetch_mo_specialist_patients_list', 'offset,locationId,limit,uniqueHealthId,userId', 'with base_data as( select distinct imm.id, imf.location_id from imt_member imm inner join imt_family imf on imf.family_id = imm.family_id inner join location_hierchy_closer_det lhcd on lhcd.child_id = imf.location_id where ( case when #locationId# is not null then lhcd.parent_id = #locationId# when #uniqueHealthId# is not null then imm.unique_health_id = #uniqueHealthId# else lhcd.parent_id in ( select uul.loc_id from um_user_location uul where uul.user_id = #userId# and state = ''ACTIVE'' ) end ) ), members as( select distinct member_id from ncd_general_screening inner join base_data on base_data.id = ncd_general_screening.member_id union select distinct member_id from ncd_urine_test inner join base_data on base_data.id = ncd_urine_test.member_id union select distinct member_id from ncd_member_ecg_detail inner join base_data on base_data.id = ncd_member_ecg_detail.member_id ), members_data as( select nsm.member_id, cast(max_researcher_date as date) max_researcher_date, cast(max_specialist_date as date) max_specialist_date from ncd_specialist_master nsm left join ( select member_id, max(service_date) as max_researcher_date from ( select member_id, service_date from ncd_member_ecg_detail union all select member_id, service_date from ncd_general_screening union all select member_id, service_date from ncd_urine_test ) as t group by member_id ) as researcher on researcher.member_id = nsm.member_id left join ( select member_id, max(screening_date) as max_specialist_date from ( select member_id, screening_date from ncd_ecg_member_detail union all select member_id, screening_date from ncd_stroke_member_detail union all select member_id, screening_date from ncd_amputation_member_detail union all select member_id, screening_date from ncd_renal_member_detail ) as t group by member_id ) as specialist on specialist.member_id = nsm.member_id ), member_with_location as( select distinct members.member_id, concat( imm.first_name, '' '', imm.middle_name, '' '', imm.last_name ) as name, imm.unique_health_id as "unqieHealthId", date_part(''year'', age(imm.dob)) as age, imm.gender, imf.location_id as locationId, lm.name as village, ( case when ( members_data.max_specialist_date < members_data.max_researcher_date ) then ''#ff4d4d'' when ( nsm.last_ecg_specialist_id is not null or nsm.last_stroke_specialist_id is not null or nsm.last_amputation_specialist_id is not null or nsm.last_renal_specialist_id is not null ) then ( case when nsm.last_cardiologist_id is null or ncd.case_confirmed is true or ncd.satisfactory_image is true then ''green'' else ( case when ncd.case_confirmed is false then ''red'' when ncd.satisfactory_image is false then ''blue'' end ) end ) end ) as color from members inner join imt_member imm on imm.id = members.member_id inner join imt_family imf on imf.family_id = imm.family_id left join location_master lm on lm.id = imf.location_id left join ncd_specialist_master nsm on nsm.member_id = members.member_id left join ncd_cardiologist_data ncd on ncd.id = nsm.last_cardiologist_id and nsm.last_cardiologist_id is not null left join members_data on members_data.member_id = nsm.member_id ) select member_with_location.*, lm.name as "subCenter" from member_with_location left join location_hierchy_closer_det lhcd on member_with_location.locationId = lhcd.child_id left join location_master lm on lhcd.parent_id = lm.id where parent_loc_type = ''SC'' order by member_with_location.name ASC limit #limit# offset #offset#', true, 'ACTIVE', NULL, true, '6ef7dce6-4fe7-4526-9ac8-6ad0f8908be7');


--Insert into mobile menu management
INSERT INTO public.mobile_menu_master (config_json,menu_name,created_on,created_by,modified_on,modified_by) VALUES
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_DATA_QUALITY","order":2},{"mobile_constant":"FHW_SURVEILLANCE","order":3},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":4},{"mobile_constant":"FHW_MY_PEOPLE","order":5},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":6},{"mobile_constant":"FHW_NOTIFICATION","order":7},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":8},{"mobile_constant":"FHW_NCD_SCREENING","order":9},{"mobile_constant":"FHW_NCD_REGISTER","order":10},{"mobile_constant":"LIBRARY","order":11},{"mobile_constant":"ANNOUNCEMENTS","order":12},{"mobile_constant":"FHW_WORK_REGISTER","order":13},{"mobile_constant":"FHW_WORK_STATUS","order":14}]','FHW Menu','2021-02-09 13:33:56.038843',-1,'2021-02-09 13:33:56.038843',-1),
	 ('[{"mobile_constant":"ASHA_FHS","order":1},{"mobile_constant":"ASHA_MY_PEOPLE","order":2},{"mobile_constant":"FHW_SURVEILLANCE","order":3},{"mobile_constant":"ASHA_NOTIFICATION","order":4},{"mobile_constant":"ASHA_HIGH_RISK_BENEFICIARIES","order":5},{"mobile_constant":"ASHA_CBAC_ENTRY","order":6},{"mobile_constant":"ASHA_NCD_REGISTER","order":7},{"mobile_constant":"ASHA_NPCB_SCREENING","order":8},{"mobile_constant":"LIBRARY","order":9},{"mobile_constant":"ANNOUNCEMENTS","order":10},{"mobile_constant":"WORK_LOG","order":11},{"mobile_constant":"ASHA_WORK_REGISTER","order":12}]','ASHA Menu','2021-07-20 14:02:45.343042',-1,'2021-07-20 14:02:45.343042',-1),
	 ('[{"mobile_constant":"LIBRARY","order":1},{"mobile_constant":"WORK_LOG","order":2},{"mobile_constant":"FHW_MY_PEOPLE","order":3},{"mobile_constant":"FHW_NOTIFICATION","order":4},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":5},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":6},{"mobile_constant":"FHW_CFHC","order":7},{"mobile_constant":"FHW_WORK_REGISTER","order":8},{"mobile_constant":"FHW_NCD_SCREENING","order":9},{"mobile_constant":"FHW_NCD_REGISTER","order":10}]','FHW Menu','2021-08-13 13:04:59.11495',69851,'2021-08-13 13:04:59.11495',69851),
	 ('[{"mobile_constant":"FHW_NCD_SCREENING","order":1},{"mobile_constant":"FHW_NCD_REGISTER","order":2},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":3},{"mobile_constant":"LIBRARY","order":4},{"mobile_constant":"ANNOUNCEMENTS","order":5},{"mobile_constant":"FHW_WORK_STATUS","order":6}]','CHO menu','2021-08-24 14:21:57.874566',97058,'2021-08-24 14:21:57.874566',97058),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_DATA_QUALITY","order":2},{"mobile_constant":"FHW_SURVEILLANCE","order":3},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":4},{"mobile_constant":"FHW_MY_PEOPLE","order":5},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":6},{"mobile_constant":"FHW_NOTIFICATION","order":7},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":8},{"mobile_constant":"FHW_NCD_SCREENING","order":9},{"mobile_constant":"FHW_NCD_REGISTER","order":10},{"mobile_constant":"FHW_WORK_REGISTER","order":11},{"mobile_constant":"FHW_WORK_STATUS","order":12}]','FHW Menu','2021-10-06 15:56:58.590083',58981,'2021-10-06 15:56:58.590083',58981),
	 ('[{"mobile_constant":"LIBRARY","order":1},{"mobile_constant":"FHW_NCD_REGISTER","order":2},{"mobile_constant":"WORK_LOG","order":3},{"mobile_constant":"FHW_MY_PEOPLE","order":4},{"mobile_constant":"FHW_NOTIFICATION","order":5},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":6},{"mobile_constant":"FHW_CFHC","order":7},{"mobile_constant":"FHW_WORK_REGISTER","order":8},{"mobile_constant":"FHW_NCD_SCREENING","order":9}]','FHW Menu','2021-09-08 00:40:10.74785',1094,'2021-09-08 00:40:10.74785',1094),
	 ('[{"mobile_constant":"FHW_NCD_REGISTER","order":1},{"mobile_constant":"WORK_LOG","order":2},{"mobile_constant":"FHW_MY_PEOPLE","order":3},{"mobile_constant":"FHW_NOTIFICATION","order":4},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":5},{"mobile_constant":"FHW_CFHC","order":6},{"mobile_constant":"FHW_WORK_REGISTER","order":7},{"mobile_constant":"FHW_NCD_SCREENING","order":8},{"mobile_constant":"LIBRARY","order":9}]','FHW Menu','2021-09-08 00:42:23.009391',1094,'2021-09-08 00:42:23.009391',1094),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_MY_PEOPLE","order":2},{"mobile_constant":"FHW_NOTIFICATION","order":3},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":4},{"mobile_constant":"FHW_NCD_REGISTER","order":5},{"mobile_constant":"FHW_WORK_REGISTER","order":6},{"mobile_constant":"FHW_NCD_SCREENING","order":7},{"mobile_constant":"LIBRARY","order":8},{"mobile_constant":"WORK_LOG","order":9}]','FHW Menu','2021-09-08 14:23:38.757311',1094,'2021-09-08 14:23:38.757311',1094),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_MY_PEOPLE","order":2},{"mobile_constant":"FHW_NOTIFICATION","order":3},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":4},{"mobile_constant":"FHW_NCD_REGISTER","order":5},{"mobile_constant":"FHW_WORK_REGISTER","order":6},{"mobile_constant":"FHW_NCD_SCREENING","order":7},{"mobile_constant":"LIBRARY","order":8},{"mobile_constant":"WORK_LOG","order":9},{"mobile_constant":"ANNOUNCEMENTS","order":10}]','FHW Menu','2021-09-08 15:04:36.065549',1094,'2021-09-08 15:04:36.065549',1094),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_NCD_REGISTER","order":2},{"mobile_constant":"FHW_WORK_REGISTER","order":3},{"mobile_constant":"FHW_WORK_STATUS","order":4},{"mobile_constant":"FHW_DATA_QUALITY","order":5},{"mobile_constant":"FHW_SURVEILLANCE","order":6},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":7},{"mobile_constant":"FHW_MY_PEOPLE","order":8},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":9},{"mobile_constant":"FHW_NOTIFICATION","order":10},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":11},{"mobile_constant":"FHW_NCD_SCREENING","order":12},{"mobile_constant":"FHW_NCD_CONFIRMATION","order":13},{"mobile_constant":"FHW_NCD_WEEKLY_VISIT","order":14}]','FHW Menu','2022-11-21 18:32:10.08321',97058,'2022-11-21 18:32:10.08321',97058),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_NOTIFICATION","order":2},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":3},{"mobile_constant":"FHW_NCD_SCREENING","order":4},{"mobile_constant":"FHW_NCD_CONFIRMATION","order":5},{"mobile_constant":"FHW_NCD_WEEKLY_VISIT","order":6},{"mobile_constant":"FHW_NCD_REGISTER","order":7},{"mobile_constant":"FHW_WORK_REGISTER","order":8},{"mobile_constant":"FHW_WORK_STATUS","order":9},{"mobile_constant":"FHW_DATA_QUALITY","order":10},{"mobile_constant":"FHW_SURVEILLANCE","order":11},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":12},{"mobile_constant":"FHW_MY_PEOPLE","order":13},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":14},{"mobile_constant":"LMS_PROGRESS_REPORT","order":15},{"mobile_constant":"LEARNING_MANAGEMENT_SYSTEM","order":16}]','FHW Menu','2022-11-24 12:38:28.137492',74724,'2022-11-24 12:38:28.137492',74724),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_DATA_QUALITY","order":2},{"mobile_constant":"FHW_SURVEILLANCE","order":3},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":4},{"mobile_constant":"FHW_MY_PEOPLE","order":5},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":6},{"mobile_constant":"LMS_PROGRESS_REPORT","order":7},{"mobile_constant":"LEARNING_MANAGEMENT_SYSTEM","order":8},{"mobile_constant":"FHW_NOTIFICATION","order":9},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":10},{"mobile_constant":"FHW_NCD_SCREENING","order":11},{"mobile_constant":"FHW_NCD_CONFIRMATION","order":12},{"mobile_constant":"FHW_NCD_WEEKLY_VISIT","order":13},{"mobile_constant":"FHW_NCD_REGISTER","order":14},{"mobile_constant":"FHW_WORK_REGISTER","order":15},{"mobile_constant":"FHW_WORK_STATUS","order":16}]','FHW Menu','2023-01-23 17:13:55.855955',74724,'2023-01-23 17:13:55.855955',74724),
	 ('[{"mobile_constant":"ASHA_FHS","order":1},{"mobile_constant":"ANNOUNCEMENTS","order":2},{"mobile_constant":"WORK_LOG","order":3},{"mobile_constant":"ASHA_WORK_REGISTER","order":4},{"mobile_constant":"ASHA_MY_PEOPLE","order":5},{"mobile_constant":"FHW_SURVEILLANCE","order":6},{"mobile_constant":"ASHA_NOTIFICATION","order":7},{"mobile_constant":"ASHA_HIGH_RISK_BENEFICIARIES","order":8},{"mobile_constant":"ASHA_CBAC_ENTRY","order":9},{"mobile_constant":"ASHA_NCD_REGISTER","order":10},{"mobile_constant":"ASHA_NPCB_SCREENING","order":11},{"mobile_constant":"LIBRARY","order":12},{"mobile_constant":"FHW_WORK_STATUS","order":13}]','ASHA Menu','2023-02-27 14:56:16.675291',69851,'2023-02-27 14:56:16.675291',69851),
	 ('[{"mobile_constant":"FHW_CFHC","order":1},{"mobile_constant":"FHW_HIGH_RISK_WOMEN_AND_CHILD","order":2},{"mobile_constant":"FHW_NCD_SCREENING","order":3},{"mobile_constant":"FHW_NCD_CONFIRMATION","order":4},{"mobile_constant":"FHW_NCD_WEEKLY_VISIT","order":5},{"mobile_constant":"FHW_NCD_REGISTER","order":6},{"mobile_constant":"FHW_WORK_REGISTER","order":7},{"mobile_constant":"FHW_WORK_STATUS","order":8},{"mobile_constant":"FHW_DATA_QUALITY","order":9},{"mobile_constant":"FHW_SURVEILLANCE","order":10},{"mobile_constant":"FHW_ASSIGN_FAMILY","order":11},{"mobile_constant":"FHW_MY_PEOPLE","order":12},{"mobile_constant":"FHW_MOBILE_VERIFICATION","order":13},{"mobile_constant":"LMS_PROGRESS_REPORT","order":14},{"mobile_constant":"LEARNING_MANAGEMENT_SYSTEM","order":15},{"mobile_constant":"FHW_NOTIFICATION","order":16},{"mobile_constant":"ABHA_NUMBER","order":17}]','FHW Menu','2023-04-18 10:27:49.749238',97058,'2023-04-18 10:27:49.749238',97058);

--insert into mobile_feature_master
delete from mobile_feature_master where mobile_display_name in ('NCD Screening','NCD Register','ASHA NCD REGISTER','NCD Confirmation','NCD MO Confirmed'
,'NCD Confirmed Cases','NCD CVC Detail');
INSERT INTO public.mobile_feature_master (mobile_constant,feature_name,mobile_display_name,state,created_on,created_by,modified_on,modified_by)
VALUES('FHW_NCD_SCREENING','FHW NCD Screening','NCD Screening','ACTIVE','2021-02-09 13:33:56.038843',-1,'2021-02-09 13:33:56.038843',-1),
	 ('FHW_NCD_REGISTER','FHW NCD Register','NCD Register','ACTIVE','2021-02-09 13:33:56.038843',-1,'2021-02-09 13:33:56.038843',-1),
	 ('ASHA_NCD_REGISTER','ASHA NCD REGISTER','ASHA NCD REGISTER','ACTIVE','2021-09-21 10:35:56.32186',-1,'2021-09-21 10:35:56.32186',-1),
	 ('FHW_NCD_CONFIRMATION','FHW NCD Confirmation','NCD Confirmation','ACTIVE','2022-11-02 22:28:32.465177',-1,'2022-11-02 22:28:32.465177',-1),
	 ('NCD_MO_CONFIRMED','NCD MO Confirmed','NCD MO Confirmed','ACTIVE','2023-08-23 10:21:50.25971',-1,'2023-08-23 10:21:50.25971',-1),
	 ('FHW_NCD_WEEKLY_VISIT','FHW NCD Weekly Visit','NCD Confirmed Cases','ACTIVE','2022-11-21 16:53:44.526364',-1,'2023-10-04 09:47:02.361819',-1),
	 ('FHW_NCD_CVC_DETAIL','FHW NCD CVC Detail','NCD CVC Detail','ACTIVE','2023-10-04 09:47:02.361819',-1,'2023-10-04 09:47:02.361819',-1);

delete from mobile_form_details where form_name in ('NCD_FHW_WEEKLY_HOME','NCD_FHW_DIABETES','NCD_ASHA_CBAC','NCD_FHW_MENTAL_HEALTH',
'NCD_FHW_HEALTH_SCREENING','NCD_FHW_DIABETES_CONFIRMATION','NCD_FHW_WEEKLY_CLINIC','NCD_MO_CONFIRMED','NCD_URINE_TEST','NCD_CVC_CLINIC'
,'NCD_CVC_HOME','NCD_RETINOPATHY_TEST');
insert into mobile_form_details (form_name,file_name,created_on,created_by,modified_on,modified_by)
values ('NCD_FHW_WEEKLY_HOME', 'NCD_FHW_WEEKLY_HOME', now(), -1, now(), -1),
    ('NCD_FHW_DIABETES', 'NCD_FHW_DIABETES', now(), -1, now(), -1),
    ('NCD_ASHA_CBAC', 'NCD_ASHA_CBAC', now(), -1, now(), -1),
    ('NCD_FHW_MENTAL_HEALTH', 'NCD_FHW_MENTAL_HEALTH', now(), -1, now(), -1),
    ('NCD_FHW_HEALTH_SCREENING', 'NCD_FHW_HEALTH_SCREENING', now(), -1, now(), -1),
    ('NCD_FHW_DIABETES_CONFIRMATION', 'NCD_FHW_DIABETES_CONFIRMATION', now(), -1, now(), -1),
    ('NCD_FHW_WEEKLY_CLINIC', 'NCD_FHW_WEEKLY_CLINIC', now(), -1, now(), -1),
    ('NCD_MO_CONFIRMED', 'NCD_MO_CONFIRMED', now(), -1, now(), -1),
    ('NCD_URINE_TEST', 'NCD_URINE_TEST', now(), -1, now(), -1),
    ('NCD_CVC_CLINIC', 'NCD_CVC_CLINIC', now(), -1, now(), -1),
    ('NCD_CVC_HOME', 'NCD_CVC_HOME', now(), -1, now(), -1),
    ('NCD_RETINOPATHY_TEST', 'NCD_RETINOPATHY_TEST', now(), -1, now(), -1);

delete from mobile_form_feature_rel where form_id in (23,24,25,26,27,40,45,46,47,48,49,50,51,52,55,56,57);
INSERT INTO mobile_form_feature_rel (form_id,mobile_constant) VALUES
	 (23,'FHW_NCD_SCREENING'),
	 (24,'FHW_NCD_SCREENING'),
	 (25,'FHW_NCD_SCREENING'),
	 (26,'FHW_NCD_SCREENING'),
	 (27,'FHW_NCD_SCREENING'),
	 (40,'FHW_NCD_SCREENING'),
	 (45,'FHW_NCD_SCREENING'),
	 (46,'FHW_NCD_SCREENING'),
	 (47,'FHW_NCD_SCREENING'),
	 (48,'FHW_NCD_CONFIRMATION'),
	 (49,'FHW_NCD_WEEKLY_VISIT'),
	 (50,'FHW_NCD_WEEKLY_VISIT'),
	 (51,'NCD_MO_CONFIRMED'),
	 (52,'NCD_MO_CONFIRMED'),
	 (55,'FHW_NCD_CVC_DETAIL'),
	 (56,'FHW_NCD_CVC_DETAIL'),
	 (57,'NCD_MO_CONFIRMED');