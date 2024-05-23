import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author malin
 */
public class IpAddress {
    public static void main(String[] args) throws IOException {
             int port = 80;
        ServerSocket serverSocket = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
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
//Reference
//1. https://www.youtube.com/watch?v=lCNUsi4Qfuw
//2. how to open file in Ip address using java in browser( Chatgpt)
        