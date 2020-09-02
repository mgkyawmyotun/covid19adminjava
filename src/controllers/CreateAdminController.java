package controllers;

import animatefx.animation.ZoomInDown;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.StringLengthValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import models.UserModel;
import models.TownModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmailValidator;
import utils.EmptyValidator;
import utils.PasswordValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAdminController implements Initializable {
    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<User> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;


    @FXML
    private Text titleText;

    private Pane addPane;


    ObservableList<User> users;
    private JFXDialog jfxDialog;

    private JFXDialogLayout jfxDialogLayout;
    private JFXTextField username;
    private JFXTextField email;
    private JFXPasswordField passwordField;
    private JFXSpinner spinner;
    private Text errorText;
    private boolean usernameBool;
    private boolean emailBool;
    private boolean passwordBool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            addPane = FXMLLoader.load(getClass().getResource("/views/components/createUser.fxml"));
            username = (JFXTextField) addPane.getChildren().get(0);
            email = (JFXTextField) addPane.getChildren().get(1);
            passwordField = (JFXPasswordField) addPane.getChildren().get(2);
            spinner = (JFXSpinner) addPane.getChildren().get(3);
            errorText = (Text) addPane.getChildren().get(4);
            HBox hBox1 = (HBox) addPane.getChildren().get(addPane.getChildren().size() - 1);
            hBox1.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton createButton = (JFXButton) hBox1.getChildren().get(1);
            createButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::createUser);
            createButton.setDisable(true);
            username.setValidators(new EmptyValidator());
            username.textProperty().addListener(c -> {
                errorText.setVisible(false);
                if (username.validate()) {
                    usernameBool = true;
                    if (usernameBool && emailBool && passwordBool) {
                        createButton.setDisable(false);
                    }
                } else {
                    createButton.setDisable(true);
                }
            });
            email.setValidators(new EmailValidator());
            email.textProperty().addListener(c -> {

                errorText.setVisible(false);
                if (email.validate()) {
                    emailBool = true;
                    if (usernameBool && emailBool && passwordBool) {
                        createButton.setDisable(false);
                    }
                } else {
                    createButton.setDisable(true);
                }
            });
            passwordField.setValidators(new EmptyValidator(), new PasswordValidator());
            passwordField.textProperty().addListener(c -> {
                errorText.setVisible(false);
                if (passwordField.validate()) {
                    passwordBool = true;
                    if (usernameBool && emailBool && passwordBool) {
                        createButton.setDisable(false);
                    }
                } else {
                    createButton.setDisable(true);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        loadTable();


    }

    private void loadTable() {
        treeView.setVisible(false);
        tableLoading.setVisible(true);

        JFXTreeTableColumn<User, String> userNameCol = new JFXTreeTableColumn<>("UserName");
        userNameCol.setCellValueFactory(param -> param.getValue().getValue().username);

        JFXTreeTableColumn<User, String> emailCol = new JFXTreeTableColumn<>("Email");
        emailCol.setCellValueFactory(param -> param.getValue().getValue().email);

        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                users = loadUsers();


                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<User> root = new RecursiveTreeItem<User>(users, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(userNameCol, emailCol);
                    treeView.setRoot(root);
                    treeView.setShowRoot(false);
                    tableLoading.setVisible(false);
                    treeView.setVisible(true);
                });
                return null;
            }
        };
        new Thread(tableRequest).start();

    }

    private ObservableList<User> loadUsers() {
        ObservableList<User> observableList = FXCollections.observableArrayList();

        UserModel userModel = new UserModel();
        JSONArray jsonUser = userModel.getAllUsers();
        System.out.println(jsonUser);
        for (int i = 0; i < jsonUser.length(); i++) {
            JSONObject jsonObject = jsonUser.getJSONObject(i);
            observableList.add(new User(jsonObject.getString("username"), jsonObject.getString("email")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        usernameBool = false;
        passwordBool = false;
        emailBool = false;
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add User"));
        jfxDialogLayout.setBody(addPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }

    private void createUser(MouseEvent e) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                spinner.setVisible(true);
                JSONObject userObject = new JSONObject();
                userObject.put("username", username.getText());
                userObject.put("email", email.getText());
                userObject.put("password", passwordField.getText());

                UserModel userModel = new UserModel();
                JSONObject jsonObject = userModel.createUser(userObject.toString());
                System.out.println(jsonObject);
                if (!jsonObject.isNull("error")) {
                    Platform.runLater(() -> {
                        errorText.setText(jsonObject.getString("error"));
                        errorText.setVisible(true);
                        spinner.setVisible(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        spinner.setVisible(false);
                        jfxDialog.close();
                        username.setText("");
                        passwordField.setText("");
                        email.setText("");
                    });
                    loadTable();
                }

                return null;
            }
        };
        new Thread(task
        ).start();

    }


    class User extends RecursiveTreeObject<User> {

        StringProperty username;
        StringProperty email;

        public User(String username, String email) {
            this.username = new SimpleStringProperty(username);
            this.email = new SimpleStringProperty(email);

        }

    }


    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }
}
