/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes.BlackJackMultiplayerGame;

/**
 *
 * @author malin
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javacodes.BlackJackMultiplayerGame.Card;
import javacodes.BlackJackMultiplayerGame.gametype.GameType;
import javacodes.BlackJackMultiplayerGame.gametype.GameTypeAJQKare10;
import javacodes.BlackJackMultiplayerGame.gametype.GameTypeJQKare10;
import javacodes.BlackJackMultiplayerGame.gametype.GameTypeSumIsLessThan17;
import javacodes.BlackJackMultiplayerGame.gametype.GameTypeSumIsLessThan21;

public class GameManager {
 
    public static Random random = new Random();
    private Scanner scanner;

    public static int playTurn(int betPoint, Card card1, Card card2, Player player, List<GameRecord> records, int round,
            GameType gametype) {
        int sum;
        Card  card3;
        card3 = new Card (random.nextInt(13) + 1);
        sum = gametype.calculate(card1, card2, card3);
        System.out.println(round);
        records.add(new GameRecord());
        records.get(round).card1 = card1;
        records.get(round).card2 = card2;
        records.get(round).card3 = card3;
        records.get(round).betPoint = betPoint;

        System.out.println("Your third card is :" + card3);
        System.out.println("Your sum is:" + sum);
        if (gametype.evaluate(card1, card2, card3) == 0) {
            player.balance -= betPoint;
            records.get(round).result = -betPoint;
            System.out.println("You lose " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        } else {
            player.balance += betPoint;
            records.get(round).result = betPoint;
            System.out.println("You win " + betPoint + " points");
            System.out.println("You remaining points " + player.balance);
        }
        records.get(round).balance = player.balance;
        return player.balance;
    }

    static void displayHistory(List<GameRecord> records, List<Player> players) {
        try (Writer writer = new FileWriter("game_history.txt")) {
            writer.write("Name,Round,Card1,Card2,Card3,Bet,Result,Balance\n");
            System.out.println("Name,Round,Card1,Card2,Card3,Bet,Result,Balance");
            int numPlayers = players.size();
            int rounds = records.size();
            int round = 0;
            for (int turns = 0; turns < rounds; turns++) {
                if (turns % numPlayers == 0) {
                    round++;
                }
                int playerIndex = turns % numPlayers;
                Player currentPlayer = players.get(playerIndex);

                String message = currentPlayer.name + "," + round + ","
                        + records.get(turns).card1 + ","
                        + records.get(turns).card2 + ","
                        + records.get(turns).card3 + ","
                        + records.get(turns).betPoint + ","
                        + records.get(turns).result + ","
                        + records.get(turns).balance;

                System.out.println(message);
                writer.write(message + "\n");
            }

            System.out.println("Game history has been saved to game_history.txt");
        } catch (IOException e) {
            System.err.println("Error writing game history to file: " + e.getMessage());
        }
    }

    static void playGame(List<Player> players, List<GameRecord> records, GameType gametype) {
        Scanner scanner = new Scanner(System.in);
        int rounds = records.size();
        while (true) {
            int betPoint;
            Card card1, card2;
            int numPlayers = players.size();
            for (int player = 0; player < numPlayers; player++) {
                if (players.get(player).balance <= 0) {
                    System.out.println("Player " + players.get(player).name + " is out of the game.");
                    continue;
                }

                card1 = new Card(random.nextInt(13) + 1);
                card2 =new Card (random.nextInt(13) + 1);

                System.out.println("\nPlayer " + players.get(player).name + ", your cards are " + card1
                        + " and " + card2);
                System.out.println("How many points do you want to bet?");

                betPoint = scanner.nextInt();
                if (betPoint > 0 && betPoint <= players.get(player).balance) {
                    playTurn(betPoint, card1, card2, players.get(player), records, rounds, gametype);
                    rounds++;
                } else if (betPoint <= 0) {
                    System.out.println("Bet point cannot be negative or zero");
                } else {
                    System.out.println("Player " + players.get(player).name + ", you don't have enough points");
                }
            }

            System.out.print("\nDo you want to continue to the next round? (y/n): ");
            char response = scanner.next().charAt(0);
            if (response == 'n') {
                break; // Exit the game if the user does not want to continue
            } else if (response != 'y') {
                System.out.println("Wrong input");
            }

        }
        System.out.println("Rounds:"
                + rounds);
        displayHistory(records, players);

        System.out.println("Game over!!!");
    }

    static List<Player> getPlayer(Scanner scanner) {
        int numPlayers;
        System.out.print("Enter the number of players (1-17): ");
        numPlayers = scanner.nextInt();

        if (numPlayers < 1 || numPlayers > 17) {
            System.out.println("Invalid number of players. Exiting...");
            return null; // Return null indicating failure
        }

        int startingPoints = 100;
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name of Player " + (i + 1) + ": ");
            String playerName = scanner.next();
            players.add(new Player(playerName, startingPoints));
        }

        System.out.println("Names of players:");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + ": " + players.get(i).name);
        }

        return players; 
    }

    static GameType selectGameCondition(Scanner scanner) {
        System.out.println("Select the game condition:");
        System.out.println("1. Win if sum is less than 21");
        System.out.println("2. Win if sum is less than 17");
        System.out.println("3. Win if J, K, Q, A and sum is less than 21");
        System.out.println("4. Win if J, K, Q are equal to 10, A is equal to 1 and sum is less than 21");

        int condition = scanner.nextInt();
        GameType gametype;

        switch (condition) {
            case 1:
                gametype = new GameTypeSumIsLessThan21();
                break;
            case 2:
                gametype = new GameTypeSumIsLessThan17();
                break;
            case 3:
                gametype = new GameTypeAJQKare10();
                break;
            case 4:
                gametype = new GameTypeJQKare10();
                break;
            default:
                System.out.println("Wrong selection of game");
                throw new IllegalArgumentException("Invalid game selection");
        }

        return gametype;
    }
}
