package io.davlor.maven.plugins.dependencyinjector.converter;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class Artifactory2AntConverter extends ArtifactConverter {
    private static final String TEMPLATE = "<dependency org=\"{org}\" name=\"{name}\" rev=\"{rev}\"/>";

    @Override
    public String asString(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();

        return TEMPLATE.replace("{org}", artifact.getGroupId())
                .replace("{name}", artifact.getArtifactId())
                .replace("{rev}", artifact.getVersion());
    }
}
