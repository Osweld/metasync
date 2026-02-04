package com.osweld.metasync.identityaccess.internal.domain.model.tenant;

import com.osweld.metasync.identityaccess.internal.domain.model.user.User;

public record TenantCreationResult(
    Tenant tenant,
    User ownerUser
) {

}
