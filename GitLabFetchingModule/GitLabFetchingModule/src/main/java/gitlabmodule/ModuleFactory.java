package gitlabmodule;

import mainapp.modules.factoryinterfaces.IFileFetchingModuleFactory;
import mainapp.modules.interfaces.IFileFetchingModule;

public class ModuleFactory implements IFileFetchingModuleFactory {

    @Override
    public String moduleID() {
        return "GitLabFetch";
    }

    @Override
    public String moduleName() {
        return "GitLab Fork Fetch";
    }

    @Override
    public String moduleDescription() {
        return "Fetch student solutions as forks of a base repository on GitLab.";
    }

    @Override
    public IFileFetchingModule newModuleInstance() {
        return new GitLabFetchingModule();
    }
}
