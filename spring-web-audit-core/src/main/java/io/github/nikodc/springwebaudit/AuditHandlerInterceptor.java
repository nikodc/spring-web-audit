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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

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

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        HandlerInfo handlerInfo = new HandlerInfo();
        handlerInfo.setClassName(handlerMethod.getMethod().getDeclaringClass().getName());
        handlerInfo.setMethodName(handlerMethod.getMethod().getName());
        handlerInfo.setMethodSignature(handlerMethod.getMethod().toGenericString());
        if (ex != null) {
            handlerInfo.setExceptionClassName(ex.getClass().getName());
            handlerInfo.setExceptionMessage(ex.getMessage());
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            handlerInfo.setExceptionStackTrace(sw.toString());
        }
        auditInfo.setHandlerInfo(handlerInfo);

        log.info("Done");

        log.info("Continuing with handler chain...");

        super.afterCompletion(request, response, handler, ex);

        log.info("Done");
    }
}
