package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import java.lang.invoke.MethodHandles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencycycle.DependencyCycleAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class DependencyCycleCommand {

    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS = "Starting dependency cycle Analysis";
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @ShellMethod("Find occurrences of the dependency cycle smell on type level")
    public void findDependencyCycleSmellType(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "type"));
    }

    @ShellMethod("Find occurrences of the dependency cycle smell on package level")
    public void findDependencyCycleSmellPackage(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "package"));

    }
    @ShellMethod("Find occurrences of the dependency cycle smell on component level")
    public void findDependencyCycleSmellComponent(String language, String code) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "component"));

    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = new SimulatorModel(SimulatorParser.parseSimulator(code));
        ModularLanguage lang = new ModularLanguage(LanguageParser.parseLanguage(language));

        DependencyCycleAnalyzer dca = new DependencyCycleAnalyzer();
        logger.info(dca.getDescription());
        Settings settings  = dca.getSettings();
        settings.setValue("level", level);
        return dca.analyze(lang, model, settings);
    }
}
