package com.uc.ht;
import java.io.*;
import java.util.*;
import javax.comm.*;

public class SimpleWrite {
    static Enumeration portList;
    static CommPortIdentifier portId;
    static String messageString = "";
    static SerialPort serialPort;
    static OutputStream outputStream;

    public static void main(String[] args) throws IOException{
        portList = CommPortIdentifier.getPortIdentifiers();
		int i;
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM5")) {
               // if (portId.getName().equals("/dev/term/a")) {
			while(( i=System.in.read()) != 13) 				
			messageString += (char)i; 		           
		try {
                        serialPort = (SerialPort)
                            portId.open("SimpleWriteApp", 2000);
                    } catch (PortInUseException e) {System.out.println(e);}
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {System.out.println(e);}
                    try {
                        serialPort.setSerialPortParams(57600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {System.out.println(e);}
                    try { 				System.out.println(messageString);
                        outputStream.write(messageString.getBytes());
                    } catch (IOException e) {System.out.println(e);} 			
                }
            }
        }
    }
}