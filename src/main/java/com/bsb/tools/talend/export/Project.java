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
     * @throws JobExportException if some jobs failed to be exported
     */
    public BuildResult export(JobExporterConfigBuilder configBuilder) throws JobExportException {
        return export(configBuilder.build());
    }

    /**
     * Exports Talend jobs to an archive file according to the specified
     * configuration.
     *
     * @return the result of the build
     * @throws JobExportException if some jobs failed to be exported
     */
    public BuildResult export(JobExporterConfig config) throws JobExportException {
        return new JobExporter(config).export();
    }
}
