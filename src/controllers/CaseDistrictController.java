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
import models.DistrictModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.CaseValidator;
import utils.EmptyValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CaseDistrictController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<DistrictCase> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;
    @FXML
    private Text titleText;
    @FXML

    private JFXComboBox<District> editDistrictCase;
    private Pane editPane;
    private Pane addPane;
    DistrictCase seletedDistrictCase;
    @FXML


    private JFXSpinner addSpinner;
    private JFXComboBox<District> addDistrict;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<DistrictCase> cases;
    private JFXDialog jfxDialog;
    private ObservableList<District> districts;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;

    private JFXTextField addTotalDeath, addTotalCase,addRecovered;

    private JFXTextField editTotalCase, editTotalDeath,editRecovered;
    private boolean totalDeathBool, totalCaseBool,recoveredBol;
    @FXML
    private JFXTextField searchInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        searchInput.textProperty().addListener(c ->{
            treeView.setPredicate(g ->{
               DistrictCase districtCase =g.getValue();
                return  districtCase.district_name.getValue().toLowerCase().contains(searchInput.getText().toLowerCase());
            });
        });
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/districtCaseEdit.fxml"));
            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            editDistrictCase = (JFXComboBox) editPane.getChildren().get(1);
            editTotalCase = (JFXTextField) editPane.getChildren().get(2);
            editTotalDeath = (JFXTextField) editPane.getChildren().get(3);
            editRecovered =(JFXTextField) editPane.getChildren().get(4);
            editSpinner = (JFXSpinner) editPane.getChildren().get(5);
            editErrorText = (Text) editPane.getChildren().get(6);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator = new EmptyValidator();

            CaseValidator caseValidator = new CaseValidator();
            editTotalCase.setValidators(caseValidator,emptyValidator);
            editTotalDeath.setValidators(caseValidator,emptyValidator);
            editRecovered.setValidators(caseValidator,emptyValidator);
            editTotalCase.textProperty().addListener(c -> {
                if (editTotalCase.validate()) {
                    totalDeathBool = true;
                    if ( totalDeathBool && totalCaseBool &&recoveredBol) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            editTotalDeath.textProperty().addListener(c -> {
                if (editTotalDeath.validate()) {
                    totalCaseBool = true;
                    if ( totalDeathBool && totalCaseBool &&recoveredBol) {
                        editButton.setDisable(false);
                    }
                } else {

                    editButton.setDisable(true);
                }
            });
            editRecovered.textProperty().addListener(c -> {
                if (editRecovered.validate()) {
                    recoveredBol = true;
                    if ( totalDeathBool && totalCaseBool &&recoveredBol) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/districtCaseAdd.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);

            addDistrict = (JFXComboBox) addPane.getChildren().get(1);
            addTotalCase = (JFXTextField) addPane.getChildren().get(2);
            addTotalDeath = (JFXTextField) addPane.getChildren().get(3);
            addRecovered =(JFXTextField) addPane.getChildren().get(4);
            addSpinner = (JFXSpinner) addPane.getChildren().get(5);
            addErrorText = (Text) addPane.getChildren().get(6);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            CaseValidator caseValidator = new CaseValidator();
            addTotalDeath.setValidators(caseValidator,emptyValidator);
            addTotalCase.setValidators(caseValidator,emptyValidator);
            addRecovered.setValidators(caseValidator,emptyValidator);
            addTotalDeath.textProperty().addListener(c -> {
                if (addTotalDeath.validate()) {
                    totalDeathBool = true;
                    if ( totalDeathBool && totalCaseBool &&recoveredBol) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addTotalCase.textProperty().addListener(c -> {
                if (addTotalCase.validate()) {
                    totalCaseBool = true;
                    if (  totalDeathBool && totalCaseBool &&recoveredBol) {
                        addButton.setDisable(false);
                    }
                } else {

                    addButton.setDisable(true);
                }
            });
            addRecovered.textProperty().addListener(c -> {
                if (addRecovered.validate()) {
                    recoveredBol = true;
                    if ( totalDeathBool && totalCaseBool &&recoveredBol) {
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

        JFXTreeTableColumn<DistrictCase, String> districtCol = new JFXTreeTableColumn<>("District Name");
        districtCol.setCellValueFactory(param -> param.getValue().getValue().district_name);
        JFXTreeTableColumn<DistrictCase, String> totalCaseCol = new JFXTreeTableColumn<>("Total Case");
        totalCaseCol.setCellValueFactory(param -> param.getValue().getValue().totalCase);

        JFXTreeTableColumn<DistrictCase, String> totalDeathCol = new JFXTreeTableColumn<>("Total Death");
        totalDeathCol.setCellValueFactory(param -> param.getValue().getValue().totalDeath);
        JFXTreeTableColumn<DistrictCase, String> totalRecovered = new JFXTreeTableColumn<>("Recovered");
        totalRecovered.setCellValueFactory(param -> param.getValue().getValue().recovered);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                cases = loadCases();
                districts = loadDistricts();
                addButton.setDisable(false);
                Platform.runLater(() -> {
                    final TreeItem<DistrictCase> root = new RecursiveTreeItem<DistrictCase>(cases, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(districtCol, totalCaseCol, totalDeathCol,totalRecovered);
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

    private ObservableList<DistrictCase> loadCases() {
        ObservableList<DistrictCase> observableList = FXCollections.observableArrayList();
        DistrictModel districtModel = new DistrictModel();
        JSONArray jsonCases = districtModel.getDistrictCases();
        for (int i = 0; i < jsonCases.length(); i++) {
            JSONObject jsonObject = jsonCases.getJSONObject(i);
            if (jsonObject.isNull("district")) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "No District (Deleted)");
                jsonObject1.put("_id", "");
                jsonObject.put("district", jsonObject1);
            }

            JSONObject districtObject = jsonObject.getJSONObject("district");

            observableList.add(new DistrictCase(
                    districtObject.getString("name"),
                    districtObject.getString("_id"),
                    jsonObject.getString("_id"),
                    jsonObject.get("totalCase") + "",
                    jsonObject.get("totalDeath") + "",
                    jsonObject.get("recovered")+""));

        }

        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        recoveredBol =false;
        totalCaseBool = false;
        totalDeathBool = false;
        addDistrict.setItems(districts);
        addDistrict.getSelectionModel().select(0);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add District Case"));
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

                seletedDistrictCase = (DistrictCase) ((TreeItem) ob.get(selectedIndex)).getValue();
                DistrictModel districtModel = new DistrictModel();
                districtModel.deleteDistrictCase(seletedDistrictCase._id);
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
        recoveredBol =false;
        totalCaseBool = false;
        totalDeathBool = false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        seletedDistrictCase = (DistrictCase) ((TreeItem) ob.get(selectedIndex)).getValue();
        District district = districts.stream().filter((x) -> x.name.equals(seletedDistrictCase.district_name.getValue())).findFirst().orElse(districts.get(0));

        editDistrictCase.setItems(districts);

        editDistrictCase.getSelectionModel().select(district);
        editTotalDeath.setText(seletedDistrictCase.totalDeath.getValue());
        editTotalCase.setText(seletedDistrictCase.totalCase.getValue());
        editRecovered.setText(seletedDistrictCase.recovered.getValue());
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit District"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class DistrictCase extends RecursiveTreeObject<DistrictCase> {


        StringProperty district_name;
        StringProperty totalCase;
        StringProperty totalDeath;
        StringProperty recovered;
        String _id;
        String district_id;

        public DistrictCase(String district_name, String district_id, String _id, String totalCase, String totalDeath,String recovered) {

            this.district_name = new SimpleStringProperty(district_name);
            this.district_id = district_id;
            this.totalCase = new SimpleStringProperty(totalCase);
            this.totalDeath = new SimpleStringProperty(totalDeath);
            this.recovered =new SimpleStringProperty(recovered);
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
                JSONObject addDistrictCaseObject = new JSONObject();
                addDistrictCaseObject.put("totalDeath", Integer.parseInt(addTotalDeath.getText()));
                addDistrictCaseObject.put("totalCase", Integer.parseInt(addTotalCase.getText()));
                addDistrictCaseObject.put("recovered", Integer.parseInt(addRecovered.getText()));

                addDistrictCaseObject.put("district", addDistrict.getSelectionModel().getSelectedItem()._id);
                DistrictModel districtModel = new DistrictModel();

                districtModel.addDistrictCase(addDistrictCaseObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addTotalDeath.setText("");
                    addTotalCase.setText("");
                    addRecovered.setText("");

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
                JSONObject editDistrictCaseObject = new JSONObject();
                editDistrictCaseObject.put("totalDeath", Integer.parseInt(editTotalDeath.getText()));
                editDistrictCaseObject.put("totalCase", Integer.parseInt(editTotalCase.getText()));
                editDistrictCaseObject.put("recovered",Integer.parseInt(editRecovered.getText()));
                editDistrictCaseObject.put("district", editDistrictCase.getSelectionModel().getSelectedItem()._id);

                DistrictModel districtModel = new DistrictModel();
                districtModel.editDistrictCase(editDistrictCaseObject.toString(), seletedDistrictCase._id);

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

    private ObservableList<District> loadDistricts() {
        ObservableList<District> observableList = FXCollections.observableArrayList();
        DistrictModel districtModel = new DistrictModel();
        districtModel.refreshDistricts();
        JSONArray jsonDistricts = districtModel.getDistricts();
        for (int i = 0; i < jsonDistricts.length(); i++) {
            JSONObject jsonObject = jsonDistricts.getJSONObject(i);
            observableList.add(new District(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    class District {
        String name;
        String _id;

        District(String name, String _id) {
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