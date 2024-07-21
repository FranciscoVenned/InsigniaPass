package com.venned.insigniapass.abstracts;

import com.venned.insigniapass.interfaces.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDatabase implements Database {

    protected Connection connection;
    protected String url;


    public AbstractDatabase(String url) {
        this.url = url;
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String createChestsTable = "CREATE TABLE IF NOT EXISTS locks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "password INTEGER NOT NULL," +
                "owner TEXT NOT NULL," +
                "location TEXT NOT NULL" +
                ");";

        try (PreparedStatement stmt1 = connection.prepareStatement(createChestsTable)) {
            stmt1.execute();
        }
    }



}
