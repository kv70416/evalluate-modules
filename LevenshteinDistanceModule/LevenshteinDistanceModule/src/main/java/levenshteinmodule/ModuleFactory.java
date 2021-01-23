package levenshteinmodule;

import mainapp.modules.factoryinterfaces.IDuplicateDetectionModuleFactory;
import mainapp.modules.interfaces.IDuplicateDetectionModule;

public class ModuleFactory implements IDuplicateDetectionModuleFactory {

    @Override
    public String moduleID() {
        return "lsDist";
    }

    @Override
    public String moduleName() {
        return "Levenshtein Distance";
    }

    @Override
    public String moduleDescription() {
        return "A module for pairwise calculation of Levenshtein Distance between solutions.";
    }

    @Override
    public IDuplicateDetectionModule newModuleInstance() {
        return new LevenshteinModule();
    }
    
}
