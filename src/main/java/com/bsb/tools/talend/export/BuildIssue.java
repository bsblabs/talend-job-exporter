package com.bsb.tools.talend.export;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

/**
 * Represents an issue that may have failed the build.
 */
public class BuildIssue {

    private final int lineNumber;
    private final String message;
    private final BuildIssueSeverity severity;
    private final Integer charStart;
    private final Integer charEnd;

    public BuildIssue(IMarker marker) {
        try {
            this.lineNumber = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
            this.message = (String) marker.getAttribute(IMarker.MESSAGE);
            this.severity = fromTalendSeverity((Integer) marker.getAttribute(IMarker.SEVERITY));

            this.charStart = (Integer) marker.getAttribute(IMarker.CHAR_START);
            this.charEnd = (Integer) marker.getAttribute(IMarker.CHAR_END);
        } catch (CoreException e) {
            throw new IllegalStateException("Error while retrieving information from marker.", e);
        }
    }

    /**
     * Converts a Talend severity integer to its counterpart representation.
     */
    private static BuildIssueSeverity fromTalendSeverity(int talendSeverity) {
        switch (talendSeverity) {
            case IMarker.SEVERITY_ERROR:
                return BuildIssueSeverity.ERROR;
            case IMarker.SEVERITY_WARNING:
                return BuildIssueSeverity.INFO;
            case IMarker.SEVERITY_INFO:
                return BuildIssueSeverity.INFO;
            default:
                throw new IllegalStateException("Unknown talend severity [" + talendSeverity + "].");
        }
    }

    /**
     * Returns the line in the source file where there is an issue.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the message describing the issue.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the severity of the issue.
     */
    public BuildIssueSeverity getSeverity() {
        return severity;
    }

    /**
     * Returns the first character where the issue in the source file starts.
     */
    public Integer getCharStart() {
        return charStart;
    }

    /**
     * Returns the last character where the issue in the source file ends.
     */
    public Integer getCharEnd() {
        return charEnd;
    }

    @Override
    public String toString() {
        return "Issue [" + getSeverity() + "] at line " + getLineNumber() + " char " + getCharStart() + ":"
              + getCharEnd() + "; message: [" + getMessage() + "].";
    }
}
