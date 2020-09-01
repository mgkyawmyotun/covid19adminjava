package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import models.StateModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;
import utils.NumberValidator;

public class StateViewController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<State> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;

    @FXML
    private JFXTextField state;
    private Pane editPane;
    private Pane addPane;
    State selectedState;
    @FXML
    private JFXTextField latitude;
    private JFXTextField addlatitude;
    private JFXTextField addlongitude;
    private JFXSpinner addSpinner;
    private JFXTextField addstate;
    private Text addErrorText;
    @FXML
    private JFXTextField longitude;
    @FXML
    private JFXButton deleteButton;
    ObservableList<State> states;
    private JFXDialog jfxDialog;

    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;
    boolean stateBool = false;
    boolean latitudeBool = false;
    boolean longtitudeBool = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/stateEdit.fxml"));

            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            state = (JFXTextField) editPane.getChildren().get(1);
            latitude = (JFXTextField) editPane.getChildren().get(3);
            longitude = (JFXTextField) editPane.getChildren().get(5);
            editSpinner = (JFXSpinner) editPane.getChildren().get(6);
            editErrorText = (Text) editPane.getChildren().get(7);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            editButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            state.setValidators(emptyValidator);
            state.textProperty().addListener(c -> {
                if (state.validate()) {
                    stateBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        editButton.setDisable(false);
                    }
                } else {
                    System.out.println("Error");
                    editButton.setDisable(true);
                }
            });
            NumberValidator numberValidator = new NumberValidator();
            latitude.setValidators(numberValidator);
            longitude.setValidators(numberValidator);
            latitude.textProperty().addListener(c -> {
                if (latitude.validate()) {
                    latitudeBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            longitude.textProperty().addListener(c -> {
                if (longitude.validate()) {
                    longtitudeBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });

            editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEdit);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addPane = FXMLLoader.load(getClass().getResource("/views/components/stateAdd.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);
            addstate = (JFXTextField) addPane.getChildren().get(1);
            addlatitude = (JFXTextField) addPane.getChildren().get(3);
            addlongitude = (JFXTextField) addPane.getChildren().get(5);
            addSpinner = (JFXSpinner) addPane.getChildren().get(6);
            addErrorText = (Text) addPane.getChildren().get(7);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();

            addstate.setValidators(emptyValidator);
            addstate.textProperty().addListener(c -> {
                if (addstate.validate()) {
                    stateBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        addButton.setDisable(false);
                    }
                } else {
                    System.out.println("Error");
                    addButton.setDisable(true);
                }
            });
            NumberValidator numberValidator = new NumberValidator();
            addlatitude.setValidators(numberValidator);
            addlongitude.setValidators(numberValidator);
            addlatitude.textProperty().addListener(c -> {
                if (addlatitude.validate()) {
                    latitudeBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addlongitude.textProperty().addListener(c -> {
                if (addlongitude.validate()) {
                    longtitudeBool = true;
                    if (stateBool && latitudeBool && longtitudeBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });

            addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAdd);

        } catch (IOException e) {
            e.printStackTrace();
        }
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            editButton.setDisable(false);
            deleteButton.setDisable(false);

        });

        loadTable();


    }

    private void loadTable() {
        treeView.setVisible(false);
        tableLoading.setVisible(true);
        JFXTreeTableColumn<State, String> deptName = new JFXTreeTableColumn<>("State Name");
        deptName.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<State, String> lonCol = new JFXTreeTableColumn<>("Latitude");
        lonCol.setCellValueFactory(param -> param.getValue().getValue().lon);

        JFXTreeTableColumn<State, String> nameCol = new JFXTreeTableColumn<>("Longitude");
        nameCol.setCellValueFactory(param -> param.getValue().getValue().lat);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                states = loadSates();
                Platform.runLater(() -> {
                    final TreeItem<StateViewController.State> root = new RecursiveTreeItem<StateViewController.State>(states, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(deptName, lonCol, nameCol);
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

    private ObservableList<State> loadSates() {
        ObservableList<State> observableList = FXCollections.observableArrayList();
        StateModel stateModel = new StateModel();
        stateModel.refreshStates();
        JSONArray jsonStates = stateModel.getStates();
        for (int i = 0; i < jsonStates.length(); i++) {
            JSONObject jsonObject = jsonStates.getJSONObject(i);
            JSONArray jsonArray = jsonObject.getJSONArray("location");
            observableList.add(new State(jsonObject.getString("name"), jsonArray.get(0) + "", jsonArray.get(1) + "", jsonObject.getString("_id")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        stateBool = false;
        longtitudeBool = false;
        latitudeBool = false;
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add State"));
        jfxDialogLayout.setBody(addPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }

    @FXML
    void onDelete(ActionEvent event) {
        JFXButton jfxButton = (JFXButton) event.getTarget();
        jfxButton.setDisable(true);
        Task<Void> deleteRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
                ObservableList ob = treeView.getRoot().getChildren();

                selectedState = (StateViewController.State) ((TreeItem) ob.get(selectedIndex)).getValue();
                StateModel stateModel = new StateModel();
                stateModel.deleteState(selectedState._id);
                loadTable();
                jfxButton.setDisable(false);
                return null;
            }


        };
        new Thread(deleteRequest).start();
        deleteRequest.setOnSucceeded((_a) -> {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });


    }

    @FXML
    void onEdit(ActionEvent event) {
        stateBool = false;
        longtitudeBool = false;
        latitudeBool = false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        selectedState = (State) ((TreeItem) ob.get(selectedIndex)).getValue();
        state.setText(selectedState.name.getValue());
        latitude.setText(selectedState.lat.getValue());
        longitude.setText(selectedState.lon.getValue());

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit State"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class State extends RecursiveTreeObject<State> {

        StringProperty name;
        StringProperty lat;
        StringProperty lon;
        String _id;

        public State(String name, String lat, String lon, String _id) {
            this.name = new SimpleStringProperty(name);
            this.lat = new SimpleStringProperty(lat);
            this.lon = new SimpleStringProperty(lon);
            this._id = _id;
        }

    }

    private void onAdd(MouseEvent e) {
        stateBool = false;
        longtitudeBool = false;
        latitudeBool = false;
        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        addSpinner.setVisible(true);
        Task<Void> addRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject addStateObject = new JSONObject();
                addStateObject.put("name", addstate.getText());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(Double.parseDouble(addlatitude.getText()));
                jsonArray.put(Double.parseDouble(addlongitude.getText()));
                addStateObject.put("location", jsonArray);
                StateModel stateModel = new StateModel();

                stateModel.addState(addStateObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();
                    addstate.setText("");
                    addlatitude.setText("");
                    addlongitude.setText("");
                });
                return null;
            }
        };
        new Thread(addRequest).start();
        addRequest.setOnSucceeded(event -> {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });
    }

    private void onEdit(MouseEvent e) {
        stateBool = false;
        longtitudeBool = false;
        latitudeBool = false;
        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);
        Task<Void> editRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editStateObject = new JSONObject();
                editStateObject.put("name", state.getText());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(Double.parseDouble(latitude.getText()));
                jsonArray.put(Double.parseDouble(longitude.getText()));
                editStateObject.put("location", jsonArray);
                StateModel stateModel = new StateModel();

                stateModel.editState(editStateObject.toString(), selectedState._id);

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    editSpinner.setVisible(false);
                    jfxDialog.close();

                });
                return null;
            }
        };

        new Thread(editRequest).start();
        editRequest.setOnSucceeded(event -> {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });


    }

    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }
}
