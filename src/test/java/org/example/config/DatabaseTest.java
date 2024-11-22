package org.example.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = null;
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Database.closeConnection(connection);
        }
    }

    @Test
    void getConnection() {
        try {
            connection = Database.getConnection();
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Exception thrown during getConnection: " + e.getMessage());
        }
    }

    @Test
    void closeConnection() {
        try {
            connection = Database.getConnection();
            Database.closeConnection(connection);
            assertTrue(connection.isClosed(), "Connection should be closed after calling closeConnection");
        } catch (SQLException e) {
            fail("Exception thrown during closeConnection: " + e.getMessage());
        }
    }
}
