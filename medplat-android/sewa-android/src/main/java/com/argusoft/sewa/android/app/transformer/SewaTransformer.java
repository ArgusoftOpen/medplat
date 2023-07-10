package com.argusoft.sewa.android.app.transformer;

import com.argusoft.sewa.android.app.databean.ComponentTagBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.MigratedFamilyDataBean;
import com.argusoft.sewa.android.app.databean.MigratedMembersDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.databean.SurveyLocationMobDataBean;
import com.argusoft.sewa.android.app.databean.UserInfoDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.PageFormBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.QuestionBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kelvin
 */
public class SewaTransformer {

    public static LoginBean loginBean;
    private static SewaTransformer transformer;

    private SewaTransformer() {
    }

    public static SewaTransformer getInstance() {
        if (transformer == null) {
            transformer = new SewaTransformer();
        }
        return transformer;
    }

    public LoginBean convertUserInfoDataBeanToLoginBeanModel(UserInfoDataBean userInfoDataBean, LoginBean myLoginBean, String password) {
        if (myLoginBean == null) {
            myLoginBean = new LoginBean();
            myLoginBean.setLastUpdateOfAnnouncements(0L);
            myLoginBean.setLastUpdateOfInventory(0L);
            myLoginBean.setLastUpdateOfLabels(0L);
            myLoginBean.setLastUpdateOfListValues(0L);
            myLoginBean.setFirstLogin(true);
            myLoginBean.setUpdateCounter(1);
        }

        myLoginBean.setLoggedIn(true);
        myLoginBean.setHidden(false);

//        if (userInfoDataBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_ASHA)) {
//            myLoginBean.setUserRoleCode(GlobalTypes.USER_ROLE_CODE_ASHA);
//        } else if (userInfoDataBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_FHW)) {
//            myLoginBean.setUserRoleCode(GlobalTypes.USER_ROLE_CODE_FHW);
//        } else if (userInfoDataBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_KIOSK)) {
//            myLoginBean.setUserRoleCode(GlobalTypes.USER_ROLE_CODE_KIOSK);
//        }

        myLoginBean.setFirstName(userInfoDataBean.getfName());
        myLoginBean.setLanguageCode(userInfoDataBean.getLanguageCode());
        myLoginBean.setPassword(password);
        myLoginBean.setPhoneNoOfPM(userInfoDataBean.getPhNoOfPM());
        myLoginBean.setServerDate(userInfoDataBean.getServerDate());
        myLoginBean.setUserID(Long.valueOf(userInfoDataBean.getId()));
        myLoginBean.setUserRole(userInfoDataBean.getUserRole());
        myLoginBean.setUserToken(userInfoDataBean.getUserToken());
        myLoginBean.setUsername(userInfoDataBean.getUsername());
        myLoginBean.setNagarpalikaUser(userInfoDataBean.isNagarPalikaUser());

        if (userInfoDataBean.getCurrentVillageCode() != null) {
            int[] currentVillageCode = userInfoDataBean.getCurrentVillageCode();
            myLoginBean.setVillageCode(UtilBean.arrayJoinToString(currentVillageCode, GlobalTypes.STRING_LIST_SEPARATOR));
        }

        if (userInfoDataBean.getCurrentAssignedVillagesName() != null) {
            String[] currentAssignedVillagesName = userInfoDataBean.getCurrentAssignedVillagesName();
            myLoginBean.setVillageName(UtilBean.arrayJoinToString(currentAssignedVillagesName, GlobalTypes.STRING_LIST_SEPARATOR));
        }

        return myLoginBean;
    }

    public List<LocationBean> convertLocationDataBeanToLocationModel(List<SurveyLocationMobDataBean> locMobBeans, List<LocationBean> locModelList) {
        if (locModelList == null) {
            locModelList = new ArrayList<>();
        }
        if (locMobBeans != null && !locMobBeans.isEmpty()) {
            for (SurveyLocationMobDataBean locBean : locMobBeans) {
                LocationBean loc = new LocationBean();
                loc.setActualID(locBean.getId());
                loc.setName(locBean.getName());
                loc.setParent(locBean.getParent());
                loc.setLevel(locBean.getLevel());
                loc.setIsActive(locBean.getIsActive());
                loc.setLgdCode(locBean.getLgdCode());
                locModelList.add(loc);
            }
        }
        return locModelList;
    }

