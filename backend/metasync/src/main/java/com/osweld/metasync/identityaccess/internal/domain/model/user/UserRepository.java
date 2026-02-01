package com.osweld.metasync.identityaccess.internal.domain.model.user;

import java.util.Optional;

public interface UserRepository {

    UserId nextIdentity();
    void save(User user);
    Optional<User> findById(UserId userId);

}
