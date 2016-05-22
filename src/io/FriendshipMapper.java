/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.Friendship;
import java.util.List;

/**
 * Manage the access to friendship information.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
interface FriendshipMapper {    
    List<String> getFriends(String username) throws PersistenceException;
    void put(Friendship f) throws PersistenceException;
    Friendship get(String requester, String recipient)  throws PersistenceException;
}
