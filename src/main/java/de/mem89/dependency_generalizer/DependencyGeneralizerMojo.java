package de.mem89.dependency_generalizer;

import de.mem89.dependency_generalizer.edges.EdgeType;
import de.mem89.dependency_generalizer.edges.MavenReactorEdge;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mojo(name = "generalize", defaultPhase = LifecyclePhase.VERIFY, aggregator = true)
public class DependencyGeneralizerMojo extends AbstractMojo {
    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactorProjects;
    @Parameter
    private File outputFile;

    @Parameter(property = "edgeTypes")
    private List<EdgeType> edgeTypes;

    @Parameter(defaultValue = "false", property = "prune")
    private boolean isPrune;

    @Inject
    private MavenReactorGraphFactory graphFactory;
    @Inject
    private MavenReactorGraphDotExporter exporter;

    @Inject
    GraphPruner pruner;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        initEdgeTypes();
        MavenReactorGraph mavenReactorGraph = graphFactory.create(reactorProjects);

        if (isPrune) {
            pruner.prune(mavenReactorGraph);
        }

        Predicate<MavenReactorEdge> keepEdge = edge -> edgeTypes.stream().map(EdgeType::getEdgeClass).map(edgeClass -> edge.getClass().equals(edgeClass)).anyMatch(b -> b);
        List<MavenReactorEdge> edgesToDelete = mavenReactorGraph.edgeSet().stream().filter(keepEdge.negate()).collect(Collectors.toList());
        if(!edgesToDelete.isEmpty()) {
            getLog().info(String.format("%d edges will be pruned", edgesToDelete.size()));
            mavenReactorGraph.removeAllEdges(edgesToDelete);
        }
        if (outputFile != null) {
            try {
                getLog().info(String.format("Writing dot represenation to %s", outputFile.getAbsolutePath()));
                exporter.export(mavenReactorGraph, outputFile);
            } catch (IOException e) {
                getLog().error("Could not write to file", e);
                throw new RuntimeException(e);
            }
        } else {
            exporter.export(mavenReactorGraph);
        }
    }

    private void initEdgeTypes() {
        if (edgeTypes == null) {
            getLog().debug("edgeTypes not initialized");
            edgeTypes = new ArrayList();
        }
        if (edgeTypes.isEmpty()) {
            getLog().debug("edgeTypes empty");
            edgeTypes.addAll(Arrays.asList(EdgeType.values()));
        }

        getLog().debug(String.format("Edge Types: %s", edgeTypes.toString()));
    }
}
