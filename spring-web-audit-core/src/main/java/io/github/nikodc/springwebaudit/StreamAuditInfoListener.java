package io.github.nikodc.springwebaudit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

public class StreamAuditInfoListener implements AuditInfoListener {

    private static final Logger log = LoggerFactory.getLogger(StreamAuditInfoListener.class);

    private OutputStream outputStream;

    private ObjectMapper objectMapper;

    public StreamAuditInfoListener(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void onNewAuditInfo(AuditInfo auditInfo) {
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, auditInfo);
        } catch (Exception e) {
            log.error("Error processing audit info", e);
        }
    }

}
