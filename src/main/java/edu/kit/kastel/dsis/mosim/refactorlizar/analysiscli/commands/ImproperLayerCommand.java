package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencylayer.DependencyLayerAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "findDependencyLayerSmell",
        description =
                "Find occurrences of the improper simulator layering smell. Available analysis levels are type, component and package",
        mixinStandardHelpOptions = true)
public class ImproperLayerCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS =
            "Starting improper layering Analysis";

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

    @Override
    public void run() {
        switch (level) {
            case "type":
                findDependencyLayerSmellType(language, code);
                break;
            case "package":
                findDependencyLayerSmellPackage(language, code);
                break;
            case "component":
                findDependencyLayerSmellComponent(language, code);
                break;
            default:
                LOGGER.atWarning().log(
                        "Level %s not found. Available analysis levels are type,component and package",
                        level);
        }
    }

    public void findDependencyLayerSmell(String language, String code, String level) {}

    private void findDependencyLayerSmellType(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "type"));
    }

    private void findDependencyLayerSmellPackage(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "package"));
    }

    private void findDependencyLayerSmellComponent(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "component"));
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        DependencyLayerAnalyzer dla = new DependencyLayerAnalyzer();
        LOGGER.atInfo().log(dla.getDescription());
        Settings settings = dla.getSettings();
        settings.setValue("level", level);
        return dla.analyze(lang, model, settings);
    }
}
