/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author User
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/MessageQueue")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class NewMessageBean implements MessageListener {
    
    private static long sum = 0;
    private static long time = 0;
    private static long calls = 0;
    
    
    public NewMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            calls++;
            TextMessage tm = (TextMessage) message;
            try {
                String msg = tm.getText();
                
                if(msg.equals("start")){
                    time = System.currentTimeMillis();
                    calls = 0;
                }
                
                
               if(msg.equals("end")) {
                   System.out.println("общая длина строк: " + sum);
                   System.out.println("Количество вызовов: " + sum);
                   System.out.println("Время затачено : " + (System.currentTimeMillis() - time) + " mills");
               }
                
                int len = msg.length();
                sum += len;
                
            } catch (JMSException ex) {
                System.out.println("Ошибка извлечения message");
            }
        }
        
        
        
    }
    
}
