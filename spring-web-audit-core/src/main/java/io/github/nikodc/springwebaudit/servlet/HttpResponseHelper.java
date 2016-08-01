package io.github.nikodc.springwebaudit.servlet;

import java.io.IOException;
import java.util.*;

public class HttpResponseHelper {

    private HttpServletResponseWrapper httpServletResponseWrapper;

    public HttpResponseHelper(HttpServletResponseWrapper httpServletResponseWrapper) {
        this.httpServletResponseWrapper = httpServletResponseWrapper;
    }

    public int getStatus() {
        return httpServletResponseWrapper.getStatus();
    }

    public Map<String, List<String>> getHeaders() {
        Map<String, List<String>> result = new HashMap<>();

        Collection<String> headerNames = httpServletResponseWrapper.getHeaderNames();
        for (String headerName : headerNames) {

            List<String> resultHeaderValues = new ArrayList<>();
            result.put(headerName, resultHeaderValues);

            Collection<String> headerValues = httpServletResponseWrapper.getHeaders(headerName);
            resultHeaderValues.addAll(headerValues);
        }

        return result;
    }

    public String getBody() throws IOException {
        return httpServletResponseWrapper.getBodyAsString();
    }

}
