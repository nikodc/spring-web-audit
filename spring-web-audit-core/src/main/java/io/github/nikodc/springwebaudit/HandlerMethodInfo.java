package io.github.nikodc.springwebaudit;

import java.util.ArrayList;
import java.util.List;

public class HandlerMethodInfo {

    private String type;

    private String returnType;

    private String method;

    private String signature;

    private Object returnValue;

    private List<HandlerParameterInfo> parameters = new ArrayList<>();

    private HandlerExceptionInfo exceptionInfo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public HandlerExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(HandlerExceptionInfo exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public List<HandlerParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<HandlerParameterInfo> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "HandlerMethodInfo{" +
                "type='" + type + '\'' +
                ", returnType='" + returnType + '\'' +
                ", method='" + method + '\'' +
                ", signature='" + signature + '\'' +
                ", returnValue=" + returnValue +
                ", parameters=" + parameters +
                ", exceptionInfo=" + exceptionInfo +
                '}';
    }
}
