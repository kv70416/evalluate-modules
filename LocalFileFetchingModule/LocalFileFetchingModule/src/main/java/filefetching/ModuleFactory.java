package filefetching;

import mainapp.moduleFactoryInterfaces.IFileFetchingModuleFactory;
import mainapp.moduleInterfaces.IFileFetchingModule;

public class ModuleFactory implements IFileFetchingModuleFactory {

    @Override
    public String moduleID() {
        return "LocalFetch";
    }
    
    @Override
    public String moduleName() {
        return "Local Source File Fetch";
    }
    
    @Override
    public String moduleDescription() {
        return "Fetch student solutions as directories from a chosen directory on the local machine.";
    }

    @Override
    public IFileFetchingModule newModuleInstance() {
        return new LocalFileFetchingModule();
    }
    
}
