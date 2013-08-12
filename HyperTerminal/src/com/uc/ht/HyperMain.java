package com.uc.ht;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

public class HyperMain implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;
    static OutputStream outputStream;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    public static void main(String[] args) throws IOException 
    {
        portList = CommPortIdentifier.getPortIdentifiers();
        int i;
        while (portList.hasMoreElements()) 
        {
            portId = (CommPortIdentifier) portList.nextElement();
            
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) 
            {
            	if (portId.getName().equals("COM5")) 
            	{
            		System.out.println("Welcome To UltraClenz Gateway Diagnosis Center");
            		HyperMain reader = new HyperMain();
                }
            }
            
        }
    }

    public HyperMain() 
    {
        try 
        {
            serialPort = (SerialPort) portId.open("UCGatewayApp", 2000);
        } 
        catch (PortInUseException e) 
        {	
        	System.out.println("Port In Use: "+e);
    	}
        try 
        {
            inputStream = serialPort.getInputStream();
            
            //System.out.println("Driver: "+inputStream.toString());
        } 
        catch (IOException e) 
        {
        	System.out.println("I/O Exception: "+e);
    	}
        try 
        {
            serialPort.addEventListener(this);
        } 
        catch (TooManyListenersException e) 
        {
        	System.out.println("Too Many Listeners: "+e);
    	}
        serialPort.notifyOnDataAvailable(true);
        try 
        {
            serialPort.setSerialPortParams(57600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } 
        catch (UnsupportedCommOperationException e) 
        {
        	System.out.println("Please check the COMM operations: "+e);
    	}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() 
    {
    	try 
    	{
    		System.out.println("Connecting to port...............");
    		Thread.sleep(20000);
        } 
    	catch (InterruptedException e) 
    	{
    		System.out.println("Thread: "+e);
		}
    }

    public void serialEvent(SerialPortEvent event) 
    {
        switch(event.getEventType()) 
        {
        	case SerialPortEvent.BI:
        	case SerialPortEvent.OE:
        	case SerialPortEvent.FE:
        	case SerialPortEvent.PE:
        	case SerialPortEvent.CD:
        	case SerialPortEvent.CTS:
        	case SerialPortEvent.DSR:
        	case SerialPortEvent.RI:
        	case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        		break;
        	case SerialPortEvent.DATA_AVAILABLE:
        		byte[] readBuffer = new byte[1024];
        		try 
        		{
        			while (inputStream.available() > 0) 
        			{
        				int numBytes = inputStream.read(readBuffer);
        			}
        			System.out.print(new String(readBuffer)+"\n");
        		} 
        		catch (IOException e) 
        		{
        			System.out.println("I/O Exception: "+e);
    			}
        		break;
        }
    }
}