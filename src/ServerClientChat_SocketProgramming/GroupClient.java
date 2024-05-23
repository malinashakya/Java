/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClientChat_SocketProgramming;

/**
 *
 * @author malin
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class GroupClient {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            socket = new Socket("localhost", 987);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            // Thread for reading messages from the server
            new Thread(new ReadMessage(bufferedReader)).start();

            while (true) {
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(name + ": " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                if (msgToSend.equalsIgnoreCase("BYE")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class ReadMessage implements Runnable {
        private BufferedReader bufferedReader;

        public ReadMessage(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {
            String msg;
            try {
                while ((msg = bufferedReader.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
