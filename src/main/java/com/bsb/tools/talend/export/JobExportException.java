package com.bsb.tools.talend.export;

/**
 * Exception thrown when jobs failed to be exported.
 *
 * @author Sebastien Gerard
 */
@SuppressWarnings("serial")
public class JobExportException extends Exception {

    public JobExportException(String message, Throwable cause) {
        super(message, cause);
    }

}
