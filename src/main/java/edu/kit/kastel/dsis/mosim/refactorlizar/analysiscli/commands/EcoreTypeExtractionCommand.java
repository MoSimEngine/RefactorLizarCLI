package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import static org.apache.logging.log4j.util.Strings.LINE_SEPARATOR;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@ShellComponent
@ShellCommandGroup("Language Knowledge Extraction")
public class EcoreTypeExtractionCommand {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final String ECORE_FILE_ENDING = "ecore";
    private final PathMatcher matcher =
            FileSystems.getDefault().getPathMatcher("glob:**." + ECORE_FILE_ENDING);
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    @ShellMethod("Metamodels Type Extraction")
    public void showTypesInMetamodels(String rootPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths.filter(matcher::matches)
                    .map(this::readMetamodels)
                    .forEach(this::printMetamodelTypes);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void printMetamodelTypes(Map<String, List<String>> metamodels) {
        StringJoiner joiner = new StringJoiner(LINE_SEPARATOR, LINE_SEPARATOR, LINE_SEPARATOR);
        for (var entry : metamodels.entrySet()) {
            joiner.add("Metamodel:").add(entry.getKey()).add("Types:");
            entry.getValue().forEach(joiner::add);
        }
        logger.info(joiner);
    }

    private Map<String, List<String>> readMetamodels(Path path) {
        Map<String, List<String>> metamodels = new HashMap<>();
        metamodels.put(path.toString(), new ArrayList<>());
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document ecoreFile = documentBuilder.parse(path.toFile());
            ecoreFile.getDocumentElement().normalize();
            NodeList types = ecoreFile.getElementsByTagName("eClassifiers");
            for (int i = 0; i < types.getLength(); i++) {
                Node node = types.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element type = (Element) node;
                    metamodels.get(path.toString()).add(type.getAttribute("name"));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error(e);
        }
        return metamodels;
    }
}
