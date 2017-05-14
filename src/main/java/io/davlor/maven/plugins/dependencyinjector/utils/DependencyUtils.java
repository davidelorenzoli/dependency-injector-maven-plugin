package io.davlor.maven.plugins.dependencyinjector.utils;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.graph.Dependency;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class DependencyUtils {
    private final Log log;

    public DependencyUtils(Log log) {
        this.log = log;
    }

    public void saveDependencies(List<Dependency> dependencies, Path destination) throws IOException {
        log.info("Saving " + dependencies.size() + " dependencies to directory " + destination);

        Files.createDirectories(destination);

        for (Dependency dependency : dependencies) {
            File artifactFile = dependency.getArtifact().getFile();
            Path outputFilePath = destination.resolve(artifactFile.getName());

            Files.copy(artifactFile.toPath(), outputFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
