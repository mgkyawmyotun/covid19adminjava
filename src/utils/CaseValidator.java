package utils;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class CaseValidator extends ValidatorBase {
    public  CaseValidator(){
        super("Must Be Number");
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
            Integer anInt=  Integer.parseInt(text);
            this.hasErrors.set(false);
        }
        catch (Exception e){
            this.hasErrors.set(true);
        }

    }
}
