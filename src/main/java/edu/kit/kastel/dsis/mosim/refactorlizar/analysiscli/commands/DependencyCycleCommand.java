package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.SearchLevels;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencycycle.DependencyCycleAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class DependencyCycleCommand {

    private static final String ERROR_MESSAGE = "Cannot Analyze language path: %s code path: %s";
    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS = "Starting dependency cycle Analysis";
    private static final Logger logger = LogManager.getLogger(DependencyCycleAnalyzer.class);

    @ShellMethod("Find occurrences of the language blobs smell on type level")
    public void findDependencyCycleSmellType(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        DependencyCycleAnalyzer dca = createDependencyCycleAnalyzer(language, code);
        if (dca.supportsFullAnalysis()) {
            logger.info(dca.fullAnalysis(SearchLevels.TYPE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }

    @ShellMethod("Find occurrences of the language blobs smell on package level")
    public void findDependencyCycleSmellPackage(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        DependencyCycleAnalyzer dca = createDependencyCycleAnalyzer(language, code);
        logger.info(dca.getDescription());
        if (dca.supportsFullAnalysis()) {
            logger.info(dca.fullAnalysis(SearchLevels.PACKAGE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);

        }
    }
    @ShellMethod("Find occurrences of the language blobs smell on component level")
    public void findDependencyCycleSmellComponent(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        DependencyCycleAnalyzer dca = createDependencyCycleAnalyzer(language, code);
        logger.info(dca.getDescription());
        if (dca.supportsFullAnalysis()) {
            logger.info(dca.fullAnalysis(SearchLevels.COMPONENT));
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }
    private DependencyCycleAnalyzer createDependencyCycleAnalyzer(String language, String code) {
        SimulatorModel model = new SimulatorModel(new SimulatorParser().parseLanguage(code));
        ModularLanguage lang = new ModularLanguage(new LanguageParser().parseLanguage(language));
        DependencyCycleAnalyzer dca = new DependencyCycleAnalyzer();
        dca.init(lang, model);
        return dca;
    }
}
