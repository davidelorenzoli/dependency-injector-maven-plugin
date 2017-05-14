package io.davlor.maven.plugins.dependencyinjector.converter;

import io.davlor.maven.plugins.dependencyinjector.converter.Artifactory2AntConverter.AntDependency;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class Artifactory2AntConverter implements ArtifactConverter<AntDependency> {
    @Override
    public AntDependency convert(Dependency dependency) {
        return new AntDependency(dependency);
    }

    public static class AntDependency {
        private static final String TEMPLATE = "<dependency groupId=\"{groupId}\" artifactId=\"{artifactId}\" version=\"{version}\"/>";

        private final Dependency dependency;

        private AntDependency(Dependency dependency) {
            this.dependency = dependency;
        }

        @Override
        public String toString() {
            Artifact artifact = dependency.getArtifact();

            return TEMPLATE.replace("{groupId}", artifact.getGroupId())
                    .replace("{artifactId}", artifact.getArtifactId())
                    .replace("{version}", artifact.getVersion());
        }
    }
}
