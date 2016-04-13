package com.bsb.tools.talend.export;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.User;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.actions.importproject.ImportProjectsUtilities;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;

/**
 * Bunch of utility methods handling a {@link RepositoryContext}.
 *
 * @author Sebastien Gerard
 */
public final class Workspace {

    private final ProxyRepositoryFactory repositoryFactory;
    private final RepositoryContext repositoryContext;

    private Workspace(ProxyRepositoryFactory repositoryFactory, RepositoryContext repositoryContext) {
        this.repositoryFactory = repositoryFactory;
        this.repositoryContext = repositoryContext;
    }

    /**
     * Initializes and returns a new Talend workspace representation.
     *
     * @throws WorkspaceInitializationException if the workspace failed to be initialized
     */
    public static Workspace initializeWorkspace() throws WorkspaceInitializationException {
        CommonsPlugin.setHeadless(true);

        final RepositoryContext repositoryContext = new RepositoryContext();
        repositoryContext.setUser(createUser());
        repositoryContext.setFields(new HashMap<String, String>());

        final ProxyRepositoryFactory repositoryFactory = initializeRepositoryFactory();
        CorePlugin.getContext().putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext);

        return new Workspace(repositoryFactory, repositoryContext);
    }

    /**
     * Logs on the project having the specified name and and located in the current workspace.
     * If the project has not been imported in the workspace yet then it's automatically imported.
     *
     * @throws ProjectImportException if the project failed to be imported
     */
    public com.bsb.tools.talend.export.Project useProject(String projectName) throws ProjectImportException {
        Project project = getProject(repositoryFactory, projectName);
        if (project == null) {
            project = importProject(repositoryFactory, projectName);
        }

        if (project == null) {
            throw new ProjectImportException("Cannot find the project [" + projectName + "].");
        }

        repositoryContext.setProject(project);

        try {
            ProxyRepositoryFactory.getInstance().logOnProject(project, new NullProgressMonitor());
        } catch (LoginException | PersistenceException e) {
            throw new ProjectImportException("Cannot login on project [" + project + "].", e);
        }

        return new com.bsb.tools.talend.export.Project();
    }

    /**
     * Initializes a new repository factory.
     *
     * @throws WorkspaceInitializationException if the workspace failed to be initialized
     */
    private static ProxyRepositoryFactory initializeRepositoryFactory() throws WorkspaceInitializationException {
        final IRepositoryFactory repositoryById =
              RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);

        try {
            final ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

            repositoryFactory.setRepositoryFactoryFromProvider(repositoryById);
            repositoryFactory.initialize();

            return repositoryFactory;
        } catch (PersistenceException e) {
            throw new WorkspaceInitializationException("Error while initializing repository [" + repositoryById + "].", e);
        }
    }

    /**
     * Returns the project contained in the specified repository and having
     * the specified name.
     *
     * @return the project, or <tt>null</tt> if not found
     * @throws ProjectImportException if the project failed to be imported
     */
    private static Project getProject(ProxyRepositoryFactory repositoryFactory, String projectName) throws ProjectImportException {
        try {
            final Project[] projects = repositoryFactory.readProject();

            for (Project project : projects) {
                if (projectName.equals(project.getLabel())) {
                    return project;
                }
            }

            return null;
        } catch (PersistenceException | BusinessException e) {
            throw new ProjectImportException("Error while reading workspace projects.", e);
        }
    }

    /**
     * Returns a new talend User as used by default in TOS DI.
     */
    private static User createUser() {
        final User user = PropertiesFactoryImpl.init().createUser();

        user.setLogin("test@talend.com");

        return user;
    }

    /**
     * Imports the project having the specified name.
     *
     * @param repositoryFactory the factory to use
     * @param projectName the specified name
     * @throws ProjectImportException if the project failed to be imported
     */
    private static Project importProject(ProxyRepositoryFactory repositoryFactory, String projectName) throws ProjectImportException {
        final String projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + File.separator + projectName;

        try {
            ImportProjectsUtilities.importProjectAs(new Shell(), projectName, projectName, projectPath, new NullProgressMonitor());
        } catch (InvocationTargetException | InterruptedException e) {
            throw new ProjectImportException("Error while importing project [" + projectName + "].", e);
        }

        return getProject(repositoryFactory, projectName);
    }
}
