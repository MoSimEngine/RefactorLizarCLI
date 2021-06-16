package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli;

import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.DependencyAdaptionCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.DependencyCycleCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.DependencyDirectionCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.EcoreTypeExtractionCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.FeatureScatterCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.ImproperLayerCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.LanguageBlobCommand;
import edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands.LanguageStructureApplianceCommand;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "analysiscli",
        subcommands = {
                DependencyAdaptionCommand.class,
                DependencyCycleCommand.class,
                DependencyDirectionCommand.class,
                EcoreTypeExtractionCommand.class,
                FeatureScatterCommand.class,
                ImproperLayerCommand.class,
                LanguageBlobCommand.class,
                LanguageStructureApplianceCommand.class
        },
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        description = "Analysis CLI"
)
public class AnalysisCLI implements Callable<Integer> {

    @Option(
            names = {"-l", "--language"},
            description = "Path to the language"
    )
    private String language;

    @Option(
            names = {"-c", "--code"},
            description = "Path to the code"
    )
    private String code;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AnalysisCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        return null;
    }
}
