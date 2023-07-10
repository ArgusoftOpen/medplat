package com.argusoft.medplat.rch.mapper;

import com.argusoft.medplat.rch.dto.AshaReportedEventDataBean;
import com.argusoft.medplat.rch.model.AshaReportedEventMaster;

import java.util.Date;

/**
 * <p>
 * Mapper for asha report event details in order to convert dto to model or model to dto.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class AshaReportedEventMapper {
    private AshaReportedEventMapper() {
    }

    /**
     * Convert asha report event details into entity.
     *
     * @param dataBean Asha report event details.
     * @return Returns Asha report event entity.
     */
    public static AshaReportedEventMaster convertAshaReportedEventDataBeanToMaster(AshaReportedEventDataBean dataBean) {
        AshaReportedEventMaster master = new AshaReportedEventMaster();
        master.setEventType(dataBean.getEventType());
        master.setFamilyId(dataBean.getFamilyId());
        master.setMemberId(dataBean.getMemberId());
        master.setLocationId(dataBean.getLocationId());
        if (dataBean.getReportedOn() != null) {
            master.setReportedOn(new Date(dataBean.getReportedOn()));
        }
        return master;
    }
}
