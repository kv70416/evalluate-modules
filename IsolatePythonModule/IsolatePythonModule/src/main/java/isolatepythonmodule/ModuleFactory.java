package isolatepythonmodule;

import mainapp.modules.factoryinterfaces.ICodeCompilationModuleFactory;
import mainapp.modules.interfaces.ICodeCompilationModule;

public class ModuleFactory implements ICodeCompilationModuleFactory {
    
    @Override
    public String moduleID() {
        return "IsoPy1";
    }
    
    @Override
    public String moduleName() {
        return "ISOLATE - Python";
    }
    
    @Override
    public String moduleDescription() {
        return "A module that runs Python scripts in an Isolate sandbox.";
    }

    @Override
    public ICodeCompilationModule newModuleInstance() {
        return new IsolatePythonModule();
    }
    
}
