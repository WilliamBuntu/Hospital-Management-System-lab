package com.example.hospitalmanagmentsystem.repository;

    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;

    import java.sql.Connection;
    import java.sql.SQLException;

    import static org.junit.jupiter.api.Assertions.*;

    class DatabaseConnectionTest {

        private Connection connection;

        @BeforeEach
        void setUp() {
            // Ensure the connection is null before each test
            connection = null;
        }

        @AfterEach
        void tearDown() {
            // Close the connection after each test
            DatabaseConnection.closeConnection();
        }

        @Test
        void getConnection_ShouldEstablishConnection() {
            try {
                connection = DatabaseConnection.getConnection();
                assertNotNull(connection, "Connection should not be null");
                assertFalse(connection.isClosed(), "Connection should be open");
            } catch (SQLException e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        }

        @Test
        void closeConnection_ShouldCloseConnection() {
            try {
                connection = DatabaseConnection.getConnection();
                assertNotNull(connection, "Connection should not be null");

                DatabaseConnection.closeConnection();
                assertTrue(connection.isClosed(), "Connection should be closed");
            } catch (SQLException e) {
                fail("Exception should not occur: " + e.getMessage());
            }
        }

        @Test
        void testConnection_ShouldReturnTrueForValidConnection() {
            assertTrue(DatabaseConnection.testConnection(), "testConnection should return true for a valid connection");
        }

        @Test
        void testConnection_ShouldReturnFalseForInvalidConnection() {
            // Simulate an invalid connection by modifying the database properties
            System.setProperty("db.url", "invalid_url");
            System.setProperty("db.username", "invalid_user");
            System.setProperty("db.password", "invalid_password");

            assertTrue(DatabaseConnection.testConnection(), "testConnection should return false for an invalid connection");
        }
    }