package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import models.UserModel;
import org.json.JSONObject;
import utils.EmailValidator;
import utils.EmptyValidator;
import utils.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane main;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Text username;

    @FXML
    private Text email;

    @FXML
    private JFXButton edit;

    @FXML
    private JFXButton changePassowrd;

    @FXML
    private AnchorPane topPane;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private BorderPane userBorder;
    private Pane editPane;
    @FXML
    private Text titleText;
    private UserModel userModel;
    private JFXTextField editUsername;
    private JFXTextField editEmail;
    private JFXDialog jfxDialog;
    private JFXPasswordField oldPassword;
    private JFXPasswordField confirmPassword;
    private JFXPasswordField newPassword;
    private JFXDialogLayout jfxDialogLayout;
    private JFXSpinner editSpinner;
    private Text editErrorText;
    private Text passErrorText;
    private JFXSpinner passSpinner;
    private boolean UserNameBool = false;
    private boolean EmailBool = false;
    private Pane changePane;
    private FontAwesomeIconView eyeIcon;
    private Tooltip toolTip;
    private Tooltip toolTipOld;
    private Tooltip toolTipConfirm;
    private boolean oldBool = false;
    private boolean newBool = false;
    private boolean confBool = false;

    @FXML
    void onChangePassword(ActionEvent event) {

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Change Password"));
        jfxDialogLayout.setBody(changePane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }

    @FXML
    void onEdit(ActionEvent event) {
        editUsername.setText(username.getText());
        editEmail.setText(email.getText());
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit Account"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }

    private void onEditAccount(MouseEvent e) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                editSpinner.setVisible(true);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", editUsername.getText());
                jsonObject.put("email", editEmail.getText());
                userModel = new UserModel();
                JSONObject jsonObject1 = userModel.changeUser(jsonObject.toString());

                if (jsonObject1.has("error")) {
                    editErrorText.setText(jsonObject1.getString("error"));
                    editErrorText.setVisible(true);
                    editSpinner.setVisible(false);
                } else {
                    Platform.runLater(() -> {
                        JSONObject user = userModel.getUser();
                        username.setText(user.getString("username"));
                        email.setText(user.getString("email"));
                        editSpinner.setVisible(false);

                        jfxDialog.close();

                    });
                }
                return null;
            }
        };
        new Thread(task).start();

    }

    private void onCancel(MouseEvent e) {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
        jfxDialog.close();
    }

    @FXML
    void initialize() throws IOException {
        editPane = FXMLLoader.load(getClass().getResource("/views/components/editUser.fxml"));
        HBox hBox = (HBox) editPane.getChildren().get(editPane.getChildren().size() - 1);
        hBox.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
        editUsername = (JFXTextField) editPane.getChildren().get(0);
        editEmail = (JFXTextField) editPane.getChildren().get(1);
        editSpinner = (JFXSpinner) editPane.getChildren().get(2);
        editErrorText = (Text) editPane.getChildren().get(3);
        JFXButton editButton = (JFXButton) hBox.getChildren().get(1);
        editButton.setDisable(true);
        editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEditAccount);
        editUsername.setValidators(new EmptyValidator());

        editUsername.textProperty().addListener(c -> {
            if (editUsername.validate()) {
                UserNameBool = true;
                if (UserNameBool && EmailBool) {
                    editButton.setDisable(false);
                }
            } else {
                editButton.setDisable(true);
            }
            editErrorText.setVisible(false);
        });
        editEmail.setValidators(new EmailValidator());
        editEmail.textProperty().addListener(c -> {
            if (editEmail.validate()) {
                EmailBool = true;
                if (UserNameBool && EmailBool) {
                    editButton.setDisable(false);
                }
            } else {
                editButton.setDisable(true);
            }
            editErrorText.setVisible(false);
        });
        spinner.setVisible(true
        );
        userBorder.setVisible(false);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                userModel = new UserModel();
                JSONObject user = userModel.getUser();

                username.setText(user.getString("username"));
                email.setText(user.getString("email"));
                spinner.setVisible(false);
                userBorder.setVisible(true);
                return null;
            }
        };

        new Thread(task).start();

        changePane = FXMLLoader.load(getClass().getResource("/views/components/changePassword.fxml"));
        HBox hBox1 = (HBox) changePane.getChildren().get(changePane.getChildren().size() - 1);
        hBox1.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
        oldPassword = (JFXPasswordField) changePane.getChildren().get(0);
        newPassword = (JFXPasswordField) changePane.getChildren().get(1);
        confirmPassword = (JFXPasswordField) changePane.getChildren().get(2);
        eyeIcon = (FontAwesomeIconView) changePane.getChildren().get(3);
        passSpinner = (JFXSpinner) changePane.getChildren().get(4);
        passErrorText = (Text) changePane.getChildren().get(5);
        JFXButton addButton = (JFXButton) hBox1.getChildren().get(1);
        addButton.setDisable(true);
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAdd);
        oldPassword.setValidators(new EmptyValidator());
        oldPassword.textProperty().addListener(c -> {
            passErrorText.setVisible(false);
            if (oldPassword.validate()) {
                oldBool = true;
                if (oldBool && confBool && newBool) {
                    addButton.setDisable(false);
                }
            } else {
                addButton.setDisable(true);
            }
        });
        newPassword.setValidators(new EmptyValidator());
        newPassword.textProperty().addListener(c -> {
            passErrorText.setVisible(false);
            if (newPassword.validate()) {
                newBool = true;
                if (oldBool && confBool && newBool) {
                    addButton.setDisable(false);
                }
            } else {
                addButton.setDisable(true);
            }
        });
        confirmPassword.setValidators(new EmptyValidator());
        confirmPassword.textProperty().addListener(c -> {
            passErrorText.setVisible(false);
            if (confirmPassword.validate()) {
                confBool = true;
                if (oldBool && confBool && confBool) {
                    addButton.setDisable(false);
                }


            } else {
                addButton.setDisable(true);
            }
        });


        eyeIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, this::eyeIconPress);
        eyeIcon.addEventHandler(MouseEvent.MOUSE_RELEASED, this::eyeIconRelease);


    }

    private void onAdd(MouseEvent e) {
        passSpinner.setVisible(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject passwordChangeObject = new JSONObject();
                passwordChangeObject.put("old_password", oldPassword.getText());
                passwordChangeObject.put("new_password", newPassword.getText());
                passwordChangeObject.put("confirm_password", confirmPassword.getText());

                userModel = new UserModel();
                JSONObject jsonObject = userModel.changePassword(passwordChangeObject.toString());

                if (!jsonObject.isNull("error")) {
                        Platform.runLater(() ->{
                            passErrorText.setText(jsonObject.getString("error"));
                            passErrorText.setVisible(true);
                            passSpinner.setVisible(false);
                        });
                }
                else{
                    Platform.runLater(() ->{
                        try {
                            Helper.deleteToken();
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

    private void eyeIconPress(MouseEvent e) {
        eyeIcon.setIcon(FontAwesomeIcon.EYE);
        toolTip = new Tooltip();
        toolTip.setAutoHide(false);
        toolTip.setMinWidth(50);
        toolTipOld = new Tooltip();
        toolTipOld.setAutoHide(false);
        toolTipOld.setMinWidth(50);
        toolTipConfirm = new Tooltip();
        toolTipConfirm.setAutoHide(false);
        toolTipConfirm.setMinWidth(50);
        toolTip.setText(newPassword.getText());
        Point2D p = newPassword.localToScene(newPassword.getBoundsInLocal().getMaxX(), newPassword.getBoundsInLocal().getMaxY());
        newPassword.setTooltip(toolTip);
        toolTip.show(newPassword,
                p.getX() + Main.stage.getScene().getX() + Main.stage.getX(),
                p.getY() + Main.stage.getScene().getY() + Main.stage.getY());


        toolTipOld.setText(oldPassword.getText());
        Point2D p1 = oldPassword.localToScene(oldPassword.getBoundsInLocal().getMaxX(), oldPassword.getBoundsInLocal().getMaxY());
        oldPassword.setTooltip(toolTipOld);
        toolTipOld.show(oldPassword,
                p1.getX() + Main.stage.getScene().getX() + Main.stage.getX(),
                p1.getY() + Main.stage.getScene().getY() + Main.stage.getY());


        toolTipConfirm.setText(confirmPassword.getText());
        Point2D p2 = confirmPassword.localToScene(confirmPassword.getBoundsInLocal().getMaxX(), confirmPassword.getBoundsInLocal().getMaxY());
        confirmPassword.setTooltip(toolTipConfirm);
        toolTipConfirm.show(confirmPassword,
                p2.getX() + Main.stage.getScene().getX() + Main.stage.getX(),
                p2.getY() + Main.stage.getScene().getY() + Main.stage.getY());

    }

    private void eyeIconRelease(MouseEvent e) {
        eyeIcon.setIcon(FontAwesomeIcon.EYE_SLASH);
        toolTip.hide();
        toolTip.setText("");
        toolTipConfirm.hide();
        toolTipConfirm.setText("");
        toolTipOld.hide();
        toolTipOld.setText("");
    }

}
