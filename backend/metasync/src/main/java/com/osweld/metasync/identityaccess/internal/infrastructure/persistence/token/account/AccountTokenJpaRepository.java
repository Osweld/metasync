package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.token.account;

import com.osweld.metasync.identityaccess.internal.domain.model.token.account.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountTokenJpaRepository extends JpaRepository<AccountTokenJpaEntity, UUID> {

     boolean existsByTokenHash(String tokenHash);
     Optional<AccountTokenJpaEntity> findByTokenHash(String tokenHash);
}
