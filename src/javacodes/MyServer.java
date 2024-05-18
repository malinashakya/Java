/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
import java.net.*;
public class MyServer {
    public static void main(String[] args) {
        try{
        ServerSocket serverSocket = new ServerSocket(3000); 
        serverSocket.accept();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
