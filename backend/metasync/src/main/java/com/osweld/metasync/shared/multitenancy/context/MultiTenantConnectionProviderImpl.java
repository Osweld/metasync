package com.osweld.metasync.shared.multitenancy.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String>{

    private final DataSource dataSource;


    @Override
    protected DataSource selectAnyDataSource() {
       return dataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
      
        String tenandId =  (tenantIdentifier != null) ? tenantIdentifier : AppTenantContext.DEFAULT_TENANT_ID;
        log.debug("Getting connection for tenant: {}", tenandId);

        Connection connection = getAnyConnection();
        try(Statement stmt = connection.createStatement()){
           
            stmt.execute("SET search_path TO " + tenandId);
        }catch(SQLException e){
            log.error("Error setting search_path to tenant: {}", tenandId, e);
            throw e;
        }

        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try(Statement stmt = connection.createStatement()){
            stmt.execute("SET search_path TO " + AppTenantContext.DEFAULT_TENANT_ID);
        }catch(SQLException e){
           log.warn("Could not reset search_path to public: {}", e.getMessage());
        }finally{
            releaseAnyConnection(connection);
        }
    }

    

    

}
