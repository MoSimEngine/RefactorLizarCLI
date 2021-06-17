package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "applyLanguageStructureToCode", description = "", mixinStandardHelpOptions = true)
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

    @Override
    public void run() {}
}
