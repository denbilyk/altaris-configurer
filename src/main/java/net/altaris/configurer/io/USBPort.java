package net.altaris.configurer.io;

import javax.usb.*;
import java.util.List;

/**
 * @author denis.bilyk.
 */
public class USBPort {

    /**
     * The vendor ID of the missile launcher.
     */
    private static final short VENDOR_ID = 0x0483;

    /**
     * The product ID of the missile launcher.
     */
    private static final short PRODUCT_ID = 0x7304;


    @SuppressWarnings("unchecked")
    public static UsbDevice findMissileLauncher(UsbHub hub) {
        UsbDevice launcher = null;

        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            if (device.isUsbHub()) {
                launcher = findMissileLauncher((UsbHub) device);
                if (launcher != null) return launcher;
            } else {
                UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
                if (desc.idVendor() == VENDOR_ID &&
                        desc.idProduct() == PRODUCT_ID) return device;
            }
        }
        return null;
    }


    public static void main(String[] args) throws UsbException {
        UsbDevice device = findMissileLauncher(
                UsbHostManager.getUsbServices().getRootUsbHub());
        if (device == null) {
            System.err.println("Missile launcher not found.");
            System.exit(1);
            return;
        }

        // Claim the interface
        UsbConfiguration configuration = device.getUsbConfiguration((byte) 1);
        UsbInterface iface = configuration.getUsbInterface((byte) 0);
        iface.claim(usbInterface -> true);

        UsbControlIrp usbControlIrp = device.createUsbControlIrp((byte) (UsbConst.REQUESTTYPE_TYPE_CLASS | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE), (byte) 0x09, (byte) 2, (byte) 1);
        usbControlIrp.setData(new byte[]{1, 4, 5, 77, 3, 4, 55, 3, 66, 7});
        device.syncSubmit(usbControlIrp);

    }
}
