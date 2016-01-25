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
    private static Enumeration ports = null;
    
    public static void initOutlets(JComboBox jCBPorts) {
        MainWindowController.jCBPorts = jCBPorts;
        Communicator.searchForPorts(MainWindowController.jCBPorts);
    }
    
    public static boolean connectPort() throws Exception {
        //Communicator.connect(jCBPorts);
        String selectedPort = (String) jCBPorts.getSelectedItem();
        Communicator.main("AA", "6264", selectedPort);
        return true;
    }
}
