package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.Application;
import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.CalculationMode;
import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup("metrics")
public class ArchitectureEvaluationCommand {

    private static final Logger logger = LogManager.getLogger(DependencyAdaptionCommand.class);

    @ShellMethod(
            "Evaluates for the given source path hypergraph code metrics. dataTypePatternsPath and observedSystemPath is a path to file for ignored/included types. Every line in this file is seen as a regex tested against the qualified type names. DataTypePatterns are ignored types and observedSystem are included types")
    public void evaluateMetrics(
            String code,
            @ShellOption(defaultValue = "") String dataTypePatternsPath,
            @ShellOption(defaultValue = "") String observedSystemPath) {
        // das array ist dort weil im moment ich meine eigene api kaputt entworfen habe, das kommt
        // noch weg
        Result result =
                new Application()
                        .evaluate(
                                CalculationMode.REINER,
                                dataTypePatternsPath,
                                observedSystemPath,
                                new String[] {code});
        logger.info("Size of System: " + result.getSizeOfSystem().getValue());
        logger.info("Lines of Code: " + result.getLoc().getValue());
        logger.info("Size of Hypergraph: " + result.getSize().getValue());
        logger.info("Coupling: " + result.getCoupling().getValue());
        logger.info("Cohesion: " + result.getCohesion().getValue());
        logger.info("Complexity: " + result.getComplexity().getValue());
    }
}
