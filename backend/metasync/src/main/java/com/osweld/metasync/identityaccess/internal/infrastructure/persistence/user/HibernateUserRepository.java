package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.user.User;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    @Override
    public UserId nextIdentity() {
        return UserId.generate();
    }
    @Override
    public void save(User user) {
        UserJpaEntity entity = userMapper.toJpaEntity(user);
        userJpaRepository.save(entity);
    }
    @Override
    public Optional<User> findById(UserId userId) {
        Optional<UserJpaEntity> entity = userJpaRepository.findById(userId.value());
        return entity.map(userMapper::toDomainModel);
    }
    @Override
    public boolean existsByEmail(EmailAddress emailAddress) {
        return userJpaRepository.existsByEmail(emailAddress.value());
    }

}
