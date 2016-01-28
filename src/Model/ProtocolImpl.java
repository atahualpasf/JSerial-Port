/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.CommunicationWindowController;

public class ProtocolImpl implements Protocol {  
   
    byte[] buffer = new byte[1024];  
    int tail = 0;  
      
    public void onReceive(byte b) {  

        if (b=='\n') {  
            onMessage();  
        } else {  
            buffer[tail] = b;  
            tail++;  
        }  
    }  
   
    public void onStreamClosed() {  
        onMessage();  
    }  
      
    /* 
     * When message is recognized onMessage is invoked  
     */  
    private void onMessage() {  
        if (tail!=0) {  
  
            String message = getMessage(buffer, tail);  
            //System.out.println("RECEIVED MESSAGE: " + message);  
            
            CommunicationWindowController.receivedMessage(message);
            
            System.out.println(message.charAt(4));
            tail = 0;  
        }  
    }  
      
    public byte[] getMessage(String message) {  
        return (message+"\n").getBytes();  
    }  
      
    public String getMessage(byte[] buffer, int len) {  
        return new String(buffer, 0, tail);  
    }  
} 