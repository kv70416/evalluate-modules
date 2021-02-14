package uniformscoringmodule;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SubMenuGUIController {
    private UniformTestCaseScoringModule module = null;
    private Stage baseStage = null;
    private Runnable refreshFunc = null;
    
    public Text chosenTestCaseDir = null;
    public TextField inFileNameField = null;
    public TextField outFileNameField = null;
    public TextField scorePerCaseField = null;

    public SubMenuGUIController(UniformTestCaseScoringModule module, Stage base, Runnable refresh) {
        this.module = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
    }


    public void initialize() {
        inFileNameField.textProperty().addListener((obj, oldText, newText) -> {
            module.setInFileName(newText);
            refreshFunc.run();
        });
        outFileNameField.textProperty().addListener((obj, oldText, newText) -> {
            module.setOutFileName(newText);
            refreshFunc.run();
        });
        scorePerCaseField.textProperty().addListener((obj, oldText, newText) -> {
            if (newText == null || newText == "") {
                return;
            }
            try {
                int s = Integer.parseInt(newText);
                module.setScorePerCase(s);
            }
            catch (NumberFormatException e) {
                scorePerCaseField.setText(Integer.toString(module.getScorePerCase()));
            }
            refreshFunc.run();
        });

        scorePerCaseField.focusedProperty().addListener((obj, oldFocus, newFocus) -> {
            if (!newFocus) {
                scorePerCaseField.setText(Integer.toString(module.getScorePerCase()));
            }
        });
    }

    
    public void setInFileNameFieldValue(String value) {
        if (inFileNameField != null) {
            inFileNameField.setText(value);
        }
    }
    
    public void setOutFileNameFieldValue(String value) {
        if (outFileNameField != null) {
            outFileNameField.setText(value);
        }
    }
    
    public void setScorePerCaseFieldValue(int score) {
        if (scorePerCaseField != null) {
            scorePerCaseField.setText(Integer.toString(score));
        }
    }
    
    
    @FXML
    public void openTestCaseDirChooser() {
        if (baseStage == null) {
            return;
        }
        
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(baseStage);
        if (selectedDirectory != null) {
            if (module.setTestCaseDir(selectedDirectory.getAbsolutePath())) {
                setChosenTestCaseDirText(selectedDirectory.getAbsolutePath());
            }
        }
        
        refreshFunc.run();
    }

    private void setChosenTestCaseDirText(String chosenFilePath) {
        if (chosenFilePath == null) {
            chosenTestCaseDir.setText("(no file chosen)");
            chosenTestCaseDir.setStyle("-fx-font-style: italic");
        }
        else {
            chosenTestCaseDir.setText(chosenFilePath);
            chosenTestCaseDir.setStyle("-fx-font-style: normal");
        }
    }

}
