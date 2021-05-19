package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencylayer.DependencyLayerAnalyzer;
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
public class ImproperLayerCommand {
    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS =
            "Starting improper layering Analysis";
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @ShellMethod("Find occurrences of the improper simulator layering smells on type level.")
    public void findDependencyLayerSmellType(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "type"));
    }

    @ShellMethod("Find occurrences of the improper simulator layering smells on package level.")
    public void findDependencyLayerSmellPackage(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "package"));
    }

    @ShellMethod("Find occurrences of the improper simulator layering smells on component level.")
    public void findDependencyLayerSmellComponent(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "component"));
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = new SimulatorModel(SimulatorParser.parseSimulator(code));
        ModularLanguage lang = new ModularLanguage(LanguageParser.parseLanguage(language));

        DependencyLayerAnalyzer dla = new DependencyLayerAnalyzer();
        logger.info(dla.getDescription());
        Settings settings = dla.getSettings();
        settings.setValue("level", level);
        return dla.analyze(lang, model, settings);
    }
}
