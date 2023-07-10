package com.argusoft.medplat.mobile.controller;

import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.AnnouncementService;
import com.argusoft.medplat.common.service.OtpService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.service.UserAttendanceMasterService;
import com.argusoft.medplat.course.service.LmsMobileEventSubmissionService;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.mobile.constants.MobileApiPathConstants;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.mobile.model.BlockedDevicesMaster;
import com.argusoft.medplat.mobile.service.*;
import com.argusoft.medplat.web.users.service.UserService;
import com.sun.jersey.api.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author kunjan
 */
@RestController
@RequestMapping("/api/mobile/")
public class MobileController extends GenericSessionUtilService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MobileFhsService mobileFhsService;
    @Autowired
    private MobileAshaService mobileAshaService;
//    @Autowired
//    private MobileFhsrService mobileFhsrService;
//    @Autowired
//    private MobileAwwService mobileAwwService;
//    @Autowired
//    private MobileRbskService mobileRbskService;
    @Autowired
    private MobileUtilService mobileUtilService;
    @Autowired
    private FormSubmissionService formSubmissionService;
//    @Autowired
//    private MigrationService migrationService;
//    @Autowired
//    private QueryMasterService queryMasterService;
//    @Autowired
//    private PatchService patchService;
//    @Autowired
//    private UploadMultimediaService uploadMultiMediaService;
//    @Autowired
//    private DellApiService dellApiService;
    @Autowired
    private BlockedDevicesService blockedDevicesService;
    //    @Autowired
//    private SohUserService sohUserService;
//    @Autowired
//    private SmsService smsService;
//    @Autowired
//    private ServerManagementService serverManagementService;
//    @Autowired
//    private SystemBuildHistoryService systemBuildHistoryService;
    @Autowired
    private OtpService otpService;
    //    @Autowired
//    private FamilyHealthSurveyService familyHealthSurveyService;
//    @Autowired
//    private DrTechoUserService drTechoUserService;
//    @Autowired
//    private MyTechoUserService myTechoUserService;
//    @Autowired
//    private HealthInfrastructureService healthInfrastructureService;
//    @Autowired
//    private LocationStateMasterService locationStateMasterService;
//    @Autowired
//    private ImmunisationService immunisationService;
//    @Autowired
//    private RbskDefectConfigurationService rbskDefectConfigurationService;
//    @Autowired
//    private DocumentService myTechoFileMasterService;
//    @Autowired
//    private SequenceService sequenceService;
//    @Autowired
//    private MobileSohService mobileSohService;
    @Autowired
    private UserAttendanceMasterService attendanceMasterService;
//    @Autowired
//    private RchOpdService;
//    @Autowired
//    private CovidSymptomCheckerMasterService covidSymptomCheckerMasterService;
//    @Autowired
//    private CovidTravellersInfoService covidTravellersInfoService;
    @Autowired
    private SystemConfigurationService systemConfigurationService;
    @Autowired
    private MobileSyncService mobileSyncService;
    @Autowired
    private LmsMobileEventSubmissionService lmsMobileEventSubmissionService;
//    @Autowired
//    private UserTokenService userTokenService;
//    @Autowired
//    private FailedHealthIdDataService failedHealthIdDataService;
    @Autowired
    private AnnouncementService announcementService;
