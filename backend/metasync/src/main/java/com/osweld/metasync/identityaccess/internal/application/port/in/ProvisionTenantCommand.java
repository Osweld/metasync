package com.osweld.metasync.identityaccess.internal.application.port.in;



import com.osweld.metasync.shared.validation.Validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProvisionTenantCommand(
    @NotBlank(message = "Tenant name cannot be blank")
    @Pattern(regexp = "^[\\p{L}0-9\\s.,&'-]+$", message = "Tenant name contains invalid characters")
    @Size(max = 100, message = "Tenant name cannot be longer than 100 characters")
    String tenantName,
    @NotBlank(message = "Plan name cannot be blank")
    String planName,
    @NotBlank(message = "Admin first name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z '-]+$", message = "Admin first name contains invalid characters")
    @Size(max = 100, message = "Admin first name cannot be longer than 100 characters")
    String ownerFirstName,
    @NotBlank(message = "Admin last name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z '-]+$", message = "Admin last name contains invalid characters")
    @Size(max = 100, message = "Admin last name cannot be longer than 100 characters")
    String ownerLastName,
    @NotBlank(message = "Owner email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Owner email must be a valid email address")
    String ownerEmail,
    @NotBlank(message = "Owner password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Owner password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
    @Size(min = 8,max = 25, message = "Owner password must be between 8 and 25 characters long")
    String ownerPassword
) {

    public ProvisionTenantCommand{
        Validator.validate(this);
    }

}
