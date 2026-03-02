package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.SicklecellScreeningDto;
import com.argusoft.medplat.rch.model.SicklecellScreening;

/**
 * <p>
 * Mapper for sickle cell screening in order to convert dto to model or model to dto.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 11:00 AM
 */
public class SicklecellScreeningMapper {
    private SicklecellScreeningMapper() {
    }

    /**
     * Convert sickle cell screening details into entity.
     *
     * @param sicklecellScreeningDto Sickle cell screening details.
     * @return Returns entity of sickle cell screening.
     */
    public static SicklecellScreening convertDtoToEntity(SicklecellScreeningDto sicklecellScreeningDto) {
        SicklecellScreening sicklecellScreening = new SicklecellScreening();
        sicklecellScreening.setMemberId(sicklecellScreeningDto.getMemberId());
        sicklecellScreening.setLocationId(sicklecellScreeningDto.getLocationId());
        sicklecellScreening.setAnemiaTestDone(sicklecellScreeningDto.getAnemiaTestDone());
        sicklecellScreening.setDttTestResult(sicklecellScreeningDto.getDttTestResult());
        sicklecellScreening.setHplcTestDone(sicklecellScreeningDto.getHplcTestDone());
        sicklecellScreening.setHplcTestResult(sicklecellScreeningDto.getHplcTestResult());
        return sicklecellScreening;
    }

    /**
     * Convert sickle cell screening entity into dto.
     *
     * @param sicklecellScreening Entity of sickle cell screening.
     * @return Returns sickle cell screening details.
     */
    public static SicklecellScreeningDto convertEntityToDto(SicklecellScreening sicklecellScreening) {
        SicklecellScreeningDto sicklecellScreeningDto = new SicklecellScreeningDto();
        sicklecellScreeningDto.setId(sicklecellScreening.getId());
        sicklecellScreeningDto.setMemberId(sicklecellScreening.getMemberId());
        sicklecellScreeningDto.setLocationId(sicklecellScreening.getLocationId());
        sicklecellScreeningDto.setAnemiaTestDone(sicklecellScreening.getAnemiaTestDone());
        sicklecellScreeningDto.setDttTestResult(sicklecellScreening.getDttTestResult());
        sicklecellScreeningDto.setHplcTestDone(sicklecellScreening.getHplcTestDone());
        sicklecellScreeningDto.setHplcTestResult(sicklecellScreening.getHplcTestResult());
        return sicklecellScreeningDto;
    }
}