//
//    @Autowired
//    @Qualifier(value = "mobileMaxRefreshCount")
//    private TenantCacheProvider<Integer> tenantCacheProviderForMobileMaxRefreshCount;
//
//    @Autowired
//    @Qualifier(value = "mobileCurrentRefreshCount")
//    private TenantCacheProvider<Integer> tenantCacheProviderForMobileCurrentRefreshCount;
//
//    @Autowired
//    private CourseMasterService courseMasterService;
//
//    @Autowired
//    private ChardhamMemberScreeningService chardhamMemberScreeningService;

    private final Client client = Client.create();

    @GetMapping(value = MobileApiPathConstants.GET_ANDROID_VERSION)
    public String retrieveAndroidVersion(HttpServletRequest request) {
        int parseInt;
        if (request.getHeader("application-version") == null) {
            parseInt = 0;
        } else {
            parseInt = Integer.parseInt(request.getHeader("application-version"));
        }

        return mobileFhsService.retrieveAndroidVersion(parseInt);
    }

    @GetMapping(value = MobileApiPathConstants.GET_SERVER_IS_ALIVE)
    public boolean getServerIsAlive() {
        return true;
    }

    @GetMapping(value = MobileApiPathConstants.TECHO_GET_IMEI_BLOCKED)
    public boolean isImeiBlocked(@RequestParam(name = "imei", required = false) String imei) {
        return blockedDevicesService.checkIfDeviceIsBlocked(imei);
    }

    @GetMapping(value = MobileApiPathConstants.TECHO_GET_IMEI_BLOCKED_OR_DELETE_DATABASE)
    public BlockedDevicesMaster isImeiBlockedOrDeleteDatabase(@RequestParam(name = "imei") String imei) {
        return blockedDevicesService.checkIfDeviceIsBlockedOrDeleteDatabase(imei);
    }

    @GetMapping(value = MobileApiPathConstants.TECHO_REMOVE_IMEI_BLOCKED_ENTRY)
    public void removeEntryForDeviceOfIMEI(@RequestParam(name = "imei") String imei) {
        blockedDevicesService.removeEntryForDeviceOfIMEI(imei);
    }

    @GetMapping(value = MobileApiPathConstants.GET_FONT_SIZE)
    public String retrieveFontSize(@RequestParam("fontSizeType") String fontSizeType) {
        return mobileFhsService.retrieveFontSize(fontSizeType);
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_IS_USER_IN_PRODUCTION)
    public Boolean isUserInProduction(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileFhsService.isUserInProduction(mobileRequestParamDto.getUserName());
    }

    @GetMapping(value = MobileApiPathConstants.TECHO_PLUS_USER_COUNT)
    public LinkedHashMap<String, Object> getTechoPlusUserCount() {
        return mobileUtilService.getTechoPlusUserCount();
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_VALIDATE_USER_NEW)
    public MobUserInfoDataBean retrieveUserInfoNew(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return userService.validateMobileUserNew(mobileRequestParamDto.getUserName(),
                mobileRequestParamDto.getPassword(), mobileRequestParamDto.getFirebaseToken(), mobileRequestParamDto.getIsFirstTimeLogin());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_RECORD_ENTRY_MOBILE_TO_DB_SERVER)
    public RecordStatusBean[] recordsEntryFromMobileToDBServer(@RequestBody MobileRequestParamDto mobileRequestParamDto, HttpServletRequest request) throws ImtechoUserException {
        Integer appVersion = Integer.parseInt(request.getHeader("application-version"));
        if (appVersion < 44) {
            return null;
        }
        return formSubmissionService.recordsEntryFromMobileToDBServer(mobileRequestParamDto.getToken(), mobileRequestParamDto.getRecords());
    }

    @PutMapping(value = MobileApiPathConstants.TECHO_UPLOAD_UNCAUGHT_EXCEPTION_DETAIL)
    public String uploadUncaughtExceptionDetail(
            @RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileUtilService.storeUncaughtExceptionDetails(mobileRequestParamDto.getUncaughtExceptionBeans(), mobileRequestParamDto.getUserId());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_MERGED_FAMILIES_INFORMATION)
    public Boolean syncMergedFamiliesInformationWithServer(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileFhsService.syncMergedFamiliesInformationWithDb(mobileRequestParamDto.getToken(), mobileRequestParamDto.getMergedFamiliesBeans());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_GET_FAMILY_TO_BE_ASSIGNED_BY_SEARCH_STRING)
    public Map<String, FamilyDataBean> getFamiliesToBeAssignedBySearchString(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileFhsService.getFamiliesToBeAssignedBySearchString(mobileRequestParamDto.getToken(),
                mobileRequestParamDto.getSearchString(), mobileRequestParamDto.getIsSearchByFamilyId());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_ASSIGN_FAMILY_TO_USER)
    public FamilyDataBean assignFamilyToUser(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileFhsService.assignFamilyToUser(mobileRequestParamDto.getToken(), mobileRequestParamDto.getLocationId(), mobileRequestParamDto.getFamilyId());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_GET_USER_FORM_ACCESS_DETAIL)
    public List<UserFormAccessBean> getUserFormAccessDetail(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return mobileFhsService.getUserFormAccessDetail(mobileRequestParamDto.getToken());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_USER_READY_TO_MOVE_PRODUCTION)
    public void postUserReadyToMoveProduction(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        mobileFhsService.userReadyToMoveProduction(mobileRequestParamDto.getToken(), mobileRequestParamDto.getFormCode(), null);
    }

    @PostMapping(value = MobileApiPathConstants.GET_DETAILS_FHW)
    public LoggedInUserPrincipleDto getDetailsForFhw(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request) throws Exception {
        String header = request.getHeader("application-version");
        if (header == null || header.equals("null")) {
            throw new ImtechoUserException("Application Version not found in header", 100, paramDetailDto);
        }
        Integer appVersion = Integer.parseInt(header);
        if (appVersion < 98) {
            return null;
        }
        return mobileFhsService.getDetails(paramDetailDto, Integer.parseInt(request.getHeader("application-version")));
//        if (tenantCacheProviderForMobileCurrentRefreshCount.get() < tenantCacheProviderForMobileMaxRefreshCount.get()) {
//            synchronized (this) {
//                Integer mobileCurrentRefreshCount = tenantCacheProviderForMobileCurrentRefreshCount.get();
//                tenantCacheProviderForMobileCurrentRefreshCount.put(++mobileCurrentRefreshCount);
//            }
//            try {
//                return mobileFhsService.getDetails(paramDetailDto, Integer.parseInt(request.getHeader("application-version")));
//            } finally {
//                synchronized (this) {
//                    Integer mobileCurrentRefreshCount = tenantCacheProviderForMobileCurrentRefreshCount.get();
//                    tenantCacheProviderForMobileCurrentRefreshCount.put(--mobileCurrentRefreshCount);
//                }
//            }
//        }
//        return null;
    }

    @PostMapping(value = MobileApiPathConstants.GET_METADATA)
    public Map<String, Boolean> getMetaData(@RequestBody MobileRequestParamDto paramDetailDto) {
        return mobileSyncService.getMetaData(paramDetailDto);
    }

    @PostMapping(value = MobileApiPathConstants.SYNC_DATA)
    public LoggedInUserPrincipleDto syncData(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request) throws Exception {
        return mobileSyncService.getDetails(paramDetailDto, Integer.parseInt(request.getHeader("application-version")));
    }

    @PostMapping(value = MobileApiPathConstants.GET_DETAILS_ASHA)
    public LoggedInUserPrincipleDto getDetailsForAsha(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request)
            throws ExecutionException, InterruptedException {
        Integer appVersion = Integer.parseInt(request.getHeader("application-version"));
        if (appVersion < 98) {
            return null;
        }
        return mobileAshaService.getDataForAsha(paramDetailDto, appVersion);
    }
