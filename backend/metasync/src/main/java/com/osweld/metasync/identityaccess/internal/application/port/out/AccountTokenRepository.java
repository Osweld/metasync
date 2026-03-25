package com.osweld.metasync.identityaccess.internal.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.osweld.metasync.identityaccess.internal.domain.model.token.account.AccountToken;
import com.osweld.metasync.identityaccess.internal.domain.model.token.account.TokenId;

public interface AccountTokenRepository {

    TokenId nextIdentity();
    void save(AccountToken accountToken);
    Optional<AccountToken> findById(TokenId tokenId);
    void updateUsedAt(TokenId tokenId, LocalDateTime usedAt);
}
