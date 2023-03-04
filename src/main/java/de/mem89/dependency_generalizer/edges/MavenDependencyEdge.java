package de.mem89.dependency_generalizer.edges;

public class MavenDependencyEdge extends MavenReactorEdge {
    private static final String PROPERTIES_FILE_NAME = "dependency_edge.properties";

    public MavenDependencyEdge() {
        super(PROPERTIES_FILE_NAME);
    }
}
