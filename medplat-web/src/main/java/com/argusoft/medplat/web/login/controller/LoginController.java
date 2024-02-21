package com.argusoft.medplat.web.login.controller;

import com.argusoft.medplat.common.dto.EncryptionKeyAndIVDto;
import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.mapper.MenuConfigMapper;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.service.MenuConfigService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.EmailUtil;
import com.argusoft.medplat.common.util.LoginAESEncryptionKeyManager;
import com.argusoft.medplat.config.requestResponseFilter.service.RequestResponseDetailsService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.fcm.service.TechoPushNotificationHandler;
import com.argusoft.medplat.fcm.service.impl.TechoPushNotificationHandlerImpl;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.reportconfig.service.ReportQueueService;
import com.argusoft.medplat.reportconfig.service.impl.ReportQueueServiceImpl;
import com.argusoft.medplat.sms_queue.service.SmsQueueService;
import com.argusoft.medplat.sms_queue.service.impl.SmsQueueServiceImpl;
import com.argusoft.medplat.timer.service.TimerEventService;
import com.argusoft.medplat.timer.service.impl.TimerEventServiceImpl;
import com.argusoft.medplat.web.healthinfra.service.HealthInfrastructureService;
import com.argusoft.medplat.web.location.service.LocationService;
import com.argusoft.medplat.web.login.dto.LoginDto;
import com.argusoft.medplat.web.login.mapper.LoginMapper;
import com.argusoft.medplat.web.users.model.UserMenuItem;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Defines rest endpoints for login</p>
 *
 * @author hshah
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private MenuConfigService menuConfigService;

    @Autowired
    @Qualifier("timerEventServiceDefault")
    private TimerEventService timerEventService;

    @Autowired
    private SmsQueueService smsQueueService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MobileUtilService mobileUtilService;

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private HealthInfrastructureService healthInfrastructureService;

    @Autowired
    private RequestResponseDetailsService requestResponseDetailsService;

    @Autowired
    private DefaultListableBeanFactory context;

    @Autowired
    private ReportQueueService reportQueueService;

    @Autowired
    private TechoPushNotificationHandler techoPushNotificationHandler;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping(value = "/get-key-and-iv")
    public EncryptionKeyAndIVDto getKeyAndIV() {
        return new EncryptionKeyAndIVDto(LoginAESEncryptionKeyManager.getKey(), LoginAESEncryptionKeyManager.getInitVector());
    }

    /**
     * Returns instance of LoginDto
     *
     * @return An instance of LoginDto
     */
    @GetMapping(value = "/principle")
    public LoginDto getPrinciple() {
        return LoginMapper.convertToLoginDto(user);
    }

    /**
     * Returns a list of main menu with its child menu
     *
     * @return A map of main menu and its child menu
     */
    @GetMapping(value = "/menu")
    public Map<String, List<MenuConfigDto>> getMenuDetail() {

        Map<String, List<MenuConfigDto>> mainMenuDtoMap = new HashMap<>();
        Map<String, List<MenuConfig>> mainMenuConfigMap = new HashMap<>();

        List<MenuConfig> menuConfigList
                = menuConfigService.getActiveMenusByUserIdAndDesignationAndGroup(Boolean.TRUE, user.getId(), user.getRoleId());

        menuConfigList.stream().forEach(menuConfig -> {
            List<MenuConfig> menuConfigs = mainMenuConfigMap.computeIfAbsent(menuConfig.getType(), k -> new ArrayList<>());

            if (!CollectionUtils.isEmpty(menuConfig.getUserMenuItemList())) {
                boolean found = false;
                for (UserMenuItem userMenuItem : menuConfig.getUserMenuItemList()) {
                    if (userMenuItem.getFeatureJson() != null && userMenuItem.getUserId() != null) {
                        menuConfig.setFeatureJson(userMenuItem.getFeatureJson());
                        found = true;
                    }
                }
                if (Boolean.FALSE.equals(found)) {
                    for (UserMenuItem userMenuItem : menuConfig.getUserMenuItemList()) {
                        if (userMenuItem.getFeatureJson() != null && userMenuItem.getRoleId() != null) {
                            menuConfig.setFeatureJson(userMenuItem.getFeatureJson());
                            found = true;
                        }
                    }
                }
                if (Boolean.FALSE.equals(found)) {
                    for (UserMenuItem userMenuItem : menuConfig.getUserMenuItemList()) {
                        if (userMenuItem.getFeatureJson() != null) {
                            menuConfig.setFeatureJson(userMenuItem.getFeatureJson());
                        }
                    }
                }
            }
            menuConfigs.add(menuConfig);
        });

        if (!mainMenuConfigMap.isEmpty()) {
            for (Map.Entry<String, List<MenuConfig>> entrySet : mainMenuConfigMap.entrySet()) {
                String key = entrySet.getKey();
                List<MenuConfig> menuConfigListByType = entrySet.getValue();
                mainMenuDtoMap.put(key, MenuConfigMapper.getMenuConfigAsMenuDtoList(menuConfigListByType, new ArrayList<>(), null, null));
            }
            return mainMenuDtoMap;
        }
        return null;
    }

    @PostConstruct
    public void initApplicationScope() {
        if (!ConstantUtil.SERVER_TYPE.equals("RCH")) {
            if (!ConstantUtil.SERVER_TYPE.equals("DEV")) {
//                cronExecutorService.execute(tenantMaster ->
                timerEventService.setNewStatusToProcessingEvents();
                timerEventService.startTimerThread();
                smsQueueService.startSmsThread();
//                );
                this.createSMSEventBeans();
                if (ConstantUtil.SERVER_TYPE.equals("LIVE")) {
                    emailUtil.sendEmail("Server Restarted");
                }
            }
            this.createReportOfflineBeans();
            this.createTimerEventBeans();
            this.createPushNotificationBeans();
            mobileUtilService.updateProcessingStringsToPendingOnServerStartup();
            techoPushNotificationHandler.startPushNotification();
//            cronExecutorService.execute(tenantMaster -> {
                mobileUtilService.updateProcessingStringsToPendingOnServerStartup();
            smsQueueService.updateStatusFromProcessedToNewOnServerStartup();
                reportQueueService.updateStatusFromProcessedToNewOnServerStartup();
//            });

        }
//        cronExecutorService.execute(tenantMaster -> {
        locationService.updateAllActiveLocationsWithWorkerInfo();
        healthInfrastructureService.updateAllHealthInfrastructureForMobile();
        // DO NOT ADD TRY CATCH HERE.. IF YOU ARE FACING ERROR IN THIS CODE.. CONTACT PRATEEK
        mobileFhsService.setXlsSheetsAsComponentTagsInMemory();
        mobileFhsService.updateAllSchoolDetailsForMobile();
        requestResponseDetailsService.setPageTitleMapping();
        requestResponseDetailsService.setUrlMapping();
        requestResponseDetailsService.setApiToBeIgnoredForReqResFilter();
        LoginAESEncryptionKeyManager.generateAndSetKey();

//        requestResponseDetailsService.initCacheVariables();
//            try {
//                ndhmCommonUtilService.loadNdhmDevURL();
//                ndhmCommonUtilService.loadABDMClientIdSecretURL();
//                ndhmCommonUtilService.loadDataRequestURL();
//                healthIdCommonUtilService.loadHealthIdURL();
//                healthIdCommonUtilService.loadOtherHealthIdURL();
//                hfrCommonUtilService.loadHFRURL();
//                try {
//                    hprCommonUtilService.loadHPRURL();
//                } catch (Exception e) {
//                    logger.error(e.getMessage());
//                }
//            } catch (ImtechoUserException e) {
//                logger.error(e.getMessage());
//            }
//        });

    }

    private void createTimerEventBeans() {
//        cronExecutorService.execute(tenantMaster -> {           // create beans upto N tenants and register with spring context
            GenericBeanDefinition genericBeanDefinition1 = new GenericBeanDefinition();
            genericBeanDefinition1.setBeanClass(TimerEventServiceImpl.class);
            String beanName =  "TimerEventServiceImpl";
            context.registerBeanDefinition(beanName, genericBeanDefinition1);
            TimerEventServiceImpl bean = (TimerEventServiceImpl) context.getBean(beanName);
            bean.startTimerThread();
//        });
    }
