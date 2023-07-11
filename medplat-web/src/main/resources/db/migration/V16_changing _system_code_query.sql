update public.query_master set query = 'WITH LIST_VALUE  AS
                                                (select * from listvalue_field_value_detail where field_key = (select field_key from listvalue_field_master where field = #fieldId# AND is_active = true )  AND is_active =true),
                                                CODES AS ( select value as "code" from listvalue_field_value_detail as lvd where field_key  = "system_codes_supported_types"  AND is_active = true ) ,
                                                CODE_MASTER AS ( select CAST ( id AS TEXT),table_id as table_id,table_type,code_type ,code,parent_code,description,created_by from system_code_master where table_type  = "LIST_VALUE" ) ,
                                                TEMP_TEB AS  ( SELECT lv.id ,
                                                count ( codes.code ) ,lv.value from
                                                LIST_VALUE lv,CODES codes group by lv.id ,lv.value
                                                )
                                                select tt.id,tt.value as name,
                                                cast(array_to_json(array_agg(json_build_object( "id", CAST ( cm.id AS TEXT), "tableId",cm.table_id,"tableType",cm.table_type,"codeType",cm.code_type
                                                ,"code",cm.code,"description",cm.description,"parentCode",cm.parent_code,"createdBy",cm.created_by ))) as text) as "SYSTEM_CODE"
                                                  from
                                                TEMP_TEB tt left JOIN CODE_MASTER cm ON tt.id  = cm.table_id group by tt.id,tt.value' where code = retrieve_system_code_mapping_by_field_id