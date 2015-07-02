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
     */
    public static Workspace initializeWorkspace(){
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
     * @throws IllegalArgumentException if the specified project does not exist
     */
    public com.bsb.tools.talend.export.Project useProject(String projectName) {
        Project project = getProject(repositoryFactory, projectName);
        if (project == null) {
            project = importProject(repositoryFactory, projectName);
        }

        if (project == null) {
            throw new IllegalArgumentException("Cannot find the project [" + projectName + "].");
        }

        repositoryContext.setProject(project);

        try {
            ProxyRepositoryFactory.getInstance().logOnProject(project, new NullProgressMonitor());
        } catch (LoginException | PersistenceException e) {
            throw new IllegalStateException("Cannot login on project [" + project + "].", e);
        }

        return new com.bsb.tools.talend.export.Project();
    }

    /**
     * Initializes a new repository factory.
     */
    private static ProxyRepositoryFactory initializeRepositoryFactory() {
        final IRepositoryFactory repositoryById =
              RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);

        try {
            final ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

            repositoryFactory.setRepositoryFactoryFromProvider(repositoryById);
            repositoryFactory.initialize();

            return repositoryFactory;
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error while initializing repository [" + repositoryById + "].", e);
        }
    }

    /**
     * Returns the project contained in the specified repository and having
     * the specified name.
     *
     * @return the project, or <tt>null</tt> if not found
     */
    private static Project getProject(ProxyRepositoryFactory repositoryFactory, String projectName) {
        try {
            final Project[] projects = repositoryFactory.readProject();

            for (Project project : projects) {
                if (projectName.equals(project.getLabel())) {
                    return project;
                }
            }

            return null;
        } catch (PersistenceException | BusinessException e) {
            throw new IllegalStateException("Error while initializing repository.", e);
        }
    }

    /**
     * Returns a new talend User as used by default in TOS DI.
     */
    private static User createUser() {
        final User user = PropertiesFactory.eINSTANCE.createUser();

        user.setLogin("test@talend.com");

        return user;
    }

    /**
     * Imports the project having the specified name.
     *
     * @param repositoryFactory the factory to use
     * @param projectName the specified name
     * @throws IllegalArgumentException if the project has not been found
     */
    private static Project importProject(ProxyRepositoryFactory repositoryFactory, String projectName) {
        final String projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + File.separator + projectName;

        try {
            ImportProjectsUtilities.importProjectAs(new Shell(), projectName, projectName, projectPath, new NullProgressMonitor());
        } catch (InvocationTargetException | InterruptedException e) {
            throw new IllegalStateException("Error while importing project [" + projectName + "].", e);
        }

        return getProject(repositoryFactory, projectName);
    }
}
