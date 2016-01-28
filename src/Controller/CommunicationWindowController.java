/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.CommPortSender;
import Model.Communicator;
import Model.ProtocolImpl;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static String PROTOCOL_SYMBOL = ":";
    private static String PROTOCOL_START = "00000000";
    private static String PROTOCOL_END = "00000000";
    
    public static void initOutlets(JTextField jTFStatusRecibido, JTextField jTFStatusEnviado, JTextField jTFTerminal,JComboBox jCBTerminalDestino, JTextField jTFMensajeDestino, JTextArea jTAConsole) {
        CommunicationWindowController.jTFStatusRecibido = jTFStatusRecibido;
        CommunicationWindowController.jTFStatusEnviado = jTFStatusEnviado;
        CommunicationWindowController.jTFTerminal = jTFTerminal;
        CommunicationWindowController.jCBTerminalDestino = jCBTerminalDestino;
        CommunicationWindowController.jTFMensajeDestino = jTFMensajeDestino;
        CommunicationWindowController.jTAConsole = jTAConsole;
        CommunicationWindowController.jTFTerminal.setText(Communicator.getTerminalName());
        loadTerminalDestinoOnComboBox();
        CommunicationWindowController.jTFStatusEnviado.setBackground(Color.LIGHT_GRAY);
        CommunicationWindowController.jTFStatusRecibido.setBackground(Color.LIGHT_GRAY);
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
            CommunicationWindowController.jTFStatusEnviado.setBackground(Color.RED);
            String messageWithProtocol = "00000000" + CommunicationWindowController.PROTOCOL_SYMBOL + 
            CommunicationWindowController.jTFTerminal.getText() + CommunicationWindowController.PROTOCOL_SYMBOL + 
            Integer.toString((int) CommunicationWindowController.jCBTerminalDestino.getSelectedItem()) + CommunicationWindowController.PROTOCOL_SYMBOL +
            messageWithoutProtocol + CommunicationWindowController.PROTOCOL_SYMBOL + "00000000";
            System.out.println("SENDING MESSAGE WITHOUT PROTOCOL: " + messageWithoutProtocol);
            System.out.println("SENDING MESSAGE WITH PROTOCOL: " + messageWithProtocol);
            CommPortSender.send(new ProtocolImpl().getMessage(messageWithProtocol));
            CommunicationWindowController.jTAConsole.append("DESTINO: " + Integer.toString((int) CommunicationWindowController.jCBTerminalDestino.getSelectedItem())
            + " - MENSAJE: " + messageWithoutProtocol + "\n");
        }
        CommunicationWindowController.jTFStatusEnviado.setBackground(Color.LIGHT_GRAY);
    }
    
    public static void receivedMessage(String receivedMessage) {
        CommunicationWindowController.jTFStatusEnviado.setBackground(Color.GREEN);
        System.out.println("RECEIVED MESSAGE WITH PROTOCOL: " + receivedMessage);
        String[] message = receivedMessage.split(CommunicationWindowController.PROTOCOL_SYMBOL);
        if (message[0].equals(CommunicationWindowController.PROTOCOL_START) && 
        message[4].equals(CommunicationWindowController.PROTOCOL_END)) {
            if (message[2].equals(CommunicationWindowController.jTFTerminal.getText())) {
                CommunicationWindowController.jTAConsole.append("ORIGEN: " + message[1] + " - MENSAJE: " + message[3] + "\n");
            } else if (message[2].equals("99")) {
                if (!message[1].equals(CommunicationWindowController.jTFTerminal.getText())) {
                    CommunicationWindowController.jTAConsole.append("ORIGEN: " + message[1] + " - MENSAJE: " + message[3] + "\n");
                    CommPortSender.send(new ProtocolImpl().getMessage(receivedMessage));
                    System.out.println("BROADCAST - FORWARD MESSAGE");
                }
            } else {
                CommPortSender.send(new ProtocolImpl().getMessage(receivedMessage));
                System.out.println("FORWARD MESSAGE");
            }
        }
        CommunicationWindowController.jTFStatusEnviado.setBackground(Color.LIGHT_GRAY);
    }
    
}
