package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.SearchLevels;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.featurescatter.FeatureScatterAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class FeatureScatterCommand {

    private static final String ERROR_MESSAGE = "Cannot Analyze language path: %s code path: %s";
    private static final String STARTING_FEATURE_SCATTERING_ANALYSIS = "Starting Feature Scattering Analysis";
    private static final Logger logger = LogManager.getLogger(FeatureScatterAnalyzer.class);

    @ShellMethod("Find occurrences of the feature scattering smell on type level")
    public void findFeatureScatteringSmellType(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        FeatureScatterAnalyzer fsa = createFeatureScatterAnalyzer(language, code);
        if (fsa.supportsFullAnalysis()) {
            logger.info(fsa.fullAnalysis(SearchLevels.TYPE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }

    @ShellMethod("Find occurrences of the feature scattering smell on package level")
    public void findFeatureScatteringSmellPackage(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        FeatureScatterAnalyzer fsa = createFeatureScatterAnalyzer(language, code);
        logger.info(fsa.getDescription());
        if (fsa.supportsFullAnalysis()) {
            logger.info(fsa.fullAnalysis(SearchLevels.PACKAGE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);

        }
    }
    @ShellMethod("Find occurrences of the feature scattering smell on component level")
    public void findFeatureScatteringSmellComponent(String language, String code) {
        logger.info(STARTING_FEATURE_SCATTERING_ANALYSIS);
        FeatureScatterAnalyzer fsa = createFeatureScatterAnalyzer(language, code);
        logger.info(fsa.getDescription());
        if (fsa.supportsFullAnalysis()) {
            logger.info(fsa.fullAnalysis(SearchLevels.COMPONENT));
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }
    private FeatureScatterAnalyzer createFeatureScatterAnalyzer(String language, String code) {
        SimulatorModel model = new SimulatorModel(new SimulatorParser().parseLanguage(code));
        ModularLanguage lang = new ModularLanguage(new LanguageParser().parseLanguage(language));
        FeatureScatterAnalyzer fsa = new FeatureScatterAnalyzer();
        fsa.init(lang, model);
        return fsa;
    }
}
