package com.bsb.tools.talend.export;

import java.util.EnumMap;
import java.util.Map;

import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;

/**
 * Builder of {@link JobExporterConfig}.
 *
 * @author Sebastien Gerard
 */
public final class JobExporterConfigBuilder {

    public static JobExporterConfigBuilder toArchiveFile(String destinationFile) {
        return new JobExporterConfigBuilder(destinationFile);
    }

    private final String destinationFile;
    private final Map<ExportChoice, Object> exportChoiceMap;
    private String jobsToExport = "[0-9a-zA-Z]*";
	private String versionToExport = "Latest";
	private String contextName;

    private JobExporterConfigBuilder(String destinationFile) {
        this.destinationFile = destinationFile;

        this.exportChoiceMap = new EnumMap<>(ExportChoice.class);
        exportChoiceMap.put(ExportChoice.needLauncher, true);
        exportChoiceMap.put(ExportChoice.needSystemRoutine, true);
        exportChoiceMap.put(ExportChoice.needUserRoutine, true);
        exportChoiceMap.put(ExportChoice.needTalendLibraries, true);
        exportChoiceMap.put(ExportChoice.needJobItem, true);
        exportChoiceMap.put(ExportChoice.needJobScript, true);
        exportChoiceMap.put(ExportChoice.needContext, true);
        exportChoiceMap.put(ExportChoice.needSourceCode, false);
        exportChoiceMap.put(ExportChoice.applyToChildren, false);
        exportChoiceMap.put(ExportChoice.doNotCompileCode, false);
        exportChoiceMap.put(ExportChoice.needDependencies, true);

    }

    /**
     * Specifies that launcher needs to be generated and exported to the archive.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needLauncher() {
        this.exportChoiceMap.put(ExportChoice.needLauncher, true);

        return this;
    }

    /**
     * Declares that exported jobs routines defined out-of-the-box by Talend.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needSystemRoutine() {
        this.exportChoiceMap.put(ExportChoice.needSystemRoutine, true);

        return this;
    }

    /**
     * Declares that exported jobs routines defined by the user.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needUserRoutine() {
        this.exportChoiceMap.put(ExportChoice.needUserRoutine, true);

        return this;
    }

    /**
     * Specifies that the build requires talend librairies (dom4j, log4, jaxen, ...).
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needTalendLibraries() {
        this.exportChoiceMap.put(ExportChoice.needTalendLibraries, true);

        return this;
    }

    /**
     * Specifies that jobs items must be exported to the archive.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needJobItem() {
        this.exportChoiceMap.put(ExportChoice.needJobItem, true);

        return this;
    }

    /**
     * Specifies that jobs scripts must be exported.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needJobScript() {
        this.exportChoiceMap.put(ExportChoice.needJobScript, true);

        return this;
    }

    /**
     * Specifies that the source code must be exported to the archive.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needSourceCode() {
        this.exportChoiceMap.put(ExportChoice.needSourceCode, true);

        return this;
    }

    /**
     * Specifies that jobs dependencies must be exported.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder needDependencies() {
        this.exportChoiceMap.put(ExportChoice.needDependencies, true);

        return this;
    }

    /**
     * Exports the jobs matching the specified pattern to an archive file.
     *
     * @return this instance
     */
    public JobExporterConfigBuilder jobsWithLabelMatching(String jobsToExport) {
        this.jobsToExport = jobsToExport;

        return this;
    }
	
	/**
	*
	*
	*/
	public JobExporterConfigBuilder setVersion(String version)
	{
		if(version != null && !"".equals(version.trim())) {
			this.versionToExport = version.trim();	
		}
		return this;
	}
	
	public JobExporterConfigBuilder setContext(String contextName)
	{
		if(contextName != null && !"".equals(contextName.trim()))
		{
			this.contextName = contextName.trim();
		}
		return this;
	}
	
	

    /**
     * Builds a new jobs exporter based on the configuration specified so far.
     */
    public JobExporterConfig build() {
        return new JobExporterConfig(destinationFile, exportChoiceMap, jobsToExport, versionToExport, contextName);
    }
}
