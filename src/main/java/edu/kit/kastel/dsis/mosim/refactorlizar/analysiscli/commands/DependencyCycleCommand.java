package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencycycle.DependencyCycleAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.InputKind;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "findDependencyCycleSmell",
        description = "Find occurrences of the dependency cycle smell.",
        mixinStandardHelpOptions = true)
public class DependencyCycleCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS =
            "Starting dependency cycle Analysis";

    @Option(
            names = {"-a", "--analysis-level"},
            description = "Available analysis levels are type, component and package",
            defaultValue = "component")
    String level = "component";

    @Option(
            names = {"-l", "--language"},
            required = true,
            description = "Path to the language")
    String language;

    @Option(
            names = {"-s", "--simulator"},
            required = true,
            description = "Path to the simulator code")
    String code;


    @Option(
            names= {"--input-type-eclipse"}
    )
    boolean eclipse_input_kind;

    private InputKind inputKind = InputKind.FEATURE_FILE;

    @Override
    public void run() {
        if(eclipse_input_kind)
            inputKind = InputKind.ECLIPSE_PLUGIN;
        switch (level) {
            case "type":
                findDependencyCycleSmellType(language, code);
                break;
            case "package":
                findDependencyCycleSmellPackage(language, code);
                break;
            case "component":
                findDependencyCycleSmellComponent(language, code);
                break;
            default:
                LOGGER.atSevere().log(
                        "Level %s not found. Available analysis levels are type,component and package",
                        level);
        }
    }

    private void findDependencyCycleSmellType(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "type"));
    }

    private void findDependencyCycleSmellPackage(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "package"));
    }

    private void findDependencyCycleSmellComponent(String language, String code) {
        LOGGER.atInfo().log(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "component"));
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = SimulatorParser.parseSimulator(code, inputKind);
        ModularLanguage lang = LanguageParser.parseLanguage(language, inputKind);

        DependencyCycleAnalyzer dca = new DependencyCycleAnalyzer();
        LOGGER.atInfo().log("%s", dca.getDescription());
        Settings settings = dca.getSettings();
        settings.setValue("level", level);
        return dca.analyze(lang, model, settings);
    }
}
