package GroupChat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started and listening on port 12345");

            new Thread(new ServerMessageSender()).start(); 

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private static class ServerMessageSender implements Runnable {
        @Override
        public void run() {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
            String serverMessage;
            try {
                while ((serverMessage = serverInput.readLine()) != null) {
                    broadcastMessage("Server: " + serverMessage);
                    if (serverMessage.equalsIgnoreCase("BYE")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println( message);
                    broadcastMessage(message);
                    if (message.equalsIgnoreCase("BYE")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) clientSocket.close();
                    if (bufferedReader != null) bufferedReader.close();
                    if (bufferedWriter != null) bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            try {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//References: Chatgpt(How can we make multi-client chat with each other) , https://www.baeldung.com/a-guide-to-java-sockets, https://www.javatpoint.com/socket-programming