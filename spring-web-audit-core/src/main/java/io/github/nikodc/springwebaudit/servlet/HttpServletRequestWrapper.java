package io.github.nikodc.springwebaudit.servlet;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

    private byte[] rawData;

    private HttpServletRequest httpServletRequest;

    private DelegatingServletInputStream servletInputStream;

    public HttpServletRequestWrapper(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            readStreamAndReset();
        }
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            readStreamAndReset();
        }
        return new BufferedReader(new InputStreamReader(servletInputStream));
    }


    public String getBodyAsString() throws IOException {
        if (rawData == null) {
            readStreamAndReset();
        }
        return new String(rawData);
    }

    public byte[] getBody() throws IOException {
        if (rawData == null) {
            readStreamAndReset();
        }
        return rawData;
    }

    private void readStreamAndReset() throws IOException {
        rawData = IOUtils.toByteArray(this.httpServletRequest.getInputStream());
        servletInputStream = new DelegatingServletInputStream( new ByteArrayInputStream(rawData));
    }

}
