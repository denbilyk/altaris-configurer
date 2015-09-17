package net.altaris.configurer.ui;

/**
 * @author denis.bilyk.
 */
public class JavaVersion {

    /*
            //HBox top = addTop();
        //Node mainArea = addMainArea();
        //VBox buttonGroup = addRight();
        //BorderPane main = new BorderPane(mainArea, top, buttonGroup, null, null);

    *   VBox addRight() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        Button wifiSettings = new Button("Status");
        wifiSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onBtnWifiStatus();
            }
        });
        vbox.getChildren().add(wifiSettings);
        return vbox;
    }

    HBox addTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #e1e1e1;");

        ComboBox<String> ports = new ComboBox<>();
        //ports.setItems(getAvailablePorst());
        ports.setEditable(true);
        Label title = new Label("Select COM port: ");
        title.setLabelFor(ports);
        title.setPadding(new Insets(5, 0, 0, 0));
        Button openPort = new Button("Open");
        openPort.setPrefSize(80, 30);
        openPort.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isConnect) {
                    isConnect = btnConnectHandler(ports.getValue());
                    openPort.setText("Close");
                    addWifiSettings().setDisable(false);
                } else {
                    System.out.println("Closed!");
                    addWifiSettings().setDisable(true);
                    openPort.setText("Open");
                    isConnect = false;
                }

            }
        });
        hbox.getChildren().addAll(title, ports, openPort);
        return hbox;
    }

    private HBox addWifiSettings() {
        TextField ssid = new TextField();
        Label ssidLabel = new Label("SSID: ");
        ssidLabel.setPadding(new Insets(5, 0, 0, 0));
        ssidLabel.setLabelFor(ssid);
        TextField wifiPass = new TextField();
        Label wifiPassLbl = new Label("Password: ");
        wifiPassLbl.setLabelFor(wifiPass);
        wifiPassLbl.setPadding(new Insets(5, 0, 0, 10));
        Button wifiSaveBtn = new Button("Save");


        HBox hBox = new HBox(ssidLabel, ssid, wifiPassLbl, wifiPass, wifiSaveBtn);
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setDisable(true);
        return hBox;
    }


    private void onBtnWifiStatus() {

    }

    private boolean btnConnectHandler(String portName) {
        try {
            System.out.println("Connected!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    Node addMainArea() {
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(1);
        grid.setStyle("-fx-background-color: #edf8f5");
        HBox wifiSettings = addWifiSettings();
        grid.add(wifiSettings, 0, 0);

        Label info = new Label("Select Serial port");
        info.setFont(new Font("Arial", 16));
        grid.add(info, 0, 1);

        return grid;
    }
    * */
}
