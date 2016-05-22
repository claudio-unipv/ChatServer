/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2016 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package io;

/**
 * Errors related to the I/O subsystem.
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class PersistenceException extends Exception {

    /**
     * Creates a new instance of <code>PersistenceException</code> without
     * detail message.
     */
    public PersistenceException() {
    }

    /**
     * Constructs an instance of <code>PersistenceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PersistenceException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>PersistenceException</code> from another
     * exception.
     *
     * @param ex the source exception.
     */
    public PersistenceException(Exception ex) {
        super(ex);
    }
}
