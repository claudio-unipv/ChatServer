/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

/**
 * Object that creates mappers.
 * In the future it could be made configurable.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
class MappersFactory {
    private UserMapper userMapper;
    private FriendshipMapper friendshipMapper;
    RDBOperations rdbops;

    /**
     * Constructor.
     */
    MappersFactory() throws PersistenceException {
        rdbops = new RDBOperations();
    }
    
    /**
     * @return the userMapper
     */
    synchronized UserMapper getUserMapper() {
        if (userMapper == null)
            userMapper = new RDBUserMapper(rdbops, 10);        
        return userMapper;
    }

    /**
     * @return the friendshipMapper
     */
    synchronized FriendshipMapper getFriendshipMapper() {
        if (friendshipMapper == null)
            friendshipMapper = new RDBFriendshipMapper(rdbops);
        return friendshipMapper;
    }
    
    
}
