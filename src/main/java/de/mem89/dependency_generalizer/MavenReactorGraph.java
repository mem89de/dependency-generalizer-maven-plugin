package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.MavenReactorEdge;
import org.apache.maven.project.MavenProject;
import org.jgrapht.graph.SimpleDirectedGraph;

public class MavenReactorGraph extends SimpleDirectedGraph<MavenProject, MavenReactorEdge> {
    public MavenReactorGraph() {
        super(MavenReactorEdge.class);
    }
}
