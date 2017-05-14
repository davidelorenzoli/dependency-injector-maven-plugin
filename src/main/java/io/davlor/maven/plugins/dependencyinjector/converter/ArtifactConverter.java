package io.davlor.maven.plugins.dependencyinjector.converter;

import org.eclipse.aether.graph.Dependency;

public interface ArtifactConverter<TYPE> {
    public TYPE convert(Dependency dependency);
}
