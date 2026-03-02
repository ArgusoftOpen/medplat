package com.argusoft.medplat.web.users.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.web.users.dto.UserUsageAnalyticsDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Defines rest end points for user usage</p>
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/api/insert_user_analytics_details")
@Tag(name = "User Usage Analytics Controller", description = "")
public class UserUsageAnalyticsController {

//    @Autowired
//    @Qualifier(value = "userUsageAnalyticsDbApiList")
//    private TenantCacheProvider<List<UserUsageAnalyticsDto>> tenantCacheProviderForUserUsageAnalyticsDbApiList;
//
//    @Autowired
//    @Qualifier(value = "isUserUsageAnalyticsActive")
//    private TenantCacheProvider<Boolean> tenantCacheProviderForIsUserUsageAnalyticsActive;
//
//    @Autowired
//    private UserUsageAnalyticsService userUsageAnalyticsService;
//    /**
//     * Create user usage analytics
//     * @param userUsageAnalyticsDto An instance of UserUsageAnalyticsDto
//     */
//    @PostMapping(value = "")
//    public void addUserUsageDetails(@RequestBody UserUsageAnalyticsDto userUsageAnalyticsDto) {
//        if (Boolean.TRUE.equals(tenantCacheProviderForIsUserUsageAnalyticsActive.get()) && userUsageAnalyticsDto.getCurrStateId() != null) {
//            tenantCacheProviderForUserUsageAnalyticsDbApiList.get().add(userUsageAnalyticsDto);
//        }
//    }

    public static boolean isUserAnalyticsActive = true;

    public static List<UserUsageAnalyticsDto> USER_USAGE_ANALYTICS_DB_API_LIST = new LinkedList<>();

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void addUserUsageDetails(@RequestBody UserUsageAnalyticsDto userUsageAnalyticsDto) {
        if (isUserAnalyticsActive && userUsageAnalyticsDto.getCurrStateId() != null) {
            USER_USAGE_ANALYTICS_DB_API_LIST.add(userUsageAnalyticsDto);
        }

    }

}