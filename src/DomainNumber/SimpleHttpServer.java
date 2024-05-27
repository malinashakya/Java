/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DomainNumber;

/**
 *
 * @author malin
 */
import java.io.*;
import java.net.*;

 class RealtimeHttpServer {
    public static void main(String[] args) throws IOException {
        int port = 1234;
        ServerSocket serverSocket = new ServerSocket(port);
        InetAddress localhost = InetAddress.getByName("127.0.0.1");
        System.err.println("Server is running on port: " + port);
        try {
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
                        clientOutput.write("Content-Type: text/html\r\n\r\n".getBytes());
                        String html = "<html><head><title>Realtime Server</title></head><body>";
                        html += "<h1>Realtime Updates</h1>";
                        html += "<div id='content'></div>";
                        html += "<script>let contentDiv = document.getElementById('content');";
                        html += "let ws = new WebSocket('ws://localhost:8080');";
                        html += "ws.onmessage = function(event) { contentDiv.innerHTML = event.data; };</script>";
                        html += "</body></html>";
                        clientOutput.write(html.getBytes());
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
