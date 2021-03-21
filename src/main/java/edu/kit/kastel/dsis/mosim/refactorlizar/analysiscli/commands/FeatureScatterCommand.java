package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.featurescatter.FeatureScatterAnalyzer;
import edu.kit.kastel.sdq.case4lang.refactorlizar.analyzer.featurescatter.PackageVisitor;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.Feature;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.ModularLanguage;
import edu.kit.kastel.sdq.case4lang.refactorlizar.model.SimulatorModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("bad-smell-analysis")
public class FeatureScatterCommand {

    private static final Logger logger = LogManager.getLogger(FeatureScatterAnalyzer.class);

    @ShellMethod("Find occurrences of the feature scattering smell")
    public void findFeatureScatteringSmell(String language, String code){
        logger.info("Starting Feature Scattering Analysis");
        Launcher languageLauncher = new Launcher();
        languageLauncher.addInputResource(language);
        CtModel languageModel = languageLauncher.buildModel();

        Collection<Feature> feature = new ArrayList<>();
        languageModel.getAllPackages().stream().filter(v->isFeaturePackage(v.getQualifiedName())).forEach(t -> feature.add(new Feature(t, null)));
        ModularLanguage lang = new ModularLanguage(feature);

        Launcher simulatorLauncher = new Launcher();
        simulatorLauncher.addInputResource(code);
        CtModel spoonSimulatorModel = simulatorLauncher.buildModel();

        SimulatorModel simulatorModel = new SimulatorModel(spoonSimulatorModel.getAllPackages().stream().map(v -> new Feature(v,null)).collect(Collectors.toList()));

        FeatureScatterAnalyzer fsa = new FeatureScatterAnalyzer();
        fsa.init(lang, simulatorModel);
        logger.info(fsa.getDescription());
        if(fsa.supportsFullAnalysis()){
            logger.info(fsa.fullAnalysis());
        } else {
            logger.error("Cannot Analyze "+language+" and "+code);
        }
    }

    private boolean isFeaturePackage(String qn){
        if(qn.contains(".impl")|qn.contains(".util")|qn.isEmpty())
            return false;
        return true;
    }
}
