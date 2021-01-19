package isolateCCompilation;

import mainapp.moduleFactoryInterfaces.ICodeCompilationModuleFactory;
import mainapp.moduleInterfaces.ICodeCompilationModule;

public class ModuleFactory implements ICodeCompilationModuleFactory {
    
    @Override
    public String moduleID() {
        return "IsoC1";
    }
    
    @Override
    public String moduleName() {
        return "ISOLATE - C";
    }
    
    @Override
    public String moduleDescription() {
        return "A module that compiles and runs C programs in an Isolate sandbox.";
    }

    @Override
    public ICodeCompilationModule newModuleInstance() {
        return new IsolateCModule();
    }
    
}
