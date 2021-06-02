package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.dependencydirection.DependencyDirectionAnalyzer;
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
public class DependencyDirectionCommand {
    private static final String STARTING_DEPENDENCY_CYCLE_ANALYSIS =
            "Starting dependency direction Analysis";
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @ShellMethod(
            "Find occurrences of the dependency direction smells on type level. Layers must be ordered from bottom to top and separated by ','")
    public void findDependencyDirectionSmellType(String language, String code, String layer) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "type", layer));
    }

    @ShellMethod(
            "Find occurrences of the dependency direction smells on package level. Layers must be ordered from bottom to top and separated by ','")
    public void findDependencyDirectionSmellPackage(String language, String code, String layer) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "package", layer));
    }

    @ShellMethod(
            "Find occurrences of the dependency direction smells on component level. Layers must be ordered from bottom to top and separated by ','")
    public void findDependencyDirectionSmellComponent(String language, String code, String layer) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "component", layer));
    }

    private Report createReport(String language, String code, String level, String layer) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        DependencyDirectionAnalyzer dda = new DependencyDirectionAnalyzer();
        logger.info(dda.getDescription());
        Settings settings = dda.getSettings();
        settings.setValue("level", level);
        settings.setValue("layers", layer);
        return dda.analyze(lang, model, settings);
    }
}
