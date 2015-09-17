package net.altaris.configurer.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jssc.SerialPortException;
import jssc.SerialPortList;
import net.altaris.configurer.io.SPort;

import java.util.Arrays;
import java.util.regex.Pattern;


public class Controller {
    private SPort sPort = new SPort();
    private boolean connected = false;
    private static volatile boolean onTimer = false;

    @FXML
    private ComboBox<String> txtPort;
    @FXML
    private Button btnOpen;
    @FXML
    private HBox wifiSettings;
    @FXML
    private VBox rightVBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Button btnWifiSave;
    @FXML
    private Parent root;
    private Timeline timeline;

    void init() {
        Pattern pattern = Pattern.compile("tty\\.+[A-Za-z0-9]+");
        String[] portNames = SerialPortList.getPortNames("/dev/", pattern);
        System.out.println(Arrays.toString(portNames));
        txtPort.setItems(FXCollections.observableList(Arrays.asList(portNames)));
    }

    public void handleBtnStatus(ActionEvent actionEvent) {
        System.out.println("Status button");
        String status = sPort.getStatus();
        String text = "WIFI: test\nIP: 10.10.0.111 \nMask: 255.255.255.0";
        infoLabel.setText(status);

    }

    public void handleBtnOpen(ActionEvent actionEvent) throws SerialPortException {
        System.out.println("Button open");
        if (btnOpen.getText().equals("Open")) {
            System.out.println(txtPort.getValue());
            sPort.connect(txtPort.getValue(), 9600);
            connected = true;
            btnOpen.setText("Close");
            infoLabel.setText("Connected!");
            triggerLayout();
        } else {
            sPort.close();
            connected = false;
            btnOpen.setText("Open");
            infoLabel.setText("Disconnected");
            triggerLayout();
        }
    }

    public void handleBtnWifiSave(ActionEvent actionEvent) throws InterruptedException {
        infoLabel.setText("Saved! Rebooting...");
        rightVBox.setDisable(true);
        wifiSettings.setDisable(true);
        timeline = new Timeline(new KeyFrame(Duration.millis(3000), ev -> {
            rightVBox.setDisable(false);
            wifiSettings.setDisable(false);
            handleBtnStatus(null);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }


    void stop() {
        sPort.close();
        if (null != timeline)
            timeline.stop();
    }

    private void triggerLayout() {
        if (connected) {
            rightVBox.setDisable(false);
            wifiSettings.setDisable(false);
            txtPort.setDisable(true);
        } else {
            rightVBox.setDisable(true);
            wifiSettings.setDisable(true);
            txtPort.setDisable(false);
        }
    }

}
