package edu.kit.kastel.dsis.mosim.refactorlizar.analysiscli.commands;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.factory.Factory;

@ShellComponent
@ShellCommandGroup("Refactorings")
public class LanguageStructureApplianceCommand {

    @ShellMethod("applyLanguageStructureToCode")
    public void applyLanguageStructureToCode(String language, String code){
        final var codeLauncher = new Launcher();
        codeLauncher.addInputResource(code);
        var codeModel = codeLauncher.buildModel();



    }
}
