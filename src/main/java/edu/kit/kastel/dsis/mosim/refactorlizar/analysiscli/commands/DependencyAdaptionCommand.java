package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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

@ShellComponent
@ShellCommandGroup("Language to program dependencies")
public class DependencyAdaptionCommand {
    private static final Logger logger = LogManager.getLogger(DependencyAdaptionCommand.class);
    private final String DELIMITER = ";";

    @ShellMethod("Adapt dependencies from modular language to monolithic program")
    public void adaptDependencies(String program, String csv){
        Map<String, String> dependencyMapping = new HashMap<>();
        try {
            // Load CSV
            Path csvPath = Paths.get(csv);
            Files.lines(csvPath).forEach(line -> {
                String[] split = line.split(DELIMITER);
                if(split.length == 2)
                    dependencyMapping.put("import " + split[0] +";", "import " + split[1] + ";");
            });

           // Change Dependencies according to CSV
            Stream<Path> filesInPath = Files.walk(Paths.get(program));
            List<Path> javaFiles = filesInPath.filter(f -> f.toString().endsWith(".java")).collect(Collectors.toList());
            for(Path path: javaFiles){
                List<String> replaced = new ArrayList<>();
                Files.lines(path).forEach(line -> {
                    String replacementLine = dependencyMapping.getOrDefault(line,line);
                    replaced.add(replacementLine);
                });
                Files.write(path, replaced);
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}