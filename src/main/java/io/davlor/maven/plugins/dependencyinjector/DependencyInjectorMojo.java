package io.davlor.maven.plugins.dependencyinjector;

import io.davlor.maven.plugins.dependencyinjector.converter.ArtifactConverter;
import io.davlor.maven.plugins.dependencyinjector.converter.ArtifactConverterFactory;
import io.davlor.maven.plugins.dependencyinjector.model.DependencyType;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.*;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mojo(name = "inject", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DependencyInjectorMojo extends AbstractMojo {
    @Component
    private ProjectDependenciesResolver projectDependenciesResolver;

    @Parameter(defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repositorySystemSession;

    @Parameter(property = "project", defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "targetFile", readonly = true)
    private File targetFile;

    @Parameter(property = "dependenciesPlaceholder", defaultValue = "%dependencies%", readonly = true)
    private String dependenciesPlaceholder;

    @Parameter(property = "dependenciesUrlPath", defaultValue = "/", readonly = true)
    private String dependenciesUrlPath;

    @Parameter(property = "dependencyType", defaultValue = "JNLP", readonly = true)
    private DependencyType dependencyType;

    @Parameter(property = "excludeScope", readonly = true)
    private Set<String> excludeScope;

    public void execute() throws MojoExecutionException, MojoFailureException {
        ArtifactConverterFactory artifactConverterFactory = new ArtifactConverterFactory(Paths.get(dependenciesUrlPath));
        ArtifactConverter artifactConverter = artifactConverterFactory.getArtifactConverter(dependencyType);

        try {
            List<Dependency> dependencies = projectDependenciesResolver.resolve(buildDependencyResolutionRequest()).getDependencies();

            Iterator<Dependency> iterator = dependencies.iterator();
            while (iterator.hasNext()) {
                boolean excludeDependency = excludeScope.contains(iterator.next().getScope());
                if (excludeDependency) {
                    iterator.remove();
                }
            }

            String dependenciesString = artifactConverter.asString(dependencies);

            createWriteFileWithDependencies(dependenciesString);
        }
        catch (DependencyResolutionException e) {
            getLog().error("Impossible to resolve dependencies", e);
        }
        catch (IOException e) {
            getLog().error("Error while copying dependencies", e);
        }
    }

    private DependencyResolutionRequest buildDependencyResolutionRequest() {
        DependencyResolutionRequest dependencyResolutionRequest = new DefaultDependencyResolutionRequest();
        dependencyResolutionRequest.setMavenProject(project);
        dependencyResolutionRequest.setRepositorySession(repositorySystemSession);

        dependencyResolutionRequest.setResolutionFilter(new DependencyFilter() {
            public boolean accept(DependencyNode dependencyNode, List<DependencyNode> list) {
                return true;
            }
        });

        return dependencyResolutionRequest;
    }

    private void createWriteFileWithDependencies(String dependenciesString) throws IOException {
        Path targetFilePath = targetFile.toPath();
        Path targetFileWithDependenciesPath = Paths.get(project.getBuild().getDirectory(), targetFile.getName());
        Charset charset = StandardCharsets.UTF_8;

        String content = new String(Files.readAllBytes(targetFilePath), charset);
        content = content.replaceAll(dependenciesPlaceholder, dependenciesString.toString());
        Files.write(targetFileWithDependenciesPath, content.getBytes(charset), StandardOpenOption.CREATE_NEW);
    }
}
