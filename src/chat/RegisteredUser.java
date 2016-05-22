package chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A users registered in the system.
 * 
 * @author Claudio Cusano
 */
public class RegisteredUser implements Participant {
    UserData data;
    List<RegisteredUserObserver> observers;
    
    /**
     * Create the user.
     * 
     * @param nickname the nickname
     * @param email the email 
     * @param password  the password
     */
    public RegisteredUser(String nickname, String email, char[] password) {
        data = new UserData(nickname, email, password);
        observers = new ArrayList<>();
    }
    
    /**
     * Create the user.
     * 
     * @param data information about the user
     */
    public RegisteredUser(UserData data) {
        this.data = data;
        observers = new ArrayList<>();
    }
    
    @Override
    public String getNickname() {
        return data.getNickname();
    }

    /**
     * Email of the user.
     * 
     * @return 
     */
    public String getEmail() {
        return data.getEmail();
    }
    
    /**
     * Password of the user.
     * 
     * @return 
     */
    public char[] getPassword() {
        return data.getPassword();
    }
    
    @Override
    public void deliverMessage(Participant sender, String msg) {
        for (RegisteredUserObserver obs : observers)
            obs.messageTaken(sender, msg);
    }
    
    /**
     * Check if the given password matches that of the user.
     * 
     * @param password the password to be checked
     * @return whether the password match or not
     */
    boolean checkPassword(char[] password) {        
        return Arrays.equals(password, data.getPassword());
    }

    /**
     * Register a new observer for the user.
     * 
     * @param observer the observer to be registered
     */
    public void addObserver(RegisteredUserObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Detach the observer from the user.
     * 
     * @param observer the observer to be detached
     */
    public void removeObserver(RegisteredUserObserver observer) {
        observers.remove(observer);
    }        
}
