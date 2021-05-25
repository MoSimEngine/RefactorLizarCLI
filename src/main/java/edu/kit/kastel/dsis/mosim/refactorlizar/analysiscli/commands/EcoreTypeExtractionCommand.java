package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
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
    private final String ECORE_FILE_ENDING = ".ecore";
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    @ShellMethod("Metamodels Type Extraction")
    public void showTypesInMetamodels(String rootPath) {
        Map<String, List<String>> metamodels = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths.filter(path -> path.toString().endsWith(ECORE_FILE_ENDING))
                    .forEach(
                            path -> {
                                readMetamodels(metamodels, path);
                                printMetamodelTypes(metamodels);
                            });
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void printMetamodelTypes(Map<String, List<String>> metamodels) {
        StringBuilder sb = new StringBuilder();
        metamodels.forEach(
                (key, value) -> {
                    sb.append(Strings.LINE_SEPARATOR)
                            .append("Metamodel:")
                            .append(Strings.LINE_SEPARATOR)
                            .append(key)
                            .append(Strings.LINE_SEPARATOR)
                            .append("Types:")
                            .append(Strings.LINE_SEPARATOR);
                    value.forEach(type -> sb.append(type).append(Strings.LINE_SEPARATOR));
                });
        logger.info(sb);
    }

    private void readMetamodels(Map<String, List<String>> metamodels, Path path) {
        metamodels.put(path.toString(), new ArrayList<>());
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document ecoreFile = documentBuilder.parse(new File(String.valueOf(path)));
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
    }
}
