package io.github.nikodc.springwebaudit.servlet;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {

    private ServletOutputStream servletOutputStream;
    private ByteArrayOutputStream extraOutputStream;
    private PrintWriter printWriter;
    private DelegatingServletOutputStream delegatingServletOutputStream;

    public HttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream == null) {
            wrapStreamAndWriter();
        }

        return delegatingServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            wrapStreamAndWriter();
        }

        return printWriter;
    }

    public String getBodyAsString() throws IOException {
        if (extraOutputStream != null) {
            extraOutputStream.flush();
            return new String(extraOutputStream.toByteArray());
        }
        return null;
    }

    public byte[] getBody() throws IOException {
        if (extraOutputStream != null) {
            extraOutputStream.flush();
            return extraOutputStream.toByteArray();
        }
        return null;
    }

    private void wrapStreamAndWriter() throws IOException {
        servletOutputStream = getResponse().getOutputStream();
        extraOutputStream = new ByteArrayOutputStream();
        delegatingServletOutputStream = new DelegatingServletOutputStream(
                new TeeOutputStream(servletOutputStream, extraOutputStream));
        printWriter = new PrintWriter(new OutputStreamWriter(delegatingServletOutputStream,
                getResponse().getCharacterEncoding()), true);
    }
}
