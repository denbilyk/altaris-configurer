package net.altaris.configurer.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.altaris.configurer.io.HidPort;

public class Main extends Application {
    private Controller controller;
    private HidPort hidPort;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("OnClose");
        controller.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fmxLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = fmxLoader.load();
        controller = fmxLoader.getController();
        hidPort = new HidPort(controller);
        controller.init(hidPort);
        primaryStage.setTitle("Altaris Configurer");
        primaryStage.setScene(new Scene(root, 650, 350));
        primaryStage.show();
    }

}
