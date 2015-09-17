package net.altaris.configurer.io;

import jssc.*;

import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static jssc.SerialPort.MASK_RXCHAR;

/**
 * @author denis.bilyk.
 */
public class SPort {

    private static SerialPort serialPort;
    private Stack<String> recieveBuffer = new Stack<>();

    public void connect(String portName, int baud) {
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(baud, 8, 1, 0);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener(new PortReaderHandler());

        } catch (SerialPortException e) {
            e.printStackTrace();
        }

    }

    public boolean send(String data) throws SerialPortException {
        if (null != serialPort && serialPort.isOpened()) {
            return serialPort.writeString(data);
        } else throw new SerialPortException("Port", "open", "Port is not opened");
    }

    public void close() {
        if (null != serialPort && serialPort.isOpened())
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
                serialPort = null;
            } finally {
                serialPort = null;
            }
    }

    public String getStatus() {
        String res = "Test";
        if (null == serialPort || !serialPort.isOpened()) return "Port closed";
        try {
            serialPort.writeString("AT+CIPSTA?");
            TimeUnit.SECONDS.sleep(3);

        } catch (SerialPortException e) {
            return "Can not read status";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) throws SerialPortException {
        SPort sPort = new SPort();
        Pattern pattern = Pattern.compile("tty\\.+[A-Za-z0-9]+");
        System.out.println(Arrays.toString(SerialPortList.getPortNames("/dev/", pattern)));
    }

    private class PortReaderHandler implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    recieveBuffer.push(data);
                } catch (SerialPortException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
