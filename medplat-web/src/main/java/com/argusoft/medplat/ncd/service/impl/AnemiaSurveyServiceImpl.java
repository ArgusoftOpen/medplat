package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.service.SequenceService;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
//import com.argusoft.medplat.mobile.dto.AnemiaMemberDataBean;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.ncd.dao.*;
import com.argusoft.medplat.ncd.dto.MemberCbcDataBean;
import com.argusoft.medplat.ncd.model.*;
import com.argusoft.medplat.ncd.service.AnemiaSurveyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class AnemiaSurveyServiceImpl implements AnemiaSurveyService {

    @Autowired
    private AnemiaSurveyDao anemiaSurveyDao;
    @Autowired
    private MemberSurveyMasterDao memberMasterDao;
    @Autowired
    private UserLocationDao userLocationDao;
    @Autowired
    private AnemiaProtocolDeviationDetailDao protocolDeviationDao;
    @Autowired
    private AnemiaCrfDetailDao crfDetailDao;

    @Autowired
    private AnemiaMemberCbcReportDao anemiaMemberCbcReportDao;
    @Autowired
    private SequenceService sequenceService;

//    @Override
//    public Integer storeAnemiaSurveyForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
//
//        MemberSurveyDetailMaster memberDetail = setBasicDataForAnemiaMemberDetails(keyAndAnswerMap);
////        memberDetail.setCreatedFrom(SystemConstantUtil.ANEMIA_SURVEY);
//        memberDetail.setOpdId(generateMemberOpdId());
//        memberDetail.setLocationId(userLocationDao.retrieveLocationIdsByUserId(user.getId()).get(0));
//        AnemiaMemberSurveyDetail anemiaMemberSurveyDetail = setBasicDataForAnemiaSurveyDetails(keyAndAnswerMap);
//        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberAnemiaDetail(key, answer, anemiaMemberSurveyDetail, memberDetail));
//
//        Integer id = memberMasterDao.create(memberDetail);
//        anemiaMemberSurveyDetail.setMemberId(id);
//        anemiaSurveyDao.create(anemiaMemberSurveyDetail);
//        memberMasterDao.flush();
//        anemiaSurveyDao.flush();
//        return anemiaMemberSurveyDetail.getId();
//    }

    private AnemiaMemberSurveyDetail setBasicDataForAnemiaSurveyDetails(Map<String, String> keyAndAnswerMap) {
        AnemiaMemberSurveyDetail anemiaDetail = new AnemiaMemberSurveyDetail();
        anemiaDetail.setLongitude(keyAndAnswerMap.get("-1"));
        anemiaDetail.setLatitude(keyAndAnswerMap.get("-2"));
        anemiaDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        anemiaDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        return anemiaDetail;
    }

    private MemberSurveyDetailMaster setBasicDataForAnemiaMemberDetails(Map<String, String> keyAndAnswerMap) {
        MemberSurveyDetailMaster anemiaMemberSurveyDetail = new MemberSurveyDetailMaster();
        anemiaMemberSurveyDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
        anemiaMemberSurveyDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
        return anemiaMemberSurveyDetail;
    }

    private String generateMemberOpdId() {
        StringBuilder memberUniqueHealthId = new StringBuilder("OPD");
        memberUniqueHealthId.append(sequenceService.getNextValueBySequenceName("member_opd_id_seq"));
        memberUniqueHealthId.append("N");
        return memberUniqueHealthId.toString();
    }

    private void setAnswersToMemberAnemiaDetail(String key, String answer, AnemiaMemberSurveyDetail anemiaDetail, MemberSurveyDetailMaster memberDetail) {
        switch (key) {
            case "1":
                memberDetail.setFirstName(answer);
                break;
            case "2":
                memberDetail.setMiddleName(answer);
                break;
            case "3":
                memberDetail.setLastName(answer);
                break;
            case "50":
                memberDetail.setDob(new Date(Long.parseLong(answer)));
                break;
            case "51":
                memberDetail.setGender(answer);
                break;
            case "4":
                anemiaDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "5":
                anemiaDetail.setInclusion1(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "6":
                anemiaDetail.setInclusion2(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "7":
                anemiaDetail.setInclusion3(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "8":
                anemiaDetail.setInclusion4(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "9":
                anemiaDetail.setInclusionComment(answer);
                break;
            case "10":
                anemiaDetail.setExclusion1(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "11":
                anemiaDetail.setExclusion2(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "12":
                anemiaDetail.setExclusion3(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "13":
                anemiaDetail.setExclusionComment(answer);
                break;
            case "14":
                anemiaDetail.setInclusion5(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "15":
                anemiaDetail.setInclusion6(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "16":
                anemiaDetail.setEligible(ImtechoUtil.returnTrueFalseFromInitials(answer));
                memberDetail.setEligibleForAnemia(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "17":
                anemiaDetail.setNotEligibleReason(answer);
                break;
            case "18":
                anemiaDetail.setEligibilityComment(answer);
                break;
            case "52":
                memberDetail.setReligion(answer);
                break;
            case "53":
                memberDetail.setCaste(answer);
                break;
            case "54":
                memberDetail.setOccupation(answer);
                break;
            case "55":
                memberDetail.setMobileNumber(answer);
                break;
            case "56":
                anemiaDetail.setClinicalSettingType(answer);
                break;
            case "57":
                anemiaDetail.setVisitReason(answer);
                break;
            case "58":
                anemiaDetail.setServiceAttended(answer);
                break;
            case "60":
                memberDetail.setIsPregnantFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "61":
                memberDetail.setLmpDate(new Date(Long.parseLong(answer)));
                break;
            case "62":
                anemiaDetail.setSmokingHistory(answer);
                break;
            case "64":
                anemiaDetail.setLackOfEnergy(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "65":
                anemiaDetail.setBreathShortness(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "66":
                anemiaDetail.setFastHeartBeat(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "67":
                anemiaDetail.setLosingWeight(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "68":
                anemiaDetail.setChestPain(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "69":
                anemiaDetail.setDizziness(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "70":
                anemiaDetail.setHeadache(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "71":
                anemiaDetail.setLegCramp(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "72":
                anemiaDetail.setBrittleNailHair(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "73":
                anemiaDetail.setAdultOtherSymptom(answer);
                break;
            case "74":
                anemiaDetail.setChildLooksPale(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "75":
                anemiaDetail.setChildLosingWeight(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "76":
                anemiaDetail.setChildTired(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "77":
                anemiaDetail.setChildBreathingDifficulty(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "78":
                anemiaDetail.setChildOtherSymptom(answer);
                break;
            case "79":
                anemiaDetail.setDataCollectionPlace(answer);
                break;
            case "80":
                anemiaDetail.setDataCollectionLocation(answer);
                break;
            case "81":
                anemiaDetail.setDataCollectionTime(answer);
                break;
            case "82":
                anemiaDetail.setDataCollectionSeason(answer);
                break;
            case "83":
                anemiaDetail.setLightIntensity(answer);
                break;
            case "84":
                anemiaDetail.setInfantHeight(Integer.valueOf(answer));
                break;
            case "85":
                anemiaDetail.setInfantWeight(Integer.valueOf(answer));
                break;
            case "87":
                String[] arr = answer.split("-");
                if (arr.length > 1) {
                    anemiaDetail.setSystolicBp(Integer.valueOf(arr[1].split("\\.")[0]));
                    anemiaDetail.setDiastolicBp(Integer.valueOf(arr[2].split("\\.")[0]));
                }
                break;
            case "88":
                anemiaDetail.setSkinToneL(Integer.valueOf(answer));
                break;
            case "89":
                anemiaDetail.setSkinToneB(Integer.valueOf(answer));
                break;
            case "90":
                anemiaDetail.setNailPolishOrHenna(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "91":
                anemiaDetail.setNailExamination(answer);
                break;
            case "92":
                anemiaDetail.setOtherNailExamination(answer);
                break;
            case "93":
                anemiaDetail.setFingerOrHandExamination(answer);
                break;
            case "94":
                anemiaDetail.setOtherFingerOrHandExamination(answer);
                break;
            case "95":
                anemiaDetail.setEyeExamination(answer);
                break;
            case "96":
                anemiaDetail.setOtherEyeExamination(answer);
                break;
            case "97":
                anemiaDetail.setTongueExamination(answer);
                break;
            case "98":
                anemiaDetail.setPpgDataCollection(answer);
                break;
            case "99":
                anemiaDetail.setHemocue(Integer.valueOf(answer));
                break;
            case "100":
                anemiaDetail.setSickleCellDisease(answer);
                break;
            case "1002":
                anemiaDetail.setLabId(Integer.valueOf(answer));
                break;
            case "-10":
                anemiaDetail.setPatientUuid(answer);
                break;
            case "-11":
                anemiaDetail.setPatientFhirResourceData(answer);
                break;
            case "200":
                anemiaDetail.setHypertension(Boolean.valueOf(answer));
                break;
            case "103":
                anemiaDetail.setHemoglobin(Double.valueOf(answer));
                break;
            case "210":
                anemiaDetail.setHeartRate(Integer.valueOf(answer));
                break;
            case "211":
                anemiaDetail.setDiabetesMellitus(Boolean.valueOf(answer));
                break;
            case "212":
                anemiaDetail.setPreeclampsia(Boolean.valueOf(answer));
                break;
            case "213":
                anemiaDetail.setAnemia(Boolean.valueOf(answer));
                break;
            case "214":
                anemiaDetail.setDisordersOfBlood(Boolean.valueOf(answer));
                break;
            case "215":
                anemiaDetail.setTypeOfBloodDisorder(answer);
                break;
            case "216":
                anemiaDetail.setCancerOrMalignancy(Boolean.valueOf(answer));
                break;
            case "217":
                anemiaDetail.setOtherKnownDiseases(answer);
                break;
            case "5006":
                anemiaDetail.setOtherOccupation(answer);
                break;
            case "5007":
                anemiaDetail.setAmbientTemperature(Float.valueOf(answer));
                break;
            case "5008":
                anemiaDetail.setBodyTemperature(Float.valueOf(answer));
                break;
            default:
        }
    }

//    @Override
//    public Integer storeAnemiaProtocolDeviationForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
//
//        AnemiaProtocolDeviationDetail protocolDeviationDetail = new AnemiaProtocolDeviationDetail();
//        protocolDeviationDetail.setMemberId(Integer.parseInt(keyAndAnswerMap.get("-4")));
//        protocolDeviationDetail.setLongitude(keyAndAnswerMap.get("-1"));
//        protocolDeviationDetail.setLatitude(keyAndAnswerMap.get("-2"));
//        protocolDeviationDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
//        protocolDeviationDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
//
//        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberProtocolDeviationDetail(key, answer, protocolDeviationDetail));
//
//        protocolDeviationDao.create(protocolDeviationDetail);
//        protocolDeviationDao.flush();
//        return protocolDeviationDetail.getId();
//    }

    private void setAnswersToMemberProtocolDeviationDetail(String key, String answer, AnemiaProtocolDeviationDetail protocolDeviationDetail) {
        switch (key) {
            case "4":
                protocolDeviationDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "5":
                protocolDeviationDetail.setMajorDeviation(answer);
                break;
            case "7":
                protocolDeviationDetail.setOtherMajorDeviation(answer);
                break;
            case "8":
                protocolDeviationDetail.setMinorDeviation(answer);
                break;
            case "10":
                protocolDeviationDetail.setOtherMinorDeviation(answer);
                break;
            case "18":
                protocolDeviationDetail.setAdditionalComment(answer);
                break;
            case "60":
                protocolDeviationDetail.setResultInAe(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "64":
                protocolDeviationDetail.setParticipantWithdrawnResult(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "65":
                protocolDeviationDetail.setReportableToIrb(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "66":
                protocolDeviationDetail.setReportedToIrb(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "67":
                protocolDeviationDetail.setCorrectiveActionPlan(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "68":
                protocolDeviationDetail.setActionPlanDescription(answer);
                break;
            case "69":
                protocolDeviationDetail.setDeviationPlan(answer);
                break;
            default:
        }
    }

//    @Override
//    public Integer storeAnemiaCrfForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {
//        AnemiaCrfDetail anemiaCrfDetail = new AnemiaCrfDetail();
//        anemiaCrfDetail.setMemberId(Integer.parseInt(keyAndAnswerMap.get("-4")));
//        anemiaCrfDetail.setLongitude(keyAndAnswerMap.get("-1"));
//        anemiaCrfDetail.setLatitude(keyAndAnswerMap.get("-2"));
//        anemiaCrfDetail.setMobileStartDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-8")));
//        anemiaCrfDetail.setMobileEndDate(ImtechoUtil.getMobileStartOrEndDateFromString(keyAndAnswerMap.get("-9")));
//
//        keyAndAnswerMap.forEach((key, answer) -> setAnswersToMemberCrfDetail(key, answer, anemiaCrfDetail));
//
//        crfDetailDao.create(anemiaCrfDetail);
//        crfDetailDao.flush();
//        return anemiaCrfDetail.getId();
//    }

    private void setAnswersToMemberCrfDetail(String key, String answer, AnemiaCrfDetail anemiaCrfDetail) {
        switch (key) {
            case "4":
                anemiaCrfDetail.setServiceDate(new Date(Long.parseLong(answer)));
                break;
            case "5":
                anemiaCrfDetail.setEventDetail(answer);
                break;
            case "6":
                anemiaCrfDetail.setEventDate(new Date(Long.parseLong(answer)));
                break;
            case "7":
                anemiaCrfDetail.setEventTime(answer);
                break;
            case "8":
                anemiaCrfDetail.setReportedDate(new Date(Long.parseLong(answer)));
                break;
            case "9":
                anemiaCrfDetail.setSeverity(answer);
                break;
            case "10":
                anemiaCrfDetail.setTreatment(answer);
                break;
            case "13":
                anemiaCrfDetail.setOtherTreatment(answer);
                break;
            case "18":
                anemiaCrfDetail.setRelationshipToStudy(answer);
                break;
            case "60":
                anemiaCrfDetail.setExpectedFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "64":
                anemiaCrfDetail.setReportedToIec(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "65":
                anemiaCrfDetail.setOutcome(answer);
                break;
            case "69":
                anemiaCrfDetail.setResolutionDescription(answer);
                break;
            case "66":
                anemiaCrfDetail.setResolutionOngoing(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            case "67":
                anemiaCrfDetail.setResolutionDate(new Date(Long.parseLong(answer)));
                break;
            case "70":
                anemiaCrfDetail.setSeriousAeFlag(ImtechoUtil.returnTrueFalseFromInitials(answer));
                break;
            default:
        }
    }

    @Override
    public MemberCbcDataBean getMemberCbcDetail(Integer labId) {
        String apiUrl = "http://123.63.181.189/sewa/Api/getTestResultbyLabno/" + labId + "?test_id=139";
        MemberCbcDataBean memberCbcDataBean = new MemberCbcDataBean();
        AnemiaMemberCbcReport anemiaMemberCbcReport;


        // Credentials
        String username = "sewa";
        String password = "312@sewa";

        try {
            anemiaMemberCbcReport = anemiaMemberCbcReportDao.retrieveAnemiaMemberCbcReportByLabId(labId);
            if(anemiaMemberCbcReport == null){
                anemiaMemberCbcReport = new AnemiaMemberCbcReport();
                anemiaMemberCbcReport.setLabId(labId);
                anemiaMemberCbcReport.setStatus("P");
                AnemiaMemberSurveyDetail anemiaMemberSurveyDetail = anemiaSurveyDao.retrieveAnemiaMemberDetailsByLabId(labId);
                if(anemiaMemberSurveyDetail != null)
                    anemiaMemberCbcReport.setMemberId(anemiaMemberSurveyDetail.getMemberId());
            }

            // Create URL object
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP GET method
            connection.setRequestMethod("GET");

            // Set Basic Authentication credentials in the request header
            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                System.out.println("Response: " + response);
                JSONObject jsonObject = new JSONObject(response.toString());
                if(jsonObject.has("CBC") && jsonObject.getInt("status") == 200) {
                    JSONArray cbcArray = jsonObject.getJSONArray("CBC");


                    for (int i = 0; i < cbcArray.length(); i++) {
                        JSONObject cbcItem = cbcArray.getJSONObject(i);

                        // Access fields within the CBC items
                        String name = cbcItem.getString("name");
                        String code = cbcItem.getString("code");
                        String result = cbcItem.getString("result");

                        if (result == null || result.isEmpty() || result.equals("---"))
                            result = "NA";

                        switch (code) {
                            case "WBC":
                                anemiaMemberCbcReport.setWbc_count(result);
                                break;
                            case "RBC":
                                anemiaMemberCbcReport.setRbc_count(result);
                                break;
                            case "HGB":
                                anemiaMemberCbcReport.setHgb_count(result);
                                break;
                            case "HCT":
                                anemiaMemberCbcReport.setHct_count(result);
                                break;
                            case "MCV":
                                anemiaMemberCbcReport.setMcv_count(result);
                                break;
                            case "MCH":
                                anemiaMemberCbcReport.setMch_count(result);
                                break;
                            case "MCHC":
                                anemiaMemberCbcReport.setMchc_count(result);
                                break;
                            case "PLT":
                                anemiaMemberCbcReport.setPlt_count(result);
                                break;
                            case "RDWCV":
                                anemiaMemberCbcReport.setRdw_cv(result);
                                break;
                        }
                    }
                    anemiaMemberCbcReport.setStatus("S");
                    if(anemiaMemberCbcReportDao.checkCbcReportEntry(labId)) {
                        anemiaMemberCbcReportDao.create(anemiaMemberCbcReport);
                        anemiaMemberCbcReportDao.flush();
                    } else {
                        anemiaMemberCbcReportDao.update(anemiaMemberCbcReport);
                        anemiaMemberCbcReportDao.flush();
                    }
                    memberCbcDataBean.setMemberId(anemiaMemberCbcReport.getMemberId());
                    memberCbcDataBean.setLabId(anemiaMemberCbcReport.getLabId());
                    memberCbcDataBean.setWbc(anemiaMemberCbcReport.getWbc_count());
                    memberCbcDataBean.setRbc(anemiaMemberCbcReport.getRbc_count());
                    memberCbcDataBean.setHgb(anemiaMemberCbcReport.getHgb_count());
                    memberCbcDataBean.setHct(anemiaMemberCbcReport.getHct_count());
                    memberCbcDataBean.setMcv(anemiaMemberCbcReport.getMcv_count());
                    memberCbcDataBean.setMch(anemiaMemberCbcReport.getMch_count());
                    memberCbcDataBean.setMchc(anemiaMemberCbcReport.getMchc_count());
                    memberCbcDataBean.setPlt(anemiaMemberCbcReport.getPlt_count());
                    memberCbcDataBean.setRdwCv(anemiaMemberCbcReport.getRdw_cv());
                    memberCbcDataBean.setStatus("S");

                    return memberCbcDataBean;
                } else {
                    memberCbcDataBean.setErrorMessage(jsonObject.getString("msg"));
                    memberCbcDataBean.setStatus("F");
                    anemiaMemberCbcReport.setMessage(jsonObject.getString("msg"));
                    anemiaMemberCbcReport.setStatus("F");
                    System.err.println("HTTP request failed with status code: " + responseCode);
                }
            } else {
                memberCbcDataBean.setErrorMessage(connection.getResponseMessage());
                memberCbcDataBean.setStatus("F");
                anemiaMemberCbcReport.setMessage(connection.getResponseMessage());
                anemiaMemberCbcReport.setStatus("F");
                System.err.println("HTTP request failed with status code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            memberCbcDataBean.setErrorMessage("Something went wrong!");
            memberCbcDataBean.setStatus("F");
            anemiaMemberCbcReport = new AnemiaMemberCbcReport();
            anemiaMemberCbcReport.setStatus("F");
            anemiaMemberCbcReport.setMessage("Something went wrong!");

        }
        return memberCbcDataBean;
    }
}
