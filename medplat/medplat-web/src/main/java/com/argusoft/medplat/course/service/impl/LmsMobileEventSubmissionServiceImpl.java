package com.argusoft.medplat.course.service.impl;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.util.EmailUtil;
import com.argusoft.medplat.course.constants.LmsMobileEventConstants;
import com.argusoft.medplat.course.dto.LmsMobileEventDto;
import com.argusoft.medplat.course.model.LmsMobileEventMaster;
import com.argusoft.medplat.course.service.LmsMobileEventService;
import com.argusoft.medplat.course.service.LmsMobileEventSubmissionService;
import com.argusoft.medplat.course.service.LmsUserMetaDataService;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dto.RecordStatusBean;
import com.argusoft.medplat.mobile.service.MobileFhsService;
//import com.argusoft.medplat.mobile.service.impl.FormSubmissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

@Service
public class LmsMobileEventSubmissionServiceImpl implements LmsMobileEventSubmissionService {

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private LmsMobileEventService mobileEventService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private LmsUserMetaDataService userMetaDataService;

    @Override
    public List<RecordStatusBean> storeLmsMobileEventToDB(String token, List<LmsMobileEventDto> mobileEventDtos) {
        if (mobileEventDtos == null || mobileEventDtos.isEmpty()) {
            return Collections.emptyList();
        }

        UserMaster user = mobileFhsService.isUserTokenValid(token);
        if (user == null) {
            return Collections.emptyList();
        }

        List<RecordStatusBean> recordStatusBeanList = new ArrayList<>();
        List<LmsMobileEventDto> pendingMobileEventDtos = new ArrayList<>();

        createLmsMobileEventMaster(mobileEventDtos, recordStatusBeanList, pendingMobileEventDtos, user);
        processPendingLmsMobileEvent(user, pendingMobileEventDtos, recordStatusBeanList);

        return recordStatusBeanList;
    }

    private void createLmsMobileEventMaster(
            List<LmsMobileEventDto> mobileEventDtos, List<RecordStatusBean> recordStatusBeanList,
            List<LmsMobileEventDto> pendingMobileEventDtos, UserMaster user) {
        for (LmsMobileEventDto dto : mobileEventDtos) {
            LmsMobileEventMaster lmsMobileEventMaster = mobileEventService.retrieveMobileEventMaster(dto.getChecksum());
            if (lmsMobileEventMaster != null &&
                    (lmsMobileEventMaster.getStatus().equalsIgnoreCase(MobileConstantUtil.SUCCESS_VALUE)
                            || lmsMobileEventMaster.getStatus().equalsIgnoreCase(MobileConstantUtil.ERROR_VALUE))) {
                RecordStatusBean recordStatusBean = new RecordStatusBean();
                recordStatusBean.setChecksum(dto.getChecksum());
                recordStatusBean.setStatus(lmsMobileEventMaster.getStatus());
                recordStatusBeanList.add(recordStatusBean);
            } else if (lmsMobileEventMaster != null && lmsMobileEventMaster.getStatus().equalsIgnoreCase(MobileConstantUtil.PENDING_VALUE)) {
                RecordStatusBean recordStatusBean = new RecordStatusBean();
                recordStatusBean.setChecksum(dto.getChecksum());
                recordStatusBean.setStatus(lmsMobileEventMaster.getStatus());
                recordStatusBeanList.add(recordStatusBean);
                lmsMobileEventMaster.setStatus(MobileConstantUtil.PROCESSING_VALUE);
                mobileEventService.createOrUpdateLmsMobileEventMaster(lmsMobileEventMaster);
                pendingMobileEventDtos.add(dto); // remove this
            } else if (lmsMobileEventMaster != null && lmsMobileEventMaster.getStatus().equalsIgnoreCase(MobileConstantUtil.PROCESSING_VALUE)) {
                RecordStatusBean recordStatusBean = new RecordStatusBean();
                recordStatusBean.setChecksum(dto.getChecksum());
                recordStatusBean.setStatus(MobileConstantUtil.PENDING_VALUE);
                recordStatusBeanList.add(recordStatusBean);
            } else {
                lmsMobileEventMaster = new LmsMobileEventMaster();
                lmsMobileEventMaster.setChecksum(dto.getChecksum());
                lmsMobileEventMaster.setActionDate(new Date());
                lmsMobileEventMaster.setStatus(MobileConstantUtil.PROCESSING_VALUE);
                lmsMobileEventMaster.setEventData(dto.getEventData());
                lmsMobileEventMaster.setEventType(dto.getEventType());
                lmsMobileEventMaster.setUserId(user.getId());
                mobileEventService.createOrUpdateLmsMobileEventMaster(lmsMobileEventMaster);
                pendingMobileEventDtos.add(dto);
            }
        }
    }

