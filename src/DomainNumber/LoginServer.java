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
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginServer {

    private static final int PORT = 5976;
    private static final String FOLDER_PATH = "LoginColor";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

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
                String requestedFile = "loginpage.html";
                boolean isPost = false;
                StringBuilder postData = new StringBuilder();

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
                                requestedFile = "loginpage.html";
                            }
                        }
                    }
                    if (line.startsWith("POST")) {
                        isPost = true;
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
                    if (isPost && line.isEmpty()) {
                        char[] postDataChars = new char[1024];
                        int len = bufferedReader.read(postDataChars);
                        postData.append(new String(postDataChars, 0, len));
                        break;
                    }
                    if (line.isEmpty()) {
                        break;
                    }
                }

                if (isPost) {
                    handlePostRequest(postData.toString());
                } else {
                    if (!cookies.containsKey("login")) {
                        setLoginCookieFalse();
                    }
                    serveFile(FOLDER_PATH, requestedFile);
                }

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

        private void handlePostRequest(String postData) throws IOException {
            Map<String, String> params = parsePostData(postData);
            String username = params.get("username");
            String password = params.get("password");

            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                String sessionId = generateRandomString();
                String loginDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String encodedUsername = encodeBase64(USERNAME);
                String encodedLoginDateTime = encodeBase64(loginDateTime);

                cookies.put("sessionId", sessionId);
                cookies.put("username", encodedUsername);
                cookies.put("loginDateTime", encodedLoginDateTime);
                cookies.put("login", "true");

                String response = "HTTP/1.1 302 Found\r\n"
                        + "Location: /page3.html\r\n"
                        + "Set-Cookie: sessionId=" + sessionId + "\r\n"
                        + "Set-Cookie: username=" + encodedUsername + "\r\n"
                        + "Set-Cookie: loginDateTime=" + encodedLoginDateTime + "\r\n"
                        + "Set-Cookie: login=true\r\n"
                        + "\r\n";
                outputStream.write(response.getBytes());
                outputStream.flush();
            } else {
                cookies.put("login", "false");
                String response = "HTTP/1.1 302 Found\r\n"
                        + "Location: /loginpage.html\r\n"
                        + "Set-Cookie: login=false\r\n"
                        + "\r\n";
                outputStream.write(response.getBytes());
                outputStream.flush();
            }
        }

        private Map<String, String> parsePostData(String postData) {
            Map<String, String> params = new HashMap<>();
            String[] pairs = postData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
            return params;
        }

        private String generateRandomString() {
            int length = 16;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            return sb.toString();
        }

        private String encodeBase64(String input) {
            return Base64.getEncoder().encodeToString(input.getBytes());
        }

        private void setLoginCookieFalse() throws IOException {
            cookies.put("login", "false");
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Set-Cookie: login=false\r\n"
                    + "Content-Type: text/html; charset=UTF-8\r\n"
                    + "\r\n";
            outputStream.write(response.getBytes());
            outputStream.flush();
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
                        contentBuilder.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    serve404();
                    return;
                }

          
                String content = contentBuilder.toString();
                String response = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html; charset=UTF-8\r\n"
                        + "\r\n" 
                        + content;  

                // Send the response
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
