package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.MavenDependencyEdge;
import org.apache.maven.project.MavenProject;

import java.util.Set;
import java.util.stream.Collectors;

public class GraphPruner {
    public void prune(MavenReactorGraph graph) {
        Set<MavenProject> leaves = graph.vertexSet().stream()
                .filter(v -> isToPrune(v, graph))
                .collect(Collectors.toSet());

        graph.removeAllVertices(leaves);
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
