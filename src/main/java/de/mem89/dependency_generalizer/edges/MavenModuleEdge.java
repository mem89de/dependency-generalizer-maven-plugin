package de.mem89.dependency_generalizer.edges;

public class MavenModuleEdge extends MavenReactorEdge {
    private static final String PROPERTIES_FILE_NAME = "module_edge.properties";

    public MavenModuleEdge() {
        super(PROPERTIES_FILE_NAME);
    }
}
