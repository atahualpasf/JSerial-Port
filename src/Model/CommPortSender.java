/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;  
import java.io.OutputStream;  
   
public class CommPortSender {  
   
    static OutputStream out;  
      
    public static void setWriterStream(OutputStream out) {  
        CommPortSender.out = out;  
    }  
      
    public static void send(byte[] bytes) {  
        try {  
            //System.out.println("SENDING: " + new String(bytes, 0, bytes.length));  
              
            // sending through serial port is simply writing into OutputStream  
            out.write(bytes);  
            out.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
} 