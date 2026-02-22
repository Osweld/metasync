package com.osweld.metasync.identityaccess.internal.application.usecase;

import com.osweld.metasync.identityaccess.internal.application.port.in.ProvisionTenantCommand;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.Tenant;

public interface ProvisionTenantUseCase {

    Tenant provisionTenant(ProvisionTenantCommand command);

}
