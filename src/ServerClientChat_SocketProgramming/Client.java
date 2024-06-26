package ServerClientChat;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author malin
 */

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter; 
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket=null;
        InputStreamReader inputStreamReader=null;
        OutputStreamWriter outputStreamWriter=null;
        BufferedReader bufferedReader=null;
        BufferedWriter bufferedWriter=null;
        try{
            socket=new Socket("example.com",80);
            inputStreamReader=new InputStreamReader(socket.getInputStream());
            outputStreamWriter=new OutputStreamWriter(socket.getOutputStream());
            bufferedReader=new BufferedReader(inputStreamReader);
            bufferedWriter=new BufferedWriter(outputStreamWriter);
            Scanner scanner=new Scanner(System.in);
            while(true)
            {
                String msgToSend=scanner.nextLine();
                bufferedWriter.write(msgToSend.replaceAll("666","\n" ));//ASCII CODE-10
                bufferedWriter.newLine();
                bufferedWriter.flush();
                String outcome=bufferedReader.readLine();
                //We use while to read all the lines
                while(outcome!=null)
                {
                System.out.println("Server: "+outcome);
                outcome=bufferedReader.readLine();
                }
                if(msgToSend.equalsIgnoreCase("BYE"))
                {
                    break;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(socket!=null)
                    socket.close();
                if(inputStreamReader!=null)
                    inputStreamReader.close();
                if(outputStreamWriter!=null)
                    outputStreamWriter.close();
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
