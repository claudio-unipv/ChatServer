/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.Friendship;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * IO for friendship using a relational database.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
class RDBFriendshipMapper implements FriendshipMapper {

    RDBOperations rdbops;

    // Constructor
    RDBFriendshipMapper(RDBOperations rdbops) {
        this.rdbops = rdbops;
    }
    
    @Override
    public void put(Friendship f) throws PersistenceException {
        if (get(f.getRequester(), f.getRequested()) == null)
            rdbops.insertFriendship(f);
        else
            rdbops.updateFriendship(f);
    }

    @Override
    public Friendship get(String requester, String recipient) throws PersistenceException {
        try {
            ResultSet rs = rdbops.getFriendship(requester, recipient);
            if (!rs.next())
                return null;
            Friendship f = new  Friendship(rs.getString("Requester"),
                                           rs.getString("Requested"));
            String ss = rs.getString("Status").trim();
            f.setStatus(Friendship.Status.valueOf(ss));
            return f;
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public List<String> getFriends(String username) throws PersistenceException {
        ResultSet rs = rdbops.getAllFriends(username);
        ArrayList<String> ret = new ArrayList<>();
        try {
            while (rs.next()) {
                String u = rs.getString("Requester");
                if (u.equals(username))
                    u = rs.getString("Requested");
                ret.add(u);
            }
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return ret;
    }    

    @Override
    public List<String> getPending(String user) throws PersistenceException {
        ResultSet rs = rdbops.getPendingRequests(user);
        ArrayList<String> ret = new ArrayList<>();
        try {
            while (rs.next())
                ret.add(rs.getString("Requester"));
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return ret;
    }        
}
