package com.bsb.tools.talend.export;

import static com.bsb.tools.talend.export.ArgumentUtils.*;
import static com.bsb.tools.talend.export.BuildResultPrintHandler.printIssuesOn;
import static com.bsb.tools.talend.export.JobExporterConfigBuilder.toArchiveFile;
import static com.bsb.tools.talend.export.Workspace.initializeWorkspace;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * {@link IApplication} exporting Talend jobs to Java in a zip file
 * containing job jars.
 *
 * @author Sebastien Gerard
 */
public class JobExportApplication implements IApplication {

    @Override
    public Object start(IApplicationContext iApplicationContext) throws Exception {
		String version = getParameterValue(iApplicationContext, "version");
		if(version == null) version = "Latest";
		String contextName = getParameterValue(iApplicationContext, "context");
		if(contextName == null) contextName = "Default";
		
		
        try {
			
            return initializeWorkspace()
                  .useProject(getMandatoryParameterValue(iApplicationContext, "projectName"))
                  .export(
                        toArchiveFile(getMandatoryParameterValue(iApplicationContext, "targetFile"))
                              .jobsWithLabelMatching(getMandatoryParameterValue(iApplicationContext, "jobsToExport"))
                              .needSystemRoutine()
                              .needUserRoutine()
                              .needTalendLibraries()
                              .needJobScript()
                              .needDependencies()
							  .setVersion(version)
							  .setContext(contextName)
                  )
                  .doOnResult(
                        printIssuesOn(System.err)
                              .filterOnSeverity(BuildIssueSeverity.ERROR)
                  )
                  .isSuccessful() ? EXIT_OK : EXIT_RELAUNCH;
        } catch (Exception e) {
            // unfortunately if this exception is propagated out of this application, the execution is frozen
            e.printStackTrace();

            return EXIT_RELAUNCH;
        }
    }

    @Override
    public void stop() {
    }
}
