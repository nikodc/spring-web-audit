package io.github.nikodc.springwebaudit;

public class AuditInfo {

    private long requestId;

    private RequestInfo requestInfo;

    private HandlerMethodInfo handlerMethodInfo;

    private ResponseInfo responseInfo;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public HandlerMethodInfo getHandlerMethodInfo() {
        return handlerMethodInfo;
    }

    public void setHandlerMethodInfo(HandlerMethodInfo handlerMethodInfo) {
        this.handlerMethodInfo = handlerMethodInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    @Override
    public String toString() {
        return "AuditInfo{" +
                "requestId=" + requestId +
                ", requestInfo=" + requestInfo +
                ", handlerMethodInfo=" + handlerMethodInfo +
                ", responseInfo=" + responseInfo +
                '}';
    }
}
