package com.argusoft.medplat.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Completely disable browser caching.
 *
 * @author
 */
@Component
public class NoCacheFilter implements Filter {


    /**
     * Place this filter into service.
     *
     * @param filterConfig the filter configuration object used by a servlet
     *                     container to pass information to a filter during initialization
     * @throws ServletException to inform the container to not place the filter
     *                          into service
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty method
    }

    /**
     * Set cache header directives.
     *
     * @param servletRequest  provides data including parameter name and values,
     *                        attributes, and an input stream
     * @param servletResponse assists a servlet in sending a response to the
     *                        client
     * @param filterChain     gives a view into the invocation chain of a filtered
     *                        request
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", -1);
            httpServletResponse.setHeader("Date", new Date().toString());
            httpServletResponse.setHeader("Server", "TeCHO+ Server");
            httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Take this filter out of service.
     */
    @Override
    public void destroy() {
        // empty method
    }
}
