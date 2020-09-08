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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.StateModel;
import models.TownModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;
import utils.NumberValidator;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TownController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<Town> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;
    @FXML
    private Text titleText;
    @FXML
    private JFXTextField edittown;
    private JFXComboBox<State> editstate;
    private Pane editPane;
    private Pane addPane;
    Town seletedTown;
    @FXML

    private JFXTextField addtown;
    private JFXSpinner addSpinner;
    private JFXComboBox<State> addstate;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<Town> towns;
    private JFXDialog jfxDialog;
    private ObservableList<State> states;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;

    private JFXTextField addLat, addLong;

    private JFXTextField editLat, editLong;
    private boolean latBool, longBool, townBool = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/townEdit.fxml"));

            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            edittown = (JFXTextField) editPane.getChildren().get(1);
            editstate = (JFXComboBox) editPane.getChildren().get(3);
            editLat = (JFXTextField) editPane.getChildren().get(4);
            editLong = (JFXTextField) editPane.getChildren().get(5);
            editSpinner = (JFXSpinner) editPane.getChildren().get(6);
            editErrorText = (Text) editPane.getChildren().get(7);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator = new EmptyValidator();
            edittown.setValidators(emptyValidator);
            edittown.textProperty().addListener(c -> {
                if (edittown.validate()) {
                    townBool = true;
                    if (townBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            NumberValidator numberValidator = new NumberValidator();
            editLat.setValidators(numberValidator);
            editLong.setValidators(numberValidator);
            editLat.textProperty().addListener(c -> {
                if (editLat.validate()) {
                    latBool = true;
                    if (townBool && latBool && longBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            editLong.textProperty().addListener(c -> {
                if (editLong.validate()) {
                    longBool = true;
                    if (townBool && latBool && longBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/addTown.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);
            addtown = (JFXTextField) addPane.getChildren().get(1);
            addstate = (JFXComboBox) addPane.getChildren().get(3);
            addLat = (JFXTextField) addPane.getChildren().get(4);
            addLong = (JFXTextField) addPane.getChildren().get(5);
            addSpinner = (JFXSpinner) addPane.getChildren().get(6);
            addErrorText = (Text) addPane.getChildren().get(7);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            addtown.setValidators(emptyValidator);
            addtown.textProperty().addListener(c -> {
                if (addtown.validate()) {
                    townBool = true;
                    if (townBool && latBool && longBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            NumberValidator numberValidator = new NumberValidator();
            addLat.setValidators(numberValidator);
            addLong.setValidators(numberValidator);
            addLat.textProperty().addListener(c -> {
                if (addLat.validate()) {
                    latBool = true;
                    if (townBool && latBool && longBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addLong.textProperty().addListener(c -> {
                if (addLong.validate()) {
                    longBool = true;
                    if (townBool && latBool && longBool) {
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

        JFXTreeTableColumn<Town, String> townName = new JFXTreeTableColumn<>("Town Name");
        townName.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<Town, String> stateCol = new JFXTreeTableColumn<>("State Name");
        stateCol.setCellValueFactory(param -> param.getValue().getValue().state_name);
        JFXTreeTableColumn<Town, String> lonCol = new JFXTreeTableColumn<>("Latitude");
        lonCol.setCellValueFactory(param -> param.getValue().getValue().lon);

        JFXTreeTableColumn<Town, String> latCol = new JFXTreeTableColumn<>("Longitude");
        latCol.setCellValueFactory(param -> param.getValue().getValue().lat);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                towns = loadTowns();
                states = loadSates();
                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<Town> root = new RecursiveTreeItem<Town>(towns, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(townName, stateCol, lonCol, latCol);
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

    private ObservableList<Town> loadTowns() {
        ObservableList<Town> observableList = FXCollections.observableArrayList();

        TownModel townModel = new TownModel();
        townModel.refreshTowns();
        JSONArray jsonTown = townModel.getTowns();
        for (int i = 0; i < jsonTown.length(); i++) {
            JSONObject jsonObject = jsonTown.getJSONObject(i);
            if (jsonObject.isNull("state")) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "No State (Deleted)");
                jsonObject1.put("_id", "");
                jsonObject.put("state", jsonObject1);
            }
            JSONArray location = jsonObject.getJSONArray("location");
            System.out.println(location);

            JSONObject stateObject = jsonObject.getJSONObject("state");
            observableList.add(new Town(jsonObject.getString("name"), location.get(0) + "", location.get(1) + "", stateObject.getString("name"), stateObject.getString("_id"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        townBool = false;
        longBool = false;
        latBool = false;
        addstate.setItems(states);
        addstate.getSelectionModel().select(0);
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

                seletedTown = (Town) ((TreeItem) ob.get(selectedIndex)).getValue();
                TownModel townModel = new TownModel();
                townModel.deleteTown(seletedTown._id);
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
        townBool = false;
        longBool = false;
        latBool = false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        seletedTown = (Town) ((TreeItem) ob.get(selectedIndex)).getValue();
        edittown.setText(seletedTown.name.getValue());
        State state = states.stream().filter((x) -> x.name.equals(seletedTown.state_name.getValue())).findFirst().orElse(states.get(0));

        editstate.setItems(states);

        editstate.getSelectionModel().select(state);
        editLong.setText(seletedTown.lat.getValue());
        editLat.setText(seletedTown.lon.getValue());
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit State"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class Town extends RecursiveTreeObject<Town> {

        StringProperty name;
        StringProperty state_name;
        StringProperty lat;
        StringProperty lon;
        String _id;
        String state_id;

        public Town(String name, String lat, String lon, String state_name, String state_id, String _id) {
            this.name = new SimpleStringProperty(name);
            this.state_name = new SimpleStringProperty(state_name);
            this.state_id = state_id;
            this.lon = new SimpleStringProperty(lon);
            this.lat = new SimpleStringProperty(lat);
            this._id = _id;
        }

    }

    private void onAdd(MouseEvent e) {

        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        addSpinner.setVisible(true);
        Task<Void> addRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject addTownObject = new JSONObject();
                addTownObject.put("name", addtown.getText());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(Double.parseDouble(addLat.getText()));
                jsonArray.put(Double.parseDouble(addLong.getText()));
                addTownObject.put("location", jsonArray);
                addTownObject.put("state_id", addstate.getSelectionModel().getSelectedItem()._id);
                TownModel townModel = new TownModel();

                townModel.addTown(addTownObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addtown.setText("");
                    addLat.setText("");
                    addLong.setText("");

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
        townBool = false;
        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);

        Task<Void> editRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editTownObject = new JSONObject();
                editTownObject.put("name", edittown.getText());

                editTownObject.put("_id", seletedTown._id);
                JSONArray jsonArray = new JSONArray();

                jsonArray.put(Double.parseDouble(editLong.getText()));
                jsonArray.put(Double.parseDouble(editLat.getText()));
                editTownObject.put("location", jsonArray);

                editTownObject.put("state", editstate.getSelectionModel().getSelectedItem()._id);
                TownModel townModel = new TownModel();

                townModel.editTown(editTownObject.toString(), seletedTown._id);

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

    private ObservableList<State> loadSates() {
        ObservableList<State> observableList = FXCollections.observableArrayList();
        StateModel stateModel = new StateModel();
        stateModel.refreshStates();
        JSONArray jsonStates = stateModel.getStates();
        for (int i = 0; i < jsonStates.length(); i++) {
            JSONObject jsonObject = jsonStates.getJSONObject(i);
            JSONArray jsonArray = jsonObject.getJSONArray("location");
            observableList.add(new State(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    class State {
        String name;
        String _id;

        State(String name, String _id) {
            this.name = name;
            this._id = _id;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }
}