    private void processPendingLmsMobileEvent(UserMaster user, List<LmsMobileEventDto> pendingMobileEventDtos, List<RecordStatusBean> recordStatusBeanList) {
        for (LmsMobileEventDto dto : pendingMobileEventDtos) {
            LmsMobileEventMaster lmsMobileEventMaster;
            Integer createdInstanceId = null;
            String errorMessage = null;
            String exception = null;
            lmsMobileEventMaster = mobileEventService.retrieveMobileEventMaster(dto.getChecksum());
            try {
                createdInstanceId = saveLmsMobileEventData(user, dto);
            } catch (Exception e) {
                String errorString = "ERROR LMS EVENT - " + dto.getChecksum() + " - " + dto.getEventType() + " - " + dto.getEventData();
                getLogger(getClass().getSimpleName()).log(Level.SEVERE, errorString, e);
                if (lmsMobileEventMaster.getMailSent() != null && !lmsMobileEventMaster.getMailSent()) {
                    emailUtil.sendLmsEventExceptionEmail(e, dto);
                    lmsMobileEventMaster.setMailSent(Boolean.TRUE);
                }
                errorMessage = "System Exception";
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                exception = writer.toString();
//                getLogger(FormSubmissionServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            }

            //  Prepare Record Status
            RecordStatusBean recordStatusBean = new RecordStatusBean();
            recordStatusBean.setChecksum(dto.getChecksum());

            if (createdInstanceId != null) {
                recordStatusBean.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            } else {
                recordStatusBean.setStatus(MobileConstantUtil.PENDING_VALUE);
            }
            recordStatusBeanList.add(recordStatusBean);

            //  If SyncStatus Exist then update otherwise create new entry
            //  Update SyncStatus Entry
            lmsMobileEventMaster.setActionDate(new Date());

            if (dto.getMobileDate() != null) {
                lmsMobileEventMaster.setMobileDate(dto.getMobileDate());
            }

            if (createdInstanceId != null) {
                lmsMobileEventMaster.setStatus(MobileConstantUtil.SUCCESS_VALUE);
            } else {
                lmsMobileEventMaster.setStatus(MobileConstantUtil.PENDING_VALUE);
                lmsMobileEventMaster.setErrorMessage(errorMessage);
                lmsMobileEventMaster.setException(exception);
            }
            mobileEventService.createOrUpdateLmsMobileEventMaster(lmsMobileEventMaster);
        }
    }

    private Integer saveLmsMobileEventData(UserMaster user, LmsMobileEventDto lmsMobileEventDto) {
        switch (lmsMobileEventDto.getEventType()) {
            case LmsMobileEventConstants.LESSON_START_DATE:
                return userMetaDataService.storeLmsLessonStartDate(user, lmsMobileEventDto);
            case LmsMobileEventConstants.LESSON_END_DATE:
                return userMetaDataService.storeLmsLessonEndDate(user, lmsMobileEventDto);
            case LmsMobileEventConstants.LESSON_SESSION:
                return userMetaDataService.storeLmsLessonSession(user, lmsMobileEventDto);
            case LmsMobileEventConstants.LESSON_FEEDBACK:
                return userMetaDataService.storeLmsLessonFeedback(user, lmsMobileEventDto);
            case LmsMobileEventConstants.LESSON_COMPLETED:
                return userMetaDataService.storeLmsLessonCompleted(user, lmsMobileEventDto);
            case LmsMobileEventConstants.LESSON_PAUSED_ON:
                return userMetaDataService.storeLmsLessonPausedOn(user, lmsMobileEventDto);
            default:
                return null;
        }
    }

}
