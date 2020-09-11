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
import models.StateModel;
import models.DistrictModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;
import utils.NumberValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DistrictController implements Initializable {
    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<District> treeView;
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
    District seletedDistrict;
    @FXML

    private JFXTextField addtown;
    private JFXSpinner addSpinner;
    private JFXComboBox<State> addstate;
    private Text addErrorText;
    @FXML
    private JFXTextField searchInput;
    @FXML
    private JFXButton deleteButton;
    ObservableList<District> towns;
    private JFXDialog jfxDialog;
    private ObservableList<State> states;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;

    private boolean districtBool = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        searchInput.textProperty().addListener(c ->{
            treeView.setPredicate(g ->{
                District district =g.getValue();
                return  district.name.getValue().toLowerCase().contains(searchInput.getText().toLowerCase())||district.state_name.getValue().toLowerCase().contains(searchInput.getText().toLowerCase());
            });
        });
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/editDistrict.fxml"));

            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            edittown = (JFXTextField) editPane.getChildren().get(1);
            editstate = (JFXComboBox) editPane.getChildren().get(3);

            editSpinner = (JFXSpinner) editPane.getChildren().get(4);
            editErrorText = (Text) editPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator = new EmptyValidator();
            edittown.setValidators(emptyValidator);
            edittown.textProperty().addListener(c -> {
                if (edittown.validate()) {
                    districtBool = true;
                    if (districtBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/addDistrict.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);
            addtown = (JFXTextField) addPane.getChildren().get(1);
            addstate = (JFXComboBox) addPane.getChildren().get(3);

            addSpinner = (JFXSpinner) addPane.getChildren().get(4);
            addErrorText = (Text) addPane.getChildren().get(5);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            addtown.setValidators(emptyValidator);
            addtown.textProperty().addListener(c -> {
                if (addtown.validate()) {
                    districtBool = true;
                    if (districtBool ) {
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

        JFXTreeTableColumn<District, String> townName = new JFXTreeTableColumn<>("District Name");
        townName.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<District, String> stateCol = new JFXTreeTableColumn<>("State Name");
        stateCol.setCellValueFactory(param -> param.getValue().getValue().state_name);

        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                towns = loadDistricts();
                states = loadSates();
                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<District> root = new RecursiveTreeItem<District>(towns, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(townName, stateCol);
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

    private ObservableList<District> loadDistricts() {
        ObservableList<District> observableList = FXCollections.observableArrayList();

        DistrictModel townModel = new DistrictModel();
        townModel.refreshDistricts();
        JSONArray jsonDistrict = townModel.getDistricts();
        for (int i = 0; i < jsonDistrict.length(); i++) {
            JSONObject jsonObject = jsonDistrict.getJSONObject(i);
            if (jsonObject.isNull("state")) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "No State (Deleted)");
                jsonObject1.put("_id", "");
                jsonObject.put("state", jsonObject1);
            }
            JSONObject stateObject = jsonObject.getJSONObject("state");
            observableList.add(new District(jsonObject.getString("name"), stateObject.getString("name"), stateObject.getString("_id"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        districtBool = false;

        addstate.setItems(states);
        addstate.getSelectionModel().select(0);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add District"));
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

                seletedDistrict = (District) ((TreeItem) ob.get(selectedIndex)).getValue();
                DistrictModel townModel = new DistrictModel();
                townModel.deleteDistrict(seletedDistrict._id);
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
        districtBool = false;

        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        seletedDistrict = (District) ((TreeItem) ob.get(selectedIndex)).getValue();
        edittown.setText(seletedDistrict.name.getValue());
        State state = states.stream().filter((x) -> x.name.equals(seletedDistrict.state_name.getValue())).findFirst().orElse(states.get(0));

        editstate.setItems(states);

        editstate.getSelectionModel().select(state);

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit District"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class District extends RecursiveTreeObject<District> {

        StringProperty name;
        StringProperty state_name;

        String _id;
        String state_id;

        public District(String name, String state_name, String state_id, String _id) {
            this.name = new SimpleStringProperty(name);
            this.state_name = new SimpleStringProperty(state_name);
            this.state_id = state_id;

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
                JSONObject addDistrictObject = new JSONObject();
                addDistrictObject.put("name", addtown.getText());

                addDistrictObject.put("state_id", addstate.getSelectionModel().getSelectedItem()._id);
                DistrictModel townModel = new DistrictModel();

                townModel.addDistrict(addDistrictObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addtown.setText("");

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
        districtBool = false;
        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);

        Task<Void> editRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editDistrictObject = new JSONObject();
                editDistrictObject.put("name", edittown.getText());

                editDistrictObject.put("_id", seletedDistrict._id);

                editDistrictObject.put("state", editstate.getSelectionModel().getSelectedItem()._id);
                DistrictModel townModel = new DistrictModel();

                townModel.editDistrict(editDistrictObject.toString(), seletedDistrict._id);

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
