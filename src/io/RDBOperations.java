/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.RegisteredUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the queries to the database.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class RDBOperations {
    Connection conn;

    RDBOperations() throws PersistenceException {
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:file:db/chat", "SA", "");
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    ResultSet getUser(String username) throws PersistenceException {
        String sql = "SELECT * FROM Users where Nickname = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    void updateUser(RegisteredUser u) throws PersistenceException {
        String sql = "UPDATE Users SET Email=?,Password=? WHERE Nickname=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getEmail());
            stmt.setString(2, new String(u.getPassword()));
            stmt.setString(3, u.getNickname());
            stmt.execute();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    void insertUser(RegisteredUser u) throws PersistenceException {
        String sql = "INSERT INTO Users VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getNickname());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, new String(u.getPassword()));
            stmt.execute();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }
}
