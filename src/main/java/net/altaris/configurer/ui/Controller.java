package net.altaris.configurer.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import net.altaris.configurer.io.HidPort;
import net.altaris.configurer.io.SPort;
import org.hid4java.HidDevice;

import java.util.Map;


public class Controller {
    private SPort sPort = new SPort();
    private boolean connected = false;
    private static volatile boolean onTimer = false;

    @FXML
    private Label devStatusText;
    @FXML
    private ComboBox<String> txtPort;
    @FXML
    private Button btnConnect;
    @FXML
    private HBox settings;
    @FXML
    private VBox rightVBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Button btnWifiSave;
    @FXML
    private Parent root;
    private Timeline timeline;
    private HidPort hidPort;

    void init(HidPort hidPort) {
        this.hidPort = hidPort;
    }

    public void handleHidDeviceAttachedCallback() {
        devStatusText.setText("Device found!");
        btnConnect.setDisable(true);
    }

    public void handleHidDeviceDetachedCallback() {
        devStatusText.setText("Device not found!");
        btnConnect.setDisable(false);

    }


    public void handleBtnReadConfig(ActionEvent actionEvent) {
        System.out.println("Status button");
        String status = "";
        String text = "WIFI: test\nIP: 10.10.0.111 \nMask: 255.255.255.0";
        infoLabel.setText(status);

    }

    public void handleBtnConnect(ActionEvent actionEvent) throws SerialPortException {
        System.out.println("Button open");
        try{
            HidDevice device = hidPort.getDevice();
            device.open();
            Map<String, String> devConfig = hidPort.readConfig();


        } catch (NullPointerException e) {
            setUIDeviceNotFound();
        }

    }

    public void handleBtnWifiSave(ActionEvent actionEvent) throws InterruptedException {
        infoLabel.setText("Saved! Rebooting...");
        rightVBox.setDisable(true);
        settings.setDisable(true);
        timeline = new Timeline(new KeyFrame(Duration.millis(3000), ev -> {
            rightVBox.setDisable(false);
            settings.setDisable(false);
            handleBtnReadConfig(null);
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
            settings.setDisable(false);
            txtPort.setDisable(true);
        } else {
            rightVBox.setDisable(true);
            settings.setDisable(true);
            txtPort.setDisable(false);
        }
    }

    public void handleBtnRefresh(ActionEvent actionEvent) {
        if (hidPort.isDeviceDetected()) {
            setUIDeviceDetected();
        } else {
            setUIDeviceNotFound();
        }

    }

    private void setUIDeviceNotFound() {
        btnConnect.setDisable(true);
        devStatusText.setText("Device not found!");
    }

    private void setUIDeviceDetected() {
        btnConnect.setDisable(false);
        devStatusText.setText("Device found!");
    }
}
