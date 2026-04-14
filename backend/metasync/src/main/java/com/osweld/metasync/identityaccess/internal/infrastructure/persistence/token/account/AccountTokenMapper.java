package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.token.account;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.token.account.*;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;
import org.springframework.stereotype.Component;

@Component
public class AccountTokenMapper {

    public void updateJpaEntity(AccountTokenJpaEntity entity, AccountToken accountToken) {
        entity.setAccountTokenId(accountToken.getTokenId().value());
        entity.setUserId(accountToken.getUserId().value());
        entity.setTenantId(accountToken.getTenantId().value());
        entity.setTokenHash(accountToken.getTokenHash().toString());
        entity.setTokenType(accountToken.getTokenType().value());
        entity.setTokenStatus(accountToken.getAccountTokenStatus().value());
        entity.setExpiresAt(accountToken.getExpiresAt());
        entity.setCreatedAt(accountToken.getCreatedAt());
        entity.setUsedAt(accountToken.getUsedAt());
    }

    public AccountToken toDomainModel(AccountTokenJpaEntity entity){
        return AccountToken.reconstitute(
                new TokenId(entity.getAccountTokenId()),
                new UserId(entity.getUserId()),
                new TenantId(entity.getTenantId()),
                new TokenHash(entity.getTokenHash()),
                new AccountTokenType(entity.getTokenType()),
                new AccountTokenStatus(entity.getTokenStatus()),
                entity.getExpiresAt(),
                entity.getCreatedAt(),
                entity.getUsedAt()
        );
    }
}
