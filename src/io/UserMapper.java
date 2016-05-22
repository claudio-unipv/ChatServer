/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.RegisteredUser;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manage the storage of registered users.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public abstract class UserMapper {
    
    LinkedHashMap<String, RegisteredUser> cachedUsers;
    
    UserMapper(final int cacheSize) {
        /* The cache is represented by a LinkedHashMap, a collection that allows
         * to specificy if ans when old elements must be deleted. */
        cachedUsers = new LinkedHashMap<String, RegisteredUser>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > cacheSize;
            }
        };
    }
    
    synchronized RegisteredUser get(String username) throws PersistenceException {
        RegisteredUser u = cachedUsers.get(username);
        if (u == null) {
            u = getUserFromStorage(username);
            if (u != null)
                cachedUsers.put(username, u);
        }
        return u;
    }
    
    synchronized void put(RegisteredUser user) throws PersistenceException {
        cachedUsers.put(user.getNickname(), user);
        putUserToStorage(user);
    }
    
    protected abstract RegisteredUser getUserFromStorage(String username) throws PersistenceException;
    protected abstract void putUserToStorage(RegisteredUser u) throws PersistenceException;
}
