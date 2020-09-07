package controllers;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.ZoomInDown;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
    private JFXButton district;
    @FXML
    private JFXButton caseDistrict;
    @FXML
    private JFXButton caseTown;

    @FXML
    void onDistrict(ActionEvent event) {
        new BounceIn(district).play();
        borderPane.setCenter(loadDistrict());
    }

    private Pane loadDistrict() {
        Pane screen = null;
        try {

            Main.addScreen("district", FXMLLoader.load(getClass().getResource("/views/components/districtView.fxml")));
            screen = Main.getScreen("district");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

    }

    @FXML
    void onCaseState(ActionEvent event) {
        new BounceIn(caseState).play();
        borderPane.setCenter(loadCaseState());
    }

    @FXML
    void onCaseDistrict(ActionEvent event) {
        new BounceIn(caseDistrict).play();
        borderPane.setCenter(loadCaseDistrict());
    }

    private Pane loadCaseDistrict() {
        Pane screen = null;
        try {

            Main.addScreen("caseDistrict", FXMLLoader.load(getClass().getResource("/views/components/caseDistrictView.fxml")));
            screen = Main.getScreen("caseDistrict");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;

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
                Pane screen = loadHospital();
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

                Platform.runLater(() -> {
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
                Pane screen = loadTown();
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
                Pane screen = loadTownShip();
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
    private Text w;

    @FXML
    private Text c;

    @FXML
    private Text e;

    @FXML
    private Text l;

    @FXML
    private Text o;

    @FXML
    private Text m;

    @FXML
    private Text ee;
    @FXML
    private ImageView covid1;
    @FXML
    private ImageView covid2;

    @FXML
    void initialize() {
        goToByRotating(covid1, covid2);
    }

    private void goToByRotating(ImageView node1, ImageView node2) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), node1);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Transition.INDEFINITE);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), node2);

        translateTransition.setToX(node1.getLayoutX() - node2.getLayoutX()
        );
        translateTransition.setToY(node1.getLayoutY() - 100);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(2);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.millis(1000));


        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(1000), node2);
        rotateTransition1.setFromAngle(0);
        rotateTransition1.setToAngle(360);
        rotateTransition1.setAutoReverse(true);
        rotateTransition1.setCycleCount(Transition.INDEFINITE);


        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(1000), node1);

        translateTransition1.setToX(node2.getLayoutX() - 100);
        translateTransition1.setToY((node2.getLayoutY()) - node1.getLayoutY());
        translateTransition1.setAutoReverse(true);
        translateTransition1.setCycleCount(2);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, rotateTransition1);

        parallelTransition.play();
        ParallelTransition parallelTransition1 = new ParallelTransition(translateTransition, pauseTransition, translateTransition1);
        parallelTransition1.pause();
        parallelTransition1.play();
    }
}
