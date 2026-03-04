package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.morbidities.beans.BeneficiaryMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.beans.ChildPNCVisitMobDataBean;
import com.argusoft.sewa.android.app.morbidities.beans.ClientPNCVisitMobDataBean;
import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyANCMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyChildCareMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyPNCMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.morbidities.service.ANCMorbiditiesService;
import com.argusoft.sewa.android.app.morbidities.service.ChildCareMorbiditiesService;
import com.argusoft.sewa.android.app.morbidities.service.PNCMorbiditiesService;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class MorbiditiesIdentification {

    public MorbiditiesIdentification() {
        //This will be used to identify Morbidities
    }

    private DataBeanToIdentifyANCMorbidities createDataBeanToIdentifyANCMorbidity(String record) {
        DataBeanToIdentifyANCMorbidities dataBeanToIdentifyANCMorbidities = new DataBeanToIdentifyANCMorbidities();
        String[] questionAnswerPart = UtilBean.split(record, GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        for (String string : questionAnswerPart) {
            String[] property = UtilBean.split(string, GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
            int bindingField = Integer.parseInt(property[0]);
            String fieldValue = property[1];
            setANCMorbidityBeanForBeneficiaryAfterANC(dataBeanToIdentifyANCMorbidities, bindingField, fieldValue);
        }
        setEDPDetailsInANCMorbidityBean(dataBeanToIdentifyANCMorbidities);
        return dataBeanToIdentifyANCMorbidities;
    }

    private void setEDPDetailsInANCMorbidityBean(DataBeanToIdentifyANCMorbidities databeanToIdentifyANCMorbidities) {
        String string = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MARITAL_STATUS);
        if (string != null && !string.equalsIgnoreCase(LabelConstants.NULL)) {
            databeanToIdentifyANCMorbidities.setMaritalStatus(string);
        }
        string = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.AGE_FOR_MORB);
        if (string != null && !string.equalsIgnoreCase(LabelConstants.NULL)) {
            databeanToIdentifyANCMorbidities.setAge(Long.valueOf(string));
        }
        string = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.COMPLICATION_DURING_LAST_PREGNANCY);
        if (string != null && !string.equalsIgnoreCase(LabelConstants.NULL)) {
            //Multi select value must be set here.
            databeanToIdentifyANCMorbidities.setComplicationPresentDuringPreviousPregnancy(UtilBean.getListFromStringBySeparator(string, GlobalTypes.COMMA));
        }
        string = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.GRAVIDA_VALUE);
        if (string != null) {
            if (MorbiditiesConstant.GRAVIDA_MORE_THAN_4.equalsIgnoreCase(SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.GRAVIDA_VALUE))) {
                // here gravida is more than 4 than in condition it will treat as 5
                databeanToIdentifyANCMorbidities.setGravida(5);
            } else {
                databeanToIdentifyANCMorbidities.setGravida(Integer.valueOf(string));
            }
        }
        string = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.GESTATIONAL_WEEK);
        if (string != null) {
            databeanToIdentifyANCMorbidities.setGestationalWeek(Integer.valueOf(string));
        }

        databeanToIdentifyANCMorbidities.setIsFirstAncHomeVisitDone(false);
        String isFirstAncHomeVisitDone = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_FIRST_ANC_HOME_VISIT_DONE);
        if (isFirstAncHomeVisitDone != null && isFirstAncHomeVisitDone.equalsIgnoreCase(GlobalTypes.TRUE)) {
            databeanToIdentifyANCMorbidities.setIsFirstAncHomeVisitDone(true);
        }
    }

    private void setANCMorbidityBeanForBeneficiaryAfterANC(DataBeanToIdentifyANCMorbidities databeanToIdentifyANCMorbidities, int caseNumber, String answer) {
        databeanToIdentifyANCMorbidities.setBeneficiaryType(GlobalTypes.CLIENT_IS_MOTHER);
        switch (caseNumber) {
            case 2:
                databeanToIdentifyANCMorbidities.setBeneficiaryName(answer);
                break;
            case 136:
                databeanToIdentifyANCMorbidities.setHeadache(UtilBean.getBooleanValue(answer));
                break;
            case 137:
                databeanToIdentifyANCMorbidities.setVisionDisturbance(UtilBean.getBooleanValue(answer));
                break;
            case 138:
                databeanToIdentifyANCMorbidities.setHasConvulsion(UtilBean.getBooleanValue(answer));
                break;
            case 139:
                databeanToIdentifyANCMorbidities.setPresenceOfCough(answer);
                break;
            case 140:
                databeanToIdentifyANCMorbidities.setFever(UtilBean.getBooleanValue(answer));
                break;
            case 141:
                databeanToIdentifyANCMorbidities.setChillsOrRigours(UtilBean.getBooleanValue(answer));
                break;
            case 142:
                databeanToIdentifyANCMorbidities.setJaundice(UtilBean.getBooleanValue(answer));
                break;
            case 143:
                databeanToIdentifyANCMorbidities.setSevereJointPain(UtilBean.getBooleanValue(answer));
                break;
            case 145:
                databeanToIdentifyANCMorbidities.setVomiting(UtilBean.getBooleanValue(answer));
                break;
            case 146:
                databeanToIdentifyANCMorbidities.setVaginalBleedingSinceLastVisit(UtilBean.getBooleanValue(answer));
                break;
            case 147:
                databeanToIdentifyANCMorbidities.setBurningUrination(UtilBean.getBooleanValue(answer));
                break;
            case 148:
                databeanToIdentifyANCMorbidities.setVaginalDischarge(answer);
                break;
            case 149:
                databeanToIdentifyANCMorbidities.setLeakingPerVaginally(UtilBean.getBooleanValue(answer));
                break;
            case 150:
                databeanToIdentifyANCMorbidities.setDoYouGetTiredEasily(UtilBean.getBooleanValue(answer));
                break;
            case 151:
                databeanToIdentifyANCMorbidities.setShortOfBreathDuringRoutingHouseholdWork(UtilBean.getBooleanValue(answer));
                break;
            case 152:
                databeanToIdentifyANCMorbidities.setSwellingOfFaceHandsOrFeet(UtilBean.getBooleanValue(answer));
                break;
            case 155:
                databeanToIdentifyANCMorbidities.setConjunctivaAndPalmsPale(UtilBean.getBooleanValue(answer));
                break;
            default:
        }
    }

    public List<BeneficiaryMorbidityDetails> findAvailableANCMorbidities(String record, String nameOfBeneficiary) {
        DataBeanToIdentifyANCMorbidities dtim = createDataBeanToIdentifyANCMorbidity(record);
        List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetails = new ArrayList<>();

        BeneficiaryMorbidityDetails bmd = new BeneficiaryMorbidityDetails();
        List<IdentifiedMorbidityDetails> identifiedMorbidities = new ArrayList<>();
        new ANCMorbiditiesService(dtim, identifiedMorbidities);
        bmd.setIdentifiedMorbidities(identifiedMorbidities);
        bmd.setBeneficiaryName(nameOfBeneficiary);
        bmd.setBeneficiaryType(dtim.getBeneficiaryType());
        // Adding one beneficiary's all morbidity details
        beneficiaryMorbidityDetails.add(bmd);
        return beneficiaryMorbidityDetails;
    }

    private void setPNCHomeVisitBeanForBeneficiary(ClientPNCVisitMobDataBean clientPNCVisitMobDataBean, float caseNumber, String answer) {
        if (clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans() == null) {
            ArrayList<ChildPNCVisitMobDataBean> childPNCVisitMobDataBeans = new ArrayList<>();
            clientPNCVisitMobDataBean.setChildPNCVisitMobDataBeans(childPNCVisitMobDataBeans);
        }
        int intCaseNumber = (int) caseNumber;
        int listIndex;
        listIndex = (int) ((caseNumber - intCaseNumber + 0.00001) * 10);
        if (clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().size() < listIndex + 1) {
            clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().add(new ChildPNCVisitMobDataBean());
        }

        switch (intCaseNumber) {
            /*
             * Mother Related Questions
             */
            case 2:
                clientPNCVisitMobDataBean.setHealthId(answer);
                break;
            case 12:
                //  Is mother alive?
                if (RchConstants.MEMBER_STATUS_AVAILABLE.equals(answer)) {
                    clientPNCVisitMobDataBean.setIsMotherAlive(true);
                } else if (RchConstants.MEMBER_STATUS_DEATH.equals(answer)) {
                    clientPNCVisitMobDataBean.setIsMotherAlive(false);
                }
                break;
            case 126:
                //  Is she present?
                clientPNCVisitMobDataBean.setPresentFlag(UtilBean.getBooleanValue(answer));
                break;
            case 21:
                //  Whether bleeding continues?
                clientPNCVisitMobDataBean.setBleedingFlag(UtilBean.getBooleanValue(answer));
                break;
            case 22:
                //  No. of pads changed in last 24 hours?
                clientPNCVisitMobDataBean.setPadsCount(Integer.valueOf(answer));
                break;
            case 23:
                //  If there is any foul smell discharge?
                clientPNCVisitMobDataBean.setFoulSmellFlag(UtilBean.getBooleanValue(answer));
                break;
            case 25:
                //  Abnormal talks or behaviour observed?
                clientPNCVisitMobDataBean.setAbnormalBehaviourFlag(UtilBean.getBooleanValue(answer));
                break;
            case 26:
                //  "Does the woman have fever?                
                clientPNCVisitMobDataBean.setFeverFlag(UtilBean.getBooleanValue(answer));
                break;
            case 27:
                //  Does the woman have visual disturbances?
                clientPNCVisitMobDataBean.setHeadacheFlag(UtilBean.getBooleanValue(answer));
                break;
            case 28:
                //  Does the woman have difficulty in breastfeeding?
                clientPNCVisitMobDataBean.setBreastfeedingDifficultyFlag(UtilBean.getBooleanValue(answer));
                break;
            case 29:
                //  Does the mother have any of the listed problems? If no, tick 'No problem'
                clientPNCVisitMobDataBean.setProblemPresent(answer);
                break;
            /*
             * Child Related Questions
             */
            case 31:
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setHealthId(answer);
                break;
            case 32:
                //  Name of beneficiary                                  
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setChildName(answer);
                break;
            case 40:
                //  Is the child alive ?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setIsChildAlive(UtilBean.getBooleanValue(answer));
                break;
            case 41:
                //  How is baby's cry?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmCryStatus(answer);
                break;
            case 42:
                //  "Was baby fed less than usual ?"
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmBabyFed(answer);
                break;
            case 43:
                //  How is baby suckling ?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmSucklingStatus(answer);
                break;
            case 44:
                //  Whether baby vomits (throws up milk consecutively after three feeds) ?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmVomitingFlag(UtilBean.getBooleanValue(answer));
                break;
            case 45:
                //  Are baby's hands and feet cold to touch ?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmHandsTouchFlag(UtilBean.getBooleanValue(answer));
                break;
            case 53:
                //  Chest indrawing?
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setmChestIndrawingFlag(UtilBean.getBooleanValue(answer));
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setChestIndrawingFlag(UtilBean.getBooleanValue(answer));
                break;
            case 52:
                //  Skin pustules :
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setSkinPustules(UtilBean.getBooleanValue(answer));
                break;
            case 54:
                //  Umbilicus:
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setUmbilicus(answer);
                break;
            case 55:
                //  Abdomen:
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setAbdomen(answer);
                break;
            case 56:
                //  Temperature (Measure in axilla) :
                //-- -- -- -- -- ---Parse Temperature----------------
                String[] tempParam = UtilBean.split(answer, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (tempParam.length == 1 && tempParam[0].equalsIgnoreCase(GlobalTypes.TRUE)) {
                    clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setTemperature(null);
                } else if (tempParam.length == 3 && tempParam[0].equalsIgnoreCase(GlobalTypes.FALSE)) {
                    float t1 = Integer.parseInt(tempParam[1]);
                    float t2 = Integer.parseInt(tempParam[2]);
                    Float tmp = t1 + (t2 / 10.0f);
                    clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setTemperature(tmp);
                }
                break;
            case 60:
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setWhetherBabyLimbsAndNeckMoreLimpThanBefore(answer);
                break;
            case 61:
                clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(listIndex).setHowAreBabyEyes(answer);
                break;
            default:
        }
    }

    private List<DataBeanToIdentifyPNCMorbidities> parseRecordOfPNC(String recordString) { // pass only question answer string
        ClientPNCVisitMobDataBean clientPNCVisitMobDataBean = new ClientPNCVisitMobDataBean();
        String[] questionAnswerPart = UtilBean.split(recordString, GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        for (String string : questionAnswerPart) {
            String[] property = UtilBean.split(string, GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
            float bindingField = Float.parseFloat(property[0]);
            String fieldValue = property[1];
            setPNCHomeVisitBeanForBeneficiary(clientPNCVisitMobDataBean, bindingField, fieldValue);
        }

        List<DataBeanToIdentifyPNCMorbidities> dataSourcesOfDiffBeneficiary = new ArrayList<>();
        //Now i have data of child and mother in ClientPNCVisitMobDataBean bean and have to filter from that and set to DataBeanToIdentifyPNCMorbidities
        DataBeanToIdentifyPNCMorbidities createDBFToIdentifyPNCMorbiditiesForMother = createDBFToIdentifyPNCMorbiditiesForMother(clientPNCVisitMobDataBean);
        dataSourcesOfDiffBeneficiary.add(createDBFToIdentifyPNCMorbiditiesForMother);
        for (int i = 0; i < clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().size(); i++) {
            ChildPNCVisitMobDataBean cVisitMobDataBean = clientPNCVisitMobDataBean.getChildPNCVisitMobDataBeans().get(i);
            DataBeanToIdentifyPNCMorbidities createDBFToIdentifyPNCMorbiditiesForChild = createDBFToIdentifyPNCMorbiditiesForChild(cVisitMobDataBean);
            createDBFToIdentifyPNCMorbiditiesForChild.setAnyDifficultyInBreastfeeding(clientPNCVisitMobDataBean.getBreastfeedingDifficultyFlag());
            try {
                setOtherDetailsForPNCMorbidity(createDBFToIdentifyPNCMorbiditiesForChild, i);
            } catch (NumberFormatException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
            dataSourcesOfDiffBeneficiary.add(createDBFToIdentifyPNCMorbiditiesForChild);
        }
        return dataSourcesOfDiffBeneficiary;
    }

    private DataBeanToIdentifyPNCMorbidities createDBFToIdentifyPNCMorbiditiesForMother(ClientPNCVisitMobDataBean clientPNCVisitMobDataBean) {
        // here in the case of mother is not alive means (Mother is not alive or mother is reported death then no need to identify morbidities for her)
        DataBeanToIdentifyPNCMorbidities dtipncM = new DataBeanToIdentifyPNCMorbidities();
        dtipncM.setBeneficiaryType(GlobalTypes.CLIENT_IS_MOTHER);
        dtipncM.setHealthId(clientPNCVisitMobDataBean.getHealthId());
        if (clientPNCVisitMobDataBean.getIsMotherAlive() != null) {
            dtipncM.setIsBeneficiaryAlive(clientPNCVisitMobDataBean.getIsMotherAlive());
        } else {
            dtipncM.setIsBeneficiaryAlive(false);
        }
        dtipncM.setNoOfPadsChangedIn24Hours(clientPNCVisitMobDataBean.getPadsCount());//(PNC 12)
        dtipncM.setFoulSmellingDischarge(clientPNCVisitMobDataBean.getFoulSmellFlag());
        dtipncM.setFeverForMother(clientPNCVisitMobDataBean.getFeverFlag());//(PNC 16)
        dtipncM.setAbnormalTalkOrBehaviourOrMoodChanges(clientPNCVisitMobDataBean.getAbnormalBehaviourFlag());
        dtipncM.setHasHeadacheWithVisualDisturbances(clientPNCVisitMobDataBean.getHeadacheFlag());
        dtipncM.setAnyDifficultyInBreastfeeding(clientPNCVisitMobDataBean.getBreastfeedingDifficultyFlag());
        dtipncM.setDoesMotherHaveAnyOfFollowingProblems(UtilBean.getListFromStringBySeparator(clientPNCVisitMobDataBean.getProblemPresent(), GlobalTypes.COMMA));//PNC 19 here needs to separate all the values all multislect
        dtipncM.setDoesMotherHaveAnyOfFollowingOnBREAST(UtilBean.getListFromStringBySeparator(clientPNCVisitMobDataBean.getBreastProblemFlag(), GlobalTypes.COMMA));
        return dtipncM;
    }

    private DataBeanToIdentifyPNCMorbidities createDBFToIdentifyPNCMorbiditiesForChild(ChildPNCVisitMobDataBean childPNCVisitMobDataBean) {
        // here in the case of child is not alive means (child is not alive or child is reported death then no need to identify morbidities for child)
        DataBeanToIdentifyPNCMorbidities dtipncM = new DataBeanToIdentifyPNCMorbidities();

        dtipncM.setHealthId(childPNCVisitMobDataBean.getHealthId());
        dtipncM.setBeneficiaryType(GlobalTypes.CLIENT_IS_CHILD);
        if (childPNCVisitMobDataBean.getIsChildAlive() != null) {
            dtipncM.setIsBeneficiaryAlive(childPNCVisitMobDataBean.getIsChildAlive());
        } else {
            dtipncM.setIsBeneficiaryAlive(false);
        }
        dtipncM.setBeneficiaryName(childPNCVisitMobDataBean.getChildName());

        dtipncM.setBabyHasWateryOrLooseMotion(childPNCVisitMobDataBean.getmLooseMotionFlag());//PNC 28
        dtipncM.setHowIsTheSkinOfTheBaby(UtilBean.getListFromStringBySeparator(childPNCVisitMobDataBean.getSkinStatus(), GlobalTypes.COMMA));//PNC 39
        dtipncM.setIsThereBleedingFromAnyPartOfTheBody(UtilBean.getListFromStringBySeparator(childPNCVisitMobDataBean.getmBleedingStatus(), GlobalTypes.COMMA));//PNC 34 // Multiple select so have to separate
        dtipncM.setAreBabyHandsAndFeetColdToTouch(childPNCVisitMobDataBean.getmHandsTouchFlag());//PNC 31
        dtipncM.setTemperature(childPNCVisitMobDataBean.getTemperature());//PNC 46 remaining in main method
        dtipncM.setRateOfRespiration(childPNCVisitMobDataBean.getRespirationRate());//PNC 43
        dtipncM.setSkinPustules(childPNCVisitMobDataBean.getSkinPustules());//PNC 40
        dtipncM.setUmbilicus(childPNCVisitMobDataBean.getUmbilicus());//PNC 44

        //New 
        dtipncM.setHowsTheBabyConsciousness(childPNCVisitMobDataBean.getmConsciousnessStatus());//(PNC 29)(CBDS)
        dtipncM.setConsciousness(childPNCVisitMobDataBean.getConsciousnessStatus());//(PNC 49)(CBDS)
        dtipncM.setWasBabyFedLessThanUsual(childPNCVisitMobDataBean.getmBabyFed());//(PNC 24)(RB but not boolean)
        dtipncM.setHowIsBabySuckling(childPNCVisitMobDataBean.getmSucklingStatus());//(PNC 25)(CBDS)
        dtipncM.setHowIsBabyCry(childPNCVisitMobDataBean.getmCryStatus());//(PNC 23)(CBDS)
        dtipncM.setCry(childPNCVisitMobDataBean.getCryStatus());//(PNC 50)(CBDS)  
        dtipncM.setAbdomen(childPNCVisitMobDataBean.getAbdomen());//(PNC 45)(CBDS)
        //This question is asking at two place but it is not necessary that it is coming two times but if it comes two times and one of those answer
        // is yes than set yes to this property
        if (Boolean.TRUE.equals(childPNCVisitMobDataBean.getmChestIndrawingFlag())) {
            dtipncM.setChestIndrawing(childPNCVisitMobDataBean.getmChestIndrawingFlag());//(PNC 42)(RB bool)
        } else {
            dtipncM.setChestIndrawing(childPNCVisitMobDataBean.getChestIndrawingFlag());//(PNC 42)(RB bool)
        }
        dtipncM.setWhetherBabyVomits(childPNCVisitMobDataBean.getmVomitingFlag());////(PNC 27)(RB bool)
        dtipncM.setAnySkinProblems(UtilBean.getListFromStringBySeparator(childPNCVisitMobDataBean.getmSkinProblem(), GlobalTypes.COMMA));//Multi select (PNC 33)(MS)

        // This fields are added last for feeding problem it is for mother but set here because it is converted from childPNCVisitMobDataBean
        dtipncM.setDoesBabyChinTouchTheBreast(childPNCVisitMobDataBean.getChinTouchStatus());
        dtipncM.setIsMouthWidelyOpen(childPNCVisitMobDataBean.getMouthOpenStatus());
        dtipncM.setIsAreolaMoreAboveTheMouthAndLessBeneathTheMouth(childPNCVisitMobDataBean.getAreolaStatus());
        dtipncM.setIsLowerLipTurnedOutward(childPNCVisitMobDataBean.getLowerLipStatus());

        String pretermDelivery = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PRETERM_DELIVERY);
        if (pretermDelivery != null && pretermDelivery.equalsIgnoreCase(GlobalTypes.TRUE)) {
            String deliveryDate = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PRETERM_DELIVERY_POV);
            if (deliveryDate != null && deliveryDate.trim().length() > 0) {
                dtipncM.setDeliveryDate(Long.parseLong(deliveryDate));
            }
            deliveryDate = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PRETERM_DELIVERY_LMP);
            if (deliveryDate != null && deliveryDate.trim().length() > 0) {
                dtipncM.setLmpDate(Long.parseLong(deliveryDate));
            }
        }
        dtipncM.setWhetherBabyLimbsAndNeckMoreLimpThanBefore(childPNCVisitMobDataBean.getWhetherBabyLimbsAndNeckMoreLimpThanBefore());
        dtipncM.setHowAreBabyEyes(childPNCVisitMobDataBean.getHowAreBabyEyes());

        return dtipncM;
    }

    private void setOtherDetailsForPNCMorbidity(DataBeanToIdentifyPNCMorbidities dtipncm, int child) {
        String ageString;
        if (child == 0) {
            ageString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_DOB);
            if (ageString != null) {
                if (ageString.equalsIgnoreCase(LabelConstants.NULL)) {
                    dtipncm.setAgeOfChild(null);
                } else {
                    String[] split = UtilBean.split(ageString, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (split.length == 3 && split[0] != null && !split[0].trim().equalsIgnoreCase("") && split[1] != null && !split[1].trim().equalsIgnoreCase("") && split[2] != null && !split[2].trim().equalsIgnoreCase("")) {
                        long age = UtilBean.getMilliSeconds(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                        dtipncm.setAgeOfChild(age);
                    } else {
                        dtipncm.setAgeOfChild(null);
                    }
                }
            }
            String temperatureString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.TEMPERATURE_FOR_MORB);
            if (temperatureString != null && !temperatureString.equalsIgnoreCase(LabelConstants.NULL)) {
                String[] tempParam = UtilBean.split(temperatureString, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (tempParam.length == 1 && tempParam[0].equalsIgnoreCase(GlobalTypes.TRUE)) {
                    dtipncm.setTemperature(null);
                } else if (tempParam.length == 3 && tempParam[0].equalsIgnoreCase(GlobalTypes.FALSE)) {
                    if (tempParam[1] != null && !tempParam[1].trim().equalsIgnoreCase("")
                            && tempParam[2] != null && !tempParam[2].trim().equalsIgnoreCase("")) {
                        float t1 = Integer.parseInt(tempParam[1]);
                        float t2 = Integer.parseInt(tempParam[2]);
                        Float tmp = t1 + (t2 / 10.0f);
                        dtipncm.setTemperature(tmp);
                    } else {
                        dtipncm.setTemperature(null);
                    }
                }
            } else {
                dtipncm.setTemperature(null);
            }
            String childLatestWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LATEST_WEIGHT);
            dtipncm.setWeightOfChild(null);
            if (childLatestWeight != null && !childLatestWeight.equalsIgnoreCase(LabelConstants.NULL) && !childLatestWeight.startsWith(".")) {
                dtipncm.setWeightOfChild(Float.parseFloat(childLatestWeight));
            } else {
                String childLastWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT);
                if (childLastWeight != null && !childLastWeight.equalsIgnoreCase(LabelConstants.NULL) && !childLastWeight.startsWith(".")) {
                    dtipncm.setWeightOfChild(Float.parseFloat(childLastWeight));
                }
            }
            dtipncm.setIsChildFirstPncDone(true);
            String isChildFirstPNCDone = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_CHILD_FIRST_PNC_DONE);
            if (isChildFirstPNCDone != null && isChildFirstPNCDone.equalsIgnoreCase("false")) {
                dtipncm.setIsChildFirstPncDone(false);
            }
            dtipncm.setNewBornWeight(null);
            //New-Born Weight is taken into consideration if it is first PNC to check morbidity(LBW)
            if (!dtipncm.getIsChildFirstPncDone()) {
                String newBornWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT);
                if (newBornWeight != null && !newBornWeight.equalsIgnoreCase("null") && !newBornWeight.startsWith(".")) {
                    dtipncm.setNewBornWeight(Float.parseFloat(newBornWeight));
                }
            }
            String whenDidBabyCry = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY);
            if (whenDidBabyCry != null && !whenDidBabyCry.equalsIgnoreCase(LabelConstants.NULL)) {
                dtipncm.setWhenDidBabyCry(whenDidBabyCry);
            }
        } else {
            ageString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_DOB + child);
            if (ageString != null) {
                String[] split = UtilBean.split(ageString, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (split.length == 3 && split[0] != null && !split[0].trim().equalsIgnoreCase("") && split[1] != null && !split[1].trim().equalsIgnoreCase("") && split[2] != null && !split[2].trim().equalsIgnoreCase("")) {
                    long age = UtilBean.getMilliSeconds(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    dtipncm.setAgeOfChild(age);
                } else {
                    dtipncm.setAgeOfChild(null);
                }
            }
            String childLatestWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LATEST_WEIGHT + child);
            dtipncm.setWeightOfChild(null);
            if (childLatestWeight != null && !childLatestWeight.equalsIgnoreCase(LabelConstants.NULL) && !childLatestWeight.startsWith(".")) {
                dtipncm.setWeightOfChild(Float.parseFloat(childLatestWeight));
            } else {
                String childLastWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT + child);
                if (childLastWeight != null && !childLastWeight.equalsIgnoreCase(LabelConstants.NULL) && !childLastWeight.startsWith(".")) {
                    dtipncm.setWeightOfChild(Float.parseFloat(childLastWeight));
                }
            }
            dtipncm.setIsChildFirstPncDone(true);
            String isChildFirstPNCDone = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_CHILD_FIRST_PNC_DONE + child);
            if (isChildFirstPNCDone != null && isChildFirstPNCDone.equalsIgnoreCase("false")) {
                dtipncm.setIsChildFirstPncDone(false);
            }
            dtipncm.setNewBornWeight(null);
            //New-Born Weight is taken into consideration if it is first PNC to check morbidity(LBW)
            if (!dtipncm.getIsChildFirstPncDone()) {
                String newBornWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT + child);
                if (newBornWeight != null && !newBornWeight.equalsIgnoreCase(LabelConstants.NULL) && !newBornWeight.startsWith(".")) {
                    dtipncm.setNewBornWeight(Float.parseFloat(newBornWeight));
                }
            }
            String whenDidBabyCry = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY + child);
            if (whenDidBabyCry != null && !whenDidBabyCry.equalsIgnoreCase(LabelConstants.NULL)) {
                dtipncm.setWhenDidBabyCry(whenDidBabyCry);
            }
            String temperatureString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.TEMPERATURE_FOR_MORB + child);
            if (temperatureString != null && !temperatureString.equalsIgnoreCase(LabelConstants.NULL)) {
                String[] tempParam = UtilBean.split(temperatureString, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (tempParam.length == 1 && tempParam[0].equalsIgnoreCase(GlobalTypes.TRUE)) {
                    dtipncm.setTemperature(null);
                } else if (tempParam.length == 3 && tempParam[0].equalsIgnoreCase(GlobalTypes.FALSE)) {
                    if (tempParam[1] != null && !tempParam[1].trim().equalsIgnoreCase("")
                            && tempParam[2] != null && !tempParam[2].trim().equalsIgnoreCase("")) {
                        float t1 = Integer.parseInt(tempParam[1]);
                        float t2 = Integer.parseInt(tempParam[2]);
                        float tmp = (t1 + (t2 / 10.0f));
                        dtipncm.setTemperature(tmp);
                    } else {
                        dtipncm.setTemperature(null);
                    }
                }
            } else {
                dtipncm.setTemperature(null);
            }
        }
        setSepsisCondition(dtipncm, child);
    }

    private void setSepsisCondition(DataBeanToIdentifyPNCMorbidities dtipncm, int child) {
        String sepsisCondition;
        if (child == 0) {
            sepsisCondition = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PNC_SEPSIS_CONDITION);
        } else {
            sepsisCondition = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PNC_SEPSIS_CONDITION + child);
        }
        if (sepsisCondition != null && sepsisCondition.length() == 5) {
            for (int i = 0; i < sepsisCondition.length(); i++) {
                char charAt = sepsisCondition.charAt(i);
                if (i == 0) {
                    if (charAt == 'T') {
                        dtipncm.setWhetherBabyLimbsNeckLastState(true);
                    } else if (charAt == 'F') {
                        dtipncm.setWhetherBabyLimbsNeckLastState(false);
                    }
                } else if (i == 1) {
                    if (charAt == 'T') {
                        dtipncm.setAnyDifficultyInBreastfeedingLastStatus(true);
                    } else if (charAt == 'F') {
                        dtipncm.setAnyDifficultyInBreastfeedingLastStatus(false);
                    }
                } else if (i == 2) {
                    if (charAt == 'T') {
                        dtipncm.setWhetherBabyFedLessLastStatus(true);
                    } else if (charAt == 'F') {
                        dtipncm.setWhetherBabyFedLessLastStatus(false);
                    }
                } else if (i == 3) {
                    if (charAt == 'T') {
                        dtipncm.setHowIsBabySucklingLastStatus(true);
                    } else if (charAt == 'F') {
                        dtipncm.setHowIsBabySucklingLastStatus(false);
                    }
                } else {
                    if (charAt == 'T') {
                        dtipncm.setHowIsBabyCryLastStatus(true);
                    } else if (charAt == 'F') {
                        dtipncm.setHowIsBabyCryLastStatus(false);
                    }
                }
            }
        }
    }

    public List<BeneficiaryMorbidityDetails> findAvailablePNCMorbidities(String record, String nameOfBeneficiary) {
        List<DataBeanToIdentifyPNCMorbidities> createDataBeanToIdentifyPNCMorbidity = parseRecordOfPNC(record);
        String lessThanUsual = null;
        List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetails = new ArrayList<>();
        for (int i = 0; i < createDataBeanToIdentifyPNCMorbidity.size(); i++) {
            List<IdentifiedMorbidityDetails> identifiedMorbidities = new ArrayList<>();
            BeneficiaryMorbidityDetails bmd = new BeneficiaryMorbidityDetails();

            DataBeanToIdentifyPNCMorbidities dtim = createDataBeanToIdentifyPNCMorbidity.get(i);
            if (dtim.getIsBeneficiaryAlive()) {
                PNCMorbiditiesService pNCMorbiditiesService = new PNCMorbiditiesService(dtim, identifiedMorbidities);

                // this is special case compare to all other morbidities it inputs require identified morbidities
                if (GlobalTypes.CLIENT_IS_CHILD.equalsIgnoreCase(dtim.getBeneficiaryType())) {
                    if (lessThanUsual == null && MorbiditiesConstant.LESS_THAN_USUAL.equalsIgnoreCase(dtim.getWasBabyFedLessThanUsual())) {
                        lessThanUsual = MorbiditiesConstant.LESS_THAN_USUAL;
                    }
                    pNCMorbiditiesService.isHighRiskLowBirthWeightOrPreterm(identifiedMorbidities);
                }
                if (GlobalTypes.CLIENT_IS_MOTHER.equalsIgnoreCase(dtim.getBeneficiaryType())) {
                    bmd.setBeneficiaryName(nameOfBeneficiary);
                } else {
                    bmd.setBeneficiaryName(dtim.getBeneficiaryName());
                }
                bmd.setBeneficiaryId(dtim.getHealthId());
                bmd.setIdentifiedMorbidities(identifiedMorbidities);
                bmd.setBeneficiaryType(dtim.getBeneficiaryType());
                beneficiaryMorbidityDetails.add(bmd);
            }
        }
        //This is all for feeding problem's third condition ([Sepsis diagnosed = No] && [(Was baby fed less than usual?  = Yes/Less than usual)])      
        BeneficiaryMorbidityDetails motherMorbidityDetails = null;
        boolean isFeedingProblemPresentInMother = false;
        boolean isSepsisPresentInAnyChild = false;
        for (int i = 0; i < beneficiaryMorbidityDetails.size(); i++) {
            BeneficiaryMorbidityDetails beneficiaryMorbidity = beneficiaryMorbidityDetails.get(i);
            if (GlobalTypes.CLIENT_IS_MOTHER.equalsIgnoreCase(beneficiaryMorbidity.getBeneficiaryType())) {
                List<IdentifiedMorbidityDetails> identifiedMorbidities = beneficiaryMorbidity.getIdentifiedMorbidities();
                for (int j = 0; j < identifiedMorbidities.size(); j++) {
                    IdentifiedMorbidityDetails identifiedMorbidityDetails = identifiedMorbidities.get(j);
                    if (MorbiditiesConstant.FEEDING_PROBLEM.equalsIgnoreCase(identifiedMorbidityDetails.getMorbidityCode())) {
                        isFeedingProblemPresentInMother = true;
                        break;
                    }
                }
                if (!isFeedingProblemPresentInMother) {
                    motherMorbidityDetails = beneficiaryMorbidity;
                }
            } else if (GlobalTypes.CLIENT_IS_CHILD.equalsIgnoreCase(beneficiaryMorbidity.getBeneficiaryType())) {
                List<IdentifiedMorbidityDetails> identifiedMorbidities = beneficiaryMorbidity.getIdentifiedMorbidities();
                for (int j = 0; j < identifiedMorbidities.size(); j++) {
                    IdentifiedMorbidityDetails identifiedMorbidityDetails = identifiedMorbidities.get(j);
                    if ((MorbiditiesConstant.SEPSIS.equalsIgnoreCase(identifiedMorbidityDetails.getMorbidityCode()))) {
                        isSepsisPresentInAnyChild = true;
                        break;
                    }
                }
            }
        }
        if (MorbiditiesConstant.LESS_THAN_USUAL.equalsIgnoreCase(lessThanUsual)
                && !isFeedingProblemPresentInMother
                && !isSepsisPresentInAnyChild
                && !createDataBeanToIdentifyPNCMorbidity.isEmpty()) {
            IdentifiedMorbidityDetails forFeedingProblemOfMother = new IdentifiedMorbidityDetails();
            forFeedingProblemOfMother.setMorbidityCode(MorbiditiesConstant.FEEDING_PROBLEM);
            forFeedingProblemOfMother.setRiskFactorOfIdentifiedMorbidities(MorbiditiesConstant.getMorbidityCodeAsKEYandRiskFactorAsVALUE(MorbiditiesConstant.FEEDING_PROBLEM));
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WAS_BABY_FED_LESS_THAN_USUAL) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(lessThanUsual));
            forFeedingProblemOfMother.getIdentifiedSymptoms().add(sb);
            if (motherMorbidityDetails != null) {
                if (motherMorbidityDetails.getIdentifiedMorbidities() == null) {
                    ArrayList<IdentifiedMorbidityDetails> identifiedMorbidityDetails = new ArrayList<>();
                    motherMorbidityDetails.setIdentifiedMorbidities(identifiedMorbidityDetails);
                }
                motherMorbidityDetails.getIdentifiedMorbidities().add(forFeedingProblemOfMother);
            }
        }
        return beneficiaryMorbidityDetails;
    }

    private DataBeanToIdentifyChildCareMorbidities createDataBeanToIdentifyChildCareMorbidity(String record) {
        DataBeanToIdentifyChildCareMorbidities databeanToIdentifyChildCareMorbidities = new DataBeanToIdentifyChildCareMorbidities();
        String[] questionAnswerPart = UtilBean.split(record, GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        for (String string : questionAnswerPart) {
            String[] property = UtilBean.split(string, GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
            int bindingField = Integer.parseInt(property[0]);
            String fieldValue = property[1];
            setChildCareMorbidityBeanForBeneficiary(databeanToIdentifyChildCareMorbidities, bindingField, fieldValue);
        }
        setOtherDetailsForChildcareMorbidities(databeanToIdentifyChildCareMorbidities);
        return databeanToIdentifyChildCareMorbidities;
    }

    private void setChildCareMorbidityBeanForBeneficiary(DataBeanToIdentifyChildCareMorbidities databeanToIdentifyChildCareMorbidities,
                                                         int bindingField, String fieldValue) {
        switch (bindingField) {
            case 40:
                //  Is the child able to drink or breastfeed?
                databeanToIdentifyChildCareMorbidities.setChildNotAbleToDrinkOrBreastfeed(UtilBean.getBooleanValue(fieldValue));
                break;
            case 41:
                //  Does the child vomit everything?
                databeanToIdentifyChildCareMorbidities.setChildVomitsEverything(UtilBean.getBooleanValue(fieldValue));
                break;
            case 42:
                //  Did the child have convulsions?
                databeanToIdentifyChildCareMorbidities.setChildHadConvulsion(UtilBean.getBooleanValue(fieldValue));
                break;
            case 44:
                //  Does the child have cough or fast breathing?
                databeanToIdentifyChildCareMorbidities.setDoesChildHaveCoughOrDifficultBreathing(UtilBean.getBooleanValue(fieldValue));
                break;
            case 45:
                //  Since how many days does child have cough/fast breathing?
                databeanToIdentifyChildCareMorbidities.setSinceHowManyDaysOfCough(Integer.parseInt(fieldValue));
                break;
            case 46:
                //  Count the number of breaths in one minute:
                databeanToIdentifyChildCareMorbidities.setRespiratoryRate(Integer.parseInt(fieldValue));
                databeanToIdentifyChildCareMorbidities.setNumberOfBreathsIn1Minute(Integer.parseInt(fieldValue));
                break;
            case 47:
                //  Has chest indrawing?
                databeanToIdentifyChildCareMorbidities.setChildHaveChestIndrawing(UtilBean.getBooleanValue(fieldValue));
                break;
            case 49:
                //  Since how many days does child have more stools?
                databeanToIdentifyChildCareMorbidities.setSinceHowManyDaysOfStools(Integer.parseInt(fieldValue));
                break;
            case 50:
                //  Does child have blood in stools?
                databeanToIdentifyChildCareMorbidities.setBloodInStools(UtilBean.getBooleanValue(fieldValue));
                break;
            case 51:
                //  Does child have sunken eyes?
                databeanToIdentifyChildCareMorbidities.setSunkenEyes(UtilBean.getBooleanValue(fieldValue));
                break;
            case 52:
                //  Is chid irritable or restless?
                databeanToIdentifyChildCareMorbidities.setRestlessOrIrritable(UtilBean.getBooleanValue(fieldValue));
                break;
            case 53:
                //  Is chid lethargic or unconscious?
                databeanToIdentifyChildCareMorbidities.setLethargicOrUnconscious(UtilBean.getBooleanValue(fieldValue));
                break;
            case 54:
                //  How does the skin of abdomen behave after pinching?
                databeanToIdentifyChildCareMorbidities.setDoesSkinGoesBackVerySlowly(fieldValue);
                break;
            case 55:
                //  How does child drink?
                databeanToIdentifyChildCareMorbidities.setHowChildDrinks(fieldValue);
                break;
            case 56:
                //  Does child have fever?
                databeanToIdentifyChildCareMorbidities.setFeverFlag(UtilBean.getBooleanValue(fieldValue));
                break;
            case 57:
                //  Since how many days does child have fever?
                databeanToIdentifyChildCareMorbidities.setSinceHowManyDaysOfFever(Integer.valueOf(fieldValue));
                break;
            case 58:
                //  Has fever been present each day?
                databeanToIdentifyChildCareMorbidities.setIfMoreThan7DaysHasFeverBeenPresentEachDay(UtilBean.getBooleanValue(fieldValue));
                break;
            case 68:
                //  Measure axillary temperature:
                if (!fieldValue.equalsIgnoreCase(GlobalTypes.TRUE)) {
                    String[] strTemp = UtilBean.split(fieldValue, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (strTemp.length == 3) {
                        int t1 = Integer.parseInt(strTemp[1]);
                        int t2 = Integer.parseInt(strTemp[2]);
                        float temp = (t1 + t2 / 10f);
                        databeanToIdentifyChildCareMorbidities.setMeasureAxillaryTemperatureIfFeverYes(temp);
                    }
                }
                break;
            case 60:
                //  Is the neck stiff?
                databeanToIdentifyChildCareMorbidities.setTheNeckStiff(UtilBean.getBooleanValue(fieldValue));
                break;

            case 62:
                //  Visible severe wasting?
                databeanToIdentifyChildCareMorbidities.setVisibleSevereWasting(UtilBean.getBooleanValue(fieldValue));
                break;
            case 63:
                //  Edema of both feet?
                databeanToIdentifyChildCareMorbidities.setEdemaOfBothFeet(UtilBean.getBooleanValue(fieldValue));
                break;
            case 64:
                //  Weigh child
                String latestWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.LATEST_WEIGHT);
                if (latestWeight != null) {
                    databeanToIdentifyChildCareMorbidities.setWeightOfChild(Float.parseFloat(latestWeight));
                } else {
                    String prevWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.PREV_WEIGHT);
                    if (prevWeight != null) {
                        databeanToIdentifyChildCareMorbidities.setWeightOfChild(Float.parseFloat(prevWeight));
                    }
                }
                break;
            case 65:
                //  Malnutrition grade of the child as per the weight entered:
                String grade = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MALNUTRITION_GRADE);
                databeanToIdentifyChildCareMorbidities.setMalnutritionGradeOfChild(grade);
                break;
            case 61:
                //  Does the child have palmer poller?
                databeanToIdentifyChildCareMorbidities.setDoesTheChildHavePalmerPoller(fieldValue);
                break;
            default:
        }

    }

    private void setOtherDetailsForChildcareMorbidities(DataBeanToIdentifyChildCareMorbidities databeanToIdentifyChildCareMorbidities) {
        String ageString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.AGE_FROM_ECR);
        if (ageString != null) {
            String[] split = UtilBean.split(ageString, GlobalTypes.KEY_VALUE_SEPARATOR);
            long age = UtilBean.getMilliSeconds(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            databeanToIdentifyChildCareMorbidities.setAgeOfChild(age);
        }
    }

    public List<BeneficiaryMorbidityDetails> findAvailableChildCareMorbidities(String record, String nameOfChild) {
        DataBeanToIdentifyChildCareMorbidities dtim = createDataBeanToIdentifyChildCareMorbidity(record);
        List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetails = new ArrayList<>();

        BeneficiaryMorbidityDetails bmd = new BeneficiaryMorbidityDetails();
        List<IdentifiedMorbidityDetails> identifiedMorbidities = new ArrayList<>();
        new ChildCareMorbiditiesService(dtim, identifiedMorbidities);
        bmd.setIdentifiedMorbidities(identifiedMorbidities);
        bmd.setBeneficiaryName(nameOfChild);
        bmd.setBeneficiaryType(GlobalTypes.CLIENT_IS_CHILD);
        beneficiaryMorbidityDetails.add(bmd);

        return beneficiaryMorbidityDetails;
    }
}
