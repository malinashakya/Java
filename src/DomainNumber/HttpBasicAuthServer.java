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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class HttpBasicAuthServer {

    public static final String USERNAME = "Admin";
    public static final String PASSWORD = "Admin";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int port = 5976;
        System.err.println("Server is running on port: " + port);
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected");
                HttpClientHandler clientHandler = new HttpClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class HttpClientHandler implements Runnable {

    private BufferedReader bufferedReader;
    private InputStreamReader inputStreamReader;
    private OutputStream outputStream;
    private Socket clientSocket;

    public HttpClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Override
        public void run() {
            try {
                String line;
                String host = null;
                String requestedFile = "index.html";
                boolean authenticated = false;

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    if (line.startsWith("Host:")) {
                        host = line.split(" ")[1];
                    }
                    if (line.startsWith("GET")) {
                        String[] parts = line.split(" ");
                        if (parts.length > 1) {
                            requestedFile = parts[1].substring(1);
                            if (requestedFile.isEmpty()) {
                                requestedFile = "index.html";
                            }
                        }
                    }
                    if (line.startsWith("Authorization: Basic ")) {
                        String base64Credentials = line.substring("Authorization: Basic ".length()).trim();
                        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                        String[] values = credentials.split(":", 2);
                        String username = values[0];
                        String password = values[1];
                        if (HttpBasicAuthServer.USERNAME.equals(username) && HttpBasicAuthServer.PASSWORD.equals(password)) {
                            authenticated = true;
                        }
                    }
                    if (line.isEmpty()) {
                        break;
                    }
                }

                if (!authenticated) {
                    sendUnauthorized();
                } else if (host != null) {
                    String folder = "";
                    // IP address, may get changed as per the change in the Ip address of laptop
                    if (host.startsWith("172.19.16.1")) {
                        folder = "172.19.16.1";
                    } else if (host.startsWith("127.0.0.1")) {
                        folder = "127.0.0.1";
                    }
                    serveFile(folder, requestedFile);
                } else {
                    serve404();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    clientSocket.close();
                    System.err.println("Client connection closed!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    private void serveFile(String folder, String fileName) {
        try (FileReader fileReader = new FileReader(folder + "/" + fileName)) {
            outputStream = clientSocket.getOutputStream();

            outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
            outputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            outputStream.write("\r\n".getBytes());

            int character;
            while ((character = fileReader.read()) != -1) {
                outputStream.write(character);
            }
            outputStream.write("\r\n".getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            serve404();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serve404() {
        try {
            outputStream = clientSocket.getOutputStream();

            String response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "\r\n" +
                    "<html><body><h1>404 Not Found</h1></body></html>";
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUnauthorized() {
        try {
            outputStream = clientSocket.getOutputStream();

            String response = "HTTP/1.1 401 Unauthorized\r\n" +
                    "WWW-Authenticate: Basic realm=\"User Visible Realm\"\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "\r\n" +
                    "<html><body><h1>401 Unauthorized</h1></body></html>";
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