//
//    @PostMapping(value = MobileApiPathConstants.GET_DETAILS_FHSR)
//    public LoggedInUserPrincipleDto getDetailsForFhsr(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request) {
//        Integer appVersion = Integer.parseInt(request.getHeader("application-version"));
//        if (appVersion < 77) {
//            return null;
//        }
//        return mobileFhsrService.getDetailsForFhsr(paramDetailDto, appVersion);
//    }
//
//    @PostMapping(value = MobileApiPathConstants.GET_DETAILS_AWW)
//    public LoggedInUserPrincipleDto getDetailsForAww(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request) {
//        String header = request.getHeader("application-version");
//        if (header == null || header.equals("null")) {
//            throw new ImtechoUserException("Application Version not found in header", 100, paramDetailDto);
//        }
//        Integer appVersion = Integer.parseInt(header);
//        if (appVersion < 77) {
//            return null;
//        }
//        return mobileAwwService.getDetails(paramDetailDto, Integer.parseInt(request.getHeader("application-version")));
//    }
//
//    @PostMapping(value = MobileApiPathConstants.GET_DETAILS_RBSK)
//    public LoggedInUserPrincipleDto getDetailsForRbsk(@RequestBody LogInRequestParamDetailDto paramDetailDto, HttpServletRequest request) {
//        String header = request.getHeader("application-version");
//        if (header == null || header.equals("null")) {
//            throw new ImtechoUserException("Application Version not found in header", 100, paramDetailDto);
//        }
//        Integer appVersion = Integer.parseInt(header);
//        if (appVersion < 77) {
//            return null;
//        }
//        return mobileRbskService.getDetails(paramDetailDto, Integer.parseInt(request.getHeader("application-version")));
//    }
//
//    @PostMapping(value = MobileApiPathConstants.GET_FAMILIES_BY_LOCATION)
//    public List<FamilyDataBean> getFamiliesByLocationId(@RequestBody LogInRequestParamDetailDto logInRequestParamDetailDto) {
//        return mobileFhsrService.retrieveAssignedFamiliesByLocationId(logInRequestParamDetailDto);
//    }
//
//    @GetMapping(value = MobileApiPathConstants.GET_FILE)
//    public FileSystemResource downloadFile(@RequestParam("token") String token, @RequestParam("fileName") String fileName) throws FileNotFoundException {
//        if (token != null && !token.isEmpty()) {
//            UserTokenDto userTokenDto = userTokenService.retrieveDtoByUserToken(token);
//            if (userTokenDto != null) {
//                UserMaster userMaster = mobileFhsService.isUserTokenValid(userTokenDto.getUserToken());
//                if (userMaster != null) {
////                    if (fileName != null && !fileName.isEmpty() && fileName.matches("[a-zA-Z0-9.]+")) {
//                    if (fileName != null && !fileName.isEmpty()) {
//                        return uploadMultiMediaService.getFileById(fileName);
//                    } else {
//                        throw new ImtechoSystemException("File name not found in download file API for Mobile - file name : " + fileName, 500);
//                    }
//                }
//            }
//        }
//        throw new ImtechoSystemException("Invalid token in download file API for Mobile - file name : " + fileName, 500);
//    }
//
//    @PostMapping(value = MobileApiPathConstants.TECHO_POST_RBSK_SCREENING_DETAILS)
//    public String createScreeningForMobile(@RequestBody RbskScreeningDto rbskScreeningDto) {
//        return rbskDefectConfigurationService.createScreeningForMobile(rbskScreeningDto);
//    }
//
//    @GetMapping(value = "getfileById")
//    public ResponseEntity getFile(@RequestParam("id") Long id) throws FileNotFoundException {
//        File file = myTechoFileMasterService.getFile(id);
//        try {
//            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(file.length())
//                    .contentType(MediaType.parseMediaType("application/octet-stream"))
//                    .body(resource);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//        return ResponseEntity.status(404).build();
//    }
//
//    @GetMapping(value = MobileApiPathConstants.DOWNLOAD_LIBRARY_FILE)
//    public ResponseEntity<Resource> downloadLibraryFile(@RequestParam(name = "fileName", required = false) String fileName,
//                                                        @RequestParam(name = "fileId", required = false) Integer fileId,
//                                                        HttpServletRequest request) throws FileNotFoundException {
//        File file = null;
//        String returnFileName = null;
//        if (fileName == null && fileId == null) {
//            return null;
//        }
//
//        if (fileName != null) {
//            file = uploadMultiMediaService.getLibraryFileByName(fileName);
//
//            if (file == null) {
//                return null;
//            }
//            returnFileName = file.getName();
//        }
//
//        if (fileId != null) {
//            file = uploadMultiMediaService.getLibraryFileById(fileId);
//
//            if (file == null) {
//                return null;
//            }
//            returnFileName = fileId.toString() + "." + file.getName().substring(file.getName().lastIndexOf('.') + 1);
//        }
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        String contentType = "application/octet-stream";
//
//        return ResponseEntity.ok()
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + returnFileName + "\"")
//                .body(resource);
//    }
//
//    public static byte[] APK = null;
//
//    @GetMapping(value = MobileApiPathConstants.DOWNLOAD_APPLICATION)
//    public ResponseEntity<Resource> downloadApk(@RequestParam("name") String name, HttpServletRequest request) throws FileNotFoundException, IOException {
//        String fileName = "techo_app_100.apk";
//        if (APK == null) {
//            File file = new File("/home/techo/techo/code/imtecho/ImtechoV2/imtecho-web/src/main/resources/apks/" + fileName);
//            APK = IOUtils.toByteArray(new FileInputStream(file));
//        }
//        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(APK));
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(APK.length)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//    }
//
//    @PostMapping(value = MobileApiPathConstants.TECHO_GET_TOKEN_VALIDITY)
//    public Boolean getTokenValidity(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
//        return userService.isUserTokenValid(mobileRequestParamDto.getToken());
//    }
//
    @PostMapping(value = MobileApiPathConstants.TECHO_REVALIDATE_TOKEN)
    public Object revalidateUserToken(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return userService.revalidateUserToken(mobileRequestParamDto.getUserPassMap());
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_GET_USER_FROM_TOKEN)
    public Map<String, String[]> getUserIdFromToken(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return userService.getUserIdAndActiveTokenFromToken(mobileRequestParamDto.getUserTokens());
    }

    //    @PostMapping(value = MobileApiPathConstants.TECHO_GET_NAME_BASED_ON_AADHAR)
