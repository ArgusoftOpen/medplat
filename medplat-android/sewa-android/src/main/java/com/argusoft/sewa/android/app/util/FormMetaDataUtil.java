package com.argusoft.sewa.android.app.util;

import android.content.SharedPreferences;

import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.MedicineListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.databean.NcdDiabetesDetailDataBean;
import com.argusoft.sewa.android.app.databean.NcdHypertensionDetailDataBean;
import com.argusoft.sewa.android.app.databean.NcdMemberMedicineDataBean;
import com.argusoft.sewa.android.app.databean.NcdMentalHealthDetailDataBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.FullFormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.ImmunisationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MorbidityDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class FormMetaDataUtil {

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<ListValueBean, Integer> listValueBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<FamilyBean, Integer> familyBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<MemberBean, Integer> memberBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<LocationBean, Integer> locationBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<HealthInfrastructureBean, Integer> healthInfrastructureBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<LocationMasterBean, Integer> locationMasterBeanDao;

    @Bean
    public ImmunisationServiceImpl immunisationService;

    @Bean
    public SewaFhsServiceImpl fhsService;

    @Bean
    NcdServiceImpl ncdService;

    private static List<String> invalidStates = new ArrayList<>();

    static {
        invalidStates.addAll(FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES);
        invalidStates.addAll(FhsConstants.FHS_DEAD_CRITERIA_MEMBER_STATES);
    }

    public void setMetaDataForRchFormByFormType(String formType, String memberActualId, String familyId, NotificationMobDataBean selectedNotification, SharedPreferences sharedPref) {

        SharedStructureData.relatedPropertyHashTable.clear();
        SharedStructureData.membersUnderTwenty.clear();
        SharedStructureData.selectedHealthInfra = null;

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberActualId);

        MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(Long.valueOf(memberActualId));
        FamilyDataBean familyDataBean;
        if (familyId != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(familyId);
        } else {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberBean.getFamilyId());
        }
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, memberBean.getFamilyId());

        if (familyDataBean == null || familyDataBean.getId() == null) {
            // Here family should never be null, So we are clearing family table.
            try {
                TableUtils.clearTable(familyBeanDao.getConnectionSource(), FamilyBean.class);
            } catch (SQLException e) {
                Log.e(getClass().getName(), null, e);
            }
            throw new DataException("Data Exception", 1);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        editor.putString(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberBean.getActualId());
        editor.putString(RelatedPropertyNameConstants.CUR_PREG_REG_DET_ID, String.valueOf(memberBean.getCurPregRegDetId()));
        editor.putString(RelatedPropertyNameConstants.FAMILY_ID, familyDataBean.getId());

        if (selectedNotification != null) {
            editor.putString(RelatedPropertyNameConstants.LOCATION_ID, String.valueOf(selectedNotification.getLocationId()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LOCATION_ID, String.valueOf(selectedNotification.getLocationId()));
        } else {
            if (familyDataBean.getAreaId() != null) {
                editor.putString(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getAreaId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getAreaId());
            } else {
                editor.putString(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getLocationId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getLocationId());
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MONTH_START, String.valueOf(cal.getTimeInMillis()));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID_AND_NAME,
                memberBean.getUniqueHealthId() + " / " + UtilBean.getMemberFullName(memberBean));

        SharedStructureData.currentRchMemberBean = memberBean;
        SharedStructureData.currentRchFamilyDataBean = familyDataBean;

        if (selectedNotification != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NOTIFICATION_ID, String.valueOf(selectedNotification.getId()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VISIT_NUMBER, selectedNotification.getCustomField());
        }

        if (familyDataBean.getAreaId() != null) {
            try {
                LocationMasterBean locationMasterBean = locationMasterBeanDao.queryBuilder().where().
                        eq(FieldNameConstants.ACTUAL_I_D, familyDataBean.getAreaId()).queryForFirst();
                if (locationMasterBean != null) {
                    String fhwDetailString = locationMasterBean.getFhwDetailString();
                    if (fhwDetailString != null) {
                        Type type = new TypeToken<List<Map<String, String>>>() {
                        }.getType();
                        List<Map<String, String>> fhwDetailMapList = new Gson().fromJson(fhwDetailString, type);
                        Map<String, String> fhwDetailMap = fhwDetailMapList.get(0);
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ASHA_INFO,
                                fhwDetailMap.get("name") + " (" + fhwDetailMap.get("mobileNumber") + ")");
                    }
                }
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        if (familyDataBean.getAnganwadiId() != null) {
            try {
                LocationBean anganwadi = locationBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.ACTUAL_I_D, familyDataBean.getAnganwadiId())
                        .and().eq("level", 8).queryForFirst();
                if (anganwadi != null && anganwadi.getName() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANGANWADI_ID, anganwadi.getName());
                }
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        if (familyDataBean.getAnganwadiUpdateFlag() != null && familyDataBean.getAnganwadiUpdateFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UPDATE_FAMILY_ANGANWADI, "yes");
        }

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberBean.getUniqueHealthId());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_NAME, UtilBean.getMemberFullName(memberBean));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberBean));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VISIT_DATE, String.valueOf(new Date().getTime()));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_FULL_NAME, UtilBean.getMemberFullName(memberBean));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberBean.getActualId());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PHONE_NUMBER, memberBean.getMobileNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, UtilBean.getFamilyFullAddress(familyDataBean));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                UtilBean.getMemberFullName(memberBean) + "(" + memberBean.getUniqueHealthId() + ")"); //for showing purpose...in worklog

        if (memberBean.getDob() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, String.valueOf(memberBean.getDob().getTime()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB_DISPLAY, sdf.format(memberBean.getDob()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY, UtilBean.getAgeDisplayOnGivenDate(memberBean.getDob(), new Date()));
        }

        if (memberBean.isAadharUpdated()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AADHAR_AVAILABLE, "1");
        } else {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AADHAR_AVAILABLE, "2");
        }

        if (memberBean.getNameBasedOnAadhar() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_BASED_ON_AADHAR, memberBean.getNameBasedOnAadhar());
        }

        if (memberBean.getLmpDate() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_LMP, sdf.format(memberBean.getLmpDate()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LMP_DATE, String.valueOf(memberBean.getLmpDate().getTime()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(memberBean.getLmpDate());
            calendar.add(Calendar.DATE, 281);
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EDD, sdf.format(calendar.getTime()));
        }

        if (memberBean.getLastDeliveryDate() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_DELIVERY_DATE, String.valueOf(memberBean.getLastDeliveryDate().getTime()));
        }

        if (memberBean.getLastDeliveryOutcome() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_DELIVERY_OUTCOME, memberBean.getLastDeliveryOutcome());
        }

        String tmpDataObj;
        if (memberBean.getLastMethodOfContraception() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_METHOD_OF_CONTRACEPTION, memberBean.getLastMethodOfContraception());
            tmpDataObj = FullFormConstants.getFullFormOfFamilyPlanningMethods(memberBean.getLastMethodOfContraception());
            if (tmpDataObj != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_METHOD_OF_CONTRACEPTION_DISPLAY, tmpDataObj);
            }

            if (memberBean.getFpInsertOperateDate() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FP_INSERT_OPERATE_DATE, String.valueOf(memberBean.getFpInsertOperateDate().getTime()));
            }
        }

        if (familyDataBean.getReligion() != null) {
            String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
            if (religion != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
            }
        }

        if (familyDataBean.getCaste() != null) {
            String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
            if (caste != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
            }
        }

        if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, LabelConstants.YES);
        } else {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, LabelConstants.NO);
        }

        if (memberBean.getMobileNumber() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_PHONE_NUMBER, memberBean.getMobileNumber());
        } else if (familyDataBean.getContactPersonId() != null) {
            try {
                MemberBean contactPerson = memberBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.ACTUAL_ID, familyDataBean.getContactPersonId()).queryForFirst();
                if (contactPerson != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_PHONE_NUMBER, contactPerson.getMobileNumber());
                }
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        if (memberBean.getMotherId() != null) {
            try {
                MemberBean mother = memberBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, memberBean.getMotherId()).queryForFirst();
                if (mother != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOTHER_HEALTH_ID, mother.getUniqueHealthId());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOTHER_NAME, UtilBean.getMemberFullName(mother));
                }
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        if (memberBean.getAccountNumber() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BANK_ACCOUNT_NUMBER, memberBean.getAccountNumber());
        }

        if (memberBean.getIfsc() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IFSC, memberBean.getIfsc());
        }

        if (memberBean.getJsyBeneficiary() != null && memberBean.getJsyBeneficiary()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_JSY_BENEFICIARY, LabelConstants.YES);
        } else if (memberBean.getJsyBeneficiary() != null && !memberBean.getJsyBeneficiary()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_JSY_BENEFICIARY, LabelConstants.NO);
        }

        if (memberBean.getJsyPaymentGiven() != null && memberBean.getJsyPaymentGiven()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_JSY_PAYMENT_DONE, LabelConstants.YES);
        } else if (memberBean.getJsyPaymentGiven() != null && !memberBean.getJsyPaymentGiven()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_JSY_PAYMENT_DONE, LabelConstants.NO);
        }

        if (memberBean.getBloodGroup() != null && !memberBean.getBloodGroup().equalsIgnoreCase(LabelConstants.N_A)) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BLOOD_GROUP, memberBean.getBloodGroup());
        }

        if (memberBean.getWeight() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_WEIGHT_AND_DATE,
                    memberBean.getWeight().toString() + " " + UtilBean.getMyLabel("Kgs"));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREV_WEIGHT, memberBean.getWeight().toString());
        }

        if (memberBean.getCurrentGravida() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_GRAVIDA, String.valueOf(memberBean.getCurrentGravida() + 1));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_GRAVIDA, String.valueOf(memberBean.getCurrentGravida() + 1));
        } else {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_GRAVIDA, ("1"));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_GRAVIDA, ("1"));
        }

        if (memberBean.getCurrentPara() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_PARA, String.valueOf(memberBean.getCurrentPara() + 1));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_PARA, String.valueOf(memberBean.getCurrentPara() + 1));
        } else {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_PARA, ("1"));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_PARA, ("1"));
        }

        if (memberBean.getLmpDate() != null && memberBean.getIsPregnantFlag()) {
            int numberOfWeeks = UtilBean.getNumberOfWeeks(memberBean.getLmpDate(), new Date());
            if (numberOfWeeks <= 40) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.WEEKS_OF_PREGNANCY, String.valueOf(numberOfWeeks));
            } else {
                int overdueWeeks = numberOfWeeks - 40;
                String displayString = 40 + " " + UtilBean.getMyLabel("+ overdue")
                        + " " + overdueWeeks + " " + UtilBean.getMyLabel("weeks");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.WEEKS_OF_PREGNANCY, displayString);
            }
        }

        MemberAdditionalInfoDataBean memberAdditionalInfo = new MemberAdditionalInfoDataBean();
        if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
            Gson gson = new Gson();
            memberAdditionalInfo = gson.fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (memberAdditionalInfo.getLastServiceLongDate() != null && memberAdditionalInfo.getLastServiceLongDate() > 0) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_SERVICE_DATE, memberAdditionalInfo.getLastServiceLongDate().toString());
            }
        }

        if (memberBean.getCongenitalAnomalyIds() != null && !memberBean.getCongenitalAnomalyIds().isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONGENITAL_ANOMALY_IDS, memberBean.getCongenitalAnomalyIds());
        }

        if (memberBean.getChronicDiseaseIds() != null && !memberBean.getChronicDiseaseIds().isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHRONIC_DISEASE_IDS, memberBean.getChronicDiseaseIds());
        }

        if (memberBean.getCurrentDiseaseIds() != null && !memberBean.getCurrentDiseaseIds().isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_DISEASE_IDS, memberBean.getCurrentDiseaseIds());
        }

        if (memberBean.getEyeIssueIds() != null && !memberBean.getEyeIssueIds().isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EYE_ISSUE_IDS, memberBean.getEyeIssueIds());
        }

        switch (formType) {
            case FormConstants.LMP_FOLLOW_UP:
            case FormConstants.ASHA_LMPFU:
            case FormConstants.FHW_PREGNANCY_CONFIRMATION:
                if (memberBean.getLmpDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_LMP, sdf.format(memberBean.getLmpDate()));
                }
                if (memberBean.getYearOfWedding() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MARRIAGE_YEAR, memberBean.getYearOfWedding().toString());
                }
                if (memberBean.getDob() != null && memberBean.getYearOfWedding() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(memberBean.getDob());
                    Integer yearOfBirth = c.get(Calendar.YEAR);
                    Integer yearOfWedding = memberBean.getYearOfWedding();
                    Integer ageAtWedding = yearOfWedding - yearOfBirth;
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_AT_WEDDING, ageAtWedding + " " + LabelConstants.YEARS);
                }

                if (memberBean.getDateOfWedding() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DATE_OF_WEDDING, String.valueOf(memberBean.getDateOfWedding().getTime()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DATE_OF_WEDDING_DISPLAY, sdf.format(memberBean.getDateOfWedding()));
                }

                SharedStructureData.membersUnderTwenty = getMembersLessThan20(memberBean.getFamilyId(), new MemberDataBean(memberBean));
                if (!SharedStructureData.membersUnderTwenty.isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "T");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "F");
                }

                try {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_LIVING_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_MALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.MALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_FEMALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    MemberBean latestChild = memberBeanDao.queryBuilder()
                            .orderBy(FieldNameConstants.DOB, false)
                            .where().eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                            .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                            .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                            .and().notIn(FieldNameConstants.STATE, invalidStates).queryForFirst();

                    if (latestChild != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LATEST_CHILD_NAME, latestChild.getFirstName() + " " + latestChild.getLastName());
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LATEST_CHILD_AGE, UtilBean.getAgeDisplayOnGivenDate(latestChild.getDob(), new Date()));
                        if (latestChild.getGender() != null && latestChild.getGender().equals(GlobalTypes.MALE)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LATEST_CHILD_GENDER, LabelConstants.MALE);
                        } else if (latestChild.getGender() != null && latestChild.getGender().equals(GlobalTypes.FEMALE)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LATEST_CHILD_GENDER, LabelConstants.FEMALE);
                        }
                    }
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
                break;

            case FormConstants.TECHO_FHW_ANC:
            case FormConstants.ASHA_ANC:
                try {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_LIVING_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_MALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.MALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_FEMALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where()
                                    .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    if (memberBean.getDob() != null) {
                        if (memberBean.getYearOfWedding() != null) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(memberBean.getDob());
                            Integer yearOfBirth = c.get(Calendar.YEAR);
                            Integer yearOfWedding = memberBean.getYearOfWedding();
                            Integer ageAtWedding = yearOfWedding - yearOfBirth;
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_AT_WEDDING, ageAtWedding + " " + LabelConstants.YEARS);
                        }

                        if (memberBean.getDateOfWedding() != null) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(memberBean.getDob());
                            Integer yearOfBirth = c.get(Calendar.YEAR);

                            c.setTime(memberBean.getDateOfWedding());
                            Integer yearOfWedding = c.get(Calendar.YEAR);
                            Integer ageAtWedding = yearOfWedding - yearOfBirth;
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_AT_WEDDING, ageAtWedding + " " + LabelConstants.YEARS);
                        }
                    }

                    SharedStructureData.membersUnderTwenty = this.getMembersLessThan20(memberBean.getFamilyId(), new MemberDataBean(memberBean));

                    if (this.checkIfMotherWasPregnantWithinLast3Years(memberBean)) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREGNANT_LAST_3_YRS, LabelConstants.YES);
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREGNANT_LAST_3_YRS, LabelConstants.NO);
                    }

                    if (memberBean.getImmunisationGiven() != null) {
                        if (memberBean.getImmunisationGiven().contains(RchConstants.TT_BOOSTER)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TT, RchConstants.TT_BOOSTER);
                        } else if (memberBean.getImmunisationGiven().contains(RchConstants.TT2)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TT, RchConstants.TT2);
                        } else if (memberBean.getImmunisationGiven().contains(RchConstants.TT1)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TT, RchConstants.TT1);
                        }

                        if (memberBean.getImmunisationGiven().contains(RchConstants.TT1)) {
                            String[] splitArray = memberBean.getImmunisationGiven().split(",");
                            for (String split : splitArray) {
                                String[] split1 = split.split("#");
                                if (split1[0].equals(RchConstants.TT1)) {
                                    SharedStructureData.relatedPropertyHashTable.put(
                                            RelatedPropertyNameConstants.TT_1_GIVEN_ON, String.valueOf(sdf.parse(split1[1]).getTime()));
                                }
                            }
                        }
                    }

                } catch (SQLException | ParseException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }

                if (memberBean.getCurrentDiseaseIds() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String diseaseId : memberBean.getCurrentDiseaseIds().split(",")) {
                        try {
                            sb.append(listValueBeanDao.queryBuilder()
                                    .where().eq(FieldNameConstants.ID_OF_VALUE, diseaseId).queryForFirst().getValue());
                            sb.append("\n");
                        } catch (SQLException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_ILLNESS, sb.toString());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_ILLNESS, LabelConstants.NONE);
                }

                if (memberBean.getHaemoglobin() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_HAEMOGLOBIN,
                            memberBean.getHaemoglobin().toString());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_HAEMOGLOBIN_DISPLAY,
                            memberBean.getHaemoglobin().toString() + " " + UtilBean.getMyLabel("gm"));
                }

                if (memberBean.getWeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_WEIGHT,
                            memberBean.getWeight().toString() + UtilBean.getMyLabel(" Kgs"));
                }

                if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELIGIBILITY_CRITERIA, LabelConstants.FAMILY_IS_BPL);
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELIGIBILITY_CRITERIA, LabelConstants.FAMILY_IS_NOT_BPL);
                }

                if (memberBean.getJsyBeneficiary() != null && memberBean.getJsyBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.JSY_BENEFICIARY, "1");
                } else if (memberBean.getJsyBeneficiary() != null && !memberBean.getJsyBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.JSY_BENEFICIARY, "2");
                }

                if (memberBean.getJsyPaymentGiven() != null && memberBean.getJsyPaymentGiven()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.JSY_PAYMENT, "yes");
                }

                if (memberBean.getKpsyBeneficiary() != null && memberBean.getKpsyBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.KPSY_BENEFICIARY, "1");
                } else if (memberBean.getKpsyBeneficiary() != null && !memberBean.getKpsyBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.KPSY_BENEFICIARY, "2");
                }

                if (memberBean.getIayBeneficiary() != null && memberBean.getIayBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IAY_BENEFICIARY, "1");
                } else if (memberBean.getIayBeneficiary() != null && !memberBean.getIayBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IAY_BENEFICIARY, "2");
                }

                if (memberBean.getChiranjeeviYojnaBeneficiary() != null && memberBean.getChiranjeeviYojnaBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHIRANJEEVI_BENEFICIARY, "1");
                } else if (memberBean.getChiranjeeviYojnaBeneficiary() != null && !memberBean.getChiranjeeviYojnaBeneficiary()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHIRANJEEVI_BENEFICIARY, "2");
                }

                if (memberBean.getCurPregRegDate() != null && memberBean.getLmpDate() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(memberBean.getLmpDate());
                    calendar.add(Calendar.DATE, 90);

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CUR_PREG_REG_DATE, String.valueOf(memberBean.getCurPregRegDate().getTime()));

                    if (calendar.getTime().after(memberBean.getCurPregRegDate())) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EARLY_REGISTRATION, LabelConstants.YES);
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EARLY_REGISTRATION, LabelConstants.NO);
                    }
                }

                if (memberBean.getHighRiskCase() != null && memberBean.getHighRiskCase()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HIGH_RISK_CONDITIONS, LabelConstants.YES);
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HIGH_RISK_CONDITIONS, LabelConstants.NO);
                }

                if (memberBean.getImmunisationGiven() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISATION_GIVEN, getImmunisationGivenString(memberBean));
                }

                if (memberBean.getPreviousPregnancyComplication() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_PREGNANCY_COMPLICATION, memberBean.getPreviousPregnancyComplication());
                }

                if (memberAdditionalInfo.getHbsagTest() != null && !memberAdditionalInfo.getHbsagTest().equals(RchConstants.NOT_DONE)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HBSAG_TEST, memberAdditionalInfo.getHbsagTest());
                }

                if (memberAdditionalInfo.getHivTest() != null && !memberAdditionalInfo.getHivTest().equals(RchConstants.NOT_DONE)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HIV_TEST, memberAdditionalInfo.getHivTest());
                }

                if (memberAdditionalInfo.getVdrlTest() != null && !memberAdditionalInfo.getVdrlTest().equals(RchConstants.NOT_DONE)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VDRL_TEST, memberAdditionalInfo.getVdrlTest());
                }

                if (memberAdditionalInfo.getSickleCellTest() != null && !memberAdditionalInfo.getSickleCellTest().equals(RchConstants.NOT_DONE)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SICKLE_CELL_TEST, memberAdditionalInfo.getSickleCellTest());
                }

                if (memberAdditionalInfo.getHeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_HEIGHT, memberAdditionalInfo.getHeight().toString());
                }

                if (memberAdditionalInfo.getAncAshaMorbidity() != null && !memberAdditionalInfo.getAncAshaMorbidity().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    try {
                        Type type = new TypeToken<List<MorbidityDataBean>>() {
                        }.getType();
                        List<MorbidityDataBean> morbidityDataBeans = new Gson().fromJson(memberAdditionalInfo.getAncAshaMorbidity(), type);
                        int count = 0;
                        for (MorbidityDataBean morbidityDataBean : morbidityDataBeans) {
                            sb.append(++count)
                                    .append(". ")
                                    .append(UtilBean.getMyLabel(MorbiditiesConstant.getMorbidityCodeAsKEYandMorbidityNameAsVALUE(morbidityDataBean.getCode())))
                                    .append("\n");
                        }
                    } catch (JsonSyntaxException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                        String ancAshaMorbidity = memberAdditionalInfo.getAncAshaMorbidity();
                        String[] splitComma = ancAshaMorbidity.split("#");
                        int count = 0;
                        for (String aCommaPart : splitComma) {
                            if (aCommaPart.contains("@")) {
                                String[] split = aCommaPart.split("@");
                                sb.append(++count)
                                        .append(". ")
                                        .append(UtilBean.getMyLabel(MorbiditiesConstant.getMorbidityCodeAsKEYandMorbidityNameAsVALUE(split[0])))
                                        .append("\n");
                            }
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANC_MORB_ASHA, sb.toString());
                }

                break;

            case FormConstants.TECHO_FHW_WPD:

                if (memberBean.getEarlyRegistration() != null && memberBean.getEarlyRegistration()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EARLY_REGISTRATION, "Yes");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EARLY_REGISTRATION, "No");
                }

                if (memberBean.getAncVisitDates() != null) {
                    String[] dates = memberBean.getAncVisitDates().split(",");
                    StringBuilder ancVisitsString = new StringBuilder();
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREG_REGISTRATION_DATE, dates[0]);
                    int i = 1;
                    int counter = 0;
                    for (String date : dates) {
                        ancVisitsString.append("Visit ");
                        ancVisitsString.append(i++);
                        ancVisitsString.append(" - ");
                        ancVisitsString.append(date);
                        counter++;
                        if (counter != dates.length) {
                            ancVisitsString.append("\n");
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANC_DETAILS, ancVisitsString.toString());
                }

                if (memberBean.getCurrentDiseaseIds() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String diseaseId : memberBean.getCurrentDiseaseIds().split(",")) {
                        try {
                            sb.append(listValueBeanDao.queryBuilder()
                                    .where().eq(FieldNameConstants.ID_OF_VALUE, diseaseId).queryForFirst().getValue());
                            sb.append("\n");
                        } catch (SQLException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_ILLNESS, sb.toString());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_ILLNESS, LabelConstants.NONE);
                }

                if (memberBean.getImmunisationGiven() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISATION_GIVEN, getImmunisationGivenString(memberBean));
                }

                if (memberBean.getHighRiskCase() != null && memberBean.getHighRiskCase()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HIGH_RISK_CONDITIONS, LabelConstants.YES);
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HIGH_RISK_CONDITIONS, LabelConstants.NO);
                }

                try {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_LIVING_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_MALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.MALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_FEMALE_CHILDREN,
                            String.valueOf(memberBeanDao.queryBuilder().where().eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE)
                                    .and().eq(FieldNameConstants.FAMILY_ID, memberBean.getFamilyId())
                                    .and().ne(FieldNameConstants.ACTUAL_ID, memberBean.getActualId())
                                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf()));
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
                break;

            case FormConstants.TECHO_FHW_CS:
            case FormConstants.ASHA_CS:
            case FormConstants.ASHA_SAM_SCREENING:
            case FormConstants.FHW_SAM_SCREENING_REF:
            case FormConstants.CMAM_FOLLOWUP:
            case FormConstants.TECHO_AWW_CS:
            case FormConstants.SAM_SCREENING:
            case FormConstants.TECHO_AWW_THR:
                tmpDataObj = FullFormConstants.getFullFormsOfPlace(memberBean.getPlaceOfBirth());
                if (tmpDataObj != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PLACE_OF_BIRTH, tmpDataObj);
                }

                tmpDataObj = FullFormConstants.getFullFormOfGender(memberBean.getGender());
                if (tmpDataObj != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER, tmpDataObj);
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHILD_FIRST_NAME, memberBean.getFirstName());

                MemberBean mother = null;
                try {
                    if (memberBean.getMotherId() != null) {
                        mother = memberBeanDao.queryBuilder().where().eq("actualID", memberBean.getMotherId()).queryForFirst();
                    }
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }

                if (mother != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PHONE_NUMBER, mother.getMobileNumber());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOTHER_HEALTH_ID, mother.getUniqueHealthId());
                }

                if (memberBean.getBirthWeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BIRTH_WEIGHT, memberBean.getBirthWeight().toString());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BIRTH_WEIGHT_DISPLAY, memberBean.getBirthWeight().toString() + " " + UtilBean.getMyLabel("Kgs"));
                }

                if (memberBean.getDob() != null) {
                    int ageInMonths = UtilBean.calculateMonthsBetweenDates(memberBean.getDob(), new Date());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE, String.valueOf(ageInMonths));
                    int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(memberBean.getDob().getTime());
                    String ageDisplay = UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2]);
                    if (ageDisplay != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY, ageDisplay);
                    }
                }

                if (memberBean.getComplementaryFeedingStarted() != null && memberBean.getComplementaryFeedingStarted()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.COMPLIMENTARY_FEEDING, "Yes");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.COMPLIMENTARY_FEEDING, "No");
                }

                if (memberBean.getImmunisationGiven() != null && memberBean.getImmunisationGiven().length() > 0) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISATION_GIVEN, getImmunisationGivenString(memberBean));
                }
                Set<String> dueImmunisationsForChild = immunisationService.getDueImmunisationsForChild(memberBean.getDob(), memberBean.getImmunisationGiven());

                if (dueImmunisationsForChild != null && !dueImmunisationsForChild.isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_VACCINES, dueImmunisationsForChild.toString().replace("[", "").replace("]", ""));
                }

                if (memberBean.getDob() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.YEAR, 1);
                    if (memberBean.getDob().before(calendar.getTime())) {
                        calendar.add(Calendar.YEAR, 1);
                        if (memberBean.getDob().before(calendar.getTime())) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISED_IN_TWO_YEAR, isChildImmunisedInTwoYear(memberBean));
                        }
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISED_IN_ONE_YEAR, isChildImmunisedInOneYear(memberBean));
                    }
                }

                try {
                    List<VersionBean> featureVersionBeans = versionBeanDao.queryForEq("key", GlobalTypes.VERSION_FEATURES_LIST);
                    if (featureVersionBeans != null && !featureVersionBeans.isEmpty()) {
                        VersionBean versionBean = featureVersionBeans.get(0);
                        if (versionBean.getValue().contains(GlobalTypes.MOB_FEATURE_CEREBRAL_PALSY_SCREENING)) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CP_SCREENING, GlobalTypes.MOB_FEATURE_CEREBRAL_PALSY_SCREENING);
                        }
                    }
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }

                if (memberAdditionalInfo.getWtGainStatus() != null && !memberAdditionalInfo.getWtGainStatus().isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.WT_GAIN_STATUS, memberAdditionalInfo.getWtGainStatus());
                }

                if (memberAdditionalInfo.getGivenRUTF() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GIVEN_RUTF, memberAdditionalInfo.getGivenRUTF().toString());
                }

                if (memberAdditionalInfo.getCpNegativeQues() != null && !memberAdditionalInfo.getCpNegativeQues().isEmpty()) {
                    Set<String> allCpQuestions = new HashSet<>(GlobalTypes.CEREBRAL_PALSY_QUESTION_IDS_MAP.keySet());
                    allCpQuestions.removeAll(memberAdditionalInfo.getCpNegativeQues());
                    String key;
                    for (String que : allCpQuestions) {
                        key = GlobalTypes.CEREBRAL_PALSY_QUESTION_IDS_MAP.get(que);
                        if (key != null) {
                            SharedStructureData.relatedPropertyHashTable.put(key, "Yes");
                        }
                    }
                }

                if (memberAdditionalInfo.getCpState() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CP_STATUS, memberAdditionalInfo.getCpState());
                }

                if (memberAdditionalInfo.getLastSamScreeningDate() != null) {
                    Calendar instance = Calendar.getInstance();
                    instance.set(Calendar.DATE, 1);
                    instance.set(Calendar.HOUR_OF_DAY, 0);
                    instance.set(Calendar.MINUTE, 0);
                    instance.set(Calendar.SECOND, 0);
                    instance.set(Calendar.MILLISECOND, 0);
                    if (new Date(memberAdditionalInfo.getLastSamScreeningDate()).after(instance.getTime())) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SAM_SCREENING_DONE, "T");
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SAM_SCREENING_DONE, "F");
                    }

                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_SAM_SCREENING_DATE, memberAdditionalInfo.getLastSamScreeningDate().toString());

                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SAM_SCREENING_DONE, "F");
                }

                SharedStructureData.vaccineGivenDateMap.clear();
                if (memberBean.getImmunisationGiven() != null && memberBean.getImmunisationGiven().length() > 0) {
                    Map<String, Date> vaccineGivenDateMapTemp = new HashMap<>();
                    StringTokenizer vaccineTokenizer = new StringTokenizer(memberBean.getImmunisationGiven(), ",");
                    while (vaccineTokenizer.hasMoreElements()) {
                        String[] vaccine = vaccineTokenizer.nextToken().split("#");
                        String givenVaccineName = vaccine[0].trim();
                        try {
                            vaccineGivenDateMapTemp.put(givenVaccineName, sdf.parse(vaccine[1]));
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                        }
                    }

                    if (vaccineGivenDateMapTemp.size() > 0) {
                        SharedStructureData.vaccineGivenDateMap = vaccineGivenDateMapTemp;
                    }
                }

                if (familyDataBean.getReligion() != null) {
                    String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                    if (religion != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
                    }
                }
                if (familyDataBean.getCaste() != null) {
                    String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                    if (caste != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
                    }
                }

                if (memberAdditionalInfo.getLastTHRServiceDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_THR_SERVICE_DATE, memberAdditionalInfo.getLastTHRServiceDate().toString());
                }

                if (memberAdditionalInfo.getWeightMap() != null && !memberAdditionalInfo.getWeightMap().isEmpty()) {
                    Long lastEntryDate = Collections.max(memberAdditionalInfo.getWeightMap().keySet());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_WEIGHT_AND_DATE,
                            memberAdditionalInfo.getWeightMap().get(lastEntryDate) + " " + UtilBean.getMyLabel("Kgs"));
                }

                break;

            case FormConstants.TECHO_FHW_PNC:
            case FormConstants.ASHA_PNC:
                if (memberBean.getLastDeliveryDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE_DISPLAY, sdf.format(memberBean.getLastDeliveryDate()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE, Long.toString(memberBean.getLastDeliveryDate().getTime()));
                }

                if (selectedNotification != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SERVICE_NUMBER, selectedNotification.getCustomField());
                }

                if (memberAdditionalInfo.getPncIfa() != null && memberAdditionalInfo.getPncIfa() > 0) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PNC_IFA, memberAdditionalInfo.getPncIfa().toString());
                }

                if (memberAdditionalInfo.getPncCalcium() != null && memberAdditionalInfo.getPncCalcium() > 0) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PNC_CALCIUM, memberAdditionalInfo.getPncCalcium().toString());
                }

                try {
                    List<MemberBean> allChildrenOfMother = memberBeanDao.queryBuilder().where()
                            .eq(FieldNameConstants.MOTHER_ID, memberBean.getActualId())
                            .and().notIn(FieldNameConstants.STATE, invalidStates).query();


                    if (allChildrenOfMother != null && !allChildrenOfMother.isEmpty()) {
                        List<MemberBean> childrenLessThan60Days = new ArrayList<>();
                        for (MemberBean child : allChildrenOfMother) {
                            Calendar cal1 = Calendar.getInstance();
                            cal1.add(Calendar.DATE, -60);
                            if (child.getDob().after(cal1.getTime())) {
                                childrenLessThan60Days.add(child);
                            }
                        }

                        //SETTING CHILDREN's METADATA
                        if (!childrenLessThan60Days.isEmpty()) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE_DISPLAY,
                                    sdf.format(childrenLessThan60Days.get(0).getDob()));
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE,
                                    Long.toString(childrenLessThan60Days.get(0).getDob().getTime()));

                            int i = 0;
                            for (MemberBean child : childrenLessThan60Days) {
                                if (i == 0) {
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID_CHILD, child.getUniqueHealthId());
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHILD_NAME, child.getFirstName());
                                    Set<String> dueImmunisations = immunisationService.getDueImmunisationsForChild(child.getDob(), child.getImmunisationGiven());

                                    if (dueImmunisations != null && !dueImmunisations.isEmpty()) {
                                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_VACCINES,
                                                dueImmunisations.toString().replace("[", "").replace("]", ""));
                                    }

                                } else {
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID_CHILD + i, child.getUniqueHealthId());
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHILD_NAME + i, child.getFirstName());
                                    Set<String> dueImmunisations = immunisationService.getDueImmunisationsForChild(child.getDob(), child.getImmunisationGiven());
                                    if (dueImmunisations != null && !dueImmunisations.isEmpty()) {
                                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_VACCINES + i,
                                                dueImmunisations.toString().replace("[", "").replace("]", ""));
                                    }
                                }
                                i++;
                            }
                        }

                        SharedStructureData.totalFamilyMembersCount = childrenLessThan60Days.size();
                    } else {
                        SharedStructureData.totalFamilyMembersCount = 0;
                    }
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
                break;

            case FormConstants.TECHO_WPD_DISCHARGE:
                if (memberBean.getLastDeliveryDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE_DISPLAY,
                            sdf.format(memberBean.getLastDeliveryDate()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE,
                            String.valueOf(memberBean.getLastDeliveryDate().getTime()));
                }
                if (selectedNotification != null && selectedNotification.getOtherDetails() != null) {
                    try {
                        HealthInfrastructureBean infrastructureBean = healthInfrastructureBeanDao.queryBuilder().where()
                                .eq(FieldNameConstants.ACTUAL_ID, selectedNotification.getOtherDetails()).queryForFirst();
                        if (infrastructureBean != null) {
                            SharedStructureData.selectedHealthInfra = infrastructureBean;
                        }
                    } catch (SQLException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                }
                break;

            case FormConstants.TECHO_FHW_VAE:
                String immunisationGivenString = memberBean.getImmunisationGiven();
                List<String> immunisationsGiven = new ArrayList<>();

                List<OptionDataBean> options = new ArrayList<>();
                options.add(new OptionDataBean("-1", GlobalTypes.SELECT, null));
                if (immunisationGivenString != null) {
                    for (String vac : immunisationGivenString.split(",")) {
                        immunisationsGiven.add(vac.split("#")[0]);
                    }
                    for (String str : immunisationsGiven) {
                        OptionDataBean optionDataBean = new OptionDataBean();
                        optionDataBean.setKey(str);
                        optionDataBean.setValue(FullFormConstants.getFullFormOfVaccines(str));
                        options.add(optionDataBean);
                    }
                }
                SharedStructureData.givenVaccinesToChild = options;
                break;

            case FormConstants.GERIATRICS_MEDICATION_ALERT:
                List<MemberBean> memberBeans = SharedStructureData.sewaFhsService.retrieveFamilyMembersContactListByMember(memberBean.getFamilyId(), memberBean.getActualId());
                StringBuilder contactDetails = new StringBuilder();
                if (memberBeans.isEmpty()) {
                    contactDetails = new StringBuilder(GlobalTypes.NOT_AVAILABLE);
                } else {
                    for (MemberBean member : memberBeans) {
                        if (contactDetails.length() != 0) {
                            contactDetails.append("\n");
                        }
                        contactDetails.append(member.getFirstName())
                                .append(" ")
                                .append(member.getMiddleName())
                                .append(" ")
                                .append(member.getLastName())
                                .append(" - ")
                                .append(member.getMobileNumber());
                    }
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONTACT_DETAILS, contactDetails.toString());

                if (memberBean.getGender() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER,
                            UtilBean.getGenderLabelFromValue(memberBean.getGender()));
                }
                break;

            case FormConstants.IDSP_MEMBER:
            case FormConstants.IDSP_MEMBER_2:
                if (memberBean.getGender() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER_DISPLAY,
                            UtilBean.getGenderLabelFromValue(memberBean.getGender()));
                }

                if (memberBean.getMobileNumber() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, memberBean.getMobileNumber());
                }
                break;
            default:
        }
        editor.commit();
    }

    private List<OptionDataBean> getMembersLessThan20(String familyId, MemberDataBean memberDataBean) {
        Calendar instance = Calendar.getInstance();
        List<OptionDataBean> membersLessThan20 = new ArrayList<>();
        StringBuilder selectedChildren = new StringBuilder();

        try {
            if (memberDataBean != null && memberDataBean.getDob() != null) {
                instance.setTimeInMillis(memberDataBean.getDob());
                instance.add(Calendar.YEAR, 16);
            } else {
                instance.add(Calendar.YEAR, -20);
            }

            List<MemberBean> memberBeans = memberBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.FAMILY_ID, familyId)
                    .and().isNull(FieldNameConstants.MOTHER_ID)
                    .and().gt(FieldNameConstants.DOB, instance.getTime())
                    .and().notIn(FieldNameConstants.STATE, invalidStates).query();

            if (memberDataBean != null) {
                List<MemberBean> children = memberBeanDao.queryBuilder().where()
                        .eq(FieldNameConstants.MOTHER_ID, memberDataBean.getId())
                        .and().eq(FieldNameConstants.FAMILY_ID, memberDataBean.getFamilyId())
                        .and().ne(FieldNameConstants.ACTUAL_ID, memberDataBean.getId())
                        .and().notIn(FieldNameConstants.STATE, invalidStates).query();

                for (MemberBean memberBean : children) {
                    selectedChildren.append(memberBean.getActualId()).append(",");
                    if (!memberBeans.contains(memberBean)) {
                        memberBeans.add(memberBean);
                    }
                }

                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ACTUAL_GRAVIDA, String.valueOf(children.size()));
                if (!children.isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SELECTED_CHILDREN, selectedChildren.toString());
                }
            }

            for (MemberBean memberBean : memberBeans) {
                membersLessThan20.add(new OptionDataBean(memberBean.getActualId(), UtilBean.getMemberFullName(memberBean), null));
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return membersLessThan20;
    }

    private boolean checkIfMotherWasPregnantWithinLast3Years(MemberBean memberBean) {
        if (memberBean.getLmpDate() == null || memberBean.getLastDeliveryDate() == null) {
            return false;
        }
        return UtilBean.calculateYearsBetweenDates(memberBean.getLmpDate().getTime(), memberBean.getLastDeliveryDate().getTime()) <= 2.0;
    }

    private String getImmunisationGivenString(MemberBean memberBean) {
        String[] immunisationAndDate = memberBean.getImmunisationGiven().split(",");
        StringBuilder immunisationString = new StringBuilder();
        int counter = 0;
        for (String date : immunisationAndDate) {
            String[] split = date.split("#");
            immunisationString.append(UtilBean.getMyLabel(split[0]));
            immunisationString.append(" - ");
            immunisationString.append(split[1]);
            counter++;
            if (counter != immunisationAndDate.length) {
                immunisationString.append("\n");
            }
        }
        return immunisationString.toString();
    }

    public String isChildImmunisedInOneYear(MemberBean memberBean) {
        if (memberBean.getImmunisationGiven() != null && memberBean.getImmunisationGiven().length() > 0) {
            String[] immunisationGiven = memberBean.getImmunisationGiven().split(",");
            List<String> immunisationList = new ArrayList<>();
            for (String immunisation : immunisationGiven) {
                immunisationList.add(immunisation.split("#")[0]);
            }

            if (!immunisationList.isEmpty()) {
                if (immunisationList.contains(RchConstants.BCG)
                        && immunisationList.contains(RchConstants.OPV_1)
                        && immunisationList.contains(RchConstants.OPV_2)
                        && immunisationList.contains(RchConstants.OPV_3)
                        && (immunisationList.contains(RchConstants.PENTA_1) || immunisationList.contains(RchConstants.DPT_1))
                        && (immunisationList.contains(RchConstants.PENTA_2) || immunisationList.contains(RchConstants.DPT_2))
                        && (immunisationList.contains(RchConstants.PENTA_3) || immunisationList.contains(RchConstants.DPT_3))
                        && (immunisationList.contains(RchConstants.MEASLES_RUBELLA_1) || immunisationList.contains(RchConstants.MEASLES_1))) {
                    return UtilBean.getMyLabel(LabelConstants.YES);
                } else {
                    return UtilBean.getMyLabel(LabelConstants.NO);
                }
            } else {
                return UtilBean.getMyLabel(LabelConstants.NO);
            }
        } else {
            return UtilBean.getMyLabel(LabelConstants.NO);
        }
    }

    public String isChildImmunisedInTwoYear(MemberBean memberBean) {
        if (memberBean.getImmunisationGiven() != null && memberBean.getImmunisationGiven().length() > 0) {
            String[] immunisationGiven = memberBean.getImmunisationGiven().split(",");
            List<String> immunisationList = new ArrayList<>();
            for (String immunisation : immunisationGiven) {
                immunisationList.add(immunisation.split("#")[0]);
            }

            if (!immunisationList.isEmpty()) {
                if (immunisationList.contains(RchConstants.BCG)
                        && immunisationList.contains(RchConstants.OPV_1)
                        && immunisationList.contains(RchConstants.OPV_2)
                        && immunisationList.contains(RchConstants.OPV_3)
                        && immunisationList.contains(RchConstants.OPV_BOOSTER)
                        && (immunisationList.contains(RchConstants.PENTA_1) || immunisationList.contains(RchConstants.DPT_1))
                        && (immunisationList.contains(RchConstants.PENTA_2) || immunisationList.contains(RchConstants.DPT_2))
                        && (immunisationList.contains(RchConstants.PENTA_3) || immunisationList.contains(RchConstants.DPT_3))
                        && (immunisationList.contains(RchConstants.MEASLES_RUBELLA_1) || immunisationList.contains(RchConstants.MEASLES_1))
                        && (immunisationList.contains(RchConstants.MEASLES_RUBELLA_2) || immunisationList.contains(RchConstants.MEASLES_2))) {
                    return UtilBean.getMyLabel("Yes");
                } else {
                    return UtilBean.getMyLabel("No");
                }
            } else {
                return UtilBean.getMyLabel("No");
            }
        } else {
            return UtilBean.getMyLabel("No");
        }
    }

    public void setMetaDataForMemberUpdateForm(MemberDataBean memberDataBean, FamilyDataBean familyDataBean, SharedPreferences sharedPreferences) {
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
        sharedPreferences.edit().clear().apply();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (memberDataBean != null) {
            editor.putString(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberDataBean.getId());
        }
        if (familyDataBean != null) {
            editor.putString(RelatedPropertyNameConstants.FAMILY_ID, familyDataBean.getId());
            if (familyDataBean.getAreaId() != null) {
                editor.putString(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getAreaId());
            } else {
                editor.putString(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getLocationId());
            }
        }

        SharedStructureData.relatedPropertyHashTable.clear();
        SharedStructureData.currentFamilyDataBean = familyDataBean;
        SharedStructureData.currentMemberDataBean = memberDataBean;
        SharedStructureData.membersUnderTwenty.clear();

        if (familyDataBean != null && familyDataBean.getFamilyId() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
            SharedStructureData.membersUnderTwenty = this.getMembersLessThan20(familyDataBean.getFamilyId(), null);
            if (!SharedStructureData.membersUnderTwenty.isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "T");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "F");
            }
        }


        if (memberDataBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AADHAR_NUMBER, memberDataBean.getAadharNumber());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BANK_ACCOUNT_NUMBER, memberDataBean.getAccountNumber());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MIDDLE_NAME, memberDataBean.getMiddleName());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                    UtilBean.getMemberFullName(memberDataBean) + "(" + memberDataBean.getUniqueHealthId() + ")"); //for showing purpose...in worklog
            if (memberDataBean.getDob() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, memberDataBean.getDob().toString());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB_DISPLAY, sdf.format(new Date(memberDataBean.getDob())));
            }
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, "2");
            if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, "1");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GRANDFATHER_NAME, memberDataBean.getGrandfatherName());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEAD_OF_FAMILY, UtilBean.getMemberFullName(memberDataBean));
            }
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_FULL_NAME, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FIRST_NAME, memberDataBean.getFirstName());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MIDDLE_NAME, memberDataBean.getMiddleName());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_NAME, memberDataBean.getLastName());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, memberDataBean.getMobileNumber());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IFSC, memberDataBean.getIfsc());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberDataBean.getUniqueHealthId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_MARITAL_STATUS, memberDataBean.getMaritalStatus());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANS_12, "1");
            if (memberDataBean.isAadharUpdated()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AADHAR_AVAILABLE, "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AADHAR_AVAILABLE, "2");
            }
            if (memberDataBean.getNormalCycleDays() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NORMAL_CYCLE_DAYS, memberDataBean.getNormalCycleDays().toString());
            }
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_PLANNING_METHOD, memberDataBean.getFamilyPlanningMethod());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_PREGNANT, "2");
            if (memberDataBean.getGender() != null && memberDataBean.getGender().equals(GlobalTypes.FEMALE)) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANS_12, "2");
                if (memberDataBean.getIsPregnantFlag() != null && memberDataBean.getIsPregnantFlag()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_PREGNANT, "1");
                }
                if (memberDataBean.getLmpDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LMP, memberDataBean.getLmpDate().toString());
                }

            }

            if (memberDataBean.getEducationStatus() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EDUCATION_STATUS, memberDataBean.getEducationStatus());
            }

            if (memberDataBean.getCongenitalAnomalyIds() != null && !memberDataBean.getCongenitalAnomalyIds().isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONGENITAL_STATUS, "1");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONGENITAL_ANOMALY_IDS, memberDataBean.getCongenitalAnomalyIds());
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONGENITAL_STATUS, "2");
            }

            if (memberDataBean.getChronicDiseaseIds() != null && !memberDataBean.getChronicDiseaseIds().isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHRONIC_DISEASE_STATUS, "1");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHRONIC_DISEASE_IDS, memberDataBean.getChronicDiseaseIds());
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CHRONIC_DISEASE_STATUS, "2");
            }

            if (memberDataBean.getCurrentDiseaseIds() != null && !memberDataBean.getCurrentDiseaseIds().isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_DISEASE_STATUS, "1");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_DISEASE_IDS, memberDataBean.getCurrentDiseaseIds());
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_DISEASE_STATUS, "2");
            }

            if (memberDataBean.getEyeIssueIds() != null && !memberDataBean.getEyeIssueIds().isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EYE_ISSUE_STATUS, "1");
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EYE_ISSUE_IDS, memberDataBean.getEyeIssueIds());
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.EYE_ISSUE_STATUS, "2");
            }

            if (memberDataBean.getMotherId() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOTHER_ID, String.valueOf(memberDataBean.getMotherId()));
            }

            if (memberDataBean.getGender() != null && memberDataBean.getGender().equals("F")
                    && memberDataBean.getMaritalStatus() != null && memberDataBean.getMaritalStatus().equals("629")) {
                SharedStructureData.membersUnderTwenty = getMembersLessThan20(memberDataBean.getFamilyId(), memberDataBean);
                if (!SharedStructureData.membersUnderTwenty.isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "T");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBERS_UNDER_20_AVAILABLE, "F");
                }
            }

        }
        editor.apply();
    }

    public void setMetaDataForNCDForms(MemberDataBean memberDataBean, FamilyDataBean familyDataBean, SharedPreferences sharedPref) {

        SharedStructureData.relatedPropertyHashTable.clear();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_DATE, String.valueOf(new Date().getTime()));

        if (memberDataBean != null) {
            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(Long.valueOf(memberDataBean.getId()));
            if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                Gson gson = new Gson();
                MemberAdditionalInfoDataBean memberAdditionalInfo = gson.fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (memberAdditionalInfo.getLastServiceLongDate() != null && memberAdditionalInfo.getLastServiceLongDate() > 0) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LAST_SERVICE_DATE, memberAdditionalInfo.getLastServiceLongDate().toString());
                }
            }

            List<FieldValueMobDataBean> labelDataBeanList = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("diseaseHistoryList");
            for (FieldValueMobDataBean data : labelDataBeanList) {
                if (data.getValue().equalsIgnoreCase(LabelConstants.NONE)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_HISTORY, String.valueOf(data.getIdOfValue()));
                }
            }
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberDataBean.getUniqueHealthId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_NAME, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PHYSICAL_ACTIVITY, "2");
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANY_DISEASE, "2");
            SharedStructureData.relatedPropertyHashTable.put("observations", "NONE");
            SharedStructureData.relatedPropertyHashTable.put("otherObservations", "NONE");
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                    UtilBean.getMemberFullName(memberDataBean) + "(" + memberDataBean.getUniqueHealthId() + ")"); //for showing purpose...in worklog
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.OBSERVATIONS, "NONE");
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.OTHER_OBSERVATIONS, "NONE");
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_HISTORY, "NONE");

            if (memberBean.getWeight() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.WEIGHT, memberBean.getWeight().toString());
            }

            if (memberDataBean.getGender() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER,
                        UtilBean.getGenderLabelFromValue(memberDataBean.getGender()));
            }

            if (memberDataBean.getMobileNumber() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, memberDataBean.getMobileNumber());
            }

            if (memberDataBean.getDob() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, memberDataBean.getDob().toString());
                int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(memberDataBean.getDob());
                String tmpDataObj = UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2]);
                if (tmpDataObj != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY, tmpDataObj);
                }
            }

            if (memberDataBean.getCbacDate() != null && new Date(memberDataBean.getCbacDate()).after(UtilBean.getStartOfFinancialYear(null))) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CBAC_DONE, "1");
            }

            if (memberDataBean.getCbacDate() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CBAC_DATE, String.valueOf(memberDataBean.getCbacDate()));
            }

            if (memberDataBean.getIsPregnantFlag() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_PREGNANT, UtilBean.returnYesNoNotAvailableFromBoolean(memberDataBean.getIsPregnantFlag()));
            }

            if (memberDataBean.getLmpDate() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LMP_DATE, String.valueOf(memberBean.getLmpDate().getTime()));
            }

            if (memberBean.getCbacScore() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CBAC_SCORE, memberBean.getCbacScore().toString());
            }

            if (memberDataBean.getAdditionalInfo() != null && !memberDataBean.getAdditionalInfo().isEmpty()) {
                MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (memberAdditionalInfoDataBean.getDiseaseHistory() != null) {
                    String[] split = memberAdditionalInfoDataBean.getDiseaseHistory().split(GlobalTypes.COMMA);
                    List<Integer> diseaseIndexList = new ArrayList<>();
                    List<String> diseaseValue = new ArrayList<>();
                    for (String disease : split) {
                        diseaseIndexList.add(Integer.valueOf(disease));
                    }

                    List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("diseaseHistoryList");
                    List<FieldValueMobDataBean> selectedDisease = new ArrayList<>();
                    for (FieldValueMobDataBean data : labelDataBeans) {
                        int index = diseaseIndexList.indexOf(data.getIdOfValue());
                        if (index != -1) {
                            selectedDisease.add(data);
                        }
                    }
                    for (FieldValueMobDataBean disease : selectedDisease) {
                        if (disease.getValue().equals("Other")) {
                            diseaseValue.add(memberAdditionalInfoDataBean.getOtherDiseaseHistory());
                        } else {
                            diseaseValue.add(disease.getValue());
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.COMPLICATION_LIST, UtilBean.stringListJoin(diseaseValue, GlobalTypes.COMMA));
                }
                if (memberAdditionalInfoDataBean.getHeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEIGHT, memberAdditionalInfoDataBean.getHeight().toString());
                }
            }
        }

        if (familyDataBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ACTUAL_ID, familyDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, UtilBean.getFamilyFullAddress(familyDataBean));

            if (familyDataBean.getAnyMemberCbacDone() != null && familyDataBean.getAnyMemberCbacDone()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANY_MEMBER_CBAC_DONE, "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANY_MEMBER_CBAC_DONE, "2");
            }
            if (familyDataBean.getReligion() != null) {
                String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                if (religion != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
                }
            }
            if (familyDataBean.getCaste() != null) {
                String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                if (caste != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
                }
            }
            if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, "Family is BPL");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, "Family is not BPL");
            }

            if (familyDataBean.getTypeOfHouse() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TYPE_OF_HOUSE, familyDataBean.getTypeOfHouse());
            }

            if (familyDataBean.getTypeOfToilet() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOILET_AVAILABILITY, familyDataBean.getTypeOfToilet());
            }

            if (familyDataBean.getDrinkingWaterSource() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DRINKING_WATER_SOURCE, familyDataBean.getDrinkingWaterSource().toString());
            }

            if (familyDataBean.getFuelForCooking() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TYPE_OF_FUEL, familyDataBean.getFuelForCooking());
            }

            if (familyDataBean.getElectricityAvailability() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELECTRIC_CONNECTION, familyDataBean.getElectricityAvailability());
            }

            if (familyDataBean.getVehicleDetails() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TYPE_OF_VEHICLE, familyDataBean.getVehicleDetails());
            }

            if (familyDataBean.getHouseOwnershipStatus() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HOUSE_OWNERSHIP, familyDataBean.getHouseOwnershipStatus());
            }

            if (familyDataBean.getAnnualIncome() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ANNUAL_INCOME, familyDataBean.getAnnualIncome());
            }
        }
    }

    public void setMetaDataForWeeklyVisitForms(MemberDataBean memberDataBean, FamilyDataBean familyDataBean, MemberMoConfirmedDataBean moConfirmedDataBean, List<String> basicMedicines, String visitType, SharedPreferences sharedPref) {

        SharedStructureData.relatedPropertyHashTable.clear();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        String diseaseStatus = "";
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CLINIC_DATE, sdf.format(new Date().getTime()));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_DATE, String.valueOf(new Date().getTime()));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VISIT_TYPE, visitType);

        if (moConfirmedDataBean != null) {
            if (moConfirmedDataBean.getReferenceDue() != null && moConfirmedDataBean.getReferenceDue()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CLINIC_TYPE, "On call reference");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CLINIC_TYPE, "Village clinic");
            }

            if (moConfirmedDataBean.getMoComment() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MO_COMMENT, moConfirmedDataBean.getMoComment());
            }

            if (moConfirmedDataBean.getConfirmedForDiabetes() != null && moConfirmedDataBean.getConfirmedForDiabetes()) {
                diseaseStatus += "Diabetes - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getDiabetesTreatmentStatus());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DIABETES_CONFIRMED, "1");
            } else if (moConfirmedDataBean.getConfirmedForDiabetes() != null && !moConfirmedDataBean.getConfirmedForDiabetes()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DIABETES_CONFIRMED, "2");
            }

            if (moConfirmedDataBean.getConfirmedForHypertension() != null && moConfirmedDataBean.getConfirmedForHypertension()) {
                String hypertensionStatus = "Hypertension - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getHypertensionTreatmentStatus());
                diseaseStatus += diseaseStatus.isEmpty() ? hypertensionStatus : "\n" + hypertensionStatus;
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HYPERTENSION_CONFIRMED, "1");
            } else if (moConfirmedDataBean.getConfirmedForHypertension() != null && !moConfirmedDataBean.getConfirmedForHypertension()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HYPERTENSION_CONFIRMED, "2");
            }

            if (moConfirmedDataBean.getConfirmedForMentalHealth() != null && moConfirmedDataBean.getConfirmedForMentalHealth()) {
                String mentalHealthStatus = "Mental Health - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getMentalHealthTreatmentStatus());
                diseaseStatus += diseaseStatus.isEmpty() ? mentalHealthStatus : "\n" + mentalHealthStatus;
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MENTAL_HEALTH_CONFIRMED, "1");
            } else if (moConfirmedDataBean.getConfirmedForMentalHealth() != null && !moConfirmedDataBean.getConfirmedForMentalHealth()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MENTAL_HEALTH_CONFIRMED, "2");
            }

            if (!diseaseStatus.isEmpty()) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CONFIRMED_DISEASE, diseaseStatus);
            }

            if (moConfirmedDataBean.getDiabetesDetails() != null && !moConfirmedDataBean.getDiabetesDetails().isEmpty()) {
                String sb = "";
                for (NcdDiabetesDetailDataBean diabetesBean : moConfirmedDataBean.getDiabetesDetails()) {
                    sb = sb + "\n" + sdf.format(diabetesBean.getScreeningDate()) + " - " + diabetesBean.getBloodSugar();
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_DIABETES, sb);
            }
            if (moConfirmedDataBean.getHypertensionDetails() != null && !moConfirmedDataBean.getHypertensionDetails().isEmpty()) {
                String sb = "";
                for (NcdHypertensionDetailDataBean hypertensionBean : moConfirmedDataBean.getHypertensionDetails()) {
                    sb = sb + "\n" + sdf.format(hypertensionBean.getScreeningDate()) + " - " + hypertensionBean.getSystolicBp()
                            + " / " + hypertensionBean.getDiastolicBp() + " / " + hypertensionBean.getPulseRate();
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_HYPERTENSION, sb);
            }
            if (moConfirmedDataBean.getMentalHealthDetails() != null && !moConfirmedDataBean.getMentalHealthDetails().isEmpty()) {
                String sb = "";
                for (NcdMentalHealthDetailDataBean mentalHealthBean : moConfirmedDataBean.getMentalHealthDetails()) {
                    if (mentalHealthBean.getTalk() != null && mentalHealthBean.getOwnDailyWork() != null && mentalHealthBean.getSocialWork() != null
                            && mentalHealthBean.getUnderstanding() != null) {
                        sb = sb + "\n" + sdf.format(mentalHealthBean.getScreeningDate()) + " - " + mentalHealthBean.getTalk()
                                + " / " + mentalHealthBean.getOwnDailyWork() + " / " + mentalHealthBean.getSocialWork()
                                + " / " + mentalHealthBean.getUnderstanding();
                    }
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_MENTAL_HEALTH, sb);
            }

            List<Integer> prescribedMedicineIds = new ArrayList<>();
            SharedStructureData.additionalMedicineList.clear();
            SharedStructureData.prescribedMedicineList.clear();
            if (moConfirmedDataBean.getMedicineDetails() != null && !moConfirmedDataBean.getMedicineDetails().isEmpty()) {
                SharedStructureData.totalMemberMedicineCount = moConfirmedDataBean.getMedicineDetails().size();
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_MEDICINE, String.valueOf(moConfirmedDataBean.getMedicineDetails().size()));

                int count = 0;
                String previousMedicineDetails = "";
                for (NcdMemberMedicineDataBean medicineDataBean : moConfirmedDataBean.getMedicineDetails()) {
                    String medicineDetail = medicineDataBean.getMedicineName() + " (" + medicineDataBean.getFrequency()
                            + " times) - " + medicineDataBean.getDuration() + " Days.";
                    previousMedicineDetails += previousMedicineDetails.isEmpty() ? medicineDetail : "\n" + medicineDetail;
                    prescribedMedicineIds.add(medicineDataBean.getMedicineId());
                    DrugInventoryBean drugDetail = ncdService.retrieveDrugByMedicineId(medicineDataBean.getMedicineId());
                    MedicineListItemDataBean medicineListItemDataBean;
                    if (drugDetail != null) {
                        medicineListItemDataBean = new MedicineListItemDataBean(medicineDataBean.getMedicineId(),
                                medicineDataBean.getMedicineName(), medicineDataBean.getFrequency(), medicineDataBean.getDuration(),
                                medicineDataBean.getQuantity(), medicineDataBean.getSpecialInstruction(), drugDetail.getBalanceInHand(), medicineDataBean.getExpiryDate());
                    } else {
                        medicineListItemDataBean = new MedicineListItemDataBean(medicineDataBean.getMedicineId(),
                                medicineDataBean.getMedicineName(), medicineDataBean.getFrequency(), medicineDataBean.getDuration(),
                                medicineDataBean.getQuantity(), medicineDataBean.getSpecialInstruction(), 0, medicineDataBean.getExpiryDate());
                    }
                    SharedStructureData.prescribedMedicineList.add(medicineListItemDataBean);
                    SharedStructureData.prescribedMedicineMap.put(medicineListItemDataBean.getMedicineId(), medicineDataBean);
                    count++;
                }
                if (!previousMedicineDetails.isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_MEDICINES, previousMedicineDetails);
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PREVIOUS_MEDICINES, LabelConstants.NOT_AVAILABLE);
                }
            } else {
                SharedStructureData.totalMemberMedicineCount = 0;
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOTAL_MEDICINE, "0");
            }

            if (basicMedicines != null && !basicMedicines.isEmpty()) {
                List<OptionDataBean> optionDataBeans = new ArrayList<>();
                try {
                    for (String medicine : basicMedicines) {
                        ListValueBean drugListValue = listValueBeanDao.queryBuilder()
                                .where().eq("value", medicine).queryForFirst();
                        if (drugListValue != null) {
                            DrugInventoryBean drugDetail = ncdService.retrieveDrugByMedicineId(drugListValue.getIdOfValue());
                            if (drugDetail != null && drugDetail.getBalanceInHand() > 0 && !prescribedMedicineIds.contains(drugDetail.getMedicineId())) {
                                OptionDataBean optionDataBean = new OptionDataBean();
                                optionDataBean.setKey(String.valueOf(drugDetail.getMedicineId()));
                                optionDataBean.setValue(drugDetail.getMedicineName());
                                optionDataBeans.add(optionDataBean);
                                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DRUGS, "1");

                                MedicineListItemDataBean medicineListItemDataBean = new MedicineListItemDataBean(drugDetail.getMedicineId(),
                                        drugDetail.getMedicineName(), null, null,
                                        null, null, drugDetail.getBalanceInHand(), null);
                                SharedStructureData.additionalMedicineList.add(medicineListItemDataBean);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                SharedStructureData.basicMedicineList = optionDataBeans;
            }
        }

        if (memberDataBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberDataBean.getUniqueHealthId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_NAME, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                    UtilBean.getMemberFullName(memberDataBean) + "(" + memberDataBean.getUniqueHealthId() + ")"); //for showing purpose...in worklog

            if (memberDataBean.getWeight() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.WEIGHT, memberDataBean.getWeight().toString());
            }

            if (memberDataBean.getGender() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER,
                        UtilBean.getGenderLabelFromValue(memberDataBean.getGender()));
            }

            if (memberDataBean.getMobileNumber() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, memberDataBean.getMobileNumber());
            }

            if (memberDataBean.getDob() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, memberDataBean.getDob().toString());
                int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(memberDataBean.getDob());
                String tmpDataObj = UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2]);
                if (tmpDataObj != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY, tmpDataObj);
                }
            }
            if (memberDataBean.getAdditionalInfo() != null && !memberDataBean.getAdditionalInfo().isEmpty()) {
                MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (memberAdditionalInfoDataBean.getHeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEIGHT, memberAdditionalInfoDataBean.getHeight().toString());
                }
            }
            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(Long.valueOf(memberDataBean.getId()));
            SharedStructureData.currentRchMemberBean = memberBean;
            SharedStructureData.currentRchFamilyDataBean = familyDataBean;
        }

        if (familyDataBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ACTUAL_ID, familyDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, UtilBean.getFamilyFullAddress(familyDataBean));

            if (familyDataBean.getReligion() != null) {
                String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                if (religion != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
                }
            }
            if (familyDataBean.getCaste() != null) {
                String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                if (caste != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
                }
            }
        }
    }

    public void setMetaDataForNPCBForms(Long memberId) {
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
        SharedStructureData.relatedPropertyHashTable.clear();

        MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(memberId);
        if (memberBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberBean.getActualId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberBean.getActualId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_NAME, UtilBean.getMemberFullName(memberBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberBean));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberBean.getUniqueHealthId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, memberBean.getFamilyId());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PHONE_NUMBER, memberBean.getMobileNumber());
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                    UtilBean.getMemberFullName(memberBean) + "(" + memberBean.getUniqueHealthId() + ")");
            if (memberBean.getDob() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, String.valueOf(memberBean.getDob().getTime()));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB_DISPLAY, sdf.format(memberBean.getDob()));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY, UtilBean.getAgeDisplayOnGivenDate(memberBean.getDob(), new Date()));
            }
            String tmpObj = FullFormConstants.getFullFormOfGender(memberBean.getGender());
            if (tmpObj != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENDER, tmpObj);
            }
            if (memberBean.getAdditionalInfo() != null) {
                MemberAdditionalInfoDataBean additionalInfo = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (additionalInfo.getBloodSugar() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BLOOD_SUGAR, additionalInfo.getBloodSugar().toString());
                }
            }

            FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberBean.getFamilyId());
            if (familyDataBean != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ACTUAL_ID, familyDataBean.getId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AREA_ID, familyDataBean.getAreaId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, UtilBean.getFamilyFullAddress(familyDataBean));
                if (familyDataBean.getReligion() != null) {
                    String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                    if (religion != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
                    }
                }
                if (familyDataBean.getCaste() != null) {
                    String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                    if (caste != null) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
                    }
                }
            }
        }
    }

    public void setMetaDataForOtherNotificationsForm(NotificationMobDataBean notification) {
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
        SharedStructureData.relatedPropertyHashTable.clear();
        SharedStructureData.membersUnderTwenty.clear();
        SharedStructureData.selectedHealthInfra = null;

        if (notification != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NOTIFICATION_ID, notification.getId().toString());

            if (notification.getScheduledDate() != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IRON_SUCROSE_DUE_DATE, sdf.format(new Date(notification.getScheduledDate())));
            }

            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(notification.getMemberId());
            if (memberBean != null) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberBean.getActualId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ACTUAL_ID, memberBean.getActualId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, UtilBean.getMemberFullName(memberBean));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_NAME, UtilBean.getMemberFullName(memberBean));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, memberBean.getUniqueHealthId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, memberBean.getFamilyId());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PHONE_NUMBER, memberBean.getMobileNumber());
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                        UtilBean.getMemberFullName(memberBean) + "(" + memberBean.getUniqueHealthId() + ")");
                if (memberBean.getDob() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB, String.valueOf(memberBean.getDob().getTime()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DOB_DISPLAY, sdf.format(memberBean.getDob()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_DISPLAY,
                            UtilBean.getAgeDisplayOnGivenDate(memberBean.getDob(), new Date()));
                }
                if (memberBean.getLmpDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LMP_DATE, String.valueOf(memberBean.getLmpDate().getTime()));
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LMP_DATE_DISPLAY, sdf.format(memberBean.getLmpDate()));
                }
                if (memberBean.getLastDeliveryDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DELIVERY_DATE_DISPLAY, sdf.format(memberBean.getLastDeliveryDate()));
                }
                if (memberBean.getImmunisationGiven() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IMMUNISATION,
                            memberBean.getImmunisationGiven().replace("#", " - ").replace(",", "\n"));

                    if (memberBean.getImmunisationGiven().contains(RchConstants.TT1)) {
                        String[] splitArray = memberBean.getImmunisationGiven().split(",");
                        for (String split : splitArray) {
                            String[] split1 = split.split("#");
                            if (split1[0].equals(RchConstants.TT1)) {
                                try {
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TT_1_GIVEN_ON,
                                            String.valueOf(sdf.parse(split1[1]).getTime()));
                                } catch (ParseException e) {
                                    Log.e(getClass().getSimpleName(), null, e);
                                }
                            }
                        }
                    }
                }

                FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberBean.getFamilyId());
                if (familyDataBean != null) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ACTUAL_ID, familyDataBean.getId());
                    if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, "Family is BPL");
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_BPL, "Family is not BPL");
                    }
                    if (familyDataBean.getReligion() != null) {
                        String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                        if (religion != null) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RELIGION, religion);
                        }
                    }
                    if (familyDataBean.getCaste() != null) {
                        String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                        if (caste != null) {
                            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CASTE, caste);
                        }
                    }
                    if (familyDataBean.getAreaId() != null) {
                        try {
                            LocationMasterBean locationMasterBean = locationMasterBeanDao.queryBuilder().where().eq("actualID", familyDataBean.getAreaId()).queryForFirst();
                            if (locationMasterBean != null) {
                                String fhwDetailString = locationMasterBean.getFhwDetailString();
                                if (fhwDetailString != null) {
                                    Type type = new TypeToken<List<Map<String, String>>>() {
                                    }.getType();
                                    List<Map<String, String>> fhwDetailMapList = new Gson().fromJson(fhwDetailString, type);
                                    Map<String, String> fhwDetailMap = fhwDetailMapList.get(0);
                                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ASHA_INFO, fhwDetailMap.get("name") + " (" + fhwDetailMap.get("mobileNumber") + ")");
                                }
                            }
                        } catch (SQLException e) {
                            Log.e(getClass().getSimpleName(), null, e);
                        }
                    }
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, UtilBean.getFamilyFullAddress(familyDataBean));
                }
            }
        }
    }

    public void setMetaDataForCovidTravellersForm(CovidTravellersInfoBean covidTravellersInfoBean, NotificationMobDataBean notificationMobDataBean) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SharedStructureData.relatedPropertyHashTable.clear();

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ACTUAL_ID, covidTravellersInfoBean.getActualId().toString());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME, covidTravellersInfoBean.getName());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, covidTravellersInfoBean.getName());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS, covidTravellersInfoBean.getAddress());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, covidTravellersInfoBean.getMobileNumber());
        if (covidTravellersInfoBean.getDateOfDeparture() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TRAVEL_DATE, String.valueOf(covidTravellersInfoBean.getDateOfDeparture().getTime()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TRAVEL_DATE_DISPLAY, sdf.format(covidTravellersInfoBean.getDateOfDeparture()));
        }
        if (covidTravellersInfoBean.getDateOfReceipt() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.INFO_RECEIVED_DATE, String.valueOf(covidTravellersInfoBean.getDateOfReceipt().getTime()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.INFO_RECEIVED_DATE_DISPLAY, sdf.format(covidTravellersInfoBean.getDateOfReceipt()));
        }
        if (covidTravellersInfoBean.getPinCode() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.PIN_CODE, covidTravellersInfoBean.getPinCode().toString());
        }
        if (covidTravellersInfoBean.getTrackingStartDate() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TRACKING_START_DATE, covidTravellersInfoBean.getTrackingStartDate().toString());
            int numberOfDays = UtilBean.getNumberOfDays(covidTravellersInfoBean.getTrackingStartDate(), new Date());
            if (numberOfDays < 14)
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TRACKING_DAY, numberOfDays + " of 14");
        }
        if (notificationMobDataBean != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.NOTIFICATION_ID, notificationMobDataBean.getId().toString());
        }
    }
}
