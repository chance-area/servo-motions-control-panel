package ru.chancearea.servomotionscontrolpanel.utils;

import jssc.*;
import ru.chancearea.servomotionscontrolpanel.GlobalConstants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public abstract class SerialPortManager {
    public static SerialPort serialPort;
    private static ArrayList<String> strFromPort = new ArrayList<>();

    private static int idToRead = 0;

    public static String sendStringToESP32(final String _str, final boolean _needRead) {
        int writingAttempts = 3;

        String outStringFromESP = "";

        if (serialPort == null) {
            openAndInitPort();
            if (serialPort == null) return "";
        }

        try {
            for (int k = 0; k < writingAttempts; k++) {
                if (serialPort.writeString(_str, "UTF-8")) {
                    if (GlobalConstants.IS_DEBUG_MODE) System.out.println("Write done! Data: " + _str);
                    break;
                } else if (k == (writingAttempts - 1)) {
                    // error msg... *тут должен быть вывод сообщения об ошибке, но мне лень*
                    return "";
                } else {
                    try {
                        Thread.sleep(MathPlus.calculateMillisToSleep(writingAttempts));
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                }
            }

            try {
                if (_needRead) {
                    outStringFromESP = readStringFromESP32();
                }
            } catch (Exception ignore_1) { /* ignore*/ }
        } catch (SerialPortException | UnsupportedEncodingException e) { /* ignore*/ }

        return outStringFromESP;
    }

    public static String readStringFromESP32() {
        String outString = "";

        if (serialPort == null) {
            openAndInitPort();
            if (serialPort == null) return "";
        }

        if (strFromPort.size() > idToRead) {
            outString = strFromPort.get(idToRead);
            idToRead++;
        }

        return outString;
    }

    public static void openAndInitPort() {
        int openingAttempts = 3;

        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length > 0) {
            if (serialPort == null) serialPort = new SerialPort(portNames[ (portNames.length - 1) ]);
            else {
                try {
                    serialPort.closePort();
                } catch (Exception ignore) { }
            }

            for (int k = 0; k < openingAttempts; k++) {
                try {
                    //int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, false, false);
                    //port.setEventsMask(mask);
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }

                if (serialPort.isOpened()) break;
                else if (k == (openingAttempts - 1)) {
                    // error msg not found ¯\_(ツ)_/¯
                    return;
                } else {
                    try {
                        Thread.sleep(MathPlus.calculateMillisToSleep(openingAttempts));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (serialPort != null && serialPort.isOpened()) {
                try {
                    serialPort.addEventListener(new SerialPortEventListener() {
                        @Override
                        public void serialEvent(SerialPortEvent serialPortEvent) {
                            // data is available
                            if (serialPortEvent.isRXCHAR()) {
                                try {
                                    byte[] buffer = serialPort.readBytes(serialPortEvent.getEventValue());
                                    String str = new String(buffer);
                                    strFromPort.add(str);
                                } catch (SerialPortException ignore) { }
                            }
                        }
                    });
                } catch (Exception ignore) { }
            }
        }
    }

    public static String executeCommandOnESP32_USB(String _command) {
        return "";
    }
}
