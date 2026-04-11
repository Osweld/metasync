package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import org.springframework.stereotype.Component;

import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRole;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserStatus;

@Component
public class UserMapper {
    public void updateJpaEntity(UserJpaEntity entity, User user) {
        entity.setUserId(user.getUserId().value());
        entity.setFirstName(user.getUserName().firstName());
        entity.setLastName(user.getUserName().lastName());
        entity.setEncryptedPassword(user.getEncryptedPassword().value());
        entity.setEmail(user.getEmailAddress().value());
        entity.setStatus(user.getStatus().value());
        entity.setRole(user.getRole().value());
        entity.setCreatedAt(user.getCreatedAt());
    }

    public User toDomainModel(UserJpaEntity entity) {
        return User.reconstitute(
                new UserId(entity.getUserId()),
                new PersonName(entity.getFirstName(), entity.getLastName()),
                new EncryptedPassword(entity.getEncryptedPassword()),
                new EmailAddress(entity.getEmail()),
                new UserStatus(entity.getStatus()),
                new UserRole(entity.getRole()),
                entity.getCreatedAt()
        );
    }

}
