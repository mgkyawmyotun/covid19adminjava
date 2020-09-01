package utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class DateValidator extends ValidatorBase {
    public  DateValidator(){
        super("Enter a valid Date");
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
        if (org.apache.commons.validator.routines.DateValidator.getInstance().isValid(text)) {
            this.hasErrors.set(false);
        }

    }
}
