package controllers;

import com.jfoenix.controls.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import models.UserModel;
import org.json.JSONObject;

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

    private JFXDialogLayout jfxDialogLayout;

    @FXML
    void onChangePassword(ActionEvent event) {

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", editUsername.getText());
        jsonObject.put("email", editUsername.getText());
        userModel =new UserModel();
        userModel.changeUser(jsonObject.toString());

    }

    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }

    @FXML
    void initialize() throws IOException {
        editPane = FXMLLoader.load(getClass().getResource("/views/components/editUser.fxml"));
        HBox hBox = (HBox) editPane.getChildren().get(editPane.getChildren().size() - 1);
        hBox.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
        hBox.getChildren().get(1).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEditAccount);
        editUsername = (JFXTextField) editPane.getChildren().get(0);
        editEmail = (JFXTextField) editPane.getChildren().get(1);
        spinner.setVisible(true
        );
        userBorder.setVisible(false);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                userModel = new UserModel();
                JSONObject user = userModel.getUser();
                System.out.println(user);
                username.setText(user.getString("username"));
                email.setText(user.getString("email"));
                spinner.setVisible(false);
                userBorder.setVisible(true);
                return null;
            }
        };

        new Thread(task).start();

    }
}
