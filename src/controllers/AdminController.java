package controllers;

import animatefx.animation.BounceIn;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import utils.Helper;

public class AdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton state;

    @FXML
    private JFXButton town;

    @FXML
    private JFXButton townShip;

    @FXML
    private JFXButton account;

    @FXML
    private JFXButton hospital;

    @FXML
    private JFXButton patient;

    @FXML
    private JFXButton admin;

    @FXML
    private JFXButton logout;
    @FXML
    private JFXButton caseState;

    @FXML
    private JFXButton caseTown;
    @FXML
    void onCaseState(ActionEvent event) {
        new BounceIn(caseState).play();
        borderPane.setCenter(loadCaseState());
    }

    @FXML
    void onCaseTown(ActionEvent event) {
        new BounceIn(caseTown).play();
        borderPane.setCenter(loadTownCase());
    }
    private Pane loadTownCase() {
        Pane screen = null;
        try {

            Main.addScreen("caseTown", FXMLLoader.load(getClass().getResource("/views/components/TownCaseView.fxml")));
            screen = Main.getScreen("caseTown");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    private Pane loadCaseState() {
        Pane screen = null;
        try {

            Main.addScreen("caseState", FXMLLoader.load(getClass().getResource("/views/components/StateCaseView.fxml")));
            screen = Main.getScreen("caseState");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    @FXML
    void onAdmin(ActionEvent event) {

        new BounceIn(admin).play();
        borderPane.setCenter(loadAdmin());
    }
    private Pane loadAdmin() {
        Pane screen = null;
        try {
            Main.addScreen("admin", FXMLLoader.load(getClass().getResource("/views/components/createAdminView.fxml")));
            screen = Main.getScreen("admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    @FXML
    void onAccount(ActionEvent event) {
        new BounceIn(account).play();
        borderPane.setCenter(loadAccount());
    }
    private Pane loadAccount() {
        Pane screen = null;
        try {
            Main.addScreen("account", FXMLLoader.load(getClass().getResource("/views/components/myAccountView.fxml")));
            screen = Main.getScreen("account");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }

    @FXML
    void onHospital(ActionEvent event) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BounceIn(hospital).play();
                Pane screen =  loadHospital();
                Platform.runLater(() -> borderPane.setCenter(screen));
                return null;
            }
        };
        new Thread(task).start();
    }
    private Pane loadHospital() {
        Pane screen = null;
        try {
            Main.addScreen("hospitalView", FXMLLoader.load(getClass().getResource("/views/components/hospitalView.fxml")));
            screen = Main.getScreen("hospitalView");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    @FXML
    void onLogout(ActionEvent event) {

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BounceIn(logout).play();

                Platform.runLater(() ->{
                    try {
                        Helper.deleteToken();
                        Main.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/login.fxml"))));
                        Main.stage.centerOnScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                return null;

            }
        };
        new Thread(task).start();


    }



    @FXML
    void onPatient(ActionEvent event) {
        new BounceIn(patient).play();
        borderPane.setCenter(loadPatient());
    }
    private Pane loadPatient() {
        Pane screen = null;
        try {
            Main.addScreen("patientView", FXMLLoader.load(getClass().getResource("/views/components/patientView.fxml")));
            screen = Main.getScreen("patientView");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }

    @FXML
    void onState(ActionEvent event) {
        new BounceIn(state).play();
        borderPane.setCenter(loadState());

    }

    private Pane loadState() {
        Pane screen = null;
        try {
            Main.addScreen("stateView", FXMLLoader.load(getClass().getResource("/views/components/stateView.fxml")));
            screen = Main.getScreen("stateView");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }

    @FXML
    void onTown(ActionEvent event) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BounceIn(town).play();
                Pane screen =  loadTown();
                Platform.runLater(() -> borderPane.setCenter(screen));
                return null;
            }
        };
        new Thread(task).start();
    }
    private Pane loadTown() {
        Pane screen = null;
        try {
            Main.addScreen("townView", FXMLLoader.load(getClass().getResource("/views/components/townView.fxml")));

            screen = Main.getScreen("townView");
            System.out.println("Loading Town");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    @FXML
    void onTownShip(ActionEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BounceIn(townShip).play();
                Pane screen =  loadTownShip();
                Platform.runLater(() -> borderPane.setCenter(screen));
                return null;
            }
        };
        new Thread(task).start();
    }
    private Pane loadTownShip() {
        Pane screen = null;
        try {
            Main.addScreen("townShipView", FXMLLoader.load(getClass().getResource("/views/components/townShipView.fxml")));
            screen = Main.getScreen("townShipView");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }
    @FXML
    void initialize() {
        System.out.println("Admin Pannel");
    }
}
