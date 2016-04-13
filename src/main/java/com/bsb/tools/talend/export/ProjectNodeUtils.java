package com.bsb.tools.talend.export;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.talend.core.GlobalServiceRegister;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryService;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * Bunch of utility methods handling {@link RepositoryNode nodes}.
 *
 * @author Sebastien Gerard
 */
public final class ProjectNodeUtils {

    private static final IRepositoryService REPOSITORY_SERVICE =
          ((IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class));

    private ProjectNodeUtils() {
    }

    /**
     * Returns the main node containing all the talend project.
     */
    public static ProjectRepositoryNode getRepositoryNode() {
        final ProjectRepositoryNode instance = ProjectRepositoryNode.getInstance();

        for(IRepositoryNode child : instance.getChildren()){
            instance.initializeChildren(ProjectManager.getInstance().getCurrentProject(), child);
        }

        return instance;
    }

    /**
     * Returns the child of the specified node that has the specified label.
     *
     * @throws IllegalStateException if there is no matching node
     */
    public static RepositoryNode getNodeByLabel(IRepositoryNode node, String label) {
        for (IRepositoryNode child : node.getChildren()) {
            if (label.equals(child.getLabel())) {
                return (RepositoryNode) child;
            }
        }

        throw new IllegalStateException("Cannot find node with label [" + label + "].");
    }

    /**
     * Finds all the jobs matching the specified path.
     *
     * @return matching children, or empty if there is no match
     * @see #findChildrenByPath(IRepositoryNode, String)
     */
    public static List<RepositoryNode> findJobsByPath(String path) {
        return findChildrenByPath(getNodeByLabel(getRepositoryNode(), "Job Designs"), path);
    }

    /**
     * Finds all the nodes matching the specified path. The path is a regular
     * expression. A possible path can be: "My category/My job transforming X to Y".
     *
     * @return matching children, or empty if there is no match
     */
    public static List<RepositoryNode> findChildrenByPath(IRepositoryNode repositoryNode, String path) {
        final List<RepositoryNode> nodes = new ArrayList<>();

        if (repositoryNode.getChildren() != null) {
            for (IRepositoryNode iRepositoryNode : repositoryNode.getChildren()) {
                if (iRepositoryNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
                    if (isMatching(repositoryNode, path)) {
                        nodes.add((RepositoryNode) iRepositoryNode);
                    }
                } else {
                    nodes.addAll(findChildrenByPath(iRepositoryNode, path));
                }
            }
        }

        return nodes;
    }

    /**
     * Checks whether the specified node match the specified path.
     */
    private static boolean isMatching(IRepositoryNode repositoryNode, String path) {
        return REPOSITORY_SERVICE.getRepositoryPath(repositoryNode).toString().matches(path);
    }
}
