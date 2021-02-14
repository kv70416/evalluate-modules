package gitlabmodule;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.stage.Stage;

public class SubMenuGUIController {
    private GitLabFetchingModule module = null;
    private Stage baseStage = null;
    private Runnable refreshFunc = null;

    public TextField repoIdField;
    public PasswordField tokenField;
    public TextField maxSolSizeField;

    public SubMenuGUIController(GitLabFetchingModule module, Stage base, Runnable refresh) {
        this.module = module;
        this.baseStage = base;
        this.refreshFunc = refresh;
    }


    private class PasswordSkin extends TextFieldSkin {
        private PasswordSkin(TextField tf) {
            super(tf);
        }

        protected String maskText(String txt) {
            if (getSkinnable() instanceof PasswordField) {
                StringBuilder str = new StringBuilder();
                for (char c : txt.toCharArray()) {
                    str.append("*");
                }
                return str.toString();
            }
            else {
                return txt;
            }
        }
    }

    public void initialize() {
        tokenField.setSkin(new PasswordSkin(tokenField));
        maxSolSizeField.setText("1");

        repoIdField.textProperty().addListener((obj, oldText, newText) -> {
            module.setRepoID(newText);
            refreshFunc.run();
        });

        tokenField.textProperty().addListener((obj, oldText, newText) -> {
            module.setToken(newText);
            refreshFunc.run();
        });

        maxSolSizeField.textProperty().addListener((obj, oldText, newText) -> {
            try {
                double mb = Double.parseDouble(newText);
                module.setMaxMBPerStudent(mb);
            }
            catch (NumberFormatException e) {
                module.setMaxMBPerStudent(0);
            }
            refreshFunc.run();
        });
    }
    
}
