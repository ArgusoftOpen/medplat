package com.argusoft.medplat.web.ddb.mapper;

import com.argusoft.medplat.web.ddb.dto.DerivedAttributeDto;
import com.argusoft.medplat.web.ddb.dto.IndicatorMasterDto;
import com.argusoft.medplat.web.ddb.dto.DatasetMasterDto;
import com.argusoft.medplat.web.ddb.model.DerivedAttribute;
import com.argusoft.medplat.web.ddb.model.IndicatorMaster;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An util class for ddb to convert dto to modal or modal to dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
public class DdbMapper {

    private DdbMapper() {}

    /**
     * Converts derived attribute entity to dto
     * @param derivedAttribute An instance of DerivedAttribute
     * @return An instance of DerivedAttributeDto
     */
    public static DerivedAttributeDto convertDerivedAttributeToDto(DerivedAttribute derivedAttribute) {
        DerivedAttributeDto dto = new DerivedAttributeDto();
        if (derivedAttribute != null) {
            dto.setId(derivedAttribute.getId());
            dto.setDerivedName(derivedAttribute.getDerivedName());
            dto.setFormula(derivedAttribute.getFormula());
            dto.setResult(derivedAttribute.getResult());
            dto.setCreatedBy(derivedAttribute.getCreatedBy());
            dto.setCreatedOn(derivedAttribute.getCreatedOn());
            dto.setModifiedBy(derivedAttribute.getModifiedBy());
            dto.setModifiedOn(derivedAttribute.getModifiedOn());
        }
        return dto;
    }

    /**
     * Converts derived attribute entity to map for API response
     * @param derivedAttribute An instance of DerivedAttribute
     * @return A map representation
     */
    public static Map<String, Object> convertDerivedAttributeToMap(DerivedAttribute derivedAttribute) {
        Map<String, Object> map = new HashMap<>();
        if (derivedAttribute != null) {
            map.put("id", derivedAttribute.getId());
            map.put("derived_name", derivedAttribute.getDerivedName());
            map.put("formula", derivedAttribute.getFormula());
            map.put("result", derivedAttribute.getResult());
            map.put("created_by", derivedAttribute.getCreatedBy());
            map.put("created_on", derivedAttribute.getCreatedOn());
            map.put("modified_by", derivedAttribute.getModifiedBy());
            map.put("modified_on", derivedAttribute.getModifiedOn());
        }
        return map;
    }

    /**
     * Converts indicator master entity to dto
     * @param indicatorMaster An instance of IndicatorMaster
     * @return An instance of IndicatorMasterDto
     */
    public static IndicatorMasterDto convertIndicatorMasterToDto(IndicatorMaster indicatorMaster) {
        IndicatorMasterDto dto = new IndicatorMasterDto();
        if (indicatorMaster != null) {
            dto.setId(indicatorMaster.getId());
            dto.setIndicatorName(indicatorMaster.getIndicatorName());
            dto.setDescription(indicatorMaster.getDescription());
            dto.setSqlQuery(indicatorMaster.getSqlQuery());
            dto.setQueryResult(indicatorMaster.getQueryResult());
            dto.setCreatedBy(indicatorMaster.getCreatedBy());
            dto.setCreatedOn(indicatorMaster.getCreatedOn());
            dto.setModifiedBy(indicatorMaster.getModifiedBy());
            dto.setModifiedOn(indicatorMaster.getModifiedOn());
        }
        return dto;
    }

    /**
     * Converts indicator master entity to map for API response
     * @param indicatorMaster An instance of IndicatorMaster
     * @return A map representation
     */
    public static Map<String, Object> convertIndicatorMasterToMap(IndicatorMaster indicatorMaster) {
        Map<String, Object> map = new HashMap<>();
        if (indicatorMaster != null) {
            map.put("id", indicatorMaster.getId());
            map.put("indicator_name", indicatorMaster.getIndicatorName());
            map.put("description", indicatorMaster.getDescription());
            map.put("sql_query", indicatorMaster.getSqlQuery());
            map.put("query_result", indicatorMaster.getQueryResult());
            map.put("created_by", indicatorMaster.getCreatedBy());
            map.put("created_on", indicatorMaster.getCreatedOn());
            map.put("modified_by", indicatorMaster.getModifiedBy());
            map.put("modified_on", indicatorMaster.getModifiedOn());
        }
        return map;
    }

    /**
     * Converts dataset master entity to dto
     * @param datasetMaster An instance of DatasetMaster
     * @return An instance of DatasetMasterDto
     */
    public static DatasetMasterDto convertDatasetMasterToDto(DatasetMaster datasetMaster) {
        DatasetMasterDto dto = new DatasetMasterDto();
        if (datasetMaster != null) {
            dto.setId(datasetMaster.getId());
            dto.setDatasetName(datasetMaster.getDatasetName());
            dto.setSqlQuery(datasetMaster.getSqlQuery());
            dto.setCreatedBy(datasetMaster.getCreatedBy());
            dto.setCreatedOn(datasetMaster.getCreatedOn());
            dto.setModifiedBy(datasetMaster.getModifiedBy());
            dto.setModifiedOn(datasetMaster.getModifiedOn());
        }
        return dto;
    }

    /**
     * Converts dataset master entity to map for API response
     * @param datasetMaster An instance of DatasetMaster
     * @return A map representation
     */
    public static Map<String, Object> convertDatasetMasterToMap(DatasetMaster datasetMaster) {
        Map<String, Object> map = new HashMap<>();
        if (datasetMaster != null) {
            map.put("id", datasetMaster.getId());
            map.put("dataset_name", datasetMaster.getDatasetName());
            map.put("sql_query", datasetMaster.getSqlQuery());
            map.put("created_by", datasetMaster.getCreatedBy());
            map.put("created_on", datasetMaster.getCreatedOn());
            map.put("modified_by", datasetMaster.getModifiedBy());
            map.put("modified_on", datasetMaster.getModifiedOn());
        }
        return map;
    }

    /**
     * Converts list of derived attributes to list of maps
     * @param derivedAttributes A list of DerivedAttribute
     * @return A list of maps
     */
    public static List<Map<String, Object>> convertDerivedAttributesToMaps(List<DerivedAttribute> derivedAttributes) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (derivedAttributes != null) {
            for (DerivedAttribute derivedAttribute : derivedAttributes) {
                maps.add(convertDerivedAttributeToMap(derivedAttribute));
            }
        }
        return maps;
    }

    /**
     * Converts list of indicator masters to list of maps
     * @param indicatorMasters A list of IndicatorMaster
     * @return A list of maps
     */
    public static List<Map<String, Object>> convertIndicatorMastersToMaps(List<IndicatorMaster> indicatorMasters) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (indicatorMasters != null) {
            for (IndicatorMaster indicatorMaster : indicatorMasters) {
                maps.add(convertIndicatorMasterToMap(indicatorMaster));
            }
        }
        return maps;
    }

    /**
     * Converts list of dataset masters to list of maps
     * @param datasetMasters A list of DatasetMaster
     * @return A list of maps
     */
    public static List<Map<String, Object>> convertDatasetMastersToMaps(List<DatasetMaster> datasetMasters) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (datasetMasters != null) {
            for (DatasetMaster datasetMaster : datasetMasters) {
                maps.add(convertDatasetMasterToMap(datasetMaster));
            }
        }
        return maps;
    }
}
