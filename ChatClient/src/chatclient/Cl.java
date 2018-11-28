/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Mesh;

/**
 *
 * @author Nicola
 */
public class Cl extends Thread{
    
    BufferedReader in = null;
    PrintStream out = null;
    private Client cl;
    private int i;
    Socket socket;
    private String user;
    private String pass;
    private boolean disconnetti;
    
    public Cl(Client cl)
    {
        this.cl = cl;
        i = 0;
        disconnetti = false;
    };
    
    @Override
    public void run()
    {    

            try
            {
                socket = new Socket("localhost", 4000);         
                
                user = cl.getUsername();
                pass = cl.getPassword();
                
                if(cl.getShowStatus() == true)
                {
                    user = user + " check";
                }
                else
                {
                    user = user + " uncheck";
                }
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream(), true); 
                out.println(user);
                out.println(pass);
                
            }
            catch(Exception e)
            {
                
                System.out.println(e.getMessage());
            }
            
            String message = "";
            
            while(disconnetti == false)
            {
                try {
                    
                    if(disconnetti == false)
                    {
                        message = in.readLine();
                                
                        String[] array = message.split("/");                        
                        
                        if(array[0].equals("Utenti"))
                        {
                            cl.setUtenti(array);
                        }
                        else
                        {                            
                            cl.setMessaggi(cl.getMessaggi() + message + "\n");
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Cl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    public void connect()
    {
        if(i == 0)
        {  
            this.start();
            i++;
            
        }
        else
        {
            cl.setUser("Gia loggato");
        }
    }
    
    public void disconnetti()
    {
        try
        { 
            out = new PrintStream(socket.getOutputStream(), true);

            out.println("Disconnetti");   
            disconnetti = true;
            
            i = 0;
            
            in.close();
            out.close();
            socket.close();
            
                        
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void invia(String Testo)
    {     
            try
            { 
                out = new PrintStream(socket.getOutputStream(), true);
                
                out.println(Testo);
                
                cl.setMessaggio();
            }
                
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        
    }
    
    public void inviaPrivate(String utente, String Testo)
    {     
            try
            { 
                out = new PrintStream(socket.getOutputStream(), true);
                
                out.println("Private message");
                out.println(utente);
                out.println(Testo);
                
                cl.setMessaggio();
            }
                
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        
    }
    
}
