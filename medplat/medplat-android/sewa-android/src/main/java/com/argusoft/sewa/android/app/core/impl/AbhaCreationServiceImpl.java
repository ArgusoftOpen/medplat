package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.AbhaCreationService;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.Calendar;

@EBean(scope = EBean.Scope.Singleton)
public class AbhaCreationServiceImpl implements AbhaCreationService {

   @Bean
   SewaServiceImpl sewaService;

   @Override
   public void createMigrationForOfflineAbha(String healthIdDetails, MemberBean memberBean) {
      Log.i(getClass().getSimpleName(), "#### Create Migration For Offline Abha : " + healthIdDetails);

      //Preparing Checksum
      StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
      checkSum.append(Calendar.getInstance().getTimeInMillis());

      StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
      storeAnswerBean.setAnswerEntity(FormConstants.OFFLINE_ABHA_NUMBER_CREATIONS);
      storeAnswerBean.setAnswer(healthIdDetails);
      storeAnswerBean.setChecksum(checkSum.toString());
      storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
      storeAnswerBean.setFormFilledUpTime(0L);
      storeAnswerBean.setMorbidityAnswer("-1");

      storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
      storeAnswerBean.setRelatedInstance("-1");
      if (memberBean != null) {
         storeAnswerBean.setMemberId(Long.valueOf(memberBean.getActualId()));
      }
      if (SewaTransformer.loginBean != null) {
         storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
         storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
      }
      sewaService.createStoreAnswerBean(storeAnswerBean);

      LoggerBean loggerBean = new LoggerBean();
      if (memberBean != null) {
         loggerBean.setBeneficiaryName(memberBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberBean));
      } else {
         loggerBean.setBeneficiaryName(LabelConstants.HEALTH_ID_MANAGEMENT);
      }
      loggerBean.setCheckSum(checkSum.toString());
      loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
      loggerBean.setFormType(FormConstants.OFFLINE_ABHA_NUMBER_CREATIONS);
      loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.OFFLINE_ABHA_NUMBER_CREATIONS));
      loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
      loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
      loggerBean.setNoOfAttempt(0);
      loggerBean.setModifiedOn(Calendar.getInstance().getTime());
      sewaService.createLoggerBean(loggerBean);
   }
}
