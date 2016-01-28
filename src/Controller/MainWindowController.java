/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Communicator;
import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.JComboBox;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 */
public class MainWindowController {
    private static JComboBox jCBPorts;
    private static JComboBox jCBTerminals;
    private static Enumeration ports = null;
    
    public static void initOutlets(JComboBox jCBPorts, JComboBox jCBTerminals) {
        MainWindowController.jCBPorts = jCBPorts;
        MainWindowController.jCBTerminals = jCBTerminals;
        Communicator.searchForPorts(MainWindowController.jCBPorts);
        loadTerminalsOnComboBox();
    }
    
    public static boolean connectPort() {
        String selectedPort = (String) jCBPorts.getSelectedItem();
        String selectedTerminal = Integer.toString((int) jCBTerminals.getSelectedItem());
        Communicator.main("JSerial-Terminal", selectedTerminal, selectedPort);
        return Communicator.getConnected();
    }
    
    public static void loadTerminalsOnComboBox() {
        for (int i = 1 ; i < 99 ; i++) {
            MainWindowController.jCBTerminals.addItem(i);
        }
    }
}
