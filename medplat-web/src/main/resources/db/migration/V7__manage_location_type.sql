DELETE FROM public.system_constraint_form_master where form_code='MANAGE_LOCATION_TYPE';
INSERT INTO public.system_constraint_form_master (uuid,form_name,form_code,menu_config_id,web_template_config,web_state,created_by) VALUES
	 ('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1','MANAGE_LOCATION_TYPE','MANAGE_LOCATION_TYPE',340,'[{"type":"CARD","config":{"title":"{{ctrl.headerText}}","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"type","fieldName":"type","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"name","fieldName":"name","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"level","fieldName":"level","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isSohEnable","fieldName":"isSohEnable","fieldType":"CHECKBOX","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"downloadSampleFileText","fieldName":"downloadSampleFile","fieldType":"INFORMATION_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]','ACTIVE',-1);

with field_value_master_deletion as (
                        delete from system_constraint_field_value_master
                        where field_master_uuid in (
                            select uuid
                            from system_constraint_field_master
                            where form_master_uuid = cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid)
                        )
                        returning uuid
                    )
                    delete from system_constraint_field_master
                    where form_master_uuid = cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid);



insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid),'level','level','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'level_MANAGE_LOCATION_TYPE' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','level_MANAGE_LOCATION_TYPE','EN','60512',now(),false,'Level',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid),'type','type','SHORT_TEXT','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'type_MANAGE_LOCATION_TYPE' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','type_MANAGE_LOCATION_TYPE','EN','60512',now(),false,'Type',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid),'name','name','SHORT_TEXT','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'name_MANAGE_LOCATION_TYPE' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','name_MANAGE_LOCATION_TYPE','EN','60512',now(),false,'Name',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),cast('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1' as uuid),'isSohEnable','isSohEnable','CHECKBOX','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'isSohEnable_MANAGE_LOCATION_TYPE' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','isSohEnable_MANAGE_LOCATION_TYPE','EN','60512',now(),false,'Is SOH Enable',false,now(),'WEB');

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8b3cc940-99c3-49cd-b4e7-95d54e766e02' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('afcc2a6e-bd5e-4ba1-9167-9b7d12b13526' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('afeab0bd-a702-483f-a28f-6498ce6e5876' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:1226","type":"Change","value":"typeChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c19a8be7-f794-46a9-bf07-b27a4954cf5d' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d857c779-89ba-4dd9-a9d8-f1db8efebacc' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'TEXT','requiredMessage',null,'Please enter location type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('045d956b-2248-4e79-a0f0-e64b28ea8f28' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'NUMBER','minLength',null,'1',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('08464c0d-8b74-414f-97f6-4f0f020a2d59' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0a0b204d-b87e-4d9a-848b-0286999c939c' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('29df6ec0-4985-4437-b905-51a9e49dcb0e' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'TEXT','placeholder',null,'Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2df823f9-6a0f-49bc-ae22-864f4ae0dbb9' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'NUMBER','maxLength',null,'10',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('339b451d-db6a-4637-8bd4-fd3ae011eb20' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('675159ad-1c62-4e6d-9c7f-a751e5f35bb0' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('35e0411b-1b14-44f0-b3f1-1461842ca998' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3f0e6a37-d51c-4850-ad3c-b4945dcb8089' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'TEXT','placeholder',null,'Name',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('81544fcf-79aa-403b-8a4d-66ac4946d4d5' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','label',null,'level_MANAGE_LOCATION_TYPE',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('17154eb0-5024-4663-9996-fe836b15022b' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','tooltip',null,'Level',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8a338cdd-27bc-45ae-adf9-32f553e3a5b5' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0f9105bb-788a-4658-937c-5acededa8c5c' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','placeholder',null,'Level',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('be5703ef-b7a2-48d4-875d-f564d6775c7a' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b92f6092-a684-477c-b549-a2d19f4813e7' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','requiredMessage',null,'Please enter location level',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('396c448d-dcba-45b0-a476-cae225bf304d' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c0242e5b-5e94-4dcb-a424-f2dda169f366' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'NUMBER','min',null,'0',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f17ae3c2-6385-47d6-82c3-adb0e8022e02' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'NUMBER','max',null,'15',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ecaf518-9a04-41bc-b5b7-b13f65bc3222' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('48edee17-8894-4897-912b-83b58381aa79' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c8b5b8ad-9cd3-4f9f-b92f-8e859f6ebd10' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'TEXT','patternMessage',null,'Please enter valid level',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8f876e6a-a44f-47d3-bd32-07033f475820' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'EVENTS','events',null,'[]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c21a0ee4-3071-4e46-9695-0ed3fea30ac7' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'VISIBILITY','visibility',null,'{}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7b27b6b7-4dc9-45d9-ab83-80cddd262922' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'REQUIRABLE','requirable',null,'{}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c028aace-fd84-4d27-b84e-20849bbb2eea' as uuid),cast('54ec539b-87f7-4cff-8a4d-25b594ae7b33' as uuid),'DISABILITY','disability',null,'{}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('26eb0976-8a5a-49fe-b1f2-753412a0b74a' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'TEXT','tooltip',null,'Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('710ce0ed-25b4-4d86-8c8c-69dfc8b8d613' as uuid),cast('83bdc207-b348-4c3e-8663-057f40a8555f' as uuid),'TEXT','label',null,'type_MANAGE_LOCATION_TYPE',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9089ada1-5baa-4b65-b4a1-a9885a8e791a' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'TEXT','label',null,'name_MANAGE_LOCATION_TYPE',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a3380872-5f31-4471-8581-b8f16c37d185' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a3acc99a-db8f-42f7-a58f-c8f142cc6932' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'NUMBER','minLength',null,'1',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aead89db-c03c-49a9-9ba2-1cc2af8cba61' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'TEXT','tooltip',null,'Name',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c5569ed5-47f1-4d89-a86e-18ad4996d082' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'TEXT','requiredMessage',null,'Please enter location name',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d8e3de4e-3926-4b96-aff4-375f0f3ba330' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e56555c8-acde-4851-bc13-a321d985c593' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:1447","type":"Change","value":"nameChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ea2799e2-5764-4d2a-8b38-caab58d407e2' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'NUMBER','maxLength',null,'20',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('028ef3a2-69c9-4a6a-bc6e-77d2b9d285c2' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('051ecfb5-deb1-44e9-a931-226778f22891' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('30e7e0d5-ad04-4716-ba92-4d0af362dfe4' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('824b2219-5398-469e-8340-aaa1a83cb17e' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('93f181a7-4012-45b1-88fb-1ca789eca9c8' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'TEXT','tooltip',null,'Is SOH Enable',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a252c66a-0bec-4322-b67c-bb1d94b105ad' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'JSON','staticOptions',null,'[{"key":"isSohEnable","value":"isSohEnable"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a4fa1da4-bafd-49c8-8837-e228072a8216' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b06be97f-9fcb-4416-b7d7-2eb528ec47ca' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'TEXT','label',null,'isSohEnable_MANAGE_LOCATION_TYPE',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cc96f74d-d220-4377-9a62-0143d24abb43' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dd152ec7-fa81-4ef9-a1cb-f271571e1cc3' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'TEXT','requiredMessage',null,'Please select is SOH Enable',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3e35bc02-9c31-432b-9fa9-0666f521642f' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'TEXT','placeholder',null,'Is SOH Enable',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('48f45f0d-9e24-47a3-acaf-c8d6a6c9d802' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5c20eebb-1662-426b-a098-649dd610d759' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('61138af1-a030-4118-a760-73c034c53ef7' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6bee492e-120a-403a-a1e7-2784fca1aca3' as uuid),cast('e50c34f9-3b7f-418a-bed3-ec6ea03f541f' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7479bb3e-a7e8-4639-9fee-53bf6c3e42bc' as uuid),cast('dd871df3-3b25-407e-a3a5-bc49c580e638' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());
