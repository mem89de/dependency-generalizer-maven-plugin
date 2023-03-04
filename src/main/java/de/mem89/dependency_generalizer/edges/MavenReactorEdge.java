package de.mem89.dependency_generalizer.edges;

import de.mem89.dependency_generalizer.util.PropertiesUtils;
import org.jgrapht.graph.DefaultEdge;

import java.util.Properties;

public abstract class MavenReactorEdge extends DefaultEdge {
    private static final PropertiesUtils propertiesUtils = new PropertiesUtils();

    private final Properties properties;

    protected MavenReactorEdge(String propertiesFileName) {
        this.properties = propertiesUtils.load(propertiesFileName);
    }

    public Properties getProperties() {
        return properties;
    }

}
