package com.bsb.tools.talend.export;

import java.io.PrintStream;

/**
 * {@link BuildResultHandler} that prints {@link BuildIssue} on a
 * {@link PrintStream}.
 *
 * @author Sebastien Gerard
 */
public class BuildResultPrintHandler implements BuildResultHandler<BuildResult> {

    private final PrintStream printStream;
    private BuildIssueSeverity severity = BuildIssueSeverity.INFO;

    private BuildResultPrintHandler(PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * Creates a new handler that prints the issues on the specified stream.
     */
    public static BuildResultPrintHandler printIssuesOn(PrintStream printStream) {
        return new BuildResultPrintHandler(printStream);
    }

    @Override
    public BuildResult handle(BuildResult buildResult) {
        for (BuildIssue buildIssue : buildResult.getIssues()) {
            if (buildIssue.getSeverity().isSeverThan(severity)) {
                printStream.println(buildIssue);
            }
        }

        return buildResult;
    }

    /**
     * Specifies that only issues with at least (or equals) the specified
     * severity will be printed out.
     *
     * @return this instance
     */
    public BuildResultPrintHandler filterOnSeverity(BuildIssueSeverity severity) {
        this.severity = severity;

        return this;
    }
}
