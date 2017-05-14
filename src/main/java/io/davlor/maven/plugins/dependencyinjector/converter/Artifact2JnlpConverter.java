package io.davlor.maven.plugins.dependencyinjector.converter;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

import java.nio.file.Path;

public class Artifact2JnlpConverter extends ArtifactConverter {
    private static final String TEMPLATE = "<jar href=\"{href}\" main=\"true\" download=\"eager\"/>";

    private final Path urlPath;

    public Artifact2JnlpConverter(Path urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public String asString(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();

        String href = urlPath.resolve(artifact.getGroupId().replace(".", "/"))
                .resolve(artifact.getVersion())
                .resolve(artifact.getArtifactId() + "-" + artifact.getVersion() + ".jar")
                .toString();

        return TEMPLATE.replace("{href}", href);
    }
}
