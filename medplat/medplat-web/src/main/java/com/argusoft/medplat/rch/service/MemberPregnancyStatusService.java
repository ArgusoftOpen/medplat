package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.mobile.dto.LogInRequestParamDetailDto;
import com.argusoft.medplat.mobile.dto.LoggedInUserPrincipleDto;

/**
 * <p>
 * Define services for pregnancy status.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface MemberPregnancyStatusService {

    /**
     * Retrieves pregnancy status for mobile.
     *
     * @param data  Logged in user details.
     * @param param Logged in request params details.
     */
    void retrievePregnancyStatusForMobile(LoggedInUserPrincipleDto data, LogInRequestParamDetailDto param);
}
