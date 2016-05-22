/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.Friendship;
import java.util.ArrayList;
import java.util.List;

/**
 * In memory storage of friendship information.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
class MemoryFriendshipMapper implements FriendshipMapper {
    List<Friendship> friendships;

    // Constructor.
    MemoryFriendshipMapper() {
        friendships = new ArrayList<>();
    }
    
    @Override
    public List<String> getFriends(String username) throws PersistenceException {
        ArrayList<String> ret = new ArrayList<>();
        for (Friendship f : friendships) {
            if (f.getStatus() == Friendship.Status.ACCEPTED) {
                if (username.equals(f.getRequester())) {
                    ret.add(f.getRequested());
                } else if (username.equals(f.getRequested())) {
                    ret.add(f.getRequester());
                }
            }
        }
        return ret;
    }

    @Override
    public void put(Friendship f) throws PersistenceException {
        
        Friendship fs = get(f.getRequester(), f.getRequested());
        if (fs == null)
            friendships.add(f);
        else
            fs.setStatus(f.getStatus());
    }

    @Override
    public Friendship get(String requester, String recipient) throws PersistenceException {
        for (Friendship f : friendships)
            if (requester.equals(f.getRequester()) && recipient.equals(f.getRequested()))
                return f;
        return null;
    }
}
