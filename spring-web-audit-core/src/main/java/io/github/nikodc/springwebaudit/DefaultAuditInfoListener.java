package io.github.nikodc.springwebaudit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAuditInfoListener implements AuditInfoListener {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuditInfoListener.class);

    private ObjectMapper objectMapper;

    public DefaultAuditInfoListener() {
        this.objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void onNewAuditInfo(AuditInfo auditInfo) {
        try {
            log.info("auditInfo: \n{}", objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(auditInfo));
        } catch (Exception e) {
            log.error("Error processing audit info", e);
        }
    }

}
