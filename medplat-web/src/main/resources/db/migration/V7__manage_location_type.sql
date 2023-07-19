DELETE FROM public.system_constraint_form_master where form_code='MANAGE_LOCATION_TYPE';
INSERT INTO public.system_constraint_form_master (uuid,form_name,form_code,menu_config_id,web_template_config,web_state,created_by) VALUES
	 ('c6ee39bb-3a99-4e7f-9695-0e301f48c3e1','MANAGE_LOCATION_TYPE','MANAGE_LOCATION_TYPE',340,'[{"type":"CARD","config":{"title":"{{ctrl.headerText}}","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"type","fieldName":"type","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"name","fieldName":"name","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"level","fieldName":"level","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isSohEnable","fieldName":"isSohEnable","fieldType":"CHECKBOX","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"downloadSampleFileText","fieldName":"downloadSampleFile","fieldType":"INFORMATION_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]','ACTIVE',-1);

DELETE FROM public.system_constraint_form_master WHERE form_code = 'RCH_FACILITY_CHILD_SERVICE' ;
INSERT INTO public.system_constraint_form_master (uuid,form_name,form_code,menu_config_id,web_template_config,web_state,created_by,created_on,modified_by,modified_on,mobile_state) VALUES
	 ('ceca045a-f256-49ae-acdc-0909f32f2f40','RCH_FACILITY_CHILD_SERVICE','RCH_FACILITY_CHILD_SERVICE',151,'[{"type":"CARD","config":{"title":"Record Details","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"uniqueHealthId","fieldName":"uniqueHealthId","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"memberName","fieldName":"childName","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"gender","fieldName":"gender","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"locationHierarchy","fieldName":"locationHierarchy","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"caste","fieldName":"caste","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"familyUniqueId","fieldName":"familyId","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"motherMobileNumber","fieldName":"motherMobileNumber","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"dob","fieldName":"dob","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"anmInfo","fieldName":"anmInfo","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ashaInfo","fieldName":"ashaInfo","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]}]},{"type":"CARD","config":{"title":"Child Service Details","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"contactNumber","fieldName":"motherMobileNumber","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"serviceDate","fieldName":"serviceDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deliveryPlace","fieldName":"placeOfVisit","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deliveryPlace","fieldName":"institutionType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hospitalByType","fieldName":"hospitalByType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hospitalByUser","fieldName":"hospitalByUser","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isAlive","fieldName":"isAlive","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathDate","fieldName":"deathDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"placeOfDeath","fieldName":"deathPlace","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathInfrastructureType","fieldName":"deathInstitutionType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathHospitalByType","fieldName":"deathHospitalByType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathReason","fieldName":"deathReason","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"otherDeathReason","fieldName":"otherDeathReason","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"weight","fieldName":"weight","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"height","fieldName":"height","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"sdScore","fieldName":"sdScore","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"font-weight:100"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"midArmCircumference","fieldName":"muac","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"havePedalEdema","fieldName":"pedalEdema","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ifaSyrupGiven","fieldName":"ifaSyrupGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"complementaryFeedingStarted","fieldName":"complementaryFeedingStarted","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"complementaryFeedingStartPeriod","fieldName":"complementaryFeedingStartPeriod","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"dieseases","fieldName":"diseases","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"otherDiseases","fieldName":"otherDiseases","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isTreatementDone","fieldName":"isTreatmentDone","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"exclusivelyBreastfeded","fieldName":"exclusiveBreastFeeding","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]}]}]}]}]},{"type":"CARD","config":{"title":"Immunisation Details","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":true,"ngIf":"ctrl.showImmunisationSection","isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"hepatitisB0Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"vitaminKGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"bcgGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"opv0Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"opv1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"opv2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"opv3Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"opvBoosterGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"penta1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"penta2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"penta3Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"dpt1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"dpt2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"dpt3Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"dptBoosterGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"rotaVirus1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"rotaVirus2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"rotaVirus3Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"measles1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"measles2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"measlesRubella1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"measlesRubella2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"fIpv101Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"fIpv201Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"fIpv205Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"given","fieldName":"vitaminAGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"hepatitisB0Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"vitaminKDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"bcgDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"opv0Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"opv1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"opv2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"opv3Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"opvBoosterDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"penta1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"penta2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"penta3Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"dpt1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"dpt2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"dpt3Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"dptBoosterDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"rotaVirus1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"rotaVirus2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"rotaVirus3Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"measles1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"measles2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"measlesRubella1Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"measlesRubella2Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"fIpv101Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"fIpv201Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"fIpv205Date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"date","fieldName":"vitaminADate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]}]},{"type":"CARD","config":{"title":"Cerebral Palsy Questions","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":true,"isCollapsible":false,"ngIf":"ctrl.isAnyCpQuestionDisplay"},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":"","cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"holdHeadStraight","fieldName":"holdHeadStraight","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"handsInMouth","fieldName":"handsInMouth","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"lookWhenSpeak","fieldName":"lookWhenSpeak","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"sitWithoutHelp","fieldName":"sitWithoutHelp","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"kneelDown","fieldName":"kneelDown","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"avoidStrangers","fieldName":"avoidStrangers","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"understandNo","fieldName":"understandNo","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"mimicOthers","fieldName":"mimicOthers","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"drinkFromGlass","fieldName":"drinkFromGlass","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"runIndependently","fieldName":"runIndependently","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"holdThingsWithFinger","fieldName":"holdThingsWithFinger","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"tellNameOfThings","fieldName":"tellNameOfThings","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"flipPages","fieldName":"flipPages","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"kickBall","fieldName":"kickBall","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"makeNoiseWhenSpeak","fieldName":"makeNoiseWhenSpeak","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"lookInDirectionOfSound","fieldName":"lookInDirectionOfSound","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"enjoyPeekaboo","fieldName":"enjoyPeekaboo","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"respondsOnNameCalling","fieldName":"respondsOnNameCalling","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"liftToys","fieldName":"liftToys","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"lookWhenNameCalled","fieldName":"lookWhenNameCalled","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"speakSimpleWords","fieldName":"speakSimpleWords","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"understandInstructions","fieldName":"understandInstructions","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"climbUpDownStairs","fieldName":"climbUpDownStairs","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"speakTwoSentences","fieldName":"speakTwoSentences","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"likePlayingWithOtherChildren","fieldName":"likePlayingWithOtherChildren","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"white-space:initial"},"elements":[]}]}]}]}]}]}]','ACTIVE',NULL,NULL,60512,'2021-09-27 17:43:01.776',NULL);


DELETE FROM public.system_constraint_form_master WHERE form_name = 'RCH_FACILITY_ANC' ;
INSERT INTO public.system_constraint_form_master
(uuid, form_name, form_code, menu_config_id, web_template_config, web_state, created_by, created_on, modified_by, modified_on, mobile_state)
VALUES('808a9e11-7f8c-4ef8-b61f-834220a1755b', 'RCH_FACILITY_ANC', 'RCH_FACILITY_ANC', 152, '[{"type":"CARD","config":{"title":"RECORD DETAILS","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":true},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"uniqueHealthId","fieldName":"uniqueHealthId","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"labelCssStyles":"","labelCssClasses":"","inputCssStyles":""},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"accountNumber","fieldName":"accountNumber","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ifsc","fieldName":"ifsc","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"mobileNumber","fieldName":"mobileNumber","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"locationHierarchy","fieldName":"locationHierarchy","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"lmpDate","fieldName":"lmpDate","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"anmInfo","fieldName":"anmInfo","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"memberName","fieldName":"memberName","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"caste","fieldName":"caste","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"benefits","fieldName":"benefits","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"bpl","fieldName":"bpl","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"edd","fieldName":"edd","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ashaInfo","fieldName":"ashaInfo","fieldType":"INFORMATION_DISPLAY","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]},{"type":"CARD","config":{"title":"PREVIOUS ANC INFORMATION","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":true,"isCollapsible":true,"ngIf":"ctrl.previousAncInfo.length"},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"previousAncInformation","fieldName":"previousAncInformation","fieldType":"TABLE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]},{"type":"CARD","config":{"title":"PREVIOUS VACCINATIONS GIVEN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":true,"ngIf":"ctrl.previousVaccinations.length","isCollapsible":true},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"previousVaccinationsGiven","fieldName":"previousVaccinationsGiven","fieldType":"TABLE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]},{"type":"CARD","config":{"title":"ANC VISIT DETAILS","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null,"isCollapsible":false},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"6","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isAlive","fieldName":"isAlive","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"accountNumber","fieldName":"accountNumber","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ifsc","fieldName":"ifsc","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"mobileNumber","fieldName":"mobileNumber","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"serviceDate","fieldName":"serviceDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deliveryPlace","fieldName":"deliveryPlace","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hospitalByUser","fieldName":"hospitalByUser","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"institutionType","fieldName":"institutionType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hospitalByType","fieldName":"hospitalByType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"bloodGroup","fieldName":"bloodGroup","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"jsyBeneficiary","fieldName":"jsyBeneficiary","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"jsyPaymentDone","fieldName":"jsyPaymentDone","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"kpsyBeneficiary","fieldName":"kpsyBeneficiary","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"iayBeneficiary","fieldName":"iayBeneficiary","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hbsagTest","fieldName":"hbsagTest","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"vdrlTest","fieldName":"vdrlTest","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"hivTest","fieldName":"hivTest","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"sickleCellTest","fieldName":"sickleCellTest","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ifaTabletsGiven","fieldName":"ifaTabletsGiven","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"faTabletsGiven","fieldName":"faTabletsGiven","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"calciumTabletsGiven","fieldName":"calciumTabletsGiven","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"albendazoleGiven","fieldName":"albendazoleGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"tt1Given","fieldName":"tt1Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"tt1date","fieldName":"tt1date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"tt2Given","fieldName":"tt2Given","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"tt2date","fieldName":"tt2date","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ttBoosterGiven","fieldName":"ttBoosterGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ttBoosterDate","fieldName":"ttBoosterDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"bloodTransfusion","fieldName":"bloodTransfusion","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ironDefAnemiaInj","fieldName":"ironDefAnemiaInj","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"ironDefAnemiaInjDueDate","fieldName":"ironDefAnemiaInjDueDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"referralDone","fieldName":"referralDone","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"referralInfraType","fieldName":"referralInfraType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"referralHospitalByType","fieldName":"referralHospitalByType","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"expectedDeliveryPlace","fieldName":"expectedDeliveryPlace","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathDate","fieldName":"deathDate","fieldType":"DATE","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"placeOfDeath","fieldName":"placeOfDeath","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"deathReason","fieldName":"deathReason","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]},{"type":"ROW","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"memberHeight","fieldName":"memberHeight","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"weight","fieldName":"weight","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"haemoglobinCount","fieldName":"haemoglobinCount","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"systolicBp","fieldName":"systolicBp","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"diastolicBp","fieldName":"diastolicBp","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"foetalMovement","fieldName":"foetalMovement","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"foetalHeight","fieldName":"foetalHeight","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"foetalHeartSound","fieldName":"foetalHeartSound","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"foetalPosition","fieldName":"foetalPosition","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"bloodSugarTest","fieldName":"bloodSugarTest","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"sugarTestBeforeFoodValue","fieldName":"sugarTestBeforeFoodValue","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"sugarTestAfterFoodValue","fieldName":"sugarTestAfterFoodValue","fieldType":"NUMBER","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"urineTestDone","fieldName":"urineTestDone","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"urineAlbumin","fieldName":"urineAlbumin","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"urineSugar","fieldName":"urineSugar","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"lastDeliveryOutcome","fieldName":"lastDeliveryOutcome","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"pregnencyComplication","fieldName":"pregnencyComplication","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"dangerousSignIds","fieldName":"dangerousSignIds","fieldType":"DROPDOWN","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"otherDangerousSign","fieldName":"otherDangerousSign","fieldType":"SHORT_TEXT","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"examinedByGynecologist","fieldName":"examinedByGynecologist","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]},{"type":"COL","config":{"size":"12","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[{"type":"TECHO_FORM_FIELD","config":{"fieldKey":"isInjCorticosteroidGiven","fieldName":"isInjCorticosteroidGiven","fieldType":"RADIO","cssStyles":null,"cssClasses":null,"isRepeatable":false,"showAddRemoveButton":false,"ngModel":null,"isConditional":false,"ngIf":null},"elements":[]}]}]}]}]}]}]', 'ACTIVE', NULL, NULL, 97067, '2021-12-06 18:18:14.639', NULL);

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


with field_value_master_deletion as (
                        delete from system_constraint_field_value_master
                        where field_master_uuid in (
                            select uuid
                            from system_constraint_field_master
                            where form_master_uuid = cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid)
                        )
                        returning uuid
                    )
                    delete from system_constraint_field_master
                    where form_master_uuid = cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid);



insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'locationHierarchy','locationHierarchy','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'caste','caste','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'benefits','benefits','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'lmpDate','lmpDate','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ashaInfo','ashaInfo','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'jsyPaymentDone','jsyPaymentDone','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'jsyPaymentDone_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','jsyPaymentDone_RCH_FACILITY_ANC','EN','-1',now(),false,'JSY Payment Done',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'jsyBeneficiary','jsyBeneficiary','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'jsyBeneficiary_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','jsyBeneficiary_RCH_FACILITY_ANC','EN','-1',now(),false,'JSY Beneficiary',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'kpsyBeneficiary','kpsyBeneficiary','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'kpsyBeneficiary_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','kpsyBeneficiary_RCH_FACILITY_ANC','EN','-1',now(),false,'KPSY Beneficiary',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'iayBeneficiary','iayBeneficiary','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'iayBeneficiary_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','iayBeneficiary_RCH_FACILITY_ANC','EN','-1',now(),false,'IAY Beneficiary',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'hbsagTest','hbsagTest','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'hbsagTest_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','hbsagTest_RCH_FACILITY_ANC','EN','-1',now(),false,'HBsAg Test',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'hospitalByType','hospitalByType','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'hospitalByType_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','hospitalByType_RCH_FACILITY_ANC','EN','-1',now(),false,'Hospital',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'hivTest','hivTest','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'hivTest_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','hivTest_RCH_FACILITY_ANC','EN','-1',now(),false,'HIV Test',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'tt2date','tt2date','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'tt2date_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','tt2date_RCH_FACILITY_ANC','EN','-1',now(),false,'TT2 Given on Date',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'anmInfo','ANM Information','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),57698,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'deliveryPlace','deliveryPlace','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'deliveryPlace_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','deliveryPlace_RCH_FACILITY_ANC','EN','-1',now(),false,'Delivery Place',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'hospitalByUser','hospitalByUser','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'hospitalByUser_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','hospitalByUser_RCH_FACILITY_ANC','EN','-1',now(),false,'Hospital',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'sickleCellTest','sickleCellTest','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'sickleCellTest_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','sickleCellTest_RCH_FACILITY_ANC','EN','-1',now(),false,'Sickle Cell Test',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ironDefAnemiaInj','ironDefAnemiaInj','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ironDefAnemiaInj_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ironDefAnemiaInj_RCH_FACILITY_ANC','EN','-1',now(),false,'Iron Deficiency Anemia Injection',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'tt1Given','tt1Given','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'tt1Given_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','tt1Given_RCH_FACILITY_ANC','EN','-1',now(),false,'TT1 Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'tt2Given','tt2Given','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'tt2Given_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','tt2Given_RCH_FACILITY_ANC','EN','-1',now(),false,'TT2 Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'foetalMovement','foetalMovement','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'foetalMovement_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','foetalMovement_RCH_FACILITY_ANC','EN','-1',now(),false,'Foetal Movement',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'faTabletsGiven','faTabletsGiven','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'faTabletsGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','faTabletsGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'No. of FA Tablets Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'bloodTransfusion','bloodTransfusion','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'bloodTransfusion_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','bloodTransfusion_RCH_FACILITY_ANC','EN','-1',now(),false,'Blood Transfusion Done',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'foetalPosition','foetalPosition','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'foetalPosition_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','foetalPosition_RCH_FACILITY_ANC','EN','-1',now(),false,'Foetal Position',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ironDefAnemiaInjDueDate','ironDefAnemiaInjDueDate','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ironDefAnemiaInjDueDate_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ironDefAnemiaInjDueDate_RCH_FACILITY_ANC','EN','-1',now(),false,'Due date of Iron Deficiency Anemia Injection',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'referralDone','referralDone','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'referralDone_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','referralDone_RCH_FACILITY_ANC','EN','-1',now(),false,'Referral Done',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ttBoosterGiven','ttBoosterGiven','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ttBoosterGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ttBoosterGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'TT Booster Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ttBoosterDate','ttBoosterDate','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ttBoosterDate_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ttBoosterDate_RCH_FACILITY_ANC','EN','-1',now(),false,'TT Booster Given on Date',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'deathDate','deathDate','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'deathDate_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','deathDate_RCH_FACILITY_ANC','EN','-1',now(),false,'Death Date',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'vdrlTest','vdrlTest','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'vdrlTest_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','vdrlTest_RCH_FACILITY_ANC','EN','-1',now(),false,'VDRL Test',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'tt1date','tt1date','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'tt1date_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','tt1date_RCH_FACILITY_ANC','EN','-1',now(),false,'TT1 Given on Date',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'calciumTabletsGiven','calciumTabletsGiven','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'calciumTabletsGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','calciumTabletsGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'Calcium Tablets Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'weight','weight','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'weight_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','weight_RCH_FACILITY_ANC','EN','-1',now(),false,'Weight',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'lastDeliveryOutcome','lastDeliveryOutcome','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'lastDeliveryOutcome_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','lastDeliveryOutcome_RCH_FACILITY_ANC','EN','-1',now(),false,'Last Delivery Outcome',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'foetalHeartSound','foetalHeartSound','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'foetalHeartSound_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','foetalHeartSound_RCH_FACILITY_ANC','EN','-1',now(),false,'Foetal Heart Sound',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'bloodSugarTest','bloodSugarTest','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'bloodSugarTest_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','bloodSugarTest_RCH_FACILITY_ANC','EN','-1',now(),false,'Blood Sugar Test',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'sugarTestAfterFoodValue','sugarTestAfterFoodValue','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'sugarTestAfterFoodValue_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','sugarTestAfterFoodValue_RCH_FACILITY_ANC','EN','-1',now(),false,'Blood Sugar Test Value',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'dangerousSignIds','dangerousSignIds','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'dangerousSignIds_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','dangerousSignIds_RCH_FACILITY_ANC','EN','-1',now(),false,'Dangerous Signs',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'urineTestDone','urineTestDone','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'urineTestDone_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','urineTestDone_RCH_FACILITY_ANC','EN','-1',now(),false,'Urine Test Done',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'urineAlbumin','urineAlbumin','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'urineAlbumin_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','urineAlbumin_RCH_FACILITY_ANC','EN','-1',now(),false,'Urine Albumin',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'urineSugar','urineSugar','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'urineSugar_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','urineSugar_RCH_FACILITY_ANC','EN','-1',now(),false,'Urine Sugar',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'serviceDate','serviceDate','DATE','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'serviceDate_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','serviceDate_RCH_FACILITY_ANC','EN','-1',now(),false,'2021-08-12 16:03:34.711305+05:30',false,now(),'TECHO_MOBILE_APP');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','serviceDate_RCH_FACILITY_ANC','EN','-1',now(),false,'Service Date',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'institutionType','institutionType','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'institutionType_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','institutionType_RCH_FACILITY_ANC','EN','-1',now(),false,'Institution Type',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ifaTabletsGiven','ifaTabletsGiven','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ifaTabletsGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ifaTabletsGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'No. of IFA Tablets Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'isAlive','isAlive','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'isAlive_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','isAlive_RCH_FACILITY_ANC','EN','-1',now(),false,'Is Member Alive',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'deathReason','deathReason','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'deathReason_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','deathReason_RCH_FACILITY_ANC','EN','-1',now(),false,'Death Reason',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'mobileNumber','mobileNumber','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'albendazoleGiven','albendazoleGiven','RADIO','formData','WEB',null,60512,now(),1094,now());

delete from internationalization_label_master where key = 'albendazoleGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','albendazoleGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'Albendanzole Tablets (400 mg)',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'edd','edd','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),78434,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'bpl','bpl','INFORMATION_DISPLAY','formData','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'uniqueHealthId','Unique Health ID','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),57698,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ifsc','ifsc','SHORT_TEXT','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'ifsc_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','ifsc_RCH_FACILITY_ANC','EN','-1',now(),false,'IFSC',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'referralInfraType','referralInfraType','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'referralInfraType_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','referralInfraType_RCH_FACILITY_ANC','EN','-1',now(),false,'Institution Type',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'previousVaccinationsGiven','previousVaccinationsGiven','TABLE',null,'WEB',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'ifsc','ifsc','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'otherDangerousSign','otherDangerousSign','SHORT_TEXT','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'otherDangerousSign_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','otherDangerousSign_RCH_FACILITY_ANC','EN','-1',now(),false,'Other Danger Sign',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'placeOfDeath','placeOfDeath','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'placeOfDeath_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','placeOfDeath_RCH_FACILITY_ANC','EN','-1',now(),false,'Death Place',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'isInjCorticosteroidGiven','isInjCorticosteroidGiven','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'isInjCorticosteroidGiven_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','isInjCorticosteroidGiven_RCH_FACILITY_ANC','EN','-1',now(),false,'Inj. Corticosteroid Given',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'examinedByGynecologist','examinedByGynecologist','RADIO','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'examinedByGynecologist_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','examinedByGynecologist_RCH_FACILITY_ANC','EN','-1',now(),false,'Examined under PMSMA',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'pregnencyComplication','pregnencyComplication','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'pregnencyComplication_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','pregnencyComplication_RCH_FACILITY_ANC','EN','-1',now(),false,'Presence of complication during any previous pregnancy',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'bloodGroup','bloodGroup','DROPDOWN','formData','WEB',
(
                                select case when count(1) = 1 then cast('7d1ca0d9-d5f6-4012-94d1-ebbce35dc469' as uuid) else null end
                                from system_constraint_standard_field_master
                                where uuid = cast('7d1ca0d9-d5f6-4012-94d1-ebbce35dc469' as uuid)
                            ),
60512,now(),60512,now());

delete from internationalization_label_master where key = 'bloodGroup_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','bloodGroup_RCH_FACILITY_ANC','EN','-1',now(),false,'Blood Group',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'haemoglobinCount','haemoglobinCount','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'haemoglobinCount_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','haemoglobinCount_RCH_FACILITY_ANC','EN','-1',now(),false,'Haemoglobin',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'memberHeight','memberHeight','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'memberHeight_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','memberHeight_RCH_FACILITY_ANC','EN','-1',now(),false,'Height (in cm)',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'systolicBp','systolicBp','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'systolicBp_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','systolicBp_RCH_FACILITY_ANC','EN','-1',now(),false,'Systolic Blood Pressure',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'diastolicBp','diastolicBp','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'diastolicBp_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','diastolicBp_RCH_FACILITY_ANC','EN','-1',now(),false,'Diastolic Blood Pressure',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'foetalHeight','foetalHeight','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'foetalHeight_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','foetalHeight_RCH_FACILITY_ANC','EN','-1',now(),false,'Fundal Height',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'sugarTestBeforeFoodValue','sugarTestBeforeFoodValue','NUMBER','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'sugarTestBeforeFoodValue_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','sugarTestBeforeFoodValue_RCH_FACILITY_ANC','EN','-1',now(),false,'Blood Sugar Test Value',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'previousAncInformation','previousAncInformation','TABLE',null,'WEB',null,60512,now(),60512,now());

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'expectedDeliveryPlace','expectedDeliveryPlace','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'expectedDeliveryPlace_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','expectedDeliveryPlace_RCH_FACILITY_ANC','EN','-1',now(),false,'Expected Delivery Place',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'mobileNumber','mobileNumber','SHORT_TEXT','formData','WEB',
(
                                select case when count(1) = 1 then cast('3c504bd8-285b-4936-b00a-c790e837042a' as uuid) else null end
                                from system_constraint_standard_field_master
                                where uuid = cast('3c504bd8-285b-4936-b00a-c790e837042a' as uuid)
                            ),
60512,now(),60512,now());

delete from internationalization_label_master where key = 'mobileNumber_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','mobileNumber_RCH_FACILITY_ANC','EN','-1',now(),false,'Mobile Number',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'referralHospitalByType','referralHospitalByType','DROPDOWN','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'referralHospitalByType_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','referralHospitalByType_RCH_FACILITY_ANC','EN','-1',now(),false,'Hospital',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'accountNumber','accountNumber','SHORT_TEXT','formData','WEB',null,60512,now(),60512,now());

delete from internationalization_label_master where key = 'accountNumber_RCH_FACILITY_ANC' and app_name in ('WEB');

insert into internationalization_label_master(country,key,language,created_by,created_on,custom3b,text,translation_pending,modified_on,app_name)
                                values('IN','accountNumber_RCH_FACILITY_ANC','EN','-1',now(),false,'Account Number',false,now(),'WEB');

