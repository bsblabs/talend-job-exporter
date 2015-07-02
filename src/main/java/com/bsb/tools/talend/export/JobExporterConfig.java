package com.bsb.tools.talend.export;

import java.util.Map;

import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;

/**
 * Configuration to use for {@link JobExporter}.
 *
 * @author Sebastien Gerard
 */
public class JobExporterConfig {

    private final String destinationFile;
    private final Map<ExportChoice, Object> choices;
    private final String jobsToExport;

    public JobExporterConfig(String destinationFile, Map<ExportChoice, Object> choices, String jobsToExport) {
        this.destinationFile = destinationFile;
        this.choices = choices;
        this.jobsToExport = jobsToExport;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public Map<ExportChoice, Object> getChoices() {
        return choices;
    }

    public String getJobsToExport() {
        return jobsToExport;
    }
}
