package io.github.nikodc.springwebaudit;

public interface AuditInfoListener {

    /**
     * Method executed when a new audit info object is available for processing.
     * @param auditInfo
     */
    void onNewAuditInfo(AuditInfo auditInfo);

}
