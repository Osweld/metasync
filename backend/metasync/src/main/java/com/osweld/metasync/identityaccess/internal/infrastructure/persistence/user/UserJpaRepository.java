package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByEmail(String email);

}
