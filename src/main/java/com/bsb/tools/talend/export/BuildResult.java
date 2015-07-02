package com.bsb.tools.talend.export;

import java.util.List;

/**
 * Result of a {@link JobExporter#export()}.
 *
 * @author Sebastien Gerard
 */
public class BuildResult {

    private final boolean successful;
    private final List<BuildIssue> issues;

    BuildResult(boolean successful, List<BuildIssue> issues) {
        this.successful = successful;
        this.issues = issues;
    }

    /**
     * Returns whether the build was successful.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Returns the issues raised during the build.
     */
    public List<BuildIssue> getIssues() {
        return issues;
    }

    /**
     * Calls the specified handler on this result.
     *
     * @return the result returned by the specified handler
     */
    public <T> T doOnResult(BuildResultHandler<T> handler) {
        return handler.handle(this);
    }
}
