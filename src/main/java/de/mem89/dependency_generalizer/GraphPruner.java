package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.MavenDependencyEdge;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.Set;
import java.util.stream.Collectors;

public class GraphPruner extends AbstractLogEnabled {
    public void prune(MavenReactorGraph graph)  {
        getLogger().info("Pruning leaves from the graph that are not dependencies");
        Set<MavenProject> modulesToPrune = graph.vertexSet().stream()
                .filter(v -> isToPrune(v, graph))
                .collect(Collectors.toSet());

        getLogger().info(String.format("%d modules will be pruned", modulesToPrune.size()));
        graph.removeAllVertices(modulesToPrune);
    }

    private boolean isToPrune(MavenProject v, MavenReactorGraph graph) {
        if(isLeaf(v, graph) || isDependency(v, graph)) {
            return false;
        }
        return true;
    }

    private static boolean isDependency(MavenProject v, MavenReactorGraph graph) {
        return graph.incomingEdgesOf(v).stream()
                .filter(e -> e instanceof MavenDependencyEdge).count() > 0;
    }

    private static boolean isLeaf(MavenProject v, MavenReactorGraph graph) {
        return graph.outgoingEdgesOf(v).size() > 0;
    }
}
