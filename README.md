# RefactorLizarCLI

This project provides a commandline interface of the [RefactorLizar](https://github.com/MoSimEngine/RefactorLizar) library.

## Commands
RefactorLizarCLI utilizes [PicoCLI](https://picocli.info/} and [GraalVM](https://www.graalvm.org/) thus, commands can be started via the provided binary or via `gradle run --args="<command/s>"`.

### evaluateCode
Evaluates for the given source path hypergraph
code metrics. dataTypePatternsPath and
observedSystemPath is a path to file for
ignored/included types. Every line in this
file is seen as a regex tested against the
qualified type names. DataTypePatterns are
ignored types and observedSystem are included type

### adaptDependencies
Change imports of simulator code according to
the new, modular metamodel.

### findDependencyCycleSmell      
Find occurrences of the dependency cycle smell.

### findDependencyDirectionSmell  
Find occurrences of the dependency direction
smell. Layers must be ordered from bottom to
top and separated by ','. Available analysis
levels are type, component and package

### showTypesInMetamodels
Metamodels Type Extraction
  
### findFeatureScatteringSmell    
Find occurrences of the feature scattering
smell. Available analysis levels are type,
component and package

### findDependencyLayerSmell      
Find occurrences of the improper simulator
layering smell. Available analysis levels are
type, component and package

# findLanguageBlobSmell         
Find occurrences of the language blobs smell.
Available analysis levels are type, component and package

### applyLanguageStructureToCode  
Not implemented yet.
