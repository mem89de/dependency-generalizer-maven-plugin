package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.MavenDependencyEdge;
import de.mem89.dependency_generalizer.util.CoordinatesUtils;
import de.mem89.dependency_generalizer.edges.MavenModuleEdge;
import de.mem89.dependency_generalizer.edges.MavenParentPomEdge;
import lombok.NonNull;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MavenReactorGraphFactory extends AbstractLogEnabled {
    private CoordinatesUtils coordinatesUtils = new CoordinatesUtils();
    private Map<String, MavenProject> artifactIdToProject;
    private MavenReactorGraph graph;

    public MavenReactorGraph create(@NonNull List<MavenProject> reactorProjects) {
        artifactIdToProject = reactorProjects.stream().collect(
                Collectors.toMap(MavenProject::getArtifactId, Function.identity())
        );
        graph = new MavenReactorGraph();
        reactorProjects.forEach(project -> graph.addVertex(project));

        for (MavenProject project : reactorProjects) {
            getLogger().debug(String.format("Adding edges for %s", coordinatesUtils.getFullCoordinates(project)));
            addParentPomEdge(project);
            addModules(project);
            addDependencies(project);
        }

        return graph;
    }

    private void addParentPomEdge(MavenProject project) {
        MavenProject parentProject = project.getParent();

        if (parentProject != null) {
            getLogger().debug(String.format("Adding %s as parent pom", coordinatesUtils.getFullCoordinates(project)));
            graph.addEdge(parentProject, project, new MavenParentPomEdge());
        }
    }

    private void addModules(MavenProject project) {
        List<MavenProject> modules = project.getModules().stream()
                .map(moduleName -> artifactIdToProject.get(moduleName))
                .collect(Collectors.toList());

        for (MavenProject module : modules) {
            getLogger().debug(String.format("Adding module %s", coordinatesUtils.getFullCoordinates(project)));
            graph.addEdge(project, module, new MavenModuleEdge());
        }
    }

    private void addDependencies(MavenProject project) {
        for (Dependency dependency : project.getDependencies()) {
            String dependencyCoordinates = coordinatesUtils.getFullCoordinates(dependency);
            getLogger().debug(String.format("Found Dependency %s", dependencyCoordinates));

            MavenProject dependencyReactorProject = artifactIdToProject.get(dependency.getArtifactId());
            if (dependencyReactorProject != null) {
                getLogger().debug(String.format("Adding %s as dependency", dependencyCoordinates));
                graph.addEdge(project, dependencyReactorProject, new MavenDependencyEdge());
            } else {
                getLogger().debug(String.format("%s ss not found within the reactor.", dependencyCoordinates));
            }
        }
    }
}
