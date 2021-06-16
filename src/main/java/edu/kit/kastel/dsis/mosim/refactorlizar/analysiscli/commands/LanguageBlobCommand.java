package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.StringUtils;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.SearchLevels;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.languageblob.LanguageBlobAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "findLanguageBlobSmell",
        description =
                "Find occurrences of the language blobs smell. Available analysis levels are type, component and package",
        mixinStandardHelpOptions = true)
public class LanguageBlobCommand implements Runnable {
    public static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static final String STARTING_LANGUAGE_BLOB_ANALYSIS =
            "Starting language blobs Analysis";
    private static final String PACKAGE_HEADER = "Simulator Package:";
    private static final String TYPE_HEADER = "Simulator Type:";
    private static final String COMPONENT_HEADER = "Simulator Component:";

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

    @Option(
            names = {"-t", "--simulator-type"},
            description = "Simulator type filter",
            defaultValue = "none")
    String simulatorType = "none";

    @Override
    public void run() {
        switch (level) {
            case "type":
                findLanguageBlobSmellType(language, code, simulatorType);
                break;
            case "package":
                findLanguageBlobSmellPackage(language, code, simulatorType);
                break;
            case "component":
                findLanguageBlobSmellComponent(language, code, simulatorType);
                break;
            default:
                LOGGER.atWarning().log(
                        "Level %s not found. Available analysis levels are type,component and package",
                        level);
        }
    }

    private void findLanguageBlobSmellType(String language, String code, String simulatorType) {
        LOGGER.atInfo().log(STARTING_LANGUAGE_BLOB_ANALYSIS);
        Report report = createReport(language, code, "type");
        if (simulatorType.equals("none")) {
            LOGGER.atInfo().log(report.getDescription());
        } else {
            LOGGER.atInfo().log("%s", filterReport(report, simulatorType, SearchLevels.TYPE));
        }
    }

    private void findLanguageBlobSmellPackage(
            String language, String code, String simulatorPackage) {
        LOGGER.atInfo().log(STARTING_LANGUAGE_BLOB_ANALYSIS);
        Report report = createReport(language, code, "package");
        if (simulatorPackage.equals("none")) {
            LOGGER.atInfo().log(report.getDescription());
        } else {
            LOGGER.atInfo().log("%s", filterReport(report, simulatorPackage, SearchLevels.PACKAGE));
        }
    }

    private void findLanguageBlobSmellComponent(
            String language, String code, String simulatorComponent) {
        LOGGER.atInfo().log(STARTING_LANGUAGE_BLOB_ANALYSIS);
        Report report = createReport(language, code, "component");
        if (simulatorComponent.equals("none")) {
            LOGGER.atInfo().log(report.getDescription());
        } else {
            LOGGER.atInfo().log(
                    "%s", filterReport(report, simulatorComponent, SearchLevels.COMPONENT));
        }
    }

    private Report createReport(String language, String code, String level) {
        SimulatorModel model = SimulatorParser.parseSimulator(code);
        ModularLanguage lang = LanguageParser.parseLanguage(language);

        LanguageBlobAnalyzer lba = new LanguageBlobAnalyzer();
        LOGGER.atInfo().log(lba.getDescription());
        Settings settings = lba.getSettings();
        settings.setValue("level", level);
        return lba.analyze(lang, model, settings);
    }

    private String filterReport(Report report, String filterLevel, SearchLevels level) {
        String header = getHeader(level);
        String reportDescription = report.getDescription();
        int packageOccurrences = StringUtils.countMatches(reportDescription, filterLevel);
        if (reportDescription.contains(filterLevel)) {
            StringBuilder trimmedReport = new StringBuilder();
            for (int i = 0; i < packageOccurrences; i++) {
                reportDescription =
                        reportDescription.substring(reportDescription.indexOf(filterLevel));
                if (reportDescription.contains(header)) {
                    trimmedReport.append(
                            reportDescription.substring(
                                    0, reportDescription.indexOf(PACKAGE_HEADER)));
                    reportDescription =
                            reportDescription.substring(reportDescription.indexOf('\n') + 1);
                } else {
                    trimmedReport.append(reportDescription);
                }
            }
            return trimmedReport.toString();
        } else {
            return "Package not found.";
        }
    }

    private String getHeader(SearchLevels level) {
        switch (level) {
            case PACKAGE:
                return PACKAGE_HEADER;
            case TYPE:
                return TYPE_HEADER;
            default:
                return COMPONENT_HEADER;
        }
    }
}
