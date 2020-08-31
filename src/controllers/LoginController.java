package controllers;

import animatefx.animation.BounceIn;
import animatefx.animation.ZoomInDown;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.StringLengthValidator;
import com.jfoenix.validation.base.ValidatorBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.UserModel;
import org.json.JSONObject;
import utils.EmailValidator;
import utils.PasswordValidator;

public class LoginController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane main;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton cancel;
    @FXML
    private Text errorText;
    @FXML
    private JFXSpinner spinner;

    @FXML
    private Text forget;

    @FXML
    private FontAwesomeIconView back;


    @FXML
    private Text backText;
    @FXML
    void cancel(ActionEvent event) throws IOException {
        System.exit(0);
    }

    @FXML
    void onBack(MouseEvent event) {

    }

    @FXML
    void onForget(MouseEvent event) {

    }

    @FXML
    void onLogin(ActionEvent event) {
        Task task =new Task() {
            @Override
            protected Object call() throws Exception {
                login.setDisable(true);
                spinner.setVisible(true);
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("email",username.getText());
                jsonObject.put("password",password.getText());
                UserModel userModel =new UserModel();
                JSONObject result =userModel.loginUser(jsonObject.toString());

                if(result.has("error")){

                    errorText.setVisible(true);
                }
                else{

                    userModel.saveToken();
                    Platform.runLater(() ->{
                        try {
                            Main.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/adminpannel.fxml"))));
                            Main.stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    });

                    //      Main.load(Main.getScreen("admin"));

                }
                login.setDisable(false);
                spinner.setVisible(false);
                return null;
            }
        };
        new Thread(task).start();


    }
    boolean emailCheck =false;
    boolean passwordCheck =false;
    @FXML
    void initialize() {


        ValidatorBase emailValidator =new EmailValidator();
        username.setValidators(emailValidator);
        ValidatorBase passwordValidator =new PasswordValidator();
        password.setValidators(passwordValidator);
        username.textProperty().addListener((e) ->{
            errorText.setVisible(false);
            if(username.validate()) {
                emailCheck =true;
            }
            else{
                emailCheck =false;
            }
            if(emailCheck && passwordCheck) {

                login.setDisable(false);
            }
            else{
                login.setDisable(true);
            }

        });
        password.textProperty().addListener((e) ->{
            errorText.setVisible(false);
            if(password.validate()){
                passwordCheck =true;
            }
            else{
                passwordCheck =false;
            }
            if(emailCheck && passwordCheck) {
                login.setDisable(false);
            }
            else{
                login.setDisable(true);
            }

        });

    }
}
