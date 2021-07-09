package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "applyLanguageStructureToCode", description = "Not implemented yet.", mixinStandardHelpOptions = true)
public class LanguageStructureApplianceCommand implements Runnable {
    public static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    @Option(
            names = {"-m", "--language-model"},
            required = true,
            description = "Path to the language")
    String language;

    @Option(
            names = {"-s", "--simulator-code"},
            required = true,
            description = "Path to the simulator code")
    String code;

    @Option(
            names = {"-p", "--path-structure"},
            description =
                    "Is optional. If the language is part of the code use \"LinC\", if the code is part of the language use \"CinL\", if none of these apply, don't use the flag.",
            defaultValue = "none")
    String language_location;

    @Override
    public void run() {
        LOGGER.atWarning().log("Command is not implemented yet!");
    }
}
