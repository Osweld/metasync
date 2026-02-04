package com.osweld.metasync.identityaccess.internal.application.usecase;

import com.osweld.metasync.identityaccess.internal.application.port.in.ProvisionTenantCommand;

public interface ProvisionTenantUseCase {

    void provisionTenant(ProvisionTenantCommand command);

}
