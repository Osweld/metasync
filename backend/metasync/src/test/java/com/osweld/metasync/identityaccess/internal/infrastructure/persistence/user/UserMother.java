package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import java.time.LocalDateTime;

import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.Role;
import com.osweld.metasync.identityaccess.internal.domain.model.user.StatusType;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRole;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserStatus;


public class UserMother{

    public static User Random(TenantId tenantId) {
        return createUser("test" + java.util.UUID.randomUUID().toString().substring(0, 5) + "@example.com", tenantId);
    }

    public static User createUser(String email, TenantId tenantId) {
        UserId userId = UserId.generate();
        PersonName userName = new PersonName("John","Doe");
        EmailAddress emailAddress = new EmailAddress(email);
        EncryptedPassword encryptedPassword = new EncryptedPassword("encryptedPasswordencryptedPasswordencryptedPasswordencryptedPassword");
        UserStatus status = new UserStatus(StatusType.ACTIVE);
        UserRole role = new UserRole(Role.TENANT_OWNER);
        LocalDateTime createdAt = LocalDateTime.now();
        return User.reconstitute(userId, userName, encryptedPassword, emailAddress, status, role, createdAt);

    }

}
