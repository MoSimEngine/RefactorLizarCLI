package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.featurescatter.FeatureScatterAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "findFeatureScatteringSmell",
        description =
                "Find occurrences of the feature scattering smell. Available analysis levels are type, component and package",
        mixinStandardHelpOptions = true)
public class FeatureScatterCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static final String STARTING_FEATURE_SCATTERING_ANALYSIS =
            "Starting Feature Scattering Analysis";

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
                findFeatureScatteringSmellType(language, code);
                break;
            case "package":
                findFeatureScatteringSmellPackage(language, code);
                break;
            case "component":
                findFeatureScatteringSmellComponent(language, code);
                break;
            default:
                LOGGER.atWarning().log(
                        "Level %s not found. Available analysis levels are type,component and package",
                        level);
        }
    }

    private void findFeatureScatteringSmellType(String language, String code) {
        LOGGER.atInfo().log(STARTING_FEATURE_SCATTERING_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "type"));
    }

    private void findFeatureScatteringSmellPackage(String language, String code) {
        LOGGER.atInfo().log(STARTING_FEATURE_SCATTERING_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "package"));
    }

    private void findFeatureScatteringSmellComponent(String language, String code) {
        LOGGER.atInfo().log(STARTING_FEATURE_SCATTERING_ANALYSIS);
        LOGGER.atInfo().log("%s", createReport(language, code, "component"));
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        FeatureScatterAnalyzer fca = new FeatureScatterAnalyzer();
        LOGGER.atInfo().log(fca.getDescription());
        Settings settings = fca.getSettings();
        settings.setValue("level", level);
        return fca.analyze(lang, model, settings);
    }
}
