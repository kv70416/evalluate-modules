package isolatejavamodule;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SubMenuGUIController {
    private ModuleConfiguration config = null;
    private Stage baseStage = null;
    private Runnable refreshFunc = null;

    public Text chosenIsolateFileText = null;
    public Text chosenJdkDirectoryText = null;
    
    public TextField mainFileField = null;
    
    public SubMenuGUIController(ModuleConfiguration module, Stage base, Runnable refresh) {
        this.config = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
    }


    public void initialize() {
        mainFileField.textProperty().addListener((obj, oldText, newText) -> {
            config.setMainJavaFile(newText);
            refreshFunc.run();
        });
    }

    
    @FXML
    public void openIsolateFileChooser() {
        if (baseStage == null) {
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(baseStage);
        if (selectedFile != null) {
            if (config.setIsolateExePath(selectedFile.getAbsolutePath())) {
                setChosenIsolateFileText(selectedFile.getAbsolutePath());
            }
        }
        
        refreshFunc.run();
    }
    
    private void setChosenIsolateFileText(String chosenFilePath) {
        if (chosenFilePath == null) {
            chosenIsolateFileText.setText("(no file chosen)");
            chosenIsolateFileText.setStyle("-fx-font-style: italic");
        }
        else {
            chosenIsolateFileText.setText(chosenFilePath);
            chosenIsolateFileText.setStyle("-fx-font-style: normal");
        }
    }

    
    @FXML
    public void openJdkDirectoryChooser() {
        if (baseStage == null) {
            return;
        }
        
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(baseStage);
        if (selectedDirectory != null) {
            if (config.setJavaJdkPath(selectedDirectory.getAbsolutePath())) {
                setChosenJdkDirText(selectedDirectory.getAbsolutePath());
            }
        }
        
        refreshFunc.run();
    }
    
    private void setChosenJdkDirText(String chosenDirPath) {
        if (chosenDirPath == null) {
            chosenJdkDirectoryText.setText("(no directory chosen)");
            chosenJdkDirectoryText.setStyle("-fx-font-style: italic");
        }
        else {
            chosenJdkDirectoryText.setText(chosenDirPath);
            chosenJdkDirectoryText.setStyle("-fx-font-style: normal");
        }
    }

    public void setMainFileField(String value) {
        if (value != null) {
            mainFileField.setText(value);
        }
    } 
    
}
