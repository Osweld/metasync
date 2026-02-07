package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import org.springframework.stereotype.Component;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.EncryptedPassword;
import com.osweld.metasync.identityaccess.internal.domain.model.user.PersonName;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserRole;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserStatus;

@Component
public class UserMapper {

    public UserJpaEntity toJpaEntity(User user) {
        return new UserJpaEntity(
                user.getUserId().value(),
                user.getTenantId().value(),
                user.getUserName().firstName(),
                user.getUserName().lastName(),
                user.getEncryptedPassword().value(),
                user.getEmailAddress().value(),
                user.getStatus().value(),
                user.getRole().value(),
                user.getCreatedAt()
        );
    }

    public User toDomainModel(UserJpaEntity entity) {
        return User.reconstitute(
                new UserId(entity.getUserId()),
                new TenantId(entity.getTenantId()),
                new PersonName(entity.getFirstName(), entity.getLastName()),
                new EncryptedPassword(entity.getEncryptedPassword()),
                new EmailAddress(entity.getEmail()),
                new UserStatus(entity.getStatus()),
                new UserRole(entity.getRole()),
                entity.getCreatedAt()
        );
    }

}
