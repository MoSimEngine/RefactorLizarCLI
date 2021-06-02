package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.featurescatter.FeatureScatterAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import java.lang.invoke.MethodHandles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class FeatureScatterCommand {

    private static final String STARTING_FEATURE_SCATTERING_ANALYSIS =
            "Starting Feature Scattering Analysis";
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @ShellMethod(
            "Find occurrences of the feature scattering smell. Available levels are type, component and package")
    public void findFeatureScatteringSmell(String language, String code, String level) {
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
                logger.atError()
                        .log("Level {} not found. Available are type,component and package", level);
        }
    }

    private void findFeatureScatteringSmellType(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "type"));
    }

    private void findFeatureScatteringSmellPackage(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "package"));
    }

    private void findFeatureScatteringSmellComponent(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "component"));
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        FeatureScatterAnalyzer fca = new FeatureScatterAnalyzer();
        logger.info(fca.getDescription());
        Settings settings = fca.getSettings();
        settings.setValue("level", level);
        return fca.analyze(lang, model, settings);
    }
}
