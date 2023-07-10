package com.argusoft.medplat.mobile.mapper;

import com.argusoft.medplat.mobile.dto.MobileLibraryDataBean;
import com.argusoft.medplat.mobile.model.MobileLibraryMaster;

/**
 * @author prateek on 12 Feb, 2019
 */
public class MobileLibraryDataBeanMapper {

    private MobileLibraryDataBeanMapper() {
        throw new IllegalStateException("Utility Class");
    }

    public static MobileLibraryDataBean convertMobileLibraryMasterToLibraryDataBean(MobileLibraryMaster mobileLibraryMaster) {

        MobileLibraryDataBean mobileLibraryDataBean = new MobileLibraryDataBean();
        mobileLibraryDataBean.setActualId(mobileLibraryMaster.getId());
        mobileLibraryDataBean.setCategory(mobileLibraryMaster.getCategory());
        mobileLibraryDataBean.setFileName(mobileLibraryMaster.getFileName());
        mobileLibraryDataBean.setFileType(mobileLibraryMaster.getFileType());
        mobileLibraryDataBean.setDescription(mobileLibraryMaster.getDescription());
        mobileLibraryDataBean.setState(mobileLibraryMaster.getState());
        mobileLibraryDataBean.setModifiedOn(mobileLibraryMaster.getModifiedOn().getTime());
        mobileLibraryDataBean.setTag(mobileLibraryMaster.getTag());
        return mobileLibraryDataBean;
    }
}