package isolatecmodule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.modules.interfaces.ICodeCompilationModule;

public class IsolateCModule implements ICodeCompilationModule {
    
    private ModuleConfiguration config = new ModuleConfiguration();
    private ModuleExecution exec = new ModuleExecution();

    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(config, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/isolatecgui/SubMenuGUI.fxml"));

        try {
            VBox submenuGUI = loader.<VBox>load();
            
            SubMenuGUIController ctrl = loader.<SubMenuGUIController>getController();

            File gf = lookForInPath("gcc");
            if (gf != null) {
                config.setGccPath(lookForInPath("gcc").getAbsolutePath());
                ctrl.setGccField(config.getGccPath());
            }

            File ldf = lookForInPath("ld");
            if (ldf != null) {
                config.setLdPath(lookForInPath("ld").getAbsolutePath());
            }

            return submenuGUI;
        }
        catch (IOException e) {
            // TODO
            System.out.println("Module GUI load error:" + e.getMessage());
            return null;
        }
    }

    private File lookForInPath(String file) {
        String pathVar = System.getenv("PATH");

        String[] pathVars;
        if (pathVar.contains(":")) {
            pathVars = pathVar.split(":");
        }
        else if (pathVar.contains(";")) {
            pathVars = pathVar.split(";");
        }
        else {
            pathVars = new String[1];
            pathVars[0] = pathVar;
        }

        for (String path : pathVars) {
            File f = Paths.get(path, file).toFile();
            if (f.exists()) {
                return f;
            }
        }
        return null;
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