    public List<NotificationBean> convertNotificationDataBeanToNotificationBeanModel(List<NotificationBean> notificationBeans, List<NotificationMobDataBean> notificationMobDataBeans) {
        if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
            if (notificationBeans == null) {
                notificationBeans = new ArrayList<>();
            }
            Calendar cal = Calendar.getInstance();
            for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
                NotificationBean notificationBean = new NotificationBean();
                notificationBean.setBeneficiaryId(notificationMobDataBean.getBeneficiaryId());
                notificationBean.setBeneficiaryName(notificationMobDataBean.getBeneficiaryName());
                //translating village name acc. to login user language
                if (notificationBean.getBeneficiaryName() != null) {
                    String[] splitNameNVillage = notificationBean.getBeneficiaryName().split("\\(");
                    if (splitNameNVillage.length > 1) {
                        String[] villageName = splitNameNVillage[1].split("\\)");
                        if (villageName.length > 0) {
                            notificationBean.setBeneficiaryName(splitNameNVillage[0] + "(" + UtilBean.getMyLabel(villageName[0]) + ")");
                        }
                    }
                }
                if (notificationMobDataBean.getExpectedDueDate() != null) {
                    cal.setTimeInMillis(notificationMobDataBean.getExpectedDueDate());
                    UtilBean.clearTimeFromDate(cal);
                    notificationBean.setDateOfNotification(cal.getTime());
                }
                if (notificationMobDataBean.getExpiryDate() != null) {
                    notificationBean.setExpiryDate(new Date(notificationMobDataBean.getExpiryDate()));
                }
                notificationBean.setExpectedDueDateString(notificationMobDataBean.getExpectedDueDateString());
                notificationBean.setId(null);
                notificationBean.setNoOfNotificationType(notificationMobDataBean.getNoOfNotificationType());
                notificationBean.setNotificationCode(notificationMobDataBean.getTask());
                notificationBean.setNotificationId(notificationMobDataBean.getId());
                if (notificationMobDataBean.getIsChild() != null) {
                    if (notificationMobDataBean.getIsChild().equals(Boolean.TRUE)) {
                        notificationBean.setNotificationOf(GlobalTypes.CLIENT_IS_CHILD);
                    } else {
                        notificationBean.setNotificationOf(GlobalTypes.CLIENT_IS_MOTHER);
                    }
                }
                notificationBean.setOverdueFlag(notificationMobDataBean.getOverdueFlag());
                notificationBean.setCustomField(notificationMobDataBean.getCustomField());
                if (notificationMobDataBean.getOverDueDate() == null) {
                    notificationMobDataBean.setOverDueDate(notificationMobDataBean.getExpectedDueDate());
                }
                notificationBean.setOverDueDate(notificationMobDataBean.getOverDueDate());
                if (notificationMobDataBean.getDiagnosisDate() != null) {
                    notificationBean.setDiagnosisDate(notificationMobDataBean.getDiagnosisDate());
                }
                if (notificationMobDataBean.getAshaId() != null) {
                    notificationBean.setAshaId(notificationMobDataBean.getAshaId());
                }
                if (notificationMobDataBean.getAshaName() != null) {
                    notificationBean.setAshaName(notificationMobDataBean.getAshaName());
                }
                if (notificationMobDataBean.getPositiveFindings() != null) {
                    notificationBean.setPositiveFindings(notificationMobDataBean.getPositiveFindings());
                }
                notificationBean.setLocationId(notificationMobDataBean.getLocationId());
                notificationBean.setMemberId(notificationMobDataBean.getMemberId());
                notificationBean.setFamilyId(notificationMobDataBean.getFamilyId());
                notificationBean.setOtherDetails(notificationMobDataBean.getOtherDetails());
                notificationBean.setMigrationId(notificationMobDataBean.getMigrationId());
                notificationBean.setModifiedOn(new Date(notificationMobDataBean.getModifiedOn()));
                notificationBean.setState(notificationMobDataBean.getState());
                notificationBean.setHeader(notificationMobDataBean.getHeader());
                if (notificationMobDataBean.getScheduledDate() != null) {
                    notificationBean.setScheduledDate(new Date(notificationMobDataBean.getScheduledDate()));
                }
                notificationBeans.add(notificationBean);
            }
        }
        return notificationBeans;
    }

    public List<NotificationMobDataBean> convertNotificationModelToNotificationDataBean(List<NotificationBean> notificationBeans, List<NotificationMobDataBean> notificationMobDataBeans) {
        if (notificationBeans != null && !notificationBeans.isEmpty()) {
            if (notificationMobDataBeans == null) {
                notificationMobDataBeans = new ArrayList<>();
            }
            NotificationMobDataBean notificationMobDataBean;
            for (NotificationBean notificationBean : notificationBeans) {
                if (notificationBean != null) {
                    notificationMobDataBean = new NotificationMobDataBean();
                    notificationMobDataBean.setAshaId(notificationBean.getAshaId());
                    notificationMobDataBean.setAshaName(notificationBean.getAshaName());
                    notificationMobDataBean.setBeneficiaryId(notificationBean.getBeneficiaryId());
                    notificationMobDataBean.setBeneficiaryName(notificationBean.getBeneficiaryName());
                    if (notificationBean.getDateOfNotification() != null) {
                        notificationMobDataBean.setExpectedDueDate(notificationBean.getDateOfNotification().getTime());
                    }
                    notificationMobDataBean.setExpectedDueDateString(notificationBean.getExpectedDueDateString());
                    if (notificationBean.getExpiryDate() != null) {
                        notificationMobDataBean.setExpiryDate(notificationBean.getExpiryDate().getTime());
                    }
                    notificationMobDataBean.setId(notificationBean.getNotificationId());
                    if (notificationBean.getNotificationOf() != null) {
                        if (notificationBean.getNotificationOf().equalsIgnoreCase(GlobalTypes.CLIENT_IS_CHILD)) {
                            notificationMobDataBean.setIsChild(Boolean.TRUE);
                        } else {
                            notificationMobDataBean.setIsChild(Boolean.FALSE);
                        }
                    }
                    notificationMobDataBean.setNoOfNotificationType(notificationBean.getNoOfNotificationType());
                    notificationMobDataBean.setOverdueFlag(notificationBean.getOverdueFlag());
                    notificationMobDataBean.setTask(notificationBean.getNotificationCode());
                    notificationMobDataBean.setCustomField(notificationBean.getCustomField());
                    if (notificationBean.getOverDueDate() == null && notificationBean.getDateOfNotification() != null) {
                        notificationBean.setOverDueDate(notificationBean.getDateOfNotification().getTime());
                    }
                    notificationMobDataBean.setOverDueDate(notificationBean.getOverDueDate());
                    if (notificationBean.getDiagnosisDate() != null) {
                        notificationMobDataBean.setDiagnosisDate(notificationBean.getDiagnosisDate());
                    }
                    if (notificationBean.getPositiveFindings() != null) {
                        notificationMobDataBean.setPositiveFindings(notificationBean.getPositiveFindings());
                    }
                    notificationMobDataBean.setLocationId(notificationBean.getLocationId());
                    notificationMobDataBean.setFamilyId(notificationBean.getFamilyId());
                    notificationMobDataBean.setMemberId(notificationBean.getMemberId());
                    notificationMobDataBean.setOtherDetails(notificationBean.getOtherDetails());
                    notificationMobDataBean.setMigrationId(notificationBean.getMigrationId());
                    notificationMobDataBean.setModifiedOn(notificationBean.getModifiedOn().getTime());
                    notificationMobDataBean.setState(notificationBean.getState());
                    notificationMobDataBean.setHeader(notificationBean.getHeader());
                    if (notificationBean.getScheduledDate() != null) {
                        notificationMobDataBean.setScheduledDate(notificationBean.getScheduledDate().getTime());
                    }
                    notificationMobDataBeans.add(notificationMobDataBean);
                }
            }
        }
        return notificationMobDataBeans;
    }

    public List<QuestionBean> convertComponentDataBeanToQuestionBeanModel(List<QuestionBean> questionBeans, List<ComponentTagBean> componentTagBeans, String entity) {
        if (componentTagBeans != null && !componentTagBeans.isEmpty()) {
            if (questionBeans == null) {
                questionBeans = new ArrayList<>();
            }
            for (ComponentTagBean componentTagBean : componentTagBeans) {
                QuestionBean questionBean = new QuestionBean();
                questionBean.setQueId(componentTagBean.getId());
                questionBean.setTitle(componentTagBean.getTitle());
                questionBean.setSubtitle(componentTagBean.getSubtitle());
                questionBean.setInstruction(componentTagBean.getInstruction());
                questionBean.setQuestion(componentTagBean.getQuestion());
                questionBean.setType(componentTagBean.getType());
                if (componentTagBean.getIsmandatory() != null && componentTagBean.getIsmandatory().equalsIgnoreCase(GlobalTypes.TRUE)) {
                    questionBean.setIsmandatory(Boolean.TRUE);
                } else {
                    questionBean.setIsmandatory(Boolean.FALSE);
                }
                questionBean.setMandatorymessage(componentTagBean.getMandatorymessage());
                questionBean.setLength(componentTagBean.getLength());
                if (componentTagBean.getValidations() != null && !componentTagBean.getValidations().isEmpty()) {
                    StringBuilder messageArray = new StringBuilder();
                    StringBuilder methodArray = new StringBuilder();
                    boolean isFirst = Boolean.TRUE;
                    for (ValidationTagBean validationTagBean : componentTagBean.getValidations()) {
                        if (isFirst) {
                            messageArray.append(validationTagBean.getMessage());
                            methodArray.append(validationTagBean.getMethod());
                            isFirst = Boolean.FALSE;
                        } else {
                            messageArray.append(GlobalTypes.STRING_LIST_SEPARATOR);
                            messageArray.append(validationTagBean.getMessage());
                            methodArray.append(GlobalTypes.STRING_LIST_SEPARATOR);
                            methodArray.append(validationTagBean.getMethod());
                        }
                    }
                    questionBean.setValidationmessage(messageArray.toString());
                    questionBean.setValidationmethod(methodArray.toString());
                } else {
                    questionBean.setValidationmessage(null);
                    questionBean.setValidationmethod(null);
                }
                if (componentTagBean.getFormulas() != null && !componentTagBean.getFormulas().isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    boolean isFirst = Boolean.TRUE;
                    for (FormulaTagBean formulaTagBean : componentTagBean.getFormulas()) {
                        if (isFirst) {
                            builder.append(formulaTagBean.getFormulavalue());
                            isFirst = Boolean.FALSE;
                        } else {
                            builder.append(GlobalTypes.STRING_LIST_SEPARATOR);
                            builder.append(formulaTagBean.getFormulavalue());
                        }
                    }
                    questionBean.setFormulass(builder.toString());
                } else {
                    questionBean.setFormulass(null);
                }
                questionBean.setDatamap(componentTagBean.getDatamap());
                if (componentTagBean.getNext() != null && !componentTagBean.getNext().equalsIgnoreCase("")) {
                    questionBean.setNext(Integer.parseInt(componentTagBean.getNext()));
                } else {
                    questionBean.setNext(0);
                }
                questionBean.setSubform(componentTagBean.getSubform());
                questionBean.setRelatedpropertyname(componentTagBean.getRelatedpropertyname());
                if (componentTagBean.getIshidden() != null && componentTagBean.getIshidden().equalsIgnoreCase(GlobalTypes.TRUE)) {
                    questionBean.setIshidden(Boolean.TRUE);
                } else {
                    questionBean.setIshidden(Boolean.FALSE);
                }
                questionBean.setEvent(componentTagBean.getEvent());
                if (componentTagBean.getBinding() != null && componentTagBean.getBinding().trim().length() > 0 && !componentTagBean.getBinding().trim().equalsIgnoreCase("null")) {
                    if (componentTagBean.getBinding().contains(GlobalTypes.DOT_SEPARATOR)) {
                        questionBean.setBinding(componentTagBean.getBinding().substring(0, componentTagBean.getBinding().indexOf(GlobalTypes.DOT_SEPARATOR)));
                    } else {
                        questionBean.setBinding(componentTagBean.getBinding().trim());
                    }
                } else {
                    questionBean.setBinding(String.valueOf(componentTagBean.getId()));
                }
                if (componentTagBean.getPage() != null) {
                    questionBean.setPage(Integer.parseInt(componentTagBean.getPage()));
                } else {
                    questionBean.setPage(0);
                }
                questionBean.setEntity(entity);
                questionBean.setHint(componentTagBean.getHint());
                questionBean.setHelpvideofield(componentTagBean.getHelpvideofield());
                questionBeans.add(questionBean);
            }
        }
        return questionBeans;
    }

    public List<AnswerBean> convertComponentDataBeanToAnswerBeanModel(List<AnswerBean> answerBeans, List<ComponentTagBean> componentTagBeans, String entity) {
        if (componentTagBeans != null && !componentTagBeans.isEmpty()) {
            if (answerBeans == null) {
                answerBeans = new ArrayList<>();
            }
            for (ComponentTagBean componentTagBean : componentTagBeans) {
                if (componentTagBean.getOptions() != null && !componentTagBean.getOptions().isEmpty()) {
                    for (OptionTagBean optionTagBean : componentTagBean.getOptions()) {
                        AnswerBean answerBean = new AnswerBean();
                        answerBean.setKey(optionTagBean.getKey());
                        if (optionTagBean.getNext() != null && !optionTagBean.getNext().equalsIgnoreCase("")) {
                            answerBean.setNext(Integer.parseInt(optionTagBean.getNext()));
                        } else {
                            answerBean.setNext(0);
                        }
                        answerBean.setQueId(componentTagBean.getId());
                        answerBean.setRelatedpropertyname(optionTagBean.getRelatedpropertyname());
                        answerBean.setSubform(optionTagBean.getSubform());
                        answerBean.setValue(optionTagBean.getValue());
                        answerBean.setEntity(entity);
                        answerBeans.add(answerBean);
                    }
                }
            }
        }
        return answerBeans;
    }

    public void convertQuestionBeansToQueFormBeans(List<QuestionBean> questionBeans, List<AnswerBean> answerBeans) {
        if (questionBeans != null && !questionBeans.isEmpty()) {
            SharedStructureData.mapIndexQuestion = new LinkedHashMap<>();
            SharedStructureData.mapIndexPage = new LinkedHashMap<>();
            Map<Integer, List<OptionTagBean>> mapQuestionOptions = new LinkedHashMap<>();
            if (answerBeans != null && !answerBeans.isEmpty()) {
                for (AnswerBean answerBean : answerBeans) {
                    OptionTagBean optionTagBean = new OptionTagBean();
                    optionTagBean.setKey(answerBean.getKey());
                    String next = String.valueOf(answerBean.getNext());
                    if (!next.equalsIgnoreCase("0")) {
                        optionTagBean.setNext(next);
                    }
                    optionTagBean.setRelatedpropertyname(answerBean.getRelatedpropertyname());
                    optionTagBean.setSubform(answerBean.getSubform());
                    optionTagBean.setValue(answerBean.getValue());
                    ArrayList<OptionTagBean> beans = new ArrayList<>();
                    if (mapQuestionOptions.get(answerBean.getQueId()) == null) {
                        mapQuestionOptions.put(answerBean.getQueId(), beans);
                    }
                    List<OptionTagBean> optionTagBeans = mapQuestionOptions.get(answerBean.getQueId());
                    if (optionTagBeans != null) {
                        optionTagBeans.add(optionTagBean);
                    }
                }
            }
            for (QuestionBean questionBean : questionBeans) {
                QueFormBean queFormBean = new QueFormBean();
                if (questionBean.getBinding() != null && questionBean.getBinding().trim().length() > 0 && !questionBean.getBinding().trim().equalsIgnoreCase("null")) {
                    if (questionBean.getBinding().contains(".")) {
                        String[] bindingNoArr = UtilBean.split(questionBean.getBinding().trim(), ".");
                        if (bindingNoArr.length > 0) {
                            queFormBean.setBinding(bindingNoArr[0]);
                        }
                    } else {
                        queFormBean.setBinding(questionBean.getBinding().trim());
                    }
                } else {
                    queFormBean.setBinding(String.valueOf(questionBean.getQueId()));
                }
                queFormBean.setDatamap(questionBean.getDatamap());
                queFormBean.setEvent(questionBean.getEvent());
                queFormBean.setHint(questionBean.getHint());
                queFormBean.setHelpvideofield(questionBean.getHelpvideofield());
                if (questionBean.getFormulass() != null && questionBean.getFormulass().length() > 0) {
                    String formulas = questionBean.getFormulass();
                    String[] splitFormula = UtilBean.split(formulas, GlobalTypes.STRING_LIST_SEPARATOR);
                    List<FormulaTagBean> formulaTagBeans = new ArrayList<>();
                    for (String string : splitFormula) {
                        FormulaTagBean formulaTagBean = new FormulaTagBean();
                        formulaTagBean.setFormulavalue(string);
                        formulaTagBeans.add(formulaTagBean);
                    }
                    queFormBean.setFormulas(formulaTagBeans);
                }
                queFormBean.setId(questionBean.getQueId());
                queFormBean.setInstruction(questionBean.getInstruction());
                if (questionBean.getIshidden() != null && questionBean.getIshidden()) {
                    queFormBean.setIshidden(GlobalTypes.TRUE);
                } else {
                    queFormBean.setIshidden(GlobalTypes.FALSE);
                }
                if (questionBean.getIsmandatory() != null && questionBean.getIsmandatory()) {
                    queFormBean.setIsmandatory(GlobalTypes.TRUE);
                } else {
                    queFormBean.setIsmandatory(GlobalTypes.FALSE);
                }
                queFormBean.setLength(questionBean.getLength());
                queFormBean.setMandatorymessage(questionBean.getMandatorymessage());
                String next = String.valueOf(questionBean.getNext());
                if (!next.equalsIgnoreCase("0")) {
                    queFormBean.setNext(next);
                    queFormBean.setIsNextNull(false);
                } else {
                    queFormBean.setIsNextNull(true);
                }
                queFormBean.setOptions(mapQuestionOptions.get(questionBean.getQueId()));
                String page = String.valueOf(questionBean.getPage());
                if (page.trim().length() > 0 && !page.equalsIgnoreCase("0")) {
                    queFormBean.setPage(page);
                } else {
                    queFormBean.setPage(String.valueOf(queFormBean.getId()));
                }
                queFormBean.setQuestion(questionBean.getQuestion());
                queFormBean.setRelatedpropertyname(questionBean.getRelatedpropertyname());
                queFormBean.setSubform(questionBean.getSubform());
                queFormBean.setSubtitle(questionBean.getSubtitle());
                queFormBean.setTitle(questionBean.getTitle());
                queFormBean.setType(questionBean.getType());
                if (questionBean.getValidationmethod() != null && questionBean.getValidationmethod().length() > 0) {
                    String validationMethods = questionBean.getValidationmethod();
                    String[] splitValidationMethods = UtilBean.split(validationMethods, GlobalTypes.STRING_LIST_SEPARATOR);
                    String validationMessages = questionBean.getValidationmessage();
                    String[] splitValidationMessages = null;
                    if (validationMessages != null) {
                        splitValidationMessages = UtilBean.split(validationMessages, GlobalTypes.STRING_LIST_SEPARATOR);
                    }
                    List<ValidationTagBean> validationTagBeans = new ArrayList<>();
                    for (int i = 0; i < splitValidationMethods.length; i++) {
                        ValidationTagBean validationTagBean = new ValidationTagBean();
                        validationTagBean.setMethod(splitValidationMethods[i]);
                        if (splitValidationMessages != null && splitValidationMessages.length > 0) {
                            validationTagBean.setMessage(splitValidationMessages[i]);
                        }
                        validationTagBeans.add(validationTagBean);
                    }
                    queFormBean.setValidations(validationTagBeans);
                }
                if (queFormBean.getId() != 0) {
                    SharedStructureData.mapIndexQuestion.put(queFormBean.getId(), queFormBean);
                } else {
                    Log.i(getClass().getSimpleName(), "^^^^^^^^^^^^^^^^ Question is not stored in map ^^^^^^^^^^^ no : " + queFormBean.getQuestion());
                }
                // add question into 
                try {
                    int pageNo = Integer.parseInt(queFormBean.getPage().trim());
                    PageFormBean pageFormBean = SharedStructureData.mapIndexPage.get(pageNo);
                    if (pageFormBean == null) {
                        pageFormBean = new PageFormBean(pageNo);
                        SharedStructureData.mapIndexPage.put(pageNo, pageFormBean);
                    }
                    if (pageFormBean.addQuestion(queFormBean)) {
                        Log.i(getClass().getSimpleName(), "question no : " + queFormBean.getId() + "  next is " + queFormBean.getNext() + "  added into pane No : " + pageNo);
                    } else {
                        Log.i(getClass().getSimpleName(), "^^^^^ Question " + queFormBean.getQuestion() + " is not added in page :^^^^^^^^^^^ " + pageNo);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Question  : " + queFormBean.getQuestion() + "  is Not added into page ..........", e);
                }

                /*
                 ====SURVEY LOCATION====
                 Prepare DataMap for first Level of Location
                 */
                if (questionBean.getDatamap() != null && SewaConstants.getLocationLevel().containsKey(questionBean.getDatamap())) {
                    String locationLevel = questionBean.getDatamap();
                    Integer level = SewaConstants.getLocationLevel().get(locationLevel);
                    List<LocationBean> retrieveLocationByLevel = SharedStructureData.sewaService.retrieveLocationByLevel(level);
                    if (retrieveLocationByLevel != null) {
                        List<FieldValueMobDataBean> allLocationOptionByLevel = new ArrayList<>();
                        for (LocationBean loc : retrieveLocationByLevel) {
                            FieldValueMobDataBean locationOptionValue = new FieldValueMobDataBean();
                            locationOptionValue.setIdOfValue(loc.getActualID());
                            locationOptionValue.setValue(loc.getName());
                            allLocationOptionByLevel.add(locationOptionValue);
                        }
                        SharedStructureData.mapDataMapLabelBean.put(locationLevel, allLocationOptionByLevel);
                    }
                }
            } // end loop question 
        }
    }

    public List<FieldValueMobDataBean> convertListValueBeansToFieldValueDataBean(List<ListValueBean> listValueBeans) {
        List<FieldValueMobDataBean> fieldValueMobDataBeans = new ArrayList<>();
        if (listValueBeans != null && !listValueBeans.isEmpty()) {
            for (ListValueBean listValueBean : listValueBeans) {
                fieldValueMobDataBeans.add(convertListValueBeansToFieldValueDataBean(listValueBean));
            }
        }
        return fieldValueMobDataBeans;
    }

    public FieldValueMobDataBean convertListValueBeansToFieldValueDataBean(ListValueBean listValueBean) {
        if (listValueBean != null) {
            FieldValueMobDataBean fieldValueMobDataBean = new FieldValueMobDataBean();
            fieldValueMobDataBean.setField(listValueBean.getField());
            fieldValueMobDataBean.setFieldType(listValueBean.getFieldType());
            fieldValueMobDataBean.setFormCode(listValueBean.getFormCode());
            fieldValueMobDataBean.setIdOfValue(listValueBean.getIdOfValue());
            fieldValueMobDataBean.setLastUpdateOfFieldValue(Long.MIN_VALUE);
            fieldValueMobDataBean.setValue(listValueBean.getValue());
            return fieldValueMobDataBean;
        }
        return null;
    }

    public List<LabelBean> convertQueryMobDataBeanToLabelModel(QueryMobDataBean queryMobDataBean) {
        List<LinkedHashMap<String, Object>> results = queryMobDataBean.getResult();
        List<LabelBean> labelBeans = new ArrayList<>();

        Object tmpObj;
        Object key;
        Object text;
        Object lang;
        Object createdOn;
        for (LinkedHashMap<String, Object> result : results) {
            tmpObj = result.get("custom3b");
            key = result.get("key");
            text = result.get("text");
            lang = result.get("language");
            createdOn = result.get("created_on");
            if (tmpObj != null && tmpObj.equals(Boolean.TRUE)
                    && key != null && text != null && lang != null && createdOn != null) {
                LabelBean labelBean = new LabelBean();
                labelBean.setLabelKey(key.toString());
                labelBean.setLabelValue(text.toString());
                labelBean.setLanguage(lang.toString());
                labelBean.setModifiedOn(new Date(Long.parseLong(createdOn.toString())));
                labelBeans.add(labelBean);
            }
        }
        return labelBeans;
    }

    public MigratedMembersBean convertMigratedMembersDataBeanToBean(MigratedMembersDataBean migratedMembersDataBean) {
        MigratedMembersBean migratedMembersBean = new MigratedMembersBean();

        migratedMembersBean.setMigrationId(migratedMembersDataBean.getMigrationId());
        migratedMembersBean.setMemberId(migratedMembersDataBean.getMemberId());
        migratedMembersBean.setName(migratedMembersDataBean.getName());
        migratedMembersBean.setHealthId(migratedMembersDataBean.getHealthId());
        migratedMembersBean.setHealthId(migratedMembersDataBean.getHealthId());
        migratedMembersBean.setLocationMigratedFrom(migratedMembersDataBean.getLocationMigratedFrom());
        migratedMembersBean.setLocationMigratedTo(migratedMembersDataBean.getLocationMigratedTo());
        migratedMembersBean.setFamilyMigratedFrom(migratedMembersDataBean.getFamilyMigratedFrom());
        migratedMembersBean.setFamilyMigratedTo(migratedMembersDataBean.getFamilyMigratedTo());
        migratedMembersBean.setFromLocationId(migratedMembersDataBean.getFromLocationId());
        migratedMembersBean.setToLocationId(migratedMembersDataBean.getToLocationId());
        migratedMembersBean.setOutOfState(migratedMembersDataBean.getOutOfState());
        migratedMembersBean.setConfirmed(migratedMembersDataBean.getConfirmed());
        migratedMembersBean.setLfu(migratedMembersDataBean.getLfu());

        if (migratedMembersDataBean.getModifiedOn() != null) {
            migratedMembersBean.setModifiedOn(new Date(migratedMembersDataBean.getModifiedOn()));
        }

        return migratedMembersBean;
    }

    public MigratedMembersDataBean convertMigratedMembersBeanToDataBean(MigratedMembersBean migratedMembersBean) {
        MigratedMembersDataBean migratedMembersDataBean = new MigratedMembersDataBean();

        migratedMembersDataBean.setMigrationId(migratedMembersBean.getMigrationId());
        migratedMembersDataBean.setMemberId(migratedMembersBean.getMemberId());
        migratedMembersDataBean.setName(migratedMembersBean.getName());
        migratedMembersDataBean.setHealthId(migratedMembersBean.getHealthId());
        migratedMembersDataBean.setHealthId(migratedMembersBean.getHealthId());
        migratedMembersDataBean.setLocationMigratedFrom(migratedMembersBean.getLocationMigratedFrom());
        migratedMembersDataBean.setLocationMigratedTo(migratedMembersBean.getLocationMigratedTo());
        migratedMembersDataBean.setFamilyMigratedFrom(migratedMembersBean.getFamilyMigratedFrom());
        migratedMembersDataBean.setFamilyMigratedTo(migratedMembersBean.getFamilyMigratedTo());
        migratedMembersDataBean.setFromLocationId(migratedMembersBean.getFromLocationId());
        migratedMembersDataBean.setToLocationId(migratedMembersBean.getToLocationId());
        migratedMembersDataBean.setOutOfState(migratedMembersBean.getOutOfState());
        migratedMembersDataBean.setConfirmed(migratedMembersBean.getConfirmed());
        migratedMembersDataBean.setLfu(migratedMembersBean.getLfu());

        if (migratedMembersBean.getModifiedOn() != null) {
            migratedMembersDataBean.setModifiedOn(migratedMembersBean.getModifiedOn().getTime());
        }

        return migratedMembersDataBean;
    }

    public MigratedFamilyBean convertMigratedFamilyDataBeanToBean(MigratedFamilyDataBean migratedFamilyDataBean) {
        MigratedFamilyBean migratedFamilyBean = new MigratedFamilyBean();

        migratedFamilyBean.setMigrationId(migratedFamilyDataBean.getMigrationId());
        migratedFamilyBean.setFamilyId(migratedFamilyDataBean.getFamilyId());
        migratedFamilyBean.setFamilyIdString(migratedFamilyDataBean.getFamilyIdString());
        migratedFamilyBean.setLocationMigratedFrom(migratedFamilyDataBean.getLocationMigratedFrom());
        migratedFamilyBean.setLocationMigratedTo(migratedFamilyDataBean.getLocationMigratedTo());
        migratedFamilyBean.setFromLocationId(migratedFamilyDataBean.getFromLocationId());
        migratedFamilyBean.setToLocationId(migratedFamilyDataBean.getToLocationId());
        migratedFamilyBean.setOutOfState(migratedFamilyDataBean.getOutOfState());
        migratedFamilyBean.setConfirmed(migratedFamilyDataBean.getIsConfirmed());
        migratedFamilyBean.setLfu(migratedFamilyDataBean.getIsLfu());
        migratedFamilyBean.setSplitFamily(migratedFamilyDataBean.getIsSplitFamily());
        migratedFamilyBean.setsFamilyId(migratedFamilyDataBean.getsFamilyId());
        migratedFamilyBean.setsFamilyIdString(migratedFamilyDataBean.getsFamilyIdString());
        migratedFamilyBean.setSplitMembersDetail(migratedFamilyDataBean.getSplitMembersDetail());

        if (migratedFamilyDataBean.getModifiedOn() != null) {
            migratedFamilyBean.setModifiedOn(new Date(migratedFamilyDataBean.getModifiedOn()));
        }

        return migratedFamilyBean;
    }

    public MigratedFamilyDataBean convertMigratedFamilyBeanToDataBean(MigratedFamilyBean migratedFamilyBean) {
        MigratedFamilyDataBean migratedFamilyDataBean = new MigratedFamilyDataBean();

        migratedFamilyDataBean.setMigrationId(migratedFamilyBean.getMigrationId());
        migratedFamilyDataBean.setFamilyId(migratedFamilyBean.getFamilyId());
        migratedFamilyDataBean.setFamilyIdString(migratedFamilyBean.getFamilyIdString());
        migratedFamilyDataBean.setLocationMigratedFrom(migratedFamilyBean.getLocationMigratedFrom());
        migratedFamilyDataBean.setLocationMigratedTo(migratedFamilyBean.getLocationMigratedTo());
        migratedFamilyDataBean.setFromLocationId(migratedFamilyBean.getFromLocationId());
        migratedFamilyDataBean.setToLocationId(migratedFamilyBean.getToLocationId());
        migratedFamilyDataBean.setOutOfState(migratedFamilyBean.getOutOfState());
        migratedFamilyDataBean.setIsConfirmed(migratedFamilyBean.getConfirmed());
        migratedFamilyDataBean.setIsLfu(migratedFamilyBean.getLfu());
        migratedFamilyDataBean.setIsSplitFamily(migratedFamilyBean.getSplitFamily());
        migratedFamilyDataBean.setsFamilyId(migratedFamilyBean.getsFamilyId());
        migratedFamilyDataBean.setsFamilyIdString(migratedFamilyBean.getsFamilyIdString());
        migratedFamilyDataBean.setSplitMembersDetail(migratedFamilyBean.getSplitMembersDetail());

        if (migratedFamilyBean.getModifiedOn() != null) {
            migratedFamilyDataBean.setModifiedOn(migratedFamilyBean.getModifiedOn().getTime());
        }

        return migratedFamilyDataBean;
    }
}
