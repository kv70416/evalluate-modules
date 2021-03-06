package isolatecmodule;

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
    public Text chosenGccFileText = null;
    
    public TextField timeLimitField = null;
    public TextField memLimitField = null;

	public SubMenuGUIController(ModuleConfiguration module, Stage base, Runnable refresh) {
        this.config = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
	}


    public void initialize() {
        timeLimitField.setText(Long.toString(config.getTimeLimit()));
        memLimitField.setText(Long.toString(config.getMemLimit()));

        timeLimitField.textProperty().addListener((obj, oldText, newText) -> {
            try {
                long s = Long.parseLong(newText);
                config.setTimeLimit(s);
            }
            catch (NumberFormatException e) {
                config.setTimeLimit(0);
            }
            refreshFunc.run();
        });
        memLimitField.textProperty().addListener((obj, oldText, newText) -> {
            try {
                long mb = Long.parseLong(newText);
                config.setMemLimit(mb);
            }
            catch (NumberFormatException e) {
                config.setMemLimit(0);
            }
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
    public void openGccFileChooser() {
        if (baseStage == null) {
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(baseStage);
        if (selectedFile != null) {
            if (config.setGccPath(selectedFile.getAbsolutePath())) {
                setChosenGccFileText(selectedFile.getAbsolutePath());
            }
        }
        
        refreshFunc.run();
    }
    
    private void setChosenGccFileText(String chosenFilePath) {
        if (chosenFilePath == null) {
            chosenGccFileText.setText("(no file chosen)");
            chosenGccFileText.setStyle("-fx-font-style: italic");
        }
        else {
            chosenGccFileText.setText(chosenFilePath);
            chosenGccFileText.setStyle("-fx-font-style: normal");
        }
    }


    public void setGccField(String path) {
        setChosenGccFileText(path);
        refreshFunc.run();
    }
}