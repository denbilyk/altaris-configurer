package net.altaris.configurer.io;

import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 * @author denis.bilyk.
 *
 * hid4java build from https://github.com/gary-rowe/hid4java.git
 * mvn clean compile exec:java -Dexec.mainClass="UsbHidTrezorV1Example"
 *
 */
public class HidPort implements HidServicesListener {

    private HidServices hidServices;

    void initHidDevices() {
        // Get HID services
        hidServices = HidManager.getHidServices();
        hidServices.addHidServicesListener(this);
    }

    void printHidDevices() {
        // Provide a list of attached devices
        hidServices.getAttachedHidDevices().forEach(System.out::println);
    }


    @Override
    public void hidDeviceAttached(HidServicesEvent event) {

    }

    @Override
    public void hidDeviceDetached(HidServicesEvent event) {

    }

    @Override
    public void hidFailure(HidServicesEvent event) {

    }

    public static void main(String[] args) {
        HidPort hidPort = new HidPort();
        hidPort.initHidDevices();
        hidPort.printHidDevices();
    }
}
