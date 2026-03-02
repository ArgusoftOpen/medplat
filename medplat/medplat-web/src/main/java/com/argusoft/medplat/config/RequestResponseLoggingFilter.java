/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.QueryAndResponseAnalysisService;
import com.argusoft.medplat.config.requestResponseFilter.MultiReadHttpServletRequest;
import com.argusoft.medplat.config.requestResponseFilter.constant.RequestResponseConstant;
import com.argusoft.medplat.config.requestResponseFilter.dto.RequestResponseDetailsDto;
import com.argusoft.medplat.config.requestResponseFilter.service.RequestResponseDetailsService;
import com.argusoft.medplat.config.requestResponseFilter.service.impl.RequestResponseDetailsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author Harshit
 */
@Component
public class RequestResponseLoggingFilter implements Filter {

    public static boolean isFilterStarted = true;

    private boolean serverFlag = true;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Autowired
    RequestResponseDetailsService requestResponseDetailsService;

    @Autowired
    QueryAndResponseAnalysisService queryAndResponseAnalysisService;



    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        Instant start = Instant.now();
        String payloadForPostRequest = null;
        String responseSizeInBytes = null;
        HttpServletRequest req = (HttpServletRequest) request;
        String paramString = req.getQueryString();
        String currentUserName = getUserName();
        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) response);
        if (!isFilterStarted) {
            MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
            if ("POST".equalsIgnoreCase(req.getMethod())) {
                payloadForPostRequest =  multiReadRequest.getReader().readLine();
            }
            chain.doFilter(multiReadRequest, responseCacheWrapperObject);
            responseSizeInBytes = logApisWhoHaveBigPayload(request, responseCacheWrapperObject);
            responseCacheWrapperObject.copyBodyToResponse();
        } else if (regXMatcher(req.getRequestURL())) {
            chain.doFilter(request, responseCacheWrapperObject);
            responseSizeInBytes = logApisWhoHaveBigPayload(request, responseCacheWrapperObject);
            responseCacheWrapperObject.copyBodyToResponse();
        } else {
            String randomUUID = UUID.randomUUID().toString();
            Date startTime = new Date();
            Integer pageTitleId = getPageTitleId(req.getHeader("title"));
            Integer urlId = getUrlId(req.getRequestURL().toString());

            MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
            if ("POST".equalsIgnoreCase(req.getMethod())) {
                payloadForPostRequest = multiReadRequest.getReader().readLine();
            }

            RequestResponseDetailsDto requestResponseDetailsDto = new RequestResponseDetailsDto(
                    currentUserName, randomUUID,
                    paramString, payloadForPostRequest, startTime,
                    null, req.getRemoteAddr(),
                    pageTitleId, urlId);

            RequestResponseDetailsServiceImpl.hm.put(randomUUID, requestResponseDetailsDto);

            chain.doFilter(multiReadRequest, responseCacheWrapperObject);
            responseSizeInBytes = logApisWhoHaveBigPayload(request, responseCacheWrapperObject);
            responseCacheWrapperObject.copyBodyToResponse();

            Date endTime = new Date();

            RequestResponseDetailsDto responseDetailsDto = RequestResponseDetailsServiceImpl.hm.get(randomUUID);
            if (responseDetailsDto != null) {
                responseDetailsDto.setEndTime(endTime);
            } else if (requestResponseDetailsDto.getId() != null) {
                requestResponseDetailsService.updateEndTimeToDB(requestResponseDetailsDto.getId(), endTime);
            }
        }
        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        if(time > ConstantUtil.MAXIMUM_TIME_TAKEN_BY_API){
            logger.info("request URL  = {}, request Body = {}, request param = {}, time taken = {} in ms ", ((HttpServletRequest) request).getRequestURI(), payloadForPostRequest, paramString,  time);
            queryAndResponseAnalysisService.insertResponseAnalysisByTimeDetails(((HttpServletRequest) request).getRequestURI(), payloadForPostRequest, paramString, String.valueOf(time));
        }
    }

    private String getUserName() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                return authentication.getName();
            }
        } catch (NullPointerException ne) {
//            To handle null value when anonymous token
        }
        return null;
    }

    private String logApisWhoHaveBigPayload(ServletRequest request, ContentCachingResponseWrapper responseCacheWrapperObject) {
        String responseBytes = null;
        try {
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
            if (responseArray.length > ConstantUtil.MAXIMUM_SIZE_OF_PAYLOAD_IN_BYTES) {
                logger.info("request URL  = {} , size = {} in Bytes", url, responseArray.length);
            }
            responseBytes = String.valueOf(responseArray.length);
        } catch (Exception e) {
            logger.info("Exception in order to log API: {}", e.getMessage());
        }

        return responseBytes;
    }


    public Integer getPageTitleId(String pageTitle) {
        if (pageTitle == null) {
            return -1;
        }
        Integer pageTitleId = RequestResponseDetailsServiceImpl.page_title_map.get(pageTitle);
        if (pageTitleId == null) {
            pageTitleId = requestResponseDetailsService.insertPageTitle(pageTitle);
        }
        return pageTitleId;
    }

    private Integer getUrlId(String url) {
        if (url == null) {
            return null;
        }
        url = url.replace("www.", "");
        Integer urlId = RequestResponseDetailsServiceImpl.url_map.get(url);
        if (urlId == null) {
            urlId = requestResponseDetailsService.insertUrl(url);
        }
        return urlId;
    }

    //    This will match regex list
    public boolean regXMatcher(StringBuffer api) {
        if (!serverFlag) {
            return true;
        }
        for (Pattern pattern : RequestResponseConstant.API_TO_BE_IGNORED_REGEX_ARRAY) {
            if (pattern.matcher(api).matches()) {
                return true;
            }
        }
        return false;
    }

    // other methods
    @Override
    public void init(FilterConfig fc) throws ServletException {
//    Do something
    }

    @Override
    public void destroy() {
//        Do Something
    }
}
