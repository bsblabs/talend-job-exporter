package com.bsb.tools.talend.export;

/**
 * Represents how a {@link BuildIssue} is sever.
 */
public enum BuildIssueSeverity {

    /**
     * Information issue.
     */
    INFO(0),

    /**
     * Warning message.
     */
    WARNING(1),

    /**
     * This issue has failed the build.
     */
    ERROR(2);

    private final int index;

    BuildIssueSeverity(int index) {
        this.index = index;
    }

    /**
     * Checks if the current severity is sever, or the same than the
     * specified severity.
     */
    public boolean isSeverThan(BuildIssueSeverity severity){
        return index >= severity.index;
    }
}