//
    private void createSMSEventBeans() {
//        cronExecutorService.execute(tenantMaster -> {
            GenericBeanDefinition genericBeanDefinition1 = new GenericBeanDefinition();
            genericBeanDefinition1.setBeanClass(SmsQueueServiceImpl.class);
            String beanName = "SmsQueueServiceImpl";
            context.registerBeanDefinition(beanName, genericBeanDefinition1);
            SmsQueueServiceImpl bean = (SmsQueueServiceImpl) context.getBean(beanName);
            bean.startSmsThread();
//        });
    }
//
    private void createPushNotificationBeans() {
//        cronExecutorService.execute(tenantMaster -> {
            GenericBeanDefinition genericBeanDefinition1 = new GenericBeanDefinition();
            genericBeanDefinition1.setBeanClass(TechoPushNotificationHandlerImpl.class);
            String beanName = "TechoPushNotificationHandlerImpl";
            context.registerBeanDefinition(beanName, genericBeanDefinition1);
            TechoPushNotificationHandlerImpl bean = (TechoPushNotificationHandlerImpl) context.getBean(beanName);
            bean.startPushNotification();
//        });
    }
//
    private void createReportOfflineBeans() {
//        cronExecutorService.execute(tenantMaster -> {
            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
            genericBeanDefinition.setBeanClass(ReportQueueServiceImpl.class);
            String beanName = "ReportQueueServiceImpl";
            context.registerBeanDefinition(beanName, genericBeanDefinition);
            ReportQueueServiceImpl bean = (ReportQueueServiceImpl) context.getBean(beanName);
            bean.startReportThread();
//        });
    }
}
