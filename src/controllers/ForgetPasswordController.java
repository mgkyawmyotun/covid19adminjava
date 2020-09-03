package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import models.UserModel;
import org.json.JSONObject;
import utils.EmailValidator;
import utils.EmptyValidator;
import utils.PasswordValidator;

import java.io.IOException;

public class ForgetPasswordController {

    @FXML
    private JFXTextField email;

    @FXML
    private JFXButton request;

    @FXML
    private JFXTextField token;

    @FXML
    private JFXButton enter;

    @FXML
    private Text error;

    @FXML
    private JFXTextField newPassword;

    @FXML
    void onRequest(ActionEvent event) {
        spinner.setVisible(true);
        error.setVisible(false);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject emailObject = new JSONObject();
                emailObject.put("email", email.getText());
                UserModel userModel = new UserModel();
                JSONObject result = userModel.forgetPassword(emailObject.toString());
                if (!result.isNull("error")) {
                    spinner.setVisible(false);
                    error.setText(result.getString("error"));
                    error.setVisible(true);
                } else {
                    spinner.setVisible(false);
                    token.setVisible(true);
                    newPassword.setVisible(true);
                    enter.setVisible(true);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private JFXSpinner spinner;

    @FXML
    void onToken(ActionEvent event) {
        spinner.setVisible(true);
        error.setVisible(false);
            Task<Void> task =new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    JSONObject resetTokenObject =new JSONObject();
                    resetTokenObject.put("token",token.getText());
                    resetTokenObject.put("new_password",newPassword.getText());
                    UserModel userModel =new UserModel();
                    JSONObject resultObject= userModel.resetPassword(resetTokenObject.toString());
                    if(!resultObject.isNull("error")){
                        spinner.setVisible(false);
                        error.setText(resultObject.getString("error"));
                        error.setVisible(true);
                    }
                    else{
                        spinner.setVisible(false);
                        Platform.runLater(() ->{
                            try {
                                Main.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/login.fxml"))));
                                Main.stage.centerOnScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        });
                    }

                    return null;
                }
            };
            new Thread(task).start();
    }

    @FXML
    void initialize() {
        request.setDisable(true);
        enter.setDisable(true);
        EmailValidator emailValidator = new EmailValidator();
        EmptyValidator emptyValidator = new EmptyValidator();
        email.setValidators(emptyValidator, emailValidator);
        email.textProperty().addListener(c -> {
            error.setVisible(false);

            if (email.validate()) {
                request.setDisable(false);
            } else {
                request.setDisable(true);
            }
        });
        token.textProperty().addListener(c ->{
            error.setVisible(false);
        });
        PasswordValidator passwordValidator =new PasswordValidator();
        newPassword.setValidators(passwordValidator,emptyValidator);
        newPassword.textProperty().addListener(c ->{
            error.setVisible(false);
            if(newPassword.validate()){
                enter.setDisable(false);
            } else {
                enter.setDisable(true);
            }
        });

    }

}
