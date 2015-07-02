package com.bsb.tools.talend.export;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.exportjob.action.JobExportAction;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;

/**
 * Service responsible of exporting jobs into an archive file.
 *
 * @author Sebastien Gerard
 */
public class JobExporter {

    private final JobExporterConfig jobExporterConfig;
    private final JobJavaScriptsManager manager;

    /**
     * Initializes a new service instance exporting to the specified archive file based
     * on the specified choices.
     */
    JobExporter(JobExporterConfig jobExporterConfig) {
        this.jobExporterConfig = jobExporterConfig;
        this.manager = new JobJavaScriptsManager(jobExporterConfig.getChoices(), "Default", "All", -1, -1);
        this.manager.setDestinationPath(jobExporterConfig.getDestinationFile());
        this.manager.setTopFolderName(new File(this.manager.getDestinationPath()).getName());
    }

    /**
     * Exports the Talend job to an archive file according to the current
     * {@link JobExporterConfig}.
     *
     * @throws JobExportException if some jobs failed to be exported
     */
    public BuildResult export() throws JobExportException {
        final List<RepositoryNode> nodes = ProjectNodeUtils.findJobsByPath(jobExporterConfig.getJobsToExport());

        try {
            final Job job = CorePlugin.getDefault().getCodeGeneratorService().initializeTemplates();
            job.join();

            final JobExportAction jobExportAction = new JobExportAction(nodes, "0.1", this.manager, null, "Job"); // TODO support versioning

            jobExportAction.run(new NullProgressMonitor());

            return new BuildResult(jobExportAction.isBuildSuccessful(), getIssues(nodes));
        } catch (InterruptedException | InvocationTargetException | CoreException | SystemException e) {
            throw new JobExportException("Error while exporting jobs.", e);
        }
    }

    /**
     * Returns the issues occurred during the build of the specified nodes.
     */
    private List<BuildIssue> getIssues(List<RepositoryNode> nodes) throws CoreException, SystemException {
        final List<BuildIssue> issues = new ArrayList<>();

        for (RepositoryNode node : nodes) {
            Item item = node.getObject().getProperty().getItem();
            for (IMarker marker : getMarkers(item)) {
                issues.add(new BuildIssue(marker));
            }
        }

        return issues;
    }

    /**
     * Returns the synchronizer to use for the specified item.
     */
    private ITalendSynchronizer getSynchronizer(Item item) {
        ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();

        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                  .getService(ICamelDesignerCoreService.class);
            if (service.isInstanceofCamel(item)) {
                synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createCamelBeanSynchronizer();
            }
        }

        return synchronizer;
    }

    /**
     * Returns all the markers associated to the specified item.
     */
    private List<IMarker> getMarkers(Item item) throws SystemException, CoreException {
        final ITalendSynchronizer synchronizer = getSynchronizer(item);

        return Arrays.asList(synchronizer.getFile(item).findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE));
    }
}
