package com.osweld.metasync.identityaccess.internal.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.identityaccess.internal.domain.model.DomainEvent;
import com.osweld.metasync.identityaccess.internal.domain.model.shared.EmailAddress;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.event.UserAdministratorRegistered;
import com.osweld.metasync.identityaccess.internal.domain.model.user.event.UserRegistered;
import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;

public class UserTest {

        private final UserId userId = new UserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        private final PersonName userName = new PersonName("John", "Doe");
        private final EmailAddress userEmail = new EmailAddress("johndoe@example.com");
        private final UserStatus userStatus = new UserStatus(StatusType.PENDING);
        private final String plainPassword = "SecureP@ssw0rd!";
        private final EncryptedPassword encryptedPassword = new EncryptedPassword(
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        private final UserRole userRole = new UserRole(Role.MEMBER);
        private final LocalDateTime now = LocalDateTime.now();


        @Test
        @DisplayName("should register tenant owner successfully")
        void shouldRegisterTenantOwnerSuccessfully() {

                EncryptionService encryptionMockService = mock(EncryptionService.class);

                when(encryptionMockService.encryptPassword(plainPassword))
                                .thenReturn(encryptedPassword);

                User user = User.registerTenantOwner(userId, userName, plainPassword, userEmail, now,
                                encryptionMockService);

                assertThat(user).isNotNull();

                List<DomainEvent> events = user.pullDomainEvents();

                assertThat(events).hasSize(1);
                assertThat(events.get(0)).isInstanceOf(UserAdministratorRegistered.class);
                UserAdministratorRegistered event = (UserAdministratorRegistered) events.get(0);

                assertThat(event.userId()).isEqualTo(userId);
                assertThat(event.userName()).isEqualTo(userName);
                assertThat(event.emailAddress()).isEqualTo(userEmail);
                assertThat(event.role()).isEqualTo(new UserRole(Role.TENANT_OWNER));
                assertThat(event.status()).isEqualTo(new UserStatus(StatusType.PENDING));
                assertThat(event.occurredOn()).isNotNull();

                verify(encryptionMockService).encryptPassword(plainPassword);

        }

        @Test
        @DisplayName("should register user successfully")
        void shouldRegisterUserSuccessfully() {

                EncryptionService encryptionMockService = mock(EncryptionService.class);

                when(encryptionMockService.encryptPassword(plainPassword))
                                .thenReturn(encryptedPassword);

                User user = User.registerUser(userId, userName, plainPassword, userEmail, userRole, now,
                                encryptionMockService);

                assertThat(user).isNotNull();

                List<DomainEvent> events = user.pullDomainEvents();
                assertThat(events).hasSize(1);
                assertThat(events.get(0)).isInstanceOf(UserRegistered.class);
                UserRegistered event = (UserRegistered) events.get(0);

                assertThat(event.userId()).isEqualTo(userId);
                assertThat(event.userName()).isEqualTo(userName);
                assertThat(event.emailAddress()).isEqualTo(userEmail);
                assertThat(event.role()).isEqualTo(userRole);
                assertThat(event.status()).isEqualTo(userStatus);
                assertThat(event.occurredOn()).isNotNull();

                verify(encryptionMockService).encryptPassword(plainPassword);

        }

        @Test
        @DisplayName("should reconstitute user successfully")
        void shouldReconstituteUserSuccessfully() {
                User user = User.reconstitute(
                                userId,
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                assertThat(user).isNotNull();
                assertThat(user.pullDomainEvents()).isEmpty();
        }

        @Test
        @DisplayName("should verify user has permission")
        void shouldVerifyUserHasPermission() {
                User user = User.reconstitute(
                                userId,
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                assertThat(user.hasPermissionTo(Permission.INVENTORY_WRITE)).isTrue();
                assertThat(user.hasPermissionTo(Permission.INVENTORY_DELETE)).isFalse();
        }

        @Test
        @DisplayName("Should consider two Users with the same identifier as equal")
        void shouldConsiderTwoUsersWithSameIdentifierAsEqual() {
                User user1 = User.reconstitute(
                                userId,
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                User user2 = User.reconstitute(
                                userId,
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                assertThat(user1).isEqualTo(user2);
        }

        @Test
        @DisplayName("Should consider two Users with different identifiers as unequal")
        void shouldConsiderTwoUsersWithDifferentIdentifiersAsUnequal() {
                User user1 = User.reconstitute(
                                userId,
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                User user2 = User.reconstitute(
                                new UserId(UUID.fromString("223e4567-e89b-12d3-a456-426614174111")),
                                userName,
                                encryptedPassword,
                                userEmail,
                                userStatus,
                                userRole,
                                now);

                assertThat(user1).isNotEqualTo(user2);
        }
}