/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package chat;

/**
 * Information about registered users.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class UserData {
    private String nickname;
    private String email;
    private char[] password;

    /**
     * Constructor.
     * @param nickname the nickname
     * @param email the email
     * @param password the password
     */
    public UserData(String nickname, String email, char[] password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
    
    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * @param passowrd the password to set
     */
    public void setPassword(char[] passowrd) {
        this.password = passowrd;
    }
}
