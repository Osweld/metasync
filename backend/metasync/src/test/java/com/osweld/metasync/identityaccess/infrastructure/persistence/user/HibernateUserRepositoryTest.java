package com.osweld.metasync.identityaccess.infrastructure.persistence.user;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.osweld.metasync.identityaccess.domain.model.tenant.Tenant;
import com.osweld.metasync.identityaccess.domain.model.user.User;
import com.osweld.metasync.identityaccess.infrastructure.persistence.schema.LiquibaseSchemaAdapter;
import com.osweld.metasync.identityaccess.infrastructure.persistence.tenant.HibernateTenantRepository;
import com.osweld.metasync.identityaccess.infrastructure.persistence.tenant.TenantMapper;
import com.osweld.metasync.identityaccess.infrastructure.persistence.tenant.TenantMother;
import com.osweld.metasync.shared.infrastructure.AbstractIntegrationTest;
import com.osweld.metasync.shared.multitenancy.context.AppTenantContext;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        HibernateUserRepository.class,
        HibernateTenantRepository.class,
        LiquibaseSchemaAdapter.class,
        UserMapper.class,
        TenantMapper.class })
public class HibernateUserRepositoryTest extends AbstractIntegrationTest {

    private final static String SQL_CHECK_TABLE_EXISTS = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = 'users'";

    @Autowired
    private HibernateUserRepository userRepository;

    @Autowired
    private HibernateTenantRepository tenantRepository;

    @Autowired
    private LiquibaseSchemaAdapter schemaProvisioner;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    @DisplayName("Should save and retrieve a user in a specific tenant schema")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testSaveAndFindUser() {
        Tenant tenant = TenantMother.Random();

        transactionTemplate.execute(status -> {
            tenantRepository.createTenant(tenant);
            return null;
        });

        schemaProvisioner.ensureSchemaExists(tenant.getSchemaName());

        Integer count = jdbcTemplate.queryForObject(
                SQL_CHECK_TABLE_EXISTS,
                Integer.class,
                tenant.getSchemaName().value());
        assertThat(count).isEqualTo(1);

        User user = UserMother.Random(tenant.getTenantId());

        try {
            AppTenantContext.setCurrentTenant(tenant.getSchemaName().value());

            transactionTemplate.execute(status -> {
                userRepository.createUser(user);
                entityManager.flush();
                entityManager.clear();
                return null;
            });

            transactionTemplate.execute(status -> {
                Optional<User> retrievedUser = userRepository.findById(user.getUserId());

                assertThat(retrievedUser).isPresent();
                assertThat(retrievedUser.get().getUserId()).isEqualTo(user.getUserId());
                return null;
            });

        } finally {
            AppTenantContext.clear();
        }
    }

}
