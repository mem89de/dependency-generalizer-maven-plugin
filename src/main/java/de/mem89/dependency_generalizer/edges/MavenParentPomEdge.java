package de.mem89.dependency_generalizer.edges;

public class MavenParentPomEdge extends MavenReactorEdge {
    private static final String PROPERTIES_FILE_NAME = "parent_pom_edge.properties";

    public MavenParentPomEdge() {
        super(PROPERTIES_FILE_NAME);
    }
}
