/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.CommPortSender;
import Model.Communicator;
import Model.ProtocolImpl;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 */
public class CommunicationWindowController {
    private static JTextField jTFStatusRecibido;
    private static JTextField jTFStatusEnviado;
    private static JTextField jTFTerminal;
    private static JComboBox jCBTerminalDestino;
    private static JTextField jTFMensajeDestino;
    
    public static void initOutlets(JTextField jTFStatusRecibido, JTextField jTFStatusEnviado, JTextField jTFTerminal,JComboBox jCBTerminalDestino, JTextField jTFMensajeDestino) {
        CommunicationWindowController.jTFStatusRecibido = jTFStatusRecibido;
        CommunicationWindowController.jTFStatusEnviado = jTFStatusEnviado;
        CommunicationWindowController.jTFTerminal = jTFTerminal;
        CommunicationWindowController.jCBTerminalDestino = jCBTerminalDestino;
        CommunicationWindowController.jTFMensajeDestino = jTFMensajeDestino;
        CommunicationWindowController.jTFTerminal.setText(Communicator.getTerminalName());
        loadTerminalDestinoOnComboBox();
    }
    
    public static void loadTerminalDestinoOnComboBox() {
        for (int i = 1 ; i < 100 ; i++) {
            if (Integer.parseInt(CommunicationWindowController.jTFTerminal.getText()) != i)
                CommunicationWindowController.jCBTerminalDestino.addItem(i);
        }      
    }
    
    public static void senMessage() { 
      CommPortSender.send(new ProtocolImpl().getMessage("HELLO\n"));
    }
    
}
