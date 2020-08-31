package utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class EmptyValidator  extends ValidatorBase {
    public  EmptyValidator(){
        super("Cannot Be Empty");
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
        if (!text.trim().isEmpty()) {
            this.hasErrors.set(false);
        }

    }
}
