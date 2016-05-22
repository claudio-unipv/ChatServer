/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.UserData;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manage the storage of registered users.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public abstract class UserMapper {
    
    LinkedHashMap<String, UserData> cachedUsers;
    
    UserMapper(final int cacheSize) {
        /* The cache is represented by a LinkedHashMap, a collection that allows
         * to specificy if ans when old elements must be deleted. */
        cachedUsers = new LinkedHashMap<String, UserData>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > cacheSize;
            }
        };
    }
    
    synchronized UserData get(String username) throws PersistenceException {
        UserData u = cachedUsers.get(username);
        if (u == null) {
            u = getUserFromStorage(username);
            if (u != null)
                cachedUsers.put(username, u);
        }
        return u;
    }
    
    synchronized void put(UserData user) throws PersistenceException {
        cachedUsers.put(user.getNickname(), user);
        putUserToStorage(user);
    }
    
    protected abstract UserData getUserFromStorage(String username) throws PersistenceException;
    protected abstract void putUserToStorage(UserData u) throws PersistenceException;
}
