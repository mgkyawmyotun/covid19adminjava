package utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class PasswordValidator extends ValidatorBase {
  public  PasswordValidator(){
        super("Enter At Least 6 Character");
    }
    @Override
    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }

    }
    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        String text = textField.getText();
        this.hasErrors.set(true);
        if (text.length() > 5) {
            this.hasErrors.set(false);
        }

    }
}
