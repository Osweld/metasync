package com.osweld.metasync.identityaccess.internal.domain.model.user.event;

import java.time.Instant;
import java.util.Objects;

import com.osweld.metasync.identityaccess.internal.domain.model.DomainEvent;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRole;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserStatus;

public record UserRegistered(
     Instant occurredOn,
    UserId userId,
    PersonName userName,
    EmailAddress emailAddress,
    UserStatus status,
    UserRole role
) implements DomainEvent {

    public UserRegistered {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(userName, "userName must not be null");
        Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(role, "role must not be null");
    }

    public static UserRegistered now(
        UserId userId,
        PersonName userName,
        EmailAddress emailAddress,
        UserStatus status,
        UserRole role
    ) {
        return new UserRegistered(
            Instant.now(),
            userId,
            userName,
            emailAddress,
            status,
            role
        );
    }

}
