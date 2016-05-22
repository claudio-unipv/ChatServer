package chat;

import io.PersistenceException;
import io.PersistenceFacade;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Object that manage the chat.
 * 
 * @author Claudio Cusano
 */
public class Chat {
    
    Map<String, Participant> loggedUsers;
    Administrator administrator;
    PersistenceFacade pFacade;
    
    /**
     * Create the chat object.
     * 
     * @param filename path to the file with the data about the users
     * @throws chat.ChatError
     */
    public Chat(String filename) throws ChatError {
        loggedUsers = new ConcurrentHashMap<>();
        administrator = new Administrator();
        try {
            pFacade = PersistenceFacade.getInstance();
        } catch (PersistenceException ex) {
            throw new ChatError(ex);
        }
    }
    
    /**
     * Login an already registered user.
     * 
     * @param nickname the nickname
     * @param password the password
     * @return the user object corresponding to the given credentials
     * @throws ChatError 
     */
    public RegisteredUser login(String nickname, char[] password) throws ChatError {
        RegisteredUser u;
        try {
            u = pFacade.getUser(nickname);
        } catch (PersistenceException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            throw new ChatError("DB error: " + ex.getMessage());
        }
        if (u == null || !u.checkPassword(password))
            throw new ChatError("Invalid username or password");
        loggedUsers.put(u.getNickname(), u);
        return u;
    }
    
    /**
     * Logout the user.
     * 
     * @param user the user to logout
     * @throws ChatError
     */
    public void logout(RegisteredUser user) throws ChatError {        
        loggedUsers.remove(user.getNickname());
        // notify the online friends
        List<RegisteredUser> friends;
        try {                        
            friends = pFacade.getFriends(user.getNickname());            
        } catch (PersistenceException ex) {
            throw new ChatError(ex);
        }        
        for (RegisteredUser u : friends)
            if (loggedUsers.containsKey(u.getNickname()))
                u.deliverMessage(user, user.getNickname() + " disconnected");
    }
    
    /**
     * Request the registration of a new user
     * 
     * @param nickname the nickname
     * @param email email of the user
     * @param password the password
     * @throws ChatError 
     */
    public synchronized void register(String nickname, String email, char[] password) throws ChatError {
        try {
            if (pFacade.getUser(nickname) != null)
                throw new ChatError("Nickname already taken");                  
            RegisteredUser u = new RegisteredUser(nickname, email, password);
            pFacade.putUser(u);
        } catch (PersistenceException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            throw new ChatError("DB error: " + ex.getMessage());
        }
    }

    /**
     * Send a message to a given user, given his nickname.
     * 
     * @param sender user who send the massage
     * @param recipient nickname of the recipient
     * @param message text message to be sent
     * @throws ChatError 
     */    
    public void sendMessage(Participant sender, String recipient, String message) throws ChatError {
        Participant p = loggedUsers.get(recipient);
        if (p == null)
            throw new ChatError("User '" + recipient + "' is unknown or offline");
        p.deliverMessage(sender, message);
    }
    
    /**
     * Send a message to all the logged users.
     * 
     * @param sender user who send the massage
     * @param message text message to be sent
     * @throws ChatError 
     */
    public void broadCastMessage(Participant sender, String message) throws ChatError {
        for (Participant p : loggedUsers.values())
            p.deliverMessage(sender, message);
    }
    
    /**
     * Request friendship.
     * 
     * @param requester
     * @param recipient
     * @throws chat.ChatError
     */
    public void requestFriendship(String requester, String recipient) throws ChatError {

        try {
            // Check if the requester already issued a request.
            Friendship f = pFacade.getFriendship(requester, recipient);
            if (f != null)
                throw new ChatError("Friendship request already recorded");
            // Check if the recipient already issued a request.
            f = pFacade.getFriendship(recipient, requester);
            if (f != null)
                throw new ChatError("Friendship request already recorded");
            // Check that the recipient is a registered user
            if (pFacade.getUser(recipient) == null)
                throw new ChatError("Unknown user " + recipient);
            // ... and that he is not the requester himself
            if (requester.equals(recipient))
                throw new ChatError("A user cannot ask friendship to himself");
            
            // Create the new request.
            pFacade.putFriendship(new Friendship(requester, recipient));
            
            // Notify the recipient of the request (if online)            
            if (loggedUsers.get(recipient) != null)
                this.sendMessage(administrator, recipient, requester + " request your friendship");
        } catch (PersistenceException ex) {
            throw new ChatError(ex);
        }
    }
    
    /**
     * Confirm a friendship request.
     * 
     * @param requester
     * @param recipient
     * @throws chat.ChatError
     */
    public void acceptFriendship(String recipient, String requester) throws ChatError {
        answerFriendship(recipient, requester, true);
    }
    
    /**
     * Confirm a friendship request.
     * 
     * @param requester
     * @param recipient
     * @throws chat.ChatError
     */
    public void rejectFriendship(String recipient, String requester) throws ChatError {
        answerFriendship(recipient, requester, false);
    }
    
    private void answerFriendship(String recipient, String requester, boolean accept) throws ChatError {
        try {
            Friendship f = pFacade.getFriendship(requester, recipient);
            if (f == null || f.getStatus() != Friendship.Status.PENDING)
                throw new ChatError("No pending friendship to accept or reject.");
            f.setStatus(accept ? Friendship.Status.ACCEPTED : Friendship.Status.REJECTED);
            pFacade.putFriendship(f);
            String msg = recipient + (accept ? " accepted " : " rejected ");
            msg += "your friendship request.";
            Participant u = loggedUsers.get(requester);
            if (u != null)
                u.deliverMessage(administrator, msg);    
        } catch (PersistenceException ex) {
            throw new ChatError(ex);
        }
    }
        
    /**
     * Send a welcome message to a user.
     * 
     * @param recipient nickname of the recipient
     * @throws ChatError 
     */
    public void welcome(String recipient) throws ChatError {
        String msg = "Hello " + recipient + ", welcome to the chat!";
        sendMessage(administrator, recipient, msg);
        
        // Notify to friends        
        msg = "Online friends:";
        List<RegisteredUser> friends;
        try {
            friends = pFacade.getFriends(recipient);
        } catch (PersistenceException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            // This is not so important, in case of error we can skip it.
            return;
        }
        for (RegisteredUser f : friends) {
            if (loggedUsers.containsKey(f.getNickname())) {
                msg += " " + f.getNickname();
                sendMessage(administrator, f.getNickname(), recipient + " is online");
            }
        }                           
        sendMessage(administrator, recipient, msg);
    }

    /**
     * Close the chat service.
     * 
     * @param delaySecs wait a few seconds before the shutdown
     */
    public void shutdown(int delaySecs) {
        for (Participant p : loggedUsers.values())
            p.deliverMessage(administrator, "The chat is closing in " + delaySecs + " seconds...");
        try {
            Thread.sleep(delaySecs * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        loggedUsers.clear();
    }        

    private Exception ChatError(PersistenceException ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
