package DomainNumber;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CookieSetHttpColorChange {

    private static final int PORT = 5976;
    private static final String FOLDER_PATH = "LoginColor"; // Folder containing the HTML files

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"));
            System.out.println("Server is running on port: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                HttpClientHandler clientHandler = new HttpClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    static class HttpClientHandler implements Runnable {

        private final Socket clientSocket;
        private BufferedReader bufferedReader;
        private OutputStream outputStream;
        private Map<String, String> cookies;

        public HttpClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.cookies = new HashMap<>();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outputStream = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String line;
                String host = null;
                String requestedFile = "index.html";

                // Read HTTP request headers
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
                                        String[] keyValue = param.split("=");
                                        if (keyValue.length == 2) {
                                            cookies.put(keyValue[0], keyValue[1]);
                                        }
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

                serveFile(FOLDER_PATH, requestedFile);

            } catch (IOException e) {
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
                    System.out.println("Client connection closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void serveFile(String folder, String fileName) {
            try {
                File file = new File(folder + "/" + fileName);
                if (!file.exists()) {
                    serve404();
                    return;
                }

                StringBuilder contentBuilder = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        contentBuilder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    serve404();
                    return;
                }

                
                String content = contentBuilder.toString();

                if (fileName.equals("page1.html") || fileName.equals("page2.html")) {
                
                    String color = cookies.getOrDefault("color", "white");
                    String modifiedContent = content.replace("{{color}}", color);

                    
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.write(modifiedContent.getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.flush();
                } else {
                   
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.write(content.getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void serve404() {
            try {
                String response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "\r\n" +
                        "<html><body><h1>404 Not Found</h1></body></html>";
                outputStream.write(response.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
