package com.uc.ht;

import javax.comm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TwoWayNewComm
{
	static int writeReset = 0;
    public TwoWayNewComm()
    {
        super();
    }
    
    static void listPorts()
    {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Available Ports: ");
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
        }        
    }
    
    static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
           
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            
            
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        System.out.println("Connected to Port: "+portName);
        
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open("UltraClenz Gateway&Hub Diagnosis Software",2000);
            
             if ( commPort instanceof SerialPort )
            {
            	
                SerialPort serialPort = (SerialPort) commPort;
                
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN);
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                               
                (new Thread(new SerialWriter(out))).start();
                
                serialPort.addEventListener(new SerialReader(in));
                
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this software.");
            }
        }     
    }
    
   
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) 
        {
            int data;
            try
            {
            	int len = 0;
                
                while ( ( data = in.read()) > -1 )
                {

                    if ( data == '\n' ) 
                    {
                        break;
                    }
                    buffer[len++] = (byte) data;
                    
                }
                
                System.out.print(new String(buffer,0,len));
        //        String eventData = new String(buffer,0,len);
             }
            catch ( IOException e )
            {
            	e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
static  int c = 0;
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out ) throws IOException
        {
        	
            this.out = out;
       }
        public SerialWriter()
        {
        	
        }
     
        public void run ()
        {
            try
            {   
            	
            	 while ( ( c = System.in.read()) > -1 )
                {
                     this.out.write(c);
                
                }         
            }
            catch ( IOException e )
            {
            	e.printStackTrace();
                System.exit(-1);
            }            
        }
    }
    

    
    public static void main ( String[] args )
    {
    	listPorts();
        try
        {
            (new TwoWayNewComm()).connect("COM5");
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
