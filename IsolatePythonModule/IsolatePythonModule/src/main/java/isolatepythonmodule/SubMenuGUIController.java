package isolatepythonmodule;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SubMenuGUIController {
    private ModuleConfiguration config = null;
    private Stage baseStage = null;
    private Runnable refreshFunc = null;

    public Text chosenIsolateFileText = null;
    public Text chosenPythonFileText = null;

    public TextField mainFileField = null;
    
	public SubMenuGUIController(ModuleConfiguration module, Stage base, Runnable refresh) {
        this.config = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
	}


    public void initialize() {
        mainFileField.textProperty().addListener((obj, oldText, newText) -> {
            config.setMainFileName(newText);
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
    public void openPythonFileChooser() {
        if (baseStage == null) {
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(baseStage);
        if (selectedFile != null) {
            if (config.setPythonPath(selectedFile.getAbsolutePath())) {
                setChosenPythonFileText(selectedFile.getAbsolutePath());
            }
        }
        
        refreshFunc.run();
    }
    
    private void setChosenPythonFileText(String chosenFilePath) {
        if (chosenFilePath == null) {
            chosenPythonFileText.setText("(no file chosen)");
            chosenPythonFileText.setStyle("-fx-font-style: italic");
        }
        else {
            chosenPythonFileText.setText(chosenFilePath);
            chosenPythonFileText.setStyle("-fx-font-style: normal");
        }
    }


    public void setPythonField(String path) {
        setChosenPythonFileText(path);
        refreshFunc.run();
    }


    public void setMainFileField(String value) {
        if (value != null) {
            mainFileField.setText(value);
        }
    } 
    
}