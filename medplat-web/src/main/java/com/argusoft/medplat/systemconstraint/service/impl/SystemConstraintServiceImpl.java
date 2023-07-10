package com.argusoft.medplat.systemconstraint.service.impl;

import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.internationalization.dao.InternationalizationLabelDao;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.systemconstraint.constants.SystemConstraintConstants;
import com.argusoft.medplat.systemconstraint.dao.*;
import com.argusoft.medplat.systemconstraint.dto.*;
import com.argusoft.medplat.systemconstraint.mapper.*;
import com.argusoft.medplat.systemconstraint.model.*;
import com.argusoft.medplat.systemconstraint.service.SystemConstraintService;
import com.google.gson.Gson;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SystemConstraintServiceImpl implements SystemConstraintService {

    @Autowired
    SystemConstraintFormMasterDao systemConstraintFormMasterDao;
    @Autowired
    SystemConstraintFieldMasterDao systemConstraintFieldMasterDao;
    @Autowired
    SystemConstraintFieldValueMasterDao systemConstraintFieldValueMasterDao;
    @Autowired
    SystemConstraintStandardFieldMasterDao systemConstraintStandardFieldMasterDao;
    @Autowired
    SystemConstraintStandardFieldMappingMasterDao systemConstraintStandardFieldMappingMasterDao;
    @Autowired
    SystemConstraintStandardFieldValueMasterDao systemConstraintStandardFieldValueMasterDao;
    @Autowired
    InternationalizationLabelDao internationalizationLabelDao;
    @Autowired
    QueryMasterService queryMasterService;
    @Autowired
    SystemConstraintFormVersionDao systemConstraintFormVersionDao;
    @Autowired
    SystemConfigurationDao systemConfigurationDao;

    @Override
    public List<SystemConstraintFormMasterDto> getSystemConstraintForms(Integer menuConfigId) {
        return systemConstraintFormMasterDao.getSystemConstraintForms(menuConfigId);
    }

    @Override
    public SystemConstraintFormMasterDto getSystemConstraintFormByUuid(UUID uuid) {
        return systemConstraintFormMasterDao.getSystemConstraintFormByUuid(uuid);
    }

    @Override
    public String getSystemConstraintFormConfigByUuid(UUID uuid, String appName) {
        return systemConstraintFormMasterDao.getSystemConstraintFormConfigByUuid(uuid, appName);
    }

    @Override
    public List<SystemConstraintFieldMasterDto> getSystemConstraintFieldsByFormMasterUuid(UUID formMasterUuid) {
        return systemConstraintFieldMasterDao.getSystemConstraintFieldsByFormMasterUuid(formMasterUuid);
    }

    @Override
    public SystemConstraintFormMasterDto createOrUpdateSystemConstraintForm(SystemConstraintFormMasterDto systemConstraintFormMasterDto, String type) {
        SystemConstraintFormMaster systemConstraintFormMaster = SystemConstraintFormMasterMapper.mapDtoToModel(systemConstraintFormMasterDto);
        UUID formMasterUuid = systemConstraintFormMaster.getUuid();
        if (formMasterUuid != null) {
            systemConstraintFormMasterDao.update(systemConstraintFormMaster);
            if (type.equals(SystemConstraintConstants.MOBILE_TYPE)) {
                this.createOrUpdateMobileFormVersion(systemConstraintFormMaster.getUuid(), systemConstraintFormMasterDto.getMobileTemplateConfig());
            }
        } else {
            systemConstraintFormMaster.setUuid(UUID.randomUUID());
            systemConstraintFormMasterDao.create(systemConstraintFormMaster);
        }
        systemConstraintFormMasterDao.flush();
        return SystemConstraintFormMasterMapper.mapModelToDto(systemConstraintFormMaster);
    }

    private void createOrUpdateMobileFormVersion(UUID formUUID, String template) {
        SystemConfiguration systemConfiguration
                = systemConfigurationDao.retrieveSystemConfigurationByKey(SystemConstantUtil.MOBILE_FORM_VERSION);
        SystemConstraintFormVersion systemConstraintFormVersion
                = systemConstraintFormVersionDao.getFormVersionByFormUuidAndType(formUUID, SystemConstraintConstants.MOBILE_TYPE);
        if (systemConstraintFormVersion != null) {
            if (systemConstraintFormVersion.getVersion() <=
                    Integer.parseInt(systemConfiguration.getKeyValue())) {
                systemConstraintFormVersion.setId(null);
                systemConstraintFormVersion.setVersion(Integer.parseInt(systemConfiguration.getKeyValue()) + 1);
                systemConstraintFormVersion.setCreatedOn(null);
                systemConstraintFormVersion.setCreatedBy(null);
            }
        } else {
            systemConstraintFormVersion = new SystemConstraintFormVersion();
            systemConstraintFormVersion.setVersion(Integer.parseInt(systemConfiguration.getKeyValue()));
            systemConstraintFormVersion.setFormMasterUuid(formUUID);
            systemConstraintFormVersion.setType(SystemConstraintConstants.MOBILE_TYPE);
        }
        systemConstraintFormVersion.setTemplateConfig(template);
        systemConstraintFormVersionDao.createOrUpdate(systemConstraintFormVersion);
        systemConstraintFormVersionDao.flush();
    }

    @Override
    public String createOrUpdateSystemConstraintFieldConfig(UUID formMasterUuid, SystemConstraintFieldMasterDto systemConstraintFieldMasterDto) {
        if (formMasterUuid == null) {
            throw new ImtechoUserException("Form not found!", 0);
        }
        SystemConstraintFieldMaster systemConstraintFieldMaster = SystemConstraintFieldMasterMapper.mapDtoToModel(systemConstraintFieldMasterDto, formMasterUuid);
        UUID fieldMasterUuid = systemConstraintFieldMaster.getUuid();
        if (fieldMasterUuid != null) {
            systemConstraintFieldMasterDao.update(systemConstraintFieldMaster);
        } else {
            systemConstraintFieldMaster.setUuid(UUID.randomUUID());
            fieldMasterUuid = systemConstraintFieldMasterDao.create(systemConstraintFieldMaster);
        }
        systemConstraintFieldMasterDao.flush();
        List<SystemConstraintFieldValueMasterDto> systemConstraintFieldValueMasterDtoList = systemConstraintFieldMasterDto.getSystemConstraintFieldValueMasterDtos();
        List<SystemConstraintFieldValueMaster> systemConstraintFieldValueMasterList = SystemConstraintFieldValueMasterMapper.mapDtoListToModelList(systemConstraintFieldValueMasterDtoList, fieldMasterUuid);
        systemConstraintFieldValueMasterDao.createOrUpdateAll(systemConstraintFieldValueMasterList);
        for (SystemConstraintFieldValueMasterDto systemConstraintFieldValueMasterDto : systemConstraintFieldValueMasterDtoList) {
            if (("label").equals(systemConstraintFieldValueMasterDto.getKey())) {
                if (("ALL").equals(systemConstraintFieldMaster.getAppName())) {
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", "WEB");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", "TECHO_MOBILE_APP");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getDefaultValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", "WEB");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getDefaultValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", "TECHO_MOBILE_APP");
                } else {
                    String appName = ("TECHO_APP").equals(systemConstraintFieldMaster.getAppName()) ? "TECHO_MOBILE_APP" : "WEB";
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", appName);
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintFieldValueMasterDto.getDefaultValue(), systemConstraintFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", appName);
                }
                internationalizationLabelDao.flush();
            }
        }
        systemConstraintFieldValueMasterDao.deleteSystemConstraintFieldValueConfigsByUuids(systemConstraintFieldMasterDto.getFieldValueUuidsToBeRemoved());
        systemConstraintFieldValueMasterDao.flush();
