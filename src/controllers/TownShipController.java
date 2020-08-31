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
import models.TownShipModel;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.EmptyValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TownShipController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<TownShip> treeView;
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
    private  JFXComboBox<Town> editTown;
    private Pane editPane;
    private Pane addPane;
    TownShip seletedTownShip;
    @FXML

    private JFXTextField addtown;
    private JFXSpinner addSpinner;
    private JFXComboBox<Town> addstate;
    private Text addErrorText;

    @FXML
    private JFXButton deleteButton;
    ObservableList<TownShip> townships;
    private JFXDialog jfxDialog;
    private ObservableList<Town>  towns;
    private JFXDialogLayout jfxDialogLayout;
    private Text editErrorText;
    private JFXSpinner editSpinner;
    private  boolean townBool;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/editTownShip.fxml"));

            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            edittown = (JFXTextField) editPane.getChildren().get(1);
            editTown = (JFXComboBox) editPane.getChildren().get(3);

            editSpinner = (JFXSpinner) editPane.getChildren().get(4);
            editErrorText = (Text) editPane.getChildren().get(5);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            JFXButton editButton = (JFXButton) gp.getChildren().get(1);
            EmptyValidator emptyValidator =new EmptyValidator();
            edittown.setValidators(emptyValidator);
            edittown.textProperty().addListener(c ->{
                if (edittown.validate()) {
                    townBool = true;
                    if (townBool) {
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
            addPane = FXMLLoader.load(getClass().getResource("/views/components/addTownShip.fxml"));

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
                    townBool = true;
                    if (townBool) {
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

        JFXTreeTableColumn<TownShip, String> townShipCol = new JFXTreeTableColumn<>("TownShip Name");
        townShipCol.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<TownShip, String> townCol = new JFXTreeTableColumn<>("Town Name");
        townCol.setCellValueFactory(param -> param.getValue().getValue().town_name);

        Task<Void> tableRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                townships = loadTownShips();

                  towns =loadTowns();
                addButton.setDisable(false);
                Platform.runLater(() ->{
                    final TreeItem<TownShip> root = new RecursiveTreeItem<TownShip>(townships, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(townShipCol, townCol);
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

    private ObservableList<TownShip> loadTownShips() {
        ObservableList<TownShip> observableList = FXCollections.observableArrayList();

        TownShipModel townModel = new TownShipModel();
        townModel.refreshTownShips();
        JSONArray jsonTownShip = townModel.getTownShips();
        for (int i = 0; i < jsonTownShip.length(); i++) {
            JSONObject jsonObject = jsonTownShip.getJSONObject(i);
           if(jsonObject.isNull("town")){
               JSONObject jsonObject1 =new JSONObject();
               jsonObject1.put("name","No Town (Deleted)");
               jsonObject1.put("_id","");
               jsonObject.put("town",jsonObject1);
            }
            JSONObject townObject =jsonObject.getJSONObject("town");
        //    System.out.println(townObject.toString());
            observableList.add(new TownShip(jsonObject.getString("name"), townObject.getString("name") ,townObject.getString("_id")  ,jsonObject.getString("_id")));
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {
        townBool =false;
        addstate.setItems(towns);
        addstate.getSelectionModel().select(0);


        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add TownShip"));
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

                seletedTownShip = (TownShip) ((TreeItem)ob.get(selectedIndex)).getValue();
                TownShipModel townModel =new TownShipModel();
                townModel.deleteTownShip(seletedTownShip._id);
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
        townBool =false;
        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob =  treeView.getRoot().getChildren();

        seletedTownShip = (TownShip) ((TreeItem)ob.get(selectedIndex)).getValue();
        edittown.setText(seletedTownShip.name.getValue());
        Town state = towns.stream().filter((x) -> x.name.equals(seletedTownShip.town_name.getValue())).findFirst().orElse(towns.get(0));
        editTown.setItems(towns);

        editTown.getSelectionModel().select(state);
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit TownShip"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();



    }

    class TownShip extends RecursiveTreeObject<TownShip> {

        StringProperty name;
        StringProperty town_name;
        String _id;
        String town_id;

        public TownShip(String name, String town_name, String town_id, String _id) {
            this.name = new SimpleStringProperty(name);
            this.town_name = new SimpleStringProperty(town_name);
            this.town_id = town_id;
            this._id = _id;
        }

    }
    private  void onAdd(MouseEvent e ){
        townBool =false;
        JFXButton jfxButton =(JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        addSpinner.setVisible(true);
        Task<Void> addRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject addTownShipObject =new JSONObject();
                addTownShipObject.put("name",addtown.getText());

                addTownShipObject.put("town_id",addstate.getSelectionModel().getSelectedItem()._id);
                TownShipModel townModel = new TownShipModel();

                townModel.addTownShip(addTownShipObject.toString());

                Platform.runLater(() ->{

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addtown.setText("");

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
        townBool =false;
        JFXButton jfxButton =(JFXButton) e.getTarget();
        jfxButton.setDisable(true);
        editSpinner.setVisible(true);

        Task<Void> editRequest =new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONObject editTownObject =new JSONObject();
                editTownObject.put("name",edittown.getText());

                editTownObject.put("_id",seletedTownShip._id);
                editTownObject.put("town",editTown.getSelectionModel().getSelectedItem()._id);
                TownShipModel townModel = new TownShipModel();

                townModel.editTownShip(editTownObject.toString(),seletedTownShip._id);

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
