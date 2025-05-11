package com.example.hospitalmanagmentsystem.repository;

import com.example.hospitalmanagmentsystem.util.CustomLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Database connection manager for the Hospital Information System
 */
public class DatabaseConnection {

    private static final Logger logger = CustomLogger.createLogger(DatabaseConnection.class.getName());
    // JDBC Database URL for Postgres
    private static final String JDBC_URL;

    // Database credentials
    private static final String USERNAME ;
    private static final String PASSWORD ;
    // Replace it with your actual password
    static {
        // Load database properties from a configuration file
        Properties properties = new Properties();
        try {
            properties.load(DatabaseConnection.class.getResourceAsStream("/db.properties"));
            JDBC_URL = properties.getProperty("db.url");
            USERNAME = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");
        } catch (Exception e) {
            logger.severe("Failed to load database properties: " + e.getMessage());
            throw new RuntimeException("Failed to load database properties", e);
        }
    }


    private static Connection connection = null;

    /**
     * Gets a connection to the database
     * @return Connection object
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load Postgres JDBC Driver
            Class.forName("org.postgresql.Driver");

            // Open a connection if it's not already open or is closed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                logger.info("Database connection established successfully!");
            }

            return connection;
        } catch (ClassNotFoundException e) {
            logger.severe("Postgres JDBC Driver not found: " + e.getMessage());
            throw new SQLException("JDBC Driver not found", e);
        } catch (SQLException e) {
            logger.severe("Failed to connect to database: " + e.getMessage());
            throw new SQLException("Failed to connect to database", e);
        }
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null; // Set to null to allow for garbage collection
                logger.info("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            logger.severe("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Tests the database connection
     * @return true if the connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            getConnection();
            return true;
        } catch (SQLException e) {
            logger.severe("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection();
        }
    }


}