DELETE FROM public.system_configuration where system_key = 'FHS_LAST_UPDATE_TIME';
INSERT INTO public.system_configuration (system_key,is_active,key_value) VALUES
    ('FHS_LAST_UPDATE_TIME',true,EXTRACT(epoch FROM CURRENT_TIMESTAMP(0)));


-- Table: public.system_code_master_list

-- DROP TABLE IF EXISTS public.system_code_master_list;

CREATE TABLE IF NOT EXISTS public.system_code_master_list
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    code_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    code_category character varying(255) COLLATE pg_catalog."default",
    code_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    parent_code character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    desc_type_id character varying(255) COLLATE pg_catalog."default",
    effective_date timestamp without time zone,
    other_details text COLLATE pg_catalog."default",
    published_edition character varying(255) COLLATE pg_catalog."default",
    is_active boolean,
    created_by integer NOT NULL,
    created_on timestamp without time zone NOT NULL DEFAULT now(),
    modified_by integer,
    modified_on timestamp without time zone DEFAULT now(),
    CONSTRAINT system_code_master_list_pkey PRIMARY KEY (id),
    CONSTRAINT system_code_master_list_ukey UNIQUE (code_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.system_code_master_list
    OWNER to postgres;

COMMENT ON TABLE public.system_code_master_list
    IS 'This is master table for code and its management like ICD-10,SNOMED CT,etc';

COMMENT ON COLUMN public.system_code_master_list.id
    IS 'UUID as primary key of table';

COMMENT ON COLUMN public.system_code_master_list.code_type
    IS 'Tyoe of code like SNOMED CT,ICD-10';

COMMENT ON COLUMN public.system_code_master_list.code_category
    IS 'Category of code like A00...Z99,finding,procedure..';

COMMENT ON COLUMN public.system_code_master_list.code_id
    IS 'unique id of code from where code was imported';

COMMENT ON COLUMN public.system_code_master_list.code
    IS 'code like A00.01,U34.99D,386661006';

COMMENT ON COLUMN public.system_code_master_list.parent_code
    IS 'Parent code like SNOMED CT concept id,ICD-10 id';

COMMENT ON COLUMN public.system_code_master_list.name
    IS 'name of entity';

COMMENT ON COLUMN public.system_code_master_list.description
    IS 'Description of entity';

COMMENT ON COLUMN public.system_code_master_list.desc_type_id
    IS 'Description Type of entity';

COMMENT ON COLUMN public.system_code_master_list.effective_date
    IS 'The Date on which this code comes to an effect';

COMMENT ON COLUMN public.system_code_master_list.other_details
    IS 'Other details of code like type_id,module_id,case_significance_id';

COMMENT ON COLUMN public.system_code_master_list.published_edition
    IS 'Code published edition like India COVID-19 Extension for SNOMED CT,Common Drug Codes for India (Terminology Integrated Package),India Reference Sets for SNOMED CT,SNOMED CT International Edition';

COMMENT ON COLUMN public.system_code_master_list.is_active
    IS 'Code is active for use or not';