//    public Map<Integer, String> getNameBasedOnAadharForAllMembers(@RequestBody MobileRequestParamDto mobileRequestParamDto) throws HttpException {
//        return mobileFhsService.getNameBasedOnAadharForAllMembersByUserId(mobileRequestParamDto);
//    }
//
    @PostMapping(value = MobileApiPathConstants.TECHO_POST_MARK_ATTENDANCE)
    public Integer markAttendanceForTheDay(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return attendanceMasterService.markAttendanceForTheDay(mobileRequestParamDto);
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_STORE_ATTENDANCE)
    public void storeAttendanceForTheDay(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        attendanceMasterService.storeAttendanceForTheDay(mobileRequestParamDto);
    }

//    @PostMapping(value = MobileApiPathConstants.TECHO_STORE_OPD_LAB_TEST_FORM)
//    public String storeOpdLabTestResult(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
//        return rchOpdService.storeOpdLabTestDetails(mobileRequestParamDto.getToken(),
//                mobileRequestParamDto.getAnswerString(),
//                mobileRequestParamDto.getLabTestDetId(),
//                mobileRequestParamDto.getLabTestVersion());
//    }

    //    @PostMapping(value = MobileApiPathConstants.TECHO_STORE_COVID_SYMPTOM_CHECKER_DUMP)