insert into system_constraint_field_master(uuid,form_master_uuid,field_key,field_name,field_type,ng_model,app_name,standard_field_master_uuid,created_by,created_on,modified_by,modified_on)
                         values(cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),cast('808a9e11-7f8c-4ef8-b61f-834220a1755b' as uuid),'accountNumber','accountNumber','INFORMATION_DISPLAY','memberDetails','ALL',null,60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e1d1bc8d-caec-437c-8859-d069efb72cf1' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('85cf1f1c-6628-42fa-9ff5-b86ba3324248' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'TEXT','tooltip',null,'IFSC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ef73e552-cb62-4f72-b10c-4e109726f191' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"''EMPTY''","value2":null,"fieldName":"formData.bloodSugarTest","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8f3a4174-2611-4546-bf6d-67cdc6ed4fe5' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8a249f61-418d-4ddd-bb7d-47a35eb2cfac' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('22b5222f-999c-48c4-a108-675f1c4d7b28' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','tooltip',null,'Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2675d473-e48a-4c96-a639-4f3e22f1d551' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'NUMBER','max',null,'600',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4f2d26bb-873e-4451-97a5-79b95883053a' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','label',null,'sugarTestBeforeFoodValue_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5397f54b-3ad9-410d-b3ae-e813ddd3b187' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a7c990ae-d1c8-448c-be25-f513d71ef966' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23ab5062-91c0-41f7-a432-998ea4740784' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('24b1840f-4155-45d9-8075-a23cea55064c' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('41d025b2-e16e-4e8b-a596-5f13c862e117' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'TEXT','displayValue',null,'Location Hierarchy',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5fc426fb-1fe4-4914-8837-58fa2695b054' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7ee17d76-5567-4ecd-ace7-529543812304' as uuid),cast('f807fabb-8571-4e31-b131-0f29eea0b2d1' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('12256bf2-cd79-4373-9f39-f7c5365512fd' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8ec8e16d-9a4e-4bc1-82f8-64a26cb4ef5a' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('915ccca1-cfde-48db-8320-217e5afd6094' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('95595451-b661-4605-aedd-073383e31742' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96b3a632-207e-417e-82a5-027de8e97980' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'TEXT','label',null,'isAlive_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9a41741b-6fdf-433f-8516-28f03e4cffce' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aab191a3-f17c-48a2-82ae-82fa166e2d44' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b6cda58d-b220-4641-bd02-4e2e6a4ab7e9' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:6791","type":"Change","value":"ironDefAnemiaInjChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1cee83fe-7c2e-48bb-92f6-5294a26857f2' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('76896310-a26e-422d-92d5-b84593ba3ad7' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9d7cc8ec-9924-48cc-a77e-a2ba3cb68bf4' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b56352ef-36db-4a8b-89dd-ef44a3721c79' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'TEXT','displayValue',null,'Member ID',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d992ed09-4c1f-4437-81a0-b830d05f6433' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'DISABILITY','disability',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dc133b45-77eb-43ca-9dd5-1ac13208fd85' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f9116b77-e127-465d-9684-f4730fe4d67a' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'EVENTS','events',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('65e9fe31-9c8a-4e1f-bbaa-427c94701a5e' as uuid),cast('64bb4abe-8890-49e2-9c11-da0e3d738ec6' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b5596924-a8f1-4831-a38e-924057dbf93e' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0fcbe37b-8c6e-45d1-a1ff-c211051c9d9c' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('21e7667a-d8c2-4b86-bb08-8effcb3f2126' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5ad7525a-f1ed-43c1-abdf-154bc9b87d00' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('794c7ee5-3f85-4d36-889e-9b047ccf8b1e' as uuid),cast('7064db52-6ac6-4732-9157-3a26da4787ec' as uuid),'TEXT','displayValue',null,'Caste',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d726d834-2dfc-4ad1-8798-7fb4b053f561' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'DROPDOWN','displayType',null,'date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('13fa7b59-504e-4eb2-b754-3bfd3982f592' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'TEXT','displayValue',null,'LMP',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('14ae7dd4-4d72-4690-8ee7-716529e96153' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('30a7cb67-4941-41f3-9386-2bc8d39ae270' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('412a5b32-d793-404e-b69d-fdbd52407110' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('613f408a-1865-4012-b7cd-a20c17301dcf' as uuid),cast('f944e285-df85-42e6-b25a-e86e66bcbaf7' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('df159bfc-2e48-4191-a9cd-76c5d80087d3' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('eecfa3d6-33c1-4477-bbd0-5f2e87b51eaf' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('063ae32d-b0f7-425f-9acc-8e062ffe6a11' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('129c2ab5-1d55-410f-80b4-66e0fa4bac11' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','tooltip',null,'Due date of Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('65a65e21-129b-45fe-8d80-b18eec5c3ee8' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('65c39164-869a-43af-9fec-2caac08ba954' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','minDateField',null,'minInjDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7053c20b-3d08-4b71-a99f-b11b14948d95' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8391def6-3a6e-47b1-97ee-b5164f8fcec3' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'TEXT','tooltip',null,'Haemoglobin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8a5d13e4-7e82-41f0-944d-b01619ccc9f8' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('837fec16-2940-475f-8b6f-848f636acef9' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b2e99342-ba49-48f5-95b5-beebff732322' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ea9d655a-4265-4484-a630-9cf53f431b1a' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('12b6d154-a015-4e9d-a66a-7c5355ce5aec' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('226c9dda-cb5d-4e73-81aa-dfe21d93bf66' as uuid),cast('71ad7966-7958-4f68-8936-e85a6eb38523' as uuid),'TEXT','displayValue',null,'Beneficiary Eligibility',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('abb496d2-a761-4819-8a34-eeb95f5d903d' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8f41e477-33f4-4154-8c99-4e850d3db3f6' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'TEXT','displayValue',null,'ASHA Name and Phone Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aef067c6-f640-4de2-8d02-1438adf5b1a5' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bc4d01a9-9752-47c1-ab4e-b4bc1f96a790' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('db8fae8d-2aac-4a42-931f-7e3d239b6e2f' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('512ec350-6788-48e8-b2b5-6013167186ca' as uuid),cast('aec42224-12fe-4a06-90b8-1921534226cf' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('911c9196-baeb-4c07-930d-e415478f3917' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'NUMBER','min',null,'2',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ae237fdb-25bd-483e-9941-2c3ed83bc756' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'DROPDOWN','displayType',null,'date',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b0af5054-1f22-4274-bf10-feed6186ab69' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'EVENTS','events',null,'null',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c92a8085-800d-49fa-82e7-07d4d0c42c24' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cd94bfb4-54bd-43ae-8dfb-929b15bb4791' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'DISABILITY','disability',null,'null',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fa344436-1d6e-473c-886d-b71fb1a889ec' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6bbb1bcd-3424-4eac-ac09-4dfe686ea0b0' as uuid),cast('740227dc-4e40-411a-bba6-3a109cbbfabf' as uuid),'TEXT','displayValue',null,'EDD',60512,now(),78434,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9ebaff35-be16-4360-9973-b66df13195ab' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c04da844-1579-49df-8df8-bbe1950190fb' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2d692f9-cd81-409b-8b30-c4d3d72b2cb6' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'TEXT','displayValue',null,'BPL',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d9af9e53-545a-48b8-baf5-0628060aa3d1' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0ae90e4a-71ae-4bbf-ac2a-6e205d1b9adb' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5bbb5eba-2454-42cc-a125-25153b0a5a57' as uuid),cast('0a67b1d7-46cd-4b85-ac48-8883b664a4a0' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f2e3c887-e68d-42ca-a2b0-449e7b63e7c2' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1f90b81d-34b3-4108-8e02-bbfefa26075f' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'DISABILITY','disability',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1ff2de2d-b5fe-423a-839e-6328eee4e1e4' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'TEXT','displayValue',null,'FHW Name and Phone Number',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('30ccbac8-ebcb-4fa6-82d6-398d9979489f' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('40eae510-01a3-49e6-aff6-7fec73ab7b83' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7e980bd8-c7cb-480d-a4f0-07aed02e9cb2' as uuid),cast('6ba3f9a1-a8b9-4ec3-944d-72a8bb0f09aa' as uuid),'EVENTS','events',null,'null',60512,now(),57698,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('72a0ba8b-e0a6-435f-b630-bbce848a35ab' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"''IRON_SUCROSE''","value2":null,"fieldName":"formData.ironDefAnemiaInj","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bafd878e-a0e9-47ac-90d1-40aae3491b2d' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5600c1fc-8a87-4a18-9882-6c8601a883dc' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'BOOLEAN','isHidden',null,'true',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5b238645-8f54-4121-97b7-344f76214ad1' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5bbc8373-539a-46aa-844d-443a6b627840' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.visitmorethan3===true"}]}}',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d1029c34-1a6c-49c2-b5a5-0976acba2f99' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a027438c-b202-4970-aa76-b23c5296f009' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c46ed0d6-950d-497f-9ca5-ab0d6f7f16e9' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','placeholder',null,'Calcium Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8cf47a3c-4b6e-454d-933b-e3d87657a89a' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f402e79e-e6a9-4b1f-9bcd-3870b6ee5899' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','requiredMessage',null,'Please enter Calcium Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('18cba37b-a017-47c2-85e7-7b4869ca18a7' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('27e46001-9eb0-4071-9712-7a653530c3f0' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','label',null,'calciumTabletsGiven_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4e6f99a9-a896-4c72-8a22-ea629d6d46fe' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5a508455-fd51-4890-b65d-74132309a94d' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','tooltip',null,'Calcium Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6be43793-af15-45c7-b78d-abd3336ed7ed' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9da55233-c320-4ad3-9fe8-31d68155ab31' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','maxDateField',null,'today',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b70bba8d-4a60-4be2-9454-ae2c2681cdbc' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a0a57024-0f9f-4f4e-afa9-6d2f5720acf1' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a2ca0993-8926-4272-9c43-3b8569009b84' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'TEXT','displayValue',null,'Mobile Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b1a2de5e-f6f4-438c-8abd-4942f7d7d14b' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fe867a7e-a05e-4246-9080-3b4c2023590e' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('256ac662-9699-415d-a9b7-1596b8454ec4' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4360ef0c-9d28-4403-a880-926ebcae0bd4' as uuid),cast('3013f732-aaa7-4baa-abc6-2d5d1d7eb25f' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd4c6038-4f69-4d22-9d69-4676c909e590' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'TEXT','listValueField',null,'Health Infrastructure Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('91cb7c46-d596-486c-b7e7-b75de4ef2734' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'TEXT','requiredMessage',null,'Please select hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a9b20eb8-21dd-44c5-a61e-bce4a2bb94e1' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'TEXT','queryBuilder',null,'retrieve_health_infra_for_user',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b76659f7-c19d-4156-89d1-ef16ba49abe6' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'TEXT','label',null,'hospitalByUser_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c52da0ed-b700-446f-be99-33549e921c01' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'TEXT','placeholder',null,'Select Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('05bbb24c-20b7-4b89-bb15-b27ee41e3b7c' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('945ce4f9-0c25-4692-9d48-fba3aafba633' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9509d1f9-f5be-4967-990e-33a50f4cb4e8' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','label',null,'deathDate_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6c498725-80ac-49e8-ba86-c1ae06b4a339' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'TEXT','requiredMessage',null,'Please select institution type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7ac42f78-7c41-4560-accb-238e8ab6cd65' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'DROPDOWN','optionsType',null,'listValueField',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9f86935b-fd2d-4f38-a028-a51328d01cc6' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b523a835-8dc4-4d7d-8c0a-b14bbc29c076' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"''HOSP''","value2":null,"fieldName":"formData.deliveryPlace","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b8880d0f-78c1-4367-8d80-967a1061c91e' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'TEXT','queryBuilder',null,'retrieve_hospitals_by_infra_type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2591c43-ef05-43c0-8653-14d69ff889c2' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'TEXT','label',null,'hospitalByType_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d2144d4b-9435-4e30-834d-3df126d3f0e0' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('83384e41-483c-4b32-8182-95df5533a440' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8b66a458-4b23-4605-95b3-d3f29b34143e' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'TEXT','tooltip',null,'Death Reason',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bff8a13d-8157-4698-b8bc-4f09b73532d8' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'TEXT','requiredMessage',null,'Please select death reason',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d5c57999-8177-4364-a39a-03d5f2632860' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'TEXT','placeholder',null,'Select Death Reason',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9cbe94ee-a3bc-402a-af2b-3c1b6db13a79' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e2fad9ba-481d-4f3f-82c4-f89b9b0f3778' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f8e8477a-59e5-4dee-b236-bf74c2b30bb3' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4e5d1631-330e-4f0c-aa31-e5cff5528663' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'TEXT','label',null,'deathReason_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b6deb6a5-81d7-455a-918b-939bab79a958' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d5ad9b3f-39fd-4183-8511-8d5fdb6f96b7' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('28bda013-d18d-4d30-a28a-74a8d3434636' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'TEXT','tooltip',null,'Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('17fc7fc0-8ea5-4788-9b2c-0b7a441d2d00' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1b854a89-1a30-4cca-a048-6232b583d7b7' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','tooltip',null,'Height (in cm)',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('22254cd8-5917-4710-a5fa-8e2ee695ea07' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'NUMBER','max',null,'250',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3866636e-448d-4294-b300-a1c7fca82e87' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5a5b95aa-7f3b-4e63-a65a-9d36c3a2ee88' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5a79dd90-ae53-4169-9bbc-b589729f4152' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','label',null,'memberHeight_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5cba22cb-9971-4040-9a3d-b00d0060f587' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96f69afc-a3b8-46c9-a56b-e7ea41d626ef' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','minDateField',null,'minServiceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bf3b1fd8-72aa-48ed-9250-43a5dbe80b0f' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','maxDateField',null,'formData.serviceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c073884d-b603-48eb-b5bf-23b84a89a5b8' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','tooltip',null,'Death Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d37c5e3c-8f62-4bd6-8af8-048a3765b0b3' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dedded08-daf8-4367-9f3f-de19033cd7e2' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ea70d53-f908-4677-8ce8-4ea6f5efb7f7' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4f5d3286-f1ce-46e0-a2d1-eb34f53f6e82' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','placeholder',null,'Death Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5e9d3c95-e665-44fb-a458-d36636bf96dc' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7f549baa-4561-4433-a288-f1fbd620951c' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"false","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('88503f36-2c19-46cd-9140-3bef4610dc08' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a8b06402-f418-4df6-b769-7c6efc291eec' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b7e71aab-14dd-4aeb-9a49-2849296a31da' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ddf6ab39-f8a0-4281-b951-cf767ff18683' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e17d7039-ae79-48b3-bf1a-7fd0f1a8df48' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('13f033b0-068a-4d6f-b34b-048629566f22' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.jsyBeneficiary","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1e622cef-4b79-4ee7-b949-e12b1c045990' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'TEXT','placeholder',null,'JSY Payment Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('94ca06e1-f3f9-48c7-b95d-e0f96f3b7b60' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('98b6f10b-7fe2-4af7-a8fa-38219a438b58' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'TEXT','placeholder',null,'JSY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b2cf8dc0-a07c-499b-8a10-ddcc46cd1ccd' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:2323","type":"Change","value":"jsyBeneficiaryChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b410f876-748a-46d4-b944-bb4b8d869a49' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd225a08-bb8e-48be-9c6a-cc12a3ed0352' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('deffbc99-2760-4337-8e09-e5828462da96' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f191a5ac-a4b5-4d95-ae51-d8c9dd2a21d7' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'TEXT','tooltip',null,'JSY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f5186a3e-3b4f-4240-89eb-5a8af2cd8143' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fad3eb7b-9249-48aa-9c5a-7249d4256449' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1e96cdde-a0ce-464d-b2d6-4310af0bd888' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('52021b0a-b4cf-4d28-a958-8f3bcd9d73b9' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'TEXT','placeholder',null,'Haemoglobin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5dda9ca0-c16a-4aea-9c52-171064f5b2c0' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'NUMBER','max',null,'18',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('688d17bd-c00b-4617-9dfb-24117551f1f8' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'TEXT','label',null,'haemoglobinCount_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3994d27e-c164-4511-8a9c-5418847ea2dc' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('80f7a38b-e55e-45d1-adfa-f2f93ebc970c' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96700efe-529b-4eb8-9c0e-ff8c083ec3bc' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4aa61baa-aab6-445c-84ac-d18c4be1f4be' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.ttBoosterGiven","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===true"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTTBoosterGiven===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('91134460-cba9-4b8c-a64d-e131c9466f51' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f989f258-f87d-4b82-9519-2e1a70313685' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('326b86bf-c7f4-446a-8336-96e6cdbb97a2' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4f3da218-897b-4d51-a6b7-6a00e5498b47' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('43960b57-22a8-4eb2-8987-82b6f938c44d' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('22a12609-4262-453b-a686-42a3deb115b2' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.urineTestDone","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('58727bdf-4376-42cd-a550-4d4d24beaab7' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('75961bfe-a845-4e58-9d93-c6c373f0cd34' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'TEXT','requiredMessage',null,'Please select Urine Albumin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3fcf3781-e05c-4dc2-8319-0c2f5904ccfc' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'JSON','staticOptions',null,'[{"value":"Not done","key":"NOT_DONE"},{"value":"Positive","key":"POSITIVE"},{"value":"Negative","key":"NEGATIVE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8f1e077c-1a30-4863-a083-837fe9bba07d' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('91b7012e-0b76-4ac6-b51f-05ea472020b0' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'TEXT','tooltip',null,'VDRL Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6ee2fa6-7d09-46ac-9928-33ead160a707' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'TEXT','requiredMessage',null,'Please select VDRL Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('82820438-9c51-4f08-a399-56aba0f9fd88' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('98a69734-29ef-4634-a01b-a82d10642ede' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9da5eb3c-a217-4a0f-80d8-de69008b0f51' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'TEXT','label',null,'kpsyBeneficiary_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('db1239e8-7e48-45e9-804d-ecbc5adf5a01' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0d2aa764-624b-4c73-88be-9ff5c6466d7a' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('86e2bac7-f72f-49bf-9e2f-009f5842b233' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('87da8121-74a3-461a-8ad9-82b68a6c5397' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b54a1d29-cbc3-42ee-9fd5-6c27cc41b058' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bcdf292e-40fd-4c40-ab24-568ed8d11875' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'TEXT','placeholder',null,'IAY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c07ff643-8974-451c-9d33-b414985ba1fc' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e598e83a-2c61-4fa1-b276-ed4153d8300c' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('14ad1b4d-bb9d-4001-b6e7-a65c778d3c59' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('22f505b2-80fd-41fa-8230-ca32e9de3543' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'TEXT','label',null,'iayBeneficiary_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('334b00c4-07a4-407a-a77d-3b8a22eb80b0' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('348333b3-fe56-4b93-b01d-b59885e7a1ea' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3954aa33-34ac-48a2-a7fa-e46d00faa1bf' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('46343249-0866-4f03-9da7-766fab2c0152' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd1a6e0e-7120-498e-98dd-66a515b21cc1' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'TEXT','label',null,'vdrlTest_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bec3ed33-7b7d-4eb7-8913-b612a3915079' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'TEXT','placeholder',null,'Select VDRL Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cb691624-b7fc-44b7-8d8d-62c60a7c2df7' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e2a33f8a-859a-445d-9a10-db9cd9c6e700' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e5e21655-b494-4d9f-bb3c-5170ca190d31' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('117822d7-2ac2-4e92-b9e3-c6408c48cd30' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('65fff79b-106e-45de-b62c-851350170108' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7603ce51-a50f-4279-9599-0fe2d474367e' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7825017b-0a5f-4577-9ee0-4afa052f81d0' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7e201355-9cd4-40c9-9516-cc0050b1f6ba' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'DISABILITY','disability',null,'null',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6aabbf1-e81d-48c6-97f5-358dc546a7e0' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e350a381-ca90-40ec-ac04-cb372a6bddea' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'TEXT','tooltip',null,'Urine Test Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('049b58bb-07e0-4b3c-8eb0-d23077a0c789' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('10bab9f7-837c-4f44-9f7a-6f34786526ae' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('377fd9e3-4bfe-4b98-af16-85ccf7062e82' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3e2083da-6dfe-47d6-8784-91ed31586766' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('831870d7-e5fc-464e-aa22-bcbe915cf8bb' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('88aef5ac-5812-4a9f-8b89-5383c5068bb4' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a9db9d4a-c11c-4802-9a6e-83cea866cc7f' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'TEXT','label',null,'hivTest_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd2724ac-6c19-4fab-8e69-7776a75ea79d' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e477a983-4fec-4c58-a979-dc5ea550d86a' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'TEXT','tooltip',null,'HIV Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9ba43335-9e52-4b56-9def-01bbe8aa40cc' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'TEXT','tooltip',null,'Sickle Cell Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a20f5857-c71b-4f18-a42d-cac00254bcb2' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d23cbf12-b1b5-44e8-81bf-fa934afa1cd3' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6facafe9-e9c4-4a1c-9906-6c2c632d6db3' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'TEXT','placeholder',null,'Select Sickle Cell Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7def8ebf-e280-4d0e-aea8-57b08d3d884d' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'TEXT','label',null,'sickleCellTest_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('694df364-a6cb-4740-b684-b172915f4584' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('777aec14-55c2-4cd6-8b1b-9b9f8186e096' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7e7e3bc3-d4bc-46cd-96b4-126045e7ca4e' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bfa1da12-5b7f-4bf2-b865-4a137eba9ffd' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cc13e733-4e86-4b35-9577-73424388ea62' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','requiredMessage',null,'Please enter Systolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4d989ac5-3eab-42f1-a149-6b18e1e93308' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','minDateField',null,'minServiceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('98d07ceb-26a9-427c-bd7f-d3d88ee14495' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ffd4beb7-18de-42bb-81e9-105794d877fd' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ad1e9e4c-cda3-4d42-b967-033582ed1873' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('949f5fd9-bd31-4621-b381-7e002fa75425' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'EVENTS','events',null,'[]',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('31363643-8f5d-4dfb-acd2-c10417561242' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:2670","type":"Change","value":"bloodSugarTestChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('49ab48cf-c732-4112-87c9-8949220e477e' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4bf54a2f-f692-4f1e-a368-d242d594a814' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e48c4754-b935-4fd4-843a-2071c061d908' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('64a87e00-673d-464e-b142-6fc0a255b64b' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d999b22e-73f8-40bd-bd5e-ab1b8ba8303c' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('acb52bdd-7554-4f23-9d88-7d974c17d4d5' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','maxDateField',null,'formData.serviceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b0c0306e-5f2e-4b90-90d5-6845f0dfe849' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','minDateField',null,'minServiceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a3543b24-881b-44a2-b08d-439c747b7d6e' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('74ad0c88-2129-481d-95f9-1a66f1d80c01' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9070ec88-cc5e-4b99-8196-5ef8f4e93468' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a26a18f8-cf34-40cc-99d2-582fb6d67ba6' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','tooltip',null,'TT1 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a38bdb9e-9462-45b6-acc8-8e10f6143870' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','label',null,'tt1date_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ba4636fc-9f1e-4d50-b3ed-14cf9f13f291' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','placeholder',null,'TT1 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c401e573-ebc2-44c1-a1b4-a8c78567b363' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1deb2936-0b4d-4844-bbb0-26e332e2f3c2' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('42e78368-ae04-4607-bd12-e9ed49d5d17b' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6ad8aea2-d661-48da-942b-7d3faaad40c9' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4aea7442-0316-4076-a3e2-1399d20637e3' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5020520e-6e30-4b4e-a517-0a27fb550b81' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'TEXT','placeholder',null,'Urine Test Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6a91204c-588e-4fbc-80f8-386a45219463' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('78e62985-a74d-4283-88f1-618535517b11' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8533184c-f885-49ab-b4d1-079c8d145a1d' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('946dc737-8034-4619-9206-d74c0de05758' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','placeholder',null,'Diastolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d0dc4aad-ecc6-4adb-bfbc-634c5ac869b9' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showCalcium===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('da472d9b-9c35-4ddc-b9b5-d39f688b27e2' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('00539978-21b1-48a5-b14e-ea2f17376d7b' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'NUMBER','max',null,'360',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('03fbe13b-4329-413b-81a3-14bffc90ada8' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'NUMBER','min',null,'0',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ac7bc53f-c24a-4828-a164-aa14fd5230f6' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'TEXT','tooltip',null,'TT Booster Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bb27899e-ca6a-4c8d-8d95-85ed071fba35' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'TEXT','placeholder',null,'TT Booster Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e5a8f2fd-2b6b-4681-9167-5c0a682d002b' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'TEXT','label',null,'ttBoosterGiven_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('79347544-8896-445e-a399-6d04ecd563b2' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ae6e77c-5952-46fb-b614-48b2d417549f' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4c06a69b-7da9-42c3-9b7e-566c0ed77a99' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'TEXT','requiredMessage',null,'Please select TT1 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2c334d1e-19a4-4fda-91a9-3305b20bb92e' as uuid),cast('946a19ae-e0d1-4ccd-86ac-b0c9cacc0f23' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('410c364a-a828-40b1-9f7f-0056ec3e03cd' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'TEXT','requiredMessage',null,'Please select Expected Delivery Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6b1cfb2b-55ec-4c4a-be7a-4c3e2b347f31' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7f8aa834-273e-4031-af7c-e82420129a49' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ca4c7f8-37d4-4c5c-a877-a3d4cde4c807' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'TEXT','placeholder',null,'Weight',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8987986f-0b0f-478e-9f9f-09a2ceb3031f' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1485e96f-38af-4950-b08a-644f9ed30ba7' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('829545cb-5352-4a90-af39-760ae3048d32' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b204bc12-dc96-45fc-9090-cfb0a542dd2a' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b4e84d82-55bd-42a5-bf14-42a0c3294b58' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:5292","type":"Change","value":"ttBoosterGivenChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d8736df2-5884-4832-89d3-f548b5fde8a3' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e058cd1e-10c8-4858-8d30-c6b7c4c536ee' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23dd346b-b40c-4ba1-ad72-0f950b8ac9b9' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2bedc958-abfd-4b24-baf6-f92293723bb6' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('812edd00-f5eb-4738-b1d0-c0ffc20d3d3d' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9853604e-9f73-4f3c-99c4-70b69ceea6ff' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b32ec490-54f6-4945-ba1c-49089beb9cab' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b5299675-bac3-4ca6-a0d6-026044f69f1d' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2dc27a6-7c27-4792-88b2-601cb9346765' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c7dc6e6b-f5f0-4a64-a709-fdef08308a90' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d3448103-b75d-4bdb-ada3-9fed7b0b9ece' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'TEXT','tooltip',null,'Blood Transfusion Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e643d7e7-3a22-4b3a-95f5-508e214a9171' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('19cb9a1c-2e14-4f55-a10d-047e3dc43c9e' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1ca55df1-03d9-44d2-8c45-2794c4cc12a0' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('33d32d44-da20-40bc-9bb2-af00d6a20828' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'TEXT','placeholder',null,'Blood Transfusion Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('39d7ab26-f92e-4782-9378-5d31fa052373' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5e038287-2e29-4334-9fd1-42e53e46ad91' as uuid),cast('8c9b36a3-33bd-42dd-a10f-fb0309ba87fc' as uuid),'TEXT','label',null,'bloodTransfusion_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('31cd5812-c8c8-42c9-9568-f54ac536636a' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6709d3c7-df09-482f-8f3b-0f1db09df304' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'TEXT','tooltip',null,'Inj. Corticosteroid Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8172c3c2-9b3a-48c6-b916-c39d666faf2f' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','requiredMessage',null,'Please select Due date of Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('87c03160-385e-4ff1-9507-fefbbaecd3de' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a2ef4eac-69c3-41ad-bc7e-0618addb4897' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','label',null,'ironDefAnemiaInjDueDate_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aef977d1-cfb2-4518-bfc3-c2d6dddc444e' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','maxDateField',null,'maxInjDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b7bc366d-a258-4c6d-a016-b8d9ecb2fa95' as uuid),cast('b5baf4e2-9575-4516-8c12-f1673ceaaa02' as uuid),'TEXT','placeholder',null,'Due date of Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('81fb918f-2204-450f-9aa5-754f240a9dfc' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('87017683-3456-41f3-a9cb-d4dda3b3998d' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'TEXT','placeholder',null,'Referral Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8cdc326b-561d-47d0-830f-e99f95f3cf4e' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8fce82cd-8a92-4445-b098-1d40dc5e7581' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b903b958-175a-4e89-a103-1a4bd7b98168' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d6ce6c3f-182c-4f30-bafa-f10e96ae388a' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f3eff9da-260a-4a7e-a69c-2c56d9b7763b' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'TEXT','label',null,'referralDone_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ffc0cfe1-c31c-4f77-8568-8eb683280566' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1afe67bc-f147-4076-9071-2b1f18773c68' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:7259","type":"Change","value":"referralDoneChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3caa2539-88f0-42c7-a757-cb9893ee6909' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'TEXT','tooltip',null,'Referral Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('459ed1a4-585c-47f2-aa8d-0e66ffe5c1ee' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6a4044e6-af65-48ec-97ad-b92c96540d73' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('74a5fc17-eba3-453b-afa4-f8702a841255' as uuid),cast('bf6c472e-0b32-4d3a-8775-c8ce46bbae36' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a344f26b-9469-4e3e-b49a-0c8e206cede2' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aaf2b552-5843-4427-9213-d2a3a7a1b443' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b4bc2429-7e4b-4f76-877a-80cbe0e838df' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'NUMBER','min',null,'30',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b58096d5-a12f-4475-80cc-8336bda1faf7' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c3fff733-56fa-4a5a-a4fb-e15cf5da370b' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cc94a191-1e17-4363-931b-a74224213d4b' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f506581d-1345-44b5-8e47-f00a038e7a1b' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'TEXT','requiredMessage',null,'Please enter weight',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('00a2d17b-237f-437b-9ffb-b4501fcf727e' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0bd5420f-d365-4a2c-b70f-9a8d6d275b84' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'TEXT','label',null,'weight_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('37f295df-79d6-46ba-a080-39627daf3b04' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'NUMBER','max',null,'150',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('47c3331c-4d1d-4275-bc15-75f4eda91c79' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('499f6827-f934-4a4f-ba7e-40dd01d1faa6' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'TEXT','tooltip',null,'Weight',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a99e3ef6-acf4-4f96-93c4-35b71292b71e' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bce8847d-d73a-438d-80b6-534d2d143c7a' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cc7d46b5-c096-4722-8326-7366509fab7d' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('29a70be0-d5cc-470a-b57a-48ef5d4829df' as uuid),cast('8401f80e-9783-4a2d-8899-25b2983c0cd9' as uuid),'BOOLEAN','isDecimalAllowed',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8db820f0-d8cc-4959-be25-7d2f6f21491b' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5b5ec55b-6c12-40b3-9152-64318c159795' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9a2ab0b3-8bf5-40f5-8e39-e108b1d5a5ce' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6730e34-bb24-470a-b227-fbb938bedd38' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'TEXT','placeholder',null,'Foetal Movement',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b4dd4a97-edb7-4f0f-8ba7-23a5da4a354e' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'TEXT','label',null,'foetalMovement_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cf00b4ac-aabc-4aab-ac66-6678ac05df76' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e1a74ccb-6dfe-4e02-842c-70692baa75d9' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e777444f-8189-4198-9da7-16fc7e90e28d' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'TEXT','tooltip',null,'Foetal Movement',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f9331aa2-ae32-42cf-843f-927734ce9877' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('472c4554-c2b1-49d7-8e66-54e0e6db747d' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('61949780-5654-439a-94c7-b1db27c5f84b' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.lmpDateIsOutside140===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('743ffc8e-3274-4c8d-ac7c-03fb58ac6f4a' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('61fb58fc-9664-4815-8871-f2a2e0f6e334' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('708379c4-59ff-41aa-94ce-1086a43af917' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7d7ba921-870c-422d-b5af-dfc4c5870698' as uuid),cast('4646c1d1-569f-488e-ba42-b6d8ab8ff45c' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c9e79eaa-c864-48c1-9a78-f654efc702de' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aeb7e127-5c98-488b-9010-aa1737c6c07a' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5378de04-feca-491c-934f-5645c1908a2f' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9a1f2352-7115-4e36-93bc-dbd1c6aac77d' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'TEXT','tooltip',null,'Institution Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b99d1d8f-5d0b-45b2-9fcf-5451bcbe2b00' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8493c9b3-854a-44aa-b479-c7d1ec187b1c' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','placeholder',null,'Height (in cm)',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c6e7a0cb-06b3-4080-a096-1f8d00178947' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d5fbc5cf-1544-4c74-85de-9fabb84c2ab7' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f541934e-ddae-4dcc-9d67-a504ba3fac7c' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','requiredMessage',null,'Please enter height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('03791db8-576d-4abd-a673-52f7b8d3f399' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'NUMBER','min',null,'60',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0fbcd8e8-bff6-48b8-bd65-2eac53455dc8' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('442ee30f-dcc0-424f-afe9-45f3644e72cb' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('59de2d39-0f7c-41f1-afcf-e52c7ce3955c' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'NUMBER','max',null,'40',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7298dd9c-a300-4f0f-9ba2-de18bf2175ec' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','patternMessage',null,'Please enter valid height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f4c9fdb1-3841-4cba-991d-f2e8e766104f' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'TEXT','requiredMessage',null,'Please select death date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1d6df7a8-619b-4bc3-8ad3-21e787a44117' as uuid),cast('933e3d7f-658d-4c23-b10c-4ed3866816aa' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('82885609-eb97-415c-8f09-9740230ce222' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aeb9e4d9-51a0-4342-8cc3-76d9083475bf' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b57a8894-3a75-40e8-b986-7371e6ac51b1' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bde49892-8cfc-4903-8865-531efaab8527' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'TEXT','placeholder',null,'Foetal Heart Sound',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dfee0de9-33c5-4d75-9ecb-113f870ba42c' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aba45155-1357-439a-a253-f5ec571c0db3' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d5f8e929-fc3f-4ae0-afb3-687454f07d8f' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'TEXT','tooltip',null,'Blood Sugar Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e02b3a21-d191-4f3e-9963-25b71ec05820' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2e20228d-10e1-4827-851f-9acdc16c9486' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('383970c9-bafc-4edf-8ff3-d7c7ea9ba7d3' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'TEXT','tooltip',null,'Foetal Heart Sound',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3bb4f8e8-ddb3-4d3a-8555-d4130dea267f' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('41963dc4-2f20-44cd-a51b-d2138e5c9b65' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'TEXT','label',null,'foetalHeartSound_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f2430fb9-1aa0-4277-b66b-0aba14dff0d8' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'TEXT','requiredMessage',null,'Please select Blood Sugar Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('58bfe34d-add7-4349-8e61-cda03afb35a9' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6f032bf7-ec72-47d1-885b-661bcf73ee7e' as uuid),cast('87a2584b-0d2d-4413-8697-2513922e1783' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8beaab90-a9d1-472a-9d29-19f76d929419' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7b0889fb-222e-476c-8aec-0f38b9730cac' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b116c187-a03a-454d-a5f4-972ef8d019d9' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b7f58c30-3d36-474e-9806-fc4cb96bde47' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b95fa418-b9f4-4b7f-8446-c46fb179c57f' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e0b339b7-d940-40b0-8042-0bef4c46f9f4' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'TEXT','requiredMessage',null,'Please enter haemoglobin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f5baf46a-9a54-4106-a876-2f2ae180f69a' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0f87494c-a878-49d9-aed0-d01b286564f3' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('76f9af95-5fa7-4f14-9c05-5cef11013d3d' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('78848d3d-e954-4a99-b62f-e3c3f17f0b1f' as uuid),cast('364fc1d1-72f5-4206-840d-64842248f3d6' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('874b27fe-0576-4e96-9b59-ac8bdc4c5aed' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3187d90b-2b1d-476a-a442-058e9c099fe7' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'TEXT','patternMessage',null,'Please enter valid height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5d7156b3-bcd3-4563-8bcc-9daaf3ca7a15' as uuid),cast('e1be18b4-22d3-495f-a3b5-7eaa063b0998' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e3802f44-c7f4-4013-a40c-dc63768a4a5f' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e9c59c8e-9d5b-4434-a43d-3bf16adc64ba' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f02ad1a9-c897-44fe-abae-c44156079c4c' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f04907e5-6efb-4416-ae87-bbb6e116b17c' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','label',null,'systolicBp_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b37ba593-1964-44c3-8dee-5b00b3955ff6' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'NUMBER','min',null,'20',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c510578d-6f62-4433-b8e9-69f2efb844ad' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0f30f35a-ca56-47ed-8d68-3fc747fcbca7' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('838a6110-102c-4655-be7c-5a6d0eb4362b' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c1e66942-d877-420f-9392-a545e786fad8' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','placeholder',null,'TT Booster Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c5455130-0138-444e-81cf-a11fa0efc118' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','tooltip',null,'TT Booster Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d47532bf-5522-47f0-a895-3f35199b0564' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c0a11b7b-8620-4d7a-a73e-f48bb3ffcd72' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'BOOLEAN','isDecimalAllowed',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('04ecbc5e-a456-421b-8a6a-dd519c4d7c91' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('845d8efa-cc81-4c54-9979-e8021fb638b6' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'TEXT','requiredMessage',null,'Please select Foetal Position',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8b539fc2-4abb-41d7-9365-044bcfda9f7c' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'JSON','staticOptions',null,'[{"value":"Vertex","key":"VERTEX"},{"value":"Breech","key":"BREECH"},{"value":"Transverse","key":"TRANSVERSE"},{"value":"Oblique","key":"OBLIQUE"},{"value":"Cannot be made out","key":"CBMO"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('afec9aaa-7338-4a91-a57f-ee7e3382300f' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'TEXT','placeholder',null,'Select Foetal Position',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('86648509-278b-4d46-b406-8b34e17ee3f4' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','requiredMessage',null,'Please enter Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a3b7f1d3-53de-41f5-b3c3-b79618d3360c' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','placeholder',null,'Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2169099f-8436-4f57-b502-2ec532e45c47' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a3e4f0a0-68a9-4d4a-8148-583a8a287695' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','label',null,'sugarTestAfterFoodValue_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a9133988-3d95-4fa6-abe1-69a8f56ac6ce' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ccb2c52f-08d1-4ec0-800e-aaa62e6b02dd' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'NUMBER','max',null,'600',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e3495e7a-b98d-4986-927d-0f270cfc7b40' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f5ea5677-ae6b-4123-880b-808912d41131' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('120f56b8-0de8-4e69-8fc0-41041c75d4dc' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2366bf4d-7286-4678-b6cd-6e5a2394e925' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'TEXT','tooltip',null,'Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a1241c9-66ff-46ff-9c2b-2cd2e1b483e1' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"''NON_EMPTY''","value2":null,"fieldName":"formData.bloodSugarTest","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4728f285-04f0-4549-be14-346f114a8045' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5940c8b7-3c87-4850-8bf7-e4201311087e' as uuid),cast('67c8213b-cdd3-494c-8ba7-d8d2b2894089' as uuid),'NUMBER','min',null,'10',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f6cbfa77-391f-4fca-884c-f45bec95e981' as uuid),cast('9d5d1777-3b65-42e1-b903-bdd8081b1ebd' as uuid),'TEXT','label',null,'urineTestDone_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('853fcb9b-3ca8-4097-839d-7c399c2c1990' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('035a52fd-6afd-40b1-a9eb-25bf68d4027d' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8b2aabdc-832b-43e5-bb23-a291ca486774' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('abbcdebb-09ee-4156-bada-29fac1673752' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b578a335-05b9-4471-a688-40fdd7b62344' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','requiredMessage',null,'Please enter Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d06113a0-5177-4628-b0bd-5c930ca3ed7e' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','placeholder',null,'Blood Sugar Test Value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0ba77cd0-b8df-4a57-bc18-f3ff50be05c8' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('283b1e19-8998-4fe8-9eb5-609983f1cc78' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ab9ad079-d221-40c4-8e8c-65cd7960b745' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b4fa0f6f-bad9-4584-9ad8-1d2606456517' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b8ce59e5-248e-425b-84d1-72cec7028b7f' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dfc73cea-4e9d-41a7-80a4-e506e021708c' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e23fb5cd-ce92-4c19-a66b-1d6d7b1b52b0' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ea6514e6-eaac-4633-ac0b-d2ecd004c93f' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'TEXT','requiredMessage',null,'Please select Last Delivery Outcome',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ecfba535-d74c-48a6-8c41-6a2427e25fc7' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'JSON','staticOptions',null,'[{"value":"Live Birth","key":"LBIRTH"},{"value":"Still Birth","key":"SBIRTH"},{"value":"MTP","key":"MTP"},{"value":"Abortion","key":"ABORTION"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f2c6bb12-b61a-4af4-9284-7ce13672b70a' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'TEXT','placeholder',null,'Last Delivery Outcome',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6e3721e-1f02-40c6-aef2-19461f2b9f23' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'TEXT','tooltip',null,'Urine Albumin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a8d8997b-d6ff-499a-b20c-48d48c2c781f' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ade992ad-e570-459e-bf29-5a020431e197' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b72dd941-74f2-42ae-bf51-d670041e57aa' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd710da9-2bb5-4cb9-9587-1e1c1d0582a0' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'JSON','staticOptions',null,'[{"value":"0","key":"0"},{"value":"+","key":"+"},{"value":"++","key":"++"},{"value":"+++","key":"+++"},{"value":"++++","key":"++++"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f49a924b-943a-4417-8081-a774779ab248' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'TEXT','label',null,'urineAlbumin_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('02cdb0b2-94d2-4c17-88a1-69cda380904e' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'TEXT','placeholder',null,'Urine Albumin',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('07f02ab2-e9c9-4ae3-9abd-aba56c78df7b' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('090cab4b-e53c-4881-b0db-7bb48723a840' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0b52fe4e-3d8a-4f69-81d1-cb4fe5941bbe' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2e41d50e-33e4-4267-8089-21df7e87176f' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4e03a21c-3a92-4dec-ae97-402ca83607fb' as uuid),cast('e2ad861d-f1a8-4e34-b0ba-07e2b1152503' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('920f9645-0974-4aad-b179-cb3bf82f7820' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'TEXT','tooltip',null,'Urine Sugar',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('92281018-0fbe-4d24-bf4f-eeec7c50ce59' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'TEXT','requiredMessage',null,'Please select Urine Sugar',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a1570a27-39fe-4aa1-8101-3da83e9e53d4' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b05e0e04-7fba-47be-b8b8-b352c210db33' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b73000a8-378e-4340-9173-7bf3df259f1c' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd129b0d-8744-4fc5-a34a-bbbf7a7839b1' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.urineTestDone","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c46568ae-da43-463c-8cf6-1ead4185e5d6' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'TEXT','placeholder',null,'Urine Sugar',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c5635a22-afb2-4f93-9b63-8aa0ed04aa64' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c7000d05-dae3-4973-acb4-686bbd1a7dcb' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'TEXT','label',null,'urineSugar_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e11c919e-b45c-4bc2-8cee-31515003b906' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e480aec0-2594-4ccc-9dc4-282fafa8314c' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('13b2a4c0-094b-4f2b-9f40-69a89e529064' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2da4f619-a38d-468c-b464-87a312305e96' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6339c549-e84e-494f-8595-a2a0deea046a' as uuid),cast('b47531cc-e1a1-4c84-8a37-75b1fe7aa001' as uuid),'JSON','staticOptions',null,'[{"value":"0","key":"0"},{"value":"+","key":"+"},{"value":"++","key":"++"},{"value":"+++","key":"+++"},{"value":"++++","key":"++++"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('022f124f-b675-4828-b6b8-d5dd017f792a' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'TEXT','tooltip',null,'Last Delivery Outcome',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('356a1e6a-e84a-46fb-aff9-6f7ac6d101be' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3af85999-c010-4127-9b5c-902d90b00292' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'TEXT','label',null,'lastDeliveryOutcome_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('871bb0ed-cde7-4fc5-87c2-3d12d4246cbe' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a135ff5b-7b30-4f30-bd7d-9db885f08fb2' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ae49dca6-2d40-4a46-9b52-3e03ea6a112d' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showOtherDangerSign===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bf6a007b-4158-429e-80b8-763b849c1eef' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0b2a11b5-649a-49b3-b8d2-2a5e989819c6' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'TEXT','tooltip',null,'Examined under PMSMA',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5d98f56a-bee0-4b84-a88d-481b51f9ea94' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'TEXT','label',null,'examinedByGynecologist_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('02fa87f5-bf64-4db4-b5d7-d2b9172ec5fa' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','placeholder',null,'Systolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('21f0540b-1ca7-417a-a598-ce41b04fa922' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('24ebd7eb-baed-4183-8696-d3732385b999' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'TEXT','tooltip',null,'Systolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('39a1a51a-811f-4c6c-85fb-59e171ac5d11' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('69d3ca73-5f6d-4a50-822f-a02bc521cde0' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'NUMBER','min',null,'60',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6cd6c4a1-9b39-4ac4-bb33-07bc4731120e' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'NUMBER','max',null,'300',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6ee31938-1b7f-49fb-939a-c1f870154021' as uuid),cast('a59bbed8-fa8a-490b-ba2b-457ae4937833' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('53eca475-6189-4f1a-a97a-548f95821941' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('68f80bc0-5c4c-4f79-b254-d9a6ce0e49f5' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'NUMBER','min',null,'10',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('791f2edf-acc0-449b-b9cf-faf897d78e6f' as uuid),cast('fe655bc9-df67-4cdf-bfb3-cb33ca95d3b1' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e1e26164-25e3-4fdc-b3b2-258334ea0145' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bb52178f-1c72-4104-92e1-83eafd10c405' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'NUMBER','minLength',null,'1',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cf6124eb-8006-47ae-ad04-cea66f5ba6e4' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('99cd680b-bdf3-4a52-bd9d-fad47797de7c' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bb783d3c-2bcf-4041-bce2-45e36a6d2f5a' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'TEXT','label',null,'isInjCorticosteroidGiven_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('df0a78d8-5629-4d35-b12f-5c0faa2ce41a' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f24795ba-b319-4b61-9086-9e3e21552153' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f41703b1-6ea3-4894-8aa3-8b5694dcc2a5' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('873300bd-da8e-45cf-8535-28bf561db169' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ac516cb1-d62e-4cac-aae4-a6af23fbdf44' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b0b91680-a14d-4875-8728-6582b6502bc0' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cea97dac-5413-40e9-9e4a-56f4d1a9acc0' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e0454572-73d9-466f-abe5-7d8139ce6bb0' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'TEXT','placeholder',null,'Examined under PMSMA',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('031343b8-aa23-4f87-998c-bf0f6c3ed7d3' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('09dbeb50-e28d-4043-add1-2a493e3a7583' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('48b71453-8695-4594-902d-c199376eab0e' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4aeeb2e6-f608-4eaa-95c1-b41ccae66317' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f88a5a2f-b3d1-4107-928f-f51e5e5b8015' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0a85560a-5a0c-4312-84ab-f6ee0d60745e' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'TEXT','placeholder',null,'Inj. Corticosteroid Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('35f995ce-9c66-4658-82cb-d17d1e1f88a4' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7ff6d350-eda7-4a29-b7a9-2ae1668163e6' as uuid),cast('47229897-c1df-4c54-91c0-e9f2c9725218' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3744a4d8-6305-40d8-ac8d-7aeb7a5770a3' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('60f99561-327b-487f-aac0-b76c15b00a1c' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showLastDeliveryOutcome===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6127b9b2-da0e-433c-831e-387dd0794aab' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('73882033-7953-44be-b2d7-b95beb890f1f' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ac34c0d9-05bd-41b9-9278-63039b63f998' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b0d357bc-e484-43ab-8db7-684fcfc483b2' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d338392a-decb-417b-9880-f1b4712f3dd0' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'NUMBER','max',null,'150',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('346e46c5-91ad-428b-b19c-78e9ee8441eb' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('393fdcf4-0ed3-4678-ae16-d31219f34f05' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','requiredMessage',null,'Please enter Diastolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('40200048-1b6b-4f64-b242-53af1a56b373' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('429ccd18-6c9d-4a08-a591-1d2ce0c42b2b' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('63b249a5-1a80-4fa2-9497-ec9c0f2e6db1' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('573584ca-69ab-40e0-a169-a0cc6133d737' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','maxDateField',null,'formData.serviceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6e2440ac-56e1-4fb9-9a4f-5e06ebddb3dc' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8e9fd8d8-f400-4959-8c55-3d438f7ba40c' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9f0944e7-ba31-421a-9eee-9cc722875b4b' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'TEXT','requiredMessage',null,'Please enter account number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a12906e6-cd7c-4db1-9a69-e9e59e7d12b6' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a1709dae-881c-46b1-a487-e9111b212cc9' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cf19059c-dad1-47bb-a386-ea9757b71c83' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'NUMBER','maxLength',null,'18',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d433f450-7853-4810-93b0-be93161d592c' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','tooltip',null,'Diastolic Blood Pressure',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5df309ed-45df-45e9-9301-d330e67be5ae' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('da8cb348-51a2-420d-a74b-4281a0d1c74a' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'TEXT','tooltip',null,'Account Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fe56819d-1ae2-4d7f-91a1-4f61c8d04ac7' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('06fcae62-02f7-4e85-b12d-b300f41c064c' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'TEXT','label',null,'diastolicBp_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0b9ee9a7-3fd0-4039-bf57-0474e3b60881' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'NUMBER','min',null,'40',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('179b322f-96c4-40ae-8ee0-bdd4d284240c' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23780a44-5307-4e27-8b0b-2265f658f1e3' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2989810a-903a-4afe-8de2-b3d17f7d5aca' as uuid),cast('0ba58d28-2174-4cea-aad4-3670bcdda387' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b7e3319d-bffa-4a2c-b640-3c5f3974571b' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','label',null,'foetalHeight_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c56e6c6d-7e2c-4d9c-84ab-ced7c1006ac0' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d45d2796-24ea-4f48-8749-814451a74e79' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','requiredMessage',null,'Please enter Fundal Height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d90efd53-0220-44db-8de2-4d2d78d589ba' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f9e95edd-a644-4b37-a170-8edff46bf718' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','placeholder',null,'Fundal Height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0e069401-e82f-43f7-9260-4be0c3d3905b' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('326e0ed9-3cc7-4e31-8692-1041c0eab8ff' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'TEXT','tooltip',null,'Fundal Height',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4af38116-60e4-4a45-85e7-7790d1036303' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('539ef4cb-60b0-4aa9-8933-761410443dd7' as uuid),cast('230bd293-2e35-4fcb-9df6-e9e3e612ed43' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f117bd56-1c9c-4555-9ed9-59e0be636c58' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('10116485-c0a2-48c3-8c9f-e4b3377eac63' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('15a38c33-15b0-4200-9860-fc550126bc53' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'TEXT','label',null,'accountNumber_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1b806ea0-6a9b-4913-92eb-83b2760becdc' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'NUMBER','minLength',null,'9',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4cce7de1-d2e9-4899-b4cf-7b9362c4dd7b' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:7247","type":"Change","value":"retrieveReferralHospitalsByInstitutionType"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('852864b0-0986-47d8-802e-aecb8466a4d5' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','requiredMessage',null,'Please enter mobile number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('942ab356-d844-44f2-9a9d-4ec86966aef2' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9d9139f1-8bd3-4e97-b658-e8f7d33ebf11' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','patternMessage',null,'Please enter valid mobile number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b256e6be-3138-43f5-93d1-59fb4a33099a' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','placeholder',null,'Mobile Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('80c686d7-f4b5-4759-82b5-eb481b8d7af4' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'TEXT','label',null,'referralHospitalByType_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8ef546b9-4784-4466-ae6c-077971772572' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'TEXT','tooltip',null,'Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9e5b89db-cfb8-4cab-8e1a-2f8bc17797e5' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aee39787-6e68-466d-a356-deac3ba6e796' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'TEXT','queryBuilder',null,'retrieve_hospitals_by_infra_type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b8b3b603-bb51-4599-a6e8-e7a8b3367a9e' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ef4079d6-f7c3-428d-b669-5f43b43a4f3b' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'TEXT','requiredMessage',null,'Please enter IFSC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f376b2d7-0763-4a6d-9106-d086bca9f5b4' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fbece81f-f8e5-4527-9371-0a95ac3a684b' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isIfscAvailable===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a689006-6763-4a7b-b1a9-802e4aebf702' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2b933fb7-6f44-45c1-baf3-bb94896410fd' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'NUMBER','maxLength',null,'11',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('31a60fa2-1319-4764-a098-b842d4793bbc' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'TEXT','label',null,'ifsc_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4766d6e2-fb89-49f5-8b05-7667853959a0' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('50098116-14b3-49aa-b421-0ca4f04c0927' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'BOOLEAN','hasPattern',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('55a8ba5d-7f78-492d-b458-3c716a41f407' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'TEXT','placeholder',null,'IFSC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ae857c04-d325-4182-ace0-4138b20c5950' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3f872a4a-be23-45b9-beb8-6bcbdd96f30f' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4920004a-8ef6-48fe-8267-e9790b923399' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ad79a80-6aff-4964-86a9-9951f79f697d' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isAccountNumberAvailable===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7199e9d8-fe41-47ec-b055-9d8b79f8eec1' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'TEXT','displayValue',null,'Account Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6f63297-15f3-45a9-bf32-b380071e691f' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isIfscAvailable===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b82806a5-8c32-4dd2-9194-89abb324a502' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'TEXT','displayValue',null,'IFSC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b61d923b-5d91-410d-8c43-396839b749c1' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'NUMBER','minLength',null,'10',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c819a792-42ba-429a-a1f8-d974b71dfeff' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d2db2564-0c9f-471b-bbaf-cf4b92498cba' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ad85d115-497e-4373-9fb8-49ba2c6ada24' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b3f40ca0-3a0d-4350-8166-11ebcfc61b24' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b6128f7b-8f46-4184-a6d7-71947369fdf6' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'JSON','tableConfig',null,'[     {"label":"Vaccination","key":"immunisation_given","type":"text"},     {"label":"Date","key":"given_on","type":"date"} ]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d18cf7e6-67db-4ccf-bc54-a65a8f3641dc' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bab7712b-28bc-469c-b663-37c587e0f3e3' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('074198d4-b58a-4eff-9dad-5ef78e9c9651' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"previousVaccinations.length"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96cff531-a8f3-4532-83d2-d8b205171312' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','placeholder',null,'No. of IFA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96e96a8b-54cb-4c0a-8083-3d57ac0f43b1' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a6740f13-b31e-493d-827e-262cb5b859e6' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7aa545b6-1594-46c1-8e82-79b176f234e7' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a73f0183-691a-463c-8fbb-f951a4f476a5' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'TEXT','label',null,'pregnencyComplication_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b58a14f3-b3f0-4abc-9581-fc4eff1fa3b3' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b940707c-f5f5-4411-9866-ede05c2dfa48' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','requiredMessage',null,'Please enter No. of IFA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bbd055f4-21e7-4023-80d7-67768dc597bc' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"''SICKLE_CELL_DISEASE''","value2":null,"fieldName":"formData.sickleCellTest","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showIfa===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9084ec78-d872-48f3-a576-ee530e4bdb4e' as uuid),cast('163a6f66-c6f3-4e28-825e-6b3d90277221' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aa1fd34f-e557-433a-a38f-4fe302715a89' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'TEXT','placeholder',null,'Presence of complication during any previous pregnancy',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8371ae3b-029c-4690-bbce-82772022cc47' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'TEXT','label',null,'bloodGroup_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('89928f41-0aae-4601-9fe5-1d0dfee09ca8' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9aadb87e-e37e-4726-ad54-e59aea376b2b' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c0943037-71b7-4e80-8a2d-95cf029a582a' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'TEXT','requiredMessage',null,'Please select blood group',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('72c9f16a-d0e8-4d54-855d-e9f31b67238e' as uuid),cast('b5df9efc-9dd8-4af5-8840-f75525e8c36d' as uuid),'TEXT','tableObject',null,'previousVaccinations',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bf3d7251-6879-42c3-8b79-74f0a062305a' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'NUMBER','min',null,'0',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c3a583c8-82ac-4a72-ab1a-893855003c20' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e19583b5-e310-420a-befa-724b1883b6bb' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('02d67432-44d5-4c33-8e08-c59da8e4e88d' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('16abd159-ba7b-48d1-9abe-0168d4d985d4' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','tooltip',null,'No. of IFA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('33e86f8d-85c2-4a9e-bee9-f02f3a4259cb' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3611917d-5b30-48e9-a157-bcac031a8b96' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','label',null,'ifaTabletsGiven_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('57e38490-073f-4430-99c3-5bb293e5389d' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('60503e9f-8441-4291-a594-732fcf0ddc40' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'NUMBER','max',null,'60',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6719dc96-9e0b-4ecc-b33e-f5f0c0c4035a' as uuid),cast('3eee174f-e032-4df8-b886-3287b697fa26' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d2106bff-e154-4d81-a56d-11d2dee99528' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','patternMessage',null,'Please enter valid number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a2d6f6bc-5f1e-441c-8605-eb780d6fc022' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a9b63fe4-ebc3-4d24-aa2c-ad17ddd73b14' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','minDateField',null,'minTT2Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fa7af627-f34d-449b-ad4f-08fa26ad057f' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','placeholder',null,'TT2 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('076cfe89-7577-4842-8649-40caafdfe1d5' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','requiredMessage',null,'Please select TT2 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0a35d913-7533-4eae-9b53-92427ed9a712' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6a606821-ccc8-4a5c-9b46-fdc247bb29ac' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','tooltip',null,'TT2 Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8dc28ac1-362d-4f90-b38d-67161fd27564' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a4188148-50d9-469d-a9cc-878eed4d7556' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('abcaa13c-e7df-4e1a-a5bd-4a0eec41c4c6' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b886680c-e783-4bd9-bdc0-081e8a26feb6' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'NUMBER','min',null,'0',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ba636382-a4ab-4d81-8ee3-c2c58145a3b0' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d6231fd9-eced-4d8e-aa05-cc23fb52a220' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','pattern',null,'^[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d690f6c7-45fc-4586-9ed3-a6601d3c53fc' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'NUMBER','max',null,'360',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fe437440-cf7e-45c4-bce8-c62ebeea9658' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===false"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT1Given===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('29aade4d-6934-46e7-9493-80c18c0b2d5c' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'TEXT','label',null,'tt1Given_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3b177003-9a18-457d-af9c-912e383d299e' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'TEXT','tooltip',null,'TT1 Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4c8e42a9-2972-40f4-9cd6-db053de618d5' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'JSON','staticOptions',null,'[{"key":true,"value":"Yes"},{"key":false,"value":"No"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4f9c2426-7263-422b-b27d-bc27c82a60d1' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5478df29-60d4-4906-a11a-489651eca6fb' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'TEXT','placeholder',null,'TT1 Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5bbc921b-5f14-4b54-90f7-58c627e403ea' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('672fb68c-a489-482d-8d06-97022b627786' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7689662e-2794-4a56-942a-9a8df52535c5' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('78344758-bf9c-49bb-82e4-7db339ccaed4' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b0c5adf0-546d-42cf-87e4-7406751371e4' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b6a7d2e2-5d51-4378-9f9e-05dea9a0f803' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bd1bd2e2-4490-4421-b81f-1be6844997b0' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'TEXT','tooltip',null,'TT2 Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c029e736-ec5d-46df-af77-93e4f916ddae' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e0ee4646-be91-4eb7-a829-988fa37faa11' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'TEXT','placeholder',null,'TT2 Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e14acabb-ad0c-42a1-9e2d-dc7476775f22' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===false"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT1Given===true"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT2Given===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('18092bff-7432-46d0-aff0-c0e81f134343' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('24fc8b80-d176-4b3c-9e26-8ff171a43ebe' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'TEXT','label',null,'jsyPaymentDone_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('39ddd111-0aeb-4511-8a7d-932c83995e29' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5705700f-56a4-4c37-9254-6e7384131336' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('64ac796c-41c2-4f47-8713-30747fa9281e' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6d0e3144-1c82-497b-8b1a-be2e380afb80' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('78c9386d-2fbb-4b68-9562-0f10c46410c8' as uuid),cast('34e404fa-ba4e-430d-82af-95a41077575a' as uuid),'TEXT','tooltip',null,'JSY Payment Done',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('36968150-1df7-49b2-9c4d-27e8b7b1048a' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6b237970-2f2f-41bd-972b-48d2d461fb82' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6dee673c-b899-4561-af8a-3ed36b9721d8' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'TEXT','label',null,'jsyBeneficiary_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('73bc3f92-6556-4f9b-937e-59a06f524ab2' as uuid),cast('43a3c7b6-7d28-474d-9eef-0b6d6e272286' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1f4b46da-3b04-4cae-97bd-5a5cbf64d635' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a32213a-d35a-4e16-bd1c-85df9b8aa41b' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('319e67e4-1544-460a-aabb-2a8fdd9d4748' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'TEXT','placeholder',null,'KPSY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4a0ec323-9703-4a9d-bd3c-3158be8268b6' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4fd7a5cf-f3cf-4e5d-a2da-af2e11081cc0' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5712611b-8b59-4467-baf4-de75540baf7d' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5d78adad-3fbc-4cdd-99be-dd911c9269b1' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'TEXT','tooltip',null,'KPSY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6fa1f644-a3b2-4d16-ae23-c72a1d038308' as uuid),cast('28dca7a3-4195-42fe-b4e0-155688f4400d' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('47fa085b-c7d5-4127-8aa7-2606312c83d4' as uuid),cast('ebed159f-41b4-4939-9364-79db5ecd67ab' as uuid),'TEXT','tooltip',null,'IAY Beneficiary',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e861d2a6-3635-4dfa-8270-7137b15bca7c' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.tt2Given","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===false"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT1Given===true"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT2Given===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('15e33fc6-43e7-46c3-9b76-3a7dc51cf0c3' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','label',null,'tt2date_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3106def5-43ba-42c6-b39a-9988a2102fb3' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('33f70e8d-1177-43e9-bd44-70e677f956e3' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'TEXT','maxDateField',null,'formData.serviceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3402a48f-43d1-4dbf-a98a-c8fd958b329d' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3993f90f-6e60-4af1-84ec-19a7ba39afb9' as uuid),cast('39cd96e2-dc62-4127-b263-67becad18c36' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ca120431-a955-4710-9841-76561fc6ff07' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'TEXT','label',null,'institutionType_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7670cad7-1dd9-4b83-9811-cadd63a3948b' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4379a6c0-e165-4ab2-ba1d-78c8aed17f16' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4624bfaa-f819-443e-865e-5fce48aa662f' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('54fae24d-dfc2-409c-81ff-1c4d055999a8' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6706e2cd-45ca-4a7c-ab5c-87f6f71c95dc' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'TEXT','placeholder',null,'Select HIV Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('beb103f9-1ab1-4d3f-a437-66a59416cc33' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c91794a8-c280-4cf5-8800-33c152f32cef' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"''HOSP''","value2":null,"fieldName":"formData.deliveryPlace","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('daa65b55-1ddd-46fc-ad44-205708c95b6a' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dc3ab379-522f-42d3-af4a-1e1fead3f624' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('fe10bbc1-bc62-4774-8c80-bc337a359f79' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0b7bb7dd-8e1e-4103-80a2-33902ad609ae' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('36f529c9-aec1-4099-b61e-2ec0187a64ad' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('41c7f4fd-baac-485a-9abe-ed7e797a89cd' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'TEXT','placeholder',null,'Select Institution Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4dceb282-fc95-484f-b592-5bbf38104231' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'TEXT','tooltip',null,'Institution Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('50c3b242-f36c-42db-b023-c1aa29bbedcd' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f942e8a4-18f5-48c3-b6b3-036f7620d4d9' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'DROPDOWN','optionsType',null,'queryBuilder',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('00f0086a-7bb2-4bec-8ca6-b51c1667e6a0' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1059845b-9e3a-444e-9f1a-fdf689b48adc' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('165731b3-e6c5-4801-8ed3-72d0ff10205c' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('16940883-3eb1-4211-92bd-09a24eb02276' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('22c1da13-8cec-45f4-8edd-2ef160809caa' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'TEXT','tooltip',null,'Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a4c96a8-feb2-4182-bdc1-c4d7aa5f2a9a' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3364264a-aebe-4606-8e1e-2421389c2dd5' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'TEXT','placeholder',null,'Select Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5826777d-ba32-4854-927f-9543fb3151c3' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6f6d7499-9f6a-4757-8975-d6916b220c59' as uuid),cast('ad668d80-cce7-451b-975a-5f273f2bffab' as uuid),'TEXT','requiredMessage',null,'Please select hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('330951c9-4441-4e17-bd07-1ce0c51472eb' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4375fdff-1c09-496a-b346-a8ce2252b96f' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"''THISHOSP''","value2":null,"fieldName":"formData.deliveryPlace","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('44d9061a-f6e8-42d6-9e02-e070291f7ce9' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f1384266-0c83-49de-a685-ed8f6e0bbf55' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:5418","type":"Change","value":"tt1GivenChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23123d9b-0c41-457a-8b5f-525c850b3e29' as uuid),cast('129f60b1-b384-482b-a16d-66d34b6af17b' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('56c72e41-53eb-4241-82f8-0a4cea283c35' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'TEXT','label',null,'tt2Given_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('772df4a0-3f72-4210-a999-77d83f7a0e38' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('83780f01-9906-44f0-a31e-62347e8f7d7d' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ed3e5e94-28bd-4f52-b8f8-8efc984220ae' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1c18ffd2-d161-4139-b450-8aadcb41adc5' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'JSON','staticOptions',null,'[{"value":"Yes","key":true},{"value":"No","key":false}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('33330d25-cb7d-41a0-ad09-1dfc10db998d' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:1871","type":"Change","value":"tt2GivenChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('33c77c30-a01d-48a6-9ee0-3d02e3b0865a' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('394086e0-4717-4360-9df5-bb511ca72676' as uuid),cast('4941b754-f742-4e9f-aa8f-9eb8cff48c8a' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('20437813-0260-40c2-8a5b-a9631d2084f9' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2ed326bf-45e5-41b4-a48d-a9bb2cee83b2' as uuid),cast('9e6f2149-2eb0-4c1d-a08f-e056b6f8b066' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===true"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTTBoosterGiven===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d847684d-4e2c-4f3f-aea2-f9e1082dced5' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'BOOLEAN','hasPattern',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('344c7b89-02d4-4307-ba54-0fe3811d4657' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('368d979c-a073-4b74-b517-6d7634f400c9' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','placeholder',null,'No. of FA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('505dd384-0085-4b33-9240-bb40f629a1da' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','label',null,'faTabletsGiven_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6088867b-3c88-43a6-b1b9-8bd62d32ba9a' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','tooltip',null,'No. of FA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('71c55eeb-dac8-466a-827d-4d022840ca07' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7aba6492-e51f-437e-a19d-5ca9bfc606e0' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showIfa===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7d8176f7-afbc-485f-9e3a-9adda55914de' as uuid),cast('c05ff1b2-516f-4506-ba79-9d3c5ef55b26' as uuid),'TEXT','requiredMessage',null,'Please enter No. of FA Tablets Given',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6b3c3772-de47-4c32-a25d-788bdcec57ca' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','label',null,'ttBoosterDate_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7f5ac17d-3819-4b33-b167-dfbf043a0bbd' as uuid),cast('250e2581-b995-46a1-be5a-cf9eca1a19b7' as uuid),'TEXT','requiredMessage',null,'Please select TT Booster Given on Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('76a792ee-e406-4c75-a4fb-0e6581e33df7' as uuid),cast('3efedde7-de03-4e74-9011-f94e6140798b' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.tt1Given","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.pregnantInLast3Years===false"},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isTT1Given===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e87183e6-b7cb-4ebc-9eed-1b7a2d9d8a6f' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.showLastDeliveryOutcome===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('428b0d2c-7a2e-4c6a-8584-b3bf41b67c62' as uuid),cast('a6c1e7bd-7969-4f77-a9db-56501402112d' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1d89bf70-646d-4932-a76e-8f0a35e6ca56' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'TEXT','placeholder',null,'Account Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2349081f-772f-4d41-b6d6-40ec52761694' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3ded9062-8ea1-4ae6-8648-becb449d4c99' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('66598c3b-ff20-4a8d-b43c-b34de6c27871' as uuid),cast('ec38bcb3-28fc-468f-9780-d77fa419ce24' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isAccountNumberAvailable===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9a52e815-3ef8-47fe-9dec-f8e0ac91ddd5' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a2ae5af7-e7f4-4df4-9c95-8c76e473b9aa' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e5d4bcc6-cbf8-455c-ae86-7df6ffa4dbc7' as uuid),cast('f5f144df-e123-48ba-bd71-e92c4e21a144' as uuid),'NUMBER','minLength',null,'11',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('047e3fd9-cf2c-43b2-8c74-d33406cae6c8' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1f656de9-d82f-4770-8ca1-8242702ab8c2' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'DROPDOWN','displayType',null,'text',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a65ecf6-4087-4c4e-9e24-4f5efde9c221' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('32c136c0-0440-4687-8607-8c3d0dc302d3' as uuid),cast('f49cafc3-69bd-45e1-b328-f1672ea7bf6e' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2ded1e1-d5d3-4fc6-97c2-5d2c2124d413' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'TEXT','requiredMessage',null,'Please enter other danger sign',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e1049e46-90ad-409f-b35c-11c402433eb0' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0873d5ff-49cc-4441-81cd-3949d6a8cb1c' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0a7118cb-78b3-4190-a5dc-f35d9d7c9bcc' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'NUMBER','maxLength',null,'100',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('143a70e0-5f4f-4e12-b3c0-68da486c6ab6' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'TEXT','placeholder',null,'Other Danger Sign',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2c6788f2-9218-4be9-88d7-530a7e69479d' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5dd62f38-a204-4f3f-a250-e59a82d3ec83' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'TEXT','tooltip',null,'Other Danger Sign',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('79175b9e-142f-40f6-ba88-f36737509986' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'TEXT','label',null,'otherDangerousSign_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7fec1f50-8c48-41bf-8cf2-8d9700d67b9c' as uuid),cast('b77de06e-b26a-47b1-a189-7e530dfdeda8' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0c472c01-d210-4704-a41e-fe523729d9fe' as uuid),cast('2ad4c37c-c35b-495c-a1f8-7c87f8fef9f5' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null},{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isApplicableForInjCorticosteroid===true"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('940facbb-c52e-429a-be2e-58093ddbf20d' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'JSON','staticOptions',null,'[{"value":"APH","key":"APH"},{"value":"PPH","key":"PPH"},{"value":"Placenta previa","key":"PLPRE"},{"value":"Pre term","key":"PRETRM"},{"value":"PIH","key":"PIH"},{"value":"Convulsion","key":"CONVLS"},{"value":"Malpresentation","key":"MLPRST"},{"value":"Previous LSCS","key":"PRELS"},{"value":"Twins","key":"TWINS"},{"value":"Still birth","key":"SBRTH"},{"value":"Previous 2 abortions","key":"P2ABO"},{"value":"Known case of sickle cell disease","key":"KCOSCD"},{"value":"Other","key":"OTHER"},{"value":"None","key":"NONE"},{"value":"Not known","key":"NK"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b60709f3-5bc6-440e-b9e5-ee75009ea8a2' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'TEXT','tooltip',null,'Presence of complication during any previous pregnancy',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b8478d1c-d3c8-41c6-8993-2d7c08ec74eb' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dc5ba297-09d0-4757-9a96-39c68398a1bd' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ef5f7ffe-1edc-4c24-9e95-37e668946b4f' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'TEXT','requiredMessage',null,'Please select pregnancy complications',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('13d656f9-e486-44a5-9b7d-0eb6014208fa' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3176bfa2-a173-4db5-b1be-fb5b812456c8' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('359f002c-3ee7-44b4-bd28-9b066b5d0017' as uuid),cast('9848b57b-31b2-438f-b2aa-16d79c102376' as uuid),'BOOLEAN','isMultiple',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('867cdf98-0b36-4622-a398-8ec27bf095f8' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8e92a2df-ee9f-4dc9-8e33-cd8c82d9790c' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'TEXT','tooltip',null,'Albendanzole Tablets (400 mg)',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8fce0158-24a1-465d-82b1-eb424b23214c' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'TEXT','requiredMessage',null,'Please select a value',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b1967155-fe15-4633-8a3d-a7ce17df2227' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'TEXT','placeholder',null,'Albendanzole Tablets (400 mg)',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e826f01b-8904-41db-9d03-02f46d4ed361' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('014ef246-9b08-46cc-b966-0ccf93998a57' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'TEXT','label',null,'albendazoleGiven_RCH_FACILITY_ANC',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('46b59205-43b8-452a-85b7-d5f2a5f6e2ff' as uuid),cast('50a61478-cdaf-4f67-95c0-6ee5eb0aa0a2' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),1094,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('364ee8ea-d105-4c6f-b842-1a5d6a9f0522' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4bcfd2b8-3eb0-4217-8358-944bc52ddca6' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'TEXT','placeholder',null,'Is Member Alive',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5f5d2a72-73e3-4274-b597-93ef8e978e8e' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c6aef817-e13a-4776-9dfa-1e7a6a5cc910' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cbd5d3bc-a6ee-4288-9782-c2c3b3f3982b' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'TEXT','tooltip',null,'Is Member Alive',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0146044f-15b5-468c-be58-23e384918751' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'BOOLEAN','isBoolean',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('32aebc94-3862-40be-9d9a-c5efb4381a95' as uuid),cast('8fde0f97-a9d4-4b24-a033-dbd9f5c2bb0c' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:14947","type":"Change","value":"isAliveChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d3c122b0-1b1e-4bda-9b5b-eb0637f30af6' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e81d0c26-733b-4876-b6f7-57c22ec624a3' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','label',null,'mobileNumber_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f90321c1-9f7a-4059-8822-4e9d9bd49ab3' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"formData.isMobileNumberAvailable===false"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f91b4d26-4b45-4f95-8d0c-1b1e0088ce7f' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('01795670-a174-43ba-b47c-f47245926323' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','tooltip',null,'Mobile Number',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('033d8fbf-af71-40f5-8fcc-e8bb4f50b088' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('274efc05-f393-4d72-8ab6-c1a60ec01a27' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'TEXT','pattern',null,'^(6|7|8|9)[0-9]*$',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('39b93ed4-26ca-48e3-bb24-ddb0d4df72af' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'NUMBER','maxLength',null,'10',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7f1a909b-9287-46ec-8f77-21a022d946c9' as uuid),cast('34735c1e-7238-476b-827b-70b2bbae87ac' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('95704a98-d4c4-492d-a33c-456eac606dec' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('98c72093-07a6-46fb-bec8-32cd663eaff0' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e4cd3ca7-82f4-401c-bf05-24118bfadaf7' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','tooltip',null,'Service Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0f977ed6-9b88-459f-beef-642288b6b780' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('17cc77e8-5959-4d42-aa50-ab33501af580' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','label',null,'serviceDate_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('20a61887-0222-4168-b50c-db6dd992cd87' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','requiredMessage',null,'Please select service date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23a11543-4fb5-4f6b-9383-843c2ff0442b' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:17695","type":"Change","value":"serviceDateChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5bf07834-28d1-46e5-ab99-bae4ffb9eb93' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('626357e3-1342-48c0-9aca-257c592f8098' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','minDateField',null,'minServiceDate',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6bbe7d2a-d527-4e5b-b9f2-2b5b168ec158' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7ecae04d-4f56-4d54-bcb8-d9ff273fb0fd' as uuid),cast('779c8dc8-0798-42cc-8a77-b4ab965f2450' as uuid),'TEXT','placeholder',null,'Service Date',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8fca98f5-48ca-47aa-80c5-af3abc14085a' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'JSON','staticOptions',null,'[{"value":"This Hospital","key":"THISHOSP"},{"value":"Another Institution","key":"HOSP"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('96a264c7-ebee-4be7-aa59-7dd37b3cf276' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2b7b42e-5397-4c42-a265-f1b865877134' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c3b04b54-dec1-47ca-b657-2155daf43f95' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cddd80ea-0ca1-4bad-a25c-c4ca527c93bc' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d2df6116-748e-49b3-9e8a-f13e092d65e1' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'TEXT','requiredMessage',null,'Please select delivery place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ebaed184-ca1b-4ee8-9d7d-c4fa724e6e95' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0204d21c-c8a7-413f-b596-fb46b4fc1836' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'TEXT','placeholder',null,'Select Delivery Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('30ca111f-6bc9-4d6d-ad56-ca422695a7e3' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4355d798-c71d-4135-897a-fa420317e3e5' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'VISIBILITY','visibility',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('483de8d1-cd37-4188-bbdd-8849e12fb9d0' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5f3ecb52-e6ef-439d-91d5-3e5d0ac682ef' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:17962","type":"Change","value":"deliveryPlaceChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('681ed1c3-cdb6-49fa-83d0-5533cc47c1e8' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'TEXT','label',null,'deliveryPlace_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6823c377-433b-4e84-adb8-4719b0446caa' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7b499b41-919f-453a-ad53-af8f2552ebc8' as uuid),cast('94d9a82e-17b0-4cae-b36a-bd8a09af3244' as uuid),'TEXT','tooltip',null,'Delivery Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bafa3557-b48d-4d74-871e-6f8f6958fc3b' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bc4371f1-a0e0-4727-875e-c744eab1cb74' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'JSON','staticOptions',null,'[{"value":"Home","key":"HOME"},{"value":"On the way","key":"ON_THE_WAY"},{"value":"Hospital","key":"HOSP"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bf6d8ba8-0245-4d24-91c3-fb9c8ca546c2' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c2dbff88-d0b2-45b8-8629-9664de919e86' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'TEXT','label',null,'placeOfDeath_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cc2b765f-272c-43f8-82d6-4b828e234270' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('def43661-052b-447b-8d1b-db42b4a01f65' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"false","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('16724366-6994-49d0-92c3-d47b3d567494' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3096f069-7b4f-4cbf-b943-7fa870f17938' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'TEXT','requiredMessage',null,'Please select death place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4ed33e98-9873-4dc8-9031-56e3a8f70663' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('56d7bd6f-eedd-4ce6-8000-0df4bbddef5a' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('618c0654-9c82-46a8-a36d-c32057ebefe9' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('69de6858-a1e5-410b-9c6f-f35264c8aad6' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('796753af-cd6f-4d2d-8cfa-1276ccecaca1' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'TEXT','placeholder',null,'Select Death Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7ae099e9-fff4-4eef-81f9-224b337ee156' as uuid),cast('216a2e6a-f827-4934-85bb-226174699b2d' as uuid),'TEXT','tooltip',null,'Death Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('47940a7b-ba2f-4bc2-b2ce-dcb2ce097f52' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"false","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('52d71889-ae89-4fbb-8c67-92adeec4b913' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5e78c026-6a4d-41d8-9525-045bb98dc780' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'TEXT','listValueField',null,'deathReasonsFhwAnc',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('726ca241-8860-4f8f-872c-873f2beaeef4' as uuid),cast('51ca5e97-d7f3-4515-9843-3fb7ebeeb2d8' as uuid),'DROPDOWN','optionsType',null,'listValueField',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c0ab246a-af20-4c1e-967c-0116dc8ca4f5' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c899d6b7-643e-4e03-a77c-a61c112909fa' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('dae7f30a-442f-4d85-9d5b-e37358702fc7' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'JSON','staticOptions',null,'[{"value":"A+","key":"A+"},{"value":"A-","key":"A-"},{"value":"B+","key":"B+"},{"value":"B-","key":"B-"},{"value":"O+","key":"O+"},{"value":"O-","key":"O-"},{"value":"AB+","key":"AB+"},{"value":"AB-","key":"AB-"},{"value":"N/A","key":"N/A"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f8ae2e02-f3ba-4c48-b1d6-e180760185b5' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0150a5a0-2626-4bf6-8899-89aff59d3ba7' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0acf1aa5-e61c-4f85-834d-5dcbe98ffd69' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3c70e1c9-5332-4929-9608-e551424e4a3b' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3e412862-258f-48a4-b439-1489a561a407' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'TEXT','placeholder',null,'Select Blood Group',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('59877fbc-e696-42ff-9738-be8f7be4244a' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('617d1956-1525-48a3-b7be-a567a1be4e74' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'TEXT','tooltip',null,'Blood Group',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('628dd204-ccbf-4c41-b07c-177c86f1f8d8' as uuid),cast('6e9f6f09-9a05-400c-afa4-b0e05f09a77a' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('92dea71f-f759-47aa-aa31-277dce5b9b11' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9351a1c0-33db-492b-8f15-ba7c6c211648' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'TEXT','placeholder',null,'Select HBsAg Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a0129db6-6c4b-46eb-9cb5-305a265ca2bc' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c959d73a-b7a7-4461-b52e-4df98719fe66' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'TEXT','tooltip',null,'HBsAg Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d2df143e-f3cf-42f3-93b1-236981955c5d' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'TEXT','requiredMessage',null,'Please select HBsAg Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e6bf59cb-8fcf-4c66-bbac-cb2b3b8bf092' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('047d6386-fb07-4b9c-8de9-602f5225051e' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'TEXT','label',null,'hbsagTest_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0683b845-2d25-4f31-9098-ac2b76d57f42' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('15ff1bfa-2c41-474a-8d2d-79a98e029355' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('23186502-bdcc-4d11-9ad1-6d7ce8f08ad5' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('57b95d53-3858-48e6-9a58-df7aa3915c99' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6d80a93a-d60a-4253-a5bc-64b4d74757de' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'JSON','staticOptions',null,'[{"value":"Not done","key":"NOT_DONE"},{"value":"Reactive","key":"REACTIVE"},{"value":"Non-reactive","key":"NON_REACTIVE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('75973c00-d244-452a-8150-fcf5a56a95b7' as uuid),cast('9fd66d87-5733-4f81-ab1b-755ee0b42061' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a16cebab-838f-4c30-bb9e-7d662ddf8a10' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a972ac9e-4f58-45e2-b6f5-7ad41a876a95' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ac6f3299-970c-4da7-a090-fb16bc36b03c' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('57dc0fb0-921f-479f-b6ea-daa0c2f93ee6' as uuid),cast('215d3297-0989-4aa8-ac7e-24370082b9f7' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e9f12566-1395-4662-b6b3-63643656b5a6' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ec15f734-5f41-4535-948e-a9ed3c6d5e7b' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ef40adab-4715-47e7-b41f-d896d0e37a3b' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('092748c0-b8c1-4e94-ab1f-dd706331ece7' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0a89284d-9af6-4fd2-b306-66e24a996929' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('35af2d68-f415-4217-a046-ab3e81ad5422' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'TEXT','requiredMessage',null,'Please select HIV Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6f0f5b24-749c-4465-aa6c-212b8afe4649' as uuid),cast('add55320-d982-4ef5-b8ee-546fa9fd976d' as uuid),'JSON','staticOptions',null,'[{"value":"Not done","key":"NOT_DONE"},{"value":"Positive","key":"POSITIVE"},{"value":"Negative","key":"NEGATIVE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c08bcb32-5856-44f7-bf7f-182cc5249078' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ff500599-34b8-46a8-901c-1736d8811081' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0417be10-4b1f-4a29-a218-4e46a6308d00' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('14507dc1-ad8d-4ec0-a71f-1ed8b9f0ec32' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('29e10d53-b701-4f74-b880-16a4cc3b652c' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:3815","type":"Change","value":"sickleCellTestChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('52bfa080-5448-4099-8a20-b09de0779210' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'JSON','staticOptions',null,'[{"value":"Not done","key":"NOT_DONE"},{"value":"Negative","key":"NEGATIVE"},{"value":"Sickle Cell Trait","key":"SOCIAL_CELL_TRAIT"},{"value":"Sickle Cell Disease","key":"SICKLE_CELL_DISEASE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('758f818e-8338-4c44-b479-b1c2b67dad7a' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('75fc76fa-e978-4e35-9706-ad23f4186b4d' as uuid),cast('1bf21575-4038-48b9-90ac-c69814c446f9' as uuid),'TEXT','requiredMessage',null,'Please select Sickle Cell Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('89cd3260-afb5-4ec7-9177-ab2330a08eda' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b80f6fe5-bbbc-4630-ae82-9f7abf5c9c47' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'JSON','staticOptions',null,'[{"value":"Iron Sucrose","key":"IRON_SUCROSE"},{"value":"FCM","key":"FCM"},{"value":"None","key":"NONE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c0b8e84f-d66b-4dab-993c-17111953abcf' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'TEXT','placeholder',null,'Select Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('da5502d6-f0f1-4561-a7d3-ca80e7f0a2a9' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e8ccb09d-e7d5-436e-9092-591e9ef224b5' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f16ef435-1c3f-41e9-84af-298608811e4f' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'TEXT','label',null,'ironDefAnemiaInj_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0b72a1b6-48c4-484c-8e98-1ba3952455e7' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'TEXT','requiredMessage',null,'Please select Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('170fc27a-12ba-4dd2-ba3e-abccd4a638b6' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'TEXT','tooltip',null,'Iron Deficiency Anemia Injection',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3a173250-9d57-4712-b727-9bc7667d1266' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4c712593-bfca-48d0-a394-74a850a374e9' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('749d0cd8-9f11-4da5-8f8d-070ab7da0e61' as uuid),cast('235ad3c0-84de-472a-8028-a10a53d25a7f' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('a67b2a4b-ef5e-4696-ae73-b4e6a0505318' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'JSON','staticOptions',null,'[{"value":"Subcenter","key":"SUBCENTER"},{"value":"PHC","key":"PHC"},{"value":"UPHC","key":"UPHC"},{"value":"CHC","key":"CHC"},{"value":"Sub-District Hospital","key":"SUBDISTRICTHOSP"},{"value":"District Hospital","key":"DISTRICTHOSP"},{"value":"Trust Hospital","key":"TRUSTHOSP"},{"value":"Private (Chiranjeevi)","key":"CHIRANJEEVIHOSP"},{"value":"Private","key":"PRIVATEHOSP"},{"value":"Other","key":"OTHER"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aab4b036-861a-4a69-87a2-854635505640' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ae95dcf4-8a08-4c22-a65f-b11d902fd08e' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b64b7ee8-375a-4f92-bc21-e2d2a37153d9' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('bb35084b-fa82-48ed-b491-de20bd0e7cf2' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('ea9c2056-13dd-45a7-937b-2a8821e9f2da' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0d7d8382-6e9b-472b-91e5-e9f279fa5785' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2955808f-bb46-4883-a34c-6eba642e74bd' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2d0e632b-ac26-472e-a52a-de358d0c1d4c' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'TEXT','placeholder',null,'Select Expected Delivery Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3427ac6e-f01e-4186-a088-43929483effa' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'TEXT','tooltip',null,'Expected Delivery Place',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('40c843fb-2f60-47d0-8f78-fef03d27f688' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5dcf48b6-4a8b-4e60-95b6-105c51d61e3f' as uuid),cast('7dd45843-72c1-4807-bd98-c4b99ccf503d' as uuid),'TEXT','label',null,'expectedDeliveryPlace_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('aec2fa4d-f7be-4028-b8b8-6855740c0399' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'TEXT','label',null,'foetalPosition_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c5a438a4-8704-4311-9691-fcbd5e5c66f3' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('cd95a1fe-9783-4249-ba19-5cbccac118c8' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('d72697a6-839c-4365-9f1a-ae04160fbf8f' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f20cb61d-1c3c-4dd2-a81c-cf44f7419c51' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'TEXT','tooltip',null,'Foetal Position',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1336b2e1-5c14-46ed-8cde-65a3afa5fbcd' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2431270f-318d-4794-9ade-211e2491bc12' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2a040b3d-d174-4c05-aa45-813e3206128e' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('57c27d66-e8d7-4c11-a544-75f92175f50e' as uuid),cast('9bcac9dd-ab67-4e5c-9c02-bd7242d2711c' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f8a6663c-b84a-4b73-bb61-65c32d5a4238' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('08560aa7-fd1a-4724-9bdf-068f3d29442c' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('11d41dbe-7a26-4442-bb3a-f6c3889c3711' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'TEXT','placeholder',null,'Select Blood Sugar Test',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1afe4757-7d37-4d13-bd5a-adc998234a62' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1c9bb305-90b2-4ebf-96b6-efaf73598c06' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'TEXT','label',null,'bloodSugarTest_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2b87e2ab-fb23-4350-a193-d3ca2e674886' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'JSON','staticOptions',null,'[{"value":"Done on empty stomach","key":"EMPTY"},{"value":"Done on non-empty stomach","key":"NON_EMPTY"},{"value":"Not done","key":"NOT_DONE"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2c5d528b-8e55-4075-a01f-20c86ed7feff' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2ed6ac51-9bc8-4912-b38e-964d5f93e342' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5b3a1d7e-849a-41a5-b0df-d8bc16e3103b' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'DROPDOWN','optionsType',null,'staticOptions',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6f0c608e-4304-46e8-bb4d-8661f3ee91c7' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('7b1f6f5c-1604-4b85-a0d7-26d8823112fc' as uuid),cast('f2ab8bb1-355e-41f2-911f-d0c6cffa9568' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8e73d954-23f4-4842-b44a-952e892fef97' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('8fd566e3-6d20-4b8c-96b5-23b76727f518' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'TEXT','tooltip',null,'Dangerous Signs',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('996acc21-0753-432c-a744-442ff52e975a' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'BOOLEAN','isMultiple',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9b203a95-e8bd-4de4-90e3-504c0d98ad43' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'TEXT','placeholder',null,'Select Dangerous Signs (if any)',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('adb84380-2fca-4543-8e5e-e98f0fadac98' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('b07a677e-6528-4369-9cff-fd674840bf9d' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'TEXT','requiredMessage',null,'Please select danger signs',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e6d7f107-7add-4fc9-8b6a-c41b825f1d46' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'TEXT','listValueField',null,'dangerousSignsFhwAnc',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('09193c6b-56f4-40c9-b2d5-b2977057cd16' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.isAlive","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('38d1a85d-44fc-432e-b9be-4e38b85aee32' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:4265","type":"Change","value":"dangerSignsChanged"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('46d8b1d2-6af2-4f2b-a9d0-99f7f2346d57' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4fd4553f-1645-429f-9683-9edfd119b920' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5266d332-850c-48ec-9b15-ed274abf0953' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('532ff39e-1671-4fd4-aca2-7354241bae8b' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'DROPDOWN','optionsType',null,'listValueField',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('60764d20-8116-4eaf-8c7c-6490dda8753d' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'TEXT','label',null,'dangerousSignIds_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6aa73c67-507f-4322-8a1f-33406b3d7890' as uuid),cast('b877bd29-642b-4d4a-944a-e7f82206e4cd' as uuid),'BOOLEAN','isRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c98d7f9d-0e66-4611-99e0-85772aecd1ac' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('e6b80776-55c4-4c3f-89a2-9f848bf1603b' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"EQWithType","value1":"true","value2":null,"fieldName":"formData.referralDone","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f0c578e5-8d64-4c3c-8bc5-338224da6ff2' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('00ac5c08-11a0-4a99-9584-ef56f4564966' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'BOOLEAN','isDisabled',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1ad86724-0408-422f-b116-ae5f68d0b4c6' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3c79244c-0bab-406f-bb94-401d3c6b4f65' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('522deeae-fda0-4160-b858-44175b8a153e' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'TEXT','requiredMessage',null,'Please select institution type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5986dc9c-229c-4a7c-91ec-120a5e2aa541' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'TEXT','label',null,'referralInfraType_RCH_FACILITY_ANC',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5d9c12fb-0fde-4523-baa9-b69d555b97aa' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'DROPDOWN','optionsType',null,'listValueField',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('65b05bdb-ffcf-4a6c-95ae-5ecb6f50888b' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'TEXT','listValueField',null,'Health Infrastructure Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('6ecfdf61-cb83-4f30-84b6-202e0c15dd39' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('75f4d9b4-c6a6-44cc-9388-7fd17e0816c7' as uuid),cast('b03d91a0-aca4-41d5-97d2-9b9e2e366fa2' as uuid),'TEXT','placeholder',null,'Select Institution Type',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0e316122-fe6c-45fe-b06d-abcbb56d71e2' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('1c7a3192-213f-4614-8c62-0421e064dd2b' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('2112ff57-4fac-4b51-87f5-9defdad1d426' as uuid),cast('c6f7c216-5cd5-4f4b-bd01-d8a503270d1b' as uuid),'DROPDOWN','optionsType',null,'queryBuilder',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('55b9c143-b1d7-4e5a-8a1c-220b726b6f40' as uuid),cast('1e6af7e9-0ea0-42b2-9c46-24c0cee859d6' as uuid),'EVENTS','events',null,'[{"0":{"type":"","value":""},"$$hashKey":"object:18661","type":"Change","value":"retrieveHospitalsByInstitutionType"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('be303353-1043-4a41-84ad-137ed2dbd41e' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'TEXT','requiredMessage',null,'Please select hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f06c2abf-7872-4b45-b593-3b15f82e39ca' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"FIELD","operator":"NQWithType","value1":"null","value2":null,"fieldName":"formData.referralInfraType","queryCode":null,"expression":null}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('0cfc6f0c-7608-47e0-8584-38f1cd51190b' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'DROPDOWN','optionsType',null,'queryBuilder',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('15083248-37ad-4053-9ff6-1c93f63a8cbc' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'TEXT','placeholder',null,'Select Hospital',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('159e6407-246e-44ae-afd5-c382609858aa' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'BOOLEAN','isHidden',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('3cb3258a-ac16-4cda-8571-1ddd933ba9e4' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'BOOLEAN','isRequired',null,'true',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('53158ffd-516f-4aca-8eef-dc9fed9aa8bc' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'BOOLEAN','isMultiple',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('5b846706-45bd-46b6-ae90-225d3222760d' as uuid),cast('ebc8ae15-500c-4e3f-8a2a-a6b2b436a524' as uuid),'BOOLEAN','additionalStaticOptionsRequired',null,'false',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('9245eea5-3603-4b64-b616-f4ad30f58fb8' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'EVENTS','events',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('c5e16f12-7e77-435d-97ad-4461a335ae82' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'JSON','tableConfig',null,'[{"label":"Family ID","key":"familyid","type":"text"},{"label":"Service Date","key":"service_date","type":"date"},{"label":"Location Name","key":"locationname","type":"text"},{"label":"LMP","key":"lmp","type":"date"},{"label":"Weight(in kg)","key":"weight","type":"text"},{"label":"JSY Beneficiary","key":"jsy_beneficiary","type":"boolean"},{"label":"KPSY Beneficiary","key":"kpsy_beneficiary","type":"boolean"},{"label":"IAY Beneficiary","key":"iay_beneficiary","type":"boolean"},{"label":"Chiranjeevi Yojna Beneficiary","key":"chiranjeevi_yojna_beneficiary","type":"boolean"},{"label":"ANC Place","key":"delivery_place","type":"text"},{"label":"Systolic BP","key":"systolic_bp","type":"text"},{"label":"Diastolic BP","key":"diastolic_bp","type":"text"},{"label":"Member Height","key":"member_height","type":"text"},{"label":"Foetal Height","key":"foetal_height","type":"text"},{"label":"Foetal Heart Sound","key":"foetal_heart_sound","type":"boolean"},{"label":"IFA Tablets Given","key":"ifa_tablets_given","type":"text"},{"label":"FA Tablets Given","key":"fa_tablets_given","type":"text"},{"label":"Calcium Tablets Given","key":"calcium_tablets_given","type":"text"},{"label":"HBsAG Test","key":"hbsag_test","type":"text"},{"label":"Blood Sugar Test","key":"blood_sugar_test","type":"text"},{"label":"Urine Test Done","key":"urine_test_done","type":"boolean"},{"label":"Urine Albumin","key":"urine_albumin","type":"text"},{"label":"Urine Sugar","key":"urine_sugar","type":"text"},{"label":"Albendazole Given","key":"albendazole_given","type":"boolean"},{"label":"Member Status","key":"member_status","type":"text"},{"label":"Expected Delivery Date","key":"edd","type":"date"},{"label":"Death Date","key":"death_date","type":"date"},{"label":"VDRL Test","key":"vdrl_test","type":"text"},{"label":"HIV Test","key":"hiv_test","type":"text"},{"label":"Death Place","key":"place_of_death","type":"text"},{"label":"Haemoglobin","key":"haemoglobin_count","type":"text"},{"label":"Death Reason","key":"death_reason","type":"text"},{"label":"JSY Payment Done","key":"jsy_payment_done","type":"boolean"},{"label":"Last Delivery Outcome","key":"last_delivery_outcome","type":"text"},{"label":"Expected Delivery Place","key":"expected_delivery_place","type":"text"},{"label":"Family Planning Method","key":"family_planning_method","type":"text"},{"label":"Foetal Position","key":"foetal_position","type":"text"},{"label":"Other Previous Pregnancy Complication","key":"other_previous_pregnancy_complication","type":"text"},{"label":"Foetal Movement","key":"foetal_movement","type":"text"},{"label":"Is High Risk Case","key":"is_high_risk_case","type":"boolean"},{"label":"Blood Group","key":"blood_group","type":"text"},{"label":"Sugar Test Before Food","key":"sugar_test_before_food_val","type":"text"},{"label":"Sugar Test After Food","key":"sugar_test_after_food_val","type":"text"},{"key":"sickle_cell_test","label":"Sickle Cell Test","type":"text"}]',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('f591562c-8482-4a91-b9a0-cb4d28d94c8b' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'VISIBILITY','visibility',null,'{"conditions":{"rule":"AND","options":[{"type":"CUSTOM","operator":null,"value1":null,"value2":null,"fieldName":null,"queryCode":null,"expression":"previousAncInfo.length"}]}}',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('13e1a17b-cfb3-4938-a028-a8a6a2193279' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'DISABILITY','disability',null,'null',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('4803afb7-1af3-46b2-a239-5762899362c6' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'TEXT','tableObject',null,'previousAncInfo',60512,now(),60512,now());

insert into system_constraint_field_value_master(uuid,field_master_uuid,value_type,key,value,default_value,created_by,created_on,modified_by,modified_on)
                         values(cast('523f942e-7daf-4ee9-91af-37dd57920b6e' as uuid),cast('13278010-2b3f-4fc4-8d8c-3df1b4524d1a' as uuid),'REQUIRABLE','requirable',null,'null',60512,now(),60512,now());
