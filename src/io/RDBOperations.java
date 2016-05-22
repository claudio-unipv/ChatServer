/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.Friendship;
import chat.RegisteredUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manage the queries to the database.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class RDBOperations {
    Connection conn;
    final static String DB_URL = "jdbc:hsqldb:file:db/chat";

    RDBOperations() throws PersistenceException {
        try {            
            conn = DriverManager.getConnection(DB_URL, "SA", "");
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

    ResultSet getFriendship(String requester, String requested) throws PersistenceException {
        String sql = "SELECT * FROM Friendships " +
                     "WHERE Requester = ? and Requested = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, requester);
            stmt.setString(2, requested);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }
    
    ResultSet getAllFriends(String user) throws PersistenceException {
        String sql = "SELECT * FROM Friendships WHERE " +
                "(Requester = ? OR Requested = ?) AND STATUS = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, user);
            stmt.setString(3, "ACCEPTED");
            return stmt.executeQuery();
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
    
    void insertFriendship(Friendship f) throws PersistenceException {
        String sql = "INSERT INTO Friendships VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, f.getRequester());
            stmt.setString(2, f.getRequested());
            stmt.setString(3, f.getStatus().toString());
            stmt.execute();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }
    
    void updateFriendship(Friendship f) throws PersistenceException {
        String sql = "UPDATE Friendships SET status=? " +
                     "WHERE requester=? AND requested=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, f.getStatus().toString());
            stmt.setString(2, f.getRequester());
            stmt.setString(3, f.getRequested());
            stmt.execute();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }
    
    /**
     * Initialize the DB.
     * @param args 
     */
    public static void main(String[] args) {
        try {            
            Connection conn = DriverManager.getConnection(DB_URL, "SA", "");
            Statement stm = conn.createStatement();
            stm.executeQuery("CREATE TABLE Users (" +
                             "Nickname VARCHAR(20) PRIMARY KEY," +
                             "Email VARCHAR(30) NOT NULL," +
                             "Password VARCHAR(15) NOT NULL" +
                             ")");
            
            stm = conn.createStatement();
            stm.executeQuery("CREATE TABLE Friendships (\n" +
                    "Requester VARCHAR(20) FOREIGN KEY REFERENCES Users(Nickname),\n" +
                    "Requested VARCHAR(20) FOREIGN KEY REFERENCES Users(Nickname),\n" +
                    "Status CHAR(10),   -- ACCEPTED, REJECTED, PENDING\n" +
                    "PRIMARY KEY (Requester, Requested)\n" +
                    ")");
            System.out.println("DB Initialized");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
