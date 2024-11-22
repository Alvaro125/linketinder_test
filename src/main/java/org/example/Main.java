package org.example;

import org.example.config.Database;
import org.example.server.Server;
import org.example.ui.Terminal;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database.getConnection();
        Terminal app = new Terminal();
        app.run();
    }
}