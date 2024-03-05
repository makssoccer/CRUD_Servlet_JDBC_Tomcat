package org.example.configuration;

import lombok.Data;
@Data
public class DatabaseConfig {
    public static final String URL = "jdbc:postgresql://localhost:5432/test";
    public static final String USER = "maks";
    public static final String PASSWORD = "123";
    public static final String DRIVER = "org.postgresql.Driver";
}