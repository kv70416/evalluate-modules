package isolatejavamodule;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.modules.interfaces.ICodeCompilationModule;

public class IsolateJavaModule implements ICodeCompilationModule {
    
    private ModuleConfiguration config = new ModuleConfiguration();
    private ModuleExecution exec = new ModuleExecution();

    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(config, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/isolatejavagui/SubMenuGUI.fxml"));

        try {
            VBox submenuGUI = loader.<VBox>load();
            
            SubMenuGUIController ctrl = loader.<SubMenuGUIController>getController();
            ctrl.setMainFileField(config.getMainFile());

            return submenuGUI;
        }
        catch (IOException e) {
            // TODO
            System.out.println("Module GUI load error:" + e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean isConfigured() {
        return config.validate();
    }


    @Override
    public boolean initialize(List<String> students) {
        return exec.initialize(students, config);
    }

    @Override
    public boolean compileSource(String student) {
        return exec.compile(student, config);
    }

    @Override
    public boolean runProgram(String student) {
        return exec.execute(student, config);
    }

    @Override
    public boolean cleanUp(List<String> students) {
        return exec.cleanUp(students, config);
    }

    
    @Override
    public String sourceTargetDirectoryPath(String student) {
        return exec.getStudentBoxSrcPath(student);
    }

    @Override
    public String inputTargetDirectoryPath(String student) {
        return exec.getStudentBoxInPath(student);
    }

    @Override
    public String programOutputFilePath(String student) {
        return exec.getStudentBoxOutPath(student);
    }
    
}
