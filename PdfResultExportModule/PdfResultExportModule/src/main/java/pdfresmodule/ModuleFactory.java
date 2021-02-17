package pdfresmodule;

import mainapp.modules.factoryinterfaces.IResultExportModuleFactory;
import mainapp.modules.interfaces.IResultExportModule;

public class ModuleFactory implements IResultExportModuleFactory {

    @Override
    public String moduleID() {
        return "PdfExport1";
    }

    @Override
    public String moduleName() {
        return "Export Results to PDF";
    }

    @Override
    public String moduleDescription() {
        return null;
    }

    @Override
    public IResultExportModule newModuleInstance() {
        return new PdfResultExportModule();
    }
}
