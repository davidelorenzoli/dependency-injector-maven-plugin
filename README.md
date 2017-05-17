# dependency-injector-maven-plugin
This plugin is to dynamically inject formatted dependencies, e.g. into JNLP and Ivy files. The dependencies are determined transitively starting from the module that specifies the plugin.

## Configuration Parameters
This plugin supports the following parameters

| Property | Description | Default Value |
| ------ | ------ | ------- |
| targetFile | the file where to inject the dependencies |
| dependenciesPlaceholder | the text, in the target file, to be replaced with the dependnecies to inject. | %dependencies% |
| dependenciesUrlPath | The prefix used in 'href' attribute in JNLP jar dependency. | /repository5 |
| dependencyType | The injected dependency format. It can be `JNLP` or `IVY` | JNLP |
| excludeScope | The dependency scope list to exclude | test |

## Dependency Formats

This plugin supports two formats: `JNLP` and `IVY`. Let's consider the artifact `junit:junit:4.12`.

**JNLP format**: by choosing `JNLP` format and by setting `dependenciesUrlPath` to `/dependency5`, the injected JNLP jar dependency is:

```xml
 <jar href="/repository5/junit/4.12/junit-4.12.jar" main="true" download="eager"/>

```

**IVY format**: by choosing `IVY` format, the injected IVY dependency is:

```xml
<dependency org="junit" name="junit" rev="4.12"/>
```

## Example Configuration

```xml
<plugin>
    <groupId>maven.plugins.dependencyinjector</groupId>
    <artifactId>dependency-injector-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>deploy</phase>
            <goals>
                <goal>inject</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <targetFile>${project.build.testOutputDirectory}/testTargetFile.txt</targetFile>
        <dependenciesPlaceholder>%dependencies%</dependenciesPlaceholder>
        <dependenciesUrlPath>/repository5</dependenciesUrlPath>
        <dependencyType>JNLP</dependencyType>
        <excludeScope>
            <param>test</param>
        </excludeScope>
    </configuration>
</plugin>
```