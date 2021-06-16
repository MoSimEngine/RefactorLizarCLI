package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.base.Splitter;
import com.google.common.flogger.FluentLogger;
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
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "adaptDependencies",
        description = "Change imports of simulator code according to the new, modular metamodel.",
        mixinStandardHelpOptions = true)
public class DependencyAdaptionCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final String DELIMITER = ";";

    @Option(
            names = {"-c", "--csv-path"},
            required = true,
            description = "Path to the *.csv mapping file")
    String csv;

    @Option(
            names = {"-s", "--simulator-code"},
            required = true,
            description = "Path to the simulator code")
    String program;

    @Override
    public void run() {
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
            LOGGER.atWarning().withCause(e).log();
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
            LOGGER.atWarning().withCause(e).log();
        }
    }
}
