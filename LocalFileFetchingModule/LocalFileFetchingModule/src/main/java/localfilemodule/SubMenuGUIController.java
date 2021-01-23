package localfilemodule;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SubMenuGUIController {
    private LocalFileFetchingModule module = null;
    private Stage baseStage = null;
    private Runnable refreshFunc = null;
    
    public Button browseBtn = null;
    public Text chosenDirText = null;
    
    public SubMenuGUIController(LocalFileFetchingModule module, Stage base, Runnable refresh) {
        this.module = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
    }
    
    private void setChosenDirText(String chosenDirPath) {
        if (chosenDirPath == null) {
            chosenDirText.setText("(no directory chosen)");
            chosenDirText.setStyle("-fx-font-style: italic");
        }
        else {
            chosenDirText.setText(chosenDirPath);
            chosenDirText.setStyle("-fx-font-style: normal");
        }
    }
    
    @FXML
    public void openDirectoryChooser() {
        if (baseStage == null) {
            return;
        }
        
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(baseStage);
        if (selectedDirectory != null) {
            module.setSelectedPath(selectedDirectory.getAbsolutePath());
            setChosenDirText(selectedDirectory.getAbsolutePath());
        }
        
        refreshFunc.run();
    }
}
