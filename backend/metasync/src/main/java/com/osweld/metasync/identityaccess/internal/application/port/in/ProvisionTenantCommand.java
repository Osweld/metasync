package com.osweld.metasync.identityaccess.internal.application.port.in;



import com.osweld.metasync.shared.validation.Validator;

import jakarta.validation.constraints.NotBlank;

public record ProvisionTenantCommand(
    @NotBlank
    String tenantName,
    @NotBlank
    String planName
) {

    public ProvisionTenantCommand{
        Validator.validate(this);
    }

}
