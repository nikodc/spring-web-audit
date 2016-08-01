package io.github.nikodc.springwebaudit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AroundAuditableMethodAspect {

    private static final Logger log = LoggerFactory.getLogger(AroundAuditableMethodAspect.class);

    private static final String AUDIT_INFO_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO";
    private static final String AUDIT_INFO_STATUS_ATTRIBUTE = AuditFilter.class.getName() + ".AUDIT_INFO_STATUS";
    private static final Integer AUDIT_INFO_STATUS_PENDING = 0;

    @Pointcut("@annotation(AuditableMethod)")
    public void auditableMethod(){
    }

    @Pointcut("execution(* *(..))")
    public void atExecution(){}

    @Around("auditableMethod() && atExecution()")
    public Object advice(ProceedingJoinPoint joinpoint) throws Throwable {

        log.info("Retrieving audit info attribute...");

        HttpServletRequest request = httpServletRequest();

        AuditInfo auditInfo = (AuditInfo) request.getAttribute(AUDIT_INFO_ATTRIBUTE);
        if (auditInfo == null) {
            log.error("Cannot proceed, audit info not found");
            throw new RuntimeException("Audit info not found");
        }

        if (!AUDIT_INFO_STATUS_PENDING.equals(request.getAttribute(AUDIT_INFO_STATUS_ATTRIBUTE))) {
            log.error("Cannot proceed, audit info already published");
            throw new RuntimeException("Audit info already published");
        }

        HandlerMethodInfo handlerMethodInfo = auditInfo.getHandlerMethodInfo();

        log.info("Done");

        Object returnValue = null;
        Object[] parameterValues = null;
        try {
            parameterValues = joinpoint.getArgs();
            returnValue = joinpoint.proceed();
        } finally {
            handlerMethodInfo.setReturnValue(returnValue);
            for (int i = 0; i < handlerMethodInfo.getParameters().size(); i++) {
                handlerMethodInfo.getParameters().get(i).setValue(parameterValues[i]);
            }
        }

        return returnValue;
    }

    private HttpServletRequest httpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

}
