package com.uc.ht;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This version of the TwoWaySerialComm makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */
public class TwoWayNewComm
{
	public TwoWayNewComm()
    {
        super();
    }
    
    // Prints the available list of ports for System
    static void listPorts()
    {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Available Ports: ");
        while ( portEnum.hasMoreElements() ) 
        {
        	// Identifies the available port number and its type
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            
            System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
        }        
    }
    
    //Returns Port Type.
    static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            // We need Serial Port
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
    
    // Connect method will connect to port and handles the thread for Read/Write threads.  
    void connect ( String portName ) throws Exception
    {
    	// Connects to Specific port that specified below in Main method
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        System.out.println("Connected to Port: "+portName);
        
        // Exception is thrown if their is any other program is connect to it.
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open("UltraClenz Gateway&Hub Diagnosis Software",2000);
            
            // Process goes thru if the connected port is Serial Port
            if ( commPort instanceof SerialPort )
            {
            	System.out.println("...Welcome UltraClenz Gateway/Hub Diagnosis Software...");
            	System.out.println("Current Timestamp: "+new TimeStamp().getDateTime());
            	System.out.println("Enter '?' for Help");
            	
            	//Serial Port Object Created for port settings
                SerialPort serialPort = (SerialPort) commPort;
                
                //Port Settings Needs to be 57600,8 Bits, 1 stop bit, Parity None
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
               // serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
                // Stream objects for IO
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                               
                // Below thread gives access to write to serial port
                (new Thread(new SerialWriter(out))).start();
                
                // Below Method handles the process of available data coming thru from the serial port. 
                serialPort.addEventListener(new SerialReader(in));
                
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this software.");
            }
        }     
    }
    
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    // Inner Class: Reads data from COMM port
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        
        // Collects the data in to byte with max 1024 buffer size
        private byte[] byteBuffer = new byte[5000];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) 
        {
            
        	int  iData = 0;
        	byte bData = 0;
        	
        	try
            {
            	// Creates a Log file
            	int iLen = 0;
                
                //data saves in to the buffer 
                while ( ( bData = (byte)in.read()) > -1 )
                	{
                    if ( bData == '\n' ) 
                    	{
                        break;
                    	}
                    
                    if(bData < 0)
                    	{
                    	iData = bData + 128;
                    	}
                    else
                    	{
                    	iData = bData;
                    	}
                    
                    byteBuffer[iLen++] = (byte)iData;  
                    //System.out.println(byteBuffer[iLen++]);
                	}
                
                // Prints the buffer in console
                System.out.print(new String(byteBuffer,0,iLen));
                
                //Creating a byte array and passing it to the String constructor
                String eventData = new String(byteBuffer,0,iLen);
                
                // Saves the data into CSV created before 
                //new CSVSaveData().sendtoCSV(eventData);
            }
            catch ( IOException e )
            {
            	try {
					new CSVSaveData().sendtoCSV(e.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }

    /** */
    // Thread to write data in to serial port
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        public SerialWriter ( OutputStream out ) throws IOException
        {
        	
            this.out = out;
        }
        
        public void run ()
        {
            try
            {   
            	int a = 0;
               int c = 10;
                // Writes the data if entered
               do
               {
            	   this.out.write(c);
                   a=c;
               }
               
                while ( ( c = System.in.read()) > -1 );
                {
                	//Sends the data out to the port
                    
                }   
                out.close();
            }
            catch ( IOException e )
            {
            	try {
					new CSVSaveData().sendtoCSV(e.toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }
    

    
    public static void main ( String[] args )
    {
    	// Listports() called to Show the available ports
    	listPorts();
        try
        {
        	// Specify the port that connected to.
            (new TwoWayNewComm()).connect("COM5");
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}