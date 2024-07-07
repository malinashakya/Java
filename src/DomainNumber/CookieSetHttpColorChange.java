package DomainNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CookieSetHttpColorChange {

    private static final int PORT = 5976;
    private static final String FOLDER_PATH = "LoginColor";

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
                String requestedFile = "colorselectionpage.html";
                boolean setColorCookie = false;
                String newColor = null;

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
                                            newColor = keyValue[1];
                                            setColorCookie = true;
                                        }
                                    }
                                }
                            }
                            if (requestedFile.isEmpty()) {
                                requestedFile = "colorselectionpage.html";
                            }
                        }
                    }
                    if (line.startsWith("Cookie:")) {
                        String[] cookiePairs = line.substring(8).split("; ");
                        for (String cookie : cookiePairs) {
                            String[] keyValue = cookie.split("=");
                            if (keyValue.length == 2) {
                                cookies.put(keyValue[0], keyValue[1]);
                            }
                        }
                    }
                    if (line.isEmpty()) {
                        break;
                    }
                }

                serveFile(FOLDER_PATH, requestedFile, newColor, setColorCookie);

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

        private void serveFile(String folder, String fileName, String newColor, boolean setColorCookie) {
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

                // Retrieve the latest color value (newColor takes precedence over cookie)
                String color = (newColor != null) ? newColor : cookies.getOrDefault("color", "white");

                // Update the placeholder with the actual color
                String modifiedContent = content.replace("{{color}}", color);

                // Send response with Set-Cookie header for color if needed
                String response = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html; charset=UTF-8\r\n";
                if (setColorCookie) {
                    response += "Set-Cookie: color=" + color + "; Path=/\r\n";
                }
                response += "\r\n" + modifiedContent + "\r\n";
                outputStream.write(response.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void serve404() {
            try {
                String response = "HTTP/1.1 404 Not Found\r\n"
                        + "Content-Type: text/html; charset=UTF-8\r\n"
                        + "\r\n"
                        + "<html><body><h1>404 Not Found</h1></body></html>";
                outputStream.write(response.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
