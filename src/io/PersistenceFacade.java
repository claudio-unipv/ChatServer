/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.RegisteredUser;

/**
 * Public interface for the persistence subsystem.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class PersistenceFacade {
    static PersistenceFacade instance;
    UserMapper userMapper;
    
    /**
     * Provide access to the Facade.
     * @return the unique instance of the Facade.
     */
    public synchronized PersistenceFacade getInstance() throws PersistenceException {
        if (instance == null)
            instance = new PersistenceFacade();
        return instance;
    }
    
    // private to enforce the singleton pattern.
    private PersistenceFacade() throws PersistenceException {
        RDBOperations rdbop = new RDBOperations();
        userMapper = new RDBUserMapper(rdbop, 10);
    }
    
    /**
     * Load the user with the given username.
     * @param username name of the user to lookup
     * @return the requested user object or null if he does not exist
     */
    public RegisteredUser getUser(String username) throws PersistenceException {
        return userMapper.get(username);
    }
    
    /**
     * Update the information about a user.
     * @param user user information to be stored.
     */
    public void putUser(RegisteredUser user) throws PersistenceException {
        userMapper.put(user);
    }
}
