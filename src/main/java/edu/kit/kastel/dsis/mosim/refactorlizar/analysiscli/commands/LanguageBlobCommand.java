package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.SearchLevels;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.languageblob.LanguageBlobAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class LanguageBlobCommand {

    private static final String ERROR_MESSAGE = "Cannot Analyze language path: %s code path: %s";
    private static final String STARTING_LANGUAGE_BLOB_ANALYSIS = "Starting language blobs Analysis";
    private static final Logger logger = LogManager.getLogger(LanguageBlobAnalyzer.class);
    private final String PACKAGE_HEADER = "Simulator Package:";
    private final String TYPE_HEADER = "Simulator Type:";
    private final String COMPONENT_HEADER = "Simulator Component:";

    @ShellMethod("Find occurrences of the language blobs smell on type level")
    public void findLanguageBlobSmellType(String language, String code, @ShellOption(defaultValue="none") String simulatorType) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        if (lba.supportsFullAnalysis()) {
            Report report = lba.fullAnalysis(SearchLevels.TYPE);
            if(simulatorType.equals("none")){
                logger.info(report.getDescription());
            } else {
                logger.info(filterReport(report, simulatorType, SearchLevels.TYPE));
            }
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }

    @ShellMethod("Find occurrences of the language blobs smell on package level")
    public void findLanguageBlobSmellPackage(String language, String code, @ShellOption(defaultValue="none") String simulatorPackage) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        logger.info(lba.getDescription());
        if (lba.supportsFullAnalysis()) {
            Report report = lba.fullAnalysis(SearchLevels.PACKAGE);
            if(simulatorPackage.equals("none")){
                logger.info(report.getDescription());
            } else {
                logger.info(filterReport(report, simulatorPackage, SearchLevels.PACKAGE));
            }
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }
    @ShellMethod("Find occurrences of the language blobs smell on component level")
    public void findLanguageBlobSmellComponent(String language, String code, @ShellOption(defaultValue="none") String simulatorComponent) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        logger.info(lba.getDescription());
        if (lba.supportsFullAnalysis()) {
            Report report = lba.fullAnalysis(SearchLevels.COMPONENT);
            if(simulatorComponent.equals("none")){
                logger.info(report.getDescription());
            } else {
                logger.info(filterReport(report, simulatorComponent, SearchLevels.COMPONENT));
            }
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }
    private LanguageBlobAnalyzer createLanguageBlobAnalyzer(String language, String code) {
        SimulatorModel model = new SimulatorModel(new SimulatorParser().parseLanguage(code));
        ModularLanguage lang = new ModularLanguage(new LanguageParser().parseLanguage(language));
        LanguageBlobAnalyzer lba = new LanguageBlobAnalyzer();
        lba.init(lang, model);
        return lba;
    }

    private String filterReport(Report report, String filterLevel, SearchLevels level){
        String header = getHeader(level);
        String reportDescription = report.getDescription();
        int packageOccurrences = StringUtils.countOccurrencesOf(reportDescription, filterLevel);
        if(reportDescription.contains(filterLevel)) {
            String trimmedReport = "";
            for(int i=0; i < packageOccurrences; i++){
                reportDescription = reportDescription.substring(reportDescription.indexOf(filterLevel));
                if(reportDescription.contains(header)) {
                    trimmedReport += reportDescription.substring(0, reportDescription.indexOf("Simulator Package:"));
                    reportDescription = reportDescription.substring(reportDescription.indexOf('\n')+1);
                }
                else {
                    trimmedReport += reportDescription;
                }
            }
            return trimmedReport;
        } else {
            return "Package not found.";
        }
    }

    private String getHeader(SearchLevels level) {
        switch(level) {
            case PACKAGE:
                return PACKAGE_HEADER;
            case TYPE:
                return TYPE_HEADER;
            default:
                return COMPONENT_HEADER;
        }
    }
}
