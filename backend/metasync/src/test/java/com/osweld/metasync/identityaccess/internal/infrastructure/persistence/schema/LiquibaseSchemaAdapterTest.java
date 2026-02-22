package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.schema;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;
import com.osweld.metasync.shared.infrastructure.AbstractIntegrationTest;

@SpringBootTest
public class LiquibaseSchemaAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private LiquibaseSchemaAdapter schemaAdapter;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Should create a new schema and run Liquibase migrations successfully")
    void shouldCreateSchemaAndRunMigrations() throws SQLException {
        SchemaName schemaName = new SchemaName("t_test_shared_" + System.currentTimeMillis());
        schemaAdapter.ensureSchemaExists(schemaName);

        try (
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement()) {
            ResultSet rsSchema = stmt.executeQuery(
                    String.format("SELECT schema_name FROM information_schema.schemata WHERE schema_name = '%s'",
                            schemaName.value()));

            assertThat(rsSchema.next()).isTrue();
            assertThat(rsSchema.getString("schema_name")).isEqualTo(schemaName.value());
        }

        ResultSet rsChangelog = dataSource.getConnection().createStatement().executeQuery(
                String.format(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' AND table_name = 'databasechangelog'",
                        schemaName.value()));

        assertThat(rsChangelog.next()).isTrue();
        assertThat(rsChangelog.getString("table_name")).isEqualTo("databasechangelog");

        ResultSet rsUsers = dataSource.getConnection().createStatement().executeQuery(
                String.format(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' AND table_name = 'users'",
                        schemaName.value()));
        assertThat(rsUsers.next()).isTrue();
        assertThat(rsUsers.getString("table_name")).isEqualTo("users");
    }

    @Test
    @DisplayName("Should drop existing schema and recreate it with migrations")
    void shouldDropAndRecreateSchemaWithMigrations() throws SQLException {
        SchemaName schemaName = new SchemaName("t_test_shared_" + System.currentTimeMillis());
        schemaAdapter.ensureSchemaExists(schemaName);
        schemaAdapter.dropSchema(schemaName);
        schemaAdapter.ensureSchemaExists(schemaName);

        try (
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement()) {
            ResultSet rsSchema = stmt.executeQuery(
                    String.format("SELECT schema_name FROM information_schema.schemata WHERE schema_name = '%s'",
                            schemaName.value()));

            assertThat(rsSchema.next()).isTrue();
            assertThat(rsSchema.getString("schema_name")).isEqualTo(schemaName.value());
        }

        ResultSet rsChangelog = dataSource.getConnection().createStatement().executeQuery(
                String.format(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' AND table_name = 'databasechangelog'",
                        schemaName.value()));

        assertThat(rsChangelog.next()).isTrue();
        assertThat(rsChangelog.getString("table_name")).isEqualTo("databasechangelog");

        ResultSet rsUsers = dataSource.getConnection().createStatement().executeQuery(
                String.format(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' AND table_name = 'users'",
                        schemaName.value()));
        assertThat(rsUsers.next()).isTrue();
        assertThat(rsUsers.getString("table_name")).isEqualTo("users");
    }
}
