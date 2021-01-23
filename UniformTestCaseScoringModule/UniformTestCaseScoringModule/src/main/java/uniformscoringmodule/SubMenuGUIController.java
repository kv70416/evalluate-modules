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

    @FXML
    public void readInFileNameField() {
        module.setInFileName(inFileNameField.getText());
        refreshFunc.run();
    }
    
    @FXML
    public void readOutFileNameField() {
        module.setOutFileName(outFileNameField.getText());
        refreshFunc.run();
    }
    
    @FXML
    public void readScorePerCaseField() {
        int s;
        try {
            s = Integer.parseInt(scorePerCaseField.getText());
        }
        catch (NumberFormatException e) {
            scorePerCaseField.setText(Integer.toString(module.getScorePerCase()));
            refreshFunc.run();
            return;
        }
        module.setScorePerCase(s);
        refreshFunc.run();
    }
    
}
