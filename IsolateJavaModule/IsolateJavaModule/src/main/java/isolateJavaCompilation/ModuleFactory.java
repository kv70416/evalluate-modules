package isolateJavaCompilation;

import mainapp.moduleFactoryInterfaces.ICodeCompilationModuleFactory;
import mainapp.moduleInterfaces.ICodeCompilationModule;

public class ModuleFactory implements ICodeCompilationModuleFactory {
    
    @Override
    public String moduleID() {
        return "IsoJava1";
    }
    
    @Override
    public String moduleName() {
        return "ISOLATE - Java";
    }
    
    @Override
    public String moduleDescription() {
        return "A module that compiles and runs Java in a Isolate sandbox.";
    }

    @Override
    public ICodeCompilationModule newModuleInstance() {
        return new IsolateJavaModule();
    }
    
}
