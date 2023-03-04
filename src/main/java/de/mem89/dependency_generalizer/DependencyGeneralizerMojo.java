package de.mem89.dependency_generalizer;

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
import java.util.List;

@Mojo(name = "generalize", defaultPhase = LifecyclePhase.VERIFY, aggregator = true)
public class DependencyGeneralizerMojo extends AbstractMojo {
    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactorProjects;
    @Parameter
    private File outputFile;

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
        MavenReactorGraph mavenReactorGraph = graphFactory.create(reactorProjects);

        if(isPrune) {
            pruner.prune(mavenReactorGraph);
        }

        if (outputFile != null) {
            try {
                exporter.export(mavenReactorGraph, outputFile);
            } catch (IOException e) {
                getLog().error("Could not write to file", e);
                throw new RuntimeException(e);
            }
        } else {
            exporter.export(mavenReactorGraph);
        }
    }
}
