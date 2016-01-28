/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.CommPortSender;
import Model.ProtocolImpl;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 */
public class CommunicationWindowController {    
    
    public static void senMessage() { 
      CommPortSender.send(new ProtocolImpl().getMessage("HELLO\n"));
    }
    
}
