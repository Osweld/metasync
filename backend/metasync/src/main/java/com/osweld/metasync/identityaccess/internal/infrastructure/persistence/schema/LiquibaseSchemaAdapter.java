package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.osweld.metasync.identityaccess.internal.application.port.out.SchemaProvisionerPort;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.SchemaName;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LiquibaseSchemaAdapter implements SchemaProvisionerPort{

    private final static String CREATE_SCHEMA_TEMPLATE = "CREATE SCHEMA IF NOT EXISTS \"%s\"";
    private final static String LIQUIBASE_CHANGELOG_PATH = "db/changelog/tenants/db.changelog-master.yaml";

    private final DataSource dataSource;

    @Override
    public void ensureSchemaExists(SchemaName schemaName) {


        String schema = schemaName.value();

        try(Connection connection = dataSource.getConnection()){

            createPhysicalSchema(connection, schema);
            runLiquibaseMigrations(connection, schema);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create schema: " + schema, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to run migrations for schema: " + schema, e);

        }
    }

    private void createPhysicalSchema(Connection connection, String schemaName) throws SQLException {
       try(Statement stmt = connection.createStatement()){
        stmt.execute(String.format(CREATE_SCHEMA_TEMPLATE, schemaName));
       }
    }

    private void runLiquibaseMigrations(Connection connection, String schemaName) throws Exception {
        Database database = DatabaseFactory.getInstance()
        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        database.setDefaultSchemaName(schemaName);
        database.setLiquibaseSchemaName(schemaName);

        try(Statement stmt = connection.createStatement()){
            stmt.execute(String.format("SET search_path TO \"%s\", public", schemaName));
        }

        try(Liquibase liquibase = new Liquibase(
            LIQUIBASE_CHANGELOG_PATH,
            new ClassLoaderResourceAccessor(),
            database
        )) {
            liquibase.update(new Contexts(),new LabelExpression());
        }
       
    }

}
