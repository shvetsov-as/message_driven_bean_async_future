/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;

/**
 *
 * @author User
 */
@Stateful
@LocalBean
public class AsyncBean {

    private long sum = 0;
    private long time = 0;
    private long calls = 0;

    @Asynchronous
    public void processMessage(String msg) {
        calls++;

        if (msg.equals("start")) {
            time = System.currentTimeMillis();
            calls = 0;
        }

        if (msg.equals("end")) {
            System.out.println("общая длина строк: " + sum);
            System.out.println("Количество вызовов: " + sum);
            System.out.println("Время затачено : " + (System.currentTimeMillis() - time) + " mills");
        }

        int len = msg.length();
        sum += len;

    }

    
    
    
    public Future <String> returnMethod(String str) {//Future t k vozvrat y nas ne srazy
        
        int len = str.length();
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AsyncBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return new AsyncResult<>("String length: " + len);
    }

}
