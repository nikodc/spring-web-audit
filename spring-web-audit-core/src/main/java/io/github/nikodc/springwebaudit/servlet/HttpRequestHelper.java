package io.github.nikodc.springwebaudit.servlet;

import java.io.IOException;
import java.util.*;

public class HttpRequestHelper {

    private HttpServletRequestWrapper httpServletRequestWrapper;

    public HttpRequestHelper(HttpServletRequestWrapper httpServletRequestWrapper) {
        this.httpServletRequestWrapper = httpServletRequestWrapper;
    }

    public String getProtocol() {
        return httpServletRequestWrapper.getProtocol();
    }

    public String getMethod() {
        return httpServletRequestWrapper.getMethod();
    }

    public String getPath() {
        String pathInfo = httpServletRequestWrapper.getRequestURI();
        if (pathInfo == null) {
            pathInfo = "";
        }
        return pathInfo;
    }

    public Map<String, List<String>> getHeaders() {
        Map<String, List<String>> result = new HashMap<>();

        Enumeration<String> headerNames = httpServletRequestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            List<String> resultHeaderValues = new ArrayList<>();
            result.put(headerName, resultHeaderValues);

            Enumeration<String> headerValues = httpServletRequestWrapper.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                resultHeaderValues.add(headerValue);
            }
        }

        return result;
    }

    public Map<String, List<String>> getParameters() {
        Map<String, List<String>> result = new HashMap<>();

        Map<String, String[]> parameterMap = httpServletRequestWrapper.getParameterMap();

        Set<String> parameterNames = parameterMap.keySet();
        for (String parameterName : parameterNames) {
            String[] parameterValues = parameterMap.get(parameterName);

            List<String> resultParameterValues = Arrays.asList(parameterValues);
            result.put(parameterName, resultParameterValues);
        }

        return result;
    }

    public String getBody() throws IOException {
        return httpServletRequestWrapper.getBodyAsString();
    }

    public String getRemoteIpAddress() {
        String ipAddress = httpServletRequestWrapper.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequestWrapper.getRemoteAddr();
        }
        return ipAddress;
    }

    public String getLocalIpAddress() {
        return httpServletRequestWrapper.getLocalAddr();
    }

}
