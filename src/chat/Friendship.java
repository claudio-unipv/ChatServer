/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package chat;

/**
 * Object representing a relationship between users.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class Friendship {

    /**
     * The status of the friendship request.
     */
    public enum Status { PENDING, ACCEPTED, REJECTED };
    private Status status;
    private String requester;
    private String requested;
    
    /**
     * Construct a PENDING friendship request.
     * 
     * @param requester the user who originally asked the friendship
     * @param requested the original recipient of the request
     */
    public Friendship(String requester, String requested) {
        this.requester = requester;
        this.requested = requested;
        status = Status.PENDING;
    }
        
    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the requester
     */
    public String getRequester() {
        return requester;
    }

    /**
     * @param requester the requester to set
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * @return the requested
     */
    public String getRequested() {
        return requested;
    }

    /**
     * @param requested the requested to set
     */
    public void setRequested(String requested) {
        this.requested = requested;
    }
}
