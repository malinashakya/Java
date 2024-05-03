/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import static javacodes.BlackJackMultiplayerGame.GameManager.getPlayer;
import static javacodes.BlackJackMultiplayerGame.GameManager.selectGameCondition;
import javacodes.BlackJackMultiplayerGame.gametype.GameType;

/**
 *
 * @author malin
 */
public class BlackJackMultiplayerGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameManager gamemanager = new GameManager();
        System.out.println("Do you want to resume the previous game? (y/n): ");
        char resumeResponse = scanner.next().charAt(0);
        if (resumeResponse == 'y') {
            GameDataReader gameDataReader = new GameDataReader();
            List<GameRecord> gameRecords = gameDataReader.readGameRecords("game_history.txt");
            GameType gametype = selectGameCondition(scanner);
            //key:String type, value: Player type
            HashMap<String, Player> players = new HashMap<>();
            // Now, you should have the gameRecords and the game type selected
            for (GameRecord record : gameRecords) {
                players.put(record.name, new Player(record.name, record.balance));
            }
            playGame(new ArrayList<>(players.values()), gameRecords, gametype);

        } else if (resumeResponse == 'n') {
            System.out.println("Starting a new game.");
            System.out.println("Welcome to the game of Blackjack!");
            GameType gametype = selectGameCondition(scanner);

            //Changed to List
            List<GameRecord> records = new ArrayList<>();
            List<Player> players = getPlayer(scanner);
            System.out.println("\nYou each have 100 points in your accounts.");

            playGame((ArrayList<Player>) players, records, gametype);
        }
    }

    private static void playGame(ArrayList<Player> arrayList, List<GameRecord> gameRecords, GameType gametype) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
