-- Started on 2023-06-29 19:51:13 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 9 (class 2615 OID 220006)
-- Name: analytics; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA IF NOT EXISTS analytics;


ALTER SCHEMA analytics OWNER TO postgres;

--
-- TOC entry 10 (class 2615 OID 220007)
-- Name: archive; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA IF NOT EXISTS archive;


ALTER SCHEMA archive OWNER TO postgres;

--
-- TOC entry 4 (class 3079 OID 220009)
-- Name: dblink; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS dblink WITH SCHEMA public;


--
-- TOC entry 6458 (class 0 OID 0)
-- Dependencies: 4
-- Name: EXTENSION dblink; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION dblink IS 'connect to other PostgreSQL databases from within a database';


--
-- TOC entry 3 (class 3079 OID 220055)
-- Name: hstore; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;


--
-- TOC entry 6459 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION hstore; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION hstore IS 'data type for storing sets of (key, value) pairs';


--
-- TOC entry 2 (class 3079 OID 220180)
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- TOC entry 6460 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


--
-- TOC entry 928 (class 1255 OID 220257)
-- Name: audit_table(regclass); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.audit_table(target_table regclass) RETURNS void
    LANGUAGE sql
    AS $_$
SELECT public.audit_table($1, BOOLEAN 't', BOOLEAN 't');
$_$;


ALTER FUNCTION public.audit_table(target_table regclass) OWNER TO postgres;

--
-- TOC entry 6461 (class 0 OID 0)
-- Dependencies: 928
-- Name: FUNCTION audit_table(target_table regclass); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.audit_table(target_table regclass) IS '
Add auditing support to the given table. Row-level changes will be logged with full client query text. No cols are ignored.
';


--
-- TOC entry 929 (class 1255 OID 220258)
-- Name: audit_table(regclass, boolean, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean) RETURNS void
    LANGUAGE sql
    AS $_$
SELECT public.audit_table($1, $2, $3, ARRAY[]::text[]);
$_$;


ALTER FUNCTION public.audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean) OWNER TO postgres;

--
-- TOC entry 930 (class 1255 OID 220259)
-- Name: audit_table(regclass, boolean, boolean, text[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean, ignored_cols text[]) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
  stm_targets text = 'INSERT OR UPDATE OR DELETE OR TRUNCATE';
  _q_txt text;
  _ignored_cols_snip text = '';
BEGIN
    EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_row ON ' || quote_ident(target_table::TEXT);
    EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_stm ON ' || quote_ident(target_table::TEXT);

    IF audit_rows THEN
        IF array_length(ignored_cols,1) > 0 THEN
            _ignored_cols_snip = ', ' || quote_literal(ignored_cols);
        END IF;
        _q_txt = 'CREATE TRIGGER audit_trigger_row AFTER INSERT OR UPDATE OR DELETE ON ' ||
                 quote_ident(target_table::TEXT) ||
                 ' FOR EACH ROW EXECUTE PROCEDURE public.if_modified_func(' ||
                 quote_literal(audit_query_text) || _ignored_cols_snip || ');';
        RAISE NOTICE '%',_q_txt;
        EXECUTE _q_txt;
        stm_targets = 'TRUNCATE';
    ELSE
    END IF;

    _q_txt = 'CREATE TRIGGER audit_trigger_stm AFTER ' || stm_targets || ' ON ' ||
             target_table ||
             ' FOR EACH STATEMENT EXECUTE PROCEDURE public.if_modified_func('||
             quote_literal(audit_query_text) || ');';
    RAISE NOTICE '%',_q_txt;
    EXECUTE _q_txt;

END;
$$;


ALTER FUNCTION public.audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean, ignored_cols text[]) OWNER TO postgres;

--
-- TOC entry 6462 (class 0 OID 0)
-- Dependencies: 930
-- Name: FUNCTION audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean, ignored_cols text[]); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean, ignored_cols text[]) IS '
Add auditing support to a table.

Arguments:
   target_table:     Table name, schema qualified if not on search_path
   audit_rows:       Record each row change, or only audit at a statement level
   audit_query_text: Record the text of the client query that triggered the audit event?
   ignored_cols:     Columns to exclude from update diffs, ignore updates that change only ignored cols.
';


--
-- TOC entry 931 (class 1255 OID 220260)
-- Name: before_update_rch_anc_master_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.before_update_rch_anc_master_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
NEW.modified_on = now();

RETURN NEW;
END;
$$;


ALTER FUNCTION public.before_update_rch_anc_master_trigger_func() OWNER TO postgres;

--
-- TOC entry 932 (class 1255 OID 220261)
-- Name: comment_on_table_or_field(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.comment_on_table_or_field(table_name text, field_name text, comment text) RETURNS text
    LANGUAGE plpgsql
    AS $_$
declare
	final_statement text;
begin
	IF field_name is null THEN
    	execute 'comment on table ' || $1::text || ' is ''' ||  cast($3 as text) || ''';';
    	select 'comment on table ' || $1::text || ' is ''' ||  cast($3 as text) || ''';' into final_statement;
    ELSE
    	execute 'comment on column ' || $1::text || ' . ' || $2::text ||  ' is ''' ||  cast($3 as text) || ''';';
    	select 'comment on column ' || $1::text || ' . ' || $2::text ||  ' is ''' ||  cast($3 as text) || ''';' into final_statement;
	END IF;
	return final_statement;
EXCEPTION
	 when others THEN RAISE NOTICE 'Error Occured while Commenting, % % ', $1,$2 ;
	 select 'ERROR  Table ' || $1 || ' Field ' || $2 into final_statement;
	 return final_statement;
END;
$_$;


ALTER FUNCTION public.comment_on_table_or_field(table_name text, field_name text, comment text) OWNER TO postgres;

--
-- TOC entry 933 (class 1255 OID 220262)
-- Name: covid19_addmission_delete_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_addmission_delete_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
INSERT INTO zzz_delete_covid19_admission_detail
	select * from covid19_admission_detail where id = OLD.id;
   RETURN OLD;
END;
$$;


ALTER FUNCTION public.covid19_addmission_delete_trigger_func() OWNER TO postgres;

--
-- TOC entry 934 (class 1255 OID 220263)
-- Name: covid19_addmission_insert_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_addmission_insert_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	NEW.search_text = concat_ws(' ',NEW.first_name,NEW.middle_name,NEW.last_name,NEW.case_no,NEW.unit_no,NEW.opd_case_no,NEW.current_bed_no,NEW.contact_number,(select ward_name from health_infrastructure_ward_details where id = NEW.current_ward_id));
	/*if NEW.lab_test_id is null then
	update covid19_lab_test_detail set created_by = created_by where covid_admission_detail_id = NEW.id and covid19_lab_test_detail.lab_test_id is null;
	end if; */
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.covid19_addmission_insert_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 935 (class 1255 OID 220264)
-- Name: covid19_admission_refer_detail_delete_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_admission_refer_detail_delete_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
INSERT INTO zzz_delete_covid19_admission_refer_detail
	select * from covid19_admission_refer_detail where id = OLD.id;
   RETURN OLD;
END;
$$;


ALTER FUNCTION public.covid19_admission_refer_detail_delete_trigger_func() OWNER TO postgres;

--
-- TOC entry 936 (class 1255 OID 220265)
-- Name: covid19_admitted_case_daily_status_delete_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_admitted_case_daily_status_delete_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
INSERT INTO zzz_delete_covid19_admitted_case_daily_status
	select * from covid19_admitted_case_daily_status where id = OLD.id;
   RETURN OLD;
END;
$$;


ALTER FUNCTION public.covid19_admitted_case_daily_status_delete_trigger_func() OWNER TO postgres;

--
-- TOC entry 937 (class 1255 OID 220266)
-- Name: covid19_lab_test_detail_delete_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_lab_test_detail_delete_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
INSERT INTO zzz_delete_covid19_lab_test_detail
	select * from covid19_lab_test_detail where id = OLD.id;
   RETURN OLD;
END;
$$;


ALTER FUNCTION public.covid19_lab_test_detail_delete_trigger_func() OWNER TO postgres;

--
-- TOC entry 938 (class 1255 OID 220267)
-- Name: covid19_lab_test_detail_insert_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_lab_test_detail_insert_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
			admission_type text;
			sample_count integer;
begin

	NEW.search_text = concat_ws(' ',
		(select concat_ws(' ',
			first_name,
			middle_name,
			last_name,
			case_no,
			unit_no,
			opd_case_no,
			current_bed_no,
			contact_number
			)
			from covid19_admission_detail
			where id = NEW.covid_admission_detail_id
		),
	NEW.lab_test_number,
	NEW.lab_test_id,
	NEW.lab_result,
	NEW.indeterminate_lab_name,
	(select name_in_english from health_infrastructure_details where id = NEW.sample_health_infra),
	(select name_in_english from health_infrastructure_details where id = NEW.sample_health_infra_send_to)
);

admission_type := (select admission_from from covid19_admission_detail where id =  NEW.covid_admission_detail_id);
sample_count := (select sample_no from (
select id,ROW_NUMBER() OVER(
    PARTITION BY covid_admission_detail_id
    ORDER BY COALESCE(lab_collection_on,created_on)
) as sample_no from covid19_lab_test_detail where
 covid_admission_detail_id = NEW.covid_admission_detail_id and lab_sample_rejected_on is null) as t
 where t.id = NEW.id);



if sample_count is null then
sample_count := 0;
else
sample_count := sample_count - 1;
end if;

new.test_count = sample_count + 1;
new.test_entry_from = (case when admission_type = 'OPD_ADMIT' and sample_count = 0 then 'OPD' else 'HOSPITAL' end);

if (NEW.lab_test_id is null and admission_type = 'OPD_ADMIT' and NEW.sample_health_infra_send_to is not null) then
		NEW.lab_test_id = upper(get_lab_test_code(-1,NEW.sample_health_infra_send_to,NEW.covid_admission_detail_id));
		update covid19_admission_detail set lab_test_id = NEW.lab_test_id  where id = NEW.covid_admission_detail_id and lab_test_id is null;
		NEW.lab_test_id = upper(concat_ws('/',NEW.lab_test_id,(case when sample_count > 0 then concat('R',sample_count)end)));
elseif (NEW.lab_test_id is null and admission_type = 'NEW' and  NEW.sample_health_infra_send_to is not null) then
NEW.lab_test_id = upper(get_lab_test_code(NEW.sample_health_infra,NEW.sample_health_infra_send_to,NEW.covid_admission_detail_id));
		update covid19_admission_detail set lab_test_id = NEW.lab_test_id  where id = NEW.covid_admission_detail_id and lab_test_id is null;
		NEW.lab_test_id = upper(concat_ws('/',NEW.lab_test_id,(case when sample_count > 0 then concat('R',sample_count)end)));

END if;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.covid19_lab_test_detail_insert_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 939 (class 1255 OID 220268)
-- Name: covid19_lab_test_recommendation_insert_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.covid19_lab_test_recommendation_insert_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
	NEW.search_text = concat_ws(' ',
		(select concat_ws(' ',
			first_name,
			middle_name,
			last_name,
			id,
                        mobile_number,
                        family_id,
                        dob
			)
			from imt_member
			where id = NEW.member_id
		),
	NEW.source,
	NEW.lab_test_status,
	(select name_in_english from health_infrastructure_details where id = NEW.refer_health_infra_id),
	(select concat_ws(' ', first_name, middle_name, last_name, contact_number, user_name)
                from um_user where id = NEW.reffer_by)
);
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.covid19_lab_test_recommendation_insert_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 940 (class 1255 OID 220269)
-- Name: create_entry_in_immunisation_archive(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.create_entry_in_immunisation_archive() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin

case when TG_OP = 'DELETE'
then INSERT INTO rch_immunisation_master_archive(id, member_id, member_type, visit_type, visit_id, notification_id,
            immunisation_given, given_on, given_by, created_by, created_on,
            modified_by, modified_on, family_id, location_id,
            pregnancy_reg_det_id)
    VALUES (old.id, old.member_id, old.member_type, old.visit_type, old.visit_id, old.notification_id,
            old.immunisation_given, old.given_on, old.given_by, old.created_by, old.created_on,
            old.modified_by, old.modified_on, old.family_id, old.location_id,
            old.pregnancy_reg_det_id);end case;
    return old;
end;
$$;


ALTER FUNCTION public.create_entry_in_immunisation_archive() OWNER TO postgres;

--
-- TOC entry 941 (class 1255 OID 220270)
-- Name: delete_rch_member_death_detail_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.delete_rch_member_death_detail_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
case when TG_OP = 'DELETE' then
    INSERT INTO rch_member_death_deatil_archive(
            id, member_id, family_id, dod, created_on, created_by, death_reason,
            place_of_death, location_id, other_death_reason)
    VALUES (OLD.id, OLD.member_id, OLD.family_id, OLD.dod, OLD.created_on, OLD.created_by, OLD.death_reason
    ,OLD.place_of_death,OLD.location_id,OLD.other_death_reason);
end case;
return old;
end;
$$;


ALTER FUNCTION public.delete_rch_member_death_detail_trigger_func() OWNER TO postgres;

--
-- TOC entry 942 (class 1255 OID 220271)
-- Name: get_lab_test_code(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_lab_test_code(source_id integer, dest_id integer, admission_id integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
		declare
			counter integer := (select count(*) from health_infra_lab_sample_id_master
								where source_infra = source_id and destination_infra = dest_id);
			code text;

			lab_test_id text := (select lab_test_id from covid19_admission_detail  where id  = admission_id);
        begin
			if lab_test_id is not null then
				return lab_test_id;
			end if;

	        	if counter > 0 then
				with t1 as(update health_infra_lab_sample_id_master set current_count = current_count + 1
	        					where source_infra = source_id and destination_infra = dest_id
	        					returning  destination_infra_code||'-'||source_infra_code||'-'||current_count)
	        		 (select * into code from t1);
				return code;
	        	else
				if source_id = -1 then
					with source_inf as (select 1 as "tmp_id",-1 as "id",'RND' as "substring"),

					dest_inf as (select 1 as "tmp_id",id,substring(name_in_english from 1 for 3) from
					health_infrastructure_details where id = dest_id),

					t as
					(insert into health_infra_lab_sample_id_master(source_infra, source_infra_code,
					destination_infra,destination_infra_code)
					select s.id,s.substring,d.id,d.substring from source_inf s,dest_inf d where s.tmp_id = d.tmp_id
					returning   destination_infra_code|| '-' || source_infra_code|| '-' || current_count)

					--t1 as (update health_infra_lab_sample_id_master set current_count = current_count + 1
       					--where source_infra = source_id and destination_infra = dest_id
       					--returning source_infra_code || '-' || destination_infra_code || '-' || current_count)
					select * into code from t;
					return code;
				else
					with source_inf as (select 1 as "tmp_id",id,substring(name_in_english from 1 for 3) from
						health_infrastructure_details where id = source_id),

						dest_inf as (select 1 as "tmp_id",id,substring(name_in_english from 1 for 3) from
						health_infrastructure_details where id = dest_id),

						t as
						(insert into health_infra_lab_sample_id_master(source_infra, source_infra_code,
						destination_infra,destination_infra_code)
						select s.id,s.substring,d.id,d.substring from source_inf s,dest_inf d where s.tmp_id = d.tmp_id
						returning   destination_infra_code|| '-' ||source_infra_code|| '-' || current_count)

						--t1 as (update health_infra_lab_sample_id_master set current_count = current_count + 1
						--where source_infra = source_id and destination_infra = dest_id
						--returning source_infra_code || '-' || destination_infra_code || '-' || current_count)
						select * into code from t;
					return code;
				end if;
	        	end if;
        end;
$$;


ALTER FUNCTION public.get_lab_test_code(source_id integer, dest_id integer, admission_id integer) OWNER TO postgres;

--
-- TOC entry 943 (class 1255 OID 220272)
-- Name: get_lab_test_id(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_lab_test_id() RETURNS text
    LANGUAGE plpgsql
    AS $$
        begin
                return 'L' || nextval('covid_lab_test_id_seq');
        end;
$$;


ALTER FUNCTION public.get_lab_test_id() OWNER TO postgres;

--
-- TOC entry 944 (class 1255 OID 220273)
-- Name: get_location(bigint, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location(loc_id bigint, u_depth integer) RETURNS TABLE(child_id bigint, parent_id bigint, depth integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
 RETURN QUERY SELECT
   lhcd.child_id, lhcd.parent_id,lhcd.depth
 FROM
   location_hierchy_closer_det lhcd
 WHERE
    (lhcd.parent_id = loc_id)
    and (u_depth is null or lhcd.depth = u_depth);
END; $$;


ALTER FUNCTION public.get_location(loc_id bigint, u_depth integer) OWNER TO postgres;

--
-- TOC entry 945 (class 1255 OID 220274)
-- Name: get_location_child_by_prent_depth(bigint, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_child_by_prent_depth(loc_id bigint, p_depth integer) RETURNS TABLE(child_id bigint, parent_id bigint, depth integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
 RETURN QUERY SELECT
   lhcd.child_id, lhcd.parent_id,lhcd.depth
 FROM
   location_hierchy_closer_det lhcd
 WHERE
    lhcd.parent_id in (select lh.child_id from location_hierchy_closer_det lh where lh.parent_id = loc_id and lh."depth" = p_depth);
END; $$;


ALTER FUNCTION public.get_location_child_by_prent_depth(loc_id bigint, p_depth integer) OWNER TO postgres;

--
-- TOC entry 946 (class 1255 OID 220275)
-- Name: get_location_hierarchy(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy(location_id bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		return (select string_agg(l2.name,' > ' order by lhcd.depth desc) as location_id
				from location_master l1
				inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id
				inner join location_master l2 on l2.id = lhcd.parent_id
				where l1.id = location_id);

        END;
$$;


ALTER FUNCTION public.get_location_hierarchy(location_id bigint) OWNER TO postgres;

--
-- TOC entry 947 (class 1255 OID 220276)
-- Name: get_location_hierarchy_by_type(bigint, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy_by_type(location_id bigint, type character varying) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		if(type = 'D' or type = 'C') then

		return (select parent.name from  location_hierchy_closer_det lhcd
				inner join location_master parent on parent.id = lhcd.parent_id
				where lhcd.child_id = location_id
				and (parent_loc_type = 'D' or (parent_loc_type='C')));
		end if;

		if(type = 'B' or type = 'Z') then

		return (select parent.name from  location_hierchy_closer_det lhcd
				inner join location_master parent on parent.id = lhcd.parent_id
				where lhcd.child_id = location_id
				and (parent_loc_type = 'B' or (parent_loc_type='Z')));
		end if;

		if(type = 'P' or type = 'U') then

		return (select parent.name from  location_hierchy_closer_det lhcd
				inner join location_master parent on parent.id = lhcd.parent_id
				where lhcd.child_id = location_id
				and (parent_loc_type = 'P' or (parent_loc_type='U')));
		end if;

		if(type = 'SC') then

		return (select parent.name from  location_hierchy_closer_det lhcd
				inner join location_master parent on parent.id = lhcd.parent_id
				where lhcd.child_id = location_id
				and parent_loc_type = 'SC');
		end if;

        END;
$$;


ALTER FUNCTION public.get_location_hierarchy_by_type(location_id bigint, type character varying) OWNER TO postgres;

--
-- TOC entry 948 (class 1255 OID 220277)
-- Name: get_location_hierarchy_from_district(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy_from_district(location_id bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		return (select string_agg(l2.name,' > ' order by lhcd.depth desc) as location_id
				from location_master l1
				inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id
				inner join location_master l2 on l2.id = lhcd.parent_id
				where l1.id = location_id and lhcd.parent_loc_type not in ('S','R'));

        END;
$$;


ALTER FUNCTION public.get_location_hierarchy_from_district(location_id bigint) OWNER TO postgres;

--
-- TOC entry 949 (class 1255 OID 220278)
-- Name: get_location_hierarchy_from_ditrict(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy_from_ditrict(location_id bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		return (select string_agg(l2.name,' > ' order by lhcd.depth desc) as location_id
				from location_master l1
				inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id
				inner join location_master l2 on l2.id = lhcd.parent_id
				where l1.id = location_id and lhcd.parent_loc_type not in ('S','R'));

        END;
$$;


ALTER FUNCTION public.get_location_hierarchy_from_ditrict(location_id bigint) OWNER TO postgres;

--
-- TOC entry 950 (class 1255 OID 220279)
-- Name: get_location_hierarchy_language_wise(bigint, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy_language_wise(location_id bigint, language character varying) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		return (select string_agg(case when ( language = 'EN' and (l2.english_name is not null)) then l2.english_name else l2.name end,' > ' order by lhcd.depth desc) as location_id
				from location_master l1
				inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id
				inner join location_master l2 on l2.id = lhcd.parent_id
				where l1.id = location_id);

        END;

$$;


ALTER FUNCTION public.get_location_hierarchy_language_wise(location_id bigint, language character varying) OWNER TO postgres;

--
-- TOC entry 951 (class 1255 OID 220280)
-- Name: get_location_hierarchy_without_region_english(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_location_hierarchy_without_region_english(location_id bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	hierarchy text;

        BEGIN
		return (select string_agg(l2.english_name,' , ' order by lhcd.depth desc) as location_id
				from location_master l1
				inner join location_hierchy_closer_det lhcd on lhcd.child_id = l1.id
				inner join location_master l2 on l2.id = lhcd.parent_id
				where l1.id = location_id and lhcd.parent_loc_type not in ('R','A','AA'));

        END;
$$;


ALTER FUNCTION public.get_location_hierarchy_without_region_english(location_id bigint) OWNER TO postgres;

--
-- TOC entry 952 (class 1255 OID 220281)
-- Name: get_sd_score(text, integer, real); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_sd_score(gender1 text, height1 integer, weight1 real) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	days integer;
	months integer;
	weeks integer;
	sd sd_score_master;
        BEGIN
		--the for loop here is just for storing the tuple from sd_score_master into a variable
		--the query below will always return only a single row
		for sd  in(select * from sd_score_master sdm where sdm.height = height1 and sdm.gender = gender1 limit 1)
		loop
		case
			when weight1 <= sd.minus4
				then return 'SD4';
			when weight1 <= sd.minus3
				then return 'SD3';
			when weight1 <= sd.minus2
				then return 'SD2';
			when weight1 <= sd.minus1
				then return 'SD1';
			when weight1 <= sd.median
				then return 'MEDIAN';
			else
				return 'NONE';

		end case;
		end loop;
		return 'NONE';
        END;
$$;


ALTER FUNCTION public.get_sd_score(gender1 text, height1 integer, weight1 real) OWNER TO postgres;

--
-- TOC entry 953 (class 1255 OID 220282)
-- Name: get_vaccination_string(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_vaccination_string(mid bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	vaccination_string text;

        BEGIN
		return (select string_agg(concat(rch_immunisation_master.immunisation_given,
			case when rch_immunisation_master.immunisation_given is null then null else '#' end,
			to_char(rch_immunisation_master.given_on,'DD/MM/YYYY')),
			',' order by rch_immunisation_master.given_on) as immunisation
			from imt_member
			left join rch_immunisation_master on rch_immunisation_master.member_id = imt_member.id
			where imt_member.id = mid
			group by imt_member.id);

        END;
$$;


ALTER FUNCTION public.get_vaccination_string(mid bigint) OWNER TO postgres;

--
-- TOC entry 954 (class 1255 OID 220283)
-- Name: get_vaccine_status(date, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.get_vaccine_status(dob date, mem bigint) RETURNS TABLE(vac_name character varying, status text)
    LANGUAGE plpgsql
    AS $$
	DECLARE
	days integer;
	months integer;
	weeks integer;
        BEGIN
		days := current_date - dob;
		months := extract (year from age(dob)) * 12 + extract (month from age(dob));
		weeks := (days/7)::integer;
		RETURN QUERY
		select cvs.name,
		case when vac.immunisation_given is not null then 'given'
		when vac.immunisation_given is null and ((from_type = 'D' and days >= from_count and days <= to_count) or
		(from_type = 'W' and weeks >= from_count and weeks <= to_count) or (from_type = 'M' and months >= from_count and months <= to_count)) then 'not_given'
		when vac.immunisation_given is null and ((from_type = 'D' and days < from_count) or
		(from_type = 'W' and weeks < from_count) or (from_type = 'M' and months < from_count )) then 'to_be_given'
		else 'missed' end as status
		from child_vac_schedule cvs left join
		(select immunisation_given from rch_immunisation_master where member_id = mem) vac
		on vac.immunisation_given = cvs.name;
        END;
$$;


ALTER FUNCTION public.get_vaccine_status(dob date, mem bigint) OWNER TO postgres;

--
-- TOC entry 955 (class 1255 OID 220284)
-- Name: getancdates(date, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.getancdates(lmp date, arr text, OUT anc1 text, OUT anc2 text, OUT anc3 text, OUT anc4 text) RETURNS record
    LANGUAGE plpgsql
    AS $$
declare
	arr_date text[];
	dt date;
	dif double precision;
begin
  ANC1 := 'NA';
  ANC2 := 'NA';
  ANC3 := 'NA';
  ANC4 := 'NA';
  if arr is null then
  	return;
  end if;
  arr_date :=  string_to_array (arr,',');
  FOR i IN 1.. array_upper(arr_date, 1)
  LOOP
    dt := to_date(arr_date[i],'DD/MM/YYYY');
    dif := DATE_PART('day',  dt::timestamp - lmp::timestamp );
    if dif >= 0 and dif <= 91 then
    	ANC1 := dt;
    elsif dif >= 92 and dif <= 182 then
    	ANC2 := dt;
    elsif dif >= 183 and dif <= 245 then
    	ANC3 :=dt;
    elseif dif >= 246 then
    	ANC4 := dt;
    end if;
  END LOOP;
end;
$$;


ALTER FUNCTION public.getancdates(lmp date, arr text, OUT anc1 text, OUT anc2 text, OUT anc3 text, OUT anc4 text) OWNER TO postgres;

--
-- TOC entry 956 (class 1255 OID 220285)
-- Name: getlocationbylatlong(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.getlocationbylatlong(lat text, lon text) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE passed BOOLEAN;
DECLARE ldgCode text;
BEGIN
	IF (lat = 'null' or lat is null or lon = 'null' or lon is null or lon::float <= 0 or lat::float <= 0)
	THEN
		return 0;
	END IF;
	SELECT  lgd_code  into ldgCode
		FROM location_geo_coordinates
		where st_contains(geom, ST_GeomFromText(CONCAT('POINT(',lon, ' ',lat, ')'),4326)) = true limit 1 ;
	return (select id from location_master where lgd_code = ldgCode and (type='V' or type = 'B' or type='D' or type='C' or type='ANM') limit 1);

END;
$$;


ALTER FUNCTION public.getlocationbylatlong(lat text, lon text) OWNER TO postgres;

--
-- TOC entry 957 (class 1255 OID 220286)
-- Name: getlocationbylatlongdist(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.getlocationbylatlongdist(lat text, lon text) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
DECLARE passed BOOLEAN;
--DECLARE geom1 geometry;
BEGIN
/*	IF (lat = 'null' or lat is null or lon = 'null' or lon is null or lon::float <= 0 or lat::float <= 0)
	THEN
		return 0;
	END IF;
	SELECT  geom  into geom1
		FROM location_geo_coordinates
		where st_contains(geom, ST_GeomFromText(CONCAT('POINT(',lat, ' ',lon, ')'),4326)) = true limit 1 ;
	return ST_Distance(geom1, ST_GeomFromText(CONCAT('POINT(71.53786519 21.11840804)'),4326));*/

END;
$$;


ALTER FUNCTION public.getlocationbylatlongdist(lat text, lon text) OWNER TO postgres;

--
-- TOC entry 958 (class 1255 OID 220287)
-- Name: getlocationbylatlongdist(text, text, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.getlocationbylatlongdist(lat text, lon text, locationid bigint) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
DECLARE passed BOOLEAN;
DECLARE lgd_code_temp text;
DECLARE geomTemp geometry;
BEGIN
	IF (lat = 'null' or lat is null or lon = 'null' or lon is null or lon::float <= 0 or lat::float <= 0)
	THEN
		return 0;
	END IF;
	select lgd_code into lgd_code_temp from location_master where id  in (select parent_id from location_hierchy_closer_det where child_id = locationId and parent_loc_type='V' limit 1);
	SELECT  geom  into geomTemp
		FROM location_geo_coordinates
		where lgd_code=lgd_code_temp limit 1;
	return ST_Distance(st_centroid(geomTemp), ST_GeomFromText(CONCAT('POINT(',lon, ' ',lat, ')'),4326),true)/1000.0;

END;
$$;


ALTER FUNCTION public.getlocationbylatlongdist(lat text, lon text, locationid bigint) OWNER TO postgres;

--
-- TOC entry 959 (class 1255 OID 220288)
-- Name: if_modified_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.if_modified_func() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    SET search_path TO 'pg_catalog', 'public'
    AS $$
DECLARE
    audit_row public.logged_actions;
    include_values boolean;
    log_diffs boolean;
    h_old hstore;
    h_new hstore;
    excluded_cols text[] = ARRAY[]::text[];
BEGIN
    IF TG_WHEN <> 'AFTER' THEN
        RAISE EXCEPTION 'public.if_modified_func() may only run as an AFTER trigger';
    END IF;

    audit_row = ROW(
        nextval('public.logged_actions_event_id_seq'), -- event_id
        TG_TABLE_SCHEMA::text,                        -- schema_name
        TG_TABLE_NAME::text,                          -- table_name
        TG_RELID,                                     -- relation OID for much quicker searches
        session_user::text,                           -- session_user_name
        current_timestamp,                            -- action_tstamp_tx
        statement_timestamp(),                        -- action_tstamp_stm
        clock_timestamp(),                            -- action_tstamp_clk
        txid_current(),                               -- transaction ID
        current_setting('application_name'),          -- client application
        inet_client_addr(),                           -- client_addr
        inet_client_port(),                           -- client_port
        current_query(),                              -- top-level query or queries (if multistatement) from client
        substring(TG_OP,1,1),                         -- action
        NULL, NULL,                                   -- row_data, changed_fields
        'f'                                        -- statement_only
        );

    IF NOT TG_ARGV[0]::boolean IS DISTINCT FROM 'f'::boolean THEN
        audit_row.client_query = NULL;
    END IF;

    IF TG_ARGV[1] IS NOT NULL THEN
        excluded_cols = TG_ARGV[1]::text[];
    END IF;

    IF (TG_OP = 'UPDATE' AND TG_LEVEL = 'ROW') THEN
        audit_row.row_data = hstore(OLD.*) - excluded_cols;
        audit_row.changed_fields =  (hstore(NEW.*) - audit_row.row_data) - excluded_cols;
        IF audit_row.changed_fields = hstore('') THEN
            -- All changed fields are ignored. Skip this update.
            RETURN NULL;
        END IF;
    ELSIF (TG_OP = 'DELETE' AND TG_LEVEL = 'ROW') THEN
        audit_row.row_data = hstore(OLD.*) - excluded_cols;
    ELSIF (TG_OP = 'INSERT' AND TG_LEVEL = 'ROW') THEN
        audit_row.row_data = hstore(NEW.*) - excluded_cols;
    ELSIF (TG_LEVEL = 'STATEMENT' AND TG_OP IN ('INSERT','UPDATE','DELETE','TRUNCATE')) THEN
        audit_row.statement_only = 't';
    ELSE
        RAISE EXCEPTION '[public.if_modified_func] - Trigger func added as trigger for unhandled case: %, %',TG_OP, TG_LEVEL;
        RETURN NULL;
    END IF;
    INSERT INTO public.logged_actions VALUES (audit_row.*);
    RETURN NULL;
END;
$$;


ALTER FUNCTION public.if_modified_func() OWNER TO postgres;

--
-- TOC entry 6463 (class 0 OID 0)
-- Dependencies: 959
-- Name: FUNCTION if_modified_func(); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.if_modified_func() IS '
Track changes to a table at the statement and/or row level.

Optional parameters to trigger in CREATE TRIGGER call:

param 0: boolean, whether to log the query text. Default ''t''.

param 1: text[], columns to ignore in updates. Default [].

         Updates to ignored cols are omitted from changed_fields.

         Updates with only ignored cols changed are not inserted
         into the audit log.

         Almost all the processing work is still done for updates
         that ignored. If you need to save the load, you need to use
         WHEN clause on the trigger instead.

         No warning or error is issued if ignored_cols contains columns
         that do not exist in the target table. This lets you specify
         a standard set of ignored columns.

There is no parameter to disable logging of values. Add this trigger as
a ''FOR EACH STATEMENT'' rather than ''FOR EACH ROW'' trigger if you do not
want to log row values.

Note that the user name logged is the login role for the session. The audit trigger
cannot obtain the active role because it is reset by the SECURITY DEFINER invocation
of the audit trigger its self.
';


--
-- TOC entry 960 (class 1255 OID 220289)
-- Name: imt_family_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.imt_family_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	if OLD.location_id != -1 and NEW.location_id<>OLD.location_id then
	insert into imt_family_location_change_detail(family_id,from_location_id,to_location_id,created_on,created_by)
	values(OLD.family_id,OLD.location_id,NEW.location_id,now(),NEW.modified_by);
	end if;
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.imt_family_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 961 (class 1255 OID 220290)
-- Name: imt_member_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.imt_member_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO imt_member_archive
    SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.imt_member_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 962 (class 1255 OID 220291)
-- Name: insert_in_gvk_family_migration_info(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.insert_in_gvk_family_migration_info() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin

if NEW.state = 'REPORTED'
and NEW.type = 'OUT' and (NEW.out_of_state = false or NEW.out_of_state is null)
and NEW.migrated_location_not_known = true
 then
	insert into gvk_family_migration_info (family_migration_id , created_by ,  created_on , modified_by , modified_on , gvk_call_status  , call_attempt , schedule_date )
	Values ( NEW.id,  -1  , now() , -1  ,now() , 'com.argusoft.imtecho.gvk.call.to-be-processed' , 0 , now() );
end if;
return new;
end;
$$;


ALTER FUNCTION public.insert_in_gvk_family_migration_info() OWNER TO postgres;

--
-- TOC entry 963 (class 1255 OID 220292)
-- Name: insert_uuid_in_report_master_uuid(integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.insert_uuid_in_report_master_uuid(query_master_id integer, isfieldcontainer boolean) RETURNS text
    LANGUAGE plpgsql
    AS $_$declare	final_json jsonb;begin	 if isFieldContainer	 	then 				with cte as (					select id					 ,json_array_elements(config_json::json->'containers' -> 'fieldsContainer') ->> 'queryIdForParam' as query_param_id					,jsonb_array_elements(jsonb_extract_path(config_json::jsonb, 'containers','fieldsContainer')::jsonb) "objects" from report_master					where id = $1 and config_json::jsonb -> 'containers' -> 'fieldsContainer' is not null				)				,final_array as (					select jsonb_build_array(d) "array_data" from 					(					select array_agg(objects::jsonb || jsonb_build_object('queryUUIDForParam',(master.uuid))) "fieldsContainer" from cte					left join report_query_master master on cast(master.id as text) = query_param_id					)d				)								update report_master set config_json = (					select jsonb_set(				 config_json::jsonb,				 '{containers,fieldsContainer}', (f.array_data -> 0 -> 'fieldsContainer'),false)				 	from report_master, final_array f				 	where id = $1)				 	where id = $1;		else				with table_config as (					select id					 ,json_array_elements(config_json::json->'containers' -> 'tableContainer') ->> 'queryId' as table_query_id					,jsonb_array_elements(jsonb_extract_path(config_json::jsonb, 'containers','tableContainer')::jsonb) "table_object" from report_master					where id = $1 and config_json::jsonb -> 'containers' -> 'tableContainer' is not null				)				,update_table_entry as (					select jsonb_build_array(d) "array_data" from 					(					select array_agg(table_object::jsonb || jsonb_build_object('queryUUID',(master.uuid))) "tableContainer" from table_config					left join report_query_master master on cast(master.id as text) = table_query_id					)d				)				update report_master set config_json = (				select jsonb_set(			 config_json::jsonb,			 '{containers,tableContainer}', (f.array_data -> 0 -> 'tableContainer'),false)			 	from report_master, update_table_entry f			 	where id = $1)			 	where id = $1;			end if;			 	select config_json into final_json from report_master where id = $1;			 	RETURN final_json;		 exception	 	when others THEN RAISE NOTICE 'Error Occured while inserting UUID, % ', $1 ;		select cast($1 as text) into final_json;		RETURN final_json;	 	 	END;$_$;


ALTER FUNCTION public.insert_uuid_in_report_master_uuid(query_master_id integer, isfieldcontainer boolean) OWNER TO postgres;

--
-- TOC entry 964 (class 1255 OID 220293)
-- Name: insert_uuid_in_report_master_uuid_for_query_id_specific(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.insert_uuid_in_report_master_uuid_for_query_id_specific(query_master_id integer) RETURNS text
    LANGUAGE plpgsql
    AS $_$
declare
	final_json jsonb;
begin

	with cte as (
		select id
		 ,json_array_elements(config_json::json->'containers' -> 'fieldsContainer') ->> 'queryId' as query_param_id
		,jsonb_array_elements(jsonb_extract_path(config_json::jsonb, 'containers','fieldsContainer')::jsonb) "objects" from report_master
		where id = $1 and config_json::jsonb -> 'containers' -> 'fieldsContainer' is not null
	)
	,final_array as (
		select jsonb_build_array(d) "array_data" from
		(
		select array_agg(objects::jsonb || jsonb_build_object('queryUUID',(master.uuid))) "fieldsContainer" from cte
		left join report_query_master master on cast(master.id as text) = query_param_id
	--	where cte.query_param_id is not null
		)d
	)


	update report_master set config_json = (
		select jsonb_set(
	        config_json::jsonb,
	        '{containers,fieldsContainer}', (f.array_data -> 0 -> 'fieldsContainer'),false)
	 	from report_master, final_array f
	 	where id = $1)
	 	where id = $1;

	select config_json into final_json  from report_master where id = $1;

	RETURN final_json;

 	exception
 		when others THEN RAISE NOTICE 'Error Occured while inserting UUID, % ', $1 ;
		select cast($1 as text) into final_json;
		RETURN final_json;


	END;
$_$;


ALTER FUNCTION public.insert_uuid_in_report_master_uuid_for_query_id_specific(query_master_id integer) OWNER TO postgres;

--
-- TOC entry 965 (class 1255 OID 220294)
-- Name: internationalization_label_master_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.internationalization_label_master_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
PERFORM dblink_exec
(
(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
'INSERT INTO public.internationalization_label_master(
country, key, language, created_by, created_on, custom3b, text,
translation_pending, modified_on, app_name)
VALUES('|| quote_nullable(NEW.country) || '
, '||quote_nullable(NEW.key) ||'
, '||quote_nullable(NEW.language) ||'
, '||quote_nullable(NEW.created_by) ||'
, '||quote_nullable(NEW.created_on) ||'
, '||quote_nullable(NEW.custom3b) ||'
, '||quote_nullable(NEW.text) ||'
, '||quote_nullable(NEW.translation_pending) ||'
, '||quote_nullable(NEW.modified_on) ||'
, '||quote_nullable(NEW.app_name) ||');'
        );
end if;

RETURN NEW;
END;
$$;


ALTER FUNCTION public.internationalization_label_master_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 967 (class 1255 OID 220295)
-- Name: internationalization_label_master_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.internationalization_label_master_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
PERFORM dblink_exec
(
(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
'UPDATE internationalization_label_master
SET country='|| quote_nullable(NEW.country) || ', key='|| quote_nullable(NEW.key) || ',
language='|| quote_nullable(NEW.language) || ', created_by='|| quote_nullable(NEW.created_by) || ',
created_on='|| quote_nullable(NEW.created_on) || ', custom3b='|| quote_nullable(NEW.custom3b) || ',
modified_on='|| quote_nullable(NEW.modified_on) || ',
text='|| quote_nullable(NEW.text) || ', translation_pending='|| quote_nullable(NEW.translation_pending) || ',
app_name='|| quote_nullable(NEW.app_name) || '
WHERE key='|| quote_nullable(NEW.key) || ' and country='|| quote_nullable(NEW.country) || ' and app_name='|| quote_nullable(NEW.app_name) || '  and language = '|| quote_nullable(NEW.language) || ';'
        );
end if;

RETURN NEW;
END;
$$;


ALTER FUNCTION public.internationalization_label_master_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 968 (class 1255 OID 220296)
-- Name: isinteger(numeric); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.isinteger(numeric) RETURNS boolean
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE x NUMERIC;
BEGIN
    x = $1::integer;
    RETURN TRUE;
EXCEPTION WHEN others THEN
    RETURN FALSE;
END;
$_$;


ALTER FUNCTION public.isinteger(numeric) OWNER TO postgres;

--
-- TOC entry 969 (class 1255 OID 220297)
-- Name: isinteger(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.isinteger(text) RETURNS boolean
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE x NUMERIC;
BEGIN
    x = $1::integer;
    RETURN TRUE;
EXCEPTION WHEN others THEN
    RETURN FALSE;
END;
$_$;


ALTER FUNCTION public.isinteger(text) OWNER TO postgres;

--
-- TOC entry 971 (class 1255 OID 220299)
-- Name: location_master_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.location_master_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ BEGIN
INSERT INTO public.location_hierchy_closer_det(
        child_id,
        child_loc_type,
        depth,
        parent_id,
        parent_loc_type
    )
VALUES (NEW.id, NEW.type, 0, NEW.id, NEW.type);
if NEW.parent is not null then
INSERT INTO public.location_hierchy_closer_det(
        child_id,
        child_loc_type,
        depth,
        parent_id,
        parent_loc_type
    )
SELECT c.child_id,
    c.child_loc_type,
    p.depth + c.depth + 1,
    p.parent_id,
    p.parent_loc_type
FROM location_hierchy_closer_det p,
    location_hierchy_closer_det c
WHERE p.child_id = NEW.parent
    AND c.parent_id = NEW.id;
END if;
insert into location_wise_analytics (loc_id)
VALUES (NEW.id);
if (
    select case
            when key_value = 'P' then true
            else false
        end
    from system_configuration
    where system_key = 'SERVER_TYPE'
) then
PERFORM dblink_exec (
    'dbname=' ||(
        select key_value
        from system_configuration
        where system_key = 'TRAINING_DB_NAME'
    ),
    'INSERT INTO location_master(
            id, address, associated_user, contact1_email, contact1_name,
            contact1_phone, contact2_email, contact2_name, contact2_phone,
            created_by, created_on, is_active, is_archive, max_users, modified_by,
            modified_on, name,english_name, pin_code, type, unique_id, parent, is_tba_avaiable,
            total_population, location_code, state)
	       Values (' || quote_nullable(NEW.id) || '
			, ' || quote_nullable(NEW.address) || '
			, ' || quote_nullable(NEW.associated_user) || '
			, ' || quote_nullable(NEW.contact1_email) || '
			, ' || quote_nullable(NEW.contact1_name) || '
			, ' || quote_nullable(NEW.contact1_phone) || '
			, ' || quote_nullable(NEW.contact2_email) || '
			, ' || quote_nullable(NEW.contact2_name) || '
			, ' || quote_nullable(NEW.contact2_phone) || '
			, ' || quote_nullable(NEW.created_by) || '
			, ' || quote_nullable(NEW.created_on) || '
			, ' || quote_nullable(NEW.is_active) || '
			, ' || quote_nullable(NEW.is_archive) || '
			, ' || quote_nullable(NEW.max_users) || '
			, ' || quote_nullable(NEW.modified_by) || '
			, ' || quote_nullable(NEW.modified_on) || '
			, ' || quote_nullable(NEW.name) || '
			, ' || quote_nullable(NEW.english_name) || '
			, ' || quote_nullable(NEW.pin_code) || '
			, ' || quote_nullable(NEW.type) || '
			, ' || quote_nullable(NEW.unique_id) || '
			, ' || quote_nullable(NEW.parent) || '
			, ' || quote_nullable(NEW.is_tba_avaiable) || '
			, ' || quote_nullable(NEW.total_population) || '
			, ' || quote_nullable(NEW.location_code) || '
			, ' || quote_nullable(NEW.state) || ');'
);
end if;
if new.type = 'SC'
or new.type = 'P' then
INSERT INTO rch_institution_master(name, location_id, type, is_location, state)
VALUES (new.name, new.id, new.type, true, 'active');
end if;
if new.type = 'SC'
or new.type = 'P'
or new.type = 'U'
or new.type = 'CHC' then
INSERT INTO health_infrastructure_details(
        location_id,
        type,
        name,
        state,
        created_on,
        created_by,
        modified_on,
        modified_by
    )
values(
        New.id,
        (
            select id
            from listvalue_field_value_detail
            where field_key = 'infra_type'
                and code = new.type
        ),
        new.name,
        'ACTIVE',
        new.created_on,
        cast(new.created_by as bigint),
        new.modified_on,
        cast(new.modified_by as bigint)
    );
end if;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.location_master_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 972 (class 1255 OID 220300)
-- Name: location_master_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.location_master_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	if NEW.type <> OLD.type then
		update location_hierchy_closer_det set child_loc_type = NEW.type where child_id = NEW.id;
		update location_hierchy_closer_det set parent_loc_type = NEW.type where parent_id = NEW.id;
	end if;

	if OLD.parent is null and NEW.parent is not null then
		INSERT INTO location_hierchy_closer_det
			(child_id, child_loc_type, depth, parent_id, parent_loc_type)
		select
			c.child_id,c.child_loc_type,
			p.depth + c.depth + 1,
			p.parent_id,
			p.parent_loc_type
		FROM
			location_hierchy_closer_det p,
			location_hierchy_closer_det c
		WHERE
			p.child_id = NEW.parent
			AND c.parent_id = NEW.id;
	end if;

	if OLD.parent is not null and NEW.parent <> OLD.parent then
	update location_hierchy_closer_det lh set parent_id = t.parent_id ,parent_loc_type = t.parent_loc_type,parent_loc_demographic_type = t.parent_loc_demographic_type
	from (
		select d.parent_id ,d.parent_loc_type ,d.parent_loc_demographic_type,c.id
		from location_hierchy_closer_det p , location_hierchy_closer_det c , location_hierchy_closer_det d
		where p.parent_id = new.id and p.child_id = c.child_id and d.child_id = new.parent and (c."depth" - p."depth" -1) = d."depth"
	) t where t.id =lh.id and lh.parent_id != t.parent_id ;

		/*--update location_hierchy_closer_det set parent_id = NEW.parent,parent_loc_type = (select type from location_master where id =NEW.parent)
		where id in (select id from location_hierchy_closer_det where child_id in (select child_id from location_hierchy_closer_det where parent_id = NEW.id) and parent_id =OLD.parent);

--		update location_hierchy_closer_det closer1 set parent_id = closer2.parent_id from location_hierchy_closer_det closer2
--	    where closer1.child_id = NEW.id and closer1.depth > 0 and closer2.child_id = new.parent
--	    and closer1.depth = closer2.depth+1 and closer1.parent_id <> closer2.parent_id
--	    and closer1.parent_id not in (-1, -2) and closer2.parent_id not in (-1, -2);

		/* query to update location_hierchy_closer_det table
		update
			location_hierchy_closer_det closer1
		set
			parent_id = closer2.parent_id
		from
			location_hierchy_closer_det closer2
		inner join
			location_type_master type2 on type2."type" = closer2.parent_loc_type
        where
        	closer1.child_id in (select child_id from location_hierchy_closer_det where child_id in (select child_id from location_hierchy_closer_det where parent_id = NEW.id) and parent_id =OLD.parent)
        	and closer1.depth > 0 and closer2.child_id = new.parent
	        and (select level from location_type_master where type = closer1.parent_loc_type) = type2."level"
        	and closer1.parent_id <> closer2.parent_id
        	and closer1.parent_id not in (-1, -2)
        	and closer2.parent_id not in (-1, -2);
        	*/

	-- DELETE EXISTING RECORDS FROM CLOSURE
		delete from location_hierchy_closer_det where child_id  = new.id;
	   	--INSERT FIRST RECORD OF LOCATION (SAME PARENT, SAME CHILD)
		INSERT INTO public.location_hierchy_closer_det(
            child_id, child_loc_type, depth, parent_id, parent_loc_type)
    	VALUES ( new.id, new.type, 0, new.id, new.type);
    	--INSERT ALL OTHER PARENTS OF LOCATION
	   	INSERT INTO public.location_hierchy_closer_det(
            child_id, child_loc_type, depth, parent_id, parent_loc_type)
		SELECT  c.child_id,c.child_loc_type, p.depth+c.depth+1,p.parent_id,p.parent_loc_type
		FROM location_hierchy_closer_det p, location_hierchy_closer_det c
		WHERE p.child_id = new.parent AND c.parent_id = new.id and c.child_id = new.id;
        */
	end if;


	if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
		'UPDATE location_master
		SET created_by='|| quote_nullable(NEW.created_by) || ', created_on='|| quote_nullable(NEW.created_on) || ',
		modified_by='|| quote_nullable(NEW.modified_by) || ', modified_on='|| quote_nullable(NEW.modified_on) || ',
		address='|| quote_nullable(NEW.address) || ', associated_user='|| quote_nullable(NEW.associated_user) || ',
		contact1_email='|| quote_nullable(NEW.contact1_email) || ', contact1_name='|| quote_nullable(NEW.contact1_name) || ',
	        contact1_phone='|| quote_nullable(NEW.contact1_phone) || ', contact2_email='|| quote_nullable(NEW.contact2_email) || ',
	        contact2_name='|| quote_nullable(NEW.contact2_name) || ',
	        contact2_phone='|| quote_nullable(NEW.contact2_phone) || ', is_active='|| quote_nullable(NEW.is_active) || ',
	        is_archive='|| quote_nullable(NEW.is_archive) || ', max_users='|| quote_nullable(NEW.max_users) || ',
	        name='|| quote_nullable(NEW.name) || ',english_name='|| quote_nullable(NEW.english_name) || ', pin_code='|| quote_nullable(NEW.pin_code) || ',
	        type='|| quote_nullable(NEW.type) || ', unique_id='|| quote_nullable(NEW.unique_id) || ',
	        parent='|| quote_nullable(NEW.parent) || ', is_tba_avaiable='|| quote_nullable(NEW.is_tba_avaiable) || ',
	        total_population='|| quote_nullable(NEW.total_population) || ',
	        location_code='|| quote_nullable(NEW.location_code) || ', state='|| quote_nullable(NEW.state) || '
		WHERE id='|| quote_nullable(NEW.id) || ';'

        );
	end if;

	if old.type='SC' or old.type='P' then
			update rch_institution_master set name=new.name where location_id=old.id and is_location = true;
	end if;

	if NEW.type in ('A','AA') and NEW.parent!=OLD.parent then
		update imt_family set location_id = NEW.parent,modified_on = now() where area_id = NEW.id;
	end if;

	RETURN NEW;
END;
$$;


ALTER FUNCTION public.location_master_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 973 (class 1255 OID 220302)
-- Name: migration_master_insert_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.migration_master_insert_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

IF TG_OP = 'INSERT' or NEW.state != OLD.state then
IF NEW.state in ('NOT_RESOLVED','REPORTED') and (NEW.out_of_state is null or NEW.out_of_state = false) then

INSERT INTO public.techo_web_notification_master(
            notification_type_id, location_id,
            member_id,user_id, escalation_level_id, schedule_date,
            state, created_by, created_on,
            modified_by, modified_on, ref_code, notification_type_escalation_id
            )
select notification_type.notification_type_id,migration_master.reported_location_id,migration_master.member_id,migration_master.reported_by,
notification_type.id,migration_master.created_on,'PENDING',-1,now(),-1,now(),migration_master.id,notification_type.notification_type_id||'_'||notification_type.id
from
(select id,notification_type_id from escalation_level_master where name = 'Default'
and notification_type_id = (select id from notification_type_master where code = (case when NEW.type = 'IN' then 'migration_pending_in' else 'migration_pending_out' end))) as notification_type
inner join
migration_master on migration_master.id = NEW.id
left join techo_web_notification_master techo_web on techo_web.notification_type_id = notification_type.notification_type_id and techo_web.location_id = migration_master.reported_location_id and techo_web.ref_code = migration_master.id and techo_web.state = 'PENDING'
where techo_web.id is null;
else
update techo_web_notification_master set state = 'COMPLETED',modified_on = now(),modified_by = NEW.modified_by
where id in (
select id from techo_web_notification_master techo_web where techo_web.notification_type_id = (select id from notification_type_master where code = (case when NEW.type = 'IN' then 'migration_pending_in' else 'migration_pending_out' end))
and techo_web.location_id = NEW.reported_location_id and techo_web.ref_code = NEW.id and techo_web.state = 'PENDING'
);

end if;

IF NEW.state in ('REPORTED') and (NEW.out_of_state is null or NEW.out_of_state = false)
and  NEW.location_migrated_from is not null
and NEW.location_migrated_to is not null
then
INSERT INTO public.techo_web_notification_master(
            notification_type_id, location_id
            member_id,user_id, escalation_level_id, schedule_date,
            state, created_by, created_on,
            modified_by, modified_on, ref_code, notification_type_escalation_id
            )
select notification_type.notification_type_id,case when NEW.type = 'IN' then NEW.location_migrated_from else NEW.location_migrated_to end,migration_master.member_id,null,
notification_type.id,migration_master.created_on,'PENDING',migration_master.created_by,now(),migration_master.modified_by,now(),migration_master.id,notification_type.notification_type_id||'_'||notification_type.id
from
(select id,notification_type_id from escalation_level_master where name = 'Default'
and notification_type_id = (select id from notification_type_master where code = 'migration_response_pending')) as notification_type
inner join
migration_master on migration_master.id = NEW.id
left join techo_web_notification_master techo_web on techo_web.notification_type_id = notification_type.notification_type_id and techo_web.location_id = migration_master.reported_location_id and techo_web.ref_code = migration_master.id and techo_web.state = 'PENDING'
where techo_web.id is null;
else
update techo_web_notification_master set state = 'COMPLETED',modified_on = now(),modified_by = NEW.modified_by
where id in (
select id from techo_web_notification_master techo_web where techo_web.notification_type_id = (select id from notification_type_master where code = 'migration_response_pending')
and techo_web.ref_code = NEW.id and techo_web.state = 'PENDING'
);
END IF;
end if;

RETURN NEW;
END;
$$;


ALTER FUNCTION public.migration_master_insert_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 974 (class 1255 OID 220303)
-- Name: ncd_breast_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_breast_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with health_infra_detail as (
        select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
        from location_hierchy_closer_det lh,health_infrastructure_details hid
        where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
        order by lh.child_id,lh.depth
    )
    select case when ncd_member_referral.referred_from_health_infrastructure_id is not null then ncd_member_referral.referred_from_health_infrastructure_id
                else health_infra_detail.health_infra_id end as health_infra_id,
           case when ncd_member_referral.referred_from_health_infrastructure_id is not null then health_infrastructure_details.type
                else health_infra_detail.type end as health_infra_type
    into _health_infra_id, _health_infra_type
    from ncd_member_breast_detail
    left join health_infra_detail on health_infra_detail.location_id = ncd_member_breast_detail.location_id
    left join ncd_member_referral on ncd_member_breast_detail.referral_id = ncd_member_referral.id
    left join health_infrastructure_details on health_infrastructure_details.id = ncd_member_referral.referred_from_health_infrastructure_id
    where ncd_member_breast_detail.id = new.id;

	update ncd_member_breast_detail
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_breast_hmis_updation() OWNER TO postgres;

--
-- TOC entry 975 (class 1255 OID 220304)
-- Name: ncd_cervical_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_cervical_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with health_infra_detail as (
        select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
        from location_hierchy_closer_det lh,health_infrastructure_details hid
        where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
        order by lh.child_id,lh.depth
    )
    select case when ncd_member_referral.referred_from_health_infrastructure_id is not null then ncd_member_referral.referred_from_health_infrastructure_id
                else health_infra_detail.health_infra_id end as health_infra_id,
           case when ncd_member_referral.referred_from_health_infrastructure_id is not null then health_infrastructure_details.type
                else health_infra_detail.type end as health_infra_type
    into _health_infra_id, _health_infra_type
    from ncd_member_cervical_detail
    left join health_infra_detail on health_infra_detail.location_id = ncd_member_cervical_detail.location_id
    left join ncd_member_referral on ncd_member_cervical_detail.referral_id = ncd_member_referral.id
    left join health_infrastructure_details on health_infrastructure_details.id = ncd_member_referral.referred_from_health_infrastructure_id
    where ncd_member_cervical_detail.id = new.id;

	update ncd_member_cervical_detail
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_cervical_hmis_updation() OWNER TO postgres;

--
-- TOC entry 976 (class 1255 OID 220305)
-- Name: ncd_diabetes_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_diabetes_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with health_infra_detail as (
        select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
        from location_hierchy_closer_det lh,health_infrastructure_details hid
        where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
        order by lh.child_id,lh.depth
    )
    select case when ncd_member_referral.referred_from_health_infrastructure_id is not null then ncd_member_referral.referred_from_health_infrastructure_id
                else health_infra_detail.health_infra_id end as health_infra_id,
           case when ncd_member_referral.referred_from_health_infrastructure_id is not null then health_infrastructure_details.type
                else health_infra_detail.type end as health_infra_type
    into _health_infra_id, _health_infra_type
    from ncd_member_diabetes_detail
    left join health_infra_detail on health_infra_detail.location_id = ncd_member_diabetes_detail.location_id
    left join ncd_member_referral on ncd_member_diabetes_detail.referral_id = ncd_member_referral.id
    left join health_infrastructure_details on health_infrastructure_details.id = ncd_member_referral.referred_from_health_infrastructure_id
    where ncd_member_diabetes_detail.id = new.id;

	update ncd_member_diabetes_detail
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_diabetes_hmis_updation() OWNER TO postgres;

--
-- TOC entry 977 (class 1255 OID 220306)
-- Name: ncd_disease_diagnosis_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_disease_diagnosis_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with details as (
        select *
        from ncd_member_referral
        where member_id = new.member_id
        and disease_code = new.disease_code
        and referred_from_health_infrastructure_id is not null
        order by id
        limit 1
    )
    select ncd_member_referral.referred_from_health_infrastructure_id,
    health_infrastructure_details.type
    into _health_infra_id, _health_infra_type
    from details
    inner join ncd_member_referral on details.id = ncd_member_referral.id
    inner join health_infrastructure_details on ncd_member_referral.referred_from_health_infrastructure_id = health_infrastructure_details.id;

	update ncd_member_diseases_diagnosis
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_disease_diagnosis_hmis_updation() OWNER TO postgres;

--
-- TOC entry 978 (class 1255 OID 220307)
-- Name: ncd_hypertension_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_hypertension_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with health_infra_detail as (
        select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
        from location_hierchy_closer_det lh,health_infrastructure_details hid
        where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
        order by lh.child_id,lh.depth
    )
    select case when ncd_member_referral.referred_from_health_infrastructure_id is not null then ncd_member_referral.referred_from_health_infrastructure_id
                else health_infra_detail.health_infra_id end as health_infra_id,
           case when ncd_member_referral.referred_from_health_infrastructure_id is not null then health_infrastructure_details.type
                else health_infra_detail.type end as health_infra_type
    into _health_infra_id, _health_infra_type
    from ncd_member_hypertension_detail
    left join health_infra_detail on health_infra_detail.location_id = ncd_member_hypertension_detail.location_id
    left join ncd_member_referral on ncd_member_hypertension_detail.referral_id = ncd_member_referral.id
    left join health_infrastructure_details on health_infrastructure_details.id = ncd_member_referral.referred_from_health_infrastructure_id
    where ncd_member_hypertension_detail.id = new.id;

	update ncd_member_hypertension_detail
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_hypertension_hmis_updation() OWNER TO postgres;

--
-- TOC entry 979 (class 1255 OID 220308)
-- Name: ncd_member_disesase_medicine_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_member_disesase_medicine_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

begin

if(new.is_deleted is null) then
insert
	into
		ncd_member_medicine_history (member_id,
		medicine_id,
	old_frequency,
	new_frequency,
	old_duration,
	new_duration,
	old_quantity,
	new_quantity,
	old_expiry_date,
	new_expiry_date ,
	created_date ,
	old_is_active ,
	new_is_active)
	values (new.member_id,
	new.medicine_id,
	old.frequency,
	new.frequency,
	old.duration,
	new.duration,
	old.quantity,
	new.quantity,
	old.expiry_date,
	new.expiry_date,
	now(),
	old.is_active,
	new.is_active);

end if;
return new;
end;
$$;


ALTER FUNCTION public.ncd_member_disesase_medicine_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 981 (class 1255 OID 220309)
-- Name: ncd_oral_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.ncd_oral_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    with health_infra_detail as (
        select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
        from location_hierchy_closer_det lh,health_infrastructure_details hid
        where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
        order by lh.child_id,lh.depth
    )
    select case when ncd_member_referral.referred_from_health_infrastructure_id is not null then ncd_member_referral.referred_from_health_infrastructure_id
                else health_infra_detail.health_infra_id end as health_infra_id,
           case when ncd_member_referral.referred_from_health_infrastructure_id is not null then health_infrastructure_details.type
                else health_infra_detail.type end as health_infra_type
    into _health_infra_id, _health_infra_type
    from ncd_member_oral_detail
    left join health_infra_detail on health_infra_detail.location_id = ncd_member_oral_detail.location_id
    left join ncd_member_referral on ncd_member_oral_detail.referral_id = ncd_member_referral.id
    left join health_infrastructure_details on health_infrastructure_details.id = ncd_member_referral.referred_from_health_infrastructure_id
    where ncd_member_oral_detail.id = new.id;

	update ncd_member_oral_detail
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.ncd_oral_hmis_updation() OWNER TO postgres;

--
-- TOC entry 982 (class 1255 OID 220310)
-- Name: rch_anc_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_anc_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

	if new.health_infrastructure_id is not null then
		select rch_anc_master.health_infrastructure_id,
		health_infrastructure_details.type
		into _health_infra_id, _health_infra_type
		from rch_anc_master
		inner join health_infrastructure_details on rch_anc_master.health_infrastructure_id = health_infrastructure_details.id
		where rch_anc_master.id = new.id;
	elsif new.delivery_place in ('HOSP','MAMTA_DAY') then
		with health_infra_detail as (
			select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
			from location_hierchy_closer_det lh,health_infrastructure_details hid
			where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
			order by lh.child_id,lh.depth
		)
		select health_infra_detail.health_infra_id,
		health_infra_detail.type
		into _health_infra_id, _health_infra_type
		from rch_anc_master
		inner join health_infra_detail on rch_anc_master.location_id = health_infra_detail.location_id
		where rch_anc_master.id = new.id;
	else
		select null,null into _health_infra_id,_health_infra_type;
	end if;

	update rch_anc_master
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.rch_anc_hmis_updation() OWNER TO postgres;

--
-- TOC entry 983 (class 1255 OID 220311)
-- Name: rch_anc_master_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_anc_master_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO public.rch_anc_master_archive
SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_anc_master_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 984 (class 1255 OID 220312)
-- Name: rch_csv_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_csv_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

	if new.health_infrastructure_id is not null then
		select rch_child_service_master.health_infrastructure_id,
		health_infrastructure_details.type
		into _health_infra_id, _health_infra_type
		from rch_child_service_master
		inner join health_infrastructure_details on rch_child_service_master.health_infrastructure_id = health_infrastructure_details.id
		where rch_child_service_master.id = new.id;
	elsif new.delivery_place in ('HOSP','MAMTA_DAY') then
		with health_infra_detail as (
			select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
			from location_hierchy_closer_det lh,health_infrastructure_details hid
			where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
			order by lh.child_id,lh.depth
		)
		select health_infra_detail.health_infra_id,
		health_infra_detail.type
		into _health_infra_id, _health_infra_type
		from rch_child_service_master
		inner join health_infra_detail on rch_child_service_master.location_id = health_infra_detail.location_id
		where rch_child_service_master.id = new.id;
	else
		select null,null into _health_infra_id,_health_infra_type;
	end if;

	update rch_child_service_master
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.rch_csv_hmis_updation() OWNER TO postgres;

--
-- TOC entry 985 (class 1255 OID 220313)
-- Name: rch_immunisation_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_immunisation_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

    select case when rch_immunisation_master.visit_type = 'FHW_ANC' then rch_anc_master.hmis_health_infra_id
                when rch_immunisation_master.visit_type = 'FHW_WPD' then rch_wpd_mother_master.health_infrastructure_id
                when rch_immunisation_master.visit_type = 'FHW_PNC' then rch_pnc_master.hmis_health_infra_id
                when rch_immunisation_master.visit_type = 'FHW_CS' then rch_child_service_master.hmis_health_infra_id
                else null end as health_infra_id,
           case when rch_immunisation_master.visit_type = 'FHW_ANC' then rch_anc_master.hmis_health_infra_type
                when rch_immunisation_master.visit_type = 'FHW_WPD' then rch_wpd_mother_master.type_of_hospital
                when rch_immunisation_master.visit_type = 'FHW_PNC' then rch_pnc_master.hmis_health_infra_type
                when rch_immunisation_master.visit_type = 'FHW_CS' then rch_child_service_master.hmis_health_infra_type
                else null end as health_infra_type
    into _health_infra_id, _health_infra_type
    from rch_immunisation_master
    left join rch_anc_master on rch_immunisation_master.visit_id = rch_anc_master.id and rch_immunisation_master.visit_type = 'FHW_ANC'
    left join rch_wpd_mother_master on rch_immunisation_master.visit_id = rch_wpd_mother_master.id and rch_immunisation_master.visit_type = 'FHW_WPD'
    left join rch_pnc_master on rch_immunisation_master.visit_id = rch_pnc_master.id and rch_immunisation_master.visit_type = 'FHW_PNC'
    left join rch_child_service_master on rch_immunisation_master.visit_id = rch_child_service_master.id and rch_immunisation_master.visit_type = 'FHW_CS'
    where rch_immunisation_master.id = new.id;

	update rch_child_service_master
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.rch_immunisation_hmis_updation() OWNER TO postgres;

--
-- TOC entry 986 (class 1255 OID 220314)
-- Name: rch_lmp_follow_up_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_lmp_follow_up_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO rch_lmp_follow_up_archive
    SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_lmp_follow_up_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 987 (class 1255 OID 220315)
-- Name: rch_member_death_deatil_change_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_member_death_deatil_change_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

WITH user_detail as (
	SELECT NEW.death_reason,role_id,id,NEW.member_id,NEW.ID, NEW.other_death_reason FROM um_user where id= NEW.modified_by
)
INSERT INTO rch_member_death_deatil_reason (death_reason,role_id,user_id,member_id,death_detail_id,other_death_reason)
select * from user_detail;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_member_death_deatil_change_function() OWNER TO postgres;

--
-- TOC entry 988 (class 1255 OID 220316)
-- Name: rch_member_services_scheduler(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_member_services_scheduler() RETURNS integer
    LANGUAGE plpgsql
    AS $$
-- select rch_member_services_scheduler();

DECLARE previous_execute_date timestamp;
        BEGIN
                -- Getting prevous value
		--select key_value into previous_execute_date from system_configuration where system_key ='RCH_MEMBER_SERVICES_LAST_EXECUTE_DATE';
		-- update system_configuration set key_value = '2019-01-01' where system_key ='RCH_MEMBER_SERVICES_LAST_EXECUTE_DATE'
		select key_value::timestamp into previous_execute_date from system_configuration where system_key ='RCH_MEMBER_SERVICES_LAST_EXECUTE_DATE';


		CREATE TEMP TABLE rch_member_services_temp_member (id bigint);		-- Creating Temp table which contains all member id which are updates in any services
		CREATE TEMP TABLE rch_member_services_temp_member_distinct (id bigint);

		INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_lmp_follow_up WHERE modified_on>=previous_execute_date;
		INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_anc_master WHERE modified_on>=previous_execute_date;
		INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_wpd_mother_master WHERE modified_on>=previous_execute_date;
		  --INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_wpd_child_master WHERE modified_on>=previous_execute_date;
		INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_child_service_master WHERE modified_on>=previous_execute_date;
		INSERT INTO rch_member_services_temp_member SELECT member_id FROM rch_pnc_master WHERE modified_on>=previous_execute_date;


		INSERT INTO rch_member_services_temp_member_distinct SELECT distinct id from rch_member_services_temp_member;

		-- delete all member data which are updated
		DELETE FROM rch_member_services where member_id in (select id from rch_member_services_temp_member_distinct);

		-- Reinseting all data for lmp;
		INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,created_on,longitude,latitude,lat_long_location_id)
		SELECT
		location_id,created_by as user_id,member_id, service_date, 'FHW_LMP' as service_type , created_on as server_date, id as visit_id , now() as created_on,longitude,latitude,getLocationByLatLong(longitude,latitude)
		FROM rch_lmp_follow_up
		--WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member_distinct);
		WHERE member_id in (select id from rch_member_services_temp_member_distinct);

		-- Reinseting all data for anc;
		INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,created_on,longitude,latitude,lat_long_location_id)
		SELECT
		location_id,created_by as user_id,member_id, service_date, 'FHW_ANC' as service_type , created_on as server_date, id as visit_id , now() as created_on,longitude,latitude,getLocationByLatLong(longitude,latitude)
		FROM rch_anc_master
		--WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member);
		WHERE member_id in (select id from rch_member_services_temp_member_distinct);

		-- Reinseting all data for mother wpd;
		INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,created_on,longitude,latitude,lat_long_location_id)
		SELECT
		location_id,created_by as user_id,member_id, created_on as service_date, 'FHW_MOTHER_WPD' as service_type , created_on as server_date, id as visit_id, now() as created_on,longitude,latitude,getLocationByLatLong(longitude,latitude)
		FROM rch_wpd_mother_master
		--WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member_distinct);
		WHERE member_id in (select id from rch_member_services_temp_member_distinct);

		-- Reinseting all data for child wpd;
		/*INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,created_on)
		SELECT
		location_id,created_by as user_id,member_id, created_on as service_date, 'FHW_CHILD_WPD' as service_type , created_on as server_date, id as visit_id , now() as created_on
		FROM rch_wpd_child_master
		WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member);*/

		-- Reinseting all data for child server;
		INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,longitude,latitude,lat_long_location_id)
		SELECT
		location_id,created_by as user_id,member_id, service_date as service_date, 'FHW_CS' as service_type , created_on as server_date, id as visit_id,longitude,latitude,getLocationByLatLong(longitude,latitude)
		FROM rch_child_service_master
		--WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member_distinct);
		WHERE member_id in (select id from rch_member_services_temp_member_distinct);

		-- Reinseting all data for pnc;
		-- update rch_pnc_master set modified_on = now() where modified_on >  now(); //TODO need to discuss with harshitbhai

		INSERT INTO rch_member_services (location_id,user_id,member_id,service_date,service_type,server_date,visit_id,created_on,longitude,latitude,lat_long_location_id)
		SELECT
		location_id,created_by as user_id,member_id, service_date as service_date, 'FHW_PNC' as service_type , created_on as server_date, id as visit_id , now() as created_on,longitude,latitude,getLocationByLatLong(longitude,latitude)
		FROM rch_pnc_master
		--WHERE modified_on >= previous_execute_date or member_id in (select id from rch_member_services_temp_member_distinct);
		WHERE member_id in (select id from rch_member_services_temp_member_distinct);

		update system_configuration set key_value = now() where system_key='RCH_MEMBER_SERVICES_LAST_EXECUTE_DATE';

		DROP TABLE rch_member_services_temp_member;
		DROP TABLE rch_member_services_temp_member_distinct;


		return 1;
        END;
$$;


ALTER FUNCTION public.rch_member_services_scheduler() OWNER TO postgres;

--
-- TOC entry 989 (class 1255 OID 220317)
-- Name: rch_pnc_hmis_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_pnc_hmis_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare
	_health_infra_id integer;
	_health_infra_type integer;

begin

	if new.health_infrastructure_id is not null then
		select rch_pnc_master.health_infrastructure_id,
		health_infrastructure_details.type
		into _health_infra_id, _health_infra_type
		from rch_pnc_master
		inner join health_infrastructure_details on rch_pnc_master.health_infrastructure_id = health_infrastructure_details.id
		where rch_pnc_master.id = new.id;
	elsif new.delivery_place in ('HOSP','MAMTA_DAY') then
		with health_infra_detail as (
			select distinct on(lh.child_id)lh.child_id as location_id,hid.id as health_infra_id,hid.type
			from location_hierchy_closer_det lh,health_infrastructure_details hid
			where hid.location_id = lh.parent_id and hid.type in (1061,1062,1063)
			order by lh.child_id,lh.depth
		)
		select health_infra_detail.health_infra_id,
		health_infra_detail.type
		into _health_infra_id, _health_infra_type
		from rch_pnc_master
		inner join health_infra_detail on rch_pnc_master.location_id = health_infra_detail.location_id
		where rch_pnc_master.id = new.id;
	else
		select null,null into _health_infra_id,_health_infra_type;
	end if;

	update rch_pnc_master
	set hmis_health_infra_id = _health_infra_id,
	hmis_health_infra_type = _health_infra_type
	where id = new.id;

	return null;

end;
$$;


ALTER FUNCTION public.rch_pnc_hmis_updation() OWNER TO postgres;

--
-- TOC entry 990 (class 1255 OID 220318)
-- Name: rch_pnc_master_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_pnc_master_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO rch_pnc_master_archive
    SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_pnc_master_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 991 (class 1255 OID 220319)
-- Name: rch_pregnancy_registration_det_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_pregnancy_registration_det_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO rch_pregnancy_registration_det_archive
    SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_pregnancy_registration_det_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 992 (class 1255 OID 220320)
-- Name: rch_wpd_mother_master_delete_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.rch_wpd_mother_master_delete_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO rch_wpd_mother_master_archive
    SELECT OLD.*;

RETURN NULL;
END$$;


ALTER FUNCTION public.rch_wpd_mother_master_delete_trigger_function() OWNER TO postgres;

--
-- TOC entry 993 (class 1255 OID 220321)
-- Name: role_hierarchy_level_updation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.role_hierarchy_level_updation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	begin
		update role_hierarchy_management
		set level = new.level
		where location_type = new.type;
		return new;
	end;
$$;


ALTER FUNCTION public.role_hierarchy_level_updation() OWNER TO postgres;

--
-- TOC entry 994 (class 1255 OID 220322)
-- Name: stop_auditing(regclass); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.stop_auditing(target_table regclass) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE

BEGIN
   EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_row ON ' || quote_ident(target_table::TEXT);
   EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_stm ON ' || quote_ident(target_table::TEXT);

END;
$$;


ALTER FUNCTION public.stop_auditing(target_table regclass) OWNER TO postgres;

--
-- TOC entry 995 (class 1255 OID 220323)
-- Name: sync_member_data(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.sync_member_data(mem bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
	DECLARE
	    BEGIN
		CREATE temp TABLE member_details(
			family_id bigint,
			member_id bigint,
			family_id_text text,
			unique_health_id text,
			location_id bigint,
			area_id bigint,
			cur_preg_reg_det_id bigint,
			lmp date,
			is_pregnant boolean,
			dob date,
			imtecho_lmp date,
			anc_visit_dates text,
			immunisation_given text,
			gender text,
			marital_status text,
			sync_status text,
			mother_id bigint,
			emamta_health_id text,
			family_state text,
			preg_reg_det_id bigint,
			is_lmp_followup_done boolean,
			is_wpd_entry_done boolean
		);

insert into member_details (family_id ,	member_id,family_id_text,unique_health_id,location_id,area_id,is_pregnant,dob,imtecho_lmp,lmp,gender,marital_status,emamta_health_id
,family_state,preg_reg_det_id)
select distinct imt_family.id as family_id,imt_member.id as member_id,imt_family.family_id,case when imt_member.state in ('com.argusoft.imtecho.member.state.new',
		'com.argusoft.imtecho.member.state.new.fhw.reverified') and imt_member.emamta_health_id is not null then UPPER(imt_member.emamta_health_id)
		else imt_member.unique_health_id end as unique_health_id
,imt_family.location_id as location_id,case when imt_family.area_id is null then imt_family.location_id else cast(imt_family.area_id as bigint) end ,
(case when imt_member.is_pregnant = true then true else false end),imt_member.dob,imt_member.lmp,imt_member.lmp,imt_member.gender,imt_member.marital_status
,UPPER(imt_member.emamta_health_id),imt_family.state,imt_member.cur_preg_reg_det_id
from imt_family
inner join imt_member on imt_family.family_id = imt_member.family_id
left join rch_member_service_data_sync_detail on rch_member_service_data_sync_detail.member_id = imt_member.id
where rch_member_service_data_sync_detail.member_id is null
and imt_member.id = mem;

insert into rch_member_service_data_sync_detail (member_id,created_on,original_lmp,is_pregnant_in_imtecho,imtecho_dob,location_id)
select member_id,now(),imtecho_lmp,is_pregnant,dob,location_id from member_details ;



-- query to insert rch_pregnancy_registration_det
INSERT INTO public.rch_pregnancy_registration_det(
             mthr_reg_no,location_id, member_id, lmp_date, edd, reg_date, state, created_on,
            created_by, modified_on, modified_by)
select mother.mthr_reg_no,m.area_id,m.member_id,mother.mthr_lastmas_date,mother.exp_del_date,mother.entrydate
,case when d.mthr_reg_no is null then 'PREGNANT' else 'DELIVERY_DONE' end
,now(),null,now(),null
from member_details m
inner join  tbl_mother mother on mother.member_id = m.unique_health_id
left join tbl_mother_delivery d on d.mthr_reg_no = mother.mthr_reg_no;



INSERT INTO public.rch_anc_master(
            member_id, family_id, location_id,
            ,mobile_start_date
            ,mobile_end_date
            ,blood_sugar_test
            ,sugar_test_after_food_val
            ,sugar_test_before_food_val
            ,urine_test_done,
            albendazole_given, referral_place, other_dangerous_sign, dangerous_sign_id,
            created_on, member_status,
            referral_done, hbsag_test,pregnancy_reg_det_id
            ,haemoglobin_count,ifa_tablets_given,weight,systolic_bp,diastolic_bp,calcium_tablets_given,fa_tablets_given,created_by)
select member_details.member_id,member_details.family_id,member_details.location_id,'01-01-1970','01-01-1970'
,'BOTH',case when isInteger(tbl_anc.bgtest_after_food) then cast(tbl_anc.bgtest_after_food as int) else null end
,case when isInteger(tbl_anc.bgtest_empty_stomach) then cast(tbl_anc.bgtest_empty_stomach as int) else null end
,case when tbl_anc.urine_test = 1 then true else false end
,case when tbl_anc.albendazole = 1 then true else false end
,tbl_reffer_place.ref_id
,tbl_anc.other_risky_symptom
,tbl_anc.anc_risky_symptom
,tbl_anc.anc_date,'AVAILABLE'
,case when tbl_anc.anc_is_referred = 1 then 'YES' else case when tbl_anc.anc_is_referred = 0 then 'NO' else 'NOT_REQUIRED' end end
,case when tbl_anc.is_hba1c = 1 then 'REACTIVE' when tbl_anc.anc_is_referred = 0 then 'NON_REACTIVE' else null end ,rch_pregnancy_registration_det.id
,case when isInteger(tbl_mother_hb.hb) then tbl_mother_hb.hb else null end
,case when isInteger(tbl_mother_hb.ifa) then tbl_mother_hb.ifa else null end
,tbl_mother_hb.weight
,case when isInteger(tbl_mother_hb.bp) then tbl_mother_hb.bp else null end
,case when isInteger(tbl_mother_hb.bp_low) then tbl_mother_hb.bp_low else null end
,case when isInteger(tbl_mother_hb.ca) then tbl_mother_hb.ca else null end
,case when isInteger(tbl_mother_hb.number_folicacid_tabletgiven) then tbl_mother_hb.number_folicacid_tabletgiven else null end
,-1
from member_details inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_anc on tbl_anc.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
left join tbl_mother_hb on tbl_anc.mthr_reg_no = tbl_mother_hb.mthr_reg_no and tbl_anc.anc_date = tbl_mother_hb.hb_date
left join tbl_reffer_place on tbl_anc.anc_referred_place = tbl_reffer_place.emamta_id;



INSERT INTO public.rch_wpd_mother_master(
            member_id, family_id, mobile_start_date,
            mobile_end_date, location_id, date_of_delivery,
            member_status, is_preterm_birth, delivery_place, type_of_hospital,
            delivery_done_by,  type_of_delivery, pregnancy_outcome,
            created_on, discharge_date,
            cortico_steroid_given,
            has_delivery_happened,pregnancy_reg_det_id,created_by)
select member_details.member_id,member_details.family_id,'01-01-1970','01-01-1970',member_details.location_id
,tbl_mother_delivery.mthr_delivery_date,'AVAILABLE'
,case when tbl_mother_delivery.is_early_birth = 1 then true else false end
,case when tbl_mother_delivery.mthr_delivery_place = 7 then 'HOME'
when tbl_mother_delivery.mthr_delivery_place = 12 then 'ON_THE_WAY'
else 'HOSP' end
,case when tbl_mother_delivery.mthr_delivery_place = 1 then 897
when tbl_mother_delivery.mthr_delivery_place = 2 then 899
when tbl_mother_delivery.mthr_delivery_place = 3 then 895
when tbl_mother_delivery.mthr_delivery_place = 4 then 891
when tbl_mother_delivery.mthr_delivery_place = 5 then 898
when tbl_mother_delivery.mthr_delivery_place = 6 then 893
when tbl_mother_delivery.mthr_delivery_place = 8 then 894
when tbl_mother_delivery.mthr_delivery_place = 9 then 890
when tbl_mother_delivery.mthr_delivery_place = 10 then 896
when tbl_mother_delivery.mthr_delivery_place = 11 then 892
else null end
,case when tbl_mother_delivery.deliver_designation = 1 then 'DOCTOR'
when tbl_mother_delivery.deliver_designation = 2 then 'ANM'
when tbl_mother_delivery.deliver_designation = 3 then 'NURSE'
when tbl_mother_delivery.deliver_designation = 4 then 'TBA'
when tbl_mother_delivery.deliver_designation = 5 then 'NON-TBA'
when tbl_mother_delivery.deliver_designation = 6 then 'CY-Doctor'
else null end
,case when tbl_mother_delivery.mthr_delivery_type = 1 then 'NORMAL'
when tbl_mother_delivery.mthr_delivery_type = 2 then 'CAESAREAN'
when tbl_mother_delivery.mthr_delivery_type = 3 then 'ASSIT'
else null end
,case when tbl_mother_delivery.mthr_delivery_type = 1 then 'LBIRTH'
when tbl_mother_delivery.mthr_delivery_type = 2 then 'LBIRTH'
when tbl_mother_delivery.mthr_delivery_type = 3 then 'LBIRTH'
when tbl_mother_delivery.mthr_delivery_type = 4 then 'ABORTION'
when tbl_mother_delivery.mthr_delivery_type = 5 then 'MTP'
else null end
,tbl_mother_delivery.entrydt
,tbl_mother_delivery.discharge_dt
,case when tbl_mother_delivery.corticost_injec = 1 then true else false end
,true
,rch_pregnancy_registration_det.id
,-1
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_mother_delivery on tbl_mother_delivery.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no;


--insert into rch_wpd_child_master



with rch_wpd_child_master_t as (
select child_detail.id as member_id,member_details.family_id,'01-01-1970'::date as mobile_start_date
,'01-01-1970'::date as mobile_end_date,member_details.location_id ,rch_wpd_mother_master.id as wpd_mother_id
,member_details.member_id as mother_id,case when tbl_child.cld_is_death = 0 then 'SBIRTH' else 'LBIRTH' end as pregnancy_outcome
,tbl_child.sex as gender, tbl_child.cld_birth_date as date_of_delivery
,-1 as created_by,tbl_child.cld_reg_date as created_on,-1 as modified_by,tbl_child.cld_reg_date as modified_on
,tbl_child.cld_death_date as death_date,tbl_death_reason.pnc_ref_id as death_reason,tbl_death_place.ref_code as place_of_death
,'AVAILABLE' as member_status
from member_details
inner join rch_wpd_mother_master on rch_wpd_mother_master.member_id = member_details.member_id
inner join tbl_child on tbl_child.mot_huid = member_details.unique_health_id and cast(tbl_child.cld_birth_date as date) = cast(rch_wpd_mother_master.date_of_delivery as date)
inner join imt_member child_detail on child_detail.unique_health_id = tbl_child.cld_huid
left join tbl_death_reason on tbl_death_reason.id = tbl_child.cld_death_reason
left join tbl_death_place on tbl_death_place.emamta_id = tbl_child.cld_death_place
),rch_wpd_child_master_ins as(
INSERT INTO public.rch_wpd_child_master(
            member_id, family_id, mobile_start_date,
            mobile_end_date, location_id, wpd_mother_id,
            mother_id, pregnancy_outcome, gender, date_of_delivery,
            created_by, created_on, modified_by, modified_on, death_date, death_reason, place_of_death, member_status
            )
select member_id, family_id, mobile_start_date,
            mobile_end_date, location_id, wpd_mother_id,
            mother_id, pregnancy_outcome, gender, date_of_delivery,
            created_by, created_on, modified_by, modified_on, death_date, death_reason, place_of_death, member_status
			from rch_wpd_child_master_t
			returning id
)
update imt_member set mother_id = rch_wpd_child_master_t.mother_id from rch_wpd_child_master_t
where imt_member.id = rch_wpd_child_master_t.member_id;

-- lmp and is_pregnant flag update

update member_details SET is_pregnant = true
,lmp = rch_pregnancy_registration_det.lmp_date
,cur_preg_reg_det_id = rch_pregnancy_registration_det.id
--,modified_on = now()
from rch_pregnancy_registration_det where rch_pregnancy_registration_det.member_id = member_details.member_id
and rch_pregnancy_registration_det.state = 'PREGNANT'
and rch_pregnancy_registration_det.lmp_date > now() - interval '350 days'
and member_details.preg_reg_det_id is null and member_details.is_wpd_entry_done is null;

update member_details set cur_preg_reg_det_id = preg_reg_det_id where preg_reg_det_id is not null;



CREATE TEMPORARY TABLE temp_child_details(
	family_id text,
	member_id bigint,
	mthr_reg_no text,
	unique_health_id text,
	birth_date date,
    cld_reg_date date,
    sex text,
    child_name text,
complementary_feeding_started boolean

);

--logic to add pregnancy done children
insert into temp_child_details(family_id,member_id,mthr_reg_no,unique_health_id,birth_date,cld_reg_date,sex,child_name,complementary_feeding_started)
select member_details.family_id_text,member_details.member_id as member_id ,rch_pregnancy_registration_det.mthr_reg_no,tbl_child.cld_huid,
cld_birth_date,cld_reg_date,sex,child_name,case when is_compliment_feed = '1' then true else false end
from member_details
--inner join imt_member on member_details.member_id = imt_member.id  and imt_member.is_pregnant = true
--inner join imt_family on imt_family.family_id = imt_member.family_id
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id and member_details.is_pregnant = true
and rch_pregnancy_registration_det.state = 'DELIVERY_DONE' and member_details.lmp < rch_pregnancy_registration_det.edd
inner join tbl_mother_delivery on rch_pregnancy_registration_det.mthr_reg_no = tbl_mother_delivery.mthr_reg_no
left join tbl_child on tbl_child.mot_huid = member_details.unique_health_id and cast(tbl_child.cld_birth_date as date) = cast(tbl_mother_delivery.mthr_delivery_date as date);


with temp_child_detail as(
INSERT INTO public.imt_member(
created_on, dob,
family_head, family_id, first_name, gender,
middle_name ,last_name ,state, unique_health_id,created_by, modified_by, modified_on,
complementary_feeding_started,
mother_id)
select cld_reg_date,birth_date,false,temp_child_details.family_id,child_name,sex,imt_member.middle_name,imt_member.last_name
,'com.argusoft.imtecho.member.state.new', temp_child_details.unique_health_id,-1,-1,cld_reg_date
,temp_child_details.complementary_feeding_started,imt_member.id from temp_child_details,imt_member
where temp_child_details.member_id = imt_member.id
and temp_child_details.unique_health_id is not null
returning id,family_id,unique_health_id,dob)
insert into member_details(family_id,member_id,unique_health_id,dob)
select imt_family.id,temp_child_detail.id,temp_child_detail.unique_health_id,dob from temp_child_detail,imt_family
where temp_child_detail.family_id = imt_family.family_id;



update member_details set is_pregnant = false where member_id in (select member_id from temp_child_details)
and cur_preg_reg_det_id is null and preg_reg_det_id is null;


--anc dates update
update member_details SET anc_visit_dates = pregnancy_reg_det.anc_visit_dates
from (select member_details.member_id,pregnancy_reg_det_id,string_agg(to_char(created_on,'DD/MM/YYYY'),',' order by created_on) anc_visit_dates from rch_anc_master,member_details
where pregnancy_reg_det_id = member_details.cur_preg_reg_det_id
--and member_details.lmp > now() - interval '281 day'
group by pregnancy_reg_det_id,member_details.member_id) as pregnancy_reg_det where pregnancy_reg_det.member_id = member_details.member_id;

--entry in pnc master for mother detail
 INSERT INTO public.rch_pnc_master(
            member_id, family_id, mobile_start_date,
            mobile_end_date, location_id,
            created_by, created_on, modified_by, modified_on, member_status,
            pregnancy_reg_det_id, pnc_no)
select member_details.member_id,member_details.family_id,'01-01-1970','01-01-1970',member_details.location_id
,-1,tbl_pnc.pnc_date,-1,tbl_pnc.pnc_date,'AVAILABLE',rch_pregnancy_registration_det.id,tbl_pnc.pnc_no
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_pnc on tbl_pnc.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
where tbl_pnc.pnc_date is not null;


--entry in pnc mother table
INSERT INTO public.rch_pnc_mother_master(
            pnc_master_id, mother_id,
            is_alive, referral_place,
            created_by, created_on, modified_by, modified_on, member_status,
            family_planning_method)
select rch_pnc_master.id,member_details.member_id,true,tbl_reffer_place.ref_id
,-1,tbl_pnc.pnc_date,-1,tbl_pnc.pnc_date,'AVAILABLE',tbl_fp_method.code
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_pnc on tbl_pnc.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
inner join rch_pnc_master on rch_pnc_master.pregnancy_reg_det_id = rch_pregnancy_registration_det.id
and tbl_pnc.pnc_date = rch_pnc_master.created_on
and tbl_pnc.pnc_no = cast(rch_pnc_master.pnc_no as bigint)
left join tbl_reffer_place on tbl_reffer_place.emamta_id = tbl_pnc.pnc_referred_place
left join tbl_fp_method on tbl_fp_method.fp_id = tbl_pnc.pnc_contra_method
where tbl_pnc.pnc_date is not null;

--update ref_id for child_table
update tbl_pnc_child set pnc_ref_id = pnc_master_id from (
select tbl_pnc_child.mthr_reg_no,tbl_pnc_child.dt,max(rch_pnc_master.id) as pnc_master_id
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_pnc_child on tbl_pnc_child.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
inner join rch_pnc_master on rch_pnc_master.pregnancy_reg_det_id = rch_pregnancy_registration_det.id
and tbl_pnc_child.dt = rch_pnc_master.created_on
group by tbl_pnc_child.mthr_reg_no,tbl_pnc_child.dt) as t
where tbl_pnc_child.mthr_reg_no = t.mthr_reg_no and t.dt =tbl_pnc_child.dt and pnc_ref_id != pnc_master_id;



--entry in pnc master for child detail
 INSERT INTO public.rch_pnc_master(
            member_id, family_id, mobile_start_date,
            mobile_end_date, location_id,
            created_by, created_on, modified_by, modified_on, member_status,
            pregnancy_reg_det_id, pnc_no)
select member_details.member_id as member_id,member_details.family_id as family_id,'01-01-1970' as mobile_start_date ,'01-01-1970' as mobile_end_date,member_details.location_id as location_id
,-1 as created_by ,tbl_pnc_child.dt as created_on,-1 as modified_by ,tbl_pnc_child.dt as modified_on
,'AVAILABLE' as member_status ,rch_pregnancy_registration_det.id as pregnancy_reg_det_id ,tbl_pnc_child.pnc_visit_day as pnc_no
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_pnc_child on tbl_pnc_child.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
where pnc_ref_id is null and tbl_pnc_child.dt is not null;

--update ref_id for child_table
update tbl_pnc_child set pnc_ref_id = pnc_master_id from (
select tbl_pnc_child.mthr_reg_no,tbl_pnc_child.dt,max(rch_pnc_master.id) as pnc_master_id
from member_details
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.member_id = member_details.member_id
inner join tbl_pnc_child on tbl_pnc_child.mthr_reg_no = rch_pregnancy_registration_det.mthr_reg_no
inner join rch_pnc_master on rch_pnc_master.pregnancy_reg_det_id = rch_pregnancy_registration_det.id
and tbl_pnc_child.dt = rch_pnc_master.created_on
and tbl_pnc_child.pnc_ref_id is null
group by tbl_pnc_child.mthr_reg_no,tbl_pnc_child.dt) as t
where tbl_pnc_child.mthr_reg_no = t.mthr_reg_no and t.dt =tbl_pnc_child.dt;

--entry in child pnc master for mother detail
 INSERT INTO public.rch_pnc_child_master(
            pnc_master_id, child_id, is_alive, other_danger_sign, child_weight,
            created_by, created_on, modified_by, modified_on, member_status,
            death_date, death_reason, place_of_death, referral_place,
            child_referral_done)
select tbl_pnc_child.pnc_ref_id,member_details.member_id,case when tbl_pnc_child.is_death = '1' then false else true end
,tbl_pnc_child.other_diseases,tbl_pnc_child.wt,-1,tbl_pnc_child.dt,-1,tbl_pnc_child.dt,'AVAILABLE'
,tbl_pnc_child.death_date,tbl_death_reason.pnc_ref_id,tbl_death_place.ref_code,tbl_reffer_place.ref_id
,case when tbl_reffer_place.ref_id is not null then true else false end
from member_details
inner join tbl_pnc_child on tbl_pnc_child.health_uid = member_details.unique_health_id
left join tbl_death_reason on tbl_death_reason.id = tbl_pnc_child.death_reason
left join tbl_death_place on tbl_death_place.emamta_id = tbl_pnc_child.death_place
left join tbl_reffer_place on tbl_reffer_place.emamta_id = tbl_pnc_child.refer_place
where tbl_pnc_child.dt is not null;


--entry for child immunization
 INSERT INTO public.rch_immunisation_master(
            member_id,location_id,member_type,
            immunisation_given, given_on, given_by, created_by, created_on,
            modified_by, modified_on)
select member_details.member_id,member_details.area_id,'C',tbl_child_vaccination_master.ref_code,tbl_child_vaccination.given_date,-1,-1
,tbl_child_vaccination.given_date,-1,tbl_child_vaccination.given_date
from member_details
inner join tbl_child_vaccination on tbl_child_vaccination.cld_huid = member_details.unique_health_id
inner join tbl_child_vaccination_master on tbl_child_vaccination_master.id = tbl_child_vaccination.vid
where tbl_child_vaccination.given_date is not null;

-- entry for TT1 immunization
 INSERT INTO public.rch_immunisation_master(
            member_id, member_type,
            immunisation_given, given_on, given_by, created_by, created_on,
            modified_by, modified_on,pregnancy_reg_det_id)
select member_details.member_id,'M','TT1',tbl_mother_tt1.mthr_tt1_date,-1,-1
,tbl_mother_tt1.mthr_tt1_date,-1,tbl_mother_tt1.mthr_tt1_date,rch_pregnancy_registration_det.id
from member_details
inner join tbl_mother_tt1 on tbl_mother_tt1.member_id = member_details.unique_health_id
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.mthr_reg_no = tbl_mother_tt1.mthr_reg_no
where tbl_mother_tt1.mthr_tt1_date is not null;


--entry for TT2 immunization
 INSERT INTO public.rch_immunisation_master(
            member_id, member_type,
            immunisation_given, given_on, given_by, created_by, created_on,
            modified_by, modified_on,pregnancy_reg_det_id)
select member_details.member_id,'M','TT2',tbl_mother_tt2.mthr_tt2_date,-1,-1
,tbl_mother_tt2.mthr_tt2_date,-1,tbl_mother_tt2.mthr_tt2_date,rch_pregnancy_registration_det.id
from member_details
inner join tbl_mother_tt2 on tbl_mother_tt2.member_id = member_details.unique_health_id
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.mthr_reg_no = tbl_mother_tt2.mthr_reg_no
where tbl_mother_tt2.mthr_tt2_date is not null;

--entry in TTBoster
 INSERT INTO public.rch_immunisation_master(
            member_id, member_type,
            immunisation_given, given_on, given_by, created_by, created_on,
            modified_by, modified_on,pregnancy_reg_det_id)
select member_details.member_id,'M','TT2',tbl_mother_ttbooster.mthr_ttbuster_date,-1,-1
,tbl_mother_ttbooster.mthr_ttbuster_date,-1,tbl_mother_ttbooster.mthr_ttbuster_date,rch_pregnancy_registration_det.id
from member_details
inner join tbl_mother_ttbooster on  tbl_mother_ttbooster.member_id = member_details.unique_health_id
inner join rch_pregnancy_registration_det on rch_pregnancy_registration_det.mthr_reg_no = tbl_mother_ttbooster.mthr_reg_no
where tbl_mother_ttbooster.mthr_ttbuster_date is not null;


--update mother immunization
update member_details set immunisation_given = t.immunisation_given
from (
select member_details.member_id
,string_agg(concat(rch_immunisation_master.immunisation_given,'#',to_char(rch_immunisation_master.given_on,'DD/MM/YYYY'))
,',' order by rch_immunisation_master.given_on) as immunisation_given
from member_details
inner join rch_immunisation_master on rch_immunisation_master.pregnancy_reg_det_id = member_details.cur_preg_reg_det_id
group by member_details.member_id) as t
where t.member_id = member_details.member_id;


--update child immunization
update member_details set immunisation_given = t.immunisation_given
from (
select member_details.member_id
,string_agg(concat(rch_immunisation_master.immunisation_given,'#',to_char(rch_immunisation_master.given_on,'DD/MM/YYYY'))
,',' order by rch_immunisation_master.given_on) as immunisation_given
from member_details
inner join rch_immunisation_master on rch_immunisation_master.member_id = member_details.member_id
where rch_immunisation_master.member_type = 'C'
group by member_details.member_id) as t
where t.member_id = member_details.member_id;


update member_details set lmp = now() - interval '28 days'
where member_details.is_pregnant = true and member_details.cur_preg_reg_det_id is null and lmp is null;

INSERT INTO public.rch_pregnancy_registration_det(
             member_id,location_id,lmp_date, edd, reg_date, state, created_on,
            created_by, modified_on, modified_by)
select member_details.member_id,member_details.area_id,member_details.lmp,member_details.lmp + interval '281 day',now(),'PREGNANT' ,now(),-1,now(),-1
from member_details
where member_details.is_pregnant = true and member_details.cur_preg_reg_det_id is null;




update member_details SET is_pregnant = true
--,lmp = case when member_details.lmp is null then rch_pregnancy_registration_det.lmp_date else member_details.lmp end
,cur_preg_reg_det_id = rch_pregnancy_registration_det.id
--,modified_on = now()
from rch_pregnancy_registration_det where
 member_details.is_wpd_entry_done is null and member_details.preg_reg_det_id is null
and rch_pregnancy_registration_det.member_id = member_details.member_id
and rch_pregnancy_registration_det.state = 'PREGNANT'
and rch_pregnancy_registration_det.lmp_date > now() - interval '350 days';


-- anc notification
INSERT INTO public.techo_notification_master(
            notification_type_id, notification_code, location_id,
            family_id, member_id, schedule_date,
            action_by, state, created_by, created_on, modified_by, modified_on,
            ref_code)
select 2,case when date_part('day',age(lmp,anc_date)) between 0 and 91 then '1'
when date_part('day',age(lmp,anc_date)) between 92 and 189 then '2'
when date_part('day',age(lmp,anc_date)) between 190 and 245 then '3'
when date_part('day',age(lmp,anc_date)) > 246 then '4' end
,location_id,family_id,member_id,anc_date,-1,'DONE_ON_EMAMTA',-1,now(),'-1',now(),cur_preg_reg_det_id
from (select member_details.family_id as family_id,member_details.member_id as member_id,member_details.cur_preg_reg_det_id,member_details.location_id
,member_details.lmp,max(rch_anc_master.created_on) as anc_date
from member_details
inner join rch_anc_master on rch_anc_master.pregnancy_reg_det_id = member_details.cur_preg_reg_det_id and member_details.preg_reg_det_id is null
where member_details.cur_preg_reg_det_id is not null
group by member_details.family_id ,member_details.member_id ,member_details.cur_preg_reg_det_id,member_details.location_id,member_details.lmp) as t;


-- lmp_notification notification
INSERT INTO public.techo_notification_master(
            notification_type_id, notification_code, location_id,
            family_id, member_id, schedule_date,
            state, created_by, created_on, modified_by, modified_on)
select 1,0
,case when imt_family.area_id is not null then cast(imt_family.area_id as bigint) else imt_family.location_id end ,imt_family.id,member_details.member_id,now(),'PENDING',-1,now(),'-1',now()
from member_details
inner join imt_family on imt_family.id = member_details.family_id
inner join location_master on cast(imt_family.area_id as bigint) = location_master.id
where member_details.is_pregnant = false and member_details.gender = 'F' and member_details.is_lmp_followup_done is null
and member_details.marital_status = '629'and member_details.dob > now() - interval '50 Year';

-- Anc notification
INSERT INTO public.event_mobile_notification_pending(
            notification_configuration_type_id, base_date, family_id,
            member_id, created_by, created_on, modified_by, modified_on,
            is_completed, state, ref_code)
select '5d1131bc-f5bc-4a4a-8d7d-6dfd3f512f0a' -- anc_config_id
,member_details.lmp,member_details.family_id,member_details.member_id,-1,now(),-1,now(),false,'PENDING',member_details.cur_preg_reg_det_id
 from member_details
where member_details.cur_preg_reg_det_id is not null and member_details.preg_reg_det_id is null;


-- WPD notification
INSERT INTO public.event_mobile_notification_pending(
            notification_configuration_type_id, base_date, family_id,
            member_id, created_by, created_on, modified_by, modified_on,
            is_completed, state, ref_code)
select 'faedb8e7-3e46-40a2-a9ac-ea7d5de944fa' -- wpd_config_id
,member_details.lmp,member_details.family_id,member_details.member_id,-1,now(),-1,now(),false,'PENDING',member_details.cur_preg_reg_det_id
 from member_details
where member_details.cur_preg_reg_det_id is not null and member_details.preg_reg_det_id is null;


-- PNC notification
INSERT INTO public.event_mobile_notification_pending(
            notification_configuration_type_id, base_date, family_id,
            member_id, created_by, created_on, modified_by, modified_on,
            is_completed, state, ref_code)
select '9b1a331b-fac5-48f0-908e-ef545e0b0c52' -- pnc_config_id
,rch_wpd_mother_master.date_of_delivery,member_details.family_id,member_details.member_id,-1,now(),-1,now(),false,'PENDING',rch_wpd_mother_master.pregnancy_reg_det_id
 from member_details
 inner join rch_wpd_mother_master on rch_wpd_mother_master.member_id = member_details.member_id and rch_wpd_mother_master.date_of_delivery > now() - interval '60 day';

-- Child service notification
INSERT INTO public.event_mobile_notification_pending(
            notification_configuration_type_id, base_date, family_id,
            member_id, created_by, created_on, modified_by, modified_on,
            is_completed, state, ref_code)
select 'f51c8c4f-6b2b-4dcb-8e64-ada1a3044a67' -- child_service_id
,member_details.dob,member_details.family_id,member_details.member_id,-1,now(),-1,now(),false,'PENDING',-1
 from member_details
 where member_details.dob > now() - interval '5 year';


-- update mother_id
/*update imt_member set mother_id = mother.id from member_details,imt_member mother,tbl_child
where member_details.member_id =imt_member.id
and tbl_child.cld_huid = imt_member.unique_health_id and mother.unique_health_id = tbl_child.mot_huid;*/

update member_details set mother_id = mother.member_id from member_details mother,tbl_child
where tbl_child.cld_huid = member_details.unique_health_id and mother.unique_health_id = tbl_child.mot_huid;



update rch_member_service_data_sync_detail set is_pregnant_in_emamta = member_details.is_pregnant,emamta_lmp = member_details.lmp from member_details
where rch_member_service_data_sync_detail.member_id = member_details.member_id and member_details.gender = 'F' ;

update member_details set sync_status = (case when gender = 'F'
and (rch_member_service_data_sync_detail.is_pregnant_in_imtecho is null or  rch_member_service_data_sync_detail.is_pregnant_in_imtecho = false)
and rch_member_service_data_sync_detail.is_pregnant_in_emamta = true then 'R' else null end) from rch_member_service_data_sync_detail
where rch_member_service_data_sync_detail.member_id = member_details.member_id;

update rch_pregnancy_registration_det set state = 'DELIVERY_DATA_NOT_FOUND'
where id in (select rch_pregnancy_registration_det.id from rch_pregnancy_registration_det
left join member_details on rch_pregnancy_registration_det.id = member_details.cur_preg_reg_det_id
where rch_pregnancy_registration_det.state = 'PREGNANT'
and member_details.member_id is null and rch_pregnancy_registration_det.member_id in (select member_id from member_details));

update imt_member set is_pregnant = member_details.is_pregnant , lmp = member_details.lmp,cur_preg_reg_det_id = member_details.cur_preg_reg_det_id
,anc_visit_dates = member_details.anc_visit_dates ,immunisation_given = member_details.immunisation_given , mother_id = member_details.mother_id
, sync_status = member_details.sync_status
from member_details where imt_member.id = member_details.member_id;

drop table member_details;
drop table temp_child_details;

return mem;
END;
$$;


ALTER FUNCTION public.sync_member_data(mem bigint) OWNER TO postgres;

--
-- TOC entry 996 (class 1255 OID 220325)
-- Name: techo_notification_master_delete_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.techo_notification_master_delete_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

	INSERT INTO public.techo_notification_location_change_detail(
	notification_id , from_location_id , to_location_id , created_by , created_on)
	values(OLD.id,OLD.location_id,null,OLD.modified_by, now());

	RETURN OLD;
END;
$$;


ALTER FUNCTION public.techo_notification_master_delete_trigger_func() OWNER TO postgres;

--
-- TOC entry 997 (class 1255 OID 220326)
-- Name: techo_notification_master_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.techo_notification_master_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

	INSERT INTO public.techo_notification_state_detail(
             notification_id,  to_state,
            to_schedule_date, created_by, created_on, modified_by, modified_on)
    VALUES (NEW.id, NEW.state,
            NEW.schedule_date, NEW.created_by, NEW.created_on, NEW.modified_by, NEW.modified_on);

	RETURN NEW;
END;
$$;


ALTER FUNCTION public.techo_notification_master_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 998 (class 1255 OID 220327)
-- Name: techo_notification_master_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.techo_notification_master_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

	IF OLD.state <> NEW.state or OLD.schedule_date <> NEW.schedule_date then

	INSERT INTO public.techo_notification_state_detail(
            notification_id, from_state, to_state, from_schedule_date,
            to_schedule_date, created_by, created_on, modified_by, modified_on)
        VALUES (NEW.id,OLD.state,NEW.state,OLD.schedule_date,
            NEW.schedule_date, NEW.modified_by, now(), NEW.modified_by, now());

	End if;

	if OLD.location_id <> NEW.location_id then

	INSERT INTO public.techo_notification_location_change_detail(
	notification_id , from_location_id , to_location_id , created_by , created_on)
	values(NEW.id,OLD.location_id,NEW.location_id,NEW.modified_by, now());

	End if;

	RETURN NEW;
END;
$$;


ALTER FUNCTION public.techo_notification_master_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 999 (class 1255 OID 220328)
-- Name: techo_web_notification_master_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.techo_web_notification_master_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
	if NEW.notification_type_id is not null and NEW.escalation_level_id is not null then
            NEW.notification_type_escalation_id := CONCAT(NEW.notification_type_id,'_',NEW.escalation_level_id);
        else
	    NEW.notification_type_escalation_id := null;
	end if;
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.techo_web_notification_master_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 1000 (class 1255 OID 220329)
-- Name: timer_event_trigger_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.timer_event_trigger_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO public.timer_event_history(
	   timer_event_id, event_id, from_state, to_state, system_trigger_on, processed_on, completed_on)
	   VALUES( old.id,
			   old.event_config_id ,
			   old.status ,
			   new.status ,
			   old.system_trigger_on ,
			  case when old.completed_on is null then new.processed_on
			  else old.completed_on end, NEW.completed_on);

RETURN NEW;
END
$$;


ALTER FUNCTION public.timer_event_trigger_function() OWNER TO postgres;

--
-- TOC entry 1001 (class 1255 OID 220330)
-- Name: truncate_schema(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.truncate_schema(_schema character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
    selectrow record;
begin
for selectrow in
select 'TRUNCATE TABLE ' || quote_ident(_schema) || '.' ||quote_ident(t.table_name) || ' CASCADE;' as qry
from (
     SELECT table_name
     FROM information_schema.tables
     WHERE table_type = 'BASE TABLE' AND table_schema = _schema
     )t
loop
execute selectrow.qry;
end loop;
end;
$$;


ALTER FUNCTION public.truncate_schema(_schema character varying) OWNER TO postgres;

--
-- TOC entry 1002 (class 1255 OID 220331)
-- Name: um_role_master_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_role_master_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
                  'INSERT INTO public.um_role_master(
		created_by, created_on, modified_by, modified_on, code, description,
		name, state, max_position, is_email_mandatory, is_contact_num_mandatory,
		is_aadhar_num_mandatory)
		VALUES ('|| quote_nullable(NEW.created_by) || ',
			'|| quote_nullable(NEW.created_on) || ',
			'|| quote_nullable(NEW.modified_by) || ',
			'|| quote_nullable(NEW.modified_on) || ',
			'|| quote_nullable(NEW.code) || ',
			'|| quote_nullable(NEW.description) || ',
			'|| quote_nullable(NEW.name) || ',
			'|| quote_nullable(NEW.state) || ',
			'|| quote_nullable(NEW.max_position) || ',
			'|| quote_nullable(NEW.is_email_mandatory) || ',
			'|| quote_nullable(NEW.is_contact_num_mandatory) || ',
			'|| quote_nullable(NEW.is_aadhar_num_mandatory) || ');'

        );
	end if;
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_role_master_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 980 (class 1255 OID 220332)
-- Name: um_role_master_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_role_master_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
		'UPDATE um_role_master
		SET created_by='|| quote_nullable(NEW.created_by) || ', created_on='|| quote_nullable(NEW.created_on) || ',
		modified_by='|| quote_nullable(NEW.modified_by) || ', modified_on='|| quote_nullable(NEW.modified_on) || ',
		code='|| quote_nullable(NEW.code) || ', description='|| quote_nullable(NEW.description) || ',
		name='|| quote_nullable(NEW.name) || ', state='|| quote_nullable(NEW.state) || ',
		max_position='|| quote_nullable(NEW.max_position) || ', is_email_mandatory='|| quote_nullable(NEW.is_email_mandatory) || ',
		is_contact_num_mandatory='|| quote_nullable(NEW.is_contact_num_mandatory) || ',
		is_aadhar_num_mandatory='|| quote_nullable(NEW.is_aadhar_num_mandatory) || '
		WHERE id='|| quote_nullable(NEW.id) || ';'
        );
	end if;

   RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_role_master_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 1003 (class 1255 OID 220333)
-- Name: um_user_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_user_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    update um_user set activation_date = now() where id=NEW.ID;

	INSERT INTO public.um_user_activation_status(
            user_id, activation_date, activate_by)
	VALUES (NEW.id, now(), NEW.created_by);

	if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
	   (select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
	   'DROP EXTENSION IF EXISTS dblink;
	    CREATE EXTENSION dblink;

	    INSERT INTO um_user(
            id, created_by, created_on, first_name,
            gender, last_name, middle_name, password, prefered_language,
            role_id, state, user_name, search_text, server_type,activation_date)
	       Values ('|| quote_nullable(NEW.id) || '
			, '||quote_nullable(NEW.created_by) ||'
			, '||quote_nullable(NEW.created_on) ||'
			, '||quote_nullable(NEW.first_name) ||'
			, '||quote_nullable(NEW.gender) ||'
			, '||quote_nullable(NEW.last_name) ||'
			, '||quote_nullable(NEW.middle_name) ||'
			, '||quote_nullable(NEW.password) ||'
			, '||quote_nullable(NEW.prefered_language) ||'
			, '||quote_nullable(NEW.role_id) ||'
			, '||quote_nullable(NEW.state) ||'
			, '||quote_nullable(NEW.user_name || '_t') ||'
			, '||quote_nullable(NEW.search_text) ||'
			, '||quote_nullable(NEW.server_type) ||'
			, '||quote_nullable(NEW.activation_date) ||');'
	);
	end if;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_user_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 1004 (class 1255 OID 220334)
-- Name: um_user_location_insert_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_user_location_insert_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN


if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
		'INSERT INTO public.um_user_location(
            id, created_by, created_on, modified_by, modified_on, loc_id,
            state, type, user_id, level)
	   VALUES ('|| quote_nullable(NEW.id) || '
	   ,'|| quote_nullable(NEW.created_by) || '
	   ,'|| quote_nullable(NEW.created_on) || '
	   ,'|| quote_nullable(NEW.modified_by) || '
	   ,'|| quote_nullable(NEW.modified_on) || '
	   ,'|| quote_nullable(NEW.loc_id) || '
	   ,'|| quote_nullable(NEW.state) || '
	   ,'|| quote_nullable(NEW.type) || '
	   ,'|| quote_nullable(NEW.user_id) || '
	   ,'|| quote_nullable(NEW.level) || '
	   );'
        );
	end if;
    update location_master set modified_on = now() where id = NEW.loc_id;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_user_location_insert_trigger_func() OWNER TO postgres;

--
-- TOC entry 1005 (class 1255 OID 220335)
-- Name: um_user_location_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_user_location_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
		'UPDATE um_user_location SET id='|| quote_nullable(NEW.id) ||
		', created_by='|| quote_nullable(NEW.created_by) ||
		', created_on='|| quote_nullable(NEW.created_on) ||
		', modified_by='|| quote_nullable(NEW.modified_by) ||
		', modified_on='|| quote_nullable(NEW.modified_on) ||
		', loc_id='|| quote_nullable(NEW.loc_id) ||
		', state='|| quote_nullable(NEW.state) ||
		', type='|| quote_nullable(NEW.type) ||
		', user_id='|| quote_nullable(NEW.user_id) ||
		', level='|| quote_nullable(NEW.level) || '
		WHERE id='|| quote_nullable(NEW.id) || ';'

        );
	end if;
    update location_master set modified_on = now() where id = NEW.loc_id;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_user_location_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 1006 (class 1255 OID 220336)
-- Name: um_user_update_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.um_user_update_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN


	IF NEW.state != OLD.state then
		if NEW.state = 'ACTIVE' then

			INSERT INTO public.um_user_activation_status(
			user_id, activation_date, activate_by)
			VALUES (NEW.id, now(), NEW.modified_by);
			update um_user set activation_date = now() where id=NEW.ID;

		else

			UPDATE um_user_activation_status SET deactivation_date = now(),deactivate_by = NEW.modified_by where user_id = NEW.id and deactivation_date is null;

		end if;
	END if;

	if (select case when key_value = 'P' then true else false end from system_configuration where system_key = 'SERVER_TYPE') then
	PERFORM dblink_exec
	(
		(select key_value from system_configuration where system_key = 'TRAINING_DB_NAME'),
		'UPDATE um_user SET created_by='||quote_nullable(NEW.created_by)||', created_on='||quote_nullable(NEW.created_on)||',
			modified_by='||quote_nullable(NEW.modified_by)||', modified_on='||quote_nullable(NEW.modified_on)||',
			aadhar_number='||quote_nullable(NEW.aadhar_number)||', address='||quote_nullable(NEW.address)||',
			code='||quote_nullable(NEW.code)||', contact_number='||quote_nullable(NEW.contact_number)||',
			date_of_birth='||quote_nullable(NEW.date_of_birth)||', email_id='||quote_nullable(NEW.email_id)||',
			first_name='||quote_nullable(NEW.first_name)||', gender='||quote_nullable(NEW.gender)||',
			last_name='||quote_nullable(NEW.last_name)||', middle_name='||quote_nullable(NEW.middle_name)||',
			password='||quote_nullable(NEW.password)||', prefered_language='||quote_nullable(NEW.prefered_language)||',
			role_id='||quote_nullable(NEW.role_id)||', state='||quote_nullable(NEW.state)||',
			user_name='||quote_nullable(NEW.user_name || '_t')||', search_text='||quote_nullable(NEW.search_text)||',
			server_type='||quote_nullable(NEW.server_type)||', title='||quote_nullable(NEW.title)||',
			imei_number='||quote_nullable(NEW.imei_number)||', techo_phone_number='||quote_nullable(NEW.techo_phone_number)||'
			WHERE id='||quote_nullable(NEW.id)||';'

	);
	end if;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.um_user_update_trigger_func() OWNER TO postgres;

--
-- TOC entry 1007 (class 1255 OID 220337)
-- Name: update_basic_state_family_trigger(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_basic_state_family_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
if (TG_OP = 'INSERT') then with rowss as(
insert
	into
		imt_family_state_detail (family_id,
		from_state,
		to_state,
		parent,
		created_by,
		created_on,
		modified_by,
		modified_on)
	values (new.family_id,
	null,
	new.state,
	null,
	new.modified_by,
	now(),
	new.modified_by,
	now()) returning id ) select
	id into
		NEW.current_state
	from
		rowss;
end if;

if (TG_OP != 'INSERT'
and new.state != old.state) then with rowss as(
insert
	into
		imt_family_state_detail (family_id,
		from_state,
		to_state,
		parent,
		created_by,
		created_on,
		modified_by,
		modified_on,
		comment)
	values (new.family_id,
	old.state,
	new.state,
	old.current_state,
	new.modified_by,
	now(),
	new.modified_by,
	now(),
	case
		when new.remarks is not null
		and old.remarks is null then new.remarks
		when new.remarks is not null
		and old.remarks is not null
		and new.remarks <> old.remarks then new.remarks
		else null
	end) returning id ) select
	id into
		NEW.current_state
	from
		rowss;
end if;
case
when new.state in ('com.argusoft.imtecho.family.state.unverified') then new.basic_state := 'UNVERIFIED';
when new.state in ('com.argusoft.imtecho.family.state.archived',
'com.argusoft.imtecho.family.state.archived.fhw.reverified',
'com.argusoft.imtecho.family.state.archived.fhsr.verified',
'com.argusoft.imtecho.family.state.new.archived.mo.fhw.reverified',
'com.argusoft.imtecho.family.state.archived.emri.fhw.reverified',
'com.argusoft.imtecho.family.state.new.archived.fhw.reverified',
'com.argusoft.imtecho.family.state.archived.mo.verified',
'com.argusoft.imtecho.family.state.archived.mo.fhw.reverified') then new.basic_state := 'ARCHIVED';
when new.state in ('com.argusoft.imtecho.family.state.verified',
'com.argusoft.imtecho.family.state.fhw.reverified',
'com.argusoft.imtecho.family.state.emri.fhw.reverified',
'com.argusoft.imtecho.family.state.emri.verified.ok',
'com.argusoft.imtecho.family.state.emri.fhw.reverified.dead',
'com.argusoft.imtecho.family.state.emri.fhw.reverified.verified',
'com.argusoft.imtecho.family.state.emri.verified.ok.dead',
'com.argusoft.imtecho.family.state.emri.verification.pool.dead',
'com.argusoft.imtecho.family.state.emri.verification.pool.verified',
'com.argusoft.imtecho.family.state.emri.verification.pool.archived',
'com.argusoft.imtecho.family.state.emri.verification.pool',
'com.argusoft.imtecho.family.state.emri.verified.ok.verified',
'com.argusoft.imtecho.family.state.mo.fhw.reverified',
'com.argusoft.imtecho.family.state.emri.fhw.reverified.archived',
'com.argusoft.imtecho.family.state.emri.verified.ok.archived',
'CFHC_FV',
'CFHC_GVK_FV',
'CFHC_MO_FV',
'CFHC_GVK_FRV',
'CFHC_MO_FRV',
'CFHC_GVK_FRVP',
'CFHC_MO_FRVP'
) then new.basic_state := 'VERIFIED';
when new.state in ('com.argusoft.imtecho.family.state.archived.fhsr.reverification',
'com.argusoft.imtecho.family.state.archived.mo.reverification',
'com.argusoft.imtecho.family.state.new.fhsr.reverification',
'com.argusoft.imtecho.family.state.new.mo.reverification',
'com.argusoft.imtecho.family.state.emri.fhw.reverification.verified',
'com.argusoft.imtecho.family.state.emri.fhw.reverification.dead',
'com.argusoft.imtecho.family.state.emri.fhw.reverification',
'com.argusoft.imtecho.family.state.emri.fhw.reverification.archived',
'CFHC_FIR',
'CFHC_GVK_FIR') then new.basic_state := 'REVERIFICATION';
when new.state in ('com.argusoft.imtecho.family.state.new',
'com.argusoft.imtecho.family.state.new.fhsr.verified',
'com.argusoft.imtecho.family.state.new.fhw.reverified',
'com.argusoft.imtecho.family.state.new.mo.verified',
'com.argusoft.imtecho.family.state.new.mo.fhw.reverified',
'CFHC_FN') then new.basic_state := 'NEW';
when new.state in ('com.argusoft.imtecho.family.state.orphan') then new.basic_state := 'ORPHAN';
when new.state in ('com.argusoft.imtecho.family.state.merged') then new.basic_state := 'MERGED';
when new.state in ('com.argusoft.imtecho.family.state.temporary') then new.basic_state := 'TEMPORARY';
when new.state in ('IDSP_DR_TECHO','IDSP_MY_TECHO','IDSP_TECHO','IDSP_TEMP') then new.basic_state := 'IDSP';
when new.state in ('com.argusoft.imtecho.family.state.migrated'
,'com.argusoft.imtecho.family.state.archived.temporary'
,'com.argusoft.imtecho.family.state.archived.temporary.outofstate') then new.basic_state := 'MIGRATED';
else new.basic_state := 'UNHANDLED';
end
case;

return new;
end;

$$;


ALTER FUNCTION public.update_basic_state_family_trigger() OWNER TO postgres;

--
-- TOC entry 1008 (class 1255 OID 220338)
-- Name: update_basic_state_member_trigger(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_basic_state_member_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
update
	imt_family
set
	modified_on = now()
where
	family_id = new.family_id
	and modified_on < now() - interval '1 minute';

if (TG_OP = 'UPDATE'
and NEW.family_id != OLD.family_id) then update
	imt_family
set
	modified_on = now()
where
	family_id = old.family_id
	and (modified_on is null
	or modified_on < now() - interval '1 minute');
end if;

if (TG_OP = 'INSERT') then with rowss as(
insert
	into
		imt_member_state_detail (member_id,
		from_state,
		to_state,
		parent,
		created_by,
		created_on,
		modified_by,
		modified_on)
	values (new.id,
	null,
	new.state,
	null,
	new.modified_by,
	now(),
	new.modified_by,
	now()) returning id ) select
	id into
		new.current_state
	from
		rowss;
end if;

if (TG_OP != 'INSERT'
and new.state != old.state) then with rowss as(
insert
	into
		imt_member_state_detail (member_id,
		from_state,
		to_state,
		parent,
		created_by,
		created_on,
		modified_by,
		modified_on,
		comment)
	values (new.id,
	old.state,
	new.state,
	old.current_state,
	new.modified_by,
	now(),
	new.modified_by,
	now(),
	case
		when new.remarks is not null
		and old.remarks is null then new.remarks
		when new.remarks is not null
		and old.remarks is not null
		and new.remarks <> old.remarks then new.remarks
		else null
	end) returning id ) select
	id into
		new.current_state
	from
		rowss;
end if;

if (TG_OP = 'INSERT'
or new.state != old.state)
and new.state in ( 'com.argusoft.imtecho.member.state.verified',
'com.argusoft.imtecho.member.state.mo.fhw.reverified',
'com.argusoft.imtecho.member.state.fhw.reverified' ,
'com.argusoft.imtecho.member.state.new',
'com.argusoft.imtecho.member.state.new.fhw.reverified',
'com.argusoft.imtecho.member.state.temporary',
'com.argusoft.imtecho.member.state.dead.fhsr.reverification',
'com.argusoft.imtecho.member.state.dead.mo.reverification',
'com.argusoft.imtecho.member.state.archived.mo.reverification',
'com.argusoft.imtecho.member.state.archived.fhsr.reverification',
'CFHC_MV',
'CFHC_MN',
'CFHC_MIR',
'CFHC_MMOV') then insert
	into
		rch_member_data_sync_pending(member_id,
		created_on,
		is_synced)
	values(NEW.id,
	now(),
	false);

if(new.state = 'com.argusoft.imtecho.member.state.temporary'
and NEW.is_pregnant = true
and NEW.cur_preg_reg_det_id is null) then drop table
	if exists member_details;

create temp table
	member_details( family_id bigint,
	member_id bigint,
	family_id_text text,
	unique_health_id text,
	location_id bigint,
	area_id bigint,
	cur_preg_reg_det_id bigint,
	cur_preg_reg_date date,
	lmp date,
	is_pregnant boolean,
	dob date,
	imtecho_lmp date,
	anc_visit_dates text,
	immunisation_given text,
	gender text,
	marital_status text,
	sync_status text,
	mother_id bigint,
	emamta_health_id text,
	family_state text,
	last_delivery_date date,
	preg_reg_det_id bigint,
	is_lmp_followup_done boolean,
	is_wpd_entry_done boolean );

insert
	into
		member_details (family_id ,
		member_id,
		family_id_text,
		unique_health_id,
		location_id,
		area_id,
		is_pregnant,
		dob,
		imtecho_lmp,
		lmp,
		gender,
		marital_status,
		emamta_health_id ,
		family_state,
		preg_reg_det_id) select
			distinct imt_family.id as family_id,
			NEW.id as member_id,
			imt_family.family_id,
			case
				when NEW.state in ('com.argusoft.imtecho.member.state.new',
				'com.argusoft.imtecho.member.state.new.fhw.reverified')
				and NEW.emamta_health_id is not null then upper(NEW.emamta_health_id)
				else NEW.unique_health_id
			end as unique_health_id ,
			imt_family.location_id as location_id,
			case
				when imt_family.area_id is null then imt_family.location_id
				else cast(imt_family.area_id as bigint)
			end ,
			(case
				when NEW.is_pregnant = true then true
				else false
			end),
			NEW.dob,
			NEW.lmp,
			NEW.lmp,
			NEW.gender,
			NEW.marital_status ,
			upper(NEW.emamta_health_id),
			imt_family.state,
			NEW.cur_preg_reg_det_id
		from
			imt_family
		where
			imt_family.family_id = NEW.family_id;

insert
	into
		rch_member_service_data_sync_detail (member_id,
		created_on,
		original_lmp,
		is_pregnant_in_imtecho,
		imtecho_dob,
		location_id) select
			member_id,
			now(),
			imtecho_lmp,
			is_pregnant,
			dob,
			location_id
		from
			member_details ;

update
	member_details
set
	lmp = now() - interval '28 days'
where
	member_details.is_pregnant = true
	and member_details.cur_preg_reg_det_id is null
	and lmp is null;

insert
	into
		public.rch_pregnancy_registration_det( member_id,
		family_id,
		location_id,
		current_location_id,
		lmp_date,
		edd,
		reg_date,
		state,
		created_on,
		created_by,
		modified_on,
		modified_by) select
			member_details.member_id,
			member_details.family_id,
			member_details.area_id,
			member_details.area_id,
			member_details.lmp,
			member_details.lmp + interval '281 day',
			now(),
			'PREGNANT' ,
			now(),
			-1,
			now(),
			-1
		from
			member_details
		where
			member_details.is_pregnant = true
			and member_details.cur_preg_reg_det_id is null;

update
	member_details
set
	is_pregnant = true,
	--,lmp = case when member_details.lmp is null then rch_pregnancy_registration_det.lmp_date else member_details.lmp end
 cur_preg_reg_det_id = rch_pregnancy_registration_det.id ,
	cur_preg_reg_date = rch_pregnancy_registration_det.reg_date
from
	rch_pregnancy_registration_det
where
	member_details.is_wpd_entry_done is null
	and member_details.preg_reg_det_id is null
	and rch_pregnancy_registration_det.member_id = member_details.member_id
	and rch_pregnancy_registration_det.state = 'PREGNANT'
	and rch_pregnancy_registration_det.lmp_date > now() - interval '350 days';
-- anc notification
 insert
	into
		public.techo_notification_master( notification_type_id,
		notification_code,
		location_id,
		family_id,
		member_id,
		schedule_date,
		action_by,
		state,
		created_by,
		created_on,
		modified_by,
		modified_on,
		ref_code) select
			2,
			case
				when date_part('day', age(lmp, anc_date)) between 0 and 91 then '1'
				when date_part('day', age(lmp, anc_date)) between 92 and 189 then '2'
				when date_part('day', age(lmp, anc_date)) between 190 and 245 then '3'
				when date_part('day', age(lmp, anc_date)) > 246 then '4'
			end ,
			location_id,
			-1,
			family_id,
			member_id,
			anc_date,
			-1,
			'DONE_ON_EMAMTA',
			-1,
			now(),
			'-1',
			now(),
			cur_preg_reg_det_id
		from
			(
			select
				member_details.family_id as family_id,
				member_details.member_id as member_id,
				member_details.cur_preg_reg_det_id,
				member_details.location_id ,
				member_details.lmp,
				max(rch_anc_master.created_on) as anc_date
			from
				member_details
			inner join rch_anc_master on
				rch_anc_master.pregnancy_reg_det_id = member_details.cur_preg_reg_det_id
				and member_details.preg_reg_det_id is null
			where
				member_details.cur_preg_reg_det_id is not null
			group by
				member_details.family_id ,
				member_details.member_id ,
				member_details.cur_preg_reg_det_id,
				member_details.location_id,
				member_details.lmp) as t;
-- Anc notification
 insert
	into
		public.event_mobile_notification_pending( notification_configuration_type_id,
		base_date,
		family_id,
		member_id,
		created_by,
		created_on,
		modified_by,
		modified_on,
		is_completed,
		state,
		ref_code) select
			'5d1131bc-f5bc-4a4a-8d7d-6dfd3f512f0a'
			-- anc_config_id
,
			member_details.lmp,
			member_details.family_id,
			member_details.member_id,
			-1,
			now(),
			-1,
			now(),
			false,
			'PENDING',
			member_details.cur_preg_reg_det_id
		from
			member_details
		where
			member_details.cur_preg_reg_det_id is not null
			and member_details.preg_reg_det_id is null;
-- WPD notification
 insert
	into
		public.event_mobile_notification_pending( notification_configuration_type_id,
		base_date,
		family_id,
		member_id,
		created_by,
		created_on,
		modified_by,
		modified_on,
		is_completed,
		state,
		ref_code) select
			'faedb8e7-3e46-40a2-a9ac-ea7d5de944fa'
			-- wpd_config_id
,
			member_details.lmp,
			member_details.family_id,
			member_details.member_id,
			-1,
			now(),
			-1,
			now(),
			false,
			'PENDING',
			member_details.cur_preg_reg_det_id
		from
			member_details
		where
			member_details.cur_preg_reg_det_id is not null
			and member_details.preg_reg_det_id is null;
-- PNC notification
 insert
	into
		public.event_mobile_notification_pending( notification_configuration_type_id,
		base_date,
		family_id,
		member_id,
		created_by,
		created_on,
		modified_by,
		modified_on,
		is_completed,
		state,
		ref_code) select
			'9b1a331b-fac5-48f0-908e-ef545e0b0c52'
			-- pnc_config_id
,
			rch_wpd_mother_master.date_of_delivery,
			member_details.family_id,
			member_details.member_id,
			-1,
			now(),
			-1,
			now(),
			false,
			'PENDING',
			rch_wpd_mother_master.pregnancy_reg_det_id
		from
			member_details
		inner join rch_wpd_mother_master on
			rch_wpd_mother_master.member_id = member_details.member_id
			and rch_wpd_mother_master.date_of_delivery > now() - interval '60 day';

NEW.cur_preg_reg_det_id = (
	select cur_preg_reg_det_id
from
	member_details);

NEW.cur_preg_reg_date = (
	select cur_preg_reg_date
from
	member_details);

drop table
	member_details;

elseif (NEW.is_pregnant = true
and NEW.cur_preg_reg_det_id is null) then NEW.is_pregnant = false;
end if;
end if;

if TG_OP != 'INSERT'
and new.current_state is null
and old.current_state is not null then new.current_state = old.current_state;
end if;

if TG_OP != 'INSERT'
and new.basic_state is null
and old.basic_state is not null then new.basic_state = old.basic_state;
end if;
case
	when new.state in ('com.argusoft.imtecho.member.state.unverified','CFHC_MU') then new.basic_state := 'UNVERIFIED';
when new.state in ('com.argusoft.imtecho.member.state.archived.fhsr.verified',
'com.argusoft.imtecho.member.state.archived.fhw.reverified',
'com.argusoft.imtecho.member.state.archived',
'com.argusoft.imtecho.member.state.archived.mo.verified',
'com.argusoft.imtecho.member.state.archived.outofstate') then new.basic_state := 'ARCHIVED';
when new.state in ('com.argusoft.imtecho.member.state.verified',
'com.argusoft.imtecho.member.state.mo.fhw.reverified',
'com.argusoft.imtecho.member.state.fhw.reverified',
'CFHC_MV','CFHC_MMOV') then new.basic_state := 'VERIFIED';
when new.state in ('com.argusoft.imtecho.member.state.dead.fhsr.reverification',
'com.argusoft.imtecho.member.state.dead.mo.reverification',
'com.argusoft.imtecho.member.state.archived.mo.reverification',
'com.argusoft.imtecho.member.state.archived.fhsr.reverification','CFHC_MIR') then new.basic_state := 'REVERIFICATION';
when new.state in ('com.argusoft.imtecho.member.state.new',
'com.argusoft.imtecho.member.state.new.fhw.reverified',
'CFHC_MN') then new.basic_state := 'NEW';
when new.state in ('com.argusoft.imtecho.member.state.dead.fhw.reverified',
'com.argusoft.imtecho.member.state.dead.mo.verified',
'com.argusoft.imtecho.member.state.dead',
'com.argusoft.imtecho.member.state.dead.mo.fhw.reverified',
'com.argusoft.imtecho.member.state.dead.fhsr.verified',
'CFHC_MD') then new.basic_state := 'DEAD';
when new.state in ('com.argusoft.imtecho.member.state.orphan') then new.basic_state := 'ORPHAN';
when new.state in ('com.argusoft.imtecho.member.state.temporary') then new.basic_state := 'TEMPORARY';
when new.state in ('com.argusoft.imtecho.member.state.migrated'
,'com.argusoft.imtecho.member.state.migrated.lfu'
,'com.argusoft.imtecho.member.state.migrated.outofstate') then new.basic_state := 'MIGRATED';
when new.state in ('IDSP_DR_TECHO','IDSP_MY_TECHO','IDSP_TECHO','IDSP_TEMP') then new.basic_state := 'IDSP';
else new.basic_state := 'UNHANDLED';
end
case;

return new;
end;
$$;


ALTER FUNCTION public.update_basic_state_member_trigger() OWNER TO postgres;

--
-- TOC entry 1009 (class 1255 OID 220340)
-- Name: update_form_master_info_on_notification_type_add_or_update(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_form_master_info_on_notification_type_add_or_update() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
	if  (TG_OP = 'INSERT') then
		insert into form_master (created_by,created_on,modified_by,modified_on,code,name,state)
		values (new.created_by,now(),new.modified_by,now(),new.code,new.name,new.state);
	else
           update form_master set modified_by=new.modified_by, modified_on= now(), code=new.code, name=new.name, state=new.state
           where code = old.code;
	end if;

	return new;
end
$$;


ALTER FUNCTION public.update_form_master_info_on_notification_type_add_or_update() OWNER TO postgres;

--
-- TOC entry 1010 (class 1255 OID 220341)
-- Name: update_location_hierchy_location_type_master(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_location_hierchy_location_type_master(locationid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	corporationCount INT :=0;
	ruralCount INT :=0;
	urbanCount INT :=0;
	hierarchy_type_string varchar(3) := '';
	r record;
        BEGIN
            create TEMPORARY table zzz_type_master_temp(location_type text);
            FOR r IN
                SELECT child_id as location_id FROM location_hierchy_closer_det WHERE (parent_id  = locationid)
                union
                SELECT parent_id as location_id FROM location_hierchy_closer_det WHERE (child_id = locationid and parent_id not in (-1, -2))

            LOOP
                truncate table zzz_type_master_temp;
                hierarchy_type_string := '';
                --DELETE FROM location_hierarchy_type WHERE location_id = r.location_id;
                INSERT INTO zzz_type_master_temp SELECT location_type FROM (
                SELECT parent_loc_type as location_type from location_hierchy_closer_det
                WHERE (child_id  = r.location_id )
                union
                SELECT child_loc_type as location_type from location_hierchy_closer_det
                WHERE (parent_id  = r.location_id)) ta;


                SELECT count(distinct(location_type)) into corporationCount from zzz_type_master_temp where location_type in ('C','Z','U');

                IF corporationCount >=3 THEN
                    hierarchy_type_string := 'C';
                END IF;

                SELECT count(distinct(location_type)) into ruralCount from zzz_type_master_temp ts where location_type in ('D','B','P');


                IF ruralCount >=3 THEN
                    hierarchy_type_string := concat(hierarchy_type_string,'R');
                END IF;

                SELECT count(distinct(location_type)) into urbanCount from zzz_type_master_temp ts where location_type in ('D','B','U');

                IF urbanCount >=3 THEN
                    hierarchy_type_string := concat(hierarchy_type_string,'U');
                END IF;

                update location_master set demographic_type = hierarchy_type_string
                where id = r.location_id and (demographic_type is null or demographic_type != hierarchy_type_string);

                update location_hierchy_closer_det set child_loc_demographic_type = hierarchy_type_string
                where child_id = r.location_id and (child_loc_demographic_type is null or child_loc_demographic_type != hierarchy_type_string);

                update location_hierchy_closer_det set parent_loc_demographic_type = hierarchy_type_string
                where parent_id = r.location_id and (parent_loc_demographic_type is null or parent_loc_demographic_type != hierarchy_type_string);

            END LOOP;
            drop table zzz_type_master_temp;
            RETURN 1;
        END;
$$;


ALTER FUNCTION public.update_location_hierchy_location_type_master(locationid integer) OWNER TO postgres;

--
-- TOC entry 966 (class 1255 OID 220342)
-- Name: update_location_hierchy_type_master(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_location_hierchy_type_master(locationid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	corporationCount INT :=0;
	ruralCount INT :=0;
	urbanCount INT :=0;
	r record;
	location_type_temp varchar;
        BEGIN
		DELETE FROM location_hierarchy_type WHERE location_id in (select location_id from
		(SELECT child_id as location_id FROM location_hierchy_closer_det WHERE (parent_id  = locationId)
		 union
		 SELECT parent_id as location_id FROM location_hierchy_closer_det WHERE (child_id = locationId)) lc);

		create TEMPORARY table zzz_type_master_temp(location_type text);
		FOR r IN
			SELECT child_id as location_id FROM location_hierchy_closer_det WHERE (parent_id  = locationId)
			union
			SELECT parent_id as location_id FROM location_hierchy_closer_det WHERE (child_id = locationId)

		LOOP
			truncate table zzz_type_master_temp;
			--DELETE FROM location_hierarchy_type WHERE location_id = r.location_id;
			INSERT INTO zzz_type_master_temp SELECT location_type FROM (SELECT parent_loc_type as location_type from location_hierchy_closer_det
			WHERE (parent_id  = r.location_id or child_id  = r.location_id )
			union
			SELECT child_loc_type as location_type from location_hierchy_closer_det
			WHERE (parent_id  = r.location_id or child_id  = r.location_id)) ta;


			SELECT count(distinct(location_type)) into corporationCount from zzz_type_master_temp where location_type in ('C','Z','U');

			IF corporationCount >=3 THEN
				insert into location_hierarchy_type values (r.location_id,'C');
			END IF;

			SELECT count(distinct(location_type)) into ruralCount from zzz_type_master_temp ts where location_type in ('D','B','P');


			IF ruralCount >=3 THEN
				insert into location_hierarchy_type values (r.location_id, 'R');
			END IF;

			SELECT count(distinct(location_type)) into urbanCount from zzz_type_master_temp ts where location_type in ('D','B','U');

			IF urbanCount >=3 THEN
				insert into location_hierarchy_type values (r.location_id,'U');
			END IF;

		END LOOP;
		drop table zzz_type_master_temp;
                RETURN 1;
        END;
$$;


ALTER FUNCTION public.update_location_hierchy_type_master(locationid integer) OWNER TO postgres;

--
-- TOC entry 1011 (class 1255 OID 220343)
-- Name: update_member_info_after_insert(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_member_info_after_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
if new.state in (
		'com.argusoft.imtecho.member.state.verified',
		'com.argusoft.imtecho.member.state.mo.fhw.reverified',
		'com.argusoft.imtecho.member.state.fhw.reverified'
		,'com.argusoft.imtecho.member.state.new',
		'com.argusoft.imtecho.member.state.new.fhw.reverified',
		'com.argusoft.imtecho.member.state.temporary') and NEW.is_pregnant = true then
		insert into t_member_details(member_id)
		select sync_member_data(NEW.id);



end if;

return new;
end
$$;


ALTER FUNCTION public.update_member_info_after_insert() OWNER TO postgres;

--
-- TOC entry 1012 (class 1255 OID 220344)
-- Name: update_ncd_status_trigger(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_ncd_status_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

begin

if (TG_OP = 'INSERT') then
insert
	into
		ncd_member_status_history (member_id,
		from_status,
		to_status,
		from_sub_status,
		to_sub_status,
		disease_code,
		created_by,
		created_on,
		modified_by,
		modified_on)
	values (new.member_id,
	null,
	new.status,
	null,
	new.sub_status,
	new.disease_code,
	new.modified_by,
	now(),
	new.modified_by,
	now());
end if;


if (TG_OP != 'INSERT'
and (new.status != old.status or new.sub_status != old.sub_status or old.sub_status is null)) then
insert
	into
		ncd_member_status_history (member_id,
		from_status,
		to_status,
		from_sub_status,
		to_sub_status,
		disease_code,
		created_by,
		created_on,
		modified_by,
		modified_on)
	values (new.member_id,
	old.status,
	new.status,
	old.sub_status,
	new.sub_status,
	new.disease_code,
	new.modified_by,
	now(),
	new.modified_by,
	now());
end if;

return new;
end;
$$;


ALTER FUNCTION public.update_ncd_status_trigger() OWNER TO postgres;

--
-- TOC entry 1013 (class 1255 OID 220345)
-- Name: update_notification_info_on_family_location_change(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_notification_info_on_family_location_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  area_id_new bigint;
  area_id_old bigint;
begin
    case
	when
	    new.area_id is null
	then
	    area_id_new := new.location_id;
	else
	    area_id_new := new.area_id;
    end case;

    case
	when
	    old.area_id is null
	then
	    area_id_old := old.location_id;
	else
	    area_id_old := old.area_id;
    end case;

    case
        when
	    area_id_new != area_id_old
	then
            --UPDATING MOBILE NOTIFICATIONS
	    update techo_notification_master set location_id = area_id_new, modified_on = now()
	    where location_id  = area_id_old and member_id in (select id from imt_member where family_id = old.family_id);
	    --UPDATING WEB NOTIFICATIONS
            update techo_web_notification_master set location_id = area_id_new, modified_on = now()
	    where location_id  = area_id_old and member_id in (select id from imt_member where family_id = old.family_id) and
	    notification_type_id in (select id from notification_type_master where notification_for = 'MEMBER');
	else
		return new;
    end case;
    return new;
end
$$;


ALTER FUNCTION public.update_notification_info_on_family_location_change() OWNER TO postgres;

--
-- TOC entry 1014 (class 1255 OID 220346)
-- Name: update_notification_info_on_member_family_change(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.update_notification_info_on_member_family_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  area_id_new bigint;

begin
    area_id_new := CAST((select area_id from imt_family where family_id = new.family_id) as bigint);
    case
	when
	    new.family_id != old.family_id
	then
      	    --UPDATING MOBILE NOTIFICATIONS
            case
                when
                    area_id_new is null or area_id_new = '-1'
                then
                    update techo_notification_master set location_id = (select f.location_id
                    from imt_family f where family_id = new.family_id), modified_on = now() where
                    (location_id  = (select CAST(area_id as bigint) from imt_family where family_id = old.family_id) or
                    location_id  = (select location_id from imt_family where family_id = old.family_id))
                    and member_id = new.id and state in ('PENDING','RESCHEDULE');

                    update techo_web_notification_master set location_id = (select f.location_id
                    from imt_family f where family_id = new.family_id), modified_on = now() where
                    (location_id  = (select CAST(area_id as bigint) from imt_family where family_id = old.family_id) or
                    location_id  = (select location_id from imt_family where family_id = old.family_id))
                    and member_id = new.id and notification_type_id in (select id from notification_type_master where notification_for = 'MEMBER')
                    and state in ('PENDING','RESCHEDULE');
	    --UPDATING WEB NOTIFICATIONS
                else
                    update techo_notification_master set location_id = area_id_new, modified_on = now()
                    where (location_id  = (select CAST(area_id as bigint) from imt_family where family_id = old.family_id) or
                    location_id  = (select location_id from imt_family where family_id = old.family_id))
                    and member_id = new.id and state in ('PENDING','RESCHEDULE');

                    update techo_web_notification_master set location_id = area_id_new, modified_on = now()
                    where (location_id  = (select CAST(area_id as bigint) from imt_family where family_id = old.family_id) or
                    location_id  = (select location_id from imt_family where family_id = old.family_id))
                    and member_id = new.id and notification_type_id in (select id from notification_type_master where notification_for = 'MEMBER')
                    and state in ('PENDING','RESCHEDULE');

                    update rch_pregnancy_registration_det set current_location_id = area_id_new where member_id = new.id
                    and rch_pregnancy_registration_det.state IN ('PREGNANT','PENDING') ;

                end case;
	else
            return new;
    end case;
    return new;
end
$$;


ALTER FUNCTION public.update_notification_info_on_member_family_change() OWNER TO postgres;

--
-- TOC entry 1015 (class 1255 OID 220347)
-- Name: user_menu_item_trigger_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.user_menu_item_trigger_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	if TG_OP = 'INSERT' then
		INSERT INTO public.user_menu_item_change_log(
		    user_menu_id, designation_id, feature_json, menu_config_id,
		    user_id, role_id, activated_on,activated_by)
		VALUES (NEW.user_menu_id,NEW.designation_id, NEW.feature_json, NEW.menu_config_id,
		    NEW.user_id, NEW.role_id, now(),NEW.created_by);
		RETURN NEW;
	ELSIF TG_OP = 'UPDATE' then
		IF NEW.feature_json != OLD.feature_json then

		UPDATE user_menu_item_change_log
		SET deactivated_on = now(),deactivated_by = NEW.modified_by where user_menu_id = NEW.user_menu_id and deactivated_on is null;

		INSERT INTO public.user_menu_item_change_log(
		    user_menu_id, designation_id, feature_json, menu_config_id,
		    user_id, role_id, activated_on,activated_by)
		VALUES (NEW.user_menu_id,NEW.designation_id, NEW.feature_json, NEW.menu_config_id,
		    NEW.user_id, NEW.role_id, now(),NEW.created_by);
		END if;
		RETURN NEW;
	ELSIF TG_OP = 'DELETE' then
		UPDATE user_menu_item_change_log
		SET deactivated_on = now()--,deactivated_by = NEW.modified_by
		where user_menu_id = OLD.user_menu_id and deactivated_on is null;
		RETURN OLD;
	END IF;
END $$;


ALTER FUNCTION public.user_menu_item_trigger_func() OWNER TO postgres;

--
-- TOC entry 1016 (class 1255 OID 220348)
-- Name: uuid_generate_v4(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.uuid_generate_v4() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v4';


ALTER FUNCTION public.uuid_generate_v4() OWNER TO postgres;

--
-- TOC entry 1017 (class 1255 OID 220349)
-- Name: vaccine_status(date, text, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE OR REPLACE FUNCTION public.vaccine_status(dob date, vac text, mem bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
	DECLARE
	days integer;
	months integer;
	weeks integer;
	from_flag boolean := false;
	to_flag boolean := false;
	from_type varchar(1);
	to_type varchar(1);
	from_count integer;
	to_count integer;
	vac_lower text;
        BEGIN
		days := current_date - dob;
		months := extract (year from age(dob)) * 12 + extract (month from age(dob));
		weeks := (days/7)::integer;
		if exists (select 1 from rch_immunisation_master  where member_id = mem and immunisation_given = vac) then
			return 'given';
		end if;
		execute 'select LOWER(''' || vac || ''')' into vac_lower;
		if vac = 'BCG' then
		  if days < 42 then
		   execute 'select bcg1_type_from from child_vaccines_schedule' into from_type;
		   execute 'select bcg1_type_to from child_vaccines_schedule' into to_type;
		   execute 'select bcg1_from from child_vaccines_schedule' into from_count;
	           execute 'select bcg1_to from child_vaccines_schedule' into to_count;
		   if from_type = 'D' and days >= from_count then from_flag := true;
		   elsif from_type = 'W' and weeks >= from_count then from_flag := true;
		   elsif from_type = 'M' and months >= from_count then from_flag := true;
		   end if;
		   if to_type = 'D' and days <= to_count then to_flag := true;
		   elsif to_type = 'W' and weeks <= to_count then to_flag := true;
		   elsif to_type = 'M' and months <= to_count then to_flag := true;
		   end if;
		   if from_flag = true and to_flag = true then return 'not_given';
		   elsif from_flag = false then return 'to_be_given';
		   elsif to_flag = false then return 'missed';
		   end if;
		else
		   execute 'select bcg2_type_from from child_vaccines_schedule' into from_type;
		   execute 'select bcg2_type_to from child_vaccines_schedule' into to_type;
		   execute 'select bcg2_from from child_vaccines_schedule' into from_count;
	           execute 'select bcg2_to from child_vaccines_schedule' into to_count;
		   if from_type = 'D' and days >= from_count then from_flag := true;
		   elsif from_type = 'W' and weeks >= from_count then from_flag := true;
		   elsif from_type = 'M' and months >= from_count then from_flag := true;
		   end if;
		   if to_type = 'D' and days <= to_count then to_flag := true;
		   elsif to_type = 'W' and weeks <= to_count then to_flag := true;
		   elsif to_type = 'M' and months <= to_count then to_flag := true;
		   end if;
		   if from_flag = true and to_flag = true then return 'not_given';
		   elsif from_flag = false then return 'to_be_given';
		   elsif to_flag = false then return 'missed';
		   end if;
		end if;
		else
		execute 'select ' || vac_lower ||  '_type_from from child_vaccines_schedule' into from_type;
		execute 'select ' || vac_lower || '_type_to from child_vaccines_schedule' into to_type;
		execute 'select ' || vac_lower || '_from from child_vaccines_schedule' into from_count;
	        execute 'select ' || vac_lower || '_to from child_vaccines_schedule' into to_count;
		if from_type = 'D' and days >= from_count then from_flag := true;
		   elsif from_type = 'W' and weeks >= from_count then from_flag := true;
		   elsif from_type = 'M' and months >= from_count then from_flag := true;
		end if;
		if to_type = 'D' and days <= from_count then to_flag := true;
		   elsif to_type = 'W' and weeks <= to_count then to_flag := true;
		   elsif to_type = 'M' and months <= to_count then to_flag := true;
		end if;
		if from_flag = true and to_flag = true then return 'not_given';
		   elsif from_flag = false then return 'to_be_given';
		   elsif to_flag = false then return 'missed';
		end if;
	   end if;
        END;
$$;


ALTER FUNCTION public.vaccine_status(dob date, vac text, mem bigint) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 220530)
-- Name: child_analytics_details; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.child_analytics_details (
    member_id integer NOT NULL,
    loc_id integer,
    dob date,
    month_year date,
    year smallint,
    birth_weight numeric(6,2),
    hep_b_date date,
    bcg_date date,
    dpt1_date date,
    penta1_date date,
    opv1_date date,
    dpt2_date date,
    penta2_date date,
    opv2_date date,
    dpt3_date date,
    penta3_date date,
    opv3_date date,
    ipv_date date,
    measles_date date,
    gravida smallint,
    has_birth_defects boolean,
    has_neural_tube_def boolean,
    has_downs_syndrome boolean,
    has_cleft_lip boolean,
    has_club_foot boolean,
    has_dysplassia boolean,
    has_cataract boolean,
    has_deafness boolean,
    has_heart_disease boolean,
    has_retinopathy boolean,
    has_other_defects boolean,
    no_defects boolean,
    has_referred boolean,
    is_infant_death boolean,
    is_neonatal_death boolean,
    is_early_neonatal_death boolean,
    has_asphyxia boolean,
    has_pnuemonia boolean,
    has_diarhoeaa boolean,
    has_measles boolean,
    has_high_fever boolean,
    has_done_breast_feeding_within_one_hour boolean
);


ALTER TABLE IF EXISTS analytics.child_analytics_details OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 220533)
-- Name: child_monthly_analytics_16_24_months_t_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_monthly_analytics_16_24_months_t_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_monthly_analytics_16_24_months_t_id_seq1 OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 220535)
-- Name: child_monthly_analytics_16_24_months; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.child_monthly_analytics_16_24_months (
    id integer DEFAULT nextval('public.child_monthly_analytics_16_24_months_t_id_seq1'::regclass) NOT NULL,
    location_id integer NOT NULL,
    month_year date NOT NULL,
    children_completed_age_16_24_months integer,
    opv_booster_given_16_24_months integer,
    dpt_booster_given_16_24_months integer,
    measles_2_given_16_24_months integer,
    fully_immunized_16_24_months integer,
    not_fully_immunized_16_24_months integer,
    infant_death integer
);


ALTER TABLE IF EXISTS analytics.child_monthly_analytics_16_24_months OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 220563)
-- Name: family_basic_detail; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.family_basic_detail (
    location_id integer NOT NULL,
    family_id text NOT NULL,
    family_head_id integer,
    total_member integer
);


ALTER TABLE IF EXISTS analytics.family_basic_detail OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 220569)
-- Name: family_data; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.family_data (
    family_id character varying(255),
    district_id integer,
    taluka_id integer,
    phc_id integer,
    subcenter_id integer,
    village_id integer,
    anganwadi_id integer,
    uhc_id integer,
    rural_urban character varying(1)
);


ALTER TABLE IF EXISTS analytics.family_data OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 220599)
-- Name: location_wise_task_complition_rate_analysis; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.location_wise_task_complition_rate_analysis (
    location_id integer NOT NULL,
    user_id integer NOT NULL,
    month_year date NOT NULL,
    notification_type_id integer NOT NULL,
    missed_count integer,
    pending_count integer,
    on_completed_count integer,
    after_due_completed_count integer,
    due_pending integer
);


ALTER TABLE IF EXISTS analytics.location_wise_task_complition_rate_analysis OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 220602)
-- Name: member_basic_detail; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.member_basic_detail (
    member_id integer NOT NULL,
    unique_health_id text,
    name text,
    dob date,
    family_id text,
    member_basic_state text,
    family_basic_state text,
    comorbidity text,
    location_id integer,
    gender text
);


ALTER TABLE IF EXISTS analytics.member_basic_detail OWNER TO postgres;

--
-- TOC entry 6464 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN member_basic_detail.gender; Type: COMMENT; Schema: analytics; Owner: postgres
--

COMMENT ON COLUMN analytics.member_basic_detail.gender IS 'gender in member basic detail';


--
-- TOC entry 217 (class 1259 OID 220617)
-- Name: rch_child_analytics_details_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_child_analytics_details_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_child_analytics_details_id_seq1 OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 220619)
-- Name: rch_child_analytics_details; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_child_analytics_details (
    id integer DEFAULT nextval('public.rch_child_analytics_details_id_seq1'::regclass) NOT NULL,
    member_state text,
    member_id integer,
    first_name text,
    middle_name text,
    last_name text,
    member_name text,
    gender text,
    wpd_child_id integer,
    native_loc_id integer,
    delivery_location_id integer,
    loc_id integer,
    dob date,
    dob_month_year date,
    financial_year text,
    death_date date,
    death_location_id integer,
    no_of_days_in_death integer,
    birth_weight numeric(6,2),
    mother_id integer,
    vitamin_k date,
    vitamin_k_loc bigint,
    bcg date,
    bcg_loc integer,
    dpt1 date,
    dpt1_loc integer,
    penta1 date,
    penta1_loc integer,
    opv0 date,
    opv0_loc integer,
    opv1 date,
    opv1_loc integer,
    hep_b date,
    hep_b_loc integer,
    dpt2 date,
    dpt2_loc integer,
    penta2 date,
    penta2_loc integer,
    opv2 date,
    opv2_loc integer,
    dpt3 date,
    dpt3_loc integer,
    penta3 date,
    penta3_loc integer,
    opv3 date,
    opv3_loc integer,
    f_ipv1 date,
    f_ipv1_loc integer,
    f_ipv2 date,
    f_ipv2_loc integer,
    measles date,
    measles_loc integer,
    measles_rubella date,
    measles_rubella_loc integer,
    opv_booster date,
    opv_booster_loc integer,
    dpt_booster date,
    dpt_booster_loc integer,
    measles_2 date,
    measles_2_loc integer,
    measles_rubella_2 date,
    measles_rubella_2_loc integer,
    measles_or_rubella_1 date,
    measles_or_rubella_1_loc integer,
    measles_or_rubella_2 date,
    measles_or_rubella_2_loc integer,
    rota_virus_1 date,
    rota_virus_1_loc bigint,
    rota_virus_2 date,
    rota_virus_2_loc bigint,
    rota_virus_3 date,
    rota_virus_3_loc bigint,
    is_infant_death boolean,
    age_of_mother smallint,
    is_early_reg boolean,
    gravida smallint,
    day_of_delivery integer,
    is_birth_defect_screened boolean,
    has_birth_defects boolean,
    has_neural_tube_def boolean,
    has_downs_syndrome boolean,
    has_cleft_lip boolean,
    has_talipes boolean,
    has_develop_mental_dysplasia_of_hip boolean,
    has_congenital_cataract boolean,
    has_congenital_deafness boolean,
    has_congenital_heart_disease boolean,
    has_retinopathy_of_prematurity boolean,
    has_any_other_birth_defect boolean,
    has_club_foot boolean,
    is_refer boolean,
    has_asphyxia_death boolean,
    has_pnuemonia_death boolean,
    has_diarhoeaa_death boolean,
    has_measles_death boolean,
    has_high_fever_death boolean,
    has_other_death boolean,
    has_lbw_death boolean,
    has_done_breast_feeding_within_one_hour boolean,
    fully_immunization_in_number_of_month smallint,
    full_immunization_date date,
    received_allvaccines boolean,
    is_sam_child boolean,
    sam_child_date timestamp without time zone,
    last_child_service_date date,
    family_id text,
    mam_child integer,
    mam_child_date timestamp without time zone,
    sam_child_normal integer,
    sam_child_normal_date timestamp without time zone,
    death_reason bigint,
    complete_immunization_date date,
    complete_immunization_in_number_of_month smallint,
    full_immunization_date_exclude_bcg date,
    last_sd_score text,
    last_sd_score_date date,
    live_birth_no smallint,
    overdue_immunisation text,
    high_risk_reasons text,
    tracking_location_id integer,
    is_valid_for_tracking_report boolean,
    vitamin_k_health_infra integer,
    vitamin_a date,
    vitamin_a_loc integer,
    vitamin_a_health_infra integer,
    bcg_health_infra integer,
    dpt1_health_infra integer,
    pendta1_health_infra integer,
    opv0_health_infra integer,
    opv1_health_infra integer,
    hep_b_health_infra integer,
    det2_health_infra integer,
    penta2_health_infra integer,
    opv2_health_infra integer,
    dpt3_health_infra integer,
    penta3_health_infra integer,
    opv3_health_infra integer,
    f_ipv1_health_infra integer,
    f_ipv2_health_infra integer,
    measles_health_infra integer,
    opv_booster_health_infra integer,
    opv_health_infra integer,
    dpt_booster_health_infra integer,
    measles_2_health_infra integer,
    measles_rubella_2_health_infra integer,
    measles_or_rubella_1_health_infra integer,
    measles_or_rubella_2_health_infra integer,
    rota_virus_1_health_infra integer,
    rota_virus_2_loc_health_infra integer,
    rota_virus_3_health_infra integer,
    delivery_health_infrastructure integer,
    measles_rubella_health_infra integer,
    death_health_infra_id integer,
    place_of_death text,
    has_sepsis_death boolean
);


ALTER TABLE IF EXISTS analytics.rch_child_analytics_details OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 220626)
-- Name: rch_data_quality_analytics_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_data_quality_analytics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_data_quality_analytics_id_seq OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 220628)
-- Name: rch_data_quality_analytics; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_data_quality_analytics (
    id integer DEFAULT nextval('public.rch_data_quality_analytics_id_seq'::regclass) NOT NULL,
    location_id integer,
    member_id integer,
    verified_on timestamp without time zone,
    verification_type character(255),
    reference_id integer,
    total_service integer,
    success_count integer,
    failed_count integer,
    fields jsonb,
    created_on timestamp without time zone,
    state character(255),
    family_id character varying(255),
    user_id integer
);


ALTER TABLE IF EXISTS analytics.rch_data_quality_analytics OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 220635)
-- Name: rch_eligible_couple_analytics; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_eligible_couple_analytics (
    member_id integer NOT NULL,
    dob date,
    last_method_of_contraception character varying(15),
    loc_id integer,
    children_count integer,
    member_basic_state text
);


ALTER TABLE IF EXISTS analytics.rch_eligible_couple_analytics OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 220641)
-- Name: rch_member_services_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_member_services_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_member_services_id_seq1 OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 220643)
-- Name: rch_member_services; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_member_services (
    id integer DEFAULT nextval('public.rch_member_services_id_seq1'::regclass) NOT NULL,
    location_id integer,
    user_id integer,
    member_id integer,
    service_type character varying(50),
    service_date timestamp without time zone,
    server_date timestamp without time zone,
    created_on timestamp without time zone,
    visit_id integer,
    longitude text,
    latitude text,
    lat_long_location_id integer,
    lat_long_location_distance double precision,
    geo_location_state character varying(50)
);


ALTER TABLE IF EXISTS analytics.rch_member_services OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 220650)
-- Name: rch_phc_month_wise_pmsma_date; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_phc_month_wise_pmsma_date (
    location_id integer NOT NULL,
    month_year date NOT NULL,
    pmsma_date date
);


ALTER TABLE IF EXISTS analytics.rch_phc_month_wise_pmsma_date OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 220653)
-- Name: rch_pmsma_service_statatics; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_pmsma_service_statatics (
    location_id integer NOT NULL,
    month_year date NOT NULL,
    finacial_year text,
    high_risk_mother_2nd_trimester integer,
    total_anc_under_pmsma integer,
    total_beneficiary_under_pmsma integer,
    eligible_high_risk_mother_for_pmsma integer,
    total_beneficiary_under_pmsma_at_least_once integer
);


ALTER TABLE IF EXISTS analytics.rch_pmsma_service_statatics OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 220659)
-- Name: rch_pregnancy_analytics_details; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_pregnancy_analytics_details (
    pregnancy_reg_id integer NOT NULL,
    member_id integer,
    dob date,
    family_id text,
    member_name text,
    mobile_number text,
    reg_service_date date,
    reg_service_date_month_year date,
    reg_service_financial_year text,
    reg_server_date timestamp without time zone,
    pregnancy_reg_location_id integer,
    native_location_id integer,
    pregnancy_reg_family_id integer,
    lmp_date date,
    edd date,
    preg_reg_state text,
    member_basic_state text,
    lmp_month_year date,
    lmp_financial_year text,
    date_of_delivery date,
    date_of_delivery_month_year date,
    delivery_location_id integer,
    delivery_family_id integer,
    delivery_reg_date date,
    delivery_reg_date_financial_year text,
    member_current_location_id integer,
    age_during_delivery smallint,
    registered_with_no_of_child smallint,
    registered_with_male_cnt smallint,
    registered_with_female_cnt smallint,
    anc1 date,
    anc1_location_id integer,
    anc2 date,
    anc2_location_id integer,
    anc3 date,
    anc3_location_id integer,
    anc4 date,
    anc4_location_id integer,
    total_regular_anc smallint,
    tt1_given date,
    tt1_location_id integer,
    tt2_given date,
    tt2_location_id integer,
    tt_boster date,
    tt_booster_location_id integer,
    tt2_tt_booster_given date,
    tt2_tt_booster_location_id integer,
    early_anc boolean,
    total_anc smallint,
    ifa integer,
    fa_tab_in_30_day integer,
    fa_tab_in_31_to_60_day integer,
    fa_tab_in_61_to_90_day integer,
    ifa_tab_in_4_month_to_9_month integer,
    hb_between_90_to_360_days integer,
    hb numeric(6,2),
    total_ca integer,
    ca_tab_in_91_to_180_day integer,
    ca_tab_in_181_to_360_day integer,
    expected_delivery_place text,
    l2l_preg_complication text,
    outcome_l2l_preg text,
    l2l_preg_complication_length smallint,
    outcome_last_preg integer,
    alben_given boolean,
    maternal_detah boolean,
    maternal_death_type text,
    death_date date,
    death_location_id integer,
    low_height boolean,
    urine_albumin boolean,
    systolic_bp smallint,
    diastolic_bp smallint,
    prev_pregnancy_date date,
    prev_preg_diff_in_month smallint,
    gravida smallint,
    jsy_beneficiary boolean,
    jsy_payment_date date,
    any_chronic_dis boolean,
    aadhar_and_bank boolean,
    aadhar_reg boolean,
    aadhar_with_no_bank boolean,
    bank_with_no_aadhar boolean,
    no_aadhar_and_bank boolean,
    high_risk_mother boolean,
    pre_preg_anemia boolean,
    pre_preg_caesarean_section boolean,
    pre_preg_aph boolean,
    pre_preg_pph boolean,
    pre_preg_pre_eclampsia boolean,
    pre_preg_abortion boolean,
    pre_preg_obstructed_labour boolean,
    pre_preg_placenta_previa boolean,
    pre_preg_malpresentation boolean,
    pre_preg_birth_defect boolean,
    pre_preg_preterm_delivery boolean,
    any_prev_preg_complication boolean,
    chro_tb boolean,
    chro_diabetes boolean,
    chro_heart_kidney boolean,
    chro_hiv boolean,
    chro_sickle boolean,
    chro_thalessemia boolean,
    cur_extreme_age boolean,
    cur_low_weight boolean,
    cur_severe_anemia boolean,
    cur_blood_pressure_issue boolean,
    cur_urine_protein_issue boolean,
    cur_convulsion_issue boolean,
    cur_malaria_issue boolean,
    cur_social_vulnerability boolean,
    cur_gestational_diabetes_issue boolean,
    cur_twin_pregnancy boolean,
    cur_mal_presentation_issue boolean,
    cur_absent_reduce_fetal_movment boolean,
    cur_less_than_18_month_interval boolean,
    cur_aph_issue boolean,
    cur_pelvic_sepsis boolean,
    cur_hiv_issue boolean,
    cur_vdrl_issue boolean,
    cur_hbsag_issue boolean,
    cur_brethless_issue boolean,
    any_cur_preg_complication boolean,
    high_risk_cnt smallint,
    hbsag_test_cnt smallint,
    hbsag_reactive_cnt smallint,
    hbsag_non_reactive_cnt smallint,
    delivery_outcome text,
    type_of_delivery text,
    delivery_place text,
    home_del boolean,
    institutional_del boolean,
    delivery_108 boolean,
    breast_feeding_in_one_hour boolean,
    delivery_hospital text,
    del_week smallint,
    is_cortico_steroid boolean,
    mother_alive boolean,
    total_out_come smallint,
    male smallint,
    female smallint,
    delivery_done_by text,
    pnc1 date,
    pnc1_location_id integer,
    pnc2 date,
    pnc2_location_id integer,
    pnc3 date,
    pnc3_location_id integer,
    pnc4 date,
    pnc4_location_id integer,
    haemoglobin_tested_count integer,
    iron_def_anemia_inj text,
    blood_transfusion boolean,
    last_systolic_bp integer,
    last_diastolic_bp integer,
    unique_health_id text,
    member_caste integer,
    is_bpl_family boolean,
    delivery_health_infrastructure integer,
    is_chiranjivi_delivery boolean,
    still_birth smallint,
    live_birth smallint,
    ifa_tab_after_delivery smallint,
    ppiucd_insert_date date,
    ppiucd_insert_location integer,
    delivery_out_of_state_govt boolean,
    delivery_out_of_state_pvt boolean,
    high_risk_reasons text,
    complete_anc_date date,
    ifa_180_anc_date date,
    complete_anc_location integer,
    ifa_180_anc_location integer,
    anc_visit_dates text,
    bp_details text,
    hb_details text,
    urine_test_details text,
    weight_details text,
    vaccines_details text,
    calcium_tablets_details text,
    ifa_tablets_details text,
    fa_tablets_details text,
    is_fru boolean,
    hb_date date,
    tracking_location_id integer,
    is_valid_for_tracking_report boolean,
    pregnancy_reg_health_infra_id integer,
    anc1_health_ifra_id integer,
    tt1_health_infra_id integer,
    tt2_health_infra_id integer,
    tt_boster_health_infra_id integer,
    ifa_180_health_infra_id integer,
    ca_360_anc_date date,
    ca_360_health_infra_id integer,
    discharge_date date,
    calcium_tab_after_delivery smallint,
    is_misoprostol_given boolean,
    free_medicines_jssk_provided boolean,
    free_diet_jssk_provided boolean,
    free_diagnostics_jssk_provided boolean,
    free_drop_back_to_home_jssk_provided boolean,
    vdrl_test text
);


ALTER TABLE IF EXISTS analytics.rch_pregnancy_analytics_details OWNER TO postgres;

--
-- TOC entry 6465 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN rch_pregnancy_analytics_details.vdrl_test; Type: COMMENT; Schema: analytics; Owner: postgres
--

COMMENT ON COLUMN analytics.rch_pregnancy_analytics_details.vdrl_test IS 'Syphilis test';


--
-- TOC entry 227 (class 1259 OID 220665)
-- Name: rch_pregnancy_analytics_dump; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.rch_pregnancy_analytics_dump (
    pregnancy_reg_id integer,
    member_id integer,
    dob date,
    family_id text,
    reg_service_date date,
    reg_service_date_month_year date,
    reg_service_financial_year text,
    reg_server_date timestamp without time zone,
    pregnancy_reg_location_id integer,
    native_location_id integer,
    pregnancy_reg_family_id integer,
    lmp_date date,
    edd date,
    preg_reg_state text,
    member_basic_state text,
    lmp_month_year date,
    lmp_financial_year text,
    date_of_delivery date,
    date_of_delivery_month_year date,
    delivery_location_id integer,
    delivery_family_id integer,
    delivery_reg_date date,
    delivery_reg_date_financial_year text,
    member_current_location_id integer,
    age_during_delivery smallint,
    registered_with_no_of_child smallint,
    registered_with_male_cnt smallint,
    registered_with_female_cnt smallint,
    anc1 date,
    anc1_location_id integer,
    anc2 date,
    anc2_location_id integer,
    anc3 date,
    anc3_location_id integer,
    anc4 date,
    anc4_location_id integer,
    total_regular_anc smallint,
    tt1_given date,
    tt1_location_id integer,
    tt2_given date,
    tt2_location_id integer,
    tt_boster date,
    tt_booster_location_id integer,
    tt2_tt_booster_given date,
    tt2_tt_booster_location_id integer,
    early_anc boolean,
    total_anc smallint,
    ifa integer,
    fa_tab_in_30_day integer,
    fa_tab_in_31_to_60_day integer,
    fa_tab_in_61_to_90_day integer,
    ifa_tab_in_4_month_to_9_month integer,
    hb_between_90_to_360_days integer,
    hb numeric(6,2),
    total_ca integer,
    ca_tab_in_91_to_180_day integer,
    ca_tab_in_181_to_360_day integer,
    expected_delivery_place text,
    l2l_preg_complication text,
    outcome_l2l_preg text,
    l2l_preg_complication_length smallint,
    outcome_last_preg integer,
    alben_given boolean,
    maternal_detah boolean,
    maternal_death_type text,
    death_date date,
    death_location_id integer,
    low_height boolean,
    urine_albumin boolean,
    systolic_bp smallint,
    diastolic_bp smallint,
    prev_pregnancy_date date,
    prev_preg_diff_in_month smallint,
    gravida smallint,
    jsy_beneficiary boolean,
    jsy_payment_date date,
    any_chronic_dis boolean,
    aadhar_and_bank boolean,
    aadhar_reg boolean,
    aadhar_with_no_bank boolean,
    bank_with_no_aadhar boolean,
    no_aadhar_and_bank boolean,
    high_risk_mother boolean,
    pre_preg_anemia boolean,
    pre_preg_caesarean_section boolean,
    pre_preg_aph boolean,
    pre_preg_pph boolean,
    pre_preg_pre_eclampsia boolean,
    pre_preg_abortion boolean,
    pre_preg_obstructed_labour boolean,
    pre_preg_placenta_previa boolean,
    pre_preg_malpresentation boolean,
    pre_preg_birth_defect boolean,
    pre_preg_preterm_delivery boolean,
    any_prev_preg_complication boolean,
    chro_tb boolean,
    chro_diabetes boolean,
    chro_heart_kidney boolean,
    chro_hiv boolean,
    chro_sickle boolean,
    chro_thalessemia boolean,
    cur_extreme_age boolean,
    cur_low_weight boolean,
    cur_severe_anemia boolean,
    cur_blood_pressure_issue boolean,
    cur_urine_protein_issue boolean,
    cur_convulsion_issue boolean,
    cur_malaria_issue boolean,
    cur_social_vulnerability boolean,
    cur_gestational_diabetes_issue boolean,
    cur_twin_pregnancy boolean,
    cur_mal_presentation_issue boolean,
    cur_absent_reduce_fetal_movment boolean,
    cur_less_than_18_month_interval boolean,
    cur_aph_issue boolean,
    cur_pelvic_sepsis boolean,
    cur_hiv_issue boolean,
    cur_vdrl_issue boolean,
    cur_hbsag_issue boolean,
    cur_brethless_issue boolean,
    any_cur_preg_complication boolean,
    high_risk_cnt smallint,
    hbsag_test_cnt smallint,
    hbsag_reactive_cnt smallint,
    hbsag_non_reactive_cnt smallint,
    delivery_outcome text,
    type_of_delivery text,
    delivery_place text,
    home_del boolean,
    institutional_del boolean,
    delivery_108 boolean,
    breast_feeding_in_one_hour boolean,
    delivery_hospital text,
    del_week smallint,
    is_cortico_steroid boolean,
    mother_alive boolean,
    total_out_come smallint,
    male smallint,
    female smallint,
    delivery_done_by text,
    pnc1 date,
    pnc1_location_id integer,
    pnc2 date,
    pnc2_location_id integer,
    pnc3 date,
    pnc3_location_id integer,
    pnc4 date,
    pnc4_location_id integer,
    haemoglobin_tested_count integer,
    iron_def_anemia_inj text,
    blood_transfusion boolean,
    last_systolic_bp integer,
    last_diastolic_bp integer,
    unique_health_id text,
    member_caste integer,
    is_bpl_family boolean,
    delivery_health_infrastructure integer,
    is_chiranjivi_delivery boolean,
    still_birth smallint,
    live_birth smallint,
    ifa_tab_after_delivery smallint,
    ppiucd_insert_date date,
    ppiucd_insert_location integer,
    delivery_out_of_state_govt boolean,
    delivery_out_of_state_pvt boolean,
    high_risk_reasons text,
    complete_anc_date date,
    ifa_180_anc_date date,
    complete_anc_location integer,
    ifa_180_anc_location integer,
    anc_visit_dates text,
    bp_details text,
    hb_details text,
    urine_test_details text,
    weight_details text,
    vaccines_details text,
    calcium_tablets_details text,
    ifa_tablets_details text,
    fa_tablets_details text,
    is_fru boolean,
    hb_date date
);


ALTER TABLE IF EXISTS analytics.rch_pregnancy_analytics_dump OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 220671)
-- Name: soh_timeline_analytics; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.soh_timeline_analytics (
    location_id integer NOT NULL,
    element_name text NOT NULL,
    target double precision,
    male integer,
    female integer,
    chart1 double precision,
    chart2 double precision,
    chart3 double precision,
    chart4 double precision,
    chart5 double precision,
    chart6 double precision,
    chart7 double precision,
    chart8 double precision,
    chart9 double precision,
    chart10 double precision,
    chart11 double precision,
    chart12 double precision,
    chart13 double precision,
    chart14 double precision,
    chart15 double precision,
    value double precision,
    timeline_type character varying(50) NOT NULL,
    created_on timestamp without time zone,
    reporting character varying(255),
    calculatedtarget numeric(12,2),
    color character varying(255),
    percentage numeric(12,2),
    displayvalue text,
    days integer,
    sortpriority integer,
    timeline_sub_type character varying(50)
);


ALTER TABLE IF EXISTS analytics.soh_timeline_analytics OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 220677)
-- Name: soh_timeline_analytics_temp; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.soh_timeline_analytics_temp (
    location_id integer,
    element_name text,
    target double precision,
    male integer,
    female integer,
    chart1 double precision,
    chart2 double precision,
    chart3 double precision,
    chart4 double precision,
    chart5 double precision,
    chart6 double precision,
    chart7 double precision,
    chart8 double precision,
    chart9 double precision,
    chart10 double precision,
    chart11 double precision,
    chart12 double precision,
    chart13 double precision,
    chart14 double precision,
    chart15 double precision,
    value double precision,
    timeline_type character varying(50),
    created_on timestamp without time zone,
    reporting character varying(255),
    calculatedtarget numeric(12,2),
    color character varying(255),
    percentage numeric(12,2),
    displayvalue text,
    days integer,
    sortpriority integer
);


ALTER TABLE IF EXISTS analytics.soh_timeline_analytics_temp OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 220683)
-- Name: state_of_health_master; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.state_of_health_master (
    location_id integer NOT NULL,
    element_name text NOT NULL,
    target double precision,
    male integer,
    female integer,
    chart1 double precision,
    chart2 double precision,
    chart3 double precision,
    chart4 double precision,
    yesterday double precision,
    last7days double precision,
    last30days double precision,
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.state_of_health_master OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 220689)
-- Name: system_database_size; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.system_database_size (
    id bigint NOT NULL,
    created_on date,
    database_name character varying(255),
    size bigint
);


ALTER TABLE IF EXISTS analytics.system_database_size OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 220692)
-- Name: system_database_size_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.system_database_size_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.system_database_size_id_seq OWNER TO postgres;

--
-- TOC entry 6466 (class 0 OID 0)
-- Dependencies: 232
-- Name: system_database_size_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.system_database_size_id_seq OWNED BY analytics.system_database_size.id;


--
-- TOC entry 233 (class 1259 OID 220694)
-- Name: system_database_table_size; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.system_database_table_size (
    id bigint NOT NULL,
    created_on date,
    database_name character varying(255),
    table_name character varying(1024),
    size bigint
);


ALTER TABLE IF EXISTS analytics.system_database_table_size OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 220700)
-- Name: system_database_table_size_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.system_database_table_size_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.system_database_table_size_id_seq OWNER TO postgres;

--
-- TOC entry 6467 (class 0 OID 0)
-- Dependencies: 234
-- Name: system_database_table_size_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.system_database_table_size_id_seq OWNED BY analytics.system_database_table_size.id;


--
-- TOC entry 235 (class 1259 OID 220702)
-- Name: um_user_day_wise_login_detail; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.um_user_day_wise_login_detail (
    login_date date NOT NULL,
    month_year date,
    location_id integer NOT NULL,
    role_id integer NOT NULL,
    user_id integer NOT NULL,
    is_logged_in boolean
);


ALTER TABLE IF EXISTS analytics.um_user_day_wise_login_detail OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 220705)
-- Name: um_user_month_wise_login_rate; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.um_user_month_wise_login_rate (
    month_year date NOT NULL,
    location_id integer NOT NULL,
    role_id integer NOT NULL,
    user_id integer NOT NULL,
    number_of_days integer,
    number_of_days_login integer,
    location_ids text
);


ALTER TABLE IF EXISTS analytics.um_user_month_wise_login_rate OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 220711)
-- Name: um_user_score_rank_details; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.um_user_score_rank_details (
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    score numeric(20,8) NOT NULL,
    score_date date NOT NULL,
    a_score numeric(20,8),
    b_score numeric(20,8),
    c_score numeric(20,8),
    d_score numeric(20,8),
    service_day_diff integer,
    total_service integer,
    login_day integer,
    total_working_day integer,
    total_notification integer,
    due_notification integer,
    login_rate numeric(10,6),
    timely_task_complete_rate numeric(10,6),
    over_due_pending_task integer,
    diff_bwn_service_entry_date_rate numeric(10,6),
    expected_pregnancy numeric(21,6),
    registered_pregnancy numeric(21,6),
    total_service_call integer,
    total_success_service_call integer
);


ALTER TABLE IF EXISTS analytics.um_user_score_rank_details OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 220714)
-- Name: user_wise_feature_time_taken_detail_analytics; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.user_wise_feature_time_taken_detail_analytics (
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    page_title_id integer NOT NULL,
    avg_active_time numeric,
    max_active_time integer,
    no_of_times integer,
    on_date date NOT NULL
);


ALTER TABLE IF EXISTS analytics.user_wise_feature_time_taken_detail_analytics OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 220720)
-- Name: location_hierchy_closer_det; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_hierchy_closer_det (
    id integer NOT NULL,
    child_id integer NOT NULL,
    child_loc_type character varying(255) NOT NULL,
    depth integer NOT NULL,
    parent_id integer NOT NULL,
    parent_loc_type character varying(255) NOT NULL,
    child_loc_demographic_type character varying(3),
    parent_loc_demographic_type character varying(3)
);


ALTER TABLE IF EXISTS public.location_hierchy_closer_det OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 220726)
-- Name: vw_location_closure_det_by_parent_location_depth; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_location_closure_det_by_parent_location_depth AS
 SELECT parent.parent_id AS location_id,
    child.parent_id,
    child.child_id,
    parent.depth,
    child.child_loc_demographic_type AS child_demographic_type
   FROM public.location_hierchy_closer_det parent,
    public.location_hierchy_closer_det child
  WHERE (child.parent_id = parent.child_id);


ALTER TABLE IF EXISTS analytics.vw_location_closure_det_by_parent_location_depth OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 220730)
-- Name: location_demographic_type_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_demographic_type_master (
    demographic_type text NOT NULL,
    demographic_group text NOT NULL
);


ALTER TABLE IF EXISTS public.location_demographic_type_master OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 220736)
-- Name: vw_location_closure_det_with_demographic_by_parent_location_dep; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_location_closure_det_with_demographic_by_parent_location_dep AS
 SELECT parent.parent_id AS main_location_id,
    parent.depth AS p_depth,
    child.parent_id,
    child.child_id,
    parent.depth,
    child.child_loc_demographic_type AS child_demographic_type,
    dtype.demographic_group
   FROM public.location_hierchy_closer_det parent,
    public.location_hierchy_closer_det child,
    public.location_demographic_type_master dtype
  WHERE ((child.parent_id = parent.child_id) AND ((child.child_loc_demographic_type)::text = dtype.demographic_type));


ALTER TABLE IF EXISTS analytics.vw_location_closure_det_with_demographic_by_parent_location_dep OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 220740)
-- Name: location_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_master (
    id integer NOT NULL,
    address character varying(300),
    associated_user character varying(50),
    contact1_email character varying(50),
    contact1_name character varying(50),
    contact1_phone character varying(15),
    contact2_email character varying(50),
    contact2_name character varying(50),
    contact2_phone character varying(20),
    created_by character varying(50) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    max_users smallint,
    modified_by character varying(50),
    modified_on timestamp without time zone,
    name character varying(4000) NOT NULL,
    pin_code character varying(15),
    type character varying(10) NOT NULL,
    unique_id character varying(50),
    parent integer,
    is_tba_avaiable boolean,
    total_population integer,
    location_code bigint,
    state character varying(100),
    location_flag character varying(255),
    census_population integer,
    is_cmtc_present boolean,
    english_name text,
    is_nrc_present boolean,
    cerebral_palsy_module boolean,
    lgd_code text,
    mdds_code text,
    anmol_location_id integer,
    geo_fencing boolean,
    rch_code bigint,
    demographic_type text,
    rch_integration boolean,
    cm_dashboard_code text,
    is_taaho boolean,
    health_block_type character varying(64),
    health_block_taluka character varying(255),
    nin_number character varying(255),
    health_facility_type character varying(255),
    is_rural_or_urban character varying(64),
    is_notional_or_physical character varying(64),
    phc_taluka character varying(255),
    subcenter_taluka character varying(255),
    village_type character varying(64),
    village_status character varying(64),
    village_taluka character varying(255)
);


ALTER TABLE IF EXISTS public.location_master OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 220746)
-- Name: um_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    aadhar_number character varying(255),
    address character varying(100),
    code character varying(255),
    contact_number character varying(15),
    date_of_birth date,
    email_id character varying(150),
    first_name character varying(100) NOT NULL,
    gender character varying(15),
    last_name character varying(100) NOT NULL,
    middle_name character varying(100),
    password character varying(250),
    prefered_language character varying(30),
    role_id integer,
    state character varying(255),
    user_name character varying(60) NOT NULL,
    server_type character varying(10),
    search_text character varying(1500),
    title character varying(10),
    imei_number character varying(100),
    techo_phone_number character varying(100),
    aadhar_number_encrypted character varying(24),
    no_of_attempts integer,
    rch_institution_master_id integer,
    infrastructure_id integer,
    sdk_version integer,
    free_space_mb integer,
    latitude text,
    longitude text,
    report_preferred_language character varying(30),
    login_code character varying(255),
    convox_id text,
    activation_date timestamp without time zone,
    member_master_id integer,
    location_id integer,
    pincode integer,
    pin character varying(10),
    first_time_password_changed boolean DEFAULT false
);


ALTER TABLE IF EXISTS public.um_user OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 220753)
-- Name: vw_location_detail_with_user_prefer_language; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_location_detail_with_user_prefer_language AS
 SELECT u.id AS user_id,
    l.id AS location_id,
        CASE
            WHEN ((u.report_preferred_language)::text = 'EN'::text) THEN (l.english_name)::character varying
            ELSE l.name
        END AS location_name
   FROM public.um_user u,
    public.location_master l;


ALTER TABLE IF EXISTS analytics.vw_location_detail_with_user_prefer_language OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 220758)
-- Name: vw_location_hierarchy_detail_with_user_prefer_language; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_location_hierarchy_detail_with_user_prefer_language AS
 SELECT u.id AS user_id,
    lhcd.child_id AS location_id,
    string_agg((
        CASE
            WHEN ((u.report_preferred_language)::text = 'EN'::text) THEN (l.english_name)::character varying
            ELSE l.name
        END)::text, ' >'::text ORDER BY lhcd.depth DESC) AS location_hierarchy_name
   FROM public.um_user u,
    public.location_master l,
    public.location_hierchy_closer_det lhcd
  WHERE (lhcd.parent_id = l.id)
  GROUP BY u.id, lhcd.child_id;


ALTER TABLE IF EXISTS analytics.vw_location_hierarchy_detail_with_user_prefer_language OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 220763)
-- Name: vw_rch_pregnancy_analytics_details; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_rch_pregnancy_analytics_details AS
 SELECT rch_pregnancy_analytics_details.pregnancy_reg_id,
    rch_pregnancy_analytics_details.member_id,
    rch_pregnancy_analytics_details.dob,
    rch_pregnancy_analytics_details.family_id,
    rch_pregnancy_analytics_details.member_name,
    rch_pregnancy_analytics_details.mobile_number,
    rch_pregnancy_analytics_details.reg_service_date,
    rch_pregnancy_analytics_details.reg_service_date_month_year,
    rch_pregnancy_analytics_details.reg_service_financial_year,
    rch_pregnancy_analytics_details.reg_server_date,
    rch_pregnancy_analytics_details.pregnancy_reg_location_id,
    rch_pregnancy_analytics_details.native_location_id,
    rch_pregnancy_analytics_details.pregnancy_reg_family_id,
    rch_pregnancy_analytics_details.lmp_date,
    rch_pregnancy_analytics_details.edd,
    rch_pregnancy_analytics_details.preg_reg_state,
    rch_pregnancy_analytics_details.member_basic_state,
    rch_pregnancy_analytics_details.lmp_month_year,
    rch_pregnancy_analytics_details.lmp_financial_year,
    rch_pregnancy_analytics_details.date_of_delivery,
    rch_pregnancy_analytics_details.date_of_delivery_month_year,
    rch_pregnancy_analytics_details.delivery_location_id,
    rch_pregnancy_analytics_details.delivery_family_id,
    rch_pregnancy_analytics_details.delivery_reg_date,
    rch_pregnancy_analytics_details.delivery_reg_date_financial_year,
    rch_pregnancy_analytics_details.member_current_location_id,
    rch_pregnancy_analytics_details.age_during_delivery,
    rch_pregnancy_analytics_details.registered_with_no_of_child,
    rch_pregnancy_analytics_details.registered_with_male_cnt,
    rch_pregnancy_analytics_details.registered_with_female_cnt,
    rch_pregnancy_analytics_details.anc1,
    rch_pregnancy_analytics_details.anc1_location_id,
    rch_pregnancy_analytics_details.anc2,
    rch_pregnancy_analytics_details.anc2_location_id,
    rch_pregnancy_analytics_details.anc3,
    rch_pregnancy_analytics_details.anc3_location_id,
    rch_pregnancy_analytics_details.anc4,
    rch_pregnancy_analytics_details.anc4_location_id,
    rch_pregnancy_analytics_details.total_regular_anc,
    rch_pregnancy_analytics_details.tt1_given,
    rch_pregnancy_analytics_details.tt1_location_id,
    rch_pregnancy_analytics_details.tt2_given,
    rch_pregnancy_analytics_details.tt2_location_id,
    rch_pregnancy_analytics_details.tt_boster,
    rch_pregnancy_analytics_details.tt_booster_location_id,
    rch_pregnancy_analytics_details.tt2_tt_booster_given,
    rch_pregnancy_analytics_details.tt2_tt_booster_location_id,
    rch_pregnancy_analytics_details.early_anc,
    rch_pregnancy_analytics_details.total_anc,
    rch_pregnancy_analytics_details.ifa,
    rch_pregnancy_analytics_details.fa_tab_in_30_day,
    rch_pregnancy_analytics_details.fa_tab_in_31_to_60_day,
    rch_pregnancy_analytics_details.fa_tab_in_61_to_90_day,
    rch_pregnancy_analytics_details.ifa_tab_in_4_month_to_9_month,
    rch_pregnancy_analytics_details.hb_between_90_to_360_days,
    rch_pregnancy_analytics_details.hb,
    rch_pregnancy_analytics_details.total_ca,
    rch_pregnancy_analytics_details.ca_tab_in_91_to_180_day,
    rch_pregnancy_analytics_details.ca_tab_in_181_to_360_day,
    rch_pregnancy_analytics_details.expected_delivery_place,
    rch_pregnancy_analytics_details.l2l_preg_complication,
    rch_pregnancy_analytics_details.outcome_l2l_preg,
    rch_pregnancy_analytics_details.l2l_preg_complication_length,
    rch_pregnancy_analytics_details.outcome_last_preg,
    rch_pregnancy_analytics_details.alben_given,
    rch_pregnancy_analytics_details.maternal_detah,
    rch_pregnancy_analytics_details.maternal_death_type,
    rch_pregnancy_analytics_details.death_date,
    rch_pregnancy_analytics_details.death_location_id,
    rch_pregnancy_analytics_details.low_height,
    rch_pregnancy_analytics_details.urine_albumin,
    rch_pregnancy_analytics_details.systolic_bp,
    rch_pregnancy_analytics_details.diastolic_bp,
    rch_pregnancy_analytics_details.prev_pregnancy_date,
    rch_pregnancy_analytics_details.prev_preg_diff_in_month,
    rch_pregnancy_analytics_details.gravida,
    rch_pregnancy_analytics_details.jsy_beneficiary,
    rch_pregnancy_analytics_details.jsy_payment_date,
    rch_pregnancy_analytics_details.any_chronic_dis,
    rch_pregnancy_analytics_details.aadhar_and_bank,
    rch_pregnancy_analytics_details.aadhar_reg,
    rch_pregnancy_analytics_details.aadhar_with_no_bank,
    rch_pregnancy_analytics_details.bank_with_no_aadhar,
    rch_pregnancy_analytics_details.no_aadhar_and_bank,
    rch_pregnancy_analytics_details.high_risk_mother,
    rch_pregnancy_analytics_details.pre_preg_anemia,
    rch_pregnancy_analytics_details.pre_preg_caesarean_section,
    rch_pregnancy_analytics_details.pre_preg_aph,
    rch_pregnancy_analytics_details.pre_preg_pph,
    rch_pregnancy_analytics_details.pre_preg_pre_eclampsia,
    rch_pregnancy_analytics_details.pre_preg_abortion,
    rch_pregnancy_analytics_details.pre_preg_obstructed_labour,
    rch_pregnancy_analytics_details.pre_preg_placenta_previa,
    rch_pregnancy_analytics_details.pre_preg_malpresentation,
    rch_pregnancy_analytics_details.pre_preg_birth_defect,
    rch_pregnancy_analytics_details.pre_preg_preterm_delivery,
    rch_pregnancy_analytics_details.any_prev_preg_complication,
    rch_pregnancy_analytics_details.chro_tb,
    rch_pregnancy_analytics_details.chro_diabetes,
    rch_pregnancy_analytics_details.chro_heart_kidney,
    rch_pregnancy_analytics_details.chro_hiv,
    rch_pregnancy_analytics_details.chro_sickle,
    rch_pregnancy_analytics_details.chro_thalessemia,
    rch_pregnancy_analytics_details.cur_extreme_age,
    rch_pregnancy_analytics_details.cur_low_weight,
    rch_pregnancy_analytics_details.cur_severe_anemia,
    rch_pregnancy_analytics_details.cur_blood_pressure_issue,
    rch_pregnancy_analytics_details.cur_urine_protein_issue,
    rch_pregnancy_analytics_details.cur_convulsion_issue,
    rch_pregnancy_analytics_details.cur_malaria_issue,
    rch_pregnancy_analytics_details.cur_social_vulnerability,
    rch_pregnancy_analytics_details.cur_gestational_diabetes_issue,
    rch_pregnancy_analytics_details.cur_twin_pregnancy,
    rch_pregnancy_analytics_details.cur_mal_presentation_issue,
    rch_pregnancy_analytics_details.cur_absent_reduce_fetal_movment,
    rch_pregnancy_analytics_details.cur_less_than_18_month_interval,
    rch_pregnancy_analytics_details.cur_aph_issue,
    rch_pregnancy_analytics_details.cur_pelvic_sepsis,
    rch_pregnancy_analytics_details.cur_hiv_issue,
    rch_pregnancy_analytics_details.cur_vdrl_issue,
    rch_pregnancy_analytics_details.cur_hbsag_issue,
    rch_pregnancy_analytics_details.cur_brethless_issue,
    rch_pregnancy_analytics_details.any_cur_preg_complication,
    rch_pregnancy_analytics_details.high_risk_cnt,
    rch_pregnancy_analytics_details.hbsag_test_cnt,
    rch_pregnancy_analytics_details.hbsag_reactive_cnt,
    rch_pregnancy_analytics_details.hbsag_non_reactive_cnt,
    rch_pregnancy_analytics_details.delivery_outcome,
    rch_pregnancy_analytics_details.type_of_delivery,
    rch_pregnancy_analytics_details.delivery_place,
    rch_pregnancy_analytics_details.home_del,
    rch_pregnancy_analytics_details.institutional_del,
    rch_pregnancy_analytics_details.delivery_108,
    rch_pregnancy_analytics_details.breast_feeding_in_one_hour,
    rch_pregnancy_analytics_details.delivery_hospital,
    rch_pregnancy_analytics_details.del_week,
    rch_pregnancy_analytics_details.is_cortico_steroid,
    rch_pregnancy_analytics_details.mother_alive,
    rch_pregnancy_analytics_details.total_out_come,
    rch_pregnancy_analytics_details.male,
    rch_pregnancy_analytics_details.female,
    rch_pregnancy_analytics_details.delivery_done_by,
    rch_pregnancy_analytics_details.pnc1,
    rch_pregnancy_analytics_details.pnc1_location_id,
    rch_pregnancy_analytics_details.pnc2,
    rch_pregnancy_analytics_details.pnc2_location_id,
    rch_pregnancy_analytics_details.pnc3,
    rch_pregnancy_analytics_details.pnc3_location_id,
    rch_pregnancy_analytics_details.pnc4,
    rch_pregnancy_analytics_details.pnc4_location_id,
    rch_pregnancy_analytics_details.haemoglobin_tested_count,
    rch_pregnancy_analytics_details.iron_def_anemia_inj,
    rch_pregnancy_analytics_details.blood_transfusion,
    rch_pregnancy_analytics_details.last_systolic_bp,
    rch_pregnancy_analytics_details.last_diastolic_bp,
    rch_pregnancy_analytics_details.unique_health_id
   FROM analytics.rch_pregnancy_analytics_details;


ALTER TABLE IF EXISTS analytics.vw_rch_pregnancy_analytics_details OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 220768)
-- Name: um_role_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_role_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    code character varying(255),
    description character varying(500),
    name character varying(50) NOT NULL,
    state character varying(255),
    max_position integer,
    is_email_mandatory boolean,
    is_contact_num_mandatory boolean,
    is_aadhar_num_mandatory boolean,
    is_convox_id_mandatory boolean,
    short_name character varying(20),
    is_last_name_mandatory boolean,
    role_type text,
    is_health_infra_mandatory boolean,
    is_geolocation_mandatory boolean,
    max_health_infra integer
);


ALTER TABLE IF EXISTS public.um_role_master OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 220774)
-- Name: um_user_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_location (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    loc_id integer,
    state character varying(255),
    type character varying(255) NOT NULL,
    user_id integer,
    hierarchy_type character varying(50),
    heirarchy_type character varying(50),
    level integer
);


ALTER TABLE IF EXISTS public.um_user_location OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 220780)
-- Name: vw_user_and_location_hierarchy_detail_with_user_prefer_language; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_user_and_location_hierarchy_detail_with_user_prefer_language AS
 SELECT u.id AS user_id,
    lhcd.child_id AS location_id,
    uu.contact_number,
    uu.first_name,
    uu.middle_name,
    uu.last_name,
    uu.user_name,
    concat_ws(' '::text, uu.first_name, uu.middle_name, uu.last_name) AS full_name,
    urm.name AS role_name,
    urm.code AS role_code,
    uu.role_id,
    string_agg((
        CASE
            WHEN ((u.report_preferred_language)::text = 'EN'::text) THEN (l.english_name)::character varying
            ELSE l.name
        END)::text, ' >'::text ORDER BY lhcd.depth DESC) AS location_hierarchy_name
   FROM public.um_user u,
    ((((public.location_master l
     JOIN public.location_hierchy_closer_det lhcd ON ((lhcd.parent_id = l.id)))
     LEFT JOIN public.um_user_location uul ON (((uul.loc_id = lhcd.parent_id) AND ((uul.state)::text = 'ACTIVE'::text))))
     LEFT JOIN public.um_user uu ON (((uu.id = uul.user_id) AND ((uu.state)::text = 'ACTIVE'::text))))
     LEFT JOIN public.um_role_master urm ON ((urm.id = uu.role_id)))
  GROUP BY u.id, lhcd.child_id, uu.contact_number, uu.first_name, uu.middle_name, uu.last_name, uu.user_name, urm.name, urm.code, uu.role_id
 LIMIT 10;


ALTER TABLE IF EXISTS analytics.vw_user_and_location_hierarchy_detail_with_user_prefer_language OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 220785)
-- Name: vw_user_detail_location_wise; Type: VIEW; Schema: analytics; Owner: postgres
--

CREATE OR REPLACE VIEW analytics.vw_user_detail_location_wise AS
 SELECT uu.id AS user_id,
    uul.loc_id AS user_location_id,
    uu.contact_number,
    uu.first_name,
    uu.middle_name,
    uu.last_name,
    uu.user_name,
    concat_ws(' '::text, uu.first_name, uu.middle_name, uu.last_name) AS full_name,
    urm.name AS role_name,
    urm.code AS role_code,
    uu.role_id,
    lhcd.child_id AS location_id
   FROM public.um_user_location uul,
    public.um_user uu,
    public.location_hierchy_closer_det lhcd,
    public.location_master lm,
    public.um_role_master urm
  WHERE ((uu.id = uul.user_id) AND (uul.loc_id = lhcd.parent_id) AND ((uu.state)::text = 'ACTIVE'::text) AND ((uul.state)::text = 'ACTIVE'::text) AND (urm.id = uu.role_id));


ALTER TABLE IF EXISTS analytics.vw_user_detail_location_wise OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 220790)
-- Name: wt_asha_10days_overdue_notification_details; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_asha_10days_overdue_notification_details (
    id integer NOT NULL,
    location_id integer,
    member_id integer,
    notification_start_date timestamp without time zone,
    notification_due_date timestamp without time zone,
    notification_expiry_date timestamp without time zone,
    notification_type text
);


ALTER TABLE IF EXISTS analytics.wt_asha_10days_overdue_notification_details OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 220796)
-- Name: wt_asha_10days_overdue_notification_details_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_asha_10days_overdue_notification_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_asha_10days_overdue_notification_details_id_seq OWNER TO postgres;

--
-- TOC entry 6468 (class 0 OID 0)
-- Dependencies: 253
-- Name: wt_asha_10days_overdue_notification_details_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_asha_10days_overdue_notification_details_id_seq OWNED BY analytics.wt_asha_10days_overdue_notification_details.id;


--
-- TOC entry 254 (class 1259 OID 220798)
-- Name: wt_cfhc_suspected_disease; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_cfhc_suspected_disease (
    id integer NOT NULL,
    member_id integer,
    location_id integer,
    family_id character varying,
    type_of_disease text,
    identified_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    status text
);


ALTER TABLE IF EXISTS analytics.wt_cfhc_suspected_disease OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 220804)
-- Name: wt_cfhc_suspected_disease_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_cfhc_suspected_disease_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_cfhc_suspected_disease_id_seq OWNER TO postgres;

--
-- TOC entry 6469 (class 0 OID 0)
-- Dependencies: 255
-- Name: wt_cfhc_suspected_disease_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_cfhc_suspected_disease_id_seq OWNED BY analytics.wt_cfhc_suspected_disease.id;


--
-- TOC entry 256 (class 1259 OID 220806)
-- Name: wt_high_risk_confirm_case_det; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_high_risk_confirm_case_det (
    pregnancy_reg_det_id integer NOT NULL,
    member_id integer,
    location_id integer,
    high_risk_det text,
    last_service_date date,
    last_service_high_risk text,
    is_pregnant boolean
);


ALTER TABLE IF EXISTS analytics.wt_high_risk_confirm_case_det OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 220812)
-- Name: wt_high_risk_member_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.wt_high_risk_member_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.wt_high_risk_member_detail_id_seq OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 220814)
-- Name: wt_high_risk_member_detail; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_high_risk_member_detail (
    id integer DEFAULT nextval('public.wt_high_risk_member_detail_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    pregnancy_reg_id integer,
    location_id integer,
    diseases_type text,
    high_risk_diseases text,
    high_risk_diseases_detail text,
    service_date date,
    state text,
    ref_id integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_high_risk_member_detail OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 220821)
-- Name: wt_last_30_days_not_registerd_any_pregnancy; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_30_days_not_registerd_any_pregnancy (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_submitted_data_time timestamp without time zone,
    next_submitted_data_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_30_days_not_registerd_any_pregnancy OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 220827)
-- Name: wt_last_30_days_not_registerd_any_pregnancy_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_30_days_not_registerd_any_pregnancy_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_30_days_not_registerd_any_pregnancy_id_seq OWNER TO postgres;

--
-- TOC entry 6470 (class 0 OID 0)
-- Dependencies: 260
-- Name: wt_last_30_days_not_registerd_any_pregnancy_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_30_days_not_registerd_any_pregnancy_id_seq OWNED BY analytics.wt_last_30_days_not_registerd_any_pregnancy.id;


--
-- TOC entry 261 (class 1259 OID 220829)
-- Name: wt_last_4_days_not_logged_in; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 220835)
-- Name: wt_last_4_days_not_logged_in_asha; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_asha (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_asha OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 220841)
-- Name: wt_last_4_days_not_logged_in_asha_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_asha_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_asha_id_seq OWNER TO postgres;

--
-- TOC entry 6471 (class 0 OID 0)
-- Dependencies: 263
-- Name: wt_last_4_days_not_logged_in_asha_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_asha_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_asha.id;


--
-- TOC entry 264 (class 1259 OID 220843)
-- Name: wt_last_4_days_not_logged_in_cho_hwc; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_cho_hwc (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_cho_hwc OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 220849)
-- Name: wt_last_4_days_not_logged_in_cho_hwc_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_cho_hwc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_cho_hwc_id_seq OWNER TO postgres;

--
-- TOC entry 6472 (class 0 OID 0)
-- Dependencies: 265
-- Name: wt_last_4_days_not_logged_in_cho_hwc_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_cho_hwc_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_cho_hwc.id;


--
-- TOC entry 266 (class 1259 OID 220851)
-- Name: wt_last_4_days_not_logged_in_fhs; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_fhs (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_fhs OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 220857)
-- Name: wt_last_4_days_not_logged_in_fhs_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_fhs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_fhs_id_seq OWNER TO postgres;

--
-- TOC entry 6473 (class 0 OID 0)
-- Dependencies: 267
-- Name: wt_last_4_days_not_logged_in_fhs_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_fhs_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_fhs.id;


--
-- TOC entry 268 (class 1259 OID 220859)
-- Name: wt_last_4_days_not_logged_in_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_id_seq OWNER TO postgres;

--
-- TOC entry 6474 (class 0 OID 0)
-- Dependencies: 268
-- Name: wt_last_4_days_not_logged_in_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in.id;


--
-- TOC entry 269 (class 1259 OID 220861)
-- Name: wt_last_4_days_not_logged_in_phc_chc_deo; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_phc_chc_deo (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_phc_chc_deo OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 220867)
-- Name: wt_last_4_days_not_logged_in_phc_chc_deo_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_phc_chc_deo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_phc_chc_deo_id_seq OWNER TO postgres;

--
-- TOC entry 6475 (class 0 OID 0)
-- Dependencies: 270
-- Name: wt_last_4_days_not_logged_in_phc_chc_deo_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_phc_chc_deo_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_phc_chc_deo.id;


--
-- TOC entry 271 (class 1259 OID 220869)
-- Name: wt_last_4_days_not_logged_in_sd_mch_deo; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_sd_mch_deo (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_sd_mch_deo OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 220875)
-- Name: wt_last_4_days_not_logged_in_sd_mch_deo_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_sd_mch_deo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_sd_mch_deo_id_seq OWNER TO postgres;

--
-- TOC entry 6476 (class 0 OID 0)
-- Dependencies: 272
-- Name: wt_last_4_days_not_logged_in_sd_mch_deo_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_sd_mch_deo_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_sd_mch_deo.id;


--
-- TOC entry 273 (class 1259 OID 220877)
-- Name: wt_last_4_days_not_logged_in_tho; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_tho (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_logged_in_time timestamp without time zone,
    next_logged_in_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_tho OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 220883)
-- Name: wt_last_4_days_not_logged_in_tho_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_4_days_not_logged_in_tho_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_4_days_not_logged_in_tho_id_seq OWNER TO postgres;

--
-- TOC entry 6477 (class 0 OID 0)
-- Dependencies: 274
-- Name: wt_last_4_days_not_logged_in_tho_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_4_days_not_logged_in_tho_id_seq OWNED BY analytics.wt_last_4_days_not_logged_in_tho.id;


--
-- TOC entry 275 (class 1259 OID 220885)
-- Name: wt_last_7_days_not_subbmitted_any_data; Type: TABLE; Schema: analytics; Owner: postgres
--

CREATE TABLE IF NOT EXISTS analytics.wt_last_7_days_not_subbmitted_any_data (
    id integer NOT NULL,
    user_id integer,
    from_date date,
    to_date date,
    last_submitted_data_time timestamp without time zone,
    next_submitted_data_time timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS analytics.wt_last_7_days_not_subbmitted_any_data OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 220891)
-- Name: wt_last_7_days_not_subbmitted_any_data_id_seq; Type: SEQUENCE; Schema: analytics; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS analytics.wt_last_7_days_not_subbmitted_any_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS analytics.wt_last_7_days_not_subbmitted_any_data_id_seq OWNER TO postgres;

--
-- TOC entry 6478 (class 0 OID 0)
-- Dependencies: 276
-- Name: wt_last_7_days_not_subbmitted_any_data_id_seq; Type: SEQUENCE OWNED BY; Schema: analytics; Owner: postgres
--

ALTER SEQUENCE analytics.wt_last_7_days_not_subbmitted_any_data_id_seq OWNED BY analytics.wt_last_7_days_not_subbmitted_any_data.id;


--
-- TOC entry 277 (class 1259 OID 220893)
-- Name: activity_type; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.activity_type (
    activity_id character varying(255) NOT NULL,
    activity_name character varying(255) NOT NULL
);


ALTER TABLE IF EXISTS archive.activity_type OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 220907)
-- Name: app_version_response; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.app_version_response (
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS archive.app_version_response OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 220933)
-- Name: deleted_locations; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.deleted_locations (
    id integer NOT NULL,
    location_id integer,
    address character varying(300),
    associated_user character varying(50),
    contact1_email character varying(50),
    contact1_name character varying(50),
    contact1_phone character varying(15),
    contact2_email character varying(50),
    contact2_name character varying(50),
    contact2_phone character varying(20),
    created_by character varying(50) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    max_users smallint,
    modified_by character varying(50),
    modified_on timestamp without time zone,
    name character varying(4000) NOT NULL,
    pin_code character varying(15),
    type character varying(10) NOT NULL,
    unique_id character varying(50),
    parent integer,
    is_tba_avaiable boolean,
    total_population integer,
    location_code character varying(255),
    state character varying(100),
    location_flag character varying(255),
    census_population integer,
    is_cmtc_present boolean,
    english_name text,
    is_nrc_present boolean,
    has_active_user boolean,
    has_family boolean,
    deleted_on timestamp without time zone,
    has_migrated_in_members boolean
);


ALTER TABLE IF EXISTS archive.deleted_locations OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 220939)
-- Name: deleted_locations_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.deleted_locations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.deleted_locations_id_seq OWNER TO postgres;

--
-- TOC entry 6480 (class 0 OID 0)
-- Dependencies: 282
-- Name: deleted_locations_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.deleted_locations_id_seq OWNED BY archive.deleted_locations.id;


--
-- TOC entry 283 (class 1259 OID 220941)
-- Name: deleted_users; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.deleted_users (
    id integer NOT NULL,
    user_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    aadhar_number character varying(255),
    address character varying(100),
    code character varying(255),
    contact_number character varying(15),
    date_of_birth date,
    email_id character varying(150),
    first_name character varying(100) NOT NULL,
    gender character varying(15),
    last_name character varying(100) NOT NULL,
    middle_name character varying(100),
    password character varying(250),
    prefered_language character varying(30),
    role_id integer,
    state character varying(255),
    user_name character varying(60) NOT NULL,
    server_type character varying(10),
    search_text character varying(1500),
    title character varying(10),
    imei_number character varying(100),
    techo_phone_number character varying(100),
    aadhar_number_encrypted character varying(24),
    no_of_attempts integer,
    rch_institution_master_id integer,
    infrastructure_id integer,
    deleted_on timestamp without time zone
);


ALTER TABLE IF EXISTS archive.deleted_users OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 220947)
-- Name: deleted_users_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.deleted_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.deleted_users_id_seq OWNER TO postgres;

--
-- TOC entry 6481 (class 0 OID 0)
-- Dependencies: 284
-- Name: deleted_users_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.deleted_users_id_seq OWNED BY archive.deleted_users.id;


--
-- TOC entry 285 (class 1259 OID 220957)
-- Name: family_state_change_issue; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.family_state_change_issue (
    id integer NOT NULL,
    family_id text,
    is_found boolean
);


ALTER TABLE IF EXISTS archive.family_state_change_issue OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 220963)
-- Name: family_state_change_issue_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.family_state_change_issue_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.family_state_change_issue_id_seq OWNER TO postgres;

--
-- TOC entry 6482 (class 0 OID 0)
-- Dependencies: 286
-- Name: family_state_change_issue_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.family_state_change_issue_id_seq OWNED BY archive.family_state_change_issue.id;


--
-- TOC entry 287 (class 1259 OID 220965)
-- Name: health_infrastructure_details_history; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.health_infrastructure_details_history (
    id integer NOT NULL,
    health_infrastructure_details_id integer NOT NULL,
    action character varying(50) NOT NULL,
    name character varying(500),
    location_id integer,
    is_nrc boolean,
    is_cmtc boolean,
    is_fru boolean,
    is_sncu boolean,
    is_chiranjeevi_scheme boolean,
    is_balsaka boolean,
    is_pmjy boolean,
    address character varying(1000),
    state character varying(255),
    type integer,
    latitude character varying(100),
    longitude character varying(100),
    nin character varying(100),
    emamta_id bigint,
    is_blood_bank boolean,
    is_gynaec boolean,
    is_pediatrician boolean,
    postal_code character varying(100),
    landline_number character varying(100),
    mobile_number character varying(100),
    email character varying(256),
    name_in_english character varying(1000),
    is_cpconfirmationcenter boolean,
    for_ncd boolean,
    no_of_beds integer,
    is_balsakha1 boolean,
    is_balsakha3 boolean,
    is_usg_facility boolean,
    is_referral_facility boolean,
    is_ma_yojna boolean,
    is_npcb boolean,
    sub_type integer,
    registration_number text,
    is_hwc boolean,
    is_no_reporting_unit boolean,
    is_idsp boolean,
    is_covid_hospital boolean,
    is_covid_lab boolean,
    no_of_ppe integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    contact_person_name text,
    contact_number text,
    funded_by character varying(50),
    has_ventilators boolean,
    has_defibrillators boolean,
    has_oxygen_cylinders boolean,
    abhay_parimiti_id text
);


ALTER TABLE IF EXISTS archive.health_infrastructure_details_history OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 220971)
-- Name: health_infrastructure_details_history_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.health_infrastructure_details_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.health_infrastructure_details_history_id_seq OWNER TO postgres;

--
-- TOC entry 6483 (class 0 OID 0)
-- Dependencies: 288
-- Name: health_infrastructure_details_history_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.health_infrastructure_details_history_id_seq OWNED BY archive.health_infrastructure_details_history.id;


--
-- TOC entry 289 (class 1259 OID 220973)
-- Name: imt_member_cfhc_backup_06_feb; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.imt_member_cfhc_backup_06_feb (
    id integer,
    member_id integer,
    is_child_going_school boolean,
    current_studying_standard character varying(5),
    current_school_name character varying(256),
    ready_for_more_child boolean,
    family_planning_method character varying(50),
    another_family_planning_method character varying(50),
    is_fever_with_cs_for_da_days boolean,
    is_fever_with_h_ep_mp_sr boolean,
    is_fever_with_h_jp boolean,
    sickle_cell_anemia text,
    is_skin_patches boolean,
    blood_pressure character varying(100),
    is_cough_for_mt_one_week boolean,
    is_fever_at_evening_time boolean,
    is_feeling_any_weakness boolean,
    created_by integer,
    created_on timestamp without time zone,
    modified_on timestamp without time zone,
    modified_by integer,
    family_id integer,
    currently_using_fp_method boolean,
    change_fp_method boolean,
    fp_method character varying(50),
    fp_insert_operate_date date,
    pmjay_number character varying(9)
);


ALTER TABLE IF EXISTS archive.imt_member_cfhc_backup_06_feb OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 220979)
-- Name: location_changer_query_table; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.location_changer_query_table (
    table_name character varying(200),
    query text
);


ALTER TABLE IF EXISTS archive.location_changer_query_table OWNER TO postgres;

--
-- TOC entry 291 (class 1259 OID 220985)
-- Name: mobile_refresh_response; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.mobile_refresh_response (
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS archive.mobile_refresh_response OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 220991)
-- Name: notification_generated_for_asha_location_det; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.notification_generated_for_asha_location_det (
    location_id integer NOT NULL
);


ALTER TABLE IF EXISTS archive.notification_generated_for_asha_location_det OWNER TO postgres;

--
-- TOC entry 293 (class 1259 OID 220994)
-- Name: patient_int_rate; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.patient_int_rate (
    rate bigint
);


ALTER TABLE IF EXISTS archive.patient_int_rate OWNER TO postgres;

--
-- TOC entry 294 (class 1259 OID 220997)
-- Name: rch_anc_master_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_anc_master_archive (
    id integer,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    lmp timestamp without time zone,
    weight numeric(6,2),
    jsy_beneficiary boolean,
    kpsy_beneficiary boolean,
    iay_beneficiary boolean,
    chiranjeevi_yojna_beneficiary boolean,
    anc_place integer,
    systolic_bp integer,
    diastolic_bp integer,
    member_height integer,
    foetal_height integer,
    foetal_heart_sound boolean,
    ifa_tablets_given integer,
    fa_tablets_given integer,
    calcium_tablets_given integer,
    hbsag_test character varying(30),
    blood_sugar_test character varying(30),
    urine_test_done boolean,
    albendazole_given boolean,
    referral_place integer,
    dead_flag boolean,
    other_dangerous_sign character varying(500),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(100),
    edd timestamp without time zone,
    notification_id integer,
    death_date timestamp without time zone,
    vdrl_test character varying(20),
    hiv_test character varying(20),
    place_of_death character varying(20),
    haemoglobin_count numeric(6,2),
    death_reason character varying(50),
    jsy_payment_done boolean,
    last_delivery_outcome character varying(50),
    expected_delivery_place character varying(50),
    family_planning_method character varying(50),
    foetal_position character varying(50),
    dangerous_sign_id character varying(50),
    other_previous_pregnancy_complication character varying(50),
    foetal_movement character varying(15),
    referral_done character varying(15),
    urine_albumin character varying(15),
    urine_sugar character varying(15),
    is_high_risk_case boolean,
    blood_group character varying(3),
    sugar_test_after_food_val integer,
    sugar_test_before_food_val integer,
    pregnancy_reg_det_id integer,
    service_date timestamp without time zone,
    sickle_cell_test text,
    is_from_web boolean,
    delivery_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    delivery_done_by text,
    delivery_person integer,
    delivery_person_name text,
    other_death_reason text,
    anmol_registration_id character varying(255),
    anmol_anc_wsdl_code text,
    anmol_anc_status character varying(255),
    anmol_anc_date timestamp without time zone,
    blood_transfusion boolean,
    iron_def_anemia_inj character varying(255),
    iron_def_anemia_inj_due_date timestamp without time zone,
    death_infra_id integer,
    examined_by_gynecologist boolean,
    is_inj_corticosteroid_given boolean,
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS archive.rch_anc_master_archive OWNER TO postgres;

--
-- TOC entry 295 (class 1259 OID 221003)
-- Name: rch_immunisation_master_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_immunisation_master_archive (
    id integer NOT NULL,
    member_id integer NOT NULL,
    member_type character varying(10),
    visit_type character varying(50),
    visit_id integer,
    notification_id integer,
    immunisation_given character varying(50),
    given_on timestamp without time zone NOT NULL,
    given_by integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    family_id integer,
    location_id integer,
    pregnancy_reg_det_id integer,
    anmol_child_tracking_status character varying(255),
    anmol_child_tracking_wsdl_code text,
    anmol_child_tracking_date timestamp without time zone,
    vitamin_a_no integer,
    anmol_child_tracking_reg_id character varying(255),
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS archive.rch_immunisation_master_archive OWNER TO postgres;

--
-- TOC entry 296 (class 1259 OID 221009)
-- Name: rch_lmp_follow_up_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_lmp_follow_up_archive (
    id integer,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    lmp timestamp without time zone,
    is_pregnant boolean,
    pregnancy_test_done boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    register_now_for_pregnancy boolean,
    notification_id integer,
    family_planning_method character varying(50),
    year smallint,
    fp_insert_operate_date timestamp without time zone,
    place_of_death character varying(20),
    member_status character varying(15),
    death_date timestamp without time zone,
    death_reason character varying(50),
    service_date timestamp without time zone,
    other_death_reason text,
    anmol_registration_id character varying(255),
    anmol_upload_status_code text,
    anmol_follow_up_status character varying(255),
    anmol_follow_up_wsdl_code text,
    anmol_follow_up_date timestamp without time zone,
    phone_number text
);


ALTER TABLE IF EXISTS archive.rch_lmp_follow_up_archive OWNER TO postgres;

--
-- TOC entry 297 (class 1259 OID 221015)
-- Name: rch_member_death_deatil_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_member_death_deatil_archive (
    id integer NOT NULL,
    member_id integer,
    family_id text,
    dod timestamp without time zone,
    created_on timestamp without time zone,
    created_by integer,
    death_reason text,
    place_of_death character varying(20),
    location_id integer,
    other_death_reason text
);


ALTER TABLE IF EXISTS archive.rch_member_death_deatil_archive OWNER TO postgres;

--
-- TOC entry 298 (class 1259 OID 221021)
-- Name: rch_member_service_data_sync_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_member_service_data_sync_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_member_service_data_sync_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 299 (class 1259 OID 221023)
-- Name: rch_member_service_data_sync_detail; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_member_service_data_sync_detail (
    id integer DEFAULT nextval('public.rch_member_service_data_sync_detail_id_seq1'::regclass) NOT NULL,
    member_id integer,
    location_id integer,
    created_on timestamp without time zone,
    original_lmp timestamp without time zone,
    emamta_lmp timestamp without time zone,
    is_pregnant_in_imtecho boolean,
    is_pregnant_in_emamta boolean,
    emamta_dob timestamp without time zone,
    imtecho_dob timestamp without time zone
);


ALTER TABLE IF EXISTS archive.rch_member_service_data_sync_detail OWNER TO postgres;

--
-- TOC entry 300 (class 1259 OID 221027)
-- Name: rch_pnc_master_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_pnc_master_archive (
    id integer,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(15),
    pregnancy_reg_det_id integer,
    pnc_no text,
    is_from_web boolean,
    service_date timestamp without time zone,
    delivery_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    delivery_done_by text,
    delivery_person integer,
    delivery_person_name text,
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS archive.rch_pnc_master_archive OWNER TO postgres;

--
-- TOC entry 301 (class 1259 OID 221033)
-- Name: rch_pregnancy_registration_det_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_pregnancy_registration_det_archive (
    id integer,
    mthr_reg_no text,
    member_id integer,
    lmp_date date,
    edd date,
    reg_date timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    location_id integer,
    family_id integer,
    current_location_id integer,
    delivery_date date,
    is_reg_date_verified boolean
);


ALTER TABLE IF EXISTS archive.rch_pregnancy_registration_det_archive OWNER TO postgres;

--
-- TOC entry 302 (class 1259 OID 221039)
-- Name: rch_wpd_mother_master_archive; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.rch_wpd_mother_master_archive (
    id integer,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    date_of_delivery timestamp without time zone,
    member_status text,
    is_preterm_birth boolean,
    delivery_place text,
    type_of_hospital integer,
    delivery_done_by text,
    mother_alive boolean,
    type_of_delivery text,
    referral_place integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    discharge_date timestamp without time zone,
    breast_feeding_in_one_hour boolean,
    notification_id integer,
    death_date timestamp without time zone,
    death_reason text,
    place_of_death text,
    cortico_steroid_given boolean,
    mtp_done_at integer,
    mtp_performed_by text,
    has_delivery_happened boolean,
    other_danger_signs text,
    is_high_risk_case boolean,
    referral_done text,
    pregnancy_reg_det_id integer,
    pregnancy_outcome text,
    is_discharged boolean,
    misoprostol_given boolean,
    free_drop_delivery text,
    delivery_person integer,
    health_infrastructure_id integer,
    state text,
    delivery_person_name text,
    is_from_web boolean,
    institutional_delivery_place text,
    other_death_reason text,
    free_medicines boolean,
    free_diet boolean,
    free_lab_test boolean,
    free_blood_transfusion boolean,
    free_drop_transport boolean,
    family_planning_method character varying,
    eligible_for_chiranjeevi boolean,
    fbmdsr boolean,
    referral_infra_id integer,
    death_infra_id integer
);


ALTER TABLE IF EXISTS archive.rch_wpd_mother_master_archive OWNER TO postgres;

--
-- TOC entry 303 (class 1259 OID 221045)
-- Name: state_detail; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.state_detail (
    state character varying(255)
);


ALTER TABLE IF EXISTS archive.state_detail OWNER TO postgres;

--
-- TOC entry 304 (class 1259 OID 221048)
-- Name: synced_wpd_mother; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.synced_wpd_mother (
    sync_status_id text NOT NULL
);


ALTER TABLE IF EXISTS archive.synced_wpd_mother OWNER TO postgres;

--
-- TOC entry 305 (class 1259 OID 221054)
-- Name: system_sync_status_dump; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.system_sync_status_dump (
    id text NOT NULL,
    action_date timestamp without time zone,
    relative_id integer,
    status text,
    record_string text,
    mobile_date timestamp without time zone,
    user_id integer,
    device text,
    client_id integer,
    lastmodified_by integer,
    lastmodified_date timestamp without time zone,
    duration_of_processing bigint,
    error_message text,
    exception text,
    mail_sent boolean
);


ALTER TABLE IF EXISTS archive.system_sync_status_dump OWNER TO postgres;

--
-- TOC entry 306 (class 1259 OID 221060)
-- Name: system_user; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.system_user (
    id integer NOT NULL,
    full_name character varying,
    email_id character varying,
    username character varying,
    password character varying,
    authorities character varying,
    activated boolean,
    archived boolean,
    last_updated_on integer,
    last_modified_by integer
);


ALTER TABLE IF EXISTS archive.system_user OWNER TO postgres;

--
-- TOC entry 307 (class 1259 OID 221066)
-- Name: system_user_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.system_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.system_user_id_seq OWNER TO postgres;

--
-- TOC entry 6484 (class 0 OID 0)
-- Dependencies: 307
-- Name: system_user_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.system_user_id_seq OWNED BY archive.system_user.id;


--
-- TOC entry 308 (class 1259 OID 221068)
-- Name: total_patient_interaction; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.total_patient_interaction (
    "?column?" text,
    date_part double precision,
    count bigint
);


ALTER TABLE IF EXISTS archive.total_patient_interaction OWNER TO postgres;

--
-- TOC entry 309 (class 1259 OID 221074)
-- Name: user_data_access_detail_request; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.user_data_access_detail_request (
    id integer NOT NULL,
    user_id integer,
    created_on timestamp without time zone,
    apk_version integer
);


ALTER TABLE IF EXISTS archive.user_data_access_detail_request OWNER TO postgres;

--
-- TOC entry 310 (class 1259 OID 221077)
-- Name: user_data_access_detail_request_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.user_data_access_detail_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.user_data_access_detail_request_id_seq OWNER TO postgres;

--
-- TOC entry 6485 (class 0 OID 0)
-- Dependencies: 310
-- Name: user_data_access_detail_request_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.user_data_access_detail_request_id_seq OWNED BY archive.user_data_access_detail_request.id;


--
-- TOC entry 311 (class 1259 OID 221079)
-- Name: user_location_detail; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.user_location_detail (
    id integer NOT NULL,
    is_active boolean NOT NULL,
    location_type character varying(5) NOT NULL,
    location integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE IF EXISTS archive.user_location_detail OWNER TO postgres;

--
-- TOC entry 312 (class 1259 OID 221082)
-- Name: user_location_detail_id_seq; Type: SEQUENCE; Schema: archive; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS archive.user_location_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS archive.user_location_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6486 (class 0 OID 0)
-- Dependencies: 312
-- Name: user_location_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: archive; Owner: postgres
--

ALTER SEQUENCE archive.user_location_detail_id_seq OWNED BY archive.user_location_detail.id;


--
-- TOC entry 313 (class 1259 OID 221084)
-- Name: usermanagement_system_user; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.usermanagement_system_user (
    id integer DEFAULT nextval(('"usermanagement_system_user_id_seq"'::text)::regclass) NOT NULL,
    user_id character varying(50) NOT NULL,
    password character varying(50),
    type character varying(30),
    created_by character varying(50) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by character varying(50),
    modified_on timestamp without time zone,
    expired_on timestamp without time zone,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    inactive_reason character varying(500),
    contact integer,
    last_login_on timestamp without time zone,
    custom1 integer,
    custom2 character varying(500),
    custom3 timestamp without time zone,
    company integer
);


ALTER TABLE IF EXISTS archive.usermanagement_system_user OWNER TO postgres;

--
-- TOC entry 314 (class 1259 OID 221091)
-- Name: verfied_families_village_wise_records; Type: TABLE; Schema: archive; Owner: postgres
--

CREATE TABLE IF NOT EXISTS archive.verfied_families_village_wise_records (
    loc_id integer,
    created_on date,
    total integer,
    verified integer
);


ALTER TABLE IF EXISTS archive.verfied_families_village_wise_records OWNER TO postgres;

--
-- TOC entry 319 (class 1259 OID 221453)
-- Name: anganwadi_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.anganwadi_master (
    id integer NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    emamta_id character varying(255),
    name character varying(255) NOT NULL,
    parent integer NOT NULL,
    state character varying(255) NOT NULL,
    type character varying(255),
    modified_by integer,
    modified_on timestamp without time zone,
    updated_by character varying(255),
    updated_on timestamp without time zone,
    alias_name character varying(255),
    emamta_parent_id integer,
    emamta_village_id integer,
    emamta_uhc_id integer,
    icds_code character varying(11)
);


ALTER TABLE IF EXISTS public.anganwadi_master OWNER TO postgres;

--
-- TOC entry 320 (class 1259 OID 221459)
-- Name: anganwadi_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.anganwadi_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.anganwadi_master_id_seq OWNER TO postgres;

--
-- TOC entry 6487 (class 0 OID 0)
-- Dependencies: 320
-- Name: anganwadi_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.anganwadi_master_id_seq OWNED BY public.anganwadi_master.id;


--
-- TOC entry 208 (class 1259 OID 220409)
-- Name: anmol_eligible_couples_serial_sequance; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.anmol_eligible_couples_serial_sequance
    START WITH 200000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.anmol_eligible_couples_serial_sequance OWNER TO postgres;

--
-- TOC entry 321 (class 1259 OID 221517)
-- Name: announcement_health_infra_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.announcement_health_infra_detail (
    announcement integer NOT NULL,
    health_infra_id integer NOT NULL,
    has_seen boolean
);


ALTER TABLE IF EXISTS public.announcement_health_infra_detail OWNER TO postgres;

--
-- TOC entry 322 (class 1259 OID 221520)
-- Name: announcement_info_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.announcement_info_detail (
    announcement integer NOT NULL,
    language character varying(50) NOT NULL,
    content bytea,
    file_extension character varying(5),
    media_path character varying(200)
);


ALTER TABLE IF EXISTS public.announcement_info_detail OWNER TO postgres;

--
-- TOC entry 323 (class 1259 OID 221526)
-- Name: announcement_info_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.announcement_info_master (
    id integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    default_language character varying(50),
    from_date timestamp without time zone NOT NULL,
    is_active boolean NOT NULL,
    subject character varying(900),
    to_date timestamp without time zone,
    modified_on timestamp without time zone,
    modified_by integer,
    created_by integer,
    contains_multimedia boolean
);


ALTER TABLE IF EXISTS public.announcement_info_master OWNER TO postgres;

--
-- TOC entry 324 (class 1259 OID 221532)
-- Name: announcement_info_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.announcement_info_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.announcement_info_master_id_seq OWNER TO postgres;

--
-- TOC entry 6488 (class 0 OID 0)
-- Dependencies: 324
-- Name: announcement_info_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.announcement_info_master_id_seq OWNED BY public.announcement_info_master.id;


--
-- TOC entry 325 (class 1259 OID 221534)
-- Name: announcement_location_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.announcement_location_detail (
    announcement integer NOT NULL,
    location integer NOT NULL,
    announcement_for character varying(20) NOT NULL,
    is_active boolean,
    location_type character varying(5) NOT NULL
);


ALTER TABLE IF EXISTS public.announcement_location_detail OWNER TO postgres;

--
-- TOC entry 326 (class 1259 OID 221537)
-- Name: app_version_response; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.app_version_response (
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.app_version_response OWNER TO postgres;

--
-- TOC entry 780 (class 1259 OID 232191)
-- Name: blocked_devices_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.blocked_devices_master (
    imei text NOT NULL,
    created_by bigint,
    created_on timestamp without time zone,
    is_delete_database boolean,
    is_block_device boolean
);


ALTER TABLE IF EXISTS public.blocked_devices_master OWNER TO postgres;

--
-- TOC entry 327 (class 1259 OID 221583)
-- Name: ccc_overdue_services_follow_up_info_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ccc_overdue_services_follow_up_info_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ccc_overdue_services_follow_up_info_id_seq1 OWNER TO postgres;

--
-- TOC entry 328 (class 1259 OID 221592)
-- Name: ccc_overdue_services_follow_up_response_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ccc_overdue_services_follow_up_response_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ccc_overdue_services_follow_up_response_id_seq OWNER TO postgres;

--
-- TOC entry 329 (class 1259 OID 221671)
-- Name: child_cmtc_nrc_admission_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_admission_detail (
    id integer NOT NULL,
    child_id integer,
    referred_by integer,
    medical_officer_visit_flag boolean,
    specialist_pediatrician_visit_flag boolean,
    admission_date timestamp without time zone,
    apetite_test text,
    bilateral_pitting_oedema text,
    type_of_admission text,
    weight_at_admission numeric(6,2),
    height integer,
    mid_upper_arm_circumference numeric(6,2),
    sd_score text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    state text,
    death_date timestamp without time zone,
    death_reason text,
    other_death_reason text,
    death_place text,
    defaulter_date timestamp without time zone,
    other_illness text,
    other_death_place text,
    screening_center integer,
    complementary_feeding boolean,
    breast_feeding boolean,
    is_imported boolean,
    case_id integer,
    problem_in_breast_feeding boolean,
    problem_in_milk_injection boolean,
    visible_wasting boolean,
    kmc_provided boolean,
    no_of_times_kmc_done integer,
    no_of_times_amoxicillin_given integer,
    consecutive_3_days_weight_gain boolean
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_admission_detail OWNER TO postgres;

--
-- TOC entry 330 (class 1259 OID 221677)
-- Name: child_cmtc_nrc_admission_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_admission_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_admission_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6489 (class 0 OID 0)
-- Dependencies: 330
-- Name: child_cmtc_nrc_admission_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_admission_detail_id_seq OWNED BY public.child_cmtc_nrc_admission_detail.id;


--
-- TOC entry 331 (class 1259 OID 221679)
-- Name: child_cmtc_nrc_admission_illness_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_admission_illness_detail (
    admission_id integer NOT NULL,
    illness text NOT NULL
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_admission_illness_detail OWNER TO postgres;

--
-- TOC entry 332 (class 1259 OID 221697)
-- Name: child_cmtc_nrc_discharge_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_discharge_detail (
    id integer NOT NULL,
    child_id integer,
    referred_by integer,
    higher_facility_referral text,
    referral_reason text,
    discharge_date timestamp without time zone,
    bilateral_pitting_oedema text,
    weight numeric(6,2),
    height integer,
    mid_upper_arm_circumference numeric(6,2),
    sd_score text,
    discharge_status text,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    other_illness text,
    higher_facility_referral_place integer,
    admission_id integer,
    is_imported boolean,
    case_id integer,
    kmc_provided boolean,
    no_of_times_kmc_done integer
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_discharge_detail OWNER TO postgres;

--
-- TOC entry 333 (class 1259 OID 221703)
-- Name: child_cmtc_nrc_discharge_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_discharge_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_discharge_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6490 (class 0 OID 0)
-- Dependencies: 333
-- Name: child_cmtc_nrc_discharge_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_discharge_detail_id_seq OWNED BY public.child_cmtc_nrc_discharge_detail.id;


--
-- TOC entry 334 (class 1259 OID 221705)
-- Name: child_cmtc_nrc_discharge_illness_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_discharge_illness_detail (
    discharge_id integer NOT NULL,
    illness text NOT NULL
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_discharge_illness_detail OWNER TO postgres;

--
-- TOC entry 335 (class 1259 OID 221711)
-- Name: child_cmtc_nrc_follow_up; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_follow_up (
    id integer NOT NULL,
    child_id integer,
    referred_by integer,
    follow_up_visit integer,
    follow_up_date timestamp without time zone,
    bilateral_pitting_oedema text,
    weight numeric(6,2),
    height integer,
    mid_upper_arm_circumference numeric(6,2),
    other_illness text,
    sd_score text,
    program_output text,
    discharge_from_program timestamp without time zone,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    other_cmtc_center_follow_up boolean,
    follow_up_other_center integer,
    is_imported boolean,
    admission_id integer,
    case_id integer
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_follow_up OWNER TO postgres;

--
-- TOC entry 336 (class 1259 OID 221717)
-- Name: child_cmtc_nrc_follow_up_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_follow_up_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_follow_up_id_seq OWNER TO postgres;

--
-- TOC entry 6491 (class 0 OID 0)
-- Dependencies: 336
-- Name: child_cmtc_nrc_follow_up_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_follow_up_id_seq OWNED BY public.child_cmtc_nrc_follow_up.id;


--
-- TOC entry 337 (class 1259 OID 221719)
-- Name: child_cmtc_nrc_follow_up_illness_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_follow_up_illness_detail (
    follow_up_id integer NOT NULL,
    illness text NOT NULL
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_follow_up_illness_detail OWNER TO postgres;

--
-- TOC entry 338 (class 1259 OID 221728)
-- Name: child_cmtc_nrc_laboratory_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_laboratory_detail (
    id integer NOT NULL,
    admission_id integer,
    laboratory_date date,
    hemoglobin_checked boolean,
    hemoglobin numeric(6,2),
    ps_for_mp_checked boolean,
    ps_for_mp text,
    ps_for_mp_value text,
    monotoux_test_checked boolean,
    monotoux_test text,
    xray_chest_checked boolean,
    xray_chest numeric(12,2),
    urine_albumin_checked boolean,
    urine_albumin text,
    blood_group text,
    test_output_state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    urine_pus_cells_checked boolean,
    urine_pus_cells numeric(12,2),
    hiv_checked boolean,
    hiv text,
    sickle_checked boolean,
    sickle text,
    is_imported boolean
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_laboratory_detail OWNER TO postgres;

--
-- TOC entry 339 (class 1259 OID 221734)
-- Name: child_cmtc_nrc_laboratory_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_laboratory_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_laboratory_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6492 (class 0 OID 0)
-- Dependencies: 339
-- Name: child_cmtc_nrc_laboratory_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_laboratory_detail_id_seq OWNED BY public.child_cmtc_nrc_laboratory_detail.id;


--
-- TOC entry 340 (class 1259 OID 221736)
-- Name: child_cmtc_nrc_mo_verification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_mo_verification (
    id integer NOT NULL,
    child_id integer,
    weight numeric(6,2),
    height integer,
    mid_upper_arm_circumference integer,
    sd_score text,
    bilateral_pitting_oedema text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_mo_verification OWNER TO postgres;

--
-- TOC entry 341 (class 1259 OID 221742)
-- Name: child_cmtc_nrc_mo_verification_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_mo_verification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_mo_verification_id_seq OWNER TO postgres;

--
-- TOC entry 6493 (class 0 OID 0)
-- Dependencies: 341
-- Name: child_cmtc_nrc_mo_verification_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_mo_verification_id_seq OWNED BY public.child_cmtc_nrc_mo_verification.id;


--
-- TOC entry 342 (class 1259 OID 221744)
-- Name: child_cmtc_nrc_referral_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_referral_detail (
    id integer NOT NULL,
    child_id integer NOT NULL,
    admission_id integer NOT NULL,
    referred_from integer NOT NULL,
    referred_to integer NOT NULL,
    referred_date timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_referral_detail OWNER TO postgres;

--
-- TOC entry 343 (class 1259 OID 221747)
-- Name: child_cmtc_nrc_referral_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_referral_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_referral_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6494 (class 0 OID 0)
-- Dependencies: 343
-- Name: child_cmtc_nrc_referral_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_referral_detail_id_seq OWNED BY public.child_cmtc_nrc_referral_detail.id;


--
-- TOC entry 344 (class 1259 OID 221749)
-- Name: child_cmtc_nrc_screening_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_screening_detail (
    id integer NOT NULL,
    child_id integer,
    screened_on timestamp without time zone,
    location_id integer,
    state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    appetite_test_done character varying(10),
    appetite_test_reported_on timestamp without time zone,
    admission_id integer,
    discharge_id integer,
    is_direct_admission boolean,
    screening_center integer,
    mo_not_verified boolean,
    is_imported boolean,
    is_case_completed boolean,
    referred_from integer,
    referred_to integer,
    referred_date timestamp without time zone,
    is_archive boolean,
    identified_from text,
    reference_id integer
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_screening_detail OWNER TO postgres;

--
-- TOC entry 345 (class 1259 OID 221755)
-- Name: child_cmtc_nrc_screening_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_screening_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_screening_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6495 (class 0 OID 0)
-- Dependencies: 345
-- Name: child_cmtc_nrc_screening_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_cmtc_nrc_screening_detail_id_seq OWNED BY public.child_cmtc_nrc_screening_detail.id;


--
-- TOC entry 346 (class 1259 OID 221757)
-- Name: child_cmtc_nrc_weight_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_cmtc_nrc_weight_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_cmtc_nrc_weight_detail_id_seq OWNER TO postgres;

--
-- TOC entry 347 (class 1259 OID 221759)
-- Name: child_cmtc_nrc_weight_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_cmtc_nrc_weight_detail (
    id integer DEFAULT nextval('public.child_cmtc_nrc_weight_detail_id_seq'::regclass) NOT NULL,
    admission_id integer,
    weight_date date,
    weight numeric(6,2),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    is_mother_councelling boolean,
    is_amoxicillin boolean,
    is_vitamina boolean,
    is_albendazole boolean,
    is_folic_acid boolean,
    is_potassium boolean,
    is_magnesium boolean,
    is_zinc boolean,
    is_iron boolean,
    bilateral_pitting_oedema text,
    formula_given text,
    other_higher_nutrients_given boolean,
    mid_upper_arm_circumference numeric(6,2),
    height integer,
    higher_facility_referral text,
    referral_reason text,
    multi_vitamin_syrup boolean,
    is_sugar_solution boolean,
    night_stay boolean,
    higher_facility_referral_place integer,
    kmc_provided boolean,
    no_of_times_kmc_done integer,
    weight_gain_5_gm_1_day boolean,
    weight_gain_5_gm_2_day boolean,
    weight_gain_5_gm_3_day boolean,
    child_id integer
);


ALTER TABLE IF EXISTS public.child_cmtc_nrc_weight_detail OWNER TO postgres;

--
-- TOC entry 348 (class 1259 OID 221781)
-- Name: child_nutrition_cmam_followup_complication_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_nutrition_cmam_followup_complication_rel (
    cmam_id integer NOT NULL,
    medical_complications text NOT NULL
);


ALTER TABLE IF EXISTS public.child_nutrition_cmam_followup_complication_rel OWNER TO postgres;

--
-- TOC entry 349 (class 1259 OID 221789)
-- Name: child_nutrition_cmam_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_nutrition_cmam_master (
    id integer NOT NULL,
    child_id integer NOT NULL,
    location_id integer NOT NULL,
    state text NOT NULL,
    service_date timestamp without time zone NOT NULL,
    identified_from text NOT NULL,
    reference_id integer NOT NULL,
    cured_on timestamp without time zone,
    cured_muac numeric(6,2),
    cured_visit_id integer,
    is_case_completed boolean,
    created_on timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL
);


ALTER TABLE IF EXISTS public.child_nutrition_cmam_master OWNER TO postgres;

--
-- TOC entry 350 (class 1259 OID 221795)
-- Name: child_nutrition_cmam_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_nutrition_cmam_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_nutrition_cmam_master_id_seq OWNER TO postgres;

--
-- TOC entry 6496 (class 0 OID 0)
-- Dependencies: 350
-- Name: child_nutrition_cmam_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.child_nutrition_cmam_master_id_seq OWNED BY public.child_nutrition_cmam_master.id;


--
-- TOC entry 351 (class 1259 OID 221800)
-- Name: child_nutrition_sam_screening_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_nutrition_sam_screening_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_nutrition_sam_screening_master_id_seq OWNER TO postgres;

--
-- TOC entry 352 (class 1259 OID 221802)
-- Name: child_nutrition_sam_screening_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.child_nutrition_sam_screening_master (
    id integer DEFAULT nextval('public.child_nutrition_sam_screening_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    location_id integer,
    notification_id integer,
    height integer,
    weight numeric(7,3),
    muac numeric(6,2),
    have_pedal_edema boolean,
    sd_score character varying(10),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    appetite_test boolean,
    referral_done boolean,
    role_id integer,
    service_date date,
    breast_feeding_done boolean,
    breast_sucking_problems boolean,
    reference_id integer,
    referral_place integer,
    medical_complications_present boolean,
    other_diseases text,
    done_from character varying(15),
    done_by integer,
    medical_management_provided boolean,
    provided_medications text,
    is_alive boolean,
    member_status text,
    death_date timestamp without time zone,
    place_of_death text,
    death_reason text,
    other_death_reason text,
    death_infra_id integer
);


ALTER TABLE IF EXISTS public.child_nutrition_sam_screening_master OWNER TO postgres;

--
-- TOC entry 353 (class 1259 OID 221821)
-- Name: child_services_given_analytics_t_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_services_given_analytics_t_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_services_given_analytics_t_id_seq1 OWNER TO postgres;

--
-- TOC entry 354 (class 1259 OID 221837)
-- Name: child_with_age_1_to_6_year_analytics_t_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_with_age_1_to_6_year_analytics_t_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_with_age_1_to_6_year_analytics_t_id_seq OWNER TO postgres;

--
-- TOC entry 355 (class 1259 OID 221847)
-- Name: child_yearly_location_wise_analytics_t_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.child_yearly_location_wise_analytics_t_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.child_yearly_location_wise_analytics_t_id_seq OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 220548)
-- Name: cmtc_child_screening_analytics_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.cmtc_child_screening_analytics_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.cmtc_child_screening_analytics_details_id_seq OWNER TO postgres;

--
-- TOC entry 356 (class 1259 OID 221970)
-- Name: covid_lab_test_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.covid_lab_test_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.covid_lab_test_id_seq OWNER TO postgres;

--
-- TOC entry 358 (class 1259 OID 222053)
-- Name: document_index; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.document_index (
    id integer NOT NULL,
    json text NOT NULL,
    type text NOT NULL,
    is_index boolean DEFAULT false,
    created_on timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.document_index OWNER TO postgres;

--
-- TOC entry 359 (class 1259 OID 222060)
-- Name: document_index_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.document_index_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.document_index_id_seq OWNER TO postgres;

--
-- TOC entry 6497 (class 0 OID 0)
-- Dependencies: 359
-- Name: document_index_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.document_index_id_seq OWNED BY public.document_index.id;


--
-- TOC entry 360 (class 1259 OID 222062)
-- Name: document_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.document_master (
    id bigint NOT NULL,
    file_name text NOT NULL,
    file_name_th text,
    extension character varying(255) NOT NULL,
    actual_file_name text NOT NULL,
    is_temporary boolean DEFAULT false,
    module_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.document_master OWNER TO postgres;

--
-- TOC entry 361 (class 1259 OID 222069)
-- Name: document_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.document_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.document_master_id_seq OWNER TO postgres;

--
-- TOC entry 6498 (class 0 OID 0)
-- Dependencies: 361
-- Name: document_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.document_master_id_seq OWNED BY public.document_master.id;


--
-- TOC entry 362 (class 1259 OID 222071)
-- Name: document_module_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.document_module_master (
    id integer NOT NULL,
    module_name text NOT NULL,
    base_path text NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.document_module_master OWNER TO postgres;

--
-- TOC entry 363 (class 1259 OID 222077)
-- Name: document_module_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.document_module_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.document_module_master_id_seq OWNER TO postgres;

--
-- TOC entry 6499 (class 0 OID 0)
-- Dependencies: 363
-- Name: document_module_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.document_module_master_id_seq OWNED BY public.document_module_master.id;


--
-- TOC entry 315 (class 1259 OID 221293)
-- Name: escalation_level_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.escalation_level_master (
    id integer NOT NULL,
    name text,
    notification_type_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    uuid uuid
);


ALTER TABLE IF EXISTS public.escalation_level_master OWNER TO postgres;

--
-- TOC entry 316 (class 1259 OID 221299)
-- Name: escalation_level_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.escalation_level_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.escalation_level_master_id_seq OWNER TO postgres;

--
-- TOC entry 6500 (class 0 OID 0)
-- Dependencies: 316
-- Name: escalation_level_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.escalation_level_master_id_seq OWNED BY public.escalation_level_master.id;


--
-- TOC entry 364 (class 1259 OID 222113)
-- Name: escalation_level_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.escalation_level_role_rel (
    escalation_level_id integer NOT NULL,
    role_id integer NOT NULL,
    can_perform_action boolean DEFAULT false
);


ALTER TABLE IF EXISTS public.escalation_level_role_rel OWNER TO postgres;

--
-- TOC entry 365 (class 1259 OID 222117)
-- Name: escalation_level_user_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.escalation_level_user_rel (
    escalation_level_id integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.escalation_level_user_rel OWNER TO postgres;

--
-- TOC entry 366 (class 1259 OID 222120)
-- Name: event_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.event_configuration (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    day smallint,
    description character varying(1000),
    event_type character varying(100),
    event_type_detail_id integer,
    form_type_id integer,
    hour smallint,
    minute smallint,
    name character varying(500) NOT NULL,
    config_json text,
    state character varying(255),
    trigger_when character varying(50),
    event_type_detail_code character varying(200),
    uuid uuid
);


ALTER TABLE IF EXISTS public.event_configuration OWNER TO postgres;

CREATE SEQUENCE IF NOT EXISTS public.event_configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.event_configuration_id_seq OWNER TO postgres;

--
-- TOC entry 367 (class 1259 OID 222127)
-- Name: event_configuration_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.event_configuration_type (
    id character varying(255) NOT NULL,
    base_date_field_name character varying(100),
    config_id integer,
    day smallint,
    email_subject character varying(500),
    email_subject_parameter character varying(500),
    hour smallint,
    minute smallint,
    mobile_notification_type integer,
    template text,
    template_parameter character varying(500),
    triger_when character varying(255),
    type character varying(100) NOT NULL,
    user_field_name character varying(255),
    family_field_name character varying(255),
    member_field_name character varying(255),
    query_master_id integer,
    query_master_param_json text,
    query_code text,
    sms_config_json text,
    push_notification_config_json text
);


ALTER TABLE IF EXISTS public.event_configuration_type OWNER TO postgres;

--
-- TOC entry 368 (class 1259 OID 222133)
-- Name: event_mobile_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.event_mobile_configuration (
    id character varying(255) NOT NULL,
    notification_code character varying(255),
    notification_type_config_id character varying(255),
    number_of_days_added_for_due_date integer,
    number_of_days_added_for_expiry_date integer,
    number_of_days_added_for_on_date integer
);


ALTER TABLE IF EXISTS public.event_mobile_configuration OWNER TO postgres;

--
-- TOC entry 369 (class 1259 OID 222139)
-- Name: event_mobile_notification_pending_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.event_mobile_notification_pending_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.event_mobile_notification_pending_id_seq OWNER TO postgres;

--
-- TOC entry 370 (class 1259 OID 222141)
-- Name: event_mobile_notification_pending; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.event_mobile_notification_pending (
    id integer DEFAULT nextval('public.event_mobile_notification_pending_id_seq'::regclass) NOT NULL,
    notification_configuration_type_id character varying(100),
    base_date timestamp without time zone,
    user_id integer,
    family_id integer,
    member_id integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    is_completed boolean,
    state text,
    ref_code integer
);


ALTER TABLE IF EXISTS public.event_mobile_notification_pending OWNER TO postgres;

--
-- TOC entry 371 (class 1259 OID 222151)
-- Name: facility_performance_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.facility_performance_master (
    id integer NOT NULL,
    health_infrastrucutre_id integer,
    performance_date date,
    no_of_opd_attended integer,
    no_of_ipd_attended integer,
    no_of_deliveres_conducted integer,
    no_of_csection_conducted integer,
    no_of_major_operation_conducted integer,
    no_of_minor_operation_conducted integer,
    no_of_laboratory_test_conducted integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.facility_performance_master OWNER TO postgres;

--
-- TOC entry 372 (class 1259 OID 222154)
-- Name: facility_performance_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.facility_performance_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.facility_performance_master_id_seq OWNER TO postgres;

--
-- TOC entry 6501 (class 0 OID 0)
-- Dependencies: 372
-- Name: facility_performance_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.facility_performance_master_id_seq OWNED BY public.facility_performance_master.id;


--
-- TOC entry 373 (class 1259 OID 222164)
-- Name: family_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.family_id_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.family_id_seq OWNER TO postgres;

--
-- TOC entry 374 (class 1259 OID 222166)
-- Name: feature_display_name; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.feature_display_name (
    menu_id integer NOT NULL,
    feature_name text NOT NULL,
    display_name text
);


ALTER TABLE IF EXISTS public.feature_display_name OWNER TO postgres;

--
-- TOC entry 375 (class 1259 OID 222172)
-- Name: field_constant_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.field_constant_master (
    id integer NOT NULL,
    field_name character varying(255),
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.field_constant_master OWNER TO postgres;

--
-- TOC entry 376 (class 1259 OID 222175)
-- Name: field_constant_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.field_constant_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.field_constant_master_id_seq OWNER TO postgres;

--
-- TOC entry 6502 (class 0 OID 0)
-- Dependencies: 376
-- Name: field_constant_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.field_constant_master_id_seq OWNED BY public.field_constant_master.id;


--
-- TOC entry 377 (class 1259 OID 222177)
-- Name: field_value_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.field_value_master (
    id integer NOT NULL,
    field_id integer,
    field_value character varying(255),
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.field_value_master OWNER TO postgres;

--
-- TOC entry 378 (class 1259 OID 222180)
-- Name: field_value_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.field_value_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.field_value_master_id_seq OWNER TO postgres;

--
-- TOC entry 6503 (class 0 OID 0)
-- Dependencies: 378
-- Name: field_value_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.field_value_master_id_seq OWNED BY public.field_value_master.id;


--
-- TOC entry 379 (class 1259 OID 222182)
-- Name: firebase_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.firebase_token (
    id bigint NOT NULL,
    user_id integer,
    token text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.firebase_token OWNER TO postgres;

--
-- TOC entry 380 (class 1259 OID 222188)
-- Name: firebase_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.firebase_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.firebase_token_id_seq OWNER TO postgres;

--
-- TOC entry 6504 (class 0 OID 0)
-- Dependencies: 380
-- Name: firebase_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.firebase_token_id_seq OWNED BY public.firebase_token.id;


--
-- TOC entry 381 (class 1259 OID 222190)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE IF EXISTS public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 382 (class 1259 OID 222197)
-- Name: forgot_password_otp; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.forgot_password_otp (
    user_id integer NOT NULL,
    forgot_password_otp character varying(4) NOT NULL,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.forgot_password_otp OWNER TO postgres;

--
-- TOC entry 383 (class 1259 OID 222200)
-- Name: form_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.form_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    code character varying(200) NOT NULL,
    name character varying(255) NOT NULL,
    state character varying(255)
);


ALTER TABLE IF EXISTS public.form_master OWNER TO postgres;

--
-- TOC entry 384 (class 1259 OID 222206)
-- Name: form_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.form_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.form_master_id_seq OWNER TO postgres;

--
-- TOC entry 6505 (class 0 OID 0)
-- Dependencies: 384
-- Name: form_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.form_master_id_seq OWNED BY public.form_master.id;


--
-- TOC entry 385 (class 1259 OID 222240)
-- Name: gvk_anm_verification_response_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_anm_verification_response_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_anm_verification_response_id_seq OWNER TO postgres;

--
-- TOC entry 386 (class 1259 OID 222286)
-- Name: gvk_emri_pregnant_member_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_emri_pregnant_member_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_emri_pregnant_member_info_id_seq OWNER TO postgres;

--
-- TOC entry 387 (class 1259 OID 222293)
-- Name: gvk_emri_pregnant_member_mobile_number_verification_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_emri_pregnant_member_mobile_number_verification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_emri_pregnant_member_mobile_number_verification_id_seq OWNER TO postgres;

--
-- TOC entry 388 (class 1259 OID 222311)
-- Name: gvk_emri_pregnant_member_responce_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_emri_pregnant_member_responce_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_emri_pregnant_member_responce_id_seq OWNER TO postgres;

--
-- TOC entry 389 (class 1259 OID 222339)
-- Name: gvk_high_risk_follow_up_responce_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_high_risk_follow_up_responce_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_high_risk_follow_up_responce_id_seq OWNER TO postgres;

--
-- TOC entry 390 (class 1259 OID 222348)
-- Name: gvk_high_risk_follow_up_usr_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_high_risk_follow_up_usr_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_high_risk_follow_up_usr_info_id_seq OWNER TO postgres;

--
-- TOC entry 391 (class 1259 OID 222364)
-- Name: gvk_immunisation_verification_response_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_immunisation_verification_response_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_immunisation_verification_response_id_seq OWNER TO postgres;

--
-- TOC entry 392 (class 1259 OID 222373)
-- Name: gvk_manage_call_master_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.gvk_manage_call_master_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.gvk_manage_call_master_id_seq1 OWNER TO postgres;

--
-- TOC entry 393 (class 1259 OID 222411)
-- Name: health_infrastructure_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_details (
    id integer NOT NULL,
    name character varying(500),
    location_id integer,
    is_nrc boolean,
    is_cmtc boolean,
    is_fru boolean,
    is_sncu boolean,
    is_chiranjeevi_scheme boolean,
    is_balsaka boolean,
    is_pmjy boolean,
    address character varying(1000),
    state character varying(255),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    type integer,
    latitude character varying(100),
    longitude character varying(100),
    nin character varying(100),
    emamta_id bigint,
    is_blood_bank boolean,
    is_gynaec boolean,
    is_pediatrician boolean,
    postal_code character varying(100),
    landline_number character varying(100),
    mobile_number character varying(100),
    email character varying(256),
    name_in_english character varying(1000),
    is_cpconfirmationcenter boolean,
    for_ncd boolean,
    no_of_beds integer,
    is_balsakha1 boolean,
    is_balsakha3 boolean,
    is_usg_facility boolean,
    is_referral_facility boolean,
    is_ma_yojna boolean,
    is_npcb boolean,
    sub_type integer,
    registration_number text,
    is_hwc boolean,
    is_no_reporting_unit boolean,
    is_idsp boolean,
    is_covid_hospital boolean,
    is_covid_lab boolean,
    no_of_ppe integer,
    contact_person_name text,
    contact_number text,
    funded_by character varying(50),
    has_ventilators boolean,
    has_defibrillators boolean,
    has_oxygen_cylinders boolean,
    abhay_parimiti_id text,
    hfr_facility_id character varying(200),
    other_details text,
    speciality_type character varying(50),
    type_of_services character varying(100),
    ownership_code character varying(100),
    ownership_sub_type_code character varying(100),
    ownership_sub_type_code2 character varying(100),
    system_of_medicine_code character varying(200),
    facility_type_code character varying(200),
    facility_sub_type_code character varying(200),
    facility_region character varying(200),
    is_link_to_bridge_id boolean DEFAULT false
);


ALTER TABLE IF EXISTS public.health_infrastructure_details OWNER TO postgres;

--
-- TOC entry 394 (class 1259 OID 222418)
-- Name: health_infrastructure_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_details_id_seq OWNER TO postgres;

--
-- TOC entry 6506 (class 0 OID 0)
-- Dependencies: 394
-- Name: health_infrastructure_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_details_id_seq OWNED BY public.health_infrastructure_details.id;


--
-- TOC entry 395 (class 1259 OID 222420)
-- Name: health_infrastructure_lab_test_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_lab_test_mapping (
    id integer NOT NULL,
    health_infra_id integer,
    ref_id integer,
    health_infra_type integer,
    permission_type character varying(50)
);


ALTER TABLE IF EXISTS public.health_infrastructure_lab_test_mapping OWNER TO postgres;

--
-- TOC entry 396 (class 1259 OID 222423)
-- Name: health_infrastructure_lab_test_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_lab_test_mapping_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_lab_test_mapping_id_seq OWNER TO postgres;

--
-- TOC entry 6507 (class 0 OID 0)
-- Dependencies: 396
-- Name: health_infrastructure_lab_test_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_lab_test_mapping_id_seq OWNED BY public.health_infrastructure_lab_test_mapping.id;


--
-- TOC entry 397 (class 1259 OID 222425)
-- Name: health_infrastructure_monthly_volunteers_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_monthly_volunteers_details (
    id integer NOT NULL,
    health_infrastructure_id integer NOT NULL,
    no_of_volunteers integer NOT NULL,
    month_year date NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.health_infrastructure_monthly_volunteers_details OWNER TO postgres;

--
-- TOC entry 398 (class 1259 OID 222428)
-- Name: health_infrastructure_monthly_volunteers_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_monthly_volunteers_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_monthly_volunteers_details_id_seq OWNER TO postgres;

--
-- TOC entry 6508 (class 0 OID 0)
-- Dependencies: 398
-- Name: health_infrastructure_monthly_volunteers_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_monthly_volunteers_details_id_seq OWNED BY public.health_infrastructure_monthly_volunteers_details.id;


--
-- TOC entry 399 (class 1259 OID 222430)
-- Name: health_infrastructure_type_allowed_facilities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_type_allowed_facilities (
    id integer NOT NULL,
    health_infra_type_id integer,
    allowed_facilities text
);


ALTER TABLE IF EXISTS public.health_infrastructure_type_allowed_facilities OWNER TO postgres;

--
-- TOC entry 400 (class 1259 OID 222436)
-- Name: health_infrastructure_type_allowed_facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_type_allowed_facilities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_type_allowed_facilities_id_seq OWNER TO postgres;

--
-- TOC entry 6509 (class 0 OID 0)
-- Dependencies: 400
-- Name: health_infrastructure_type_allowed_facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_type_allowed_facilities_id_seq OWNED BY public.health_infrastructure_type_allowed_facilities.id;


--
-- TOC entry 401 (class 1259 OID 222438)
-- Name: health_infrastructure_type_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_type_location (
    id integer NOT NULL,
    health_infra_type_id integer,
    location_level integer,
    location_type character varying(10)
);


ALTER TABLE IF EXISTS public.health_infrastructure_type_location OWNER TO postgres;

--
-- TOC entry 6510 (class 0 OID 0)
-- Dependencies: 401
-- Name: COLUMN health_infrastructure_type_location.location_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.health_infrastructure_type_location.location_type IS 'Health Infra Location Type';


--
-- TOC entry 402 (class 1259 OID 222441)
-- Name: health_infrastructure_type_location_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_type_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_type_location_id_seq OWNER TO postgres;

--
-- TOC entry 6511 (class 0 OID 0)
-- Dependencies: 402
-- Name: health_infrastructure_type_location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_type_location_id_seq OWNED BY public.health_infrastructure_type_location.id;


--
-- TOC entry 403 (class 1259 OID 222443)
-- Name: health_infrastructure_ward_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_ward_details (
    id integer NOT NULL,
    health_infra_id integer,
    ward_name character varying(255),
    ward_type integer,
    number_of_beds integer,
    number_of_ventilators_type1 integer,
    number_of_vantilators_type2 integer,
    number_of_o2 integer,
    status character varying(50),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.health_infrastructure_ward_details OWNER TO postgres;

--
-- TOC entry 404 (class 1259 OID 222446)
-- Name: health_infrastructure_ward_details_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.health_infrastructure_ward_details_history (
    id integer NOT NULL,
    health_infrastructure_ward_details_id integer,
    action character varying(50),
    ward_name character varying(255),
    ward_type integer,
    number_of_beds integer,
    number_of_ventilators_type1 integer,
    number_of_vantilators_type2 integer,
    number_of_o2 integer,
    status character varying(50),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.health_infrastructure_ward_details_history OWNER TO postgres;

--
-- TOC entry 405 (class 1259 OID 222449)
-- Name: health_infrastructure_ward_details_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_ward_details_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_ward_details_history_id_seq OWNER TO postgres;

--
-- TOC entry 6512 (class 0 OID 0)
-- Dependencies: 405
-- Name: health_infrastructure_ward_details_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_ward_details_history_id_seq OWNED BY public.health_infrastructure_ward_details_history.id;


--
-- TOC entry 406 (class 1259 OID 222451)
-- Name: health_infrastructure_ward_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.health_infrastructure_ward_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.health_infrastructure_ward_details_id_seq OWNER TO postgres;

--
-- TOC entry 6513 (class 0 OID 0)
-- Dependencies: 406
-- Name: health_infrastructure_ward_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.health_infrastructure_ward_details_id_seq OWNED BY public.health_infrastructure_ward_details.id;


--
-- TOC entry 407 (class 1259 OID 222456)
-- Name: idsp_2_family_screening_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.idsp_2_family_screening_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.idsp_2_family_screening_details_id_seq OWNER TO postgres;

--
-- TOC entry 408 (class 1259 OID 222465)
-- Name: idsp_2_member_screening_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.idsp_2_member_screening_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.idsp_2_member_screening_details_id_seq OWNER TO postgres;

--
-- TOC entry 409 (class 1259 OID 222474)
-- Name: idsp_family_screening_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.idsp_family_screening_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.idsp_family_screening_details_id_seq OWNER TO postgres;

--
-- TOC entry 410 (class 1259 OID 222491)
-- Name: idsp_member_screening_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.idsp_member_screening_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.idsp_member_screening_details_id_seq OWNER TO postgres;

--
-- TOC entry 411 (class 1259 OID 222503)
-- Name: imt_family_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_id_seq1 OWNER TO postgres;

--
-- TOC entry 412 (class 1259 OID 222505)
-- Name: imt_family; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family (
    address1 character varying(255),
    address2 character varying(255),
    anganwadi_id integer,
    area_id integer,
    assigned_to integer,
    bpl_flag boolean,
    caste character varying(255),
    created_on timestamp without time zone,
    family_id character varying(255),
    house_number character varying(255),
    is_verified_flag boolean,
    latitude character varying(255),
    longitude character varying(255),
    maa_vatsalya_number character varying(255),
    migratory_flag boolean,
    religion character varying(255),
    state character varying(255),
    toilet_available_flag boolean,
    type character varying(255),
    vulnerable_flag boolean,
    rsby_card_number character varying(255),
    comment character varying(4000),
    is_report boolean,
    verified_by_fhsr character varying(255),
    oldid character varying(256),
    id integer DEFAULT nextval('public.imt_family_id_seq1'::regclass) NOT NULL,
    created_by integer,
    modified_by integer,
    modified_on timestamp without time zone,
    old_current_state character varying(256),
    current_state integer,
    location_id integer NOT NULL,
    old_contact_person_id character varying(256),
    contact_person_id integer,
    updated_by character varying(255),
    updated_on timestamp without time zone,
    merged_with character varying(50),
    emamta_location_id integer,
    emamta_location_parent_id integer,
    basic_state text,
    hof_id integer,
    anganwadi_update_flag boolean,
    any_member_cbac_done boolean,
    type_of_house text,
    type_of_toilet text,
    electricity_availability text,
    drinking_water_source text,
    fuel_for_cooking text,
    house_ownership_status text,
    ration_card_number text,
    annual_income text,
    remarks text,
    split_from integer,
    additional_info text,
    pmjay_card_number character varying(9),
    bpl_card_number character varying(25),
    color_of_ration_card text,
    available_cooking_gas boolean,
    available_electric_connection boolean,
    have_pmjay_card_number boolean,
    is_providing_consent boolean,
    vulnerability_criteria text,
    eligible_for_pmjay boolean,
    other_type_of_house character varying(256),
    anyone_travelled_foreign boolean,
    ration_card_color text,
    residence_status text,
    native_state text,
    other_motorized_vehicle text,
    other_toilet text,
    other_water_source text,
    vehicle_availability_flag boolean,
    ration_card_type text,
    pmjay_or_health_insurance text
);


ALTER TABLE IF EXISTS public.imt_family OWNER TO postgres;

--
-- TOC entry 413 (class 1259 OID 222512)
-- Name: imt_family_cfhc_done_by_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_cfhc_done_by_details (
    family_id character varying(255) NOT NULL,
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.imt_family_cfhc_done_by_details OWNER TO postgres;

--
-- TOC entry 786 (class 1259 OID 232221)
-- Name: imt_family_location_change_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_location_change_detail (
    id bigint NOT NULL,
    family_id text,
    from_location_id integer,
    to_location_id integer,
    created_on timestamp without time zone,
    created_by bigint
);


ALTER TABLE IF EXISTS public.imt_family_location_change_detail OWNER TO postgres;

--
-- TOC entry 414 (class 1259 OID 222515)
-- Name: imt_family_location_change_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_location_change_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_location_change_detail_id_seq OWNER TO postgres;

--
-- TOC entry 785 (class 1259 OID 232219)
-- Name: imt_family_location_change_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_location_change_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_location_change_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 6514 (class 0 OID 0)
-- Dependencies: 785
-- Name: imt_family_location_change_detail_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.imt_family_location_change_detail_id_seq1 OWNED BY public.imt_family_location_change_detail.id;


--
-- TOC entry 415 (class 1259 OID 222524)
-- Name: imt_family_migration_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_migration_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_migration_master_id_seq OWNER TO postgres;

--
-- TOC entry 416 (class 1259 OID 222526)
-- Name: imt_family_migration_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_migration_master (
    id integer DEFAULT nextval('public.imt_family_migration_master_id_seq'::regclass) NOT NULL,
    family_id integer,
    is_split_family boolean,
    split_family_id integer,
    is_current_location boolean,
    member_ids text,
    state text,
    type text,
    out_of_state boolean,
    migrated_location_not_known boolean,
    location_migrated_to integer,
    location_migrated_from integer,
    area_migrated_to integer,
    area_migrated_from integer,
    nearest_loc_id integer,
    village_name text,
    fhw_asha_name text,
    fhw_asha_phone text,
    other_information text,
    reported_on timestamp without time zone,
    reported_by integer,
    reported_location_id integer,
    confirmed_on timestamp without time zone,
    confirmed_by integer,
    mobile_data text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.imt_family_migration_master OWNER TO postgres;

--
-- TOC entry 417 (class 1259 OID 222533)
-- Name: imt_family_state_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_state_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_state_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 418 (class 1259 OID 222535)
-- Name: imt_family_state_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_state_detail (
    comment character varying(4000),
    family_id character varying(255),
    from_state character varying(255),
    to_state character varying(255),
    created_on timestamp without time zone,
    oldid character varying(256),
    id integer DEFAULT nextval('public.imt_family_state_detail_id_seq1'::regclass) NOT NULL,
    old_created_by character varying(256),
    created_by integer,
    modified_by integer,
    modified_on timestamp without time zone,
    old_parent character varying(256),
    parent integer
);


ALTER TABLE IF EXISTS public.imt_family_state_detail OWNER TO postgres;

--
-- TOC entry 419 (class 1259 OID 222542)
-- Name: imt_family_vehicle_detail_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_vehicle_detail_rel (
    family_id integer NOT NULL,
    vehicle_details text NOT NULL
);


ALTER TABLE IF EXISTS public.imt_family_vehicle_detail_rel OWNER TO postgres;

--
-- TOC entry 420 (class 1259 OID 222548)
-- Name: imt_family_verification_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_family_verification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_family_verification_id_seq OWNER TO postgres;

--
-- TOC entry 421 (class 1259 OID 222550)
-- Name: imt_family_verification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_family_verification (
    id integer DEFAULT nextval('public.imt_family_verification_id_seq'::regclass) NOT NULL,
    family_id character varying(255),
    location_id integer,
    schedule_date timestamp without time zone,
    call_attempt integer DEFAULT 0,
    created_by integer,
    created_on timestamp without time zone,
    gvk_state character varying(255),
    state character varying(255),
    type character varying(255),
    modified_by integer,
    modified_on timestamp without time zone,
    updated_by character varying(255),
    updated_on timestamp without time zone,
    head character varying(255),
    mobile_number character varying(255),
    survey_type character varying(55),
    verification_body character varying(250),
    verified_on timestamp without time zone,
    verification_state character varying(255)
);


ALTER TABLE IF EXISTS public.imt_family_verification OWNER TO postgres;

--
-- TOC entry 357 (class 1259 OID 221984)
-- Name: imt_member_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_member_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_member_id_seq1 OWNER TO postgres;

--
-- TOC entry 422 (class 1259 OID 222558)
-- Name: imt_member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member (
    id integer DEFAULT nextval('public.imt_member_id_seq1'::regclass) NOT NULL,
    unique_health_id text,
    family_id text,
    first_name text,
    middle_name text,
    last_name text,
    grandfather_name text,
    dob date,
    emamta_health_id text,
    family_head boolean,
    is_aadhar_verified boolean,
    is_mobile_verified boolean,
    is_native boolean,
    is_pregnant boolean,
    lmp date,
    family_planning_method text,
    gender text,
    account_number text,
    ifsc text,
    marital_status integer,
    mobile_number text,
    normal_cycle_days smallint,
    state text,
    education_status integer,
    is_report boolean,
    name_as_per_aadhar text,
    current_state integer,
    merged_from_family_id character varying(50),
    agreed_to_share_aadhar boolean,
    aadhar_number_available boolean,
    aadhar_number_encrypted character varying(32),
    death_detail_id integer,
    jsy_payment_given boolean,
    early_registration boolean,
    jsy_beneficiary boolean,
    haemoglobin numeric(6,2),
    weight numeric(6,2),
    edd timestamp without time zone,
    anc_visit_dates text,
    immunisation_given text,
    place_of_birth text,
    birth_weight numeric(6,2),
    complementary_feeding_started boolean,
    mother_id integer,
    year_of_wedding smallint,
    last_method_of_contraception character varying(50),
    is_high_risk_case boolean,
    cur_preg_reg_det_id integer,
    blood_group character varying(3),
    fp_insert_operate_date timestamp without time zone,
    menopause_arrived boolean,
    kpsy_beneficiary boolean,
    iay_beneficiary boolean,
    chiranjeevi_yojna_beneficiary boolean,
    sync_status text,
    is_iucd_removed boolean,
    iucd_removal_date timestamp without time zone,
    cur_preg_reg_date timestamp without time zone,
    basic_state text,
    eye_issue text,
    current_disease text,
    chronic_disease text,
    congenital_anomaly text,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    last_delivery_date timestamp without time zone,
    hysterectomy_done boolean,
    cbac_done boolean,
    ncd_screening_needed boolean,
    hypertension_screening_done boolean,
    oral_screening_done boolean,
    diabetes_screening_done boolean,
    breast_screening_done boolean,
    cervical_screening_done boolean,
    child_nrc_cmtc_status text,
    last_delivery_outcome text,
    remarks text,
    additional_info text,
    suspected_cp boolean,
    npcb_screening_date date,
    fhsr_phone_verified boolean,
    iucd_removal_reason character varying(255),
    husband_name text,
    current_gravida smallint,
    current_para smallint,
    eligible_couple_date timestamp without time zone,
    date_of_wedding date,
    husband_id integer,
    family_planning_health_infra integer,
    relation_with_hof character varying(25),
    previous_pregnancy_complication text,
    religion character varying(255),
    caste character varying(255),
    pmjay_card_number character varying(255),
    member_uuid character varying(255),
    vulnerable_flag boolean,
    aadhaar_reference_key text,
    health_id character varying(250),
    health_id_number character varying(250),
    health_insurance boolean,
    scheme_detail character varying(256),
    is_personal_history_done boolean,
    pmjay_availability character varying(30),
    alcohol_addiction character varying(30),
    smoking_addiction character varying(30),
    tobacco_addiction character varying(30),
    rch_id text,
    hospitalized_earlier boolean,
    alternate_number text,
    physical_disability text,
    cataract_surgery text,
    sickle_cell_status text,
    pension_scheme text
);


ALTER TABLE IF EXISTS public.imt_member OWNER TO postgres;

--
-- TOC entry 423 (class 1259 OID 222565)
-- Name: imt_member_cfhc_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_member_cfhc_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_member_cfhc_master_id_seq OWNER TO postgres;

--
-- TOC entry 424 (class 1259 OID 222567)
-- Name: imt_member_cfhc_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_cfhc_master (
    id integer DEFAULT nextval('public.imt_member_cfhc_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    is_child_going_school boolean,
    current_studying_standard character varying(5),
    ready_for_more_child boolean,
    family_planning_method character varying(50),
    another_family_planning_method character varying(50),
    is_fever_with_cs_for_da_days boolean,
    is_fever_with_h_ep_mp_sr boolean,
    is_fever_with_h_jp boolean,
    sickle_cell_anemia text,
    is_skin_patches boolean,
    blood_pressure character varying(100),
    is_cough_for_mt_one_week boolean,
    is_fever_at_evening_time boolean,
    is_feeling_any_weakness boolean,
    created_by integer,
    created_on timestamp without time zone NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    modified_by integer,
    family_id integer,
    currently_using_fp_method boolean,
    change_fp_method boolean,
    fp_method character varying(50),
    fp_insert_operate_date date,
    pmjay_number character varying(9),
    current_school integer,
    type_of_school text
);


ALTER TABLE IF EXISTS public.imt_member_cfhc_master OWNER TO postgres;

--
-- TOC entry 425 (class 1259 OID 222574)
-- Name: imt_member_chronic_disease_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_chronic_disease_rel (
    member_id integer NOT NULL,
    chronic_disease_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_chronic_disease_rel OWNER TO postgres;

--
-- TOC entry 426 (class 1259 OID 222577)
-- Name: imt_member_congenital_anomaly_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_congenital_anomaly_rel (
    member_id integer NOT NULL,
    congenital_anomaly_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_congenital_anomaly_rel OWNER TO postgres;

--
-- TOC entry 427 (class 1259 OID 222580)
-- Name: imt_member_current_disease_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_current_disease_rel (
    member_id integer NOT NULL,
    current_disease_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_current_disease_rel OWNER TO postgres;

--
-- TOC entry 428 (class 1259 OID 222594)
-- Name: imt_member_eye_issue_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_eye_issue_rel (
    member_id integer NOT NULL,
    eye_issue_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_eye_issue_rel OWNER TO postgres;

--
-- TOC entry 429 (class 1259 OID 222603)
-- Name: imt_member_health_issue_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_health_issue_rel (
    member_id integer NOT NULL,
    health_issue_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_health_issue_rel OWNER TO postgres;

--
-- TOC entry 430 (class 1259 OID 222606)
-- Name: imt_member_previous_pregnancy_complication_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_previous_pregnancy_complication_rel (
    member_id integer NOT NULL,
    previous_pregnancy_complication text NOT NULL
);


ALTER TABLE IF EXISTS public.imt_member_previous_pregnancy_complication_rel OWNER TO postgres;

--
-- TOC entry 431 (class 1259 OID 222612)
-- Name: imt_member_sickle_cell_anemia_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_sickle_cell_anemia_rel (
    sickle_cell_anemia_id integer,
    cfhc_id integer
);


ALTER TABLE IF EXISTS public.imt_member_sickle_cell_anemia_rel OWNER TO postgres;

--
-- TOC entry 432 (class 1259 OID 222615)
-- Name: imt_member_state_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.imt_member_state_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.imt_member_state_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 433 (class 1259 OID 222617)
-- Name: imt_member_state_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.imt_member_state_detail (
    comment character varying(255),
    from_state character varying(255),
    to_state character varying(255),
    created_on timestamp without time zone,
    oldid character varying(256),
    id integer DEFAULT nextval('public.imt_member_state_detail_id_seq1'::regclass) NOT NULL,
    old_parent character varying(256),
    parent integer,
    old_created_by character varying(256),
    created_by integer,
    modified_by integer,
    modified_on timestamp without time zone,
    old_member_id character varying(256),
    member_id integer
);


ALTER TABLE IF EXISTS public.imt_member_state_detail OWNER TO postgres;

--
-- TOC entry 434 (class 1259 OID 222624)
-- Name: internationalization_label_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.internationalization_label_master (
    country character varying(255) NOT NULL,
    key character varying(500) NOT NULL,
    language character varying(255) NOT NULL,
    created_by character varying(255) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom3b boolean,
    text character varying(500) NOT NULL,
    translation_pending boolean,
    modified_on timestamp without time zone,
    app_name character varying(50) NOT NULL
);


ALTER TABLE IF EXISTS public.internationalization_label_master OWNER TO postgres;

--
-- TOC entry 435 (class 1259 OID 222636)
-- Name: internationalization_language_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.internationalization_language_master (
    code character varying(50) NOT NULL,
    country character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    character_encoding character varying(100),
    is_left_to_right boolean NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    inactive_reason character varying(500),
    created_by character varying(50) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by character varying(50),
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.internationalization_language_master OWNER TO postgres;

--
-- TOC entry 6515 (class 0 OID 0)
-- Dependencies: 435
-- Name: TABLE internationalization_language_master; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.internationalization_language_master IS 'This table is used to store language detail specific to country.';


--
-- TOC entry 436 (class 1259 OID 222648)
-- Name: listvalue_field_form_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.listvalue_field_form_relation (
    field text,
    form_id integer
);


ALTER TABLE IF EXISTS public.listvalue_field_form_relation OWNER TO postgres;

--
-- TOC entry 437 (class 1259 OID 222654)
-- Name: listvalue_field_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.listvalue_field_master (
    field_key character varying(50) NOT NULL,
    field character varying(50) NOT NULL,
    is_active boolean NOT NULL,
    field_type character varying(10),
    form character varying(50),
    role_type character varying(6)
);


ALTER TABLE IF EXISTS public.listvalue_field_master OWNER TO postgres;

--
-- TOC entry 438 (class 1259 OID 222657)
-- Name: listvalue_field_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.listvalue_field_role (
    role_id integer,
    id integer NOT NULL,
    field_key character varying
);


ALTER TABLE IF EXISTS public.listvalue_field_role OWNER TO postgres;

--
-- TOC entry 439 (class 1259 OID 222663)
-- Name: listvalue_field_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.listvalue_field_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.listvalue_field_role_id_seq OWNER TO postgres;

--
-- TOC entry 6516 (class 0 OID 0)
-- Dependencies: 439
-- Name: listvalue_field_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.listvalue_field_role_id_seq OWNED BY public.listvalue_field_role.id;


--
-- TOC entry 440 (class 1259 OID 222665)
-- Name: listvalue_field_value_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.listvalue_field_value_detail (
    id integer NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by character varying(50) NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    value text NOT NULL,
    field_key character varying(50) NOT NULL,
    file_size integer NOT NULL,
    multimedia_type character varying(250),
    code text
);


ALTER TABLE IF EXISTS public.listvalue_field_value_detail OWNER TO postgres;

--
-- TOC entry 441 (class 1259 OID 222671)
-- Name: listvalue_field_value_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.listvalue_field_value_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.listvalue_field_value_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6517 (class 0 OID 0)
-- Dependencies: 441
-- Name: listvalue_field_value_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.listvalue_field_value_detail_id_seq OWNED BY public.listvalue_field_value_detail.id;


--
-- TOC entry 442 (class 1259 OID 222673)
-- Name: listvalue_form_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.listvalue_form_master (
    form_key character varying(100) NOT NULL,
    form character varying(100) NOT NULL,
    is_active boolean NOT NULL,
    is_training_req boolean DEFAULT false,
    query_for_training_completed character varying(255)
);


ALTER TABLE IF EXISTS public.listvalue_form_master OWNER TO postgres;

--
-- TOC entry 443 (class 1259 OID 222700)
-- Name: location_cluster_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_cluster_master (
    id integer NOT NULL,
    name text,
    state text,
    population integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.location_cluster_master OWNER TO postgres;

--
-- TOC entry 444 (class 1259 OID 222706)
-- Name: location_cluster_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_cluster_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_cluster_master_id_seq OWNER TO postgres;

--
-- TOC entry 6518 (class 0 OID 0)
-- Dependencies: 444
-- Name: location_cluster_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_cluster_master_id_seq OWNED BY public.location_cluster_master.id;


--
-- TOC entry 445 (class 1259 OID 222708)
-- Name: location_geo_coordinates_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_geo_coordinates_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_geo_coordinates_gid_seq OWNER TO postgres;

--
-- TOC entry 446 (class 1259 OID 222710)
-- Name: location_hierarchy_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_hierarchy_type (
    location_id integer NOT NULL,
    hierarchy_type character varying(2) NOT NULL
);


ALTER TABLE IF EXISTS public.location_hierarchy_type OWNER TO postgres;

--
-- TOC entry 447 (class 1259 OID 222713)
-- Name: location_hierchy_closer_det_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_hierchy_closer_det_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_hierchy_closer_det_id_seq OWNER TO postgres;

--
-- TOC entry 6519 (class 0 OID 0)
-- Dependencies: 447
-- Name: location_hierchy_closer_det_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_hierchy_closer_det_id_seq OWNED BY public.location_hierchy_closer_det.id;

--
-- TOC entry 450 (class 1259 OID 222721)
-- Name: location_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_master_id_seq OWNER TO postgres;

--
-- TOC entry 6520 (class 0 OID 0)
-- Dependencies: 450
-- Name: location_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_master_id_seq OWNED BY public.location_master.id;


--
-- TOC entry 451 (class 1259 OID 222723)
-- Name: location_mobile_feature_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_mobile_feature_master (
    id integer NOT NULL,
    location_id integer,
    feature text
);


ALTER TABLE IF EXISTS public.location_mobile_feature_master OWNER TO postgres;

--
-- TOC entry 452 (class 1259 OID 222729)
-- Name: location_mobile_feature_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_mobile_feature_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_mobile_feature_master_id_seq OWNER TO postgres;

--
-- TOC entry 6521 (class 0 OID 0)
-- Dependencies: 452
-- Name: location_mobile_feature_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_mobile_feature_master_id_seq OWNED BY public.location_mobile_feature_master.id;


--
-- TOC entry 453 (class 1259 OID 222731)
-- Name: location_state_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_state_master (
    id integer NOT NULL,
    name text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    state text
);


ALTER TABLE IF EXISTS public.location_state_master OWNER TO postgres;

--
-- TOC entry 454 (class 1259 OID 222737)
-- Name: location_state_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_state_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_state_master_id_seq OWNER TO postgres;

--
-- TOC entry 6522 (class 0 OID 0)
-- Dependencies: 454
-- Name: location_state_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_state_master_id_seq OWNED BY public.location_state_master.id;


--
-- TOC entry 455 (class 1259 OID 222739)
-- Name: location_type_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_type_master (
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    level integer,
    is_soh_enable boolean DEFAULT true,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    id integer NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);


ALTER TABLE IF EXISTS public.location_type_master OWNER TO postgres;

--
-- TOC entry 456 (class 1259 OID 222747)
-- Name: location_type_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.location_type_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.location_type_master_id_seq OWNER TO postgres;

--
-- TOC entry 6523 (class 0 OID 0)
-- Dependencies: 456
-- Name: location_type_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_type_master_id_seq OWNED BY public.location_type_master.id;


--
-- TOC entry 457 (class 1259 OID 222762)
-- Name: location_wise_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_wise_analytics (
    loc_id integer NOT NULL,
    fhs_imported_from_emamta_family integer,
    fhs_imported_from_emamta_member integer,
    fhs_to_be_processed_family integer,
    fhs_verified_family integer,
    fhs_archived_family integer,
    fhs_new_family integer,
    fhs_total_member integer,
    fhs_inreverification_family integer,
    total_family integer,
    total_members integer,
    total_alive integer,
    total_male integer,
    total_female integer,
    total_women integer,
    total_children integer,
    total_adolescents integer,
    blindness integer,
    tb integer,
    sickle_cell integer,
    diabetes integer,
    thalessemia integer,
    hiv integer,
    leprosy integer,
    sickle_male integer,
    sickle_female integer,
    sickle_0_5 integer,
    sickle_5_15 integer,
    sickle_15_45 integer,
    sickle_above_45 integer,
    sickle_bpl integer,
    sickle_apl integer,
    sickle_sc integer,
    sickle_st integer,
    sickle_obc integer,
    sickle_gen integer,
    sickle_abandon integer,
    sickle_married integer,
    sickle_unmarried integer,
    sickle_widow integer,
    sickle_widower integer,
    r11_15_49 integer,
    r11_migrated integer,
    r11_dead integer,
    r11_eligible_couple integer,
    new_fam integer,
    deleted_fam integer,
    new_mem integer,
    deleted_mem integer,
    child_0_5 integer,
    child_dead integer,
    child_new integer,
    child_migrated integer,
    family_varified_last_3_days integer,
    seasonal_migrant_families integer,
    eligible_couples_in_techo integer,
    pregnant_woman_techo integer,
    child_under_5_year integer,
    member_with_adhar_number integer,
    member_with_mobile_number integer,
    total_member_over_thirty integer,
    total_male_over_thirty integer,
    total_female_over_thirty integer,
    total_0to1_children integer,
    total_0to5_children integer,
    total_infant_deaths integer,
    total_10_to_14_unmarried_female integer,
    total_10_to_14_unmarried_male integer,
    total_15_to_18_unmarried_female integer,
    total_15_to_18_unmarried_male integer,
    total_15_to_18_female integer,
    total_10_to_14_male integer,
    total_10_to_14_female integer,
    total_15_to_18_male integer,
    total_male_less_then_18 integer,
    total_female_less_then_18 integer,
    total_transgender_less_then_18 integer,
    total_population_0_to_18 integer,
    total_population_19_to_40 integer,
    total_population_more_than_40 integer,
    chfc_member_verified_by_fhw integer,
    chfc_member_verified_by_mphw integer,
    chfc_family_verified_by_fhw integer,
    chfc_family_verified_by_mphw integer,
    chfc_new_member_by_fhw integer,
    chfc_new_member_by_mphw integer,
    chfc_new_family_by_fhw integer,
    chfc_new_family_by_mphw integer,
    chfc_member_in_reverification_by_fhw integer,
    chfc_member_in_reverification_by_mphw integer,
    chfc_family_in_reverification_by_fhw integer,
    chfc_family_in_reverification_by_mphw integer,
    chfc_single_member_existing_families integer,
    chfc_single_member_newly_added_families integer,
    chfc_two_member_existing_families integer,
    chfc_two_member_newly_added_families integer,
    chfc_three_member_existing_families integer,
    chfc_three_member_newly_added_families integer,
    chfc_more_then_three_member_existing_families integer,
    chfc_more_then_three_member_newly_added_families integer,
    member_60_plus_age integer,
    total_6_to_10 integer,
    total_11_to_49 integer,
    total_50_to_59 integer,
    total_60_plus integer,
    chfc_remaining_family integer,
    member_0_to_5_male integer,
    member_0_to_5_female integer,
    member_0_to_5_male_with_comorbid integer,
    member_0_to_5_female_with_comorbid integer,
    member_6_to_10_male integer,
    member_6_to_10_female integer,
    member_6_to_10_male_with_comorbid integer,
    member_6_to_10_female_with_comorbid integer,
    member_11_to_20_male integer,
    member_11_to_20_female integer,
    member_11_to_20_male_with_comorbid integer,
    member_11_to_20_female_with_comorbid integer,
    member_21_to_30_male integer,
    member_21_to_30_female integer,
    member_21_to_30_male_with_comorbid integer,
    member_21_to_30_female_with_comorbid integer,
    member_31_to_40_male integer,
    member_31_to_40_female integer,
    member_31_to_40_male_with_comorbid integer,
    member_31_to_40_female_with_comorbid integer,
    member_41_to_50_male integer,
    member_41_to_50_female integer,
    member_41_to_50_male_with_comorbid integer,
    member_41_to_50_female_with_comorbid integer,
    member_51_to_60_male integer,
    member_51_to_60_female integer,
    member_51_to_60_male_with_comorbid integer,
    member_51_to_60_female_with_comorbid integer,
    member_60_plus_male integer,
    member_60_plus_female integer,
    member_60_plus_male_with_comorbid integer,
    member_60_plus_female_with_comorbid integer,
    member_11_to_18_male integer,
    member_11_to_18_male_with_comorbid integer,
    member_11_to_18_female integer,
    member_11_to_18_female_with_comorbid integer,
    member_19_to_20_male integer,
    member_19_to_20_male_with_comorbid integer,
    member_19_to_20_female integer,
    member_19_to_20_female_with_comorbid integer,
    chfc_member_verified_by_asha integer,
    chfc_new_family_by_asha integer,
    chfc_new_member_by_asha integer
);


ALTER TABLE IF EXISTS public.location_wise_analytics OWNER TO postgres;

--
-- TOC entry 6524 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.chfc_remaining_family; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.chfc_remaining_family IS 'Chfc remaining family count';


--
-- TOC entry 6525 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_0_to_5_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_0_to_5_male IS '0 to 5 male in location wise analytics';


--
-- TOC entry 6526 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_0_to_5_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_0_to_5_female IS '0 to 5 female in location wise analytics';


--
-- TOC entry 6527 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_0_to_5_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_0_to_5_male_with_comorbid IS '0 to 5 male with comorbidity in location wise analytics';


--
-- TOC entry 6528 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_0_to_5_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_0_to_5_female_with_comorbid IS '0 to 5 female with comorbidity in location wise analytics';


--
-- TOC entry 6529 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_6_to_10_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_6_to_10_male IS '6 to 10 male in location wise analytics';


--
-- TOC entry 6530 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_6_to_10_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_6_to_10_female IS '6 to 10 female in location wise analytics';


--
-- TOC entry 6531 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_6_to_10_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_6_to_10_male_with_comorbid IS '6 to 10 male with comorbidity in location wise analytics';


--
-- TOC entry 6532 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_6_to_10_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_6_to_10_female_with_comorbid IS '6 to 10 female with comorbid in location wise analytics';


--
-- TOC entry 6533 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_20_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_20_male IS '11 to 20 male in location wise analytics';


--
-- TOC entry 6534 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_20_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_20_female IS '11 to 20 female in location wise analytics';


--
-- TOC entry 6535 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_20_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_20_male_with_comorbid IS '11 to 20 male with comorbidity in location wise analytics';


--
-- TOC entry 6536 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_20_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_20_female_with_comorbid IS '11 to 20 female with comorbidity in location wise analytics';


--
-- TOC entry 6537 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_21_to_30_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_21_to_30_male IS '21 to 30 male in location wise analytics';


--
-- TOC entry 6538 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_21_to_30_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_21_to_30_female IS '21 to 30 female in location wise analytics';


--
-- TOC entry 6539 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_21_to_30_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_21_to_30_male_with_comorbid IS '21 to 30 male with comorbidity in location wise analytics';


--
-- TOC entry 6540 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_21_to_30_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_21_to_30_female_with_comorbid IS '21 to 30 female with comorbidity in location wise analytics';


--
-- TOC entry 6541 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_31_to_40_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_31_to_40_male IS '31 to 40 male in location wise analytics';


--
-- TOC entry 6542 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_31_to_40_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_31_to_40_female IS '31 to 40 female in location wise analytics';


--
-- TOC entry 6543 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_31_to_40_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_31_to_40_male_with_comorbid IS '31 to 40 male with comorbidity in location wise analytics';


--
-- TOC entry 6544 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_31_to_40_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_31_to_40_female_with_comorbid IS '31 to 40 female with comorbidity in location wise analytics';


--
-- TOC entry 6545 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_41_to_50_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_41_to_50_male IS '41 to 50 male in location wise analytics';


--
-- TOC entry 6546 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_41_to_50_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_41_to_50_female IS '41 to 50 female in location wise analytics';


--
-- TOC entry 6547 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_41_to_50_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_41_to_50_male_with_comorbid IS '41 to 50 male with comorbidity in location wise analytics';


--
-- TOC entry 6548 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_41_to_50_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_41_to_50_female_with_comorbid IS '41 to 50 female with comorbidity in location wise analytics';


--
-- TOC entry 6549 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_51_to_60_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_51_to_60_male IS '51 to 60 male in location wise analytics';


--
-- TOC entry 6550 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_51_to_60_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_51_to_60_female IS '51 to 60 female in location wise analytics';


--
-- TOC entry 6551 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_51_to_60_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_51_to_60_male_with_comorbid IS '51 to 60 female with comorbidity in location wise analytics';


--
-- TOC entry 6552 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_51_to_60_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_51_to_60_female_with_comorbid IS '51 to 60 female with comorbidity in location wise analytics';


--
-- TOC entry 6553 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_60_plus_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_60_plus_male IS '60 plus male in location wise analytics';


--
-- TOC entry 6554 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_60_plus_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_60_plus_female IS '60 plus female in location wise analytics';


--
-- TOC entry 6555 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_60_plus_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_60_plus_male_with_comorbid IS '60 plus male with comorbidity in location wise analytics';


--
-- TOC entry 6556 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_60_plus_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_60_plus_female_with_comorbid IS '60 plus female with comorbidity in location wise analytics';


--
-- TOC entry 6557 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_18_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_18_male IS '11 to 18 male in location wise analytics';


--
-- TOC entry 6558 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_18_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_18_male_with_comorbid IS '11 to 18 male in location wise analytics with comorbid';


--
-- TOC entry 6559 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_18_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_18_female IS '11 to 18 female in location wise analytics';


--
-- TOC entry 6560 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_11_to_18_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_11_to_18_female_with_comorbid IS '11 to 18 female in location wise analytics with comorbid';


--
-- TOC entry 6561 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_19_to_20_male; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_19_to_20_male IS '19 to 20 male in location wise analytics';


--
-- TOC entry 6562 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_19_to_20_male_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_19_to_20_male_with_comorbid IS '19 to 20 male in location wise analytics with comorbid';


--
-- TOC entry 6563 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_19_to_20_female; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_19_to_20_female IS '19 to 20 female in location wise analytics';


--
-- TOC entry 6564 (class 0 OID 0)
-- Dependencies: 457
-- Name: COLUMN location_wise_analytics.member_19_to_20_female_with_comorbid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_analytics.member_19_to_20_female_with_comorbid IS '19 to 20 female in location wise analytics with comorbid';


--
-- TOC entry 458 (class 1259 OID 222765)
-- Name: location_wise_expected_target; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.location_wise_expected_target (
    location_id integer NOT NULL,
    financial_year character varying(20) NOT NULL,
    expected_mother_reg integer,
    expected_delivery_reg integer,
    ela_dpt_opv_mes_vita_1dose integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    ela_dpt_opv_mes_vita_2dose integer,
    state character varying(10),
    no_of_times_unlocked integer
);


ALTER TABLE IF EXISTS public.location_wise_expected_target OWNER TO postgres;

--
-- TOC entry 6565 (class 0 OID 0)
-- Dependencies: 458
-- Name: COLUMN location_wise_expected_target.ela_dpt_opv_mes_vita_2dose; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.location_wise_expected_target.ela_dpt_opv_mes_vita_2dose IS '2nd Year No Of expected vaccination dose(DPT Booster 1st & 2nd Dose, OPV Booster, Measles 2nd Dose and Vitamin A 2nd Dose))';


--
-- TOC entry 459 (class 1259 OID 222768)
-- Name: logged_actions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.logged_actions (
    event_id integer NOT NULL,
    schema_name text NOT NULL,
    table_name text NOT NULL,
    relid oid NOT NULL,
    session_user_name text,
    action_tstamp_tx timestamp with time zone NOT NULL,
    action_tstamp_stm timestamp with time zone NOT NULL,
    action_tstamp_clk timestamp with time zone NOT NULL,
    transaction_id bigint,
    application_name text,
    client_addr inet,
    client_port integer,
    client_query text,
    action text NOT NULL,
    row_data public.hstore,
    changed_fields public.hstore,
    statement_only boolean NOT NULL,
    CONSTRAINT logged_actions_action_check CHECK ((action = ANY (ARRAY['I'::text, 'D'::text, 'U'::text, 'T'::text])))
);


ALTER TABLE IF EXISTS public.logged_actions OWNER TO postgres;

--
-- TOC entry 6566 (class 0 OID 0)
-- Dependencies: 459
-- Name: TABLE logged_actions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.logged_actions IS 'History of auditable actions on audited tables, from public.if_modified_func()';


--
-- TOC entry 6567 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.event_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.event_id IS 'Unique identifier for each auditable event';


--
-- TOC entry 6568 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.schema_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.schema_name IS 'Database schema audited table for this event is in';


--
-- TOC entry 6569 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.table_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.table_name IS 'Non-schema-qualified table name of table event occured in';


--
-- TOC entry 6570 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.relid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.relid IS 'Table OID. Changes with drop/create. Get with ''tablename''::regclass';


--
-- TOC entry 6571 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.session_user_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.session_user_name IS 'Login / session user whose statement caused the audited event';


--
-- TOC entry 6572 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.action_tstamp_tx; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.action_tstamp_tx IS 'Transaction start timestamp for tx in which audited event occurred';


--
-- TOC entry 6573 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.action_tstamp_stm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.action_tstamp_stm IS 'Statement start timestamp for tx in which audited event occurred';


--
-- TOC entry 6574 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.action_tstamp_clk; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.action_tstamp_clk IS 'Wall clock time at which audited event''s trigger call occurred';


--
-- TOC entry 6575 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.transaction_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.transaction_id IS 'Identifier of transaction that made the change. May wrap, but unique paired with action_tstamp_tx.';


--
-- TOC entry 6576 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.application_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.application_name IS 'Application name set when this audit event occurred. Can be changed in-session by client.';


--
-- TOC entry 6577 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.client_addr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.client_addr IS 'IP address of client that issued query. Null for unix domain socket.';


--
-- TOC entry 6578 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.client_port; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.client_port IS 'Remote peer IP port address of client that issued query. Undefined for unix socket.';


--
-- TOC entry 6579 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.client_query; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.client_query IS 'Top-level query that caused this auditable event. May be more than one statement.';


--
-- TOC entry 6580 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.action; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.action IS 'Action type; I = insert, D = delete, U = update, T = truncate';


--
-- TOC entry 6581 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.row_data; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.row_data IS 'Record value. Null for statement-level trigger. For INSERT this is the new tuple. For DELETE and UPDATE it is the old tuple.';


--
-- TOC entry 6582 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.changed_fields; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.changed_fields IS 'New values of fields changed by UPDATE. Null except for row-level UPDATE events.';


--
-- TOC entry 6583 (class 0 OID 0)
-- Dependencies: 459
-- Name: COLUMN logged_actions.statement_only; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.logged_actions.statement_only IS '''t'' if audit event is from an FOR EACH STATEMENT trigger, ''f'' for FOR EACH ROW';


--
-- TOC entry 460 (class 1259 OID 222775)
-- Name: logged_actions_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.logged_actions_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.logged_actions_event_id_seq OWNER TO postgres;

--
-- TOC entry 6584 (class 0 OID 0)
-- Dependencies: 460
-- Name: logged_actions_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.logged_actions_event_id_seq OWNED BY public.logged_actions.event_id;


--
-- TOC entry 461 (class 1259 OID 222794)
-- Name: member_unique_health_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.member_unique_health_id_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.member_unique_health_id_seq OWNER TO postgres;

--
-- TOC entry 462 (class 1259 OID 222796)
-- Name: menu_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.menu_config (
    id integer NOT NULL,
    feature_json text,
    group_id integer,
    active boolean,
    is_dynamic_report boolean,
    menu_name character varying(150),
    navigation_state character varying(255),
    sub_group_id integer,
    menu_type character varying(100),
    only_admin boolean,
    menu_display_order integer,
    uuid uuid,
    group_name_uuid uuid,
    sub_group_uuid uuid,
    description character varying(500)
);


ALTER TABLE IF EXISTS public.menu_config OWNER TO postgres;

--
-- TOC entry 463 (class 1259 OID 222802)
-- Name: menu_config_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.menu_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.menu_config_id_seq OWNER TO postgres;

--
-- TOC entry 6585 (class 0 OID 0)
-- Dependencies: 463
-- Name: menu_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.menu_config_id_seq OWNED BY public.menu_config.id;


--
-- TOC entry 464 (class 1259 OID 222804)
-- Name: menu_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.menu_group (
    id integer NOT NULL,
    group_name character varying(100),
    active boolean,
    parent_group integer,
    group_type character varying(255),
    menu_display_order integer
);


ALTER TABLE IF EXISTS public.menu_group OWNER TO postgres;

--
-- TOC entry 465 (class 1259 OID 222807)
-- Name: menu_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.menu_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.menu_group_id_seq OWNER TO postgres;

--
-- TOC entry 6586 (class 0 OID 0)
-- Dependencies: 465
-- Name: menu_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.menu_group_id_seq OWNED BY public.menu_group.id;


--
-- TOC entry 466 (class 1259 OID 222820)
-- Name: migration_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.migration_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.migration_master_id_seq OWNER TO postgres;

--
-- TOC entry 467 (class 1259 OID 222822)
-- Name: migration_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.migration_master (
    id integer DEFAULT nextval('public.migration_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    reported_by integer NOT NULL,
    reported_on timestamp without time zone NOT NULL,
    location_migrated_to integer,
    location_migrated_from integer,
    migrated_location_not_known boolean NOT NULL,
    confirmed_by integer,
    confirmed_on timestamp without time zone,
    type character varying(10) NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    state character varying(15) NOT NULL,
    other_information text,
    family_migrated_from character varying(20),
    family_migrated_to character varying(20),
    no_family boolean,
    out_of_state boolean,
    has_children boolean,
    is_temporary boolean,
    area_migrated_to integer,
    area_migrated_from integer,
    nearest_loc_id integer,
    village_name text,
    fhw_asha_name text,
    fhw_asha_phone text,
    mobile_data text,
    similar_member_verified boolean,
    status character varying(200),
    gvk_call_status text DEFAULT 'com.argusoft.imtecho.gvk.call.to-be-processed'::text,
    call_attempt integer DEFAULT 0,
    migration_reason text,
    schedule_date timestamp without time zone DEFAULT now(),
    reported_location_id integer
);


ALTER TABLE IF EXISTS public.migration_master OWNER TO postgres;

--
-- TOC entry 468 (class 1259 OID 222835)
-- Name: mobile_beans_feature_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_beans_feature_rel (
    id integer NOT NULL,
    bean text,
    feature text
);


ALTER TABLE IF EXISTS public.mobile_beans_feature_rel OWNER TO postgres;

--
-- TOC entry 469 (class 1259 OID 222841)
-- Name: mobile_beans_feature_rel_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.mobile_beans_feature_rel_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.mobile_beans_feature_rel_id_seq OWNER TO postgres;

--
-- TOC entry 6587 (class 0 OID 0)
-- Dependencies: 469
-- Name: mobile_beans_feature_rel_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mobile_beans_feature_rel_id_seq OWNED BY public.mobile_beans_feature_rel.id;


--
-- TOC entry 470 (class 1259 OID 222843)
-- Name: mobile_beans_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_beans_master (
    bean text NOT NULL,
    depends_on_last_sync boolean
);


ALTER TABLE IF EXISTS public.mobile_beans_master OWNER TO postgres;

--
-- TOC entry 471 (class 1259 OID 222849)
-- Name: mobile_feature_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_feature_master (
    mobile_constant text NOT NULL,
    feature_name text,
    mobile_display_name text,
    state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.mobile_feature_master OWNER TO postgres;

--
-- TOC entry 472 (class 1259 OID 222855)
-- Name: mobile_form_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_form_details (
    id integer NOT NULL,
    form_name character varying(255),
    file_name character varying(255),
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.mobile_form_details OWNER TO postgres;

--
-- TOC entry 473 (class 1259 OID 222861)
-- Name: mobile_form_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.mobile_form_details_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.mobile_form_details_id_seq OWNER TO postgres;

--
-- TOC entry 6588 (class 0 OID 0)
-- Dependencies: 473
-- Name: mobile_form_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mobile_form_details_id_seq OWNED BY public.mobile_form_details.id;


--
-- TOC entry 474 (class 1259 OID 222863)
-- Name: mobile_form_feature_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_form_feature_rel (
    form_id integer NOT NULL,
    mobile_constant text NOT NULL
);


ALTER TABLE IF EXISTS public.mobile_form_feature_rel OWNER TO postgres;

--
-- TOC entry 475 (class 1259 OID 222869)
-- Name: mobile_form_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_form_master (
    id integer NOT NULL,
    form_code text NOT NULL,
    title text,
    subtitle text,
    instruction text,
    question text,
    type text,
    is_mandatory boolean,
    mandatory_message text,
    length integer,
    validation text,
    formula text,
    datamap text,
    options text,
    next integer,
    subform text,
    related_property_name text,
    is_hidden boolean,
    event text,
    binding integer,
    page integer,
    hint text,
    help_video text,
    "row" integer,
    list_value_field_key text
);


ALTER TABLE IF EXISTS public.mobile_form_master OWNER TO postgres;

--
-- TOC entry 476 (class 1259 OID 222875)
-- Name: mobile_form_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_form_role_rel (
    form_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.mobile_form_role_rel OWNER TO postgres;

--
-- TOC entry 779 (class 1259 OID 232181)
-- Name: mobile_library_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_library_master (
    id bigint NOT NULL,
    role_id bigint,
    category text,
    file_name text,
    file_type text,
    created_by bigint,
    created_on timestamp without time zone,
    modified_by bigint,
    modified_on timestamp without time zone,
    state character varying DEFAULT 'ACTIVE'::character varying,
    description character varying,
    tag character varying(250)
);


ALTER TABLE IF EXISTS public.mobile_library_master OWNER TO postgres;

--
-- TOC entry 778 (class 1259 OID 232179)
-- Name: mobile_library_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.mobile_library_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.mobile_library_master_id_seq OWNER TO postgres;

--
-- TOC entry 6589 (class 0 OID 0)
-- Dependencies: 778
-- Name: mobile_library_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mobile_library_master_id_seq OWNED BY public.mobile_library_master.id;


--
-- TOC entry 477 (class 1259 OID 222887)
-- Name: mobile_menu_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_menu_master (
    id integer NOT NULL,
    config_json text,
    menu_name text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer
);


ALTER TABLE IF EXISTS public.mobile_menu_master OWNER TO postgres;

--
-- TOC entry 478 (class 1259 OID 222893)
-- Name: mobile_menu_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.mobile_menu_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.mobile_menu_master_id_seq OWNER TO postgres;

--
-- TOC entry 6590 (class 0 OID 0)
-- Dependencies: 478
-- Name: mobile_menu_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mobile_menu_master_id_seq OWNED BY public.mobile_menu_master.id;


--
-- TOC entry 479 (class 1259 OID 222895)
-- Name: mobile_menu_role_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_menu_role_relation (
    menu_id integer,
    role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.mobile_menu_role_relation OWNER TO postgres;

--
-- TOC entry 782 (class 1259 OID 232201)
-- Name: mobile_number_filter_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_number_filter_master (
    id integer NOT NULL,
    mobile_number character varying(10) NOT NULL,
    type character varying(50) NOT NULL,
    reference_id integer
);


ALTER TABLE IF EXISTS public.mobile_number_filter_master OWNER TO postgres;

--
-- TOC entry 781 (class 1259 OID 232199)
-- Name: mobile_number_filter_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.mobile_number_filter_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.mobile_number_filter_master_id_seq OWNER TO postgres;

--
-- TOC entry 6591 (class 0 OID 0)
-- Dependencies: 781
-- Name: mobile_number_filter_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mobile_number_filter_master_id_seq OWNED BY public.mobile_number_filter_master.id;


--
-- TOC entry 480 (class 1259 OID 222903)
-- Name: mobile_version_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.mobile_version_mapping (
    apk_version integer NOT NULL,
    text_version text
);


ALTER TABLE IF EXISTS public.mobile_version_mapping OWNER TO postgres;

--
-- TOC entry 482 (class 1259 OID 223086)
-- Name: ncd_member_breast_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_breast_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_breast_detail_id_seq OWNER TO postgres;

--
-- TOC entry 483 (class 1259 OID 223095)
-- Name: ncd_member_cbac_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_cbac_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_cbac_detail_id_seq OWNER TO postgres;

--
-- TOC entry 484 (class 1259 OID 223104)
-- Name: ncd_member_cervical_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_cervical_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_cervical_detail_id_seq OWNER TO postgres;

--
-- TOC entry 485 (class 1259 OID 223129)
-- Name: ncd_member_diabetes_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_diabetes_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_diabetes_detail_id_seq OWNER TO postgres;

--
-- TOC entry 486 (class 1259 OID 223191)
-- Name: ncd_member_hypertension_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_hypertension_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_hypertension_detail_id_seq OWNER TO postgres;

--
-- TOC entry 487 (class 1259 OID 223229)
-- Name: ncd_member_oral_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_oral_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_oral_detail_id_seq OWNER TO postgres;

--
-- TOC entry 488 (class 1259 OID 223251)
-- Name: ncd_member_referral_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.ncd_member_referral_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.ncd_member_referral_id_seq OWNER TO postgres;

--
-- TOC entry 317 (class 1259 OID 221366)
-- Name: notification_type_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.notification_type_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    code character varying(200) NOT NULL,
    name character varying(300),
    type character varying(50),
    role_id integer,
    state character varying(255),
    notification_for character varying(255),
    action_on_role_id integer,
    data_query text,
    action_query text,
    order_no integer,
    color_code character varying(10),
    data_for text,
    url_based_action boolean,
    url text,
    modal_based_action boolean,
    modal_name text,
    is_location_filter_required boolean DEFAULT false,
    fetch_up_to_level integer,
    required_up_to_level integer,
    is_fetch_according_aoi boolean DEFAULT true,
    uuid uuid
);


ALTER TABLE IF EXISTS public.notification_type_master OWNER TO postgres;

--
-- TOC entry 318 (class 1259 OID 221374)
-- Name: notification_type_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.notification_type_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.notification_type_master_id_seq OWNER TO postgres;

--
-- TOC entry 6592 (class 0 OID 0)
-- Dependencies: 318
-- Name: notification_type_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notification_type_master_id_seq OWNED BY public.notification_type_master.id;


--
-- TOC entry 489 (class 1259 OID 223383)
-- Name: notification_type_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.notification_type_role_rel (
    notification_type_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.notification_type_role_rel OWNER TO postgres;

--
-- TOC entry 490 (class 1259 OID 223407)
-- Name: oauth_access_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_access_token (
    token_id character varying(255),
    token bytea,
    authentication_id character varying(255) NOT NULL,
    user_name character varying(255),
    client_id character varying(255),
    authentication bytea,
    refresh_token character varying(255)
);


ALTER TABLE IF EXISTS public.oauth_access_token OWNER TO postgres;

--
-- TOC entry 491 (class 1259 OID 223413)
-- Name: oauth_approvals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_approvals (
    userid character varying(255),
    clientid character varying(255),
    scope character varying(255),
    status character varying(10),
    expiresat timestamp without time zone,
    lastmodifiedat timestamp without time zone
);


ALTER TABLE IF EXISTS public.oauth_approvals OWNER TO postgres;

--
-- TOC entry 492 (class 1259 OID 223419)
-- Name: oauth_client_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_client_details (
    client_id character varying(255) NOT NULL,
    resource_ids character varying(255),
    client_secret character varying(255),
    scope character varying(255),
    authorized_grant_types character varying(255),
    web_server_redirect_uri character varying(255),
    authorities character varying(255),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information character varying(4096),
    autoapprove character varying(255)
);


ALTER TABLE IF EXISTS public.oauth_client_details OWNER TO postgres;

--
-- TOC entry 493 (class 1259 OID 223425)
-- Name: oauth_client_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_client_token (
    token_id character varying(255),
    token bytea,
    authentication_id character varying(255) NOT NULL,
    user_name character varying(255),
    client_id character varying(255)
);


ALTER TABLE IF EXISTS public.oauth_client_token OWNER TO postgres;

--
-- TOC entry 494 (class 1259 OID 223431)
-- Name: oauth_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_code (
    code character varying(255),
    authentication bytea
);


ALTER TABLE IF EXISTS public.oauth_code OWNER TO postgres;

--
-- TOC entry 495 (class 1259 OID 223437)
-- Name: oauth_refresh_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.oauth_refresh_token (
    token_id character varying(255),
    token bytea,
    authentication bytea
);


ALTER TABLE IF EXISTS public.oauth_refresh_token OWNER TO postgres;

--
-- TOC entry 496 (class 1259 OID 223443)
-- Name: otp_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.otp_master (
    mobile_number text,
    otp text,
    expiry timestamp without time zone,
    state text,
    count integer DEFAULT 0
);


ALTER TABLE IF EXISTS public.otp_master OWNER TO postgres;

--
-- TOC entry 497 (class 1259 OID 223453)
-- Name: query_analysis_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.query_analysis_details (
    id integer NOT NULL,
    execution_time timestamp without time zone NOT NULL,
    query_string text,
    parameters text,
    total_rows integer
);


ALTER TABLE IF EXISTS public.query_analysis_details OWNER TO postgres;

--
-- TOC entry 498 (class 1259 OID 223459)
-- Name: query_analysis_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.query_analysis_details_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.query_analysis_details_id_seq OWNER TO postgres;

--
-- TOC entry 6593 (class 0 OID 0)
-- Dependencies: 498
-- Name: query_analysis_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.query_analysis_details_id_seq OWNED BY public.query_analysis_details.id;


--
-- TOC entry 499 (class 1259 OID 223461)
-- Name: query_analytics_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.query_analytics_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.query_analytics_id_seq1 OWNER TO postgres;

--
-- TOC entry 500 (class 1259 OID 223463)
-- Name: query_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.query_analytics (
    id integer DEFAULT nextval('public.query_analytics_id_seq1'::regclass) NOT NULL,
    execution_time integer,
    query text,
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.query_analytics OWNER TO postgres;

--
-- TOC entry 501 (class 1259 OID 223470)
-- Name: query_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.query_history (
    id integer NOT NULL,
    query text,
    user_id integer,
    params character varying(255),
    returns_result_set boolean,
    executed_state character varying(255),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.query_history OWNER TO postgres;

--
-- TOC entry 502 (class 1259 OID 223476)
-- Name: query_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.query_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.query_history_id_seq OWNER TO postgres;

--
-- TOC entry 6594 (class 0 OID 0)
-- Dependencies: 502
-- Name: query_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.query_history_id_seq OWNED BY public.query_history.id;


--
-- TOC entry 503 (class 1259 OID 223478)
-- Name: query_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.query_master (
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    code character varying(255),
    params text,
    query text,
    returns_result_set boolean,
    state character varying(255),
    description text,
    is_public boolean DEFAULT true,
    uuid uuid
);


ALTER TABLE IF EXISTS public.query_master OWNER TO postgres;

--
-- TOC entry 504 (class 1259 OID 223518)
-- Name: rch_anc_dangerous_sign_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_anc_dangerous_sign_rel (
    anc_id integer NOT NULL,
    dangerous_sign_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_anc_dangerous_sign_rel OWNER TO postgres;

--
-- TOC entry 505 (class 1259 OID 223521)
-- Name: rch_anc_death_reason_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_anc_death_reason_rel (
    anc_id integer NOT NULL,
    death_reason integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_anc_death_reason_rel OWNER TO postgres;

--
-- TOC entry 506 (class 1259 OID 223524)
-- Name: rch_anc_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_anc_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_anc_master_id_seq OWNER TO postgres;

--
-- TOC entry 507 (class 1259 OID 223526)
-- Name: rch_anc_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_anc_master (
    id integer DEFAULT nextval('public.rch_anc_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    location_id integer,
    lmp timestamp without time zone,
    weight numeric(6,2),
    jsy_beneficiary boolean,
    kpsy_beneficiary boolean,
    iay_beneficiary boolean,
    chiranjeevi_yojna_beneficiary boolean,
    anc_place integer,
    systolic_bp integer,
    diastolic_bp integer,
    member_height integer,
    foetal_height integer,
    foetal_heart_sound boolean,
    ifa_tablets_given integer,
    fa_tablets_given integer,
    calcium_tablets_given integer,
    hbsag_test character varying(30),
    blood_sugar_test character varying(30),
    urine_test_done boolean,
    albendazole_given boolean,
    referral_place integer,
    dead_flag boolean,
    other_dangerous_sign character varying(500),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(100),
    edd timestamp without time zone,
    notification_id integer,
    death_date timestamp without time zone,
    vdrl_test character varying(20),
    hiv_test character varying(20),
    place_of_death character varying(20),
    haemoglobin_count numeric(6,2),
    death_reason character varying(50),
    jsy_payment_done boolean,
    last_delivery_outcome character varying(50),
    expected_delivery_place character varying(50),
    family_planning_method character varying(50),
    foetal_position character varying(50),
    dangerous_sign_id character varying(50),
    other_previous_pregnancy_complication text,
    foetal_movement character varying(15),
    referral_done character varying(15),
    urine_albumin character varying(15),
    urine_sugar character varying(15),
    is_high_risk_case boolean,
    blood_group character varying(3),
    sugar_test_after_food_val integer,
    sugar_test_before_food_val integer,
    pregnancy_reg_det_id integer,
    service_date timestamp without time zone,
    sickle_cell_test text,
    is_from_web boolean,
    delivery_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    delivery_done_by text,
    delivery_person integer,
    delivery_person_name text,
    other_death_reason text,
    anmol_registration_id character varying(255),
    anmol_anc_wsdl_code text,
    anmol_anc_status character varying(50),
    anmol_anc_date timestamp without time zone,
    blood_transfusion boolean,
    iron_def_anemia_inj character varying,
    iron_def_anemia_inj_due_date timestamp without time zone,
    death_infra_id integer,
    examined_by_gynecologist boolean,
    is_inj_corticosteroid_given boolean,
    referral_infra_id integer,
    hmis_health_infra_type integer,
    hmis_health_infra_id integer,
    oral_gtt text,
    active_tb boolean,
    malaria boolean,
    ultrasound_test_done boolean,
    ultrasound_test_date timestamp without time zone,
    abnormality_in_ultrasound boolean,
    is_linked_to_abha boolean
);


ALTER TABLE IF EXISTS public.rch_anc_master OWNER TO postgres;

--
-- TOC entry 508 (class 1259 OID 223533)
-- Name: rch_anc_previous_pregnancy_complication_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_anc_previous_pregnancy_complication_rel (
    anc_id integer NOT NULL,
    previous_pregnancy_complication character varying(15) NOT NULL
);


ALTER TABLE IF EXISTS public.rch_anc_previous_pregnancy_complication_rel OWNER TO postgres;

--
-- TOC entry 509 (class 1259 OID 223536)
-- Name: rch_anemia_status_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_anemia_status_analytics (
    location_id integer NOT NULL,
    month_year date NOT NULL,
    anc_reg integer,
    anc_with_hb integer,
    anc_with_hb_more_than_4 integer,
    mild_hb integer,
    modrate_hb integer,
    severe_hb integer,
    normal_hb integer,
    severe_hb_with_iron_def_inj integer,
    severe_hb_with_blood_transfusion integer
);


ALTER TABLE IF EXISTS public.rch_anemia_status_analytics OWNER TO postgres;

--
-- TOC entry 510 (class 1259 OID 223539)
-- Name: rch_asha_anc_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_anc_master (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    service_date timestamp without time zone NOT NULL,
    pregnancy_reg_det_id integer,
    family_understands boolean,
    trained_dai_introduced boolean,
    planned_delivery_at text,
    expected_delivery_place text,
    family_understands_danger_signs boolean,
    money_arrangements boolean,
    accompany_identified boolean,
    headache boolean,
    visual_disturbances boolean,
    convulsions boolean,
    cough text,
    fever boolean,
    chills_rigors boolean,
    jaundice boolean,
    vomitting boolean,
    vaginal_bleeding boolean,
    burning_urination boolean,
    severe_joint_pain boolean,
    vaginal_discharge text,
    leaking_per_vaginally boolean,
    easily_tired boolean,
    short_of_breath boolean,
    swelling_feet_hands_face boolean,
    visited_doctor_since_last_visit boolean,
    doctor_visit_date date,
    ifa_tablets_taken integer,
    conjuctiva_and_palms_pale boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status text,
    is_alive boolean,
    death_date date,
    death_place text,
    death_reason text,
    other_death_reason text,
    blood_donor_identified boolean,
    blood_donor_from_family boolean,
    blood_donor_name text,
    blood_donor_contact_number text,
    severe_abdominal_pain boolean,
    fetal_movement text,
    iron_sucrose_given boolean,
    no_of_inj_iron_sucrose_given integer
);


ALTER TABLE IF EXISTS public.rch_asha_anc_master OWNER TO postgres;

--
-- TOC entry 511 (class 1259 OID 223545)
-- Name: rch_asha_anc_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_anc_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_anc_master_id_seq OWNER TO postgres;

--
-- TOC entry 6595 (class 0 OID 0)
-- Dependencies: 511
-- Name: rch_asha_anc_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_anc_master_id_seq OWNED BY public.rch_asha_anc_master.id;


--
-- TOC entry 512 (class 1259 OID 223547)
-- Name: rch_asha_anc_morbidity_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_anc_morbidity_details (
    morbidity_id integer NOT NULL,
    code text NOT NULL,
    status character varying(1),
    symptoms text
);


ALTER TABLE IF EXISTS public.rch_asha_anc_morbidity_details OWNER TO postgres;

--
-- TOC entry 6596 (class 0 OID 0)
-- Dependencies: 512
-- Name: COLUMN rch_asha_anc_morbidity_details.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rch_asha_anc_morbidity_details.status IS 'Indicates Morbidity risk status(RED,YELLOW,GREEN)';


--
-- TOC entry 513 (class 1259 OID 223553)
-- Name: rch_asha_anc_morbidity_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_anc_morbidity_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    location_id integer NOT NULL,
    anc_id integer,
    longitude text,
    latitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    family_understand boolean,
    morbidity_found boolean,
    family_understands_big_hospital boolean,
    ready_for_referral boolean,
    able_to_call_108 boolean,
    call_log text,
    accompany_women boolean,
    referral_slip_given boolean,
    referral_place text,
    referral_vehicle text,
    understand_hospital_delivery boolean,
    severe_anemia_ifa_understand boolean,
    sickle_cell_pcm_given boolean,
    bad_obstetric_doctor_visit boolean,
    bad_obstetric_hospital_visit boolean,
    bad_obstetric_hospital_delivery boolean,
    unintended_preg_continue_pregnancy boolean,
    unintended_preg_arrange_marriage boolean,
    unintended_preg_termination_understand boolean,
    unintended_preg_help boolean,
    mild_hypertension_hospital_delivery boolean,
    malaria_chloroquine_given boolean,
    malaria_pcm_given boolean,
    malaria_chloroquine_understand boolean,
    malaria_family_understand boolean,
    fever_pcm_given boolean,
    fever_phc_visit boolean,
    fever_family_understand boolean,
    urinary_tract_hospital_visit boolean,
    urinary_tract_family_understand boolean,
    vaginitis_hospital_visit boolean,
    vaginitis_family_understand boolean,
    vaginitis_bathe_daily boolean,
    night_blindness_vhnd_visit boolean,
    probable_anemia_ifa_understand boolean,
    probable_anemia_hospital_delivery boolean,
    emesis_pregnancy_family_understand boolean,
    respiratory_tract_drink_water boolean,
    moderate_anemia_ifa_given boolean,
    moderate_anemia_ifa_understand boolean,
    moderate_anemia_hospital_delivery boolean,
    breast_problem_demonstration boolean,
    breast_problem_syringe_given boolean
);


ALTER TABLE IF EXISTS public.rch_asha_anc_morbidity_master OWNER TO postgres;

--
-- TOC entry 514 (class 1259 OID 223559)
-- Name: rch_asha_anc_morbidity_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_anc_morbidity_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_anc_morbidity_master_id_seq OWNER TO postgres;

--
-- TOC entry 6597 (class 0 OID 0)
-- Dependencies: 514
-- Name: rch_asha_anc_morbidity_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_anc_morbidity_master_id_seq OWNED BY public.rch_asha_anc_morbidity_master.id;


--
-- TOC entry 515 (class 1259 OID 223561)
-- Name: rch_asha_child_service_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_child_service_master (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    service_date date,
    service_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    is_alive boolean,
    death_date date,
    death_place text,
    death_reason text,
    other_death_reason text,
    unable_to_drink_breastfeed boolean,
    vomit_everything boolean,
    have_convulsions boolean,
    have_cough_or_fast_breathing boolean,
    cough_days integer,
    breath_in_one_minute integer,
    have_chest_indrawing boolean,
    have_more_stools_diarrhea boolean,
    diarrhea_days integer,
    blood_in_stools boolean,
    sunken_eyes boolean,
    irritable_or_restless boolean,
    lethargic_or_unconsious boolean,
    skin_behaviour_after_pinching text,
    drinking_behaviour text,
    have_fever boolean,
    fever_days integer,
    fever_present_each_day boolean,
    is_neck_stiff boolean,
    have_fever_on_service boolean,
    have_palmer_poller text,
    have_severe_wasting boolean,
    have_edema_both_feet boolean,
    weight numeric(6,2),
    date_of_weight date,
    mother_informed_about_grade boolean,
    member_status text
);


ALTER TABLE IF EXISTS public.rch_asha_child_service_master OWNER TO postgres;

--
-- TOC entry 516 (class 1259 OID 223567)
-- Name: rch_asha_child_service_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_child_service_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_child_service_master_id_seq OWNER TO postgres;

--
-- TOC entry 6598 (class 0 OID 0)
-- Dependencies: 516
-- Name: rch_asha_child_service_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_child_service_master_id_seq OWNED BY public.rch_asha_child_service_master.id;


--
-- TOC entry 517 (class 1259 OID 223569)
-- Name: rch_asha_cs_morbidity_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_cs_morbidity_details (
    morbidity_id integer NOT NULL,
    code text NOT NULL,
    status character varying(1),
    symptoms text
);


ALTER TABLE IF EXISTS public.rch_asha_cs_morbidity_details OWNER TO postgres;

--
-- TOC entry 518 (class 1259 OID 223575)
-- Name: rch_asha_cs_morbidity_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_cs_morbidity_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    location_id integer NOT NULL,
    cs_id integer,
    longitude text,
    latitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    family_understand boolean,
    morbidity_found boolean,
    ready_for_referral boolean,
    able_to_call_108 boolean,
    call_log text,
    accompany_child boolean,
    referral_slip_given boolean,
    referral_place text,
    referral_vehicle text,
    severe_pneumonia_oral_food boolean,
    severe_pneumonia_cotri_given text,
    chronic_cough_family_understand boolean,
    diarrhoea_severe_dehydration_oral_food boolean,
    diarrhoea_severe_dehydration_ors_given text,
    febrile_illness_cotri_given text,
    febrile_illness_slides_taken boolean,
    febrile_illness_chloroquine_given text,
    febrile_illness_pcm_given text,
    severe_malnutrition_vitamin_a_given text,
    severe_malnutrition_breast_feeding_understand boolean,
    severe_anemia_folic_given text,
    severe_persistent_diarrioea_oral_food boolean,
    severe_persistent_diarrioea_ors_given text,
    pneumonia_cotri_given text,
    pneumonia_cotrimoxazole_syrup boolean,
    pneumonia_family_understand boolean,
    diarrhoea_some_dehydration_ors_given text,
    diarrhoea_some_dehydration_ors_understand boolean,
    diarrhoea_some_dehydration_family_understand boolean,
    diarrhoea_some_dehydration_dehydration_understand boolean,
    dysentry_cotri_given text,
    dysentry_cotrimoxazole_syrup boolean,
    dysentry_return_immediately boolean,
    malaria_slides_taken boolean,
    malaria_chloroquine_given text,
    malaria_pcm_given text,
    malaria_fever_persist_family_understand boolean,
    anemia_folic_given text,
    cold_cough_family_understand boolean,
    diarrhoea_no_dehydration_ors_given text,
    diarrhoea_no_dehydration_ors_understand boolean,
    diarrhoea_no_dehydration_family_understand boolean,
    no_anemia_folic_given text
);


ALTER TABLE IF EXISTS public.rch_asha_cs_morbidity_master OWNER TO postgres;

--
-- TOC entry 519 (class 1259 OID 223581)
-- Name: rch_asha_cs_morbidity_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_cs_morbidity_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_cs_morbidity_master_id_seq OWNER TO postgres;

--
-- TOC entry 6599 (class 0 OID 0)
-- Dependencies: 519
-- Name: rch_asha_cs_morbidity_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_cs_morbidity_master_id_seq OWNED BY public.rch_asha_cs_morbidity_master.id;


--
-- TOC entry 520 (class 1259 OID 223583)
-- Name: rch_asha_lmp_follow_up; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_lmp_follow_up (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    year smallint,
    service_date timestamp without time zone,
    member_status text,
    lmp timestamp without time zone,
    phone_number text,
    is_pregnant boolean,
    pregnancy_test_done boolean,
    family_planning_method text,
    fp_insert_operate_date timestamp without time zone,
    place_of_death text,
    death_date timestamp without time zone,
    death_reason text,
    other_death_reason text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    preg_conf_status text,
    current_gravida smallint,
    current_para smallint,
    confirmed_by text,
    past_abortions smallint
);


ALTER TABLE IF EXISTS public.rch_asha_lmp_follow_up OWNER TO postgres;

--
-- TOC entry 521 (class 1259 OID 223589)
-- Name: rch_asha_lmp_follow_up_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_lmp_follow_up_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_lmp_follow_up_id_seq OWNER TO postgres;

--
-- TOC entry 6600 (class 0 OID 0)
-- Dependencies: 521
-- Name: rch_asha_lmp_follow_up_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_lmp_follow_up_id_seq OWNED BY public.rch_asha_lmp_follow_up.id;


--
-- TOC entry 522 (class 1259 OID 223591)
-- Name: rch_asha_member_services_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_member_services_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_member_services_id_seq OWNER TO postgres;

--
-- TOC entry 523 (class 1259 OID 223593)
-- Name: rch_asha_member_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_member_services (
    id integer DEFAULT nextval('public.rch_asha_member_services_id_seq'::regclass) NOT NULL,
    location_id integer,
    user_id integer,
    member_id integer,
    service_type character varying(50),
    service_date timestamp without time zone,
    server_date timestamp without time zone,
    created_on timestamp without time zone,
    visit_id integer,
    longitude text,
    latitude text,
    lat_long_location_id integer,
    lat_long_location_distance double precision,
    location_state character varying(50),
    geo_location_state character varying(50)
);


ALTER TABLE IF EXISTS public.rch_asha_member_services OWNER TO postgres;

--
-- TOC entry 524 (class 1259 OID 223600)
-- Name: rch_asha_pnc_child_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_child_master (
    id integer NOT NULL,
    pnc_master_id integer,
    child_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    is_alive boolean,
    death_date date,
    death_place text,
    death_reason text,
    other_death_reason text,
    cry text,
    fed_less_than_usual text,
    sucking text,
    throws_milk boolean,
    hands_feets_cold boolean,
    skin text,
    skin_pustules boolean,
    have_chest_indrawing boolean,
    umbilicus text,
    abdomen text,
    tempreature numeric(6,2),
    limbs_neck text,
    eyes text,
    weight numeric(6,2),
    weight_date date,
    how_baby_fed text,
    fed_on_demand boolean,
    has_convulsions boolean,
    blood_in_stool boolean,
    bleeding_from_any_site boolean,
    ready_for_referral boolean,
    able_to_talk_ambulance boolean,
    accompany_the_woman_to_hospital boolean,
    give_referral_slip_to_woman boolean,
    how_patients_go_to_facility text,
    skin_to_skin_contact_kmc boolean,
    no_of_hours_skin_to_skin_contact integer,
    exclusive_breast_feeding boolean,
    referral_place integer
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_child_master OWNER TO postgres;

--
-- TOC entry 525 (class 1259 OID 223606)
-- Name: rch_asha_pnc_child_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_pnc_child_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_pnc_child_master_id_seq OWNER TO postgres;

--
-- TOC entry 6601 (class 0 OID 0)
-- Dependencies: 525
-- Name: rch_asha_pnc_child_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_pnc_child_master_id_seq OWNED BY public.rch_asha_pnc_child_master.id;


--
-- TOC entry 526 (class 1259 OID 223608)
-- Name: rch_asha_pnc_child_morbidity_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_child_morbidity_details (
    morbidity_id integer NOT NULL,
    code text NOT NULL,
    status character varying(1),
    symptoms text
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_child_morbidity_details OWNER TO postgres;

--
-- TOC entry 527 (class 1259 OID 223614)
-- Name: rch_asha_pnc_child_morbidity_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_child_morbidity_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    location_id integer NOT NULL,
    pnc_id integer,
    longitude text,
    latitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    family_understand boolean,
    morbidity_found boolean,
    ready_for_referral boolean,
    able_to_call_108 boolean,
    call_log text,
    accompany_child boolean,
    referral_slip_given boolean,
    referral_place text,
    referral_vehicle text,
    sepsis_cotri_understand boolean,
    sepsis_cotri_given text,
    sepsis_warm_understand boolean,
    sepsis_breast_feeding_understand boolean,
    sepsis_syrup_pcm_given text,
    sepsis_gv_lotion text,
    vlbw_breast_feeding_understand boolean,
    vlbw_warm_understand boolean,
    vlbw_kmc_understand boolean,
    vlbw_call_understand boolean,
    bleeding_warm_understand boolean,
    bleeding_breast_feeding_understand boolean,
    jaundice_breast_feeding_understand boolean,
    jaundice_warm_understand boolean,
    diarrhoea_ors_understand boolean,
    diarrhoea_breast_feeding_understand boolean,
    diarrhoea_call_understand boolean,
    pneumonia_cotri_understand boolean,
    pneumonia_cotri_given text,
    pneumonia_call_understand boolean,
    local_infection_gv_lotion_understand boolean,
    local_infection_gv_lotion_given text,
    local_infection_call_understand boolean,
    hypothermia_risk_understand boolean,
    hypothermia_warm_understand boolean,
    hypothermia_kmc_understand boolean,
    hypothermia_kmc_help boolean,
    high_risk_lbw_breast_feeding_understand boolean,
    high_risk_lbw_warm_understand boolean,
    high_risk_lbw_kmc_understand boolean,
    high_risk_lbw_kmc_help boolean,
    high_risk_lbw_call_understand boolean,
    lbw_breast_feeding_understand boolean,
    lbw_kmc_understand boolean,
    lbw_warm_understand boolean,
    lbw_kmc_help boolean,
    lbw_call_understand boolean
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_child_morbidity_master OWNER TO postgres;

--
-- TOC entry 528 (class 1259 OID 223620)
-- Name: rch_asha_pnc_child_morbidity_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_pnc_child_morbidity_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_pnc_child_morbidity_master_id_seq OWNER TO postgres;

--
-- TOC entry 6602 (class 0 OID 0)
-- Dependencies: 528
-- Name: rch_asha_pnc_child_morbidity_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_pnc_child_morbidity_master_id_seq OWNED BY public.rch_asha_pnc_child_morbidity_master.id;


--
-- TOC entry 529 (class 1259 OID 223622)
-- Name: rch_asha_pnc_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_master (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    pregnancy_reg_det_id integer,
    pnc_no integer,
    service_date date,
    service_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    member_status text
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_master OWNER TO postgres;

--
-- TOC entry 530 (class 1259 OID 223628)
-- Name: rch_asha_pnc_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_pnc_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_pnc_master_id_seq OWNER TO postgres;

--
-- TOC entry 6603 (class 0 OID 0)
-- Dependencies: 530
-- Name: rch_asha_pnc_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_pnc_master_id_seq OWNED BY public.rch_asha_pnc_master.id;


--
-- TOC entry 531 (class 1259 OID 223630)
-- Name: rch_asha_pnc_mother_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_mother_master (
    id integer NOT NULL,
    pnc_master_id integer,
    mother_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    is_alive boolean,
    death_date date,
    death_place text,
    death_reason text,
    other_death_reason text,
    bleeding_continues boolean,
    pads_changed_in_24_hours integer,
    foul_smell_discharge boolean,
    abnormal_behaviour boolean,
    have_fever boolean,
    have_visual_disturbances boolean,
    difficulty_in_breastfeeding boolean,
    severe_abdominal_pain boolean,
    family_understand_to_keep_baby_warm boolean,
    family_understand_for_kangaroo_care boolean,
    helped_mother_to_carry_kmc_first_time boolean
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_master OWNER TO postgres;

--
-- TOC entry 532 (class 1259 OID 223636)
-- Name: rch_asha_pnc_mother_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_pnc_mother_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_master_id_seq OWNER TO postgres;

--
-- TOC entry 6604 (class 0 OID 0)
-- Dependencies: 532
-- Name: rch_asha_pnc_mother_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_pnc_mother_master_id_seq OWNED BY public.rch_asha_pnc_mother_master.id;


--
-- TOC entry 533 (class 1259 OID 223638)
-- Name: rch_asha_pnc_mother_morbidity_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_mother_morbidity_details (
    morbidity_id integer NOT NULL,
    code text NOT NULL,
    status character varying(1),
    symptoms text
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_morbidity_details OWNER TO postgres;

--
-- TOC entry 534 (class 1259 OID 223644)
-- Name: rch_asha_pnc_mother_morbidity_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_mother_morbidity_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    location_id integer NOT NULL,
    pnc_id integer,
    longitude text,
    latitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    family_understand boolean,
    morbidity_found boolean,
    ready_for_referral boolean,
    able_to_call_108 boolean,
    call_log text,
    accompany_women boolean,
    referral_slip_given boolean,
    referral_place text,
    referral_vehicle text,
    mastitis_pcm_given text,
    mastitis_breast_feeding_understand boolean,
    mastitis_warm_water_understand boolean,
    feeding_problem_breast_feeding_understand boolean,
    feeding_problem_engorged_breast_understand boolean,
    feeding_problem_crakd_nipple_understand boolean,
    feeding_problem_retrctd_nipple_understand boolean,
    feeding_problem_family_understand boolean,
    referral_infra_id integer
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_morbidity_master OWNER TO postgres;

--
-- TOC entry 535 (class 1259 OID 223650)
-- Name: rch_asha_pnc_mother_morbidity_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_pnc_mother_morbidity_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_morbidity_master_id_seq OWNER TO postgres;

--
-- TOC entry 6605 (class 0 OID 0)
-- Dependencies: 535
-- Name: rch_asha_pnc_mother_morbidity_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_pnc_mother_morbidity_master_id_seq OWNED BY public.rch_asha_pnc_mother_morbidity_master.id;


--
-- TOC entry 536 (class 1259 OID 223652)
-- Name: rch_asha_pnc_mother_problem_present; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_pnc_mother_problem_present (
    pnc_id integer NOT NULL,
    problem text NOT NULL
);


ALTER TABLE IF EXISTS public.rch_asha_pnc_mother_problem_present OWNER TO postgres;

--
-- TOC entry 537 (class 1259 OID 223658)
-- Name: rch_asha_reported_event_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_reported_event_master (
    id integer NOT NULL,
    event_type text,
    family_id integer,
    member_id integer,
    location_id integer,
    reported_on timestamp without time zone,
    action text,
    action_on timestamp without time zone,
    action_by integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_asha_reported_event_master OWNER TO postgres;

--
-- TOC entry 538 (class 1259 OID 223664)
-- Name: rch_asha_reported_event_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_reported_event_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_reported_event_master_id_seq OWNER TO postgres;

--
-- TOC entry 6606 (class 0 OID 0)
-- Dependencies: 538
-- Name: rch_asha_reported_event_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_reported_event_master_id_seq OWNED BY public.rch_asha_reported_event_master.id;


--
-- TOC entry 539 (class 1259 OID 223666)
-- Name: rch_asha_wpd_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_asha_wpd_master (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    pregnancy_reg_det_id integer,
    notification_id integer,
    service_date date,
    member_status text,
    mother_alive boolean,
    death_date date,
    death_reason text,
    place_of_death text,
    other_death_reason text,
    has_delivery_happened boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_asha_wpd_master OWNER TO postgres;

--
-- TOC entry 540 (class 1259 OID 223672)
-- Name: rch_asha_wpd_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_asha_wpd_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_asha_wpd_master_id_seq OWNER TO postgres;

--
-- TOC entry 6607 (class 0 OID 0)
-- Dependencies: 540
-- Name: rch_asha_wpd_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_asha_wpd_master_id_seq OWNED BY public.rch_asha_wpd_master.id;


--
-- TOC entry 541 (class 1259 OID 223674)
-- Name: rch_child_cerebral_palsy_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_child_cerebral_palsy_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_child_cerebral_palsy_master_id_seq OWNER TO postgres;

--
-- TOC entry 542 (class 1259 OID 223676)
-- Name: rch_child_cerebral_palsy_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_child_cerebral_palsy_master (
    id integer DEFAULT nextval('public.rch_child_cerebral_palsy_master_id_seq'::regclass) NOT NULL,
    member_id integer NOT NULL,
    child_service_id integer,
    dob date,
    hold_head_straight boolean,
    hands_in_mouth boolean,
    look_when_speak boolean,
    make_noise_when_speak boolean,
    look_in_direc_of_sound boolean,
    sit_without_help boolean,
    kneel_down boolean,
    avoid_strangers boolean,
    understand_no boolean,
    enjoy_peekaboo boolean,
    responds_on_name_calling boolean,
    lifts_toys boolean,
    mimic_others boolean,
    drink_from_glass boolean,
    run_independently boolean,
    hold_things_with_finger boolean,
    look_when_name_called boolean,
    speak_simple_words boolean,
    understand_instructions boolean,
    tell_name_of_things boolean,
    flip_pages boolean,
    kick_ball boolean,
    climb_updown_stairs boolean,
    speak_two_sentences boolean,
    like_playing_other_children boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_child_cerebral_palsy_master OWNER TO postgres;

--
-- TOC entry 543 (class 1259 OID 223680)
-- Name: rch_child_cp_suspects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_child_cp_suspects (
    id integer NOT NULL,
    member_id integer,
    location_id integer,
    remarks text,
    child_service_id integer,
    remarks_date date,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    status text
);


ALTER TABLE IF EXISTS public.rch_child_cp_suspects OWNER TO postgres;

--
-- TOC entry 544 (class 1259 OID 223686)
-- Name: rch_child_cp_suspects_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_child_cp_suspects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_child_cp_suspects_id_seq OWNER TO postgres;

--
-- TOC entry 6608 (class 0 OID 0)
-- Dependencies: 544
-- Name: rch_child_cp_suspects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_child_cp_suspects_id_seq OWNED BY public.rch_child_cp_suspects.id;


--
-- TOC entry 545 (class 1259 OID 223688)
-- Name: rch_child_service_death_reason_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_child_service_death_reason_rel (
    child_service_id integer NOT NULL,
    child_death_reason integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_child_service_death_reason_rel OWNER TO postgres;

--
-- TOC entry 546 (class 1259 OID 223691)
-- Name: rch_child_service_diseases_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_child_service_diseases_rel (
    child_service_id integer NOT NULL,
    diseases integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_child_service_diseases_rel OWNER TO postgres;

--
-- TOC entry 547 (class 1259 OID 223694)
-- Name: rch_child_service_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_child_service_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_child_service_master_id_seq OWNER TO postgres;

--
-- TOC entry 548 (class 1259 OID 223696)
-- Name: rch_child_service_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_child_service_master (
    id integer DEFAULT nextval('public.rch_child_service_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    location_id integer,
    notification_id integer,
    is_alive boolean,
    weight numeric(6,2),
    ifa_syrup_given boolean,
    complementary_feeding_started boolean,
    is_treatement_done character varying(100),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    death_date timestamp without time zone,
    place_of_death character varying(20),
    member_status character varying(15),
    death_reason text,
    other_death_reason text,
    complementary_feeding_start_period character varying(15),
    other_diseases text,
    mid_arm_circumference numeric(6,2),
    height integer,
    have_pedal_edema boolean,
    exclusively_breastfeded boolean,
    any_vaccination_pending boolean,
    service_date timestamp without time zone,
    sd_score text,
    is_from_web boolean,
    delivery_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    delivery_done_by text,
    delivery_person integer,
    delivery_person_name text,
    is_high_risk_case boolean,
    anmol_child_tracking_reg_id character varying(255),
    anmol_child_tracking_status character varying(50),
    anmol_child_tracking_wsdl_code text,
    anmol_child_tracking_medical_reg_id character varying(255),
    anmol_child_tracking_medical_status character varying(50),
    anmol_child_tracking_medical_wsdl_code text,
    anmol_child_tracking_date timestamp without time zone,
    anmol_child_tracking_medical_date timestamp without time zone,
    death_infra_id integer,
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS public.rch_child_service_master OWNER TO postgres;

--
-- TOC entry 549 (class 1259 OID 223703)
-- Name: rch_current_state_pregnancy_analytics_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_current_state_pregnancy_analytics_data (
    location_id bigint NOT NULL,
    reg_preg_women integer,
    high_risk integer,
    prev_compl integer,
    chronic integer,
    two_or_more_risk integer,
    current_preg_compl integer,
    severe_anemia integer,
    diabetes integer,
    cur_mal_presentation_issue bigint,
    cur_malaria_issue bigint,
    multipara bigint,
    blood_pressure integer,
    interval_bet_preg_less_18_months integer,
    extreme_age integer,
    height integer,
    weight integer,
    urine_albumin integer,
    anc_in_2or3_trim integer,
    alben_given integer,
    alben_not_given integer,
    pre_preg_pre_eclampsia bigint,
    prev_anemia bigint,
    prev_caesarian bigint,
    prev_aph_pph bigint,
    prev_abortion bigint,
    chro_tb bigint,
    chro_diabetes bigint,
    chro_heart_kidney bigint,
    chro_hiv bigint,
    chro_sickle bigint,
    chro_thalessemia bigint
);


ALTER TABLE IF EXISTS public.rch_current_state_pregnancy_analytics_data OWNER TO postgres;

--
-- TOC entry 550 (class 1259 OID 223706)
-- Name: rch_dashboard_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_dashboard_analytics (
    location_id integer NOT NULL,
    financial_year text NOT NULL,
    expected_mother_reg text,
    anc_reg integer,
    per_anc_reg double precision,
    anc_reg_rank integer,
    early_anc integer,
    per_early_anc double precision,
    early_anc_rank integer,
    expected_delivery_reg text,
    no_of_del integer,
    per_no_of_del double precision,
    no_of_del_rank integer,
    inst_del integer,
    phi_del integer,
    per_phi_del double precision,
    phi_del_rank integer,
    breast_feeding integer,
    per_breast_feeding double precision,
    breast_feeding_rank integer,
    maternal_death integer,
    expected_mmr text,
    per_maternal_death double precision,
    maternal_death_rank integer,
    high_risk_mother_2nd_trimester integer,
    total_beneficiary_under_pmsma integer,
    per_total_beneficiary_under_pmsma double precision,
    pmsma_rank integer,
    infant_death integer,
    expected_imr text,
    per_imr double precision,
    imr_rank integer,
    del_less_eq_34 integer,
    cortico_steroid integer,
    per_cortico_steroid double precision,
    cortico_steroid_rank integer,
    live_birth integer,
    weighed integer,
    weighed_less_than_2_5 integer,
    per_lbw double precision,
    lbw_rank integer,
    expected_fully_immu text,
    fully_immunized integer,
    per_fully_immu double precision,
    fully_immu_rank integer,
    total_male integer,
    total_female integer,
    per_sex_ratio double precision,
    sex_ratio_rank integer,
    ppiucd integer,
    per_ppiucd double precision,
    ppiucd_rank integer,
    total_0_to_5_child integer,
    total_screened_for_malnutition integer,
    per_total_anemic_malnutrition_treated double precision,
    total_anemic_malnutrition_rank integer,
    total_sam_child integer,
    per_total_sam_malnutrition double precision,
    per_total_sam_malnutrition_rank double precision,
    total_anemic integer,
    per_total_anemic double precision,
    total_anemic_rank integer,
    total_severe_anemic integer,
    per_total_sever_anemic double precision,
    total_severe_anemic_treated integer,
    per_total_anemic_treated double precision,
    expected_pw_reg integer,
    expected_inst_del integer,
    expected_phi_del integer,
    expected_home_del integer,
    expected_cortico_steroid integer,
    expected_child_reg integer,
    expected_weighed integer,
    expected_lbw integer,
    expected_ppiucd integer,
    expected_anemic_pw integer,
    expected_severe_anemic_treated integer,
    expected_anc4 integer,
    expected_c_section_delivery integer,
    expected_child_screening_for_malnutritition integer,
    per_sam double precision,
    total_sam_rank integer,
    expected_sam integer,
    expected_total_anemic integer,
    per_anc4 numeric(7,2)
);


ALTER TABLE IF EXISTS public.rch_dashboard_analytics OWNER TO postgres;

--
-- TOC entry 551 (class 1259 OID 223712)
-- Name: rch_data_migration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_data_migration (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    location_id integer,
    executed_on date,
    state character varying(255)
);


ALTER TABLE IF EXISTS public.rch_data_migration OWNER TO postgres;

--
-- TOC entry 552 (class 1259 OID 223715)
-- Name: rch_data_migration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_data_migration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_data_migration_id_seq OWNER TO postgres;

--
-- TOC entry 6609 (class 0 OID 0)
-- Dependencies: 552
-- Name: rch_data_migration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_data_migration_id_seq OWNED BY public.rch_data_migration.id;


--
-- TOC entry 553 (class 1259 OID 223717)
-- Name: rch_delivery_date_base_location_wise_data_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_delivery_date_base_location_wise_data_point (
    location_id bigint NOT NULL,
    month_year date NOT NULL,
    del_reg integer,
    del_reg_still_live_birth integer,
    preg_reg integer,
    del_less_eq_34 integer,
    del_bet_35_37 integer,
    del_greater_37 integer,
    cortico_steroid integer,
    mtp integer,
    lbirth integer,
    sbirth integer,
    abortion integer,
    home_del integer,
    home_del_by_sba integer,
    home_del_by_non_sba integer,
    breast_feeding integer,
    sc integer,
    phc integer,
    chc integer,
    sdh integer,
    uhc integer,
    gia integer,
    chiranjivi integer,
    cm_dashboard_chirnajivi integer,
    pvt integer,
    mdh integer,
    dh integer,
    delivery_108 integer,
    delivery_out_of_state_govt integer,
    delivery_out_of_state_pvt integer,
    phi_del_3_ancs integer,
    inst_del integer,
    maternal_detah integer,
    pnc1 integer,
    pnc2 integer,
    pnc3 integer,
    pnc4 integer,
    full_pnc integer,
    ifa_180_after_delivery integer,
    calcium_360_after_delivery integer
);


ALTER TABLE IF EXISTS public.rch_delivery_date_base_location_wise_data_point OWNER TO postgres;

--
-- TOC entry 554 (class 1259 OID 223720)
-- Name: rch_eligible_couple_location_wise_count_anlytics_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_eligible_couple_location_wise_count_anlytics_detail (
    location_id integer NOT NULL,
    age_group_or_child_cnt character varying(30) NOT NULL,
    eligiblecouple integer,
    male integer,
    female integer,
    coppert integer,
    condom integer,
    orelpills integer,
    injectable integer,
    "none" integer
);


ALTER TABLE IF EXISTS public.rch_eligible_couple_location_wise_count_anlytics_detail OWNER TO postgres;

--
-- TOC entry 555 (class 1259 OID 223723)
-- Name: rch_immunisation_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_immunisation_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_immunisation_master_id_seq OWNER TO postgres;

--
-- TOC entry 556 (class 1259 OID 223725)
-- Name: rch_immunisation_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_immunisation_master (
    id integer DEFAULT nextval('public.rch_immunisation_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    member_type character varying(10),
    visit_type character varying(50),
    visit_id integer,
    notification_id integer,
    immunisation_given character varying(50),
    given_on date,
    given_by integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    family_id integer,
    location_id integer,
    pregnancy_reg_det_id integer,
    anmol_child_tracking_status character varying(50),
    anmol_child_tracking_wsdl_code text,
    anmol_child_tracking_date timestamp without time zone,
    vitamin_a_no integer,
    anmol_child_tracking_reg_id character varying(255),
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS public.rch_immunisation_master OWNER TO postgres;

--
-- TOC entry 557 (class 1259 OID 223732)
-- Name: rch_institution_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_institution_master (
    id integer NOT NULL,
    name text NOT NULL,
    location_id integer NOT NULL,
    type character varying(10),
    is_location boolean,
    state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_institution_master OWNER TO postgres;

--
-- TOC entry 558 (class 1259 OID 223738)
-- Name: rch_institution_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_institution_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_institution_master_id_seq OWNER TO postgres;

--
-- TOC entry 6610 (class 0 OID 0)
-- Dependencies: 558
-- Name: rch_institution_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_institution_master_id_seq OWNED BY public.rch_institution_master.id;


--
-- TOC entry 559 (class 1259 OID 223761)
-- Name: rch_lmp_base_location_wise_data_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_lmp_base_location_wise_data_point (
    location_id bigint NOT NULL,
    month_year date NOT NULL,
    financial_year text,
    anc_reg bigint,
    jsy_beneficiary integer,
    jsy_beneficiaries_with_aadhar_and_bank_no integer,
    jsy_beneficiaries_with_aadhar_reg_no integer,
    jsy_beneficiaries_with_aadhar_id_but_no_bank_details integer,
    jsy_beneficiaries_with_bank_acc_but_no_aadhar_details integer,
    jsy_beneficiaries_with_no_aadhar_and_bank_details integer,
    abortion integer,
    mtp integer,
    no_of_inst_del integer,
    no_of_home_del integer,
    delivery_108 integer,
    delivery_out_of_state_govt integer,
    delivery_out_of_state_pvt integer,
    no_of_maternal_death integer,
    no_of_missing_del integer,
    no_of_not_missing_del integer,
    high_risk_mother bigint,
    pre_preg_pre_eclampsia bigint,
    prev_anemia bigint,
    prev_caesarian bigint,
    prev_aph_pph bigint,
    prev_abortion bigint,
    multipara bigint,
    cur_mal_presentation_issue bigint,
    cur_malaria_issue bigint,
    tb bigint,
    diabetes bigint,
    heart_kidney bigint,
    hiv bigint,
    sickle bigint,
    thalessemia bigint,
    early_anc integer,
    anc1 integer,
    anc2 integer,
    anc3 integer,
    anc4 integer,
    full_anc integer,
    ifa integer,
    tt1 integer,
    tt2 integer,
    tt_booster integer,
    tt2_tt_booster integer,
    lbirth integer,
    sbirth integer,
    home_del integer,
    breast_feeding integer,
    sc integer,
    phc integer,
    chc integer,
    sdh integer,
    uhc integer,
    gia integer,
    chiranjivi integer,
    medi_college integer,
    taluka_hospi integer,
    pvt integer,
    home_del_by_sba integer,
    home_del_by_non_sba integer,
    del_over_due integer,
    ifa_30_tablet_in_30_day integer,
    ifa_30_tablet_in_31_to_61_day integer,
    ifa_30_tablet_in_61_to_90_day integer,
    hb_done integer,
    hb_more_then_11_in_91_to_360_days integer,
    ifa_180_with_hb_in_4_to_9_month integer,
    hb_between_7_to_11 integer,
    ifa_360_with_hb_between_7_to_11_in_4_to_9_month integer,
    ca_180_given_in_2nd_trimester integer,
    ca_180_given_in_3rd_trimester integer,
    hr_anc_regd integer,
    hr_tt1 integer,
    hr_tt2_and_tt_boster integer,
    hr_tt2_tt_booster integer,
    hr_early_anc integer,
    hr_anc1 integer,
    hr_anc2 integer,
    hr_anc3 integer,
    hr_anc4 integer,
    hr_no_of_delivery integer,
    hr_mtp integer,
    hr_abortion integer,
    hr_pnc1 integer,
    hr_pnc2 integer,
    hr_pnc3 integer,
    hr_pnc4 integer,
    hr_maternal_death integer,
    hr_sc integer,
    hr_phc integer,
    hr_fru_chc integer,
    hr_non_fru_chc integer,
    hr_sdh integer,
    hr_uhc integer,
    hr_gia integer,
    hr_chiranjivi integer,
    hr_medi_college integer,
    hr_taluka_hospi integer,
    hr_pvt integer,
    hr_home_del_by_sba integer,
    hr_home_del_by_non_sba integer
);


ALTER TABLE IF EXISTS public.rch_lmp_base_location_wise_data_point OWNER TO postgres;

--
-- TOC entry 560 (class 1259 OID 223767)
-- Name: rch_lmp_follow_up_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_lmp_follow_up_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_lmp_follow_up_id_seq1 OWNER TO postgres;

--
-- TOC entry 561 (class 1259 OID 223769)
-- Name: rch_lmp_follow_up; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_lmp_follow_up (
    id integer DEFAULT nextval('public.rch_lmp_follow_up_id_seq1'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    location_id integer,
    lmp timestamp without time zone,
    is_pregnant boolean,
    pregnancy_test_done boolean,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    register_now_for_pregnancy boolean,
    notification_id integer,
    family_planning_method character varying(50),
    year smallint,
    fp_insert_operate_date timestamp without time zone,
    place_of_death character varying(20),
    member_status character varying(15),
    death_date timestamp without time zone,
    death_reason character varying(50),
    service_date timestamp without time zone,
    other_death_reason text,
    anmol_registration_id character varying(255),
    anmol_upload_status_code text,
    anmol_follow_up_status character varying(50),
    anmol_follow_up_wsdl_code text,
    anmol_follow_up_date timestamp without time zone,
    phone_number text,
    death_infra_id integer,
    date_of_wedding date,
    current_gravida smallint,
    current_para smallint,
    anmol_case_no integer,
    ready_for_more_child boolean,
    currently_using_fp_method boolean,
    reason_of_not_using_fp_method character varying(100),
    change_fp_method boolean,
    changed_fp_method character varying(50),
    changed_fp_insert_operate_date date,
    past_abortions smallint
);


ALTER TABLE IF EXISTS public.rch_lmp_follow_up OWNER TO postgres;

--
-- TOC entry 562 (class 1259 OID 223776)
-- Name: rch_member_data_sync_pending; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_member_data_sync_pending (
    id integer NOT NULL,
    member_id integer,
    created_on timestamp without time zone,
    is_synced boolean
);


ALTER TABLE IF EXISTS public.rch_member_data_sync_pending OWNER TO postgres;

--
-- TOC entry 563 (class 1259 OID 223779)
-- Name: rch_member_data_sync_pending_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_member_data_sync_pending_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_member_data_sync_pending_id_seq OWNER TO postgres;

--
-- TOC entry 6611 (class 0 OID 0)
-- Dependencies: 563
-- Name: rch_member_data_sync_pending_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_member_data_sync_pending_id_seq OWNED BY public.rch_member_data_sync_pending.id;


--
-- TOC entry 564 (class 1259 OID 223781)
-- Name: rch_member_death_deatil_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_member_death_deatil_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_member_death_deatil_id_seq OWNER TO postgres;

--
-- TOC entry 565 (class 1259 OID 223783)
-- Name: rch_member_death_deatil; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_member_death_deatil (
    id integer DEFAULT nextval('public.rch_member_death_deatil_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id text,
    dod timestamp without time zone,
    created_on timestamp without time zone,
    created_by integer,
    death_reason text,
    place_of_death character varying(20),
    location_id integer,
    other_death_reason text,
    service_type text,
    reference_id integer,
    modified_by integer,
    modified_on timestamp without time zone,
    verbal_autopsy boolean,
    health_infrastructure_id integer
);


ALTER TABLE IF EXISTS public.rch_member_death_deatil OWNER TO postgres;

--
-- TOC entry 566 (class 1259 OID 223790)
-- Name: rch_member_death_deatil_reason_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_member_death_deatil_reason_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_member_death_deatil_reason_id_seq OWNER TO postgres;

--
-- TOC entry 567 (class 1259 OID 223792)
-- Name: rch_member_death_deatil_reason; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_member_death_deatil_reason (
    id integer DEFAULT nextval('public.rch_member_death_deatil_reason_id_seq'::regclass) NOT NULL,
    death_detail_id integer NOT NULL,
    death_reason text,
    other_death_reason text,
    user_id integer NOT NULL,
    member_id integer NOT NULL,
    role_id integer NOT NULL,
    created_on timestamp without time zone DEFAULT now()
);


ALTER TABLE IF EXISTS public.rch_member_death_deatil_reason OWNER TO postgres;

--
-- TOC entry 568 (class 1259 OID 223800)
-- Name: rch_member_pregnancy_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_member_pregnancy_status (
    member_id integer NOT NULL,
    reg_date date,
    anc_date text,
    bp text,
    haemoglobin text,
    urine_test text,
    weight text,
    immunisation text,
    fa_tablets text,
    ifa_tablets text,
    calcium_tablets text,
    night_blindness boolean,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_member_pregnancy_status OWNER TO postgres;

--
-- TOC entry 569 (class 1259 OID 223806)
-- Name: rch_member_services_last_90_days; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_member_services_last_90_days (
    id integer NOT NULL,
    location_id integer,
    user_id integer,
    member_id integer,
    service_type character varying(50),
    service_date timestamp without time zone,
    server_date timestamp without time zone,
    created_on timestamp without time zone,
    visit_id integer,
    longitude text,
    latitude text,
    lat_long_location_id integer,
    lat_long_location_distance double precision,
    geo_location_state character varying(50)
);


ALTER TABLE IF EXISTS public.rch_member_services_last_90_days OWNER TO postgres;

--
-- TOC entry 570 (class 1259 OID 223812)
-- Name: rch_opd_edl_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_edl_details (
    id integer NOT NULL,
    member_id integer NOT NULL,
    opd_member_master_id integer NOT NULL,
    edl_id integer NOT NULL,
    frequency numeric(6,2),
    quantity_before_food numeric(6,2),
    quantity_after_food numeric(6,2),
    number_of_days numeric(6,2),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.rch_opd_edl_details OWNER TO postgres;

--
-- TOC entry 571 (class 1259 OID 223815)
-- Name: rch_opd_edl_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_opd_edl_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_opd_edl_details_id_seq OWNER TO postgres;

--
-- TOC entry 6612 (class 0 OID 0)
-- Dependencies: 571
-- Name: rch_opd_edl_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_opd_edl_details_id_seq OWNED BY public.rch_opd_edl_details.id;


--
-- TOC entry 572 (class 1259 OID 223825)
-- Name: rch_opd_lab_test_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_lab_test_master (
    id integer NOT NULL,
    form_id integer,
    name character varying(255) NOT NULL,
    category integer,
    is_active boolean,
    created_by integer,
    created_on timestamp without time zone NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    modified_by integer
);


ALTER TABLE IF EXISTS public.rch_opd_lab_test_master OWNER TO postgres;

--
-- TOC entry 573 (class 1259 OID 223828)
-- Name: rch_opd_lab_test_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_opd_lab_test_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_opd_lab_test_master_id_seq OWNER TO postgres;

--
-- TOC entry 6613 (class 0 OID 0)
-- Dependencies: 573
-- Name: rch_opd_lab_test_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_opd_lab_test_master_id_seq OWNED BY public.rch_opd_lab_test_master.id;


--
-- TOC entry 574 (class 1259 OID 223830)
-- Name: rch_opd_lab_test_provisional_diagnosis_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_lab_test_provisional_diagnosis_mapping (
    lab_test_id integer,
    provisional_id integer
);


ALTER TABLE IF EXISTS public.rch_opd_lab_test_provisional_diagnosis_mapping OWNER TO postgres;

--
-- TOC entry 575 (class 1259 OID 223833)
-- Name: rch_opd_lab_test_provisional_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_lab_test_provisional_rel (
    opd_member_master_id integer NOT NULL,
    provisional_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_opd_lab_test_provisional_rel OWNER TO postgres;

--
-- TOC entry 576 (class 1259 OID 223836)
-- Name: rch_opd_member_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_member_master (
    id integer NOT NULL,
    member_id integer NOT NULL,
    health_infra_id integer NOT NULL,
    service_date timestamp without time zone NOT NULL,
    medicines_given_on timestamp without time zone,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    any_other_state_specific_disease character varying(50),
    unusual_syndromes character varying(50),
    opd_member_registration_id integer,
    is_medicines_given boolean,
    location_id integer,
    instructions text
);


ALTER TABLE IF EXISTS public.rch_opd_member_master OWNER TO postgres;

--
-- TOC entry 577 (class 1259 OID 223842)
-- Name: rch_opd_member_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_opd_member_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_opd_member_master_id_seq OWNER TO postgres;

--
-- TOC entry 6614 (class 0 OID 0)
-- Dependencies: 577
-- Name: rch_opd_member_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_opd_member_master_id_seq OWNED BY public.rch_opd_member_master.id;


--
-- TOC entry 578 (class 1259 OID 223844)
-- Name: rch_opd_member_registration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_opd_member_registration (
    id integer NOT NULL,
    member_id integer NOT NULL,
    registration_date timestamp without time zone NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    health_infra_id integer,
    reference_id integer,
    reference_type character varying(50),
    location_id integer
);


ALTER TABLE IF EXISTS public.rch_opd_member_registration OWNER TO postgres;

--
-- TOC entry 579 (class 1259 OID 223847)
-- Name: rch_opd_member_registration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_opd_member_registration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_opd_member_registration_id_seq OWNER TO postgres;

--
-- TOC entry 6615 (class 0 OID 0)
-- Dependencies: 579
-- Name: rch_opd_member_registration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_opd_member_registration_id_seq OWNED BY public.rch_opd_member_registration.id;


--
-- TOC entry 580 (class 1259 OID 223849)
-- Name: rch_other_form_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_other_form_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_other_form_master_id_seq OWNER TO postgres;

--
-- TOC entry 581 (class 1259 OID 223851)
-- Name: rch_other_form_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_other_form_master (
    id integer DEFAULT nextval('public.rch_other_form_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    location_id integer,
    longitude text,
    latitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    form_code text,
    form_data text,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_other_form_master OWNER TO postgres;

--
-- TOC entry 582 (class 1259 OID 223858)
-- Name: rch_pmsma_service_statistics_during_year; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pmsma_service_statistics_during_year (
    location_id integer NOT NULL,
    financial_year text NOT NULL,
    high_risk_mother_under_techo integer,
    total_anc_under_pmsma integer,
    total_beneficiary_under_pmsma_at_least_once integer
);


ALTER TABLE IF EXISTS public.rch_pmsma_service_statistics_during_year OWNER TO postgres;

--
-- TOC entry 583 (class 1259 OID 223864)
-- Name: rch_pmsma_survey_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pmsma_survey_master (
    id integer NOT NULL,
    member_id integer,
    family_id integer,
    service_date timestamp without time zone,
    name text,
    husband_name text,
    rch_id text,
    edd date,
    is_hrp boolean,
    is_bank_acc_available boolean,
    bank_acc_number text,
    ifsc_code text,
    mobile_number text,
    location_id integer,
    notification_id integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    name_in_bank_acc text,
    bank_acc_opening_form_provided boolean
);


ALTER TABLE IF EXISTS public.rch_pmsma_survey_master OWNER TO postgres;

--
-- TOC entry 584 (class 1259 OID 223870)
-- Name: rch_pmsma_survey_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_pmsma_survey_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_pmsma_survey_master_id_seq OWNER TO postgres;

--
-- TOC entry 6616 (class 0 OID 0)
-- Dependencies: 584
-- Name: rch_pmsma_survey_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_pmsma_survey_master_id_seq OWNED BY public.rch_pmsma_survey_master.id;


--
-- TOC entry 585 (class 1259 OID 223872)
-- Name: rch_pnc_child_danger_signs_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_child_danger_signs_rel (
    child_pnc_id integer NOT NULL,
    child_danger_signs integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_pnc_child_danger_signs_rel OWNER TO postgres;

--
-- TOC entry 586 (class 1259 OID 223875)
-- Name: rch_pnc_child_death_reason_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_child_death_reason_rel (
    child_pnc_id integer NOT NULL,
    child_death_reason integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_pnc_child_death_reason_rel OWNER TO postgres;

--
-- TOC entry 587 (class 1259 OID 223878)
-- Name: rch_pnc_child_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_pnc_child_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_pnc_child_master_id_seq OWNER TO postgres;

--
-- TOC entry 588 (class 1259 OID 223880)
-- Name: rch_pnc_child_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_child_master (
    id integer DEFAULT nextval('public.rch_pnc_child_master_id_seq'::regclass) NOT NULL,
    pnc_master_id integer,
    child_id integer,
    is_alive boolean,
    other_danger_sign text,
    child_weight numeric(6,2),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(15),
    death_date timestamp without time zone,
    death_reason text,
    place_of_death text,
    referral_place integer,
    other_death_reason text,
    is_high_risk_case boolean,
    child_referral_done character varying(15),
    anmol_child_registration_id character varying(255),
    anmol_pnc_wsdl_code text,
    anmol_pnc_status character varying(50),
    anmol_pnc_date timestamp without time zone,
    death_infra_id integer,
    referral_infra_id integer,
    child_length integer
);


ALTER TABLE IF EXISTS public.rch_pnc_child_master OWNER TO postgres;

--
-- TOC entry 589 (class 1259 OID 223887)
-- Name: rch_pnc_family_planning_methods_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_family_planning_methods_rel (
    mother_pnc_id integer NOT NULL,
    family_planning_methods integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_pnc_family_planning_methods_rel OWNER TO postgres;

--
-- TOC entry 590 (class 1259 OID 223890)
-- Name: rch_pnc_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_pnc_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_pnc_master_id_seq OWNER TO postgres;

--
-- TOC entry 591 (class 1259 OID 223892)
-- Name: rch_pnc_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_master (
    id integer DEFAULT nextval('public.rch_pnc_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    location_id integer,
    notification_id integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(15),
    pregnancy_reg_det_id integer,
    pnc_no text,
    is_from_web boolean,
    service_date timestamp without time zone,
    delivery_place text,
    type_of_hospital integer,
    health_infrastructure_id integer,
    delivery_done_by text,
    delivery_person integer,
    delivery_person_name text,
    hmis_health_infra_type integer,
    hmis_health_infra_id integer
);


ALTER TABLE IF EXISTS public.rch_pnc_master OWNER TO postgres;

--
-- TOC entry 592 (class 1259 OID 223899)
-- Name: rch_pnc_mother_danger_signs_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_mother_danger_signs_rel (
    mother_pnc_id integer NOT NULL,
    mother_danger_signs integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_pnc_mother_danger_signs_rel OWNER TO postgres;

--
-- TOC entry 593 (class 1259 OID 223902)
-- Name: rch_pnc_mother_death_reason_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_mother_death_reason_rel (
    mother_pnc_id integer NOT NULL,
    mother_death_reason integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_pnc_mother_death_reason_rel OWNER TO postgres;

--
-- TOC entry 594 (class 1259 OID 223905)
-- Name: rch_pnc_mother_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_pnc_mother_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_pnc_mother_master_id_seq OWNER TO postgres;

--
-- TOC entry 595 (class 1259 OID 223907)
-- Name: rch_pnc_mother_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pnc_mother_master (
    id integer DEFAULT nextval('public.rch_pnc_mother_master_id_seq'::regclass) NOT NULL,
    pnc_master_id integer,
    mother_id integer,
    date_of_delivery timestamp without time zone,
    service_date timestamp without time zone,
    is_alive boolean,
    ifa_tablets_given integer,
    other_danger_sign character varying(999),
    referral_place integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    member_status character varying(15),
    death_date timestamp without time zone,
    death_reason text,
    place_of_death text,
    fp_insert_operate_date timestamp without time zone,
    family_planning_method character varying(50),
    other_death_reason text,
    is_high_risk_case boolean,
    mother_referral_done character varying(15),
    anmol_registration_id character varying(255),
    anmol_pnc_wsdl_code text,
    anmol_pnc_status character varying(50),
    anmol_pnc_date timestamp without time zone,
    death_infra_id integer,
    calcium_tablets_given integer,
    blood_transfusion boolean,
    iron_def_anemia_inj character varying,
    iron_def_anemia_inj_due_date timestamp without time zone,
    referral_infra_id integer
);


ALTER TABLE IF EXISTS public.rch_pnc_mother_master OWNER TO postgres;

--
-- TOC entry 596 (class 1259 OID 223914)
-- Name: rch_pregnancy_registration_det_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_pregnancy_registration_det_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_pregnancy_registration_det_id_seq OWNER TO postgres;

--
-- TOC entry 597 (class 1259 OID 223916)
-- Name: rch_pregnancy_registration_det; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_pregnancy_registration_det (
    id integer DEFAULT nextval('public.rch_pregnancy_registration_det_id_seq'::regclass) NOT NULL,
    mthr_reg_no text,
    member_id integer,
    lmp_date date,
    edd date,
    reg_date timestamp without time zone,
    state text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    location_id integer,
    family_id integer,
    current_location_id bigint,
    delivery_date date,
    is_reg_date_verified boolean
);


ALTER TABLE IF EXISTS public.rch_pregnancy_registration_det OWNER TO postgres;

--
-- TOC entry 598 (class 1259 OID 223923)
-- Name: rch_sam_screening_diseases_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_sam_screening_diseases_rel (
    sam_screening_id integer NOT NULL,
    diseases integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_sam_screening_diseases_rel OWNER TO postgres;

--
-- TOC entry 599 (class 1259 OID 223926)
-- Name: rch_sam_screening_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_sam_screening_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_sam_screening_master_id_seq OWNER TO postgres;

--
-- TOC entry 600 (class 1259 OID 223928)
-- Name: rch_sam_screening_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_sam_screening_master (
    id integer DEFAULT nextval('public.rch_sam_screening_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    location_id integer,
    notification_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    child_visit_id integer,
    screened_on timestamp without time zone,
    height integer,
    weight numeric(6,2),
    mid_arm_circumference numeric(6,2),
    have_pedal_edema boolean,
    sd_score text,
    other_diseases text
);


ALTER TABLE IF EXISTS public.rch_sam_screening_master OWNER TO postgres;

--
-- TOC entry 601 (class 1259 OID 223935)
-- Name: rch_service_provided_during_year; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_service_provided_during_year (
    location_id bigint NOT NULL,
    month_year date NOT NULL,
    financial_year text,
    anc_reg integer,
    beneficiaries_with_aadhar_reg_no integer,
    jsy_beneficiary integer,
    jsy_beneficiary_in_third_trimenster integer,
    jsy_beneficiaries_with_aadhar_and_bank_no integer,
    jsy_beneficiaries_with_aadhar_reg_no integer,
    jsy_beneficiaries_with_aadhar_id_but_no_bank_details integer,
    jsy_beneficiaries_with_bank_acc_but_no_aadhar_details integer,
    jsy_beneficiaries_with_no_aadhar_and_bank_details integer,
    regd_live_children integer,
    regd_no_live_children integer,
    c1_m1 integer,
    c1_f1 integer,
    c2_f2 integer,
    c2_m2 integer,
    c2_f1_m1 integer,
    c3_f3 integer,
    c3_m3 integer,
    c3_m2_f1 integer,
    c3_m1_f2 integer,
    high_risk integer,
    current_preg_compl integer,
    severe_anemia integer,
    diabetes integer,
    cur_mal_presentation_issue bigint,
    cur_malaria_issue bigint,
    multipara bigint,
    blood_pressure integer,
    interval_bet_preg_less_18_months integer,
    extreme_age integer,
    height integer,
    weight integer,
    urine_albumin integer,
    tt1 integer,
    tt2_tt_booster integer,
    early_anc integer,
    anc1 integer,
    anc2 integer,
    anc3 integer,
    anc4 integer,
    no_of_delivery integer,
    mtp integer,
    abortion integer,
    live_birth integer,
    still_birth integer,
    pnc1 integer,
    pnc2 integer,
    pnc3 integer,
    pnc4 integer,
    mother_death integer,
    ppiucd integer,
    complete_anc_date integer,
    ifa_180_anc_date integer,
    phi_del integer,
    phi_del_with_ppiucd integer
);


ALTER TABLE IF EXISTS public.rch_service_provided_during_year OWNER TO postgres;

--
-- TOC entry 602 (class 1259 OID 223941)
-- Name: rch_vaccine_adverse_effect; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_vaccine_adverse_effect (
    id integer NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    notification_id integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    adverse_effect character varying(15),
    vaccine_name character varying(50),
    batch_number character varying(50),
    expiry_date timestamp without time zone NOT NULL,
    manufacturer character varying(50)
);


ALTER TABLE IF EXISTS public.rch_vaccine_adverse_effect OWNER TO postgres;

--
-- TOC entry 603 (class 1259 OID 223944)
-- Name: rch_vaccine_adverse_effect_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_vaccine_adverse_effect_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_vaccine_adverse_effect_id_seq OWNER TO postgres;

--
-- TOC entry 6617 (class 0 OID 0)
-- Dependencies: 603
-- Name: rch_vaccine_adverse_effect_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rch_vaccine_adverse_effect_id_seq OWNED BY public.rch_vaccine_adverse_effect.id;


--
-- TOC entry 604 (class 1259 OID 223946)
-- Name: rch_wpd_child_congential_deformity_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_child_congential_deformity_rel (
    wpd_id integer NOT NULL,
    congential_deformity integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_child_congential_deformity_rel OWNER TO postgres;

--
-- TOC entry 605 (class 1259 OID 223949)
-- Name: rch_wpd_child_danger_signs_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_child_danger_signs_rel (
    wpd_id integer NOT NULL,
    danger_signs integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_child_danger_signs_rel OWNER TO postgres;

--
-- TOC entry 606 (class 1259 OID 223952)
-- Name: rch_wpd_child_high_risk_symptoms_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_child_high_risk_symptoms_rel (
    wpd_id integer NOT NULL,
    high_risk_symptoms integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_child_high_risk_symptoms_rel OWNER TO postgres;

--
-- TOC entry 607 (class 1259 OID 223955)
-- Name: rch_wpd_child_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_wpd_child_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_wpd_child_master_id_seq OWNER TO postgres;

--
-- TOC entry 608 (class 1259 OID 223957)
-- Name: rch_wpd_child_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_child_master (
    id integer DEFAULT nextval('public.rch_wpd_child_master_id_seq'::regclass) NOT NULL,
    member_id integer NOT NULL,
    family_id integer NOT NULL,
    latitude character varying(100),
    longitude character varying(100),
    mobile_start_date timestamp without time zone NOT NULL,
    mobile_end_date timestamp without time zone NOT NULL,
    location_id integer NOT NULL,
    wpd_mother_id integer,
    mother_id integer,
    pregnancy_outcome character varying(100),
    gender character varying(100) NOT NULL,
    birth_weight numeric(6,2),
    date_of_delivery timestamp without time zone,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    baby_cried_at_birth boolean,
    notification_id integer,
    death_date timestamp without time zone,
    death_reason text,
    place_of_death text,
    member_status character varying(15),
    type_of_delivery character varying(15),
    breast_feeding_in_one_hour boolean,
    other_congential_deformity text,
    is_high_risk_case boolean,
    was_premature boolean,
    referral_reason character varying(100),
    referral_transport character varying(100),
    referral_place integer,
    name character varying(100),
    breast_crawl boolean,
    kangaroo_care boolean,
    other_danger_sign character varying(100),
    state text,
    referral_infra_id integer,
    referral_done character varying(300),
    type_of_still_birth text,
    child_alive_after_resuscitation boolean,
    other_death_reason text,
    health_infrastructure_id integer,
    skin_to_skin_care boolean,
    any_complication boolean
);


ALTER TABLE IF EXISTS public.rch_wpd_child_master OWNER TO postgres;

--
-- TOC entry 609 (class 1259 OID 223964)
-- Name: rch_wpd_child_treatments_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_child_treatments_rel (
    wpd_id integer NOT NULL,
    treatments integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_child_treatments_rel OWNER TO postgres;

--
-- TOC entry 610 (class 1259 OID 223967)
-- Name: rch_wpd_mother_danger_signs_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_mother_danger_signs_rel (
    wpd_id integer NOT NULL,
    mother_danger_signs integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_mother_danger_signs_rel OWNER TO postgres;

--
-- TOC entry 611 (class 1259 OID 223970)
-- Name: rch_wpd_mother_death_reason_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_mother_death_reason_rel (
    wpd_id integer NOT NULL,
    mother_death_reason integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_mother_death_reason_rel OWNER TO postgres;

--
-- TOC entry 612 (class 1259 OID 223973)
-- Name: rch_wpd_mother_high_risk_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_mother_high_risk_rel (
    wpd_id integer NOT NULL,
    mother_high_risk integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_mother_high_risk_rel OWNER TO postgres;

--
-- TOC entry 613 (class 1259 OID 223976)
-- Name: rch_wpd_mother_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_wpd_mother_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_wpd_mother_master_id_seq OWNER TO postgres;

--
-- TOC entry 614 (class 1259 OID 223978)
-- Name: rch_wpd_mother_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_mother_master (
    id integer DEFAULT nextval('public.rch_wpd_mother_master_id_seq'::regclass) NOT NULL,
    member_id integer,
    family_id integer,
    latitude text,
    longitude text,
    mobile_start_date timestamp without time zone,
    mobile_end_date timestamp without time zone,
    location_id integer,
    date_of_delivery timestamp without time zone,
    member_status text,
    is_preterm_birth boolean,
    delivery_place text,
    type_of_hospital integer,
    delivery_done_by text,
    mother_alive boolean,
    type_of_delivery text,
    referral_place integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    discharge_date timestamp without time zone,
    breast_feeding_in_one_hour boolean,
    notification_id integer,
    death_date timestamp without time zone,
    death_reason text,
    place_of_death text,
    cortico_steroid_given boolean,
    mtp_done_at integer,
    mtp_performed_by text,
    has_delivery_happened boolean,
    other_danger_signs text,
    is_high_risk_case boolean,
    referral_done text,
    pregnancy_reg_det_id integer,
    pregnancy_outcome text,
    is_discharged boolean,
    misoprostol_given boolean,
    free_drop_delivery text,
    delivery_person integer,
    health_infrastructure_id integer,
    state text,
    delivery_person_name text,
    is_from_web boolean,
    institutional_delivery_place text,
    other_death_reason text,
    free_medicines boolean,
    free_diet boolean,
    free_lab_test boolean,
    free_blood_transfusion boolean,
    free_drop_transport boolean,
    family_planning_method character varying,
    eligible_for_chiranjeevi boolean,
    fbmdsr boolean,
    referral_infra_id integer,
    death_infra_id integer,
    institutional_admission timestamp without time zone,
    post_abortion_fp text,
    initial_assessment text,
    sent_back_home_reason text,
    any_complication boolean,
    admission_date timestamp without time zone
);


ALTER TABLE IF EXISTS public.rch_wpd_mother_master OWNER TO postgres;

--
-- TOC entry 615 (class 1259 OID 223985)
-- Name: rch_wpd_mother_master_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.rch_wpd_mother_master_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.rch_wpd_mother_master_id_seq1 OWNER TO postgres;

--
-- TOC entry 616 (class 1259 OID 223987)
-- Name: rch_wpd_mother_treatment_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_wpd_mother_treatment_rel (
    wpd_id integer NOT NULL,
    mother_treatment integer NOT NULL
);


ALTER TABLE IF EXISTS public.rch_wpd_mother_treatment_rel OWNER TO postgres;

--
-- TOC entry 617 (class 1259 OID 223990)
-- Name: rch_yearly_location_wise_analytics_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.rch_yearly_location_wise_analytics_data (
    location_id bigint NOT NULL,
    financial_year text NOT NULL,
    month_year date NOT NULL,
    age_less_15 integer,
    age_15_19 integer,
    age_20_24 integer,
    age_25_29 integer,
    age_30_34 integer,
    age_35_39 integer,
    age_40_44 integer,
    age_45_49 integer,
    age_greater_49 integer,
    anc_reg integer,
    hbsag_test integer,
    non_reactive integer,
    reactive integer,
    tt1 integer,
    tt2_tt_booster integer,
    early_anc integer,
    anc1 integer,
    anc2 integer,
    anc3 integer,
    anc4 integer,
    no_of_del integer,
    mtp integer,
    abortion integer,
    pnc1 integer,
    pnc2 integer,
    pnc3 integer,
    pnc4 integer,
    maternal_detah integer
);


ALTER TABLE IF EXISTS public.rch_yearly_location_wise_analytics_data OWNER TO postgres;

--
-- TOC entry 618 (class 1259 OID 223996)
-- Name: report_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.report_master (
    id integer NOT NULL,
    report_name character varying(200),
    file_name character varying(500),
    active boolean,
    report_type character varying(15),
    menu_id integer,
    modified_on timestamp without time zone,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    config_json text,
    code character varying(20),
    uuid uuid
);


ALTER TABLE IF EXISTS public.report_master OWNER TO postgres;

--
-- TOC entry 619 (class 1259 OID 224002)
-- Name: report_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.report_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.report_master_id_seq OWNER TO postgres;

--
-- TOC entry 6618 (class 0 OID 0)
-- Dependencies: 619
-- Name: report_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.report_master_id_seq OWNED BY public.report_master.id;


--
-- TOC entry 620 (class 1259 OID 224004)
-- Name: report_offline_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.report_offline_details (
    id integer NOT NULL,
    user_id integer NOT NULL,
    report_id integer NOT NULL,
    report_name character varying(250) NOT NULL,
    report_parameters text,
    file_id bigint,
    file_type character varying(250) NOT NULL,
    status character varying(250) NOT NULL,
    state character varying(250) NOT NULL,
    error text,
    completed_on timestamp without time zone,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.report_offline_details OWNER TO postgres;

--
-- TOC entry 6619 (class 0 OID 0)
-- Dependencies: 620
-- Name: TABLE report_offline_details; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.report_offline_details IS 'This is table to store report offline request';


--
-- TOC entry 6620 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.id IS 'Primary key of table';


--
-- TOC entry 6621 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.user_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.user_id IS 'Id of logged in user';


--
-- TOC entry 6622 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.report_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.report_id IS 'Id of requested report';


--
-- TOC entry 6623 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.report_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.report_name IS 'Name of requested report';


--
-- TOC entry 6624 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.report_parameters; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.report_parameters IS 'ReprotExcelDto in string';


--
-- TOC entry 6625 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.file_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.file_id IS 'Id of generated file of report';


--
-- TOC entry 6626 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.file_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.file_type IS 'Type of file. It can be pdf or excel';


--
-- TOC entry 6627 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.status IS 'Status of report offline request. It can be NEW, PROCESSED, READY_FOR_DOWNLOAD, ERROR, ARCHIVED';


--
-- TOC entry 6628 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.state; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.state IS 'State of report offline request. It can be Active or Inactive';


--
-- TOC entry 6629 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.error; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.error IS 'If Any error occurred during processing report';


--
-- TOC entry 6630 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.completed_on; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.completed_on IS 'Completed of report processing timestamp';


--
-- TOC entry 6631 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.created_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.created_by IS 'Id from um_user';


--
-- TOC entry 6632 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.created_on; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.created_on IS 'Created on timestamp';


--
-- TOC entry 6633 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.modified_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.modified_by IS 'Id from um_user';


--
-- TOC entry 6634 (class 0 OID 0)
-- Dependencies: 620
-- Name: COLUMN report_offline_details.modified_on; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.report_offline_details.modified_on IS 'Modified on timestamp';


--
-- TOC entry 621 (class 1259 OID 224010)
-- Name: report_offline_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.report_offline_details_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.report_offline_details_id_seq OWNER TO postgres;

--
-- TOC entry 6635 (class 0 OID 0)
-- Dependencies: 621
-- Name: report_offline_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.report_offline_details_id_seq OWNED BY public.report_offline_details.id;


--
-- TOC entry 622 (class 1259 OID 224012)
-- Name: report_parameter_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.report_parameter_master (
    id integer NOT NULL,
    label character varying(200),
    name character varying(200),
    type character varying(200),
    report_master_id integer,
    rpt_data_type character varying(200),
    report_name character varying(200),
    is_query boolean,
    is_required boolean,
    options character varying,
    query character varying,
    default_value character varying,
    implicit_parameter character varying,
    created_on timestamp without time zone NOT NULL,
    modified_on timestamp without time zone,
    created_by integer NOT NULL,
    modified_by integer,
    uuid uuid
);


ALTER TABLE IF EXISTS public.report_parameter_master OWNER TO postgres;

--
-- TOC entry 623 (class 1259 OID 224018)
-- Name: report_parameter_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.report_parameter_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.report_parameter_master_id_seq OWNER TO postgres;

--
-- TOC entry 6636 (class 0 OID 0)
-- Dependencies: 623
-- Name: report_parameter_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.report_parameter_master_id_seq OWNED BY public.report_parameter_master.id;


--
-- TOC entry 624 (class 1259 OID 224020)
-- Name: report_query_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.report_query_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    params character varying(255),
    query text,
    returns_result_set boolean,
    state character varying(255),
    uuid uuid
);


ALTER TABLE IF EXISTS public.report_query_master OWNER TO postgres;

--
-- TOC entry 625 (class 1259 OID 224026)
-- Name: report_query_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.report_query_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.report_query_master_id_seq OWNER TO postgres;

--
-- TOC entry 6637 (class 0 OID 0)
-- Dependencies: 625
-- Name: report_query_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.report_query_master_id_seq OWNED BY public.report_query_master.id;


--
-- TOC entry 626 (class 1259 OID 224028)
-- Name: request_response_details_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.request_response_details_master (
    id integer NOT NULL,
    url integer,
    page_title integer,
    param text,
    body text,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    remote_ip text,
    username text
);


ALTER TABLE IF EXISTS public.request_response_details_master OWNER TO postgres;

--
-- TOC entry 627 (class 1259 OID 224034)
-- Name: request_response_details_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.request_response_details_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.request_response_details_master_id_seq OWNER TO postgres;

--
-- TOC entry 6638 (class 0 OID 0)
-- Dependencies: 627
-- Name: request_response_details_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_response_details_master_id_seq OWNED BY public.request_response_details_master.id;


--
-- TOC entry 628 (class 1259 OID 224042)
-- Name: request_response_navigation_state_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.request_response_navigation_state_mapping (
    id integer NOT NULL,
    navigation_state text NOT NULL
);


ALTER TABLE IF EXISTS public.request_response_navigation_state_mapping OWNER TO postgres;

--
-- TOC entry 629 (class 1259 OID 224048)
-- Name: request_response_navigation_state_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.request_response_navigation_state_mapping_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.request_response_navigation_state_mapping_id_seq OWNER TO postgres;

--
-- TOC entry 6639 (class 0 OID 0)
-- Dependencies: 629
-- Name: request_response_navigation_state_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_response_navigation_state_mapping_id_seq OWNED BY public.request_response_navigation_state_mapping.id;


--
-- TOC entry 630 (class 1259 OID 224058)
-- Name: request_response_page_wise_time_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.request_response_page_wise_time_details (
    id text NOT NULL,
    user_id integer,
    active_tab_time integer,
    total_time integer,
    next_page_id text,
    prev_page_id text,
    created_on timestamp without time zone,
    page_title_id integer
);


ALTER TABLE IF EXISTS public.request_response_page_wise_time_details OWNER TO postgres;

--
-- TOC entry 631 (class 1259 OID 224064)
-- Name: request_response_regex_list_to_be_ignored; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.request_response_regex_list_to_be_ignored (
    id integer NOT NULL,
    regex_pattern text NOT NULL
);


ALTER TABLE IF EXISTS public.request_response_regex_list_to_be_ignored OWNER TO postgres;

--
-- TOC entry 632 (class 1259 OID 224070)
-- Name: request_response_regex_list_to_be_ignored_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.request_response_regex_list_to_be_ignored_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.request_response_regex_list_to_be_ignored_id_seq OWNER TO postgres;

--
-- TOC entry 6640 (class 0 OID 0)
-- Dependencies: 632
-- Name: request_response_regex_list_to_be_ignored_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_response_regex_list_to_be_ignored_id_seq OWNED BY public.request_response_regex_list_to_be_ignored.id;


--
-- TOC entry 633 (class 1259 OID 224072)
-- Name: request_response_url_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.request_response_url_mapping (
    id integer NOT NULL,
    url text NOT NULL
);


ALTER TABLE IF EXISTS public.request_response_url_mapping OWNER TO postgres;

--
-- TOC entry 634 (class 1259 OID 224078)
-- Name: request_response_url_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.request_response_url_mapping_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.request_response_url_mapping_id_seq OWNER TO postgres;

--
-- TOC entry 6641 (class 0 OID 0)
-- Dependencies: 634
-- Name: request_response_url_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_response_url_mapping_id_seq OWNED BY public.request_response_url_mapping.id;


--
-- TOC entry 635 (class 1259 OID 224086)
-- Name: response_analysis_by_time_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.response_analysis_by_time_details (
    id integer NOT NULL,
    requested_time timestamp without time zone NOT NULL,
    requested_url text,
    request_body text,
    request_param text,
    time_taken_in_ms character varying(100)
);


ALTER TABLE IF EXISTS public.response_analysis_by_time_details OWNER TO postgres;

--
-- TOC entry 636 (class 1259 OID 224092)
-- Name: response_analysis_by_time_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.response_analysis_by_time_details_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.response_analysis_by_time_details_id_seq OWNER TO postgres;

--
-- TOC entry 6642 (class 0 OID 0)
-- Dependencies: 636
-- Name: response_analysis_by_time_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.response_analysis_by_time_details_id_seq OWNED BY public.response_analysis_by_time_details.id;


--
-- TOC entry 637 (class 1259 OID 224094)
-- Name: role_health_infrastructure_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.role_health_infrastructure_type (
    role_id integer,
    health_infrastructure__type_id integer,
    id integer NOT NULL,
    state character varying(255),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.role_health_infrastructure_type OWNER TO postgres;

--
-- TOC entry 638 (class 1259 OID 224097)
-- Name: role_health_infrastructure_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.role_health_infrastructure_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.role_health_infrastructure_id_seq OWNER TO postgres;

--
-- TOC entry 6643 (class 0 OID 0)
-- Dependencies: 638
-- Name: role_health_infrastructure_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_health_infrastructure_id_seq OWNED BY public.role_health_infrastructure_type.id;


--
-- TOC entry 639 (class 1259 OID 224099)
-- Name: role_hierarchy_management; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.role_hierarchy_management (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    role_id integer,
    user_id integer,
    location_type character varying(255),
    hierarchy_id integer,
    state character varying(255),
    level integer
);


ALTER TABLE IF EXISTS public.role_hierarchy_management OWNER TO postgres;

--
-- TOC entry 640 (class 1259 OID 224105)
-- Name: role_hierarchy_management_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.role_hierarchy_management_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.role_hierarchy_management_id_seq OWNER TO postgres;

--
-- TOC entry 6644 (class 0 OID 0)
-- Dependencies: 640
-- Name: role_hierarchy_management_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_hierarchy_management_id_seq OWNED BY public.role_hierarchy_management.id;


--
-- TOC entry 641 (class 1259 OID 224107)
-- Name: role_management; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.role_management (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    role_id integer,
    managed_by_user_id integer,
    managed_by_role_id integer,
    state character varying(255)
);


ALTER TABLE IF EXISTS public.role_management OWNER TO postgres;

--
-- TOC entry 642 (class 1259 OID 224110)
-- Name: role_management_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.role_management_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.role_management_id_seq OWNER TO postgres;

--
-- TOC entry 6645 (class 0 OID 0)
-- Dependencies: 642
-- Name: role_management_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_management_id_seq OWNED BY public.role_management.id;


--
-- TOC entry 643 (class 1259 OID 224119)
-- Name: school_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.school_master (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    grant_type integer NOT NULL,
    school_type integer,
    is_primary_school boolean,
    is_higher_secondary_school boolean,
    is_madresa boolean,
    is_gurukul boolean,
    no_of_teachers integer,
    principal_name character varying(255),
    contact_person_name character varying(255) NOT NULL,
    contact_number character varying(21) NOT NULL,
    child_male_1_to_5 integer,
    child_female_1_to_5 integer,
    child_male_6_to_8 integer,
    child_female_6_to_8 integer,
    child_male_9_to_10 integer,
    child_female_9_to_10 integer,
    child_male_11_to_12 integer,
    child_female_11_to_12 integer,
    rbsk_team_id character varying(255),
    location_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    english_name character varying(255),
    is_pre_primary_school boolean,
    is_other boolean,
    other_school_type integer
);


ALTER TABLE IF EXISTS public.school_master OWNER TO postgres;

--
-- TOC entry 644 (class 1259 OID 224125)
-- Name: school_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.school_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.school_master_id_seq OWNER TO postgres;

--
-- TOC entry 6646 (class 0 OID 0)
-- Dependencies: 644
-- Name: school_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.school_master_id_seq OWNED BY public.school_master.id;


--
-- TOC entry 645 (class 1259 OID 224127)
-- Name: sd_score_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sd_score_master (
    height integer NOT NULL,
    minus4 numeric(6,2),
    minus3 numeric(6,2),
    minus2 numeric(6,2),
    minus1 numeric(6,2),
    median numeric(6,2),
    gender text NOT NULL
);


ALTER TABLE IF EXISTS public.sd_score_master OWNER TO postgres;

--
-- TOC entry 646 (class 1259 OID 224133)
-- Name: server_list_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.server_list_master (
    id integer NOT NULL,
    server_name text NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    host_url text NOT NULL,
    is_active boolean
);


ALTER TABLE IF EXISTS public.server_list_master OWNER TO postgres;

--
-- TOC entry 647 (class 1259 OID 224139)
-- Name: server_list_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.server_list_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.server_list_master_id_seq OWNER TO postgres;

--
-- TOC entry 6647 (class 0 OID 0)
-- Dependencies: 647
-- Name: server_list_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.server_list_master_id_seq OWNED BY public.server_list_master.id;


--
-- TOC entry 648 (class 1259 OID 224141)
-- Name: sickle_cell_screening; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sickle_cell_screening (
    id integer NOT NULL,
    member_id integer NOT NULL,
    location_id integer NOT NULL,
    anemia_test_done boolean NOT NULL,
    dtt_test_result character varying(10),
    hplc_test_done boolean,
    hplc_test_result character varying(100),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.sickle_cell_screening OWNER TO postgres;

--
-- TOC entry 649 (class 1259 OID 224144)
-- Name: sickle_cell_screening_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.sickle_cell_screening_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.sickle_cell_screening_id_seq OWNER TO postgres;

--
-- TOC entry 6648 (class 0 OID 0)
-- Dependencies: 649
-- Name: sickle_cell_screening_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sickle_cell_screening_id_seq OWNED BY public.sickle_cell_screening.id;


--
-- TOC entry 650 (class 1259 OID 224146)
-- Name: sms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sms (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    mobile_numbers text,
    message text,
    response text,
    message_type text,
    status text,
    exception_string text,
    response_id text,
    carrier_status text
);


ALTER TABLE IF EXISTS public.sms OWNER TO postgres;

--
-- TOC entry 651 (class 1259 OID 224152)
-- Name: sms_block_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sms_block_master (
    id integer NOT NULL,
    mobile_number text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    member_id integer NOT NULL,
    status character varying(20) DEFAULT 'ACTIVE'::character varying NOT NULL,
    active_remark text,
    inactive_remark text
);


ALTER TABLE IF EXISTS public.sms_block_master OWNER TO postgres;

--
-- TOC entry 652 (class 1259 OID 224159)
-- Name: sms_block_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.sms_block_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.sms_block_master_id_seq OWNER TO postgres;

--
-- TOC entry 6649 (class 0 OID 0)
-- Dependencies: 652
-- Name: sms_block_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_block_master_id_seq OWNED BY public.sms_block_master.id;


--
-- TOC entry 653 (class 1259 OID 224161)
-- Name: sms_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.sms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.sms_id_seq OWNER TO postgres;

--
-- TOC entry 6650 (class 0 OID 0)
-- Dependencies: 653
-- Name: sms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_id_seq OWNED BY public.sms.id;


--
-- TOC entry 654 (class 1259 OID 224163)
-- Name: sms_queue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sms_queue (
    id integer NOT NULL,
    mobile_number text,
    message text,
    message_type text,
    status text,
    is_processed boolean,
    is_sent boolean,
    processed_on timestamp without time zone,
    created_on timestamp without time zone,
    completed_on timestamp without time zone,
    exception_string text,
    sms_id integer
);


ALTER TABLE IF EXISTS public.sms_queue OWNER TO postgres;

--
-- TOC entry 655 (class 1259 OID 224169)
-- Name: sms_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.sms_queue_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.sms_queue_id_seq OWNER TO postgres;

--
-- TOC entry 6651 (class 0 OID 0)
-- Dependencies: 655
-- Name: sms_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_queue_id_seq OWNED BY public.sms_queue.id;


--
-- TOC entry 656 (class 1259 OID 224171)
-- Name: sms_response; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sms_response (
    a2wackid text NOT NULL,
    a2wstatus text,
    carrierstatus text,
    lastutime text,
    custref text,
    submitdt text,
    mnumber text,
    acode text,
    senderid text,
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.sms_response OWNER TO postgres;

--
-- TOC entry 777 (class 1259 OID 232170)
-- Name: soh_element_module_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.soh_element_module_master (
    id integer NOT NULL,
    module character varying(255),
    module_name character varying(255),
    is_public boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    module_order integer,
    state character varying(50),
    footer_description character varying(255)
);


ALTER TABLE IF EXISTS public.soh_element_module_master OWNER TO postgres;

--
-- TOC entry 776 (class 1259 OID 232168)
-- Name: soh_element_module_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.soh_element_module_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.soh_element_module_master_id_seq OWNER TO postgres;

--
-- TOC entry 6652 (class 0 OID 0)
-- Dependencies: 776
-- Name: soh_element_module_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.soh_element_module_master_id_seq OWNED BY public.soh_element_module_master.id;


--
-- TOC entry 657 (class 1259 OID 224292)
-- Name: sync_server_feature_mapping_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sync_server_feature_mapping_detail (
    server_id integer NOT NULL,
    feature_uuid uuid NOT NULL,
    is_in_sync boolean
);


ALTER TABLE IF EXISTS public.sync_server_feature_mapping_detail OWNER TO postgres;

--
-- TOC entry 658 (class 1259 OID 224295)
-- Name: sync_system_configuration_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sync_system_configuration_master (
    id integer NOT NULL,
    feature_type text NOT NULL,
    config_json text,
    created_on timestamp without time zone,
    created_by integer,
    feature_uuid uuid,
    feature_name text
);


ALTER TABLE IF EXISTS public.sync_system_configuration_master OWNER TO postgres;

--
-- TOC entry 659 (class 1259 OID 224301)
-- Name: sync_system_configuration_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.sync_system_configuration_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.sync_system_configuration_master_id_seq OWNER TO postgres;

--
-- TOC entry 6653 (class 0 OID 0)
-- Dependencies: 659
-- Name: sync_system_configuration_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sync_system_configuration_master_id_seq OWNED BY public.sync_system_configuration_master.id;


--
-- TOC entry 660 (class 1259 OID 224303)
-- Name: sync_system_configuration_server_access_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.sync_system_configuration_server_access_details (
    id integer NOT NULL,
    config_id integer,
    is_in_sync boolean,
    server_id integer
);


ALTER TABLE IF EXISTS public.sync_system_configuration_server_access_details OWNER TO postgres;

--
-- TOC entry 661 (class 1259 OID 224306)
-- Name: system_build_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_build_history (
    id integer NOT NULL,
    server_start_date timestamp without time zone NOT NULL,
    build_date timestamp without time zone NOT NULL,
    build_version integer,
    maven_version character varying
);


ALTER TABLE IF EXISTS public.system_build_history OWNER TO postgres;

--
-- TOC entry 662 (class 1259 OID 224312)
-- Name: system_build_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.system_build_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.system_build_history_id_seq OWNER TO postgres;

--
-- TOC entry 6654 (class 0 OID 0)
-- Dependencies: 662
-- Name: system_build_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.system_build_history_id_seq OWNED BY public.system_build_history.id;


--
-- TOC entry 663 (class 1259 OID 224314)
-- Name: system_code_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_code_master (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    table_id integer NOT NULL,
    table_type character varying(255) NOT NULL,
    code_type character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    parent_code character varying(255),
    description character varying(255),
    created_by integer NOT NULL,
    created_on timestamp without time zone DEFAULT now() NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone DEFAULT now()
);


ALTER TABLE IF EXISTS public.system_code_master OWNER TO postgres;

--
-- TOC entry 6655 (class 0 OID 0)
-- Dependencies: 663
-- Name: TABLE system_code_master; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.system_code_master IS 'This is master table for code and its management like ICD-10,SNOMED CT,etc';


--
-- TOC entry 6656 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.table_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.table_id IS 'Id of related table';


--
-- TOC entry 6657 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.table_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.table_type IS 'Type of related table';


--
-- TOC entry 6658 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.code_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.code_type IS 'Tyoe of code like SNOMED CT,ICD-10';


--
-- TOC entry 6659 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.code IS 'code like SNOMED CT concept id,ICD-10 id';


--
-- TOC entry 6660 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.parent_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.parent_code IS 'Parent code like SNOMED CT concept id,ICD-10 id';


--
-- TOC entry 6661 (class 0 OID 0)
-- Dependencies: 663
-- Name: COLUMN system_code_master.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_code_master.description IS 'Description of entity';


--
-- TOC entry 664 (class 1259 OID 224332)
-- Name: system_config_sync_access_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.system_config_sync_access_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.system_config_sync_access_id_seq OWNER TO postgres;

--
-- TOC entry 6662 (class 0 OID 0)
-- Dependencies: 664
-- Name: system_config_sync_access_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.system_config_sync_access_id_seq OWNED BY public.sync_system_configuration_server_access_details.id;


--
-- TOC entry 665 (class 1259 OID 224334)
-- Name: system_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_configuration (
    system_key character varying(100) NOT NULL,
    is_active boolean,
    key_value text NOT NULL
);


ALTER TABLE IF EXISTS public.system_configuration OWNER TO postgres;

--
-- TOC entry 666 (class 1259 OID 224346)
-- Name: system_constraint_field_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_field_master (
    uuid uuid NOT NULL,
    form_master_uuid uuid NOT NULL,
    field_key character varying(255) NOT NULL,
    field_name character varying(255) NOT NULL,
    field_type character varying(255) NOT NULL,
    ng_model text,
    app_name character varying(50),
    standard_field_master_uuid uuid,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_field_master OWNER TO postgres;

--
-- TOC entry 667 (class 1259 OID 224352)
-- Name: system_constraint_field_value_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_field_value_master (
    uuid uuid NOT NULL,
    field_master_uuid uuid NOT NULL,
    value_type character varying(255) NOT NULL,
    key character varying(255) NOT NULL,
    value text,
    default_value text NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_field_value_master OWNER TO postgres;

--
-- TOC entry 668 (class 1259 OID 224358)
-- Name: system_constraint_form_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_form_master (
    uuid uuid NOT NULL,
    form_name character varying(255) NOT NULL,
    form_code character varying(50) NOT NULL,
    menu_config_id integer,
    web_template_config text,
    web_state character varying(50) DEFAULT 'ACTIVE'::character varying NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    mobile_state character varying(50)
);


ALTER TABLE IF EXISTS public.system_constraint_form_master OWNER TO postgres;

--
-- TOC entry 669 (class 1259 OID 224365)
-- Name: system_constraint_form_version; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_form_version (
    id integer NOT NULL,
    form_master_uuid uuid NOT NULL,
    template_config text,
    version integer,
    type text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_form_version OWNER TO postgres;

--
-- TOC entry 670 (class 1259 OID 224371)
-- Name: system_constraint_form_version_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.system_constraint_form_version_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.system_constraint_form_version_id_seq OWNER TO postgres;

--
-- TOC entry 6663 (class 0 OID 0)
-- Dependencies: 670
-- Name: system_constraint_form_version_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.system_constraint_form_version_id_seq OWNED BY public.system_constraint_form_version.id;


--
-- TOC entry 671 (class 1259 OID 224373)
-- Name: system_constraint_standard_field_mapping_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_standard_field_mapping_master (
    uuid uuid NOT NULL,
    standard_master_id integer NOT NULL,
    standard_field_master_uuid uuid NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_standard_field_mapping_master OWNER TO postgres;

--
-- TOC entry 672 (class 1259 OID 224376)
-- Name: system_constraint_standard_field_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_standard_field_master (
    uuid uuid NOT NULL,
    field_key character varying(255) NOT NULL,
    field_name character varying(255) NOT NULL,
    field_type character varying(255) NOT NULL,
    app_name character varying(50),
    category_id integer,
    state character varying(50) DEFAULT 'ACTIVE'::character varying NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_standard_field_master OWNER TO postgres;

--
-- TOC entry 673 (class 1259 OID 224383)
-- Name: system_constraint_standard_field_value_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_constraint_standard_field_value_master (
    uuid uuid NOT NULL,
    standard_field_mapping_master_uuid uuid NOT NULL,
    value_type character varying(255) NOT NULL,
    key character varying(255) NOT NULL,
    value text,
    default_value text NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_constraint_standard_field_value_master OWNER TO postgres;

--
-- TOC entry 674 (class 1259 OID 224405)
-- Name: system_function_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_function_master (
    id integer NOT NULL,
    name character varying(500) NOT NULL,
    class_name character varying(500) NOT NULL,
    description character varying(1000),
    parameters text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.system_function_master OWNER TO postgres;

--
-- TOC entry 675 (class 1259 OID 224411)
-- Name: system_function_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.system_function_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.system_function_master_id_seq OWNER TO postgres;

--
-- TOC entry 6664 (class 0 OID 0)
-- Dependencies: 675
-- Name: system_function_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.system_function_master_id_seq OWNED BY public.system_function_master.id;


--
-- TOC entry 676 (class 1259 OID 224413)
-- Name: system_sync_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.system_sync_status (
    id text NOT NULL,
    action_date timestamp without time zone,
    relative_id integer,
    status text,
    record_string text,
    mobile_date timestamp without time zone,
    user_id integer,
    device text,
    client_id integer,
    lastmodified_by integer,
    lastmodified_date timestamp without time zone,
    duration_of_processing integer,
    error_message text,
    exception text,
    mail_sent boolean
);


ALTER TABLE IF EXISTS public.system_sync_status OWNER TO postgres;

--
-- TOC entry 677 (class 1259 OID 224469)
-- Name: techo_notification_location_change_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_notification_location_change_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_notification_location_change_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 678 (class 1259 OID 224471)
-- Name: techo_notification_location_change_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_notification_location_change_detail (
    id integer DEFAULT nextval('public.techo_notification_location_change_detail_id_seq1'::regclass) NOT NULL,
    notification_id integer,
    from_location_id integer,
    to_location_id integer,
    created_by integer,
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.techo_notification_location_change_detail OWNER TO postgres;

--
-- TOC entry 679 (class 1259 OID 224475)
-- Name: techo_notification_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_notification_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_notification_master_id_seq OWNER TO postgres;

--
-- TOC entry 680 (class 1259 OID 224477)
-- Name: techo_notification_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_notification_master (
    id integer DEFAULT nextval('public.techo_notification_master_id_seq'::regclass) NOT NULL,
    notification_type_id integer,
    notification_code character varying(100),
    location_id integer,
    user_id integer,
    family_id integer,
    member_id integer,
    schedule_date timestamp without time zone,
    due_on timestamp without time zone,
    expiry_date timestamp without time zone,
    action_by integer,
    state character varying(100),
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    ref_code integer,
    other_details text,
    migration_id integer,
    related_id integer,
    header text
);


ALTER TABLE IF EXISTS public.techo_notification_master OWNER TO postgres;

--
-- TOC entry 681 (class 1259 OID 224487)
-- Name: techo_notification_state_detail_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_notification_state_detail_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_notification_state_detail_id_seq1 OWNER TO postgres;

--
-- TOC entry 682 (class 1259 OID 224489)
-- Name: techo_notification_state_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_notification_state_detail (
    id integer DEFAULT nextval('public.techo_notification_state_detail_id_seq1'::regclass) NOT NULL,
    notification_id integer,
    from_state character varying(100),
    to_state character varying(100),
    from_schedule_date timestamp without time zone,
    to_schedule_date timestamp without time zone,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.techo_notification_state_detail OWNER TO postgres;

--
-- TOC entry 683 (class 1259 OID 224493)
-- Name: techo_push_notification_config_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_push_notification_config_master (
    id integer NOT NULL,
    name character varying(200) NOT NULL,
    notification_type_id integer,
    description character varying(1000),
    config_type text NOT NULL,
    trigger_type text NOT NULL,
    status text NOT NULL,
    state text NOT NULL,
    schedule_date_time timestamp without time zone,
    query_uuid uuid,
    created_on timestamp without time zone,
    modified_on timestamp without time zone,
    created_by integer,
    modified_by integer
);


ALTER TABLE IF EXISTS public.techo_push_notification_config_master OWNER TO postgres;

--
-- TOC entry 684 (class 1259 OID 224499)
-- Name: techo_push_notification_config_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_push_notification_config_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_push_notification_config_master_id_seq OWNER TO postgres;

--
-- TOC entry 6665 (class 0 OID 0)
-- Dependencies: 684
-- Name: techo_push_notification_config_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.techo_push_notification_config_master_id_seq OWNED BY public.techo_push_notification_config_master.id;


--
-- TOC entry 685 (class 1259 OID 224501)
-- Name: techo_push_notification_location_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_push_notification_location_detail (
    id integer NOT NULL,
    push_config_id integer NOT NULL,
    location_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.techo_push_notification_location_detail OWNER TO postgres;

--
-- TOC entry 686 (class 1259 OID 224504)
-- Name: techo_push_notification_location_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_push_notification_location_detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_push_notification_location_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6666 (class 0 OID 0)
-- Dependencies: 686
-- Name: techo_push_notification_location_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.techo_push_notification_location_detail_id_seq OWNED BY public.techo_push_notification_location_detail.id;


--
-- TOC entry 687 (class 1259 OID 224506)
-- Name: techo_push_notification_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_push_notification_master (
    id bigint NOT NULL,
    user_id integer,
    type text,
    response text,
    exception text,
    is_sent boolean DEFAULT false,
    is_processed boolean DEFAULT false,
    completed_on timestamp without time zone,
    processed_on timestamp without time zone,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    event_id character varying(255),
    message text,
    heading text
);


ALTER TABLE IF EXISTS public.techo_push_notification_master OWNER TO postgres;

--
-- TOC entry 688 (class 1259 OID 224514)
-- Name: techo_push_notification_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_push_notification_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_push_notification_master_id_seq OWNER TO postgres;

--
-- TOC entry 6667 (class 0 OID 0)
-- Dependencies: 688
-- Name: techo_push_notification_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.techo_push_notification_master_id_seq OWNED BY public.techo_push_notification_master.id;


--
-- TOC entry 689 (class 1259 OID 224516)
-- Name: techo_push_notification_role_user_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_push_notification_role_user_detail (
    id integer NOT NULL,
    push_config_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.techo_push_notification_role_user_detail OWNER TO postgres;

--
-- TOC entry 690 (class 1259 OID 224519)
-- Name: techo_push_notification_role_user_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_push_notification_role_user_detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_push_notification_role_user_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6668 (class 0 OID 0)
-- Dependencies: 690
-- Name: techo_push_notification_role_user_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.techo_push_notification_role_user_detail_id_seq OWNED BY public.techo_push_notification_role_user_detail.id;


--
-- TOC entry 691 (class 1259 OID 224521)
-- Name: techo_push_notification_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_push_notification_type (
    id integer NOT NULL,
    type text NOT NULL,
    message text,
    heading text,
    description text,
    is_active boolean DEFAULT true,
    media_id integer,
    created_on timestamp without time zone,
    modified_on timestamp without time zone,
    created_by integer,
    modified_by integer
);


ALTER TABLE IF EXISTS public.techo_push_notification_type OWNER TO postgres;

--
-- TOC entry 692 (class 1259 OID 224528)
-- Name: techo_push_notification_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_push_notification_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_push_notification_type_id_seq OWNER TO postgres;

--
-- TOC entry 6669 (class 0 OID 0)
-- Dependencies: 692
-- Name: techo_push_notification_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.techo_push_notification_type_id_seq OWNED BY public.techo_push_notification_type.id;


--
-- TOC entry 693 (class 1259 OID 224530)
-- Name: techo_web_notification_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_web_notification_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_web_notification_master_id_seq OWNER TO postgres;

--
-- TOC entry 694 (class 1259 OID 224532)
-- Name: techo_web_notification_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_web_notification_master (
    id integer DEFAULT nextval('public.techo_web_notification_master_id_seq'::regclass) NOT NULL,
    notification_type_id integer NOT NULL,
    location_id integer,
    user_id integer,
    family_id integer,
    member_id integer,
    escalation_level_id integer NOT NULL,
    schedule_date timestamp without time zone NOT NULL,
    due_on timestamp without time zone,
    expiry_date timestamp without time zone,
    action_by integer,
    state character varying(100),
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    ref_code integer,
    other_details text,
    notification_type_escalation_id text,
    action_taken text,
    related_notification_id integer
);


ALTER TABLE IF EXISTS public.techo_web_notification_master OWNER TO postgres;

--
-- TOC entry 695 (class 1259 OID 224539)
-- Name: techo_web_notification_response_det_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.techo_web_notification_response_det_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.techo_web_notification_response_det_id_seq OWNER TO postgres;

--
-- TOC entry 696 (class 1259 OID 224541)
-- Name: techo_web_notification_response_det; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.techo_web_notification_response_det (
    id integer DEFAULT nextval('public.techo_web_notification_response_det_id_seq'::regclass) NOT NULL,
    notification_type_id integer NOT NULL,
    notification_id integer NOT NULL,
    location_id integer,
    schedule_date timestamp without time zone NOT NULL,
    due_on timestamp without time zone,
    expiry_date timestamp without time zone,
    from_state character varying(100),
    to_state character varying(100),
    ref_code integer,
    other_details text,
    notification_type_escalation_id text,
    action_taken text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.techo_web_notification_response_det OWNER TO postgres;

--
-- TOC entry 697 (class 1259 OID 224554)
-- Name: timer_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.timer_event (
    id integer NOT NULL,
    ref_id integer,
    event_config_id integer,
    processed boolean,
    status character varying(20),
    type character varying(50),
    system_trigger_on timestamp without time zone,
    processed_on timestamp without time zone,
    notification_config_id character varying(100),
    notification_base_date timestamp without time zone,
    json_data text,
    completed_on timestamp without time zone,
    exception_string text
);


ALTER TABLE IF EXISTS public.timer_event OWNER TO postgres;

--
-- TOC entry 698 (class 1259 OID 224568)
-- Name: timer_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.timer_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.timer_event_id_seq OWNER TO postgres;

--
-- TOC entry 6670 (class 0 OID 0)
-- Dependencies: 698
-- Name: timer_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.timer_event_id_seq OWNED BY public.timer_event.id;


--
-- TOC entry 699 (class 1259 OID 224572)
-- Name: tp_api_access_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tp_api_access_log (
    id integer NOT NULL,
    req_body text,
    req_param text,
    res_body text,
    req_time timestamp without time zone,
    req_state text,
    req_error text,
    tp_type text,
    req_remote_ip text
);


ALTER TABLE IF EXISTS public.tp_api_access_log OWNER TO postgres;

--
-- TOC entry 700 (class 1259 OID 224578)
-- Name: tp_api_access_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tp_api_access_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tp_api_access_log_id_seq OWNER TO postgres;

--
-- TOC entry 6671 (class 0 OID 0)
-- Dependencies: 700
-- Name: tp_api_access_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tp_api_access_log_id_seq OWNED BY public.tp_api_access_log.id;


--
-- TOC entry 701 (class 1259 OID 224580)
-- Name: tr_attendance_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_attendance_master (
    attendance_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    completed_on timestamp without time zone,
    descr character varying(255),
    effective_date timestamp without time zone,
    expiration_date timestamp without time zone,
    is_present boolean,
    name character varying(255),
    reason character varying(255),
    remarks character varying(255),
    state character varying(255),
    type character varying(255),
    training_id integer,
    user_id integer
);


ALTER TABLE IF EXISTS public.tr_attendance_master OWNER TO postgres;

--
-- TOC entry 702 (class 1259 OID 224586)
-- Name: tr_attendance_master_attendance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_attendance_master_attendance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_attendance_master_attendance_id_seq OWNER TO postgres;

--
-- TOC entry 6672 (class 0 OID 0)
-- Dependencies: 702
-- Name: tr_attendance_master_attendance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_attendance_master_attendance_id_seq OWNED BY public.tr_attendance_master.attendance_id;


--
-- TOC entry 703 (class 1259 OID 224588)
-- Name: tr_attendance_topic_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_attendance_topic_rel (
    attendance_id integer NOT NULL,
    topic_id integer
);


ALTER TABLE IF EXISTS public.tr_attendance_topic_rel OWNER TO postgres;

--
-- TOC entry 704 (class 1259 OID 224591)
-- Name: tr_certificate_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_certificate_master (
    certificate_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    certificate_descr character varying(255),
    certificate_name character varying(255),
    remarks character varying(255),
    certificate_state character varying(255),
    certificate_type character varying(255),
    certification_on timestamp without time zone,
    course_id integer,
    grade_type character varying(255),
    training_id integer,
    user_id integer
);


ALTER TABLE IF EXISTS public.tr_certificate_master OWNER TO postgres;

--
-- TOC entry 705 (class 1259 OID 224597)
-- Name: tr_certificate_master_certificate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_certificate_master_certificate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_certificate_master_certificate_id_seq OWNER TO postgres;

--
-- TOC entry 6673 (class 0 OID 0)
-- Dependencies: 705
-- Name: tr_certificate_master_certificate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_certificate_master_certificate_id_seq OWNED BY public.tr_certificate_master.certificate_id;


--
-- TOC entry 706 (class 1259 OID 224599)
-- Name: tr_course_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_course_master (
    course_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    course_description text,
    course_name character varying(255) NOT NULL,
    course_state character varying(255) NOT NULL,
    module_id integer,
    course_type character varying(250) NOT NULL,
    test_config_json text,
    estimated_time_in_hrs integer,
    course_image_json text,
    is_allowed_to_skip_lessons boolean DEFAULT false
);


ALTER TABLE IF EXISTS public.tr_course_master OWNER TO postgres;

--
-- TOC entry 707 (class 1259 OID 224606)
-- Name: tr_course_master_course_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_course_master_course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_course_master_course_id_seq OWNER TO postgres;

--
-- TOC entry 6674 (class 0 OID 0)
-- Dependencies: 707
-- Name: tr_course_master_course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_course_master_course_id_seq OWNED BY public.tr_course_master.course_id;


--
-- TOC entry 708 (class 1259 OID 224608)
-- Name: tr_course_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_course_role_rel (
    course_id integer NOT NULL,
    role_id integer
);


ALTER TABLE IF EXISTS public.tr_course_role_rel OWNER TO postgres;

--
-- TOC entry 709 (class 1259 OID 224611)
-- Name: tr_course_topic_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_course_topic_rel (
    course_id integer,
    topic_id integer
);


ALTER TABLE IF EXISTS public.tr_course_topic_rel OWNER TO postgres;

--
-- TOC entry 710 (class 1259 OID 224614)
-- Name: tr_course_trainer_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_course_trainer_role_rel (
    course_id integer NOT NULL,
    trainer_role_id integer
);


ALTER TABLE IF EXISTS public.tr_course_trainer_role_rel OWNER TO postgres;

--
-- TOC entry 711 (class 1259 OID 224617)
-- Name: tr_course_wise_count_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_course_wise_count_analytics (
    user_id integer,
    course_id integer,
    total_lessons integer,
    lessons_completed integer,
    course_progress integer,
    total_sessions integer,
    time_spent_on_course_content interval,
    time_spent_on_quiz interval,
    time_spent_on_course interval,
    is_activity_completed_on_time boolean,
    frequently_watched_lessons text,
    total_quiz integer,
    quiz_attempted integer,
    total_quiz_sessions integer,
    quiz_passing_rate integer,
    average_marks numeric(5,2),
    average_time_for_quiz_completion interval
);


ALTER TABLE IF EXISTS public.tr_course_wise_count_analytics OWNER TO postgres;

--
-- TOC entry 712 (class 1259 OID 224623)
-- Name: tr_lesson_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_lesson_analytics (
    id integer NOT NULL,
    user_id integer,
    lesson_id integer,
    module_id integer,
    course_id integer,
    is_completed boolean,
    spent_time interval,
    started_on timestamp without time zone,
    ended_on timestamp without time zone,
    time_to_complete_lesson interval
);


ALTER TABLE IF EXISTS public.tr_lesson_analytics OWNER TO postgres;

--
-- TOC entry 713 (class 1259 OID 224626)
-- Name: tr_lesson_analytics_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_lesson_analytics_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_lesson_analytics_id_seq OWNER TO postgres;

--
-- TOC entry 6675 (class 0 OID 0)
-- Dependencies: 713
-- Name: tr_lesson_analytics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_lesson_analytics_id_seq OWNED BY public.tr_lesson_analytics.id;


--
-- TOC entry 714 (class 1259 OID 224628)
-- Name: tr_mobile_event_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_mobile_event_master (
    checksum text NOT NULL,
    user_id integer,
    mobile_date timestamp without time zone,
    event_type text,
    event_data text,
    status text,
    action_date timestamp without time zone,
    error_message text,
    exception text,
    mail_sent boolean
);


ALTER TABLE IF EXISTS public.tr_mobile_event_master OWNER TO postgres;

--
-- TOC entry 715 (class 1259 OID 224634)
-- Name: tr_question_bank_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_question_bank_configuration (
    id integer NOT NULL,
    question_set_id integer NOT NULL,
    config_json text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE IF EXISTS public.tr_question_bank_configuration OWNER TO postgres;

--
-- TOC entry 716 (class 1259 OID 224640)
-- Name: tr_question_bank_configuration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_question_bank_configuration_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_question_bank_configuration_id_seq OWNER TO postgres;

--
-- TOC entry 6676 (class 0 OID 0)
-- Dependencies: 716
-- Name: tr_question_bank_configuration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_question_bank_configuration_id_seq OWNED BY public.tr_question_bank_configuration.id;


--
-- TOC entry 717 (class 1259 OID 224642)
-- Name: tr_question_set_answer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_question_set_answer (
    id integer NOT NULL,
    user_id integer,
    question_set_id integer,
    marks_scored integer,
    passing_marks integer,
    is_passed boolean,
    answer_json text,
    created_on timestamp without time zone,
    created_by integer,
    modified_on timestamp without time zone,
    modified_by integer,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    is_locked boolean
);


ALTER TABLE IF EXISTS public.tr_question_set_answer OWNER TO postgres;

--
-- TOC entry 718 (class 1259 OID 224648)
-- Name: tr_question_set_answer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_question_set_answer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_question_set_answer_id_seq OWNER TO postgres;

--
-- TOC entry 6677 (class 0 OID 0)
-- Dependencies: 718
-- Name: tr_question_set_answer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_question_set_answer_id_seq OWNED BY public.tr_question_set_answer.id;


--
-- TOC entry 719 (class 1259 OID 224650)
-- Name: tr_question_set_configuration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_question_set_configuration (
    id integer NOT NULL,
    ref_id integer NOT NULL,
    ref_type character varying(255) NOT NULL,
    question_set_name character varying(255),
    status character varying(255),
    minimum_marks integer,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    question_set_type integer,
    course_id integer,
    total_marks integer,
    quiz_at_second integer
);


ALTER TABLE IF EXISTS public.tr_question_set_configuration OWNER TO postgres;

--
-- TOC entry 720 (class 1259 OID 224656)
-- Name: tr_question_set_configuration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_question_set_configuration_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_question_set_configuration_id_seq OWNER TO postgres;

--
-- TOC entry 6678 (class 0 OID 0)
-- Dependencies: 720
-- Name: tr_question_set_configuration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_question_set_configuration_id_seq OWNED BY public.tr_question_set_configuration.id;


--
-- TOC entry 721 (class 1259 OID 224658)
-- Name: tr_session_analytics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_session_analytics (
    user_id integer,
    lesson_id integer,
    started_on timestamp without time zone,
    ended_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.tr_session_analytics OWNER TO postgres;

--
-- TOC entry 722 (class 1259 OID 224661)
-- Name: tr_topic_coverage_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_topic_coverage_master (
    id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    course_id integer,
    completed_on timestamp without time zone,
    descr text,
    effective_date timestamp without time zone,
    expiration_date timestamp without time zone,
    name character varying(255),
    reason character varying(255),
    remarks character varying(255),
    state character varying(255),
    submitted_on timestamp without time zone,
    topic_id integer,
    training_id integer
);


ALTER TABLE IF EXISTS public.tr_topic_coverage_master OWNER TO postgres;

--
-- TOC entry 723 (class 1259 OID 224667)
-- Name: tr_topic_coverage_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_topic_coverage_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_topic_coverage_master_id_seq OWNER TO postgres;

--
-- TOC entry 6679 (class 0 OID 0)
-- Dependencies: 723
-- Name: tr_topic_coverage_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_topic_coverage_master_id_seq OWNED BY public.tr_topic_coverage_master.id;


--
-- TOC entry 724 (class 1259 OID 224669)
-- Name: tr_topic_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_topic_master (
    topic_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    topic_description text,
    topic_name character varying(255) NOT NULL,
    topic_order character varying(255),
    topic_state character varying(255),
    day integer
);


ALTER TABLE IF EXISTS public.tr_topic_master OWNER TO postgres;

--
-- TOC entry 725 (class 1259 OID 224675)
-- Name: tr_topic_master_topic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_topic_master_topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_topic_master_topic_id_seq OWNER TO postgres;

--
-- TOC entry 6680 (class 0 OID 0)
-- Dependencies: 725
-- Name: tr_topic_master_topic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_topic_master_topic_id_seq OWNED BY public.tr_topic_master.topic_id;


--
-- TOC entry 726 (class 1259 OID 224677)
-- Name: tr_topic_media_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_topic_media_master (
    id integer NOT NULL,
    topic_id integer NOT NULL,
    media_id bigint,
    media_file_name character varying(1000),
    url character varying(1000),
    title character varying(1000),
    description text,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    media_type character varying(250),
    media_order integer,
    transcript_file_id bigint,
    transcript_file_name character varying(250),
    media_state character varying(255),
    is_user_feedback_required boolean,
    size bigint
);


ALTER TABLE IF EXISTS public.tr_topic_media_master OWNER TO postgres;

--
-- TOC entry 727 (class 1259 OID 224683)
-- Name: tr_topic_media_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_topic_media_master_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_topic_media_master_id_seq OWNER TO postgres;

--
-- TOC entry 6681 (class 0 OID 0)
-- Dependencies: 727
-- Name: tr_topic_media_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_topic_media_master_id_seq OWNED BY public.tr_topic_media_master.id;


--
-- TOC entry 728 (class 1259 OID 224685)
-- Name: tr_training_additional_attendee_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_additional_attendee_rel (
    training_id integer NOT NULL,
    additional_attendee_id integer
);


ALTER TABLE IF EXISTS public.tr_training_additional_attendee_rel OWNER TO postgres;

--
-- TOC entry 729 (class 1259 OID 224688)
-- Name: tr_training_attendee_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_attendee_rel (
    training_id integer NOT NULL,
    attendee_id integer
);


ALTER TABLE IF EXISTS public.tr_training_attendee_rel OWNER TO postgres;

--
-- TOC entry 730 (class 1259 OID 224691)
-- Name: tr_training_course_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_course_rel (
    training_id integer NOT NULL,
    course_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_course_rel OWNER TO postgres;

--
-- TOC entry 731 (class 1259 OID 224694)
-- Name: tr_training_master; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_master (
    training_id integer NOT NULL,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by integer,
    modified_on timestamp without time zone,
    training_descr text,
    effective_date timestamp without time zone NOT NULL,
    expiration_date timestamp without time zone,
    location_name character varying(255),
    training_name character varying(255) NOT NULL,
    training_state character varying(255) NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_master OWNER TO postgres;

--
-- TOC entry 732 (class 1259 OID 224700)
-- Name: tr_training_master_training_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_training_master_training_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_training_master_training_id_seq OWNER TO postgres;

--
-- TOC entry 6682 (class 0 OID 0)
-- Dependencies: 732
-- Name: tr_training_master_training_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_training_master_training_id_seq OWNED BY public.tr_training_master.training_id;


--
-- TOC entry 733 (class 1259 OID 224702)
-- Name: tr_training_optional_trainer_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_optional_trainer_rel (
    training_id integer NOT NULL,
    optional_trainer_id integer
);


ALTER TABLE IF EXISTS public.tr_training_optional_trainer_rel OWNER TO postgres;

--
-- TOC entry 734 (class 1259 OID 224705)
-- Name: tr_training_org_unit_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_org_unit_rel (
    training_id integer NOT NULL,
    org_unit_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_org_unit_rel OWNER TO postgres;

--
-- TOC entry 735 (class 1259 OID 224708)
-- Name: tr_training_primary_trainer_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_primary_trainer_rel (
    training_id integer NOT NULL,
    primary_trainer_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_primary_trainer_rel OWNER TO postgres;

--
-- TOC entry 736 (class 1259 OID 224711)
-- Name: tr_training_target_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_target_role_rel (
    training_id integer NOT NULL,
    target_role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_target_role_rel OWNER TO postgres;

--
-- TOC entry 737 (class 1259 OID 224714)
-- Name: tr_training_trainer_role_rel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_training_trainer_role_rel (
    training_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE IF EXISTS public.tr_training_trainer_role_rel OWNER TO postgres;

--
-- TOC entry 738 (class 1259 OID 224717)
-- Name: tr_user_meta_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.tr_user_meta_data (
    id integer NOT NULL,
    user_id integer,
    course_id integer,
    quiz_meta_data text,
    lesson_meta_data text,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    is_course_completed boolean,
    last_accessed_lesson_on timestamp without time zone,
    last_accessed_quiz_on timestamp without time zone,
    quizzes_completed boolean
);


ALTER TABLE IF EXISTS public.tr_user_meta_data OWNER TO postgres;

--
-- TOC entry 739 (class 1259 OID 224723)
-- Name: tr_user_meta_data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.tr_user_meta_data_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.tr_user_meta_data_id_seq OWNER TO postgres;

--
-- TOC entry 6683 (class 0 OID 0)
-- Dependencies: 739
-- Name: tr_user_meta_data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tr_user_meta_data_id_seq OWNED BY public.tr_user_meta_data.id;


--
-- TOC entry 740 (class 1259 OID 224733)
-- Name: um_role_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_role_category (
    role_id integer,
    category_id integer,
    id integer NOT NULL,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    state character varying(200)
);


ALTER TABLE IF EXISTS public.um_role_category OWNER TO postgres;

--
-- TOC entry 741 (class 1259 OID 224736)
-- Name: um_role_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_role_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_role_category_id_seq OWNER TO postgres;

--
-- TOC entry 6684 (class 0 OID 0)
-- Dependencies: 741
-- Name: um_role_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_role_category_id_seq OWNED BY public.um_role_category.id;


--
-- TOC entry 742 (class 1259 OID 224738)
-- Name: um_role_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_role_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_role_master_id_seq OWNER TO postgres;

--
-- TOC entry 6685 (class 0 OID 0)
-- Dependencies: 742
-- Name: um_role_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_role_master_id_seq OWNED BY public.um_role_master.id;


--
-- TOC entry 743 (class 1259 OID 224740)
-- Name: um_user_activation_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_activation_status (
    id integer NOT NULL,
    user_id integer,
    activation_date timestamp without time zone NOT NULL,
    deactivation_date timestamp without time zone,
    activate_by integer,
    deactivate_by integer
);


ALTER TABLE IF EXISTS public.um_user_activation_status OWNER TO postgres;

--
-- TOC entry 744 (class 1259 OID 224743)
-- Name: um_user_activation_status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_activation_status_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_activation_status_id_seq OWNER TO postgres;

--
-- TOC entry 6686 (class 0 OID 0)
-- Dependencies: 744
-- Name: um_user_activation_status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_activation_status_id_seq OWNED BY public.um_user_activation_status.id;


--
-- TOC entry 745 (class 1259 OID 224745)
-- Name: um_user_app_access_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_app_access_details (
    id integer NOT NULL,
    user_id integer,
    app_name character varying(30),
    app_version character varying(10),
    device_type character varying(30),
    created_on timestamp without time zone NOT NULL,
    imei_number character varying(100)
);


ALTER TABLE IF EXISTS public.um_user_app_access_details OWNER TO postgres;

--
-- TOC entry 746 (class 1259 OID 224748)
-- Name: um_user_app_access_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_app_access_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_app_access_details_id_seq OWNER TO postgres;

--
-- TOC entry 6687 (class 0 OID 0)
-- Dependencies: 746
-- Name: um_user_app_access_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_app_access_details_id_seq OWNED BY public.um_user_app_access_details.id;


--
-- TOC entry 747 (class 1259 OID 224750)
-- Name: um_user_attendance_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_attendance_info (
    id integer NOT NULL,
    user_id integer,
    attendance_date date,
    locations text,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    created_on timestamp without time zone,
    created_by integer,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.um_user_attendance_info OWNER TO postgres;

--
-- TOC entry 748 (class 1259 OID 224756)
-- Name: um_user_attendance_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_attendance_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_attendance_info_id_seq OWNER TO postgres;

--
-- TOC entry 6688 (class 0 OID 0)
-- Dependencies: 748
-- Name: um_user_attendance_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_attendance_info_id_seq OWNED BY public.um_user_attendance_info.id;


--
-- TOC entry 749 (class 1259 OID 224758)
-- Name: um_user_help_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_help_details (
    id integer NOT NULL,
    issue_type text,
    issue_details text,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.um_user_help_details OWNER TO postgres;

--
-- TOC entry 750 (class 1259 OID 224764)
-- Name: um_user_help_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_help_details_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_help_details_id_seq OWNER TO postgres;

--
-- TOC entry 6689 (class 0 OID 0)
-- Dependencies: 750
-- Name: um_user_help_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_help_details_id_seq OWNED BY public.um_user_help_details.id;


--
-- TOC entry 481 (class 1259 OID 223020)
-- Name: um_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_id_seq OWNER TO postgres;

--
-- TOC entry 6690 (class 0 OID 0)
-- Dependencies: 481
-- Name: um_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_id_seq OWNED BY public.um_user.id;


--
-- TOC entry 751 (class 1259 OID 224766)
-- Name: um_user_location_change_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_location_change_detail (
    user_id integer NOT NULL,
    location_id integer NOT NULL,
    role_id integer NOT NULL,
    activation_date timestamp without time zone NOT NULL,
    deactivation_date timestamp without time zone,
    activate_by integer,
    deactivate_by integer
);


ALTER TABLE IF EXISTS public.um_user_location_change_detail OWNER TO postgres;

--
-- TOC entry 752 (class 1259 OID 224769)
-- Name: um_user_location_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_location_id_seq OWNER TO postgres;

--
-- TOC entry 6691 (class 0 OID 0)
-- Dependencies: 752
-- Name: um_user_location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_location_id_seq OWNED BY public.um_user_location.id;


--
-- TOC entry 753 (class 1259 OID 224771)
-- Name: um_user_login_det; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.um_user_login_det (
    id integer NOT NULL,
    user_id integer NOT NULL,
    no_of_attempts integer,
    logging_from_web boolean NOT NULL,
    imei_number text,
    apk_version integer,
    created_on timestamp without time zone NOT NULL,
    mobile_form_version integer
);


ALTER TABLE IF EXISTS public.um_user_login_det OWNER TO postgres;

--
-- TOC entry 754 (class 1259 OID 224777)
-- Name: um_user_login_det_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.um_user_login_det_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.um_user_login_det_id_seq OWNER TO postgres;

--
-- TOC entry 6692 (class 0 OID 0)
-- Dependencies: 754
-- Name: um_user_login_det_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.um_user_login_det_id_seq OWNED BY public.um_user_login_det.id;


--
-- TOC entry 755 (class 1259 OID 224779)
-- Name: uncaught_exception_mobile_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.uncaught_exception_mobile_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.uncaught_exception_mobile_sequence OWNER TO postgres;

--
-- TOC entry 756 (class 1259 OID 224781)
-- Name: uncaught_exception_mobile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.uncaught_exception_mobile (
    id integer DEFAULT nextval('public.uncaught_exception_mobile_sequence'::regclass) NOT NULL,
    android_version character varying(255),
    manufacturer character varying(255),
    model character varying(255),
    on_date_mobile timestamp without time zone,
    revision_number integer,
    stack_trace text,
    user_name character varying(255),
    is_active boolean,
    user_id integer,
    exception_type text
);


ALTER TABLE IF EXISTS public.uncaught_exception_mobile OWNER TO postgres;

--
-- TOC entry 757 (class 1259 OID 224796)
-- Name: user_basket_preference; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_basket_preference (
    id integer NOT NULL,
    user_id integer NOT NULL,
    preference text NOT NULL
);


ALTER TABLE IF EXISTS public.user_basket_preference OWNER TO postgres;

--
-- TOC entry 758 (class 1259 OID 224802)
-- Name: user_basket_preference_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_basket_preference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_basket_preference_id_seq OWNER TO postgres;

--
-- TOC entry 6693 (class 0 OID 0)
-- Dependencies: 758
-- Name: user_basket_preference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_basket_preference_id_seq OWNED BY public.user_basket_preference.id;


--
-- TOC entry 759 (class 1259 OID 224804)
-- Name: user_form_access; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_form_access (
    user_id integer NOT NULL,
    form_code character varying(10) NOT NULL,
    state character varying(255),
    created_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.user_form_access OWNER TO postgres;

--
-- TOC entry 760 (class 1259 OID 224807)
-- Name: user_health_infrastructure; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_health_infrastructure (
    id integer NOT NULL,
    user_id integer,
    health_infrastrucutre_id integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone,
    state character varying(200),
    is_default boolean
);


ALTER TABLE IF EXISTS public.user_health_infrastructure OWNER TO postgres;

--
-- TOC entry 761 (class 1259 OID 224810)
-- Name: user_health_infrastructure_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_health_infrastructure_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_health_infrastructure_id_seq OWNER TO postgres;

--
-- TOC entry 6694 (class 0 OID 0)
-- Dependencies: 761
-- Name: user_health_infrastructure_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_health_infrastructure_id_seq OWNED BY public.user_health_infrastructure.id;


--
-- TOC entry 762 (class 1259 OID 224812)
-- Name: user_input_duration_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_input_duration_detail (
    id integer NOT NULL,
    by_user integer NOT NULL,
    duration bigint NOT NULL,
    form_type character varying(15) NOT NULL,
    is_active boolean NOT NULL,
    on_date date NOT NULL,
    related_id integer NOT NULL,
    beneficiaryid integer,
    is_child boolean,
    mobile_created_on_date timestamp without time zone,
    form_start_date timestamp without time zone
);


ALTER TABLE IF EXISTS public.user_input_duration_detail OWNER TO postgres;

--
-- TOC entry 763 (class 1259 OID 224815)
-- Name: user_input_duration_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_input_duration_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_input_duration_detail_id_seq OWNER TO postgres;

--
-- TOC entry 6695 (class 0 OID 0)
-- Dependencies: 763
-- Name: user_input_duration_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_input_duration_detail_id_seq OWNED BY public.user_input_duration_detail.id;


--
-- TOC entry 764 (class 1259 OID 224817)
-- Name: user_installed_apps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_installed_apps (
    id integer NOT NULL,
    user_id integer NOT NULL,
    imei text NOT NULL,
    uid integer,
    version_name text,
    version_code integer,
    application_name text NOT NULL,
    package_name text,
    installed_date timestamp without time zone,
    last_update_date timestamp without time zone,
    used_date timestamp without time zone,
    recieved_data integer,
    sent_data integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.user_installed_apps OWNER TO postgres;

--
-- TOC entry 765 (class 1259 OID 224823)
-- Name: user_installed_apps_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_installed_apps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_installed_apps_id_seq OWNER TO postgres;

--
-- TOC entry 6696 (class 0 OID 0)
-- Dependencies: 765
-- Name: user_installed_apps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_installed_apps_id_seq OWNED BY public.user_installed_apps.id;


--
-- TOC entry 766 (class 1259 OID 224825)
-- Name: user_menu_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_menu_item (
    user_menu_id integer NOT NULL,
    designation_id integer,
    feature_json character varying(5000),
    group_id integer,
    menu_config_id integer,
    user_id integer,
    role_id integer,
    created_by integer,
    created_on timestamp without time zone,
    modified_by integer,
    modified_on timestamp without time zone
);


ALTER TABLE IF EXISTS public.user_menu_item OWNER TO postgres;

--
-- TOC entry 783 (class 1259 OID 232208)
-- Name: user_menu_item_change_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_menu_item_change_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_menu_item_change_log_id_seq OWNER TO postgres;

--
-- TOC entry 784 (class 1259 OID 232210)
-- Name: user_menu_item_change_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_menu_item_change_log (
    id integer DEFAULT nextval('public.user_menu_item_change_log_id_seq'::regclass) NOT NULL,
    user_menu_id integer,
    designation_id integer,
    feature_json character varying(5000),
    menu_config_id integer,
    user_id integer,
    role_id integer,
    activated_on timestamp without time zone,
    deactivated_on timestamp without time zone,
    activated_by integer,
    deactivated_by integer
);


ALTER TABLE IF EXISTS public.user_menu_item_change_log OWNER TO postgres;

--
-- TOC entry 767 (class 1259 OID 224839)
-- Name: user_menu_item_user_menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_menu_item_user_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_menu_item_user_menu_id_seq OWNER TO postgres;

--
-- TOC entry 6697 (class 0 OID 0)
-- Dependencies: 767
-- Name: user_menu_item_user_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_menu_item_user_menu_id_seq OWNED BY public.user_menu_item.user_menu_id;


--
-- TOC entry 768 (class 1259 OID 224846)
-- Name: user_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE IF NOT EXISTS public.user_token (
    id integer NOT NULL,
    user_id integer NOT NULL,
    created_on timestamp without time zone,
    is_active boolean NOT NULL,
    is_archieve boolean NOT NULL,
    modified_on timestamp without time zone,
    user_token character varying(255)
);


ALTER TABLE IF EXISTS public.user_token OWNER TO postgres;

--
-- TOC entry 769 (class 1259 OID 224849)
-- Name: user_token_temp_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.user_token_temp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.user_token_temp_id_seq OWNER TO postgres;

--
-- TOC entry 6698 (class 0 OID 0)
-- Dependencies: 769
-- Name: user_token_temp_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_token_temp_id_seq OWNED BY public.user_token.id;


--
-- TOC entry 770 (class 1259 OID 224851)
-- Name: usermanagement_company_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_company_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_company_info_id_seq OWNER TO postgres;

--
-- TOC entry 771 (class 1259 OID 224853)
-- Name: usermanagement_system_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_system_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_system_feature_id_seq OWNER TO postgres;

--
-- TOC entry 772 (class 1259 OID 224855)
-- Name: usermanagement_system_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_system_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_system_role_id_seq OWNER TO postgres;

--
-- TOC entry 773 (class 1259 OID 224857)
-- Name: usermanagement_system_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_system_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_system_user_id_seq OWNER TO postgres;

--
-- TOC entry 774 (class 1259 OID 224859)
-- Name: usermanagement_user_contact_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_user_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_user_contact_id_seq OWNER TO postgres;

--
-- TOC entry 775 (class 1259 OID 224861)
-- Name: usermanagement_user_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE IF NOT EXISTS public.usermanagement_user_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE IF EXISTS public.usermanagement_user_group_id_seq OWNER TO postgres;

--
-- TOC entry 5240 (class 2604 OID 224891)
-- Name: system_database_size id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.system_database_size ALTER COLUMN id SET DEFAULT nextval('analytics.system_database_size_id_seq'::regclass);


--
-- TOC entry 5241 (class 2604 OID 224892)
-- Name: system_database_table_size id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.system_database_table_size ALTER COLUMN id SET DEFAULT nextval('analytics.system_database_table_size_id_seq'::regclass);


--
-- TOC entry 5248 (class 2604 OID 224893)
-- Name: wt_asha_10days_overdue_notification_details id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_asha_10days_overdue_notification_details ALTER COLUMN id SET DEFAULT nextval('analytics.wt_asha_10days_overdue_notification_details_id_seq'::regclass);


--
-- TOC entry 5249 (class 2604 OID 224894)
-- Name: wt_cfhc_suspected_disease id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_cfhc_suspected_disease ALTER COLUMN id SET DEFAULT nextval('analytics.wt_cfhc_suspected_disease_id_seq'::regclass);


--
-- TOC entry 5251 (class 2604 OID 224895)
-- Name: wt_last_30_days_not_registerd_any_pregnancy id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_30_days_not_registerd_any_pregnancy ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_30_days_not_registerd_any_pregnancy_id_seq'::regclass);


--
-- TOC entry 5252 (class 2604 OID 224896)
-- Name: wt_last_4_days_not_logged_in id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_id_seq'::regclass);


--
-- TOC entry 5253 (class 2604 OID 224897)
-- Name: wt_last_4_days_not_logged_in_asha id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_asha ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_asha_id_seq'::regclass);


--
-- TOC entry 5254 (class 2604 OID 224898)
-- Name: wt_last_4_days_not_logged_in_cho_hwc id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_cho_hwc ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_cho_hwc_id_seq'::regclass);


--
-- TOC entry 5255 (class 2604 OID 224899)
-- Name: wt_last_4_days_not_logged_in_fhs id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_fhs ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_fhs_id_seq'::regclass);


--
-- TOC entry 5256 (class 2604 OID 224900)
-- Name: wt_last_4_days_not_logged_in_phc_chc_deo id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_phc_chc_deo ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_phc_chc_deo_id_seq'::regclass);


--
-- TOC entry 5257 (class 2604 OID 224901)
-- Name: wt_last_4_days_not_logged_in_sd_mch_deo id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_sd_mch_deo ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_sd_mch_deo_id_seq'::regclass);


--
-- TOC entry 5258 (class 2604 OID 224902)
-- Name: wt_last_4_days_not_logged_in_tho id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_tho ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_4_days_not_logged_in_tho_id_seq'::regclass);


--
-- TOC entry 5259 (class 2604 OID 224903)
-- Name: wt_last_7_days_not_subbmitted_any_data id; Type: DEFAULT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_7_days_not_subbmitted_any_data ALTER COLUMN id SET DEFAULT nextval('analytics.wt_last_7_days_not_subbmitted_any_data_id_seq'::regclass);

--
-- TOC entry 5261 (class 2604 OID 224905)
-- Name: deleted_locations id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.deleted_locations ALTER COLUMN id SET DEFAULT nextval('archive.deleted_locations_id_seq'::regclass);


--
-- TOC entry 5262 (class 2604 OID 224906)
-- Name: deleted_users id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.deleted_users ALTER COLUMN id SET DEFAULT nextval('archive.deleted_users_id_seq'::regclass);


--
-- TOC entry 5263 (class 2604 OID 224907)
-- Name: family_state_change_issue id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.family_state_change_issue ALTER COLUMN id SET DEFAULT nextval('archive.family_state_change_issue_id_seq'::regclass);


--
-- TOC entry 5264 (class 2604 OID 224908)
-- Name: health_infrastructure_details_history id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.health_infrastructure_details_history ALTER COLUMN id SET DEFAULT nextval('archive.health_infrastructure_details_history_id_seq'::regclass);


--
-- TOC entry 5266 (class 2604 OID 224909)
-- Name: system_user id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.system_user ALTER COLUMN id SET DEFAULT nextval('archive.system_user_id_seq'::regclass);


--
-- TOC entry 5267 (class 2604 OID 224910)
-- Name: user_data_access_detail_request id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.user_data_access_detail_request ALTER COLUMN id SET DEFAULT nextval('archive.user_data_access_detail_request_id_seq'::regclass);


--
-- TOC entry 5268 (class 2604 OID 224911)
-- Name: user_location_detail id; Type: DEFAULT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.user_location_detail ALTER COLUMN id SET DEFAULT nextval('archive.user_location_detail_id_seq'::regclass);


--
-- TOC entry 5274 (class 2604 OID 224917)
-- Name: anganwadi_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.anganwadi_master ALTER COLUMN id SET DEFAULT nextval('public.anganwadi_master_id_seq'::regclass);


--
-- TOC entry 5275 (class 2604 OID 224923)
-- Name: announcement_info_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_info_master ALTER COLUMN id SET DEFAULT nextval('public.announcement_info_master_id_seq'::regclass);


--
-- TOC entry 5276 (class 2604 OID 224936)
-- Name: child_cmtc_nrc_admission_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_admission_detail ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_admission_detail_id_seq'::regclass);


--
-- TOC entry 5277 (class 2604 OID 224937)
-- Name: child_cmtc_nrc_discharge_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_discharge_detail ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_discharge_detail_id_seq'::regclass);


--
-- TOC entry 5278 (class 2604 OID 224938)
-- Name: child_cmtc_nrc_follow_up id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_follow_up ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_follow_up_id_seq'::regclass);


--
-- TOC entry 5279 (class 2604 OID 224939)
-- Name: child_cmtc_nrc_laboratory_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_laboratory_detail ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_laboratory_detail_id_seq'::regclass);


--
-- TOC entry 5280 (class 2604 OID 224940)
-- Name: child_cmtc_nrc_mo_verification id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_mo_verification ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_mo_verification_id_seq'::regclass);


--
-- TOC entry 5281 (class 2604 OID 224941)
-- Name: child_cmtc_nrc_referral_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_referral_detail ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_referral_detail_id_seq'::regclass);


--
-- TOC entry 5282 (class 2604 OID 224942)
-- Name: child_cmtc_nrc_screening_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_screening_detail ALTER COLUMN id SET DEFAULT nextval('public.child_cmtc_nrc_screening_detail_id_seq'::regclass);


--
-- TOC entry 5284 (class 2604 OID 224944)
-- Name: child_nutrition_cmam_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_nutrition_cmam_master ALTER COLUMN id SET DEFAULT nextval('public.child_nutrition_cmam_master_id_seq'::regclass);


--
-- TOC entry 5287 (class 2604 OID 224964)
-- Name: document_index id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_index ALTER COLUMN id SET DEFAULT nextval('public.document_index_id_seq'::regclass);


--
-- TOC entry 5289 (class 2604 OID 224965)
-- Name: document_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_master ALTER COLUMN id SET DEFAULT nextval('public.document_master_id_seq'::regclass);


--
-- TOC entry 5290 (class 2604 OID 224966)
-- Name: document_module_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_module_master ALTER COLUMN id SET DEFAULT nextval('public.document_module_master_id_seq'::regclass);


--
-- TOC entry 5270 (class 2604 OID 224972)
-- Name: escalation_level_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.escalation_level_master ALTER COLUMN id SET DEFAULT nextval('public.escalation_level_master_id_seq'::regclass);


--
-- TOC entry 5293 (class 2604 OID 224973)
-- Name: facility_performance_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.facility_performance_master ALTER COLUMN id SET DEFAULT nextval('public.facility_performance_master_id_seq'::regclass);


--
-- TOC entry 5294 (class 2604 OID 224975)
-- Name: field_constant_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.field_constant_master ALTER COLUMN id SET DEFAULT nextval('public.field_constant_master_id_seq'::regclass);


--
-- TOC entry 5295 (class 2604 OID 224976)
-- Name: field_value_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.field_value_master ALTER COLUMN id SET DEFAULT nextval('public.field_value_master_id_seq'::regclass);


--
-- TOC entry 5296 (class 2604 OID 224977)
-- Name: firebase_token id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.firebase_token ALTER COLUMN id SET DEFAULT nextval('public.firebase_token_id_seq'::regclass);


--
-- TOC entry 5298 (class 2604 OID 224978)
-- Name: form_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.form_master ALTER COLUMN id SET DEFAULT nextval('public.form_master_id_seq'::regclass);


--
-- TOC entry 5300 (class 2604 OID 224997)
-- Name: health_infrastructure_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_details ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_details_id_seq'::regclass);


--
-- TOC entry 5301 (class 2604 OID 224998)
-- Name: health_infrastructure_lab_test_mapping id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_lab_test_mapping ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_lab_test_mapping_id_seq'::regclass);


--
-- TOC entry 5302 (class 2604 OID 224999)
-- Name: health_infrastructure_monthly_volunteers_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_monthly_volunteers_details ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_monthly_volunteers_details_id_seq'::regclass);


--
-- TOC entry 5303 (class 2604 OID 225000)
-- Name: health_infrastructure_type_allowed_facilities id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_type_allowed_facilities ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_type_allowed_facilities_id_seq'::regclass);


--
-- TOC entry 5304 (class 2604 OID 225001)
-- Name: health_infrastructure_type_location id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_type_location ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_type_location_id_seq'::regclass);


--
-- TOC entry 5305 (class 2604 OID 225002)
-- Name: health_infrastructure_ward_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_ward_details ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_ward_details_id_seq'::regclass);


--
-- TOC entry 5306 (class 2604 OID 225003)
-- Name: health_infrastructure_ward_details_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_ward_details_history ALTER COLUMN id SET DEFAULT nextval('public.health_infrastructure_ward_details_history_id_seq'::regclass);


--
-- TOC entry 5455 (class 2604 OID 232224)
-- Name: imt_family_location_change_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_location_change_detail ALTER COLUMN id SET DEFAULT nextval('public.imt_family_location_change_detail_id_seq1'::regclass);


--
-- TOC entry 5315 (class 2604 OID 225006)
-- Name: listvalue_field_role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_role ALTER COLUMN id SET DEFAULT nextval('public.listvalue_field_role_id_seq'::regclass);


--
-- TOC entry 5316 (class 2604 OID 225007)
-- Name: listvalue_field_value_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_value_detail ALTER COLUMN id SET DEFAULT nextval('public.listvalue_field_value_detail_id_seq'::regclass);


--
-- TOC entry 5318 (class 2604 OID 225009)
-- Name: location_cluster_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_cluster_master ALTER COLUMN id SET DEFAULT nextval('public.location_cluster_master_id_seq'::regclass);


--
-- TOC entry 5242 (class 2604 OID 225010)
-- Name: location_hierchy_closer_det id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_hierchy_closer_det ALTER COLUMN id SET DEFAULT nextval('public.location_hierchy_closer_det_id_seq'::regclass);


--
-- TOC entry 5243 (class 2604 OID 225011)
-- Name: location_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_master ALTER COLUMN id SET DEFAULT nextval('public.location_master_id_seq'::regclass);


--
-- TOC entry 5320 (class 2604 OID 225012)
-- Name: location_mobile_feature_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_mobile_feature_master ALTER COLUMN id SET DEFAULT nextval('public.location_mobile_feature_master_id_seq'::regclass);


--
-- TOC entry 5321 (class 2604 OID 225013)
-- Name: location_state_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_state_master ALTER COLUMN id SET DEFAULT nextval('public.location_state_master_id_seq'::regclass);


--
-- TOC entry 5324 (class 2604 OID 225014)
-- Name: location_type_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_type_master ALTER COLUMN id SET DEFAULT nextval('public.location_type_master_id_seq'::regclass);


--
-- TOC entry 5325 (class 2604 OID 225017)
-- Name: logged_actions event_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.logged_actions ALTER COLUMN event_id SET DEFAULT nextval('public.logged_actions_event_id_seq'::regclass);


--
-- TOC entry 5327 (class 2604 OID 225020)
-- Name: menu_config id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_config ALTER COLUMN id SET DEFAULT nextval('public.menu_config_id_seq'::regclass);


--
-- TOC entry 5328 (class 2604 OID 225021)
-- Name: menu_group id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_group ALTER COLUMN id SET DEFAULT nextval('public.menu_group_id_seq'::regclass);


--
-- TOC entry 5333 (class 2604 OID 225023)
-- Name: mobile_beans_feature_rel id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_beans_feature_rel ALTER COLUMN id SET DEFAULT nextval('public.mobile_beans_feature_rel_id_seq'::regclass);


--
-- TOC entry 5334 (class 2604 OID 225024)
-- Name: mobile_form_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_form_details ALTER COLUMN id SET DEFAULT nextval('public.mobile_form_details_id_seq'::regclass);


--
-- TOC entry 5451 (class 2604 OID 232184)
-- Name: mobile_library_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_library_master ALTER COLUMN id SET DEFAULT nextval('public.mobile_library_master_id_seq'::regclass);


--
-- TOC entry 5335 (class 2604 OID 225026)
-- Name: mobile_menu_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_menu_master ALTER COLUMN id SET DEFAULT nextval('public.mobile_menu_master_id_seq'::regclass);


--
-- TOC entry 5453 (class 2604 OID 232204)
-- Name: mobile_number_filter_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_number_filter_master ALTER COLUMN id SET DEFAULT nextval('public.mobile_number_filter_master_id_seq'::regclass);


--
-- TOC entry 5273 (class 2604 OID 225075)
-- Name: notification_type_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.notification_type_master ALTER COLUMN id SET DEFAULT nextval('public.notification_type_master_id_seq'::regclass);


--
-- TOC entry 5337 (class 2604 OID 225079)
-- Name: query_analysis_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.query_analysis_details ALTER COLUMN id SET DEFAULT nextval('public.query_analysis_details_id_seq'::regclass);


--
-- TOC entry 5339 (class 2604 OID 225080)
-- Name: query_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.query_history ALTER COLUMN id SET DEFAULT nextval('public.query_history_id_seq'::regclass);


--
-- TOC entry 5342 (class 2604 OID 225084)
-- Name: rch_asha_anc_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_anc_master_id_seq'::regclass);


--
-- TOC entry 5343 (class 2604 OID 225085)
-- Name: rch_asha_anc_morbidity_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_morbidity_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_anc_morbidity_master_id_seq'::regclass);


--
-- TOC entry 5344 (class 2604 OID 225086)
-- Name: rch_asha_child_service_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_child_service_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_child_service_master_id_seq'::regclass);


--
-- TOC entry 5345 (class 2604 OID 225087)
-- Name: rch_asha_cs_morbidity_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_cs_morbidity_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_cs_morbidity_master_id_seq'::regclass);


--
-- TOC entry 5346 (class 2604 OID 225088)
-- Name: rch_asha_lmp_follow_up id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_lmp_follow_up ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_lmp_follow_up_id_seq'::regclass);


--
-- TOC entry 5348 (class 2604 OID 225089)
-- Name: rch_asha_pnc_child_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_pnc_child_master_id_seq'::regclass);


--
-- TOC entry 5349 (class 2604 OID 225090)
-- Name: rch_asha_pnc_child_morbidity_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_morbidity_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_pnc_child_morbidity_master_id_seq'::regclass);


--
-- TOC entry 5350 (class 2604 OID 225091)
-- Name: rch_asha_pnc_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_pnc_master_id_seq'::regclass);


--
-- TOC entry 5351 (class 2604 OID 225092)
-- Name: rch_asha_pnc_mother_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_pnc_mother_master_id_seq'::regclass);


--
-- TOC entry 5352 (class 2604 OID 225093)
-- Name: rch_asha_pnc_mother_morbidity_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_morbidity_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_pnc_mother_morbidity_master_id_seq'::regclass);


--
-- TOC entry 5353 (class 2604 OID 225094)
-- Name: rch_asha_reported_event_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_reported_event_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_reported_event_master_id_seq'::regclass);


--
-- TOC entry 5354 (class 2604 OID 225095)
-- Name: rch_asha_wpd_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_wpd_master ALTER COLUMN id SET DEFAULT nextval('public.rch_asha_wpd_master_id_seq'::regclass);


--
-- TOC entry 5356 (class 2604 OID 225096)
-- Name: rch_child_cp_suspects id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_cp_suspects ALTER COLUMN id SET DEFAULT nextval('public.rch_child_cp_suspects_id_seq'::regclass);


--
-- TOC entry 5358 (class 2604 OID 225097)
-- Name: rch_data_migration id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_data_migration ALTER COLUMN id SET DEFAULT nextval('public.rch_data_migration_id_seq'::regclass);


--
-- TOC entry 5360 (class 2604 OID 225098)
-- Name: rch_institution_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_institution_master ALTER COLUMN id SET DEFAULT nextval('public.rch_institution_master_id_seq'::regclass);


--
-- TOC entry 5362 (class 2604 OID 225102)
-- Name: rch_member_data_sync_pending id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_member_data_sync_pending ALTER COLUMN id SET DEFAULT nextval('public.rch_member_data_sync_pending_id_seq'::regclass);


--
-- TOC entry 5366 (class 2604 OID 225103)
-- Name: rch_opd_edl_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_edl_details ALTER COLUMN id SET DEFAULT nextval('public.rch_opd_edl_details_id_seq'::regclass);


--
-- TOC entry 5367 (class 2604 OID 225105)
-- Name: rch_opd_lab_test_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_lab_test_master ALTER COLUMN id SET DEFAULT nextval('public.rch_opd_lab_test_master_id_seq'::regclass);


--
-- TOC entry 5368 (class 2604 OID 225106)
-- Name: rch_opd_member_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_member_master ALTER COLUMN id SET DEFAULT nextval('public.rch_opd_member_master_id_seq'::regclass);


--
-- TOC entry 5369 (class 2604 OID 225107)
-- Name: rch_opd_member_registration id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_member_registration ALTER COLUMN id SET DEFAULT nextval('public.rch_opd_member_registration_id_seq'::regclass);


--
-- TOC entry 5371 (class 2604 OID 225108)
-- Name: rch_pmsma_survey_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pmsma_survey_master ALTER COLUMN id SET DEFAULT nextval('public.rch_pmsma_survey_master_id_seq'::regclass);


--
-- TOC entry 5377 (class 2604 OID 225109)
-- Name: rch_vaccine_adverse_effect id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_vaccine_adverse_effect ALTER COLUMN id SET DEFAULT nextval('public.rch_vaccine_adverse_effect_id_seq'::regclass);


--
-- TOC entry 5380 (class 2604 OID 225110)
-- Name: report_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_master ALTER COLUMN id SET DEFAULT nextval('public.report_master_id_seq'::regclass);


--
-- TOC entry 5381 (class 2604 OID 225111)
-- Name: report_offline_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_offline_details ALTER COLUMN id SET DEFAULT nextval('public.report_offline_details_id_seq'::regclass);


--
-- TOC entry 5382 (class 2604 OID 225112)
-- Name: report_parameter_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_parameter_master ALTER COLUMN id SET DEFAULT nextval('public.report_parameter_master_id_seq'::regclass);


--
-- TOC entry 5383 (class 2604 OID 225113)
-- Name: report_query_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_query_master ALTER COLUMN id SET DEFAULT nextval('public.report_query_master_id_seq'::regclass);


--
-- TOC entry 5384 (class 2604 OID 225114)
-- Name: request_response_details_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_details_master ALTER COLUMN id SET DEFAULT nextval('public.request_response_details_master_id_seq'::regclass);


--
-- TOC entry 5385 (class 2604 OID 225115)
-- Name: request_response_navigation_state_mapping id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_navigation_state_mapping ALTER COLUMN id SET DEFAULT nextval('public.request_response_navigation_state_mapping_id_seq'::regclass);


--
-- TOC entry 5386 (class 2604 OID 225117)
-- Name: request_response_regex_list_to_be_ignored id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_regex_list_to_be_ignored ALTER COLUMN id SET DEFAULT nextval('public.request_response_regex_list_to_be_ignored_id_seq'::regclass);


--
-- TOC entry 5387 (class 2604 OID 225118)
-- Name: request_response_url_mapping id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_url_mapping ALTER COLUMN id SET DEFAULT nextval('public.request_response_url_mapping_id_seq'::regclass);


--
-- TOC entry 5388 (class 2604 OID 225119)
-- Name: response_analysis_by_time_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.response_analysis_by_time_details ALTER COLUMN id SET DEFAULT nextval('public.response_analysis_by_time_details_id_seq'::regclass);


--
-- TOC entry 5389 (class 2604 OID 225120)
-- Name: role_health_infrastructure_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_health_infrastructure_type ALTER COLUMN id SET DEFAULT nextval('public.role_health_infrastructure_id_seq'::regclass);


--
-- TOC entry 5390 (class 2604 OID 225121)
-- Name: role_hierarchy_management id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_hierarchy_management ALTER COLUMN id SET DEFAULT nextval('public.role_hierarchy_management_id_seq'::regclass);


--
-- TOC entry 5391 (class 2604 OID 225122)
-- Name: role_management id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_management ALTER COLUMN id SET DEFAULT nextval('public.role_management_id_seq'::regclass);


--
-- TOC entry 5392 (class 2604 OID 225123)
-- Name: school_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.school_master ALTER COLUMN id SET DEFAULT nextval('public.school_master_id_seq'::regclass);


--
-- TOC entry 5393 (class 2604 OID 225124)
-- Name: server_list_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.server_list_master ALTER COLUMN id SET DEFAULT nextval('public.server_list_master_id_seq'::regclass);


--
-- TOC entry 5394 (class 2604 OID 225125)
-- Name: sickle_cell_screening id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sickle_cell_screening ALTER COLUMN id SET DEFAULT nextval('public.sickle_cell_screening_id_seq'::regclass);


--
-- TOC entry 5395 (class 2604 OID 225126)
-- Name: sms id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms ALTER COLUMN id SET DEFAULT nextval('public.sms_id_seq'::regclass);


--
-- TOC entry 5397 (class 2604 OID 225127)
-- Name: sms_block_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_block_master ALTER COLUMN id SET DEFAULT nextval('public.sms_block_master_id_seq'::regclass);


--
-- TOC entry 5398 (class 2604 OID 225128)
-- Name: sms_queue id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_queue ALTER COLUMN id SET DEFAULT nextval('public.sms_queue_id_seq'::regclass);


--
-- TOC entry 5450 (class 2604 OID 232173)
-- Name: soh_element_module_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.soh_element_module_master ALTER COLUMN id SET DEFAULT nextval('public.soh_element_module_master_id_seq'::regclass);


--
-- TOC entry 5399 (class 2604 OID 225141)
-- Name: sync_system_configuration_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sync_system_configuration_master ALTER COLUMN id SET DEFAULT nextval('public.sync_system_configuration_master_id_seq'::regclass);


--
-- TOC entry 5400 (class 2604 OID 225142)
-- Name: sync_system_configuration_server_access_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sync_system_configuration_server_access_details ALTER COLUMN id SET DEFAULT nextval('public.system_config_sync_access_id_seq'::regclass);


--
-- TOC entry 5401 (class 2604 OID 225143)
-- Name: system_build_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_build_history ALTER COLUMN id SET DEFAULT nextval('public.system_build_history_id_seq'::regclass);


--
-- TOC entry 5406 (class 2604 OID 225144)
-- Name: system_constraint_form_version id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_form_version ALTER COLUMN id SET DEFAULT nextval('public.system_constraint_form_version_id_seq'::regclass);


--
-- TOC entry 5408 (class 2604 OID 225147)
-- Name: system_function_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_function_master ALTER COLUMN id SET DEFAULT nextval('public.system_function_master_id_seq'::regclass);


--
-- TOC entry 5412 (class 2604 OID 225155)
-- Name: techo_push_notification_config_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_config_master ALTER COLUMN id SET DEFAULT nextval('public.techo_push_notification_config_master_id_seq'::regclass);


--
-- TOC entry 5413 (class 2604 OID 225156)
-- Name: techo_push_notification_location_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_location_detail ALTER COLUMN id SET DEFAULT nextval('public.techo_push_notification_location_detail_id_seq'::regclass);


--
-- TOC entry 5416 (class 2604 OID 225157)
-- Name: techo_push_notification_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_master ALTER COLUMN id SET DEFAULT nextval('public.techo_push_notification_master_id_seq'::regclass);


--
-- TOC entry 5417 (class 2604 OID 225158)
-- Name: techo_push_notification_role_user_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_role_user_detail ALTER COLUMN id SET DEFAULT nextval('public.techo_push_notification_role_user_detail_id_seq'::regclass);


--
-- TOC entry 5419 (class 2604 OID 225159)
-- Name: techo_push_notification_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_type ALTER COLUMN id SET DEFAULT nextval('public.techo_push_notification_type_id_seq'::regclass);


--
-- TOC entry 5422 (class 2604 OID 225160)
-- Name: timer_event id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.timer_event ALTER COLUMN id SET DEFAULT nextval('public.timer_event_id_seq'::regclass);


--
-- TOC entry 5423 (class 2604 OID 225162)
-- Name: tp_api_access_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tp_api_access_log ALTER COLUMN id SET DEFAULT nextval('public.tp_api_access_log_id_seq'::regclass);


--
-- TOC entry 5424 (class 2604 OID 225163)
-- Name: tr_attendance_master attendance_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_attendance_master ALTER COLUMN attendance_id SET DEFAULT nextval('public.tr_attendance_master_attendance_id_seq'::regclass);


--
-- TOC entry 5425 (class 2604 OID 225164)
-- Name: tr_certificate_master certificate_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_certificate_master ALTER COLUMN certificate_id SET DEFAULT nextval('public.tr_certificate_master_certificate_id_seq'::regclass);


--
-- TOC entry 5427 (class 2604 OID 225165)
-- Name: tr_course_master course_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_course_master ALTER COLUMN course_id SET DEFAULT nextval('public.tr_course_master_course_id_seq'::regclass);


--
-- TOC entry 5428 (class 2604 OID 225166)
-- Name: tr_lesson_analytics id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_lesson_analytics ALTER COLUMN id SET DEFAULT nextval('public.tr_lesson_analytics_id_seq'::regclass);


--
-- TOC entry 5429 (class 2604 OID 225167)
-- Name: tr_question_bank_configuration id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_bank_configuration ALTER COLUMN id SET DEFAULT nextval('public.tr_question_bank_configuration_id_seq'::regclass);


--
-- TOC entry 5430 (class 2604 OID 225168)
-- Name: tr_question_set_answer id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_set_answer ALTER COLUMN id SET DEFAULT nextval('public.tr_question_set_answer_id_seq'::regclass);


--
-- TOC entry 5431 (class 2604 OID 225169)
-- Name: tr_question_set_configuration id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_set_configuration ALTER COLUMN id SET DEFAULT nextval('public.tr_question_set_configuration_id_seq'::regclass);


--
-- TOC entry 5432 (class 2604 OID 225170)
-- Name: tr_topic_coverage_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_coverage_master ALTER COLUMN id SET DEFAULT nextval('public.tr_topic_coverage_master_id_seq'::regclass);


--
-- TOC entry 5433 (class 2604 OID 225171)
-- Name: tr_topic_master topic_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_master ALTER COLUMN topic_id SET DEFAULT nextval('public.tr_topic_master_topic_id_seq'::regclass);


--
-- TOC entry 5434 (class 2604 OID 225172)
-- Name: tr_topic_media_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_media_master ALTER COLUMN id SET DEFAULT nextval('public.tr_topic_media_master_id_seq'::regclass);


--
-- TOC entry 5435 (class 2604 OID 225173)
-- Name: tr_training_master training_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_master ALTER COLUMN training_id SET DEFAULT nextval('public.tr_training_master_training_id_seq'::regclass);


--
-- TOC entry 5436 (class 2604 OID 225174)
-- Name: tr_user_meta_data id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_user_meta_data ALTER COLUMN id SET DEFAULT nextval('public.tr_user_meta_data_id_seq'::regclass);


--
-- TOC entry 5437 (class 2604 OID 225176)
-- Name: um_role_category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_role_category ALTER COLUMN id SET DEFAULT nextval('public.um_role_category_id_seq'::regclass);


--
-- TOC entry 5246 (class 2604 OID 225177)
-- Name: um_role_master id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_role_master ALTER COLUMN id SET DEFAULT nextval('public.um_role_master_id_seq'::regclass);


--
-- TOC entry 5245 (class 2604 OID 225178)
-- Name: um_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user ALTER COLUMN id SET DEFAULT nextval('public.um_user_id_seq'::regclass);


--
-- TOC entry 5438 (class 2604 OID 225179)
-- Name: um_user_activation_status id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_activation_status ALTER COLUMN id SET DEFAULT nextval('public.um_user_activation_status_id_seq'::regclass);


--
-- TOC entry 5439 (class 2604 OID 225180)
-- Name: um_user_app_access_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_app_access_details ALTER COLUMN id SET DEFAULT nextval('public.um_user_app_access_details_id_seq'::regclass);


--
-- TOC entry 5440 (class 2604 OID 225181)
-- Name: um_user_attendance_info id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_attendance_info ALTER COLUMN id SET DEFAULT nextval('public.um_user_attendance_info_id_seq'::regclass);


--
-- TOC entry 5441 (class 2604 OID 225182)
-- Name: um_user_help_details id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_help_details ALTER COLUMN id SET DEFAULT nextval('public.um_user_help_details_id_seq'::regclass);


--
-- TOC entry 5247 (class 2604 OID 225183)
-- Name: um_user_location id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_location ALTER COLUMN id SET DEFAULT nextval('public.um_user_location_id_seq'::regclass);


--
-- TOC entry 5442 (class 2604 OID 225184)
-- Name: um_user_login_det id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_login_det ALTER COLUMN id SET DEFAULT nextval('public.um_user_login_det_id_seq'::regclass);


--
-- TOC entry 5444 (class 2604 OID 225186)
-- Name: user_basket_preference id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_basket_preference ALTER COLUMN id SET DEFAULT nextval('public.user_basket_preference_id_seq'::regclass);


--
-- TOC entry 5445 (class 2604 OID 225187)
-- Name: user_health_infrastructure id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_health_infrastructure ALTER COLUMN id SET DEFAULT nextval('public.user_health_infrastructure_id_seq'::regclass);


--
-- TOC entry 5446 (class 2604 OID 225188)
-- Name: user_input_duration_detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_input_duration_detail ALTER COLUMN id SET DEFAULT nextval('public.user_input_duration_detail_id_seq'::regclass);


--
-- TOC entry 5447 (class 2604 OID 225189)
-- Name: user_installed_apps id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_installed_apps ALTER COLUMN id SET DEFAULT nextval('public.user_installed_apps_id_seq'::regclass);


--
-- TOC entry 5448 (class 2604 OID 225190)
-- Name: user_menu_item user_menu_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_menu_item ALTER COLUMN user_menu_id SET DEFAULT nextval('public.user_menu_item_user_menu_id_seq'::regclass);


--
-- TOC entry 5449 (class 2604 OID 225193)
-- Name: user_token id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_token ALTER COLUMN id SET DEFAULT nextval('public.user_token_temp_id_seq'::regclass);


--
-- TOC entry 5457 (class 2606 OID 230407)
-- Name: child_analytics_details child_analytics_details_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.child_analytics_details
    ADD CONSTRAINT child_analytics_details_t_pkey PRIMARY KEY (member_id);


--
-- TOC entry 5459 (class 2606 OID 230409)
-- Name: child_monthly_analytics_16_24_months child_monthly_analytics_16_24_months_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.child_monthly_analytics_16_24_months
    ADD CONSTRAINT child_monthly_analytics_16_24_months_t_pkey PRIMARY KEY (month_year, location_id);


--
-- TOC entry 5461 (class 2606 OID 230421)
-- Name: family_basic_detail family_basic_detail_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.family_basic_detail
    ADD CONSTRAINT family_basic_detail_t_pkey PRIMARY KEY (family_id);


--
-- TOC entry 5463 (class 2606 OID 230433)
-- Name: location_wise_task_complition_rate_analysis location_wise_task_complition_rate_analysis_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.location_wise_task_complition_rate_analysis
    ADD CONSTRAINT location_wise_task_complition_rate_analysis_t_pkey PRIMARY KEY (location_id, month_year, notification_type_id, user_id);


--
-- TOC entry 5466 (class 2606 OID 230435)
-- Name: member_basic_detail member_basic_detail_t_pkey1; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.member_basic_detail
    ADD CONSTRAINT member_basic_detail_t_pkey1 PRIMARY KEY (member_id);


--
-- TOC entry 5476 (class 2606 OID 230443)
-- Name: rch_child_analytics_details rch_child_analytics_details_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_child_analytics_details
    ADD CONSTRAINT rch_child_analytics_details_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5479 (class 2606 OID 230445)
-- Name: rch_data_quality_analytics rch_data_quality_analytics_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_data_quality_analytics
    ADD CONSTRAINT rch_data_quality_analytics_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5481 (class 2606 OID 230447)
-- Name: rch_eligible_couple_analytics rch_eligible_couple_analytics_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_eligible_couple_analytics
    ADD CONSTRAINT rch_eligible_couple_analytics_t_pkey PRIMARY KEY (member_id);


--
-- TOC entry 5485 (class 2606 OID 230449)
-- Name: rch_member_services rch_member_services_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_member_services
    ADD CONSTRAINT rch_member_services_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5490 (class 2606 OID 230451)
-- Name: rch_phc_month_wise_pmsma_date rch_phc_month_wise_pmsma_date_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_phc_month_wise_pmsma_date
    ADD CONSTRAINT rch_phc_month_wise_pmsma_date_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 5492 (class 2606 OID 230453)
-- Name: rch_pmsma_service_statatics rch_pmsma_service_statatics_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_pmsma_service_statatics
    ADD CONSTRAINT rch_pmsma_service_statatics_t_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 5497 (class 2606 OID 230455)
-- Name: rch_pregnancy_analytics_details rch_pregnancy_analytics_details_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.rch_pregnancy_analytics_details
    ADD CONSTRAINT rch_pregnancy_analytics_details_t_pkey PRIMARY KEY (pregnancy_reg_id);


--
-- TOC entry 5500 (class 2606 OID 230457)
-- Name: soh_timeline_analytics soh_timeline_analytics_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.soh_timeline_analytics
    ADD CONSTRAINT soh_timeline_analytics_t_pkey PRIMARY KEY (location_id, timeline_type, element_name);


--
-- TOC entry 5504 (class 2606 OID 230459)
-- Name: state_of_health_master state_of_health_master_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.state_of_health_master
    ADD CONSTRAINT state_of_health_master_t_pkey PRIMARY KEY (location_id, element_name);


--
-- TOC entry 5506 (class 2606 OID 230461)
-- Name: system_database_table_size system_database_table_size_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.system_database_table_size
    ADD CONSTRAINT system_database_table_size_pkey PRIMARY KEY (id);


--
-- TOC entry 5509 (class 2606 OID 230463)
-- Name: um_user_day_wise_login_detail um_user_day_wise_login_detail_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.um_user_day_wise_login_detail
    ADD CONSTRAINT um_user_day_wise_login_detail_pkey PRIMARY KEY (login_date, location_id, role_id, user_id);


--
-- TOC entry 5511 (class 2606 OID 230465)
-- Name: um_user_month_wise_login_rate um_user_month_wise_login_rate_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.um_user_month_wise_login_rate
    ADD CONSTRAINT um_user_month_wise_login_rate_pkey PRIMARY KEY (month_year, location_id, role_id, user_id);


--
-- TOC entry 5513 (class 2606 OID 230467)
-- Name: um_user_score_rank_details um_user_score_rank_details_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.um_user_score_rank_details
    ADD CONSTRAINT um_user_score_rank_details_pkey PRIMARY KEY (user_id, score_date);


--
-- TOC entry 5515 (class 2606 OID 230471)
-- Name: user_wise_feature_time_taken_detail_analytics user_wise_feature_time_taken_detail_analytics_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.user_wise_feature_time_taken_detail_analytics
    ADD CONSTRAINT user_wise_feature_time_taken_detail_analytics_pkey PRIMARY KEY (user_id, role_id, page_title_id, on_date);


--
-- TOC entry 5553 (class 2606 OID 230473)
-- Name: wt_asha_10days_overdue_notification_details wt_asha_10dy_od_ntfn_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_asha_10days_overdue_notification_details
    ADD CONSTRAINT wt_asha_10dy_od_ntfn_pkey PRIMARY KEY (id);


--
-- TOC entry 5555 (class 2606 OID 230475)
-- Name: wt_cfhc_suspected_disease wt_cfhc_suspected_disease_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_cfhc_suspected_disease
    ADD CONSTRAINT wt_cfhc_suspected_disease_pkey PRIMARY KEY (id);


--
-- TOC entry 5557 (class 2606 OID 230477)
-- Name: wt_high_risk_confirm_case_det wt_high_risk_confirm_case_det_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_high_risk_confirm_case_det
    ADD CONSTRAINT wt_high_risk_confirm_case_det_pkey PRIMARY KEY (pregnancy_reg_det_id);


--
-- TOC entry 5559 (class 2606 OID 230479)
-- Name: wt_high_risk_member_detail wt_high_risk_member_detail_t_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_high_risk_member_detail
    ADD CONSTRAINT wt_high_risk_member_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5561 (class 2606 OID 230481)
-- Name: wt_last_4_days_not_logged_in_asha wt_last_4_days_not_logged_in_asha_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_asha
    ADD CONSTRAINT wt_last_4_days_not_logged_in_asha_pkey PRIMARY KEY (id);


--
-- TOC entry 5563 (class 2606 OID 230483)
-- Name: wt_last_4_days_not_logged_in_fhs wt_last_4_days_not_logged_in_fhs_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_fhs
    ADD CONSTRAINT wt_last_4_days_not_logged_in_fhs_pkey PRIMARY KEY (id);


--
-- TOC entry 5565 (class 2606 OID 230485)
-- Name: wt_last_4_days_not_logged_in_phc_chc_deo wt_last_4_days_not_logged_in_phc_chc_deo_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_phc_chc_deo
    ADD CONSTRAINT wt_last_4_days_not_logged_in_phc_chc_deo_pkey PRIMARY KEY (id);


--
-- TOC entry 5567 (class 2606 OID 230487)
-- Name: wt_last_4_days_not_logged_in_sd_mch_deo wt_last_4_days_not_logged_in_sd_mch_deo_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_sd_mch_deo
    ADD CONSTRAINT wt_last_4_days_not_logged_in_sd_mch_deo_pkey PRIMARY KEY (id);


--
-- TOC entry 5569 (class 2606 OID 230489)
-- Name: wt_last_4_days_not_logged_in_tho wt_last_4_days_not_logged_in_tho_pkey; Type: CONSTRAINT; Schema: analytics; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY analytics.wt_last_4_days_not_logged_in_tho
    ADD CONSTRAINT wt_last_4_days_not_logged_in_tho_pkey PRIMARY KEY (id);


--
-- TOC entry 5599 (class 2606 OID 230491)
-- Name: system_user PK_ID; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.system_user
    ADD CONSTRAINT "PK_ID" PRIMARY KEY (id);


--
-- TOC entry 5571 (class 2606 OID 230493)
-- Name: activity_type activity_type_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.activity_type
    ADD CONSTRAINT activity_type_pkey PRIMARY KEY (activity_id);

--
-- TOC entry 5575 (class 2606 OID 230499)
-- Name: deleted_locations deleted_locations_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.deleted_locations
    ADD CONSTRAINT deleted_locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5577 (class 2606 OID 230501)
-- Name: deleted_users deleted_users_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.deleted_users
    ADD CONSTRAINT deleted_users_pkey PRIMARY KEY (id);


--
-- TOC entry 5580 (class 2606 OID 230503)
-- Name: family_state_change_issue family_state_change_issue_family_id_key; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.family_state_change_issue
    ADD CONSTRAINT family_state_change_issue_family_id_key UNIQUE (family_id);


--
-- TOC entry 5582 (class 2606 OID 230505)
-- Name: family_state_change_issue family_state_change_issue_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.family_state_change_issue
    ADD CONSTRAINT family_state_change_issue_pkey PRIMARY KEY (id);


--
-- TOC entry 5584 (class 2606 OID 230513)
-- Name: notification_generated_for_asha_location_det notification_generated_for_asha_location_det_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.notification_generated_for_asha_location_det
    ADD CONSTRAINT notification_generated_for_asha_location_det_pkey PRIMARY KEY (location_id);


--
-- TOC entry 5607 (class 2606 OID 230517)
-- Name: usermanagement_system_user pk_usermanagement_system_user; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.usermanagement_system_user
    ADD CONSTRAINT pk_usermanagement_system_user PRIMARY KEY (id);


--
-- TOC entry 5586 (class 2606 OID 230521)
-- Name: rch_immunisation_master_archive rch_immunisation_master_archive_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.rch_immunisation_master_archive
    ADD CONSTRAINT rch_immunisation_master_archive_pkey PRIMARY KEY (id);


--
-- TOC entry 5588 (class 2606 OID 230523)
-- Name: rch_member_death_deatil_archive rch_member_death_deatil_archive_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.rch_member_death_deatil_archive
    ADD CONSTRAINT rch_member_death_deatil_archive_pkey PRIMARY KEY (id);


--
-- TOC entry 5591 (class 2606 OID 230525)
-- Name: rch_member_service_data_sync_detail rch_member_service_data_sync_detail1_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.rch_member_service_data_sync_detail
    ADD CONSTRAINT rch_member_service_data_sync_detail1_pkey PRIMARY KEY (id);


--
-- TOC entry 5593 (class 2606 OID 230527)
-- Name: synced_wpd_mother synced_wpd_mother_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.synced_wpd_mother
    ADD CONSTRAINT synced_wpd_mother_pkey PRIMARY KEY (sync_status_id);


--
-- TOC entry 5595 (class 2606 OID 230529)
-- Name: system_sync_status_dump system_sync_status_dump1_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.system_sync_status_dump
    ADD CONSTRAINT system_sync_status_dump1_pkey PRIMARY KEY (id);


--
-- TOC entry 5601 (class 2606 OID 230531)
-- Name: user_data_access_detail_request user_data_access_detail_request_pk; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.user_data_access_detail_request
    ADD CONSTRAINT user_data_access_detail_request_pk PRIMARY KEY (id);


--
-- TOC entry 5604 (class 2606 OID 230533)
-- Name: user_location_detail user_location_detail_pkey; Type: CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.user_location_detail
    ADD CONSTRAINT user_location_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5616 (class 2606 OID 230563)
-- Name: anganwadi_master anganwadi_master_emamta_id_uniquekey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.anganwadi_master
    ADD CONSTRAINT anganwadi_master_emamta_id_uniquekey UNIQUE (emamta_id);


--
-- TOC entry 5618 (class 2606 OID 230565)
-- Name: anganwadi_master anganwadi_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.anganwadi_master
    ADD CONSTRAINT anganwadi_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5622 (class 2606 OID 230579)
-- Name: announcement_info_detail announcement_info_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_info_detail
    ADD CONSTRAINT announcement_info_detail_pkey PRIMARY KEY (announcement, language);


--
-- TOC entry 5624 (class 2606 OID 230581)
-- Name: announcement_info_master announcement_info_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_info_master
    ADD CONSTRAINT announcement_info_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5626 (class 2606 OID 230583)
-- Name: announcement_location_detail announcement_location_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_location_detail
    ADD CONSTRAINT announcement_location_detail_pkey PRIMARY KEY (announcement, location, announcement_for);


--
-- TOC entry 6226 (class 2606 OID 232198)
-- Name: blocked_devices_master blocked_devices_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.blocked_devices_master
    ADD CONSTRAINT blocked_devices_master_pkey PRIMARY KEY (imei);


--
-- TOC entry 5628 (class 2606 OID 230621)
-- Name: child_cmtc_nrc_admission_detail child_cmtc_nrc_admission_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_admission_detail
    ADD CONSTRAINT child_cmtc_nrc_admission_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5630 (class 2606 OID 230623)
-- Name: child_cmtc_nrc_admission_illness_detail child_cmtc_nrc_admission_illness_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_admission_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_admission_illness_detail_pkey PRIMARY KEY (admission_id, illness);


--
-- TOC entry 5632 (class 2606 OID 230625)
-- Name: child_cmtc_nrc_discharge_detail child_cmtc_nrc_discharge_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_discharge_detail
    ADD CONSTRAINT child_cmtc_nrc_discharge_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5634 (class 2606 OID 230627)
-- Name: child_cmtc_nrc_discharge_illness_detail child_cmtc_nrc_discharge_illness_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_discharge_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_discharge_illness_detail_pkey PRIMARY KEY (discharge_id, illness);


--
-- TOC entry 5638 (class 2606 OID 230629)
-- Name: child_cmtc_nrc_follow_up_illness_detail child_cmtc_nrc_follow_up_illness_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_follow_up_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_follow_up_illness_detail_pkey PRIMARY KEY (follow_up_id, illness);


--
-- TOC entry 5636 (class 2606 OID 230631)
-- Name: child_cmtc_nrc_follow_up child_cmtc_nrc_follow_up_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_follow_up
    ADD CONSTRAINT child_cmtc_nrc_follow_up_pkey PRIMARY KEY (id);


--
-- TOC entry 5640 (class 2606 OID 230633)
-- Name: child_cmtc_nrc_laboratory_detail child_cmtc_nrc_laboratory_deta_admission_id_laboratory_date_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_laboratory_detail
    ADD CONSTRAINT child_cmtc_nrc_laboratory_deta_admission_id_laboratory_date_key UNIQUE (admission_id, laboratory_date);


--
-- TOC entry 5642 (class 2606 OID 230635)
-- Name: child_cmtc_nrc_laboratory_detail child_cmtc_nrc_laboratory_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_laboratory_detail
    ADD CONSTRAINT child_cmtc_nrc_laboratory_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5644 (class 2606 OID 230638)
-- Name: child_cmtc_nrc_mo_verification child_cmtc_nrc_mo_verification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_mo_verification
    ADD CONSTRAINT child_cmtc_nrc_mo_verification_pkey PRIMARY KEY (id);


--
-- TOC entry 5646 (class 2606 OID 230640)
-- Name: child_cmtc_nrc_referral_detail child_cmtc_nrc_referral_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_referral_detail
    ADD CONSTRAINT child_cmtc_nrc_referral_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5648 (class 2606 OID 230642)
-- Name: child_cmtc_nrc_screening_detail child_cmtc_nrc_screening_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_screening_detail
    ADD CONSTRAINT child_cmtc_nrc_screening_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5650 (class 2606 OID 230644)
-- Name: child_cmtc_nrc_weight_detail child_cmtc_nrc_weight_detail_t_admission_id_weight_date_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_weight_detail
    ADD CONSTRAINT child_cmtc_nrc_weight_detail_t_admission_id_weight_date_key UNIQUE (admission_id, weight_date);


--
-- TOC entry 5652 (class 2606 OID 230646)
-- Name: child_cmtc_nrc_weight_detail child_cmtc_nrc_weight_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_weight_detail
    ADD CONSTRAINT child_cmtc_nrc_weight_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5654 (class 2606 OID 230652)
-- Name: child_nutrition_cmam_followup_complication_rel child_nutrition_cmam_followup_complication_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_nutrition_cmam_followup_complication_rel
    ADD CONSTRAINT child_nutrition_cmam_followup_complication_rel_pkey PRIMARY KEY (cmam_id, medical_complications);


--
-- TOC entry 5656 (class 2606 OID 230656)
-- Name: child_nutrition_cmam_master child_nutrition_cmam_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_nutrition_cmam_master
    ADD CONSTRAINT child_nutrition_cmam_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5658 (class 2606 OID 230660)
-- Name: child_nutrition_sam_screening_master child_nutrition_sam_screening_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_nutrition_sam_screening_master
    ADD CONSTRAINT child_nutrition_sam_screening_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5801 (class 2606 OID 230668)
-- Name: location_wise_expected_target cm_dashboard_expected_target_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_wise_expected_target
    ADD CONSTRAINT cm_dashboard_expected_target_details_pkey PRIMARY KEY (location_id, financial_year);


--
-- TOC entry 5660 (class 2606 OID 230706)
-- Name: document_index document_index_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_index
    ADD CONSTRAINT document_index_pkey PRIMARY KEY (id);


--
-- TOC entry 5663 (class 2606 OID 230708)
-- Name: document_master document_master_file_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_master
    ADD CONSTRAINT document_master_file_name_key UNIQUE (file_name);


--
-- TOC entry 5665 (class 2606 OID 230710)
-- Name: document_master document_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_master
    ADD CONSTRAINT document_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5667 (class 2606 OID 230712)
-- Name: document_module_master document_module_master_module_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_module_master
    ADD CONSTRAINT document_module_master_module_name_key UNIQUE (module_name);


--
-- TOC entry 5669 (class 2606 OID 230714)
-- Name: document_module_master document_module_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.document_module_master
    ADD CONSTRAINT document_module_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5610 (class 2606 OID 230728)
-- Name: escalation_level_master escalation_level_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.escalation_level_master
    ADD CONSTRAINT escalation_level_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5671 (class 2606 OID 230730)
-- Name: escalation_level_role_rel escalation_level_role_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.escalation_level_role_rel
    ADD CONSTRAINT escalation_level_role_rel_pkey PRIMARY KEY (escalation_level_id, role_id);


--
-- TOC entry 5673 (class 2606 OID 230732)
-- Name: escalation_level_user_rel escalation_level_user_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.escalation_level_user_rel
    ADD CONSTRAINT escalation_level_user_rel_pkey PRIMARY KEY (escalation_level_id, user_id);


--
-- TOC entry 5675 (class 2606 OID 230734)
-- Name: event_configuration event_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.event_configuration
    ADD CONSTRAINT event_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 5677 (class 2606 OID 230736)
-- Name: event_configuration_type event_configuration_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.event_configuration_type
    ADD CONSTRAINT event_configuration_type_pkey PRIMARY KEY (id);


--
-- TOC entry 5679 (class 2606 OID 230738)
-- Name: event_mobile_configuration event_mobile_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.event_mobile_configuration
    ADD CONSTRAINT event_mobile_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 5682 (class 2606 OID 230740)
-- Name: event_mobile_notification_pending event_mobile_notification_pending_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.event_mobile_notification_pending
    ADD CONSTRAINT event_mobile_notification_pending_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5684 (class 2606 OID 230742)
-- Name: facility_performance_master facility_performance_master_health_infrastrucutre_id_perfor_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.facility_performance_master
    ADD CONSTRAINT facility_performance_master_health_infrastrucutre_id_perfor_key UNIQUE (health_infrastrucutre_id, performance_date);


--
-- TOC entry 5686 (class 2606 OID 230744)
-- Name: facility_performance_master facility_performance_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.facility_performance_master
    ADD CONSTRAINT facility_performance_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5720 (class 2606 OID 230748)
-- Name: imt_family family_id_1_unique_constraint; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family
    ADD CONSTRAINT family_id_1_unique_constraint UNIQUE (family_id);


--
-- TOC entry 5688 (class 2606 OID 230750)
-- Name: feature_display_name feature_display_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.feature_display_name
    ADD CONSTRAINT feature_display_name_pkey PRIMARY KEY (menu_id, feature_name);


--
-- TOC entry 5690 (class 2606 OID 230752)
-- Name: field_constant_master field_constant_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.field_constant_master
    ADD CONSTRAINT field_constant_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5692 (class 2606 OID 230754)
-- Name: field_value_master field_value_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.field_value_master
    ADD CONSTRAINT field_value_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5694 (class 2606 OID 230756)
-- Name: firebase_token firebase_token_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.firebase_token
    ADD CONSTRAINT firebase_token_data_pkey PRIMARY KEY (id);


--
-- TOC entry 5696 (class 2606 OID 230758)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

-- ALTER TABLE IF EXISTS ONLY public.flyway_schema_history
--     ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 5699 (class 2606 OID 230760)
-- Name: forgot_password_otp forgot_password_otp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.forgot_password_otp
    ADD CONSTRAINT forgot_password_otp_pkey PRIMARY KEY (user_id);


--
-- TOC entry 5701 (class 2606 OID 230762)
-- Name: form_master form_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.form_master
    ADD CONSTRAINT form_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5704 (class 2606 OID 230810)
-- Name: health_infrastructure_details health_infrastructure_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_details
    ADD CONSTRAINT health_infrastructure_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5708 (class 2606 OID 230812)
-- Name: health_infrastructure_lab_test_mapping health_infrastructure_lab_test_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_lab_test_mapping
    ADD CONSTRAINT health_infrastructure_lab_test_mapping_pkey PRIMARY KEY (id);


--
-- TOC entry 5710 (class 2606 OID 230814)
-- Name: health_infrastructure_monthly_volunteers_details health_infrastructure_monthly_volunteers_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_monthly_volunteers_details
    ADD CONSTRAINT health_infrastructure_monthly_volunteers_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5712 (class 2606 OID 230816)
-- Name: health_infrastructure_type_allowed_facilities health_infrastructure_type_allowed_facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_type_allowed_facilities
    ADD CONSTRAINT health_infrastructure_type_allowed_facilities_pkey PRIMARY KEY (id);


--
-- TOC entry 5714 (class 2606 OID 230818)
-- Name: health_infrastructure_type_location health_infrastructure_type_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_type_location
    ADD CONSTRAINT health_infrastructure_type_location_pkey PRIMARY KEY (id);


--
-- TOC entry 5718 (class 2606 OID 230820)
-- Name: health_infrastructure_ward_details_history health_infrastructure_ward_details_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_ward_details_history
    ADD CONSTRAINT health_infrastructure_ward_details_history_pkey PRIMARY KEY (id);


--
-- TOC entry 5716 (class 2606 OID 230822)
-- Name: health_infrastructure_ward_details health_infrastructure_ward_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_ward_details
    ADD CONSTRAINT health_infrastructure_ward_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5779 (class 2606 OID 230828)
-- Name: listvalue_field_role id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_role
    ADD CONSTRAINT id PRIMARY KEY (id);


--
-- TOC entry 5727 (class 2606 OID 230844)
-- Name: imt_family imt_family1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family
    ADD CONSTRAINT imt_family1_pkey PRIMARY KEY (id);


--
-- TOC entry 5730 (class 2606 OID 230846)
-- Name: imt_family_cfhc_done_by_details imt_family_cfhc_done_by_details_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_cfhc_done_by_details
    ADD CONSTRAINT imt_family_cfhc_done_by_details_t_pkey PRIMARY KEY (family_id);


--
-- TOC entry 6232 (class 2606 OID 232229)
-- Name: imt_family_location_change_detail imt_family_location_change_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_location_change_detail
    ADD CONSTRAINT imt_family_location_change_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5732 (class 2606 OID 230850)
-- Name: imt_family_migration_master imt_family_migration_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_migration_master
    ADD CONSTRAINT imt_family_migration_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5734 (class 2606 OID 230852)
-- Name: imt_family_state_detail imt_family_state_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_state_detail
    ADD CONSTRAINT imt_family_state_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5736 (class 2606 OID 230854)
-- Name: imt_family_vehicle_detail_rel imt_family_vehicle_detail_rel_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_vehicle_detail_rel
    ADD CONSTRAINT imt_family_vehicle_detail_rel_t_pkey PRIMARY KEY (family_id, vehicle_details);


--
-- TOC entry 5739 (class 2606 OID 230856)
-- Name: imt_family_verification imt_family_verification_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_family_verification
    ADD CONSTRAINT imt_family_verification_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5752 (class 2606 OID 230858)
-- Name: imt_member_cfhc_master imt_member_cfhc_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_cfhc_master
    ADD CONSTRAINT imt_member_cfhc_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5755 (class 2606 OID 230860)
-- Name: imt_member_chronic_disease_rel imt_member_chronic_disease_rel_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_chronic_disease_rel
    ADD CONSTRAINT imt_member_chronic_disease_rel_t_pkey PRIMARY KEY (member_id, chronic_disease_id);


--
-- TOC entry 5758 (class 2606 OID 230862)
-- Name: imt_member_congenital_anomaly_rel imt_member_congenital_anomaly_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_congenital_anomaly_rel
    ADD CONSTRAINT imt_member_congenital_anomaly_rel_pkey PRIMARY KEY (member_id, congenital_anomaly_id);


--
-- TOC entry 5761 (class 2606 OID 230864)
-- Name: imt_member_current_disease_rel imt_member_current_disease_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_current_disease_rel
    ADD CONSTRAINT imt_member_current_disease_rel_pkey PRIMARY KEY (member_id, current_disease_id);


--
-- TOC entry 5764 (class 2606 OID 230868)
-- Name: imt_member_eye_issue_rel imt_member_eye_issue_rel_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_eye_issue_rel
    ADD CONSTRAINT imt_member_eye_issue_rel_t_pkey PRIMARY KEY (member_id, eye_issue_id);


--
-- TOC entry 5766 (class 2606 OID 230872)
-- Name: imt_member_health_issue_rel imt_member_health_issue_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_health_issue_rel
    ADD CONSTRAINT imt_member_health_issue_rel_pkey PRIMARY KEY (member_id, health_issue_id);


--
-- TOC entry 5768 (class 2606 OID 230874)
-- Name: imt_member_previous_pregnancy_complication_rel imt_member_previous_pregnancy_complication_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_previous_pregnancy_complication_rel
    ADD CONSTRAINT imt_member_previous_pregnancy_complication_rel_pkey PRIMARY KEY (member_id, previous_pregnancy_complication);


--
-- TOC entry 5771 (class 2606 OID 230876)
-- Name: imt_member_state_detail imt_member_state_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member_state_detail
    ADD CONSTRAINT imt_member_state_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5748 (class 2606 OID 230878)
-- Name: imt_member imt_member_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.imt_member
    ADD CONSTRAINT imt_member_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5773 (class 2606 OID 230883)
-- Name: internationalization_label_master internationalization_label_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.internationalization_label_master
    ADD CONSTRAINT internationalization_label_master_pkey PRIMARY KEY (country, key, language, app_name);


--
-- TOC entry 5777 (class 2606 OID 230891)
-- Name: listvalue_field_master listvalue_field_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_master
    ADD CONSTRAINT listvalue_field_master_pkey PRIMARY KEY (field_key);


--
-- TOC entry 5781 (class 2606 OID 230893)
-- Name: listvalue_field_value_detail listvalue_field_value_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_value_detail
    ADD CONSTRAINT listvalue_field_value_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 5783 (class 2606 OID 230895)
-- Name: listvalue_form_master listvalue_form_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_form_master
    ADD CONSTRAINT listvalue_form_master_pkey PRIMARY KEY (form_key);


--
-- TOC entry 5785 (class 2606 OID 230901)
-- Name: location_cluster_master location_cluster_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_cluster_master
    ADD CONSTRAINT location_cluster_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5787 (class 2606 OID 230903)
-- Name: location_hierarchy_type location_hierarchy_type_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_hierarchy_type
    ADD CONSTRAINT location_hierarchy_type_master_pkey PRIMARY KEY (location_id, hierarchy_type);


--
-- TOC entry 5524 (class 2606 OID 230907)
-- Name: location_demographic_type_master location_hierarchy_type_master_pkey1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_demographic_type_master
    ADD CONSTRAINT location_hierarchy_type_master_pkey1 PRIMARY KEY (demographic_type, demographic_group);


--
-- TOC entry 5518 (class 2606 OID 230909)
-- Name: location_hierchy_closer_det location_hierchy_closer_det_parent_id_child_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_hierchy_closer_det
    ADD CONSTRAINT location_hierchy_closer_det_parent_id_child_id_key UNIQUE (parent_id, child_id);


--
-- TOC entry 5522 (class 2606 OID 230911)
-- Name: location_hierchy_closer_det location_hierchy_closer_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_hierchy_closer_det
    ADD CONSTRAINT location_hierchy_closer_det_pkey PRIMARY KEY (id);


--
-- TOC entry 5531 (class 2606 OID 230923)
-- Name: location_master location_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_master
    ADD CONSTRAINT location_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5534 (class 2606 OID 230926)
-- Name: location_master location_master_unique_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_master
    ADD CONSTRAINT location_master_unique_key UNIQUE (name, parent);


--
-- TOC entry 5793 (class 2606 OID 230973)
-- Name: location_mobile_feature_master location_mobile_feature_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_mobile_feature_master
    ADD CONSTRAINT location_mobile_feature_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5795 (class 2606 OID 230975)
-- Name: location_state_master location_state_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_state_master
    ADD CONSTRAINT location_state_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5797 (class 2606 OID 230977)
-- Name: location_type_master location_type_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_type_master
    ADD CONSTRAINT location_type_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5799 (class 2606 OID 230983)
-- Name: location_wise_analytics location_wise_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_wise_analytics
    ADD CONSTRAINT location_wise_analytics_pkey PRIMARY KEY (loc_id);


--
-- TOC entry 5805 (class 2606 OID 230985)
-- Name: logged_actions logged_actions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.logged_actions
    ADD CONSTRAINT logged_actions_pkey PRIMARY KEY (event_id);


--
-- TOC entry 5808 (class 2606 OID 230989)
-- Name: menu_config menu_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_config
    ADD CONSTRAINT menu_config_pkey PRIMARY KEY (id);


--
-- TOC entry 5810 (class 2606 OID 230991)
-- Name: menu_group menu_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_group
    ADD CONSTRAINT menu_group_pkey PRIMARY KEY (id);


--
-- TOC entry 5814 (class 2606 OID 230997)
-- Name: migration_master migration_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.migration_master
    ADD CONSTRAINT migration_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5816 (class 2606 OID 231001)
-- Name: mobile_beans_master mobile_bean_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_beans_master
    ADD CONSTRAINT mobile_bean_master_pkey PRIMARY KEY (bean);


--
-- TOC entry 5818 (class 2606 OID 231003)
-- Name: mobile_feature_master mobile_feature_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_feature_master
    ADD CONSTRAINT mobile_feature_master_pkey PRIMARY KEY (mobile_constant);


--
-- TOC entry 5820 (class 2606 OID 231005)
-- Name: mobile_form_details mobile_form_details_file_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_form_details
    ADD CONSTRAINT mobile_form_details_file_name_key UNIQUE (file_name);


--
-- TOC entry 5822 (class 2606 OID 231007)
-- Name: mobile_form_details mobile_form_details_form_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_form_details
    ADD CONSTRAINT mobile_form_details_form_name_key UNIQUE (form_name);


--
-- TOC entry 5824 (class 2606 OID 231009)
-- Name: mobile_form_details mobile_form_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_form_details
    ADD CONSTRAINT mobile_form_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5826 (class 2606 OID 231011)
-- Name: mobile_form_master mobile_form_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_form_master
    ADD CONSTRAINT mobile_form_master_pkey PRIMARY KEY (form_code, id);


--
-- TOC entry 6224 (class 2606 OID 232190)
-- Name: mobile_library_master mobile_library_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_library_master
    ADD CONSTRAINT mobile_library_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5828 (class 2606 OID 231015)
-- Name: mobile_menu_master mobile_menu_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_menu_master
    ADD CONSTRAINT mobile_menu_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5830 (class 2606 OID 231017)
-- Name: mobile_menu_role_relation mobile_menu_role_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_menu_role_relation
    ADD CONSTRAINT mobile_menu_role_relation_pkey PRIMARY KEY (role_id);


--
-- TOC entry 6228 (class 2606 OID 232206)
-- Name: mobile_number_filter_master mobile_number_filter_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_number_filter_master
    ADD CONSTRAINT mobile_number_filter_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5832 (class 2606 OID 231021)
-- Name: mobile_version_mapping mobile_version_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.mobile_version_mapping
    ADD CONSTRAINT mobile_version_mapping_pkey PRIMARY KEY (apk_version);


--
-- TOC entry 5612 (class 2606 OID 231157)
-- Name: notification_type_master notification_type_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.notification_type_master
    ADD CONSTRAINT notification_type_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5834 (class 2606 OID 231159)
-- Name: notification_type_role_rel notification_type_role_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.notification_type_role_rel
    ADD CONSTRAINT notification_type_role_rel_pkey PRIMARY KEY (notification_type_id, role_id);


--
-- TOC entry 5836 (class 2606 OID 231167)
-- Name: oauth_access_token oauth_access_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.oauth_access_token
    ADD CONSTRAINT oauth_access_token_pkey PRIMARY KEY (authentication_id);


--
-- TOC entry 5838 (class 2606 OID 231169)
-- Name: oauth_client_details oauth_client_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.oauth_client_details
    ADD CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id);


--
-- TOC entry 5840 (class 2606 OID 231171)
-- Name: oauth_client_token oauth_client_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.oauth_client_token
    ADD CONSTRAINT oauth_client_token_pkey PRIMARY KEY (authentication_id);


--
-- TOC entry 5775 (class 2606 OID 231173)
-- Name: internationalization_language_master pk_internationalization_language_master; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.internationalization_language_master
    ADD CONSTRAINT pk_internationalization_language_master PRIMARY KEY (code, country);


--
-- TOC entry 6209 (class 2606 OID 231175)
-- Name: user_health_infrastructure pkey_health_infra_user_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_health_infrastructure
    ADD CONSTRAINT pkey_health_infra_user_id PRIMARY KEY (id);


--
-- TOC entry 5842 (class 2606 OID 231179)
-- Name: query_analysis_details query_analysis_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.query_analysis_details
    ADD CONSTRAINT query_analysis_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5844 (class 2606 OID 231181)
-- Name: query_analytics query_analytics1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.query_analytics
    ADD CONSTRAINT query_analytics1_pkey PRIMARY KEY (id);


--
-- TOC entry 5846 (class 2606 OID 231183)
-- Name: query_history query_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.query_history
    ADD CONSTRAINT query_history_pkey PRIMARY KEY (id);


--
-- TOC entry 5848 (class 2606 OID 231197)
-- Name: rch_anc_dangerous_sign_rel rch_anc_dangerous_sign_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_anc_dangerous_sign_rel
    ADD CONSTRAINT rch_anc_dangerous_sign_rel_pkey PRIMARY KEY (anc_id, dangerous_sign_id);


--
-- TOC entry 5850 (class 2606 OID 231199)
-- Name: rch_anc_death_reason_rel rch_anc_death_reason_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_anc_death_reason_rel
    ADD CONSTRAINT rch_anc_death_reason_rel_pkey PRIMARY KEY (anc_id, death_reason);


--
-- TOC entry 5856 (class 2606 OID 231201)
-- Name: rch_anc_master rch_anc_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_anc_master
    ADD CONSTRAINT rch_anc_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5859 (class 2606 OID 231203)
-- Name: rch_anc_previous_pregnancy_complication_rel rch_anc_previous_pregnancy_complication_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_anc_previous_pregnancy_complication_rel
    ADD CONSTRAINT rch_anc_previous_pregnancy_complication_rel_pkey PRIMARY KEY (anc_id, previous_pregnancy_complication);


--
-- TOC entry 5861 (class 2606 OID 231205)
-- Name: rch_anemia_status_analytics rch_anemia_status_analytics_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_anemia_status_analytics
    ADD CONSTRAINT rch_anemia_status_analytics_t_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 5863 (class 2606 OID 231207)
-- Name: rch_asha_anc_master rch_asha_anc_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_master
    ADD CONSTRAINT rch_asha_anc_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5865 (class 2606 OID 231209)
-- Name: rch_asha_anc_morbidity_details rch_asha_anc_morbidity_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_morbidity_details
    ADD CONSTRAINT rch_asha_anc_morbidity_details_pkey PRIMARY KEY (morbidity_id, code);


--
-- TOC entry 5867 (class 2606 OID 231211)
-- Name: rch_asha_anc_morbidity_master rch_asha_anc_morbidity_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_morbidity_master
    ADD CONSTRAINT rch_asha_anc_morbidity_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5869 (class 2606 OID 231213)
-- Name: rch_asha_child_service_master rch_asha_child_service_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_child_service_master
    ADD CONSTRAINT rch_asha_child_service_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5871 (class 2606 OID 231215)
-- Name: rch_asha_cs_morbidity_details rch_asha_cs_morbidity_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_cs_morbidity_details
    ADD CONSTRAINT rch_asha_cs_morbidity_details_pkey PRIMARY KEY (morbidity_id, code);


--
-- TOC entry 5873 (class 2606 OID 231217)
-- Name: rch_asha_cs_morbidity_master rch_asha_cs_morbidity_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_cs_morbidity_master
    ADD CONSTRAINT rch_asha_cs_morbidity_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5875 (class 2606 OID 231219)
-- Name: rch_asha_lmp_follow_up rch_asha_lmp_follow_up_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_lmp_follow_up
    ADD CONSTRAINT rch_asha_lmp_follow_up_pkey PRIMARY KEY (id);


--
-- TOC entry 5877 (class 2606 OID 231221)
-- Name: rch_asha_member_services rch_asha_member_services_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_member_services
    ADD CONSTRAINT rch_asha_member_services_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5879 (class 2606 OID 231223)
-- Name: rch_asha_pnc_child_master rch_asha_pnc_child_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_master
    ADD CONSTRAINT rch_asha_pnc_child_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5881 (class 2606 OID 231225)
-- Name: rch_asha_pnc_child_morbidity_details rch_asha_pnc_child_morbidity_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_morbidity_details
    ADD CONSTRAINT rch_asha_pnc_child_morbidity_details_pkey PRIMARY KEY (morbidity_id, code);


--
-- TOC entry 5883 (class 2606 OID 231227)
-- Name: rch_asha_pnc_child_morbidity_master rch_asha_pnc_child_morbidity_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_morbidity_master
    ADD CONSTRAINT rch_asha_pnc_child_morbidity_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5885 (class 2606 OID 231229)
-- Name: rch_asha_pnc_master rch_asha_pnc_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_master
    ADD CONSTRAINT rch_asha_pnc_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5887 (class 2606 OID 231231)
-- Name: rch_asha_pnc_mother_master rch_asha_pnc_mother_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_master
    ADD CONSTRAINT rch_asha_pnc_mother_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5889 (class 2606 OID 231233)
-- Name: rch_asha_pnc_mother_morbidity_details rch_asha_pnc_mother_morbidity_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_morbidity_details
    ADD CONSTRAINT rch_asha_pnc_mother_morbidity_details_pkey PRIMARY KEY (morbidity_id, code);


--
-- TOC entry 5891 (class 2606 OID 231235)
-- Name: rch_asha_pnc_mother_morbidity_master rch_asha_pnc_mother_morbidity_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_morbidity_master
    ADD CONSTRAINT rch_asha_pnc_mother_morbidity_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5893 (class 2606 OID 231237)
-- Name: rch_asha_pnc_mother_problem_present rch_asha_pnc_mother_problem_present_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_problem_present
    ADD CONSTRAINT rch_asha_pnc_mother_problem_present_pkey PRIMARY KEY (pnc_id, problem);


--
-- TOC entry 5895 (class 2606 OID 231239)
-- Name: rch_asha_reported_event_master rch_asha_reported_event_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_reported_event_master
    ADD CONSTRAINT rch_asha_reported_event_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5897 (class 2606 OID 231241)
-- Name: rch_asha_wpd_master rch_asha_wpd_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_wpd_master
    ADD CONSTRAINT rch_asha_wpd_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5899 (class 2606 OID 231243)
-- Name: rch_child_cerebral_palsy_master rch_child_cerebral_palsy_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_cerebral_palsy_master
    ADD CONSTRAINT rch_child_cerebral_palsy_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5902 (class 2606 OID 231245)
-- Name: rch_child_cp_suspects rch_child_cp_suspects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_cp_suspects
    ADD CONSTRAINT rch_child_cp_suspects_pkey PRIMARY KEY (id);


--
-- TOC entry 5904 (class 2606 OID 231247)
-- Name: rch_child_service_death_reason_rel rch_child_service_death_reason_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_service_death_reason_rel
    ADD CONSTRAINT rch_child_service_death_reason_rel_pkey PRIMARY KEY (child_service_id, child_death_reason);


--
-- TOC entry 5906 (class 2606 OID 231249)
-- Name: rch_child_service_diseases_rel rch_child_service_diseases_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_service_diseases_rel
    ADD CONSTRAINT rch_child_service_diseases_rel_pkey PRIMARY KEY (child_service_id, diseases);


--
-- TOC entry 5912 (class 2606 OID 231251)
-- Name: rch_child_service_master rch_child_service_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_child_service_master
    ADD CONSTRAINT rch_child_service_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5914 (class 2606 OID 231253)
-- Name: rch_current_state_pregnancy_analytics_data rch_current_state_pregnancy_analytics_data_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_current_state_pregnancy_analytics_data
    ADD CONSTRAINT rch_current_state_pregnancy_analytics_data_t_pkey PRIMARY KEY (location_id);


--
-- TOC entry 5916 (class 2606 OID 231255)
-- Name: rch_dashboard_analytics rch_dashboard_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_dashboard_analytics
    ADD CONSTRAINT rch_dashboard_analytics_pkey PRIMARY KEY (financial_year, location_id);


--
-- TOC entry 5918 (class 2606 OID 231257)
-- Name: rch_data_migration rch_data_migration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_data_migration
    ADD CONSTRAINT rch_data_migration_pkey PRIMARY KEY (id);


--
-- TOC entry 5920 (class 2606 OID 231259)
-- Name: rch_delivery_date_base_location_wise_data_point rch_delivery_date_base_location_wise_data_point_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_delivery_date_base_location_wise_data_point
    ADD CONSTRAINT rch_delivery_date_base_location_wise_data_point_t_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 5922 (class 2606 OID 231261)
-- Name: rch_eligible_couple_location_wise_count_anlytics_detail rch_eligible_couple_location_wise_count_anlytics_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_eligible_couple_location_wise_count_anlytics_detail
    ADD CONSTRAINT rch_eligible_couple_location_wise_count_anlytics_detail_t_pkey PRIMARY KEY (location_id, age_group_or_child_cnt);


--
-- TOC entry 5927 (class 2606 OID 231263)
-- Name: rch_immunisation_master rch_immunisation_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_immunisation_master
    ADD CONSTRAINT rch_immunisation_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5931 (class 2606 OID 231265)
-- Name: rch_institution_master rch_institution_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_institution_master
    ADD CONSTRAINT rch_institution_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5933 (class 2606 OID 231273)
-- Name: rch_lmp_base_location_wise_data_point rch_lmp_base_location_wise_data_point_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_lmp_base_location_wise_data_point
    ADD CONSTRAINT rch_lmp_base_location_wise_data_point_t_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 5940 (class 2606 OID 231275)
-- Name: rch_lmp_follow_up rch_lmp_follow_up_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_lmp_follow_up
    ADD CONSTRAINT rch_lmp_follow_up_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5942 (class 2606 OID 231277)
-- Name: rch_member_data_sync_pending rch_member_data_sync_pending_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_member_data_sync_pending
    ADD CONSTRAINT rch_member_data_sync_pending_pkey PRIMARY KEY (id);


--
-- TOC entry 5944 (class 2606 OID 231279)
-- Name: rch_member_death_deatil_reason rch_member_death_deatil_reason_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_member_death_deatil_reason
    ADD CONSTRAINT rch_member_death_deatil_reason_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5946 (class 2606 OID 231283)
-- Name: rch_member_pregnancy_status rch_member_pregnancy_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_member_pregnancy_status
    ADD CONSTRAINT rch_member_pregnancy_status_pkey PRIMARY KEY (member_id);


--
-- TOC entry 5950 (class 2606 OID 231285)
-- Name: rch_member_services_last_90_days rch_member_services_last_90_days_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_member_services_last_90_days
    ADD CONSTRAINT rch_member_services_last_90_days_pkey PRIMARY KEY (id);


--
-- TOC entry 5952 (class 2606 OID 231287)
-- Name: rch_opd_edl_details rch_opd_edl_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_edl_details
    ADD CONSTRAINT rch_opd_edl_details_pkey PRIMARY KEY (id);


--
-- TOC entry 5954 (class 2606 OID 231291)
-- Name: rch_opd_lab_test_master rch_opd_lab_test_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_lab_test_master
    ADD CONSTRAINT rch_opd_lab_test_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5956 (class 2606 OID 231293)
-- Name: rch_opd_lab_test_provisional_rel rch_opd_lab_test_provisional_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_lab_test_provisional_rel
    ADD CONSTRAINT rch_opd_lab_test_provisional_rel_pkey PRIMARY KEY (opd_member_master_id, provisional_id);


--
-- TOC entry 5958 (class 2606 OID 231295)
-- Name: rch_opd_member_master rch_opd_member_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_member_master
    ADD CONSTRAINT rch_opd_member_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5960 (class 2606 OID 231297)
-- Name: rch_opd_member_registration rch_opd_member_registration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_opd_member_registration
    ADD CONSTRAINT rch_opd_member_registration_pkey PRIMARY KEY (id);


--
-- TOC entry 5962 (class 2606 OID 231299)
-- Name: rch_other_form_master rch_other_form_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_other_form_master
    ADD CONSTRAINT rch_other_form_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5964 (class 2606 OID 231301)
-- Name: rch_pmsma_service_statistics_during_year rch_pmsma_service_statistics_during_year_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pmsma_service_statistics_during_year
    ADD CONSTRAINT rch_pmsma_service_statistics_during_year_pkey PRIMARY KEY (location_id, financial_year);


--
-- TOC entry 5966 (class 2606 OID 231303)
-- Name: rch_pmsma_survey_master rch_pmsma_survey_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pmsma_survey_master
    ADD CONSTRAINT rch_pmsma_survey_master_pkey PRIMARY KEY (id);


--
-- TOC entry 5968 (class 2606 OID 231305)
-- Name: rch_pnc_child_danger_signs_rel rch_pnc_child_danger_signs_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_child_danger_signs_rel
    ADD CONSTRAINT rch_pnc_child_danger_signs_rel_pkey PRIMARY KEY (child_pnc_id, child_danger_signs);


--
-- TOC entry 5970 (class 2606 OID 231307)
-- Name: rch_pnc_child_death_reason_rel rch_pnc_child_death_reason_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_child_death_reason_rel
    ADD CONSTRAINT rch_pnc_child_death_reason_rel_pkey PRIMARY KEY (child_pnc_id, child_death_reason);


--
-- TOC entry 5973 (class 2606 OID 231309)
-- Name: rch_pnc_child_master rch_pnc_child_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_child_master
    ADD CONSTRAINT rch_pnc_child_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5976 (class 2606 OID 231311)
-- Name: rch_pnc_family_planning_methods_rel rch_pnc_family_planning_methods_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_family_planning_methods_rel
    ADD CONSTRAINT rch_pnc_family_planning_methods_rel_pkey PRIMARY KEY (mother_pnc_id, family_planning_methods);


--
-- TOC entry 5981 (class 2606 OID 231313)
-- Name: rch_pnc_master rch_pnc_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_master
    ADD CONSTRAINT rch_pnc_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5984 (class 2606 OID 231315)
-- Name: rch_pnc_mother_danger_signs_rel rch_pnc_mother_danger_signs_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_mother_danger_signs_rel
    ADD CONSTRAINT rch_pnc_mother_danger_signs_rel_pkey PRIMARY KEY (mother_pnc_id, mother_danger_signs);


--
-- TOC entry 5986 (class 2606 OID 231317)
-- Name: rch_pnc_mother_death_reason_rel rch_pnc_mother_death_reason_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_mother_death_reason_rel
    ADD CONSTRAINT rch_pnc_mother_death_reason_rel_pkey PRIMARY KEY (mother_pnc_id, mother_death_reason);


--
-- TOC entry 5989 (class 2606 OID 231319)
-- Name: rch_pnc_mother_master rch_pnc_mother_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pnc_mother_master
    ADD CONSTRAINT rch_pnc_mother_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 5998 (class 2606 OID 231321)
-- Name: rch_pregnancy_registration_det rch_pregnancy_registration_det_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_pregnancy_registration_det
    ADD CONSTRAINT rch_pregnancy_registration_det_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6000 (class 2606 OID 231323)
-- Name: rch_sam_screening_diseases_rel rch_sam_screening_diseases_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_sam_screening_diseases_rel
    ADD CONSTRAINT rch_sam_screening_diseases_rel_pkey PRIMARY KEY (sam_screening_id, diseases);


--
-- TOC entry 6002 (class 2606 OID 231325)
-- Name: rch_sam_screening_master rch_sam_screening_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_sam_screening_master
    ADD CONSTRAINT rch_sam_screening_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6004 (class 2606 OID 231327)
-- Name: rch_service_provided_during_year rch_service_provided_during_year_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_service_provided_during_year
    ADD CONSTRAINT rch_service_provided_during_year_t_pkey PRIMARY KEY (location_id, month_year);


--
-- TOC entry 6006 (class 2606 OID 231329)
-- Name: rch_vaccine_adverse_effect rch_vaccine_adverse_effect_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_vaccine_adverse_effect
    ADD CONSTRAINT rch_vaccine_adverse_effect_pkey PRIMARY KEY (id);


--
-- TOC entry 6008 (class 2606 OID 231331)
-- Name: rch_wpd_child_congential_deformity_rel rch_wpd_child_congential_deformity_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_child_congential_deformity_rel
    ADD CONSTRAINT rch_wpd_child_congential_deformity_rel_pkey PRIMARY KEY (wpd_id, congential_deformity);


--
-- TOC entry 6010 (class 2606 OID 231333)
-- Name: rch_wpd_child_danger_signs_rel rch_wpd_child_danger_signs_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_child_danger_signs_rel
    ADD CONSTRAINT rch_wpd_child_danger_signs_rel_pkey PRIMARY KEY (wpd_id, danger_signs);


--
-- TOC entry 6012 (class 2606 OID 231335)
-- Name: rch_wpd_child_high_risk_symptoms_rel rch_wpd_child_high_risk_symptoms_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_child_high_risk_symptoms_rel
    ADD CONSTRAINT rch_wpd_child_high_risk_symptoms_rel_pkey PRIMARY KEY (wpd_id, high_risk_symptoms);


--
-- TOC entry 6015 (class 2606 OID 231337)
-- Name: rch_wpd_child_master rch_wpd_child_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_child_master
    ADD CONSTRAINT rch_wpd_child_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6018 (class 2606 OID 231339)
-- Name: rch_wpd_child_treatments_rel rch_wpd_child_treatments_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_child_treatments_rel
    ADD CONSTRAINT rch_wpd_child_treatments_rel_pkey PRIMARY KEY (wpd_id, treatments);


--
-- TOC entry 6020 (class 2606 OID 231341)
-- Name: rch_wpd_mother_danger_signs_rel rch_wpd_mother_danger_signs_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_mother_danger_signs_rel
    ADD CONSTRAINT rch_wpd_mother_danger_signs_rel_pkey PRIMARY KEY (wpd_id, mother_danger_signs);


--
-- TOC entry 6022 (class 2606 OID 231343)
-- Name: rch_wpd_mother_death_reason_rel rch_wpd_mother_death_reason_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_mother_death_reason_rel
    ADD CONSTRAINT rch_wpd_mother_death_reason_rel_pkey PRIMARY KEY (wpd_id, mother_death_reason);


--
-- TOC entry 6024 (class 2606 OID 231345)
-- Name: rch_wpd_mother_high_risk_rel rch_wpd_mother_high_risk_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_mother_high_risk_rel
    ADD CONSTRAINT rch_wpd_mother_high_risk_rel_pkey PRIMARY KEY (wpd_id, mother_high_risk);


--
-- TOC entry 6031 (class 2606 OID 231347)
-- Name: rch_wpd_mother_master rch_wpd_mother_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_mother_master
    ADD CONSTRAINT rch_wpd_mother_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6034 (class 2606 OID 231349)
-- Name: rch_wpd_mother_treatment_rel rch_wpd_mother_treatment_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_wpd_mother_treatment_rel
    ADD CONSTRAINT rch_wpd_mother_treatment_rel_pkey PRIMARY KEY (wpd_id, mother_treatment);


--
-- TOC entry 6036 (class 2606 OID 231351)
-- Name: rch_yearly_location_wise_analytics_data rch_yearly_location_wise_analytics_data_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_yearly_location_wise_analytics_data
    ADD CONSTRAINT rch_yearly_location_wise_analytics_data_t_pkey PRIMARY KEY (location_id, financial_year, month_year);


--
-- TOC entry 6038 (class 2606 OID 231353)
-- Name: report_master report_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_master
    ADD CONSTRAINT report_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6040 (class 2606 OID 231355)
-- Name: report_offline_details report_offline_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_offline_details
    ADD CONSTRAINT report_offline_details_pkey PRIMARY KEY (id);


--
-- TOC entry 6042 (class 2606 OID 231357)
-- Name: report_parameter_master report_parameter_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_parameter_master
    ADD CONSTRAINT report_parameter_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6044 (class 2606 OID 231359)
-- Name: report_query_master report_query_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_query_master
    ADD CONSTRAINT report_query_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6046 (class 2606 OID 231361)
-- Name: request_response_details_master request_response_details_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_details_master
    ADD CONSTRAINT request_response_details_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6048 (class 2606 OID 231365)
-- Name: request_response_navigation_state_mapping request_response_navigation_state_mapping_navigation_state_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_navigation_state_mapping
    ADD CONSTRAINT request_response_navigation_state_mapping_navigation_state_key UNIQUE (navigation_state);


--
-- TOC entry 6050 (class 2606 OID 231367)
-- Name: request_response_navigation_state_mapping request_response_navigation_state_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_navigation_state_mapping
    ADD CONSTRAINT request_response_navigation_state_mapping_pkey PRIMARY KEY (id);


--
-- TOC entry 6052 (class 2606 OID 231373)
-- Name: request_response_page_wise_time_details request_response_page_wise_time_details_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_page_wise_time_details
    ADD CONSTRAINT request_response_page_wise_time_details_pk PRIMARY KEY (id);


--
-- TOC entry 6054 (class 2606 OID 231375)
-- Name: request_response_regex_list_to_be_ignored request_response_regex_list_to_be_ignored_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_regex_list_to_be_ignored
    ADD CONSTRAINT request_response_regex_list_to_be_ignored_pkey PRIMARY KEY (regex_pattern);


--
-- TOC entry 6056 (class 2606 OID 231377)
-- Name: request_response_url_mapping request_response_url_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_url_mapping
    ADD CONSTRAINT request_response_url_mapping_pkey PRIMARY KEY (id);


--
-- TOC entry 6058 (class 2606 OID 231379)
-- Name: request_response_url_mapping request_response_url_mapping_url_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.request_response_url_mapping
    ADD CONSTRAINT request_response_url_mapping_url_key UNIQUE (url);


--
-- TOC entry 6060 (class 2606 OID 231383)
-- Name: response_analysis_by_time_details response_analysis_by_time_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.response_analysis_by_time_details
    ADD CONSTRAINT response_analysis_by_time_details_pkey PRIMARY KEY (id);


--
-- TOC entry 6062 (class 2606 OID 231385)
-- Name: role_hierarchy_management role_hierarchy_management_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_hierarchy_management
    ADD CONSTRAINT role_hierarchy_management_pkey PRIMARY KEY (id);


--
-- TOC entry 6064 (class 2606 OID 231387)
-- Name: role_management role_management_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_management
    ADD CONSTRAINT role_management_pkey PRIMARY KEY (id);


--
-- TOC entry 6066 (class 2606 OID 231394)
-- Name: school_master school_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.school_master
    ADD CONSTRAINT school_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6068 (class 2606 OID 231396)
-- Name: sd_score_master sd_score_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sd_score_master
    ADD CONSTRAINT sd_score_master_pkey PRIMARY KEY (height, gender);


--
-- TOC entry 6070 (class 2606 OID 231398)
-- Name: server_list_master server_list_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.server_list_master
    ADD CONSTRAINT server_list_master_pkey PRIMARY KEY (server_name);


--
-- TOC entry 6072 (class 2606 OID 231400)
-- Name: sickle_cell_screening sickle_cell_screening_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sickle_cell_screening
    ADD CONSTRAINT sickle_cell_screening_pkey PRIMARY KEY (id);


--
-- TOC entry 6077 (class 2606 OID 231402)
-- Name: sms_block_master sms_block_master_mobile_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_block_master
    ADD CONSTRAINT sms_block_master_mobile_number_key UNIQUE (mobile_number);


--
-- TOC entry 6079 (class 2606 OID 231404)
-- Name: sms_block_master sms_block_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_block_master
    ADD CONSTRAINT sms_block_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6074 (class 2606 OID 231406)
-- Name: sms sms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms
    ADD CONSTRAINT sms_pkey PRIMARY KEY (id);


--
-- TOC entry 6081 (class 2606 OID 231408)
-- Name: sms_queue sms_queue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_queue
    ADD CONSTRAINT sms_queue_pkey PRIMARY KEY (id);


--
-- TOC entry 6083 (class 2606 OID 231410)
-- Name: sms_response sms_response_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sms_response
    ADD CONSTRAINT sms_response_pkey PRIMARY KEY (a2wackid);


--
-- TOC entry 6222 (class 2606 OID 232178)
-- Name: soh_element_module_master soh_element_module_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.soh_element_module_master
    ADD CONSTRAINT soh_element_module_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6085 (class 2606 OID 231442)
-- Name: sync_server_feature_mapping_detail sync_server_feature_mapping_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sync_server_feature_mapping_detail
    ADD CONSTRAINT sync_server_feature_mapping_detail_pkey PRIMARY KEY (server_id, feature_uuid);


--
-- TOC entry 6089 (class 2606 OID 231444)
-- Name: system_build_history system_build_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_build_history
    ADD CONSTRAINT system_build_history_pkey PRIMARY KEY (id);


--
-- TOC entry 6091 (class 2606 OID 231450)
-- Name: system_code_master system_code_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_code_master
    ADD CONSTRAINT system_code_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6093 (class 2606 OID 231452)
-- Name: system_code_master system_code_master_ukey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_code_master
    ADD CONSTRAINT system_code_master_ukey UNIQUE (table_id, table_type, code_type);


--
-- TOC entry 6087 (class 2606 OID 231454)
-- Name: sync_system_configuration_master system_config_sync_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.sync_system_configuration_master
    ADD CONSTRAINT system_config_sync_pkey PRIMARY KEY (id, feature_type);


--
-- TOC entry 6095 (class 2606 OID 231456)
-- Name: system_configuration system_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_configuration
    ADD CONSTRAINT system_configuration_pkey PRIMARY KEY (system_key);


--
-- TOC entry 6097 (class 2606 OID 231460)
-- Name: system_constraint_field_master system_constraint_field_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_field_master
    ADD CONSTRAINT system_constraint_field_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6099 (class 2606 OID 231462)
-- Name: system_constraint_field_value_master system_constraint_field_value_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_field_value_master
    ADD CONSTRAINT system_constraint_field_value_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6101 (class 2606 OID 231464)
-- Name: system_constraint_form_master system_constraint_form_master_form_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_form_master
    ADD CONSTRAINT system_constraint_form_master_form_code_key UNIQUE (form_code);


--
-- TOC entry 6103 (class 2606 OID 231466)
-- Name: system_constraint_form_master system_constraint_form_master_form_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_form_master
    ADD CONSTRAINT system_constraint_form_master_form_name_key UNIQUE (form_name);


--
-- TOC entry 6105 (class 2606 OID 231468)
-- Name: system_constraint_form_master system_constraint_form_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_form_master
    ADD CONSTRAINT system_constraint_form_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6107 (class 2606 OID 231470)
-- Name: system_constraint_form_version system_constraint_form_version_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_form_version
    ADD CONSTRAINT system_constraint_form_version_pkey PRIMARY KEY (id);


--
-- TOC entry 6109 (class 2606 OID 231472)
-- Name: system_constraint_standard_field_mapping_master system_constraint_standard_field_mapping_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_standard_field_mapping_master
    ADD CONSTRAINT system_constraint_standard_field_mapping_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6111 (class 2606 OID 231474)
-- Name: system_constraint_standard_field_master system_constraint_standard_field_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_standard_field_master
    ADD CONSTRAINT system_constraint_standard_field_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6113 (class 2606 OID 231476)
-- Name: system_constraint_standard_field_value_master system_constraint_standard_field_value_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_constraint_standard_field_value_master
    ADD CONSTRAINT system_constraint_standard_field_value_master_pkey PRIMARY KEY (uuid);


--
-- TOC entry 6115 (class 2606 OID 231482)
-- Name: system_function_master system_function_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_function_master
    ADD CONSTRAINT system_function_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6117 (class 2606 OID 231484)
-- Name: system_sync_status system_sync_status1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.system_sync_status
    ADD CONSTRAINT system_sync_status1_pkey PRIMARY KEY (id);


--
-- TOC entry 6123 (class 2606 OID 231507)
-- Name: techo_notification_location_change_detail techo_notification_location_change_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_notification_location_change_detail
    ADD CONSTRAINT techo_notification_location_change_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6129 (class 2606 OID 231515)
-- Name: techo_notification_master techo_notification_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_notification_master
    ADD CONSTRAINT techo_notification_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6133 (class 2606 OID 231520)
-- Name: techo_notification_state_detail techo_notification_state_detail_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_notification_state_detail
    ADD CONSTRAINT techo_notification_state_detail_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6135 (class 2606 OID 231522)
-- Name: techo_push_notification_config_master techo_push_notification_config_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_config_master
    ADD CONSTRAINT techo_push_notification_config_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6137 (class 2606 OID 231524)
-- Name: techo_push_notification_location_detail techo_push_notification_location_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_location_detail
    ADD CONSTRAINT techo_push_notification_location_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 6139 (class 2606 OID 231526)
-- Name: techo_push_notification_master techo_push_notification_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_master
    ADD CONSTRAINT techo_push_notification_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6141 (class 2606 OID 231528)
-- Name: techo_push_notification_role_user_detail techo_push_notification_role_user_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_role_user_detail
    ADD CONSTRAINT techo_push_notification_role_user_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 6143 (class 2606 OID 231530)
-- Name: techo_push_notification_type techo_push_notification_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_type
    ADD CONSTRAINT techo_push_notification_type_pkey PRIMARY KEY (id);


--
-- TOC entry 6145 (class 2606 OID 231532)
-- Name: techo_push_notification_type techo_push_notification_type_type_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_type
    ADD CONSTRAINT techo_push_notification_type_type_key UNIQUE (type);


--
-- TOC entry 6148 (class 2606 OID 231534)
-- Name: techo_web_notification_master techo_web_notification_master_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_web_notification_master
    ADD CONSTRAINT techo_web_notification_master_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6150 (class 2606 OID 231536)
-- Name: techo_web_notification_response_det techo_web_notification_response_det_t_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_web_notification_response_det
    ADD CONSTRAINT techo_web_notification_response_det_t_pkey PRIMARY KEY (id);


--
-- TOC entry 6152 (class 2606 OID 231540)
-- Name: timer_event timer_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.timer_event
    ADD CONSTRAINT timer_event_pkey PRIMARY KEY (id);


--
-- TOC entry 6154 (class 2606 OID 231542)
-- Name: tr_attendance_master tr_attendance_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_attendance_master
    ADD CONSTRAINT tr_attendance_master_pkey PRIMARY KEY (attendance_id);


--
-- TOC entry 6156 (class 2606 OID 231544)
-- Name: tr_certificate_master tr_certificate_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_certificate_master
    ADD CONSTRAINT tr_certificate_master_pkey PRIMARY KEY (certificate_id);


--
-- TOC entry 6158 (class 2606 OID 231546)
-- Name: tr_course_master tr_course_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_course_master
    ADD CONSTRAINT tr_course_master_pkey PRIMARY KEY (course_id);


--
-- TOC entry 6160 (class 2606 OID 231548)
-- Name: tr_lesson_analytics tr_lesson_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_lesson_analytics
    ADD CONSTRAINT tr_lesson_analytics_pkey PRIMARY KEY (id);


--
-- TOC entry 6162 (class 2606 OID 231550)
-- Name: tr_mobile_event_master tr_mobile_event_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_mobile_event_master
    ADD CONSTRAINT tr_mobile_event_master_pkey PRIMARY KEY (checksum);


--
-- TOC entry 6174 (class 2606 OID 231552)
-- Name: tr_topic_media_master tr_online_topic_video_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_media_master
    ADD CONSTRAINT tr_online_topic_video_pkey PRIMARY KEY (id);


--
-- TOC entry 6164 (class 2606 OID 231554)
-- Name: tr_question_bank_configuration tr_question_bank_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_bank_configuration
    ADD CONSTRAINT tr_question_bank_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 6166 (class 2606 OID 231556)
-- Name: tr_question_set_answer tr_question_set_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_set_answer
    ADD CONSTRAINT tr_question_set_answer_pkey PRIMARY KEY (id);


--
-- TOC entry 6168 (class 2606 OID 231558)
-- Name: tr_question_set_configuration tr_question_set_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_question_set_configuration
    ADD CONSTRAINT tr_question_set_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 6170 (class 2606 OID 231560)
-- Name: tr_topic_coverage_master tr_topic_coverage_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_coverage_master
    ADD CONSTRAINT tr_topic_coverage_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6172 (class 2606 OID 231562)
-- Name: tr_topic_master tr_topic_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_topic_master
    ADD CONSTRAINT tr_topic_master_pkey PRIMARY KEY (topic_id);


--
-- TOC entry 6176 (class 2606 OID 231564)
-- Name: tr_training_course_rel tr_training_course_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_course_rel
    ADD CONSTRAINT tr_training_course_rel_pkey PRIMARY KEY (training_id, course_id);


--
-- TOC entry 6178 (class 2606 OID 231566)
-- Name: tr_training_master tr_training_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_master
    ADD CONSTRAINT tr_training_master_pkey PRIMARY KEY (training_id);


--
-- TOC entry 6180 (class 2606 OID 231568)
-- Name: tr_training_org_unit_rel tr_training_org_unit_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_org_unit_rel
    ADD CONSTRAINT tr_training_org_unit_rel_pkey PRIMARY KEY (training_id, org_unit_id);


--
-- TOC entry 6182 (class 2606 OID 231570)
-- Name: tr_training_primary_trainer_rel tr_training_primary_trainer_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_primary_trainer_rel
    ADD CONSTRAINT tr_training_primary_trainer_rel_pkey PRIMARY KEY (training_id, primary_trainer_id);


--
-- TOC entry 6184 (class 2606 OID 231572)
-- Name: tr_training_target_role_rel tr_training_target_role_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_target_role_rel
    ADD CONSTRAINT tr_training_target_role_rel_pkey PRIMARY KEY (training_id, target_role_id);


--
-- TOC entry 6186 (class 2606 OID 231574)
-- Name: tr_training_trainer_role_rel tr_training_trainer_role_rel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_trainer_role_rel
    ADD CONSTRAINT tr_training_trainer_role_rel_pkey PRIMARY KEY (training_id, role_id);


--
-- TOC entry 6188 (class 2606 OID 231576)
-- Name: tr_user_meta_data tr_user_meta_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_user_meta_data
    ADD CONSTRAINT tr_user_meta_data_pkey PRIMARY KEY (id);


--
-- TOC entry 5536 (class 2606 OID 231578)
-- Name: um_user uk_um_user_user_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user
    ADD CONSTRAINT uk_um_user_user_name UNIQUE (user_name);


--
-- TOC entry 5544 (class 2606 OID 231580)
-- Name: um_role_master um_role_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_role_master
    ADD CONSTRAINT um_role_master_pkey PRIMARY KEY (id);


--
-- TOC entry 6190 (class 2606 OID 231582)
-- Name: um_user_activation_status um_user_activation_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_activation_status
    ADD CONSTRAINT um_user_activation_status_pkey PRIMARY KEY (id);


--
-- TOC entry 6192 (class 2606 OID 231584)
-- Name: um_user_app_access_details um_user_app_access_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_app_access_details
    ADD CONSTRAINT um_user_app_access_details_pkey PRIMARY KEY (id);


--
-- TOC entry 6194 (class 2606 OID 231586)
-- Name: um_user_attendance_info um_user_attendance_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_attendance_info
    ADD CONSTRAINT um_user_attendance_info_pkey PRIMARY KEY (id);


--
-- TOC entry 6196 (class 2606 OID 231588)
-- Name: um_user_help_details um_user_help_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_help_details
    ADD CONSTRAINT um_user_help_details_pkey PRIMARY KEY (id);


--
-- TOC entry 6198 (class 2606 OID 231590)
-- Name: um_user_location_change_detail um_user_location_change_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_location_change_detail
    ADD CONSTRAINT um_user_location_change_detail_pkey PRIMARY KEY (user_id, location_id, role_id, activation_date);


--
-- TOC entry 5547 (class 2606 OID 231592)
-- Name: um_user_location um_user_location_loc_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_location
    ADD CONSTRAINT um_user_location_loc_id_user_id_key UNIQUE (loc_id, user_id);


--
-- TOC entry 5549 (class 2606 OID 231594)
-- Name: um_user_location um_user_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_location
    ADD CONSTRAINT um_user_location_pkey PRIMARY KEY (id);


--
-- TOC entry 6200 (class 2606 OID 231596)
-- Name: um_user_login_det um_user_login_det_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_login_det
    ADD CONSTRAINT um_user_login_det_pkey PRIMARY KEY (id);


--
-- TOC entry 5538 (class 2606 OID 231598)
-- Name: um_user um_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user
    ADD CONSTRAINT um_user_pkey PRIMARY KEY (id);


--
-- TOC entry 6203 (class 2606 OID 231600)
-- Name: uncaught_exception_mobile uncaught_exception_mobile1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.uncaught_exception_mobile
    ADD CONSTRAINT uncaught_exception_mobile1_pkey PRIMARY KEY (id);


--
-- TOC entry 5614 (class 2606 OID 231602)
-- Name: notification_type_master unique_code_notification_type_master; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.notification_type_master
    ADD CONSTRAINT unique_code_notification_type_master UNIQUE (code);


--
-- TOC entry 5706 (class 2606 OID 231604)
-- Name: health_infrastructure_details unique_hfr_facility_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.health_infrastructure_details
    ADD CONSTRAINT unique_hfr_facility_id UNIQUE (hfr_facility_id);


--
-- TOC entry 5620 (class 2606 OID 231606)
-- Name: anganwadi_master unique_icds; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.anganwadi_master
    ADD CONSTRAINT unique_icds UNIQUE (icds_code);


--
-- TOC entry 6205 (class 2606 OID 231610)
-- Name: user_basket_preference user_basket_preference_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_basket_preference
    ADD CONSTRAINT user_basket_preference_pkey PRIMARY KEY (id);


--
-- TOC entry 6207 (class 2606 OID 231612)
-- Name: user_form_access user_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_form_access
    ADD CONSTRAINT user_form_pkey PRIMARY KEY (user_id, form_code);


--
-- TOC entry 6211 (class 2606 OID 231614)
-- Name: user_input_duration_detail user_input_duration_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_input_duration_detail
    ADD CONSTRAINT user_input_duration_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 6213 (class 2606 OID 231616)
-- Name: user_installed_apps user_installed_apps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_installed_apps
    ADD CONSTRAINT user_installed_apps_pkey PRIMARY KEY (id);


--
-- TOC entry 6230 (class 2606 OID 232218)
-- Name: user_menu_item_change_log user_menu_item_change_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_menu_item_change_log
    ADD CONSTRAINT user_menu_item_change_log_pkey PRIMARY KEY (id);


--
-- TOC entry 6215 (class 2606 OID 231620)
-- Name: user_menu_item user_menu_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_menu_item
    ADD CONSTRAINT user_menu_item_pkey PRIMARY KEY (user_menu_id);


--
-- TOC entry 6217 (class 2606 OID 231624)
-- Name: user_token user_token_temp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_token
    ADD CONSTRAINT user_token_temp_pkey PRIMARY KEY (id);


--
-- TOC entry 6220 (class 2606 OID 231626)
-- Name: user_token user_token_user_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_token
    ADD CONSTRAINT user_token_user_token_key UNIQUE (user_token);


--
-- TOC entry 5464 (class 1259 OID 231634)
-- Name: member_basic_detail_t_location_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX member_basic_detail_t_location_id_idx ON analytics.member_basic_detail USING btree (location_id);


--
-- TOC entry 5467 (class 1259 OID 231635)
-- Name: rch_child_analytics_details_t_death_location_id_death_date_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_death_location_id_death_date_idx ON analytics.rch_child_analytics_details USING btree (death_location_id, death_date DESC NULLS LAST);


--
-- TOC entry 5468 (class 1259 OID 231636)
-- Name: rch_child_analytics_details_t_delivery_location_id_dob_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_delivery_location_id_dob_idx ON analytics.rch_child_analytics_details USING btree (delivery_location_id, dob DESC);


--
-- TOC entry 5469 (class 1259 OID 231637)
-- Name: rch_child_analytics_details_t_loc_id_dob_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_loc_id_dob_idx ON analytics.rch_child_analytics_details USING btree (loc_id, dob DESC NULLS LAST);


--
-- TOC entry 5470 (class 1259 OID 231638)
-- Name: rch_child_analytics_details_t_loc_id_sam_child_date_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_loc_id_sam_child_date_idx ON analytics.rch_child_analytics_details USING btree (loc_id, sam_child_date);


--
-- TOC entry 5471 (class 1259 OID 231639)
-- Name: rch_child_analytics_details_t_member_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_member_id_idx ON analytics.rch_child_analytics_details USING btree (member_id DESC NULLS LAST);


--
-- TOC entry 5472 (class 1259 OID 231640)
-- Name: rch_child_analytics_details_t_mother_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_mother_id_idx ON analytics.rch_child_analytics_details USING btree (mother_id);


--
-- TOC entry 5473 (class 1259 OID 231641)
-- Name: rch_child_analytics_details_t_native_loc_id_dob_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_native_loc_id_dob_idx ON analytics.rch_child_analytics_details USING btree (native_loc_id, dob DESC);


--
-- TOC entry 5474 (class 1259 OID 231642)
-- Name: rch_child_analytics_details_t_native_loc_id_full_immunizati_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_native_loc_id_full_immunizati_idx ON analytics.rch_child_analytics_details USING btree (native_loc_id, full_immunization_date DESC NULLS LAST);


--
-- TOC entry 5477 (class 1259 OID 231643)
-- Name: rch_child_analytics_details_t_wpd_child_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_child_analytics_details_t_wpd_child_id_idx ON analytics.rch_child_analytics_details USING btree (wpd_child_id DESC NULLS LAST);


--
-- TOC entry 5482 (class 1259 OID 231644)
-- Name: rch_member_services_t_location_id_service_date_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_member_services_t_location_id_service_date_idx ON analytics.rch_member_services USING btree (location_id, service_date DESC);


--
-- TOC entry 5483 (class 1259 OID 231645)
-- Name: rch_member_services_t_member_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_member_services_t_member_id_idx ON analytics.rch_member_services USING btree (member_id);


--
-- TOC entry 5486 (class 1259 OID 231646)
-- Name: rch_member_services_t_service_type_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_member_services_t_service_type_idx ON analytics.rch_member_services USING btree (service_type);


--
-- TOC entry 5487 (class 1259 OID 231647)
-- Name: rch_member_services_t_service_type_location_id_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_member_services_t_service_type_location_id_idx ON analytics.rch_member_services USING btree (service_type, location_id);


--
-- TOC entry 5488 (class 1259 OID 231648)
-- Name: rch_member_services_t_user_id_service_date_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_member_services_t_user_id_service_date_idx ON analytics.rch_member_services USING btree (user_id, service_date);


--
-- TOC entry 5493 (class 1259 OID 231649)
-- Name: rch_pregnancy_analytics_detai_delivery_location_id_delivery_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_pregnancy_analytics_detai_delivery_location_id_delivery_idx ON analytics.rch_pregnancy_analytics_details USING btree (delivery_location_id, delivery_reg_date DESC NULLS LAST);


--
-- TOC entry 5494 (class 1259 OID 231650)
-- Name: rch_pregnancy_analytics_detai_native_location_id_reg_servic_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_pregnancy_analytics_detai_native_location_id_reg_servic_idx ON analytics.rch_pregnancy_analytics_details USING btree (native_location_id, reg_service_date DESC NULLS LAST);


--
-- TOC entry 5495 (class 1259 OID 231651)
-- Name: rch_pregnancy_analytics_detail_death_location_id_death_date_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX rch_pregnancy_analytics_detail_death_location_id_death_date_idx ON analytics.rch_pregnancy_analytics_details USING btree (death_location_id, death_date DESC NULLS LAST);


--
-- TOC entry 5498 (class 1259 OID 231652)
-- Name: soh_timeline_analytics_1_timeline_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX soh_timeline_analytics_1_timeline_idx ON analytics.soh_timeline_analytics USING btree (location_id, timeline_sub_type, timeline_type, element_name);


--
-- TOC entry 5501 (class 1259 OID 231653)
-- Name: soh_timeline_analytics_temp_element_name_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX soh_timeline_analytics_temp_element_name_idx ON analytics.soh_timeline_analytics_temp USING btree (element_name);


--
-- TOC entry 5502 (class 1259 OID 231654)
-- Name: soh_timeline_analytics_temp_timeline_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX soh_timeline_analytics_temp_timeline_idx ON analytics.soh_timeline_analytics_temp USING btree (timeline_type, location_id, element_name);


--
-- TOC entry 5507 (class 1259 OID 231655)
-- Name: um_user_day_wise_login_detail_month_year_idx; Type: INDEX; Schema: analytics; Owner: postgres
--

CREATE INDEX um_user_day_wise_login_detail_month_year_idx ON analytics.um_user_day_wise_login_detail USING btree (month_year);


--
-- TOC entry 5578 (class 1259 OID 231657)
-- Name: family_state_change_issue_family_id_idx; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX family_state_change_issue_family_id_idx ON archive.family_state_change_issue USING btree (family_id);


--
-- TOC entry 5589 (class 1259 OID 231658)
-- Name: rch_member_service_data_sync_detail1_member_id_idx; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX rch_member_service_data_sync_detail1_member_id_idx ON archive.rch_member_service_data_sync_detail USING btree (member_id);


--
-- TOC entry 5596 (class 1259 OID 231659)
-- Name: system_sync_status_dump_action_date_idx1; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX system_sync_status_dump_action_date_idx1 ON archive.system_sync_status_dump USING btree (action_date DESC NULLS LAST);


--
-- TOC entry 5597 (class 1259 OID 231660)
-- Name: system_sync_status_dump_user_id_action_date_idx1; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX system_sync_status_dump_user_id_action_date_idx1 ON archive.system_sync_status_dump USING btree (user_id, action_date DESC NULLS LAST);


--
-- TOC entry 5602 (class 1259 OID 231661)
-- Name: user_location_detail_location_idx; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX user_location_detail_location_idx ON archive.user_location_detail USING btree (location);


--
-- TOC entry 5605 (class 1259 OID 231662)
-- Name: user_location_detail_user_id_idx; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX user_location_detail_user_id_idx ON archive.user_location_detail USING btree (user_id);


--
-- TOC entry 5608 (class 1259 OID 231663)
-- Name: usermanagement_system_user_user_id_idx; Type: INDEX; Schema: archive; Owner: postgres
--

CREATE INDEX usermanagement_system_user_user_id_idx ON archive.usermanagement_system_user USING btree (user_id);


--
-- TOC entry 5680 (class 1259 OID 231675)
-- Name: event_mobile_notification_pending_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX event_mobile_notification_pending_t_member_id_idx ON public.event_mobile_notification_pending USING btree (member_id DESC);


--
-- TOC entry 5697 (class 1259 OID 231676)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

-- CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 5702 (class 1259 OID 231678)
-- Name: health_infrastructure_details_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX health_infrastructure_details_location_id_idx ON public.health_infrastructure_details USING btree (location_id);


--
-- TOC entry 5661 (class 1259 OID 231690)
-- Name: idx_document_index_is_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_index_is_index ON public.document_index USING btree (is_index, id) WHERE (is_index IS FALSE);


--
-- TOC entry 5525 (class 1259 OID 231691)
-- Name: idx_location_master_ldg_code; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_location_master_ldg_code ON public.location_master USING btree (lgd_code);


--
-- TOC entry 5947 (class 1259 OID 231694)
-- Name: idx_rch_member_services_last_90_days_location_id_service_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_rch_member_services_last_90_days_location_id_service_date ON public.rch_member_services_last_90_days USING btree (location_id, service_date);


--
-- TOC entry 5948 (class 1259 OID 231695)
-- Name: idx_rch_member_services_last_90_days_service_date_location_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_rch_member_services_last_90_days_service_date_location_id ON public.rch_member_services_last_90_days USING btree (service_date DESC, location_id);


--
-- TOC entry 5721 (class 1259 OID 231696)
-- Name: imt_family1_anganwadi_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_anganwadi_id_idx ON public.imt_family USING btree (anganwadi_id);


--
-- TOC entry 5722 (class 1259 OID 231697)
-- Name: imt_family1_area_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_area_id_idx ON public.imt_family USING btree (area_id);


--
-- TOC entry 5723 (class 1259 OID 231698)
-- Name: imt_family1_family_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_family_id_idx ON public.imt_family USING btree (family_id);


--
-- TOC entry 5724 (class 1259 OID 231699)
-- Name: imt_family1_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_location_id_idx ON public.imt_family USING btree (location_id);


--
-- TOC entry 5725 (class 1259 OID 231700)
-- Name: imt_family1_location_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_location_id_state_idx ON public.imt_family USING btree (location_id, state);


--
-- TOC entry 5728 (class 1259 OID 231701)
-- Name: imt_family1_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family1_state_idx ON public.imt_family USING btree (state);


--
-- TOC entry 5737 (class 1259 OID 231703)
-- Name: imt_family_verification_t_family_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_family_verification_t_family_id_idx ON public.imt_family_verification USING btree (family_id);


--
-- TOC entry 5750 (class 1259 OID 231704)
-- Name: imt_member_cfhc_master_t_family_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_cfhc_master_t_family_id_idx ON public.imt_member_cfhc_master USING btree (family_id DESC NULLS LAST);


--
-- TOC entry 5753 (class 1259 OID 231705)
-- Name: imt_member_chronic_disease_rel_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_chronic_disease_rel_t_member_id_idx ON public.imt_member_chronic_disease_rel USING btree (member_id);


--
-- TOC entry 5756 (class 1259 OID 231706)
-- Name: imt_member_congenital_anomaly_rel_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_congenital_anomaly_rel_member_id_idx ON public.imt_member_congenital_anomaly_rel USING btree (member_id);


--
-- TOC entry 5759 (class 1259 OID 231707)
-- Name: imt_member_current_disease_rel_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_current_disease_rel_member_id_idx ON public.imt_member_current_disease_rel USING btree (member_id);


--
-- TOC entry 5762 (class 1259 OID 231708)
-- Name: imt_member_eye_issue_rel_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_eye_issue_rel_t_member_id_idx ON public.imt_member_eye_issue_rel USING btree (member_id);


--
-- TOC entry 5769 (class 1259 OID 231710)
-- Name: imt_member_state_detail_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_state_detail_t_member_id_idx ON public.imt_member_state_detail USING btree (member_id DESC NULLS LAST);


--
-- TOC entry 5740 (class 1259 OID 231711)
-- Name: imt_member_t_aadhar_number_encrypted_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_aadhar_number_encrypted_idx ON public.imt_member USING btree (aadhar_number_encrypted);


--
-- TOC entry 5741 (class 1259 OID 231712)
-- Name: imt_member_t_dob_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_dob_idx ON public.imt_member USING btree (dob);


--
-- TOC entry 5742 (class 1259 OID 231713)
-- Name: imt_member_t_family_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_family_id_idx ON public.imt_member USING btree (family_id);


--
-- TOC entry 5743 (class 1259 OID 231714)
-- Name: imt_member_t_family_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_family_id_state_idx ON public.imt_member USING btree (family_id, state);


--
-- TOC entry 5744 (class 1259 OID 231715)
-- Name: imt_member_t_mobile_number_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_mobile_number_idx ON public.imt_member USING btree (mobile_number);


--
-- TOC entry 5745 (class 1259 OID 231716)
-- Name: imt_member_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_modified_on_idx ON public.imt_member USING btree (modified_on DESC);


--
-- TOC entry 5746 (class 1259 OID 231717)
-- Name: imt_member_t_mother_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_mother_id_idx ON public.imt_member USING btree (mother_id);


--
-- TOC entry 5749 (class 1259 OID 231718)
-- Name: imt_member_t_unique_health_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX imt_member_t_unique_health_id_idx ON public.imt_member USING btree (unique_health_id);


--
-- TOC entry 5516 (class 1259 OID 231719)
-- Name: location_hierchy_closer_det_child_id_parent_loc_type_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_hierchy_closer_det_child_id_parent_loc_type_idx ON public.location_hierchy_closer_det USING btree (child_id, parent_loc_type);


--
-- TOC entry 5519 (class 1259 OID 231720)
-- Name: location_hierchy_closer_det_parent_id_child_loc_type_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_hierchy_closer_det_parent_id_child_loc_type_idx ON public.location_hierchy_closer_det USING btree (parent_id, child_loc_type);


--
-- TOC entry 5520 (class 1259 OID 231723)
-- Name: location_hierchy_closer_det_parent_id_depth_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_hierchy_closer_det_parent_id_depth_idx ON public.location_hierchy_closer_det USING btree (parent_id, depth);

--
-- TOC entry 5526 (class 1259 OID 231725)
-- Name: location_master_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_master_created_on_idx ON public.location_master USING btree (created_on DESC NULLS LAST);


--
-- TOC entry 5527 (class 1259 OID 231734)
-- Name: location_master_location_code_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_master_location_code_idx ON public.location_master USING btree (location_code);


--
-- TOC entry 5528 (class 1259 OID 231735)
-- Name: location_master_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_master_modified_on_idx ON public.location_master USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 5529 (class 1259 OID 231736)
-- Name: location_master_parent_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_master_parent_id_idx ON public.location_master USING btree (parent NULLS FIRST);


--
-- TOC entry 5532 (class 1259 OID 231737)
-- Name: location_master_type_english_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_master_type_english_name_idx ON public.location_master USING btree (type, english_name);


--
-- TOC entry 5791 (class 1259 OID 231738)
-- Name: location_mobile_feature_master_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_mobile_feature_master_location_id_idx ON public.location_mobile_feature_master USING btree (location_id);


--
-- TOC entry 5802 (class 1259 OID 231739)
-- Name: logged_actions_action_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX logged_actions_action_idx ON public.logged_actions USING btree (action);


--
-- TOC entry 5803 (class 1259 OID 231740)
-- Name: logged_actions_action_tstamp_tx_stm_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX logged_actions_action_tstamp_tx_stm_idx ON public.logged_actions USING btree (action_tstamp_stm);


--
-- TOC entry 5806 (class 1259 OID 231741)
-- Name: logged_actions_relid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX logged_actions_relid_idx ON public.logged_actions USING btree (relid);


--
-- TOC entry 5811 (class 1259 OID 231742)
-- Name: migration_master_t_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX migration_master_t_created_on_idx ON public.migration_master USING btree (created_on DESC);


--
-- TOC entry 5812 (class 1259 OID 231743)
-- Name: migration_master_t_location_migrated_from_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX migration_master_t_location_migrated_from_created_on_idx ON public.migration_master USING btree (location_migrated_from, created_on DESC);


--
-- TOC entry 5851 (class 1259 OID 231766)
-- Name: rch_anc_master_t_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_anc_master_t_location_id_created_on_idx ON public.rch_anc_master USING btree (location_id, created_on);


--
-- TOC entry 5852 (class 1259 OID 231767)
-- Name: rch_anc_master_t_location_id_service_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_anc_master_t_location_id_service_date_idx ON public.rch_anc_master USING btree (location_id, service_date);


--
-- TOC entry 5853 (class 1259 OID 231768)
-- Name: rch_anc_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_anc_master_t_member_id_idx ON public.rch_anc_master USING btree (member_id);


--
-- TOC entry 5854 (class 1259 OID 231769)
-- Name: rch_anc_master_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_anc_master_t_modified_on_idx ON public.rch_anc_master USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 5857 (class 1259 OID 231770)
-- Name: rch_anc_master_t_pregnancy_reg_det_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_anc_master_t_pregnancy_reg_det_id_idx ON public.rch_anc_master USING btree (pregnancy_reg_det_id);


--
-- TOC entry 5900 (class 1259 OID 231771)
-- Name: rch_child_cp_suspects_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_child_cp_suspects_location_id_idx ON public.rch_child_cp_suspects USING btree (location_id);


--
-- TOC entry 5907 (class 1259 OID 231772)
-- Name: rch_child_service_master_t_created_on_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_child_service_master_t_created_on_location_id_idx ON public.rch_child_service_master USING btree (created_on DESC, location_id);


--
-- TOC entry 5908 (class 1259 OID 231773)
-- Name: rch_child_service_master_t_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_child_service_master_t_location_id_created_on_idx ON public.rch_child_service_master USING btree (location_id, created_on DESC NULLS LAST);


--
-- TOC entry 5909 (class 1259 OID 231774)
-- Name: rch_child_service_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_child_service_master_t_member_id_idx ON public.rch_child_service_master USING btree (member_id);


--
-- TOC entry 5910 (class 1259 OID 231775)
-- Name: rch_child_service_master_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_child_service_master_t_modified_on_idx ON public.rch_child_service_master USING btree (modified_on DESC);


--
-- TOC entry 5923 (class 1259 OID 231776)
-- Name: rch_immunisation_master_t_given_on_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_immunisation_master_t_given_on_location_id_idx ON public.rch_immunisation_master USING btree (given_on, location_id);


--
-- TOC entry 5924 (class 1259 OID 231777)
-- Name: rch_immunisation_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_immunisation_master_t_member_id_idx ON public.rch_immunisation_master USING btree (member_id);


--
-- TOC entry 5925 (class 1259 OID 231778)
-- Name: rch_immunisation_master_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_immunisation_master_t_modified_on_idx ON public.rch_immunisation_master USING btree (modified_on DESC);


--
-- TOC entry 5928 (class 1259 OID 231779)
-- Name: rch_immunisation_master_t_pregnancy_reg_det_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_immunisation_master_t_pregnancy_reg_det_id_idx ON public.rch_immunisation_master USING btree (pregnancy_reg_det_id);


--
-- TOC entry 5929 (class 1259 OID 231780)
-- Name: rch_immunisation_master_t_visit_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_immunisation_master_t_visit_id_idx ON public.rch_immunisation_master USING btree (visit_id);


--
-- TOC entry 5934 (class 1259 OID 231781)
-- Name: rch_lmp_follow_up_t_created_on_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_lmp_follow_up_t_created_on_location_id_idx ON public.rch_lmp_follow_up USING btree (created_on DESC, location_id);


--
-- TOC entry 5935 (class 1259 OID 231782)
-- Name: rch_lmp_follow_up_t_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_lmp_follow_up_t_location_id_created_on_idx ON public.rch_lmp_follow_up USING btree (location_id, created_on);


--
-- TOC entry 5936 (class 1259 OID 231783)
-- Name: rch_lmp_follow_up_t_member_id_anmol_registration_id_anmol_f_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_lmp_follow_up_t_member_id_anmol_registration_id_anmol_f_idx ON public.rch_lmp_follow_up USING btree (member_id, anmol_registration_id, anmol_follow_up_status);


--
-- TOC entry 5937 (class 1259 OID 231784)
-- Name: rch_lmp_follow_up_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_lmp_follow_up_t_member_id_idx ON public.rch_lmp_follow_up USING btree (member_id);


--
-- TOC entry 5938 (class 1259 OID 231785)
-- Name: rch_lmp_follow_up_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_lmp_follow_up_t_modified_on_idx ON public.rch_lmp_follow_up USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 5971 (class 1259 OID 231786)
-- Name: rch_pnc_child_master_t_child_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_child_master_t_child_id_idx ON public.rch_pnc_child_master USING btree (child_id);


--
-- TOC entry 5974 (class 1259 OID 231787)
-- Name: rch_pnc_child_master_t_pnc_master_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_child_master_t_pnc_master_id_idx ON public.rch_pnc_child_master USING btree (pnc_master_id);


--
-- TOC entry 5977 (class 1259 OID 231788)
-- Name: rch_pnc_master_t_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_master_t_location_id_created_on_idx ON public.rch_pnc_master USING btree (location_id, created_on);


--
-- TOC entry 5978 (class 1259 OID 231789)
-- Name: rch_pnc_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_master_t_member_id_idx ON public.rch_pnc_master USING btree (member_id);


--
-- TOC entry 5979 (class 1259 OID 231790)
-- Name: rch_pnc_master_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_master_t_modified_on_idx ON public.rch_pnc_master USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 5982 (class 1259 OID 231791)
-- Name: rch_pnc_master_t_pregnancy_reg_det_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_master_t_pregnancy_reg_det_id_idx ON public.rch_pnc_master USING btree (pregnancy_reg_det_id);


--
-- TOC entry 5987 (class 1259 OID 231792)
-- Name: rch_pnc_mother_master_t_mother_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_mother_master_t_mother_id_idx ON public.rch_pnc_mother_master USING btree (mother_id);


--
-- TOC entry 5990 (class 1259 OID 231793)
-- Name: rch_pnc_mother_master_t_pnc_master_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pnc_mother_master_t_pnc_master_id_idx ON public.rch_pnc_mother_master USING btree (pnc_master_id);


--
-- TOC entry 5991 (class 1259 OID 231794)
-- Name: rch_pregnancy_registration_det_t_current_location_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_current_location_id_state_idx ON public.rch_pregnancy_registration_det USING btree (current_location_id, state);


--
-- TOC entry 5992 (class 1259 OID 231795)
-- Name: rch_pregnancy_registration_det_t_lmp_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_lmp_date_idx ON public.rch_pregnancy_registration_det USING btree (lmp_date);


--
-- TOC entry 5993 (class 1259 OID 231796)
-- Name: rch_pregnancy_registration_det_t_location_id_lmp_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_location_id_lmp_date_idx ON public.rch_pregnancy_registration_det USING btree (location_id, lmp_date);


--
-- TOC entry 5994 (class 1259 OID 231797)
-- Name: rch_pregnancy_registration_det_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_member_id_idx ON public.rch_pregnancy_registration_det USING btree (member_id);


--
-- TOC entry 5995 (class 1259 OID 231798)
-- Name: rch_pregnancy_registration_det_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_modified_on_idx ON public.rch_pregnancy_registration_det USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 5996 (class 1259 OID 231799)
-- Name: rch_pregnancy_registration_det_t_mthr_reg_no_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_pregnancy_registration_det_t_mthr_reg_no_idx ON public.rch_pregnancy_registration_det USING btree (mthr_reg_no);


--
-- TOC entry 6013 (class 1259 OID 231800)
-- Name: rch_wpd_child_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_child_master_t_member_id_idx ON public.rch_wpd_child_master USING btree (member_id DESC);


--
-- TOC entry 6016 (class 1259 OID 231801)
-- Name: rch_wpd_child_master_t_wpd_mother_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_child_master_t_wpd_mother_id_idx ON public.rch_wpd_child_master USING btree (wpd_mother_id);


--
-- TOC entry 6025 (class 1259 OID 231802)
-- Name: rch_wpd_mother_master_t_health_infrastructure_id_date_of_de_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_health_infrastructure_id_date_of_de_idx ON public.rch_wpd_mother_master USING btree (health_infrastructure_id, date_of_delivery DESC);


--
-- TOC entry 6026 (class 1259 OID 231803)
-- Name: rch_wpd_mother_master_t_is_discharged_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_is_discharged_idx ON public.rch_wpd_mother_master USING btree (is_discharged DESC);


--
-- TOC entry 6027 (class 1259 OID 231804)
-- Name: rch_wpd_mother_master_t_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_location_id_created_on_idx ON public.rch_wpd_mother_master USING btree (location_id, created_on);


--
-- TOC entry 6028 (class 1259 OID 231805)
-- Name: rch_wpd_mother_master_t_member_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_member_id_idx ON public.rch_wpd_mother_master USING btree (member_id);


--
-- TOC entry 6029 (class 1259 OID 231806)
-- Name: rch_wpd_mother_master_t_modified_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_modified_on_idx ON public.rch_wpd_mother_master USING btree (modified_on DESC NULLS LAST);


--
-- TOC entry 6032 (class 1259 OID 231807)
-- Name: rch_wpd_mother_master_t_pregnancy_reg_det_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX rch_wpd_mother_master_t_pregnancy_reg_det_id_idx ON public.rch_wpd_mother_master USING btree (pregnancy_reg_det_id);


--
-- TOC entry 6075 (class 1259 OID 231811)
-- Name: sms_block_master_mobile_no_status_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sms_block_master_mobile_no_status_idx ON public.sms_block_master USING btree (mobile_number, status);


--
-- TOC entry 6118 (class 1259 OID 231812)
-- Name: system_sync_status1_status_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX system_sync_status1_status_idx ON public.system_sync_status USING btree (status);


--
-- TOC entry 6119 (class 1259 OID 231813)
-- Name: system_sync_status1_user_id_action_date_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX system_sync_status1_user_id_action_date_idx ON public.system_sync_status USING btree (user_id, action_date DESC NULLS LAST);


--
-- TOC entry 6120 (class 1259 OID 231814)
-- Name: techo_notification_location_cha_created_on_from_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_location_cha_created_on_from_location_id_idx ON public.techo_notification_location_change_detail USING btree (created_on DESC, from_location_id);


--
-- TOC entry 6121 (class 1259 OID 231815)
-- Name: techo_notification_location_cha_from_location_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_location_cha_from_location_id_created_on_idx ON public.techo_notification_location_change_detail USING btree (from_location_id, created_on DESC);


--
-- TOC entry 6124 (class 1259 OID 231816)
-- Name: techo_notification_master1_family_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_master1_family_id_idx ON public.techo_notification_master USING btree (family_id);


--
-- TOC entry 6125 (class 1259 OID 231817)
-- Name: techo_notification_master_t_location_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_master_t_location_id_state_idx ON public.techo_notification_master USING btree (location_id, state);


--
-- TOC entry 6126 (class 1259 OID 231818)
-- Name: techo_notification_master_t_member_id_notification_type_id__idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_master_t_member_id_notification_type_id__idx ON public.techo_notification_master USING btree (member_id, notification_type_id, state);


--
-- TOC entry 6127 (class 1259 OID 231819)
-- Name: techo_notification_master_t_modified_on_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_master_t_modified_on_location_id_idx ON public.techo_notification_master USING btree (modified_on, location_id);


--
-- TOC entry 6130 (class 1259 OID 231820)
-- Name: techo_notification_master_t_state_location_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_master_t_state_location_id_idx ON public.techo_notification_master USING btree (state, location_id);


--
-- TOC entry 6131 (class 1259 OID 231821)
-- Name: techo_notification_state_detail_t_notification_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_notification_state_detail_t_notification_id_idx ON public.techo_notification_state_detail USING btree (notification_id DESC NULLS LAST);


--
-- TOC entry 6146 (class 1259 OID 231822)
-- Name: techo_web_notification_master_location_id_notification_typ_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX techo_web_notification_master_location_id_notification_typ_idx1 ON public.techo_web_notification_master USING btree (location_id, notification_type_escalation_id, state);


--
-- TOC entry 5542 (class 1259 OID 231823)
-- Name: um_role_master_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_role_master_name_idx ON public.um_role_master USING btree (name);


--
-- TOC entry 5545 (class 1259 OID 231824)
-- Name: um_user_location_loc_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_location_loc_id_state_idx ON public.um_user_location USING btree (loc_id, state);


--
-- TOC entry 5550 (class 1259 OID 231825)
-- Name: um_user_location_user_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_location_user_id_state_idx ON public.um_user_location USING btree (user_id, state);


--
-- TOC entry 6201 (class 1259 OID 231829)
-- Name: um_user_login_det_user_id_created_on_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_login_det_user_id_created_on_idx ON public.um_user_login_det USING btree (user_id, created_on);


--
-- TOC entry 5539 (class 1259 OID 231830)
-- Name: um_user_role_id_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_role_id_state_idx ON public.um_user USING btree (role_id, state);


--
-- TOC entry 5540 (class 1259 OID 231831)
-- Name: um_user_search_text_trgm_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_search_text_trgm_idx ON public.um_user USING gin (search_text public.gin_trgm_ops);


--
-- TOC entry 5551 (class 1259 OID 231840)
-- Name: um_user_state_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_state_idx ON public.um_user_location USING btree (state);


--
-- TOC entry 5541 (class 1259 OID 231841)
-- Name: um_user_user_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX um_user_user_name_idx ON public.um_user USING btree (user_name);


--
-- TOC entry 6218 (class 1259 OID 231843)
-- Name: user_token_user_token_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX user_token_user_token_idx ON public.user_token USING btree (user_token);


--
-- TOC entry 6281 (class 2620 OID 231846)
-- Name: event_configuration audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.event_configuration FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6283 (class 2620 OID 231847)
-- Name: event_configuration_type audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.event_configuration_type FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6285 (class 2620 OID 231848)
-- Name: event_mobile_configuration audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.event_mobile_configuration FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6299 (class 2620 OID 231849)
-- Name: query_master audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.query_master FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6309 (class 2620 OID 231850)
-- Name: report_master audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.report_master FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6311 (class 2620 OID 231851)
-- Name: report_parameter_master audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.report_parameter_master FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6313 (class 2620 OID 231852)
-- Name: report_query_master audit_trigger_row; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_row AFTER INSERT OR DELETE OR UPDATE ON public.report_query_master FOR EACH ROW EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6282 (class 2620 OID 231853)
-- Name: event_configuration audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.event_configuration FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6284 (class 2620 OID 231854)
-- Name: event_configuration_type audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.event_configuration_type FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6286 (class 2620 OID 231855)
-- Name: event_mobile_configuration audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.event_mobile_configuration FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6300 (class 2620 OID 231856)
-- Name: query_master audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.query_master FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6310 (class 2620 OID 231857)
-- Name: report_master audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.report_master FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6312 (class 2620 OID 231858)
-- Name: report_parameter_master audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.report_parameter_master FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6314 (class 2620 OID 231859)
-- Name: report_query_master audit_trigger_stm; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER audit_trigger_stm AFTER TRUNCATE ON public.report_query_master FOR EACH STATEMENT EXECUTE FUNCTION public.if_modified_func('true');


--
-- TOC entry 6303 (class 2620 OID 231867)
-- Name: rch_immunisation_master immunisation_archive_entry_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER immunisation_archive_entry_trigger BEFORE DELETE ON public.rch_immunisation_master FOR EACH ROW EXECUTE FUNCTION public.create_entry_in_immunisation_archive();


--
-- TOC entry 6287 (class 2620 OID 231868)
-- Name: imt_family imt_family_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER imt_family_update_trigger AFTER UPDATE ON public.imt_family FOR EACH ROW EXECUTE FUNCTION public.imt_family_update_trigger_func();

ALTER TABLE IF EXISTS public.imt_family DISABLE TRIGGER imt_family_update_trigger;


--
-- TOC entry 6291 (class 2620 OID 231869)
-- Name: imt_member imt_member_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER imt_member_delete_trigger AFTER DELETE ON public.imt_member FOR EACH ROW EXECUTE FUNCTION public.imt_member_delete_trigger_function();


--
-- TOC entry 6288 (class 2620 OID 231870)
-- Name: imt_family insert_basic_state_family_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER insert_basic_state_family_trigger BEFORE INSERT ON public.imt_family FOR EACH ROW EXECUTE FUNCTION public.update_basic_state_family_trigger();


--
-- TOC entry 6292 (class 2620 OID 231871)
-- Name: imt_member insert_basic_state_member_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER insert_basic_state_member_trigger BEFORE INSERT ON public.imt_member FOR EACH ROW EXECUTE FUNCTION public.update_basic_state_member_trigger();


--
-- TOC entry 6295 (class 2620 OID 231873)
-- Name: internationalization_label_master internationalization_label_master_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER internationalization_label_master_insert_trigger AFTER INSERT ON public.internationalization_label_master FOR EACH ROW EXECUTE FUNCTION public.internationalization_label_master_insert_trigger_func();


--
-- TOC entry 6296 (class 2620 OID 231875)
-- Name: internationalization_label_master internationalization_label_master_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER internationalization_label_master_update_trigger AFTER UPDATE ON public.internationalization_label_master FOR EACH ROW EXECUTE FUNCTION public.internationalization_label_master_update_trigger_func();

--
-- TOC entry 6272 (class 2620 OID 231878)
-- Name: location_master location_master_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER location_master_insert_trigger AFTER INSERT ON public.location_master FOR EACH ROW EXECUTE FUNCTION public.location_master_insert_trigger_func();


--
-- TOC entry 6273 (class 2620 OID 231879)
-- Name: location_master location_master_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER location_master_update_trigger AFTER UPDATE ON public.location_master FOR EACH ROW EXECUTE FUNCTION public.location_master_update_trigger_func();


--
-- TOC entry 6318 (class 2620 OID 231880)
-- Name: timer_event log_timer_event_history; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER log_timer_event_history AFTER UPDATE ON public.timer_event FOR EACH ROW EXECUTE FUNCTION public.timer_event_trigger_function();


--
-- TOC entry 6301 (class 2620 OID 231888)
-- Name: rch_anc_master rch_anc_hmis_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_anc_hmis_update AFTER INSERT ON public.rch_anc_master FOR EACH ROW EXECUTE FUNCTION public.rch_anc_hmis_updation();


--
-- TOC entry 6307 (class 2620 OID 231889)
-- Name: rch_pregnancy_registration_det rch_anc_mastech_pregnancy_registration_det_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_anc_mastech_pregnancy_registration_det_delete_trigger AFTER DELETE ON public.rch_pregnancy_registration_det FOR EACH ROW EXECUTE FUNCTION public.rch_pregnancy_registration_det_delete_trigger_function();


--
-- TOC entry 6302 (class 2620 OID 231890)
-- Name: rch_child_service_master rch_csv_hmis_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_csv_hmis_update AFTER INSERT ON public.rch_child_service_master FOR EACH ROW EXECUTE FUNCTION public.rch_csv_hmis_updation();


--
-- TOC entry 6304 (class 2620 OID 231891)
-- Name: rch_immunisation_master rch_immunisation_hmis_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_immunisation_hmis_update AFTER INSERT ON public.rch_immunisation_master FOR EACH ROW EXECUTE FUNCTION public.rch_immunisation_hmis_updation();


--
-- TOC entry 6305 (class 2620 OID 231892)
-- Name: rch_lmp_follow_up rch_lmp_follow_up_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_lmp_follow_up_delete_trigger AFTER DELETE ON public.rch_lmp_follow_up FOR EACH ROW EXECUTE FUNCTION public.rch_lmp_follow_up_delete_trigger_function();


--
-- TOC entry 6306 (class 2620 OID 231893)
-- Name: rch_pnc_master rch_pnc_hmis_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rch_pnc_hmis_update AFTER INSERT ON public.rch_pnc_master FOR EACH ROW EXECUTE FUNCTION public.rch_pnc_hmis_updation();


--
-- TOC entry 6298 (class 2620 OID 231894)
-- Name: location_type_master role_hierarchy_level_updation; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER role_hierarchy_level_updation AFTER UPDATE ON public.location_type_master FOR EACH ROW EXECUTE FUNCTION public.role_hierarchy_level_updation();


--
-- TOC entry 6308 (class 2620 OID 231895)
-- Name: rch_wpd_mother_master rrch_wpd_mother_master_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rrch_wpd_mother_master_delete_trigger AFTER DELETE ON public.rch_wpd_mother_master FOR EACH ROW EXECUTE FUNCTION public.rch_wpd_mother_master_delete_trigger_function();


--
-- TOC entry 6315 (class 2620 OID 231896)
-- Name: techo_notification_master techo_notification_master_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER techo_notification_master_delete_trigger AFTER DELETE ON public.techo_notification_master FOR EACH ROW EXECUTE FUNCTION public.techo_notification_master_delete_trigger_func();


--
-- TOC entry 6316 (class 2620 OID 231897)
-- Name: techo_notification_master techo_notification_master_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER techo_notification_master_insert_trigger AFTER INSERT ON public.techo_notification_master FOR EACH ROW EXECUTE FUNCTION public.techo_notification_master_insert_trigger_func();


--
-- TOC entry 6317 (class 2620 OID 231898)
-- Name: techo_notification_master techo_notification_master_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER techo_notification_master_update_trigger AFTER UPDATE ON public.techo_notification_master FOR EACH ROW EXECUTE FUNCTION public.techo_notification_master_update_trigger_func();


--
-- TOC entry 6276 (class 2620 OID 231899)
-- Name: um_role_master um_role_master_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_role_master_insert_trigger AFTER INSERT ON public.um_role_master FOR EACH ROW EXECUTE FUNCTION public.um_role_master_insert_trigger_func();


--
-- TOC entry 6277 (class 2620 OID 231900)
-- Name: um_role_master um_role_master_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_role_master_update_trigger AFTER UPDATE ON public.um_role_master FOR EACH ROW EXECUTE FUNCTION public.um_role_master_update_trigger_func();


--
-- TOC entry 6274 (class 2620 OID 231901)
-- Name: um_user um_user_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_user_insert_trigger AFTER INSERT ON public.um_user FOR EACH ROW EXECUTE FUNCTION public.um_user_insert_trigger_func();


--
-- TOC entry 6278 (class 2620 OID 231902)
-- Name: um_user_location um_user_location_insert_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_user_location_insert_trigger AFTER INSERT ON public.um_user_location FOR EACH ROW EXECUTE FUNCTION public.um_user_location_insert_trigger_func();


--
-- TOC entry 6279 (class 2620 OID 231903)
-- Name: um_user_location um_user_location_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_user_location_update_trigger AFTER UPDATE ON public.um_user_location FOR EACH ROW EXECUTE FUNCTION public.um_user_location_update_trigger_func();


--
-- TOC entry 6275 (class 2620 OID 231904)
-- Name: um_user um_user_update_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER um_user_update_trigger AFTER UPDATE ON public.um_user FOR EACH ROW EXECUTE FUNCTION public.um_user_update_trigger_func();


--
-- TOC entry 6289 (class 2620 OID 231905)
-- Name: imt_family update_basic_state_family_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_basic_state_family_trigger BEFORE UPDATE ON public.imt_family FOR EACH ROW EXECUTE FUNCTION public.update_basic_state_family_trigger();


--
-- TOC entry 6293 (class 2620 OID 231906)
-- Name: imt_member update_basic_state_member_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_basic_state_member_trigger BEFORE UPDATE ON public.imt_member FOR EACH ROW EXECUTE FUNCTION public.update_basic_state_member_trigger();


--
-- TOC entry 6280 (class 2620 OID 231907)
-- Name: notification_type_master update_form_master_info_on_notification_type_add_or_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_form_master_info_on_notification_type_add_or_update AFTER INSERT OR UPDATE ON public.notification_type_master FOR EACH ROW EXECUTE FUNCTION public.update_form_master_info_on_notification_type_add_or_update();


--
-- TOC entry 6290 (class 2620 OID 231909)
-- Name: imt_family update_notification_info_on_family_location_change; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_notification_info_on_family_location_change AFTER UPDATE ON public.imt_family FOR EACH ROW EXECUTE FUNCTION public.update_notification_info_on_family_location_change();


--
-- TOC entry 6294 (class 2620 OID 231910)
-- Name: imt_member update_notification_info_on_member_family_change; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_notification_info_on_member_family_change AFTER UPDATE ON public.imt_member FOR EACH ROW EXECUTE FUNCTION public.update_notification_info_on_member_family_change();


--
-- TOC entry 6319 (class 2620 OID 231911)
-- Name: user_menu_item user_menu_item_insert_update_delete_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER user_menu_item_insert_update_delete_trigger AFTER INSERT OR DELETE OR UPDATE ON public.user_menu_item FOR EACH ROW EXECUTE FUNCTION public.user_menu_item_trigger_func();


--
-- TOC entry 6237 (class 2606 OID 231912)
-- Name: system_user FK_LAST_UPDATED_BY_TO_USER; Type: FK CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.system_user
    ADD CONSTRAINT "FK_LAST_UPDATED_BY_TO_USER" FOREIGN KEY (last_modified_by) REFERENCES archive.system_user(id);


--
-- TOC entry 6238 (class 2606 OID 231922)
-- Name: user_location_detail fk55939ba7aa88e1a9; Type: FK CONSTRAINT; Schema: archive; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY archive.user_location_detail
    ADD CONSTRAINT fk55939ba7aa88e1a9 FOREIGN KEY (location) REFERENCES public.location_master(id);


--
-- TOC entry 6241 (class 2606 OID 231932)
-- Name: child_cmtc_nrc_admission_illness_detail child_cmtc_nrc_admission_illness_detail_admission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_admission_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_admission_illness_detail_admission_id_fkey FOREIGN KEY (admission_id) REFERENCES public.child_cmtc_nrc_admission_detail(id);


--
-- TOC entry 6242 (class 2606 OID 231937)
-- Name: child_cmtc_nrc_discharge_illness_detail child_cmtc_nrc_discharge_illness_detail_discharge_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_discharge_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_discharge_illness_detail_discharge_id_fkey FOREIGN KEY (discharge_id) REFERENCES public.child_cmtc_nrc_discharge_detail(id);


--
-- TOC entry 6243 (class 2606 OID 231942)
-- Name: child_cmtc_nrc_follow_up_illness_detail child_cmtc_nrc_follow_up_illness_detail_follow_up_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.child_cmtc_nrc_follow_up_illness_detail
    ADD CONSTRAINT child_cmtc_nrc_follow_up_illness_detail_follow_up_id_fkey FOREIGN KEY (follow_up_id) REFERENCES public.child_cmtc_nrc_follow_up(id);


--
-- TOC entry 6244 (class 2606 OID 231957)
-- Name: field_value_master field_value_master_field_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.field_value_master
    ADD CONSTRAINT field_value_master_field_id_fkey FOREIGN KEY (field_id) REFERENCES public.field_constant_master(id);


--
-- TOC entry 6240 (class 2606 OID 231962)
-- Name: announcement_location_detail fk6c1a8523ac71054d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_location_detail
    ADD CONSTRAINT fk6c1a8523ac71054d FOREIGN KEY (announcement) REFERENCES public.announcement_info_master(id);


--
-- TOC entry 6245 (class 2606 OID 231967)
-- Name: listvalue_field_value_detail fk7484ce30e35a0b5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.listvalue_field_value_detail
    ADD CONSTRAINT fk7484ce30e35a0b5 FOREIGN KEY (field_key) REFERENCES public.listvalue_field_master(field_key);


--
-- TOC entry 6236 (class 2606 OID 231982)
-- Name: um_user_location fk_location_master_id_um_user_location_loc_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user_location
    ADD CONSTRAINT fk_location_master_id_um_user_location_loc_id FOREIGN KEY (loc_id) REFERENCES public.location_master(id);


--
-- TOC entry 6253 (class 2606 OID 231987)
-- Name: report_master fk_menu_config; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_master
    ADD CONSTRAINT fk_menu_config FOREIGN KEY (menu_id) REFERENCES public.menu_config(id);


--
-- TOC entry 6271 (class 2606 OID 231992)
-- Name: user_menu_item fk_menu_config_id_user_menu_item_menu_config_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.user_menu_item
    ADD CONSTRAINT fk_menu_config_id_user_menu_item_menu_config_id FOREIGN KEY (menu_config_id) REFERENCES public.menu_config(id);


--
-- TOC entry 6246 (class 2606 OID 231997)
-- Name: menu_config fk_menu_group_id_menu_config_group_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_config
    ADD CONSTRAINT fk_menu_group_id_menu_config_group_id FOREIGN KEY (group_id) REFERENCES public.menu_group(id);


--
-- TOC entry 6247 (class 2606 OID 232002)
-- Name: menu_config fk_menu_group_id_menu_config_sub_group_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.menu_config
    ADD CONSTRAINT fk_menu_group_id_menu_config_sub_group_id FOREIGN KEY (sub_group_id) REFERENCES public.menu_group(id);


--
-- TOC entry 6255 (class 2606 OID 232007)
-- Name: role_management fk_role_master_id_um_role_management_managed_role_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.role_management
    ADD CONSTRAINT fk_role_master_id_um_role_management_managed_role_id FOREIGN KEY (role_id) REFERENCES public.um_role_master(id);


--
-- TOC entry 6235 (class 2606 OID 232012)
-- Name: um_user fk_um_role_master_id_um_user_role_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.um_user
    ADD CONSTRAINT fk_um_role_master_id_um_user_role_id FOREIGN KEY (role_id) REFERENCES public.um_role_master(id);


--
-- TOC entry 6239 (class 2606 OID 232017)
-- Name: announcement_info_detail fka8acaa0aac71054d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.announcement_info_detail
    ADD CONSTRAINT fka8acaa0aac71054d FOREIGN KEY (announcement) REFERENCES public.announcement_info_master(id);


--
-- TOC entry 6234 (class 2606 OID 232027)
-- Name: location_master fkf5e1622cfde44a9e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.location_master
    ADD CONSTRAINT fkf5e1622cfde44a9e FOREIGN KEY (parent) REFERENCES public.location_master(id);


--
-- TOC entry 6248 (class 2606 OID 232037)
-- Name: rch_asha_anc_morbidity_details rch_asha_anc_morbidity_details_morbidity_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_anc_morbidity_details
    ADD CONSTRAINT rch_asha_anc_morbidity_details_morbidity_id_fkey FOREIGN KEY (morbidity_id) REFERENCES public.rch_asha_anc_morbidity_master(id);


--
-- TOC entry 6249 (class 2606 OID 232042)
-- Name: rch_asha_cs_morbidity_details rch_asha_cs_morbidity_details_morbidity_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_cs_morbidity_details
    ADD CONSTRAINT rch_asha_cs_morbidity_details_morbidity_id_fkey FOREIGN KEY (morbidity_id) REFERENCES public.rch_asha_cs_morbidity_master(id);


--
-- TOC entry 6250 (class 2606 OID 232047)
-- Name: rch_asha_pnc_child_morbidity_details rch_asha_pnc_child_morbidity_details_morbidity_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_child_morbidity_details
    ADD CONSTRAINT rch_asha_pnc_child_morbidity_details_morbidity_id_fkey FOREIGN KEY (morbidity_id) REFERENCES public.rch_asha_pnc_child_morbidity_master(id);


--
-- TOC entry 6251 (class 2606 OID 232052)
-- Name: rch_asha_pnc_mother_morbidity_details rch_asha_pnc_mother_morbidity_details_morbidity_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_morbidity_details
    ADD CONSTRAINT rch_asha_pnc_mother_morbidity_details_morbidity_id_fkey FOREIGN KEY (morbidity_id) REFERENCES public.rch_asha_pnc_mother_morbidity_master(id);


--
-- TOC entry 6252 (class 2606 OID 232057)
-- Name: rch_asha_pnc_mother_problem_present rch_asha_pnc_mother_problem_present_pnc_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.rch_asha_pnc_mother_problem_present
    ADD CONSTRAINT rch_asha_pnc_mother_problem_present_pnc_id_fkey FOREIGN KEY (pnc_id) REFERENCES public.rch_asha_pnc_mother_master(id);


--
-- TOC entry 6254 (class 2606 OID 232062)
-- Name: report_parameter_master report_parameter_master_report_master_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.report_parameter_master
    ADD CONSTRAINT report_parameter_master_report_master_id_fkey FOREIGN KEY (report_master_id) REFERENCES public.report_master(id);


--
-- TOC entry 6256 (class 2606 OID 232067)
-- Name: techo_push_notification_config_master techo_push_notification_config_master_type_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_config_master
    ADD CONSTRAINT techo_push_notification_config_master_type_foreign_key FOREIGN KEY (notification_type_id) REFERENCES public.techo_push_notification_type(id);


--
-- TOC entry 6257 (class 2606 OID 232072)
-- Name: techo_push_notification_location_detail techo_push_notification_location_detail_config_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_location_detail
    ADD CONSTRAINT techo_push_notification_location_detail_config_foreign_key FOREIGN KEY (push_config_id) REFERENCES public.techo_push_notification_config_master(id);


--
-- TOC entry 6258 (class 2606 OID 232077)
-- Name: techo_push_notification_master techo_push_notification_master_type_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_master
    ADD CONSTRAINT techo_push_notification_master_type_foreign_key FOREIGN KEY (type) REFERENCES public.techo_push_notification_type(type);


--
-- TOC entry 6259 (class 2606 OID 232082)
-- Name: techo_push_notification_role_user_detail techo_push_notification_role_user_detail_config_foreign_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.techo_push_notification_role_user_detail
    ADD CONSTRAINT techo_push_notification_role_user_detail_config_foreign_key FOREIGN KEY (push_config_id) REFERENCES public.techo_push_notification_config_master(id);


--
-- TOC entry 6260 (class 2606 OID 232087)
-- Name: tr_attendance_topic_rel tr_attendance_topic_rel_attendance_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_attendance_topic_rel
    ADD CONSTRAINT tr_attendance_topic_rel_attendance_id_fkey FOREIGN KEY (attendance_id) REFERENCES public.tr_attendance_master(attendance_id);


--
-- TOC entry 6261 (class 2606 OID 232092)
-- Name: tr_course_role_rel tr_course_role_rel_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_course_role_rel
    ADD CONSTRAINT tr_course_role_rel_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.tr_course_master(course_id);


--
-- TOC entry 6262 (class 2606 OID 232097)
-- Name: tr_course_trainer_role_rel tr_course_trainer_role_rel_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_course_trainer_role_rel
    ADD CONSTRAINT tr_course_trainer_role_rel_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.tr_course_master(course_id);


--
-- TOC entry 6263 (class 2606 OID 232102)
-- Name: tr_training_additional_attendee_rel tr_training_additional_attendee_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_additional_attendee_rel
    ADD CONSTRAINT tr_training_additional_attendee_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6264 (class 2606 OID 232107)
-- Name: tr_training_attendee_rel tr_training_attendee_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_attendee_rel
    ADD CONSTRAINT tr_training_attendee_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6265 (class 2606 OID 232112)
-- Name: tr_training_course_rel tr_training_course_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_course_rel
    ADD CONSTRAINT tr_training_course_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6266 (class 2606 OID 232117)
-- Name: tr_training_optional_trainer_rel tr_training_optional_trainer_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_optional_trainer_rel
    ADD CONSTRAINT tr_training_optional_trainer_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6267 (class 2606 OID 232122)
-- Name: tr_training_org_unit_rel tr_training_org_unit_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_org_unit_rel
    ADD CONSTRAINT tr_training_org_unit_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6268 (class 2606 OID 232127)
-- Name: tr_training_primary_trainer_rel tr_training_primary_trainer_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_primary_trainer_rel
    ADD CONSTRAINT tr_training_primary_trainer_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6269 (class 2606 OID 232132)
-- Name: tr_training_target_role_rel tr_training_target_role_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_target_role_rel
    ADD CONSTRAINT tr_training_target_role_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


--
-- TOC entry 6270 (class 2606 OID 232137)
-- Name: tr_training_trainer_role_rel tr_training_trainer_role_rel_training_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE IF EXISTS ONLY public.tr_training_trainer_role_rel
    ADD CONSTRAINT tr_training_trainer_role_rel_training_id_fkey FOREIGN KEY (training_id) REFERENCES public.tr_training_master(training_id);


-- Completed on 2023-06-29 19:51:14 IST

--
-- PostgreSQL database dump complete
--