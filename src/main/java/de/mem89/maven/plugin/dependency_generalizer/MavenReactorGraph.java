package de.mem89.maven.plugin.dependency_generalizer;

import org.apache.maven.project.MavenProject;
import org.jgrapht.graph.SimpleDirectedGraph;

public class MavenReactorGraph extends SimpleDirectedGraph<MavenProject, MavenReactorEdge> {
    public MavenReactorGraph() {
        super(MavenReactorEdge.class);
    }
}
