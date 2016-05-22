/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.RegisteredUser;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Use a Relational Data Base to manage the storage of the users.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class RDBUserMapper extends UserMapper {
    
    RDBOperations rdbop;
    
    RDBUserMapper(RDBOperations rdbop, int cacheSize) {
        super(cacheSize);
        this.rdbop = rdbop;
    }

    @Override
    protected RegisteredUser getUserFromStorage(String username) throws PersistenceException {        
        String email;
        char[] pwd;
        try {
            ResultSet rs = rdbop.getUser(username);
            if (!rs.next())
                return null;
            email = rs.getString("Email");        
            pwd = rs.getString("Password").toCharArray();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return new RegisteredUser(username, email, pwd);
    }

    @Override
    protected void putUserToStorage(RegisteredUser u) throws PersistenceException {
        if (getUserFromStorage(u.getNickname()) == null)
            rdbop.insertUser(u);
        else
            rdbop.updateUser(u);
    }
}