//    public void storeCovidSymtomCheckerDump(@RequestBody List<CovidSymptomCheckerDto> covidSymptomCheckerDtos) {
//        LOGGER.info("========== MY TECHO COVID SYMPTOMS CHECKER DUMP STARTED ==========");
//        covidSymptomCheckerMasterService.storeCovidSymtomCheckerDump(covidSymptomCheckerDtos);
//        LOGGER.info("========== MY TECHO COVID SYMPTOMS CHECKER DUMP FINISHED ==========");
//    }
//
//    @PostMapping(value = "/getdata")
//    public QueryDto executeQuery(@RequestBody QueryDto queryDto) {
//        List<QueryDto> queryDtos = new LinkedList<>();
//        queryDtos.add(queryDto);
//        List<QueryDto> executeQueryByCode = queryMasterService.executeQuery(queryDtos, true);
//        if (!executeQueryByCode.isEmpty()) {
//            return executeQueryByCode.get(0);
//        } else {
//            throw new ImtechoSystemException("Get multiple response " + queryDto, 0);
//        }
//    }
//
//    @PostMapping(value = "/getdata/{code}")
//    public QueryDto execute(@PathVariable(value = "code") String code
//            , @RequestBody QueryDto queryDto) {
//        List<QueryDto> queryDtos = new LinkedList<>();
//        queryDtos.add(queryDto);
//        List<QueryDto> executeQueryByCode = queryMasterService.execute(queryDtos, true);
//        if (!executeQueryByCode.isEmpty()) {
//            return executeQueryByCode.get(0);
//        } else {
//            throw new ImtechoSystemException("Get multiple response " + queryDto, 0);
//        }
//    }
//
//    @GetMapping(value = MobileApiPathConstants.RUN_PATCH)
//    public String runPatch() {
//        mobileFhsService.runPatch();
//        return "22";
//    }
//
//    @GetMapping(value = "testncdpush")
//    public String testNcdPush() {
//        dellApiService.push();
//        return "Data Push Started...";
//    }
//
//    @PostMapping(value = "testsms")
//    public void testSMS(@RequestParam("mobile") String mobile, @RequestParam("message") String message) {
//        smsService.sendSms(mobile, message, true, "TEST_SMS");
//    }
//
//    @GetMapping(value = "deployserver")
//    public void deployServer() throws IOException, InterruptedException {
//        Process p = Runtime.getRuntime().exec("/home/kunjan/testsh.sh");
//        p.waitFor();
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line).append("\n");
//        }
//        LOGGER.info(sb.toString());
//    }
//
//    @GetMapping(value = "patchForChildEntryNotDone")
//    public String patchForChildEntryNotDone() {
//        patchService.patchForChildEntryNotDone();
//        return "Patch Started...";
//    }
//
//    @GetMapping(value = "patchToUpdateLastServiceDateOfMember")
//    public void patchToUpdateLastServiceDateOfMember() {
//        LOGGER.info("Patch to update Last Service Date of Member Started...");
//        patchService.patchToUpdateLastServiceDateOfMember();
//        LOGGER.info("Patch to update Last Service Date of Member Completed...");
//    }
//
//    @GetMapping(value = "patchForIDSPFamilyEntry")
//    public void patchForIDSPFamilyEntry() {
//        LOGGER.info("Patch to IDSP family entry Started...");
//        patchService.patchForIDSPFamilyEntry();
//        LOGGER.info("Patch to IDSP family entry Completed...");
//    }
//
//    @GetMapping(value = "patchToRestoreCovidTravellerInfo")
//    public void patchToRestoreCovidTravellerInfo() {
//        LOGGER.info("Patch to restore traveller's screening Started...");
//        patchService.patchToRestoreCovidTravellerInfo();
//        LOGGER.info("Patch to restore traveller's screening Completed...");
//    }
//
//    @PostMapping(value = "sohRegisterSendOTP")
//    public SohUserDto sohRegisterSendOTP(@RequestBody SohUserDto sohUserDto) {
//        return sohUserService.sohRegisterSendOTP(sohUserDto);
//    }
//
//    @PostMapping(value = "sohRegister")
//    public SohUserDto sohRegister(@RequestBody SohUserDto sohUserDto) {
//        return sohUserService.save(sohUserDto);
//    }
//
//    @GetMapping(value = "activeCode")
//    public Map<String, Object> activeCode(@RequestParam("code") int code, @RequestParam("location") int locationId) {
//
//        Map<String, Object> result = new HashMap<>();
//        Optional<SohUserDto> optional = sohUserService.activeCode(code, locationId);
//        if (optional.isPresent()) {
//            result.put("success", true);
//            result.put("user", optional.get());
//        } else {
//            result.put("success", false);
//        }
//        return result;
//    }
//
//    @GetMapping(value = "inActiveCode")
//    public Map<String, Object> inActiveCode(@RequestParam("code") int code, @RequestParam("reason") String reason) {
//
//        Map<String, Object> result = new HashMap<>();
//        Optional<SohUserDto> optional = sohUserService.inActiveCode(code, reason);
//        if (optional.isPresent()) {
//            result.put("success", true);
//            result.put("user", optional.get());
//        } else {
//            result.put("success", false);
//        }
//        return result;
//    }
//
//    @GetMapping(value = "patchforbreastfeeding")
//    public String patchForUpdatingBreastFeedingInWPD() {
//        LOGGER.info("Patch to update Breast Feeding For WPD Started...   {}", new Date());
//
//        List<SyncStatus> syncStatuses = patchService.retrieveSyncStatusForUpdatingBreastFeedingForWPD();
//        LOGGER.info("Total Sync Status retrieved : {} .........@ : {} ", syncStatuses.size(), new Date());
//
//        int updatedCount = 0;
//        for (SyncStatus syncStatus : syncStatuses) {
//            patchService.updateProcessedSyncStatusId(syncStatus.getId());
//
//            ParsedRecordBean parsedRecordBean = patchService.parseRecordStringToBean(syncStatus.getRecordString());
//            String[] keyAndAnswerSet = parsedRecordBean.getAnswerRecord().split(MobileConstantUtil.ANSWER_STRING_FIRST_SEPARATER);
//            Map<String, String> keyAndAnswerMap = new HashMap<>();
//            List<String> keyAndAnswerSetList = new ArrayList<>(Arrays.asList(keyAndAnswerSet));
//            for (String aKeyAndAnswer : keyAndAnswerSetList) {
//                String[] keyAnswerSplit = aKeyAndAnswer.split(MobileConstantUtil.ANSWER_STRING_SECOND_SEPARATER);
//                if (keyAnswerSplit.length != 2) {
//                    continue;
//                }
//                keyAndAnswerMap.put(keyAnswerSplit[0], keyAnswerSplit[1]);
//            }
//
//            Boolean breastFeeding = null;
//            String answer = keyAndAnswerMap.get("21");
//            if (answer != null && answer.equals("1")) {
//                breastFeeding = true;
//            }
//
//            if (breastFeeding != null && breastFeeding) {
//                updatedCount = patchService.updateWpdMotherMasterForBreastFeeding(keyAndAnswerMap, syncStatus, breastFeeding, updatedCount);
//            }
//        }
//
//        LOGGER.info("Total WPDMotherMaster records updated : {} ", updatedCount);
//        StringBuilder sb = new StringBuilder();
//        sb.append("Total SyncStatus retrieved : ")
//                .append(syncStatuses.size())
//                .append("\n")
//                .append("Total WPDMotherMaster records updated : ")
//                .append(updatedCount);
//
//        LOGGER.info("Patch to update Breast Feeding For WPD Completed...  {} ", new Date());
//        return sb.toString();
//    }
//
//    @GetMapping(value = "/downloadmctsdb")
//    public ResponseEntity download(@RequestParam("username") String userName,
//                                   @RequestParam("password") String password,
//                                   @RequestParam("fileName") String fileName) {
//        if (userName == null || password == null || !userName.equals("superuser") || !password.equals("argusadmin")) {
//            throw new ImtechoUserException("Wrong Username or password", 0);
//        }
//        if (userName.equals("superuser") && password.equals("argusadmin")) {
//            return serverManagementService.downloadFile("/home/techo/techo/db_backup/" + fileName);
//        }
//        return null;
//    }
//
//    @GetMapping(value = MobileApiPathConstants.TECHO_TEST_RECORD_ENTRY)
//    public RecordStatusBean[] recordsEntryFromMobileToDBServer() throws ImtechoUserException {
//        String[] arrayOfRecords = new String[1];
//        arrayOfRecords[0] = "drraval11567268302379|1567268302382|ASHA_CS|-1|-1|43416|66424310|-1|1!A062080910~2!આરવકુમાર~3!A001866382~4!સૂર્યાબેન ગોવિંદભાઇ રાવળ~5!21/11/2015~7!12.9~8!M~9!3 વર્ષ 9 મહિનો  10 દિવસ~11!રાવળવાસ~12!9723025631~13!HINDU~14!OBC/SEBC~15!OPV_0 - 23/11/2015\n"
//                + "HEPATITIS_B_0 - 23/11/2015\n"
//                + "બીસીજી - 23/11/2015\n"
//                + "VITAMIN_K - 23/11/2015\n"
//                + "OPV_1 - 06/01/2016\n"
//                + "PENTA_1 - 06/01/2016\n"
//                + "PENTA_2 - 17/02/2016\n"
//                + "OPV_2 - 17/02/2016\n"
//                + "f-IPV1 - 21/03/2016\n"
//                + "PENTA_3 - 21/03/2016\n"
//                + "OPV_3 - 21/03/2016\n"
//                + "VITAMIN_A - 29/08/2016\n"
//                + "MEASLES_1 - 29/08/2016\n"
//                + "MEASLES_2 - 08/05/2017\n"
//                + "DPT_BOOSTER - 08/05/2017\n"
//                + "OPV_BOOSTER - 08/05/2017\n"
//                + "VITAMIN_A - 08/05/2017\n"
//                + "VITAMIN_A - 08/11/2017\n"
//                + "MEASLES_RUBELLA_2 - 08/05/2017~17!હા~19!હા~20!1567268263544~21!AVAILABLE~29!T~30!9723025631~40!T~41!T~42!T~44!F~48!F~52!F~53!F~54!NRMLY~55!NABTDR~56!F~61!SEVR~62!F~63!F~66!No~67!F~68!T~9997!S~-1!72.3751216~-2!23.5974194~-4!196733~-5!1828~-6!2086~-7!null~-8!1567268258957~-9!1567268302373~";
//        String token = "Pl1t3AVm6tANnl2iQRMYQFP2a0kaoYQ8";
//        return formSubmissionService.recordsEntryFromMobileToDBServer(token, arrayOfRecords);
//    }
//
//    @GetMapping(value = "build")
//    public SystemBuildHistory retrieveLastSystemBuild() {
//        return systemBuildHistoryService.retrieveLastBuildHistory();
//    }
//
//    @GetMapping(value = "runcronformigration")
//    public String runCronForMigration() {
//        migrationService.cronForMigrationOutWithNoResponse();
//        return "Success";
//    }
//
//    @GetMapping(value = "resolvemigrationinwithoutphone")
//    public String createTemporaryMemberForMigrationInWithoutPhoneNumber() {
//        migrationService.createTemporaryMemberForMigrationInWithoutPhoneNumber();
//        return "Success";
//    }
//
    @GetMapping(value = "generateotp")
    public void generateOtp(@RequestParam("mobilenumber") String mobileNumber) {
        otpService.generateOtp(mobileNumber, "REGISTRATION_PROCESS_OTP");
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_GENERATE_OTP)
    public void generateOtpTecho(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        otpService.generateOtpTecho(mobileRequestParamDto);
    }

    @PostMapping(value = MobileApiPathConstants.TECHO_POST_VERIFY_OTP)
    public boolean verifyOtpTecho(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        return otpService.verifyOtpTecho(mobileRequestParamDto);
    }

    @GetMapping(value = "verifyotp")
    public boolean verifyOtp(@RequestParam("mobilenumber") String mobileNumber, @RequestParam("otp") String otp) {
        return otpService.verifyOtp(mobileNumber, otp);
    }

