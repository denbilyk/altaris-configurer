package net.altaris.configurer.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;

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
        controller.init();
        primaryStage.setTitle("Altaris Configurer");
        primaryStage.setScene(new Scene(root, 600, 275));
        primaryStage.show();
    }

}