//        this.updateMobileTemplate(formMasterUuid, fieldMasterUuid);
        return systemConstraintFieldMasterDao.getSystemConstraintFieldConfigByUuid(fieldMasterUuid);
    }

//    private void updateMobileTemplate(UUID formMasterUuid, UUID fieldMasterUuid) {
//        SystemConstraintFormVersion systemConstraintFormVersion
//                = systemConstraintFormVersionDao.getFormVersionByFormUuidAndType(formMasterUuid, SystemConstraintConstants.MOBILE_TYPE);
//        if (systemConstraintFormVersion.getTemplateConfig() != null) {
//            List<SystemConstraintMobileTemplatePage> rawTemplate = new GsonBuilder()
//                    .registerTypeAdapter(Date.class, MobileConstantUtil.jsonDateDeserializerToStringFormat).
//                    create().fromJson(systemConstraintFormVersion.getTemplateConfig(),
//                            new TypeToken<List<SystemConstraintMobileTemplatePage>>() {
//                            }.getType());
//
//            SystemConstraintFieldMasterDto systemConstraintFieldMasterDto
//                    = new GsonBuilder()
//                    .registerTypeAdapter(Date.class, MobileConstantUtil.jsonDateDeserializerToStringFormat).
//                    create().fromJson(systemConstraintFieldMasterDao.getSystemConstraintFieldConfigByUuid(fieldMasterUuid),
//                            new TypeToken<SystemConstraintFieldMasterDto>() {
//                            }.getType());
//
//            rawTemplate.forEach((page) -> {
//                page.getQuestions().forEach((question) -> {
//                    boolean willNextFieldEmpty = false;
//                    if (question.getUuid().compareTo(systemConstraintFieldMasterDto.getUuid()) == 0) {
//                        question.setFieldKey(systemConstraintFieldMasterDto.getFieldKey());
//                        question.setFieldName(systemConstraintFieldMasterDto.getFieldName());
//                        if (!question.getFieldType().equals(systemConstraintFieldMasterDto.getFieldType())) {
//                            willNextFieldEmpty = true;
//                        }
//                        question.setFieldType(systemConstraintFieldMasterDto.getFieldType());
//                        question.setNgModel(systemConstraintFieldMasterDto.getNgModel());
//                        question.setAppName(systemConstraintFieldMasterDto.getAppName());
//                        question.setStandardFieldMasterUuid(
//                                systemConstraintFieldMasterDto.getStandardFieldMasterUuid());
//                        if (checkIFFieldValueIsChange(
//                                question.getSystemConstraintFieldValueMasterDtos(),
//                                systemConstraintFieldMasterDto.getSystemConstraintFieldValueMasterDtos())
//                        ) {
//                            willNextFieldEmpty = true;
//                        }
//                        question.setSystemConstraintFieldValueMasterDtos(
//                                systemConstraintFieldMasterDto.getSystemConstraintFieldValueMasterDtos()
//                        );
//                        if (willNextFieldEmpty) {
//                            question.setNextField(null);
//                            question.setNextFieldJson(null);
//                            question.setNextFieldBy("BY_FIELD");
//                        }
//                    }
//                });
//            });
//            String toJson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").
//                    create().toJson(rawTemplate);
//            this.createOrUpdateMobileFormVersion(formMasterUuid, toJson);
//        }
//    }

    private boolean checkIFFieldValueIsChange(List<SystemConstraintFieldValueMasterDto> rawFieldValues, List<SystemConstraintFieldValueMasterDto> originalValues) {
        boolean isFieldValueChange = false;
        List<String> keys = new ArrayList<>();
        keys.add("optionsType");
        keys.add("staticOptions");
        keys.add("listValueField");
        keys.add("queryBuilder");
        keys.add("additionalStaticOptionsRequired");
        for (SystemConstraintFieldValueMasterDto rawFieldValue : rawFieldValues) {
            for (SystemConstraintFieldValueMasterDto originalValue : originalValues) {
                if (rawFieldValue.getKey().equals(originalValue.getKey()) &&
                        keys.contains(rawFieldValue.getKey())) {
                    if (!rawFieldValue.getDefaultValue().
                            equals(originalValue.getDefaultValue())) {
                        isFieldValueChange = true;
                        break;
                    }
                }
            }
            if (isFieldValueChange) {
                break;
            }
        }
        return isFieldValueChange;
    }

    @Override
    public void deleteSystemConstraintFieldConfig(UUID uuid) {
        systemConstraintFieldMasterDao.deleteSystemConstraintFieldConfigByUuid(uuid);
    }

    @Override
    public Map<String, Object> getSystemConstraintWebTemplateConfigsByMenuConfigId(Integer menuConfigId) {
        Map<String, Object> webTemplateConfigMap = new HashMap<>();
        List<SystemConstraintFormMasterDto> systemConstraintFormMasterDtos = this.getSystemConstraintForms(menuConfigId);
        for (SystemConstraintFormMasterDto systemConstraintFormMasterDto : systemConstraintFormMasterDtos) {
            webTemplateConfigMap.put(systemConstraintFormMasterDto.getFormName(), new Gson().fromJson(systemConstraintFormMasterDto.getWebTemplateConfig(), Object.class));
        }
        return webTemplateConfigMap;
    }

    @Override
    public List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFields(Boolean fetchOnlyActive) {
        return systemConstraintFormMasterDao.getSystemConstraintStandardFields(fetchOnlyActive);
    }

    @Override
    public SystemConstraintStandardFieldMaster getSystemConstraintStandardFieldByUuid(UUID uuid) {
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq(SystemConstraintStandardFieldMaster.Fields.UUID, uuid));
        SystemConstraintStandardFieldMaster systemConstraintStandardFieldMaster = systemConstraintStandardFieldMasterDao.findEntityByCriteriaList(criterions);
        if (systemConstraintStandardFieldMaster == null) {
            throw new ImtechoUserException("Standard Field not found!", 0);
        }
        return systemConstraintStandardFieldMaster;
    }

    @Override
    public String getSystemConstraintStandardConfigById(Integer id) {
        return systemConstraintFormMasterDao.getSystemConstraintStandardConfigById(id);
    }

    @Override
    public void createOrUpdateSystemConstraintStandardField(SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto) {
        SystemConstraintStandardFieldMaster systemConstraintStandardFieldMaster = SystemConstraintStandardFieldMasterMapper.mapDtoToModel(systemConstraintStandardFieldMasterDto);
        UUID standardFieldMasterUuid = systemConstraintStandardFieldMaster.getUuid();
        if (standardFieldMasterUuid != null) {
            systemConstraintStandardFieldMasterDao.update(systemConstraintStandardFieldMaster);
        } else {
            systemConstraintStandardFieldMaster.setUuid(UUID.randomUUID());
            standardFieldMasterUuid = systemConstraintStandardFieldMasterDao.create(systemConstraintStandardFieldMaster);

            QueryDto queryDto = new QueryDto();
            queryDto.setCode("retrieve_list_values_by_field_key");
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("fieldKey", "system_codes_supported_types");
            queryDto.setParameters(parameters);
            List<QueryDto> queryDtos = new LinkedList<>();
            queryDtos.add(queryDto);
            List<QueryDto> resultQueryDto = queryMasterService.execute(queryDtos, true);
            List<LinkedHashMap<String, Object>> standards = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> standard : standards) {
                systemConstraintStandardFieldMappingMasterDao.create(new SystemConstraintStandardFieldMappingMaster(UUID.randomUUID(), (Integer) standard.get("id"), standardFieldMasterUuid));
            }
        }
    }

    @Override
    public String createOrUpdateSystemConstraintStandardFieldConfig(SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto) {
        List<SystemConstraintStandardFieldValueMasterDto> systemConstraintStandardFieldValueMasterDtoList = systemConstraintStandardFieldMasterDto.getSystemConstraintStandardFieldValueMasterDtos();
        systemConstraintStandardFieldValueMasterDao.createOrUpdateAll(SystemConstraintStandardFieldValueMasterMapper.mapDtoListToModelList(systemConstraintStandardFieldValueMasterDtoList, systemConstraintStandardFieldMasterDto.getStandardFieldMappingMasterUuid()));
        for (SystemConstraintStandardFieldValueMasterDto systemConstraintStandardFieldValueMasterDto : systemConstraintStandardFieldValueMasterDtoList) {
            if (("label").equals(systemConstraintStandardFieldValueMasterDto.getKey())) {
                if (("ALL").equals(systemConstraintStandardFieldMasterDto.getAppName())) {
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", "WEB");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", "TECHO_MOBILE_APP");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getDefaultValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", "WEB");
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getDefaultValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", "TECHO_MOBILE_APP");
                } else {
                    String appName = ("TECHO_APP").equals(systemConstraintStandardFieldMasterDto.getAppName()) ? "TECHO_MOBILE_APP" : "WEB";
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfLabel(), "EN", "IN", appName);
                    internationalizationLabelDao.createOrUpdateLabel(systemConstraintStandardFieldValueMasterDto.getDefaultValue(), systemConstraintStandardFieldValueMasterDto.getEnTranslationOfDefaultLabel(), "EN", "IN", appName);
                }
                internationalizationLabelDao.flush();
            }
        }
        systemConstraintStandardFieldValueMasterDao.deleteSystemConstraintStandardFieldValueConfigsByUuids(systemConstraintStandardFieldMasterDto.getStandardFieldValueUuidsToBeRemoved());
        systemConstraintStandardFieldValueMasterDao.flush();
        return systemConstraintStandardFieldMasterDao.getSystemConstraintFieldConfigByUuid(systemConstraintStandardFieldMasterDto.getStandardFieldMappingMasterUuid());
    }

    @Override
    public List<SystemConstraintMobileTemplateConfigDto> getMobileTemplateConfig(String formCode) {
        return systemConstraintFormMasterDao.getMobileTemplateConfig(formCode);
    }

    @Override
    public List<String> getActiveMobileForms() {
        return systemConstraintFormMasterDao.getActiveMobileForms();

    }

    @Override
    public String getSystemConstraintConfigsByMenuConfigId(Integer menuConfigId) {
        return systemConstraintFormMasterDao.getSystemConstraintConfigsByMenuConfigId(menuConfigId);
    }
}
