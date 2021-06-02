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
            "Find occurrences of the dependency direction smell.Layers must be ordered from bottom to top and separated by ','. Available levels are type, component and package")
    public void findDependencyDirectionSmell(
            String language, String code, String layer, String level) {
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
                logger.atError()
                        .log("Level {} not found. Available are type,component and package", level);
        }
    }

    private void findDependencyDirectionSmellType(String language, String code, String layer) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "type", layer));
    }

    private void findDependencyDirectionSmellPackage(String language, String code, String layer) {
        logger.info(STARTING_DEPENDENCY_CYCLE_ANALYSIS);
        logger.info("{}", () -> createReport(language, code, "package", layer));
    }

    private void findDependencyDirectionSmellComponent(String language, String code, String layer) {
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
