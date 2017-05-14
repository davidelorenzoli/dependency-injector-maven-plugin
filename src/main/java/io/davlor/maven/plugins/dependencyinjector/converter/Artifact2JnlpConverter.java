package io.davlor.maven.plugins.dependencyinjector.converter;

import io.davlor.maven.plugins.dependencyinjector.converter.Artifact2JnlpConverter.JnlpJarDependency;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

import java.nio.file.Path;

public class Artifact2JnlpConverter implements ArtifactConverter<JnlpJarDependency> {
    private final Path urlPath;

    public Artifact2JnlpConverter(Path urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public JnlpJarDependency convert(Dependency dependency) {
        return new JnlpJarDependency(urlPath, dependency);
    }

    public static class JnlpJarDependency {
        private static final String TEMPLATE = "<jar href=\"{href}\" main=\"true\" download=\"eager\"/>";
        private final String href;

        private JnlpJarDependency(Path urlPath, Dependency dependency) {
            Artifact artifact = dependency.getArtifact();
            this.href = urlPath.resolve(artifact.getGroupId().replace(".", "/"))
                    .resolve(artifact.getVersion())
                    .resolve(artifact.getArtifactId() + "-" + artifact.getVersion() + ".jar")
                    .toString();
        }

        @Override
        public String toString() {
            return TEMPLATE.replace("{href}", href);
        }
    }
}
