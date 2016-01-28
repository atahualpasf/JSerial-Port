/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;
import javax.swing.JComboBox;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 */
public class Communicator implements SerialPortEventListener
{
    //passed from main GUI
    //GUI window = null;

    //for containing the ports that will be found
    private static Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private static HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private static CommPortIdentifier selectedPortIdentifier = null;
    private static SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private static InputStream input = null;
    private static OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private static boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    private static String portName = null;
    private static String portId = null;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    private static String logText = "";
    public Communicator(String portName, String portId, String selectedPort) throws IOException, Exception
   {
      Communicator.portName = portName;
      Communicator.portId = portId;
      Communicator.connect(selectedPort);
      if (!Communicator.getConnected()) {
          throw new Exception("Lo siento pero no puedo realizar la conexión en el puerto.");
      }
   }
    
    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public static void searchForPorts(JComboBox jCBPorts)
    {
        ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                jCBPorts.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public static void connect(String selectedPort)
    {
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

        CommPort commPort = null;
        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open(portName, TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
            System.out.println(commPort.getName());
            //for controlling GUI elements
            setConnected(true);

            //logging
            logText = selectedPort + " opened successfully.";
            
            // setup serial port reader  
            CommPortSender.setWriterStream(serialPort.getOutputStream());
            new CommPortReceiver(serialPort.getInputStream()).start();
            //window.txtLog.setForeground(Color.black);
            //window.txtLog.append(logText + "\n");

            //CODE ON SETTING BAUD RATE ETC OMITTED
            //XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

            //enables the controls on the GUI if a successful connection is made
            //window.keybindingController.toggleControls();
        }
        catch (PortInUseException e)
        {
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            System.out.println(logText + "\n");
            //window.txtLog.setForeground(Color.RED);
            //window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            System.out.println(logText + "\n");
            //window.txtLog.append(logText + "\n");
            //window.txtLog.setForeground(Color.RED);
        }
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public static boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;
        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            writeData(0, 0);
            
            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
            return successful;
        }
        //return true;
    }

    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public static void initListener()
    {
        /*try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }*/
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public static void disconnect()
    {
        //close the serial port
        try
        {
            writeData(0, 0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);
            //window.keybindingController.toggleControls();

            logText = "Disconnected.";
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }
    }

    final static public boolean getConnected()
    {
        return Communicator.bConnected;
    }

    public static void setConnected(boolean bConnected)
    {
        Communicator.bConnected = bConnected;
    }
    
    final static public String getPortName()
    {
        return Communicator.portName;
    }
    
    public static void setPortName(String portName)
    {
        Communicator.portName = portName;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    public void serialEvent(SerialPortEvent evt) {
        /*if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII)
                {
                    logText = new String(new byte[] {singleData});
                    //window.txtLog.append(logText);
                    System.out.println(logText);
                }
                else
                {
                    //window.txtLog.append("\n");
                }
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                //window.txtLog.setForeground(Color.red);
                //window.txtLog.append(logText + "\n");
            }
        }*/
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public static void writeData(int leftThrottle, int rightThrottle)
    {
        try
        {
            output.write(leftThrottle);
            output.flush();
            //this is a delimiter for the data
            output.write(DASH_ASCII);
            output.flush();
            
            output.write(rightThrottle);
            output.flush();
            //will be read as a byte so it is a space key
            output.write(SPACE_ASCII);
            output.flush();
        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }
    }
    
    public static void main(String portAppName, String portId, String serialPort) throws Exception
   {
      try {
         Communicator communicator = new Communicator(portAppName, portId, serialPort);
      }
      catch (IOException e) {
         System.out.println("Caught Exception: " + e.toString());
      }
   }
}
