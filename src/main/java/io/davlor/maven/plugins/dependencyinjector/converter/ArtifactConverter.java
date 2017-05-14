package io.davlor.maven.plugins.dependencyinjector.converter;

import org.eclipse.aether.graph.Dependency;

import java.util.List;

public abstract class ArtifactConverter {
    public abstract String asString(Dependency dependency);

    public String asString(List<Dependency> dependencies) {
        StringBuilder dependenciesString = new StringBuilder();

        for (Dependency dependency : dependencies) {
            dependenciesString.append(asString(dependency));
            dependenciesString.append(System.lineSeparator());
        }
        return dependenciesString.toString();
    }
}
