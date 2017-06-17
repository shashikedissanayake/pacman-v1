/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author thz
 */
@WebServlet(urlPatterns = {"/gameUpdate"})
public class GameUpdate extends HttpServlet {

     private GameGrid grid;
    
    @Override
    public void init(final ServletConfig config) {
        grid = new GameGrid();        
        
        grid.addPlayer();
        grid.addPlayer();
        grid.addPlayer();
        grid.addPlayer();
        
        Logger.getGlobal().log(Level.INFO, "Started the Game Server");
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException{
        
        
        response.setContentType("text/event-stream;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session=request.getSession();
        
        /*
        if(session.isNew()){
           session.setAttribute("number",new Integer(grid.addPerson()));
           sendData(out);    
        }
        */
        
        
        sendData(out);
        
        try{
        while(!Thread.interrupted()){
                synchronized (this) {
                    wait();
                    sendData(out);
                }
            }
        }catch(InterruptedException e){
          Logger.getGlobal().log(Level.INFO, "Exiting");
        }

    }

    private void sendData(PrintWriter out){
        out.print("data: ");
        out.println("{" + "\"PLAYERS\" : "+ grid.getPlayers() + ",\"DOTS\": "+ grid.gridtoCoordinates() + "}" );
        out.println();
        out.flush();
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        
        Logger.getGlobal().log(Level.INFO,req.getParameter("press")+"is presed");          
        
        HttpSession session=req.getSession();
        
        synchronized (this) {
        grid.moveonGrid(0,Integer.parseInt(req.getParameter("keypress")));
        notifyAll();
        }
    }
   

    
    
    
}
