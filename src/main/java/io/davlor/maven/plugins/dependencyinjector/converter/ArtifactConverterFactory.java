package io.davlor.maven.plugins.dependencyinjector.converter;

import io.davlor.maven.plugins.dependencyinjector.model.DependencyType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ArtifactConverterFactory {
    private final Map<DependencyType, ArtifactConverter> artifactConverters;

    public ArtifactConverterFactory(Path urlPath) {
        artifactConverters = new HashMap<>();
        artifactConverters.put(DependencyType.IVY, new Artifactory2IvyConverter());
        artifactConverters.put(DependencyType.JNLP, new Artifact2JnlpConverter(urlPath));
    }

    public ArtifactConverter getArtifactConverter(DependencyType dependencyType) {
        return artifactConverters.get(dependencyType);
    }
}
