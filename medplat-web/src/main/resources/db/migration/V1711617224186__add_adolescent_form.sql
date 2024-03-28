insert into mobile_form_details (form_name,file_name,created_on,created_by,modified_on,modified_by)
values ('ADOLESCENT_SCREENING', 'ADOLESCENT_SCREENING', now(), -1, now(), -1);

insert into mobile_form_feature_rel (form_id, mobile_constant)
select id, 'FHW_MY_PEOPLE' from mobile_form_details where form_name in ('ADOLESCENT_SCREENING');


DROP TABLE IF EXISTS adolescent_entity;

CREATE TABLE if not exists adolescent_entity
(
  id serial primary key,
  member_id integer NOT NULL,
  family_id integer,
  height_of_member integer,
  weight integer,
  created_by integer NOT NULL,
  created_on timestamp without time zone NOT NULL,
  modified_by integer,
  modified_on timestamp without time zone
);