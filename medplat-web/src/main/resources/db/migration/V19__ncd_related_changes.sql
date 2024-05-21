delete from um_user where user_name = 'asha_test';
delete from um_role_master where id = 24;
INSERT INTO um_role_master (Id,created_by,created_on,modified_by,modified_on,code,description,"name",state,max_position,is_email_mandatory,is_contact_num_mandatory,is_aadhar_num_mandatory,is_convox_id_mandatory,short_name,is_last_name_mandatory,role_type,is_health_infra_mandatory,is_geolocation_mandatory) VALUES
	 (24,1,now(),97067,now(),'ASHA',NULL,'ASHA','ACTIVE',2,false,false,false,false,'ASHA',null,'MOBILE',null,null);

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


-- public.ncd_cancer_screening_master definition

CREATE TABLE IF NOT EXISTS public.ncd_cancer_screening_master (
	id serial4 NOT NULL,
	member_id int4 NULL,
	family_id int4 NULL,
	location_id int4 NULL,
	service_date timestamp NULL,
	oral_id int4 NULL,
	breast_id int4 NULL,
	cervical_id int4 NULL,
	created_by int4 NULL,
	created_on timestamp NULL,
	modified_by int4 NULL,
	modified_on timestamp NULL,
	CONSTRAINT ncd_cancer_screening_master_pkey PRIMARY KEY (id)
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
DROP TRIGGER IF EXISTS update_ncd_status_trigger ON public.ncd_master;

create trigger insert_ncd_status_trigger before
insert
    on
    public.ncd_master for each row execute function update_ncd_status_trigger();
create trigger update_ncd_status_trigger before
update
    on
    public.ncd_master for each row execute function update_ncd_status_trigger();


-- public.ncd_member_breast_detail definition

CREATE TABLE IF NOT EXISTS public.ncd_member_breast_detail (
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
	any_breast_related_symptoms bool NULL,
	lump_in_breast bool NULL,
	size_change bool NULL,
	nipple_shape_and_position_change bool NULL,
	any_retraction_of_nipple bool NULL,
	discharge_from_nipple bool NULL,
	redness_of_skin_over_nipple bool NULL,
	erosions_of_nipple bool NULL,
	remarks text NULL,
	agreed_for_self_breast_exam bool NULL,
	visual_lump_in_breast text NULL,
	swelling_in_armpit_flag text NULL,
	visual_nipple_retraction_distortion text NULL,
	visual_ulceration text NULL,
	discharge_from_nipple_flag bool NULL,
	visual_skin_dimpling_retraction text NULL,
	visual_remarks text NULL,
	lymphadenopathy bool NULL,
	done_by varchar(200) NULL,
	done_on timestamp NULL,
	retraction_of_skin text NULL,
	refferal_done bool NULL,
	refferal_place text NULL,
	ulceration bool NULL,
	nipples_not_on_same_level bool NULL,
	is_axillary bool NULL,
	is_super_clavicular_area bool NULL,
	is_infra_clavicular_area bool NULL,
	referral_id int4 NULL,
	location_id int4 NULL,
	family_id int4 NULL,
	health_infra_id int4 NULL,
	hmis_health_infra_type int4 NULL,
	hmis_health_infra_id int4 NULL,
	consultant_flag bool NULL,
	skin_edema bool NULL,
	visual_skin_retraction varchar NULL,
	swelling_or_lump bool NULL,
	puckering_or_dimpling bool NULL,
	constant_pain_in_breast bool NULL,
	skin_dimpling_retraction_flag bool NULL,
	nipple_retraction_distortion_flag bool NULL,
	lump_in_breast_flag bool NULL,
	visual_discharge_from_nipple text NULL,
	visual_swelling_in_armpit text NULL,
	status varchar(50) NULL,
	does_suffering bool NULL,
	retraction_of_skin_flag bool NULL,
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
	CONSTRAINT ncd_member_breast_detail_t_pkey PRIMARY KEY (id)
);
DROP INDEX IF EXISTS ncd_member_breast_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_breast_detail_t_created_by_idx;
DROP INDEX IF EXISTS ncd_member_breast_detail_t_member_id_idx;
DROP INDEX IF EXISTS ncd_member_breast_detail_t_refferal_done_refferal_place_idx;

CREATE INDEX ncd_member_breast_detail_modified_on_idx ON public.ncd_member_breast_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_breast_detail_t_created_by_idx ON public.ncd_member_breast_detail USING btree (created_by);
CREATE INDEX ncd_member_breast_detail_t_member_id_idx ON public.ncd_member_breast_detail USING btree (member_id);
CREATE INDEX ncd_member_breast_detail_t_refferal_done_refferal_place_idx ON public.ncd_member_breast_detail USING btree (refferal_done, refferal_place);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_breast_hmis_update ON public.ncd_member_breast_detail;

create trigger ncd_breast_hmis_update after
insert
    on
    public.ncd_member_breast_detail for each row execute function ncd_breast_hmis_updation();


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
DROP INDEX IF EXISTS ncd_member_cbac_detail_t_family_id_idx;
DROP INDEX IF EXISTS ncd_member_cbac_detail_t_member_id_idx;

CREATE INDEX ncd_member_cbac_detail_t_family_id_idx ON public.ncd_member_cbac_detail USING btree (family_id);
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
DROP INDEX IF EXISTS ncd_member_cervical_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_created_by_idx;
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_member_id_idx;
DROP INDEX IF EXISTS ncd_member_cervical_detail_t_refferal_done_refferal_place_idx;

CREATE INDEX ncd_member_cervical_detail_modified_on_idx ON public.ncd_member_cervical_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_cervical_detail_t_created_by_idx ON public.ncd_member_cervical_detail USING btree (created_by);
CREATE INDEX ncd_member_cervical_detail_t_member_id_idx ON public.ncd_member_cervical_detail USING btree (member_id);
CREATE INDEX ncd_member_cervical_detail_t_refferal_done_refferal_place_idx ON public.ncd_member_cervical_detail USING btree (refferal_done, refferal_place);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_cervical_hmis_update ON public.ncd_member_cervical_detail;

create trigger ncd_cervical_hmis_update after
insert
    on
    public.ncd_member_cervical_detail for each row execute function ncd_cervical_hmis_updation();

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
DROP INDEX IF EXISTS ncd_member_diabetes_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_created_by_idx;
DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_member_id_idx;
DROP INDEX IF EXISTS ncd_member_diabetes_detail_t_refferal_done_refferal_place_idx;

CREATE INDEX ncd_member_diabetes_detail_modified_on_idx ON public.ncd_member_diabetes_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_diabetes_detail_t_created_by_idx ON public.ncd_member_diabetes_detail USING btree (created_by);
CREATE INDEX ncd_member_diabetes_detail_t_member_id_idx ON public.ncd_member_diabetes_detail USING btree (member_id);
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
DROP TRIGGER IF EXISTS ncd_disease_diagnosis_hmis_update ON public.ncd_member_diseases_diagnosis;

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
DROP TRIGGER IF EXISTS ncd_member_disesase_medicine_update_trigger ON public.ncd_member_disesase_medicine;

create trigger ncd_member_disesase_medicine_update_trigger after
update
    on
    public.ncd_member_disesase_medicine for each row execute function ncd_member_disesase_medicine_update_trigger_func();


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
DROP INDEX IF EXISTS ncd_member_hypertension_detai_refferal_done_refferal_place_idx1;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_modified_on_idx;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_t_created_by_idx;
DROP INDEX IF EXISTS ncd_member_hypertension_detail_t_member_id_idx;

CREATE INDEX ncd_member_hypertension_detai_refferal_done_refferal_place_idx1 ON public.ncd_member_hypertension_detail USING btree (refferal_done, refferal_place);
CREATE INDEX ncd_member_hypertension_detail_modified_on_idx ON public.ncd_member_hypertension_detail USING btree (modified_on DESC NULLS LAST);
CREATE INDEX ncd_member_hypertension_detail_t_created_by_idx ON public.ncd_member_hypertension_detail USING btree (created_by);
CREATE INDEX ncd_member_hypertension_detail_t_member_id_idx ON public.ncd_member_hypertension_detail USING btree (member_id);

-- Table Triggers
DROP TRIGGER IF EXISTS ncd_hypertension_hmis_update ON public.ncd_member_hypertension_detail;

create trigger ncd_hypertension_hmis_update after
insert
    on
    public.ncd_member_hypertension_detail for each row execute function ncd_hypertension_hmis_updation();


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


--Reports related to ncd
	 DELETE FROM REPORT_QUERY_MASTER WHERE uuid = 'daf6615b-bd52-490b-b773-4c6c26f306d5';
	 DELETE FROM REPORT_QUERY_MASTER WHERE id in (127,2071,2554);
     INSERT INTO REPORT_QUERY_MASTER(Id,created_by, created_on, modified_by, modified_on, params, query, returns_result_set, state, uuid)
     VALUES (
     2071,1,  current_date , 1,  current_date , 'from_date,to_date,location_id', 'with location_detail as (
     	select child_id from location_hierchy_closer_det where parent_id = #location_id# and depth = 1
     ),dates as (
     	select to_date(case when ''#from_date#'' = ''null'' then null else ''#from_date#'' end, ''MM-DD-YYYY'') as from_date,
     	to_date(case when ''#to_date#'' = ''null'' then null else ''#to_date#'' end,''MM-DD-YYYY'') + interval ''1 month'' - interval ''1 millisecond'' as to_date
     ),
     users_location as (
         select distinct on (uul.user_id) uul.user_id,
         uul.loc_id, uu.role_id
         from um_user_location uul
         inner join um_user uu on uul.user_id = uu.id
         inner join um_role_master urm on urm.id = uu.role_id
         where uul.loc_id in (select child_id from location_hierchy_closer_det where parent_id = #location_id# )
         and uul.state = ''ACTIVE'' and uu.state = ''ACTIVE'' and urm.name in (''CHO'',''ANM'',''ASHA'')
     ),
     asha_fhw_count as (
     	select location_detail.child_id,
         count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''ASHA'') and ul.user_id is not null) as tot_number_of_asha,
         count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''ASHA'') and ufa.user_id is not null) as number_of_asha_moved_to_production,
         count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''FHW'' or ur.name = ''ANM'') and ul.user_id is not null) as tot_number_of_anm,
     	count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''FHW'' or ur.name = ''ANM'') and ufa.user_id is not null) as number_of_anm_moved_to_production,
          count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.name = ''CHO'') and ul.user_id is not null) as tot_number_of_cho,
     	count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.name = ''CHO'') and ufa.user_id is not null) as number_of_cho_moved_to_production
     	from location_detail
     	inner join location_hierchy_closer_det on location_detail.child_id = location_hierchy_closer_det.parent_id
     	inner join users_location ul on location_hierchy_closer_det.child_id = ul.loc_id
         left join user_form_access ufa on ul.user_id = ufa.user_id and ufa.state = ''MOVE_TO_PRODUCTION'' and ufa.form_code = ''NCD''
     	group by location_detail.child_id
     )
     ,details as (
     	select location_detail.child_id,
     	coalesce(sum(member_enrolled),0) as member_enrolled,
     	coalesce(sum(member_30_plus),0) as member_30_plus,
     	coalesce(sum(number_of_mo),0) as number_of_mo,
     	coalesce(sum(number_of_active_mo),0) as number_of_active_mo
     	from location_detail
     	inner join location_hierchy_closer_det on location_hierchy_closer_det.parent_id = location_detail.child_id
     	inner join ncd_analytics_detail on ncd_analytics_detail.location_id = location_hierchy_closer_det.child_id
     	inner join dates on dates.from_date = ncd_analytics_detail.month_year
     	group by location_detail.child_id
     ),analytics as (
     	select location_detail.child_id,
     	coalesce(sum(number_of_cbac_form_filled),0) as number_of_cbac_form_filled,
     	coalesce(sum(number_of_member_at_risk),0) as number_of_member_at_risk,
     	coalesce(sum(fhw_screened_diabetes_male),0) as fhw_screened_diabetes_male,
     	coalesce(sum(fhw_screened_diabetes_female),0) as fhw_screened_diabetes_female,
     	coalesce(sum(fhw_screened_hypertension_male),0) as fhw_screened_hypertension_male,
     	coalesce(sum(fhw_screened_hypertension_female),0) as fhw_screened_hypertension_female,
     	coalesce(sum(fhw_screened_oral_male),0) as fhw_screened_oral_male,
     	coalesce(sum(fhw_screened_oral_female),0) as fhw_screened_oral_female,
     	coalesce(sum(fhw_screened_breast_female),0) as fhw_screened_breast_female,
     	coalesce(sum(fhw_screened_cervical_female),0) as fhw_screened_cervical_female,
     	coalesce(sum(no_abnormally_detected_male),0) as no_abnormally_detected_male,
     	coalesce(sum(no_abnormally_detected_female),0) as no_abnormally_detected_female,
     	coalesce(sum(fhw_referred_diabetes_male),0) as fhw_referred_diabetes_male,
     	coalesce(sum(fhw_referred_diabetes_female),0) as fhw_referred_diabetes_female,
     	coalesce(sum(fhw_referred_hypertension_male),0) as fhw_referred_hypertension_male,
     	coalesce(sum(fhw_referred_hypertension_female),0) as fhw_referred_hypertension_female,
     	coalesce(sum(fhw_referred_oral_male),0) as fhw_referred_oral_male,
     	coalesce(sum(fhw_referred_oral_female),0) as fhw_referred_oral_female,
     	coalesce(sum(fhw_referred_breast_female),0) as fhw_referred_breast_female,
     	coalesce(sum(fhw_referred_cervical_female),0) as fhw_referred_cervical_female,
     	coalesce(sum(mo_examined_diabetes_male),0) as mo_examined_diabetes_male,
     	coalesce(sum(mo_examined_diabetes_female),0) as mo_examined_diabetes_female,
     	coalesce(sum(mo_examined_hypertension_male),0) as mo_examined_hypertension_male,
     	coalesce(sum(mo_examined_hypertension_female),0) as mo_examined_hypertension_female,
     	coalesce(sum(mo_examined_oral_male),0) as mo_examined_oral_male,
     	coalesce(sum(mo_examined_oral_female),0) as mo_examined_oral_female,
     	coalesce(sum(mo_examined_breast_female),0) as mo_examined_breast_female,
     	coalesce(sum(mo_examined_cervical_female),0) as mo_examined_cervical_female,
     	coalesce(sum(mo_diagnosed_diabetes_male),0) as mo_diagnosed_diabetes_male,
     	coalesce(sum(mo_diagnosed_diabetes_female),0) as mo_diagnosed_diabetes_female,
     	coalesce(sum(mo_diagnosed_hypertension_male),0) as mo_diagnosed_hypertension_male,
     	coalesce(sum(mo_diagnosed_hypertension_female),0) as mo_diagnosed_hypertension_female,
     	coalesce(sum(mo_diagnosed_oral_male),0) as mo_diagnosed_oral_male,
     	coalesce(sum(mo_diagnosed_oral_female),0) as mo_diagnosed_oral_female,
     	coalesce(sum(mo_diagnosed_breast_female),0) as mo_diagnosed_breast_female,
     	coalesce(sum(mo_diagnosed_cervical_female),0) as mo_diagnosed_cervical_female,
     	coalesce(sum(under_treatment_diabetes_male),0) as under_treatment_diabetes_male,
     	coalesce(sum(under_treatment_diabetes_female),0) as under_treatment_diabetes_female,
     	coalesce(sum(under_treatment_hypertension_male),0) as under_treatment_hypertension_male,
     	coalesce(sum(under_treatment_hypertension_female),0) as under_treatment_hypertension_female,
     	coalesce(sum(under_treatment_oral_male),0) as under_treatment_oral_male,
     	coalesce(sum(under_treatment_oral_female),0) as under_treatment_oral_female,
     	coalesce(sum(under_treatment_breast_female),0) as under_treatment_breast_female,
     	coalesce(sum(under_treatment_cervical_female),0) as under_treatment_cervical_female,
     	coalesce(sum(secondary_referred_diabetes_male),0) as secondary_referred_diabetes_male,
     	coalesce(sum(secondary_referred_diabetes_female),0) as secondary_referred_diabetes_female,
     	coalesce(sum(secondary_referred_hypertension_male),0) as secondary_referred_hypertension_male,
     	coalesce(sum(secondary_referred_hypertension_female),0) as secondary_referred_hypertension_female,
     	coalesce(sum(secondary_referred_oral_male),0) as secondary_referred_oral_male,
     	coalesce(sum(secondary_referred_oral_female),0) as secondary_referred_oral_female,
     	coalesce(sum(secondary_referred_breast_female),0) as secondary_referred_breast_female,
     	coalesce(sum(secondary_referred_cervical_female),0) as secondary_referred_cervical_female
     	from location_detail
     	inner join location_hierchy_closer_det on location_hierchy_closer_det.parent_id = location_detail.child_id
     	inner join ncd_analytics_detail on ncd_analytics_detail.location_id = location_hierchy_closer_det.child_id
     	inner join dates on ncd_analytics_detail.month_year between dates.from_date and dates.to_date
     	group by location_detail.child_id
     )
     select
     	row_number() over(order by lower(lm.english_name)) as "Sr.no",
     	lm.english_name as "Location",
     	lm.id as hidden_location_id,
     	member_enrolled as "Population enrollment",
     	member_30_plus as "Population ≥ 30 years",
     	asha_fhw_count.tot_number_of_asha as "Total number of ASHAs",
     	number_of_asha_moved_to_production as "Number of ASHAs moved to production",
     	number_of_cbac_form_filled as "Number of CBAC forms filled",
     	number_of_member_at_risk as "Number of Members At Risk",
     	asha_fhw_count.tot_number_of_anm as "Total number of ANMs",
     	asha_fhw_count.number_of_anm_moved_to_production as "Number of ANMs moved to production",
     	asha_fhw_count.tot_number_of_cho as "Total number of CHOs",
     	-- asha_fhw_count.number_of_cho_moved_to_production as "Number of CHOs moved to production",
     	fhw_screened_diabetes_male as "Screening counts for Diabetes Male",
     	fhw_screened_diabetes_female as "Screening counts for Diabetes Female",
     	fhw_screened_hypertension_male as "Screening counts for Hypertension Male",
     	fhw_screened_hypertension_female as "Screening counts for Hypertension Female",
     	fhw_screened_oral_male as "Screening counts for Oral Cancer Male",
     	fhw_screened_oral_female as "Screening counts for Oral Cancer Female",
     	fhw_screened_breast_female as "Screening counts for Breast Cancer Female",
     	fhw_screened_cervical_female as "Screening counts for Cervical Cancer Female",
     	no_abnormally_detected_male as "Number of No Abnormality Detected Male",
     	no_abnormally_detected_female as "Number of No Abnormality Detected Female",
     	fhw_referred_diabetes_male as "Referred to PHC Diabetes Male",
     	fhw_referred_diabetes_female as "Referred to PHC Diabetes Female",
     	fhw_referred_hypertension_male as "Referred to PHC Hypertension Male",
     	fhw_referred_hypertension_female as "Referred to PHC Hypertension Female",
     	fhw_referred_oral_male as "Referred to PHC Oral Cancer Male",
     	fhw_referred_oral_female as "Referred to PHC Oral Cancer Female",
     	fhw_referred_breast_female as "Referred to PHC Breast Cancer Female",
     	fhw_referred_cervical_female as "Referred to PHC Cervical Cancer Female",
     	number_of_mo as "Number of MO",
     	number_of_active_mo as "Number of Active Mos",
     	mo_examined_diabetes_male as "Examined at PHC Diabetes Male",
     	mo_examined_diabetes_female as "Examined at PHC Diabetes Female",
     	mo_examined_hypertension_male as "Examined at PHC Hypertension Male",
     	mo_examined_hypertension_female as "Examined at PHC Hypertension Female",
     	mo_examined_oral_male as "Examined at PHC Oral Cancer Male",
     	mo_examined_oral_female as "Examined at PHC Oral Cancer Female",
     	mo_examined_breast_female as "Examined at PHC Breast Cancer Female",
     	mo_examined_cervical_female as "Examined at PHC Cervical Cancer Female",
     	mo_diagnosed_diabetes_male as "Diagnosed Cases Diabetes Male",
     	mo_diagnosed_diabetes_female as "Diagnosed Cases Diabetes Female",
     	mo_diagnosed_hypertension_male as "Diagnosed Cases Hypertension Male",
     	mo_diagnosed_hypertension_female as "Diagnosed Cases Hypertension Female",
     	mo_diagnosed_oral_male as "Diagnosed Cases Oral Cancer Male",
     	mo_diagnosed_oral_female as "Diagnosed Cases Oral Cancer Female",
     	mo_diagnosed_breast_female as "Diagnosed Cases Breast Cancer Female",
     	mo_diagnosed_cervical_female as "Diagnosed Cases Cervical Cancer Female",
     	under_treatment_diabetes_male as "Under Treatment Diabetes Male",
     	under_treatment_diabetes_female as "Under Treatment Diabetes Female",
     	under_treatment_hypertension_male as "Under Treatment Hypertension Male",
     	under_treatment_hypertension_female as "Under Treatment Hypertension Female",
     	under_treatment_oral_male as "Under Treatment Oral Cancer Male",
     	under_treatment_oral_female as "Under Treatment Oral Cancer Female",
     	under_treatment_breast_female as "Under Treatment Breast Cancer Female",
     	under_treatment_cervical_female as "Under Treatment Cervical Cancer Female",
     	secondary_referred_diabetes_male as "Referred to Secondary Diabetes Male",
     	secondary_referred_diabetes_female as "Referred to Secondary Diabetes Female",
     	secondary_referred_hypertension_male as "Referred to Secondary Hypertension Male",
     	secondary_referred_hypertension_female as "Referred to Secondary Hypertension Female",
     	secondary_referred_oral_male as "Referred to Secondary Oral Cancer Male",
     	secondary_referred_oral_female as "Referred to Secondary Oral Cancer Female",
     	secondary_referred_breast_female as "Referred to Secondary Breast Cancer Female",
     	secondary_referred_cervical_female as "Referred to Secondary Cervical Cancer Female"
     from
     	location_detail
     	left join location_master lm on lm.id = location_detail.child_id
     	left join asha_fhw_count on asha_fhw_count.child_id = location_detail.child_id
     	left join analytics on analytics.child_id = location_detail.child_id
     	left join details on details.child_id = location_detail.child_id

     union all

     select
     	null,
     	''<b>Total<b>'',
     	#location_id#,
     	sum(member_enrolled),
     	sum(member_30_plus),
     	sum(tot_number_of_asha),
     	sum(number_of_asha_moved_to_production),
     	sum(number_of_cbac_form_filled),
     	sum(number_of_member_at_risk),
     	sum(tot_number_of_anm),
     	sum(number_of_anm_moved_to_production),
     	sum(tot_number_of_cho),
     	-- sum(number_of_cho_moved_to_production),
     	sum(fhw_screened_diabetes_male),
     	sum(fhw_screened_diabetes_female),
     	sum(fhw_screened_hypertension_male),
     	sum(fhw_screened_hypertension_female),
     	sum(fhw_screened_oral_male),
     	sum(fhw_screened_oral_female),
     	sum(fhw_screened_breast_female),
     	sum(fhw_screened_cervical_female),
     	sum(no_abnormally_detected_male),
     	sum(no_abnormally_detected_female),
     	sum(fhw_referred_diabetes_male),
     	sum(fhw_referred_diabetes_female),
     	sum(fhw_referred_hypertension_male),
     	sum(fhw_referred_hypertension_female),
     	sum(fhw_referred_oral_male),
     	sum(fhw_referred_oral_female),
     	sum(fhw_referred_breast_female),
     	sum(fhw_referred_cervical_female),
     	sum(number_of_mo),
     	sum(number_of_active_mo),
     	sum(mo_examined_diabetes_male),
     	sum(mo_examined_diabetes_female),
     	sum(mo_examined_hypertension_male),
     	sum(mo_examined_hypertension_female),
     	sum(mo_examined_oral_male),
     	sum(mo_examined_oral_female),
     	sum(mo_examined_breast_female),
     	sum(mo_examined_cervical_female),
     	sum(mo_diagnosed_diabetes_male),
     	sum(mo_diagnosed_diabetes_female),
     	sum(mo_diagnosed_hypertension_male),
     	sum(mo_diagnosed_hypertension_female),
     	sum(mo_diagnosed_oral_male),
     	sum(mo_diagnosed_oral_female),
     	sum(mo_diagnosed_breast_female),
     	sum(mo_diagnosed_cervical_female),
     	sum(under_treatment_diabetes_male),
     	sum(under_treatment_diabetes_female),
     	sum(under_treatment_hypertension_male),
     	sum(under_treatment_hypertension_female),
     	sum(under_treatment_oral_male),
     	sum(under_treatment_oral_female),
     	sum(under_treatment_breast_female),
     	sum(under_treatment_cervical_female),
     	sum(secondary_referred_diabetes_male),
     	sum(secondary_referred_diabetes_female),
     	sum(secondary_referred_hypertension_male),
     	sum(secondary_referred_hypertension_female),
     	sum(secondary_referred_oral_male),
     	sum(secondary_referred_oral_female),
     	sum(secondary_referred_breast_female),
     	sum(secondary_referred_cervical_female)
     from
     	location_detail
     	left join location_master lm on lm.id = location_detail.child_id
     	left join asha_fhw_count on asha_fhw_count.child_id = location_detail.child_id
     	left join analytics on analytics.child_id = location_detail.child_id
     	left join details on details.child_id = location_detail.child_id', true, 'ACTIVE', 'daf6615b-bd52-490b-b773-4c6c26f306d5');

     DELETE FROM REPORT_MASTER WHERE uuid ='04737cee-18b2-4d04-bb14-0d9ee73c67d1';

     INSERT INTO REPORT_MASTER(Id, report_name, file_name, active, report_type, modified_on, created_by, created_on, modified_by, config_json, code, uuid)
     VALUES (
     680,'NCD Progress tracking report',  null,  true, 'DYNAMIC',  current_date , 409,  current_date , 409, '{"layout":"dynamicReport1","containers":{"tableContainer":[{"query":"with location_detail as (\n\tselect child_id from location_hierchy_closer_det where parent_id = #location_id# and depth = 1\n),dates as (\n\tselect to_date(case when ''#from_date#'' = ''null'' then null else ''#from_date#'' end, ''MM-DD-YYYY'') as from_date,\n\tto_date(case when ''#to_date#'' = ''null'' then null else ''#to_date#'' end,''MM-DD-YYYY'') + interval ''1 month'' - interval ''1 millisecond'' as to_date\n),\nusers_location as (\n    select distinct on (uul.user_id) uul.user_id,\n    uul.loc_id, uu.role_id\n    from um_user_location uul\n    inner join um_user uu on uul.user_id = uu.id\n    inner join um_role_master urm on urm.id = uu.role_id\n    where uul.loc_id in (select child_id from location_hierchy_closer_det where parent_id = #location_id# )\n    and uul.state = ''ACTIVE'' and uu.state = ''ACTIVE'' and urm.name in (''CHO'',''ANM'',''ASHA'')\n),\nasha_fhw_count as (\n\tselect location_detail.child_id,\n    count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''ASHA'') and ul.user_id is not null) as tot_number_of_asha,\n    count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''ASHA'') and ufa.user_id is not null) as number_of_asha_moved_to_production,\n    count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''FHW'' or ur.name = ''ANM'') and ul.user_id is not null) as tot_number_of_anm,\n\tcount(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.code = ''FHW'' or ur.name = ''ANM'') and ufa.user_id is not null) as number_of_anm_moved_to_production,\n     count(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.name = ''CHO'') and ul.user_id is not null) as tot_number_of_cho,\n\tcount(ul.user_id) filter (where ul.role_id = (select id from um_role_master ur where ur.name = ''CHO'') and ufa.user_id is not null) as number_of_cho_moved_to_production\n\tfrom location_detail\n\tinner join location_hierchy_closer_det on location_detail.child_id = location_hierchy_closer_det.parent_id\n\tinner join users_location ul on location_hierchy_closer_det.child_id = ul.loc_id\n    left join user_form_access ufa on ul.user_id = ufa.user_id and ufa.state = ''MOVE_TO_PRODUCTION'' and ufa.form_code = ''NCD''\n\tgroup by location_detail.child_id\n)\n,details as (\n\tselect location_detail.child_id,\n\tcoalesce(sum(member_enrolled),0) as member_enrolled,\n\tcoalesce(sum(member_30_plus),0) as member_30_plus,\n\tcoalesce(sum(number_of_mo),0) as number_of_mo,\n\tcoalesce(sum(number_of_active_mo),0) as number_of_active_mo\n\tfrom location_detail\n\tinner join location_hierchy_closer_det on location_hierchy_closer_det.parent_id = location_detail.child_id\n\tinner join ncd_analytics_detail on ncd_analytics_detail.location_id = location_hierchy_closer_det.child_id\n\tinner join dates on dates.from_date = ncd_analytics_detail.month_year\n\tgroup by location_detail.child_id\n),analytics as (\n\tselect location_detail.child_id,\n\tcoalesce(sum(number_of_cbac_form_filled),0) as number_of_cbac_form_filled,\n\tcoalesce(sum(number_of_member_at_risk),0) as number_of_member_at_risk,\n\tcoalesce(sum(fhw_screened_diabetes_male),0) as fhw_screened_diabetes_male,\n\tcoalesce(sum(fhw_screened_diabetes_female),0) as fhw_screened_diabetes_female,\n\tcoalesce(sum(fhw_screened_hypertension_male),0) as fhw_screened_hypertension_male,\n\tcoalesce(sum(fhw_screened_hypertension_female),0) as fhw_screened_hypertension_female,\n\tcoalesce(sum(fhw_screened_oral_male),0) as fhw_screened_oral_male,\n\tcoalesce(sum(fhw_screened_oral_female),0) as fhw_screened_oral_female,\n\tcoalesce(sum(fhw_screened_breast_female),0) as fhw_screened_breast_female,\n\tcoalesce(sum(fhw_screened_cervical_female),0) as fhw_screened_cervical_female,\n\tcoalesce(sum(no_abnormally_detected_male),0) as no_abnormally_detected_male,\n\tcoalesce(sum(no_abnormally_detected_female),0) as no_abnormally_detected_female,\n\tcoalesce(sum(fhw_referred_diabetes_male),0) as fhw_referred_diabetes_male,\n\tcoalesce(sum(fhw_referred_diabetes_female),0) as fhw_referred_diabetes_female,\n\tcoalesce(sum(fhw_referred_hypertension_male),0) as fhw_referred_hypertension_male,\n\tcoalesce(sum(fhw_referred_hypertension_female),0) as fhw_referred_hypertension_female,\n\tcoalesce(sum(fhw_referred_oral_male),0) as fhw_referred_oral_male,\n\tcoalesce(sum(fhw_referred_oral_female),0) as fhw_referred_oral_female,\n\tcoalesce(sum(fhw_referred_breast_female),0) as fhw_referred_breast_female,\n\tcoalesce(sum(fhw_referred_cervical_female),0) as fhw_referred_cervical_female,\n\tcoalesce(sum(mo_examined_diabetes_male),0) as mo_examined_diabetes_male,\n\tcoalesce(sum(mo_examined_diabetes_female),0) as mo_examined_diabetes_female,\n\tcoalesce(sum(mo_examined_hypertension_male),0) as mo_examined_hypertension_male,\n\tcoalesce(sum(mo_examined_hypertension_female),0) as mo_examined_hypertension_female,\n\tcoalesce(sum(mo_examined_oral_male),0) as mo_examined_oral_male,\n\tcoalesce(sum(mo_examined_oral_female),0) as mo_examined_oral_female,\n\tcoalesce(sum(mo_examined_breast_female),0) as mo_examined_breast_female,\n\tcoalesce(sum(mo_examined_cervical_female),0) as mo_examined_cervical_female,\n\tcoalesce(sum(mo_diagnosed_diabetes_male),0) as mo_diagnosed_diabetes_male,\n\tcoalesce(sum(mo_diagnosed_diabetes_female),0) as mo_diagnosed_diabetes_female,\n\tcoalesce(sum(mo_diagnosed_hypertension_male),0) as mo_diagnosed_hypertension_male,\n\tcoalesce(sum(mo_diagnosed_hypertension_female),0) as mo_diagnosed_hypertension_female,\n\tcoalesce(sum(mo_diagnosed_oral_male),0) as mo_diagnosed_oral_male,\n\tcoalesce(sum(mo_diagnosed_oral_female),0) as mo_diagnosed_oral_female,\n\tcoalesce(sum(mo_diagnosed_breast_female),0) as mo_diagnosed_breast_female,\n\tcoalesce(sum(mo_diagnosed_cervical_female),0) as mo_diagnosed_cervical_female,\n\tcoalesce(sum(under_treatment_diabetes_male),0) as under_treatment_diabetes_male,\n\tcoalesce(sum(under_treatment_diabetes_female),0) as under_treatment_diabetes_female,\n\tcoalesce(sum(under_treatment_hypertension_male),0) as under_treatment_hypertension_male,\n\tcoalesce(sum(under_treatment_hypertension_female),0) as under_treatment_hypertension_female,\n\tcoalesce(sum(under_treatment_oral_male),0) as under_treatment_oral_male,\n\tcoalesce(sum(under_treatment_oral_female),0) as under_treatment_oral_female,\n\tcoalesce(sum(under_treatment_breast_female),0) as under_treatment_breast_female,\n\tcoalesce(sum(under_treatment_cervical_female),0) as under_treatment_cervical_female,\n\tcoalesce(sum(secondary_referred_diabetes_male),0) as secondary_referred_diabetes_male,\n\tcoalesce(sum(secondary_referred_diabetes_female),0) as secondary_referred_diabetes_female,\n\tcoalesce(sum(secondary_referred_hypertension_male),0) as secondary_referred_hypertension_male,\n\tcoalesce(sum(secondary_referred_hypertension_female),0) as secondary_referred_hypertension_female,\n\tcoalesce(sum(secondary_referred_oral_male),0) as secondary_referred_oral_male,\n\tcoalesce(sum(secondary_referred_oral_female),0) as secondary_referred_oral_female,\n\tcoalesce(sum(secondary_referred_breast_female),0) as secondary_referred_breast_female,\n\tcoalesce(sum(secondary_referred_cervical_female),0) as secondary_referred_cervical_female\n\tfrom location_detail\n\tinner join location_hierchy_closer_det on location_hierchy_closer_det.parent_id = location_detail.child_id\n\tinner join ncd_analytics_detail on ncd_analytics_detail.location_id = location_hierchy_closer_det.child_id\n\tinner join dates on ncd_analytics_detail.month_year between dates.from_date and dates.to_date\n\tgroup by location_detail.child_id\n)\nselect \n\trow_number() over(order by lower(lm.english_name)) as \"Sr.no\",\n\tlm.english_name as \"Location\",\n\tlm.id as hidden_location_id,\n\tmember_enrolled as \"Population enrollment\",\n\tmember_30_plus as \"Population ≥ 30 years\",\n\tasha_fhw_count.tot_number_of_asha as \"Total number of ASHAs\",\n\tnumber_of_asha_moved_to_production as \"Number of ASHAs moved to production\",\n\tnumber_of_cbac_form_filled as \"Number of CBAC forms filled\",\n\tnumber_of_member_at_risk as \"Number of Members At Risk\",\n\tasha_fhw_count.tot_number_of_anm as \"Total number of ANMs\",\n\tasha_fhw_count.number_of_anm_moved_to_production as \"Number of ANMs moved to production\",\n\tasha_fhw_count.tot_number_of_cho as \"Total number of CHOs\",\n\t-- asha_fhw_count.number_of_cho_moved_to_production as \"Number of CHOs moved to production\",\n\tfhw_screened_diabetes_male as \"Screening counts for Diabetes Male\",\n\tfhw_screened_diabetes_female as \"Screening counts for Diabetes Female\",\n\tfhw_screened_hypertension_male as \"Screening counts for Hypertension Male\",\n\tfhw_screened_hypertension_female as \"Screening counts for Hypertension Female\",\n\tfhw_screened_oral_male as \"Screening counts for Oral Cancer Male\",\n\tfhw_screened_oral_female as \"Screening counts for Oral Cancer Female\",\n\tfhw_screened_breast_female as \"Screening counts for Breast Cancer Female\",\n\tfhw_screened_cervical_female as \"Screening counts for Cervical Cancer Female\",\n\tno_abnormally_detected_male as \"Number of No Abnormality Detected Male\",\n\tno_abnormally_detected_female as \"Number of No Abnormality Detected Female\",\n\tfhw_referred_diabetes_male as \"Referred to PHC Diabetes Male\",\n\tfhw_referred_diabetes_female as \"Referred to PHC Diabetes Female\",\n\tfhw_referred_hypertension_male as \"Referred to PHC Hypertension Male\",\n\tfhw_referred_hypertension_female as \"Referred to PHC Hypertension Female\",\n\tfhw_referred_oral_male as \"Referred to PHC Oral Cancer Male\",\n\tfhw_referred_oral_female as \"Referred to PHC Oral Cancer Female\",\n\tfhw_referred_breast_female as \"Referred to PHC Breast Cancer Female\",\n\tfhw_referred_cervical_female as \"Referred to PHC Cervical Cancer Female\",\n\tnumber_of_mo as \"Number of MO\",\n\tnumber_of_active_mo as \"Number of Active Mos\",\n\tmo_examined_diabetes_male as \"Examined at PHC Diabetes Male\",\n\tmo_examined_diabetes_female as \"Examined at PHC Diabetes Female\",\n\tmo_examined_hypertension_male as \"Examined at PHC Hypertension Male\",\n\tmo_examined_hypertension_female as \"Examined at PHC Hypertension Female\",\n\tmo_examined_oral_male as \"Examined at PHC Oral Cancer Male\",\n\tmo_examined_oral_female as \"Examined at PHC Oral Cancer Female\",\n\tmo_examined_breast_female as \"Examined at PHC Breast Cancer Female\",\n\tmo_examined_cervical_female as \"Examined at PHC Cervical Cancer Female\",\n\tmo_diagnosed_diabetes_male as \"Diagnosed Cases Diabetes Male\",\n\tmo_diagnosed_diabetes_female as \"Diagnosed Cases Diabetes Female\",\n\tmo_diagnosed_hypertension_male as \"Diagnosed Cases Hypertension Male\",\n\tmo_diagnosed_hypertension_female as \"Diagnosed Cases Hypertension Female\",\n\tmo_diagnosed_oral_male as \"Diagnosed Cases Oral Cancer Male\",\n\tmo_diagnosed_oral_female as \"Diagnosed Cases Oral Cancer Female\",\n\tmo_diagnosed_breast_female as \"Diagnosed Cases Breast Cancer Female\",\n\tmo_diagnosed_cervical_female as \"Diagnosed Cases Cervical Cancer Female\",\n\tunder_treatment_diabetes_male as \"Under Treatment Diabetes Male\",\n\tunder_treatment_diabetes_female as \"Under Treatment Diabetes Female\",\n\tunder_treatment_hypertension_male as \"Under Treatment Hypertension Male\",\n\tunder_treatment_hypertension_female as \"Under Treatment Hypertension Female\",\n\tunder_treatment_oral_male as \"Under Treatment Oral Cancer Male\",\n\tunder_treatment_oral_female as \"Under Treatment Oral Cancer Female\",\n\tunder_treatment_breast_female as \"Under Treatment Breast Cancer Female\",\n\tunder_treatment_cervical_female as \"Under Treatment Cervical Cancer Female\",\n\tsecondary_referred_diabetes_male as \"Referred to Secondary Diabetes Male\",\n\tsecondary_referred_diabetes_female as \"Referred to Secondary Diabetes Female\",\n\tsecondary_referred_hypertension_male as \"Referred to Secondary Hypertension Male\",\n\tsecondary_referred_hypertension_female as \"Referred to Secondary Hypertension Female\",\n\tsecondary_referred_oral_male as \"Referred to Secondary Oral Cancer Male\",\n\tsecondary_referred_oral_female as \"Referred to Secondary Oral Cancer Female\",\n\tsecondary_referred_breast_female as \"Referred to Secondary Breast Cancer Female\",\n\tsecondary_referred_cervical_female as \"Referred to Secondary Cervical Cancer Female\"\nfrom \n\tlocation_detail\n\tleft join location_master lm on lm.id = location_detail.child_id\n\tleft join asha_fhw_count on asha_fhw_count.child_id = location_detail.child_id\n\tleft join analytics on analytics.child_id = location_detail.child_id\n\tleft join details on details.child_id = location_detail.child_id\n\nunion all\n\nselect \n\tnull,\n\t''<b>Total<b>'',\n\t#location_id#,\n\tsum(member_enrolled),\n\tsum(member_30_plus),\n\tsum(tot_number_of_asha),\n\tsum(number_of_asha_moved_to_production),\n\tsum(number_of_cbac_form_filled),\n\tsum(number_of_member_at_risk),\n\tsum(tot_number_of_anm),\n\tsum(number_of_anm_moved_to_production),\n\tsum(tot_number_of_cho),\n\t-- sum(number_of_cho_moved_to_production),\n\tsum(fhw_screened_diabetes_male),\n\tsum(fhw_screened_diabetes_female),\n\tsum(fhw_screened_hypertension_male),\n\tsum(fhw_screened_hypertension_female),\n\tsum(fhw_screened_oral_male),\n\tsum(fhw_screened_oral_female),\n\tsum(fhw_screened_breast_female),\n\tsum(fhw_screened_cervical_female),\n\tsum(no_abnormally_detected_male),\n\tsum(no_abnormally_detected_female),\n\tsum(fhw_referred_diabetes_male),\n\tsum(fhw_referred_diabetes_female),\n\tsum(fhw_referred_hypertension_male),\n\tsum(fhw_referred_hypertension_female),\n\tsum(fhw_referred_oral_male),\n\tsum(fhw_referred_oral_female),\n\tsum(fhw_referred_breast_female),\n\tsum(fhw_referred_cervical_female),\n\tsum(number_of_mo),\n\tsum(number_of_active_mo),\n\tsum(mo_examined_diabetes_male),\n\tsum(mo_examined_diabetes_female),\n\tsum(mo_examined_hypertension_male),\n\tsum(mo_examined_hypertension_female),\n\tsum(mo_examined_oral_male),\n\tsum(mo_examined_oral_female),\n\tsum(mo_examined_breast_female),\n\tsum(mo_examined_cervical_female),\n\tsum(mo_diagnosed_diabetes_male),\n\tsum(mo_diagnosed_diabetes_female),\n\tsum(mo_diagnosed_hypertension_male),\n\tsum(mo_diagnosed_hypertension_female),\n\tsum(mo_diagnosed_oral_male),\n\tsum(mo_diagnosed_oral_female),\n\tsum(mo_diagnosed_breast_female),\n\tsum(mo_diagnosed_cervical_female),\n\tsum(under_treatment_diabetes_male),\n\tsum(under_treatment_diabetes_female),\n\tsum(under_treatment_hypertension_male),\n\tsum(under_treatment_hypertension_female),\n\tsum(under_treatment_oral_male),\n\tsum(under_treatment_oral_female),\n\tsum(under_treatment_breast_female),\n\tsum(under_treatment_cervical_female),\n\tsum(secondary_referred_diabetes_male),\n\tsum(secondary_referred_diabetes_female),\n\tsum(secondary_referred_hypertension_male),\n\tsum(secondary_referred_hypertension_female),\n\tsum(secondary_referred_oral_male),\n\tsum(secondary_referred_oral_female),\n\tsum(secondary_referred_breast_female),\n\tsum(secondary_referred_cervical_female)\nfrom \n\tlocation_detail\n\tleft join location_master lm on lm.id = location_detail.child_id\n\tleft join asha_fhw_count on asha_fhw_count.child_id = location_detail.child_id\n\tleft join analytics on analytics.child_id = location_detail.child_id\n\tleft join details on details.child_id = location_detail.child_id","queryId":2071,"fieldName":"tableField","queryUUID":"daf6615b-bd52-490b-b773-4c6c26f306d5","queryParams":"from_date,to_date,location_id","htmlData":"#table#\n<br/>\n<b><span style=\"color: red;\">Note : </span><br/>\n<span style=\"color: red;\">The \"Number of CBAC forms filled\" count is for overall population.</span><br/></b>"}],"fieldsContainer":[{"index":0,"fieldName":"location_id","fieldType":"location","queryUUID":null,"displayName":"Location","isMandatory":true,"fetchUptoLevel":"7","queryUUIDForParam":null,"requiredUptoLevel":1,"fetchAccordingToUserAoi":true},{"index":1,"fieldName":"date","fieldType":"onlyMonthFromTo","queryUUID":null,"displayName":"Month","isMandatory":true,"queryUUIDForParam":null,"requiredUptoLevel":1}],"tableFieldContainer":[{"index":0,"isLink":true,"fieldName":"Number of ASHAs moved to production","customParam":"","customState":"techo.report.view({\"id\":\"6856ae3a-4a95-4e44-9aaf-ecd6c677c9e0\",\"queryParams\":{\"location_id\":@hidden_location_id@, \"type\":\"MOVED TO PRODUCTION\", \"role_name\":\"ASHA\", \"from_date\": \"$from_date$\", \"to_date\": \"$to_date$\"}})","navigationState":null},{"index":1,"isLink":true,"fieldName":"Number of ANMs moved to production","customParam":"","customState":"techo.report.view({\"id\":\"6856ae3a-4a95-4e44-9aaf-ecd6c677c9e0\",\"queryParams\":{\"location_id\":@hidden_location_id@, \"type\":\"MOVED TO PRODUCTION\", \"role_name\":\"ANM\", \"from_date\": \"$from_date$\", \"to_date\": \"$to_date$\"}})","navigationState":null},{"index":2,"isLink":true,"fieldName":"Total number of ASHAs","customParam":"","customState":"techo.report.view({\"id\":\"6856ae3a-4a95-4e44-9aaf-ecd6c677c9e0\",\"queryParams\":{\"location_id\":@hidden_location_id@, \"type\":\"TOTAL\", \"role_name\":\"ASHA\", \"from_date\": \"$from_date$\", \"to_date\": \"$to_date$\"}})","navigationState":null},{"index":3,"isLink":true,"fieldName":"Total number of ANMs","customParam":"","customState":"techo.report.view({\"id\":\"6856ae3a-4a95-4e44-9aaf-ecd6c677c9e0\",\"queryParams\":{\"location_id\":@hidden_location_id@, \"type\":\"TOTAL\", \"role_name\":\"ANM\", \"from_date\": \"$from_date$\", \"to_date\": \"$to_date$\"}})","navigationState":null}]},"isLandscape":false,"templateType":"DYNAMIC_REPORTS","isExcelOption":true,"isPrintOption":true,"locationLevel":"","numberOfColumnPerPage":10,"numberOfRecordsPerPage":20,"htmlData":true,"showDateAsOn":true}', 'ncd_progress_trackin', '04737cee-18b2-4d04-bb14-0d9ee73c67d1' );



    DELETE FROM REPORT_QUERY_MASTER WHERE uuid = 'e761dc24-22fb-4f36-9375-b4802102db57';

    INSERT INTO REPORT_QUERY_MASTER(Id,created_by, created_on, modified_by, modified_on, params, query, returns_result_set, state, uuid)
    VALUES (
    127, 1,  current_date , 1,  current_date , 'loggedInUserId', 'with prefered_language as(
    select (case
    		when report_preferred_language = ''EN'' then true
    		else false
    	end) as is_enlish
    from um_user where id = #loggedInUserId#),
    users as (
    	select distinct created_by from ncd_member_breast_detail
    	union
    	select distinct created_by from ncd_member_cervical_detail
    	union
    	select distinct created_by from ncd_member_diabetes_detail
    	union
    	select distinct created_by from ncd_member_hypertension_detail
    	union
    	select distinct created_by from ncd_member_oral_detail
    ), aoi as (
    	select usr.id as id, usr.user_name, usr.first_name, usr.last_name,
    	string_agg(loc_name,''<br>'') as aoi,
    	(select count(*) from ncd_member_breast_detail where created_by = usr.id) as breast,
    	(select count(*) from ncd_member_cervical_detail where created_by = usr.id) as cervical,
    	(select count(*) from ncd_member_diabetes_detail where created_by = usr.id) as diabetes,
    	(select count(*) from ncd_member_hypertension_detail where created_by = usr.id) as hyper,
    	(select count(*) from ncd_member_oral_detail where created_by = usr.id) as oral
    	from (select usr.id,u_loc.loc_id,string_agg(case when ( prefered_language.is_enlish and (lm.english_name is not null)) then lm.english_name else lm.name end ,'' >'' order by lh.depth desc) as loc_name
    	from (select distinct usr.id from um_user usr,um_user_location u_loc,location_hierchy_closer_det lcloser
    	where usr.id in (select * from users) and usr.id = u_loc.user_id and u_loc.state = ''ACTIVE''
    	and lcloser.child_id = u_loc.loc_id
    	) as usr, um_user_location u_loc, location_hierchy_closer_det lh, location_master lm ,prefered_language
    	where usr.id = u_loc.user_id and u_loc.state = ''ACTIVE'' and u_loc.loc_id = lh.child_id and lh.parent_id = lm.id
    	group by usr.id,u_loc.loc_id) as user_loc_det, um_user usr
    	where user_loc_det.id = usr.id
    	group by usr.id
    	order by aoi
    )
    select  row_number() over() as "Sr. no",
    aoi.user_name as "User Name",
    aoi.first_name || '' '' || aoi.last_name as "Name",
    aoi.aoi as "Area of Intervention",
    breast as "Breast Form Filled",
    cervical as "Cervical Form Filled",
    diabetes as "Diabetes Form Filled",
    hyper as "Hypertension Form Filled",
    oral as "Oral Form Filled"
    from aoi
    union all
    select null, null, null, ''<b>Total Forms Filled</b>'',
    sum(breast), sum(cervical), sum(diabetes), sum(hyper), sum(oral)
    from aoi', true, 'ACTIVE', 'e761dc24-22fb-4f36-9375-b4802102db57');

    DELETE FROM REPORT_MASTER WHERE uuid ='598bde2d-dd11-40bd-aea8-a946a9925e53';

    INSERT INTO REPORT_MASTER(Id,report_name, file_name, active, report_type, modified_on, created_by, created_on, modified_by, config_json, code, uuid)
    VALUES (
    681,'NCD Screening Status',  null,  true, 'DYNAMIC',  current_date , 58981,  current_date , 58981, '{"layout":"dynamicReport1","containers":{"tableContainer":[{"query":"with prefered_language as(\nselect (case \n\t\twhen report_preferred_language = ''EN'' then true  \n\t\telse false \n\tend) as is_enlish \nfrom um_user where id = #loggedInUserId#),\nusers as (\n\tselect distinct created_by from ncd_member_breast_detail\n\tunion\n\tselect distinct created_by from ncd_member_cervical_detail\n\tunion\n\tselect distinct created_by from ncd_member_diabetes_detail\n\tunion\n\tselect distinct created_by from ncd_member_hypertension_detail\n\tunion\n\tselect distinct created_by from ncd_member_oral_detail\n), aoi as (\n\tselect usr.id as id, usr.user_name, usr.first_name, usr.last_name, \n\tstring_agg(loc_name,''<br>'') as aoi, \n\t(select count(*) from ncd_member_breast_detail where created_by = usr.id) as breast,\n\t(select count(*) from ncd_member_cervical_detail where created_by = usr.id) as cervical,\n\t(select count(*) from ncd_member_diabetes_detail where created_by = usr.id) as diabetes,\n\t(select count(*) from ncd_member_hypertension_detail where created_by = usr.id) as hyper,\n\t(select count(*) from ncd_member_oral_detail where created_by = usr.id) as oral\n\tfrom (select usr.id,u_loc.loc_id,string_agg(case when ( prefered_language.is_enlish and (lm.english_name is not null)) then lm.english_name else lm.name end ,'' >'' order by lh.depth desc) as loc_name \n\tfrom (select distinct usr.id from um_user usr,um_user_location u_loc,location_hierchy_closer_det lcloser \n\twhere usr.id in (select * from users) and usr.id = u_loc.user_id and u_loc.state = ''ACTIVE''\n\tand lcloser.child_id = u_loc.loc_id\n\t) as usr, um_user_location u_loc, location_hierchy_closer_det lh, location_master lm ,prefered_language\n\twhere usr.id = u_loc.user_id and u_loc.state = ''ACTIVE'' and u_loc.loc_id = lh.child_id and lh.parent_id = lm.id \n\tgroup by usr.id,u_loc.loc_id) as user_loc_det, um_user usr\n\twhere user_loc_det.id = usr.id \n\tgroup by usr.id\n\torder by aoi\n)\nselect  row_number() over() as \"Sr. no\", \naoi.user_name as \"User Name\", \naoi.first_name || '' '' || aoi.last_name as \"Name\",\naoi.aoi as \"Area of Intervention\",\nbreast as \"Breast Form Filled\",\ncervical as \"Cervical Form Filled\",\ndiabetes as \"Diabetes Form Filled\",\nhyper as \"Hypertension Form Filled\",\noral as \"Oral Form Filled\"\nfrom aoi \nunion all\nselect null, null, null, ''<b>Total Forms Filled</b>'',\nsum(breast), sum(cervical), sum(diabetes), sum(hyper), sum(oral)\nfrom aoi","queryId":127,"fieldName":"tableField","queryUUID":"e761dc24-22fb-4f36-9375-b4802102db57","queryParams":"loggedInUserId"}],"fieldsContainer":[]},"templateType":"DYNAMIC_REPORTS","isExcelOption":true,"isPrintOption":true,"locationLevel":"","selectedContainer":"tableContainer"}', 'anm_ncd_work_done', '598bde2d-dd11-40bd-aea8-a946a9925e53' );


    DELETE FROM REPORT_QUERY_MASTER WHERE uuid = '62cd3640-8f5b-458e-9f43-2a95c9559255';

    INSERT INTO REPORT_QUERY_MASTER(created_by, created_on, modified_by, modified_on, query, returns_result_set, state, uuid)
    VALUES (
    1,  current_date , 1,  current_date , 'with genders as (
    select ''M'' as key, ''Male'' as value, 1 as ord
    union
    select ''F'' as key, ''Female'' as value, 2 as ord
    union
    select ''B'' as key, ''Both'' as value, 3 as ord
    )
    select * from genders order by ord', true, 'ACTIVE', '62cd3640-8f5b-458e-9f43-2a95c9559255');

    DELETE FROM REPORT_QUERY_MASTER WHERE uuid = '7e987989-b6d4-49e9-b4e8-af444e15d14a';

    INSERT INTO REPORT_QUERY_MASTER(Id,created_by, created_on, modified_by, modified_on, params, query, returns_result_set, state, uuid)
    VALUES (
    2554,1,  current_date , 1,  current_date , 'from_date,to_date,gender,location_id', 'with loc_det as (
        select child_id as id
        from location_hierchy_closer_det
        where parent_id = #location_id#
        and depth = 1
    ),
    dates as (
        select
            case when ''#from_date#'' = ''null'' then null else to_date(''#from_date#'', ''MM/DD/YYYY'') end as from_date,
            case when ''#to_date#'' = ''null'' then null else to_date(''#to_date#'', ''MM/DD/YYYY'') end as to_date
    ),
    analytics as (
    	select
    	lhcd.parent_id,
        sum(nca.cbac_filled) as cbac_filled,
        sum(nca.member_with_chronic_disease) as member_with_chronic_disease,
        sum(nca.bmi_normal) as bmi_normal,
        sum(nca.bmi_under) as bmi_under,
        sum(nca.bmi_pre_obese) as bmi_pre_obese,
        sum(nca.bmi_obese) as bmi_obese,
        sum(nca.smoke_daily) as smoke_daily,
        sum(nca.smoke_never) as smoke_never,
        sum(nca.smoke_occasionally) as smoke_occasionally,
        sum(nca.consume_gutka_daily) as consume_gutka_daily,
        sum(nca.consume_gutka_never) as consume_gutka_never,
        sum(nca.consume_gutka_occasionally) as consume_gutka_occasionally,
        sum(nca.consume_alcohol_daily) as consume_alcohol_daily,
        sum(nca.consume_alcohol_never) as consume_alcohol_never,
        sum(nca.consume_alcohol_occasionally) as consume_alcohol_occasionally,
        sum(nca.phys_activity_lt_30) as phys_activity_lt_30,
        sum(nca.phys_activity_gt_30) as phys_activity_gt_30,
        sum(nca.taking_anti_tb_drugs) as taking_anti_tb_drugs,
        sum(nca.treatment_leprosy) as treatment_leprosy,
        sum(nca.oral_cancer_sus) as oral_cancer_sus,
        sum(nca.breast_cancer_sus) as breast_cancer_sus,
        sum(nca.cervical_cancer_sus) as cervical_cancer_sus,
        sum(nca.mental_health_sus) as mental_health_sus,
        sum(nca.copd_diagnosed) as copd_diagnosed,
        sum(nca.deafness_sus) as deafness_sus,
        sum(nca.blurred_vision) as blurred_vision,
        sum(nca.epilepsy) as epilepsy,
        sum(nca.elderly) as elderly
    	from location_hierchy_closer_det lhcd
        inner join ncd_cbac_status_analytics nca on lhcd.child_id = nca.location_id
        inner join dates on true
    	where lhcd.parent_id in (select id from loc_det)
        and nca.service_date between dates.from_date and dates.to_date
        and case when ''#gender#'' = ''M'' then nca.gender = ''M'' when ''#gender#'' = ''F'' then nca.gender = ''F'' when ''#gender#'' = ''B'' then true else true end
    	group by lhcd.parent_id
    )
    select
        row_number() over(order by lm.english_name) as "Sr. No.",
        lm.english_name AS "Location",
        lm.id as hidden_location_id,
        coalesce(cbac_filled, 0) as "Total CBAC filled",
        coalesce(member_with_chronic_disease, 0) as "Members with chronic disease(s)",
        coalesce(bmi_normal, 0) as "Normal",
        coalesce(bmi_under, 0) as "Under",
        coalesce(bmi_pre_obese, 0) as "Pre-Obesity",
        coalesce(bmi_obese, 0) as "Obesity (class I, II and III)",
        coalesce(smoke_daily, 0) as "Daily (Smokes)",
        coalesce(smoke_occasionally, 0) as "Occasionally (Smokes)",
        coalesce(smoke_never, 0) as "Never (Smokes)",
        coalesce(consume_gutka_daily, 0) as "Daily (Consumes Gutka)",
        coalesce(consume_gutka_occasionally, 0) as "Occasionally (Consumes Gutka)",
        coalesce(consume_gutka_never, 0) as "Never (Consumes Gutka)",
        coalesce(consume_alcohol_daily, 0) as "Daily (Consumes Alcohol)",
        coalesce(consume_alcohol_occasionally, 0) as "Occasionally (Consumes Alcohol)",
        coalesce(consume_alcohol_never, 0) as "Never (Consumes Alcohol)",
        coalesce(phys_activity_gt_30, 0) as "Atleast 30 minutes daily",
        round(cast(coalesce(phys_activity_gt_30, 0) as numeric(10,2)) * 100/ case when (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) = 0 then 1 else (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) end, 2) as "% (Atleast 30 minutes daily)",
        coalesce(phys_activity_lt_30, 0) as "Less than 30 minutes daily",
        round(cast(coalesce(phys_activity_lt_30, 0) as numeric(10,2)) * 100/ case when (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) = 0 then 1 else (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) end, 2) as "% (Less than 30 minutes daily)",
        coalesce(taking_anti_tb_drugs, 0) as "Member taking anti-TB drugs",
        coalesce(treatment_leprosy, 0) as "Member on treatment for leprosy issues",
        coalesce(oral_cancer_sus, 0) as "Oral cancer suspect",
        coalesce(breast_cancer_sus, 0) as "Breast cancer suspect",
        coalesce(cervical_cancer_sus, 0) as "Cervical cancer suspect",
        coalesce(mental_health_sus, 0) as "Mental Health suspect",
        coalesce(copd_diagnosed, 0) as "Member diagnosed for COPD",
        coalesce(deafness_sus, 0) as "Deafness suspect",
        coalesce(blurred_vision, 0) as "Blurred vision",
        coalesce(epilepsy, 0) as "Epilepsy (fits)",
        coalesce(elderly, 0) as "Elderly specific"
    from loc_det ld left join analytics a
    on ld.id = a.parent_id
    inner join location_master lm on lm.id = ld.id

    union all

    select null,
        ''<b>Total</b>'',
        #location_id#,
        coalesce(sum(cbac_filled), 0),
        coalesce(sum(member_with_chronic_disease), 0),
        coalesce(sum(bmi_normal), 0),
        coalesce(sum(bmi_under), 0),
        coalesce(sum(bmi_pre_obese), 0),
        coalesce(sum(bmi_obese), 0),
        coalesce(sum(smoke_daily), 0),
        coalesce(sum(smoke_occasionally), 0),
        coalesce(sum(smoke_never), 0),
        coalesce(sum(consume_gutka_daily), 0),
        coalesce(sum(consume_gutka_occasionally), 0),
        coalesce(sum(consume_gutka_never), 0),
        coalesce(sum(consume_alcohol_daily), 0),
        coalesce(sum(consume_alcohol_occasionally), 0),
        coalesce(sum(consume_alcohol_never), 0),
        coalesce(sum(phys_activity_gt_30), 0),
        round(cast(coalesce(sum(phys_activity_gt_30), 0) as numeric(10,2)) * 100/ case when (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) = 0 then 1 else (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) end, 2),
        coalesce(sum(phys_activity_lt_30), 0),
        round(cast(coalesce(sum(phys_activity_lt_30), 0) as numeric(10,2)) * 100/ case when (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) = 0 then 1 else (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) end, 2),
        coalesce(sum(taking_anti_tb_drugs), 0),
        coalesce(sum(treatment_leprosy), 0),
        coalesce(sum(oral_cancer_sus), 0),
        coalesce(sum(breast_cancer_sus), 0),
        coalesce(sum(cervical_cancer_sus), 0),
        coalesce(sum(mental_health_sus), 0),
        coalesce(sum(copd_diagnosed), 0),
        coalesce(sum(deafness_sus), 0),
        coalesce(sum(blurred_vision), 0),
        coalesce(sum(epilepsy), 0),
        coalesce(sum(elderly), 0)
    from analytics a;', true, 'ACTIVE', '7e987989-b6d4-49e9-b4e8-af444e15d14a');

    DELETE FROM REPORT_MASTER WHERE uuid ='82eb86f7-6d16-448f-8496-677bd9ed5a2b';

    INSERT INTO REPORT_MASTER(Id,report_name, file_name, active, report_type, modified_on, created_by, created_on, modified_by, config_json, code, uuid)
    VALUES (
    682,'CBAC Status Report',  null,  true, 'DYNAMIC',  current_date , 1,  current_date , 1, '{"layout":"dynamicReport1","templateType":"DYNAMIC_REPORTS","containers":{"fieldsContainer":[{"fieldName":"location_id","displayName":"Location","fieldType":"location","fetchUptoLevel":"7","requiredUptoLevel":"1","fetchAccordingToUserAoi":true,"isMandatory":true,"index":0},{"fieldName":"date","displayName":"Date","fieldType":"dateFromTo","isMandatory":true,"ckSetCustomMaxDate":true,"setCustomMaxDate":0},{"fieldName":"gender","displayName":"Gender","fieldType":"comboForReport","isMandatory":true,"isQuery":true,"availableOptions":[],"query":"with genders as (\nselect ''M'' as key, ''Male'' as value, 1 as ord\nunion\nselect ''F'' as key, ''Female'' as value, 2 as ord\nunion\nselect ''B'' as key, ''Both'' as value, 3 as ord\n)\nselect * from genders order by ord","queryId":2554,"queryParams":null,"queryUUID":"62cd3640-8f5b-458e-9f43-2a95c9559255"}],"tableContainer":[{"fieldName":"tableField","query":"with loc_det as (\n    select child_id as id\n    from location_hierchy_closer_det\n    where parent_id = #location_id# \n    and depth = 1\n), \ndates as (\n    select\n        case when ''#from_date#'' = ''null'' then null else to_date(''#from_date#'', ''MM/DD/YYYY'') end as from_date,\n        case when ''#to_date#'' = ''null'' then null else to_date(''#to_date#'', ''MM/DD/YYYY'') end as to_date\n), \nanalytics as (\n\tselect\n\tlhcd.parent_id,\n    sum(nca.cbac_filled) as cbac_filled,\n    sum(nca.member_with_chronic_disease) as member_with_chronic_disease,\n    sum(nca.bmi_normal) as bmi_normal,\n    sum(nca.bmi_under) as bmi_under,   \n    sum(nca.bmi_pre_obese) as bmi_pre_obese,\n    sum(nca.bmi_obese) as bmi_obese,\n    sum(nca.smoke_daily) as smoke_daily,\n    sum(nca.smoke_never) as smoke_never,\n    sum(nca.smoke_occasionally) as smoke_occasionally,\n    sum(nca.consume_gutka_daily) as consume_gutka_daily,\n    sum(nca.consume_gutka_never) as consume_gutka_never,\n    sum(nca.consume_gutka_occasionally) as consume_gutka_occasionally,\n    sum(nca.consume_alcohol_daily) as consume_alcohol_daily,\n    sum(nca.consume_alcohol_never) as consume_alcohol_never,\n    sum(nca.consume_alcohol_occasionally) as consume_alcohol_occasionally,\n    sum(nca.phys_activity_lt_30) as phys_activity_lt_30,\n    sum(nca.phys_activity_gt_30) as phys_activity_gt_30,\n    sum(nca.taking_anti_tb_drugs) as taking_anti_tb_drugs,\n    sum(nca.treatment_leprosy) as treatment_leprosy,\n    sum(nca.oral_cancer_sus) as oral_cancer_sus,\n    sum(nca.breast_cancer_sus) as breast_cancer_sus,\n    sum(nca.cervical_cancer_sus) as cervical_cancer_sus,\n    sum(nca.mental_health_sus) as mental_health_sus,\n    sum(nca.copd_diagnosed) as copd_diagnosed,\n    sum(nca.deafness_sus) as deafness_sus,\n    sum(nca.blurred_vision) as blurred_vision,\n    sum(nca.epilepsy) as epilepsy,\n    sum(nca.elderly) as elderly\n\tfrom location_hierchy_closer_det lhcd\n    inner join ncd_cbac_status_analytics nca on lhcd.child_id = nca.location_id\n    inner join dates on true\n\twhere lhcd.parent_id in (select id from loc_det)\n    and nca.service_date between dates.from_date and dates.to_date\n    and case when ''#gender#'' = ''M'' then nca.gender = ''M'' when ''#gender#'' = ''F'' then nca.gender = ''F'' when ''#gender#'' = ''B'' then true else true end\n\tgroup by lhcd.parent_id\n)\nselect  \n    row_number() over(order by lm.english_name) as \"Sr. No.\", \n    lm.english_name AS \"Location\",\n    lm.id as hidden_location_id,\n    coalesce(cbac_filled, 0) as \"Total CBAC filled\",\n    coalesce(member_with_chronic_disease, 0) as \"Members with chronic disease(s)\",\n    coalesce(bmi_normal, 0) as \"Normal\",\n    coalesce(bmi_under, 0) as \"Under\",\n    coalesce(bmi_pre_obese, 0) as \"Pre-Obesity\",\n    coalesce(bmi_obese, 0) as \"Obesity (class I, II and III)\",\n    coalesce(smoke_daily, 0) as \"Daily (Smokes)\",\n    coalesce(smoke_occasionally, 0) as \"Occasionally (Smokes)\",\n    coalesce(smoke_never, 0) as \"Never (Smokes)\",\n    coalesce(consume_gutka_daily, 0) as \"Daily (Consumes Gutka)\",\n    coalesce(consume_gutka_occasionally, 0) as \"Occasionally (Consumes Gutka)\",\n    coalesce(consume_gutka_never, 0) as \"Never (Consumes Gutka)\",\n    coalesce(consume_alcohol_daily, 0) as \"Daily (Consumes Alcohol)\",\n    coalesce(consume_alcohol_occasionally, 0) as \"Occasionally (Consumes Alcohol)\",\n    coalesce(consume_alcohol_never, 0) as \"Never (Consumes Alcohol)\",\n    coalesce(phys_activity_gt_30, 0) as \"Atleast 30 minutes daily\",\n    round(cast(coalesce(phys_activity_gt_30, 0) as numeric(10,2)) * 100/ case when (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) = 0 then 1 else (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) end, 2) as \"% (Atleast 30 minutes daily)\",\n    coalesce(phys_activity_lt_30, 0) as \"Less than 30 minutes daily\",\n    round(cast(coalesce(phys_activity_lt_30, 0) as numeric(10,2)) * 100/ case when (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) = 0 then 1 else (coalesce(phys_activity_gt_30,0) + coalesce(phys_activity_lt_30,0)) end, 2) as \"% (Less than 30 minutes daily)\",\n    coalesce(taking_anti_tb_drugs, 0) as \"Member taking anti-TB drugs\",\n    coalesce(treatment_leprosy, 0) as \"Member on treatment for leprosy issues\",\n    coalesce(oral_cancer_sus, 0) as \"Oral cancer suspect\",\n    coalesce(breast_cancer_sus, 0) as \"Breast cancer suspect\",\n    coalesce(cervical_cancer_sus, 0) as \"Cervical cancer suspect\",\n    coalesce(mental_health_sus, 0) as \"Mental Health suspect\",\n    coalesce(copd_diagnosed, 0) as \"Member diagnosed for COPD\",\n    coalesce(deafness_sus, 0) as \"Deafness suspect\",\n    coalesce(blurred_vision, 0) as \"Blurred vision\",\n    coalesce(epilepsy, 0) as \"Epilepsy (fits)\",\n    coalesce(elderly, 0) as \"Elderly specific\"\nfrom loc_det ld left join analytics a\non ld.id = a.parent_id\ninner join location_master lm on lm.id = ld.id\n\nunion all\n\nselect null,\n    ''<b>Total</b>'',\n    #location_id#,\n    coalesce(sum(cbac_filled), 0),\n    coalesce(sum(member_with_chronic_disease), 0),\n    coalesce(sum(bmi_normal), 0),\n    coalesce(sum(bmi_under), 0),\n    coalesce(sum(bmi_pre_obese), 0),\n    coalesce(sum(bmi_obese), 0),\n    coalesce(sum(smoke_daily), 0),\n    coalesce(sum(smoke_occasionally), 0),\n    coalesce(sum(smoke_never), 0),\n    coalesce(sum(consume_gutka_daily), 0),\n    coalesce(sum(consume_gutka_occasionally), 0),\n    coalesce(sum(consume_gutka_never), 0),\n    coalesce(sum(consume_alcohol_daily), 0),\n    coalesce(sum(consume_alcohol_occasionally), 0),\n    coalesce(sum(consume_alcohol_never), 0),\n    coalesce(sum(phys_activity_gt_30), 0),\n    round(cast(coalesce(sum(phys_activity_gt_30), 0) as numeric(10,2)) * 100/ case when (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) = 0 then 1 else (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) end, 2),\n    coalesce(sum(phys_activity_lt_30), 0),\n    round(cast(coalesce(sum(phys_activity_lt_30), 0) as numeric(10,2)) * 100/ case when (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) = 0 then 1 else (coalesce(sum(phys_activity_gt_30),0) + coalesce(sum(phys_activity_lt_30),0)) end, 2),\n    coalesce(sum(taking_anti_tb_drugs), 0),\n    coalesce(sum(treatment_leprosy), 0),\n    coalesce(sum(oral_cancer_sus), 0),\n    coalesce(sum(breast_cancer_sus), 0),\n    coalesce(sum(cervical_cancer_sus), 0),\n    coalesce(sum(mental_health_sus), 0),\n    coalesce(sum(copd_diagnosed), 0),\n    coalesce(sum(deafness_sus), 0),\n    coalesce(sum(blurred_vision), 0),\n    coalesce(sum(epilepsy), 0),\n    coalesce(sum(elderly), 0)\nfrom analytics a;","queryId":2555,"queryUUID":"7e987989-b6d4-49e9-b4e8-af444e15d14a","queryParams":"from_date,to_date,gender,location_id","dataColspan":"<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th colspan=\"4\" style=\"text-align:center\">BMI categorization</th>\n<th colspan=\"3\" style=\"text-align:center\">Smoke</th>\n<th colspan=\"3\" style=\"text-align:center\">Consume smokless products such as gutka or khaini</th>\n<th colspan=\"3\" style=\"text-align:center\">Consume alcohol</th>\n<th colspan=\"4\" style=\"text-align:center\">Physical activities</th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>\n<th></th>"}],"tableFieldContainer":[{"fieldName":"Sr. No.","isReferenceColumn":true},{"fieldName":"Location","isReferenceColumn":true},{"fieldName":"Members with chronic disease(s)","isReferenceColumn":false,"isLink":true,"customState":"techo.report.view({\"id\":\"f5772b75-0094-42b9-a931-9ad075d71d08\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"Members with chronic disease(s)\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":2},{"fieldName":"Member taking anti-TB drugs","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ANTI_TB\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3"},{"fieldName":"Member on treatment for leprosy issues","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"LEPROSY\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":4},{"fieldName":"Deafness suspect","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"DEAFNESS\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":5},{"fieldName":"Blurred vision","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BLURRY\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":6},{"fieldName":"Elderly specific","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ELDERLY\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":7},{"fieldName":"Daily (Smokes)","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"SMOKE_DAILY\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3"},{"fieldName":"Occasionally (Smokes)","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"SMOKE_OCC\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3"},{"fieldName":"Never (Smokes)","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"SMOKE_NEVER\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","index":10},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"GUTKA_DAILY\"}})","customParam":"","navigationState":null,"fieldName":"Daily (Consumes Gutka)","locationLevelForLink":"3","index":11},{"fieldName":"Occasionally (Consumes Gutka)","isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"GUTKA_OCC\"}})","customParam":"","navigationState":null,"index":12,"locationLevelForLink":"3"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"GUTKA_NEVER\"}})","customParam":"","navigationState":null,"fieldName":"Never (Consumes Gutka)","index":13,"locationLevelForLink":"3"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ALCOHOL\"}})","customParam":"","navigationState":null,"fieldName":"Daily (Consumes Alcohol)","locationLevelForLink":"3","index":14},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ALCOHOL_NEVER\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","fieldName":"Never (Consumes Alcohol)","index":15},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ALCOHOL_OCC\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","fieldName":"Occasionally (Consumes Alcohol)","index":16},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"PHYSICAL_ACTIVITY_ATLEAST_30\"}})","customParam":"","navigationState":null,"fieldName":"Atleast 30 minutes daily","locationLevelForLink":"3","index":17},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"PHYSICAL_ACTIVITY_LESS_THAN_30\"}})","customParam":"","navigationState":null,"fieldName":"Less than 30 minutes daily","locationLevelForLink":"3","index":18},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"ORAL_CANCER\"}})","customParam":"","navigationState":null,"fieldName":"Oral cancer suspect","locationLevelForLink":"3"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BREAST_CANCER\"}})","customParam":"","navigationState":null,"fieldName":"Breast cancer suspect","locationLevelForLink":"3","index":20},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"CERVICAL_CANCER\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","fieldName":"Cervical cancer suspect"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"MENTAL_HEALTH\"}})","customParam":"","navigationState":null,"fieldName":"Mental Health suspect","locationLevelForLink":"3"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"COPD\"}})","customParam":"","navigationState":null,"fieldName":"Member diagnosed for COPD","locationLevelForLink":"3","index":23},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"EPILEPSY\"}})","customParam":"","navigationState":null,"locationLevelForLink":"3","fieldName":"Epilepsy (fits)","index":24},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BMI_NORMAL\"}})","customParam":"","navigationState":null,"fieldName":"Normal","locationLevelForLink":"3","index":25},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BMI_UNDER\"}})","customParam":"","navigationState":null,"fieldName":"Under","locationLevelForLink":"3"},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BMI_PRE_OBESE\"}})","customParam":"","navigationState":null,"fieldName":"Pre-Obesity","locationLevelForLink":"3","index":27},{"isLink":true,"customState":"techo.report.view({\"id\":\"e56eb796-4c18-4fc6-a26c-0c4f5ddf141d\",\"queryParams\":{\"location_id\":@hidden_location_id@,\"from_date\":\"$from_date$\",\"to_date\":\"$to_date$\",\"gender\":\"$gender$\",\"type\":\"BMI_OBESE\"}})","customParam":"","navigationState":null,"fieldName":"Obesity (class I, II and III)","locationLevelForLink":"3"}]},"isPrintOption":true,"isExcelOption":true,"isLandscape":false,"numberOfRecordsPerPage":20,"numberOfColumnPerPage":10,"locationLevel":"","showDateAsOn":true,"htmlData":false,"colspanData":true}', 'cbac_status', '82eb86f7-6d16-448f-8496-677bd9ed5a2b' );



--Insert into mobile menu management
delete from mobile_menu_master where id in (81,82,83,84,85,86,97,98,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116);

INSERT INTO public.mobile_menu_master
    (id, config_json, menu_name, created_on, created_by, modified_on, modified_by)
VALUES
    (81, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":5}]', 'FHW Menu', now(), 100954, now(), 100954),
    (82, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":4}]', 'ASHA Menu', now(), 100954, now(), 100954),
    (83, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":5},{"mobile_constant":"DNHDD_NCD_SCREENING","order":6}]', 'FHW Menu', now(), 100954, now(), 100954),
    (84, '[{"mobile_constant":"FHW_WORK_STATUS","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FAMILY_FOLDER","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5}]', 'CHO menu', now(), 100954, now(), 100954),
    (85, '[{"mobile_constant":"FHW_WORK_STATUS","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FAMILY_FOLDER","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5}]', 'CHO menu', now(), 100954,now(), 100954),
    (86, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5}]', 'FHW Menu', now(), 100954, now(), 100954),
    (102, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6}]', 'FHW Menu', now(), 100954, now(), 100954),
    (103, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6},{"mobile_constant":"ADULT_BCG_VACCINATION","order":7}]', 'FHW Menu', now(), 121657, now(), 121657),
    (104, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":4},{"mobile_constant":"ADULT_BCG_VACCINATION","order":5}]', 'ASHA Menu', now(), 121657, now(), 121657),
    (105, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6}]', 'FHW Menu',now(), 100954, now(), 100954),
    (106, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":4}]', 'ASHA Menu', now(), 100954, now(), 100954),
    (107, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6},{"mobile_constant":"ADULT_BCG_VACCINATION","order":7}]', 'FHW Menu', now(), 100954, now(), 100954),
    (108, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6}]', 'FHW Menu', now(), 121554, now(), 121554),
    (109, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6},{"mobile_constant":"ADULT_BCG_VACCINATION","order":7}]', 'FHW Menu', now(), 100954, now(), 100954),
    (110, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6}]', 'FHW Menu', '12-01-2024', 100954, '12-01-2024', 100954),
    (111, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6},{"mobile_constant":"ADULT_BCG_VACCINATION","order":7}]', 'FHW Menu', now(), 121554, now(), 121554),
    (112, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NOTIFICATION","order":6},{"mobile_constant":"ADULT_BCG_VACCINATION","order":7},{"mobile_constant":"FHW_NCD_REGISTER","order":8}]', 'FHW Menu', now(), 121554, now(), 121554),
    (113, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":4},{"mobile_constant":"ADULT_BCG_VACCINATION","order":5}]', 'ASHA Menu', now(), 100954, now(), 100954),
    (114, '[{"mobile_constant":"FAMILY_FOLDER","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FHW_WORK_STATUS","order":3},{"mobile_constant":"DNHDD_NCD_CBAC_AND_NUTRITION","order":4},{"mobile_constant":"ADULT_BCG_VACCINATION","order":5},{"mobile_constant":"ASHA_NCD_REGISTER","order":6}]', 'ASHA Menu', now(), 100954,now(), -1),
    (115, '[{"mobile_constant":"FHW_WORK_STATUS","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FAMILY_FOLDER","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NCD_REGISTER","order":6}]', 'CHO menu', now(), 100954, now(), 100954),
    (116, '[{"mobile_constant":"FHW_WORK_STATUS","order":1},{"mobile_constant":"FAMILY_AVAILABILITY","order":2},{"mobile_constant":"FAMILY_FOLDER","order":3},{"mobile_constant":"FAMILY_FOLDER_REVERIFICATION","order":4},{"mobile_constant":"DNHDD_NCD_SCREENING","order":5},{"mobile_constant":"FHW_NCD_REGISTER","order":6}]', 'CHO menu', now(), 100954, now(), 100954);

--insert into mobile_feature_master
delete from mobile_feature_master where mobile_constant in ('DNHDD_NCD_CBAC_AND_NUTRITION','DNHDD_NCD_SCREENING');
INSERT INTO public.mobile_feature_master (mobile_constant,feature_name,mobile_display_name,state,created_on,created_by,modified_on,modified_by) VALUES
        ('DNHDD_NCD_CBAC_AND_NUTRITION','CBAC And Nutrition','CBAC And Nutrition','ACTIVE',now(),-1,now(),-1),
        ('DNHDD_NCD_SCREENING','Dnhdd NCD Screening','NCD Screening','ACTIVE',now(),-1,now(),-1);

delete from mobile_form_details where form_name in ('DNHDD_NCD_CBAC_AND_NUTRITION','CANCER_SCREENING','DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH');
insert into mobile_form_details (form_name,file_name,created_on,created_by,modified_on,modified_by)
values ('DNHDD_NCD_CBAC_AND_NUTRITION', 'DNHDD_NCD_CBAC_AND_NUTRITION', now(), -1, now(), -1),
       ('CANCER_SCREENING', 'CANCER_SCREENING', now(), -1, now(), -1),
       ('DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH', 'DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH', now(), -1, now(), -1);


delete from mobile_form_feature_rel where form_id in (57,59,60);
INSERT INTO mobile_form_feature_rel (form_id,mobile_constant) VALUES
	 (57,'DNHDD_NCD_CBAC_AND_NUTRITION'),
	 (59,'DNHDD_NCD_SCREENING'),
	 (60,'DNHDD_NCD_SCREENING');


delete from listvalue_field_form_relation where form_id in (59,60) and field = 'chronicDiseaseList';
insert into listvalue_field_form_relation(field,form_id) values
     ('chronicDiseaseList',59),
     ('chronicDiseaseList',60);

delete from listvalue_field_form_relation where form_id in (59,60) and field = 'mentalHealthOtherProblemList';
insert into listvalue_field_form_relation(field,form_id) values
     ('mentalHealthOtherProblemList',59),
     ('mentalHealthOtherProblemList',60);


ALTER TABLE ncd_member_mental_health_detail ADD COLUMN IF NOT EXISTS is_suspected boolean;


INSERT INTO public.user_health_infrastructure
(id, user_id, health_infrastrucutre_id, created_by, created_on, modified_by, modified_on, state, is_default)
VALUES(1, 1, 1, 1, now(), 1, now(), 'ACTIVE', false),
(2, 1, 2, 1, now(), 1, now(), 'ACTIVE', false);