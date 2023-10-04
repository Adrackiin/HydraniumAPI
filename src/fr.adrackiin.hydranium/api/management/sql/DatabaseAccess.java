package fr.adrackiin.hydranium.api.management.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {

    private final DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    DatabaseAccess(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    public void initPool() {
        setupHikariCP();
    }

    public void closePool() {
        if(hikariDataSource != null){
            hikariDataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        if(this.hikariDataSource == null) {
            setupHikariCP();
        }
        return hikariDataSource.getConnection();
    }

    private void setupHikariCP() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setJdbcUrl(credentials.toURL());
        hikariConfig.setUsername(credentials.getUser());
        hikariConfig.setPassword(credentials.getPassword());
        hikariConfig.setMaxLifetime(600_000L);
        hikariConfig.setIdleTimeout(300_000L);
        hikariConfig.setLeakDetectionThreshold(300_000L);
        hikariConfig.setConnectionTimeout(10_000L);
        hikariDataSource = new HikariDataSource(hikariConfig);
    }
}
