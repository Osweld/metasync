package com.osweld.metasync.identityaccess.infrastructure.persistence.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.osweld.metasync.identityaccess.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.domain.model.user.User;
import com.osweld.metasync.identityaccess.domain.model.user.UserId;

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
    public User createUser(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        userMapper.updateJpaEntity(entity, user);
        return userMapper.toDomainModel(userJpaRepository.save(entity));
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
