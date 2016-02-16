package net.altaris.configurer.io;

import net.altaris.configurer.ui.Controller;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

import java.util.Collections;
import java.util.Map;

/**
 * @author denis.bilyk.
 *         <p>
 *         hid4java build from https://github.com/gary-rowe/hid4java.git
 *         mvn clean compile exec:java -Dexec.mainClass="UsbHidTrezorV1Example"
 */
public class HidPort implements HidServicesListener {

    private static int VENDOR_ID = 0x0483;
    private static int PRODUCT_ID = 0x7304;
    private final static String SERIAL_NUM = "8788256C5151";
    private HidServices hidServices;
    private Controller controller;


    public HidPort(Controller controller) {
        this.controller = controller;
    }

    public HidPort() {
    }

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
        if (controller != null)
            controller.handleHidDeviceAttachedCallback();
        //System.out.println("Attached");
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent event) {
        if (controller != null)
            controller.handleHidDeviceDetachedCallback();
        //System.out.println("Detached");
    }

    @Override
    public void hidFailure(HidServicesEvent event) {
        System.out.println("HID Failure");
    }


    public boolean isDeviceDetected() {
        return getDevice() != null;
    }

    public HidDevice getDevice() {
        HidDevice dev = null;
        try {
            dev = hidServices.getHidDevice(VENDOR_ID, PRODUCT_ID, SERIAL_NUM);
        } catch (NullPointerException ex) {
            System.out.println("Dev not connected!");
        }
        return dev;
    }

    public Map<String, String> readConfig() {
        return Collections.emptyMap();
    }

    public static void main(String[] args) throws InterruptedException {
        HidPort hidPort = new HidPort();
        hidPort.initHidDevices();
        hidPort.printHidDevices();
        HidDevice hidDevice = hidPort.hidServices.getHidDevice(VENDOR_ID, PRODUCT_ID, SERIAL_NUM);
        readConfig(hidDevice);
       // writeCfg(hidDevice);

    }


    static void writeCfg(HidDevice hidDevice) throws InterruptedException {
        eraseData(hidDevice);
        writeCmd("", (byte) 0x32, 2, hidDevice);
        Thread.sleep(800);
        writeAuth("004854-AE45-22", hidDevice);
        Thread.sleep(800);
        writeHost("10.10.0.171", hidDevice);
        Thread.sleep(800);
        writePort("8080", hidDevice);
        Thread.sleep(800);
        writeSsid("infinity", hidDevice);
        Thread.sleep(800);
        writeSsidPass("0672086028", hidDevice);
        Thread.sleep(800);
        writeCmd("", (byte) 0x31, 2, hidDevice);
    }

    static void eraseData(HidDevice hidDevice) {
        hidDevice.open();
        hidDevice.sendFeatureReport(new byte[]{0x29}, (byte) 0x02);
        hidDevice.close();
    }

    static void writeAuth(String auth, HidDevice hidDevice) {
        writeCmd(auth, (byte) 0x2A, 34, hidDevice);
    }

    static void writeHost(String host, HidDevice hidDevice) {
        writeCmd(host, (byte) 0x2B, 18, hidDevice);
    }

    static void writePort(String port, HidDevice hidDevice) {
        writeCmd(port, (byte) 0x2C, 18, hidDevice);
    }

    static void writeSsid(String ssid, HidDevice hidDevice) {
        writeCmd(ssid, (byte) 0x2D, 34, hidDevice);
    }

    static void writeSsidPass(String ssidPass, HidDevice hidDevice) {
        writeCmd(ssidPass, (byte) 0x2E, 34, hidDevice);
    }


    static void readConfig(HidDevice hidDevice) {
        byte[] a = new byte[64];
        hidDevice.open();
        hidDevice.sendFeatureReport(new byte[]{0x1B}, (byte) 0x03);
        hidDevice.read(a, 2000);
        processRecaivedBytes(a);
        hidDevice.sendFeatureReport(new byte[]{0x1C}, (byte) 0x03);
        hidDevice.read(a, 2000);
        processRecaivedBytes(a);
        hidDevice.sendFeatureReport(new byte[]{0x1D}, (byte) 0x03);
        hidDevice.read(a, 2000);
        processRecaivedBytes(a);
        hidDevice.sendFeatureReport(new byte[]{0x1E}, (byte) 0x03);
        hidDevice.read(a, 2000);
        processRecaivedBytes(a);
        hidDevice.close();
    }

    private static void writeCmd(String data, byte code, int len, HidDevice hidDevice) {
        byte[] codeAuth = {code};
        byte[] newAuth = data.getBytes();
        byte[] cmd = new byte[len];
        System.arraycopy(codeAuth, 0, cmd, 0, 1);
        System.arraycopy(newAuth, 0, cmd, 1, newAuth.length);
        hidDevice.open();
        hidDevice.sendFeatureReport(cmd, (byte) 0x02);
        hidDevice.close();
    }


    private static void processRecaivedBytes(byte[] a) {
        String s = new String(a);
        String[] split = s.split(new String(new byte[]{(byte) 254}));
        for (String s1 : split) {
            System.out.println(s1.trim());
        }
    }

}
