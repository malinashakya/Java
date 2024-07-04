package DomainNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CookieSetHttpColorChange {

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
            String color = "white"; 

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("Host:")) {
                    host = line.split(" ")[1];
                }
                if (line.startsWith("GET")) {
                    String[] parts = line.split(" ");
                    if (parts.length > 1) {
                        requestedFile = parts[1].substring(1);
                        if (requestedFile.contains("?")) {
                            String[] fileAndParams = requestedFile.split("\\?");
                            requestedFile = fileAndParams[0];
                            String[] params = fileAndParams[1].split("&");
                            for (String param : params) {
                                if (param.startsWith("color=")) {
                                    color = param.split("=")[1];
                                }
                            }
                        }
                        if (requestedFile.isEmpty()) {
                            requestedFile = "index.html";
                        }
                    }
                }
                if (line.isEmpty()) {
                    break;
                }
            }

            if (host != null) {
                if (requestedFile.equals("page1.html")) {
                    serveColorPage(color);
                } else {
                    String folder = "LoginColor"; 
                    serveFile(folder, requestedFile);
                }
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

    private void serveColorPage(String color) {
        try {
            outputStream = clientSocket.getOutputStream();

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "\r\n" +
                    "<html><body style='background-color:" + color + ";'><h1>Page 1 with color " + color + "</h1></body></html>";
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
