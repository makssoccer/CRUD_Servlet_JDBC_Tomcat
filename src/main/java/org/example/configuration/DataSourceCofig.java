package org.example.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.example.configuration.DatabaseConfig.*;

public class DataSourceCofig {
    private static HikariDataSource dataSource;
    private static HikariConfig config = new HikariConfig();

    static {
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER);
        config.setJdbcUrl(URL);
//        config.setMaximumPoolSize(20);
//        config.setConnectionTimeout(300000);
//        config.setLeakDetectionThreshold(300000);
        dataSource = new HikariDataSource(config);
    }

    private DataSourceCofig(){
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

}