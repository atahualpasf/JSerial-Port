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
import javax.swing.JComboBox;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 */
public class Communicator {
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
    private static String appName = null;
    private static String terminal = null;
    private static String port = null;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    private static String logText = "";
    public Communicator(String appName, String selectedTerminal, String selectedPort) {
      Communicator.appName = appName;
      Communicator.terminal = selectedTerminal;
      Communicator.port = selectedPort;
      Communicator.connect(selectedPort);
   }
    
    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public static void searchForPorts(JComboBox jCBPorts) {
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
    public static void connect(String selectedPort) {
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

        CommPort commPort = null;
        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open(terminal, TIMEOUT);
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

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public static void disconnect() {
        //close the serial port
        try {
            //serialPort.removeEventListener();
            serialPort.getOutputStream().close();
            serialPort.getInputStream().close();
            serialPort.close();
            setConnected(false);

            logText = "Disconnected.";
            System.out.println("Terminal:" + Communicator.getTerminalName() + " - Port:" + Communicator.getPort());
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }
        catch (Exception e) {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            System.out.println(logText);
            //window.txtLog.setForeground(Color.red);
            //window.txtLog.append(logText + "\n");
        }
    }

    final static public boolean getConnected() {
        return Communicator.bConnected;
    }

    public static void setConnected(boolean bConnected) {
        Communicator.bConnected = bConnected;
    }
    
    final static public String getTerminalName() {
        return Communicator.terminal;
    }
    
    public static void setTerminalName(String terminal) {
        Communicator.terminal = terminal;
    }
    
    final static public String getPort() {
        return Communicator.port;
    }
    
    public static void setPort(String port) {
        Communicator.port = port;
    }
    
    public static void main(String portAppName, String portId, String serialPort) {
        Communicator communicator = new Communicator(portAppName, portId, serialPort);
   }
}
