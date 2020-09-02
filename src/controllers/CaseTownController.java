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
import models.TownModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.CaseValidator;
import utils.EmptyValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CaseTownController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<TownCase> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;
    @FXML
    private Text titleText;
    @FXML

    private JFXComboBox<Town> editTownCase;
    private Pane editPane;
    private Pane addPane;
    TownCase seletedTownCase;
    @FXML


    private JFXSpinner addSpinner;
    private JFXComboBox<Town> addTown;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<TownCase> cases;
    private JFXDialog jfxDialog;
    private ObservableList<Town> towns;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;

    private JFXTextField addTotalDeath, addTotalCase;

    private JFXTextField editTotalCase, editTotalDeath;
    private boolean totalDeathBool, totalCaseBool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/townCaseEdit.fxml"));
            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            editTownCase = (JFXComboBox) editPane.getChildren().get(1);
            editTotalCase = (JFXTextField) editPane.getChildren().get(2);
            editTotalDeath = (JFXTextField) editPane.getChildren().get(3);
            editSpinner = (JFXSpinner) editPane.getChildren().get(4);
            editErrorText = (Text) editPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator = new EmptyValidator();

            CaseValidator caseValidator = new CaseValidator();
            editTotalCase.setValidators(caseValidator, emptyValidator);
            editTotalDeath.setValidators(caseValidator, emptyValidator);
            editTotalCase.textProperty().addListener(c -> {
                if (editTotalCase.validate()) {
                    totalDeathBool = true;
                    if (totalDeathBool && totalCaseBool) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            editTotalDeath.textProperty().addListener(c -> {
                if (editTotalDeath.validate()) {
                    totalCaseBool = true;
                    if (totalDeathBool && totalCaseBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/townCaseAdd.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);

            addTown = (JFXComboBox) addPane.getChildren().get(1);
            addTotalCase = (JFXTextField) addPane.getChildren().get(2);
            addTotalDeath = (JFXTextField) addPane.getChildren().get(3);
            addSpinner = (JFXSpinner) addPane.getChildren().get(4);
            addErrorText = (Text) addPane.getChildren().get(5);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            CaseValidator caseValidator = new CaseValidator();
            addTotalDeath.setValidators(caseValidator, emptyValidator);
            addTotalCase.setValidators(caseValidator, emptyValidator);
            addTotalDeath.textProperty().addListener(c -> {
                if (addTotalDeath.validate()) {
                    totalDeathBool = true;
                    if (totalDeathBool && totalCaseBool) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addTotalCase.textProperty().addListener(c -> {
                if (addTotalCase.validate()) {
                    totalCaseBool = true;
                    if (totalDeathBool && totalCaseBool) {
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

        JFXTreeTableColumn<TownCase, String> townCol = new JFXTreeTableColumn<>("Town Name");
        townCol.setCellValueFactory(param -> param.getValue().getValue().town_name);
        JFXTreeTableColumn<TownCase, String> totalCaseCol = new JFXTreeTableColumn<>("Total Case");
        totalCaseCol.setCellValueFactory(param -> param.getValue().getValue().totalCase);

        JFXTreeTableColumn<TownCase, String> totalDeathCol = new JFXTreeTableColumn<>("Total Death");
        totalDeathCol.setCellValueFactory(param -> param.getValue().getValue().totalDeath);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                cases = loadCases();
                towns = loadTowns();
                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<TownCase> root = new RecursiveTreeItem<TownCase>(cases, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(townCol, totalCaseCol, totalDeathCol);
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

    private ObservableList<TownCase> loadCases() {
        ObservableList<TownCase> observableList = FXCollections.observableArrayList();
        TownModel townModel = new TownModel();
        JSONArray jsonCases = townModel.getTownCases();
        for (int i = 0; i < jsonCases.length(); i++) {
            JSONObject jsonObject = jsonCases.getJSONObject(i);
            if (jsonObject.isNull("town")) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "No Town (Deleted)");
                jsonObject1.put("_id", "");
                jsonObject.put("town", jsonObject1);
            }

            JSONObject townObject = jsonObject.getJSONObject("town");
            System.out.println(townObject);
            System.out.println(jsonObject);
            observableList.add(new TownCase(
                    townObject.getString("name"),
                    townObject.getString("_id"),
                    jsonObject.getString("_id"),
                    jsonObject.get("totalCase") + "",
                    jsonObject.get("death") + ""));

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
        addTown.setItems(towns);
        addTown.getSelectionModel().select(0);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add Town Case"));
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

                seletedTownCase = (TownCase) ((TreeItem) ob.get(selectedIndex)).getValue();
                TownModel townModel = new TownModel();
                townModel.deleteTownCase(seletedTownCase._id);
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

        seletedTownCase = (TownCase) ((TreeItem) ob.get(selectedIndex)).getValue();
        Town town = towns.stream().filter((x) -> x.name.equals(seletedTownCase.town_name.getValue())).findFirst().orElse(towns.get(0));

        editTownCase.setItems(towns);

        editTownCase.getSelectionModel().select(town);
        editTotalDeath.setText(seletedTownCase.totalDeath.getValue());
        editTotalCase.setText(seletedTownCase.totalCase.getValue());
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit Town"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class TownCase extends RecursiveTreeObject<TownCase> {


        StringProperty town_name;
        StringProperty totalCase;
        StringProperty totalDeath;
        String _id;
        String town_id;

        public TownCase(String town_name, String town_id, String _id, String totalCase, String totalDeath) {

            this.town_name = new SimpleStringProperty(town_name);
            this.town_id = town_id;
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
                JSONObject addTownCaseObject = new JSONObject();
                addTownCaseObject.put("totalDeath", Integer.parseInt(addTotalDeath.getText()));
                addTownCaseObject.put("totalCase", Integer.parseInt(addTotalCase.getText()));

                addTownCaseObject.put("town", addTown.getSelectionModel().getSelectedItem()._id);
                TownModel townModel = new TownModel();

                townModel.addTownCase(addTownCaseObject.toString());

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
                JSONObject editTownCaseObject = new JSONObject();
                editTownCaseObject.put("totalDeath", Integer.parseInt(editTotalDeath.getText()));
                editTownCaseObject.put("totalCase", Integer.parseInt(editTotalCase.getText()));
                editTownCaseObject.put("town", editTownCase.getSelectionModel().getSelectedItem()._id);
                System.out.println(editTownCaseObject);
                TownModel townModel = new TownModel();
                townModel.editTownCase(editTownCaseObject.toString(), seletedTownCase._id);

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

    private ObservableList<Town> loadTowns() {
        ObservableList<Town> observableList = FXCollections.observableArrayList();
        TownModel townModel = new TownModel();
        townModel.refreshTowns();
        JSONArray jsonTowns = townModel.getTowns();
        for (int i = 0; i < jsonTowns.length(); i++) {
            JSONObject jsonObject = jsonTowns.getJSONObject(i);
               observableList.add(new Town(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    class Town {
        String name;
        String _id;

        Town(String name, String _id) {
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