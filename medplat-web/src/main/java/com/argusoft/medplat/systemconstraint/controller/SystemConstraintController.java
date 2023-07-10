package com.argusoft.medplat.systemconstraint.controller;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.mobile.dto.ComponentTagDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFieldMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintFormMasterDto;
import com.argusoft.medplat.systemconstraint.dto.SystemConstraintStandardFieldMasterDto;
import com.argusoft.medplat.systemconstraint.mapper.SystemConstraintComponentTagDtoMapper;
import com.argusoft.medplat.systemconstraint.model.SystemConstraintStandardFieldMaster;
import com.argusoft.medplat.systemconstraint.service.SystemConstraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/systemConstraint")
public class SystemConstraintController {

    @Autowired
    SystemConstraintService systemConstraintService;

    @Autowired
    ImtechoSecurityUser user;

    @RequestMapping(value = "/forms", method = RequestMethod.GET)
    public List<SystemConstraintFormMasterDto> getSystemConstraintForms() {
        return systemConstraintService.getSystemConstraintForms(null);
    }

    @RequestMapping(value = "/forms/{menuConfigId}", method = RequestMethod.GET)
    public List<SystemConstraintFormMasterDto> getSystemConstraintFormsByMenuConfigId(@PathVariable Integer menuConfigId) {
        return systemConstraintService.getSystemConstraintForms(menuConfigId);
    }

    @RequestMapping(value = "/form/{uuid}", method = RequestMethod.GET)
    public SystemConstraintFormMasterDto getSystemConstraintFormByUuid(@PathVariable UUID uuid) {
        return systemConstraintService.getSystemConstraintFormByUuid(uuid);
    }

    @RequestMapping(value = "/formConfig/{uuid}", method = RequestMethod.GET)
    public String getSystemConstraintFormConfigByUuid(@PathVariable UUID uuid, @RequestParam(required = false) String appName) {
        return systemConstraintService.getSystemConstraintFormConfigByUuid(uuid, appName);
    }

    @RequestMapping(value = "/fields/{formMasterUuid}", method = RequestMethod.GET)
    public List<SystemConstraintFieldMasterDto> getSystemConstraintFieldsByFormMasterUuid(@PathVariable UUID formMasterUuid) {
        return systemConstraintService.getSystemConstraintFieldsByFormMasterUuid(formMasterUuid);
    }

    @RequestMapping(value = "/form/{type}", method = RequestMethod.POST)
    public SystemConstraintFormMasterDto createOrUpdateSystemConstraintForm(@PathVariable String type,
                                                                            @RequestBody SystemConstraintFormMasterDto systemConstraintFormMasterDto) {
        return systemConstraintService.createOrUpdateSystemConstraintForm(systemConstraintFormMasterDto, type);
    }

    @RequestMapping(value = "/fieldConfig/{formMasterUuid}", method = RequestMethod.POST)
    public String createOrUpdateSystemConstraintFieldConfig(@PathVariable UUID formMasterUuid, @RequestBody SystemConstraintFieldMasterDto systemConstraintFieldMasterDto) {
        return systemConstraintService.createOrUpdateSystemConstraintFieldConfig(formMasterUuid, systemConstraintFieldMasterDto);
    }

    @RequestMapping(value = "/fieldConfig/{uuid}", method = RequestMethod.DELETE)
    public void deleteSystemConstraintFieldConfig(@PathVariable UUID uuid) {
        systemConstraintService.deleteSystemConstraintFieldConfig(uuid);
    }

    @RequestMapping(value = "/webTemplateConfigs/{menuConfigId}", method = RequestMethod.GET)
    public Map<String, Object> getSystemConstraintWebTemplateConfigsByMenuConfigId(@PathVariable Integer menuConfigId) {
        return systemConstraintService.getSystemConstraintWebTemplateConfigsByMenuConfigId(menuConfigId);
    }

    @RequestMapping(value = "/standard/fields", method = RequestMethod.GET)
    public List<SystemConstraintStandardFieldMasterDto> getSystemConstraintStandardFields(@RequestParam(name = "fetchOnlyActive") Boolean fetchOnlyActive) {
        return systemConstraintService.getSystemConstraintStandardFields(fetchOnlyActive);
    }

    @RequestMapping(value = "/standard/field/{uuid}", method = RequestMethod.GET)
    public SystemConstraintStandardFieldMaster getSystemConstraintStandardFieldByUuid(@PathVariable UUID uuid) {
        return systemConstraintService.getSystemConstraintStandardFieldByUuid(uuid);
    }

    @RequestMapping(value = "/standardConfig/{id}", method = RequestMethod.GET)
    public String getSystemConstraintStandardConfigById(@PathVariable Integer id) {
        return systemConstraintService.getSystemConstraintStandardConfigById(id);
    }

    @RequestMapping(value = "/standard/field", method = RequestMethod.POST)
    public void createOrUpdateSystemConstraintStandardField(@RequestBody SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto) {
        systemConstraintService.createOrUpdateSystemConstraintStandardField(systemConstraintStandardFieldMasterDto);
    }

    @RequestMapping(value = "/standard/fieldConfig", method = RequestMethod.POST)
    public String createOrUpdateSystemConstraintStandardFieldConfig(@RequestBody SystemConstraintStandardFieldMasterDto systemConstraintStandardFieldMasterDto) {
        return systemConstraintService.createOrUpdateSystemConstraintStandardFieldConfig(systemConstraintStandardFieldMasterDto);
    }

    @RequestMapping(value = "/mobileTemplateConfig/{formCode}", method = RequestMethod.GET)
    public List<ComponentTagDto> getMobileTemplateConfig(@PathVariable String formCode) {
        return SystemConstraintComponentTagDtoMapper.
                mapToComponentTagDtoList(systemConstraintService.getMobileTemplateConfig(formCode));
    }


    @RequestMapping(value = "/configs/{menuConfigId}", method = RequestMethod.GET)
    public String getSystemConstraintConfigsByMenuConfigId(@PathVariable Integer menuConfigId) {
        return systemConstraintService.getSystemConstraintConfigsByMenuConfigId(menuConfigId);
    }
}
