/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

import chat.RegisteredUser;
import java.util.HashMap;
import java.util.Map;

/**
 * In memory storage of mappers, useful for testing.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
class MemoryUserMapper extends UserMapper {

    Map<String, RegisteredUser> users;
    
    MemoryUserMapper() {
        super(0);
        users = new HashMap<>();
    }
    
    @Override
    protected RegisteredUser getUserFromStorage(String username) {
        return users.get(username);
    }

    @Override
    protected void putUserToStorage(RegisteredUser u) {
        users.put(u.getNickname(), u);
    }    
}