//    @GetMapping(value = "memberbyphonenumber")
//    public Map<String, Object> retrieveMemberByPhoneNumber(@RequestParam("mobilenumber") String mobileNumber) {
//        return familyHealthSurveyService.retrieveMemberByPhoneNumber(mobileNumber);
//    }
//
//    @PostMapping(value = "memberbyphonenumber")
//    public Map<String, Object> retrieveMemberByPhoneNumber(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) {
//        return familyHealthSurveyService.retrieveMemberByPhoneNumber(myTechoRequestParamDto.getMobileNumber());
//    }
//
//    @PostMapping(value = "createmytechouser")
//    public void createMyTechoUser(@RequestBody MyTechoUserMasterDto myTechoUser) {
//        myTechoUserService.createOrUpdateUser(myTechoUser);
//    }
//
//    @PostMapping(value = "createdrtechouser")
//    public void createDrTechoUser(@RequestBody DrTechoUserDto myTechoUser) {
//        if (!myTechoUser.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
//            return;
//        }
//        drTechoUserService.createOrUpdate(myTechoUser);
//    }
//
//    @GetMapping(value = "mytecho/checkmobilenumber")
//    public boolean checkUserExistsByPhoneNumberForMyTecho(@RequestParam("mobileNumber") String mobileNumber) {
//        return myTechoUserService.validatePhoneNumberForMyTecho(mobileNumber);
//    }
//
//    @GetMapping(value = "drtecho/checkmobilenumber")
//    public boolean checkUserExistsByPhoneNumberForDrTecho(@RequestParam("mobileNumber") String mobileNumber) {
//        return userService.validatePhoneNumberForDrTecho(mobileNumber);
//    }
//
//    @GetMapping(value = "checkusername")
//    public boolean isUsernameAvailable(@RequestParam("username") String username, @RequestParam(name = "user_id", required = false) Integer userId) {
//        return myTechoUserService.validateUserName(username);
//    }
//
//    @GetMapping(value = "getHealthInfraPrivateHospital")
//    public List<HealthInfrastructureBean> getHealthInfraPrivateHospital(@RequestParam("query") String query) {
//        return healthInfrastructureService.getHealthInfrastructurePrivateHospital(query);
//    }
//
//    @GetMapping(value = "mytechominimumversion")
//    public String retrieveMyTechoVersion(HttpServletRequest request) {
//        return myTechoUserService.retrieveMinimumSupportedAppVersion();
//    }
//
//    @PutMapping(value = "mytecho/forgotpassword")
//    public void resetPassword(@RequestBody UserPasswordDto userPasswordDto) {
//        myTechoUserService.resetPassword(userPasswordDto);
//    }
//
//    @GetMapping(value = "getfile")
//    public ResponseEntity<Resource> getAFile(@RequestParam("filename") String fileName, HttpServletRequest request) throws FileNotFoundException {
//        File file = uploadMultiMediaService.getLibraryFileByName(fileName);
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//        String contentType = "video/mp4";
//
//        return ResponseEntity.ok()
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION)
//                .body(resource);
//    }
//
//    @PostMapping(value = "mytecho/verifyMemberDetailByFamilyId")
//    public MemberDataBean verifyDobOrAadharOfMember(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) {
//        if (myTechoRequestParamDto.getFamilyId() == null) {
//            throw new ImtechoMobileException("Please Provide Family ID!", 501);
//        }
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(myTechoRequestParamDto.getDob());
//            return familyHealthSurveyService.verifyMemberDetailByFamilyId(myTechoRequestParamDto.getFamilyId(), date1.getTime(), myTechoRequestParamDto.getAadharNumber());
//        } catch (ParseException e) {
//            throw new ImtechoMobileException("Date parsing error", 501);
//        }
//    }
//
//    @PostMapping(value = "mytecho/verifyFamilyById")
//    public Map<String, Object> verifyFamilyById(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) {
//        if (myTechoRequestParamDto.getFamilyId() == null) {
//            throw new ImtechoMobileException("Please Provide Family ID!", 501);
//        }
//        return familyHealthSurveyService.verifyFamilyById(myTechoRequestParamDto.getFamilyId(), myTechoRequestParamDto.getMobileNumber());
//    }
//
//    @PostMapping(value = "mytecho/removeMobileNumbeExceptVerifiedFamily")
//    public void deleteMobileNumberInFamilyExceptVerifiedFamily(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) {
//        if (myTechoRequestParamDto.getFamilyId() == null) {
//            throw new ImtechoMobileException("Please Provide Family ID!", 501);
//        }
//        familyHealthSurveyService.deleteMobileNumberInFamilyExceptVerifiedFamily(myTechoRequestParamDto.getFamilyId(), myTechoRequestParamDto.getMobileNumber());
//    }
//
//    @GetMapping(value = "retrieveAllStates")
//    public List<LocationStateMasterDto> getAllStates() {
//        return locationStateMasterService.getAllStates();
//    }
//
//    @PostMapping(value = "mytecho/retrieveImtechoFamilyDataByMemberId")
//    public MyTechoFamilyDataBean retrieveImTechoFamilyDataByMemberId(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) throws ImtechoMobileException {
//        if (myTechoRequestParamDto.getMemberId() == null) {
//            throw new ImtechoMobileException("Please Provide Member ID!", 501);
//        }
//        return familyHealthSurveyService.retrieveImTechoFamilyDataByMemberId(myTechoRequestParamDto.getMemberId());
//    }
//
//    @PostMapping(value = "mytecho/retrieveFamilyIdByFamilyId")
//    public Integer retrieveFamilyIdByFamilyId(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) {
//        return familyHealthSurveyService.retrieveFamilyIdByFamilyId(myTechoRequestParamDto.getFamilyId());
//    }
//
//    @GetMapping(value = "/getimmunisationsforchild")
//    public Set<String> getDueImmunisationsForChild(@RequestParam Long dateOfBirth, @RequestParam(required = false) String givenImmunisations) throws ImtechoUserException {
//        return immunisationService.getDueImmunisationsForChild(new Date(dateOfBirth), givenImmunisations);
//    }
//
//    @PostMapping(value = "mytecho/isUserAllowToRegisterInMyTecho")
//    public Boolean checkUserIsAllowToRegisterInMyTecho(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) throws ImtechoMobileException {
//        return familyHealthSurveyService.checkUserIsAllowToRegisterInMyTecho(myTechoRequestParamDto.getMobileNumber(), myTechoRequestParamDto.getMemberId(), myTechoRequestParamDto.getFamilyId(), myTechoRequestParamDto.getIsUserNotIdentified());
//    }
//
//    @GetMapping(value = "/getTokens")
//    public String getLoginTokens(@RequestParam("token") String token) {
//        if ("fpPMI$LA5lZ".equals(token)) {
//            String grantType = "password";
//            String password = "12345678";
//            String clientId = "imtecho-ui";
//            String userName = "cmdashboard";
//
//            WebResource webResource = client.resource("https://techo.gujarat.gov.in" + "/oauth/token" + "?username=" + userName + "&grant_type=" + grantType + "&password=" + password + "&client_id=" + clientId + "&loginas=");
//            ClientResponse response = webResource
//                    .header("Content-Type", "application/json;charset=UTF-8")
//                    .header("Authorization", "Basic aW10ZWNoby11aTppbXRlY2hvLXVpLXNlY3JldA==")
//                    .post(ClientResponse.class);
//            return response.getEntity(String.class);
//        } else {
//            return "";
//        }
//    }
//
//    @PostMapping(value = "mytecho/getequaldobmembers")
//    public Map<String, Object> getEqualDobMembers(@RequestBody MyTechoRequestParamDto myTechoRequestParamDto) throws ImtechoMobileException {
//        return familyHealthSurveyService.getEqualDobMembers(myTechoRequestParamDto.getFamilyId(), myTechoRequestParamDto.getDob());
//    }
//
//    @GetMapping(value = "soh/elements")
//    public List<SohElementConfiguration> getElements() {
//        return mobileSohService.getElements();
//    }
//
//    @GetMapping(value = "soh/elementsJson")
//    public List<SohGroupByElementsDto> getElementsJson(@RequestParam(value = "userId", required = false) Integer userId) {
//        return mobileSohService.getElementsJson(userId);
//    }
//
//    @GetMapping(value = "soh/element/{id}")
//    public SohElementConfiguration getElementById(@PathVariable() Integer id) {
//        return mobileSohService.getElementById(id);
//    }
//
//    @PostMapping(value = "soh/element")
//    public void createOrUpdateElement(@RequestBody SohElementConfigurationDto sohElementConfigurationDto) {
//        mobileSohService.createOrUpdateElement(sohElementConfigurationDto);
//    }
//
//    @GetMapping(value = "soh/chartsJson")
//    public List<SohChartConfigurationDto> getChartsJson() {
//        return mobileSohService.getChartsJson();
//    }
//
//    @GetMapping(value = "soh/chart/{id}")
//    public SohChartConfiguration getChartById(@PathVariable() Integer id) {
//        return mobileSohService.getChartById(id);
//    }
//
//    @PostMapping(value = "soh/chart")
//    public void createOrUpdateChart(@RequestBody SohChartConfigurationDto sohChartConfigurationDto) {
//        mobileSohService.createOrUpdateChart(sohChartConfigurationDto);
//    }
//
//    @GetMapping(value = "soh/elementModules")
//    public List<SohElementModuleMaster> getElementModules(@RequestParam Boolean retrieveActiveOnly) {
//        return mobileSohService.getElementModules(retrieveActiveOnly);
//    }
//
//    @GetMapping(value = "soh/elementModule/{id}")
//    public SohElementModuleMaster getElementModuleById(@PathVariable() Integer id) {
//        return mobileSohService.getElementModuleById(id);
//    }
//
//    @PostMapping(value = "soh/elementModule")
//    public void createOrUpdateElementModule(@RequestBody SohElementModuleMasterDto sohElementModuleMasterDto) {
//        mobileSohService.createOrUpdateElementModule(sohElementModuleMasterDto);
//    }
//
//    @GetMapping(value = "covid/pincode")
//    public void getMapApiCovid() {
////        covidTravellersInfoService.mapPincodeWithLocation();
//    }

    @RequestMapping(value = "systemNotice", method = RequestMethod.GET)
    public SystemConfiguration retrieveSystemNotice() {
        return systemConfigurationService.retrieveSystemConfigurationByKey("SYSTEM_NOTICE");
    }

    @RequestMapping(value = "apkInfo", method = RequestMethod.GET)
    public List<String> retrieveApkInfo() {
        List<String> apkInfoList = new ArrayList<String>();
        try {
            apkInfoList.add(systemConfigurationService.retrieveSystemConfigurationByKey("ANDROID_APP_LINK").getKeyValue());
            apkInfoList.add(systemConfigurationService.retrieveSystemConfigurationByKey("ANDROID_APP_VERSION").getKeyValue());
            apkInfoList.add(systemConfigurationService.retrieveSystemConfigurationByKey("ANDROID_APP_RELEASE_DATE").getKeyValue());
        } catch (NullPointerException e) {
            System.out.print("NullPointerException Caught, add the required details in System Configuration");
        }
        return apkInfoList;
    }

    @RequestMapping(value = "lmsevententry", method = RequestMethod.POST)
    public List<RecordStatusBean> lmsEventEntryFromMobileToDB(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        System.out.println("IN LMS EVENT ENTRY");
        return lmsMobileEventSubmissionService.storeLmsMobileEventToDB(mobileRequestParamDto.getToken(), mobileRequestParamDto.getMobileEvents());
    }

