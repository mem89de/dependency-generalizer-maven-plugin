package de.mem89.dependency_generalizer.edges;

public enum EdgeType {
    DEPENDENCIES(MavenDependencyEdge.class),
    MODULES(MavenModuleEdge.class),
    PARENTS(MavenParentPomEdge.class);
    private Class edgeClass;

    EdgeType(Class edgeClass) {
        this.edgeClass = edgeClass;
    }

    public Class getEdgeClass() {
        return edgeClass;
    }
}
