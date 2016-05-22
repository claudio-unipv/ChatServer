package chat;

import io.PersistenceException;
import io.PersistenceFacade;
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
    
    /**
     * Create the chat object.
     * 
     * @param filename path to the file with the data about the users
     */
    public Chat(String filename) {
        loggedUsers = new ConcurrentHashMap<>();
        administrator = new Administrator();        
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
            u = PersistenceFacade.getInstance().getUser(nickname);
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
     */
    public void logout(RegisteredUser user) {
        loggedUsers.remove(user.getNickname());
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
            if (PersistenceFacade.getInstance().getUser(nickname) != null)
                throw new ChatError("Nickname already taken");                  
            RegisteredUser u = new RegisteredUser(nickname, email, password);
            PersistenceFacade.getInstance().putUser(u);
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
     * Send a welcome message to a user.
     * 
     * @param recipient nickname of the recipient
     * @throws ChatError 
     */
    public void welcome(String recipient) throws ChatError {
        String msg = "Hello " + recipient + ", welcome to the chat!";
        sendMessage(administrator, recipient, msg);
        msg = "" + loggedUsers.size() + " logged users:";
        for (String s : loggedUsers.keySet())
            msg += " " + s;
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
}
