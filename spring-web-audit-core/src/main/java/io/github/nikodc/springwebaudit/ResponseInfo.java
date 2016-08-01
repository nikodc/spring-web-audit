package io.github.nikodc.springwebaudit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResponseInfo {

    private int status;

    private Map<String, List<String>> headers;

    private String body;

    public ResponseInfo(int status, Map<String, List<String>> headers, String body) {

        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() throws IOException {
        return body;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "status=" + status +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
