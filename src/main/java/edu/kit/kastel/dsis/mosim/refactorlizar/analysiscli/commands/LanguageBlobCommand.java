package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import java.lang.invoke.MethodHandles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.Report;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.api.SearchLevels;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.languageblob.LanguageBlobAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.commons.Settings;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.LanguageParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.core.SimulatorParser;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class LanguageBlobCommand {

    private static final String STARTING_LANGUAGE_BLOB_ANALYSIS = "Starting language blobs Analysis";
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PACKAGE_HEADER = "Simulator Package:";
    private static final String TYPE_HEADER = "Simulator Type:";
    private static final String COMPONENT_HEADER = "Simulator Component:";

    @ShellMethod("Find occurrences of the language blobs smell on type level")
    public void findLanguageBlobSmellType(String language, String code, @ShellOption(defaultValue="none") String simulatorType) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        Report report = createReport(language, code, "type");
            if(simulatorType.equals("none")){
                logger.info(report.getDescription());
            } else {
                logger.info("{}",() -> filterReport(report, simulatorType, SearchLevels.TYPE));
            }
    }

    @ShellMethod("Find occurrences of the language blobs smell on package level")
    public void findLanguageBlobSmellPackage(String language, String code, @ShellOption(defaultValue="none") String simulatorPackage) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
            Report report = createReport(language, code, "package");
            if(simulatorPackage.equals("none")){
                logger.info(report.getDescription());
            } else {
                logger.info("{}",() -> filterReport(report, simulatorPackage, SearchLevels.PACKAGE));
            }
    }
    @ShellMethod("Find occurrences of the language blobs smell on component level")
    public void findLanguageBlobSmellComponent(String language, String code, @ShellOption(defaultValue="none") String simulatorComponent) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        Report report = createReport(language, code, "component");
        if(simulatorComponent.equals("none")){
            logger.info(report.getDescription());
        } else {
            logger.info("{}",() -> filterReport(report, simulatorComponent, SearchLevels.COMPONENT));
        }
    }
    private Report createReport(String language, String code, String level) {
        SimulatorModel model = new SimulatorModel(SimulatorParser.parseSimulator(code));
        ModularLanguage lang = new ModularLanguage(LanguageParser.parseLanguage(language));

        LanguageBlobAnalyzer lba = new LanguageBlobAnalyzer();
        logger.info(lba.getDescription());
        Settings settings  = lba.getSettings();
        settings.setValue("level", level);
        return lba.analyze(lang, model, settings);
    }

    private String filterReport(Report report, String filterLevel, SearchLevels level){
        String header = getHeader(level);
        String reportDescription = report.getDescription();
        int packageOccurrences = StringUtils.countOccurrencesOf(reportDescription, filterLevel);
        if(reportDescription.contains(filterLevel)) {
            StringBuilder trimmedReport = new StringBuilder();
            for(int i=0; i < packageOccurrences; i++){
                reportDescription = reportDescription.substring(reportDescription.indexOf(filterLevel));
                if(reportDescription.contains(header)) {
                    trimmedReport.append(reportDescription.substring(0, reportDescription.indexOf(PACKAGE_HEADER)));
                    reportDescription = reportDescription.substring(reportDescription.indexOf('\n')+1);
                }
                else {
                    trimmedReport.append(reportDescription);
                }
            }
            return trimmedReport.toString();
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
