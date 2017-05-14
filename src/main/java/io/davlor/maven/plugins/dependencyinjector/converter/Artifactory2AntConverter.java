package io.davlor.maven.plugins.dependencyinjector.converter;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

import java.util.List;

public class Artifactory2AntConverter extends ArtifactConverter {
    private static final String TEMPLATE = "<dependency groupId=\"{groupId}\" artifactId=\"{artifactId}\" version=\"{version}\"/>";

    @Override
    public String asString(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();

        return TEMPLATE.replace("{groupId}", artifact.getGroupId())
                .replace("{artifactId}", artifact.getArtifactId())
                .replace("{version}", artifact.getVersion());
    }
}
