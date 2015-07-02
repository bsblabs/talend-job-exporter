package com.bsb.tools.talend.export;

/**
 * Exception thrown when a workspace failed to be initialized.
 *
 * @author Sebastien Gerard
 */
public class WorkspaceInitializationException extends Exception {

    public WorkspaceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
