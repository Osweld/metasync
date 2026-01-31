package com.osweld.metasync.identityaccess.internal.domain.model.user.event;

import java.time.Instant;
import java.util.Objects;

import com.osweld.metasync.identityaccess.internal.domain.model.DomainEvent;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRole;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserStatus;

public record UserAdministratorRegistered(
    Instant occurredOn,
    UserId userId,
    TenantId tenantId,
    PersonName userName,
    EmailAddress emailAddress,
    UserStatus status,
    UserRole role
) implements DomainEvent {

    public UserAdministratorRegistered {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(userName, "userName must not be null");
        Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(role, "role must not be null");
    }

    public static UserAdministratorRegistered now(
        UserId userId,
        TenantId tenantId,
        PersonName userName,
        EmailAddress emailAddress,
        UserStatus status,
        UserRole role
    ) {
        return new UserAdministratorRegistered(
            Instant.now(),
            userId,
            tenantId,
            userName,
            emailAddress,
            status,
            role
        );
    }
}
