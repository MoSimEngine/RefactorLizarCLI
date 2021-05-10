package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
@ShellCommandGroup("Language to program dependencies")
public class DependencyAdaptionCommand {
    private static final Logger logger = LogManager.getLogger(DependencyAdaptionCommand.class);
    private final String DELIMITER = ";";

    @ShellMethod("Adapt dependencies from modular language to monolithic program")
    public void adaptDependencies(String program, String csv){
        StringBuilder sb = new StringBuilder();
        // Load CSV
        Map<String, String> dependencyMapping = new HashMap<>();
        try {
            String line;
            BufferedReader csvReader = new BufferedReader(new FileReader(csv));
            while((line = csvReader.readLine()) != null){
                String[] split = line.split(DELIMITER);
                if(split.length == 2)
                    dependencyMapping.put("import " + split[0] +";", "import " + split[1] + ";");
            }
        } catch (IOException e){
            logger.error(e.getStackTrace());
        }
        //
        try (Stream<Path> walk = Files.walk(Paths.get(program))) {

            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".java")).collect(Collectors.toList());

            for(String javaFile : result){
                if(javaFile.contains("RDSeffSwitch")) {
                    Path path = Paths.get(javaFile);
                    Stream<String> lines = Files.lines(path);
                    List<String> replaced = new ArrayList<>();
                    lines.forEach(line -> {
                        if(dependencyMapping.get(line) != null){
                            replaced.add(line.replace(line, dependencyMapping.get(line)));
                        }
                        else{
                            replaced.add(line);
                        }
                    });
                    Files.write(path, replaced);
                    lines.close();
                }
            }

        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }
}