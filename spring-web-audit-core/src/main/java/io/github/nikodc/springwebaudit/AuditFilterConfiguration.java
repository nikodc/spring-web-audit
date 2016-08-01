package io.github.nikodc.springwebaudit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;

@Configuration
public class AuditFilterConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditInfoListener.class)
    public AuditInfoListener defaultAuditInfoListener() {
        return new DefaultAuditInfoListener();
    }

    @Bean
    public AuditFilter auditFilter(AuditInfoListener auditInfoListener) {
        return new AuditFilter(auditInfoListener);
    }

    @Bean
    public FilterRegistrationBean auditFilterRegistrationBean(AuditFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setName("auditFilter");
        registration.setDispatcherTypes(REQUEST, ERROR);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

}
