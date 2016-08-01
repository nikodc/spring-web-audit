package io.github.nikodc.springwebaudit;

public class AuditInfo {

    private long requestId;

    private RequestInfo requestInfo;

    private HandlerInfo handlerInfo;

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

    public HandlerInfo getHandlerInfo() {
        return handlerInfo;
    }

    public void setHandlerInfo(HandlerInfo handlerInfo) {
        this.handlerInfo = handlerInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
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
                ", handlerInfo=" + handlerInfo +
                ", responseInfo=" + responseInfo +
                '}';
    }

}
