package com.osweld.metasync.identityaccess.application.usecase;

import com.osweld.metasync.identityaccess.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;

public interface ProvisionTenantUseCase {

    Tenant provisionTenant(ProvisionTenantCommand command);

}
