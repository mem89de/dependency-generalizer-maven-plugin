package de.mem89.dependency_generalizer.util;

import lombok.NonNull;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoordinatesUtils {
    public String getFullCoordinates(@NonNull MavenProject mavenProject) {
        Stream<Function<MavenProject, String>> attributeFunctions = Stream.of(
                MavenProject::getGroupId,
                MavenProject::getArtifactId,
                MavenProject::getVersion,
                MavenProject::getPackaging
        );

        return attributeFunctions.map(function -> function.apply(mavenProject))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(":"));
    }
    public String getFullCoordinates(@NonNull Dependency dependency) {
        Stream<Function<Dependency, String>> attributeFunctions = Stream.of(
                Dependency::getGroupId,
                Dependency::getArtifactId,
                Dependency::getVersion,
                Dependency::getScope,
                Dependency::getClassifier
        );

        return attributeFunctions.map(function -> function.apply(dependency))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(":"));
    }

}
