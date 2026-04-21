package com.osweld.metasync.identityaccess.infrastructure.in.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.osweld.metasync.identityaccess.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.application.usecase.ProvisionTenantUseCase;
import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.infrastructure.in.web.dto.ProvisionTenantRequest;
import com.osweld.metasync.identityaccess.infrastructure.in.web.dto.ProvisionTenantResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantProvisioningController {

    private final static String PROVISION_SUCCESS_MESSAGE = "Tenant provisioned successfully";

    private final ProvisionTenantUseCase provisionTenantUseCase;

    @PostMapping("/register")
    public ResponseEntity<ProvisionTenantResponse> provisionTenant(@RequestBody @Valid ProvisionTenantRequest request) {

        ProvisionTenantCommand command = new ProvisionTenantCommand(
                request.tenantName(),
                request.planName(),
                request.ownerFirstName(),
                request.ownerLastName(),
                request.ownerEmail(),
                request.ownerPassword());

        Tenant tenant = provisionTenantUseCase.provisionTenant(command);

        ProvisionTenantResponse response = new ProvisionTenantResponse(
                tenant.getTenantId().value().toString(),
                tenant.getStatus().value().toString(),
                PROVISION_SUCCESS_MESSAGE);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tenant.getTenantId().value())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

}
