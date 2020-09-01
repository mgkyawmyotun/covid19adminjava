package controllers;

import animatefx.animation.ZoomInDown;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import models.UserModel;
import models.TownModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAdminController  implements Initializable {
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

    @FXML

    private JFXTextField addhospital;
    private JFXSpinner addSpinner;

    private Text addErrorText;


    ObservableList<User> users;
    private JFXDialog jfxDialog;

    private JFXDialogLayout jfxDialogLayout;

    private  boolean hospitalBool =false;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();


        loadTable();


    }

    private void loadTable() {
        treeView.setVisible(false);
        tableLoading.setVisible(true);

        JFXTreeTableColumn<User, String> userNameCol = new JFXTreeTableColumn<>("UserName");
        userNameCol.setCellValueFactory(param -> param.getValue().getValue().username);

        JFXTreeTableColumn<User, String> emailCol = new JFXTreeTableColumn<>("Email");
        emailCol.setCellValueFactory(param -> param.getValue().getValue().email);

        Task<Void> tableRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                users = loadUsers();


                addButton.setDisable(false);
                Platform.runLater(() ->{
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
        for (int i = 0; i < jsonUser.length(); i++) {
            JSONObject jsonObject = jsonUser.getJSONObject(i);
            observableList.add(new User(jsonObject.getString("username"),jsonObject.getString("email")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add Town"));
        jfxDialogLayout.setBody(addPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }



    class User extends RecursiveTreeObject<User> {

        StringProperty username;
        StringProperty email;

        public User(String username, String email) {
            this.username = new SimpleStringProperty(username);
            this.email = new SimpleStringProperty(email);

        }

    }
    private  void onAdd(MouseEvent e ){


    }


    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }
}
