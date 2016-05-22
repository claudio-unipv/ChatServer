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
import java.util.ArrayList;
import java.util.List;

/**
 * Public interface for the persistence subsystem.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class PersistenceFacade {
    static PersistenceFacade instance;
    UserMapper userMapper;
    FriendshipMapper friendshipMapper;
    
    /**
     * Provide access to the Facade.
     * @return the unique instance of the Facade.
     * @throws io.PersistenceException
     */
    public static synchronized PersistenceFacade getInstance() throws PersistenceException {
        if (instance == null)
            instance = new PersistenceFacade();
        return instance;
    }
    
    // private to enforce the singleton pattern.
    private PersistenceFacade() throws PersistenceException {
        RDBOperations rdbop = new RDBOperations();
        userMapper = new RDBUserMapper(rdbop, 10);
        friendshipMapper = new RDBFriendshipMapper(rdbop);
    }
    
    /**
     * Load the user with the given username.
     * @param username name of the user to lookup
     * @return the requested user object or null if he does not exist
     * @throws io.PersistenceException
     */
    public RegisteredUser getUser(String username) throws PersistenceException {
        return userMapper.get(username);
    }
    
    /**
     * Update the information about a user.
     * @param user user information to be stored.
     * @throws io.PersistenceException
     */
    public void putUser(RegisteredUser user) throws PersistenceException {
        userMapper.put(user);
    }
    
    /**
     * Return the list of names of the friends of the given user.
     * @param user the name of the user
     * @return the list of names of the friends
     * @throws io.PersistenceException
     */
    public List<RegisteredUser> getFriends(String user) throws PersistenceException {
        List<RegisteredUser> friends = new ArrayList<>();
        for (String s : friendshipMapper.getFriends(user))
            friends.add(userMapper.get(s));
        return friends;        
    }

    /**
     * Add a new friendship.
     * @param f the friendship
     */
    public void putFriendship(Friendship f) throws PersistenceException {
        friendshipMapper.put(f);
    }

    /**
     * Retrieve friendship information.
     * @param requester
     * @param recipient
     * @return friendship among the two users or null.
     */
    public Friendship getFriendship(String requester, String recipient) throws PersistenceException {
        return friendshipMapper.get(requester, recipient);
    }
}
