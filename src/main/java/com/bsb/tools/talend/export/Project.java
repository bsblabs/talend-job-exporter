package com.bsb.tools.talend.export;

/**
 * Represents a logged-on Talend project.
 *
 * @author Sebastien Gerard
 */
public class Project {

    Project() {
    }

    /**
     * Exports Talend jobs to an archive file according to the specified
     * configuration.
     *
     * @return the result of the build
     */
    public BuildResult export(JobExporterConfigBuilder configBuilder) {
        return export(configBuilder.build());
    }

    /**
     * Exports Talend jobs to an archive file according to the specified
     * configuration.
     *
     * @return the result of the build
     */
    public BuildResult export(JobExporterConfig config) {
        return new JobExporter(config).export();
    }
}
