package com.osweld.metasync.identityaccess.infrastructure.in.web.dto;

public record ProvisionTenantResponse(
    String tenantId,
    String status,
    String message
) {
}
