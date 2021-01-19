package uniformScoring;

import mainapp.moduleFactoryInterfaces.ISolutionScoringModuleFactory;
import mainapp.moduleInterfaces.ISolutionScoringModule;

public class ModuleFactory implements ISolutionScoringModuleFactory {

    @Override
    public String moduleID() {
        return "uniform";
    }

    @Override
    public String moduleName() {
        return "Uniform Test Case Scoring";
    }

    @Override
    public String moduleDescription() {
        return "Solutions are scored per test case, with each case awarding a set score towards a total sum. Test cases are read from a local directory.";
    }

    @Override
    public ISolutionScoringModule newModuleInstance() {
        return new UniformTestCaseScoringModule();
    }
    
}
