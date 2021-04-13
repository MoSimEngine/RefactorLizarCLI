package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

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


@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class LanguageBlobCommand {

    private static final String ERROR_MESSAGE = "Cannot Analyze language path: %s code path: %s";
    private static final String STARTING_LANGUAGE_BLOB_ANALYSIS = "Starting language blobs Analysis";
    private static final Logger logger = LogManager.getLogger(LanguageBlobAnalyzer.class);

    @ShellMethod("Find occurrences of the language blobs smell on type level")
    public void findLanguageBlobSmellType(String language, String code) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        if (lba.supportsFullAnalysis()) {
            logger.info(lba.fullAnalysis(SearchLevels.TYPE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);
        }
    }

    @ShellMethod("Find occurrences of the language blobs smell on package level")
    public void findLanguageBlobSmellPackage(String language, String code) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        logger.info(lba.getDescription());
        if (lba.supportsFullAnalysis()) {
            logger.info(lba.fullAnalysis(SearchLevels.PACKAGE));
        } else {
            logger.error(ERROR_MESSAGE, language, code);

        }
    }
    @ShellMethod("Find occurrences of the language blobs smell on component level")
    public void findLanguageBlobSmellComponent(String language, String code) {
        logger.info(STARTING_LANGUAGE_BLOB_ANALYSIS);
        LanguageBlobAnalyzer lba = createLanguageBlobAnalyzer(language, code);
        logger.info(lba.getDescription());
        if (lba.supportsFullAnalysis()) {
            logger.info(lba.fullAnalysis(SearchLevels.COMPONENT));
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
}
