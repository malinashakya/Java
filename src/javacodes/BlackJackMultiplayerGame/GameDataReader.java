/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame;

/**
 *
 * @author malin
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameDataReader {

    public static String reverseDisplay(String card) {
        switch (card) {
            case "J":
                return "11";
            case "Q":
                return "12";
            case "K":
                return "13";
            case "A":
                return "1";
            default:
                if (card.length() == 2 && card.charAt(0) == '1' && card.charAt(1) == '0') {
                    return "10";
                } else {
                    return String.valueOf(card.charAt(0) - '0');
                }
        }
    }

    public static List<GameRecord> readGameRecords(String filename) {
        List<GameRecord> gameRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name,Round,Card1,Card2,Card3,Bet,Result,Balance")) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    GameRecord record = new GameRecord();
                    String card1, card2, card3;
                    record.name = parts[0];
                    // Add the name to the set
                    record.round = Integer.parseInt(parts[1]);
                    card1 = reverseDisplay(parts[2]);
                    card2 = reverseDisplay(parts[3]);
                    card3 = reverseDisplay(parts[4]);
                    record.card1 = new Card(card1);
                    record.card2 = new Card(card2);
                    record.card3 = new Card(card3);
                    record.betPoint = Integer.parseInt(parts[5]);
                    record.result = Integer.parseInt(parts[6]);
                    record.balance = Integer.parseInt(parts[7]);
                    gameRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading game history: " + e.getMessage());
        }

        // Display information for each game record
        System.out.println("Game Records:");
        for (GameRecord record : gameRecords) {
            System.out.println("Name: " + record.name);
            System.out.println("Round: " + record.round);
            System.out.println("Card1: " + record.card1);
            System.out.println("Card2: " + record.card2);
            System.out.println("Card3: " + record.card3);
            System.out.println("Bet Point: " + record.betPoint);
            System.out.println("Result: " + record.result);
            System.out.println("Balance: " + record.balance);
            System.out.println();
        }

        return gameRecords;
    }

    public static List<Player> createPlayers(List<GameRecord> gameRecords) {
        List<Player> players = new ArrayList<>();
        for (GameRecord record : gameRecords) {
            Player player = new Player(record.name, record.balance);
            players.add(player);
        }
        return players;
    }
}
