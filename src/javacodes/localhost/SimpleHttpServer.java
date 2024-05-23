/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.localhost;

import java.io.*;
import java.net.*;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        int port = 8081;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server is running on port: " + port);
        try (FileReader reader = new FileReader("abc.html")) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.err.println("Client connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String s;
                    while ((s = in.readLine()) != null) {
                        System.out.println(s);
                        if (s.isEmpty()) {
                            break;
                        }
                    }
                    OutputStream clientOutput = clientSocket.getOutputStream();
                    try {
                        clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                        clientOutput.write("\r\n".getBytes());
                        int character;
                        while ((character = reader.read()) != -1) {
                            clientOutput.write(character);
                        }
                        clientOutput.write("\r\n\r\n".getBytes());
                        clientOutput.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        System.err.println("Client connection closed!");
                        in.close();
                        clientOutput.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//Reference: 
//1. https://www.youtube.com/watch?v=lCNUsi4Qfuw
//2. How to read html file in java (Chatgpt)
