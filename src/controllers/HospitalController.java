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
import models.HospitalModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HospitalController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<Hospital> treeView;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXSpinner tableLoading;

    @FXML
    private JFXButton editButton;
    @FXML
    private Text titleText;
    @FXML
    private JFXTextField edithospital;
    private  JFXComboBox<Town> edittown;
    private Pane editPane;
    private Pane addPane;
    Hospital seletedHospital;
    @FXML

    private JFXTextField addhospital;
    private JFXSpinner addSpinner;
    private JFXComboBox<Town> addtown;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<Hospital> hospitals;
    private JFXDialog jfxDialog;
    private ObservableList<Town>  towns;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;
    private  boolean hospitalBool =false;
    @FXML
    private JFXTextField searchInput;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        searchInput.textProperty().addListener(c ->{
            treeView.setPredicate(g ->{
                Hospital hospital =g.getValue();
                return  hospital.name.getValue().toLowerCase().contains(searchInput.getText().toLowerCase()) ||hospital.town_name.getValue().toLowerCase().contains(searchInput.getText().toLowerCase());
            });
        });
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/hospitalEdit.fxml"));
            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            edithospital = (JFXTextField) editPane.getChildren().get(1);
            edittown = (JFXComboBox) editPane.getChildren().get(3);

            editSpinner = (JFXSpinner) editPane.getChildren().get(4);
            editErrorText = (Text) editPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator =new EmptyValidator();
            edithospital.setValidators(emptyValidator);
            edithospital.textProperty().addListener(c ->{
                if (edithospital.validate()) {
                    hospitalBool = true;
                    if (hospitalBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/addHosptial.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);
            addhospital = (JFXTextField) addPane.getChildren().get(1);
            addtown = (JFXComboBox) addPane.getChildren().get(3);

            addSpinner = (JFXSpinner) addPane.getChildren().get(4);
            addErrorText = (Text) addPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton addButton = (JFXButton) gp.getChildren().get(1);
            addButton.setDisable(true);
            EmptyValidator emptyValidator = new EmptyValidator();
            addhospital.setValidators(emptyValidator);
            addhospital.textProperty().addListener(c -> {
                if (addhospital.validate()) {
                    hospitalBool = true;
                    if (hospitalBool) {
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
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            editButton.setDisable(false);
            deleteButton.setDisable(false);

        });

        loadTable();


    }

    private void loadTable() {
        treeView.setVisible(false);
        tableLoading.setVisible(true);

        JFXTreeTableColumn<Hospital, String> hospitalCol = new JFXTreeTableColumn<>("Hospital Name");
        hospitalCol.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<Hospital, String> townCol = new JFXTreeTableColumn<>("Town Name");
        townCol.setCellValueFactory(param -> param.getValue().getValue().town_name);

        Task<Void> tableRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                hospitals = loadHospitals();

                towns =loadTowns();
                addButton.setDisable(false);
                Platform.runLater(() ->{
                    final TreeItem<Hospital> root = new RecursiveTreeItem<Hospital>(hospitals, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(hospitalCol, townCol);
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

    private ObservableList<Hospital> loadHospitals() {
        ObservableList<Hospital> observableList = FXCollections.observableArrayList();

        HospitalModel townModel = new HospitalModel();
        townModel.refreshHospitals();
        JSONArray jsonHospital = townModel.getHospitals();
        for (int i = 0; i < jsonHospital.length(); i++) {
            JSONObject jsonObject = jsonHospital.getJSONObject(i);
           if(jsonObject.isNull("town")){
                JSONObject jsonObject1 =new JSONObject();
                jsonObject1.put("name","No Town (Deleted)");
                jsonObject1.put("_id","");
                jsonObject.put("town",jsonObject1);
            }
            JSONObject townObject =jsonObject.getJSONObject("town");

            observableList.add(new Hospital(jsonObject.getString("name"), townObject.getString("name") ,townObject.getString("_id")  ,jsonObject.getString("_id")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        hospitalBool=false;
        addtown.setItems(towns);
        addtown.getSelectionModel().select(0);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add Town"));
        jfxDialogLayout.setBody(addPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();
    }

    @FXML
    void onDelete(ActionEvent event) {
        JFXButton jfxButton =(JFXButton) event.getTarget();
        jfxButton.setDisable(true);
        Task<Void> deleteRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
                ObservableList ob =  treeView.getRoot().getChildren();

                seletedHospital = (Hospital) ((TreeItem)ob.get(selectedIndex)).getValue();
                HospitalModel townModel =new HospitalModel();
                townModel.deleteHospital(seletedHospital._id);
                loadTable();
                jfxButton.setDisable(false);
                return  null;
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
        hospitalBool=false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob =  treeView.getRoot().getChildren();

        seletedHospital = (Hospital) ((TreeItem)ob.get(selectedIndex)).getValue();
        edithospital.setText(seletedHospital.name.getValue());
        Town state = towns.stream().filter((x) -> x.name.equals(seletedHospital.town_name.getValue())).findFirst().orElse(towns.get(0));

        edittown.setItems(towns);

        edittown.getSelectionModel().select(state);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit Town"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class Hospital extends RecursiveTreeObject<Hospital> {

        StringProperty name;
        StringProperty town_name;
        String _id;
        String town_id;

        public Hospital(String name, String town_name, String town_id, String _id) {
            this.name = new SimpleStringProperty(name);
            this.town_name = new SimpleStringProperty(town_name);
            this.town_id = town_id;
            this._id = _id;
        }

    }
    private  void onAdd(MouseEvent e ){
        hospitalBool=false;
        JFXButton jfxButton =(JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        addSpinner.setVisible(true);
        Task<Void> addRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject addHospitalObject =new JSONObject();
                addHospitalObject.put("name",addhospital.getText());

                addHospitalObject.put("town_id",addtown.getSelectionModel().getSelectedItem()._id);
                HospitalModel townModel = new HospitalModel();

                townModel.addHospital(addHospitalObject.toString());

                Platform.runLater(() ->{

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addhospital.setText("");

                });
                return  null;
            }
        };
        new Thread(addRequest).start();
        addRequest.setOnSucceeded(event -> {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });
    }

    private void onEdit(MouseEvent e) {
        hospitalBool=false;
        JFXButton jfxButton =(JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);

        Task<Void> editRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editTownObject =new JSONObject();
                editTownObject.put("name",edithospital.getText());

                editTownObject.put("_id",seletedHospital._id);
                editTownObject.put("town",edittown.getSelectionModel().getSelectedItem()._id);
                HospitalModel townModel = new HospitalModel();

                townModel.editHospital(editTownObject.toString(),seletedHospital._id);

                Platform.runLater(() ->{

                    loadTable();
                    jfxButton.setDisable(false);
                    editSpinner.setVisible(false);
                    jfxDialog.close();

                });
                return  null;
            }
        };

        new Thread(editRequest).start();
        editRequest.setOnSucceeded(event -> {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        });



    }
    private ObservableList<Town> loadTowns() {
        System.out.println("From Town");
        ObservableList<Town> observableList = FXCollections.observableArrayList();
        TownModel townModel = new TownModel();

        JSONArray jsonTowns = townModel.getTownName();

        for (int i = 0; i < jsonTowns.length(); i++) {
            JSONObject jsonObject = jsonTowns.getJSONObject(i);
            observableList.add(new Town(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }
    class Town{
        String name ;
        String _id;
        Town(String name,String _id){
            this.name =name;
            this._id =_id;
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
