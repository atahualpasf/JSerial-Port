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
import javax.swing.JTextArea;
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
    private static JTextArea jTAConsole;
    private static String protocolSymbol = ":";
    
    public static void initOutlets(JTextField jTFStatusRecibido, JTextField jTFStatusEnviado, JTextField jTFTerminal,JComboBox jCBTerminalDestino, JTextField jTFMensajeDestino, JTextArea jTAConsole) {
        CommunicationWindowController.jTFStatusRecibido = jTFStatusRecibido;
        CommunicationWindowController.jTFStatusEnviado = jTFStatusEnviado;
        CommunicationWindowController.jTFTerminal = jTFTerminal;
        CommunicationWindowController.jCBTerminalDestino = jCBTerminalDestino;
        CommunicationWindowController.jTFMensajeDestino = jTFMensajeDestino;
        CommunicationWindowController.jTAConsole = jTAConsole;
        CommunicationWindowController.jTFTerminal.setText(Communicator.getTerminalName());
        loadTerminalDestinoOnComboBox();
    }
    
    public static void loadTerminalDestinoOnComboBox() {
        for (int i = 1 ; i < 100 ; i++) {
            if (Integer.parseInt(CommunicationWindowController.jTFTerminal.getText()) != i) {
                CommunicationWindowController.jCBTerminalDestino.addItem(i);
            }
        }      
    }
    
    public static boolean disconnect() { 
        Communicator.disconnect();
        return Communicator.getConnected();
    }
    
    public static void sendingMessage() {
        String messageWithoutProtocol = CommunicationWindowController.jTFMensajeDestino.getText();
        if (!messageWithoutProtocol.trim().isEmpty()) {
            String messageWithProtocol = "00000000" + CommunicationWindowController.protocolSymbol + 
            CommunicationWindowController.jTFTerminal.getText() + CommunicationWindowController.protocolSymbol + 
            Integer.toString((int) CommunicationWindowController.jCBTerminalDestino.getSelectedItem()) + CommunicationWindowController.protocolSymbol +
            messageWithoutProtocol + CommunicationWindowController.protocolSymbol + "00000000";
            System.out.println("SENDING MESSAGE WITHOUT PROTOCOL: " + messageWithoutProtocol);
            System.out.println("SENDING MESSAGE WITH PROTOCOL: " + messageWithProtocol);
            CommPortSender.send(new ProtocolImpl().getMessage(messageWithProtocol));
            CommunicationWindowController.jTAConsole.append("ORIGEN - " + 
            Integer.toString((int) CommunicationWindowController.jCBTerminalDestino.getSelectedItem()) + 
            CommunicationWindowController.protocolSymbol + messageWithoutProtocol + "\n");
        }
    }
    
    public static void receivedMessage(String receivedMessage) {
        System.out.println("RECEIVED MESSAGE WITH PROTOCOL: " + receivedMessage);
        /*String[] message = msj.split(";");
        System.out.println(msj);
        
        if (message[1].equals(ValoresUniversales.getNumterminal())){
            
            System.out.println("DESTINO ES  "+ ValoresUniversales.getNumterminal()+"   MENSAJE ES PARA   " +message[2]);
                campomsj.setText(message[0]);
                terminalorigen.setText(message[2]);
            
        } else {
            
            System.out.println("DESTINO ES  "+ ValoresUniversales.getNumterminal()+"   MENSAJE ES PARA   " +message[2]);
            // Primer campo TERMINAL ORIGEN, segundo campo MENSAJE A ENVIAR, tercer campo PUERTO
         //   ControladorPrincipalSerial.enviandoMensaje(message[2], message[0], message[1], ValoresUniversales.getPuerto());
        
        }*/
    }
    
}
