/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.mapper;

import com.argusoft.medplat.notification.model.TechoWebNotificationMaster;
import com.argusoft.medplat.notification.model.TechoWebNotificationResponseDetail;

/**
 *
 * <p>
 *     Mapper for techo web notification response in order to convert dto to model or model to dto.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public class TechoWebNotificationResponseDetailMapper {
    private TechoWebNotificationResponseDetailMapper(){
    }

    /**
     * Convert techo web notification response details into entity.
     * @param notificationMaster Entity of notification master.
     * @param actionTaken Which action taken.
     * @param otherDetail Other details.
     * @param toState Define state.
     * @return Returns techo web notification response details.
     */
    public static TechoWebNotificationResponseDetail convertToTechoWebNotificationResposeDetailEntity(TechoWebNotificationMaster notificationMaster,
            String actionTaken, String otherDetail, TechoWebNotificationMaster.State toState) {
        TechoWebNotificationResponseDetail techoWebNotificationResponseDetail = new TechoWebNotificationResponseDetail();
        techoWebNotificationResponseDetail.setNotificationId(notificationMaster.getId());
        techoWebNotificationResponseDetail.setNotificationTypeId(notificationMaster.getNotificationTypeId());
        techoWebNotificationResponseDetail.setNotificationTypeEscalationId(notificationMaster.getNotificationTypeEscalationId());
        techoWebNotificationResponseDetail.setLocationId(notificationMaster.getLocationId());
        techoWebNotificationResponseDetail.setScheduleDate(notificationMaster.getScheduleDate());
        techoWebNotificationResponseDetail.setDueOn(notificationMaster.getDueOn());
        techoWebNotificationResponseDetail.setExpiryDate(notificationMaster.getExpiryDate());
        techoWebNotificationResponseDetail.setFromState(notificationMaster.getState());
        techoWebNotificationResponseDetail.setRefCode(notificationMaster.getRefCode());

        techoWebNotificationResponseDetail.setToState(toState);
        techoWebNotificationResponseDetail.setOtherDetails(otherDetail);
        techoWebNotificationResponseDetail.setActionTaken(actionTaken);
        return techoWebNotificationResponseDetail;
    }

}
