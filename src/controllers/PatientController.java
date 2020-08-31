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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Helper;
import utils.HttpService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML
    private StackPane main;
    @FXML
    private JFXTreeTableView<Patient> treeView;
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
    private JFXComboBox<Town> editstate;
    private Pane editPane;
    private Pane addPane;
    Patient seletedPatient;
    @FXML

    private JFXTextField addPatient;
    private JFXTextField addAge;
    private JFXComboBox<State> addState;
    private JFXComboBox<String> addContactPerson;
    private JFXComboBox<String> addGender;
    private JFXComboBox<TownShip> addTownShip;
    private JFXComboBox<Town> addTown;
    private JFXComboBox<Hospital> addHospital;
    private JFXComboBox<String> addOversea;
    private JFXDatePicker addDate;
    private JFXSpinner addSpinner;
    private Text addErrorText;
    //Edit
    private JFXTextField editPatient;
    private JFXTextField editAge;
    private JFXComboBox<State> editState;
    private JFXComboBox<String> editContactPerson;
    private JFXComboBox<String> editGender;
    private JFXComboBox<TownShip> editTownShip;
    private JFXComboBox<Town> editTown;
    private JFXComboBox<Hospital> editHospital;
    private JFXComboBox<String> editOversea;
    private JFXDatePicker editDate;
    private JFXSpinner editSpinner;
    private Text editErrorText;

    @FXML
    private JFXButton deleteButton;

    private JFXDialog jfxDialog;
    private ObservableList<Town> towns;
    private ObservableList<State> states;
    private ObservableList<TownShip> townships;
    private ObservableList<Hospital> hospitals;
    private ObservableList<Patient> patients;
    private ObservableList<String> genders;
    private ObservableList<String> contacts;
    private ObservableList<String> countries;
    private JFXDialogLayout jfxDialogLayout;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ZoomInDown(titleText).play();
        try {
            editPane = FXMLLoader.load(getClass().getResource("/views/components/editPatient.fxml"));

            GridPane gp = (GridPane) editPane.getChildren().get(editPane.getChildren().size() - 1);
            HBox hBox = (HBox) editPane.getChildren().get(0);
            editPatient = (JFXTextField) hBox.getChildren().get(0);
            editAge = (JFXTextField) hBox.getChildren().get(1);
            AnchorPane anchorPane = (AnchorPane) editPane.getChildren().get(1);
            editGender = (JFXComboBox) anchorPane.getChildren().get(2);
            editContactPerson = (JFXComboBox) anchorPane.getChildren().get(4);
            editState = (JFXComboBox) anchorPane.getChildren().get(5);
            editOversea = (JFXComboBox) anchorPane.getChildren().get(6);
            AnchorPane anchorPane1 = (AnchorPane) editPane.getChildren().get(2);
            editTownShip = (JFXComboBox) anchorPane1.getChildren().get(2);
            editTown = (JFXComboBox) anchorPane1.getChildren().get(4);
            editHospital = (JFXComboBox) anchorPane1.getChildren().get(5);
            editDate = (JFXDatePicker) anchorPane1.getChildren().get(6);
            editSpinner = (JFXSpinner) editPane.getChildren().get(3);
            editErrorText = (Text) editPane.getChildren().get(4);
            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            gp.getChildren().get(1).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEdit);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addPane = FXMLLoader.load(getClass().getResource("/views/components/addPatient.fxml"));

            GridPane gp = (GridPane) addPane.getChildren().get(addPane.getChildren().size() - 1);
            HBox hBox = (HBox) addPane.getChildren().get(0);
            addPatient = (JFXTextField) hBox.getChildren().get(0);
            addAge = (JFXTextField) hBox.getChildren().get(1);
            AnchorPane anchorPane = (AnchorPane) addPane.getChildren().get(1);
            addGender = (JFXComboBox) anchorPane.getChildren().get(2);
            addContactPerson = (JFXComboBox) anchorPane.getChildren().get(4);
            addState = (JFXComboBox) anchorPane.getChildren().get(5);
            addOversea = (JFXComboBox) anchorPane.getChildren().get(6);
            AnchorPane anchorPane1 = (AnchorPane) addPane.getChildren().get(2);
            addTownShip = (JFXComboBox) anchorPane1.getChildren().get(2);
            addTown = (JFXComboBox) anchorPane1.getChildren().get(4);
            addHospital = (JFXComboBox) anchorPane1.getChildren().get(5);
            addDate = (JFXDatePicker) anchorPane1.getChildren().get(6);
            addSpinner = (JFXSpinner) addPane.getChildren().get(3);
            addErrorText = (Text) addPane.getChildren().get(4);

            gp.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCancel);
            gp.getChildren().get(1).addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAdd);

        } catch (IOException e) {
            e.printStackTrace();
        }
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            editButton.setDisable(false);
            deleteButton.setDisable(false);

        });


        loadTable();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addButton.setDisable(true);
                states = loadSates();
                towns = loadTowns();
                townships = loadTownShips();

                genders = FXCollections.observableArrayList();
                genders.add("Male");
                genders.add("Female");


                Platform.runLater(() -> {
                    addDate.setValue(LocalDate.now());
                    addState.setItems(states);
                    editState.setItems(states);
                    addState.getSelectionModel().select(0);
                    addTown.setItems(towns);
                    editTown.setItems(towns);
                    addTown.getSelectionModel().select(0);
                    addTownShip.setItems(townships);
                    editTownShip.setItems(townships);
                    addTownShip.getSelectionModel().select(0);

                    addGender.setItems(genders);
                    addGender.getSelectionModel().select(0);
                    editGender.setItems(genders);

                });
                return null;
            }
        };
        Task<Void> task1 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                countries = loadCountries();

                hospitals = loadHospitals();
                Platform.runLater(() -> {

                    countries.add(0, "No");
                    editHospital.setItems(hospitals);

                    addHospital.setItems(hospitals);
                    addHospital.getSelectionModel().select(0);
                    addOversea.setItems(countries);
                    editOversea.setItems(countries);
                    addOversea.getSelectionModel().select(0);
                    addContactPerson.setItems(contacts);
                    addContactPerson.getSelectionModel().select(0);

                });
                return null;
            }
        };
        new Thread(task1).start();
        new Thread(task).start();
        task.setOnSucceeded((sd) -> {
            addButton.setDisable(false);
        });
        task1.setOnSucceeded((s) -> {
            addButton.setDisable(false);
        });
    }

    private void loadTable() {
        treeView.setVisible(false);
        tableLoading.setVisible(true);

        JFXTreeTableColumn<Patient, String> patientCol = new JFXTreeTableColumn<>("Patient");
        patientCol.setCellValueFactory(param -> param.getValue().getValue().name);

        JFXTreeTableColumn<Patient, String> ageCol = new JFXTreeTableColumn<>("Age");
        ageCol.setCellValueFactory(param -> param.getValue().getValue().age);
        JFXTreeTableColumn<Patient, String> genderCol = new JFXTreeTableColumn<>("Gender");
        genderCol.setCellValueFactory(param -> param.getValue().getValue().gender);
        JFXTreeTableColumn<Patient, String> stateCol = new JFXTreeTableColumn<>("State");
        stateCol.setCellValueFactory(param -> param.getValue().getValue().state_name);
        JFXTreeTableColumn<Patient, String> townCol = new JFXTreeTableColumn<>("Town");
        townCol.setCellValueFactory(param -> param.getValue().getValue().town_name);
        JFXTreeTableColumn<Patient, String> townShipCol = new JFXTreeTableColumn<>("TownShip");
        townShipCol.setCellValueFactory(param -> param.getValue().getValue().township_name);
        JFXTreeTableColumn<Patient, String> hospitalCol = new JFXTreeTableColumn<>("Hospital");
        hospitalCol.setCellValueFactory(param -> param.getValue().getValue().hospital_name);
        JFXTreeTableColumn<Patient, String> date = new JFXTreeTableColumn<>("Date");
        date.setCellValueFactory(param -> param.getValue().getValue().date);
        JFXTreeTableColumn<Patient, String> contactCol = new JFXTreeTableColumn<>("Contact");
        contactCol.setCellValueFactory(param -> param.getValue().getValue().contact);
        JFXTreeTableColumn<Patient, String> overseaCol = new JFXTreeTableColumn<>("Oversea Country");
        overseaCol.setCellValueFactory(param -> param.getValue().getValue().oversea_country);
        Task<Void> tableRequest = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                patients = loadPatients();
                contacts
                        = loadContacts();

                Platform.runLater(() -> {
                    final TreeItem<Patient> root = new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren);
                    treeView.getColumns().setAll(patientCol, ageCol, genderCol, stateCol, townCol, townShipCol, hospitalCol, overseaCol, date, contactCol);
                    contacts.add(0, "No");
                    addContactPerson.setItems(contacts);
                    editContactPerson.setItems(contacts);
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

    private ObservableList<Patient> loadPatients() {
        ObservableList<Patient> observableList = FXCollections.observableArrayList();

        PatientModel townModel = new PatientModel();
        townModel.refreshPatients();
        JSONArray jsonPatient = townModel.getPatients();
        for (int i = 0; i < jsonPatient.length(); i++) {
            JSONObject jsonObject = jsonPatient.getJSONObject(i);
            JSONObject townObject = jsonObject.getJSONObject("town");
            JSONObject townShipObject = jsonObject.getJSONObject("townShip");
            JSONObject stateObject = jsonObject.getJSONObject("state");
            JSONObject hospitalObject = jsonObject.getJSONObject("hospital");


            Patient p = new Patient(jsonObject.getString("patient_id"),
                    jsonObject.getString("_id"),
                    townObject.getString("name"),
                    townObject.getString("_id"),
                    stateObject.getString("name"),
                    stateObject.getString("_id"),
                    townShipObject.getString("name"),
                    townShipObject.getString("_id"),
                    hospitalObject.getString("name"),
                    hospitalObject.getString("_id"),
                    Helper.formatDate(jsonObject.getString("date")),
                    jsonObject.getString("contact_person"),
                    jsonObject.get("age") + "",
                    jsonObject.getString("gender"),
                    jsonObject.getString("oversea_country")
            );

            observableList.add(p);
        }
        return observableList;
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    @FXML
    void onAdd(ActionEvent event) {

        //   addstate.setItems(towns);

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Add Patient"));
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

                seletedPatient = (Patient) ((TreeItem) ob.get(selectedIndex)).getValue();
                PatientModel townModel = new PatientModel();
                townModel.deletePatient(seletedPatient._id);
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

        int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        ObservableList ob = treeView.getRoot().getChildren();

        seletedPatient = (Patient) ((TreeItem) ob.get(selectedIndex)).getValue();
        editPatient.setText(seletedPatient.name.getValue());
        Town town = towns.stream().filter((x) -> x.name.equals(seletedPatient.town_name.getValue())).findFirst().orElse(towns.get(0));
        TownShip townShip = townships.stream().filter((x) -> x.name.equals(seletedPatient.township_name.getValue())).findFirst().orElse(townships.get(0));
        State state = states.stream().filter((x) -> x.name.equals(seletedPatient.state_name.getValue())).findFirst().orElse(states.get(0));
        Hospital hospital = hospitals.stream().filter((x) -> x.name.equals(seletedPatient.hospital_name.getValue())).findFirst().orElse(hospitals.get(0));
        String gender = seletedPatient.gender.getValue();
        String age = seletedPatient.age.getValue();
        String contactPerson = seletedPatient.contact.getValue();
        String date = seletedPatient.date.getValue();
        String oversea = seletedPatient.oversea_country.getValue();


        editTown.getSelectionModel().select(town);

        editTownShip.getSelectionModel().select(townShip);

        editState.getSelectionModel().select(state);

        editHospital.getSelectionModel().select(hospital);

        editGender.getSelectionModel().select(gender);

        editAge.setText(age);

        editContactPerson.getSelectionModel().select(contactPerson);

        editDate.setValue(Helper.formatLocalDate(date));

        editOversea.getSelectionModel().select(oversea);

        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setHeading(new Text("Edit Patient"));
        jfxDialogLayout.setBody(editPane);
        jfxDialog = new JFXDialog(main, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.setOverlayClose(false);
        jfxDialog.show();

        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }

    class Patient extends RecursiveTreeObject<Patient> {

        StringProperty name;
        StringProperty town_name;
        StringProperty state_name;
        StringProperty township_name;
        StringProperty date;
        StringProperty contact;
        StringProperty age;
        StringProperty gender;
        StringProperty hospital_name;
        StringProperty oversea_country;
        String _id;
        String town_id;
        String state_id;
        String township_id;
        String hospital_id;

        public Patient(String name, String _id, String town_name, String town_id,
                       String state_name,
                       String state_id,
                       String township_name,
                       String township_id,
                       String hospital_name,
                       String hospital_id,
                       String date,
                       String contact,
                       String age,
                       String gender,
                       String oversea_country
        ) {
            this.name = new SimpleStringProperty(name);
            this.town_name = new SimpleStringProperty(town_name);
            this.state_name = new SimpleStringProperty(state_name);
            this.township_name = new SimpleStringProperty(township_name);
            this.hospital_name = new SimpleStringProperty(hospital_name);

            this.town_id = town_id;
            this.hospital_id = hospital_id;
            this.state_id = state_id;
            this.township_id = township_id;
            this.state_id = state_id;
            this.contact = new SimpleStringProperty(contact);
            this.date = new SimpleStringProperty(date);
            this.age = new SimpleStringProperty(age);
            this.gender = new SimpleStringProperty(gender);
            this.oversea_country = new SimpleStringProperty(oversea_country);
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
                JSONObject addPatientObject = new JSONObject();
                addPatientObject.put("patient_id", addPatient.getText());

                addPatientObject.put("age", addAge.getText());
                addPatientObject.put("gender", addGender.getSelectionModel().getSelectedItem());
                addPatientObject.put("date", addDate.getValue());
                addPatientObject.put("town_id", addTown.getSelectionModel().getSelectedItem()._id);
                addPatientObject.put("state_id", addState.getSelectionModel().getSelectedItem()._id);
                addPatientObject.put("towns_ship_id", addTownShip.getSelectionModel().getSelectedItem()._id);
                addPatientObject.put("hospital_id", addHospital.getSelectionModel().getSelectedItem()._id);
                addPatientObject.put("contact_person", addContactPerson.getSelectionModel().getSelectedItem());
                addPatientObject.put("oversea_country", "No");
                PatientModel townModel = new PatientModel();

                townModel.addPatient(addPatientObject.toString());

                Platform.runLater(() -> {

                    loadTable();
                    jfxButton.setDisable(false);
                    addSpinner.setVisible(false);
                    jfxDialog.close();

                    addPatient.setText("");

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
                JSONObject editPatientObject = new JSONObject();
                editPatientObject.put("patient_id", editPatient.getText());
                editPatientObject.put("age", editAge.getText());
                editPatientObject.put("gender", editGender.getSelectionModel().getSelectedItem());
                editPatientObject.put("state", editState.getSelectionModel().getSelectedItem()._id);
                editPatientObject.put("hospital",editHospital.getSelectionModel().getSelectedItem()._id);
                editPatientObject.put("town",editTown.getSelectionModel().getSelectedItem()._id);
                editPatientObject.put("townShip",editTownShip.getSelectionModel().getSelectedItem()._id);
                editPatientObject.put("oversea_country",editOversea.getSelectionModel().getSelectedItem());
                editPatientObject.put("date",editDate.getValue());
                editPatientObject.put("contact_person",editContactPerson.getSelectionModel().getSelectedItem());

                PatientModel townModel = new PatientModel();

                townModel.editPatient(editPatientObject.toString(), seletedPatient._id);

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

    class State {
        String name;
        String _id;

        State(String name, String _id) {
            this._id = _id;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    class Hospital {
        String name;
        String _id;

        Hospital(String name, String _id) {
            this.name = name;
            this._id = _id;

        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    class TownShip {
        String name;
        String _id;

        TownShip(String name, String _id) {
            this._id = _id;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private ObservableList<State> loadSates() {
        ObservableList<State> observableList = FXCollections.observableArrayList();
        StateModel stateModel = new StateModel();
        stateModel.refreshStates();
        JSONArray jsonStates = stateModel.getStates();
        for (int i = 0; i < jsonStates.length(); i++) {
            JSONObject jsonObject = jsonStates.getJSONObject(i);

            observableList.add(new State(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    private ObservableList<Town> loadTowns() {

        ObservableList<Town> observableList = FXCollections.observableArrayList();
        TownModel townModel = new TownModel();

        JSONArray jsonTowns = townModel.getTownName();

        for (int i = 0; i < jsonTowns.length(); i++) {
            JSONObject jsonObject = jsonTowns.getJSONObject(i);
            observableList.add(new Town(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    private ObservableList<TownShip> loadTownShips() {
        ObservableList<TownShip> observableList = FXCollections.observableArrayList();

        TownShipModel townModel = new TownShipModel();
        townModel.refreshTownShips();
        JSONArray jsonTownShip = townModel.getTownShips();
        for (int i = 0; i < jsonTownShip.length(); i++) {
            JSONObject jsonObject = jsonTownShip.getJSONObject(i);

            observableList.add(new TownShip(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    private ObservableList<String> loadContacts() {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        PatientModel patientModel = new PatientModel();
        JSONArray jsonContacts = patientModel.getContacts();
        for (int i = 0; i < jsonContacts.length(); i++) {
            JSONObject jsonObject = jsonContacts.getJSONObject(i);

            observableList.add(jsonObject.getString("patient_id"));
        }
        return observableList;
    }

    private ObservableList<Hospital> loadHospitals() {
        ObservableList<Hospital> observableList = FXCollections.observableArrayList();

        HospitalModel townModel = new HospitalModel();
        townModel.refreshHospitals();
        JSONArray jsonHospital = townModel.getHospitals();
        for (int i = 0; i < jsonHospital.length(); i++) {
            JSONObject jsonObject = jsonHospital.getJSONObject(i);

            observableList.add(new Hospital(jsonObject.getString("name"), jsonObject.getString("_id")));
        }
        return observableList;
    }

    private ObservableList<String> loadCountries() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        JSONArray jsonCountries = HttpService.getCountries();
        for (int i = 0; i < jsonCountries.length(); i++) {
            JSONObject jsonObject = jsonCountries.getJSONObject(i);

            observableList.add(jsonObject.getString("name"));
        }
        return observableList;
    }

    private void onCancel(MouseEvent e) {
        jfxDialog.close();
    }

}
