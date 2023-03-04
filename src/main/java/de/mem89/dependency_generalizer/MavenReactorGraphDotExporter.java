package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.MavenReactorEdge;
import de.mem89.dependency_generalizer.util.PropertiesUtils;
import lombok.NonNull;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MavenReactorGraphDotExporter {

    @Component
    private PropertiesUtils propertiesUtils;

    public void export(@NonNull SimpleDirectedGraph<MavenProject, MavenReactorEdge> graph) {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = createExporter();

        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        System.out.println(writer);
    }

    public void export(@NonNull MavenReactorGraph graph, @NonNull File outputFile) throws IOException {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = createExporter();

        FileWriter writer = new FileWriter(outputFile);
        exporter.exportGraph(graph, writer);

        writer.close();
    }

    private static DOTExporter<MavenProject, MavenReactorEdge> createExporter() {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = new DOTExporter<>(getVertexIdProvider());
        exporter.setVertexAttributeProvider(getVertexAttributeProvider());
        exporter.setEdgeAttributeProvider(getEdgeAttributeProvider());
        return exporter;
    }

    private static Function<MavenProject, String> getVertexIdProvider() {
        return v -> getNodeId(v);
    }

    private static Function<MavenReactorEdge, Map<String, Attribute>> getEdgeAttributeProvider() {
        return e -> getEdgeAttributeProvider(e);
    }

    private static Function<MavenProject, Map<String, Attribute>> getVertexAttributeProvider() {
        return v -> {
            Map<String, Attribute> attributes = new LinkedHashMap<>();

            attributes.put("label", DefaultAttribute.createAttribute(v.getArtifactId()));

            return attributes;
        };
    }

    private static Map<String, Attribute> getEdgeAttributeProvider(MavenReactorEdge edge) {
        return edge.getProperties().entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> DefaultAttribute.createAttribute((entry.getValue().toString())
        )));
    }

    private static String getNodeId(MavenProject v) {
        return v.getArtifactId().replace('.','_');
    }


}
