package de.mem89.maven.plugin.dependency_generalizer;

import lombok.NonNull;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.util.List;

public class MavenReactorGraphFactory {

    @Inject
    private Log logger;

    public MavenReactorGraph create(@NonNull List<MavenProject> reactorProjects) {
        MavenReactorGraph graph = new MavenReactorGraph();

        for (MavenProject project : reactorProjects) {
            addModuleEdge(graph, project);
        }

        return graph;
    }

    private void addModuleEdge(MavenReactorGraph graph, MavenProject project) {
        logger.debug(String.format("Adding %s to graph", project));
        graph.addVertex(project);

        MavenProject parentProject = project.getParent();
        if (parentProject != null) {
            graph.addVertex(parentProject);
            graph.addEdge(project, parentProject, new MavenModuleEdge());
        }
    }
}
