package com.osweld.metasync.identityaccess.internal.application.port.out;

import java.util.Optional;

import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;

public interface UserRepository {

    UserId nextIdentity();
    User createUser(User user);
    Optional<User> findById(UserId userId);
    boolean existsByEmail(EmailAddress emailAddress);

}
