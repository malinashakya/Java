package DomainNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginServer {

    private static final int PORT = 5976;
    private static final String FOLDER_PATH = "LoginColor";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    private static final Map<String, String> sessions = new HashMap<>();

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
        private OutputStream os;
        private Map<String, String> cookies;

        public HttpClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.cookies = new HashMap<>();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String line;
                String requestedFile = "loginpage.html";
                boolean isPost = false;
                StringBuilder postData = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
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

                synchronized (sessions) {
                    if (isPost) {
                        handlePostRequest(postData.toString());
                    } else {
                        if (!isLoggedIn()) {
                            serveFile(FOLDER_PATH, "loginpage.html");
                        } else {
                            serveFile(FOLDER_PATH, requestedFile);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (os != null) {
                        os.close();
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

    if (authenticate(username, password)) {
        String sessionId = createSession(username, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String response = "HTTP/1.1 302 Found\r\n"
                + "Location: /page3.html\r\n"
                + "Set-Cookie: sessionId=" + sessionId + "; Path=/; HttpOnly; Secure\r\n"
                + "\r\n";
        os.write(response.getBytes());
        os.flush();
    } else {
        cookies.put("login", "false");
        serveFile(FOLDER_PATH, "loginpage.html");
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

        private boolean authenticate(String username, String password) {
            return USERNAME.equals(username) && PASSWORD.equals(password);
        }

        private String createSession(String username, String loginTime) {
            String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sessionIdBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                sessionIdBuilder.append(characters.charAt(random.nextInt(characters.length())));
            }
            String sessionId = sessionIdBuilder.toString();

            String sessionData = username + ":" + loginTime;
            sessions.put(sessionId, sessionData);

            return sessionId;
        }

        private boolean validateSession(String sessionId) {
            return sessions.containsKey(sessionId);
        }

        private boolean isLoggedIn() {
            String sessionId = cookies.get("sessionId");
            return sessionId != null && validateSession(sessionId);
        }

        private void sendAuthenticatedResponse(String sessionId, String loginTime) throws IOException {
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write(("Set-Cookie: sessionId=" + sessionId + "; Path=/; HttpOnly; Secure\r\n").getBytes());
            os.write(("Set-Cookie: loginTime=" + loginTime + "; Path=/; HttpOnly; Secure\r\n").getBytes());
            os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            os.write("\r\n".getBytes());
            serveFileContent("index.html");
            os.flush();
        }

        private void serveFile(String folder, String fileName) {
            try (FileReader fileReader = new FileReader(folder + "/" + fileName)) {
                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());

                int character;
                while ((character = fileReader.read()) != -1) {
                    os.write(character);
                }
                os.write("\r\n".getBytes());
                os.flush();
            } catch (FileNotFoundException e) {
                error();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void serveFileContent(String fileName) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    os.write(line.getBytes());
                    os.write("\r\n".getBytes());
                }
                os.flush();
            } catch (FileNotFoundException e) {
                error();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void error() {
            try {
                os.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                os.write("<html><body><h1>404 Not Found</h1></body></html>".getBytes());
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
