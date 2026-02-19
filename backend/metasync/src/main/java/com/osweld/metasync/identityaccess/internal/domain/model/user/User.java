package com.osweld.metasync.identityaccess.internal.domain.model.user;

import java.time.LocalDateTime;
import java.util.Objects;

import com.osweld.metasync.identityaccess.internal.domain.model.AggregateRoot;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.event.UserAdministratorRegistered;
import com.osweld.metasync.identityaccess.internal.domain.model.user.event.UserRegistered;
import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;

import lombok.Getter;

@Getter
public class User extends AggregateRoot {

    private final UserId userId;
    private final TenantId tenantId;
    private PersonName userName;
    private EncryptedPassword encryptedPassword;
    private EmailAddress emailAddress;
    private UserStatus status;
    private UserRole role;
    private LocalDateTime createdAt;

    private User(
            UserId userId,
            TenantId tenantId,
            PersonName userName,
            EncryptedPassword encryptedPassword,
            EmailAddress emailAddress,
            UserStatus status,
            UserRole role,
            LocalDateTime createdAt) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId must not be null");
        this.userName = Objects.requireNonNull(userName, "userName must not be null");
        this.encryptedPassword = Objects.requireNonNull(encryptedPassword, "encryptedPassword must not be null");
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static User registerTenantOwner(
            UserId userId,
            TenantId tenantId,
            PersonName userName,
            String plainPassword,
            EmailAddress emailAddress,
            LocalDateTime createdAt,
            EncryptionService encryptionService) {
        UserRole tenantOwnerRole = new UserRole(Role.TENANT_OWNER);
        UserStatus initialStatus = new UserStatus(StatusType.ACTIVE);

        EncryptedPassword encryptedPassword = encryptionService.encryptPassword(plainPassword);

        User user = new User(
                userId,
                tenantId,
                userName,
                encryptedPassword,
                emailAddress,
                initialStatus,
                tenantOwnerRole,
                createdAt);

        user.registerDomainEvent(
                UserAdministratorRegistered.now(
                        userId,
                        tenantId,
                        userName,
                        emailAddress,
                        initialStatus,
                        tenantOwnerRole));
        return user;
    }

    public static User registerUser(
            UserId userId,
            TenantId tenantId,
            PersonName userName,
            String plainPassword,
            EmailAddress emailAddress,
            UserRole role,
            LocalDateTime createdAt,
            EncryptionService encryptionService) {
        UserStatus initialStatus = new UserStatus(StatusType.ACTIVE);

        EncryptedPassword encryptedPassword = encryptionService.encryptPassword(plainPassword);

        User user = new User(
                userId,
                tenantId,
                userName,
                encryptedPassword,
                emailAddress,
                initialStatus,
                role,
                createdAt);

        user.registerDomainEvent(UserRegistered.now(userId, tenantId, userName, emailAddress, initialStatus, role));

        return user;
    }

    public static User reconstitute(
            UserId userId,
            TenantId tenantId,
            PersonName userName,
            EncryptedPassword encryptedPassword,
            EmailAddress emailAddress,
            UserStatus status,
            UserRole role,
            LocalDateTime createdAt) {
        return new User(
                userId,
                tenantId,
                userName,
                encryptedPassword,
                emailAddress,
                status,
                role,
                createdAt);
    }

    public boolean isActive() {
        return this.status.equals(new UserStatus(StatusType.ACTIVE));
    }

    public boolean hasPermissionTo(Permission permission) {
        return this.role.value().grantsPermission(permission);
    }

    @Override
    public int hashCode() {
        return (151513 * 229) + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return this.userId.equals(other.userId);
    }

}
