package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.Application;
import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.CalculationMode;
import edu.kit.kastel.sdq.case4lang.refactorlizar.architecture_evaluation.Result;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "evaluateCode",
        description =
                "Evaluates for the given source path hypergraph code metrics. dataTypePatternsPath and observedSystemPath is a path to file for ignored/included types. Every line in this file is seen as a regex tested against the qualified type names. DataTypePatterns are ignored types and observedSystem are included types")
public class ArchitectureEvaluationCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    @Option(
            names = {"-c", "--code"},
            required = true,
            description = "Path to the code to create the metrics")
    String code;

    @Option(
            names = {"-d", "--data-types"},
            required = true,
            description = "Path to the data-types-pattern.cfg file")
    String dataTypePatternsPath;

    @Option(
            names = {"-o", "--observed-system"},
            required = true,
            description = "Path to the observed-system.cfg file")
    String observedSystemPath;

    @Override
    public void run() {
        // das array ist dort weil im moment ich meine eigene api kaputt entworfen habe, das kommt
        // noch weg
        Result result =
                new Application()
                        .evaluate(
                                CalculationMode.REINER,
                                dataTypePatternsPath,
                                observedSystemPath,
                                new String[] {code});
        LOGGER.atInfo().log("Size of System: %s", result.getSizeOfSystem().getValue());
        LOGGER.atInfo().log("Lines of Code: %s", result.getLoc().getValue());
        LOGGER.atInfo().log("Size of Hypergraph: %s", result.getSize().getValue());
        LOGGER.atInfo().log("Coupling: %s", result.getCoupling().getValue());
        LOGGER.atInfo().log("Cohesion: %s", result.getCohesion().getValue());
        LOGGER.atInfo().log("Complexity: %s", result.getComplexity().getValue());
    }
}
