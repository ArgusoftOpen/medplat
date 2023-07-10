/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.dto.LocationDto;
import com.argusoft.medplat.common.model.AnnouncementLocationDetail;
import com.argusoft.medplat.common.model.AnnouncementMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * An util class for announcement to convert modal to dto or dto to modal
 * </p>
 *
 * @author smeet
 * @since 26/08/2020 5:30
 */
public class AnnouncementMapper {

    /**
     * Converts announcement dto to modal
     *
     * @param announcementMasterDto An instance of AnnouncementMasterDto
     * @return An instance of An instance of
     */
    public static AnnouncementMaster convertAnnouncementDtoToMaster(AnnouncementMasterDto announcementMasterDto) {
        AnnouncementMaster announcementMaster = new AnnouncementMaster();
        announcementMaster.setSubject(announcementMasterDto.getSubject());
        announcementMaster.setFromDate(announcementMasterDto.getFromDate());
        announcementMaster.setIsActive(announcementMasterDto.getIsActive());
        announcementMaster.setDefaultLanguage(announcementMasterDto.getDefaultLanguage());
        announcementMaster.setContainsMultimedia(announcementMasterDto.getContainsMultimedia());
        return announcementMaster;
    }

    /**
     * Converts a announcement modal to announcement dto
     *
     * @param announcementMaster An instance of AnnouncementMaster
     * @return An instance of AnnouncementMasterDto
     */
    public static AnnouncementMasterDto convertAnnouncementMasterToDto(AnnouncementMaster announcementMaster) {
        AnnouncementMasterDto announcementMasterDto = new AnnouncementMasterDto();
        announcementMasterDto.setId(announcementMaster.getId());
        announcementMasterDto.setSubject(announcementMaster.getSubject());
        announcementMasterDto.setFromDate(announcementMaster.getFromDate());
        announcementMasterDto.setIsActive(announcementMaster.getIsActive());
        announcementMasterDto.setDefaultLanguage(announcementMaster.getDefaultLanguage());
        announcementMasterDto.setCreatedBy(announcementMaster.getCreatedBy());
        announcementMasterDto.setCreatedOn(announcementMaster.getCreatedOn());
        return announcementMasterDto;
    }

    /**
     * Converts a list of announcement location modal to list of location dto
     *
     * @param announcementLocationDetailList A list of AnnouncementLocationDetail
     * @return A list of LocationDto
     */
    public static List<LocationDto> convertLocationDetailToDto(List<AnnouncementLocationDetail> announcementLocationDetailList) {
        List<LocationDto> locationDtoList = new ArrayList<>();
        for (AnnouncementLocationDetail announcementLocationDetail : announcementLocationDetailList) {
            LocationDto locationDto = new LocationDto();
            locationDto.setLocationId(announcementLocationDetail.getAnnouncementLocationDetailPKey().getLocation());
            locationDto.setType(announcementLocationDetail.getLocationType());
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }
}
