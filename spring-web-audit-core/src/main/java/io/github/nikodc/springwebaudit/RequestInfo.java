package io.github.nikodc.springwebaudit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RequestInfo {

    private String protocol;

    private String method;

    private String path;

    private Map<String, List<String>> headers;

    private Map<String, List<String>> parameters;

    private String body;

    private String remoteIpAddress;

    private String localIpAddress;

    public RequestInfo(String protocol, String method, String path, Map<String, List<String>> headers,
            Map<String, List<String>> parameters, String body, String remoteIpAddress, String localIpAddress) {

        this.protocol = protocol;
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.parameters = parameters;
        this.body = body;
        this.remoteIpAddress = remoteIpAddress;
        this.localIpAddress = localIpAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public String getBody() throws IOException {
        return body;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public String getLocalIpAddress() {
        return localIpAddress;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                ", body='" + body + '\'' +
                ", remoteIpAddress='" + remoteIpAddress + '\'' +
                ", localIpAddress='" + localIpAddress + '\'' +
                '}';
    }
}
