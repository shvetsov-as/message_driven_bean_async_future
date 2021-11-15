/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "Call", urlPatterns = {"/Call"})
public class Call extends HttpServlet {

    AsyncBean asyncBean = lookupAsyncBeanBean();

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private QueueConnectionFactory factory;

    @Resource(lookup = "jms/MessageQueue")
    private Queue queue;
    
    String result = null;
    
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Call</title>");
            out.println("</head>");
            out.println("<body>");
            asyncBean.processMessage("start");
            out.println("<h1>Запуск отправки сообщения в MDB sendAsyncMsg(); " + request.getContextPath() + "</h1>");
            //sendAsyncMsg();
            
            Future<String> fs = asyncBean.returnMethod("12345"); //poluchaem budushee znachenie
            System.out.println("Async Call");
            //System.out.println("isDone() returns " + fs.isDone()); tolko ne na stateful binah!
            
            
            try {
                result = fs.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(Call.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Call.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
//            while(!fs.isDone()){ tolko ne na stateful binah!
//                System.out.println("returnMethod is not finish yet");
//            }
            
            
            
                System.out.println("returnMethod is finished, result = " + result);
            
            
            
            
            out.println("<h1>End " + request.getContextPath() + "</h1>");
            out.println("<h1>End " + result + "</h1>");
            
            
            out.println("<h1>Запуск отправки сообщения в MDB sendMessages(); " + request.getContextPath() + "</h1>");
            //sendMessages();

            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void sendMessages() {
        try {
            if (factory != null) {
                QueueConnection conn = factory.createQueueConnection();
                QueueSession ses = conn.createQueueSession(true, 0);
                QueueSender sender = ses.createSender(queue);
                TextMessage tm = ses.createTextMessage("start");
                sender.send(tm);
                for(int i = 0; i<1000; i++){
                    tm = ses.createTextMessage("work");
                    sender.send(tm);
                }
                tm = ses.createTextMessage("end");
                sender.send(tm);

            }
        } catch (JMSException e) {
            System.out.println("Error send message " + e);
        }
    }

    private AsyncBean lookupAsyncBeanBean() {
        try {
            Context c = new InitialContext();
            return (AsyncBean) c.lookup("java:global/AsyncTest/AsyncBean!at.AsyncBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private void sendAsyncMsg() {
        
        for(int i = 0; i<1000; i++){
                    asyncBean.processMessage("work");
                }
        
        asyncBean.processMessage("end");
    }

}
