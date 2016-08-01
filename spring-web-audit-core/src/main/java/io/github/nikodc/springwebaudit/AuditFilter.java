package io.github.nikodc.springwebaudit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nikodc.springwebaudit.servlet.HttpRequestHelper;
import io.github.nikodc.springwebaudit.servlet.HttpResponseHelper;
import io.github.nikodc.springwebaudit.servlet.HttpServletRequestWrapper;
import io.github.nikodc.springwebaudit.servlet.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class AuditFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuditFilter.class);

    private static final String AUDIT_INFO_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO";
    private static final String AUDIT_INFO_STATUS_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO_STATUS";
    private static final Integer AUDIT_INFO_STATUS_PENDING = 0;
    private static final Integer AUDIT_INFO_STATUS_PUBLISHING = 1;
    private static final Integer AUDIT_INFO_STATUS_PUBLISHED = 2;

    private static final String BODY_ERROR_INDICATOR = "$ERROR";

    private AtomicLong requestIdNumerator = new AtomicLong(1);

    @SuppressWarnings("unused")
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info("Retrieving audit info attribute...");

        AuditInfo auditInfo = (AuditInfo) request.getAttribute(AUDIT_INFO_ATTRIBUTE);
        if (auditInfo == null) {
            log.info("Audit info attribute not found, creating it...");

            auditInfo = new AuditInfo();
            auditInfo.setRequestId(requestIdNumerator.getAndIncrement());
            request.setAttribute(AUDIT_INFO_ATTRIBUTE, auditInfo);
            request.setAttribute(AUDIT_INFO_STATUS_ATTRIBUTE, AUDIT_INFO_STATUS_PENDING);
        }

        if (!AUDIT_INFO_STATUS_PENDING.equals(request.getAttribute(AUDIT_INFO_STATUS_ATTRIBUTE))) {
            log.error("Cannot proceed, audit info already published");
            throw new ServletException("Audit info already published");
        }

        log.info("Done");

        log.info("Wrapping request and response...");

        HttpServletRequestWrapper htpServletRequestWrapper = new HttpServletRequestWrapper(
                (HttpServletRequest) request);
        HttpServletResponseWrapper htpServletResponseWrapper = new HttpServletResponseWrapper(
                (HttpServletResponse) response);

        log.info("Done");

        log.info("Processing request...");

        RequestInfo requestInfo = requestInfo(htpServletRequestWrapper);
        if (auditInfo.getRequestInfo() == null) {
            log.info("Initial request detected, updating audit info attribute...");
            auditInfo.setRequestInfo(requestInfo);
        }

        log.info("Done");

        log.info("Continuing with filter chain...");

        chain.doFilter(htpServletRequestWrapper, htpServletResponseWrapper);

        log.info("Processing response...");

        ResponseInfo responseInfo = responseInfo(htpServletResponseWrapper);
        log.info("Response found, updating audit info attribute...");
        auditInfo.setResponseInfo(responseInfo);

        log.info("Done");

        if (responseInfo != null && responseInfo.getBody() != null) {

            log.info("Final response detected, publishing...");

            request.setAttribute(AUDIT_INFO_STATUS_ATTRIBUTE, AUDIT_INFO_STATUS_PUBLISHING);
            log.info("auditInfo: \n{}", new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(auditInfo));
            request.setAttribute(AUDIT_INFO_STATUS_ATTRIBUTE, AUDIT_INFO_STATUS_PUBLISHED);

            log.info("Done");
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    private RequestInfo requestInfo(HttpServletRequestWrapper htpServletRequestWrapper) {

        HttpRequestHelper requestHelper = new HttpRequestHelper(htpServletRequestWrapper);

        String body = null;
        try {
            body = requestHelper.getBody();
        } catch(Exception e) {
            log.error("Exception extracting HTTP request body", e);
            body = BODY_ERROR_INDICATOR;
        }

        RequestInfo requestInfo = new RequestInfo(
            requestHelper.getProtocol(),
            requestHelper.getMethod(),
            requestHelper.getPath(),
            requestHelper.getHeaders(),
            requestHelper.getParameters(),
            body,
            requestHelper.getRemoteIpAddress(),
            requestHelper.getLocalIpAddress());

        return requestInfo;
    }

    private ResponseInfo responseInfo(HttpServletResponseWrapper httpServletResponseWrapper) {

        HttpResponseHelper responseHelper = new HttpResponseHelper(httpServletResponseWrapper);

        String body = null;
        try {
            body = responseHelper.getBody();
        } catch(Exception e) {
            log.error("Exception extracting HTTP response body", e);
            body = BODY_ERROR_INDICATOR;
        }

        ResponseInfo responseInfo = new ResponseInfo(
                responseHelper.getStatus(),
                responseHelper.getHeaders(),
                body);

        return responseInfo;
    }

}
