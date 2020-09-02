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
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;
import utils.NumberValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CaseStateController  implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<StateCase> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;
    @FXML
    private Text titleText;
    @FXML

    private JFXComboBox<State> editStateCase;
    private Pane editPane;
    private Pane addPane;
    StateCase seletedStateCase;
    @FXML


    private JFXSpinner addSpinner;
    private JFXComboBox<State> addState;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<StateCase> cases;
    private JFXDialog jfxDialog;
    private ObservableList<State> states;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;

    private JFXTextField addTotalDeath,addTotalCase;

    private JFXTextField editTotalCase, editTotalDeath;
    private boolean totalDeathBool, totalCaseBool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/stateCaseEdit.fxml"));
            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            editStateCase = (JFXComboBox) editPane.getChildren().get(1);
            editTotalCase = (JFXTextField) editPane.getChildren().get(2);
            editTotalDeath = (JFXTextField) editPane.getChildren().get(3);
            editSpinner = (JFXSpinner) editPane.getChildren().get(4);
            editErrorText = (Text) editPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator = new EmptyValidator();

            NumberValidator numberValidator = new NumberValidator();
            editTotalCase.setValidators(numberValidator);
            editTotalDeath.setValidators(numberValidator);
            editTotalCase.textProperty().addListener(c -> {
                if (editTotalCase.validate()) {
                    totalDeathBool = true;
                    if ( totalDeathBool && totalCaseBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            editTotalDeath.textProperty().addListener(c -> {
                if (editTotalDeath.validate()) {
                    totalCaseBool = true;
                    if ( totalDeathBool && totalCaseBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/stateCaseAdd.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);

            addState = (JFXComboBox) addPane.getChildren().get(1);
            addTotalCase = (JFXTextField) addPane.getChildren().get(2);
            addTotalDeath = (JFXTextField) addPane.getChildren().get(3);
            addSpinner = (JFXSpinner) addPane.getChildren().get(4);
            addErrorText = (Text) addPane.getChildren().get(5);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            NumberValidator numberValidator = new NumberValidator();
            addTotalDeath.setValidators(numberValidator);
            addTotalDeath.setValidators(numberValidator);
            addTotalDeath.textProperty().addListener(c -> {
                if (addTotalDeath.validate()) {
                    totalDeathBool = true;
                    if ( totalDeathBool && totalCaseBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addTotalDeath.textProperty().addListener(c -> {
                if (addTotalDeath.validate()) {
                    totalCaseBool = true;
                    if ( totalDeathBool && totalCaseBool) {
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

        JFXTreeTableColumn<StateCase, String> stateCol = new JFXTreeTableColumn<>("State Name");
        stateCol.setCellValueFactory(param -> param.getValue().getValue().state_name);
        JFXTreeTableColumn<StateCase, String> totalCaseCol = new JFXTreeTableColumn<>("Total Case");
        totalCaseCol.setCellValueFactory(param -> param.getValue().getValue().totalCase);

        JFXTreeTableColumn<StateCase, String> totalDeathCol = new JFXTreeTableColumn<>("Total Death");
        totalDeathCol.setCellValueFactory(param -> param.getValue().getValue().totalDeath);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                cases = loadCases();
                states = loadSates();
                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<StateCase> root = new RecursiveTreeItem<StateCase>(cases, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(stateCol, totalCaseCol, totalDeathCol);
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

    private ObservableList<StateCase> loadCases() {
        ObservableList<StateCase> observableList = FXCollections.observableArrayList();
        StateModel stateModel = new StateModel();
        JSONArray jsonCases = stateModel.getStateCases();
        for (int i = 0; i < jsonCases.length(); i++) {
            JSONObject jsonObject = jsonCases.getJSONObject(i);
            if (jsonObject.isNull("state")) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "No State (Deleted)");
                jsonObject1.put("_id", "");
                jsonObject.put("state", jsonObject1);
            }

            JSONObject stateObject = jsonObject.getJSONObject("state");
            System.out.println(stateObject);
            System.out.println(jsonObject);
            observableList.add(new StateCase(
                    stateObject.getString("name"),
                    stateObject.getString("_id"),
                    jsonObject.getString("_id"),
                    jsonObject.get("totalCase")+"",
                    jsonObject.get("totalDeath")+""));
            System.out.println(observableList);
        }

        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {

        totalCaseBool = false;
        totalDeathBool = false;
        addState.setItems(states);
        addState.getSelectionModel().select(0);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add State Case"));
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

                seletedStateCase = (StateCase) ((TreeItem) ob.get(selectedIndex)).getValue();
                StateModel stateModel = new StateModel();
                stateModel.deleteStateCase(seletedStateCase._id);
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

        totalCaseBool = false;
        totalDeathBool = false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        seletedStateCase = (StateCase) ((TreeItem) ob.get(selectedIndex)).getValue();
        State state = states.stream().filter((x) -> x.name.equals(seletedStateCase.state_name.getValue())).findFirst().orElse(states.get(0));

        editStateCase.setItems(states);

        editStateCase.getSelectionModel().select(state);
        editTotalDeath.setText(seletedStateCase.totalDeath.getValue());
        editTotalCase.setText(seletedStateCase.totalCase.getValue());
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit State"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class StateCase extends RecursiveTreeObject<StateCase> {


        StringProperty state_name;
        StringProperty totalCase;
        StringProperty totalDeath;
        String _id;
        String state_id;

        public StateCase( String state_name, String state_id,String _id,String totalCase,String totalDeath) {

            this.state_name = new SimpleStringProperty(state_name);
            this.state_id = state_id;
            this.totalCase = new SimpleStringProperty(totalCase);
            this.totalDeath = new SimpleStringProperty(totalDeath);
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
                JSONObject addStateCaseObject = new JSONObject();
                addStateCaseObject.put("totalDeath",Integer.parseInt(addTotalDeath.getText()));
                addStateCaseObject.put("totalCase",Integer.parseInt(addTotalCase.getText()));

                addStateCaseObject.put("state", addState.getSelectionModel().getSelectedItem()._id);
                StateModel stateModel = new StateModel();

                stateModel.addStateCase(addStateCaseObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addTotalDeath.setText("");
                    addTotalCase.setText("");

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

        JFXButton jfxButton = (JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);

        Task<Void> editRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editStateCaseObject = new JSONObject();
                editStateCaseObject.put("totalDeath",Integer.parseInt(editTotalDeath.getText()));
                editStateCaseObject.put("totalCase",Integer.parseInt(editTotalCase.getText()));
                editStateCaseObject.put("state", editStateCase.getSelectionModel().getSelectedItem()._id);
                System.out.println(editStateCaseObject);
                StateModel stateModel = new StateModel();
                stateModel.editStateCase(editStateCaseObject.toString(), seletedStateCase._id);

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
