package com.osweld.metasync.identityaccess.internal.application.port.in.web.dto;

public record ProvisionTenantResponse(
    String tenantId,
    String status,
    String message
) {
}
