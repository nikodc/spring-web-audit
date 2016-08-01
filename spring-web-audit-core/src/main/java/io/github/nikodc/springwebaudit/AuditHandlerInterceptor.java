package io.github.nikodc.springwebaudit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AuditHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(AuditHandlerInterceptor.class);

    private static final String AUDIT_INFO_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO";
    private static final String AUDIT_INFO_STATUS_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO_STATUS";
    private static final Integer AUDIT_INFO_STATUS_PENDING = 0;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("Checking @AuditableMethod annotation present...");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethodAnnotation(AuditableMethod.class) == null) {

            log.info("@AuditableMethod annotation not present, returning");
            return true;
        }

        log.info("@AuditableMethod annotation present");

        log.info("Retrieving audit info attribute...");

        AuditInfo auditInfo = (AuditInfo) request.getAttribute(AUDIT_INFO_ATTRIBUTE);
        if (auditInfo == null) {
            log.error("Cannot proceed, audit info not found");
            throw new RuntimeException("Audit info not found");
        }

        if (!AUDIT_INFO_STATUS_PENDING.equals(request.getAttribute(AUDIT_INFO_STATUS_ATTRIBUTE))) {
            log.error("Cannot proceed, audit info already published");
            throw new RuntimeException("Audit info already published");
        }

        log.info("Done");

        log.info("Updating audit info attribute...");

        HandlerMethodInfo handlerMethodInfo = new HandlerMethodInfo();
        handlerMethodInfo.setType(handlerMethod.getMethod().getDeclaringClass().getName());
        if (!handlerMethod.isVoid()) {
            handlerMethodInfo.setReturnType(handlerMethod.getReturnType().getParameterType().getName());
        }
        handlerMethodInfo.setMethod(handlerMethod.getMethod().getName());
        handlerMethodInfo.setSignature(handlerMethod.getMethod().toGenericString());
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            HandlerParameterInfo handlerParameterInfo = new HandlerParameterInfo();
            handlerParameterInfo.setName(methodParameter.getParameterName());
            handlerParameterInfo.setType(methodParameter.getParameterType().getName());
            handlerMethodInfo.getParameters().add(handlerParameterInfo);
        }
        auditInfo.setHandlerMethodInfo(handlerMethodInfo);

        log.info("Done");

        log.info("Continuing with handler chain...");

        try {

            return super.preHandle(request, response, handler);

        } finally {

            log.info("Done");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        log.info("Checking @AuditableMethod annotation present...");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethodAnnotation(AuditableMethod.class) == null) {

            log.info("@AuditableMethod annotation not present");

        } else {

            log.info("@AuditableMethod annotation present");

            log.info("Retrieving audit info attribute...");

            AuditInfo auditInfo = (AuditInfo) request.getAttribute(AUDIT_INFO_ATTRIBUTE);
            if (auditInfo == null) {
                log.error("Cannot proceed, audit info not found");
                throw new RuntimeException("Audit info not found");
            }

            if (!AUDIT_INFO_STATUS_PENDING.equals(request.getAttribute(AUDIT_INFO_STATUS_ATTRIBUTE))) {
                log.error("Cannot proceed, audit info already published");
                throw new RuntimeException("Audit info already published");
            }

            log.info("Done");

            HandlerMethodInfo handlerMethodInfo = auditInfo.getHandlerMethodInfo();
            if (ex != null) {

                log.info("Exception found, updating audit info attribute...");

                HandlerExceptionInfo handlerExceptionInfo = new HandlerExceptionInfo();
                handlerExceptionInfo.setType(ex.getClass().getName());
                handlerExceptionInfo.setMessage(ex.getMessage());
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                handlerExceptionInfo.setStackTrace(sw.toString());
                handlerMethodInfo.setExceptionInfo(handlerExceptionInfo);
            }
            auditInfo.setHandlerMethodInfo(handlerMethodInfo);

            log.info("Done");

        }

        log.info("Continuing with handler chain...");

        super.afterCompletion(request, response, handler, ex);

        log.info("Done");
    }

}