//    @RequestMapping(value = "failedHealthIdData", method = RequestMethod.POST)
//    public void failedHealthIdData(@RequestBody FailedHealthIdDataEntity failedHealthIdDataEntity) {
//        System.out.println("failedHealthIdData: " + failedHealthIdDataEntity);
//        failedHealthIdDataService.failedHealthIdData(failedHealthIdDataEntity);
//    }

    @PostMapping(value = "user/changeLanguage")
    public void updateLanguagePreference(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
        userService.updateLanguagePreference(mobileRequestParamDto.getToken(), mobileRequestParamDto.getLanguageCode());
    }

    //
//    @GetMapping(value = "updateMediaSize/{courseId}")
//    public String updateMediaSize(@PathVariable(value = "courseId") String courseId) {
//        return courseMasterService.updateMediaSize(Integer.parseInt(courseId));
//    }
//
//    @RequestMapping(value = "firebaseToken", method = RequestMethod.POST)
//    public void firebaseToken(@RequestBody MobileRequestParamDto mobileRequestParamDto) {
//        System.out.println("Add Firebase Token");
//        userService.addFirebaseToken(mobileRequestParamDto.getUserId(), mobileRequestParamDto.getFirebaseToken());
//    }
//
    @GetMapping(value = "announcements/byhealthinfra")
    public List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(
            @RequestParam(name = "healthInfraId") Integer healthInfraId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset") Integer offset
    ) throws ParseException {
        return announcementService.getAnnouncementsByHealthInfra(healthInfraId, limit, offset);
    }

    @GetMapping(value = "announcements/unreadcount")
    public LinkedHashMap<String, Integer> getAnnouncementsUnreadCountByHealthInfra(@RequestParam(name = "healthInfraId") Integer healthInfraId) {
        return announcementService.getAnnouncementsUnreadCountByHealthInfra(healthInfraId);
    }

    @PutMapping(value = "announcements/markseen")
    public void markAnnouncementAsSeen(@RequestParam(name = "announcementId") Integer announcementId, @RequestParam(name = "healthInfraId") Integer healthInfraId) {
        announcementService.markAnnouncementAsSeen(announcementId, healthInfraId);
    }
