package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencydirection.DependencyDirectionAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "findDependencyDirectionSmell",
        description =
                "Find occurrences of the dependency direction smell. Layers must be ordered from bottom to top and separated by ','. Available analysis levels are type, component and package",
        mixinStandardHelpOptions = true)
public class DependencyDirectionCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS =
            "Starting dependency direction Analysis";

    @Option(
            names = {"-l", "--level"},
            description = "Level to apply analysis",
            defaultValue = "component")
    String level = "component";

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
            names = {"-a", "--architecture-layer"},
            required = true,
            description = "Layer information")
    String layer;

    @Override
    public void run() {
        switch (level) {
            case "type":
                findDependencyDirectionSmellType(language, code, layer);
                break;
            case "package":
                findDependencyDirectionSmellPackage(language, code, layer);
                break;
            case "component":
                findDependencyDirectionSmellComponent(language, code, layer);
                break;
            default:
                LOGGER.atWarning().log(
                        "Level %s not found. Available are type,component and package", level);
        }
    }

    private void findDependencyDirectionSmellType(String language, String code, String layer) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "type", layer));
    }

    private void findDependencyDirectionSmellPackage(String language, String code, String layer) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "package", layer));
    }

    private void findDependencyDirectionSmellComponent(String language, String code, String layer) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "component", layer));
    }

    private Report createReport(String language, String code, String level, String layer) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        DependencyDirectionAnalyzer dda = new DependencyDirectionAnalyzer();
        LOGGER.atInfo().log(dda.getDescription());
        Settings settings = dda.getSettings();
        settings.setValue("level", level);
        settings.setValue("layers", layer);
        return dda.analyze(lang, model, settings);
    }
}
