package DomainNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadLocalHostServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int port = 1234;
        System.err.println("Server is running on port: " + port);
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ClientHandler implements Runnable {

    private BufferedReader bufferedreader;
    private InputStreamReader inputStreamReader;
    private OutputStream outputStream;
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            bufferedreader = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) {
                    break;
                }
            }
            try (FileReader fileReader = new FileReader("abc.html")) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedreader != null) {
                    bufferedreader.close();
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
}

//Reference: https://github.com/lijala-shakya/java-works/tree/main/LocalHost/src/localhost