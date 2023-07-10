/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.upload.location.mapper;

import com.argusoft.medplat.mobile.model.MobileLibraryMaster;
import com.argusoft.medplat.upload.location.dto.MobileLibraryDto;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * <p>
 *     Mapper for mobile library document in order to convert dto to model or model to dto.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class MobileLibraryDocumentMapper {
    private MobileLibraryDocumentMapper(){
    }

    /**
     * Convert mobile library document details into entity.
     * @param mobileLibraryDto Details of mobile library document.
     * @return Returns entity of mobile library document.
     */
    public static MobileLibraryMaster convertMobileLibraryDocumentToMobileLibraryMaster(MobileLibraryDto mobileLibraryDto) {

        MobileLibraryMaster mobileLibraryMaster = new MobileLibraryMaster();
        mobileLibraryMaster.setCategory(mobileLibraryDto.getCategory());
        mobileLibraryMaster.setFileName(mobileLibraryDto.getFileName());
        mobileLibraryMaster.setFileType(mobileLibraryDto.getFileType());
        mobileLibraryMaster.setDescription(mobileLibraryDto.getDescription());
        mobileLibraryMaster.setState(mobileLibraryDto.getState());
        mobileLibraryMaster.setTag(mobileLibraryDto.getTag());

        return mobileLibraryMaster;
    }

    /**
     * Convert list of mobile library document entities into dto list.
     * @param mobileLibraryMasterList List of mobile library document entities.
     * @return Returns list of mobile library document details.
     */
    public static List<MobileLibraryDto> convertMasterListToDtoList(List<MobileLibraryMaster> mobileLibraryMasterList) {

        List<MobileLibraryDto> mobileLibraryDtos = new LinkedList<>();
        if (!CollectionUtils.isEmpty(mobileLibraryMasterList)) {
            for (MobileLibraryMaster mobileLibraryMaster : mobileLibraryMasterList) {
                MobileLibraryDto mobileLibraryDto = new MobileLibraryDto();
                mobileLibraryDto.setCategory(mobileLibraryMaster.getCategory());
                mobileLibraryDto.setFileName(mobileLibraryMaster.getFileName());
                mobileLibraryDto.setDescription(mobileLibraryMaster.getDescription());
                mobileLibraryDto.setState(mobileLibraryMaster.getState());
                mobileLibraryDto.setId(mobileLibraryMaster.getId());
                mobileLibraryDto.setFileType(mobileLibraryMaster.getFileType());
                mobileLibraryDto.setTag(mobileLibraryMaster.getTag());
                mobileLibraryDtos.add((mobileLibraryDto));

            }
        }
        return mobileLibraryDtos;

    }
}
