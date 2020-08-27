package controllers;

import animatefx.animation.Bounce;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.util.Duration;
import netscape.javascript.JSObject;
import utils.HttpService;

import java.io.IOException;

public class DashBoardController {
    static  WebEngine webEngine = null;
      static WebView webView = null;
     static StackPane publicStackPane;
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private AnchorPane topPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXSpinner spinner;

    private JFXDrawer drawer;

    @FXML
    void initialize() {
        System.out.println("I got call db");
        publicStackPane = stackPane;
        // Case Component
        Platform.runLater(() -> {
            loadCase();
            //Drawer
            loadDrawer();
            //Map Related
            loadMap();
            //Make rip  effect on topPane

            //activate Hamberger
            loadTopPane();

            activateHamberger();
        });
    }

    private void activateHamberger() {
        HamburgerBasicCloseTransition hst = new HamburgerBasicCloseTransition(hamburger);
        hst.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            hst.setRate(hst.getRate() * -1);
            hst.play();
            borderPane.setRight(drawer);
            if (drawer.isOpened()) {
                borderPane.setRight(null);
                drawer.close();
            } else {
                drawer.open();
            }

        });

    }

    private void loadTopPane() {
        JFXRippler rippler = new JFXRippler(topPane);
        borderPane.setTop(rippler);
    }

    private void loadMap() {
        Platform.runLater(() -> {

            webView = new WebView();
            webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);

            JSObject window = (JSObject) webEngine.executeScript("window");

            webEngine.setOnError(e -> {
                System.out.println(e.getMessage());

            });
            webView.setCache(true);
            webView.setContextMenuEnabled(true);

            webEngine.load(getClass().getResource("/views/map.html").toString());
            webEngine.setOnAlert(e -> {
                System.out.println(e.getData());

                borderPane.setCenter(webView);
            });

            webEngine.getLoadWorker().stateProperty().addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                            webEngine.executeScript("test(" + HttpService.getCaseByCountries() + ")");

                            if (newValue != Worker.State.SUCCEEDED) {
                                return;
                            }
                            JSObject window = (JSObject) webEngine.executeScript("window");

                        }
                    }
            );


        });

    }

    private void loadCase() {

        borderPane.setLeft(Main.getComponent("caseComponent"));
    }

    private void loadDrawer() {
        drawer = new JFXDrawer();
        drawer.setDefaultDrawerSize(200);
        drawer.setDirection(JFXDrawer.DrawerDirection.RIGHT);
        drawer.setPrefWidth(200);
        try {
            VBox vb = FXMLLoader.load(getClass().getResource("/views/sideBar.fxml"));
            for (Node button : vb.getChildren()) {
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    switch (button.getId()) {
                        case "loacal":
                            new FadeOut(borderPane).playOnFinished(new FadeIn(borderPane)).play();

                            borderPane.setCenter(null);

                            System.out.println("I am Local");
                            break;
                        case "global":
                            new FadeOut(borderPane).playOnFinished(new FadeIn(borderPane)).play();
                            borderPane.setCenter(webView);
                            System.out.println("I am Global");
                            break;
                        case "login":
                            new FadeOut(borderPane).playOnFinished(new FadeIn(borderPane)).play();

                             System.out.println("I am Login ");
                            break;

                    }
                });
            }
            drawer.setSidePane(vb);

        } catch (IOException e) {
            System.out.println("Error On Loading VBox to drawer");
        }
    }

}
