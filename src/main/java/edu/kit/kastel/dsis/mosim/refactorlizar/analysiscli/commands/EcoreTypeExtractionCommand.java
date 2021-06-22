package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import com.google.common.flogger.FluentLogger;
import java.io.IOException;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "showTypesInMetamodels",
        description = "Metamodels Type Extraction",
        mixinStandardHelpOptions = true)
public class EcoreTypeExtractionCommand implements Runnable {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private final String ECORE_FILE_ENDING = "ecore";
    private final PathMatcher matcher =
            FileSystems.getDefault().getPathMatcher("glob:**." + ECORE_FILE_ENDING);
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    @Option(
            names = {"-m", "--modular-language"},
            required = true,
            description = "Path to the root of a language/metamodel (*.ecore)")
    String rootPath;

    @Override
    public void run() {
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths.filter(matcher::matches)
                    .map(this::readMetamodels)
                    .forEach(this::printMetamodelTypes);
        } catch (IOException e) {
            LOGGER.atWarning().withCause(e).log();
        }
    }

    private void printMetamodelTypes(Map<String, List<String>> metamodels) {
        StringJoiner joiner =
                new StringJoiner(
                        System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        for (var entry : metamodels.entrySet()) {
            joiner.add("Metamodel:").add(entry.getKey()).add("Types:");
            entry.getValue().forEach(joiner::add);
        }
        LOGGER.atInfo().log("%s", joiner);
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
            LOGGER.atWarning().withCause(e).log();
        }
        return metamodels;
    }
}