//
//    @GetMapping(value = "chardham/initiatescan")
//    public ChardhamInitiateScanDto initiateDeviceScan(@RequestParam(name = "mmid") String mmId, @RequestParam(name = "cid") String cId) {
//        return chardhamMemberScreeningService.initiateDeviceScan(mmId, cId);
//    }
//
//    @PostMapping(value = "chardham/savescreeningdetailsfromdevice")
//    public ImtechoResponseEntity saveChardhamScreeningDetails(@RequestBody ChardhamScreeningDetailsByDeviceDto chardhamScreeningDetailsByDeviceDto) {
//        chardhamMemberScreeningService.saveScreeningDetailsFromDevice(chardhamScreeningDetailsByDeviceDto);
//        return new ImtechoResponseEntity("success", 200);
//    }
//
//    @GetMapping(value = "chardham/retrievescreeningbydevice")
//    public ChardhamTouristScreeningDto retrieveScreening(
//            @RequestParam(name = "mmId", required = true) String mmId,
//            @RequestParam(name = "uniqueId", required = true) String uniqueId,
//            @RequestParam(name = "scanId", required = true) String scanId
//    ) {
//        return chardhamMemberScreeningService.retrieveChardhamTouristScreeningByDevice(mmId, uniqueId, scanId);
//    }
//
//    @PostMapping(value = "updateUserDetails")
//    public void updateUserDetailsMobile(@RequestParam(name = "token") String token, @RequestBody MobUserInfoDataBean userDetails) {
//        userService.updateUserDetailsFromMobile(token, userDetails);
//    }
//
//    @GetMapping(value = "systemConfiguration")
//    public Map<String, String> getSystemConfigurationByKey(@RequestParam(name = "key") String key) {
//        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(key);
//        Map<String, String> map = new HashMap<>();
//        if (systemConfiguration != null) {
//            map.put(key, systemConfiguration.getKeyValue());
//        }
//        return map;
//    }
}
