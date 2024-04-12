INSERT INTO public.listvalue_form_master(
           form_key, form, is_active, is_training_req, query_for_training_completed)
   VALUES ('ADOLESCENT_SCREENING','Adolescent Screening',TRUE,FALSE,null);


INSERT INTO listvalue_field_master(field_key, field, is_active, field_type, form)
VALUES ('listOfAddictions', 'listOfAddictions', true, 'T', 'ADOLESCENT_SCREENING' );

insert into listvalue_field_form_relation (field, form_id)
select f.field, id
from
mobile_form_details mfm,
(
    values
        ('listOfAddictions')
) f(field)
where mfm.form_name = 'ADOLESCENT_SCREENING';


alter table adolescent_entity
add column if not exists has_addictions boolean,
add column if not exists addictions text;