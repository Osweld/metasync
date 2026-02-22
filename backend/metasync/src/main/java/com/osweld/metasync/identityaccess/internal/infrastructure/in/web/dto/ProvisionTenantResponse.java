package com.osweld.metasync.identityaccess.internal.infrastructure.in.web.dto;

public record ProvisionTenantResponse(
    String tenantId,
    String status,
    String message
) {
}
