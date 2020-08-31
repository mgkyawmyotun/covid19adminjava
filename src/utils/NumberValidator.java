package utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

import java.util.regex.Pattern;

public class NumberValidator extends ValidatorBase {
    public  NumberValidator(){
        super("Enter valid long/lat");
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
        try{
          Double aDouble=  Double.parseDouble(text);
          this.hasErrors.set(false);
        }
        catch (Exception e){
            this.hasErrors.set(true);
        }

    }
}
