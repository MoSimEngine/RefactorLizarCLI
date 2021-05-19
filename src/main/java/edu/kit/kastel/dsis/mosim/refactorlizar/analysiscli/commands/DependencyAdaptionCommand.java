package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.base.Splitter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Language to program dependencies")
public class DependencyAdaptionCommand {
    private static final Logger logger = LogManager.getLogger(DependencyAdaptionCommand.class);
    private final String DELIMITER = ";";

    @ShellMethod("Adapt dependencies from modular language to monolithic program")
    public void adaptDependencies(String program, String csv) {
        Map<String, String> dependencyMapping = new HashMap<>();
        // Load CSV
        Path csvPath = Paths.get(csv);

        try (Stream<String> stream = Files.lines(csvPath)) {
            stream.forEach(
                    line -> {
                        List<String> split = Splitter.onPattern(DELIMITER).splitToList(line);
                        if (split.size() == 2)
                            dependencyMapping.put(
                                    "import " + split.get(0) + ";", "import " + split.get(1) + ";");
                    });
        } catch (IOException e) {
            logger.error(e);
        }

        // Change Dependencies according to CSV
        try (Stream<Path> filesInPath = Files.walk(Paths.get(program))) {
            List<Path> javaFiles =
                    filesInPath
                            .filter(f -> f.toString().endsWith(".java"))
                            .collect(Collectors.toList());
            for (Path path : javaFiles) {
                List<String> replaced = new ArrayList<>();
                try (Stream<String> stream = Files.lines(path)) {
                    stream.forEach(
                            line -> {
                                String replacementLine = dependencyMapping.getOrDefault(line, line);
                                replaced.add(replacementLine);
                            });
                    Files.write(path, replaced);
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
