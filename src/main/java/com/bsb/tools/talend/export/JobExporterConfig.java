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
	private String versionToExport;
	private String contextName;

    public JobExporterConfig(String destinationFile, Map<ExportChoice, Object> choices, String jobsToExport, String version, String contextName) {
        this.destinationFile = destinationFile;
        this.choices = choices;
        this.jobsToExport = jobsToExport;
		this.versionToExport = version;
		this.contextName = contextName;
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
	
	public String getVersion(){
		return versionToExport;
	}
	
	public String getContextName()
	{
		return contextName;
	}
}
