package de.mem89.maven.plugin.dependency_generalizer;

import lombok.NonNull;
import org.apache.maven.project.MavenProject;
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

public class MavenReactorGraphDotExporter {

    private static Map<String, Attribute> getEdgeAttributeProvider(MavenModuleEdge edge) {
        Map<String, Attribute> attributes = new LinkedHashMap<>();

        attributes.put("label", DefaultAttribute.createAttribute("is child of"));

        return attributes;
    }

    public void export(@NonNull MavenReactorGraph graph) {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = createExporter();

        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        System.out.println(writer);
    }

    private DOTExporter<MavenProject, MavenReactorEdge> createExporter() {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = new DOTExporter<>(getVertexIdProvider());
        exporter.setVertexAttributeProvider(getVertexAttributeProvider());
        exporter.setEdgeAttributeProvider(getEdgeAttributeProvider());
        return exporter;
    }

    private static Function<MavenProject, String> getVertexIdProvider() {
        return v -> sanitize(v.getArtifactId());
    }

    private static String sanitize(String nodeName) {
        return nodeName.replace('.', '_');
    }

    private Function<MavenProject, Map<String, Attribute>> getVertexAttributeProvider() {
        return v -> {
            Map<String, Attribute> attributes = new LinkedHashMap<>();

            attributes.put("label", DefaultAttribute.createAttribute(v.getArtifactId()));

            return attributes;
        };
    }

    private static Function<MavenReactorEdge, Map<String, Attribute>> getEdgeAttributeProvider() {
        return e -> getEdgeAttributeProvider(e);
    }

    private static LinkedHashMap<String, Attribute> getEdgeAttributeProvider(MavenReactorEdge edge) {
        return new LinkedHashMap<>();
    }

    public void export(@NonNull MavenReactorGraph graph, @NonNull File outputFile) throws IOException {
        DOTExporter<MavenProject, MavenReactorEdge> exporter = createExporter();

        FileWriter writer = new FileWriter(outputFile);
        exporter.exportGraph(graph, writer);

        writer.close();
    }


}
