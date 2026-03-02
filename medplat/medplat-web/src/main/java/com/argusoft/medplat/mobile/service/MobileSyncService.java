package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.dto.LogInRequestParamDetailDto;
import com.argusoft.medplat.mobile.dto.LoggedInUserPrincipleDto;
import com.argusoft.medplat.mobile.dto.MobileRequestParamDto;

import java.util.Map;

/**
 * <p>
 *     Defines methods for MobileSyncService
 * </p>
 *
 * @author rahul
 * @since 21/05/21 4:30 PM
 */
public interface MobileSyncService {

    /**
     * map of metadata assigned to user
     * @param mobileRequestParamDto request body data
     * @return map of metadata
     */
    Map<String, Boolean> getMetaData(MobileRequestParamDto mobileRequestParamDto);

    /**
     * generate response based on request body object
     * @param paramDetailDto request body object
     * @param apkVersion current app version
     * @return response object
     * @throws Exception throws exceptions if there is any error
     */
    LoggedInUserPrincipleDto getDetails(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) throws Exception;
}
