/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.Friendship;
import chat.UserData;
import java.util.List;

/**
 * Public interface for the persistence subsystem.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class PersistenceFacade {
    UserMapper userMapper;
    FriendshipMapper friendshipMapper;
    
    /**
     * Create the facade.
     * @throws PersistenceException 
     */
    public PersistenceFacade() throws PersistenceException {
        MappersFactory factory = new MappersFactory();        
        userMapper = factory.getUserMapper();
        friendshipMapper = factory.getFriendshipMapper();
    }
    
    /**
     * Load the user with the given username.
     * @param username name of the user to lookup
     * @return the requested user object or null if he does not exist
     * @throws io.PersistenceException
     */
    public UserData getUser(String username) throws PersistenceException {
        return userMapper.get(username);
    }
    
    /**
     * Update the information about a user.
     * @param user user information to be stored.
     * @throws io.PersistenceException
     */
    public void putUser(UserData user) throws PersistenceException {
        userMapper.put(user);
    }
    
    /**
     * Return the list of names of the friends of the given user.
     * @param user the name of the user
     * @return the list of names of the friends
     * @throws io.PersistenceException
     */
    public List<String> getFriends(String user) throws PersistenceException {
        return friendshipMapper.getFriends(user);            
    }
    
    /**
     * Return the list of names of pending request of friendship for the given user.
     * @param user the name of the user
     * @return the list of names of the users waiting an answer
     * @throws io.PersistenceException
     */
    public List<String> getPendingFriends(String user) throws PersistenceException {
        return friendshipMapper.getPending(user);            
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
