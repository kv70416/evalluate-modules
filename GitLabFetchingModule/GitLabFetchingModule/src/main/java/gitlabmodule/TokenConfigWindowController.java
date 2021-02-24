package gitlabmodule;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class TokenConfigWindowController {
    public VBox tokenBox = null;
    public PasswordField tokenField = null;
    public Button tokenConfirmBtn = null;

    private GitLabFetchingModule module = null;

	public TokenConfigWindowController(GitLabFetchingModule gitLabFetchingModule) {
        module = gitLabFetchingModule;
	}

	public void initialize() {
        AnchorPane.setLeftAnchor(tokenBox, 12.0);
        AnchorPane.setTopAnchor(tokenBox, 12.0);
        AnchorPane.setBottomAnchor(tokenBox, 12.0);
        AnchorPane.setRightAnchor(tokenBox, 12.0);

        tokenField.setSkin(new PasswordSkin(tokenField));

        tokenField.textProperty().addListener((obj, oldText, newText) -> {
            module.setToken(newText);
        });

        tokenConfirmBtn.setOnAction(e -> {
            tokenConfirmBtn.getScene().getWindow().hide();
        });
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


}
