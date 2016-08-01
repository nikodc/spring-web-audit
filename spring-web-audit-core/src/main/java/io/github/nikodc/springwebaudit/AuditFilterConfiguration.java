package io.github.nikodc.springwebaudit;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import static javax.servlet.DispatcherType.*;

@Configuration
public class AuditFilterConfiguration {

    @Bean
    public AuditFilter auditFilter() {
        return new AuditFilter();
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
